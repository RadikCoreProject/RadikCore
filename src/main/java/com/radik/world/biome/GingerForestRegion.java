package com.radik.world.biome;

import com.mojang.datafixers.util.Pair;
import com.radik.Radik;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class GingerForestRegion extends Region {
    public GingerForestRegion() {
        super(Identifier.of(Radik.MOD_ID, "ginger_forest_region"), RegionType.OVERWORLD, 2);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        this.addBiome(mapper,
            // Temperature
            MultiNoiseUtil.ParameterRange.of(0.1f, 0.7f),
            // Watering
            MultiNoiseUtil.ParameterRange.of(0.3f, 0.8f),
            // Continentalness
            MultiNoiseUtil.ParameterRange.of(0.3f, 1f),
            // Erosion
            MultiNoiseUtil.ParameterRange.of(0.1f, 0.9f),
            // Weirdness
            MultiNoiseUtil.ParameterRange.of(0.2f, 0.9f),
            // Depth
            MultiNoiseUtil.ParameterRange.of(-0.1f, 0.1f),
            0.0f,
            RegisterBiomes.GINGER_FOREST
        );

        this.addBiome(mapper,
            ParameterUtils.Temperature.WARM.parameter(),
            ParameterUtils.Humidity.HUMID.parameter(),
            ParameterUtils.Continentalness.INLAND.parameter(),
            ParameterUtils.Erosion.EROSION_3.parameter(),
            MultiNoiseUtil.ParameterRange.of(0.1f, 0.2f),
            ParameterUtils.Depth.SURFACE.parameter(),
            0.0f,
            BiomeKeys.FOREST
        );
    }
}
