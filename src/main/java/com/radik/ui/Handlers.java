package com.radik.ui;

import com.radik.MainInit;
import com.radik.Radik;
import com.radik.block.custom.blockentity.embassy.EmbassyScreenHandler;
import com.radik.block.custom.blockentity.event.ChallengesScreenHandler;
import com.radik.block.custom.blockentity.event.EventScreenHandler;
import com.radik.block.custom.blockentity.event.LeaderboardScreenHandler;
import com.radik.block.custom.blockentity.event.ShopScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static com.radik.connecting.game.EmbassyData.EMBASSY_DATA_PACKET_CODEC;

public class Handlers {
    public static ScreenHandlerType<EventScreenHandler> EVENT_SCREEN_HANDLER = Handlers.register("event_screen", EventScreenHandler::new);
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN_HANDLER = Handlers.register("event_shop_screen", ShopScreenHandler::new);
    public static ScreenHandlerType<ShopScreenHandler.ShopAccessScreenHandler> SHOP_ACCESS_SCREEN_HANDLER = Handlers.register("event_shop_access_screen", ShopScreenHandler.ShopAccessScreenHandler::new);
    public static ScreenHandlerType<ChallengesScreenHandler> CHALLENGES_SCREEN_HANDLER = Handlers.register("challenges_screen", ChallengesScreenHandler::new);
    public static ScreenHandlerType<LeaderboardScreenHandler> LEADERBOARD_SCREEN_HANDLER = Handlers.register("leaderboard_screen", LeaderboardScreenHandler::new);

    public static ScreenHandlerType<EmbassyScreenHandler> EMBASSY_SCREEN_HANDLER =
        new ExtendedScreenHandlerType<>(
            EmbassyScreenHandler::new,
            EMBASSY_DATA_PACKET_CODEC
        );

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType<T> type) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Radik.MOD_ID, id), type);
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory, FeatureFlag... requiredFeatures) {
        return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory, FeatureFlags.FEATURE_MANAGER.featureSetOf(requiredFeatures)));
    }

    @MainInit
    public static void initialize() {
        EMBASSY_SCREEN_HANDLER = register("embassy_screen", EMBASSY_SCREEN_HANDLER);
    }
}
