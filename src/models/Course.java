package models;

public class Course {
   private Integer id; // instead of int
       // course ID
    private String name;   // course name
    private double fee;    // course fee

    public Course() {}

    public Course(int id, String name, double fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    // For compatibility with existing code (AdminDashboard/StudentDashboard)
    public Integer getCourseId() { return id; }
    public void setCourseId(int id) { this.id = id; }

    public String getCourseName() { return name; }
    public void setCourseName(String name) { this.name = name; }
}
