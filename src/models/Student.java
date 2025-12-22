package models;

public class Student {
    private int id;
    private String name;
    private String email;
    private String password;
    private Integer courseId;  // wrapper type, allows null

    private String phone;
    private double fee; // Add this field
 private double feesDue;

    // Default constructor
    public Student() {
    }

    // Constructor with all fields
    public Student(int id, String name, String email, String password, int courseId, String phone, double feesDue) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.courseId = courseId;
        this.phone = phone;
        this.feesDue = feesDue;
    }

    // Constructor without ID (for insert)
    public Student(String name, String email, String password, int courseId, String phone, double feesDue,double fee) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.courseId = courseId;
        this.phone = phone;
        this.feesDue = feesDue;
    }
   public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
public double getFeesPaid() {
    return fee;  // assuming 'fee' stores the amount student has paid
}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   public Integer getCourseId() {
    return courseId;
}

public void setCourseId(Integer courseId) {
    this.courseId = courseId;
}


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

 public double getFeesDue() {
    return feesDue;
}

public void setFeesDue(double feesDue) {
    this.feesDue = feesDue;
}

}

