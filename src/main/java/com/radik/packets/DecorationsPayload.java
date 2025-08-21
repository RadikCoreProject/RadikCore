package com.radik.packets;

import com.radik.connecting.Decoration;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

import static com.radik.Data.MOD_ID;

public record DecorationsPayload(ArrayList<Decoration> decorations) implements CustomPayload {
    public static final Id<DecorationsPayload> ID =
            new Id<>(Identifier.of(MOD_ID, "decors_packet"));

    public static final PacketCodec<RegistryByteBuf, DecorationsPayload> CODEC = PacketCodec.tuple(Packets.DECORATION_CODEC, DecorationsPayload::decorations, DecorationsPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
