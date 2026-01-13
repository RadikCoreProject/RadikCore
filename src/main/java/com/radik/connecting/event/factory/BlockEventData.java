package com.radik.connecting.event.factory;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record BlockEventData(Identifier blockId) implements EventData {

    @Override
    public String getType() {
        return "block";
    }

    @Override
    public String getStringValue() {
        return blockId != null ? blockId.toString() : "";
    }

    public Block block() {
        return Registries.BLOCK.get(blockId);
    }
}