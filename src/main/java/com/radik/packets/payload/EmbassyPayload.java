package com.radik.packets.payload;

import com.radik.connecting.game.EmbassyData;
import com.radik.packets.EmbassyAction;
import com.radik.util.Duplet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static com.radik.Data.MOD_ID;

public record EmbassyPayload(EmbassyAction action, EmbassyData data) implements CustomPayload {
    public static final Id<EmbassyPayload> ID = new Id<>(Identifier.of(MOD_ID, "embassy_payload"));

    public static final PacketCodec<PacketByteBuf, EmbassyPayload> CODEC = PacketCodec.of(EmbassyPayload::write, EmbassyPayload::new);

    public EmbassyPayload(@NotNull PacketByteBuf buf) {
        this(
            buf.readEnumConstant(EmbassyAction.class),
            new EmbassyData(buf)
        );
    }

    private void write(@NotNull PacketByteBuf buf) {
        buf.writeEnumConstant(this.action);
        data.write(buf);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Contract("_ -> new")
    private static int @NotNull [] toMass(@NotNull BlockPos pos) { return new int[]{pos.getX(), pos.getY(), pos.getZ()}; }
    @Contract(value = "_ -> new", pure = true)
    private static @NotNull BlockPos fromMass(int @NotNull [] pos) { return new BlockPos(pos[0], pos[1], pos[2]); }

    static EmbassyData decode(@NotNull PacketByteBuf buf) {
        return new EmbassyData(
            buf.readString(),
            buf.readString(),
            fromMass(buf.readIntArray()),
            fromMass(buf.readIntArray()),
            LocalDateTime.parse(buf.readString()),
            buf.readByte(),
            buf.readByte()
        );
    }

    static void encode(@NotNull PacketByteBuf buf, @NotNull EmbassyData value) {
        buf.writeString(value.owner == null ? "" : value.owner);
        buf.writeString(value.legate == null ? "" : value.legate);
        buf.writeIntArray(toMass(value.from));
        buf.writeIntArray(toMass(value.to == null ? new BlockPos(-100, -100, -100) : value.to));
        buf.writeString(value.cooldown == null ? LocalDateTime.MAX.toString() : value.cooldown.toString());
        buf.writeByte(value.access);
        buf.writeByte(value.powered);
    }
}
