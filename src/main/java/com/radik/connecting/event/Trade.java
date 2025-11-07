package com.radik.connecting.event;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryWrapper;

import java.util.Arrays;
import java.util.Objects;

public record Trade(ItemStack seller, ItemStack[] buyer, boolean elite) implements Comparable<Trade> {

    @Override
    public int compareTo(@NotNull Trade o) {
        return elite == o.elite ? Integer.compare(o.buyer().length, this.buyer().length) : elite ? 1 : -1;
    }

    public @NotNull NbtCompound toNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = new NbtCompound();
        nbt.put("seller", seller.toNbt(registries));
        NbtList buyerList = new NbtList();
        for (ItemStack stack : buyer) buyerList.add(stack.toNbt(registries));
        nbt.put("buyer", buyerList);
        nbt.putBoolean("elite", elite);
        return nbt;
    }

    @Contract("_, _ -> new")
    public static @NotNull Trade fromNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        ItemStack seller = ItemStack.fromNbt(registries, nbt.getCompound("seller").orElseThrow()).orElse(ItemStack.EMPTY);

        NbtList list = nbt.getList("buyer").orElseThrow();
        ItemStack[] buyer = new ItemStack[list.size()];
        for (int i = 0; i < list.size(); i++) {
            buyer[i] = ItemStack.fromNbt(registries, list.getCompound(i).orElseThrow()).orElse(ItemStack.EMPTY);
        }

        return new Trade(seller, buyer, nbt.getBoolean("elite").orElseThrow());
    }

    public void writeToBuffer(RegistryByteBuf buf) {
        ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, this.seller());
        buf.writeInt(this.buyer().length);
        for (ItemStack stack : this.buyer()) {
            ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, stack);
        }
        buf.writeBoolean(elite);
    }

    @Contract("_ -> new")
    public static @NotNull Trade readFromBuffer(RegistryByteBuf buf) {
        ItemStack seller = ItemStack.OPTIONAL_PACKET_CODEC.decode(buf);
        int length = buf.readInt();
        ItemStack[] buyer = new ItemStack[length];
        for (int i = 0; i < length; i++) buyer[i] = ItemStack.OPTIONAL_PACKET_CODEC.decode(buf);
        return new Trade(seller, buyer, buf.readBoolean());
    }

    public static final PacketCodec<RegistryByteBuf, Trade> PACKET_CODEC = PacketCodec.of(
        Trade::writeToBuffer,
        Trade::readFromBuffer
    );

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return ItemStack.areEqual(seller, trade.seller) && Arrays.equals(buyer, trade.buyer) && elite == ((Trade) o).elite;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(seller);
        result = 31 * result + Arrays.hashCode(buyer);
        return result;
    }

    @Override
    public @NotNull String toString() {
        return String.format("Trade{seller=%s, buyer=%s, elite=%s}", seller, Arrays.toString(buyer), elite);
    }
}
