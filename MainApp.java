package com.university.system;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
// use default look and feel
            }
            SystemManager manager = new SystemManager();
            new LoginFrame(manager);
        });
    }
}