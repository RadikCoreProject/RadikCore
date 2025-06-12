package bot.database;

import com.radik.Radik;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import static bot.database.Main.state;

public class Registration {


    public static int queuedPlayers(long userId, String username) {
        try {
            ResultSet st = state.executeQuery(String.format("SELECT name FROM Registered WHERE name = %s UNION SELECT name FROM RegistrationQueue WHERE name = %s", username, username));
            while (st.next()) { if (st.getString("name").equals(username)) { return 2; } }
        } catch (SQLException ignored) {}
        try {
            ResultSet st = state.executeQuery(String.format("SELECT id FROM Registered WHERE id = %d UNION SELECT id FROM RegistrationQueue WHERE id = %d", userId, userId));
            while (st.next()) { if (st.getLong("id") == (userId)) { return 1; } }
        } catch (SQLException ignored) {}
        return 0;
    }

    public static int isUserRegistered(Long id) {
        String query = "SELECT id FROM Registered WHERE id = ?";
        String query2 = "SELECT id FROM RegistrationQueue WHERE id = ?";

        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query);
             PreparedStatement pstmt2 = state.getConnection().prepareStatement(query2)) {
            pstmt.setLong(1, id);
            pstmt2.setLong(1, id);

            ResultSet st = pstmt.executeQuery();
            ResultSet st2 = pstmt2.executeQuery();

            while(st.next()) { if(st.getLong("id") == id) { return 2; } }
            while(st2.next()) { if(st2.getLong("id") == id) { return 0; } }
            return 1;
        }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static String[] isUserRegistered(String ip, String username) {
        String query = "SELECT ip, id FROM Registered WHERE name = ?";

        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {

            pstmt.setString(1, username);

            ResultSet st = pstmt.executeQuery();

            if (st.next()) {
                String registeredIp = st.getString("ip");
                long registeredId = st.getLong("id");
                Radik.LOGGER.info("{}-{}-{}", registeredIp, registeredId, username);

                if (registeredIp.equals("0")) {
                    return new String[]{username, String.valueOf(registeredId)};
                }
                if (registeredIp.equals(ip)) {
                    return new String[]{"1"};
                }
                else {
                    return new String[]{"2"};
                }
            } else {
                return new String[]{"0"};
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке регистрации: " + e.getMessage(), e);
        }
    }



    public static void updateIp(long id, String ip) {
        String updateQuery = "UPDATE Registered SET ip = ? WHERE id = ?";
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(updateQuery)) {
            pstmt.setString(1, ip);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Ошибка при обновлении IP-адреса: " + e.getMessage(), e); }
    }


    public static void endRegister(String username, long userId) {
        // Удаление из таблицы RegistrationQueue
        try {
            String deleteQuery = "DELETE FROM RegistrationQueue WHERE id = ?";
            try (PreparedStatement pstmt = state.getConnection().prepareStatement(deleteQuery)) {
                pstmt.setLong(1, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении из RegistrationQueue: " + e.getMessage(), e);
        }

        // Добавление строки в таблицу Registered
        try {
            String insertQuery = "INSERT INTO Registered (id, name, ip) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = state.getConnection().prepareStatement(insertQuery)) {
                pstmt.setLong(1, userId);
                pstmt.setString(2, username);
                pstmt.setString(3, "0");
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении в Registered: " + e.getMessage(), e);
        }

        try {
            String insertQuery = "INSERT INTO UserInfo (id) VALUES (?)";
            try (PreparedStatement pstmt = state.getConnection().prepareStatement(insertQuery)) {
                pstmt.setLong(1, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении в Registered: " + e.getMessage(), e);
        }

        String query = "INSERT INTO UserBooleanSettings (id, wrongIpNotify) VALUES (?, ?)";
        try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.setInt(2, 0);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }


    public static void cancelRegister(String username) {
        String deleteQuery = "DELETE FROM RegistrationQueue WHERE name = ?";
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(deleteQuery)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Логируем ошибку для отладки
            System.err.println("Ошибка при удалении из RegistrationQueue: " + e.getMessage());
        }
    }

}
