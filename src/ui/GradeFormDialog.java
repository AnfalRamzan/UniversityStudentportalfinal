package ui;

import dao.CourseDAO;
import dao.GradesDAO;
import dao.StudentDAO;
import models.Course;
import models.Grade;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GradeFormDialog extends JDialog {
    private JComboBox<Student> studentBox;
    private JComboBox<Course> courseBox;
    private JTextField gradeField;
    private JButton btnSave;

    public GradeFormDialog(JFrame parent) {
        super(parent, "Add/Edit Grade", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Student selection
        add(new JLabel("Student:"));
        studentBox = new JComboBox<>();
        List<Student> students = StudentDAO.getAll();
        for (Student s : students) studentBox.addItem(s);
        add(studentBox);

        // Course selection
        add(new JLabel("Course:"));
        courseBox = new JComboBox<>();
        List<Course> courses = CourseDAO.getAllCourses();
        for (Course c : courses) courseBox.addItem(c);
        add(courseBox);

        // Grade input
        add(new JLabel("Grade:"));
        gradeField = new JTextField();
        add(gradeField);

        // Save button
        btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveGrade());
        add(btnSave);

        // Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);
    }

    private void saveGrade() {
        try {
            Student s = (Student) studentBox.getSelectedItem();
            Course c = (Course) courseBox.getSelectedItem();
            String g = gradeField.getText().trim();

            if (s == null || c == null || g.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            // Get existing grade for student-course
            Grade grade = GradesDAO.getByStudentCourse(s.getId(), c.getId());
            if (grade == null) grade = new Grade();

            grade.setStudentId(s.getId());
            grade.setCourseId(c.getId());
            grade.setGrade(g);

            // Save grade
            GradesDAO.saveGrades(grade);

            JOptionPane.showMessageDialog(this, "Grade saved successfully!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving grade: " + ex.getMessage());
        }
    }
}
