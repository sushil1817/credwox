package com.badge.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing an audit log entry of a public badge verification attempt.
 */
public class VerificationRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String badgeCode;
    private Timestamp verificationTime;
    private String verificationResult; // ACTIVE, EXPIRED, REVOKED, NOT_FOUND
    private String ipAddress;

    // Optional joined fields for administrative audit display
    private String studentName;
    private String moduleName;
    private String badgeLevel;

    public VerificationRecord() {}

    public VerificationRecord(int id, String badgeCode, Timestamp verificationTime, String verificationResult, String ipAddress) {
        this.id = id;
        this.badgeCode = badgeCode;
        this.verificationTime = verificationTime;
        this.verificationResult = verificationResult;
        this.ipAddress = ipAddress;
    }

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

    public Timestamp getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(Timestamp verificationTime) {
        this.verificationTime = verificationTime;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getBadgeLevel() {
        return badgeLevel;
    }

    public void setBadgeLevel(String badgeLevel) {
        this.badgeLevel = badgeLevel;
    }
}
