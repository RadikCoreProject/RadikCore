package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

// test
public record GasLakeFeatureConfig(int maxBlocks, BlockState hydrogenState, BlockState wallBlock) implements FeatureConfig {
    public static final Codec<GasLakeFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("max_blocks").forGetter(GasLakeFeatureConfig::maxBlocks),
                    BlockState.CODEC.fieldOf("hydrogen_block").forGetter(GasLakeFeatureConfig::hydrogenState),
                    BlockState.CODEC.fieldOf("wall_block").forGetter(GasLakeFeatureConfig::wallBlock)
            ).apply(instance, GasLakeFeatureConfig::new)
    );
}