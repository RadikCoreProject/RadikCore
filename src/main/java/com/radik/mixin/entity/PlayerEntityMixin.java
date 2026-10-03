package com.radik.mixin.entity;

import com.radik.DamageTypes;
import com.radik.ModTags;
import com.radik.Radik;
import com.radik.effect.RegisterEffect;
import com.radik.effect.custom.RadiationEffect;
import com.radik.item.RegisterItems;
import com.radik.property.base.EventProperty;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.radik.Data.DOZA;
import static com.radik.property.base.BaseProperties.EVENT_PROPERTY;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements RadiationEffect.LeadArmorAccessor {
    @Unique
    private int lastLeadArmorCount = 0;

    @Override
    public int getRadik$LeadArmorCount() {
        return lastLeadArmorCount;
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(@NotNull Entity target, CallbackInfo ci) {
        if (target.getEntityWorld().isClient()) return;
        ServerWorld world = (ServerWorld) target.getEntityWorld();
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack mainHand = player.getMainHandStack();
        Item item = mainHand.getItem();

        if (item == RegisterItems.FLOWERY_SWORD) {
            float damage = computeDamage(player, target, world);
            float newHealth;
            if (damage > 0) {
                if (target instanceof PassiveEntity entity) {
                    newHealth = entity.getHealth() + damage;
                    entity.setHealth(Math.min(entity.getMaxHealth(), newHealth));
                    Radik.sendEventToPlayers(2, target.getBlockPos(), 0, world);
                    mainHand.damage(4, player);
                    ci.cancel();
                } else if (target instanceof LivingEntity entity && Radik.RANDOM.nextInt(100) <= EVENT_PROPERTY.getInt(EventProperty.FLOWERY_SWORD_ULTA_CHANCE)) {
                    newHealth = entity.getHealth() - damage;
                    entity.setHealth(Math.min(entity.getMaxHealth(), newHealth));
                    Radik.sendEventToPlayers(2, target.getBlockPos(), 0, world);
                    mainHand.damage(2, player);
                }
            }
        } else if (item == RegisterItems.FLOWERY_AXE) {
            if (Radik.RANDOM.nextInt(100) <= EVENT_PROPERTY.getInt(EventProperty.FLOWERY_SWORD_ULTA_CHANCE)) {
                float damage = computeDamage(player, target, world) / 2;
                if (damage > 0) {
                    float newHealth = player.getHealth() + damage;
                    player.setHealth(Math.min(player.getMaxHealth(), newHealth));
                    Radik.sendEventToPlayers(2, target.getBlockPos(), 0, world);
                    mainHand.damage(5, player);
                }
            }
        }
    }

    @Unique
    private float computeDamage(@NotNull PlayerEntity player, Entity target, ServerWorld world) {
        float baseDamage = (float) player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
        float enchantmentDamage = EnchantmentHelper.getDamage(world, player.getMainHandStack(), target,
            player.getDamageSources().playerAttack(player), baseDamage);

        float progress = player.getAttackCooldownProgress(0.5F);
        float strengthModifier = 0.2F + progress * progress * 0.8F;
        float finalDamage = (baseDamage + enchantmentDamage) * strengthModifier;
        if (progress > 0.9F && isCriticalHit(player)) finalDamage *= 1.5F;
        return finalDamage;
    }

    @Unique
    private boolean isCriticalHit(@NotNull PlayerEntity player) {
        return player.getAttackCooldownProgress(0.5F) > 0.9F
            && player.fallDistance > 0.0
            && !player.isOnGround()
            && !player.isClimbing()
            && !player.isTouchingWater()
            && !player.hasBlindnessEffect()
            && !player.hasVehicle()
            && !player.isSprinting();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getEntityWorld();
        if (world.isClient() || player.isRemoved() || world.getTime() % 100 != 0) return;
        ServerWorld serverWorld = (ServerWorld) world;

        int cur = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                if (player.getEquippedStack(slot).isIn(ModTags.Items.LEAD_ARMOR)) cur++;
            }
        }

        if (cur < lastLeadArmorCount) {
            StatusEffectInstance mainRad = player.getStatusEffect(RegisterEffect.RADIATION_EFFECT);
            if (mainRad != null) {
                int amp = mainRad.getAmplifier();
                int newDuration = mainRad.getDuration() - (lastLeadArmorCount - cur) * 10 * 60 * 20 / (1 << amp);
                if (newDuration <= 100) player.damage(serverWorld, player.getDamageSources().create(DamageTypes.RADIATION), Float.MAX_VALUE);

                player.setStatusEffect(new StatusEffectInstance(
                    RegisterEffect.RADIATION_EFFECT,
                    Math.max(newDuration, 100),
                    amp
                ), player);
            }
        }
        lastLeadArmorCount = cur;

        long t = 0;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            t += calculateRadiationRecursive(stack);
        }

        if (t > 0) {
            long passedChance = 103900L / t;
            int finalChance = (int) Math.max(1, passedChance);
            RadiationEffect.applyRadiation(player, world, player.getBlockPos(), finalChance);
        }
    }

    @Unique
    private long calculateRadiationRecursive(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return 0;
        long total = 0;

        Integer radValue = stack.get(DOZA);
        if (radValue != null && radValue > 0 && !stack.isOf(RegisterItems.TOMMY) && !stack.isOf(RegisterItems.MAGAZINE)) total += (long) radValue * stack.getCount();

        BundleContentsComponent bundle = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundle != null) {
            for (ItemStack innerStack : bundle.iterate()) total += calculateRadiationRecursive(innerStack);
        }

        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container != null) {
            for (ItemStack innerStack : container.iterateNonEmpty()) total += calculateRadiationRecursive(innerStack);
        }

        return total;
    }
}