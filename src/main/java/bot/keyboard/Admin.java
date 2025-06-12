package bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static bot.keyboard.KeyboardButtons.*;
import static bot.keyboard.KeyboardButtons.KEYBOARD_MARKUP;

public class Admin {
    public static synchronized InlineKeyboardMarkup question(long userId) {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
        rowButtons.add(ANSWER_DISABLE.callbackData("admin=question=disable=" + userId).build());
        rowButtons.add(ANSWER_ENABLE.callbackData("admin=question=enable=" + userId).build());
        rowButtons1.add(USER_INFO.url(String.format("tg://openmessage?user_id=%d", userId)).build());
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        keyboard.add(rowButtons1);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }

    public static synchronized InlineKeyboardMarkup law() {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();

        rowButtons.add(AGREE.callbackData("law=agree").build());
        rowButtons.add(DISAGREE.callbackData("law=disagree").build());
        rowButtons1.add(LAW_INFO.callbackData("law=info").build());
        rowButtons2.add(ADMIN.build());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        keyboard.add(rowButtons1);
        keyboard.add(rowButtons2);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }
}
