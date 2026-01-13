package com.radik.item.custom;

import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;

public class LuckyBasket extends EventItem {
    private static final HashMap<Item, Integer> rewards = new HashMap<>();
    private static final Random random = new Random();
    static {
        rewards.put(RegisterItems.SNOWFLAKE, 99);
        rewards.put(RegisterItems.ICE_SHARD, 40);
        rewards.put(RegisterItems.CHRISTMAS_BALLS, 60);
        rewards.put(RegisterItems.TOMATO, 20);
        rewards.put(RegisterItems.CHAMPAGNE, 3);
        rewards.put(RegisterItems.RED_WINE, 3);
        rewards.put(RegisterItems.SALAD, 10);
        rewards.put(RegisterBlocks.GARLAND.asItem(), 5);
        rewards.put(RegisterItems.ORANGE, 50);
    }

    public LuckyBasket(@NotNull Item.Settings settings) {
        super(settings.maxCount(99), ChallengeEvent.HALLOWEEN);
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        Item item = rewards.keySet().toArray(new Item[9])[random.nextInt(9)];
        ItemEntity stack = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), new ItemStack(item, random.nextInt(1, rewards.get(item) + 1)));
        world.spawnEntity(stack);
        user.getStackInHand(hand).decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }
}
