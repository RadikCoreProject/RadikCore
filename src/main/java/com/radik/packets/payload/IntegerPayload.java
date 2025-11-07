package com.radik.packets.payload;

import com.radik.Radik;
import com.radik.packets.PacketType;
import com.radik.util.Duplet;
import com.radik.util.Triplet;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.minecraft.network.RegistryByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record IntegerPayload(Triplet<Integer, Integer, PacketType> packet) implements CustomPayload {
    public static final CustomPayload.Id<IntegerPayload> ID =
            new CustomPayload.Id<>(Identifier.of(Radik.MOD_ID, "integer_payload"));

    public static final PacketCodec<RegistryByteBuf, IntegerPayload> CODEC = new PacketCodec<>() {
        @Contract("_ -> new")
        @Override
        public @NotNull IntegerPayload decode(@NotNull RegistryByteBuf buf) {
            Integer integerValue = null;
            if (buf.readBoolean()) integerValue = buf.readInt();
            Integer data = null;
            if (buf.readBoolean()) data = buf.readInt();
            PacketType packetType = null;
            if (buf.readBoolean()) {
                String typeName = buf.readString();
                try {
                    packetType = PacketType.valueOf(typeName);
                } catch (IllegalArgumentException e) {
                    Radik.LOGGER.error("Invalid PacketType: {}", typeName);
                }
            }

            return new IntegerPayload(new Triplet<>(integerValue, data, packetType));
        }

        @Override
        public void encode(@NotNull RegistryByteBuf buf, @NotNull IntegerPayload payload) {
            Triplet<Integer, Integer, PacketType> value = payload.packet();

            buf.writeBoolean(value.getType() != null);
            if (value.getType() != null) buf.writeInt(value.getType());
            buf.writeBoolean(value.getParametrize() != null);
            if (value.getParametrize() != null) buf.writeInt(value.getParametrize());

            buf.writeBoolean(value.getCount() != null);
            if (value.getCount() != null) {
                buf.writeString(value.getCount().name());
            }
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}


