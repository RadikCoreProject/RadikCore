package com.radik.behavior;

import com.radik.fluid.RegisterFluids;
import com.radik.item.custom.Capsule;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.ShapeContext;
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

import java.io.IOException;
import java.util.Objects;

import static com.radik.Data.CAPSULE_FLUID;
import static com.radik.Data.CAPSULE_LEVEL;
import static net.minecraft.block.FluidBlock.LEVEL;

public class CapsuleDispenserBehavior extends ItemDispenserBehavior {
    private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

    private static Fluid getCapsule(int type) {
        switch (type) {
            case 1 -> {return Fluids.WATER;}
            case 2 -> {return Fluids.LAVA;}
            case 3 -> {return RegisterFluids.STILL_HYDROGEN;}
            case 4 -> {return RegisterFluids.STILL_HELIUM;}
            default -> {return Fluids.EMPTY;}
        }
    }

    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        if (!(stack.getItem() instanceof Capsule capsule)) return this.fallbackBehavior.dispense(pointer, stack);
        Direction direction = pointer.state().get(DispenserBlock.FACING);
        ServerWorld serverWorld = pointer.world();
        //try (ServerWorld serverWorld = pointer.world()) {
            Vec3d vec3d = pointer.centerPos();

            BlockPos pos = BlockPos.ofFloored(vec3d).offset(direction);

            ComponentMap map = stack.getComponents();
            Fluid fluid = getCapsule(Objects.requireNonNull(map.get(CAPSULE_FLUID)));
            int level = Objects.requireNonNull(map.get(CAPSULE_LEVEL));
            BlockState blockState = serverWorld.getBlockState(pos);

            if (fluid == null || fluid == Fluids.EMPTY) {
                stack = capsule.tryDrainFluid(stack, serverWorld, pos, blockState);
                return stack;
            }

            FluidState fluidState = blockState.getFluidState();
            Fluid fluid2 = fluidState.getFluid();

            if (fluid2 != null && fluid == fluid2) {
                int level2 = fluid2.getLevel(fluidState);
                if ((level + level2) < 8) {
                    return stack;
                }
                stack.set(CAPSULE_LEVEL, level + level2);
                serverWorld.setBlockState(pos, fluidState.with(LEVEL, level2 - (8 - level)).getBlockState());
                return stack;
            }

            if (!serverWorld.getBlockState(pos).isAir() || !serverWorld.canPlace(serverWorld.getBlockState(pos), pos, ShapeContext.absent())) {
                return this.fallbackBehavior.dispense(pointer, stack);
            }

            LivingEntity user = new LivingEntity(EntityType.BAT, serverWorld) {
                @Override
                public Arm getMainArm() {
                    return null; // просто пустой метод шоб обойти ошибку
                }
            };

            user.equipStack(EquipmentSlot.MAINHAND, stack);

            if (capsule.placeFluid(user, serverWorld, pos, new BlockHitResult(vec3d, direction, pos, false))) {
                capsule.onEmptied(user, serverWorld, stack, pos);
                user.kill(serverWorld);
                stack = capsule.getDefaultStack();
                return stack;
            }
            user.kill(serverWorld);
        //} catch (IOException e) { LOGGER.error(e.getMessage()); }
        return this.fallbackBehavior.dispense(pointer, stack);
    }
}
