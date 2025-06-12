package bot.registering;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.database.Settings.GetUser.getData;
import static bot.database.Settings.GetUser.setData;
import static bot.database.User.setUserInfo;
import static bot.keyboard.KeyboardButtons.admin;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class Callback {
    protected static synchronized void setInfo(long id, String text) {
        MESSAGE.setChatId(id);
        MESSAGE.setReplyMarkup(emptyMarkup());
        if (text.length() <= 120) {
            MESSAGE.setText("Профиль изменен на \"" + text + "\" успешно✅");
            setUserInfo("info", text, id);
            setUserInfo("setsInfo", "0", id);
        } else {
            MESSAGE.setText("Слишком длинный профиль❌");
        }
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    protected static synchronized void setPassword(long id, String text) {
        MESSAGE.setChatId(id);
        MESSAGE.setReplyMarkup(emptyMarkup());
        if (text.length() <= 20) {
            MESSAGE.setText("Пароль изменен на \"" + text + "\" успешно✅");
            setData("password", text, id);
            setData("setsPassword", "0", id);
        }
        else {
            MESSAGE.setText("Слишком длинный пароль❌");
        }
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    protected static synchronized void adminAnswers(long id, String text) {
        MESSAGE.setChatId(id);
        MESSAGE.setText("✅");
        MESSAGE.setReplyMarkup(emptyMarkup());
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        MESSAGE.setChatId(ADMIN_ANSWERS);
        MESSAGE.setText("Администрация отреагировала на ваш вопрос. Текст вопроса:\n\n*" + ADMIN_TEXT + "*\n\nОтвет администрации:\n\n*" + text + "*");
        MESSAGE.setReplyMarkup(admin());
        setData("question", "", ADMIN_ANSWERS);
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        ADMIN_ANSWERS = 0;
        ADMIN_TEXT = "";
    }

    protected static synchronized void login(long id, String text) {
        MESSAGE.setChatId(id);
        MESSAGE.setReplyMarkup(emptyMarkup());
        if(text.equals(getData(LOGIN.get(id))[4])) {
            setData("id", String.valueOf(id), LOGIN.get(id));
            MESSAGE.setText("Вы успешно вошли в аккаунт!");
        }
        else {
            MESSAGE.setText("ОТКАЗАНО В ДОСТУПЕ.\nError: wrong password");
        }
        LOGIN.remove(id);
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
