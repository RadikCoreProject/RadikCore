package com.radik.datagen;

import com.radik.block.RegisterBlocks;
import com.radik.fluid.RegisterFluids;
import com.radik.world.WorldGenRegister;
import com.radik.world.features.GasLakeFeature;
import com.radik.world.features.GasLakeFeatureConfig;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TutorialModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModRegistryDataGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, WorldGenRegister::bootstrapConfigured);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, WorldGenRegister::bootstrapPlaced);
    }

    public static class WorldGenProvider extends FabricDynamicRegistryProvider {
        public WorldGenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            GasLakeFeature gasLakeFeature = new GasLakeFeature(GasLakeFeatureConfig.CODEC);
            GasLakeFeatureConfig config = new GasLakeFeatureConfig(
                    15,
                    RegisterFluids.HYDROGEN_BLOCK.getDefaultState(),
                    RegisterBlocks.CHAOTIC_1_8.getDefaultState()
            );
            ConfiguredFeature<?, ?> configuredFeature = new ConfiguredFeature<>(gasLakeFeature,config);

            // Добавляем ConfiguredFeature
            entries.add(WorldGenRegister.HYDROGEN_LAKE_KEY, configuredFeature);

            entries.add(WorldGenRegister.HYDROGEN_LAKE_PLACED_KEY, new PlacedFeature(
                    RegistryEntry.of(entries.ref(WorldGenRegister.HYDROGEN_LAKE_KEY).value()),
                    List.of(
                            RarityFilterPlacementModifier.of(30),
                            SquarePlacementModifier.of(),
                            BiomePlacementModifier.of()
                    )
            ));
        }

        @Override
        public String getName() {
            return "World Gen";
        }
    }
}