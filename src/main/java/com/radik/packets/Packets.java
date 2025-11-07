package com.radik.packets;

import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.custom.chemistry.VialContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Packets {
    public static final PacketCodec<ByteBuf, Vec3d> VEC3D = new PacketCodec<>() {
        public @NotNull Vec3d decode(ByteBuf byteBuf) {
            Vector3f vector3f = PacketByteBuf.readVector3f(byteBuf);
            return new Vec3d(vector3f.x, vector3f.y, vector3f.z);
        }

        public void encode(ByteBuf byteBuf, @NotNull Vec3d vector3f) {
            PacketByteBuf.writeVector3f(byteBuf, vector3f.toVector3f());
        }
    };

    public static final PacketCodec<ByteBuf, VialContainer> VIAL_CONTAINER_CODEC =
            PacketCodecs.VAR_INT.xmap(
                    i -> VialContainer.values()[i],
                    Enum::ordinal
            );

    public static final PacketCodec<ByteBuf, ChallengeEvent> EVENT_TYPE_CODEC =
            PacketCodecs.VAR_INT.xmap(
                    i -> ChallengeEvent.values()[i],
                    Enum::ordinal
            );
}

