package com.radik.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.radik.registration.IRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.radik.Data.SERVER;

public class RegisterCommands implements IRegistry {
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            GetCommand.register(dispatcher, registryAccess, environment);
            ManaCommand.register(dispatcher, registryAccess, environment);
        });
    }

    static CompletableFuture<Suggestions> suggestOnlinePlayers(@NotNull CommandContext<ServerCommandSource> context, @NotNull SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();

        SERVER.getPlayerManager().getPlayerList().forEach(player -> {
            String name = player.getName().getString();
            if (name.toLowerCase().contains(input)) {
                builder.suggest(name);
            }
        });

        return builder.buildFuture();
    }
}
