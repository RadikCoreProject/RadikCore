package bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static bot.callback.Account.callbackAccount;
import static bot.callback.Admin.callbackAdmin;
import static bot.callback.Back.callbackBack;
import static bot.callback.Bot.callbackBot;
import static bot.callback.Information.callbackInformation;
import static bot.callback.Law.callbackLaw;
import static bot.callback.Question.callbackQuestion;
import static bot.callback.Register.callbackRegister;
import static bot.callback.Server.callbackServer;
import static bot.callback.Settings.callbackSettings;
import static bot.callback.Surveys.callbackSurveys;

public abstract class Callback {

    public static synchronized void callbackHandler(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");

        switch(data[0]) {
            case "register": callbackRegister(callbackQuery); break;
            case "settings": callbackSettings(callbackQuery); break;
            case "information": callbackInformation(callbackQuery); break;
            case "back": callbackBack(callbackQuery); break;
            case "account": callbackAccount(callbackQuery);
            case "bot": callbackBot(callbackQuery); break;
            case "server": callbackServer(callbackQuery); break;
            case "question": callbackQuestion(callbackQuery); break;
            case "admin": callbackAdmin(callbackQuery); break;
            case "law": callbackLaw(callbackQuery); return;
//            case "surveys": callbackSurveys(callbackQuery); break;
        }
    }
}