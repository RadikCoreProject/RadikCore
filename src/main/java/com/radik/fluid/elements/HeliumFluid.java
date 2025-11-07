package com.radik.fluid.elements;

import com.radik.fluid.BasedGas;
import com.radik.fluid.RegisterFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class HeliumFluid extends BasedGas {
    @Override
    public Fluid getFlowing() {
        return RegisterFluids.FLOWING_HELIUM;
    }

    @Override
    protected void tryFlow(ServerWorld world, BlockPos fluidPos, BlockState blockState, FluidState fluidState) {
        super.tryFlow(world, fluidPos, blockState, fluidState);
    }

    @Override
    public Fluid getStill() {
        return RegisterFluids.STILL_HELIUM;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return RegisterFluids.HELIUM_BLOCK.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    @Override
    protected void transferLevel(@NotNull World world, BlockPos fromPos, FluidState fromState, BlockPos toPos) {
        super.transferLevel(world, fromPos, fromState, toPos);
    }

    public FluidState getFlowing(int level, boolean falling) {
        return this.getFlowing().getDefaultState().with(LEVEL, level).with(FALLING, falling);
    }

    public static class Still extends HeliumFluid {
        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends HeliumFluid {
        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }
}
