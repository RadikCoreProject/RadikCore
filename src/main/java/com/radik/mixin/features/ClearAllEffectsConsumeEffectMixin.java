package com.radik.mixin.features;

import com.radik.Radik;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ClearAllEffectsConsumeEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClearAllEffectsConsumeEffect.class)
public class ClearAllEffectsConsumeEffectMixin {
    @Inject(method = "onConsume", at = @At("HEAD"), cancellable = true)
    private void onConsume(World world, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Boolean> cir) {
        RegistryKey<StatusEffect> radiationKey = RegistryKey.of(RegistryKeys.STATUS_EFFECT, Identifier.of(Radik.MOD_ID, "radiation"));

        List<RegistryEntry<StatusEffect>> toRemove = new ArrayList<>();
        boolean hasRadiation = false;
        int ampl = 0;

        for (StatusEffectInstance effect : user.getStatusEffects()) {
            RegistryEntry<StatusEffect> entry = effect.getEffectType();
            RegistryKey<StatusEffect> key = entry.getKey().orElse(null);
            if (radiationKey.equals(key)) {
                hasRadiation = true;
                ampl = effect.getAmplifier();
            } else {
                toRemove.add(entry);
            }
        }

        toRemove.forEach(user::removeStatusEffect);

        if (hasRadiation) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 30 * 20, ampl));
            if (user instanceof PlayerEntity player) {
                player.sendMessage(Text.of("Кажется, молоко тут не поможет..."), true);
            }
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}