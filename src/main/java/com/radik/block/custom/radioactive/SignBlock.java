package com.radik.block.custom.radioactive;

import com.radik.block.custom.RotatableBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.function.BiFunction;

public class SignBlock extends RotatableBlock {
    public SignBlock(Settings settings) {
        super(settings.strength(1, 2).mapColor(DyeColor.LIGHT_GRAY).nonOpaque().noCollision());
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BooleanBiFunction func = BooleanBiFunction.OR;

        return switch (state.get(FACING)) {
            case SOUTH -> VoxelShapes.combine(
                VoxelShapes.cuboid(0.46875, 0, 0.46875, 0.53125, 1.4375, 0.53125),
                VoxelShapes.cuboid(0.125, 0.8125, 0.515625, 0.875, 1.484375, 0.640625),
                func
            );
            case NORTH -> VoxelShapes.combine(
                VoxelShapes.cuboid(0.46875, 0, 0.46875, 0.53125, 1.4375, 0.53125),
                VoxelShapes.cuboid(0.125, 0.8125, 0.359375, 0.875, 1.484375, 0.484375),
                func
            );
            case EAST -> VoxelShapes.combine(
                VoxelShapes.cuboid(0.46875, 0, 0.46875, 0.53125, 1.4375, 0.53125),
                VoxelShapes.cuboid(0.515625, 0.8125, 0.125, 0.640625, 1.484375, 0.875),
                func
            );
            case WEST -> VoxelShapes.combine(
                VoxelShapes.cuboid(0.46875, 0, 0.46875, 0.53125, 1.4375, 0.53125),
                VoxelShapes.cuboid(0.359375, 0.8125, 0.125, 0.484375, 1.484375, 0.875),
                func
            );
            case null, default -> VoxelShapes.fullCube();
        };
    }
}
