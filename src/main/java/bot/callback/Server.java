package bot.callback;

import bot.keyboard.OnStart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bot.Bot.*;
import static bot.Properties.FABRIC_API_FILE;
import static bot.Properties.RADIK_CORE_FILE;
import static bot.Text.SERVER_INFO1;
import static bot.Text.SERVER_INFO2;
import static bot.database.Settings.GetUser.getData;
import static bot.database.Settings.getCount;
import static bot.database.User.*;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.logic.Information.sendUserProfile;

public class Server {
    protected static synchronized void callbackServer(CallbackQuery callbackQuery) {
        switch (callbackQuery.getData().split("=")[1]) {
            case "server", "3": server(callbackQuery); break;
            case "info": info(callbackQuery); break;
            case "players", "31": players(callbackQuery); break;
            case "like": like(callbackQuery); break;
            case "dislike": dislike(callbackQuery); break;
        }
    }

    private static synchronized void dislike(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        LocalDateTime time = LocalDateTime.now();
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        long userId = Long.parseLong(getData(data[2])[1]);
        int reputation = getIntegerUserInfo(userId, "reputation");
        int dislikes = getIntegerUserInfo(userId, "dislikes");

        setUserInfo("reacted", String.valueOf(time), chatId);
        setUserInfo("reputation", String.valueOf(reputation - 5), userId);
        setUserInfo("dislikes", String.valueOf(dislikes + 1), userId);
        sendUserProfile(userId, chatId, data[3] + "=" + data[2], messageId);
    }

    private static synchronized void like(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        LocalDateTime time = LocalDateTime.now();
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        long userId = Long.parseLong(getData(data[2])[1]);
        int reputation = getIntegerUserInfo(userId, "reputation");
        int likes = getIntegerUserInfo(userId, "likes");

        setUserInfo("reacted", String.valueOf(time), chatId);
        setUserInfo("reputation", String.valueOf(reputation + 4), userId);
        setUserInfo("likes", String.valueOf(likes + 1), userId);
        sendUserProfile(userId, chatId, data[3] + "=" + data[2], messageId);
    }

    private static synchronized void server(@NotNull CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText(SERVER_INFO1);
        EDITED_TEXT.setReplyMarkup(OnStart.Server.server());
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void info(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        SendMediaGroup sendMediaGroup = getSendMediaGroup(chatId);
        try { BOT.execute(sendMediaGroup); } catch (TelegramApiException e) { throw new RuntimeException(e); }

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText(SERVER_INFO2);
        EDITED_TEXT.setReplyMarkup(backMarkup("3"));
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized @NotNull SendMediaGroup getSendMediaGroup(long chatId) {
        List<InputMedia> mediaGroup = new ArrayList<>();
        InputMediaDocument media = new InputMediaDocument();
        InputMediaDocument media2 = new InputMediaDocument();
        media.setMedia(FABRIC_API_FILE);
        media.setMediaName("fabric-api-0.102.0-1.21");
        mediaGroup.add(media);
        media2.setMedia(RADIK_CORE_FILE);
        media2.setMediaName("RadikCore-1.3-1.21");
        mediaGroup.add(media2);

        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        sendMediaGroup.setMedias(mediaGroup);
        return sendMediaGroup;
    }

    private static synchronized void players(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        boolean info = message.hasPhoto() || message.hasAnimation();

        if(info) {
            BOT.deleteMessage(chatId, messageId);
            MESSAGE.setChatId(chatId);
            MESSAGE.setText("Выберите игрока, нажав по его его никнейму. Чтобы просмотреть дальше, необходимо нажать на стрелочки.");
            MESSAGE.setReplyMarkup(OnStart.Server.players(Integer.parseInt(data[3])));
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
        else {
            EDITED_TEXT.setChatId(chatId);
            EDITED_TEXT.setMessageId(messageId);
            EDITED_TEXT.setText("Выберите игрока, нажав по его никнейму. Чтобы просмотреть дальше, необходимо нажать на стрелочки.");


            if(data.length == 2) {
                EDITED_TEXT.setReplyMarkup(OnStart.Server.players(0));
                try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            } else if(data[2].equals("row")) {
                EDITED_TEXT.setReplyMarkup(OnStart.Server.players(Integer.parseInt(data[3])));
                try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException ignored) {}
            } else {
                long userId = Long.parseLong(getData(data[2])[1]);
                sendUserProfile(userId, chatId, data[3] + "=" + data[2], messageId);
            }
        }
    }
}
