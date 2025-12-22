package ui;

import dao.AttendanceDAO;
import dao.StudentDAO;
import models.Attendance;
import models.Student;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AttendanceManagerDialog extends JDialog {
    public AttendanceManagerDialog(Frame owner) {
        super(owner, true);
        setTitle("Attendance Manager");
        setSize(500,300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(5,2,8,8));

        JTextField txtStudentId = new JTextField();
        JComboBox<String> status = new JComboBox<>(new String[]{"P","A"});
        JButton btnMark = new JButton("Mark Attendance");

        btnMark.addActionListener(a->{
            try {
                int sid = Integer.parseInt(txtStudentId.getText().trim());
                Student st = StudentDAO.getById(sid);
                if (st==null){
                    JOptionPane.showMessageDialog(this,"Student not found");
                    return;
                }

                Attendance att = new Attendance();
                att.setStudentId(sid);
                att.setCourseId(st.getCourseId()); // assuming marking for current course
                att.setDate((java.sql.Date) new Date(System.currentTimeMillis()));
                att.setStatus((String)status.getSelectedItem());

                boolean ok = AttendanceDAO.markAttendance(att); // <-- use object
                if (ok) JOptionPane.showMessageDialog(this,"Attendance marked!");
                else JOptionPane.showMessageDialog(this,"Failed to mark attendance");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid student id");
            }
        });

        add(new JLabel("Student ID:")); add(txtStudentId);
        add(new JLabel("Status (P/A):")); add(status);
        add(new JLabel()); add(btnMark);
    }
}
