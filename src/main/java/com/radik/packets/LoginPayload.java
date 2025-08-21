package com.radik.packets;

import com.radik.connecting.Player;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.minecraft.network.codec.PacketCodec;

import static com.radik.Data.MOD_ID;

public record LoginPayload(Player player) implements CustomPayload {
    public static final CustomPayload.Id<LoginPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "login_packet"));

    public static final PacketCodec<RegistryByteBuf, LoginPayload> CODEC = PacketCodec.tuple(Packets.PLAYER_CODEC, LoginPayload::player, LoginPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
