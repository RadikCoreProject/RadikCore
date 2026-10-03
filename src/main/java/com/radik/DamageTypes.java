package com.radik;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DamageTypes {
    public static final RegistryKey<DamageType> RADIATION = register("radiation");

    private static RegistryKey<DamageType> register(String name) {
        return RegistryKey.of(
            RegistryKeys.DAMAGE_TYPE,
            Identifier.of(Radik.MOD_ID, name)
        );
    }

    @MainInit
    public static void initialize() {}
}
