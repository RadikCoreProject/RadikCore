package com.radik.world;

import net.minecraft.registry.RegistryEntryLookup;
import com.radik.MainInit;
import com.radik.block.RegisterBlocks;
import com.radik.fluid.RegisterFluids;
import com.radik.world.biome.RegisterBiomes;
import com.radik.world.features.BoulderFeature;
import com.radik.world.features.BoulderFeatureConfig;
import com.radik.world.features.GasLakeFeature;
import com.radik.world.features.GasLakeFeatureConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registerable;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

import static com.radik.Radik.MOD_ID;

public class WorldGenRegister {

    public static final RegistryKey<ConfiguredFeature<?, ?>> HYDROGEN_LAKE_KEY = registerConfigured("hydrogen_lake");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HELIUM_LAKE_KEY = registerConfigured("helium_lake");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HYDROGEN_RADIOACTIVE_LAKE_KEY = registerConfigured("hydrogen_radioactive_lake");
    public static final RegistryKey<ConfiguredFeature<?, ?>> RADIOACTIVE_STONE_BOULDER_KEY = registerConfigured("radioactive_stone_boulder");

    public static final RegistryKey<PlacedFeature> HYDROGEN_LAKE_PLACED_KEY = registerPlaced("hydrogen_lake_placed");
    public static final RegistryKey<PlacedFeature> HELIUM_LAKE_PLACED_KEY = registerPlaced("helium_lake_placed");
    public static final RegistryKey<PlacedFeature> HYDROGEN_RADIOACTIVE_LAKE_PLACED_KEY = registerPlaced("hydrogen_radioactive_lake_placed");
    public static final RegistryKey<PlacedFeature> RADIOACTIVE_STONE_BOULDER_PLACED_KEY = registerPlaced("radioactive_stone_boulder_placed");

    public static final GasLakeFeature GAS_LAKE_FEATURE = Registry.register(
            Registries.FEATURE,
            Identifier.of(MOD_ID, "lake_feature"),
            new GasLakeFeature(GasLakeFeatureConfig.CODEC)
    );

    public static final BoulderFeature BOULDER_FEATURE = Registry.register(
            Registries.FEATURE,
            Identifier.of(MOD_ID, "boulder_feature"),
            new BoulderFeature(BoulderFeatureConfig.CODEC)
    );

    private static RegistryKey<ConfiguredFeature<?, ?>> registerConfigured(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(MOD_ID, name));
    }

    private static RegistryKey<PlacedFeature> registerPlaced(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, name));
    }

//    public static void bootstrapConfigured(Registerable<ConfiguredFeature<?, ?>> context) {
//        context.register(
//                RADIOACTIVE_STONE_BOULDER_KEY,
//                new ConfiguredFeature<>(
//                        BOULDER_FEATURE,
//                        new BoulderFeatureConfig(
//                                Blocks.DEEPSLATE.getDefaultState(),
//                                List.of(
//                                        RegisterBlocks.URANUS_ORE.getDefaultState(),
//                                        RegisterBlocks.LEAD_ORE.getDefaultState()
//                                ),
//                                20, 200, false
//                        )
//                )
//        );
//
//        context.register(
//                HYDROGEN_LAKE_KEY,
//                new ConfiguredFeature<>(
//                        GAS_LAKE_FEATURE,
//                        new GasLakeFeatureConfig(
//                                25,
//                                RegisterFluids.HYDROGEN_BLOCK.getDefaultState(),
//                                Blocks.END_STONE.getDefaultState(),
//                                List.of(
//                                        RegisterBlocks.CHAOTIC_1_8.getDefaultState(),
//                                        RegisterBlocks.CHAOTIC_2_8.getDefaultState(),
//                                        RegisterBlocks.CHAOTIC_3_8.getDefaultState(),
//                                        Blocks.END_STONE.getDefaultState()
//                                ),
//                                false, false
//                        )
//                )
//        );
//
//        context.register(
//                HYDROGEN_RADIOACTIVE_LAKE_KEY,
//                new ConfiguredFeature<>(
//                        GAS_LAKE_FEATURE,
//                        new GasLakeFeatureConfig(
//                                20,
//                                RegisterFluids.HYDROGEN_BLOCK.getDefaultState(),
//                                Blocks.DEEPSLATE.getDefaultState(),
//                                List.of(
//                                        RegisterBlocks.CHAOTIC_1_4.getDefaultState(),
//                                        RegisterBlocks.CHAOTIC_2_4.getDefaultState(),
//                                        RegisterBlocks.CHAOTIC_3_4.getDefaultState(),
//                                        Blocks.DEEPSLATE.getDefaultState()
//                                ),
//                                true, true
//                        )
//                )
//        );
//
//        context.register(
//                HELIUM_LAKE_KEY,
//                new ConfiguredFeature<>(
//                        GAS_LAKE_FEATURE,
//                        new GasLakeFeatureConfig(
//                                25,
//                                RegisterFluids.HELIUM_BLOCK.getDefaultState(),
//                                Blocks.END_STONE.getDefaultState(),
//                                List.of(
//                                        RegisterBlocks.CHAOTIC_1_8.getDefaultState(),
//                                        RegisterBlocks.CHAOTIC_2_8.getDefaultState(),
//                                        RegisterBlocks.CHAOTIC_3_8.getDefaultState(),
//                                        Blocks.END_STONE.getDefaultState()
//                                ),
//                                false, false
//                        )
//                )
//        );
//    }
//
//    public static void bootstrapPlaced(Registerable<PlacedFeature> context) {
//        RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatures =
//                context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
//
//        context.register(
//                HYDROGEN_LAKE_PLACED_KEY,
//                new PlacedFeature(
//                        configuredFeatures.getOrThrow(HYDROGEN_LAKE_KEY),
//                        List.of(
//                                RarityFilterPlacementModifier.of(200),
//                                SquarePlacementModifier.of(),
//                                BiomePlacementModifier.of()
//                        )
//                )
//        );
//
//        context.register(
//                HYDROGEN_RADIOACTIVE_LAKE_PLACED_KEY,
//                new PlacedFeature(
//                        configuredFeatures.getOrThrow(HYDROGEN_RADIOACTIVE_LAKE_KEY),
//                        List.of(
//                                RarityFilterPlacementModifier.of(200),
//                                SquarePlacementModifier.of(),
//                                BiomePlacementModifier.of()
//                        )
//                )
//        );
//
//        context.register(
//                HELIUM_LAKE_PLACED_KEY,
//                new PlacedFeature(
//                        configuredFeatures.getOrThrow(HELIUM_LAKE_KEY),
//                        List.of(
//                                RarityFilterPlacementModifier.of(200),
//                                SquarePlacementModifier.of(),
//                                BiomePlacementModifier.of()
//                        )
//                )
//        );
//
//        context.register(
//                RADIOACTIVE_STONE_BOULDER_PLACED_KEY,
//                new PlacedFeature(
//                        configuredFeatures.getOrThrow(RADIOACTIVE_STONE_BOULDER_KEY),
//                        List.of(
//                                RarityFilterPlacementModifier.of(200),
//                                SquarePlacementModifier.of(),
//                                BiomePlacementModifier.of()
//                        )
//                )
//        );
//    }

    @MainInit
    public static void initialize() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and(context ->
                        !context.getBiomeKey().equals(BiomeKeys.THE_END)),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                HYDROGEN_LAKE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd().and(context ->
                        !context.getBiomeKey().equals(BiomeKeys.THE_END)),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                HELIUM_LAKE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld().and(context ->
                        context.getBiomeKey().equals(RegisterBiomes.GINGER_FOREST)),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                HYDROGEN_RADIOACTIVE_LAKE_PLACED_KEY
        );
    }
}