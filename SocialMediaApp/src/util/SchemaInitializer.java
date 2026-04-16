package util;

import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void createTables() {

        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement()) {

            // ===== USER =====
            s.execute("""
                CREATE TABLE IF NOT EXISTS user (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE,
                    email VARCHAR(100) UNIQUE,
                    password_hash VARCHAR(255),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // ===== POST =====
            s.execute("""
            	    CREATE TABLE IF NOT EXISTS post (
            	        post_id INT AUTO_INCREMENT PRIMARY KEY,
            	        user_id INT NOT NULL,
            	        content TEXT,
            	        media_path VARCHAR(500),
            	        media_type VARCHAR(20),
            	        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            	        FOREIGN KEY (user_id) REFERENCES user(user_id)
            	            ON DELETE CASCADE
            	    )
            	""");
            // ===== FOLLOWER =====
            s.execute("""
                CREATE TABLE IF NOT EXISTS follower (
                    follower_id INT NOT NULL,
                    following_id INT NOT NULL,
                    PRIMARY KEY (follower_id, following_id),
                    CHECK (follower_id <> following_id),
                    FOREIGN KEY (follower_id) REFERENCES user(user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (following_id) REFERENCES user(user_id)
                        ON DELETE CASCADE
                )
            """);

            // ===== LIKE =====
            s.execute("""
                CREATE TABLE IF NOT EXISTS `like` (
                    user_id INT NOT NULL,
                    post_id INT NOT NULL,
                    PRIMARY KEY (user_id, post_id),
                    FOREIGN KEY (user_id) REFERENCES user(user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (post_id) REFERENCES post(post_id)
                        ON DELETE CASCADE
                )
            """);

            // ===== COMMENT =====
            s.execute("""
                CREATE TABLE IF NOT EXISTS comment (
                    comment_id INT AUTO_INCREMENT PRIMARY KEY,
                    post_id INT NOT NULL,
                    user_id INT NOT NULL,
                    comment_text TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (post_id) REFERENCES post(post_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (user_id) REFERENCES user(user_id)
                        ON DELETE CASCADE
                )
            """);

            // ===== MESSAGE =====
            s.execute("""
                CREATE TABLE IF NOT EXISTS message (
                    message_id INT AUTO_INCREMENT PRIMARY KEY,
                    sender_id INT NOT NULL,
                    receiver_id INT NOT NULL,
                    message_text TEXT,
                    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (sender_id) REFERENCES user(user_id)
                        ON DELETE CASCADE,
                    FOREIGN KEY (receiver_id) REFERENCES user(user_id)
                        ON DELETE CASCADE
                )
            """);

            // ===== NOTIFICATION =====
            s.execute("""
                CREATE TABLE IF NOT EXISTS notification (
                    notification_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    message VARCHAR(255),
                    is_read BOOLEAN DEFAULT FALSE,
                    FOREIGN KEY (user_id) REFERENCES user(user_id)
                        ON DELETE CASCADE
                )
            """);
            /* ================= MESSAGE ================= */
            s.execute("""
                CREATE TABLE IF NOT EXISTS message (
                    message_id INT AUTO_INCREMENT PRIMARY KEY,
                    sender_id INT NOT NULL,
                    receiver_id INT NOT NULL,
                    text TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (sender_id)
                    REFERENCES user(user_id)
                    ON DELETE CASCADE,

                    FOREIGN KEY (receiver_id)
                    REFERENCES user(user_id)
                    ON DELETE CASCADE
                )
            """);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
