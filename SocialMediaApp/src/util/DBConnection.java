package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String HOST_URL =
        "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_URL =
        "jdbc:mysql://localhost:3306/social_media_db?useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "0000";

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try (Connection con =
                 DriverManager.getConnection(HOST_URL, USER, PASSWORD)) {

            con.createStatement().executeUpdate(
                "CREATE DATABASE IF NOT EXISTS social_media_db"
            );

        } catch (SQLException e) {
            throw new RuntimeException("Database init failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
