package com.radik.behavior;

import com.radik.fluid.RegisterFluids;
import com.radik.item.RegisterItems;
import com.radik.item.custom.Capsule;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.radik.Data.CAPSULE_FLUID;
import static com.radik.Data.CAPSULE_LEVEL;
import static net.minecraft.block.FluidBlock.LEVEL;

public class CapsuleDispenserBehavior extends ItemDispenserBehavior {
    private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

    private static Fluid getCapsule(int type) {
        return switch (type) {
            case 1 -> Fluids.WATER;
            case 2 -> Fluids.LAVA;
            case 3 -> RegisterFluids.STILL_HYDROGEN;
            case 4 -> RegisterFluids.STILL_HELIUM;
            default -> Fluids.EMPTY;
        };
    }

    public ItemStack dispenseSilently(BlockPointer pointer, @NotNull ItemStack stack) {
        if (!(stack.getItem() instanceof Capsule capsule)) return this.fallbackBehavior.dispense(pointer, stack);
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        ServerWorld serverWorld = pointer.world();
        Vec3d vec3d = pointer.centerPos();
        ItemStack capsuleStack = RegisterItems.CAPSULE.getDefaultStack();
        BlockPos pos = BlockPos.ofFloored(vec3d).offset(direction);
        ComponentMap map = stack.getComponents();
        Fluid fluid = getCapsule(Objects.requireNonNull(map.get(CAPSULE_FLUID)));
        int level = Objects.requireNonNull(map.get(CAPSULE_LEVEL));
        BlockState blockState = serverWorld.getBlockState(pos);

        if (fluid == null || fluid == Fluids.EMPTY) {
            if (blockState.getBlock().equals(Blocks.LAVA_CAULDRON)) {
                serverWorld.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                capsuleStack.set(CAPSULE_FLUID, 2);
                capsuleStack.set(CAPSULE_LEVEL, 8);
                return this.decrementStackWithRemainder(pointer, stack, capsuleStack);
            } else if (blockState.getBlock().equals(Blocks.WATER_CAULDRON) && blockState.get(LeveledCauldronBlock.LEVEL) == 3) {
                serverWorld.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                capsuleStack.set(CAPSULE_FLUID, 1);
                capsuleStack.set(CAPSULE_LEVEL, 8);
                return this.decrementStackWithRemainder(pointer, stack, capsuleStack);
            } else {
                capsuleStack = capsule.tryDrainFluid(stack, serverWorld, pos, blockState);
                if (capsuleStack.get(CAPSULE_FLUID) == null) return this.fallbackBehavior.dispense(pointer, stack);
                return this.decrementStackWithRemainder(pointer, stack, capsuleStack);
            }
        }

        FluidState fluidState = blockState.getFluidState();
        Fluid fluid2 = fluidState.getFluid();

        if (fluid2 != null && fluid == fluid2) {
            int level2 = fluid2.getLevel(fluidState);

            if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
                if (level < 8) {
                    serverWorld.setBlockState(pos, Blocks.AIR.getDefaultState());
                    stack.set(CAPSULE_LEVEL, 8);
                    return this.decrementStackWithRemainder(pointer, stack, new ItemStack(RegisterItems.CAPSULE));
                }
                return this.fallbackBehavior.dispense(pointer, stack);
            }
            else {
                if ((level + level2) < 8) return this.fallbackBehavior.dispense(pointer, stack);
                stack.set(CAPSULE_LEVEL, level + level2);
                serverWorld.setBlockState(pos, fluidState.with(LEVEL, level2 - (8 - level)).getBlockState());
                return this.decrementStackWithRemainder(pointer, stack, new ItemStack(RegisterItems.CAPSULE));
            }
        }

        if (!blockState.isAir() || !serverWorld.canPlace(blockState, pos, ShapeContext.absent())) {
            return this.fallbackBehavior.dispense(pointer, stack);
        }

        // размещаем воду
        LivingEntity user = new LivingEntity(EntityType.BAT, serverWorld) {
            @Contract(pure = true)
            @Override
            public @Nullable Arm getMainArm() {
                return null;
            }
        };

        user.equipStack(EquipmentSlot.MAINHAND, stack);

        if (capsule.placeFluid(user, serverWorld, pos, new BlockHitResult(vec3d, direction, pos, false))) {
            capsule.onEmptied(user, serverWorld, stack, pos);
            user.kill(serverWorld);
            return this.decrementStackWithRemainder(pointer, stack, new ItemStack(RegisterItems.CAPSULE));
        }
        user.kill(serverWorld);
        return this.fallbackBehavior.dispense(pointer, stack);
    }
}