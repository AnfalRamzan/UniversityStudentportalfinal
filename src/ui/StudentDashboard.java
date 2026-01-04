package ui;

import dao.AssignmentDAO;
import dao.AttendanceDAO;
import dao.GradesDAO;
import dao.StudentDAO;
import models.Assignment;
import models.Attendance;
import models.Grade;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.util.List;

public class StudentDashboard extends JFrame {

    private Student student;

    private JTable tblMain;
    private DefaultTableModel mainModel;

    public StudentDashboard(Student student) {
        this.student = student;

        setTitle("Student Dashboard - " + student.getName());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initTopPanel();
        initLeftPanel();
        initBottomPanel();

        mainModel = new DefaultTableModel();
        tblMain = new JTable(mainModel);
        styleTable(tblMain);
        JScrollPane scrollPane = new JScrollPane(tblMain);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ---------------- TOP PANEL ----------------
    private void initTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // ✅ LOGO
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/riphah.png"));
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(img));
            logo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            topPanel.add(logo, BorderLayout.WEST);
        } catch (Exception e) {
            System.out.println("Logo not found: " + e.getMessage());
        }

        JLabel lblTitle = new JLabel("STUDENT DASHBOARD", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(lblTitle, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
    }

    // ---------------- LEFT PANEL ----------------
    private void initLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(5, 1, 10, 15));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnProfile = createBtn("Profile");
        JButton btnAttendance = createBtn("Attendance");
        JButton btnGrades = createBtn("Grades");
        JButton btnAssignments = createBtn("Assignments");
        JButton btnUpload = createBtn("Upload Assignment");

        leftPanel.add(btnProfile);
        leftPanel.add(btnAttendance);
        leftPanel.add(btnGrades);
        leftPanel.add(btnAssignments);
        leftPanel.add(btnUpload);

        add(leftPanel, BorderLayout.WEST);

        btnProfile.addActionListener(e -> showProfile());
        btnAttendance.addActionListener(e -> loadAttendance());
        btnGrades.addActionListener(e -> loadGrades());
        btnAssignments.addActionListener(e -> loadAssignments());
        btnUpload.addActionListener(e -> uploadAssignment());
    }

    // ---------------- BOTTOM PANEL (LOGOUT) ----------------
    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(Color.RED);
        btnLogout.addActionListener(e -> logout());

        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.BLACK);
        return btn;
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // ---------------- DATA METHODS ----------------
    private void loadAttendance() {
        mainModel.setRowCount(0);
        mainModel.setColumnIdentifiers(new String[]{"Course ID", "Date", "Status"});
        List<Attendance> list = AttendanceDAO.getAttendanceByStudent(student.getId());
        if (list != null) {
            for (Attendance a : list) {
                mainModel.addRow(new Object[]{a.getCourseId(), a.getDate(), a.getStatus()});
            }
        }
    }

    private void loadGrades() {
        mainModel.setRowCount(0);
        mainModel.setColumnIdentifiers(new String[]{"Course ID", "Grade"});
        List<Grade> list = GradesDAO.getGradesByStudent(student.getId());
        if (list != null) {
            for (Grade g : list) {
                mainModel.addRow(new Object[]{g.getCourseId(), g.getGrade()});
            }
        }
    }

    private void loadAssignments() {
        mainModel.setRowCount(0);
        mainModel.setColumnIdentifiers(new String[]{"Course ID", "Title", "Submission Date", "Status", "File Path"});
        List<Assignment> list = AssignmentDAO.getAssignmentsByStudent(student.getId());
        if (list != null) {
            for (Assignment a : list) {
                mainModel.addRow(new Object[]{
                        a.getCourseId(),
                        a.getTitle(),
                        a.getSubmissionDate(),
                        a.getStatus(),
                        a.getFilePath()
                });
            }
        }
    }

    private void showProfile() {
        mainModel.setRowCount(0);
        mainModel.setColumnIdentifiers(new String[]{"Field", "Value"});
        mainModel.addRow(new Object[]{"Name", student.getName()});
        mainModel.addRow(new Object[]{"Email", student.getEmail()});
        mainModel.addRow(new Object[]{"Course ID", student.getCourseId()});
        mainModel.addRow(new Object[]{"Fees Paid", student.getFeesPaid()});
        mainModel.addRow(new Object[]{"Fees Due", student.getFeesDue()});

        List<Grade> list = GradesDAO.getGradesByStudent(student.getId());
        double totalPoints = 0;
        if (list != null && !list.isEmpty()) {
            for (Grade g : list)
                totalPoints += GradesDAO.gradeToPoint(g.getGrade());
            double gpa = totalPoints / list.size();
            mainModel.addRow(new Object[]{"GPA", String.format("%.2f", gpa)});
            mainModel.addRow(new Object[]{"CGPA", String.format("%.2f", gpa)});
        } else {
            mainModel.addRow(new Object[]{"GPA", "0.0"});
            mainModel.addRow(new Object[]{"CGPA", "0.0"});
        }
    }

    // ---------------- ASSIGNMENT UPLOAD ----------------
    private void uploadAssignment() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String title = JOptionPane.showInputDialog(this, "Enter Assignment Title:");
            if (title != null && !title.trim().isEmpty()) {
                Assignment a = new Assignment();
                a.setStudentId(student.getId());
                a.setCourseId(student.getCourseId());
                a.setTitle(title);
                a.setFilePath(file.getAbsolutePath());
                a.setSubmissionDate(new Date(System.currentTimeMillis()));
                a.setStatus("Pending");

                if (AssignmentDAO.saveAssignment(a)) {
                    JOptionPane.showMessageDialog(this, "Assignment uploaded successfully!");
                    loadAssignments();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to upload assignment.");
                }
            }
        }
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
