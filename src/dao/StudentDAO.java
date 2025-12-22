package dao;

import db.DBConnection;
import models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    // ------------------- Authenticate Student -------------------
  public static Student authenticate(String email, String password) {
        String sql = "SELECT * FROM student WHERE email=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setPassword(rs.getString("password"));
                s.setCourseId(rs.getInt("course_id"));
                s.setFeesDue(rs.getDouble("fees_due"));
                s.setPhone(rs.getString("phone"));
//                s.setFee(rs.getDouble("fee"));
                return s; // login success
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // invalid credentials
    }


    // ------------------- Get All Students -------------------
    public static List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setCourseId(rs.getInt("course_id"));
                s.setFeesDue(rs.getDouble("fees_due"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ------------------- Get Student By ID -------------------
    public static Student getById(int id) {
        String sql = "SELECT * FROM student WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setCourseId(rs.getInt("course_id"));
                s.setFeesDue(rs.getDouble("fees_due"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ------------------- Add Student -------------------
  public static boolean addStudent(Student s) {
    String sql = "INSERT INTO student(name, email, password, course_id, fees_due) VALUES(?,?,?,?,?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, s.getName());
        ps.setString(2, s.getEmail());
        ps.setString(3, s.getPassword()); // mandatory
        if (s.getCourseId() != null) {
            ps.setInt(4, s.getCourseId());
        } else {
            ps.setNull(4, Types.INTEGER);
        }
        ps.setDouble(5, s.getFeesDue());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // ------------------- Update Student -------------------
    public static boolean updateStudent(Student s) {
        String sql = "UPDATE student SET name=?, email=?, course_id=?, fees_due=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setInt(3, s.getCourseId());
            ps.setDouble(4, s.getFeesDue());
            ps.setInt(5, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ------------------- Delete Student -------------------
   public static boolean deleteStudent(int studentId) {
    String deleteAttendance = "DELETE FROM attendance WHERE student_id=?";
    String deleteStudent = "DELETE FROM student WHERE id=?";
    
    try (Connection conn = DBConnection.getConnection()) {
        // Start transaction
        conn.setAutoCommit(false);

        try (PreparedStatement ps1 = conn.prepareStatement(deleteAttendance);
             PreparedStatement ps2 = conn.prepareStatement(deleteStudent)) {

            ps1.setInt(1, studentId);
            ps1.executeUpdate(); // delete all attendance for student

            ps2.setInt(1, studentId);
            int rows = ps2.executeUpdate(); // delete student
            conn.commit();
            return rows > 0;

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public static boolean updatePassword(int studentId, String newPassword) {
        String sql = "UPDATE student SET password=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
