package com.radik.item.custom;

import com.radik.fluid.Gas;
import com.radik.fluid.RegisterFluids;
import com.radik.fluid.elements.HydrogenFluid;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import static com.radik.Data.CAPSULE_FLUID;
import static com.radik.Data.CAPSULE_LEVEL;
import static com.radik.item.RegisterItems.CAPSULE;
import static net.minecraft.block.FluidBlock.LEVEL;

public class Capsule extends Item implements FluidModificationItem {
    public Capsule(Settings settings) {
        super(settings.component(CAPSULE_LEVEL, 0).component(CAPSULE_FLUID, 0));
    }

    protected static Fluid getCapsule(int type) {
        switch (type) {
            case 1 -> {return Fluids.WATER;}
            case 2 -> {return Fluids.LAVA;}
            case 3 -> {return RegisterFluids.STILL_HYDROGEN;}
            case 4 -> {return RegisterFluids.STILL_HELIUM;}
            default -> {return Fluids.EMPTY;}
        }
    }

    protected static int getCapsuleType(Fluid fluid) {
        if (fluid == Fluids.WATER) return 1;
        if (fluid == Fluids.LAVA) return 2;
        if (fluid == RegisterFluids.STILL_HYDROGEN) return 3;
        if (fluid == RegisterFluids.STILL_HELIUM) return 4;
        return 0;
    }

    private static String getFluidName(int type) {
        switch (type) {
            case 1 -> {return "water";}
            case 2 -> {return "lava";}
            case 3 -> {return "hydrogen";}
            case 4 -> {return "helium";}
            default -> {return "empty";}
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ComponentMap map = user.getStackInHand(hand).getComponents();
        int level = Objects.requireNonNull(map.get(CAPSULE_LEVEL));
        Fluid fluid = getCapsule(Objects.requireNonNull(map.get(CAPSULE_FLUID)));
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, level == 8 ? RaycastContext.FluidHandling.NONE : RaycastContext.FluidHandling.ANY);

        if (!world.isClient) {
            if (user.isGliding() && fluid instanceof HydrogenFluid) {
                if (user.isSpectator() || user.isInCreativeMode()) return ActionResult.FAIL;
                ItemUsage.exchangeStack(itemStack, user, CAPSULE.getDefaultStack());

                var rotation = user.getRotationVector();
                var currentVel = user.getVelocity();

                double boostMultiplier = 3;
                double baseBoost = 0.3;

                Vec3d newVelocity = new Vec3d(
                        currentVel.x + rotation.x * baseBoost + (rotation.x * boostMultiplier - currentVel.x) * level * 0.2,
                        currentVel.y + rotation.y * baseBoost + (rotation.y * boostMultiplier - currentVel.y) * level * 0.2,
                        currentVel.z + rotation.z * baseBoost + (rotation.z * boostMultiplier - currentVel.z) * level * 0.2
                );
                user.setVelocity(newVelocity);
                world.createExplosion(user, user.capeX, user.capeY, user.capeZ, 4, true, World.ExplosionSourceType.MOB);
                user.damage((ServerWorld) world, user.getDamageSources().explosion(user, user), 2);
                return ActionResult.SUCCESS_SERVER;
            }

            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return ActionResult.PASS;
            } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
                return ActionResult.PASS;
            } else {
                BlockPos blockPos = blockHitResult.getBlockPos();
                Direction direction = blockHitResult.getSide();
                BlockPos blockPos2 = blockPos.offset(direction);

                if (!world.canEntityModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack)) {
                    return ActionResult.FAIL;
                } else if (level != 8 && world.getFluidState(blockPos).getFluid() != Fluids.EMPTY) {
                    BlockState blockState = world.getBlockState(blockPos);
                    ItemStack itemStack2 = tryDrainFluid(itemStack, world, blockPos, blockState);
                    if (!itemStack2.isEmpty()) {
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                        ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, user, itemStack2);
                        Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity)user, itemStack2);
                        return ActionResult.SUCCESS.withNewHandStack(itemStack3);
                    }
                    return ActionResult.FAIL;
                } else {
                    BlockState blockState = world.getBlockState(blockPos);
                    BlockPos blockPos3 = blockState.getBlock() instanceof FluidFillable && fluid.getDefaultState().getBlockState().getBlock() == Blocks.WATER ? blockPos : blockPos2;
                    if (this.placeFluid(user, world, blockPos3, blockHitResult)) {
                        this.onEmptied(user, world, itemStack, blockPos3);
                        if (user instanceof ServerPlayerEntity) {
                            Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, blockPos3, itemStack);
                        }

                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, user, CAPSULE.getDefaultStack());
                        return ActionResult.SUCCESS.withNewHandStack(itemStack2);
                    } else {
                        return ActionResult.FAIL;
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    public ItemStack tryDrainFluid(@NotNull ItemStack itemStack, World world, BlockPos pos, @NotNull BlockState blockState) {
        ComponentMap map = itemStack.getComponents();
        int level = Objects.requireNonNull(map.get(CAPSULE_LEVEL));
        Fluid fluid = getCapsule(Objects.requireNonNull(map.get(CAPSULE_FLUID)));
        FluidState fluidBlock = blockState.getFluidState();
        if (fluidBlock == null) return ItemStack.EMPTY;
        Fluid fluid1 = ((FlowableFluid) fluidBlock.getFluid()).getStill();
        Fluid fluid2 = fluidBlock.getFluid();

        if (level == 8) return ItemStack.EMPTY;
        if (fluid != fluid1 && fluid != Fluids.EMPTY) return ItemStack.EMPTY;
        if ((fluid2 == Fluids.WATER || fluid2 == Fluids.LAVA) && fluidBlock.getLevel() == 8 && level == 0) {
            ItemStack newStack = itemStack.copyWithCount(1);
            newStack.set(CAPSULE_LEVEL, 8);
            newStack.set(CAPSULE_FLUID, getCapsuleType(fluid1));
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return newStack;
        }
        if (fluid1 instanceof Gas) {
            int l = fluidBlock.getLevel();
            int min = Math.min(8 - level, l);
            ItemStack newStack = itemStack.copyWithCount(1);
            newStack.set(CAPSULE_LEVEL, level + min);
            newStack.set(CAPSULE_FLUID, getCapsuleType(fluid1));
            if (l == min) world.setBlockState(pos, Blocks.AIR.getDefaultState());
            else world.setBlockState(pos, fluidBlock.getBlockState().with(LEVEL, 8 - (l - min)), Block.NOTIFY_ALL);
            return newStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean placeFluid(@Nullable LivingEntity user, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (user == null) return false;
        ComponentMap map = user.getMainHandStack().getComponents();
        int level = Objects.requireNonNull(map.get(CAPSULE_LEVEL));
        Fluid fluid = getCapsule(Objects.requireNonNull(map.get(CAPSULE_FLUID)));


        Block block;
        boolean bl;
        BlockState blockState;
        boolean var10000;
        label82: {
            blockState = world.getBlockState(pos);
            block = blockState.getBlock();
            bl = blockState.canBucketPlace(fluid);
            label70:
            if (!blockState.isAir() && !bl) {
                if (block instanceof FluidFillable fluidFillable && fluidFillable.canFillWithFluid(user, world, pos, blockState, fluid)) {
                    break label70;
                }
                var10000 = false;
                break label82;
            }
            var10000 = true;
        }

        boolean bl2 = var10000;
        if (!bl2) {
            return hitResult != null && this.placeFluid(user, world, hitResult.getBlockPos().offset(hitResult.getSide()), null);
        } else if (world.getDimension().ultrawarm() && fluid == Fluids.WATER) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            world.playSound(
                    user, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F
            );

            for (int l = 0; l < 8; l++) {
                world.addParticleClient(ParticleTypes.LARGE_SMOKE, i + Math.random(), j + Math.random(), k + Math.random(), 0, 0, 0);
            }
            return true;
        } else {
            if (block instanceof FluidFillable fluidFillable && fluid == Fluids.WATER) {
                fluidFillable.tryFillWithFluid(world, pos, blockState, fluid.getDefaultState().with(FlowableFluid.LEVEL, level));
                this.playEmptyingSound(user, world, pos);
                return true;
            }
            if (!world.isClient && bl && !blockState.isLiquid()) {
                world.breakBlock(pos, true);
            }

            if (!world.setBlockState(pos, Objects.requireNonNull(map.get(CAPSULE_FLUID)) > 2 ? fluid.getDefaultState().getBlockState().with(LEVEL, level == 8 ? 8 : 8 - level) : fluid.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW) && !blockState.getFluidState().isStill()) {
                return false;
            } else {
                this.playEmptyingSound(user, world, pos);
                return true;
            }
        }
    }

    protected void playEmptyingSound(@Nullable LivingEntity user, @NotNull WorldAccess world, BlockPos pos) {
        SoundEvent soundEvent = getCapsule(Objects.requireNonNull(this.getComponents().get(CAPSULE_FLUID))).isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(user, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(user, GameEvent.FLUID_PLACE, pos);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer level = stack.get(CAPSULE_LEVEL);
        Integer fluid = stack.get(CAPSULE_FLUID);
        if (level == null || fluid == null) {return;}

        String name = getFluidName(fluid);
        if (name.equals("empty")) {
            textConsumer.accept(Text.translatable("tooltip.radik.capsule.empty"));
            super.appendTooltip(stack, context, displayComponent, textConsumer, type);
            return;
        }

        textConsumer.accept(Text.translatable("tooltip.radik.capsule." + name));
        textConsumer.accept(Text.of(Text.translatable("tooltip.radik.capsule.level").append(String.valueOf(level))));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    private boolean canDrain(World world, BlockPos pos, @NotNull BlockState state) {
        Block block = state.getBlock();
        ComponentMap map = this.getComponents();
        int level = Objects.requireNonNull(map.get(CAPSULE_LEVEL));
        Fluid fluid = getCapsule(Objects.requireNonNull(map.get(CAPSULE_FLUID)));

        if (block instanceof FluidBlock) {
            if ((block == Blocks.WATER || block == Blocks.LAVA) && fluid instanceof Gas) { return false; }
        }
        else {
            return true;
        }
        return false;
    }
}