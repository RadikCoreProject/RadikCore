package com.radik.world.biome;

import com.radik.MainInit;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import terrablender.api.SurfaceRuleManager;
import terrablender.worldgen.TBSurfaceRuleData;

public class RegisterBiomes {
    public static final RegistryKey<Biome> GINGER_FOREST = register("ginger_forest");

    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of("radik", name)
        );
    }

    @MainInit
    public static void initialize() {}
}
