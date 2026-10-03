package com.radik.connecting.game;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;

public class EmbassyData {
    public String owner;
    public String legate;
    public BlockPos from;
    public BlockPos to;
    public LocalDateTime cooldown;
    public byte access;
    public byte powered;

    public EmbassyData(String owner, String legate, BlockPos from, BlockPos to, LocalDateTime cooldown, byte access, byte powered) {
        this.owner = owner;
        this.legate = legate;
        this.from = from;
        this.to = to;
        this.cooldown = cooldown;
        this.access = access;
        this.powered = powered;
    }

    public EmbassyData(PacketByteBuf buf) {
        this.owner = buf.readString();
        this.legate = buf.readString();
        this.from = buf.readBlockPos();
        this.to = buf.readBlockPos();
        this.cooldown = LocalDateTime.parse(buf.readString());
        this.powered = buf.readByte();
        this.access = buf.readByte();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(owner);
        buf.writeString(legate);
        buf.writeBlockPos(from);
        buf.writeBlockPos(to);
        buf.writeString(cooldown.toString());
        buf.writeByte(powered);
        buf.writeByte(access);
    }

    public static final PacketCodec<RegistryByteBuf, EmbassyData> EMBASSY_DATA_PACKET_CODEC = PacketCodec.of(
        EmbassyData::write,
        EmbassyData::new
    );
}
