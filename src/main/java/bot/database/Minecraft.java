package bot.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static bot.database.Main.state;

public class Minecraft {
    public static String getMinecraftName(long id) {
        String query = "SELECT * FROM Registered WHERE id = ?";
        String minecraftName = null;
        try (PreparedStatement pstmt = state.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                minecraftName = rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return minecraftName;
    }

}
