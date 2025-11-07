package com.radik.item.custom;

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

import static com.radik.Data.EVENT_TYPE;

public class LuckyBasket extends EventItem {
    private static final HashMap<Item, Integer> rewards = new HashMap<>();
    private static final Random random = new Random();
    static {
        rewards.put(RegisterItems.CANDY_GREEN, 51);
        rewards.put(RegisterItems.CANDY_BLUE, 51);
        rewards.put(RegisterItems.CANDY_RED, 51);
        rewards.put(RegisterItems.CANDY_YELLOW, 51);
        rewards.put(RegisterItems.CANDY_BASKET_EMPTY, 6);
    }

    public LuckyBasket(@NotNull Item.Settings settings) {
        super(settings.maxCount(99).component(EVENT_TYPE, ChallengeEvent.HALLOWEEN));
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        Item item = rewards.keySet().toArray(new Item[5])[random.nextInt(5)];
        ItemEntity stack = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), new ItemStack(item, random.nextInt(1, rewards.get(item))));
        world.spawnEntity(stack);
        user.getStackInHand(hand).decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }
}
