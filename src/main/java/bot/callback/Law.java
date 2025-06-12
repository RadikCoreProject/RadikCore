package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.BOT;
import static bot.Bot.EDITED_TEXT;
import static bot.Text.LAW;
import static bot.database.Settings.GetUser.setData;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class Law {
    protected static synchronized void callbackLaw(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();
        String[] data = callbackQuery.getData().split("=");
        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setReplyMarkup(emptyMarkup());

        if(data[1].equals("agree")) {
            EDITED_TEXT.setText("✅");
            setData("law", "1", userId);
        }
        else if (data[1].equals("disagree")) {
            EDITED_TEXT.setText("❌");
        }
        else {
            EDITED_TEXT.setText(LAW);
            EDITED_TEXT.setReplyMarkup(backMarkup("01"));
        }
        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
