package com.radik.fluid;

import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.radik.Data.getDimension;
import static net.minecraft.fluid.FlowableFluid.FALLING;

public class ExplosiveGasFluid extends BasedGasFluid {
    public ExplosiveGasFluid(FlowableFluid fluid, Settings settings, float K) {
        super(fluid, settings, K);
    }

    @Override
    public void scheduledTick(BlockState state, @NotNull ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockState(pos).getBlock() instanceof ExplosiveGasFluid) {
            FluidState fluidState = world.getFluidState(pos);
            if (fluidState.getLevel() > 0) {
                explode(world, pos);
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state)
                .with(FALLING, true);
    }

    private void explode(@NotNull World world, @NotNull BlockPos pos) {
        Vec3d center = pos.toCenterPos();
        FluidState fluidState = world.getFluidState(pos);
        int level = fluidState.getLevel();
        world.removeBlock(pos, false);
        float power = (float) Math.log(level + 1);

        world.createExplosion(
                null,
                world.getDamageSources().inFire(),
                new GasExplosionBehavior(),
                center.getX(), center.getY(), center.getZ(),
                power,
                true,
                World.ExplosionSourceType.BLOCK
        );

        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighbor);

            // проверяем уровень соседнего газа
            if (neighborState.getBlock() instanceof ExplosiveGasFluid) {
                FluidState neighborFluid = world.getFluidState(neighbor);
                if (neighborFluid.getLevel() > 0) {
                    scheduleExplosion(world, neighbor);
                }
            }
        }
    }

    private static class GasExplosionBehavior extends ExplosionBehavior {
        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, @NotNull BlockState state, FluidState fluid) {
            if (state.getBlock() instanceof ExplosiveGasFluid) {
                return Optional.of(0.0f);
            }
            return super.getBlastResistance(explosion, world, pos, state, fluid);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, @NotNull World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (getDimension(world).equals("nether")) {
            scheduleExplosion(world, pos);
        }
        if (isNearIgnitionSource(world, pos)) {
            scheduleExplosion(world, pos);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (isNearIgnitionSource(world, pos)) {
            scheduleExplosion(world, pos);
        }
        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    private void scheduleExplosion(@NotNull World world, BlockPos pos) {
        if (!world.isClient()) {
            world.scheduleBlockTick(pos, this, 2);
        }
    }

    private boolean isNearIgnitionSource(World world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockState state = world.getBlockState(pos.offset(dir));
            if (state.isIn(BlockTags.FIRE)) return true;

            if (state.isOf(Blocks.LAVA) ||
                    state.isOf(Blocks.MAGMA_BLOCK) ||
                    state.isOf(Blocks.CAMPFIRE) ||
                    state.isOf(Blocks.TORCH) ||
                    state.isOf(Blocks.SOUL_TORCH) ||
                    state.isOf(Blocks.SOUL_CAMPFIRE) ||
                    state.isOf(Blocks.REDSTONE_BLOCK) ||
                    state.isOf(Blocks.REDSTONE_WALL_TORCH) ||
                    state.isOf(Blocks.REDSTONE_TORCH) ||
                    state.isOf(Blocks.SOUL_WALL_TORCH) ||
                    state.isOf(Blocks.WALL_TORCH)
            ) {
                return true;
            }
        }
        return false;
    }
}