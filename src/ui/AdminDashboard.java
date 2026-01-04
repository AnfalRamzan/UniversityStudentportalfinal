package ui;

import dao.*;
import db.DBConnection;
import models.*;
import java.sql.*;   
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
        getContentPane().setBackground(new Color(245, 247, 250)); // Light professional background

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(230, 233, 240));

        JLabel lblTitle = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));

        // Logo
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/riphah.png"));
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(img));
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            topPanel.add(logo, BorderLayout.WEST);
        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }

        topPanel.add(lblTitle, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ================= TABS =================
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(new Color(245, 247, 250));
        tabs.setFont(new Font("Arial", Font.PLAIN, 14));

        // ================= STUDENTS TAB =================
        studentModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Course", "Fees Due"}, 0);
        studentTable = new JTable(studentModel);
        studentTable.setFillsViewportHeight(true);
        studentTable.setBackground(Color.WHITE);

        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(new Color(245, 247, 250));
        studentPanel.add(createLeftActionPanel(), BorderLayout.WEST);
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        tabs.add("Students", studentPanel);

        // ================= OVERVIEW TAB =================
        overviewModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Course", "Fees", "Date", "Status"}, 0);
        overviewTable = new JTable(overviewModel);
        overviewTable.setFillsViewportHeight(true);
        overviewTable.setBackground(Color.WHITE);

        JButton btnRefreshOverview = new JButton("Refresh Overview");
        btnRefreshOverview.setFocusPainted(false);
        btnRefreshOverview.addActionListener(e -> loadStudentsOverview());

        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBackground(new Color(245, 247, 250));
        overviewPanel.add(new JScrollPane(overviewTable), BorderLayout.CENTER);
        overviewPanel.add(btnRefreshOverview, BorderLayout.SOUTH);
        tabs.add("Students Overview", overviewPanel);

        add(tabs, BorderLayout.CENTER);

        // ================= LOGOUT BUTTON AT BOTTOM-RIGHT =================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        bottomPanel.setBackground(new Color(245, 247, 250));

        JButton btnLogoutBottom = new JButton("Logout");
        btnLogoutBottom.setBackground(new Color(200, 55, 55)); // Red
        btnLogoutBottom.setForeground(Color.WHITE);
        btnLogoutBottom.setFocusPainted(false);
        btnLogoutBottom.setPreferredSize(new Dimension(100, 35)); // smaller size
        btnLogoutBottom.addActionListener(e -> logout());

        bottomPanel.add(btnLogoutBottom);
        add(bottomPanel, BorderLayout.SOUTH);

        // ================= LOAD DATA =================
        loadStudents();
        loadStudentsOverview();
    }

    // ================= LEFT PANEL FOR STUDENTS =================
    private JPanel createLeftActionPanel() {
        JPanel left = new JPanel(new GridLayout(7,1,10,10));
        left.setBackground(new Color(230, 233, 240));
        left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton btnAdd = new JButton("Add Student");
        JButton btnEdit = new JButton("Edit Student");
        JButton btnDelete = new JButton("Delete Student");
        JButton btnAttendance = new JButton("Mark Attendance");
        JButton btnGrade = new JButton("Assign Grade");
        JButton btnEnroll = new JButton("Enroll Student");
        JButton btnRefresh = new JButton("Refresh");

        JButton[] buttons = {btnAdd, btnEdit, btnDelete, btnAttendance, btnGrade, btnEnroll, btnRefresh};
        for (JButton btn : buttons) {
            btn.setFocusPainted(false); // Keep default OS button color
        }

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> { if (studentTable.getSelectedRow() < 0) { showWarning(); return; } editStudent(); });
        btnDelete.addActionListener(e -> { if (studentTable.getSelectedRow() < 0) { showWarning(); return; } deleteStudent(); });
        btnAttendance.addActionListener(e -> { if (studentTable.getSelectedRow() < 0) { showWarning(); return; } markAttendance(); });
        btnGrade.addActionListener(e -> { if (studentTable.getSelectedRow() < 0) { showWarning(); return; } assignGrade(); });
        btnEnroll.addActionListener(e -> { if (studentTable.getSelectedRow() < 0) { showWarning(); return; } enrollStudent(); });
        btnRefresh.addActionListener(e -> loadStudents());

        left.add(btnAdd);
        left.add(btnEdit);
        left.add(btnDelete);
        left.add(btnAttendance);
        left.add(btnGrade);
        left.add(btnEnroll);
        left.add(btnRefresh);

        return left;
    }

    private void showWarning() {
        JOptionPane.showMessageDialog(this,"Please select a student first!","Warning",JOptionPane.WARNING_MESSAGE);
    }

    // ================= STUDENTS =================
    private void loadStudents() {
        studentModel.setRowCount(0);
        for (Student s : StudentDAO.getAll()) {
            Course c = CourseDAO.getById(s.getCourseId());
            studentModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getEmail(),
                    c != null ? c.getName() : "-",
                    s.getFeesDue()
            });
        }
    }

    private void addStudent() {
    JTextField name = new JTextField();
    JTextField email = new JTextField();
    JTextField fee = new JTextField();
    JPasswordField pass = new JPasswordField();

    JComboBox<String> cbCourse = new JComboBox<>();
    for (Course c : CourseDAO.getAllCourses())
        cbCourse.addItem(c.getId() + " - " + c.getName());

    JPanel p = new JPanel(new GridLayout(0,2,5,5));
    p.add(new JLabel("Name")); p.add(name);
    p.add(new JLabel("Email")); p.add(email);
    p.add(new JLabel("Course")); p.add(cbCourse);
    p.add(new JLabel("Fees")); p.add(fee);
    p.add(new JLabel("Password")); p.add(pass);

    int result = JOptionPane.showConfirmDialog(this, p, "Add Student", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        // ===== VALIDATE FEE =====
        double feeValue;
        try {
            feeValue = Double.parseDouble(fee.getText().trim());
            if (feeValue < 0) throw new NumberFormatException("Negative value");
            // Round to 2 decimals
            feeValue = Math.round(feeValue * 100.0) / 100.0;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric value for Fees (e.g., 5000.00)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ===== CREATE STUDENT OBJECT =====
        Student s = new Student();
        s.setName(name.getText().trim());
        s.setEmail(email.getText().trim());
        s.setPassword(new String(pass.getPassword()));
        s.setCourseId(Integer.parseInt(cbCourse.getSelectedItem().toString().split(" - ")[0]));
        s.setFeesDue(feeValue);

        // ===== ADD STUDENT TO DATABASE =====
        boolean success = StudentDAO.addStudent(s); // Make sure your DAO uses table `student`
        if (success) {
            loadStudents();
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


    private void editStudent() {
        int r = studentTable.getSelectedRow();
        int id = (int) studentModel.getValueAt(r,0);
        Student s = StudentDAO.getById(id);

        JTextField name = new JTextField(s.getName());
        JTextField email = new JTextField(s.getEmail());
        JTextField fee = new JTextField(String.valueOf(s.getFeesDue()));

        JPanel p = new JPanel(new GridLayout(0,2));
        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Email")); p.add(email);
        p.add(new JLabel("Fees")); p.add(fee);

        if (JOptionPane.showConfirmDialog(this,p,"Edit Student",
                JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {

            s.setName(name.getText());
            s.setEmail(email.getText());
            s.setFeesDue(Double.parseDouble(fee.getText()));
            StudentDAO.updateStudent(s);
            loadStudents();
            JOptionPane.showMessageDialog(this,"Student edited successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
        }
    }

   private void deleteStudent() {
        int r = studentTable.getSelectedRow();
        if (r < 0) { showWarning(); return; }

        int id = (int) studentModel.getValueAt(r,0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.execute("SET FOREIGN_KEY_CHECKS=0"); // Disable foreign key temporarily
            st.executeUpdate("DELETE FROM student WHERE id=" + id);
            st.execute("SET FOREIGN_KEY_CHECKS=1"); // Re-enable foreign key

            JOptionPane.showMessageDialog(this,"Student deleted successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
            loadStudents();
            loadStudentsOverview();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Error deleting student: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }





    // ================= ATTENDANCE =================
    private void markAttendance() {
        int r = studentTable.getSelectedRow();
        int studentId = (int) studentModel.getValueAt(r,0);

        String[] options = {"Present","Absent"};
        String status = (String) JOptionPane.showInputDialog(
                this,"Status","Attendance",
                JOptionPane.QUESTION_MESSAGE,null,options,options[0]);

        Attendance a = new Attendance();
        a.setStudentId(studentId);
        a.setCourseId(StudentDAO.getById(studentId).getCourseId());
        a.setDate(new Date(System.currentTimeMillis()));
        a.setStatus(status);

        AttendanceDAO.addAttendance(a);
        loadStudentsOverview();
        JOptionPane.showMessageDialog(this,"Attendance marked successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= GRADES =================
    private void assignGrade() {
        int r = studentTable.getSelectedRow();
        int studentId = (int) studentModel.getValueAt(r,0);
        String grade = JOptionPane.showInputDialog("Enter Grade");

        Grade g = new Grade();
        g.setStudentId(studentId);
        g.setCourseId(StudentDAO.getById(studentId).getCourseId());
        g.setGrade(grade);

        GradesDAO.addGrade(g);
        JOptionPane.showMessageDialog(this,"Grade assigned successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= OVERVIEW =================
    private void loadStudentsOverview() {
        overviewModel.setRowCount(0);

        for (Student s : StudentDAO.getAll()) {
            List<Attendance> list = AttendanceDAO.getByStudent(s.getId());
            if (list.isEmpty()) {
                overviewModel.addRow(new Object[]{
                        s.getId(), s.getName(), s.getEmail(),
                        "-", s.getFeesDue(), "-", "-"
                });
            } else {
                for (Attendance a : list) {
                    overviewModel.addRow(new Object[]{
                            s.getId(), s.getName(), s.getEmail(),
                            CourseDAO.getById(a.getCourseId()).getName(),
                            s.getFeesDue(), a.getDate(), a.getStatus()
                    });
                }
            }
        }
    }

    // ================= ENROLLMENT =================
    private void enrollStudent() {
        int r = studentTable.getSelectedRow();
        int studentId = (int) studentModel.getValueAt(r,0);

        JComboBox<String> cbCourse = new JComboBox<>();
        for (Course c : CourseDAO.getAllCourses())
            cbCourse.addItem(c.getId() + " - " + c.getName());

        JPanel p = new JPanel(new GridLayout(0,2));
        p.add(new JLabel("Course")); p.add(cbCourse);

        if (JOptionPane.showConfirmDialog(this,p,"Enroll Student",
                JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {

            int cid = Integer.parseInt(cbCourse.getSelectedItem().toString().split(" - ")[0]);
            EnrollmentDAO.enroll(studentId, cid);
            JOptionPane.showMessageDialog(this,"Student enrolled successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ================= LOGOUT =================
    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
 