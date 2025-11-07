package com.radik.connecting.event.factory;

import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public record ItemEventData(Item item) implements EventData {
    @Override
    public @Unmodifiable NbtElement toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putString("type", "item");
        compound.putString("id", Registries.ITEM.getId(item).toString());
        return compound;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getType() {
        return "item";
    }

    public static ItemEventData fromNbt(NbtCompound compound) {
        Identifier id = Identifier.of(compound.getString("id").orElse("minecraft:stone"));
        return new ItemEventData(Registries.ITEM.get(id));
    }
}
