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

        if (!entity.getEntityWorld().isClient()) {
            CumEffectServerBridge.getHandler().onApplied(entity, amplifier);
        }
    }

    public interface CumEffectServerHandler {
        void onApplied(LivingEntity entity, int amplifier);
    }

    static class NoopCumEffectServerHandler implements CumEffectServerHandler {
        @Override
        public void onApplied(LivingEntity entity, int amplifier) {}
    }

    public static class CumEffectServerBridge {
        private static CumEffectServerHandler handler = new NoopCumEffectServerHandler();

        public static void setHandler(CumEffectServerHandler newHandler) {
            handler = newHandler;
        }

        public static CumEffectServerHandler getHandler() {
            return handler;
        }
    }
}
