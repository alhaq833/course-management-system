package com.university.system;

public class Course {
    private String courseCode;
    private String courseName;
    private String lecturer;
    private int creditHours;

    public Course(String courseCode, String courseName, String lecturer, int creditHours) {
        this.courseCode   = courseCode;
        this.courseName   = courseName;
        this.lecturer     = lecturer;
        this.creditHours  = creditHours;
    }

    public String getCourseCode()  { return courseCode; }
    public String getCourseName()  { return courseName; }
    public String getLecturer()    { return lecturer; }
    public int    getCreditHours() { return creditHours; }

    public void setCourseName(String courseName)   { this.courseName  = courseName; }
    public void setLecturer(String lecturer)        { this.lecturer    = lecturer; }
    public void setCreditHours(int creditHours)     { this.creditHours = creditHours; }

    // CSV format: courseCode,courseName,lecturer,creditHours
    public String toCSV() {
        return courseCode + "," + courseName + "," + lecturer + "," + creditHours;
    }

    public static Course fromCSV(String line) {
        String[] parts = line.split(",");
        return new Course(parts[0], parts[1], parts[2], Integer.parseInt(parts[3].trim()));
    }

    @Override
    public String toString() {
        return String.format("  [%s] %s | Lecturer: %s | Credits: %d",
                courseCode, courseName, lecturer, creditHours);
    }

}