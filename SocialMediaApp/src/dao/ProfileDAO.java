package dao;

import util.DBConnection;
import model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileDAO {

    public UserProfile getProfile(int userId) {

        String sql = """
            SELECT
              u.username,
              u.email,
              (SELECT COUNT(*) FROM post WHERE user_id = ?) AS posts,
              (SELECT COUNT(*) FROM follower WHERE following_id = ?) AS followers,
              (SELECT COUNT(*) FROM follower WHERE follower_id = ?) AS following
            FROM user u
            WHERE u.user_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserProfile(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getInt("posts"),
                        rs.getInt("followers"),
                        rs.getInt("following")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean isFollowing(int me, int them) {

        String sql = "SELECT 1 FROM follower WHERE follower_id=? AND following_id=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, me);
            ps.setInt(2, them);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
}
