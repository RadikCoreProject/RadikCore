package com.radik.logic;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Arrays;

import static com.radik.Radik.SERVER;

public class Craft {
    public static void kick(String name, String type) {
        if(Arrays.asList(SERVER.getPlayerNames()).contains(name)) {
            ServerPlayerEntity player = SERVER.getPlayerManager().getPlayer(name);
            assert player != null;
            if(type.equals("leave")) {
                player.networkHandler.disconnect(Text.literal("ВЫ ВЫШЛИ ИЗ АККАУКНТА"));
            }
            else {
                player.networkHandler.disconnect(Text.literal("АККАУНТ БЫЛ УДАЛЁН."));
            }
        }
    }
}
