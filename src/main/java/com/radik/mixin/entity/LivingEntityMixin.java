package com.radik.mixin.entity;

import com.radik.ModTags;
import com.radik.effect.custom.RadiationEffect;
import com.radik.property.base.RadiationProperty;
import com.radik.world.biome.RegisterBiomes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.radik.property.base.BaseProperties.RADIATION_PROPERTY;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private int radiationTick = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        World world = entity.getEntityWorld();

        if (world.isClient() || entity.isRemoved() || world.getTime() % 20 != 0 || entity.isInCreativeMode()) return;
        BlockPos pos = entity.getBlockPos();
        var h = world.getBiome(pos);
        boolean ginger = h.matchesKey(RegisterBiomes.GINGER_FOREST);
        if (!ginger) return;

        if (world.hasRain(pos) || world.getBlockState(pos).getFluidState().isIn(FluidTags.WATER)) poisoning(entity);

        int antiRadCount = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getEquippedStack(slot);
            if (!stack.isEmpty() && stack.isDamageable()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    if (stack.isIn(ModTags.Items.LEAD_ARMOR)) antiRadCount++;
                }
                if (stack.isIn(ModTags.Items.CORRODIBLE)) stack.damage(5, entity, slot);
            }
        }

        int delay = 200 * (1 << antiRadCount);
        radiationTick += 20;

        if (radiationTick >= delay) {
            radiationTick = 0;
            RadiationEffect.applyRadiation(entity, world, pos, 5);
        }
    }

    @Unique
    private static void poisoning(@NotNull LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40, 1));
    }
}
