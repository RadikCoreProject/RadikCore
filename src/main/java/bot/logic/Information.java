package bot.logic;

import bot.Bot;
import bot.keyboard.OnStart;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAnimation;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

import static bot.Bot.*;
import static bot.database.Settings.GetUser.getData;
import static bot.database.User.getIntegerUserInfo;
import static bot.database.User.getStringUserInfo;
import static bot.keyboard.KeyboardButtons.backMarkup;
import static bot.keyboard.OnStart.Server.playerBack;

public class Information {
    public static synchronized void sendUserProfile(long userId, long chatId, String method, int messagseId) {
        int[] integer_settings = getIntegerUserInfo(userId);
        String[] string_settings = getStringUserInfo(userId);
        int reputation = integer_settings[0];
        int likes = integer_settings[1];
        int dislikes = integer_settings[2];
        int hasAvatar = integer_settings[3];
        String job = string_settings[0];
        String info = string_settings[2];
        String username = getData(userId)[0];

        String default_path = "https://t.me/segatokar/209489";

        EDITED_MEDIA.setChatId(chatId);
        EDITED_MEDIA.setMessageId(messagseId);
        if (hasAvatar == 1) {
            String[] avatarId = string_settings[3].split("~");
            if (avatarId[1].equals("photo")) { EDITED_MEDIA.setMedia(new InputMediaPhoto(avatarId[0])); }
            else { { EDITED_MEDIA.setMedia(new InputMediaAnimation(avatarId[0])); } }
        }
        else { EDITED_MEDIA.setMedia(new InputMediaPhoto(default_path)); }

        EDITED_CAPTION.setChatId(chatId);
        EDITED_CAPTION.setMessageId(messagseId);
        EDITED_CAPTION.setCaption(String.format("Профиль *%s*\n\nОписание: *%s*\n\nРепутация: *%d*\nДолжность: *%s*\n👍: *%d*  /  👎: *%d*", username, info, reputation, job, likes, dislikes));

        LocalDateTime dateTime = LocalDateTime.now();
        String reacted = getStringUserInfo(chatId, "reacted");
        String[] data = method.split("=");
        if(method.equals("main")) {
            EDITED_CAPTION.setReplyMarkup(OnStart.Me.me("null"));
        } else if(chatId == userId) {
            EDITED_CAPTION.setReplyMarkup(playerBack(Integer.parseInt(data[0])));
        } else if(!reacted.isEmpty()) {
            if(dateTime.isBefore(LocalDateTime.parse(reacted).plusDays(1))) {
                EDITED_CAPTION.setReplyMarkup(playerBack(Integer.parseInt(data[0])));
            }
            else {
                EDITED_CAPTION.setReplyMarkup(OnStart.Server.info(data[0], getData(userId)[0]));
            }
        }
        else {
            EDITED_CAPTION.setReplyMarkup(OnStart.Server.info(data[0], getData(userId)[0]));
        }
        try { Bot.BOT.execute(EDITED_MEDIA); } catch (TelegramApiException e) { throw new RuntimeException(e); }
        try { Bot.BOT.execute(EDITED_CAPTION); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }
}
