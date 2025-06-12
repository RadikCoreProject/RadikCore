package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

import static bot.Bot.*;
import static bot.database.Settings.GetUser.getData;
import static bot.database.Settings.GetUser.setData;
import static bot.keyboard.Admin.question;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.KeyboardButtons.emptyMarkup;
import static bot.keyboard.OnStart.Me.me;

public class Question {
    protected static synchronized void callbackQuestion(CallbackQuery callbackQuery) {
        switch (callbackQuery.getData().split("=")[1]) {
            case "me", "13": meQuestion(callbackQuery); break;
        }
    }

    private static synchronized void meQuestion(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] data = callbackQuery.getData().split("=");
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        String[] data2 = getData(chatId);

        System.out.println(Arrays.toString(data2));
        if(!data2[6].isEmpty()) {
            BOT.deleteMessage(chatId, messageId);
            MESSAGE.setChatId(chatId);
            MESSAGE.setText("Ты не можешь отправлять более 1 вопроса. Дождись, пока администрация ответит на твой прошлый вопрос");
            MESSAGE.setReplyMarkup(backMarkup("1"));
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            return;
        }

        if(data.length == 2 && data[1].equals("me")) {
            BOT.deleteMessage(chatId, messageId);
            MESSAGE.setChatId(chatId);
            MESSAGE.setText("Что ты хочешь спросить или получить? Ты можешь обратиться к админу лично, если ситуация того требует.");
            MESSAGE.setReplyMarkup(me("question"));
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
        else if (data.length == 2) {
            EDITED_TEXT.setChatId(chatId);
            EDITED_TEXT.setMessageId(messageId);
            EDITED_TEXT.setText("Что ты хочешь спросить или получить? Ты можешь обратиться к админу лично, если ситуация того требует.");
            EDITED_TEXT.setReplyMarkup(me("question"));
            setData("question", "", chatId);
            try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        } else {
            EDITED_TEXT.setChatId(chatId);
            EDITED_TEXT.setMessageId(messageId);
            EDITED_TEXT.setReplyMarkup(backMarkup("13"));
            setData("question", "me_" + data[2], chatId);
            switch (data[2]) {
                case "1": EDITED_TEXT.setText("Опиши, пожалуйста, подробно, что ты не понял или какая у тебя проблема. Здесь мы не решаем вопросы кому что выдавать или отбирать. Ваш вопрос будет отклонен. Длина сообщения до 1024 символов."); break;
                case "2": EDITED_TEXT.setText("Опиши, пожалуйста, подробно, что бы ты хотел получить. Также ты можешь пожаловаться на злонамеренное использование привилегий игроком."); break;
                case "3": EDITED_TEXT.setText("Опиши свою проблему / идею. Мы постараемся ответить в ближайшее время!"); break;
            }
            try { BOT.execute(EDITED_TEXT); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        }
    }

    public static synchronized void catchQuestion(long chatId, String text) {
        String[] data = getData(chatId)[6].split("_");

        MESSAGE.setChatId(chatId);
        MESSAGE.setText("Вопрос успешно отправлен! Ответ на него вы должны получить в течении 24 часов.");
        MESSAGE.setReplyMarkup(emptyMarkup());
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }

        QUESTIONS.put(chatId, text);
        setData("question", "", chatId);

        MESSAGE.setChatId(ADMIN);
        MESSAGE.setReplyMarkup(question(chatId));
        switch (data[0]) {
            case "me":
                switch (data[1]) {
                    case "1": MESSAGE.setText(String.format("Игрок отправил вопрос о помощи / подсказке. Содержимое:\n\n*%s*\n\nЧто будешь делать?", text)); break;
                    case "2": MESSAGE.setText(String.format("Игрок отправил заявку на повышение / понижение. Содержимое:\n\n*%s*\n\nЧто будешь делать?", text)); break;
                    case "3": MESSAGE.setText(String.format("Игрок отправил идею / другой вопрос. Содержимое:\n\n*%s*\n\nЧто будешь делать?", text)); break;
                }
                break;
        }
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
