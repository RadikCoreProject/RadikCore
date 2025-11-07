package com.radik.packets.payload;

import com.radik.connecting.Player;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

import static com.radik.Data.LOGGER;
import static com.radik.Data.MOD_ID;
import static com.radik.packets.payload.PayloadData.writeBoundedString;

public record LoginPayload(Player player) implements CustomPayload {
    public static final CustomPayload.Id<LoginPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "login_packet"));

    public static final PacketCodec<RegistryByteBuf, Player> PLAYER_CODEC = new PacketCodec<>() {
        @Override
        public void encode(@NotNull RegistryByteBuf buf, Player player) {
            buf.writerIndex();
            buf.writeBoolean(player != null);
            if (player == null) return;

            writeBoundedString(buf, player.name, 16, 64);
            writeBoundedString(buf, player.colorCode, 64, 256);

            buf.writeLong(player.id);
            buf.writeByte(player.particle);

            byte flags = (byte) (player.bold ? 0x01 : 0);
            buf.writeByte(flags);
        }

        @Override
        public @Nullable Player decode(@NotNull RegistryByteBuf buf) {
            int startIndex = buf.readerIndex();

            boolean exists = buf.readBoolean();
            if (!exists) return null;

            Player player = new Player();

            player.name = readAndLogField(buf, "name", 16);
            player.colorCode = readAndLogField(buf, "colorCode", 64);

            player.id = buf.readLong();
            player.particle = buf.readByte();

            byte flags = buf.readByte();
            player.bold = (flags & 0x01) != 0;

            int bytesRead = buf.readerIndex() - startIndex;
            LOGGER.info("<<< decoded {} bytes for Player packet", bytesRead);

            return player;
        }

        private @NotNull String readAndLogField(@NotNull ByteBuf buf, String name, int maxBytes) {
            int length = buf.readByte() & 0xFF;
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            String value = new String(bytes, StandardCharsets.UTF_8);
            LOGGER.info("{}: '{}' ({} bytes)", name, value, length);
            return value;
        }
    };

    public static final PacketCodec<RegistryByteBuf, LoginPayload> CODEC = PacketCodec.tuple(PLAYER_CODEC, LoginPayload::player, LoginPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
