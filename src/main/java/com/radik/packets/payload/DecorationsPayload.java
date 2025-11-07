package com.radik.packets.payload;

import com.radik.connecting.Decoration;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.radik.Data.MOD_ID;
import static com.radik.packets.payload.PayloadData.readBoundedString;
import static com.radik.packets.payload.PayloadData.writeBoundedString;

public record DecorationsPayload(HashMap<Decoration, Boolean> decorations) implements CustomPayload {
    public static final Id<DecorationsPayload> ID =
            new Id<>(Identifier.of(MOD_ID, "decors_packet"));

    public static final PacketCodec<RegistryByteBuf, HashMap<Decoration, Boolean>> DECORATION_CODEC = new PacketCodec<>() {

        @Override
        public void encode(@NotNull RegistryByteBuf buf, @NotNull HashMap<Decoration, Boolean> decorations) {
            buf.writeVarInt(decorations.size());

            for (Map.Entry<Decoration, Boolean> entry : decorations.entrySet()) {
                Decoration deco = entry.getKey();
                Boolean value = entry.getValue();

                buf.writeByte(deco.id);
                buf.writeByte(deco.type);
                buf.writeShort(deco.cost);

                writeBoundedString(buf, deco.owner, 16, 64);          // 16 symbols
                writeBoundedString(buf, deco.description, 256, 1024); // 256 symbols
                writeBoundedString(buf, deco.name, 32, 128);          // 32 symbols

                buf.writeBoolean(value);
            }
        }

        @Override
        public @NotNull HashMap<Decoration, Boolean> decode(@NotNull RegistryByteBuf buf) {
            HashMap<Decoration, Boolean> decorations = new HashMap<>();
            int size = buf.readVarInt();

            for (int i = 0; i < size; i++) {
                byte id = buf.readByte();
                byte type = buf.readByte();
                short cost = buf.readShort();

                String owner = readBoundedString(buf, 16, 64);
                String description = readBoundedString(buf, 256, 1024);
                String name = readBoundedString(buf, 32, 128);

                Decoration deco = new Decoration(
                        new byte[]{id, type},
                        cost,
                        new String[]{owner, description, name}
                );

                Boolean value = buf.readBoolean();
                decorations.put(deco, value);
            }
            return decorations;
        }
    };

    public static final PacketCodec<RegistryByteBuf, DecorationsPayload> CODEC = PacketCodec.tuple(DECORATION_CODEC, DecorationsPayload::decorations, DecorationsPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
