package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;

public record BoulderFeatureConfig(
        BlockState base,
        List<BlockState> ores,
        int chance,
        int volume,
        boolean ground
) implements FeatureConfig {

    public static final Codec<BoulderFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockState.CODEC.fieldOf("base").forGetter(BoulderFeatureConfig::base),
                    BlockState.CODEC.listOf().fieldOf("ores").forGetter(BoulderFeatureConfig::ores),
                    Codec.intRange(1, 100).fieldOf("chance").forGetter(BoulderFeatureConfig::chance),
                    Codec.intRange(1, 1024).fieldOf("volume").forGetter(BoulderFeatureConfig::volume),
                    Codec.BOOL.fieldOf("ground").forGetter(BoulderFeatureConfig::ground)
            ).apply(instance, BoulderFeatureConfig::new)
    );

    public BoulderFeatureConfig {
        if (ores.isEmpty()) {
            throw new IllegalArgumentException("Ores list must not be empty");
        }
    }
}