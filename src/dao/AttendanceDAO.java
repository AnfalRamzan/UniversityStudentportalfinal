package dao;

import db.DBConnection;
import models.Attendance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // ---------------- MARK ATTENDANCE ----------------
    // Insert or update attendance
    public static boolean markAttendance(Attendance a) {
        String check = "SELECT * FROM attendance WHERE student_id=? AND course_id=? AND date=?";
        String insert = "INSERT INTO attendance(student_id,course_id,date,status) VALUES(?,?,?,?)";
        String update = "UPDATE attendance SET status=? WHERE student_id=? AND course_id=? AND date=?";

        try (Connection conn = DBConnection.getConnection()) {
            // Check if record exists
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setInt(1, a.getStudentId());
            psCheck.setInt(2, a.getCourseId());
            psCheck.setDate(3, a.getDate());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                // Record exists, update it
                PreparedStatement psUpdate = conn.prepareStatement(update);
                psUpdate.setString(1, a.getStatus());
                psUpdate.setInt(2, a.getStudentId());
                psUpdate.setInt(3, a.getCourseId());
                psUpdate.setDate(4, a.getDate());
                return psUpdate.executeUpdate() > 0;
            } else {
                // Record does not exist, insert it
                PreparedStatement psInsert = conn.prepareStatement(insert);
                psInsert.setInt(1, a.getStudentId());
                psInsert.setInt(2, a.getCourseId());
                psInsert.setDate(3, a.getDate());
                psInsert.setString(4, a.getStatus());
                return psInsert.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- ADD ATTENDANCE ----------------
    // Simple insert (alias for markAttendance)
    public static boolean addAttendance(Attendance a) {
        return markAttendance(a);
    }

    // ---------------- GET ATTENDANCE BY STUDENT ----------------
    public static List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Attendance a = new Attendance();
                a.setId(rs.getInt("id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setCourseId(rs.getInt("course_id"));
                a.setDate(rs.getDate("date"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- GET ATTENDANCE BY STUDENT AND COURSE ----------------
    public static List<Attendance> getByStudent(int studentId) {
        return getAttendanceByStudent(studentId);
    }

    // ---------------- GET ATTENDANCE BY STUDENT, COURSE, AND DATE ----------------
    public static Attendance getByStudentCourseDate(int studentId, int courseId, Date date) {
        String sql = "SELECT * FROM attendance WHERE student_id=? AND course_id=? AND date=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDate(3, date);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Attendance a = new Attendance();
                a.setId(rs.getInt("id"));
                a.setStudentId(rs.getInt("student_id"));
                a.setCourseId(rs.getInt("course_id"));
                a.setDate(rs.getDate("date"));
                a.setStatus(rs.getString("status"));
                return a;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
