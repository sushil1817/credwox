package com.badge.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Model representing a Digital Skill Badge issued to a student.
 */
public class Badge implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String badgeCode;
    private int studentId;
    private int moduleId;
    private String badgeLevel; // Bronze, Silver, Gold
    private Date issueDate;
    private Date expiryDate;
    private String status; // ACTIVE, REVOKED, EXPIRED
    private String issuedBy;
    private String revokedReason;
    private Timestamp createdAt;

    // Joined fields from students and modules for easy display
    private String studentName;
    private String studentRoll;
    private String studentEmail;
    private String studentCourse;
    private String moduleCode;
    private String moduleName;
    private String moduleDescription;

    public Badge() {}

    /**
     * Determines whether this badge is expired based on current date.
     * @return true if expiryDate exists and is strictly before today
     */
    public boolean isExpired() {
        if ("REVOKED".equalsIgnoreCase(this.status)) {
            return false;
        }
        if (this.expiryDate == null) {
            return false;
        }
        LocalDate exp = this.expiryDate.toLocalDate();
        LocalDate now = LocalDate.now();
        return exp.isBefore(now);
    }

    /**
     * Returns the dynamic effective status:
     * - REVOKED if explicitly revoked
     * - EXPIRED if expired by date or marked EXPIRED
     * - ACTIVE otherwise
     */
    public String getEffectiveStatus() {
        if ("REVOKED".equalsIgnoreCase(this.status)) {
            return "REVOKED";
        }
        if (isExpired() || "EXPIRED".equalsIgnoreCase(this.status)) {
            return "EXPIRED";
        }
        return "ACTIVE";
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBadgeCode() {
        return badgeCode;
    }

    public void setBadgeCode(String badgeCode) {
        this.badgeCode = badgeCode;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getBadgeLevel() {
        return badgeLevel;
    }

    public void setBadgeLevel(String badgeLevel) {
        this.badgeLevel = badgeLevel;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getRevokedReason() {
        return revokedReason;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(String studentCourse) {
        this.studentCourse = studentCourse;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }
}
