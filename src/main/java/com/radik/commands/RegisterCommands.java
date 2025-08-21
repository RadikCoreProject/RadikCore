package com.radik.commands;

import com.radik.registration.IRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class RegisterCommands implements IRegistry {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FlyingCommand.register(dispatcher, registryAccess, environment);
            TeleporterCommand.register(dispatcher, registryAccess, environment);
            ManaCommand.register(dispatcher, registryAccess, environment);
        });
    }
}
