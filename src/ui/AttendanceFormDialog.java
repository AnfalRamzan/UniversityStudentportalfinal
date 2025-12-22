package ui;

import dao.AttendanceDAO;
import dao.CourseDAO;
import dao.StudentDAO;
import models.Attendance;
import models.Course;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AttendanceFormDialog extends JDialog {
    private JComboBox<Student> studentBox;
    private JComboBox<Course> courseBox;
    private JComboBox<String> statusBox;
    private JButton btnSave;

    public AttendanceFormDialog(JFrame parent) {
        super(parent, "Mark Attendance", true);
        setSize(400,250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4,2,10,10));

        add(new JLabel("Student:"));
        studentBox = new JComboBox<>();
        List<Student> students = StudentDAO.getAll();
        for(Student s: students) studentBox.addItem(s);
        add(studentBox);

        add(new JLabel("Course:"));
        courseBox = new JComboBox<>();
        List<Course> courses = CourseDAO.getAllCourses();
        for(Course c: courses) courseBox.addItem(c);
        add(courseBox);

        add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"Present","Absent"});
        add(statusBox);

        btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveAttendance());
        add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);
    }

    private void saveAttendance(){
        try {
            Student s = (Student) studentBox.getSelectedItem();
            Course c = (Course) courseBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();
            if(s==null || c==null || status==null){
                JOptionPane.showMessageDialog(this,"Fill all fields"); 
                return;
            }

            Attendance att = AttendanceDAO.getByStudentCourseDate(s.getId(), c.getId(), new Date(System.currentTimeMillis()));
            if(att == null) att = new Attendance();

            att.setStudentId(s.getId());
            att.setCourseId(c.getId());
            att.setStatus(status);
            att.setDate(new java.sql.Date(System.currentTimeMillis()));

            // Updated line
            AttendanceDAO.addAttendance(att);

            JOptionPane.showMessageDialog(this,"Attendance saved!");
            dispose();
        } catch(Exception ex){ 
            ex.printStackTrace(); 
            JOptionPane.showMessageDialog(this,"Error saving attendance"); 
        }
    }
}
