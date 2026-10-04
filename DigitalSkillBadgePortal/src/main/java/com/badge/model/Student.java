package com.badge.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a Student registered on the portal.
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String studentId;
    private String name;
    private String email;
    private String password;
    private String course;
    private Timestamp createdAt;

    // Derived stats for display
    private int totalBadges;
    private int activeBadges;

    public Student() {}

    public Student(int id, String studentId, String name, String email, String password, String course, Timestamp createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.course = course;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotalBadges() {
        return totalBadges;
    }

    public void setTotalBadges(int totalBadges) {
        this.totalBadges = totalBadges;
    }

    public int getActiveBadges() {
        return activeBadges;
    }

    public void setActiveBadges(int activeBadges) {
        this.activeBadges = activeBadges;
    }
}
