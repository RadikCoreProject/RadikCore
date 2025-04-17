package com.radik.block.custom;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.radik.block.custom.BlockData.*;

public class Snowman extends Block {
    public Snowman() {super(Settings.create().mapColor(MapColor.WHITE).strength(0.2F).sounds(BlockSoundGroup.SNOW));}


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 1.125, 0.75);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        Random random = new Random();
        int x = random.nextInt(1, 9);
        return this.getDefaultState().with(TYPE, x);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING2, TYPE);
    }
}
