package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FollowDAO {

    public void follow(int me, int them) {

        if (me == them) return;

        String sql =
            "INSERT IGNORE INTO follower (follower_id, following_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, me);
            ps.setInt(2, them);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unfollow(int me, int them) {

        String sql =
            "DELETE FROM follower WHERE follower_id=? AND following_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, me);
            ps.setInt(2, them);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
