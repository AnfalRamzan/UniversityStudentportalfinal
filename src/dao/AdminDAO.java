package dao;
import db.DBConnection;
import models.Admin;

import java.sql.*;

public class AdminDAO {
    public static boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username=? AND password=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch(SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username,password,name) VALUES(?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getName());
            return ps.executeUpdate() == 1;
        } catch(SQLException e) { e.printStackTrace(); return false; }
    }
}
