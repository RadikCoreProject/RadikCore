package com.radik.ui;

import com.radik.Radik;
import com.radik.block.custom.blockentity.event.*;
import com.radik.registration.IRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ClientHandlers implements IRegistry {
    public static ScreenHandlerType<EventScreenHandler> EVENT_SCREEN_HANDLER;
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN_HANDLER;
    public static ScreenHandlerType<ShopScreenHandler.ShopAccessScreenHandler> SHOP_ACCESS_SCREEN_HANDLER;
    public static ScreenHandlerType<ChallengesScreenHandler> CHALLENGES_SCREEN_HANDLER;
    public static ScreenHandlerType<LeaderboardScreenHandler> LEADERBOARD_SCREEN_HANDLER;

    public static void initialize()
    {
        EVENT_SCREEN_HANDLER =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        Identifier.of(Radik.MOD_ID, "event_screen"),
                        new ScreenHandlerType<>(EventScreenHandler::new, FeatureSet.empty())
                );
        SHOP_SCREEN_HANDLER =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        Identifier.of(Radik.MOD_ID, "event_shop_screen"),
                        new ScreenHandlerType<>(ShopScreenHandler::new, FeatureSet.empty())
                );
        SHOP_ACCESS_SCREEN_HANDLER =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        Identifier.of(Radik.MOD_ID, "event_shop_access_screen"),
                        new ScreenHandlerType<>(ShopScreenHandler.ShopAccessScreenHandler::new, FeatureSet.empty())
                );
        CHALLENGES_SCREEN_HANDLER =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        Identifier.of(Radik.MOD_ID, "challenges_screen"),
                        new ScreenHandlerType<>(ChallengesScreenHandler::new, FeatureSet.empty())
                );
        LEADERBOARD_SCREEN_HANDLER =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        Identifier.of(Radik.MOD_ID, "leaderboard_screen"),
                        new ScreenHandlerType<>(LeaderboardScreenHandler::new, FeatureSet.empty())
                );

        HandledScreens.register(EVENT_SCREEN_HANDLER, EventScreen::new);
        HandledScreens.register(SHOP_SCREEN_HANDLER, ShopScreen::new);
        HandledScreens.register(SHOP_ACCESS_SCREEN_HANDLER, ShopAccessScreen::new);
        HandledScreens.register(CHALLENGES_SCREEN_HANDLER, ChallengesScreen::new);
        HandledScreens.register(LEADERBOARD_SCREEN_HANDLER, LeaderboardScreen::new);
    }
}
