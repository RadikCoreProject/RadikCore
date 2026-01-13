package com.radik.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.TickPriority;
import org.apache.http.annotation.Experimental;
import org.jetbrains.annotations.NotNull;

import static com.radik.Data.getDimension;

public class BasedGas extends Gas {

    @Override
    protected void tryFlow(ServerWorld world, BlockPos fluidPos, BlockState blockState, FluidState fluidState) {
        int currentLevel = getLevel(fluidState);
        String dimension = getDimension(world);
        BlockPos Pos;
        Direction d;
        if (currentLevel <= 0) return;

        if (dimension.isEmpty()) dimension = getDimension(world);

        if (dimension.equals("overworld")) {
            Pos = fluidPos.up();
            d = Direction.UP;
        } else {
            Pos = fluidPos.down();
            d = Direction.DOWN;
        }
        if (canFlow(world, fluidPos, fluidState, d, Pos)) {
            transferLevel(world, fluidPos, fluidState, Pos);
            return;
        }

        BlockState upBlockState = world.getBlockState(Pos);
        if (upBlockState.isSolid() || upBlockState.getFluidState().getLevel() == 8) {
            java.util.Random random = new java.util.Random();
            for (int i = 0; i < 4; i++) {
                Direction direction = Direction.byIndex(random.nextInt(4) + 2);
                BlockPos sidePos = fluidPos.offset(direction);
                if (canFlow(world, fluidPos, fluidState, direction, sidePos)) {
                    transferLevel(world, fluidPos, fluidState, sidePos);
                    break;
                }
            }
        }
    }

    protected void transferLevel(@NotNull World world, BlockPos fromPos, FluidState fromState, BlockPos toPos) {
        int currentLevel = getLevel(fromState);
        BlockState toBlockState = world.getBlockState(toPos);
        FluidState existingState = toBlockState.getFluidState();
        Block block = world.getBlockState(fromPos).getBlock();

        if (toBlockState.isAir()) {
            world.setBlockState(toPos, getFlowing(1, false).getBlockState());
        } else if (toBlockState.getBlock() == block) {
            world.setBlockState(toPos, getFlowing(existingState.getLevel() + 1, existingState.getLevel() == 7).getBlockState());
        } else {
            return;
        }

        if (currentLevel > 1) {
            world.setBlockState(fromPos, getFlowing(currentLevel - 1, false).getBlockState());
        } else {
            world.setBlockState(fromPos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onScheduledTick(ServerWorld world, BlockPos pos, BlockState blockState, FluidState fluidState) {
        this.tryFlow(world, pos, blockState, fluidState);

        if (!world.isClient() && world.getFluidState(pos).getFluid() instanceof BasedGas) {
            world.scheduleFluidTick(pos, fluidState.getFluid(), this.getTickRate(world), TickPriority.NORMAL);
        }
    }

    @Override
    protected boolean canFlow(@NotNull BlockView world, BlockPos fromPos, FluidState fromState, Direction direction, BlockPos toPos) {
        int currentLevel = getLevel(fromState);
        BlockState toState = world.getBlockState(toPos);
        if (toState.isAir()) return true;
        int pLevel = toState.getFluidState().getLevel();

        if (toState.getFluidState().getFluid() instanceof BasedGas) {
            return (direction == Direction.UP || direction == Direction.DOWN) && toState.getBlock() == fromState.getBlockState().getBlock() ? pLevel < 8 : pLevel < currentLevel;
        }
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
        super.appendProperties(builder);
        builder.add(LEVEL);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return fluid instanceof Gas;
    }

    public FluidState getMax() {
        return getFlowing(8, true);
    }

    @Override
    public boolean isStill(FluidState state) {
        return getLevel(state) == 8;
    }

    @Override
    public int getLevel(@NotNull FluidState state) {
        return state.get(LEVEL);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, @NotNull FluidState state, Random random) {
        super.randomDisplayTick(world, pos, state, random);
        if (state.isStill() && random.nextInt(30) == 0) {
            world.addParticleClient(
                    ParticleTypes.CLOUD,
                    pos.getX() + random.nextFloat(),
                    pos.getY() + 1,
                    pos.getZ() + random.nextFloat(),
                    0, 0.1, 0
            );
        }
    }

    @Override
    protected boolean isInfinite(ServerWorld world) {
        return false;
    }

    // TODO: не реализовано
    // TODO: СРАВНЕНИЕ ПЛОТНОСТЕЙ ВЫЗЫВАЕТ КРАШ!
    @Experimental
    private boolean density(@NotNull World world, @NotNull BlockPos pos) {
        FluidBlock block_up = (FluidBlock) world.getBlockState(pos.up()).getBlock();
        FluidBlock block_this = (FluidBlock) world.getBlockState(pos).getBlock();
        return false;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 5;
    }

    public static class Still extends BasedGas {
        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends BasedGas {
        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }
}