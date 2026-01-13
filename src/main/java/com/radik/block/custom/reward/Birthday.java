package com.radik.block.custom.reward;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.RegisterBlocks.*;

public class Birthday extends Block {
    public final String owner;

    public Birthday(AbstractBlock.@NotNull Settings settings, String owner) {
        super(settings.sounds(BlockSoundGroup.GLASS).strength(3, 9999999).noBlockBreakParticles().nonOpaque());
        this.owner = owner;
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Block block = state.getBlock();
        if (block.equals(HOUSE)) return VoxelShapes.cuboid(0.0625, 0, -0.1875, 0.9375, 1, 1.1875);
        if (block.equals(BICYCLE)) return VoxelShapes.cuboid(0.5, 0, -0.6125, 0.61125, 1.0235, 1.6125);
        if (block.equals(BAG)) return VoxelShapes.cuboid(0.11, 0, 0.195, 0.89, 0.703, 0.898);
        return VoxelShapes.cuboid(0, 0, 0, 1, 1, 1);
    }
}
