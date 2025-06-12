package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.keyboard.KeyboardButtons.admin;

public class Admin {
    protected static synchronized void callbackAdmin(CallbackQuery callbackQuery) {
        switch (callbackQuery.getData().split("=")[1]) {
            case "question": question(callbackQuery);
        }
    }

    private static synchronized void question(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        long userId = Long.parseLong(data[3]);

        if(data[2].equals("enable")) {
            ADMIN_ANSWERS = userId;
            ADMIN_TEXT = QUESTIONS.get(userId);
        } else {
            MESSAGE.setChatId(userId);
            MESSAGE.setText("Администрация отреагировала на ваш вопрос. Текст вопроса:\n\n*" + QUESTIONS.get(userId) + "*\n\nОтвет администрации:\n\n*Ответа нет.*");
            MESSAGE.setReplyMarkup(admin());
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
        QUESTIONS.remove(userId);
    }
}
