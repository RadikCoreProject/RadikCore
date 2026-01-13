package com.radik.block.custom.winter;

import com.radik.Radik;
import com.radik.block.custom.RotatableBlock;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.custom.BlockData.*;

public class Snowman extends RotatableBlock {
    public Snowman(Settings settings) {super(settings);}

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 1.125, 0.75);
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState()
            .with(FACING2, Math.abs(ctx.getPlayerLookDirection().getHorizontalQuarterTurns()))
            .with(TYPE, Radik.RANDOM.nextInt(1, 10));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING2, TYPE);
    }
}
