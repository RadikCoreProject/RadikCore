package com.radik.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.radik.logic.OnWorldTick;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ManaCommand {
    protected static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("mana")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .suggests(ManaCommand::suggestPlayers)
                        .executes(context -> {
                            boolean b = BoolArgumentType.getBool(context, "bool");
                            return setMana(context.getSource(), b);
                        })
                )
        );
    }

    private static int setMana(ServerCommandSource source, boolean b) {
        OnWorldTick.MANA = b;
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestPlayers(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        List<String> b = List.of("true", "false");

        for (String bool : b) {
            builder.suggest(bool);
        }
        return builder.buildFuture();
    }
}
