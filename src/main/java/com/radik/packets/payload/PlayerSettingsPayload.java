package com.radik.packets.payload;

import com.radik.Radik;
import com.radik.connecting.PlayerSettings;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record PlayerSettingsPayload(PlayerSettings settings) implements CustomPayload {
    public static final Id<PlayerSettingsPayload> ID = new Id<>(Identifier.of(Radik.MOD_ID, "player_settings"));
    public static final PacketCodec<PacketByteBuf, PlayerSettingsPayload> CODEC = PacketCodec.of(PlayerSettingsPayload::write, PlayerSettingsPayload::new);

    public PlayerSettingsPayload(@NotNull PacketByteBuf buf) {
        this(decode(buf.readByte()));
    }

    public void write(@NotNull PacketByteBuf buf) {
        buf.writeByte(encode(this.settings));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Contract(pure = true)
    private static byte encode(@NotNull PlayerSettings settings) {
        byte result = 0;
        if (settings.netherHotSpot) result |= 1;
        if (settings.overworldHotSpot) result |= 2;
        if (settings.presentOpening) result |= 4;
        if (settings.antiMuteCommands) result |= 8;
        return result;
    }

    @Contract(value = "_ -> new", pure = true)
    private static @NotNull PlayerSettings decode(byte data) {
        boolean[] booleans = new boolean[4];
        booleans[0] = (data & 1) != 0;  // netherHotSpot
        booleans[1] = (data & 2) != 0;  // overworldHotSpot
        booleans[2] = (data & 4) != 0;  // presentOpening
        booleans[3] = (data & 8) != 0;  // antiMuteCommands
        return new PlayerSettings(booleans);
    }
}
