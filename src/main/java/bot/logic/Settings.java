package bot.logic;

import com.radik.Radik;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.BOT;
import static bot.Bot.MESSAGE;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.keyboard.Settings.playerWarn;

public class Settings {
    public static synchronized void wrongIp(String ip, long userId) {
        Radik.LOGGER.info("lll");
        MESSAGE.setChatId(String.valueOf(userId));
        MESSAGE.setReplyMarkup(playerWarn(ip));
        MESSAGE.setText(String.format("Осторожно! игрок с ip %s пытается зайти на твой аккаунт! что ты будешь делать?", ip));
        try {BOT.execute(MESSAGE);} catch (TelegramApiException e) {throw new RuntimeException(e);}
        MESSAGE.setReplyMarkup(emptyMarkup());
    }
}
