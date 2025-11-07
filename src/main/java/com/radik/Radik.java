package com.radik;

import com.radik.packets.payload.EventPayload;
import com.radik.registration.RegistrationController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Random;

import static com.radik.Data.SERVER;

public final class Radik implements ModInitializer {
	public static final Random RANDOM = new Random();
	public static final String MOD_ID = "radik";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean enablePackets = true;// включишь потом
	public static final BlockPos EVENT_BLOCK_POS = new BlockPos(0, 200, 0);
	private static final Text INVALID_PACKET = Text.literal("§4INVALID PACKET ERROR\n§9Why do you want to cheat?\nCheat report sent to Administrator");

	@Override
	public void onInitialize() {
		RegistrationController.init(); // регистрация
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarted);
	}

	private void onServerStarted(MinecraftServer minecraftServer) {
		SERVER = minecraftServer;

		Data.commandSource = Objects.requireNonNull(SERVER).getCommandSource();
		Data.dispatcher = Objects.requireNonNull(SERVER).getCommandManager().getDispatcher();

	}

	public static void sendEventToPlayers(int eventId, BlockPos pos, int data, ServerWorld world) {
		for (ServerPlayerEntity i : SERVER.getPlayerManager().getPlayerList()) {
			if (i.getWorld().getRegistryKey() == world.getRegistryKey()) {
				double d = pos.getX() - i.getX();
				double e = pos.getY() - i.getY();
				double f = pos.getZ() - i.getZ();
				if (d * d + e * e + f * f < 64.0 * 64.0) {
					ServerPlayNetworking.send(i, new EventPayload(eventId, pos, data));
				}
			}
		}
	}

	public static void kickPlayer(@NotNull ServerPlayerEntity player) {
		player.networkHandler.disconnect(INVALID_PACKET);
	}
}
