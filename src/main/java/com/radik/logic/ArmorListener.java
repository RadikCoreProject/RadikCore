package com.radik.logic;

import com.radik.ModTags;
import com.radik.effect.RegisterEffect;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArmorListener {
    static void register() {
        ServerEntityEvents.EQUIPMENT_CHANGE.register(ArmorListener::equipmentChange);
    }

    private static void equipmentChange(LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot, ItemStack stack, ItemStack stack1) {
        if (equipmentSlot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) return;
        if (!(livingEntity instanceof PlayerEntity player)) return;

        boolean wasLead = isLeadArmor(stack);
        boolean isLead = isLeadArmor(stack1);

        if (wasLead && !isLead) {
            reduceRadiationDuration(player);
        }
    }

    private static boolean isLeadArmor(ItemStack stack) {
        return !stack.isEmpty() && stack.isIn(ModTags.Items.LEAD_ARMOR);
    }

    private static void reduceRadiationDuration(PlayerEntity player) {
        StatusEffectInstance effect = player.getStatusEffect(RegisterEffect.RADIATION_EFFECT);
        if (effect == null) return;

        int amplifier = effect.getAmplifier();
        int reductionTicks = (int) (10 * 60 * 20 / Math.pow(2, amplifier));
        if (reductionTicks < 1) reductionTicks = 1;

        int newDuration = effect.getDuration() - reductionTicks;
        if (newDuration <= 0) {
            player.removeStatusEffect(RegisterEffect.RADIATION_EFFECT);
        } else {
            player.addStatusEffect(new StatusEffectInstance(
                RegisterEffect.RADIATION_EFFECT,
                newDuration,
                amplifier,
                false, false, true
            ));
        }
    }
}