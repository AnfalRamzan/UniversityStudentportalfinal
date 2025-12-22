package models;

import java.sql.Date; // use java.sql.Date for DB mapping

public class Assignment {

    private int id;
    private int studentId;
    private int courseId;
    private String title;
    private Date submissionDate;   // matches DB
    private String status;         // matches DB
    private String filePath;

    // ---------- GETTERS & SETTERS ----------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(Date submissionDate) { this.submissionDate = submissionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

}
