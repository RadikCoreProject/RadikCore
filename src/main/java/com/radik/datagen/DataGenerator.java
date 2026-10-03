package com.radik.datagen;

import com.radik.world.WorldGenRegister;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.NotNull;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NotNull FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(AdvancementsProvider::new);
        pack.addProvider(WorldGenProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
//        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, WorldGenRegister::bootstrapConfigured);
//        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, WorldGenRegister::bootstrapPlaced);
    }
}
