package com.radik.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import static com.radik.Data.MOD_ID;

public record TeleportPayload(Vec3d pos) implements CustomPayload {
    public static final Id<TeleportPayload> ID =
            new Id<>(Identifier.of(MOD_ID, "teleport_packet"));

    public static final PacketCodec<PacketByteBuf, TeleportPayload> CODEC = CustomPayload.codecOf(TeleportPayload::write, TeleportPayload::new);

    private TeleportPayload(PacketByteBuf buf) {
        this(buf.readVec3d());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVec3d(this.pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
