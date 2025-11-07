package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.client.RadikClient;
import com.radik.connecting.event.*;
import com.radik.packets.payload.LeaderboardPayload;
import com.radik.util.Duplet;
import com.radik.util.Triplet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.entity.EntityChangeListener;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static com.radik.block.custom.blockentity.event.EventBlock.EVENT_TYPE;

public class EventBlockEntity extends BlockEntity {
    private static final String PLAYER_EVENTS_KEY = "PlayerEvents";
    private static final String GLOBAL_EVENT_KEY = "GlobalEvent";
    private static final String LAST_GEN_DAY_KEY = "LastGenDay";
    private static final String UUID = "TextUUID";
    private static final String TRADES_KEY = "Trades";
    private static final String LEADERBOARD_KEY = "Leaderboard";

    public final Map<String, Eventer[]> playerEvents = new HashMap<>();
    private Duplet<Eventer, List<String>> globalEvent;
    private int lastGenDay = -1;
    private UUID uuid;
    public final HashMap<Trade, Byte> TRADES = new HashMap<>();
    public final HashMap<String, Integer> LEADERBOARD = new HashMap<>();

    public EventBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.EVENT_BLOCK_ENTITY, pos, state);
    }


    public static void tick(ServerWorld world, BlockPos pos, @NotNull BlockState state, EventBlockEntity blockEntity) {
    }

    public static int getEventType(int day) {
        if (day >= 305 && day <= 365) return 1;
        if (day >= 1 && day <= 59) return 2;
        if (day >= 91 && day <= 151) return 3;
        if (day >= 182 && day <= 223) return 4;
        return 0;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.lastGenDay = nbt.getInt(LAST_GEN_DAY_KEY).orElse(0);

        if (nbt.contains(UUID)) {
            String uuidString = nbt.getString(UUID).orElse(null);
            if (uuidString != null && !uuidString.isEmpty()) {
                try {
                    this.uuid = java.util.UUID.fromString(uuidString);
                } catch (IllegalArgumentException e) {
                    Radik.LOGGER.warn("Invalid UUID: {}", uuidString);
                }
            }
        }

        if (nbt.contains(GLOBAL_EVENT_KEY)) {
            NbtCompound globalEventNbt = nbt.getCompound(GLOBAL_EVENT_KEY).orElse(null);
            if (globalEventNbt != null && !globalEventNbt.isEmpty()) {
                Eventer eventer = null;

                if (globalEventNbt.contains("eventer")) {
                    NbtCompound eventerNbt = globalEventNbt.getCompound("eventer").orElse(null);
                    if (eventerNbt != null && !eventerNbt.isEmpty()) {
                        try {
                            eventer = Event.fromNbt(eventerNbt, registries);
                        } catch (Exception e) {
                            Radik.LOGGER.error("Failed to load eventer from NBT", e);
                        }
                    }
                }

                List<String> parametrize = new ArrayList<>();
                if (globalEventNbt.contains("parametrize")) {
                    NbtList parametrizeList = globalEventNbt.getList("parametrize").orElse(null);
                    if (parametrizeList != null) {
                        for (int i = 0; i < parametrizeList.size(); i++) {
                            try {
                                String param = parametrizeList.getString(i).orElse(null);
                                if (param != null) {
                                    parametrize.add(param);
                                }
                            } catch (Exception e) {
                                Radik.LOGGER.warn("Failed to read parametrize at index {}", i, e);
                            }
                        }
                    }
                }

                this.globalEvent = new Duplet<>(eventer, parametrize);
            }
        }

        if (nbt.contains(PLAYER_EVENTS_KEY)) {
            NbtCompound eventsCompound = nbt.getCompound(PLAYER_EVENTS_KEY).orElse(null);
            if (eventsCompound != null) {
                for (String playerName : eventsCompound.getKeys()) {
                    try {
                        NbtList eventList = eventsCompound.getList(playerName).orElse(null);
                        if (eventList != null) {
                            Eventer[] events = new Eventer[4];
                            for (int i = 0; i < eventList.size() && i < 4; i++) {
                                try {
                                    NbtCompound eventNbt = eventList.getCompound(i).orElse(null);
                                    if (eventNbt != null && !eventNbt.isEmpty()) {
                                        events[i] = Event.fromNbt(eventNbt, registries);
                                    }
                                } catch (Exception e) {
                                    Radik.LOGGER.warn("Failed to read event at index {} for player {}", i, playerName, e);
                                }
                            }
                            playerEvents.put(playerName, events);
                        }
                    } catch (Exception e) {
                        Radik.LOGGER.warn("Failed to read events for {}", playerName, e);
                    }
                }
            }
        }

        TRADES.clear();
        if (nbt.contains(TRADES_KEY)) {
            NbtList tradesList = nbt.getList(TRADES_KEY).orElse(null);
            if (tradesList != null) {
                for (int i = 0; i < tradesList.size(); i++) {
                    try {
                        NbtCompound tradeEntry = tradesList.getCompound(i).orElse(null);
                        if (tradeEntry != null) {
                            NbtCompound tradeNbt = tradeEntry.getCompound("trade").orElse(null);
                            if (tradeNbt != null) {
                                Trade trade = Trade.fromNbt(tradeNbt, registries);
                                byte value = tradeEntry.getByte("material").orElse((byte) 0);
                                TRADES.put(trade, value);
                            }
                        }
                    } catch (Exception e) {
                        Radik.LOGGER.warn("Failed to read trade at {}", i, e);
                    }
                }
            }
        }

        if (nbt.contains(LEADERBOARD_KEY)) {
            NbtList leaderboardList = nbt.getList(LEADERBOARD_KEY).orElse(null);
            if (leaderboardList != null) {
                for (int i = 0; i < leaderboardList.size(); i++) {
                    try {
                        NbtCompound leaderboardEntry = leaderboardList.getCompound(i).orElse(null);
                        if (leaderboardEntry != null) {
                            String playerName = leaderboardEntry.getString("trade").orElse(null);
                            int score = leaderboardEntry.getInt("material").orElse(0);
                            if (playerName != null) {
                                LEADERBOARD.put(playerName, score);
                            }
                        }
                    } catch (Exception e) {
                        Radik.LOGGER.warn("Failed to read leaderboard entry at {}", i, e);
                    }
                }
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        nbt.putInt(LAST_GEN_DAY_KEY, lastGenDay);

        if (uuid != null) {
            nbt.putString(UUID, uuid.toString());
        }

        if (globalEvent != null) {
            NbtCompound global = new NbtCompound();
            if (globalEvent.type() != null) {
                Eventer eventer = globalEvent.type();
                if (eventer instanceof Event) {
                    ItemStack reward = eventer.getReward();
                    if (reward.getCount() > reward.getMaxCount()) {
                        reward = reward.copy();
                        reward.setCount(reward.getMaxCount());
                        ((Event<?>) eventer).setReward(reward);
                    }
                }
                global.put("eventer", eventer.toNbt(registries));
            } else {
                global.put("eventer", new NbtCompound());
            }

            NbtList paramList = new NbtList();
            if (globalEvent.parametrize() != null) {
                for (String param : globalEvent.parametrize()) {
                    paramList.add(NbtString.of(param));
                }
            }
            global.put("parametrize", paramList);

            nbt.put(GLOBAL_EVENT_KEY, global);
        }

        NbtCompound events = new NbtCompound();
        for (Map.Entry<String, Eventer[]> entry : playerEvents.entrySet()) {
            NbtList eventList = new NbtList();
            Eventer[] eventers = entry.getValue();

            if (eventers.length == 0) continue;

            for (Eventer event : eventers) {
                if (event != null) {
                    if (event instanceof Event) {
                        ItemStack reward = event.getReward();
                        if (reward.getCount() > reward.getMaxCount()) {
                            reward = reward.copy();
                            reward.setCount(reward.getMaxCount());
                            ((Event<?>) event).setReward(reward);
                        }
                    }
                    eventList.add(event.toNbt(registries));
                } else {
                    eventList.add(new NbtCompound());
                }
            }
            events.put(entry.getKey(), eventList);
        }
        nbt.put(PLAYER_EVENTS_KEY, events);

        NbtList trades = new NbtList();
        for (Map.Entry<Trade, Byte> entry : TRADES.entrySet()) {
            NbtCompound tradeEntry = new NbtCompound();
            tradeEntry.put("trade", entry.getKey().toNbt(registries));
            tradeEntry.putByte("material", entry.getValue());
            trades.add(tradeEntry);
        }
        nbt.put(TRADES_KEY, trades);

        NbtList leaderboard = new NbtList();
        for (Map.Entry<String, Integer> entry : LEADERBOARD.entrySet()) {
            NbtCompound leaderboardEntry = new NbtCompound();
            leaderboardEntry.putString("trade", entry.getKey());
            leaderboardEntry.putInt("material", entry.getValue());
            leaderboard.add(leaderboardEntry);
        }
        nbt.put(LEADERBOARD_KEY, leaderboard);
    }

    @Environment(EnvType.CLIENT)
    public boolean claimReward(String playerName, int eventIndex) {
        if (this.world == null) return false;

        if (eventIndex <= 4 && eventIndex >= 0) {
            Eventer event = eventIndex == 4 ? RadikClient.GLOBAL_CHALLENGE : RadikClient.CHALLENGES[eventIndex];
            if (event != null && event.isCompleted() && !event.isClaimed()) {
                event.setClaimed(true);
                return true;
            } else return false;
        } else return false;
    }
}
