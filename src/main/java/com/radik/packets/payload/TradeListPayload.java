package com.radik.packets.payload;

import com.radik.connecting.event.Trade;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record TradeListPayload(List<Trade> trades) implements CustomPayload {
    public static final CustomPayload.Id<TradeListPayload> ID =
        new CustomPayload.Id<>(Identifier.of("radik", "trade_list"));

    public static final PacketCodec<RegistryByteBuf, List<Trade>> TRADE_LIST_PACKET_CODEC =
        Trade.PACKET_CODEC.collect(PacketCodecs.toCollection(ArrayList::new));

    public static final PacketCodec<RegistryByteBuf, TradeListPayload> CODEC =
        PacketCodec.tuple(TRADE_LIST_PACKET_CODEC, TradeListPayload::trades, TradeListPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

