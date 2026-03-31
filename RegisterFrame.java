package com.university.system;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private SystemManager manager;

    public RegisterFrame(SystemManager manager) {
        this.manager = manager;
        setTitle("Student Registration");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        getContentPane().setBackground(new Color(20, 80, 50));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Student Registration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(20, 80, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Username:", "Password:", "Confirm Password:"};
        JTextField[] fields = {new JTextField(18), new JPasswordField(18), new JPasswordField(18)};

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 0; gbc.gridy = i;
            center.add(lbl, gbc);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 1; gbc.gridy = i;
            center.add(fields[i], gbc);
        }

        add(center, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btnPanel.setBackground(new Color(20, 80, 50));

        JButton registerBtn = createButton("Register", new Color(46, 117, 182));
        JButton backBtn = createButton("Back to Login", new Color(120, 60, 60));

        registerBtn.addActionListener(e -> {
            String username = fields[0].getText().trim();
            String password = new String(((JPasswordField) fields[1]).getPassword()).trim();
            String confirm  = new String(((JPasswordField) fields[2]).getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean success = manager.registerStudent(username, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame(manager);
            }
        });

        backBtn.addActionListener(e -> { dispose(); new LoginFrame(manager); });

        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        return btn;
    }

}