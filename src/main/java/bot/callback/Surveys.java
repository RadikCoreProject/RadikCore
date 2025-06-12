package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static bot.Bot.ADMIN;
import static bot.Bot.EDITED_TEXT;

public class Surveys {
    protected static synchronized void callbackSurveys(CallbackQuery callbackQuery) {
        switch (callbackQuery.getData().split("=")[1]) {
            case "info", "32": info(callbackQuery);
        }
    }

    private static synchronized void info(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(chatId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText("Эта вкладка для опросов. Если ты обычный игрок, то у тебя только одна кнопка - *Посмотреть опросы*\n\nОднако, возможно, что позже будет добавлена роль опросника, который сможет создавать и останавливать опросы");

        if(chatId == ADMIN) {

        }
        else {

        }
    }
}
