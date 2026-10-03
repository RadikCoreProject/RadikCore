package com.radik.connecting.event.factory;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EntityEventData implements EventData {
    private final Identifier entityId;

    public EntityEventData(Identifier entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getType() {
        return "entity";
    }

    @Override
    public String getStringValue() {
        return entityId != null ? entityId.toString() : "";
    }

    public EntityType<?> entityType() {
        return Registries.ENTITY_TYPE.get(entityId);
    }

    public Identifier getEntityTypeId() {
        return entityId;
    }
}
