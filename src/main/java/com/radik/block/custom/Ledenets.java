package com.radik.block.custom;

import com.radik.block.RegisterBlocks;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.radik.block.custom.BlockData.*;

public class Ledenets extends Block {
    public Ledenets() {
        super(Settings.copy(Blocks.WHITE_WOOL).nonOpaque());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 0.75, 0.5625);
    }


    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Random random = new Random();
        int x = random.nextInt(1, 72);
        BlockState blockState = RegisterBlocks.LEDENETS2.getDefaultState().with(TYPE2, x);
        return this.getStateWithProperties(blockState);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING2, TYPE2);
    }
}
