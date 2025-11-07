package com.radik.fluid;

import java.util.Optional;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public abstract class Gas extends FlowableFluid {
    @Override
    public Fluid getFlowing() {
        return RegisterFluids.FLOWING_HYDROGEN;
    }

    @Override
    public Fluid getStill() {
        return RegisterFluids.STILL_HYDROGEN;
    }

    @Override
    protected void tryFlow(ServerWorld world, BlockPos fluidPos, BlockState blockState, FluidState fluidState) {}

    @Override
    protected float getBlastResistance() {
        return 0.5f;
    }

    @Override
    public Item getBucketItem() {
        return Items.BUCKET;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, @NotNull FluidState state, Random random) {
        if (!state.isStill() && !(Boolean)state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound(null, (double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F);
            }
        }
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, @NotNull BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public int getMaxFlowDistance(WorldView world) {
        return 10;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return RegisterFluids.HYDROGEN_BLOCK.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    public boolean matchesType(Fluid fluid) {
        return fluid == RegisterFluids.FLOWING_HYDROGEN || fluid == RegisterFluids.STILL_HYDROGEN;
    }

    public int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    public int getTickRate(WorldView world) {
        return 3;
    }

    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }

    protected abstract boolean canFlow(BlockView world, BlockPos fromPos, FluidState fromState,
                                       Direction direction, BlockPos toPos);

    public static class Still extends Gas {
        public Still() {}

        @Override
        protected boolean isInfinite(ServerWorld world) {
            return false;
        }

        @Override
        protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
            return false;
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        protected boolean canFlow(BlockView world, BlockPos fromPos, FluidState fromState, Direction direction, BlockPos toPos) {
            return false;
        }
    }
}
