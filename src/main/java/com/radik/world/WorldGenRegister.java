package com.radik.world;

import com.radik.block.RegisterBlocks;
import com.radik.fluid.RegisterFluids;
import com.radik.world.features.GasLakeFeature;
import com.radik.world.features.GasLakeFeatureConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

import static com.radik.Radik.MOD_ID;

public class WorldGenRegister {
    // 1. Регистрируем саму фичу (Feature)
    public static final GasLakeFeature GAS_LAKE_FEATURE = Registry.register(
            Registries.FEATURE,
            Identifier.of(MOD_ID, "hydrogen_lake_feature"),
            new GasLakeFeature(GasLakeFeatureConfig.CODEC)
    );

    // 2. Ключи для ConfiguredFeature и PlacedFeature
    public static final RegistryKey<ConfiguredFeature<?, ?>> HYDROGEN_LAKE_KEY =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MOD_ID, "hydrogen_lake"));

    public static final RegistryKey<PlacedFeature> HYDROGEN_LAKE_PLACED_KEY =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, "hydrogen_lake_placed"));

    // 3. Bootstrap для ConfiguredFeature
    public static void bootstrapConfigured(Registerable<ConfiguredFeature<?, ?>> context) {
        context.register(
                HYDROGEN_LAKE_KEY,
                new ConfiguredFeature<>(
                        GAS_LAKE_FEATURE,
                        new GasLakeFeatureConfig(
                                15,
                                RegisterFluids.HYDROGEN_BLOCK.getDefaultState(),
                                RegisterBlocks.CHAOTIC_1_8.getDefaultState()
                        )
                )
        );
    }

    // 4. Bootstrap для PlacedFeature
    public static void bootstrapPlaced(Registerable<PlacedFeature> context) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures =
                context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        context.register(
                HYDROGEN_LAKE_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(HYDROGEN_LAKE_KEY),
                        List.of(
                                RarityFilterPlacementModifier.of(400),
                                SquarePlacementModifier.of(),
                                BiomePlacementModifier.of()
                        )
                )
        );
    }

    // 5. Добавление в биомы
    public static void registerInBiomes() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and(context ->
                        !context.getBiomeKey().getValue().getPath().contains("barrens")),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                HYDROGEN_LAKE_PLACED_KEY
        );
    }
}
