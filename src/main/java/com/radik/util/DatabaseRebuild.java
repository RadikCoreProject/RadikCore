package com.radik.util;

import java.sql.*;

public class DatabaseRebuild {
    private static final String URL = "jdbc:sqlite:Data.db";
    private static final String URL_OLD = "jdbc:sqlite:Data_old.db";

    private static Connection newConn;
    private static Connection oldConn;

    public static void main(String[] args) {
        try {
            oldConn = DriverManager.getConnection(URL_OLD);
            newConn = DriverManager.getConnection(URL);

            oldConn.setAutoCommit(true);
            newConn.setAutoCommit(false);

            registeredRebuild();
            registrationsRebuild();
            userInfoRebuild();
            decorationsRebuild();

            newConn.commit();

        } catch (SQLException e) {
            System.err.println("Migration failed: " + e.getMessage());
            try {
                if (newConn != null) newConn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
        } finally {
            closeQuietly(oldConn);
            closeQuietly(newConn);
        }
    }

    private static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    private static void registrationsRebuild() throws SQLException {
        String selectSQL = "SELECT id, law, question FROM Registered";
        String insertSQL = "INSERT OR IGNORE INTO Registrations (id, law, question) VALUES (?, ?, ?)";

        try (PreparedStatement selectStmt = oldConn.prepareStatement(selectSQL);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insertStmt = newConn.prepareStatement(insertSQL)) {
            while (rs.next()) {
                insertStmt.setLong(1, rs.getLong("id"));
                insertStmt.setInt(2, rs.getInt("law"));
                insertStmt.setString(3, rs.getString("question"));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

    private static void registeredRebuild() throws SQLException {
        String selectSQL = "SELECT id, name, password, setsPassword, ip FROM Registered";
        String insertSQL = "INSERT OR IGNORE INTO Registered (name, id, ip, password, setsPassword, reg) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement selectStmt = oldConn.prepareStatement(selectSQL);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insertStmt = newConn.prepareStatement(insertSQL)) {
            while (rs.next()) {
                insertStmt.setString(1, rs.getString("name"));
                insertStmt.setLong(2, rs.getLong("id"));
                insertStmt.setString(3, rs.getString("ip"));
                insertStmt.setString(4, rs.getString("password"));
                insertStmt.setInt(5, rs.getInt("setsPassword"));
                insertStmt.setInt(6, 1);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

    private static void decorationsRebuild() throws SQLException {
        String selectSQL = "SELECT id FROM Registered";
        String insertSQL = "INSERT OR IGNORE INTO Decorations (id) VALUES (?)";

        try (PreparedStatement selectStmt = oldConn.prepareStatement(selectSQL);
             ResultSet rs = selectStmt.executeQuery();
             PreparedStatement insertStmt = newConn.prepareStatement(insertSQL)) {
            while (rs.next()) {
                insertStmt.setString(1, rs.getString("id"));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

    private static void userInfoRebuild() throws SQLException {
        String selectUsersSQL = "SELECT * FROM UserInfo";
        String selectNameSQL = "SELECT name FROM Registered WHERE id = ?";
        String insertSQL = "INSERT OR IGNORE INTO UserInfo (name, reputation, likes, dislikes, job, reacted, info, setsInfo, setsAvatar, avatarId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement selectUsersStmt = oldConn.prepareStatement(selectUsersSQL);
             ResultSet usersRS = selectUsersStmt.executeQuery();
             PreparedStatement insertStmt = newConn.prepareStatement(insertSQL)) {
            while (usersRS.next()) {
                long userId = usersRS.getLong("id");
                String userName;
                try (PreparedStatement selectNameStmt = oldConn.prepareStatement(selectNameSQL)) {
                    selectNameStmt.setLong(1, userId);
                    try (ResultSet nameRS = selectNameStmt.executeQuery()) {
                        userName = nameRS.next() ? nameRS.getString("name") : "unknown";
                    }
                }
                insertStmt.setString(1, userName);
                insertStmt.setInt(2, usersRS.getInt("reputation"));
                insertStmt.setInt(3, usersRS.getInt("likes"));
                insertStmt.setInt(4, usersRS.getInt("dislikes"));
                insertStmt.setString(5, usersRS.getString("job"));
                insertStmt.setString(6, usersRS.getString("reacted"));
                insertStmt.setString(7, usersRS.getString("info"));
                insertStmt.setInt(8, usersRS.getInt("setsInfo"));
                insertStmt.setInt(9, usersRS.getInt("setsAvatar"));
                insertStmt.setString(10, usersRS.getString("avatarId"));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }
}
