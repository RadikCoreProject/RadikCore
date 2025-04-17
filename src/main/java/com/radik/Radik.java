package com.radik;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.radik.block.RegisterBlocks;
import com.radik.commands.RegisterCommands;
import com.radik.item.RegisterItems;
import com.radik.logic.LogicInitialize;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

import static com.radik.ModTags.register;
import static com.radik.Music.registerSounds;


public class Radik implements ModInitializer {
	public static final String MOD_ID = "radik";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
//		initializeAnnotatedMethods();
		registerSounds();
		register();
		LogicInitialize.register();
		ModGroup.initialize();
		RegisterItems.initialize();
		RegisterBlocks.initialize();
		RegisterCommands.initialize();
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarted);
	}

//	private void initializeAnnotatedMethods() {
//		ConfigurationBuilder config = new ConfigurationBuilder();
//		config.setUrls(ClasspathHelper.forPackage("com.radik"));
//		config.setScanners(new MethodAnnotationsScanner());
//		Reflections reflections = new Reflections(config);
//		Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(RadikCore.class);
//
//		for (Method method : annotatedMethods) {
//			try {
//				Class<?> clazz = method.getDeclaringClass();
//				Object instance = clazz.getDeclaredConstructor().newInstance();
//				method.invoke(instance);
//			} catch (Exception ignored) {}
//		}
//	}


	public static void command(String command) {
		ServerCommandSource commandSource = Objects.requireNonNull(SERVER).getCommandSource();
		CommandDispatcher<ServerCommandSource> dispatcher = Objects.requireNonNull(SERVER).getCommandManager().getDispatcher();
		ParseResults<ServerCommandSource> parseResults;
		parseResults = dispatcher.parse(command, commandSource);
		try { dispatcher.execute(parseResults); } catch (CommandSyntaxException e) { throw new RuntimeException(e); }
	}


	private void onServerStarted(MinecraftServer minecraftServer) {
		SERVER = minecraftServer;
	}

}
