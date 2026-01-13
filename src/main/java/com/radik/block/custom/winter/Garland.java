package com.radik.block.custom.winter;

import com.radik.block.custom.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.custom.BlockData.*;

public class Garland extends RotatableBlock {
    public Garland(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING2, COUNT);
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        int c = 0;
        boolean[] p = new boolean[4];
        if (world.getBlockState(pos.north()).isFullCube(world, pos.north())) {
            c++;
            p[0] = true;
        }
        if (world.getBlockState(pos.west()).isFullCube(world, pos.west())) {
            c++;
            p[1] = true;
        }
        if (world.getBlockState(pos.south()).isFullCube(world, pos.south())) {
            c++;
            p[2] = true;
        }
        if (world.getBlockState(pos.east()).isFullCube(world, pos.east())) {
            c++;
            p[3] = true;
        }

        if (c == 0) return null;
        if (c == 2 && (p[0] && p[2] || p[1] && p[3])) c = 5;
        BlockState s = this.getDefaultState().with(COUNT, c);

        return switch (c) {
            case 1 -> s.with(FACING2, p[0] ? 0 : p[1] ? 3 : p[2] ? 2 : 1);
            case 2 -> s.with(FACING2, p[0] ? (p[1] ? 3 : 0) : (p[1] ? 2 : 1));
            case 3 -> s.with(FACING2, !p[0] ? 1 : !p[1] ? 0 : !p[2] ? 3 : 2);
            case 4 -> s;
            case 5 -> s.with(FACING2, p[0] ? 0 : 1);
            default -> null;
        };
    }
}
