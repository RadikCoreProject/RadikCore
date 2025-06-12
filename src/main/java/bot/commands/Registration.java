package bot.commands;

import bot.keyboard.Register;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.*;
import java.util.Arrays;

import static bot.Bot.*;
import static bot.database.Main.*;
import static bot.database.Registration.*;
import static bot.keyboard.KeyboardButtons.admin;
import static bot.keyboard.KeyboardButtons.emptyMarkup;

public class Registration {

    public static synchronized void registerUser(Update update) {
        Message message = update.getMessage();
        String chatId = String.valueOf(message.getChatId());
        MESSAGE.setChatId(chatId);
        long userId = message.getFrom().getId();
        String[] text = message.getText().split(" ");
        System.out.println(Arrays.toString(text));
        if(text.length != 2) {
            MESSAGE.setText("Неправильный синтаксис команды!\n\n*/register {name}*");
            MESSAGE.setReplyMarkup(emptyMarkup());
            try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
            return;
        }
        String username = text[1];
        if (testRegister(userId, username)) {return;}

        String sql = "INSERT INTO RegistrationQueue (name, id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        MESSAGE.setText("Ваша заявка на регистрацию с ником " + username + " успешно отправлена на рассмотрение.");
        MESSAGE.setReplyMarkup(admin());
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }

        MESSAGE.setChatId(String.valueOf(ADMIN));
        MESSAGE.setText(String.format("игрок хочет зарегистрироваться под ником %s", username));
        MESSAGE.setReplyMarkup(Register.registerMarkup(userId, username));
        try { BOT.execute(MESSAGE); } catch (TelegramApiException e) { throw new RuntimeException(e); }
    }

    public static synchronized boolean testRegister(long userId, String username) {
        MESSAGE.setReplyMarkup(emptyMarkup());
        try {
            int append = queuedPlayers(userId, username);

            if (append == 1) {
                MESSAGE.setText("Ты уже зарегистрировал свой никнейм или находишься в процессе регистрации.");
                BOT.execute(MESSAGE);
                return true;
            } else if (append == 2) {
                MESSAGE.setText("Этот ник уже занят. Попробуй зарегистрировать другой никнейм");
                BOT.execute(MESSAGE);
                return true;
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}