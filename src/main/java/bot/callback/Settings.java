package bot.callback;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

import static bot.Bot.*;
import static bot.callback.Communicate.reportToAdmin;
import static bot.database.Settings.*;
import static bot.database.Settings.GetUser.getData;
import static bot.database.Settings.GetUser.setData;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.keyboard.Settings.settingsType;

public class Settings {


    protected static synchronized void callbackSettings(@NotNull CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=", 4);
        switch (data[1]) {
            case "list", "12": settingsList(callbackQuery); break;
            case "notify": notify(callbackQuery); break;
            case "report": reportToAdmin(callbackQuery); break;
            case "account": setAccountData(callbackQuery); break;
            case "server": serverSettings(callbackQuery, ""); break;
            case "server2": serverSettings(callbackQuery, data[2]); break;
            case "user": userSettings(callbackQuery, "main"); break;
            case "121": userSettings(callbackQuery, "back"); break;
        }
    }

    private static synchronized void settingsList(@NotNull CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();

        if(callbackQuery.getData().split("=")[1].equals("12")) {
            EDITED_TEXT.setChatId(chatId);
            EDITED_TEXT.setMessageId(messageId);
            EDITED_TEXT.setText("Выбери тип настроек");
            EDITED_TEXT.setReplyMarkup(settingsType("main", chatId));
            try {BOT.execute(EDITED_TEXT);} catch (TelegramApiException e) {throw new RuntimeException(e);}
        }
        else {
            BOT.deleteMessage(chatId, messageId);
            MESSAGE.setChatId(chatId);
            MESSAGE.setText("Выбери тип настроек");
            MESSAGE.setReplyMarkup(settingsType("main", chatId));
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
    }

    private static synchronized void notify(@NotNull CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        long userId = callbackQuery.getFrom().getId();
        if (data[2].equals("wrongIpNotify")) {
            setSetting(data[2], data[3], userId);
        }
    }

    private static synchronized void setAccountData(@NotNull CallbackQuery callbackQuery) {
        String id = String.valueOf(callbackQuery.getMessage().getChatId());
        String[] data = callbackQuery.getData().split("=");
        MESSAGE.setChatId(id);
        switch (data[2]) {
            case "addip": setData("ip", data[3], Long.parseLong(id)); MESSAGE.setText("IP адрес успешно обновлен✅"); break;
            case "deleteip": setData("ip", "0", Long.parseLong(id)); MESSAGE.setText("IP адрес успешно сброшен✅"); break;
        }
        try {BOT.execute(MESSAGE);} catch (TelegramApiException e) {throw new RuntimeException(e);}
    }

    private static synchronized void userSettings(@NotNull CallbackQuery callbackQuery, @NotNull String type) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();
        String[] data = callbackQuery.getData().split("=");
        String[] data2 = getData(userId);
        String pwd = data2[4];
        String password;

        if(type.equals("back")) { setData("setsPassword", "0", userId); }

        if(pwd.isEmpty()) { password = "Не установлен"; }
        else { password = pwd; }

        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);

        if(data.length == 2) {
            EDITED_TEXT.setText(String.format("Это настройки твоего *аккаунта*. Здесь ты можешь изменить локальные настройки. *Осторожно, ты можешь удалить аккаунт!*\n\nТвой пароль: *%s*", password));
            EDITED_TEXT.setReplyMarkup(settingsType("user", userId));
        }
        else {
            switch(data[2]) {
                case "passwords":
                    EDITED_TEXT.setText("Поставь пароль для возможности *выйти* из аккаунта! Ты всегда сможешь удалить или изменить его. Пароль не должен быть длиннее 20 символов.");
                    EDITED_TEXT.setReplyMarkup(backMarkup("121"));
                    setData("setsPassword", "1", userId);
                    break;
                case "passwordc":
                    EDITED_TEXT.setText("Напиши новый пароль ниже. Пароль не должен быть длиннее 20 символов.");
                    EDITED_TEXT.setReplyMarkup(backMarkup("121"));
                    setData("setsPassword", "1", userId);
                    break;
                case "passwordr":
                    EDITED_TEXT.setText("Пароль успешно удален!");
                    EDITED_TEXT.setReplyMarkup(backMarkup("121"));
                    setData("password", "", userId);
                    break;
                case "leave":
                    EDITED_TEXT.setText("Вы уверены? вы не сможете посмотреть свой пароль после подтверждения");
                    EDITED_TEXT.setReplyMarkup(settingsType("leave", userId));
                    break;
                case "delete":
                    EDITED_TEXT.setText("Вы уверены? *ЭТО ДЕЙСТВИЕ НЕЛЬЗЯ ОТМЕНИТЬ*. Все данные аккаунта будут *УДАЛЕНЫ* без возможности восстановления. Любой игрок позже сможет зарегистрироваться под *Вашим* никнеймом.");
                    EDITED_TEXT.setReplyMarkup(settingsType("delete", userId));
                    break;
                case "leavey":
                    leaveAccount("leave", userId);
                    MESSAGE.setChatId(userId);
                    MESSAGE.setText("Вы вышли из аккаунта✅");
                    MESSAGE.setReplyMarkup(emptyMarkup());
                    try {BOT.execute(MESSAGE);} catch (TelegramApiException e) {throw new RuntimeException(e);}
                    return;
                case "deletey":
                    leaveAccount("delete", userId);
                    MESSAGE.setChatId(userId);
                    MESSAGE.setText("Аккаунт был удален.");
                    MESSAGE.setReplyMarkup(emptyMarkup());
                    try {BOT.execute(MESSAGE);} catch (TelegramApiException e) {throw new RuntimeException(e);}
                    return;
            }
        }
        try {BOT.execute(EDITED_TEXT);} catch (TelegramApiException e) {throw new RuntimeException(e);}
    }

    private static synchronized void serverSettings(CallbackQuery callbackQuery, String type) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();
        if(!type.isEmpty()) { setSetting(type, String.valueOf(1 - getSetting(type, userId)), userId); }
        int[] settings = getBooleanSettings(userId);

        String _1 = "❌";
        String _2 = "❌";
        String _3 = "❌";
        if(settings[0] == 1) { _1 = "✅"; }
        if(settings[1] == 1) { _2 = "✅"; }
        if(settings[2] == 1) { _3 = "✅"; }

        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setText(String.format("""
                Это серверные настройки. Они помогут сделать тебе игру комфортной! Их можно менять *динамически* прямо здесь.
                
                1) Уведомления о заходе - %s
                2) Получать сообщения из тг - %s
                3) Получать личные сообщения - %s
                """, _1, _2, _3));
        EDITED_TEXT.setReplyMarkup(settingsType("server", userId));
        try {BOT.execute(EDITED_TEXT);} catch (TelegramApiException e) {throw new RuntimeException(e);}
    }
}
