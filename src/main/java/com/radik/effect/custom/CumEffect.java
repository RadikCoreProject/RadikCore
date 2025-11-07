package com.radik.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CumEffect extends StatusEffect {
    public CumEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFFFFF);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
    }
}
