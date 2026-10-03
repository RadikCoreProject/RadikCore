package com.radik.block.custom.crop;

import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.NotNull;

public abstract class ManyLayerCropBlock extends CropBlock {
    public static final BooleanProperty STICKED = BooleanProperty.of("stick");
    public static final BooleanProperty HAS_FRUIT = BooleanProperty.of("fruit");
    public static final IntProperty AGE = Properties.AGE_7;

    public ManyLayerCropBlock(Settings settings) {
        super(settings
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)
            .pistonBehavior(PistonBehavior.DESTROY));
        setDefaultState(getStateManager().getDefaultState().with(Properties.AGE_7, 0).with(STICKED, false).with(HAS_FRUIT, false));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AGE, STICKED, HAS_FRUIT);
    }

    @Override
    protected IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void randomTick(BlockState state, @NotNull ServerWorld world, BlockPos pos, @NotNull Random random) {
        if (random.nextInt(3) != 0) return;
        if (world.getLightLevel(pos) >= 7) {
            int i = this.getAge(state);
            float f = getAvailableMoisture(this, world, pos);
            if (i < 7 && (this.hasStick(state) || i < 3)) {
                int pk = (int)(25.0F / f) + 1;
                int k = random.nextInt(pk);
                if (k == 0) world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_LISTENERS);
                else growFruit(state, world, pos, k, i);
            } else if (i == 7) {
                int pk = (int) (25.0F / f) + 1;
                int k = random.nextInt(pk);
                if (world.getBlockState(pos.down(2)).isOf(this) && world.getBlockState(pos.down()).isOf(this)) {
                    growFruit(state, world, pos, k, i);
                    return;
                }
                if (k == 0 && world.getBlockState(pos.up()).isAir()) world.setBlockState(pos.up(), this.withAge(0), Block.NOTIFY_LISTENERS);
                else growFruit(state, world, pos, k, i);
            } else {
                world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), Block.NOTIFY_LISTENERS);
            }
        } else {
            world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), Block.NOTIFY_LISTENERS);
        }
    }

    protected void growFruit(BlockState state, World world, BlockPos pos, int k, int size) {
        boolean b;
        if (size == 7) b = k % 2 == 1;
        else b = k % 3 == 2 && this.getAge(state) >= 4;
        if (b && !this.hasFruit(state)) world.setBlockState(pos, state.with(HAS_FRUIT, true), Block.NOTIFY_LISTENERS);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world instanceof ServerWorld serverWorld) {
            if (this.hasFruit(state)) {
                this.dropVegetables(serverWorld, pos);
                serverWorld.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + serverWorld.random.nextFloat() * 0.4F);
                return ActionResult.SUCCESS;
            } else if (!this.hasStick(state) && player.getStackInHand(Hand.MAIN_HAND).getItem().equals(Items.STICK)) {
                ItemStack stick = player.getStackInHand(Hand.MAIN_HAND);
                stick.decrementUnlessCreative(1, player);
                BlockState state1 = state.with(STICKED, true);
                serverWorld.setBlockState(pos, state1, Block.NOTIFY_LISTENERS);
                serverWorld.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + serverWorld.random.nextFloat() * 0.4F);
                serverWorld.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state1));
                return ActionResult.SUCCESS;
            }
        }

        return super.onUse(state, world, pos, player, hit);
    }

    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (world instanceof ServerWorld serverWorld) {
            Block block = state.getBlock();
            BlockState state1 = world.getBlockState(pos.up());
            BlockState state2 = world.getBlockState(pos.up(2));
            if (state1.getBlock().equals(block)) {
                if (state2.getBlock().equals(block)) {
                    this.dropVegetables(serverWorld, pos.up(2), state2, true);
                }
                this.dropVegetables(serverWorld, pos.up(1), state1, true);
            }
            this.dropVegetables(serverWorld, pos, state, true);
        }
    }

    protected abstract int[][] dropRange();
    protected abstract Item vegetable();
    protected abstract boolean canPlaceOn(BlockState floor, BlockView world, BlockPos pos);

    public void dropVegetables(@NotNull ServerWorld world, BlockPos pos) {
        dropVegetables(world, pos, world.getBlockState(pos), false);
    }

    public void dropVegetables(@NotNull ServerWorld world, BlockPos pos, BlockState state, boolean breaks) {
        int[][] range = this.dropRange();
        int i = this.getAge(state) + range.length - 8;
        if (i < 0 || i >= range.length) return;
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        if (this.hasFruit(state)) {
            world.spawnEntity(
                new ItemEntity(
                    world,
                    x, y, z,
                    new ItemStack(this.vegetable(), world.random.nextBetween(range[i][0], range[i][1]))
                )
            );
            if (!breaks) {
                BlockState state1 = state.with(HAS_FRUIT, false);
                world.setBlockState(pos, state1, Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(null, state1));
            }
        }
        if (this.hasStick(state) && breaks) {
            world.spawnEntity(
                new ItemEntity(
                    world,
                    x, y, z,
                    new ItemStack(Items.STICK)
                )
            );
        }
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return !this.isMature(state) && this.getAge(state) < 7 && (this.hasStick(state) || this.getAge(state) < 3);
    }

    protected final boolean hasFruit(@NotNull BlockState state) { return state.get(HAS_FRUIT); }
    protected final boolean hasStick(@NotNull BlockState state) { return state.get(STICKED); }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, @NotNull BlockPos pos) {
        BlockPos blockPos = pos.down();
        return this.canPlaceOn(world.getBlockState(blockPos), world, blockPos);
    }

    @Override
    public int getMaxAge() {
        return 8;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(@NotNull BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        return !((ManyLayerCropBlock)state.getBlock()).canPlantOnTop(world.getBlockState(pos.down()), world, pos)
            ? Blocks.AIR.getDefaultState()
            : state;
    }

    @Override
    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int i = Math.min(this.getMaxAge(), this.getAge(state) + this.getGrowthAmount(world));
        world.setBlockState(pos, state.with(AGE, i).with(HAS_FRUIT, hasFruit(state)).with(STICKED, hasStick(state)), Block.NOTIFY_LISTENERS);
    }
}
