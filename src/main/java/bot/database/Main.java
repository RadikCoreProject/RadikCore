package bot.database;

import com.radik.Radik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Main {
    public static final String DATABASE = "jdbc:sqlite:Data.db";
    public static Connection conn;
    public static Statement state;

    static {
        Radik.LOGGER.info("Текущий рабочий каталог: {}", System.getProperty("user.dir"));
        try {
            conn = DriverManager.getConnection(DATABASE);
            state = conn.createStatement();
            createTables(state);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для создания таблиц
    static void createTables(Statement stmt) throws SQLException {
        String createRegisteredTable = """
                CREATE TABLE IF NOT EXISTS Registered (
                name TEXT NOT NULL,
                id INTEGER NOT NULL,
                ip TEXT NOT NULL DEFAULT 0,
                law INTEGER NOT NULL DEFAULT 0,
                password STRING NOT NULL DEFAULT '',
                setsPassword INTEGER NOT NULL DEFAULT 0,
                question TEXT NOT NULL DEFAULT 0,
                PRIMARY KEY(id));
                """;

        String createRegistrationQueueTable = """
                CREATE TABLE IF NOT EXISTS RegistrationQueue (
                name TEXT NOT NULL,
                id INTEGER NOT NULL);
                """;

        String createUserBooleanSettingsTable = """
                CREATE TABLE IF NOT EXISTS UserBooleanSettings (
                id INTEGER NOT NULL,
                wrongIpNotify INTEGER NOT NULL DEFAULT 1,
                allowTg INTEGER NOT NULL DEFAULT 1,
                allowPrivateTg INTEGER NOT NULL DEFAULT 1,
                PRIMARY KEY(id));
                """;

        String createUserInfoTable = """
                CREATE TABLE IF NOT EXISTS UserInfo (
                id INTEGER NOT NULL UNIQUE,
                reputation INTEGER NOT NULL DEFAULT 0,
                likes INTEGER NOT NULL DEFAULT 0,
                dislikes INTEGER NOT NULL DEFAULT 0,
                job	TEXT NOT NULL DEFAULT 'игрок',
                reacted	TEXT NOT NULL DEFAULT '0',
                hasAvatar INTEGER NOT NULL DEFAULT 0,
                info TEXT NOT NULL DEFAULT '',
                setsInfo INTEGER NOT NULL DEFAULT 0,
                setsAvatar INTEGER NOT NULL DEFAULT 0,
                avatarId STRING NOT NULL DEFAULT '',
                PRIMARY KEY(id));
                """;

        String createSurveysTable = """
                CREATE TABLE IF NOT EXISTS Surveys (
                id INTEGER NOT NULL,
                name STRING NOT NULL,
                text STRING NOT NULL,
                buttons STRING NOT NULL,
                voted STRING NOT NULL DEFAULT '',
                votes STRING NOT NULL DEFAULT '',
                deadline STRING NOT NULL DEFAULT '',
                PRIMARY KEY(id));
                """;


        stmt.execute(createRegisteredTable);
        stmt.execute(createRegistrationQueueTable);
        stmt.execute(createUserBooleanSettingsTable);
        stmt.execute(createUserInfoTable);
        stmt.execute(createSurveysTable);
    }

    public static void connect() {
        if (conn != null) {
            Radik.LOGGER.info("DATABASE SUCCESS");
        } else {
            Radik.LOGGER.error("Соединение с базой данных не установлено.");
        }
    }
}
