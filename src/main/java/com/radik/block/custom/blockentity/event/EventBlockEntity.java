package com.radik.block.custom.blockentity.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.radik.Radik;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.client.RadikClient;
import com.radik.connecting.event.*;
import com.radik.util.Duplet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public class EventBlockEntity extends BlockEntity {
    private static final String PLAYER_EVENTS_KEY = "PlayerEvents";
    private static final String GLOBAL_EVENT_KEY = "GlobalEvent";
    private static final String LAST_GEN_DAY_KEY = "LastGenDay";
    private static final String UUID = "TextUUID";
    private static final String TRADES_KEY = "Trades";
    private static final String LEADERBOARD_KEY = "Leaderboard";
    public static boolean hasUpdate = false;

    public final Map<String, Eventer[]> playerEvents = new HashMap<>();
    public Duplet<Eventer, List<String>> globalEvent;
    public int lastGenDay = -1;
    public UUID uuid;
    public final HashMap<String, HashMap<Trade, Byte>> TRADES = new HashMap<>();
    public final HashMap<String, Integer> LEADERBOARD = new HashMap<>();

    public EventBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.EVENT_BLOCK_ENTITY, pos, state);
    }

    public Eventer getGlobalEvent() {
        return globalEvent == null ? null : globalEvent.type();
    }

    public static ChallengeEvent getEventType() {
        int day = LocalDateTime.now().getDayOfYear();

        if (day >= 305) return ChallengeEvent.HALLOWEEN;
        if (day <= 59) return ChallengeEvent.WINTER;
        if (day <= 151) return ChallengeEvent.FLOWERY;
        return ChallengeEvent.SUMMER;
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.lastGenDay = view.getInt(LAST_GEN_DAY_KEY, 0);

        if (view.contains(UUID)) {
            String uuidString = view.getString(UUID, "");
            if (!uuidString.isEmpty()) {
                try {
                    this.uuid = java.util.UUID.fromString(uuidString);
                } catch (IllegalArgumentException e) {
                    Radik.LOGGER.warn("Invalid UUID: {}", uuidString);
                }
            }
        }

        if (view.contains(GLOBAL_EVENT_KEY)) {
            this.globalEvent = view.read(GLOBAL_EVENT_KEY, Event.GLOBAL_EVENT_CODEC).orElse(null);
        }

        if (view.contains(PLAYER_EVENTS_KEY)) {
            Optional<Map<String, Eventer[]>> eventsMapOpt = view.read(PLAYER_EVENTS_KEY,
                Event.PLAYER_EVENTS_CODEC);
            eventsMapOpt.ifPresent(this.playerEvents::putAll);
        }

        if (view.contains(TRADES_KEY)) {
            Codec<Duplet<Trade, Byte>> entryCodec = RecordCodecBuilder.create(instance -> instance.group(
                Trade.CODEC.fieldOf("trade").forGetter(Duplet::type),
                Codec.BYTE.fieldOf("material").forGetter(Duplet::parametrize)
            ).apply(instance, Duplet::new));
            Codec<Map<String, List<Duplet<Trade, Byte>>>> personalTradesCodec =
                Codec.unboundedMap(Codec.STRING, Codec.list(entryCodec));
            Optional<Map<String, List<Duplet<Trade, Byte>>>> tradesMapOpt = view.read(TRADES_KEY, personalTradesCodec);
            tradesMapOpt.ifPresent(map -> {
                TRADES.clear();
                map.forEach((playerName, list) -> {
                    HashMap<Trade, Byte> playerMap = new HashMap<>();
                    for (Duplet<Trade, Byte> duplet : list) {
                        playerMap.put(duplet.type(), duplet.parametrize());
                    }
                    TRADES.put(playerName, playerMap);
                });
            });
        }

        if (view.contains(LEADERBOARD_KEY)) {
            Codec<Map<String, Integer>> leaderboardCodec = Codec.unboundedMap(
                Codec.STRING,
                Codec.INT
            );

            Optional<Map<String, Integer>> leaderboardOpt = view.read(LEADERBOARD_KEY, leaderboardCodec);
            leaderboardOpt.ifPresent(this.LEADERBOARD::putAll);
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);

        view.putInt(LAST_GEN_DAY_KEY, lastGenDay);

        if (uuid != null) {
            view.putString(UUID, uuid.toString());
        }

        if (globalEvent != null && globalEvent.type() != null) {
            if (globalEvent.type() instanceof Event originalEvent) {
                Event s = createSafeEvent(originalEvent);
                Duplet<Eventer, List<String>> d = new Duplet<>(s, globalEvent.parametrize() != null ? globalEvent.parametrize() : new ArrayList<>());
                view.put(GLOBAL_EVENT_KEY, Event.GLOBAL_EVENT_CODEC, d);
            }
        }

        if (!playerEvents.isEmpty()) {
            view.put(PLAYER_EVENTS_KEY,
                Event.PLAYER_EVENTS_CODEC,
                playerEvents);
        }

        if (!TRADES.isEmpty()) {
            Map<String, List<Duplet<Trade, Byte>>> dataToWrite = new HashMap<>();
            TRADES.forEach((playerName, playerTrades) -> {
                List<Duplet<Trade, Byte>> list = new ArrayList<>();
                playerTrades.forEach((trade, value) -> list.add(new Duplet<>(trade, value)));
                dataToWrite.put(playerName, list);
            });
            Codec<Duplet<Trade, Byte>> entryCodec = RecordCodecBuilder.create(instance -> instance.group(
                Trade.CODEC.fieldOf("trade").forGetter(Duplet::type),
                Codec.BYTE.fieldOf("material").forGetter(Duplet::parametrize)
            ).apply(instance, Duplet::new));
            Codec<Map<String, List<Duplet<Trade, Byte>>>> personalTradesCodec = Codec.unboundedMap(Codec.STRING, Codec.list(entryCodec));

            view.put(TRADES_KEY, personalTradesCodec, dataToWrite);
        }

        if (!LEADERBOARD.isEmpty()) {
            Codec<Map<String, Integer>> leaderboardCodec = Codec.unboundedMap(
                Codec.STRING,
                Codec.INT
            );

            view.put(LEADERBOARD_KEY, leaderboardCodec, LEADERBOARD);
        }
    }

    @Contract("_ -> new")
    @Environment(EnvType.SERVER)
    private static @NotNull Event createSafeEvent(@NotNull Event event) {
        ItemStack reward = event.getReward().copy();
        if (reward.getCount() > reward.getMaxCount()) {
            reward.setCount(reward.getMaxCount());
        }

        return new Event(
            event.time(),
            event.type(),
            event.event(),
            event.data(),
            reward,
            event.count()
        );
    }

    @Environment(EnvType.CLIENT)
    public static boolean claimReward(EventBlockEntity be, String playerName, int eventIndex) {
        if (eventIndex <= 4 && eventIndex >= 0) {
            Eventer event = eventIndex == 4 ? RadikClient.GLOBAL_CHALLENGE : RadikClient.CHALLENGES[eventIndex];
            if (event != null && event.isCompleted() && !event.isClaimed()) {
                event.setClaimed(true);
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (world != null && !world.isClient()) {
            EventBlockEntityServerBridge.getHandler().onMarkRemoved(pos);
        }
    }

    public interface EventBlockEntityServerHandler {
        void onMarkRemoved(BlockPos pos);
    }

    static class NoopEventBlockEntityServerHandler implements EventBlockEntityServerHandler {
        @Override
        public void onMarkRemoved(BlockPos pos) {}
    }

    public static class EventBlockEntityServerBridge {
        private static EventBlockEntityServerHandler handler = new NoopEventBlockEntityServerHandler();

        public static void setHandler(EventBlockEntityServerHandler newHandler) {
            handler = newHandler;
        }

        public static EventBlockEntityServerHandler getHandler() {
            return handler;
        }
    }
}
