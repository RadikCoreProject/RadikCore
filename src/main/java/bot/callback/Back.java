package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static bot.callback.Account.callbackAccount;
import static bot.callback.Bot.callbackBot;
import static bot.callback.Question.callbackQuestion;
import static bot.callback.Server.callbackServer;
import static bot.callback.Settings.callbackSettings;
import static bot.callback.Surveys.callbackSurveys;
import static bot.commands.OnStart.onStart;

public class Back {
    protected static synchronized void callbackBack(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        Message message = callbackQuery.getMessage();
        long userId = message.getChatId();
        int messageId = message.getMessageId();

        switch (data[1]) {
            case "0": onStart(userId, messageId, 0); break;
            case "01": onStart(userId, messageId, 1); break;

            case "1", "11", "111", "112": callbackAccount(callbackQuery); break;
            case "12", "121": callbackSettings(callbackQuery); break;
            case "13": callbackQuestion(callbackQuery); break;

            case "2", "21", "211", "212", "213": callbackBot(callbackQuery); break;

            case "3", "31": callbackServer(callbackQuery); break;
            case "32": callbackSurveys(callbackQuery); break;
        }
    }
}
