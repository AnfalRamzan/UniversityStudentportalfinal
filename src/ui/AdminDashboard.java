package ui;

import dao.StudentDAO;
import dao.CourseDAO;
import dao.AttendanceDAO;
import dao.GradesDAO;
import models.Student;
import models.Course;
import models.Attendance;
import models.Grade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JTable studentTable, overviewTable;
    private DefaultTableModel studentModel, overviewModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------------- TOP PANEL ----------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // Centered title
        JLabel lblTitle = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logout button on top-right
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.addActionListener(e -> logout());

        // Title panel to ensure center alignment
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(btnLogout, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- TABS ----------------
        JTabbedPane tabs = new JTabbedPane();

        // ================= STUDENT MANAGEMENT TAB =================
        studentModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Course", "Fees Due"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = new JTable(studentModel);

        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(Color.WHITE);

        studentPanel.add(createLeftActionPanel(), BorderLayout.WEST);
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        tabs.add("Students", studentPanel);

        // ================= STUDENTS OVERVIEW TAB =================
        overviewModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Course", "Fees Due", "Attendance Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        overviewTable = new JTable(overviewModel);

        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(Color.WHITE);

        JButton btnRefreshOverview = new JButton("Refresh Overview");
        btnRefreshOverview.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefreshOverview.setForeground(Color.BLACK);
        btnRefreshOverview.addActionListener(e -> loadStudentsOverview());

        JPanel overviewBtnPanel = new JPanel();
        overviewBtnPanel.setBackground(Color.WHITE);
        overviewBtnPanel.add(btnRefreshOverview);

        overviewPanel.add(new JScrollPane(overviewTable), BorderLayout.CENTER);
        overviewPanel.add(overviewBtnPanel, BorderLayout.SOUTH);

        tabs.add("Students Overview", overviewPanel);

        add(tabs, BorderLayout.CENTER);

        // Load initial data
        loadStudents();
        loadStudentsOverview();
    }

    // ---------------- LEFT BUTTON PANEL ----------------
    private JPanel createLeftActionPanel() {
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(6, 1, 10, 15));
        left.setBackground(Color.WHITE);
        left.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnAdd = createBtn("Add Student");
        JButton btnEdit = createBtn("Edit Student");
        JButton btnDelete = createBtn("Delete Student");
        JButton btnAttendance = createBtn("Mark Attendance");
        JButton btnGrade = createBtn("Assign Grade");
        JButton btnRefresh = createBtn("Refresh");

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnAttendance.addActionListener(e -> markAttendance());
        btnGrade.addActionListener(e -> assignGrade());
        btnRefresh.addActionListener(e -> loadStudents());

        left.add(btnAdd);
        left.add(btnEdit);
        left.add(btnDelete);
        left.add(btnAttendance);
        left.add(btnGrade);
        left.add(btnRefresh);

        return left;
    }

    private JButton createBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.BLACK);
        return btn;
    }

    // ---------------- LOGOUT ----------------
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }

    // ---------------- STUDENT CRUD ----------------
    private void loadStudents() {
        studentModel.setRowCount(0);
        List<Student> list = StudentDAO.getAll();

        for (Student s : list) {
            String courseName = "-";
            if (s.getCourseId() != null) {
                Course c = CourseDAO.getById(s.getCourseId());
                if (c != null) courseName = c.getName();
            }
            studentModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getEmail(),
                    courseName,
                    s.getFeesDue()
            });
        }
    }

    private void addStudent() {
        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtFee = new JTextField();
        JPasswordField txtPassword = new JPasswordField();

        List<Course> courses = CourseDAO.getAllCourses();
        JComboBox<String> comboCourse = new JComboBox<>();
        for (Course c : courses) comboCourse.addItem(c.getId() + " - " + c.getName());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Name:")); panel.add(txtName);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Course:")); panel.add(comboCourse);
        panel.add(new JLabel("Fees Due:")); panel.add(txtFee);
        panel.add(new JLabel("Password:")); panel.add(txtPassword);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Student", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Student s = new Student();
                s.setName(txtName.getText().trim());
                s.setEmail(txtEmail.getText().trim());
                s.setPassword(new String(txtPassword.getPassword()));
                s.setCourseId(Integer.parseInt(comboCourse.getSelectedItem().toString().split(" - ")[0]));
                s.setFeesDue(Double.parseDouble(txtFee.getText()));

                if (StudentDAO.addStudent(s)) {
                    JOptionPane.showMessageDialog(this, "Student Added Successfully!");
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding student.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        }
    }

    private void editStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first!"); return; }

        int studentId = (int) studentModel.getValueAt(row, 0);
        Student s = StudentDAO.getById(studentId);
        if (s == null) return;

        JTextField txtName = new JTextField(s.getName());
        JTextField txtEmail = new JTextField(s.getEmail());
        JTextField txtFee = new JTextField(String.valueOf(s.getFeesDue()));

        List<Course> courses = CourseDAO.getAllCourses();
        JComboBox<String> comboCourse = new JComboBox<>();
        for (Course c : courses) {
            comboCourse.addItem(c.getId() + " - " + c.getName());
            if (c.getId() == s.getCourseId()) comboCourse.setSelectedItem(c.getId() + " - " + c.getName());
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Name:")); panel.add(txtName);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Course:")); panel.add(comboCourse);
        panel.add(new JLabel("Fees Due:")); panel.add(txtFee);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Edit Student", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                s.setName(txtName.getText().trim());
                s.setEmail(txtEmail.getText().trim());
                s.setCourseId(Integer.parseInt(comboCourse.getSelectedItem().toString().split(" - ")[0]));
                s.setFeesDue(Double.parseDouble(txtFee.getText()));

                if (StudentDAO.updateStudent(s)) {
                    JOptionPane.showMessageDialog(this, "Student Updated!");
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update student.");
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid input!"); }
        }
    }

    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student first!"); return; }

        int studentId = (int) studentModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (StudentDAO.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Deleted successfully.");
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete.");
            }
        }
    }

    // ---------------- ATTENDANCE ----------------
    private void markAttendance() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student!"); return; }
        int studentId = (int) studentModel.getValueAt(row, 0);

        List<Course> courses = CourseDAO.getAllCourses();
        String[] courseNames = courses.stream().map(Course::getName).toArray(String[]::new);

        String courseName = (String) JOptionPane.showInputDialog(this, "Select course:", "Attendance",
                JOptionPane.QUESTION_MESSAGE, null, courseNames, courseNames[0]);
        if (courseName == null) return;

        Course selectedCourse = courses.stream().filter(c -> c.getName().equals(courseName)).findFirst().orElse(null);
        if (selectedCourse == null) return;

        String[] statusOptions = {"Present", "Absent"};
        String status = (String) JOptionPane.showInputDialog(this, "Select status:", "Attendance",
                JOptionPane.QUESTION_MESSAGE, null, statusOptions, statusOptions[0]);
        if (status == null) return;

        Attendance a = new Attendance();
        a.setStudentId(studentId);
        a.setCourseId(selectedCourse.getId());
        a.setDate(new Date(System.currentTimeMillis()));
        a.setStatus(status);

        if (AttendanceDAO.addAttendance(a)) {
            JOptionPane.showMessageDialog(this, "Attendance recorded!");
            loadStudentsOverview();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to record attendance.");
        }
    }

    // ---------------- GRADES ----------------
    private void assignGrade() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a student!"); return; }
        int studentId = (int) studentModel.getValueAt(row, 0);

        List<Course> courses = CourseDAO.getAllCourses();
        String[] courseNames = courses.stream().map(Course::getName).toArray(String[]::new);

        String courseName = (String) JOptionPane.showInputDialog(this, "Select course:", "Assign Grade",
                JOptionPane.QUESTION_MESSAGE, null, courseNames, courseNames[0]);
        if (courseName == null) return;

        Course selectedCourse = courses.stream().filter(c -> c.getName().equals(courseName)).findFirst().orElse(null);
        if (selectedCourse == null) return;

        String gradeValue = JOptionPane.showInputDialog(this, "Enter Grade (A, B, C, etc.):");
        if (gradeValue == null || gradeValue.trim().isEmpty()) return;

        Grade grade = GradesDAO.getByStudentCourse(studentId, selectedCourse.getId());
        if (grade == null) grade = new Grade();

        grade.setStudentId(studentId);
        grade.setCourseId(selectedCourse.getId());
        grade.setGrade(gradeValue.trim());

        if (grade.getId() == 0) {
            if (GradesDAO.addGrade(grade)) JOptionPane.showMessageDialog(this, "Grade added successfully!");
            else JOptionPane.showMessageDialog(this, "Failed to add grade.");
        } else {
            if (GradesDAO.updateGrade(grade)) JOptionPane.showMessageDialog(this, "Grade updated successfully!");
            else JOptionPane.showMessageDialog(this, "Failed to update grade.");
        }
    }

    // ---------------- STUDENTS OVERVIEW ----------------
    private void loadStudentsOverview() {
        overviewModel.setRowCount(0);
        List<Student> students = StudentDAO.getAll();

        for (Student s : students) {
            List<Attendance> attendances = AttendanceDAO.getByStudent(s.getId());
            String courseName = "-";
            if (s.getCourseId() != null) {
                Course c = CourseDAO.getById(s.getCourseId());
                if (c != null) courseName = c.getName();
            }

            if (attendances.isEmpty()) {
                overviewModel.addRow(new Object[]{
                        s.getId(), s.getName(), s.getEmail(),
                        courseName, s.getFeesDue(), "-", "-"
                });
            } else {
                for (Attendance a : attendances) {
                    Course c = CourseDAO.getById(a.getCourseId());
                    overviewModel.addRow(new Object[]{
                            s.getId(), s.getName(), s.getEmail(),
                            c != null ? c.getName() : courseName,
                            s.getFeesDue(), a.getDate(), a.getStatus()
                    });
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
