package com.badge.dao;

import com.badge.model.Badge;
import com.badge.util.DBUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Badge lifecycle management:
 * issuing, retrieval, public verification queries, revocation, and expiration handling.
 */
public class BadgeDAO {

    private static final Logger LOGGER = Logger.getLogger(BadgeDAO.class.getName());

    /**
     * Base SQL query selecting badge along with joined student and module details.
     */
    private static final String BASE_SELECT = 
        "SELECT b.id, b.badge_code, b.student_id, b.module_id, b.badge_level, " +
        "b.issue_date, b.expiry_date, b.status, b.issued_by, b.revoked_reason, b.created_at, " +
        "s.name AS student_name, s.student_id AS student_roll, s.email AS student_email, s.course AS student_course, " +
        "m.module_code, m.module_name, m.description AS module_description " +
        "FROM badges b " +
        "JOIN students s ON b.student_id = s.id " +
        "JOIN modules m ON b.module_id = m.id ";

    /**
     * Issues a new badge.
     * @param badge badge details
     * @return true if successful
     */
    public boolean issueBadge(Badge badge) {
        String sql = "INSERT INTO badges (badge_code, student_id, module_id, badge_level, issue_date, expiry_date, status, issued_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, badge.getBadgeCode().trim());
            ps.setInt(2, badge.getStudentId());
            ps.setInt(3, badge.getModuleId());
            ps.setString(4, badge.getBadgeLevel());
            ps.setDate(5, badge.getIssueDate());
            ps.setDate(6, badge.getExpiryDate());
            ps.setString(7, badge.getStatus() != null ? badge.getStatus() : "ACTIVE");
            ps.setString(8, badge.getIssuedBy() != null ? badge.getIssuedBy() : "Administrator");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        badge.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error issuing badge with code: " + badge.getBadgeCode(), e);
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
        return false;
    }

    /**
     * Fetches all badges earned by a specific student.
     * @param studentId the student's primary key
     * @return List of badges
     */
    public List<Badge> getBadgesByStudentId(int studentId) {
        List<Badge> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE b.student_id = ? ORDER BY b.issue_date DESC, b.id DESC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToBadge(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting badges for student ID: " + studentId, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return list;
    }

    /**
     * Fetches a badge by its unique public verification code.
     * @param badgeCode unique code
     * @return Badge object if found, null otherwise
     */
    public Badge getBadgeByCode(String badgeCode) {
        if (badgeCode == null || badgeCode.trim().isEmpty()) {
            return null;
        }
        String sql = BASE_SELECT + "WHERE UPPER(b.badge_code) = UPPER(?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, badgeCode.trim());
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToBadge(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error looking up badge by code: " + badgeCode, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return null;
    }

    /**
     * Fetches a badge by primary key ID.
     */
    public Badge getBadgeById(int id) {
        String sql = BASE_SELECT + "WHERE b.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToBadge(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error looking up badge by ID: " + id, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return null;
    }

    /**
     * Fetches all issued badges for administration, supporting search and filters.
     * @param search keyword (code, student name, roll, module)
     * @param statusFilter ACTIVE, REVOKED, EXPIRED, or ALL
     * @param levelFilter Bronze, Silver, Gold, or ALL
     * @return List of matching badges
     */
    public List<Badge> getAllBadges(String search, String statusFilter, String levelFilter) {
        List<Badge> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> params = new ArrayList<>();
        boolean hasWhere = false;

        if (search != null && !search.trim().isEmpty()) {
            sql.append("WHERE (b.badge_code LIKE ? OR s.name LIKE ? OR s.student_id LIKE ? OR m.module_name LIKE ?) ");
            String term = "%" + search.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
            hasWhere = true;
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(statusFilter)) {
            sql.append(hasWhere ? "AND " : "WHERE ");
            sql.append("b.status = ? ");
            params.add(statusFilter.trim().toUpperCase());
            hasWhere = true;
        }

        if (levelFilter != null && !levelFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(levelFilter)) {
            sql.append(hasWhere ? "AND " : "WHERE ");
            sql.append("b.badge_level = ? ");
            params.add(levelFilter.trim());
        }

        sql.append("ORDER BY b.id DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRowToBadge(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching/retrieving badges", e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return list;
    }

    /**
     * Revokes a badge with an optional stated reason.
     * @param badgeId primary key
     * @param reason revocation explanation
     * @return true if updated
     */
    public boolean revokeBadge(int badgeId, String reason) {
        String sql = "UPDATE badges SET status = 'REVOKED', revoked_reason = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, reason != null && !reason.trim().isEmpty() ? reason.trim() : "Revoked by Administrator");
            ps.setInt(2, badgeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error revoking badge ID: " + badgeId, e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Reactivates a revoked or inactive badge.
     * @param badgeId primary key
     * @return true if updated
     */
    public boolean reactivateBadge(int badgeId) {
        String sql = "UPDATE badges SET status = 'ACTIVE', revoked_reason = NULL WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, badgeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reactivating badge ID: " + badgeId, e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Deletes a badge completely from the system.
     */
    public boolean deleteBadge(int badgeId) {
        String sql = "DELETE FROM badges WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, badgeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting badge ID: " + badgeId, e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Checks if a badge code already exists.
     */
    public boolean badgeCodeExists(String badgeCode) {
        String sql = "SELECT id FROM badges WHERE badge_code = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, badgeCode.trim());
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking badge code existence: " + badgeCode, e);
            return false;
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
    }

    /**
     * Maps a ResultSet row into a populated Badge model object.
     */
    private Badge mapRowToBadge(ResultSet rs) throws SQLException {
        Badge b = new Badge();
        b.setId(rs.getInt("id"));
        b.setBadgeCode(rs.getString("badge_code"));
        b.setStudentId(rs.getInt("student_id"));
        b.setModuleId(rs.getInt("module_id"));
        b.setBadgeLevel(rs.getString("badge_level"));
        b.setIssueDate(rs.getDate("issue_date"));
        b.setExpiryDate(rs.getDate("expiry_date"));
        b.setStatus(rs.getString("status"));
        b.setIssuedBy(rs.getString("issued_by"));
        b.setRevokedReason(rs.getString("revoked_reason"));
        b.setCreatedAt(rs.getTimestamp("created_at"));

        b.setStudentName(rs.getString("student_name"));
        b.setStudentRoll(rs.getString("student_roll"));
        b.setStudentEmail(rs.getString("student_email"));
        b.setStudentCourse(rs.getString("student_course"));
        b.setModuleCode(rs.getString("module_code"));
        b.setModuleName(rs.getString("module_name"));
        b.setModuleDescription(rs.getString("module_description"));
        return b;
    }
}
