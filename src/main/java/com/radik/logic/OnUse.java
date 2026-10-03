package com.radik.logic;

import com.radik.block.custom.blockentity.storage.StorageBlock;
import com.radik.block.custom.blockentity.storage.StorageBlockEntity;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
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
        AttackBlockCallback.EVENT.register(OnUse::blockAttack);
    }

    private static ActionResult blockAttack(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);

        if (!world.isClient()) {
            if (world.getBlockEntity(pos) instanceof StorageBlockEntity sbe) return storageBlock(sbe, state, world, player);
        }
        return ActionResult.PASS;
    }

    private static ActionResult onBlockUse(@NotNull PlayerEntity player, @NotNull World world, Hand hand, @NotNull BlockHitResult blockHitResult) {
        ItemStack stack = player.getStackInHand(hand);
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        Item item = stack.getItem();

        if (!world.isClient()) {
            if (item.equals(Items.BONE_MEAL) && block.equals(Blocks.SUGAR_CANE)) return sugarcaneGrow(world, pos, x, y, z, player, stack);
            else if (DOWNFALLED_BLOCKS.containsKey(item)) return downfalledBlock(world, pos, blockHitResult, item, player, hand);
            else if (world.getBlockEntity(pos) instanceof StorageBlockEntity sbe) return storageBlock(sbe, state, player, stack);
        } else {
            if (DOWNFALLED_BLOCKS.containsKey(item)) return downfalledBlockClient(world, pos, blockHitResult);
        }
        return ActionResult.PASS;
    }

    private static ActionResult storageBlock(StorageBlockEntity sbe, BlockState state, World world, PlayerEntity player) {
        if ((!player.getName().getString().equals(sbe.owner)) ||
        (player.getHorizontalFacing().getOpposite() != state.get(StorageBlock.FACING)) ||
        (sbe.filling == 0 || sbe.type.isEmpty())) return ActionResult.PASS;

        BlockPos pos = player.getBlockPos();
        int cou = Math.min(sbe.filling, player.isSneaking() ? 64 : 1);
        ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), sbe.type.copyWithCount(cou));
        if (cou == sbe.filling) sbe.setType(ItemStack.EMPTY);
        sbe.setFilling(sbe.filling - cou);
        world.spawnEntity(item);
        return ActionResult.PASS;
    }

    private static ActionResult storageBlock(StorageBlockEntity sbe, BlockState state, PlayerEntity player, ItemStack stack) {
        if (!player.getName().getString().equals(sbe.owner) ||
            player.getHorizontalFacing().getOpposite() != state.get(StorageBlock.FACING) ||
            sbe.filling == sbe.storage) return ActionResult.PASS;

        boolean b = player.isSneaking();
        boolean c = sbe.filling == 0;
        int cou;
        PlayerInventory inventory = player.getInventory();
        if (c) {
            cou = Math.min(sbe.storage, b ?
                itemsCount(inventory, stack) :
                stack.getCount());
            sbe.setType(stack.copy());
        } else {
            cou = Math.min(sbe.storage - sbe.filling, b ?
                itemsCount(inventory, sbe.type) :
                ItemStack.areItemsAndComponentsEqual(sbe.type, stack) ? stack.getCount() : 0);
        }

        if (cou == 0) return ActionResult.PASS;
        if (b) {
            if (deductItems(inventory, sbe.type, cou)) sbe.setFilling(sbe.filling + cou);
        } else {
            stack.decrement(cou);
            sbe.setFilling(sbe.filling + cou);
        }
        return ActionResult.SUCCESS;
    }

    private static ActionResult downfalledBlockClient(World world, BlockPos pos, BlockHitResult blockHitResult) {
        if (!(world.isAir(pos.up(2)) && world.isAir(pos.up()) && blockHitResult.getSide() == Direction.UP)) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    private static ActionResult downfalledBlock(World world, BlockPos pos, BlockHitResult blockHitResult, Item item, PlayerEntity player, Hand hand) {
        if (world.isAir(pos.up(2)) && world.isAir(pos.up()) && blockHitResult.getSide() == Direction.UP) {
            world.setBlockState(pos.up(2), DOWNFALLED_BLOCKS.get(item).getDefaultState());
            player.getStackInHand(hand).decrementUnlessCreative(1, player);
        }
        return ActionResult.FAIL;
    }

    private static void onGrow(int x, int y, int z) {
        command("particle minecraft:composter " + (x) + " " + (y+1) + " " + (z) + " 0.5 0.5 0.5 10 10 force");
    }

    private static ActionResult sugarcaneGrow(World world, BlockPos pos, int x, int y, int z, PlayerEntity player, ItemStack stack) {
        if (world.getBlockState(pos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(pos.down(2)).getBlock().equals(Blocks.SUGAR_CANE)) {
            return ActionResult.FAIL;
        } else if (world.getBlockState(pos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(pos.up(1)).isAir()) {
            world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
            stack.decrementUnlessCreative(1, player);
            onGrow(x, y, z);
            return ActionResult.PASS;
        } else if (world.getBlockState(pos.up(1)).isAir() && world.getBlockState(pos.up(2)).isAir()) {
            world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
            world.setBlockState(pos.up(2), Blocks.SUGAR_CANE.getDefaultState());
            stack.decrementUnlessCreative(1, player);
            onGrow(x, y, z);
            return ActionResult.PASS;
        } else if (world.getBlockState(pos.up(1)).isAir()) {
            world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState());
            stack.decrementUnlessCreative(1, player);
            onGrow(x, y, z);
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }
}
