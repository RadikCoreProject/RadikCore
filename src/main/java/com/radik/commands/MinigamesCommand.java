package com.radik.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.radik.Radik.SERVER;
import static com.radik.Radik.command;

public class MinigamesCommand {
    private static final List<String> MINIGAMES = Arrays.asList("floorIsLava");
    public static boolean MINIGAME = false;

    //                           tick          tick/lvl  color
    public static final HashMap<Integer, HashMap<Integer, String>> FLOOR_IS_LAVA_INFO = new HashMap<>();
    public static int FLOOR_IS_LAVA_TICK = 0;
    public static int FLOOR_IS_LAVA_LVL = -64;
    public static boolean FLOOR_IS_LAVA_EVENT = false;

    static {
        FLOOR_IS_LAVA_INFO.put(0, new HashMap<>(){{put(80, "§1");}});
        FLOOR_IS_LAVA_INFO.put(40, new HashMap<>(){{put(120, "§b");}});
        FLOOR_IS_LAVA_INFO.put(80, new HashMap<>(){{put(350, "§2");}});
        FLOOR_IS_LAVA_INFO.put(110, new HashMap<>(){{put(100, "§e");}});
        FLOOR_IS_LAVA_INFO.put(150, new HashMap<>(){{put(80, "§6");}});
        FLOOR_IS_LAVA_INFO.put(200, new HashMap<>(){{put(40, "§c");}});
        FLOOR_IS_LAVA_INFO.put(275, new HashMap<>(){{put(60, "§4");}});
        FLOOR_IS_LAVA_INFO.put(315, new HashMap<>(){{put(80, "§0");}});
    }

    protected static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("minigames")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("minigame", StringArgumentType.string())
                        .suggests(MinigamesCommand::suggestMinigame)
                        .executes(context -> {
                            String minigame = StringArgumentType.getString(context, "minigame");
                            return createMinigame(context.getSource(), minigame);
                        })
                )
        );
    }

    private static CompletableFuture<Suggestions> suggestMinigame(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (String games : MINIGAMES) {
            builder.suggest(games);
        }
        return builder.buildFuture();
    }

    private static int createMinigame(ServerCommandSource source, String minigame) {
        switch (minigame) {
            case "floorIsLava" -> floorIsLava(source);
            case null, default -> source.sendError(Text.literal("НЕИЗВЕСТНАЯ МИНИИГРА"));
        }
        return 1;
    }

    private static void floorIsLava(ServerCommandSource source) {
        if(FLOOR_IS_LAVA_EVENT) { source.sendError(Text.literal("ИГРА УЖЕ ЗАПУЩЕНА")); }
        else {
            FLOOR_IS_LAVA_EVENT = true;
            if(!MINIGAME) { MINIGAME = true; }
            source.sendMessage(Text.literal("Миниигра Floor is Lava запущена!"));
            command("title @a title \"§4FLOOR IS LAVA!\"");
            for(ServerPlayerEntity q: SERVER.getPlayerManager().getPlayerList()) {
                q.sendMessage(Text.literal("Этап I: базовое развитие.\nДлина: 5 минут"));
                FLOOR_IS_LAVA_TICK = -12000;
            }
        }
    }
}
