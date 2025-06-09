package com.radik.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class RegisterCommands {
    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FlyingCommand.register(dispatcher, registryAccess, environment);
            MinigamesCommand.register(dispatcher, registryAccess, environment);
        });
    }
}
