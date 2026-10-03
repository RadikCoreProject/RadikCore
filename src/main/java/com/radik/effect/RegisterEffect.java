package com.radik.effect;

import com.radik.MainInit;
import com.radik.Radik;
import com.radik.effect.custom.CumEffect;
import com.radik.effect.custom.RadiationEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class RegisterEffect {
    public static final RegistryEntry<StatusEffect> CUM_EFFECT = register(new CumEffect(), "cum");
    public static final RegistryEntry<StatusEffect> RADIATION_EFFECT = register(new RadiationEffect(), "radiation");

    private static RegistryEntry<StatusEffect> register(StatusEffect effect, String id) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Radik.MOD_ID, id), effect);
    }

    @MainInit
    public static void initialize() {}
}
