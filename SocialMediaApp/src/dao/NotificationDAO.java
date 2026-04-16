package dao;

import util.*;
import java.sql.*;
import java.util.*;

public class NotificationDAO {

    public void notify(int u, String m) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "INSERT INTO notification(user_id,message) VALUES(?,?)")) {

            ps.setInt(1, u);
            ps.setString(2, m);
            ps.executeUpdate();
        } catch (Exception e) {}
    }

    public List<String> unread(int u) {
        List<String> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT message FROM notification WHERE user_id=? AND is_read=false")) {

            ps.setInt(1, u);
            var rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {}
        return list;
    }
}
