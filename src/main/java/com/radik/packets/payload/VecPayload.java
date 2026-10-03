package com.radik.packets.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import static com.radik.Data.MOD_ID;

public record VecPayload(Vec3d pos) implements CustomPayload {
    public static final Id<VecPayload> ID =
            new Id<>(Identifier.of(MOD_ID, "teleport_packet"));

    public static final PacketCodec<ByteBuf, Vec3d> VEC3D = new PacketCodec<>() {
        public @NotNull Vec3d decode(ByteBuf byteBuf) {
            Vector3f vector3f = PacketByteBuf.readVector3f(byteBuf);
            return new Vec3d(vector3f.x, vector3f.y, vector3f.z);
        }

        public void encode(ByteBuf byteBuf, @NotNull Vec3d vector3f) {
            PacketByteBuf.writeVector3f(byteBuf, vector3f.toVector3f());
        }
    };

    public static final PacketCodec<PacketByteBuf, VecPayload> CODEC = CustomPayload.codecOf(VecPayload::write, VecPayload::new);

    private VecPayload(@NotNull PacketByteBuf buf) {
        this(buf.readVec3d());
    }

    private void write(@NotNull PacketByteBuf buf) {
        buf.writeVec3d(this.pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
