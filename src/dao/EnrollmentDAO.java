package dao;

import db.DBConnection;
import models.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // ================= ENROLL =================
    public static boolean enroll(int studentId, int courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id, enrolled_at) VALUES (?, ?, NOW())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= GET ALL =================
    public static List<Enrollment> getAll() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setId(rs.getInt("id"));
                e.setStudentId(rs.getInt("student_id"));
                e.setCourseId(rs.getInt("course_id"));
                e.setEnrolledAt(rs.getTimestamp("enrolled_at"));
                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static void deleteByStudentId(int studentId) {
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement("DELETE FROM enrollment WHERE student_id=?")) {
        ps.setInt(1, studentId);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
