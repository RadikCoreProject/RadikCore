package com.radik.logic;

import com.radik.Radik;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.radik.Data.*;
import static com.radik.MixinData.DOWNFALLED_BLOCKS;

public class OnUse {

    protected static void register() {
        UseBlockCallback.EVENT.register(OnUse::onBlockUse);
    }

    private static ActionResult onBlockUse(@NotNull PlayerEntity player, @NotNull World world, Hand hand, @NotNull BlockHitResult blockHitResult) {
        ItemStack stack = player.getStackInHand(hand);
        BlockPos pos = blockHitResult.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        Item item = stack.getItem();

        if (!world.isClient()) {
            if (item.equals(Items.BONE_MEAL) && block.equals(Blocks.SUGAR_CANE)) {
                if (world.getBlockState(pos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(pos.down(2)).getBlock().equals(Blocks.SUGAR_CANE)) {
                    return ActionResult.FAIL;
                } else if (world.getBlockState(pos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(pos.up(1)).isAir()) {
                    world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrementUnlessCreative(1, player);
                    onGrow(x, y, z);
                } else if (world.getBlockState(pos.up(1)).isAir() && world.getBlockState(pos.up(2)).isAir()) {
                    world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    world.setBlockState(pos.up(2), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrementUnlessCreative(1, player);
                    onGrow(x, y, z);
                } else if (world.getBlockState(pos.up(1)).isAir()) {
                    world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrementUnlessCreative(1, player);
                    onGrow(x, y, z);
                }
            } else if (DOWNFALLED_BLOCKS.containsKey(item)) {
                Radik.LOGGER.error(world.getBlockState(pos.up()) + " " + world.getBlockState(pos.up(2)));
                if (world.isAir(pos.up(2)) && world.isAir(pos.up()) && blockHitResult.getSide() == Direction.UP) {
                    world.setBlockState(pos.up(2), DOWNFALLED_BLOCKS.get(item).getDefaultState());
                    player.getStackInHand(hand).decrementUnlessCreative(1, player);
                }
                return ActionResult.FAIL;
            }
        } else {
            if (DOWNFALLED_BLOCKS.containsKey(item)) {
                if (!(world.isAir(pos.up(2)) && world.isAir(pos.up()) && blockHitResult.getSide() == Direction.UP)) {
                    return ActionResult.FAIL;
                }
            }
        }
        return ActionResult.PASS;
    }

    private static void onGrow(int x, int y, int z) {
        command("particle minecraft:composter " + (x) + " " + (y+1) + " " + (z) + " 0.5 0.5 0.5 10 10 force");
    }
}
