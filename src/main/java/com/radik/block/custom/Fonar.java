package com.radik.block.custom;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.custom.BlockData.FACING;

public class Fonar extends Block {
    public Fonar(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, true));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 1, 0.5625);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
