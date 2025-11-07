package com.radik.connecting.event.factory;

import net.minecraft.nbt.NbtElement;

public interface EventData {
    NbtElement toNbt();
    String getType();
}
