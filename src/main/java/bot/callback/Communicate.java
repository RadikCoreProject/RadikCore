package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class Communicate {

    protected static synchronized void reportToAdmin(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=", 4);

        if (data[2].equals("wrongip")) {
            MESSAGE.setChatId(String.valueOf(ADMIN));
            MESSAGE.setText(String.format("игрок с ip %s пытался зайти на чужой аккаунт!", data[3]));
            MESSAGE.setReplyMarkup(emptyMarkup());
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }

            MESSAGE.setChatId(callbackQuery.getMessage().getChatId());
            MESSAGE.setText("Жалоба успешно отправлена✅");
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
    }
}
