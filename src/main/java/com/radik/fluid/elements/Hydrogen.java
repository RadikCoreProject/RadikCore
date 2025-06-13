package com.radik.fluid.elements;

import com.radik.fluid.Gas;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

import static com.radik.Data.getDimension;

public abstract class Hydrogen extends Gas {
    private String dimension = "";

    @Override
    protected void tryFlow(ServerWorld world, BlockPos fluidPos, BlockState blockState, FluidState fluidState) {
        int currentLevel = getLevel(fluidState);
        BlockPos Pos;
        Direction d;
        if (currentLevel <= 0) return;

        if (dimension.isEmpty()) {
            dimension = getDimension(world);
        }

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

    private void transferLevel(World world, BlockPos fromPos, FluidState fromState, BlockPos toPos) {
        int currentLevel = getLevel(fromState);

        if (currentLevel > 1) {
            world.setBlockState(fromPos, getFlowing(currentLevel - 1, false).getBlockState());
        } else {
            world.setBlockState(fromPos, Blocks.AIR.getDefaultState());
        }

        BlockState toBlockState = world.getBlockState(toPos);
        FluidState existingState = toBlockState.getFluidState();
        if (toBlockState.isAir()) {
            world.setBlockState(toPos, getFlowing(1, false).getBlockState());
        } else if (existingState.getFluid() instanceof Hydrogen) {
            world.setBlockState(toPos, getFlowing(existingState.getLevel() + 1, existingState.getLevel() == 7).getBlockState());
        }
    }

    @Override
    public void onScheduledTick(ServerWorld world, BlockPos pos, BlockState blockState, FluidState fluidState) {
        this.tryFlow(world, pos, blockState, fluidState);

        if (!world.isClient && world.getFluidState(pos).getFluid() instanceof Hydrogen) {
            world.scheduleFluidTick(pos, fluidState.getFluid(), this.getTickRate(world), TickPriority.NORMAL);
        }
    }

    @Override
    protected boolean canFlow(BlockView world, BlockPos fromPos, FluidState fromState,
                              Direction direction, BlockPos toPos) {
        int currentLevel = getLevel(fromState);

        BlockState toState = world.getBlockState(toPos);
        if (toState.isAir()) return true;

        int pLevel = toState.getFluidState().getLevel();

        if (toState.getFluidState().getFluid() instanceof Hydrogen) {
            return direction == Direction.UP || direction == Direction.DOWN ? pLevel < 8 : pLevel < currentLevel;
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
    public int getLevel(FluidState state) {
        return state.get(LEVEL);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
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
    public int getTickRate(WorldView world) {
        return 5;
    }

    public static class Still extends Hydrogen {

        @Override
        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        protected boolean isInfinite(ServerWorld world) {
            return false;
        }
    }

    public static class Flowing extends Hydrogen {

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }

        @Override
        protected boolean isInfinite(ServerWorld world) {
            return false;
        }
    }
}