-- =============================================================================
-- Project: Digital Skill Badge & Verification Portal
-- Database Schema: badge_portal
-- Target RDBMS: MySQL 8.x
-- =============================================================================

CREATE DATABASE IF NOT EXISTS badge_portal 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE badge_portal;

-- Disable foreign key checks for clean drops
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS verification_history;
DROP TABLE IF EXISTS badges;
DROP TABLE IF EXISTS modules;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS admins;
SET FOREIGN_KEY_CHECKS = 1;

-- -----------------------------------------------------------------------------
-- 1. Table: admins
-- Stores administrator credentials and profiles for portal management.
-- -----------------------------------------------------------------------------
CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- SHA-256 hashed password
    full_name VARCHAR(100) DEFAULT 'Portal Administrator',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. Table: students
-- Stores registered students who can earn skill badges.
-- -----------------------------------------------------------------------------
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- SHA-256 hashed password
    course VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. Table: modules
-- Stores skill curriculum modules for which badges are awarded.
-- -----------------------------------------------------------------------------
CREATE TABLE modules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    module_code VARCHAR(50) NOT NULL UNIQUE,
    module_name VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 4. Table: badges
-- Stores issued digital badges associated with students and skill modules.
-- Status can be: ACTIVE, REVOKED, EXPIRED
-- Badge levels: Bronze, Silver, Gold
-- -----------------------------------------------------------------------------
CREATE TABLE badges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    badge_code VARCHAR(50) NOT NULL UNIQUE,
    student_id INT NOT NULL,
    module_id INT NOT NULL,
    badge_level ENUM('Bronze', 'Silver', 'Gold') NOT NULL,
    issue_date DATE NOT NULL,
    expiry_date DATE DEFAULT NULL,
    status ENUM('ACTIVE', 'REVOKED', 'EXPIRED') NOT NULL DEFAULT 'ACTIVE',
    issued_by VARCHAR(50) DEFAULT 'Administrator',
    revoked_reason VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_badge_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_badge_module FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_badge_code (badge_code),
    INDEX idx_badge_status (status),
    INDEX idx_badge_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 5. Table: verification_history
-- Records every public badge lookup and validation attempt for audit & security.
-- -----------------------------------------------------------------------------
CREATE TABLE verification_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    badge_code VARCHAR(50) NOT NULL,
    verification_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verification_result VARCHAR(50) NOT NULL, -- e.g. ACTIVE, EXPIRED, REVOKED, NOT_FOUND
    ip_address VARCHAR(45) DEFAULT NULL,
    INDEX idx_verify_code (badge_code),
    INDEX idx_verify_time (verification_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
