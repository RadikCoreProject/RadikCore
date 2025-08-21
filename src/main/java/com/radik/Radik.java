package com.radik;


import com.radik.registration.RegistrationController;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.radik.Data.SERVER;

public class Radik implements ModInitializer {
	public static final String MOD_ID = "radik";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean enablePackets = true;// включишь потом

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
}
