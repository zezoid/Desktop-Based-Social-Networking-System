package dao;

import model.Post;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    /* ================= TEXT-ONLY POST ================= */
    public void create(int userId, String content) {

        String sql = "INSERT INTO post (user_id, content) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, content);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= POST WITH MEDIA ================= */
    public void create(int userId, String content,
                       String mediaPath, String mediaType) {

        String sql = """
            INSERT INTO post (user_id, content, media_path, media_type)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, content);

            if (mediaPath == null) {
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(3, mediaPath);
                ps.setString(4, mediaType);
            }

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= UPDATE POST (EDIT) ================= */
    public void updatePost(int postId, int userId, String newContent) {

        String sql = """
            UPDATE post
            SET content = ?
            WHERE post_id = ? AND user_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newContent);
            ps.setInt(2, postId);
            ps.setInt(3, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= DELETE POST (SAFE) ================= */
    public void deletePost(int postId, int userId) {

        String deleteLikes = "DELETE FROM `like` WHERE post_id = ?";
        String deleteComments = "DELETE FROM comment WHERE post_id = ?";
        String deletePost = "DELETE FROM post WHERE post_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(deleteLikes);
                 PreparedStatement ps2 = con.prepareStatement(deleteComments);
                 PreparedStatement ps3 = con.prepareStatement(deletePost)) {

                ps1.setInt(1, postId);
                ps1.executeUpdate();

                ps2.setInt(1, postId);
                ps2.executeUpdate();

                ps3.setInt(1, postId);
                ps3.setInt(2, userId);
                ps3.executeUpdate();

                con.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= GLOBAL FEED ================= */
    public List<Post> feed(int userId) {

        List<Post> posts = new ArrayList<>();

        String sql = """
            SELECT
                p.post_id,
                p.user_id,
                u.username,
                p.content,
                p.media_path,
                p.media_type
            FROM post p
            JOIN `user` u ON u.user_id = p.user_id
            ORDER BY p.created_at DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("content"),
                        rs.getString("media_path"),
                        rs.getString("media_type")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }

    /* ================= USER PROFILE POSTS ================= */
    public List<Post> getUserPosts(int userId) {

        List<Post> posts = new ArrayList<>();

        String sql = """
            SELECT
                p.post_id,
                p.user_id,
                u.username,
                p.content,
                p.media_path,
                p.media_type
            FROM post p
            JOIN `user` u ON u.user_id = p.user_id
            WHERE p.user_id = ?
            ORDER BY p.created_at DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt("post_id"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("content"),
                            rs.getString("media_path"),
                            rs.getString("media_type")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }
}
