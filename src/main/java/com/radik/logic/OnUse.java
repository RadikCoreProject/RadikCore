package com.radik.logic;

import com.radik.block.RegisterBlocks;
import com.radik.block.custom.RotatableBlock;
import com.radik.item.RegisterItems;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.radik.Data.*;
import static com.radik.block.RegisterBlocks.*;
import static com.radik.block.custom.BlockData.*;

public class OnUse {
    private static final HashMap<Item, Block> DOWNFALLED_BLOCKS = new HashMap<>();
    private static final HashMap<Item, Block> BLOCKITEMS = new HashMap<>();
    private static final HashMap<Item, String> EQUIPPABLE = new HashMap<>();

    static {
        DOWNFALLED_BLOCKS.put(ELKA.asItem(), ELKA);
        DOWNFALLED_BLOCKS.put(RegisterBlocks.PIX.asItem(), RegisterBlocks.PIX);

        EQUIPPABLE.put(RegisterItems.SCARF_1, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_2, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_3, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_4, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_5, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_6, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_7, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_8, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_9, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_10, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_11, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_12, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_13, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_14, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_15, "head");
        EQUIPPABLE.put(RegisterItems.SCARF_16, "head");
        EQUIPPABLE.put(RegisterItems.ADVENTURE_HAT, "head");
        EQUIPPABLE.put(RegisterItems.TASHERS_CRONE, "head");
        EQUIPPABLE.put(RegisterItems.ADVENTURE_1M_HAT, "head");
        EQUIPPABLE.put(JAVA_PROGRAMMER.asItem(), "head");

        BLOCKITEMS.put(RegisterItems.LEDENETS, LEDENETS);
        BLOCKITEMS.put(RegisterItems.LEDENETS1, LEDENETS1);
        BLOCKITEMS.put(RegisterItems.LEDENETS2, LEDENETS2);
    }

    protected static void register() {
        UseItemCallback.EVENT.register(OnUse::onItemUse);
        UseBlockCallback.EVENT.register(OnUse::onBlockUse);
    }

    private static ActionResult onItemUse(@NotNull PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();
        Inventory inventory = player.getInventory();

        if (player.isSpectator()) return ActionResult.PASS;

        if (!world.isClient) {
            if (EQUIPPABLE.containsKey(item)) {
                if (inventory.getStack(39).getItem().equals(Items.AIR)) {
                    command(String.format("item replace entity %s armor.head with %s", player.getName().getString(), item));
                    stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
                return ActionResult.FAIL;
            }
        }

        return ActionResult.PASS;
    }

    // TODO: пофиксить серверсайд баг с использованием дважды при повороте
    private static ActionResult onBlockUse(@NotNull PlayerEntity player, @NotNull World world, Hand hand, @NotNull BlockHitResult blockHitResult) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        String item_name = item.getName().toString();
        int x = blockPos.getX(), y = blockPos.getY(), z = blockPos.getZ();
        
        if (!world.isClient) {
            if (player.getAbilities().allowFlying && !player.getAbilities().creativeMode && !player.isSpectator()) {
                player.sendMessage(Text.literal("Ты находишься в режиме оценивания!"), false);
                return ActionResult.FAIL;
            }

            if (block instanceof RotatableBlock) {
                world.setBlockState(blockPos, state.with(FACING2, (state.get(FACING2) + 1) % 4));
                return ActionResult.FAIL;
            } else if (DOWNFALLED_BLOCKS.containsKey(item)) {
                if (!world.isAir(blockPos.up(2)) || !world.isAir(blockPos.up())) {
                    return ActionResult.FAIL;
                }
                world.setBlockState(blockPos.up(2), DOWNFALLED_BLOCKS.get(item).getDefaultState());
                player.getStackInHand(hand).decrementUnlessCreative(1, player);
                return ActionResult.SUCCESS_SERVER;
            } else if (BLOCKITEMS.containsKey(item) && world.isAir(blockPos.up())) {
                Random random = new Random();
                int a = random.nextInt(1, 72);
                if (item_name.contains("ledenets")) {
                    world.setBlockState(blockPos.offset(blockHitResult.getSide()), BLOCKITEMS.get(item).getDefaultState().with(TYPE2, a).with(FACING2, Math.abs(player.getFacing().getHorizontalQuarterTurns())));
                }
                player.getStackInHand(hand).decrementUnlessCreative(1, player);
                return ActionResult.PASS;
            } else if (item.equals(Items.BONE_MEAL) && block.equals(Blocks.SUGAR_CANE)) {
                if (world.getBlockState(blockPos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(blockPos.down(2)).getBlock().equals(Blocks.SUGAR_CANE)) {
                    return ActionResult.PASS;
                }
                if (world.getBlockState(blockPos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(blockPos.up(1)).isAir()) {
                    world.setBlockState(blockPos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrement(1);
                    onGrow(x, y, z);
                    return ActionResult.SUCCESS_SERVER;
                }
                if (world.getBlockState(blockPos.up(1)).isAir() && world.getBlockState(blockPos.up(2)).isAir()) {
                    world.setBlockState(blockPos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    world.setBlockState(blockPos.up(2), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrement(1);
                    onGrow(x, y, z);
                    return ActionResult.SUCCESS_SERVER;
                }
                if (world.getBlockState(blockPos.up(1)).isAir()) {
                    world.setBlockState(blockPos.up(), Blocks.SUGAR_CANE.getDefaultState());
                    stack.decrement(1);
                    onGrow(x, y, z);
                    return ActionResult.SUCCESS_SERVER;
                }
            }
        }
        return ActionResult.PASS;
    }

    private static void onGrow(int x, int y, int z) {
        command("particle minecraft:composter " + (x-0.5) + " " + (y+1) + " " + (z-0.5) + " 0.5 0.5 0.5 10 10 force");
    }
}
