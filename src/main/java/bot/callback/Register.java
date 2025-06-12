package bot.callback;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.callback.Callback.*;
import static bot.database.Registration.cancelRegister;
import static bot.database.Registration.endRegister;
import static bot.keyboard.KeyboardButtons.admin;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class Register {

    protected static synchronized void callbackRegister(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=", 4);
        Message message = callbackQuery.getMessage();
        BOT.deleteMessage(message.getChatId(), message.getMessageId());
        MESSAGE.setChatId(data[2]);

        if (data[1].equals("agree")) {
            MESSAGE.setText("Поздравляю! Администраторы одобрили вашу заявку! Чтобы обезопасить свой аккаунт, войдите на сервер как можно скорее!");
            MESSAGE.setReplyMarkup(emptyMarkup());
            endRegister(data[3], Long.parseLong(data[2]));
        }
        else if (data[1].equals("cancel")) {
            MESSAGE.setText("Регистрация отменена.");
            MESSAGE.setReplyMarkup(emptyMarkup());
            cancelRegister(data[3]);
        }
        else {
            MESSAGE.setText("Увы, администраторы отклонили вашу заявку. Попробуйте кинуть ее позже");
            MESSAGE.setReplyMarkup(admin());
            cancelRegister(data[3]);
        }
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
