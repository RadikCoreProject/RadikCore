package com.radik.connecting.game;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class StorageBlockData {
    public String owner;
    public int storage;
    public int filling;
    public ItemStack stack;

    public StorageBlockData(String owner, int storage, int filling, ItemStack stack) {
        this.owner = owner;
        this.storage = storage;
        this.filling = filling;
        this.stack = stack.copy();
    }

    public StorageBlockData(String owner, int storage, int filling, ItemConvertible type) {
        this(owner, storage, filling, new ItemStack(type));
    }

    public StorageBlockData(String owner, int storage, int filling, int id) {
        this(owner, storage, filling, new ItemStack(Item.byRawId(id)));
    }

    public StorageBlockData(RegistryByteBuf buf) {
        this.owner = buf.readString();
        this.storage = buf.readInt();
        this.filling = buf.readInt();
        this.stack = ItemStack.PACKET_CODEC.decode(buf);
    }

    public void write(RegistryByteBuf buf) {
        buf.writeString(owner);
        buf.writeInt(storage);
        buf.writeInt(filling);
        ItemStack.PACKET_CODEC.encode(buf, stack);
    }

    public static final PacketCodec<RegistryByteBuf, StorageBlockData> STORAGE_DATA_PACKET_CODEC = PacketCodec.of(
        StorageBlockData::write,
        StorageBlockData::new
    );

    // id < 5
    public static int storageById(int id) {
        return (1 << 6) << (2 * id);
    }
}
