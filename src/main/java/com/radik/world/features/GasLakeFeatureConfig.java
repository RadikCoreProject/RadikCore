package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;

public record GasLakeFeatureConfig(
        int maxBlocks,
        BlockState hydrogenState,
        BlockState replaceState,
        List<BlockState> wallBlocks,
        boolean underground,
        boolean deep
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
                    BlockState.CODEC.fieldOf("replace_block").forGetter(GasLakeFeatureConfig::replaceState),
                    BlockState.CODEC.listOf()
                            .fieldOf("wall_blocks")
                            .flatXmap(
                                    list -> list.isEmpty()
                                            ? DataResult.error(() -> "Wall blocks list must contain at least one block")
                                            : DataResult.success(list),
                                    DataResult::success
                            )
                            .forGetter(GasLakeFeatureConfig::wallBlocks),
                    Codec.BOOL.optionalFieldOf("underground", false).forGetter(GasLakeFeatureConfig::underground),
                    Codec.BOOL.optionalFieldOf("deep", false).forGetter(GasLakeFeatureConfig::deep)
            ).apply(instance, GasLakeFeatureConfig::new)
    );
}