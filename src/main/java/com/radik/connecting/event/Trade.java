package com.radik.connecting.event;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.Arrays;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public record Trade(ItemStack seller, ItemStack[] buyer, boolean elite) implements Comparable<Trade> {
    public static final Codec<Trade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.CODEC.fieldOf("seller").forGetter(Trade::seller),
        Codec.list(ItemStack.CODEC)
            .xmap(list -> list.toArray(new ItemStack[0]), Arrays::asList)
            .fieldOf("buyer").forGetter(Trade::buyer),
        Codec.BOOL.fieldOf("elite").forGetter(Trade::elite)
    ).apply(instance, Trade::new));

    public static final PacketCodec<RegistryByteBuf, Trade> PACKET_CODEC = PacketCodec.of(
        Trade::writeToBuffer,
        Trade::readFromBuffer
    );

    @Override
    public int compareTo(@NotNull Trade o) {
        return elite == o.elite ? Integer.compare(o.buyer().length, this.buyer().length) : elite ? 1 : -1;
    }

    public void writeToWriteView(@NotNull WriteView view) {
        view.put("trade", CODEC, this);
    }
    public static Trade fromReadView(@NotNull ReadView view) {
        return view.read("trade", CODEC).orElse(null);
    }

    public void writeToBuffer(RegistryByteBuf buf) {
        ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, this.seller());
        buf.writeInt(this.buyer().length);
        for (ItemStack stack : this.buyer()) ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, stack);
        buf.writeBoolean(elite);
    }
    public static @NotNull Trade readFromBuffer(RegistryByteBuf buf) {
        ItemStack seller = ItemStack.OPTIONAL_PACKET_CODEC.decode(buf);
        int length = buf.readInt();
        ItemStack[] buyer = new ItemStack[length];
        for (int i = 0; i < length; i++) buyer[i] = ItemStack.OPTIONAL_PACKET_CODEC.decode(buf);
        return new Trade(seller, buyer, buf.readBoolean());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return elite == trade.elite &&
            ItemStack.areEqual(seller, trade.seller) &&
            Arrays.equals(buyer, trade.buyer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seller, Arrays.hashCode(buyer), elite);
    }

    @Override
    public @NotNull String toString() {
        return String.format("Trade{seller=%s, buyer=%s, elite=%s}", seller, Arrays.toString(buyer), elite);
    }
}
