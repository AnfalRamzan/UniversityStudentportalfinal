package dao;

import db.DBConnection;
import models.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradesDAO {

    // Add a new grade
    public static boolean addGrade(Grade g) {
        String sql = "INSERT INTO grade (student_id, course_id, grade) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, g.getStudentId());
            ps.setInt(2, g.getCourseId());
            ps.setString(3, g.getGrade());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update existing grade
    public static boolean updateGrade(Grade g) {
        String sql = "UPDATE grade SET grade=? WHERE student_id=? AND course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getGrade());
            ps.setInt(2, g.getStudentId());
            ps.setInt(3, g.getCourseId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a grade
    public static boolean deleteGrade(int studentId, int courseId) {
        String sql = "DELETE FROM grade WHERE student_id=? AND course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get grade by student and course
    public static Grade getByStudentCourse(int studentId, int courseId) {
        Grade g = null;
        String sql = "SELECT * FROM grade WHERE student_id=? AND course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                g = new Grade();
                g.setStudentId(rs.getInt("student_id"));
                g.setCourseId(rs.getInt("course_id"));
                g.setGrade(rs.getString("grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return g;
    }

    // Save grade (insert if new, update if exists)
    public static boolean saveGrades(Grade g) {
        Grade existing = getByStudentCourse(g.getStudentId(), g.getCourseId());
        if (existing == null) {
            return addGrade(g);
        } else {
            return updateGrade(g);
        }
    }

    // ✅ Get all grades of a student
    public static List<Grade> getGradesByStudent(int studentId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grade WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Grade g = new Grade();
                g.setStudentId(rs.getInt("student_id"));
                g.setCourseId(rs.getInt("course_id"));
                g.setGrade(rs.getString("grade"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Convert letter grade to points (example for GPA calculation)
    public static double gradeToPoint(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }
}
