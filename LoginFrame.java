package com.university.system;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private SystemManager manager;
    private Admin admin = new Admin("admin", "admin123");

    public LoginFrame(SystemManager manager) {
        this.manager = manager;
        setTitle("University Course Management System");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        getContentPane().setBackground(new Color(20, 50, 100));
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("University Course Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Center panel
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(20, 50, 100));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        center.add(userLabel, gbc);

        JTextField userField = new JTextField(18);
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 0;
        center.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        center.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(18);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        center.add(passField, gbc);

        JLabel roleLabel = new JLabel("Login as:");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        center.add(roleLabel, gbc);

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Admin", "Student"});
        roleBox.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        center.add(roleBox, gbc);

        add(center, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(20, 50, 100));

        JButton loginBtn = createButton("Login", new Color(46, 117, 182));
        JButton registerBtn = createButton("Register as Student", new Color(70, 130, 70));

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String role = (String) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (role.equals("Admin")) {
                if (admin.login(username, password)) {
                    dispose();
                    new AdminFrame(manager);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Student student = manager.loginStudent(username, password);
                if (student != null) {
                    dispose();
                    new StudentFrame(manager, student);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Please register first.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame(manager);
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 38));
        return btn;
    }

}