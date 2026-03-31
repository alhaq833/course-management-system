package com.university.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {

    private SystemManager manager;
    private DefaultTableModel tableModel;
    private JTable courseTable;

    public AdminFrame(SystemManager manager) {
        this.manager = manager;
        setTitle("Admin Dashboard - University Course Management");
        setSize(850, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        getContentPane().setBackground(new Color(240, 245, 255));
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 50, 100));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        JButton logoutBtn = createButton("Logout", new Color(180, 50, 50));
        logoutBtn.addActionListener(e -> { dispose(); new LoginFrame(manager); });
        header.add(title, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("Manage Courses", buildCoursesTab());
        tabs.addTab("View Students", buildStudentsTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildCoursesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 245, 255));

        // Table
        String[] columns = {"Course Code", "Course Name", "Lecturer", "Credit Hours"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        courseTable = new JTable(tableModel);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 13));
        courseTable.setRowHeight(28);
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        courseTable.getTableHeader().setBackground(new Color(20, 50, 100));
        courseTable.getTableHeader().setForeground(Color.WHITE);
        courseTable.setSelectionBackground(new Color(180, 210, 255));
        refreshTable();

        JScrollPane scroll = new JScrollPane(courseTable);
        panel.add(scroll, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(240, 245, 255));
        form.setBorder(BorderFactory.createTitledBorder("Add / Update Course"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField codeField    = new JTextField(12);
        JTextField nameField    = new JTextField(12);
        JTextField lecField     = new JTextField(12);
        JTextField credField    = new JTextField(12);

        String[] lbls = {"Code:", "Name:", "Lecturer:", "Credits:"};
        JTextField[] flds = {codeField, nameField, lecField, credField};
        for (int i = 0; i < lbls.length; i++) {
            gbc.gridx = i * 2; gbc.gridy = 0;
            form.add(new JLabel(lbls[i]), gbc);
            gbc.gridx = i * 2 + 1; gbc.gridy = 0;
            flds[i].setFont(new Font("Arial", Font.PLAIN, 13));
            form.add(flds[i], gbc);
        }

        // Auto-fill form on table row select
        courseTable.getSelectionModel().addListSelectionListener(ev -> {
            int row = courseTable.getSelectedRow();
            if (row >= 0) {
                codeField.setText((String) tableModel.getValueAt(row, 0));
                nameField.setText((String) tableModel.getValueAt(row, 1));
                lecField.setText((String) tableModel.getValueAt(row, 2));
                credField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        btnRow.setBackground(new Color(240, 245, 255));

        JButton addBtn    = createButton("Add Course",    new Color(46, 117, 182));
        JButton updateBtn = createButton("Update Course", new Color(100, 130, 60));
        JButton deleteBtn = createButton("Delete Course", new Color(180, 50, 50));
        JButton clearBtn  = createButton("Clear",         new Color(120, 120, 120));

        addBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String lec  = lecField.getText().trim();
            String cred = credField.getText().trim();
            if (code.isEmpty() || name.isEmpty() || lec.isEmpty() || cred.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            try {
                int credits = Integer.parseInt(cred);
                if (credits <= 0) throw new NumberFormatException();
                manager.addCourse(new Course(code, name, lec, credits));
                refreshTable();
                clearFields(codeField, nameField, lecField, credField);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credit hours must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String lec  = lecField.getText().trim();
            String cred = credField.getText().trim();
            if (code.isEmpty() || name.isEmpty() || lec.isEmpty() || cred.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a course from the table first.", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            try {
                int credits = Integer.parseInt(cred);
                manager.updateCourse(code, name, lec, credits);
                refreshTable();
                clearFields(codeField, nameField, lecField, credField);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credit hours must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a course from the table first.", "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete course " + code + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteCourse(code);
                refreshTable();
                clearFields(codeField, nameField, lecField, credField);
            }
        });

        clearBtn.addActionListener(e -> clearFields(codeField, nameField, lecField, credField));

        btnRow.add(addBtn); btnRow.add(updateBtn); btnRow.add(deleteBtn); btnRow.add(clearBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(new Color(240, 245, 255));
        south.add(form, BorderLayout.CENTER);
        south.add(btnRow, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 245, 255));

        JTextArea area = new JTextArea();
        area.setFont(new Font("Courier New", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(new Color(245, 250, 255));

        JButton refreshBtn = createButton("Refresh", new Color(46, 117, 182));
        refreshBtn.addActionListener(e -> loadStudents(area));
        loadStudents(area);

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(new Color(240, 245, 255));
        btnRow.add(refreshBtn);
        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    private void loadStudents(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        List<Student> students = manager.getStudents();
        if (students.isEmpty()) {
            sb.append("No students registered yet.");
        } else {
            for (Student s : students) {
                sb.append("Student: ").append(s.getUsername()).append("\n");
                List<String> enrolled = s.getEnrolledCourses();
                if (enrolled.isEmpty()) {
                    sb.append("  Enrolled Courses: None\n");
                } else {
                    sb.append("  Enrolled Courses:\n");
                    for (String c : enrolled) sb.append("    - ").append(c).append("\n");
                }
                sb.append("\n");
            }
        }
        area.setText(sb.toString());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Course c : manager.getCourses()) {
            tableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getLecturer(), c.getCreditHours()});
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
        courseTable.clearSelection();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 34));
        return btn;
    }

}