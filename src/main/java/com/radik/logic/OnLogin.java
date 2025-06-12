package com.radik.logic;

import com.radik.Radik;
import com.radik.util.Duplet;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import static bot.database.Registration.*;
import static bot.database.Settings.*;
import static bot.database.Settings.GetUser.getData;
import static bot.logic.Settings.wrongIp;
import static com.radik.Radik.LOGGER;
import static com.radik.logic.OnWorldTick.KICK;

class OnLogin {

    protected static void register() {
        ServerPlayConnectionEvents.INIT.register(OnLogin::onPlayerInit);
    }


    private static void onPlayerInit(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        String ip = serverPlayNetworkHandler.getPlayer().getIp();
        String username = serverPlayNetworkHandler.getPlayer().getName().getString();
        long userId;
        String[] registerdata = isUserRegistered(ip, username);
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();

        try {
            userId = Long.parseLong(getData(username)[1]);
        } catch (Exception e) {
            userId = 0;
        }

        if (registerdata.length > 1) {
            if (getData(userId)[3].equals("0")) {
                LOGGER.error("abc");
                KICK.put(player, new Duplet<>(5, Text.literal("§4LAW ERROR§r§n\n\nДля игры на сервере необходимо принять правовую информацию.")));
                return;
            }
            updateIp(Long.parseLong(registerdata[1]), ip);
            player.sendMessage(Text.literal("Добро пожаловать в Radik PupsikiSurvival 3.0!\nСпасибо за регистрацию ^-^"));
        }
        else if (registerdata[0].equals("1")) {
            if (getData(userId)[3].equals("0")) {
                KICK.put(player, new Duplet<>(5, Text.literal("§4LAW ERROR§r§n\n\nДля игры на сервере необходимо принять правовую информацию.")));
                return;
            }
            player.sendMessage(Text.literal("Добро пожаловать в Radik PupsikiSurvival 3.0!\nПриятной игры!"));
        }
        else if (registerdata[0].equals("2")) {
            KICK.put(player, new Duplet<>(5, Text.literal("§4WRONG IP ERROR§r§n\n\nЕСЛИ ЭТО ВАШ АККАУНТ:\nзайдите в личку бота и нажмите \"ЭТО Я\"\nи только потом выходите в меню!!!")));
            if(getSetting("wrongIpNotify", userId) == 1) { wrongIp(ip, userId); }
        } else {
            KICK.put(player, new Duplet<>(5, Text.literal("YOU ARE NOT AUTHORIZED")));
        }
    }
}
