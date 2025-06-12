package bot.keyboard;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class KeyboardButtons {
    // markup
    protected static final InlineKeyboardMarkup KEYBOARD_MARKUP = new InlineKeyboardMarkup();
    public static final InlineKeyboardButton.InlineKeyboardButtonBuilder BASIC = InlineKeyboardButton.builder();

    // back
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder BACK = InlineKeyboardButton.builder().text("⬅️Назад");

    // admin
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ADMIN = InlineKeyboardButton.builder().text("Поддержка").url(String.format("tg://openmessage?user_id=%d", Bot.ADMIN));
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ANSWER_ENABLE = InlineKeyboardButton.builder().text("Ответить на вопрос");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ANSWER_DISABLE = InlineKeyboardButton.builder().text("Отклонить вопрос");

    // registration
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder CANCEL = InlineKeyboardButton.builder().text("Отменить регистрацию");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder DISAGREE = InlineKeyboardButton.builder().text("❌ОТКЛОНИТЬ❌");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder AGREE = InlineKeyboardButton.builder().text("✅ПРИНЯТЬ✅");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder USER_INFO = InlineKeyboardButton.builder().text("Посмотреть профиль");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder LAW_INFO = InlineKeyboardButton.builder().text("Правовая информация");

    // settings
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SETTINGS = InlineKeyboardButton.builder().text("Настройки");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder DISABLE_NOTIFY = InlineKeyboardButton.builder().text("Отключить уведомления о заходах");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder REPORT_TO_ADMIN = InlineKeyboardButton.builder().text("Отпраивть репорт админу");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ITS_ME = InlineKeyboardButton.builder().text("ЭТО Я");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ITS_ME2 = InlineKeyboardButton.builder().text("НЕ МОГУ ЗАЙТИ");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder USER_SETTINGS = InlineKeyboardButton.builder().text("Настройки аккаунта");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SERVER_SETTINGS = InlineKeyboardButton.builder().text("Настройки сервера");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SET_PASSWORD = InlineKeyboardButton.builder().text("Поставить пароль");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder CHANGE_PASSWORD = InlineKeyboardButton.builder().text("Изменить пароль");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder REMOVE_PASSWORD = InlineKeyboardButton.builder().text("Удалить пароль");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder LEAVE_ACCOUNT = InlineKeyboardButton.builder().text("🟨🟨ВЫЙТИ С АККАУНТА🟨🟨");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder DELETE_ACCOUNT = InlineKeyboardButton.builder().text("🟥🟥УДАЛИТЬ АККАУНТ🟥🟥");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ALLOW_WARNINGS = InlineKeyboardButton.builder().text("Вкл / выкл уведомлений");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ALLOW_MESSAGES_SEND = InlineKeyboardButton.builder().text("Вкл / выкл сообщений из тг");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ALLOW_PRIVATE_MESSAGES_SEND = InlineKeyboardButton.builder().text("Вкл / выкл личных сообщений");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder I_KNOW_WHAT_I_DO = InlineKeyboardButton.builder().text("🟥Я ЗНАЮ ЧТО ДЕЛАЮ🟥");

    // on starting info
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ME = InlineKeyboardButton.builder().text("Мой аккаунт");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SERVER = InlineKeyboardButton.builder().text("О сервере");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder INFO = InlineKeyboardButton.builder().text("Что это?");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder QUESTION = InlineKeyboardButton.builder().text("Вопрос / подать заявку");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder CHANGE_PROFILE = InlineKeyboardButton.builder().text("Изменить профиль");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SET_AVATAR = InlineKeyboardButton.builder().text("Поставить аватар");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SET_INFO = InlineKeyboardButton.builder().text("Поставить описание");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder CHANGE_AVATAR = InlineKeyboardButton.builder().text("Изменить аватар");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder CHANGE_INFO = InlineKeyboardButton.builder().text("Изменить описание");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder REMOVE_AVATAR = InlineKeyboardButton.builder().text("Удалить аватар");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder REMOVE_INFO = InlineKeyboardButton.builder().text("Удалить описание");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder LAW = InlineKeyboardButton.builder().text("Правовая информация");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder WHAT_YOU_CAN = InlineKeyboardButton.builder().text("Зачем это?");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ALL_INFO = InlineKeyboardButton.builder().text("Полная информация");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder COMMANDS = InlineKeyboardButton.builder().text("Список команд");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder WITH_SERVER = InlineKeyboardButton.builder().text("Взаимодействия с сервером");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder WITH_PLAYER = InlineKeyboardButton.builder().text("Взаимодействия с пользователем");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder HOW_TO_USE = InlineKeyboardButton.builder().text("Как сделать ...?");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder I_WANT_TO_GET = InlineKeyboardButton.builder().text("Хочу подать заявку на ...");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder IDEA_OTHER = InlineKeyboardButton.builder().text("Идея для бота / сервера / другое");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder PLAYERS = InlineKeyboardButton.builder().text("Игроки");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SERVER_INFO = InlineKeyboardButton.builder().text("Информация о сервере");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder COMING_SOON = InlineKeyboardButton.builder().text("(Опросы) Вскоре...");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder LIKE = InlineKeyboardButton.builder().text("👍");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder DISLIKE = InlineKeyboardButton.builder().text("👎");

    // surveys
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder SURVEYS = InlineKeyboardButton.builder().text("Просмотр опросов");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder ADD_SURVEY = InlineKeyboardButton.builder().text("Добавить опрос");
    protected static final InlineKeyboardButton.InlineKeyboardButtonBuilder STOP_SURVEY = InlineKeyboardButton.builder().text("Остановить опрос");

    public static synchronized InlineKeyboardMarkup emptyMarkup() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }

    public static synchronized InlineKeyboardMarkup admin() {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        rowButtons.add(ADMIN.build());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }

    public static InlineKeyboardMarkup backMarkup(String num) {
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        rowButtons.add(BACK.callbackData("back=" + num).build());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(rowButtons);
        KEYBOARD_MARKUP.setKeyboard(keyboard);
        return KEYBOARD_MARKUP;
    }
}
