package dao;

import model.Comment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    // ================= ADD COMMENT =================
    public void add(int userId, int postId, String text) {

        String sql =
            "INSERT INTO comment(post_id, user_id, comment_text) VALUES (?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);
            ps.setString(3, text);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= COUNT =================
    public int getCommentCount(int postId) {

        String sql = "SELECT COUNT(*) FROM comment WHERE post_id=?";

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

    // ================= FOR SIMPLE DISPLAY (OLD USAGE) =================
    public List<String> getComments(int postId) {

        List<String> comments = new ArrayList<>();

        String sql = """
            SELECT u.username, c.comment_text
            FROM comment c
            JOIN `user` u ON u.user_id = c.user_id
            WHERE c.post_id = ?
            ORDER BY c.created_at
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                comments.add(rs.getString("username") + ": " +
                             rs.getString("comment_text"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    // ================= FOR DELETE SUPPORT =================
    public List<Comment> getCommentsWithOwner(int postId) {

        List<Comment> comments = new ArrayList<>();

        String sql = """
            SELECT c.comment_id, c.user_id, u.username, c.comment_text
            FROM comment c
            JOIN `user` u ON u.user_id = c.user_id
            WHERE c.post_id = ?
            ORDER BY c.created_at
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                comments.add(new Comment(
                    rs.getInt("comment_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("comment_text")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

    // ================= DELETE COMMENT =================
    public void deleteComment(int commentId, int userId) {

        String sql =
            "DELETE FROM comment WHERE comment_id=? AND user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isPostOwner(int postId, int userId) {

        String sql = "SELECT 1 FROM post WHERE post_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
