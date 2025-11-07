package com.radik.packets.payload;

import com.radik.packets.Action;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static com.radik.Data.MOD_ID;

public record ActionPayload(Action action) implements CustomPayload {
    public static final Id<ActionPayload> ID =
            new Id<>(Identifier.of(MOD_ID, "action_payload"));

    public static final PacketCodec<PacketByteBuf, Action> INTEGER_CODEC = new PacketCodec<>() {
                @Override
                public Action decode(@NotNull PacketByteBuf buf) {
                    Action action = null;
                    if (buf.readBoolean()) {
                        String typeName = PacketCodecs.STRING.decode(buf);
                        action = Action.valueOf(typeName);
                    }
                    return action;
                }

                @Override
                public void encode(@NotNull PacketByteBuf buf, Action value) {
                    buf.writeBoolean(value != null);
                    if (value != null) PacketCodecs.STRING.encode(buf, value.name());
                }
            };

    public static final PacketCodec<PacketByteBuf, ActionPayload> CODEC = PacketCodec.tuple(INTEGER_CODEC, ActionPayload::action, ActionPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
