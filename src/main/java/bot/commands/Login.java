package bot.commands;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

import static bot.Bot.*;
import static bot.database.Settings.getIds;
import static bot.database.Settings.getNames;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class Login {
    public static synchronized void onLogin(Update update) {
        Message message = update.getMessage();
        long chatId = message.getChatId();
        // ["/login", {name}]
        String[] data = message.getText().split(" ");

        MESSAGE.setChatId(chatId);
        MESSAGE.setReplyMarkup(emptyMarkup());

        if(getIds().contains(chatId)) {
            MESSAGE.setText("Вы уже находитесь в аккаунте! Для смены аккаунта нужно выйти из него.");
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            return;
        }

        if(data.length != 2) {
            MESSAGE.setText("Неправильный синтаксис команды!\n\n*/login {name}*");
        } else {
            ArrayList<String> names = getNames();
            if(names.contains(data[1])) {
                MESSAGE.setText("Игрок " + data[1] + " найден! Введи пароль от этого аккаунта для входа в него.");
                LOGIN.put(chatId, data[1]);
            }
            else {
                MESSAGE.setText("ОТКАЗАНО В ДОСТУПЕ.\nError: unknown nickname");
            }
        }
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
