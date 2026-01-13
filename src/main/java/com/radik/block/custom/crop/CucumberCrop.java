package com.radik.block.custom.crop;

import com.mojang.serialization.MapCodec;
import com.radik.block.RegisterBlocks;
import com.radik.item.RegisterItems;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class CucumberCrop extends ManyLayerCropBlock {
    public static final MapCodec<CucumberCrop> CODEC = createCodec(CucumberCrop::new);
    private static final VoxelShape[] SHAPES_BY_AGE = Block.createShapeArray(7, age -> Block.createColumnShape(16.0, 0.0, 2 + age * 2));

    @Override
    public MapCodec<CucumberCrop> getCodec() {
        return CODEC;
    }

    public CucumberCrop(AbstractBlock.@NotNull Settings settings) {
        super(settings
            .mapColor(MapColor.DARK_GREEN)
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)
            .pistonBehavior(PistonBehavior.DESTROY));
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return RegisterItems.CUCUMBER_SEEDS;
    }

    @Override
    protected void randomTick(BlockState state, @NotNull ServerWorld world, BlockPos pos, @NotNull Random random) {
        if (random.nextInt(3) != 0) {
            super.randomTick(state, world, pos, random);
        }
    }

    @Override
    protected int[][] dropRange() {
        return new int[][]{
            new int[]{1, 1},
            new int[]{1, 2},
            new int[]{2, 3},
            new int[]{3, 4}
        };
    }

    @Override
    protected Item vegetable() {
        return RegisterItems.CUCUMBER;
    }

    @Override
    protected int getGrowthAmount(World world) {
        return super.getGrowthAmount(world) / 3;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_AGE[this.getAge(state)];
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.FARMLAND) || floor.isOf(RegisterBlocks.CUCUMBER);
    }

    protected boolean canPlaceOn(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.FARMLAND);
    }
}
