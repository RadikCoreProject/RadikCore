package com.radik.packets.payload;

import com.radik.Radik;
import com.radik.packets.PacketType;
import com.radik.util.Triplet;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.minecraft.network.RegistryByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record IntegerPayload(Integer f, Integer s, PacketType type) implements CustomPayload {
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
            if (buf.readBoolean()) packetType = buf.readEnumConstant(PacketType.class);

            return new IntegerPayload(integerValue, data, packetType);
        }

        @Override
        public void encode(@NotNull RegistryByteBuf buf, @NotNull IntegerPayload payload) {
            buf.writeBoolean(payload.f != null);
            if (payload.f != null) buf.writeInt(payload.f);
            buf.writeBoolean(payload.s != null);
            if (payload.s != null) buf.writeInt(payload.s);

            buf.writeBoolean(payload.type != null);
            if (payload.type != null) buf.writeEnumConstant(payload.type);
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}


