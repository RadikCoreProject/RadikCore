package com.radik.block.custom.tech;

import com.radik.block.custom.RotatableBlock;
import net.minecraft.block.*;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.custom.BlockData.FACING2;

public class Fonar2 extends RotatableBlock {
    public Fonar2(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING2, 0));
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING2, Math.abs(ctx.getPlayerLookDirection().getHorizontalQuarterTurns()));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 1, 0.5625);
    }
}
