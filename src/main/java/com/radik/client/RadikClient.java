package com.radik.client;

import com.mojang.brigadier.Message;
import com.radik.Radik;
import com.radik.block.custom.blockentity.event.*;
import com.radik.client.logic.OnWorldTick;
import com.radik.client.packet.SendPacket;
import com.radik.client.screen.game.TeleporterScreen;
import com.radik.connecting.Decoration;
import com.radik.connecting.Player;
import com.radik.connecting.event.Event;
import com.radik.connecting.event.Eventer;
import com.radik.connecting.event.Trade;
import com.radik.connecting.event.factory.EventData;
import com.radik.packets.*;
import com.radik.packets.payload.*;
import com.radik.property.client.Property;
import com.radik.property.client.SettingsProperties;
import com.radik.registration.ClientRegistrationController;
import com.radik.util.Triplet;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDateTime;
import java.util.*;

@Environment(EnvType.CLIENT)
public class RadikClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static HashMap<Decoration, Boolean> DECORATIONS;
    public static Player PLAYER;

    public static Triplet<Integer, Integer, LinkedHashMap<String, Integer>> LEADERBOARD;
    public static ArrayList<Event<EventData>> EVENTS;
    public static List<Trade> TRADES = new ArrayList<>();
    public static Eventer[] CHALLENGES;
    public static Eventer GLOBAL_CHALLENGE;

    public static KeyBinding keyBinding;

    // TODO: вынести регистрацию логики в отдельный регистратор
    @Override
    public void onInitializeClient() {
        ClientRegistrationController.init(); // регистрация
        if (Radik.enablePackets) registerPackets();
        registerLogic();
    }

    // TODO: вынести пакеты
    private void registerPackets() {
        SendPacket.register();

        ClientPlayNetworking.registerGlobalReceiver(LoginPayload.ID, (payload, context) -> {
            PLAYER = payload.player();
            Radik.LOGGER.info("received Login Payload");});

        ClientPlayNetworking.registerGlobalReceiver(DecorationsPayload.ID, (payload, context) -> {
            DECORATIONS = payload.decorations();
            Radik.LOGGER.info("received Decoration Payload");});

        ClientPlayNetworking.registerGlobalReceiver(IntegerPayload.ID, (payload, context) -> {
            Triplet<Integer, Integer, PacketType> packet = payload.packet();
            Integer i = packet.getType();
            Integer j = packet.getParametrize();
            PacketType type = packet.getCount();
            if (i != null && type != null && j != null) {
                switch (type) {
                    case TELEPORTER_COOLDOWN -> TeleporterScreen.cooldown = LocalDateTime.now().plusSeconds(i);
                    case GET_TRADES_COUNT -> {
                        ShopAccessScreen.COUNT = j;
                        ShopAccessScreen.TRADE = i;
                    }
                    case GET_CHALLENGE_COUNT -> {
                        if (i == 4) GLOBAL_CHALLENGE.setValue(j);
                        else CHALLENGES[i].setValue(j);
                    }
                }
            }});

        ClientPlayNetworking.registerGlobalReceiver(EventPayload.ID, (payload, context) -> {
            int eventId = payload.eventId();
            BlockPos pos = payload.pos();
            int p = Property.getOrdinal(SettingsProperties.get("event_particles"));
            switch (eventId) {
                case 0 -> ParticleUtil.spawnParticle(context.player().clientWorld, pos, ParticleTypes.LANDING_HONEY, UniformIntProvider.create(p * p, p * p * p));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(TradeListPayload.ID, (payload, context) -> {
            MinecraftClient client = context.client();
            if (client == null || client.player == null) return;
            boolean b = TRADES.isEmpty();
            TRADES = payload.trades();
            client.player.sendMessage(Text.of(b ? "Торги для блока событий получены!" : "Торги были обновлены!"), false);
        });

        ClientPlayNetworking.registerGlobalReceiver(ChallengesDataPayload.ID, (payload, context) -> {
            MinecraftClient client = context.client();
            if (client == null || client.player == null) return;
            boolean b = CHALLENGES == null;
            CHALLENGES = payload.playerEvents();
            GLOBAL_CHALLENGE = payload.globalEvent();
            client.player.sendMessage(Text.of(b ? "Задания для блока событий получены!" : "Задания были обновлены!"), false);
        });

        ClientPlayNetworking.registerGlobalReceiver(LeaderboardPayload.ID, (payload, context) -> {
            Triplet<Integer, Integer, LinkedHashMap<String, Integer>> data = payload.leaderboard();
            if (data.getCount() == null || data.getParametrize() == null || data.getType() == null) return;
            LEADERBOARD = new Triplet<>(data.getType(), data.getParametrize(), data.getCount());

            context.client().execute(() -> {
                if (context.client().currentScreen instanceof LeaderboardScreen screen) {
                    screen.updateLeaderboardData();
                }
            });
        });
    }

    // TODO: вынести кейбиндинги
    private void registerLogic() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.radik.change_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.radik.core"
        ));

        OnWorldTick.register();
    }
}