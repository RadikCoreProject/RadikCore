package com.radik.logic;

import com.radik.block.RegisterBlocks;
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
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.radik.Radik.*;
import static com.radik.block.RegisterBlocks.*;
import static com.radik.block.custom.BlockData.*;
import static com.radik.commands.MinigamesCommand.MINIGAME;

public class OnUse {
    private static final ArrayList<Block> ROTATED_BLOCKS = new ArrayList<>();
    private static final ArrayList<Block> ROTATED_BLOCKS2 = new ArrayList<>();
    private static final HashMap<Item, Block> DOWNFALLED_BLOCKS = new HashMap<>();
    private static final HashMap<Item, Block> BLOCKITEMS = new HashMap<>();
    private static final HashMap<Item, String> EQUIPPABLE = new HashMap<>();

    private static final HashMap<Integer, String[]> PIX = new HashMap<>();

    static {
        PIX.put(30, new String[]{"radik:present_small", "1"});
        PIX.put(200, new String[]{"radik:present_small", "4"});
        PIX.put(600, new String[]{"radik:present_medium", "2"});
        PIX.put(1000, new String[]{"radik:present_big", "1"});
        PIX.put(2500, new String[]{"radik:present_big", "2"});
        PIX.put(6000, new String[]{"radik:present_big", "3"});
        PIX.put(10000, new String[]{"radik:present_winter", "10"});
        PIX.put(15000, new String[]{"radik:present_winter", "2"});
        PIX.put(25000, new String[]{"radik:present_winter", "5"});

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

        ROTATED_BLOCKS.add(SNOWMAN);
        ROTATED_BLOCKS.add(LEDENETS);
        ROTATED_BLOCKS.add(LEDENETS1);
        ROTATED_BLOCKS.add(LEDENETS2);
        ROTATED_BLOCKS.add(FONAR_LAMP20);

        ROTATED_BLOCKS2.add(FONAR_LAMP2);
        ROTATED_BLOCKS2.add(FONAR_LAMP3);
        ROTATED_BLOCKS2.add(FONAR_LAMP12);
        ROTATED_BLOCKS2.add(FONAR_LAMP13);

        DOWNFALLED_BLOCKS.put(PRESENT_BIG.asItem(), PRESENT_BIG);
        DOWNFALLED_BLOCKS.put(ELKA.asItem(), ELKA);
        DOWNFALLED_BLOCKS.put(RegisterBlocks.PIX.asItem(), RegisterBlocks.PIX);

        BLOCKITEMS.put(RegisterItems.LEDENETS, LEDENETS);
        BLOCKITEMS.put(RegisterItems.LEDENETS1, LEDENETS1);
        BLOCKITEMS.put(RegisterItems.LEDENETS2, LEDENETS2);
    }

    protected static void register() {
        UseItemCallback.EVENT.register(OnUse::onItemUse);
        UseBlockCallback.EVENT.register(OnUse::onBlockUse);
    }

    private static TypedActionResult<ItemStack> onItemUse(@NotNull PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();
        Inventory inventory = player.getInventory();

        if (EQUIPPABLE.containsKey(item)) {
            switch (EQUIPPABLE.get(item)) {
                case "head":
                    if (inventory.getStack(39).getItem().equals(Items.AIR)) {
                        command(String.format("item replace entity %s armor.head with %s", player.getName().getString(), item));
                        stack.decrement(1);
                        return TypedActionResult.success(stack);
                    }
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    private static ActionResult onBlockUse(@NotNull PlayerEntity player, @NotNull World world, Hand hand, @NotNull BlockHitResult blockHitResult) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        String player_name = player.getName().getString();
        String block_name = block.getName().toString().split("'")[1].split("\\.")[2];
        String item_name = item.getName().toString();
        int x = blockPos.getX(), y = blockPos.getY(), z = blockPos.getZ();

        if(MINIGAME && block.equals(Blocks.CHEST)) {
            command(String.format("fill %d %d %d %d %d %d air destroy", x, y, z, x, y, z));
        }

        if (player.getAbilities().allowFlying && !player.getAbilities().creativeMode && !player.isSpectator()) {
            player.sendMessage(Text.literal("Ты находишься в режиме оценивания!"));
            return ActionResult.SUCCESS;
        }

        if(block_name.equals("bicycle")) {
            happyBirhday(player_name, x, y, z);
            return ActionResult.SUCCESS;
        }

        if(block_name.equals("pix")) {
            pix(player);
            return ActionResult.SUCCESS;
        }


        else if (ROTATED_BLOCKS.contains(world.getBlockState(blockPos.up()).getBlock()) || ROTATED_BLOCKS2.contains(world.getBlockState(blockPos.up()).getBlock())) { return ActionResult.SUCCESS_NO_ITEM_USED; }

        else if (ROTATED_BLOCKS.contains(block)) {
            world.setBlockState(blockPos, state.with(FACING2, (state.get(FACING2) + 1) % 4));
            return ActionResult.SUCCESS;
        }

        else if (DOWNFALLED_BLOCKS.containsKey(item)) {
            if (!world.isAir(blockPos.up(2)) || !world.isAir(blockPos.up())) { return ActionResult.SUCCESS_NO_ITEM_USED; }
            world.setBlockState(blockPos.up(2), DOWNFALLED_BLOCKS.get(item).getDefaultState());
            player.getStackInHand(hand).decrement(1);
            return ActionResult.SUCCESS;
        }

        else if (ROTATED_BLOCKS2.contains(block)) {
            if (!world.isClient) { return ActionResult.SUCCESS_NO_ITEM_USED; }
            world.setBlockState(blockPos, state.with(FACING, !state.get(FACING)));
            return ActionResult.SUCCESS;
        }

        else if (BLOCKITEMS.containsKey(item) && world.isAir(blockPos.up())) {
            Random random = new Random();
            int a = random.nextInt(1, 72);
            if (item_name.contains("ledenets")) {
                world.setBlockState(blockPos.offset(blockHitResult.getSide()), BLOCKITEMS.get(item).getDefaultState().with(TYPE2, a));
            }
            player.getStackInHand(hand).decrement(1);
            return ActionResult.PASS;
        }

        else if(item.equals(Items.BONE_MEAL) && block.equals(Blocks.SUGAR_CANE)) {
            if(world.getBlockState(blockPos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(blockPos.down(2)).getBlock().equals(Blocks.SUGAR_CANE)) {
                return ActionResult.PASS;
            }
            if(world.getBlockState(blockPos.down(1)).getBlock().equals(Blocks.SUGAR_CANE) && world.getBlockState(blockPos.up(1)).isAir()) {
                world.setBlockState(blockPos.up(), Blocks.SUGAR_CANE.getDefaultState());
                stack.decrement(1);
                onGrow(x, y, z);
                return ActionResult.SUCCESS;
            }
            if(world.getBlockState(blockPos.up(1)).isAir() && world.getBlockState(blockPos.up(2)).isAir()) {
                world.setBlockState(blockPos.up(), Blocks.SUGAR_CANE.getDefaultState());
                world.setBlockState(blockPos.up(2), Blocks.SUGAR_CANE.getDefaultState());
                stack.decrement(1);
                onGrow(x, y, z);
                return ActionResult.SUCCESS;
            }
            if(world.getBlockState(blockPos.up(1)).isAir()) {
                world.setBlockState(blockPos.up(), Blocks.SUGAR_CANE.getDefaultState());
                stack.decrement(1);
                onGrow(x, y, z);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private static void pix(@NotNull PlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        if(!Arrays.stream(SERVER.getPlayerNames()).toList().contains("catgirl_cute777")) { return; }
        int c = player.getScoreboard().getOrCreateScore(ScoreHolder.fromName("PIX"), scoreboard.getNullableObjective("PIX")).incrementScore();
        for(int i: PIX.keySet()) {
            if(c == i) {
                command(String.format("give catgirl_cute777 %s %s", PIX.get(i)[0], PIX.get(i)[1]));
                command("title catgirl_cute777 title \"С ДНЁМ РОЖДЕНИЯ, catgirl_cute777!\"");
            }
            else if(c < i) {
                player.sendMessage(Text.literal(String.format("%d / %s", c, i)));
            }
        }
    }

    private static void onGrow(int x, int y, int z) {
        command("particle minecraft:composter " + (x-0.5) + " " + (y+1) + " " + (z-0.5) + " 0.5 0.5 0.5 10 10 force");
    }

    private static void happyBirhday(@NotNull String name, int x, int y, int z) {
        if(name.equals("Boomboxcuff")) {
            command("title Boomboxcuff title \"С ДНЁМ РОЖДЕНИЯ!\"");
            command(String.format("particle minecraft:composter %f %d %f 2 2 2 100 10 force", x-0.5, y+1, z-0.5));
        }
        else {
            for (ServerPlayerEntity p : SERVER.getPlayerManager().getPlayerList()) {
                p.sendMessage(Text.literal("<" + name + ">" + " С днём рождения, Дима!"), false);
            }
        }
    }
}
