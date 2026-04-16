package dao;

import util.DBConnection;
import java.sql.*;

public class LikeDAO {

    // Check if user already liked the post
    public boolean hasLiked(int userId, int postId) {

        String sql = "SELECT 1 FROM `like` WHERE user_id=? AND post_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, postId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Toggle like / unlike
    public void toggleLike(int userId, int postId) {

        if (hasLiked(userId, postId)) {
            unlike(userId, postId);
        } else {
            like(userId, postId);
        }
    }

    private void like(int userId, int postId) {

        String sql = "INSERT INTO `like` (user_id, post_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, postId);
            ps.executeUpdate();

        } catch (Exception e) {
            // ignore duplicate insert
        }
    }

    private void unlike(int userId, int postId) {

        String sql = "DELETE FROM `like` WHERE user_id=? AND post_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, postId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLikeCount(int postId) {

        String sql = "SELECT COUNT(*) FROM `like` WHERE post_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
