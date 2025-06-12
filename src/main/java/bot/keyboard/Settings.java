package bot.keyboard;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static bot.database.Settings.GetUser.getData;
import static bot.keyboard.KeyboardButtons.*;

public class Settings {

    public static synchronized InlineKeyboardMarkup playerWarn(String ip) {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
        rowButtons.add(ADMIN.build());
        rowButtons.add(DISABLE_NOTIFY.callbackData("settings=notify=wrongIpNotify=1").build());
        rowButtons.add(REPORT_TO_ADMIN.callbackData(String.format("settings=report=wrongip=%s", ip)).build());
        rowButtons2.add(ITS_ME.callbackData(String.format("settings=account=addip=%s", ip)).build());
        rowButtons2.add(ITS_ME2.callbackData("settings=account=deleteip").build());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        keyboard.add(rowButtons2);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }

    public static synchronized InlineKeyboardMarkup settingsType(String type, long id) {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons4 = new ArrayList<>();
        String back = "";
        String getPassword = getData(id)[4];

        switch (type) {
            case "leave":
                rowButtons.add(I_KNOW_WHAT_I_DO.callbackData("settings=user=leavey").build());
                back = "121";
                break;
            case "delete":
                rowButtons.add(I_KNOW_WHAT_I_DO.callbackData("settings=user=deletey").build());
                back = "121";
                break;
            case "main":
                rowButtons.add(USER_SETTINGS.callbackData("settings=user").build());
                rowButtons1.add(SERVER_SETTINGS.callbackData("settings=server").build());
                back = "1";
                break;
            case "user":
                if(getPassword.isEmpty()) {
                    rowButtons.add(SET_PASSWORD.callbackData("settings=user=passwords").build());
                }
                else {
                    rowButtons.add(CHANGE_PASSWORD.callbackData("settings=user=passwordc").build());
                    rowButtons1.add(REMOVE_PASSWORD.callbackData("settings=user=passwordr").build());
                    rowButtons2.add(LEAVE_ACCOUNT.callbackData("settings=user=leave").build());
                }
                rowButtons3.add(DELETE_ACCOUNT.callbackData("settings=user=delete").build());
                back = "12";
                break;
            case "server":
                rowButtons.add(ALLOW_WARNINGS.callbackData("settings=server2=wrongIpNotify").build());
                rowButtons1.add(ALLOW_MESSAGES_SEND.callbackData("settings=server2=allowTg").build());
                rowButtons2.add(ALLOW_PRIVATE_MESSAGES_SEND.callbackData("settings=server2=allowPrivateTg").build());
                back = "12";
                break;
        }
        rowButtons4.add(BACK.callbackData("back=" + back).build());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        keyboard.add(rowButtons1);
        keyboard.add(rowButtons2);
        keyboard.add(rowButtons3);
        keyboard.add(rowButtons4);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }
}
