package com.radik.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.radik.property.EventProperties;
import com.radik.property.EventProperty;
import com.radik.property.server.InGameProperties;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CoreCommand {
    protected static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("core")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.literal("event")
                    .then(CommandManager.literal("get")
                        .executes(CoreCommand::getEvent))
                    .then(CommandManager.literal("set")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                            .suggests(CoreCommand::suggestEventProperty)
                            .then(CommandManager.argument("material", IntegerArgumentType.integer(2, 10000))
                                .executes(CoreCommand::updateEvent)))))
                .then(CommandManager.literal("inGame")
                    .then(CommandManager.literal("minecartMultiplier")
                        .then(CommandManager.argument("multiplier", FloatArgumentType.floatArg(0, 1000))
                            .executes(CoreCommand::updateIngame)
                        )
                )
        ));
    }

    private static CompletableFuture<Suggestions> suggestEventProperty(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (EventProperty property : EventProperty.values()) builder.suggest(property.getId());
        return builder.buildFuture();
    }

    private static int updateIngame(CommandContext<ServerCommandSource> context) {
        float multi = FloatArgumentType.getFloat(context, "multiplier");
        InGameProperties.set("minecart_multiplier", String.valueOf(multi));
        context.getSource().sendFeedback(() -> Text.literal("Minecart speed multiplier set to " + multi), true);
        return 1;
    }

    private static int getEvent(@NotNull CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> {
            MutableText text = Text.literal("");
            for (EventProperty property : EventProperty.values()) {
                text.append(property.getId() + ": " + EventProperties.get(property.getId()) + "\n");
            }
            return text;
        }, true);
        return 1;
    }

    private static int updateEvent(CommandContext<ServerCommandSource> context) {
        int multi = IntegerArgumentType.getInteger(context, "material");
        String name = StringArgumentType.getString(context, "name");
        EventProperties.set(name, String.valueOf(multi));
        context.getSource().sendFeedback(() -> Text.literal("Value " + name + " changed to " + multi), true);
        return 1;
    }
}
