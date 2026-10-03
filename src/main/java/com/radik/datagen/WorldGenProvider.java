package com.radik.datagen;

import com.mojang.serialization.Lifecycle;
import com.radik.world.WorldGenRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends FabricDynamicRegistryProvider {

    public WorldGenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(@NotNull RegistryWrapper.WrapperLookup registries, @NotNull Entries entries) {
        Registerable<ConfiguredFeature<?, ?>> configuredRegisterable = new Registerable<>() {
            @Override
            public RegistryEntry.Reference<ConfiguredFeature<?, ?>> register(RegistryKey<ConfiguredFeature<?, ?>> key, ConfiguredFeature<?, ?> value, Lifecycle lifecycle) {
                entries.add(key, value);
                return null;
            }

            @Override
            public <S> RegistryEntryLookup<S> getRegistryLookup(RegistryKey<? extends Registry<? extends S>> registryRef) {
                return registries.getOrThrow(registryRef);
            }
        };
        WorldGenRegister.bootstrapConfigured(configuredRegisterable);

        Registerable<PlacedFeature> placedRegisterable = new Registerable<>() {
            @Override
            public RegistryEntry.Reference<PlacedFeature> register(RegistryKey<PlacedFeature> key, PlacedFeature value, Lifecycle lifecycle) {
                entries.add(key, value);
                return null;
            }

            @Override
            public <S> RegistryEntryLookup<S> getRegistryLookup(RegistryKey<? extends Registry<? extends S>> registryRef) {
                return registries.getOrThrow(registryRef);
            }
        };
        WorldGenRegister.bootstrapPlaced(placedRegisterable);
    }

    @Override
    public @NotNull String getName() {
        return "World Generation (Configured & Placed Features)";
    }
}
