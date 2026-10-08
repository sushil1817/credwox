-- =============================================================================
-- CredWox — Woxsen Digital Credential & Verification Portal
-- Database Seed Data: badge_portal / railway
-- Woxsen University
-- =============================================================================
-- Note: If running on Railway MySQL where the database is named 'railway',
-- you can omit or comment out 'USE badge_portal;' if executing in the default database.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS badge_portal 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE badge_portal;

-- Clean existing demo data (manual execution only; not run on application startup)
DELETE FROM verification_history;
DELETE FROM badges;
DELETE FROM modules;
DELETE FROM students;
DELETE FROM admins;

-- -----------------------------------------------------------------------------
-- 1. Seed Administrators (Woxsen University Authority)
-- Username: admin | Password: admin123
-- SHA-256: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- -----------------------------------------------------------------------------
INSERT INTO admins (id, username, password, full_name) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Prof. Veeresh Biradar (Dept. Head)');

-- -----------------------------------------------------------------------------
-- 2. Seed Students (Official Woxsen University Students)
-- Default password: student123
-- SHA-256: 703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b
-- -----------------------------------------------------------------------------
INSERT INTO students (id, student_id, name, email, password, course) VALUES
(1, '25WU0101141', 'Sushil Pal', 'sushil.pal@woxsen.edu.in', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b', 'B.Tech Computer Science & Engineering'),
(2, '25WU0101142', 'Tanishq Hanumanta', 'tanishq.hanumanta@woxsen.edu.in', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b', 'B.Tech Computer Science & Engineering'),
(3, '25WU0104027', 'Suhaan Kapoor', 'suhaan.kapoor@woxsen.edu.in', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b', 'B.Tech Data Science & Artificial Intelligence');

-- -----------------------------------------------------------------------------
-- 3. Seed Skill Modules (Woxsen University Academic Curriculum)
-- -----------------------------------------------------------------------------
INSERT INTO modules (id, module_code, module_name, description) VALUES
(1, 'WT-101', 'Full-Stack Web Development & Enterprise Java', 'Mastery in HTTP protocols, Servlet 4.0 architecture, JSP templating, JDBC persistence, and enterprise application security.'),
(2, 'DS-201', 'Advanced Data Structures & Algorithmic Problem Solving', 'Competency in asymptotic analysis, balanced tree structures, graph algorithms, and dynamic programming optimization.'),
(3, 'CS-301', 'Cloud Native Systems & Container Orchestration', 'Practical implementation of microservices, Docker containerization, RESTful API design, and CI/CD pipelines.'),
(4, 'AI-401', 'Applied Machine Learning & Neural Computation', 'Foundations in supervised learning, regression models, tensor computing, and practical deep neural network architectures.'),
(5, 'CY-501', 'Secure Web Architecture & Cryptographic Systems', 'Mitigation of OWASP Top 10 vulnerabilities, cryptographic token validation, input sanitization, and session defense.');

-- -----------------------------------------------------------------------------
-- 4. Seed Issued Credentials
-- Signatory: Prof. Veeresh Biradar (Dept. Head)
-- -----------------------------------------------------------------------------
-- Sushil Pal: Active Gold Credential in Web Development
INSERT INTO badges (id, badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by) VALUES
(1, 'CWX-GOLD-202601', 1, 1, 'Gold', '2026-01-15', '2028-01-15', 'ACTIVE', 'Prof. Veeresh Biradar (Dept. Head)');

-- Tanishq Hanumanta: Active Silver Credential in Data Structures
INSERT INTO badges (id, badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by) VALUES
(2, 'CWX-SILV-202602', 2, 2, 'Silver', '2026-02-10', '2027-02-10', 'ACTIVE', 'Prof. Veeresh Biradar (Dept. Head)');

-- Suhaan Kapoor: Active Gold Credential in Machine Learning
INSERT INTO badges (id, badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by) VALUES
(3, 'CWX-GOLD-202603', 3, 4, 'Gold', '2026-01-20', '2028-01-20', 'ACTIVE', 'Prof. Veeresh Biradar (Dept. Head)');

-- Suhaan Kapoor: Expired Silver Credential in Cloud Native Systems (for verification testing)
INSERT INTO badges (id, badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by) VALUES
(4, 'CWX-EXPD-202501', 3, 3, 'Silver', '2024-06-01', '2025-12-31', 'EXPIRED', 'Prof. Veeresh Biradar (Dept. Head)');

-- Sushil Pal: Sample Revoked Credential for Demonstration
INSERT INTO badges (id, badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by, revoked_reason) VALUES
(5, 'CWX-REVK-202605', 1, 5, 'Bronze', '2026-01-10', '2027-01-10', 'REVOKED', 'Prof. Veeresh Biradar (Dept. Head)', 'Revoked following administrative coursework policy review.');

-- Tanishq Hanumanta: Active Gold Credential in Cloud Native Systems
INSERT INTO badges (id, badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by) VALUES
(6, 'CWX-GOLD-202606', 2, 3, 'Gold', '2026-02-01', '2028-02-01', 'ACTIVE', 'Prof. Veeresh Biradar (Dept. Head)');

-- -----------------------------------------------------------------------------
-- 5. Seed Verification Audit History
-- -----------------------------------------------------------------------------
INSERT INTO verification_history (badge_code, verification_time, verification_result, ip_address) VALUES
('CWX-GOLD-202601', '2026-09-10 14:22:10', 'ACTIVE', '127.0.0.1'),
('CWX-EXPD-202501', '2026-09-12 11:05:43', 'EXPIRED', '192.168.1.15'),
('CWX-REVK-202605', '2026-09-15 16:48:20', 'REVOKED', '192.168.1.42');
