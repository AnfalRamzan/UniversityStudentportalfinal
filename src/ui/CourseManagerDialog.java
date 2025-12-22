package ui;

import dao.CourseDAO;
import models.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseManagerDialog extends JDialog {
    private DefaultTableModel model;
    public CourseManagerDialog(Frame owner) {
        super(owner, true);
        setTitle("Course Manager");
        setSize(600,400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID","Name","Fee"},0) {
            @Override public boolean isCellEditable(int r,int c){return false;}
        };

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JTextField txtName = new JTextField(15);
        JTextField txtFee = new JTextField(8);
        JButton btnAdd = new JButton("Add Course");
        btnAdd.addActionListener(a -> {
            String name = txtName.getText().trim();
            double fee = 0;
            try { fee = Double.parseDouble(txtFee.getText().trim()); } catch(Exception ex){}
            if (name.isEmpty()) { JOptionPane.showMessageDialog(this,"Enter name"); return; }
            Course c = new Course();
            c.setName(name); c.setFee(fee);
            if (CourseDAO.addCourse(c)) { JOptionPane.showMessageDialog(this,"Added"); loadCourses(); }
            else JOptionPane.showMessageDialog(this,"Failed");
        });

        bottom.add(new JLabel("Name:")); bottom.add(txtName);
        bottom.add(new JLabel("Fee:")); bottom.add(txtFee);
        bottom.add(btnAdd);
        add(bottom, BorderLayout.SOUTH);

        loadCourses();
    }

    private void loadCourses() {
        model.setRowCount(0);
        List<Course> list = CourseDAO.getAll();
        for (Course c : list) model.addRow(new Object[]{c.getId(), c.getName(), c.getFee()});
    }
}
