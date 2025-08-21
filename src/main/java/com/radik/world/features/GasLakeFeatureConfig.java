package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;

// test
public record GasLakeFeatureConfig(
        int maxBlocks,
        BlockState hydrogenState,
        List<BlockState> wallBlocks // Изменено на список
) implements FeatureConfig {

    public GasLakeFeatureConfig {
        if (wallBlocks.isEmpty()) {
            throw new IllegalArgumentException("Wall blocks list cannot be empty");
        }
    }

    public static final Codec<GasLakeFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("max_blocks").forGetter(GasLakeFeatureConfig::maxBlocks),
                    BlockState.CODEC.fieldOf("hydrogen_block").forGetter(GasLakeFeatureConfig::hydrogenState),
                    BlockState.CODEC.listOf()
                            .fieldOf("wall_blocks")
                            .flatXmap(
                                    list -> list.isEmpty()
                                            ? DataResult.error(() -> "Wall blocks list must contain at least one block")
                                            : DataResult.success(list),
                                    DataResult::success
                            )
                            .forGetter(GasLakeFeatureConfig::wallBlocks)
            ).apply(instance, GasLakeFeatureConfig::new)
    );
}