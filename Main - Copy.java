package com.university.system;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static SystemManager manager = new SystemManager();
    static Admin admin = new Admin("admin", "admin123");

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  UNIVERSITY COURSE MANAGEMENT SYSTEM");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Login");
            System.out.println("3. Student Registration");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> adminLogin();
                case 2 -> studentLogin();
                case 3 -> studentRegistration();
                case 4 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

// ──────────────────────────────────────────────
//  ADMIN
// ──────────────────────────────────────────────

    static void adminLogin() {
        System.out.println("\n--- ADMIN LOGIN ---");
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if (admin.login(user, pass)) {
            System.out.println("Welcome, Admin!");
            adminMenu();
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Update Course");
            System.out.println("4. Delete Course");
            System.out.println("5. View All Students & Enrollments");
            System.out.println("6. Logout");
            System.out.print("Choose: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addCourse();
                case 2 -> manager.viewAllCourses();
                case 3 -> updateCourse();
                case 4 -> deleteCourse();
                case 5 -> manager.viewAllStudents();
                case 6 -> { System.out.println("Logged out."); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void addCourse() {
        System.out.println("\n--- ADD COURSE ---");
        try {
            System.out.print("Course Code: ");
            String code = scanner.nextLine().trim();
            if (code.isEmpty()) { System.out.println("Course code cannot be empty."); return; }

            System.out.print("Course Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) { System.out.println("Course name cannot be empty."); return; }

            System.out.print("Lecturer: ");
            String lecturer = scanner.nextLine().trim();
            if (lecturer.isEmpty()) { System.out.println("Lecturer cannot be empty."); return; }

            System.out.print("Credit Hours: ");
            int credits = readInt();
            if (credits <= 0) { System.out.println("Credit hours must be greater than 0."); return; }

            manager.addCourse(new Course(code, name, lecturer, credits));

        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    static void updateCourse() {
        System.out.println("\n--- UPDATE COURSE ---");
        manager.viewAllCourses();
        System.out.print("Enter Course Code to update: ");
        String code = scanner.nextLine().trim();

        System.out.print("New Course Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("New Lecturer: ");
        String lecturer = scanner.nextLine().trim();
        System.out.print("New Credit Hours: ");
        int credits = readInt();

        if (name.isEmpty() || lecturer.isEmpty() || credits <= 0) {
            System.out.println("Invalid input. Update cancelled.");
            return;
        }
        manager.updateCourse(code, name, lecturer, credits);
    }

    static void deleteCourse() {
        System.out.println("\n--- DELETE COURSE ---");
        manager.viewAllCourses();
        System.out.print("Enter Course Code to delete: ");
        String code = scanner.nextLine().trim();
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            manager.deleteCourse(code);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

// ──────────────────────────────────────────────
//  STUDENT
// ──────────────────────────────────────────────

    static void studentRegistration() {
        System.out.println("\n--- STUDENT REGISTRATION ---");
        System.out.print("Choose a Username: ");
        String user = scanner.nextLine().trim();
        System.out.print("Choose a Password: ");
        String pass = scanner.nextLine().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }
        if (pass.length() < 4) {
            System.out.println("Password must be at least 4 characters.");
            return;
        }
        manager.registerStudent(user, pass);
    }

    static void studentLogin() {
        System.out.println("\n--- STUDENT LOGIN ---");
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        Student student = manager.loginStudent(user, pass);
        if (student != null) {
            System.out.println("Welcome, " + student.getUsername() + "!");
            studentMenu(student);
        } else {
            System.out.println("Invalid credentials. Please register first.");
        }
    }

    static void studentMenu(Student student) {
        while (true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. View My Enrolled Courses");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> manager.viewAllCourses();
                case 2 -> enrollInCourse(student);
                case 3 -> student.viewCourses();
                case 4 -> {
                    manager.saveEnrollments();
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void enrollInCourse(Student student) {
        manager.viewAllCourses();
        System.out.print("Enter Course Code or Name to enroll: ");
        String input = scanner.nextLine().trim();

        Course course = manager.findCourse(input);
        if (course == null) {
            System.out.println("Course not found. Please check the code or name.");
        } else {
            student.enrollCourse(course.getCourseName());
            manager.saveEnrollments();
        }
    }

// ──────────────────────────────────────────────
//  UTILITY: safe integer input
// ──────────────────────────────────────────────

    static int readInt() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear bad input
                System.out.print("Please enter a valid number: ");
            }
        }
    }

}