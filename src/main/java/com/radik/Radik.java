package com.radik;

import com.radik.block.custom.blockentity.event.EventBlockEntity;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.packets.payload.EventPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.radik.Data.SERVER;

public final class Radik implements ModInitializer {
	public static final Random RANDOM = new Random();
	public static final String MOD_ID = "radik";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final BlockPos EVENT_BLOCK_POS = new BlockPos(0, 0, 0);
	public static final boolean enablePackets = true;

	@Override
	public void onInitialize() {
		ModTags.initialize();
		RegisterItems.initialize();
		InitScanner.scanAll("com.radik", InitScanner.Dist.COMMON);
		ServerLifecycleEvents.SERVER_STARTING.register(s -> SERVER = s);
	}

	public static Identifier id(String p) {
		return Identifier.of(Radik.MOD_ID, p);
	}

	public static void sendPacketToNearPlayers(CustomPayload payload, BlockPos pos, double range, ServerWorld world) {
		double r = range * range;
		for (ServerPlayerEntity i : SERVER.getPlayerManager().getPlayerList()) {
			if (i.getEntityWorld().getRegistryKey() == world.getRegistryKey()) {
				double d = pos.getX() - i.getX();
				double e = pos.getY() - i.getY();
				double f = pos.getZ() - i.getZ();
				if (d * d + e * e + f * f < r) ServerPlayNetworking.send(i, payload);
			}
		}
	}

	public static void sendEventToPlayers(int eventId, BlockPos pos, int data, ServerWorld world) {
		if (EventBlockEntity.getEventType() == ChallengeEvent.NONE) return;
		for (ServerPlayerEntity i : SERVER.getPlayerManager().getPlayerList()) {
			if (i.getEntityWorld().getRegistryKey() == world.getRegistryKey()) {
				double d = pos.getX() - i.getX();
				double e = pos.getY() - i.getY();
				double f = pos.getZ() - i.getZ();
				if (d * d + e * e + f * f < 64.0 * 64.0) {
					ServerPlayNetworking.send(i, new EventPayload(eventId, pos, data));
				}
			}
		}
	}
}
