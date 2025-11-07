package com.radik.effect;

import com.radik.Radik;
import com.radik.effect.custom.CumEffect;
import com.radik.registration.IRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterEffect implements IRegistry {
    public static final StatusEffect CUM_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(Radik.MOD_ID, "cum"), new CumEffect());

    public static void initialize() {}
}
