package com.badge.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a Skill Module/Course curriculum.
 */
public class SkillModule implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String moduleCode;
    private String moduleName;
    private String description;
    private Timestamp createdAt;

    // Derived count
    private int badgesIssuedCount;

    public SkillModule() {}

    public SkillModule(int id, String moduleCode, String moduleName, String description, Timestamp createdAt) {
        this.id = id;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getBadgesIssuedCount() {
        return badgesIssuedCount;
    }

    public void setBadgesIssuedCount(int badgesIssuedCount) {
        this.badgesIssuedCount = badgesIssuedCount;
    }
}
