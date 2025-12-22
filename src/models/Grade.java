package models;

public class Grade {
    private int id;
    private int studentId;
    private int courseId;
    private String grade; // e.g., A, B, C

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
