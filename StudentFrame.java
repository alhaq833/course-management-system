package com.university.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentFrame extends JFrame {

    private SystemManager manager;
    private Student student;
    private DefaultTableModel availableModel;
    private DefaultTableModel enrolledModel;

    public StudentFrame(SystemManager manager, Student student) {
        this.manager = manager;
        this.student = student;
        setTitle("Student Dashboard - " + student.getUsername());
        setSize(820, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        getContentPane().setBackground(new Color(240, 250, 245));
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 90, 50));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("Welcome, " + student.getUsername() + "!");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        JButton logoutBtn = createButton("Logout", new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> {
            manager.saveEnrollments();
            dispose();
            new LoginFrame(manager);
        });
        header.add(title, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Split pane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(430);
        split.setResizeWeight(0.5);
        split.setLeftComponent(buildAvailablePanel());
        split.setRightComponent(buildEnrolledPanel());
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildAvailablePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Available Courses"));
        panel.setBackground(new Color(240, 250, 245));

        String[] cols = {"Code", "Name", "Lecturer", "Credits"};
        availableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(availableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(20, 90, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(180, 230, 200));
        refreshAvailable();

        JButton enrollBtn = createButton("Enroll in Selected", new Color(20, 90, 50));
        enrollBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a course to enroll.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String courseName = (String) availableModel.getValueAt(row, 1);
            student.enrollCourse(courseName);
            manager.saveEnrollments();
            refreshEnrolled();
            JOptionPane.showMessageDialog(this, "Enrolled in \"" + courseName + "\" successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.setBackground(new Color(240, 250, 245));
        btnRow.add(enrollBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildEnrolledPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("My Enrolled Courses"));
        panel.setBackground(new Color(240, 250, 245));

        String[] cols = {"Course Name"};
        enrolledModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(enrolledModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(20, 90, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(180, 230, 200));
        refreshEnrolled();

        JLabel countLabel = new JLabel("", SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        countLabel.setForeground(new Color(60, 100, 60));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(countLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAvailable() {
        availableModel.setRowCount(0);
        for (Course c : manager.getCourses()) {
            availableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getLecturer(), c.getCreditHours()});
        }
    }

    private void refreshEnrolled() {
        enrolledModel.setRowCount(0);
        for (String c : student.getEnrolledCourses()) {
            enrolledModel.addRow(new Object[]{c});
        }
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 34));
        return btn;
    }

}