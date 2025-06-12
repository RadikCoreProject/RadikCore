package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static bot.Bot.*;
import static bot.Text.LAW;
import static bot.Text.SERVER_INFO2;
import static bot.keyboard.KeyboardButtons.backMarkup;

public class Information {
    protected static synchronized void callbackInformation(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=", 4);
        switch (data[1]) {
            case "bot": bot(callbackQuery); break;
            case "law": law(callbackQuery); break;
        }
    }

    private static synchronized void bot(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setReplyMarkup(backMarkup("2"));
        EDITED_TEXT.setText(SERVER_INFO2);

        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    private static synchronized void law(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();

        EDITED_TEXT.setChatId(userId);
        EDITED_TEXT.setMessageId(messageId);
        EDITED_TEXT.setReplyMarkup(backMarkup("2"));
        EDITED_TEXT.setText(LAW);

        try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }

    }
}
