package bot.callback;

import bot.keyboard.OnStart;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

import static bot.Bot.*;
import static bot.Properties.SERVER_VERSION;
import static bot.Text.*;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.OnStart.Info.info;

public class Bot {
    protected static synchronized void callbackBot(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        switch (data[1]) {
            case "info", "2": information(callbackQuery); break;
            case "can", "21": can(callbackQuery); break;
            case "commands", "211": command(callbackQuery); break;
            case "server", "212": server(callbackQuery); break;
            case "player", "213": player(callbackQuery); break;
        }
    }

    protected static synchronized void information(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText("Я *RadikBOT*! Я создан для поддержания и разнообразия сервера под продуктом *RadikCore*.\n\nОсновная информация:\nВерсия сборки: *" + SERVER_VERSION + "*\nРазработчик: *Radik*\nТестировщики: *Radik*, *Abobusniev*\n\nТы можешь узнать обо мне больше, нажав на кнопки ниже.");
        EDITED_TEXT.setReplyMarkup(OnStart.Info.info("main"));

        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void can(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText("Это мой основной функционал. Если останутся вопросы, обратись к админу!");
        EDITED_TEXT.setReplyMarkup(info("can"));

        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void command(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText(COMMANDS);
        EDITED_TEXT.setReplyMarkup(backMarkup("21"));
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void player(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText(CLIENT_FUNCTIONAL);
        EDITED_TEXT.setReplyMarkup(backMarkup("21"));
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void server(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText(SERVER_FUNCTIONAL);
        EDITED_TEXT.setReplyMarkup(backMarkup("21"));
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
