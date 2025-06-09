package com.radik.block.custom;

import com.radik.block.RegisterBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.custom.BlockData.FACING2;

public class Trophy extends Block {
    public Trophy() {
        super(AbstractBlock.Settings.create().strength(3, 10).sounds(BlockSoundGroup.AMETHYST_BLOCK).noBlockBreakParticles());
        setDefaultState(getStateManager().getDefaultState().with(FACING2, 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 1, 0.75);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING2);
    }
}
