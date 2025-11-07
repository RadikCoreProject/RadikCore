package com.radik.mixin;

import com.radik.block.custom.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.radik.Data.*;
import static com.radik.MixinData.*;
import static com.radik.block.custom.BlockData.FACING2;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow @Final private Block block;

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("RETURN"), cancellable = true)
    private void onBlockPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (player == null || world == null) return;
        Hand hand = context.getHand();
        ItemStack stack = player.getStackInHand(hand);
        BlockState state = ((BlockItem)(Object)this).getBlock().getDefaultState();
        BlockPos pos = context.getBlockPos();
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        Item item = stack.getItem();

        if (!world.isClient()) {
            if (block instanceof RotatableBlock) {
                world.setBlockState(pos, state.with(FACING2, (state.get(FACING2) + 1) % 4));
            } else if (DOWNFALLED_BLOCKS.containsKey(item)) {
                if (!world.isAir(pos.up(2)) || !world.isAir(pos.up())) {
                    return;
                }
                world.setBlockState(pos.up(2), DOWNFALLED_BLOCKS.get(item).getDefaultState());
                player.getStackInHand(hand).decrementUnlessCreative(1, player);
            } else if (item.equals(Items.BONE_MEAL) && block.equals(Blocks.SUGAR_CANE)) {
                if (world.getBlockState(pos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(pos.down(2)).getBlock().equals(Blocks.SUGAR_CANE)) {
                    return;
                }
                if (world.getBlockState(pos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(pos.up(1)).isAir()) {
                    world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrement(1);
                    onGrow(x, y, z);
                    return;
                }
                if (world.getBlockState(pos.up(1)).isAir() && world.getBlockState(pos.up(2)).isAir()) {
                    world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    world.setBlockState(pos.up(2), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrement(1);
                    onGrow(x, y, z);
                    return;
                }
                if (world.getBlockState(pos.up(1)).isAir()) {
                    world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrement(1);
                    onGrow(x, y, z);
                }
            }
        }
    }

    @Unique
    private static void onGrow(int x, int y, int z) {
        command("particle minecraft:composter " + (x-0.5) + " " + (y+1) + " " + (z-0.5) + " 0.5 0.5 0.5 10 10 force");
    }
}
