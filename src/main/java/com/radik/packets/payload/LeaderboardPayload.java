package com.radik.packets.payload;

import com.radik.util.Triplet;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.radik.Data.MOD_ID;

public record LeaderboardPayload(Triplet<Integer, Integer, LinkedHashMap<String, Integer>> leaderboard) implements CustomPayload {
    public static final Id<LeaderboardPayload> ID = new Id<>(Identifier.of(MOD_ID, "leaderboard_packet"));

    public static final PacketCodec<RegistryByteBuf, Triplet<Integer, Integer, LinkedHashMap<String, Integer>>> LEADERBOARD_CODEC = new PacketCodec<>() {

        @Override
        public void encode(@NotNull RegistryByteBuf buf, @NotNull Triplet<Integer, Integer, LinkedHashMap<String, Integer>> leaderboard) {
            if (leaderboard.getType() == null || leaderboard.getParametrize() == null || leaderboard.getCount() == null) {
                buf.writeBoolean(true);
                return;
            }

            buf.writeBoolean(false);
            buf.writeInt(leaderboard.getType());
            buf.writeInt(leaderboard.getParametrize());

            LinkedHashMap<String, Integer> topPlayers = leaderboard.getCount();
            buf.writeVarInt(topPlayers.size());

            for (Map.Entry<String, Integer> entry : topPlayers.entrySet()) {
                String name = entry.getKey();
                Integer value = entry.getValue();
                PayloadData.writeBoundedString(buf, name, 16, 64);
                buf.writeInt(value);
            }
        }

        @Override
        public @NotNull Triplet<Integer, Integer, LinkedHashMap<String, Integer>> decode(@NotNull RegistryByteBuf buf) {
            if (buf.readBoolean()) {
                return new Triplet<>(0, 0, new LinkedHashMap<>());
            }

            int playerScore = buf.readInt();
            int playerRank = buf.readInt();
            int size = buf.readVarInt();

            LinkedHashMap<String, Integer> topPlayers = new LinkedHashMap<>();
            for (int i = 0; i < size; i++) {
                String name = PayloadData.readBoundedString(buf, 16, 64);
                int score = buf.readInt();
                topPlayers.put(name, score);
            }

            return new Triplet<>(playerScore, playerRank, topPlayers);
        }
    };

    public static final PacketCodec<RegistryByteBuf, LeaderboardPayload> CODEC =
        PacketCodec.tuple(LEADERBOARD_CODEC, LeaderboardPayload::leaderboard, LeaderboardPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
