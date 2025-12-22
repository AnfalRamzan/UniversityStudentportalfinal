package dao;

import db.DBConnection;
import models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Get all courses
    public static List<Course> getAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course"; // make sure your table is 'course'

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setFee(rs.getDouble("fee")); // assuming your table has a 'fee' column
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Course getById(int id) {
        String sql = "SELECT * FROM course WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Course c = new Course();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setFee(rs.getDouble("fee"));
                    return c;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Add a new course
    public static boolean addCourse(Course c) {
        String sql = "INSERT INTO course(name, fee) VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setDouble(2, c.getFee());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) c.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
public static Course getByName(String name) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT * FROM course WHERE name=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Course c = new Course();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            return c;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    // Update course
    public static boolean updateCourse(Course c) {
        String sql = "UPDATE course SET name=?, fee=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setDouble(2, c.getFee());
            ps.setInt(3, c.getId());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete course
    public static boolean deleteCourse(int id) {
        String sql = "DELETE FROM course WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Alias for AdminDashboard
    public static List<Course> getAllCourses() {
        return getAll();
    }
}
