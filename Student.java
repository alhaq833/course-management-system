package com.university.system;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private List<String> enrolledCourses = new ArrayList<>();

    public Student(String username, String password) {
        super(username, password, "STUDENT");
    }

    public void enrollCourse(String courseName) {
        if (enrolledCourses.contains(courseName)) {
            System.out.println("Already enrolled in this course.");
        } else {
            enrolledCourses.add(courseName);
            System.out.println("Enrolled successfully.");
        }
    }

    public void viewCourses() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled yet.");
        } else {
            System.out.println("Your Enrolled Courses:");
            for (int i = 0; i < enrolledCourses.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + enrolledCourses.get(i));
            }
        }
    }

    public List<String> getEnrolledCourses() { return enrolledCourses; }

    public void setEnrolledCourses(List<String> courses) {
        this.enrolledCourses = courses;
    }
}