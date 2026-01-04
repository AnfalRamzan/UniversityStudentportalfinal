package models;

import java.sql.Timestamp;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private Timestamp enrolledAt;

    public Enrollment() {
    }

    public Enrollment(int studentId, int courseId, Timestamp enrolledAt) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setEnrolledAt(Timestamp enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
