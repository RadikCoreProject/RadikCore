package com.radik.commands;

import com.radik.RadikCore;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class RegisterCommands {
    @RadikCore
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FlyingCommand.register(dispatcher, registryAccess, environment);
            MinigamesCommand.register(dispatcher, registryAccess, environment);
        });
    }
}
