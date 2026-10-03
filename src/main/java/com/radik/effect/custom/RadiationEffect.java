package com.radik.effect.custom;

import com.radik.DamageTypes;
import com.radik.ModTags;
import com.radik.Radik;
import com.radik.effect.RegisterEffect;
import com.radik.property.base.RadiationProperty;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.radik.configs.base.BaseConfigs.RADIATION_CONFIG;
import static com.radik.property.base.BaseProperties.RADIATION_PROPERTY;

public class RadiationEffect extends StatusEffect {
    public RadiationEffect() {
        super(StatusEffectCategory.HARMFUL, 0x88FF88);
        this.addAttributeModifier(EntityAttributes.MOVEMENT_SPEED,
            Identifier.of(Radik.MOD_ID, "radiation_speed"),
            -0.15, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(EntityAttributes.ATTACK_DAMAGE,
            Identifier.of(Radik.MOD_ID, "radiation_attack"),
            -0.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, @NotNull LivingEntity entity, int amplifier) {
        if (entity.isInCreativeMode() || entity.isSpectator()) return false;
        StatusEffectInstance instance = entity.getStatusEffect(RegisterEffect.RADIATION_EFFECT);
        if (instance != null && instance.getDuration() <= 20)
            return entity.damage(world, entity.getDamageSources().create(DamageTypes.RADIATION), Float.MAX_VALUE);

        if (world.getTime() % 20 != 0) return true;

        int level = amplifier + 1;
        if (level == 2) {
            if (world.random.nextInt(10) == 0) addHiddenEffect(entity, StatusEffects.DARKNESS, 40, 0);
        } else if (level == 3) {
            if (world.random.nextInt(2) == 0) addHiddenEffect(entity, StatusEffects.DARKNESS, 80, 0);
        } else if (level == 4) addHiddenEffect(entity, StatusEffects.DARKNESS, 40, 0);
        else if (level >= 5) {
            addHiddenEffect(entity, StatusEffects.BLINDNESS, 9999, 0);
            addHiddenEffect(entity, StatusEffects.NAUSEA, 9999, 0);
        }

        if (world.getTime() % 40 != 0) return true;

        if (amplifier >= 1) {
            entity.damage(world, entity.getDamageSources().create(DamageTypes.RADIATION), (float) (amplifier / 2));
            if (entity instanceof PlayerEntity player) player.getHungerManager().addExhaustion(0.1f * (amplifier + 1));
        }

        return true;
    }

    private void addHiddenEffect(@NotNull LivingEntity entity, RegistryEntry<StatusEffect> effect, int durationTicks, int amplifier) {
        StatusEffectInstance existing = entity.getStatusEffect(effect);
        if (existing == null || existing.getDuration() < durationTicks) {
            StatusEffectInstance newEffect = new StatusEffectInstance(effect, durationTicks, amplifier, false, false, false);
            entity.addStatusEffect(newEffect);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public static void applyRadiation(@NotNull LivingEntity entity, World world, BlockPos pos, int chance) {
        if (entity.isInCreativeMode() || entity.isSpectator()) return;
        StatusEffectInstance cur = entity.getStatusEffect(RegisterEffect.RADIATION_EFFECT);
        int c = 0;
        if (entity instanceof LeadArmorAccessor accessor) {
            c = accessor.getRadik$LeadArmorCount();
        } else {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (entity.getEquippedStack(slot).isIn(ModTags.Items.LEAD_ARMOR)) c++;
            }
        }
        int minutes = c * RADIATION_PROPERTY.getInt(RadiationProperty.LEAD_PART_TIMER);

        if (cur == null) {
            entity.addStatusEffect(new StatusEffectInstance(RegisterEffect.RADIATION_EFFECT, 20 * 60 * (30 + minutes), 0));
            sendRadiationTitle(entity, 0);
        } else {
            boolean upgraded = false;
            int fchance = chance;
            fchance += chance * c * RADIATION_PROPERTY.getInt(RadiationProperty.LEAD_PART_BUFF) / 100;
            if (c == 4) fchance += chance * RADIATION_PROPERTY.getInt(RadiationProperty.FULL_LEAD_SET_BUFF) / 100;

            if (world.random.nextInt(fchance) == 0) {
                int newAmplifier = cur.getAmplifier() + 1;
                entity.addStatusEffect(new StatusEffectInstance(RegisterEffect.RADIATION_EFFECT, cur.getDuration() / 2, newAmplifier));
                sendRadiationTitle(entity, newAmplifier);
                world.playSound(null, pos, SoundEvents.BLOCK_CHERRY_WOOD_BREAK, SoundCategory.BLOCKS, 1.0f, 0.5f);
                upgraded = true;
            }
            if (!upgraded && world.random.nextInt(1000) == 0) {
                if (cur.getAmplifier() < 4) sendDefaultRadiationTitle(entity);
                else sendDefaultDeadRadiationTitle(entity);
            }
        }
    }

    private static void sendDefaultDeadRadiationTitle(LivingEntity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) return;
        String message = RADIATION_CONFIG.getRandomDefault1Message();
        player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(message)));
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 60, 20));
    }

    private static void sendDefaultRadiationTitle(LivingEntity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) return;
        String message = RADIATION_CONFIG.getRandomDefaultMessage();
        player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(message)));
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 60, 20));
    }

    private static void sendRadiationTitle(LivingEntity entity, int amplifier) {
        if (!(entity instanceof ServerPlayerEntity player)) return;
        String message = RADIATION_CONFIG.getRandomMessage(amplifier);
        player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal(message)));
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 60, 20));
    }

    public interface LeadArmorAccessor {
        int getRadik$LeadArmorCount();
    }
}

