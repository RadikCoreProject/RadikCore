package com.radik.behavior;

import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class FallingBlockDispenserBehavior extends ItemDispenserBehavior {
    private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();
    private final Block fallingBlock;

    public FallingBlockDispenserBehavior(Block fallingBlock) {
        if (!(fallingBlock instanceof FallingBlock))
            throw new IllegalArgumentException("fallingBlock must be an instance of FallingBlock");
        this.fallingBlock = fallingBlock;
    }

    public ItemStack dispenseSilently(@NotNull BlockPointer pointer, ItemStack stack) {
        ServerWorld serverWorld = pointer.world();
        Vec3d vec3d = pointer.centerPos();

        BlockPos pos = BlockPos.ofFloored(vec3d);

        if (!serverWorld.getBlockState(pos).isAir() || !serverWorld.canPlace(serverWorld.getBlockState(pos), pos, ShapeContext.absent())) {
            return this.fallbackBehavior.dispense(pointer, stack);
        }

        if (serverWorld.setBlockState(pos, fallingBlock.getDefaultState())) {
            stack.decrement(1);
            return stack;
        }

        return this.fallbackBehavior.dispense(pointer, stack);
    }
}
