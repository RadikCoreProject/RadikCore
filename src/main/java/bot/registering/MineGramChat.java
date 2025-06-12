package bot.registering;

import bot.logic.TransChatMessages;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Deprecated
public class MineGramChat {
    public static synchronized void register(Update update) {
        TransChatMessages.toMinecraft(update);
    }
}
