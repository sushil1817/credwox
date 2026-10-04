package com.badge.dao;

import com.badge.model.Admin;
import com.badge.util.DBUtil;
import com.badge.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Administrator operations and high-level platform metrics.
 */
public class AdminDAO {

    private static final Logger LOGGER = Logger.getLogger(AdminDAO.class.getName());

    /**
     * Authenticates an admin by username and plain-text password.
     * @param username admin username
     * @param plainPassword plain-text password
     * @return Admin object if authenticated, null otherwise
     */
    public Admin authenticate(String username, String plainPassword) {
        String sql = "SELECT id, username, password, full_name, created_at FROM admins WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (PasswordUtil.verifyPassword(plainPassword, storedHash)) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setFullName(rs.getString("full_name"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error authenticating admin: " + username, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    /**
     * Fetches comprehensive system analytics for the admin dashboard.
     * @return Map containing metric counters
     */
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalStudents", 0);
        stats.put("totalModules", 0);
        stats.put("totalBadges", 0);
        stats.put("activeBadges", 0);
        stats.put("revokedBadges", 0);
        stats.put("expiredBadges", 0);
        stats.put("totalVerifications", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // Total Students
            ps = conn.prepareStatement("SELECT COUNT(*) FROM students");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("totalStudents", rs.getInt(1));
            rs.close(); ps.close();

            // Total Modules
            ps = conn.prepareStatement("SELECT COUNT(*) FROM modules");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("totalModules", rs.getInt(1));
            rs.close(); ps.close();

            // Total Badges
            ps = conn.prepareStatement("SELECT COUNT(*) FROM badges");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("totalBadges", rs.getInt(1));
            rs.close(); ps.close();

            // Active Badges (Active and expiry either null or >= CURDATE())
            ps = conn.prepareStatement("SELECT COUNT(*) FROM badges WHERE status = 'ACTIVE' AND (expiry_date IS NULL OR expiry_date >= CURDATE())");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("activeBadges", rs.getInt(1));
            rs.close(); ps.close();

            // Revoked Badges
            ps = conn.prepareStatement("SELECT COUNT(*) FROM badges WHERE status = 'REVOKED'");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("revokedBadges", rs.getInt(1));
            rs.close(); ps.close();

            // Expired Badges
            ps = conn.prepareStatement("SELECT COUNT(*) FROM badges WHERE status = 'EXPIRED' OR (status = 'ACTIVE' AND expiry_date < CURDATE())");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("expiredBadges", rs.getInt(1));
            rs.close(); ps.close();

            // Total Verifications
            ps = conn.prepareStatement("SELECT COUNT(*) FROM verification_history");
            rs = ps.executeQuery();
            if (rs.next()) stats.put("totalVerifications", rs.getInt(1));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving dashboard statistics", e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return stats;
    }
}
