package dao;

import util.*;
import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;



public class UserDAO {

	  public User login(String username, String password) {
	        String sql =
	            "SELECT user_id, username FROM user WHERE username=? AND password_hash=?";

	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, username);
	            ps.setString(2, password);

	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                return new User(
	                    rs.getInt("user_id"),
	                    rs.getString("username")
	                );
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public boolean register(String username, String email, String password) {
	        String sql =
	            "INSERT INTO user (username, email, password_hash) VALUES (?, ?, ?)";

	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, username);
	            ps.setString(2, email);
	            ps.setString(3, password);
	            ps.executeUpdate();
	            return true;

	        } catch (SQLException e) {
	            return false;
	        }
	    }
	    
	    public Map<Integer, String> searchUsers(String keyword) {

	        Map<Integer, String> users = new HashMap<>();

	        String sql =
	            "SELECT user_id, username FROM user WHERE username LIKE ?";

	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, "%" + keyword + "%");
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                users.put(rs.getInt("user_id"), rs.getString("username"));
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return users;
	    }


    public Integer getUserId(String username) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 "SELECT user_id FROM user WHERE username=?")) {

            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {}
        return null;
    }
}
