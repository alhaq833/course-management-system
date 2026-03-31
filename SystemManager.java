package com.university.system;

import java.util.List;

public class SystemManager {
    private List<Course>  courses;
    private List<Student> students;

    public SystemManager() {
        courses  = FileManager.loadCourses();
        students = FileManager.loadStudents();
        FileManager.loadEnrollments(students);
    }

// ──────────────────────────────────────────────
//  COURSE OPERATIONS
// ──────────────────────────────────────────────

    public void addCourse(Course course) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                System.out.println("A course with that code already exists.");
                return;
            }
        }
        courses.add(course);
        FileManager.saveCourses(courses);
        System.out.println("Course added successfully.");
    }

    public void viewAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            System.out.println("\n===== ALL COURSES =====");
            for (Course c : courses) {
                System.out.println(c);
            }
        }
    }

    public boolean updateCourse(String code, String newName, String newLecturer, int newCredits) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                c.setCourseName(newName);
                c.setLecturer(newLecturer);
                c.setCreditHours(newCredits);
                FileManager.saveCourses(courses);
                System.out.println("Course updated successfully.");
                return true;
            }
        }
        System.out.println("Course not found.");
        return false;
    }

    public boolean deleteCourse(String code) {
        Course toRemove = null;
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                toRemove = c;
                break;
            }
        }
        if (toRemove != null) {
            courses.remove(toRemove);
            FileManager.saveCourses(courses);
            System.out.println("Course deleted successfully.");
            return true;
        }
        System.out.println("Course not found.");
        return false;
    }

    public Course findCourse(String name) {
        for (Course c : courses) {
            if (c.getCourseName().equalsIgnoreCase(name) ||
                    c.getCourseCode().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

// ──────────────────────────────────────────────
//  STUDENT OPERATIONS
// ──────────────────────────────────────────────

    public boolean registerStudent(String username, String password) {
        for (Student s : students) {
            if (s.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists. Choose another.");
                return false;
            }
        }
        Student newStudent = new Student(username, password);
        students.add(newStudent);
        FileManager.saveUsers(students);
        System.out.println("Registration successful! You can now log in.");
        return true;
    }

    public Student loginStudent(String username, String password) {
        for (Student s : students) {
            if (s.login(username, password)) {
                return s;
            }
        }
        return null;
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
        } else {
            System.out.println("\n===== REGISTERED STUDENTS =====");
            for (Student s : students) {
                System.out.println("  Student: " + s.getUsername());
                List<String> enrolled = s.getEnrolledCourses();
                if (enrolled.isEmpty()) {
                    System.out.println("    Enrolled Courses: None");
                } else {
                    System.out.println("    Enrolled Courses:");
                    for (String course : enrolled) {
                        System.out.println("      - " + course);
                    }
                }
            }
        }
    }

    public void saveEnrollments() {
        FileManager.saveEnrollments(students);
    }

    public List<Course>  getCourses()  { return courses; }
    public List<Student> getStudents() { return students; }

}