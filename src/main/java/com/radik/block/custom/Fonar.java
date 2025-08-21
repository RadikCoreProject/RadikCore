package com.radik.block.custom;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.radik.block.custom.BlockData.FACING;

public class Fonar extends Block {
    private static final List<Direction> dirs = List.of(Direction.NORTH, Direction.SOUTH, Direction.DOWN);
    public Fonar(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, true));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, dirs.contains(ctx.getPlayerLookDirection()));
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
