package com.radik.client;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.block.custom.blockentity.event.*;
import com.radik.client.logic.OnWorldTick;
import com.radik.client.screen.game.TeleporterScreen;
import com.radik.connecting.Decoration;
import com.radik.connecting.Player;
import com.radik.connecting.PlayerSettings;
import com.radik.connecting.event.Eventer;
import com.radik.connecting.event.Trade;
import com.radik.entity.RegisterEntities;
import com.radik.entity.projictile.bullet.BulletEntityModel;
import com.radik.entity.projictile.bullet.BulletEntityRenderer;
import com.radik.entity.projictile.ice_shard.IceShardModel;
import com.radik.entity.projictile.ice_shard.IceShardRenderer;
import com.radik.packets.*;
import com.radik.packets.payload.*;
import com.radik.property.Property;
import com.radik.property.SettingsProperties;
import com.radik.registration.ClientRegistrationController;
import com.radik.packets.PacketType;
import com.radik.util.Duplet;
import com.radik.util.Triplet;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Environment(EnvType.CLIENT)
public class RadikClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static HashMap<Decoration, Boolean> DECORATIONS;
    public static Player PLAYER;

    public static Triplet<Integer, Integer, LinkedHashMap<String, Integer>> LEADERBOARD;
    public static List<Trade> TRADES = new ArrayList<>();
    public static Eventer[] CHALLENGES;
    public static Eventer GLOBAL_CHALLENGE;

    public static KeyBinding.Category CORE_CATEGORY = new KeyBinding.Category(Identifier.of(Radik.MOD_ID, "core"));
    public static KeyBinding keyBinding;

    // TODO: вынести регистрацию логики в отдельный регистратор
    @Override
    public void onInitializeClient() {
        ClientRegistrationController.init(); // регистрация
        if (Radik.enablePackets) registerPackets();
        registerLogic();

//        ClientCommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess) -> {
//            TestTeleportCommand.register(commandDispatcher, commandRegistryAccess);
//            TestNoFallCommand.register(commandDispatcher, commandRegistryAccess);
//        }));

        EntityModelLayerRegistry.registerModelLayer(BulletEntityModel.BULLET_LAYER, BulletEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(IceShardModel.ICE_SHARD_LAYER, IceShardModel::getTexturedModelData);
        // TODO: update deprecated method
        EntityRendererRegistry.register(RegisterEntities.BULLET, BulletEntityRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.ICE_SHARD, IceShardRenderer::new);

        BlockRenderLayerMap.putBlocks(BlockRenderLayer.TRANSLUCENT,
            RegisterBlocks.RAINBOW_STAINED_GLASS,
            RegisterBlocks.RAINBOW_STAINED_GLASS_PANE,
            RegisterBlocks.FREEZER,
            RegisterBlocks.GARLAND,
            RegisterBlocks.EVENT_BLOCK
        );
    }

    // TODO: вынести пакеты
    private void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(StringPayload.ID, ((payload, context) -> {
            ClientPlayerEntity player = context.player();
            Duplet<String, PacketType> d = payload.packet();
            String s = d.type();
            PacketType p = d.parametrize();
            if (s == null || p == null) return;
            switch (p) {
                case PASSWORD -> {
                    try {
                        File file = new File("core/pwd/" + player.getName().getString() + ".PWD");
                        File parentDir = file.getParentFile();
                        if (parentDir != null && !parentDir.exists()) {
                            if (!parentDir.mkdirs()) {
                                throw new IOException("Cannot create directories: " + parentDir.getPath());
                            }
                        }
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(s);
                        }
                    } catch (IOException e) {
                        Radik.LOGGER.error("Failed to save password: " + e.getMessage());
                    }
                }
            }
        }));

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
                case 0 -> ParticleUtil.spawnParticle(context.player().getEntityWorld(), pos, ParticleTypes.LANDING_HONEY, UniformIntProvider.create(p * p, p * p * p));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(TradeListPayload.ID, (payload, context) -> {
            try {
                MinecraftClient client = context.client();
                if (client == null || client.player == null) return;

                boolean b = TRADES.isEmpty();
                TRADES = payload.trades();
//                client.player.sendMessage(Text.of(b ? "Торги для блока событий получены!" : "Торги были обновлены!"), false);
            } catch (Exception e) {
                Radik.LOGGER.error("Error handling TradeListPayload: {}", e.getMessage());
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ChallengesDataPayload.ID, (payload, context) -> {
            try {
                MinecraftClient client = context.client();
                if (client == null || client.player == null) return;

                boolean b = CHALLENGES == null;
                CHALLENGES = payload.playerEvents();
                GLOBAL_CHALLENGE = payload.globalEvent();
//                client.player.sendMessage(Text.of(b ? "Задания для блока событий получены!" : "Задания были обновлены!"), false);
            } catch (Exception e) {
                Radik.LOGGER.error("Error handling ChallengesDataPayload: {}", e.getMessage());
            }
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

        ClientPlayNetworking.registerGlobalReceiver(ActionPayload.ID, ((actionPayload, context) -> {
            Action action = actionPayload.action();
            MinecraftClient client = context.client();
            ClientPlayerEntity player = client.player;
            if (player == null) return;

            switch (action) {
                case SEND_PASSWORD -> {
                    String playerName = player.getName().getString();

                    context.client().send(() -> {
                        File file = new File("core/pwd/" + playerName + ".PWD");
                        if (!file.exists()) return;
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            String pwd = reader.readLine();
                            if (pwd == null || pwd.trim().isEmpty()) return;
                            ClientPlayNetworking.send(new StringPayload(new Duplet<>(pwd.trim(), PacketType.PASSWORD)));
                        } catch (IOException e) {
                            Radik.LOGGER.error("Failed to read password: " + e.getMessage());
                        }
                    });
                }
                case LOGIN -> {
                    try {
                        ClientPlayNetworking.send(new LoginPayload(PLAYER));
                        ClientPlayNetworking.send(new PlayerSettingsPayload(new PlayerSettings(
                            SettingsProperties.getBoolean(Property.NETHER_PLACES),
                            SettingsProperties.getBoolean(Property.OVERWORLD_PLACES),
                            SettingsProperties.getBoolean(Property.PRESENT_NOTIFY),
                            SettingsProperties.getBoolean(Property.ME_NOTIFY)
                        )));
                    } catch (Exception e) {
                        Radik.LOGGER.error(e.getMessage());
                    }
                }
            }
        }));
    }

    // TODO: вынести кейбиндинги
    private void registerLogic() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.radik.change_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CORE_CATEGORY
        ));

        OnWorldTick.register();
    }
}