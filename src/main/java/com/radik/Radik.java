package com.radik;

import com.radik.block.custom.blockentity.event.ChallengesScreenHandler;
import com.radik.block.custom.blockentity.event.EventScreenHandler;
import com.radik.block.custom.blockentity.event.LeaderboardScreenHandler;
import com.radik.block.custom.blockentity.event.ShopScreenHandler;
import com.radik.packets.payload.EventPayload;
import com.radik.registration.RegistrationController;
import com.radik.ui.Handlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.radik.Data.SERVER;

public final class Radik implements ModInitializer {
	public static ScreenHandlerType<EventScreenHandler> EVENT_SCREEN_HANDLER = Handlers.register("radik:event_screen", EventScreenHandler::new);
	public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN_HANDLER = Handlers.register("radik:event_shop_screen", ShopScreenHandler::new);
	public static ScreenHandlerType<ShopScreenHandler.ShopAccessScreenHandler> SHOP_ACCESS_SCREEN_HANDLER = Handlers.register("radik:event_shop_access_screen", ShopScreenHandler.ShopAccessScreenHandler::new);
	public static ScreenHandlerType<ChallengesScreenHandler> CHALLENGES_SCREEN_HANDLER = Handlers.register("radik:challenges_screen", ChallengesScreenHandler::new);
	public static ScreenHandlerType<LeaderboardScreenHandler> LEADERBOARD_SCREEN_HANDLER = Handlers.register("radik:leaderboard_screen", LeaderboardScreenHandler::new);

	public static final Random RANDOM = new Random();
	public static final String MOD_ID = "radik";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean enablePackets = true;
	public static final BlockPos EVENT_BLOCK_POS = new BlockPos(0, 0, 0);

	@Override
	public void onInitialize() {
		RegistrationController.init();
		ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> SERVER = minecraftServer);
	}
}
