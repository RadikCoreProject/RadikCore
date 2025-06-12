package bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.*;

import java.util.ArrayList;
import java.util.List;

import static bot.keyboard.KeyboardButtons.*;

public class Register {
    // для регистрации админу
    public static synchronized InlineKeyboardMarkup registerMarkup(long userId, String username) {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        rowButtons.add(USER_INFO.url(String.format("tg://openmessage?user_id=%d", userId)).build());
        rowButtons.add(DISAGREE.callbackData(String.format("register=disagree=%s=%s", userId, username)).build());
        rowButtons.add(AGREE.callbackData(String.format("register=agree=%s=%s", userId, username)).build());
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }

    public static synchronized InlineKeyboardMarkup unRegister() {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
        rowButtons.add(ADMIN.build());
        rowButtons.add(CANCEL.callbackData("register=cancel").build());
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        keyboard.add(rowButtons1);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }
}
