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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ManaCommand {
    protected static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredRegistryAccess, CommandManager.RegistrationEnvironment ignoredEnvironment) {
        dispatcher.register(CommandManager.literal("mana")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                        .suggests(ManaCommand::suggestPlayers)
                        .executes(context -> {
                            boolean b = BoolArgumentType.getBool(context, "bool");
                            return setMana(b);
                        })
                )
        );
    }

    private static int setMana(boolean b) {
        OnWorldTick.MANA = b;
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestPlayers(CommandContext<ServerCommandSource> ignoredContext, @NotNull SuggestionsBuilder builder) {
        List<String> bool = List.of("true", "false");
        bool.forEach(builder::suggest);
        return builder.buildFuture();
    }
}
