package com.radik.connecting.event.factory;

import com.radik.connecting.event.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EventFactory {
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Event<BlockEventData> createBlockEvent(ChallengeTime time, ChallengeType type, ChallengeEvent event, Block block, ItemStack reward, int count) {
        return new Event<>(time, type, event, new BlockEventData(block), reward, count);
    }
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Event<ItemEventData> createItemEvent(ChallengeTime time, ChallengeType type, ChallengeEvent event, Item item, ItemStack reward, int count) {
        return new Event<>(time, type, event, new ItemEventData(item), reward, count);
    }
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Event<EntityEventData> createEntityEvent(ChallengeTime time, ChallengeType type, ChallengeEvent event, EntityType<?> entityType, ItemStack reward, int count) {
        return new Event<>(time, type, event, new EntityEventData(entityType), reward, count);
    }
}
