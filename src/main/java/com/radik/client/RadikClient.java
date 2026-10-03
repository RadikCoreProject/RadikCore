package com.radik.client;

import com.radik.InitScanner;
import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.block.custom.blockentity.embassy.EmbassyScreen;
import com.radik.block.custom.blockentity.event.*;
import com.radik.client.logic.OnWorldTick;
import com.radik.client.logic.Tooltips;
import com.radik.client.particle.RegisterParticles;
import com.radik.client.render.StorageBlockEntityRenderer;
import com.radik.client.screen.game.TeleporterScreen;
import com.radik.connecting.client.Decoration;
import com.radik.connecting.client.Player;
import com.radik.connecting.client.PlayerSettings;
import com.radik.connecting.event.Eventer;
import com.radik.connecting.event.Trade;
import com.radik.entity.RegisterEntities;
import com.radik.entity.projictile.bullet.BulletEntityModel;
import com.radik.entity.projictile.bullet.BulletEntityRenderer;
import com.radik.entity.projictile.ice_shard.IceShardModel;
import com.radik.entity.projictile.ice_shard.IceShardRenderer;
import com.radik.entity.projictile.water_drop.WaterDropEntityModel;
import com.radik.entity.projictile.water_drop.WaterDropEntityRenderer;
import com.radik.fluid.RegisterFluids;
import com.radik.packets.*;
import com.radik.packets.payload.*;
import com.radik.property.client.SettingsProperty;
import com.radik.util.Duplet;
import com.radik.util.Triplet;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static com.radik.block.custom.winter.Pedestal.PEDESTAL;
import static com.radik.property.client.ClientProperties.SETTINGS_PROPERTY;
import static com.radik.ui.Handlers.*;
import static com.radik.ui.Handlers.LEADERBOARD_SCREEN_HANDLER;

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
        InitScanner.scanAll("com.radik", InitScanner.Dist.CLIENT);

        BlockRenderLayerMap.putBlock(RegisterBlocks.BATUT, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlocks(BlockRenderLayer.TRANSLUCENT, RegisterFluids.HELIUM_BLOCK, RegisterFluids.HYDROGEN_BLOCK);

        HandledScreens.register(EMBASSY_SCREEN_HANDLER, EmbassyScreen::new);
        HandledScreens.register(EVENT_SCREEN_HANDLER, EventScreen::new);
        HandledScreens.register(SHOP_SCREEN_HANDLER, ShopScreen::new);
        HandledScreens.register(SHOP_ACCESS_SCREEN_HANDLER, ShopAccessScreen::new);
        HandledScreens.register(CHALLENGES_SCREEN_HANDLER, ChallengesScreen::new);
        HandledScreens.register(LEADERBOARD_SCREEN_HANDLER, LeaderboardScreen::new);

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> colorProvider());
        BlockEntityRendererRegistry.register(BlockEntities.STORAGE_BLOCK_ENTITY, StorageBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(BulletEntityModel.BULLET_LAYER, BulletEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(IceShardModel.ICE_SHARD_LAYER, IceShardModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WaterDropEntityModel.WATER_DROP_LAYER, WaterDropEntityModel::getTexturedModelData);
        EntityRendererFactories.register(RegisterEntities.BULLET, BulletEntityRenderer::new);
        EntityRendererFactories.register(RegisterEntities.ICE_SHARD, IceShardRenderer::new);
        EntityRendererFactories.register(RegisterEntities.WATER_DROP, WaterDropEntityRenderer::new);

        BlockRenderLayerMap.putBlocks(BlockRenderLayer.TRANSLUCENT,
            RegisterBlocks.RAINBOW_STAINED_GLASS,
            RegisterBlocks.RAINBOW_STAINED_GLASS_PANE,
            RegisterBlocks.PEDESTAL,
            RegisterBlocks.GARLAND,
            RegisterBlocks.EVENT_BLOCK
        );
    }

    // TODO: вынести пакеты
    @ClientInit
    public static void registerPackets() {
        if (!Radik.enablePackets) return;
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
                        Radik.LOGGER.error("Failed to save password: {}", e.getMessage());
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
            Integer i = payload.f();
            Integer j = payload.s();
            PacketType type = payload.type();
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
            int p = SettingsProperty.getOrdinal(SETTINGS_PROPERTY.getString(SettingsProperty.EVENT_PARTICLES));
            World world = context.player().getEntityWorld();
            int min = p * p;
            int max = p * p * p;

            switch (eventId) {
                case 0 -> ParticleUtil.spawnParticle(world, pos, ParticleTypes.LANDING_HONEY, UniformIntProvider.create(min, max));
                case 2 -> ParticleUtil.spawnParticle(world, pos, RegisterParticles.FLOWERY_PARTICLE, UniformIntProvider.create(min, max));
                case 3 -> ParticleUtil.spawnParticle(world, pos, ParticleTypes.COMPOSTER, UniformIntProvider.create(min, max));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(TradeListPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player == null) return;
                TRADES = payload.trades();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ChallengesDataPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().player == null) return;
                CHALLENGES = payload.playerEvents();
                GLOBAL_CHALLENGE = payload.globalEvent();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(LeaderboardPayload.ID, (payload, context) -> {
            Triplet<Integer, Integer, LinkedHashMap<String, Integer>> data = payload.leaderboard();
            if (data.getCount() == null || data.parametrize() == null || data.type() == null) return;
            LEADERBOARD = new Triplet<>(data.type(), data.parametrize(), data.getCount());

            context.client().execute(() -> {
                if (context.client().player == null) return;
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
                            SETTINGS_PROPERTY.getBoolean(SettingsProperty.NETHER_PLACES),
                            SETTINGS_PROPERTY.getBoolean(SettingsProperty.OVERWORLD_PLACES),
                            SETTINGS_PROPERTY.getBoolean(SettingsProperty.PRESENT_NOTIFY),
                            SETTINGS_PROPERTY.getBoolean(SettingsProperty.ME_NOTIFY)
                        )));
                    } catch (Exception e) {
                        Radik.LOGGER.error(e.getMessage());
                    }
                }
            }
        }));
    }

    // TODO: вынести кейбиндинги
    @ClientInit
    public static void registerLogic() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.radik.change_ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CORE_CATEGORY
        ));

        OnWorldTick.register();
        Tooltips.register();
    }

    public void colorProvider() {
        BlockColors blockColors = MinecraftClient.getInstance().getBlockColors();
        blockColors.registerColorProvider(
            (state, world, pos, tintIndex) -> state.get(PEDESTAL).color(),
            RegisterBlocks.PEDESTAL);
    }
}