package bot.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static bot.database.Main.state;

public class User {

    private static final List<String> INTEGER_INFO = Arrays.asList("reputation", "likes", "dislikes", "hasAvatar", "setsAvatar", "setsInfo");
    private static final List<String> STRING_INFO = Arrays.asList("job", "reacted", "info", "avatarId");

    public static int getIntegerUserInfo(long id, String info) {
        String query = String.format("SELECT %s FROM UserInfo WHERE id = ?", info);
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet st = pstmt.executeQuery();
            return st.getInt(info);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] getIntegerUserInfo(long id) {
        String query = "SELECT * FROM UserInfo WHERE id = ?";
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet st = pstmt.executeQuery();
            return new int[]{st.getInt("reputation"), st.getInt("likes"), st.getInt("dislikes"), st.getInt("hasAvatar"), st.getInt("setsInfo"), st.getInt("setsAvatar")};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringUserInfo(long id, String info) {
        String query = String.format("SELECT %s FROM UserInfo WHERE id = ?", info);
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet st = pstmt.executeQuery();
            return st.getString(info);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getStringUserInfo(long id) {
        String query = "SELECT * FROM UserInfo WHERE id = ?";
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet st = pstmt.executeQuery();
            return new String[]{st.getString("job"), st.getString("reacted"), st.getString("info"), st.getString("avatarId")};
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setUserInfo(String setting, String states, long userId) {
        if(INTEGER_INFO.contains(setting)) { setIntegerUserInfo(setting, Integer.parseInt(states), userId); }
        else if(STRING_INFO.contains(setting)) { setStringUserInfo(setting, states, userId); }
    }

    private static void setIntegerUserInfo(String setting, int states, long userId) {
        String query = String.format("UPDATE UserInfo SET %s = ? WHERE id = ?", setting);
        try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
            stmt.setInt(1, states);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }

    private static void setStringUserInfo(String setting, String states, long userId) {
        String query = String.format("UPDATE UserInfo SET %s = ? WHERE id = ?", setting);
        try (PreparedStatement stmt = state.getConnection().prepareStatement(query)) {
            stmt.setString(1, states);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
    }
}
