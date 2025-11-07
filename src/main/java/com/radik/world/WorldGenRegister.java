package com.radik.world;

import com.radik.block.RegisterBlocks;
import com.radik.fluid.RegisterFluids;
import com.radik.registration.IRegistry;
import com.radik.world.features.GasLakeFeature;
import com.radik.world.features.GasLakeFeatureConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

import static com.radik.Radik.MOD_ID;

public class WorldGenRegister implements IRegistry {
    public static final GasLakeFeature GAS_LAKE_FEATURE = Registry.register(
            Registries.FEATURE,
            Identifier.of(MOD_ID, "lake_feature"),
            new GasLakeFeature(GasLakeFeatureConfig.CODEC)
    );

    public static final RegistryKey<ConfiguredFeature<?, ?>> HYDROGEN_LAKE_KEY = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MOD_ID, "hydrogen_lake"));
    public static final RegistryKey<PlacedFeature> HYDROGEN_LAKE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, "hydrogen_lake_placed"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> HELIUM_LAKE_KEY = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MOD_ID, "helium_lake"));
    public static final RegistryKey<PlacedFeature> HELIUM_LAKE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, "helium_lake_placed"));

    // configured feature
    public static void bootstrapConfigured(Registerable<ConfiguredFeature<?, ?>> context) {
        context.register(
                HYDROGEN_LAKE_KEY,
                new ConfiguredFeature<>(GAS_LAKE_FEATURE,
                        new GasLakeFeatureConfig(
                                25,
                                RegisterFluids.HYDROGEN_BLOCK.getDefaultState(),
                                List.of(
                                        RegisterBlocks.CHAOTIC_1_8.getDefaultState(),
                                        RegisterBlocks.CHAOTIC_2_8.getDefaultState(),
                                        RegisterBlocks.CHAOTIC_3_8.getDefaultState(),
                                        Blocks.END_STONE.getDefaultState()
                                )
                        )
                )
        );

        context.register(
                HELIUM_LAKE_KEY,
                new ConfiguredFeature<>(GAS_LAKE_FEATURE,
                        new GasLakeFeatureConfig(
                                25,
                                RegisterFluids.HELIUM_BLOCK.getDefaultState(),
                                List.of(
                                        RegisterBlocks.CHAOTIC_1_8.getDefaultState(),
                                        RegisterBlocks.CHAOTIC_2_8.getDefaultState(),
                                        RegisterBlocks.CHAOTIC_3_8.getDefaultState(),
                                        Blocks.END_STONE.getDefaultState()
                                )
                        )
                )
        );
    }

    // placed feature
    public static void bootstrapPlaced(Registerable<PlacedFeature> context) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        context.register(
                HYDROGEN_LAKE_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(HYDROGEN_LAKE_KEY),
                        List.of(
                                RarityFilterPlacementModifier.of(200),
                                SquarePlacementModifier.of(),
                                BiomePlacementModifier.of()
                        )
                )
        );

        context.register(
                HELIUM_LAKE_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(HELIUM_LAKE_KEY),
                        List.of(
                                RarityFilterPlacementModifier.of(200),
                                SquarePlacementModifier.of(),
                                BiomePlacementModifier.of()
                        )
                )
        );
    }

    // biomes
    public static void initialize() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and(context ->
                        !context.getBiomeKey().getValue().getPath().contains("small_end_islands")),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                HYDROGEN_LAKE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and(context ->
                        !context.getBiomeKey().getValue().getPath().contains("small_end_islands")),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                HELIUM_LAKE_PLACED_KEY
        );
    }
}
