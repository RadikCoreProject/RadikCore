package com.radik.connecting.event.factory;

import com.radik.connecting.event.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EventFactory {
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Event createBlockEvent(ChallengeTime time, ChallengeType type, ChallengeEvent event, Block block, ItemStack reward, int count) {
        return new Event(time, type, event, createBlockEventData(block), reward, count);
    }
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Event createItemEvent(ChallengeTime time, ChallengeType type, ChallengeEvent event, Item item, ItemStack reward, int count) {
        return new Event(time, type, event, createItemEventData(item), reward, count);
    }
    @Contract("_, _, _, _, _, _ -> new")
    public static @NotNull Event createEntityEvent(ChallengeTime time, ChallengeType type, ChallengeEvent event, EntityType<?> entityType, ItemStack reward, int count) {
        return new Event(time, type, event, createEntityEventData(entityType), reward, count);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull EventData createBlockEventData(Identifier blockId) {
        return new BlockEventData(blockId);
    }

    @Contract("_ -> new")
    public static @NotNull EventData createBlockEventData(Block block) {
        return new BlockEventData(Registries.BLOCK.getId(block));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull EventData createItemEventData(Identifier itemId) {
        return new ItemEventData(itemId);
    }

    @Contract("_ -> new")
    public static @NotNull EventData createItemEventData(Item item) {
        return new ItemEventData(Registries.ITEM.getId(item));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull EventData createEntityEventData(Identifier entityId) {
        return new EntityEventData(entityId);
    }

    @Contract("_ -> new")
    public static @NotNull EventData createEntityEventData(EntityType<?> entityType) {
        return new EntityEventData(Registries.ENTITY_TYPE.getId(entityType));
    }
}
