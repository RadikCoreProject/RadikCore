package bot;

import bot.registering.MineGramChat;
import bot.registering.UserChatRegister;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

import static bot.callback.Callback.callbackHandler;
import static bot.database.Main.connect;
import static bot.database.Settings.getCount;
import static com.radik.Radik.SERVER;

public class Bot extends TelegramLongPollingBot {
    // for prod
    // 6795789190:AAGHMKkTwwsBd5XlmHxPrDZBQbjjCvZJTnU
    // testing_abc746_bot

    // for test
    // 6843337254:AAGubZFDTe-3aNERsTf6utF99-FUy6kDp0g
    // Test716273bot
    public static final String BOT_TOKEN = "6795789190:AAGHMKkTwwsBd5XlmHxPrDZBQbjjCvZJTnU";
    public static final String BOT_USERNAME = "testing_abc746_bot";
    public final String BOTTOKEN;
    public final String BOTUSERNAME;

    public static final long ADMIN = 2102888844;
    public static long ADMIN_ANSWERS = 0;
    public static String ADMIN_TEXT = "";

    public static HashMap<Long, String> QUESTIONS = new HashMap<>();
    public static HashMap<Long, String> LOGIN = new HashMap<>();

    public static final Bot BOT = new Bot(BOT_TOKEN, BOT_USERNAME);
    public static final long CHAT_SEND = -1002346533509L;
    public Bot(String botToken, String botUsername) {
        this.BOTTOKEN = botToken;
        this.BOTUSERNAME = botUsername;
    }

    public static EditMessageText EDITED_TEXT = new EditMessageText();
    public static EditMessageMedia EDITED_MEDIA = new EditMessageMedia();
    public static EditMessageCaption EDITED_CAPTION = new EditMessageCaption();
    public static SendMessage MESSAGE = new SendMessage();

    static {
        connect();
        MESSAGE.enableMarkdown(true);
        EDITED_TEXT.enableMarkdown(true);
        EDITED_CAPTION.setParseMode("Markdown");
    }


    public synchronized void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try { execute(deleteMessage); } catch (TelegramApiException ignored) {  }
    }


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public synchronized void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.getChat().getId().equals(CHAT_SEND)) { MineGramChat.register(update); return; }
            if (message.getChat().isUserChat()) {
                if (message.hasText()) { UserChatRegister.registerText(update); return; }
                if (message.hasPhoto() || message.hasAnimation()) { UserChatRegister.registerPhoto(update); return; }
                if (message.hasDocument() && message.getFrom().getId() == ADMIN) { UserChatRegister.registerDocument(update); return; }
            }
        } else if (update.hasCallbackQuery()) { callbackHandler(update.getCallbackQuery()); }

    }
}
