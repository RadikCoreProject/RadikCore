package bot.keyboard;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static bot.database.Settings.getNames;
import static bot.keyboard.KeyboardButtons.*;

public class OnStart {
    public static synchronized InlineKeyboardMarkup start() {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
        rowButtons.add(ME.callbackData("account=me").build());
        rowButtons1.add(INFO.callbackData("bot=info").build());
        rowButtons2.add(SERVER.callbackData("server=server").build());
        rowButtons3.add(ADMIN.build());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        keyboard.add(rowButtons1);
        keyboard.add(rowButtons2);
        keyboard.add(rowButtons3);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }

    public static class Info {
        public static synchronized InlineKeyboardMarkup info(String type) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons4 = new ArrayList<>();

            String back = "0";
            switch (type) {
                case "main":
                    rowButtons.add(LAW.callbackData("information=law").build());
                    rowButtons1.add(WHAT_YOU_CAN.callbackData("bot=can").build());
                    rowButtons2.add(ALL_INFO.callbackData("information=bot").build());
                    back = "01";
                    break;
                case "can":
                    rowButtons.add(COMMANDS.callbackData("bot=commands").build());
                    rowButtons1.add(WITH_SERVER.callbackData("bot=server").build());
                    rowButtons2.add(WITH_PLAYER.callbackData("bot=player").build());
                    rowButtons3.add(ADMIN.build());
                    back = "2";
                    break;
                default:
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

    public static class Me {
        public static synchronized InlineKeyboardMarkup me(@NotNull String type) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons4 = new ArrayList<>();
            String back = "0";

            switch (type) {
                case "null":
                    rowButtons.add(SETTINGS.callbackData("settings=list").build());
                    rowButtons1.add(QUESTION.callbackData("question=me").build());
                    rowButtons2.add(CHANGE_PROFILE.callbackData("account=profile").build());
                    back = "0";
                    break;
                case "0":
                    rowButtons.add(SET_AVATAR.callbackData("account=setavatar").build());
                    rowButtons1.add(SET_INFO.callbackData("account=setinfo").build());
                    back = "1";
                    break;
                case "1":
                    rowButtons.add(SET_AVATAR.callbackData("account=setavatar").build());
                    rowButtons1.add(CHANGE_INFO.callbackData("account=setinfo").build());
                    rowButtons1.add(REMOVE_INFO.callbackData("account=setinfo=0").build());
                    back = "1";
                    break;
                case "2":
                    rowButtons.add(CHANGE_AVATAR.callbackData("account=setavatar").build());
                    rowButtons.add(REMOVE_AVATAR.callbackData("account=setavatar=0").build());
                    rowButtons1.add(SET_INFO.callbackData("account=setinfo").build());
                    back = "1";
                    break;
                case "3":
                    rowButtons.add(CHANGE_AVATAR.callbackData("account=setavatar").build());
                    rowButtons.add(REMOVE_AVATAR.callbackData("account=setavatar=0").build());
                    rowButtons1.add(CHANGE_INFO.callbackData("account=setinfo").build());
                    rowButtons1.add(REMOVE_INFO.callbackData("account=setinfo=0").build());
                    back = "1";
                    break;
                case "question":
                    rowButtons.add(HOW_TO_USE.callbackData("question=me=1").build());
                    rowButtons1.add(I_WANT_TO_GET.callbackData("question=me=2").build());
                    rowButtons2.add(IDEA_OTHER.callbackData("question=me=3").build());
                    rowButtons3.add(ADMIN.build());
                    back = "1";
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

    public static class Server{
        public static synchronized InlineKeyboardMarkup server() {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();

            rowButtons.add(PLAYERS.callbackData("server=players").build());
            rowButtons1.add(SERVER_INFO.callbackData("server=info").build());
            rowButtons2.add(COMING_SOON.callbackData("surveys=info").build());
            rowButtons3.add(BACK.callbackData("back=0").build());

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(rowButtons);
            keyboard.add(rowButtons1);
            keyboard.add(rowButtons2);
            keyboard.add(rowButtons3);
            KEYBOARD_MARKUP.setKeyboard(keyboard);
            return KEYBOARD_MARKUP;
        }

        public static synchronized InlineKeyboardMarkup players(int row) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons4 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons5 = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons6 = new ArrayList<>();
            ArrayList<String> names = getNames();
            Collections.sort(names);

            if((row + 1) * 10 > names.size()) {
                int naming = names.size() - row * 10;
                switch (naming) {
                    case 9: String name1 = names.get(row * 10 + 8); rowButtons3.add(BASIC.text(name1).callbackData("server=players=" + name1 + "=" + row).build());
                    case 8: String name2 = names.get(row * 10 + 7); rowButtons2.add(BASIC.text(name2).callbackData("server=players=" + name2 + "=" + row).build());
                    case 7: String name3 = names.get(row * 10 + 6); rowButtons1.add(BASIC.text(name3).callbackData("server=players=" + name3 + "=" + row).build());
                    case 6: String name4 = names.get(row * 10 + 5); rowButtons.add(BASIC.text(name4).callbackData("server=players=" + name4 + "=" + row).build());
                    case 5: String name5 = names.get(row * 10 + 4); rowButtons4.add(BASIC.text(name5).callbackData("server=players=" + name5 + "=" + row).build());
                    case 4: String name6 = names.get(row * 10 + 3); rowButtons3.add(BASIC.text(name6).callbackData("server=players=" + name6 + "=" + row).build());
                    case 3: String name7 = names.get(row * 10 + 2); rowButtons2.add(BASIC.text(name7).callbackData("server=players=" + name7 + "=" + row).build());
                    case 2: String name8 = names.get(row * 10 + 1); rowButtons1.add(BASIC.text(name8).callbackData("server=players=" + name8 + "=" + row).build());
                    case 1: String name9 = names.get(row * 10); rowButtons.add(BASIC.text(name9).callbackData("server=players=" + name9 + "=" + row).build());
                }
            } else {
                String name1 = names.get(row * 10);
                String name2 = names.get(row * 10 + 1);
                String name3 = names.get(row * 10 + 2);
                String name4 = names.get(row * 10 + 3);
                String name5 = names.get(row * 10 + 4);
                String name6 = names.get(row * 10 + 5);
                String name7 = names.get(row * 10 + 6);
                String name8 = names.get(row * 10 + 7);
                String name9 = names.get(row * 10 + 8);
                String name10 = names.get(row * 10 + 9);

                rowButtons.add(BASIC.text(name1).callbackData("server=players=" + name1 + "=" + row).build());
                rowButtons1.add(BASIC.text(name2).callbackData("server=players=" + name2 + "=" + row).build());
                rowButtons2.add(BASIC.text(name3).callbackData("server=players=" + name3 + "=" + row).build());
                rowButtons3.add(BASIC.text(name4).callbackData("server=players=" + name4 + "=" + row).build());
                rowButtons4.add(BASIC.text(name5).callbackData("server=players=" + name5 + "=" + row).build());
                rowButtons.add(BASIC.text(name6).callbackData("server=players=" + name6 + "=" + row).build());
                rowButtons1.add(BASIC.text(name7).callbackData("server=players=" + name7 + "=" + row).build());
                rowButtons2.add(BASIC.text(name8).callbackData("server=players=" + name8 + "=" + row).build());
                rowButtons3.add(BASIC.text(name9).callbackData("server=players=" + name9 + "=" + row).build());
                rowButtons4.add(BASIC.text(name10).callbackData("server=players=" + name10 + "=" + row).build());
            }


            rowButtons5.add(BASIC.text("1").callbackData("server=players").build());
            rowButtons5.add(BASIC.text("<").callbackData("server=players=row=" + (row - 1)).build());
            rowButtons5.add(BASIC.text(String.valueOf(row + 1)).callbackData("a").build());
            rowButtons5.add(BASIC.text(">").callbackData("server=players=row=" + (row + 1)).build());
            rowButtons5.add(BASIC.text(String.valueOf((names.size() + 9) / 10)).callbackData("server=players=row=" + ((names.size() - 1) / 10)).build());
            rowButtons6.add(BACK.callbackData("back=3").build());

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(rowButtons);
            keyboard.add(rowButtons1);
            keyboard.add(rowButtons2);
            keyboard.add(rowButtons3);
            keyboard.add(rowButtons4);
            keyboard.add(rowButtons5);
            keyboard.add(rowButtons6);
            KEYBOARD_MARKUP.setKeyboard(keyboard);
            return KEYBOARD_MARKUP;
        }

        public static synchronized InlineKeyboardMarkup info(String row, String name) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            List<InlineKeyboardButton> rowButtons1 = new ArrayList<>();

            rowButtons.add(LIKE.callbackData("server=like=" + name + "=" + row).build());
            rowButtons.add(DISLIKE.callbackData("server=dislike=" + name + "=" + row).build());
            rowButtons1.add(BACK.callbackData("server=players=row=" + row).build());

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(rowButtons);
            keyboard.add(rowButtons1);
            KEYBOARD_MARKUP.setKeyboard(keyboard);
            return KEYBOARD_MARKUP;
        }

        public static synchronized InlineKeyboardMarkup playerBack(int row) {
            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            rowButtons.add(BACK.callbackData("server=players=row=" + row).build());

            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(rowButtons);
            KEYBOARD_MARKUP.setKeyboard(keyboard);
            return KEYBOARD_MARKUP;
        }
    }
}
