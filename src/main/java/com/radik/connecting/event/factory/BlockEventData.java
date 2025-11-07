package com.radik.connecting.event.factory;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public record BlockEventData(Block block) implements EventData {
    @Override
    public @Unmodifiable NbtElement toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putString("type", "block");
        compound.putString("id", Registries.BLOCK.getId(block).toString());
        return compound;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getType() {
        return "block";
    }

    public static BlockEventData fromNbt(NbtCompound compound) {
        Identifier id = Identifier.of(compound.getString("id").orElse("minecraft:air"));
        return new BlockEventData(Registries.BLOCK.get(id));
    }
}