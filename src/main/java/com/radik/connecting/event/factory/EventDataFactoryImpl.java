package com.radik.connecting.event.factory;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EventDataFactoryImpl implements EventDataFactory {
    @Override
    public EventData fromNbt(NbtElement nbt) {
        if (nbt instanceof NbtCompound compound) {
            String type = compound.getString("type").orElse("item");
            Identifier id = Identifier.of(compound.getString("id").orElse("minecraft:air"));

            return switch (type) {
                case "block" -> new BlockEventData(Registries.BLOCK.get(id));
                case "item" -> new ItemEventData(Registries.ITEM.get(id));
                case "entity" -> new EntityEventData(Registries.ENTITY_TYPE.get(id));
                default -> throw new IllegalArgumentException("Unknown event data type: " + type);
            };
        } else if (nbt instanceof NbtString nbtString) {
            Identifier id = Identifier.of(nbtString.asString().orElseThrow());
            if (Registries.BLOCK.containsId(id)) return new BlockEventData(Registries.BLOCK.get(id));
            else if (Registries.ITEM.containsId(id)) return new ItemEventData(Registries.ITEM.get(id));
            else if (Registries.ENTITY_TYPE.containsId(id)) return new EntityEventData(Registries.ENTITY_TYPE.get(id));
        }
        throw new IllegalArgumentException("Unknown event data type in NBT: " + (nbt != null ? nbt.getType() : "null"));
    }
}
