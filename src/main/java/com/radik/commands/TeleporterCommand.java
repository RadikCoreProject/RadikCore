package com.radik.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.radik.Data;
import com.radik.item.RegisterItems;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.concurrent.CompletableFuture;

import static com.radik.Data.*;

public class TeleporterCommand {
    protected static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        dispatcher.register(CommandManager.literal("teleporter")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("get")
                        .then(CommandManager.argument("player", StringArgumentType.string())
                                .suggests(TeleporterCommand::suggestOnlinePlayers)
                                .then(CommandManager.argument("level", IntegerArgumentType.integer(0, 2))
                                        .suggests((context, builder) -> suggestLevels(builder))
                                        .executes(context -> executeGet(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "player"),
                                                IntegerArgumentType.getInteger(context, "level"))))))
                .then(CommandManager.literal("upgrade")
                        .executes(context -> executeUpgrade(context.getSource()))));
    }

    private static CompletableFuture<Suggestions> suggestOnlinePlayers(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        ServerCommandSource source = context.getSource();
        String input = builder.getRemaining().toLowerCase();

        source.getServer().getPlayerManager().getPlayerList().forEach(player -> {
            String name = player.getName().getString();
            if (name.toLowerCase().contains(input)) {
                builder.suggest(name);
            }
        });

        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestLevels(SuggestionsBuilder builder) {
        for (int i = 0; i <= 2; i++) {
            if (String.valueOf(i).startsWith(builder.getRemaining())) {
                builder.suggest(i);
            }
        }
        return builder.buildFuture();
    }

    private static int executeGet(ServerCommandSource source, String name, int level) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.of("Player is null"));
            return 0;
        }
        ItemStack stack = RegisterItems.TELEPORTER.getDefaultStack();
        stack.set(TELEPORTER, level);
        stack.set(OWNER, name);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        source.sendFeedback(() -> Text.of("Телепортер выдан."), false);
        return 1;
    }

    private static int executeUpgrade(ServerCommandSource source) {
        if (source.getPlayer() == null) {
            source.sendError(Text.of("Player is null"));
            return 0;
        }
        ItemStack stack = source.getPlayer().getStackInHand(Hand.MAIN_HAND);
        Integer s = stack.get(TELEPORTER);
        if (s == null) {
            source.sendError(Text.of("У тебя в руке не телепортер!"));
            return 0;
        }
        if (s == 2) {
            source.sendError(Text.of("У тебя уже максимальный уровень телепортера"));
            return 0;
        }
        stack.set(TELEPORTER, s + 1);
        source.sendFeedback(() -> Text.of("Уровень телепортера увеличен до " + (s + 2)), false);
        return 1;
    }
}
