package com.radik.connecting.event.factory;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public record EntityEventData(EntityType<?> entityType) implements EventData {
    @Override
    public @Unmodifiable NbtElement toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putString("type", "entity");
        compound.putString("id", Registries.ENTITY_TYPE.getId(entityType).toString());
        return compound;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getType() {
        return "entity";
    }

    public static EntityEventData fromNbt(NbtCompound compound) {
        Identifier id = Identifier.of(compound.getString("id").orElse("minecraft:pig"));
        return new EntityEventData(Registries.ENTITY_TYPE.get(id));
    }
}
