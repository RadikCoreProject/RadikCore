package bot.database;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static bot.database.Main.*;
import static bot.database.Settings.GetUser.getData;
import static bot.database.Settings.GetUser.setData;
import static com.radik.logic.Craft.kick;

public class Settings {

    private static final List<String> BOOLEAN_SETTINGS = Arrays.asList("wrongIpNotify", "allowTg", "allowPrivateTg");

    public static class GetUser {
        @Contract("_ -> new")
        public static String @NotNull [] getData(String username) {
            String query = "SELECT * FROM Registered WHERE name = ?";
            try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
                pstmt.setString(1, username);
                ResultSet st = pstmt.executeQuery();
                return new String[]{st.getString("name"), String.valueOf(st.getLong("id")), st.getString("ip"), String.valueOf(st.getInt("law")), st.getString("password"), String.valueOf(st.getInt("setsPassword")), st.getString("question")};
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Contract("_ -> new")
        public static String @NotNull [] getData(long id) {
            String query = "SELECT * FROM Registered WHERE id = ?";
            try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
                pstmt.setLong(1, id);
                ResultSet st = pstmt.executeQuery();
                return new String[]{st.getString("name"), String.valueOf(st.getLong("id")), st.getString("ip"), String.valueOf(st.getInt("law")), st.getString("password"), String.valueOf(st.getInt("setsPassword")), st.getString("question")};
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public static void setData(String setting, String states, long userId) {
            String query = String.format("UPDATE Registered SET %s = ? WHERE id = ?", setting);
            try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
                stmt.setString(1, states);
                stmt.setLong(2, userId);
                stmt.executeUpdate();
            } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        }

        public static void setData(String setting, String states, String username) {
            String query = String.format("UPDATE Registered SET %s = ? WHERE name = ?", setting);
            try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
                stmt.setString(1, states);
                stmt.setString(2, username);
                stmt.executeUpdate();
            } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
        }
    }

    public static void setSetting(String setting, String value, long userId) {
        if (BOOLEAN_SETTINGS.contains(setting)) {
            setBooleanSetting(setting, Integer.parseInt(value), userId);
        }
    }

    public static int getCount(String table) {
        String query = String.format("SELECT COUNT(*) FROM %s", table);
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            ResultSet st = pstmt.executeQuery();
            st.next();
            return st.getInt(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ArrayList<String> getNames() {
        String query = "SELECT name FROM registered";
        ArrayList<String> names = new ArrayList<>();
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            ResultSet st = pstmt.executeQuery();
            while (st.next()) {
                names.add(st.getString("name"));
            }
            return names;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static ArrayList<Long> getIds() {
        String query = "SELECT name FROM registered";
        ArrayList<Long> names = new ArrayList<>();
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            ResultSet st = pstmt.executeQuery();
            while (st.next()) {
                names.add(st.getLong("name"));
            }
            return names;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static int[] getBooleanSettings(long userId) {
        String query = "SELECT * FROM UserBooleanSettings WHERE id = ?";
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, userId);
            ResultSet st = pstmt.executeQuery();
            st.next();
            return new int[]{st.getInt("wrongIpNotify"), st.getInt("allowTg"), st.getInt("allowPrivateTg")};
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static int getSetting(String setting, long userId) {
        if (BOOLEAN_SETTINGS.contains(setting)) {
            return getBooleanSetting(setting, userId);
        }
        return 0;
    }

    private static int getBooleanSetting(String setting, long id) {
        String query = "SELECT * FROM UserBooleanSettings WHERE id = ?";
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet st = pstmt.executeQuery();
            st.next();
            return st.getInt(setting);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private static void setBooleanSetting(String setting, int states, long userId) {
        String query = String.format("UPDATE UserBooleanSettings SET %s = ? WHERE id = ?", setting);
        try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
            stmt.setInt(1, states);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private static void deleteRow(long userId, String row) {
        String query = String.format("DELETE FROM %s WHERE id = ?", row);
        try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void leaveAccount(String type, long userId) {
        if(type.equals("leave")) {
            setData("id", "0", userId);
            setData("ip", "999", userId);
        }
        else {
            deleteRow(userId, "Registered");
            deleteRow(userId, "UserBooleanSettings");
            deleteRow(userId, "UserInfo");
        }
        kick(getData(userId)[0], type);
    }
}
