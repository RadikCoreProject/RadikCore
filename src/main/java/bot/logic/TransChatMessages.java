package bot.logic;

import bot.database.Minecraft;
import bot.keyboard.Register;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.database.Settings.GetUser.getData;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class TransChatMessages {
    public static synchronized void sendTelegram(String message, String player) {
        MESSAGE.enableMarkdown(false);
        MESSAGE.setText("{MC} <" + player + "> " + message);
        MESSAGE.setChatId(CHAT_SEND);
        MESSAGE.setReplyMarkup(emptyMarkup());
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        MESSAGE.enableMarkdown(true);
    }

//    public static synchronized void toMinecraft(Message message) {
//        String player = Minecraft.getMinecraftName(message.getFrom().getId());
//        long id = message.getChatId();
//        String txt = message.getText();
//        long userId = message.getFrom().getId();
//        if(player == null || getData(userId)[3].equals("0")) { BOT.deleteMessage(id, message.getMessageId()); return; }
//        if (message.hasText()) { com.radik.logic.TransChatMessages.sendMinecraft(txt, player); }
//    }

    public static synchronized void toMinecraft(@NotNull Update update) {
        Message message = update.getMessage();
        String player = Minecraft.getMinecraftName(message.getFrom().getId());
        long id = message.getChatId();
        String txt = message.getText();
        String caption = message.getCaption();
        txt = txt == null ? "" : txt;
        caption = caption == null ? "" : caption;
        if (message.hasPhoto()) {
            txt = "§6[Photo]§r " + caption;
        }
        else if (message.hasAudio()) {
            txt = "§6[Audio]§r " + caption;
        }
        else if (message.hasVideo()) {
            txt = "§6[Video]§r " + caption;
        }
        else if (message.hasVoice()) {
            txt = "§6[Voice]§r " + caption;
        }
        else if (message.hasAnimation()) {
            txt = "§6[GIF]§r";
        }
        else if (message.hasDocument()) {
            txt = "§6[Document]§r " + caption;
        }
        else if (message.hasSticker()) {
            txt = "§6[Sticker]§r";
        }
        else if (message.hasPoll()) {
            txt = "§6[Poll]§r " + message.getPoll().getQuestion();
        }
        else if (message.hasViaBot()) {
            txt = "§6[Via @" + message.getViaBot().getUserName() + "]§r " + caption;
        }
        User forwarded = message.getForwardFrom();
        if (forwarded != null) {
            String forwardedFrom = Minecraft.getMinecraftName(forwarded.getId());
            if (forwardedFrom == null) forwardedFrom = "@" + forwarded.getUserName();
            txt = "§6[Forwarded From " + forwardedFrom + "]§r " + txt;
        }
        if (message.isReply()) {
            Message reply = message.getReplyToMessage();
            String replied = Minecraft.getMinecraftName(reply.getFrom().getId());
            if (replied == null) replied = reply.getText().split(">")[0].substring(6);
            txt = "§6[Reply to " + replied + "]§r " + txt;
        }
        long userId = message.getFrom().getId();
        if(player == null || getData(userId)[3].equals("0")) { BOT.deleteMessage(id, message.getMessageId()); return; }
        com.radik.logic.TransChatMessages.sendMinecraft(txt, player);
    }
}
