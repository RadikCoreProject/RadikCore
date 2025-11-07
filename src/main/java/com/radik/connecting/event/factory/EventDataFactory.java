package com.radik.connecting.event.factory;

import net.minecraft.nbt.NbtElement;

public interface EventDataFactory {
    EventData fromNbt(NbtElement nbt);
}
