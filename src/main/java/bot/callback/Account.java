package bot.callback;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.database.User.*;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.keyboard.OnStart.Me.me;
import static bot.logic.Information.sendUserProfile;

public class Account {
    protected static synchronized void callbackAccount(@NotNull CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        switch (data[1]) {
            case "me", "1": account(callbackQuery); break;

            case "profile": profile(callbackQuery, 0); break;
            case "11": profile(callbackQuery, 1); break;

            case "setavatar", "111": setAvatar(callbackQuery); break;
            case "setinfo", "112": setProfile(callbackQuery); break;
        }
    }

    private static synchronized void account(@NotNull CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        sendUserProfile(chatId, chatId, "main", message.getMessageId());
    }

    private static synchronized void profile(@NotNull CallbackQuery callbackQuery, int type) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();
        int[] info = getIntegerUserInfo(userId);
        String[] info2 = getStringUserInfo(userId);
        int t = 0;

        if(info[3] == 0 && !info2[2].isEmpty()) {
            t = 1;
        } else if(info[3] != 0 && info2[2].isEmpty()) {
            t = 2;
        } else if(info[3] != 0) {
            t = 3;
        }

        if(type == 0) {
            BOT.deleteMessage(userId, messageId);
            MESSAGE.setChatId(userId);
            MESSAGE.setText("Изменить описание");
            MESSAGE.setReplyMarkup(me(String.valueOf(t)));
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        } else {
            setUserInfo("setsAvatar", "0", userId);
            setUserInfo("setsInfo", "0", userId);
            EDITED_TEXT.setChatId(userId);
            EDITED_TEXT.setMessageId(messageId);
            EDITED_TEXT.setText("Изменить описание");
            EDITED_TEXT.setReplyMarkup(me(String.valueOf(t)));
            try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
    }

    private static synchronized void setAvatar(@NotNull CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] data = callbackQuery.getData().split("=");
        long userId = message.getChatId();
        int messageId = message.getMessageId();

        if(data.length == 3) {
            setUserInfo("hasAvatar", "0", userId);
            setUserInfo("avatarId", "", userId);
            MESSAGE.setChatId(userId);
            MESSAGE.setReplyMarkup(emptyMarkup());
            MESSAGE.setText("Аватар успешно удален✅");
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            return;
        }

        setUserInfo("setsAvatar", "1", userId);
        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText("Отправь фотографию поддерживаемого формата, чтобы я установил ее на аватарку!");
        EDITED_TEXT.setReplyMarkup(backMarkup("11"));
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void setProfile(@NotNull CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] data = callbackQuery.getData().split("=");
        long userId = message.getChatId();
        int messageId = message.getMessageId();

        if(data.length == 3) {
            setUserInfo("info", "", userId);
            MESSAGE.setChatId(userId);
            MESSAGE.setReplyMarkup(emptyMarkup());
            MESSAGE.setText("Описание успешно удалено✅");
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            return;
        }

        setUserInfo("setsInfo", "1", userId);
        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText("Отправь сообщение, чтобы я установил его тебе в профиль! (не больше 120 символов)");
        EDITED_TEXT.setReplyMarkup(backMarkup("11"));
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
