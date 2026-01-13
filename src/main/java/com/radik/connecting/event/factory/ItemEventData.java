package com.radik.connecting.event.factory;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemEventData implements EventData {
    private final Identifier entityId;

    public ItemEventData(Identifier entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public String getStringValue() {
        return entityId != null ? entityId.toString() : "";
    }

    public Item item() {
        return Registries.ITEM.get(entityId);
    }

    public Identifier getItemId() {
        return entityId;
    }
}
