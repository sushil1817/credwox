package com.badge.dao;

import com.badge.model.Student;
import com.badge.util.DBUtil;
import com.badge.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Student CRUD, authentication, and stats.
 */
public class StudentDAO {

    private static final Logger LOGGER = Logger.getLogger(StudentDAO.class.getName());

    /**
     * Registers a new student into the database with a hashed password.
     * @param student student details
     * @return true if inserted successfully, false otherwise
     */
    public boolean register(Student student) {
        String sql = "INSERT INTO students (student_id, name, email, password, course) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getStudentId().trim().toUpperCase());
            ps.setString(2, student.getName().trim());
            ps.setString(3, student.getEmail().trim().toLowerCase());
            ps.setString(4, PasswordUtil.hashPassword(student.getPassword()));
            ps.setString(5, student.getCourse().trim());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering student: " + student.getEmail(), e);
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
        return false;
    }

    private String lastError = null;

    public String getLastError() {
        return lastError;
    }

    /**
     * Authenticates a student using either their email or student ID and plain-text password.
     * @param identifier email or student ID
     * @param plainPassword plain-text password
     * @return Student object if valid, null otherwise
     */
    public Student authenticate(String identifier, String plainPassword) {
        lastError = null;
        String sql = "SELECT id, student_id, name, email, password, course, created_at FROM students WHERE email = ? OR student_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, identifier.trim());
            ps.setString(2, identifier.trim().toUpperCase());
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (PasswordUtil.verifyPassword(plainPassword, storedHash)) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setStudentId(rs.getString("student_id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setCourse(rs.getString("course"));
                    student.setCreatedAt(rs.getTimestamp("created_at"));
                    return student;
                }
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            LOGGER.log(Level.SEVERE, "Error authenticating student with identifier: " + identifier, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    /**
     * Finds a student by primary key ID.
     */
    public Student findById(int id) {
        String sql = "SELECT id, student_id, name, email, password, course, created_at FROM students WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setStudentId(rs.getString("student_id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setCourse(rs.getString("course"));
                s.setCreatedAt(rs.getTimestamp("created_at"));
                return s;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding student by ID: " + id, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    /**
     * Checks if an email is already registered.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM students WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email.trim().toLowerCase());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking email existence: " + email, e);
            return false;
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
    }

    /**
     * Checks if a Student ID is already registered.
     */
    public boolean studentIdExists(String studentId) {
        String sql = "SELECT id FROM students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, studentId.trim().toUpperCase());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking studentId existence: " + studentId, e);
            return false;
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
    }

    /**
     * Updates student profile details (name and course).
     */
    public boolean updateProfile(int studentId, String name, String course) {
        String sql = "UPDATE students SET name = ?, course = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name.trim());
            ps.setString(2, course.trim());
            ps.setInt(3, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating profile for student ID: " + studentId, e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Updates student password.
     */
    public boolean updatePassword(int studentId, String newPlainPassword) {
        String sql = "UPDATE students SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, PasswordUtil.hashPassword(newPlainPassword));
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password for student ID: " + studentId, e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Retrieves all students along with their badge metrics for admin view.
     */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.id, s.student_id, s.name, s.email, s.course, s.created_at, " +
                     "COUNT(b.id) AS total_badges, " +
                     "SUM(CASE WHEN b.status = 'ACTIVE' AND (b.expiry_date IS NULL OR b.expiry_date >= CURDATE()) THEN 1 ELSE 0 END) AS active_badges " +
                     "FROM students s " +
                     "LEFT JOIN badges b ON s.id = b.student_id " +
                     "GROUP BY s.id, s.student_id, s.name, s.email, s.course, s.created_at " +
                     "ORDER BY s.id DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setStudentId(rs.getString("student_id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setCourse(rs.getString("course"));
                s.setCreatedAt(rs.getTimestamp("created_at"));
                s.setTotalBadges(rs.getInt("total_badges"));
                s.setActiveBadges(rs.getInt("active_badges"));
                list.add(s);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all students", e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return list;
    }
}
