package ui;

import dao.CourseDAO;
import dao.StudentDAO;
import models.Course;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentFormDialog extends JDialog {
    private JTextField txtName, txtEmail, txtPassword, txtFees;
    private JComboBox<String> cbCourses;
    private Integer[] courseIds;
    private boolean saved = false;
    private Student editing;

    public StudentFormDialog(Frame owner, Student s) {
        super(owner, true);
        editing = s;
        setTitle(s == null ? "Add Student" : "Edit Student");
        setSize(400,300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(6,2,8,8));

        add(new JLabel("Name:")); txtName = new JTextField(); add(txtName);
        add(new JLabel("Email:")); txtEmail = new JTextField(); add(txtEmail);
        add(new JLabel("Password:")); txtPassword = new JTextField(); add(txtPassword);
        add(new JLabel("Course:")); cbCourses = new JComboBox<>(); add(cbCourses);
        add(new JLabel("Fees Due:")); txtFees = new JTextField("0.00"); add(txtFees);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(a -> save());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(a -> dispose());
        add(btnSave); add(btnCancel);

        loadCourses();

        if (s != null) {
            txtName.setText(s.getName());
            txtEmail.setText(s.getEmail());
            txtPassword.setText(s.getPassword());
            txtFees.setText(String.valueOf(s.getFeesDue()));
            if (s.getCourseId() != null) {
                for (int i=0;i<courseIds.length;i++){
                    if (courseIds[i] == s.getCourseId()){
                        cbCourses.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    private void loadCourses() {
        java.util.List<Course> courses = CourseDAO.getAll();
        courseIds = new Integer[courses.size()+1];
        cbCourses.addItem(" -- none -- ");
        courseIds[0] = null;
        for (int i=0;i<courses.size();i++){
            Course c = courses.get(i);
            cbCourses.addItem(c.getName() + " (ID:"+c.getId()+", Fee:"+c.getFee()+")");
            courseIds[i+1] = c.getId();
        }
    }

    private void save() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String pwd = txtPassword.getText();
        double fees = 0.0;
        try { fees = Double.parseDouble(txtFees.getText().trim()); } catch(Exception ex){}
        Integer courseId = courseIds[cbCourses.getSelectedIndex()];

        if (name.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill required fields");
            return;
        }

        if (editing == null) {
            Student s = new Student();
            s.setName(name); s.setEmail(email); s.setPassword(pwd);
            s.setCourseId(courseId); s.setFeesDue(fees);
            boolean ok = StudentDAO.addStudent(s);
            if (ok) { JOptionPane.showMessageDialog(this,"Added"); saved = true; dispose(); }
            else JOptionPane.showMessageDialog(this, "Add failed (maybe duplicate email)");
        } else {
            editing.setName(name); editing.setEmail(email); editing.setPassword(pwd);
            editing.setCourseId(courseId); editing.setFeesDue(fees);
            boolean ok = StudentDAO.updateStudent(editing);
            if (ok) { JOptionPane.showMessageDialog(this,"Updated"); saved = true; dispose(); }
            else JOptionPane.showMessageDialog(this,"Update failed");
        }
    }

    public boolean isSaved(){ return saved; }
}
