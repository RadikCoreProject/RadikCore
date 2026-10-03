package com.radik.packets.payload;

import com.radik.Radik;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record EventPayload(int eventId, BlockPos pos, int data) implements CustomPayload {
    public static final Id<EventPayload> ID = new Id<>(Identifier.of(Radik.MOD_ID, "world_event"));

    public static final PacketCodec<PacketByteBuf, EventPayload> CODEC = PacketCodec.of(EventPayload::write, EventPayload::new);

    public EventPayload(@NotNull PacketByteBuf buf) {
        this(
                buf.readInt(),      // eventId
                buf.readBlockPos(), // pos
                buf.readInt()       // data
        );
    }

    private void write(@NotNull PacketByteBuf buf) {
        buf.writeInt(this.eventId);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.data);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}