package com.radik.world;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.fluid.RegisterFluids;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import com.radik.world.features.GasLakeFeature;
import com.radik.world.features.GasLakeFeatureConfig;

import static com.radik.Radik.MOD_ID;

// test
// TODO: удалить говнокод
public class RegisterConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> HYDROGEN_LAKE_KEY = registerConfiguredKey("hydrogen_lake");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        GasLakeFeature gasLakeFeature = new GasLakeFeature(GasLakeFeatureConfig.CODEC);

        register(context, HYDROGEN_LAKE_KEY, gasLakeFeature, new GasLakeFeatureConfig(
                15,
                RegisterFluids.HYDROGEN_BLOCK.getDefaultState(),
                RegisterBlocks.CHAOTIC_1_8.getDefaultState()
        ));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> context,
            RegistryKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC config
    ) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerConfiguredKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MOD_ID, name));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Radik.MOD_ID, name));
    }
}
