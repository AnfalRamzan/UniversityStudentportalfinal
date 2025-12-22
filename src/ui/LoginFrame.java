package ui;

import dao.AdminDAO;
import dao.StudentDAO;
import models.Student;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> roleBox;

    public LoginFrame() {
        setTitle("Student Portal - Login");
        setSize(420, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(360, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // ===== LOGO =====
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/riphah.png"));
        Image img = icon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(15));
        card.add(logo);

        // ===== TITLE =====
        JLabel title = new JLabel("STUDENT PORTAL");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(title);

        card.add(Box.createVerticalStrut(20));

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setMaximumSize(new Dimension(320, 150));
        form.setBackground(Color.WHITE);

        Font f = new Font("Arial", Font.BOLD, 14);

        JLabel l1 = new JLabel("Role:");
        l1.setFont(f);
        l1.setForeground(Color.BLACK);
        form.add(l1);

        roleBox = new JComboBox<>(new String[]{"Admin", "Student"});
        form.add(roleBox);

        JLabel l2 = new JLabel("Username / Email:");
        l2.setFont(f);
        l2.setForeground(Color.BLACK);
        form.add(l2);

        txtUser = new JTextField();
        form.add(txtUser);

        JLabel l3 = new JLabel("Password:");
        l3.setFont(f);
        l3.setForeground(Color.BLACK);
        form.add(l3);

        txtPass = new JPasswordField();
        form.add(txtPass);

        card.add(form);
        card.add(Box.createVerticalStrut(20));

        // ===== BUTTONS =====
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setMaximumSize(new Dimension(300, 45));
        btnPanel.setBackground(Color.WHITE);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false); // keeps default OS color
        btnLogin.addActionListener(e -> login());

        JButton btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));
        btnExit.setFocusPainted(false); // keeps default OS color
        btnExit.addActionListener(e -> System.exit(0));

        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);

        card.add(btnPanel);
        card.add(Box.createVerticalStrut(15));

        add(card);
    }

    private void login() {
        String role = (String) roleBox.getSelectedItem();
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if ("Admin".equals(role)) {
            if (AdminDAO.authenticate(user, pass)) {
                JOptionPane.showMessageDialog(this, "Admin login success");
                new AdminDashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials");
            }
        } else {
            Student s = StudentDAO.authenticate(user, pass);
            if (s != null) {
                JOptionPane.showMessageDialog(this, "Student login success");
                new StudentDashboard(s).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid student credentials");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
