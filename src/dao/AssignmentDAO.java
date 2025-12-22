package dao;

import db.DBConnection;
import models.Assignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    public static boolean saveAssignment(Assignment a) {

        String sql = "INSERT INTO assignment " +
                     "(student_id, course_id, title, submission_date, status, file_path) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getStudentId());
            ps.setInt(2, a.getCourseId());
            ps.setString(3, a.getTitle());
            ps.setDate(4, a.getSubmissionDate());
            ps.setString(5, a.getStatus());
            ps.setString(6, a.getFilePath());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Assignment> getAssignmentsByStudent(int studentId) {
        List<Assignment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM assignment WHERE student_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Assignment a = new Assignment();
                a.setStudentId(rs.getInt("student_id"));
                a.setCourseId(rs.getInt("course_id"));
                a.setTitle(rs.getString("title"));
                a.setFilePath(rs.getString("file_path"));
                a.setSubmissionDate(rs.getDate("submission_date"));  // ✅ fix
                a.setStatus(rs.getString("status"));                // ✅ fix
                list.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
