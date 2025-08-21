package com.radik.packets;

import com.radik.connecting.Decoration;
import com.radik.connecting.Player;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.radik.Data.LOGGER;

public class Packets {
    public static final PacketCodec<RegistryByteBuf, Player> PLAYER_CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, Player player) {
            buf.writerIndex();
            buf.writeBoolean(player != null);
            if (player == null) return;

            writeBoundedString(buf, player.ip, 15);
            writeBoundedString(buf, player.name, 16);
            writeBoundedString(buf, player.password, 20);
            writeBoundedString(buf, player.muted, 128);
            writeBoundedString(buf, player.colorCode, 64);

            buf.writeLong(player.id);
            buf.writeByte(player.particle);

            byte flags = (byte) ((player.bold ? 0x01 : 0) | (player.reg ? 0x02 : 0) | (player.law ? 0x04 : 0));
            buf.writeByte(flags);
        }

        @Override
        public Player decode(RegistryByteBuf buf) {
            int startIndex = buf.readerIndex();

            boolean exists = buf.readBoolean();
            if (!exists) return null;

            Player player = new Player();

            player.ip = readAndLogField(buf, "ip", 15);
            player.name = readAndLogField(buf, "name", 16);
            player.password = readAndLogField(buf, "password", 20);
            player.muted = readAndLogField(buf, "muted", 128);
            player.colorCode = readAndLogField(buf, "colorCode", 64);

            player.id = buf.readLong();
            player.particle = buf.readByte();

            byte flags = buf.readByte();
            player.bold = (flags & 0x01) != 0;
            player.reg = (flags & 0x02) != 0;
            player.law = (flags & 0x04) != 0;

            int bytesRead = buf.readerIndex() - startIndex;
            LOGGER.info("<<< decoded {} bytes for Player packet", bytesRead);

            return player;
        }

        private String readAndLogField(ByteBuf buf, String name, int maxBytes) {
            int length = buf.readByte() & 0xFF;
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            String value = new String(bytes, StandardCharsets.UTF_8);
            LOGGER.info("{}: '{}' ({} bytes)", name, value, length);
            return value;
        }

        private void writeBoundedString(ByteBuf buf, String value, int maxBytes) {
            if (value == null) value = "";
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            if (bytes.length > maxBytes) {
                throw new IllegalArgumentException("LENGTH LIMIT: " + maxBytes);
            }
            buf.writeByte(bytes.length);
            buf.writeBytes(bytes);
        }
    };

    public static final PacketCodec<RegistryByteBuf, ArrayList<Decoration>> DECORATION_CODEC = new PacketCodec<>() {

                @Override
                public void encode(RegistryByteBuf buf, ArrayList<Decoration> decorations) {
                    buf.writeVarInt(decorations.size());

                    for (Decoration deco : decorations) {
                        buf.writeByte(deco.id);
                        buf.writeByte(deco.type);
                        buf.writeShort(deco.cost);
                        buf.writeBoolean(deco.own);

                        writeBoundedString(buf, deco.owner, 16, 64); // 16 симв
                        writeBoundedString(buf, deco.description, 256, 1024); // 256 симв
                        writeBoundedString(buf, deco.name, 32, 128); // 32 симв
                    }
                }

                @Override
                public ArrayList<Decoration> decode(RegistryByteBuf buf) {
                    ArrayList<Decoration> decorations = new ArrayList<>();
                    int size = buf.readVarInt();

                    for (int i = 0; i < size; i++) {
                        byte id = buf.readByte();
                        byte type = buf.readByte();
                        short cost = buf.readShort();
                        boolean own = buf.readBoolean();

                        String owner = readBoundedString(buf, 16, 64);
                        String description = readBoundedString(buf, 256, 1024);
                        String name = readBoundedString(buf, 32, 128);

                        decorations.add(new Decoration(
                                new byte[]{id, type},
                                cost,
                                own,
                                new String[]{owner, description, name}
                        ));
                    }
                    return decorations;
                }

                private void writeBoundedString(ByteBuf buf, String value, int maxChars, int maxBytes) {
                    if (value == null) value = "";
                    if (value.length() > maxChars) {
                        throw new IllegalArgumentException(
                                "LIMIT EXCEED: " + maxChars + " > " + value.length()
                        );
                    }

                    byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
                    if (bytes.length > maxBytes) {
                        throw new IllegalArgumentException(
                                "LIMIT EXCEED: " + maxBytes + " > " + bytes.length
                        );
                    }
                    buf.writeByte(bytes.length);
                    buf.writeBytes(bytes);
                }

                private String readBoundedString(ByteBuf buf, int maxChars, int maxBytes) {
                    int length = buf.readByte() & 0xFF;
                    if (length > maxBytes) {
                        throw new DecoderException(
                                "LIMIT EXCEED: " + maxBytes
                        );
                    }

                    byte[] bytes = new byte[length];
                    buf.readBytes(bytes);
                    String result = new String(bytes, StandardCharsets.UTF_8);
                    if (result.length() > maxChars) {
                        throw new DecoderException(
                                "LIMIT EXCEED: " + maxChars
                        );
                    }

                    return result;
                }
            };

    public static final PacketCodec<ByteBuf, Vec3d> VEC3D = new PacketCodec<>() {
        public Vec3d decode(ByteBuf byteBuf) {
            Vector3f vector3f = PacketByteBuf.readVector3f(byteBuf);
            return new Vec3d(vector3f.x, vector3f.y, vector3f.z);
        }

        public void encode(ByteBuf byteBuf, Vec3d vector3f) {
            PacketByteBuf.writeVector3f(byteBuf, vector3f.toVector3f());
        }
    };
}

