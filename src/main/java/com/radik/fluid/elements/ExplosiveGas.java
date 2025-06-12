package com.radik.fluid.elements;

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

import static net.minecraft.fluid.FlowableFluid.FALLING;

public class ExplosiveGas extends FluidBlock {
    // TODO: та не релизнуто еще, не бей, я правда думаю как это релизнуть
    private final byte[] DENSITY = new byte[16];

    public ExplosiveGas(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void scheduledTick(BlockState state, @NotNull ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockState(pos).getBlock() instanceof ExplosiveGas) {
            FluidState fluidState = world.getFluidState(pos);
            if (fluidState.getLevel() > 0) {
                explode(world, pos);
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state)
                .with(LEVEL, 8)
                .with(FALLING, true);
    }

    private void explode(@NotNull World world, BlockPos pos) {
        world.removeBlock(pos, false);

        Vec3d center = pos.toCenterPos();
        float power = 2 * (float) Math.log(world.getFluidState(pos).getLevel());
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

            // Проверяем уровень соседнего газа
            if (neighborState.getBlock() instanceof ExplosiveGas) {
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
            if (state.getBlock() instanceof ExplosiveGas) {
                return Optional.of(0.0f);
            }
            return super.getBlastResistance(explosion, world, pos, state, fluid);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, @NotNull World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (world.getDimension().toString().split("/ ")[1].split("]")[0].equals("minecraft:infiniburn_nether")) {
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
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 2);
        }
    }

    private boolean isNearIgnitionSource(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos checkPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(checkPos);

            if (neighborState.isIn(BlockTags.FIRE) ||
                    neighborState.isOf(Blocks.LAVA) ||
                    neighborState.isOf(Blocks.LAVA_CAULDRON) ||
                    neighborState.isOf(Blocks.MAGMA_BLOCK) ||
                    neighborState.isOf(Blocks.CAMPFIRE)) {
                return true;
            }

            if (neighborState.isOf(Blocks.REDSTONE_TORCH) ||
                    neighborState.isOf(Blocks.REDSTONE_BLOCK) ||
                    neighborState.isOf(Blocks.REDSTONE_WIRE) && neighborState.get(RedstoneWireBlock.POWER) > 0) {
                if (world.random.nextInt(20) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}