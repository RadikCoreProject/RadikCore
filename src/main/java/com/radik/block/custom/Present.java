package com.radik.block.custom;

import com.radik.block.RegisterBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static com.radik.block.custom.BlockData.TYPE2;

public class Present extends Block {

    public Present(Settings settings) {
        super(settings);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Random random = new Random();
        int x = random.nextInt(1, 72);
        BlockState blockState = RegisterBlocks.PRESENT_SMALL.getDefaultState().with(TYPE2, x);
        return this.getStateWithProperties(blockState);
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        String name = state.getBlock().getName().toString().split("_")[1].split("'")[0];
        VoxelShape shape;
        shape = switch (name) {
            case "small" -> VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.5, 0.75);
            case "medium", "winter" -> VoxelShapes.cuboid(0, 0, 0, 1, 1, 1);
            case "instrument" -> VoxelShapes.cuboid(-0.375, 0, 0.125, 1.375, 0.3125, 0.875);
            case "big" -> VoxelShapes.cuboid(-0.875, -1, -0.875, 1.875, 1.5, 1.875);
            default -> VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 1, 0.5625);
        };
        return shape;
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(TYPE2);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (state.getBlock().equals(RegisterBlocks.PRESENT_BIG)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.setBlockState(pos.up(), state);
        }
    }
}
