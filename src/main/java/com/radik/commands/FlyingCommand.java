package com.radik.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FlyingCommand {
    protected static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("getfly")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("player", StringArgumentType.string())
                        .suggests(FlyingCommand::suggestPlayers)
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "player");
                            return giveFlyAbility(context.getSource(), playerName);
                        })
                )
        );
    }

    private static CompletableFuture<Suggestions> suggestPlayers(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        List<String> playerNames = context.getSource().getServer().getPlayerManager().getPlayerList().stream()
                .map(ServerPlayerEntity::getName)
                .map(Text::getString)
                .toList();

        for (String playerName : playerNames) {
            builder.suggest(playerName);
        }
        return builder.buildFuture();
    }

    private static int giveFlyAbility(ServerCommandSource source, String playerName) {
        ServerPlayerEntity targetPlayer = source.getServer().getPlayerManager().getPlayer(playerName);

        if (targetPlayer != null) {
            PlayerAbilities abilities = targetPlayer.getAbilities();
            if (abilities.allowFlying) {
                abilities.allowFlying = false;
                targetPlayer.changeGameMode(GameMode.SURVIVAL);
                targetPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200));
                targetPlayer.sendMessage(Text.literal("У вас отобрали полёт!"));
                source.sendMessage(Text.literal("Полет отобран у игрока " + playerName));
            } else {
                abilities.allowFlying = true;
                targetPlayer.changeGameMode(GameMode.ADVENTURE);
                targetPlayer.sendMessage(Text.literal("Вам выдан полёт!"));
                source.sendMessage(Text.literal("Полет выдан игроку " + playerName));
            }
            targetPlayer.sendAbilitiesUpdate();
            return Command.SINGLE_SUCCESS;
        } else {
            source.sendError(Text.literal("Игрок с ником " + playerName + " не найден."));
            return 0;
        }
    }
}
