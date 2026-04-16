package dao;

import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /* ================= SEND ================= */
    public void send(int sender, int receiver, String text) {

        String sql = "INSERT INTO message (sender_id, receiver_id, text) VALUES (?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sender);
            ps.setInt(2, receiver);
            ps.setString(3, text);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 MUST
        }
    }

    /* ================= CHAT ================= */
    public List<String> chat(int a, int b) {

        List<String> list = new ArrayList<>();

        String sql = """
            SELECT sender_id, text
            FROM message
            WHERE (sender_id = ? AND receiver_id = ?)
               OR (sender_id = ? AND receiver_id = ?)
            ORDER BY created_at ASC
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, a);
            ps.setInt(2, b);
            ps.setInt(3, b);
            ps.setInt(4, a);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int sender = rs.getInt("sender_id");
                String msg = rs.getString("text");

                list.add((sender == a ? "You: " : "Them: ") + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}