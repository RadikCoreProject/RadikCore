package bot.registering;

import bot.commands.Registration;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.Properties.setProperty;
import static bot.callback.Question.catchQuestion;
import static bot.commands.Login.onLogin;
import static bot.commands.OnStart.onStart;
import static bot.database.Registration.isUserRegistered;
import static bot.database.Settings.GetUser.getData;
import static bot.database.User.getIntegerUserInfo;
import static bot.database.User.setUserInfo;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.registering.Callback.*;

public class UserChatRegister {
    public static synchronized void registerText(@NotNull Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        long id = message.getChatId();

        BOT.deleteMessage(id, message.getMessageId());
        if(text.charAt(0) == '/') {
            switch (text.split(" ")[0]) {
                case "/register":  Registration.registerUser(update); break;
                case "/start":  onStart(id, null, 0); break;
                case "/login": onLogin(update);
            }
        } else if(isUserRegistered(id) != 0) {
            if (LOGIN.containsKey(id)) { login(id, text); }
            else if (getIntegerUserInfo(id, "setsInfo") == 1) { setInfo(id, text); }
            else if (getData(id)[5].equals("1")) { setPassword(id, text); }
            else if (!getData(id)[6].isEmpty()) { catchQuestion(id, text); }
            else if (ADMIN_ANSWERS != 0 && id == ADMIN) { adminAnswers(id, text); }
        }
    }

    public static synchronized void registerPhoto(@NotNull Update update) {
        Message message = update.getMessage();
        long id = message.getChatId();
        int messageId = message.getMessageId();


        if(getIntegerUserInfo(id, "setsAvatar") == 1) {
            String fileId;
            if(message.hasAnimation()) {
                fileId = message.getAnimation().getFileId();
                setUserInfo("avatarId", fileId + "~animation", id);
            }
            else {
                fileId = message.getPhoto().getFirst().getFileId();
                setUserInfo("avatarId", fileId + "~photo", id);
            }
            MESSAGE.setChatId(id);
            MESSAGE.setReplyMarkup(emptyMarkup());
            MESSAGE.setText("Аватар изменен успешно✅\n\nНЕ удаляйте, пожалуйста, сообщение с аватаром, иначе она может плохо загружаться");
            setUserInfo("setsAvatar", "0", id);
            setUserInfo("hasAvatar", "1", id);
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        } else {
            BOT.deleteMessage(id, messageId);
        }
    }

    public static synchronized void registerDocument(@NotNull Update update) {
        setProperty("radik_core", update.getMessage().getDocument().getFileId());
    }
}
