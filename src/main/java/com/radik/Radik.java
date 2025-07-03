package com.radik;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.radik.block.RegisterBlocks;
import com.radik.commands.RegisterCommands;
import com.radik.fluid.RegisterFluids;
import com.radik.item.RegisterItems;
import com.radik.logic.LogicInitialize;
import com.radik.world.WorldGenRegister;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Radik implements ModInitializer {
	public static final String MOD_ID = "radik";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer SERVER;
	private static ServerCommandSource commandSource;
	public static CommandDispatcher<ServerCommandSource> dispatcher;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarted);
		RegisterBlocks.registerBlock();
		RegisterItems.registerItem();
		Music.registerSounds();
		ModTags.registerTags();
		ModGroup.registerGroup();
		LogicInitialize.registerLogic();
		RegisterCommands.registerCommand();
		RegisterFluids.register();

		// Добавляем в биомы
		WorldGenRegister.registerInBiomes();
	}


	public static void command(String command) {
        command(command, commandSource);
	}

	public static void command(String command, ServerCommandSource source) {
		ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);
		LOGGER.info(command);
		try { dispatcher.execute(parseResults); } catch (CommandSyntaxException e) { Radik.LOGGER.error(e.getMessage()); }
	}


	private void onServerStarted(MinecraftServer minecraftServer) {
		SERVER = minecraftServer;
		commandSource = Objects.requireNonNull(SERVER).getCommandSource();

		dispatcher = Objects.requireNonNull(SERVER).getCommandManager().getDispatcher();
	}
}
