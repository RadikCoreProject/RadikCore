package bot.commands;

import bot.database.Registration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.database.Settings.GetUser.getData;
import static bot.keyboard.Admin.law;
import static bot.keyboard.KeyboardButtons.admin;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.keyboard.OnStart.start;

public class OnStart {
    public static synchronized void starting(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();

        switch (callbackQuery.getData().split("_")[1]) {
            case "0": onStart(userId, messageId, 0);
            case "01": onStart(userId, messageId, 1);
        }
    }

    public static synchronized void onStart(long id, @Nullable Integer messageId, @NotNull Integer type) {
        if(Registration.isUserRegistered(id) == 2 && getData(id)[3].equals("0")) {
            if(type == 0) {
                MESSAGE.setChatId(id);
                MESSAGE.setReplyMarkup(law());
                MESSAGE.setText("Чтобы использовать все ресурсы бота вам необходимо принять *Правовую информацию* и *Условия об использование конфиденциальных данных*.");
                try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            }
            else {
                EDITED_TEXT.setChatId(id);
                EDITED_TEXT.setMessageId(messageId);
                EDITED_TEXT.setReplyMarkup(law());
                EDITED_TEXT.setText("Чтобы использовать все ресурсы бота вам необходимо принять *Правовую информацию* и *Условия об использование конфиденциальных данных*.");
                try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            }
            return;
        }

        if(messageId == null) {
            MESSAGE.setChatId(id);

            switch (Registration.isUserRegistered(id)) {
                case 0: MESSAGE.setText("Привет, извини, но ты еще не можешь получить функционал бота, ведь тебя не зарегистрировали администраторы. Обычно это делается в течении *30 минут*. Если что-то пошло не так, ты можешь написать админу в лс или отменить регистрацию"); MESSAGE.setReplyMarkup(admin()); break;
                case 1: MESSAGE.setText("Привет, я *RadikBot*! Я отвечаю за *Minecraft* сервера и *чаты*. Вижу, что тебе интересен наш проект.\n\nЧтобы получить доступ к большинству функций бота зарегистрируйся, написав\n*/register {свой никнейм в майне}*\n\nЕсли ты вышел из аккаунта, пропиши */login {свой ник}*"); MESSAGE.setReplyMarkup(emptyMarkup()); break;
                case 2: MESSAGE.setText(String.format("Привет, *%s*! Что бы ты хотел узнать?", getData(id)[0])); MESSAGE.setReplyMarkup(start()); break;
            }
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        } else if(type == 0) {
            BOT.deleteMessage(id, messageId);

            MESSAGE.setChatId(id);
            MESSAGE.setText(String.format("*%s*, что бы ты хотел узнать?", getData(id)[0]));
            MESSAGE.setReplyMarkup(start());

            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        } else {
            EDITED_TEXT.setMessageId(messageId);
            EDITED_TEXT.setChatId(id);
            EDITED_TEXT.setText(String.format("*%s*, что бы ты хотел узнать?", getData(id)[0]));
            EDITED_TEXT.setReplyMarkup(start());

            try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
    }
}
