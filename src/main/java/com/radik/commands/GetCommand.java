package com.radik.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.radik.item.RegisterItems;
import com.radik.item.custom.Medal;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.radik.Data.*;

public class GetCommand {
    protected static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredRegistryAccess, CommandManager.RegistrationEnvironment ignoredEnvironment) {

        dispatcher.register(CommandManager.literal("get")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("teleporter")
                    .then(CommandManager.literal("get")
                                .then(CommandManager.argument("player", StringArgumentType.string())
                                        .suggests(GetCommand::suggestOnlinePlayers)
                                        .then(CommandManager.argument("level", IntegerArgumentType.integer(0, 2))
                                                .suggests((context, builder) -> suggestLevels(builder))
                                                .executes(context -> executeGet(
                                                    context.getSource(),
                                                    StringArgumentType.getString(context, "player"),
                                                    IntegerArgumentType.getInteger(context, "level"))))))
                        .then(CommandManager.literal("upgrade")
                                .executes(context -> executeUpgrade(context.getSource()))))
                .then(CommandManager.literal("medal")
                        .then(CommandManager.argument("player", StringArgumentType.string())
                                .suggests(GetCommand::suggestOnlinePlayers)
                                .then(CommandManager.argument("type", IntegerArgumentType.integer(0, Medal.MAX_TYPE))
                                        .then(CommandManager.argument("material", IntegerArgumentType.integer(0, Medal.MAX_MATERIAL))
                                                .then(CommandManager.argument("text", StringArgumentType.string())
                                                .executes(context -> executeMedal(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "player"),
                                                        IntegerArgumentType.getInteger(context, "type"),
                                                        IntegerArgumentType.getInteger(context, "material"),
                                                        StringArgumentType.getString(context, "text")
                                                ))))))));
    }

    private static CompletableFuture<Suggestions> suggestOnlinePlayers(@NotNull CommandContext<ServerCommandSource> context, @NotNull SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();

        SERVER.getPlayerManager().getPlayerList().forEach(player -> {
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

    private static int executeGet(@NotNull ServerCommandSource source, String name, int level) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.of("Player is null"));
            return 0;
        }
        ItemStack stack = RegisterItems.TELEPORTER.getDefaultStack();
        stack.set(TELEPORTER, level);
        stack.set(OWNER, name);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        source.sendFeedback(() -> Text.of("Телепортер выдан."), true);
        return 1;
    }


    private static int executeUpgrade(ServerCommandSource source) {
        if (source.getPlayer() == null) {
            source.sendError(Text.literal("Player is null"));
            return 0;
        }
        ItemStack stack = source.getPlayer().getStackInHand(Hand.MAIN_HAND);
        Integer s = stack.get(TELEPORTER);
        if (s == null) {
            source.sendError(Text.literal("У тебя в руке не телепортер!"));
            return 0;
        }
        if (s == 2) {
            source.sendError(Text.literal("У тебя уже максимальный уровень телепортера"));
            return 0;
        }
        stack.set(TELEPORTER, s + 1);
        source.sendFeedback(() -> Text.literal("Уровень телепортера увеличен до " + (s + 2)), true);
        return 1;
    }

    private static int executeMedal(ServerCommandSource source, String name, int type, int material, String text) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.literal("Player is null"));
            return 0;
        }
        Item stack = player.getStackInHand(Hand.MAIN_HAND).getItem();
        if (!stack.equals(Items.AIR))  {
            source.sendError(Text.literal("Убери предметы из руки!"));
            return 0;
        } else if (type > Medal.MAX_TYPE || type < 0 || material > Medal.MAX_MATERIAL || material < 0) {
            source.sendError(Text.literal("неверные значения"));
            return 0;
        }
        ItemStack stack2 = RegisterItems.MEDAL.getDefaultStack();
        stack2.set(OWNER, name);
        stack2.set(MEDAL, type);
        stack2.set(MEDAL_MATERIAL, material);
        stack2.set(TEXT, text);
        player.setStackInHand(Hand.MAIN_HAND, stack2);
        source.sendFeedback(() -> Text.of("Медаль выдана."), true);
        return 1;
    }
}
