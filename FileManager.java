package com.university.system;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String COURSES_FILE     = "courses.csv";
    private static final String USERS_FILE       = "users.csv";
    private static final String ENROLLMENTS_FILE = "enrollments.csv";

// ──────────────────────────────────────────────
//  COURSES
// ──────────────────────────────────────────────

    public static void saveCourses(List<Course> courses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COURSES_FILE))) {
            for (Course c : courses) {
                writer.write(c.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File(COURSES_FILE);
        if (!file.exists()) return courses;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    courses.add(Course.fromCSV(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
        return courses;
    }

// ──────────────────────────────────────────────
//  USERS (students only; admin is hardcoded)
// ──────────────────────────────────────────────

    public static void saveUsers(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Student s : students) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return students;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(":");
                    // format: STUDENT:username:password
                    if (parts.length == 3 && parts[0].equals("STUDENT")) {
                        students.add(new Student(parts[1], parts[2]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return students;
    }

// ──────────────────────────────────────────────
//  ENROLLMENTS
// ──────────────────────────────────────────────

    public static void saveEnrollments(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENROLLMENTS_FILE))) {
            for (Student s : students) {
                for (String course : s.getEnrolledCourses()) {
                    writer.write(s.getUsername() + "," + course);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving enrollments: " + e.getMessage());
        }
    }

    public static void loadEnrollments(List<Student> students) {
        File file = new File(ENROLLMENTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        String username = parts[0];
                        String course   = parts[1];
                        for (Student s : students) {
                            if (s.getUsername().equals(username)) {
                                s.getEnrolledCourses().add(course);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading enrollments: " + e.getMessage());
        }
    }
}