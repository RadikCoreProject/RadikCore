package com.radik.packets.payload;

import com.radik.Radik;
import com.radik.connecting.event.Event;
import com.radik.connecting.event.Eventer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ChallengesDataPayload(Eventer[] playerEvents, Eventer globalEvent) implements CustomPayload {
    public static final CustomPayload.Id<ChallengesDataPayload> ID = new CustomPayload.Id<>(Identifier.of(Radik.MOD_ID, "challenges_data"));

    public static final PacketCodec<RegistryByteBuf, ChallengesDataPayload> CODEC = PacketCodec.of(
        ChallengesDataPayload::write,
        ChallengesDataPayload::read
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void write(RegistryByteBuf buf) {
        if (playerEvents != null) {
            buf.writeInt(playerEvents.length);
            for (Eventer event : playerEvents) {
                writeEventer(buf, event);
            }
        } else {
            buf.writeInt(0);
        }

        writeEventer(buf, globalEvent);
    }

    @Contract("_ -> new")
    public static @NotNull ChallengesDataPayload read(@NotNull RegistryByteBuf buf) {
        int playerEventsLength = buf.readInt();
        Eventer[] playerEvents = new Eventer[playerEventsLength];
        for (int i = 0; i < playerEventsLength; i++) {
            playerEvents[i] = readEventer(buf);
        }

        Eventer globalEvent = readEventer(buf);

        return new ChallengesDataPayload(playerEvents, globalEvent);
    }

    private static void writeEventer(RegistryByteBuf buf, Eventer eventer) {
        if (eventer != null) {
            buf.writeBoolean(true);
            NbtCompound nbt = eventer.toNbt(buf.getRegistryManager());
            buf.writeNbt(nbt);
        } else {
            buf.writeBoolean(false);
        }
    }

    private static @Nullable Eventer readEventer(@NotNull RegistryByteBuf buf) {
        if (buf.readBoolean()) {
            NbtCompound nbt = buf.readNbt();
            if (nbt != null) {
                return Event.fromNbt(nbt, buf.getRegistryManager());
            }
        }
        return null;
    }
}
