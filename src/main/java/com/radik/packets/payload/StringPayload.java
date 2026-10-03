package com.radik.packets.payload;

import com.radik.Radik;
import com.radik.packets.PacketType;
import com.radik.util.Duplet;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record StringPayload(Duplet<String, PacketType> packet) implements CustomPayload {
    public static final CustomPayload.Id<StringPayload> ID =
        new CustomPayload.Id<>(Identifier.of(Radik.MOD_ID, "string_payload"));

    public static final PacketCodec<RegistryByteBuf, StringPayload> CODEC = new PacketCodec<>() {
        @Contract("_ -> new")
        @Override
        public @NotNull StringPayload decode(@NotNull RegistryByteBuf buf) {
            String string = null;
            if (buf.readBoolean()) string = buf.readString();
            PacketType packet = null;
            if (buf.readBoolean()) {
                String typeName = buf.readString();
                try {
                    packet = PacketType.valueOf(typeName);
                } catch (IllegalArgumentException e) {
                    Radik.LOGGER.error("Invalid PacketType: {}", typeName);
                }
            }

            return new StringPayload(new Duplet<>(string, packet));
        }

        @Override
        public void encode(@NotNull RegistryByteBuf buf, @NotNull StringPayload payload) {
            Duplet<String, PacketType> value = payload.packet();

            buf.writeBoolean(value.type() != null);
            if (value.type() != null) buf.writeString(value.type());
            buf.writeBoolean(value.parametrize() != null);
            if (value.parametrize() != null) {
                buf.writeString(value.parametrize().name());
            }
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
