package com.radik.logic;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static com.radik.Radik.SERVER;

public class TransChatMessages {
    private static final HashMap<String, String> SENDERS = new HashMap<>();

    static {
        SENDERS.put("TASHERBGA", "4§l");
        SENDERS.put("SkyGlue555", "5§l");
        SENDERS.put("SerzhTheGreat", "6");
        SENDERS.put("Yar1kGG", "e");
        SENDERS.put("X_xPIXx_X", "5§l");
    }

    protected static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(TransChatMessages::allowing);
    }

    private static boolean allowing(@NotNull SignedMessage signedMessage, @NotNull ServerPlayerEntity serverPlayerEntity, MessageType.Parameters parameters) {
        String text = signedMessage.getContent().getString();
        String name = serverPlayerEntity.getName().getString();
        if (!text.startsWith("/")) {
            CompletableFuture.runAsync(() -> bot.logic.TransChatMessages.sendTelegram(text, name));
        }

        String coloured = setColour(text, name, "");
        sendMsg(coloured);
        return false;
    }

    public static void sendMinecraft(String message, String player) {
        String text = setColour(message, player, "§a{TG}");
        sendMsg(text);
    }

    private static @NotNull String setColour(String text, @NotNull String name, String prefix) {
        String msg;
        if (name.equals("BulkGecko6535")) {
            msg = "§1§lBulk§4§lGecko§0§l6535";
            prefix = prefix.isEmpty() ? "§9§l⚝§4§l『Noob〃』§9§l⚝" : prefix;
        } else if (name.equals("Boomboxcuff")) {
            msg = "§eBoomb§9oxcuff";
        }
        else msg = SENDERS.containsKey(name) ? String.format("§%s%s", SENDERS.get(name), name) : name;
        return prefix.isEmpty() ? String.format("<%s§r> %s", msg, text) : String.format("%s§r <%s§r> %s", prefix, msg, text);
    }


    private static void sendMsg(String msg) {
        for (ServerPlayerEntity p : SERVER.getPlayerManager().getPlayerList()) {
            p.sendMessage(Text.literal(msg), false);
        }
    }
}
