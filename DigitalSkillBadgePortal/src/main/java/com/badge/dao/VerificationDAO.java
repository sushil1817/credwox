package com.badge.dao;

import com.badge.model.VerificationRecord;
import com.badge.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for recording and auditing public verification activities.
 */
public class VerificationDAO {

    private static final Logger LOGGER = Logger.getLogger(VerificationDAO.class.getName());

    /**
     * Records a verification event in the verification_history audit log.
     * @param badgeCode verified code
     * @param result result outcome (ACTIVE, EXPIRED, REVOKED, NOT_FOUND)
     * @param ipAddress IP address of the verifier
     * @return true if logged successfully
     */
    public boolean recordVerification(String badgeCode, String result, String ipAddress) {
        String sql = "INSERT INTO verification_history (badge_code, verification_result, ip_address) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, badgeCode != null ? badgeCode.trim() : "UNKNOWN");
            ps.setString(2, result != null ? result.trim().toUpperCase() : "UNKNOWN");
            ps.setString(3, ipAddress != null ? ipAddress.trim() : "UNKNOWN");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error logging verification history for code: " + badgeCode, e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Retrieves recent verification audit records with joined badge, student and module information.
     * @param limit maximum records to fetch
     * @return list of records
     */
    public List<VerificationRecord> getRecentVerifications(int limit) {
        List<VerificationRecord> list = new ArrayList<>();
        String sql = "SELECT v.id, v.badge_code, v.verification_time, v.verification_result, v.ip_address, " +
                     "s.name AS student_name, m.module_name, b.badge_level " +
                     "FROM verification_history v " +
                     "LEFT JOIN badges b ON v.badge_code = b.badge_code " +
                     "LEFT JOIN students s ON b.student_id = s.id " +
                     "LEFT JOIN modules m ON b.module_id = m.id " +
                     "ORDER BY v.verification_time DESC, v.id DESC " +
                     "LIMIT ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit > 0 ? limit : 20);
            rs = ps.executeQuery();

            while (rs.next()) {
                VerificationRecord rec = new VerificationRecord();
                rec.setId(rs.getInt("id"));
                rec.setBadgeCode(rs.getString("badge_code"));
                rec.setVerificationTime(rs.getTimestamp("verification_time"));
                rec.setVerificationResult(rs.getString("verification_result"));
                rec.setIpAddress(rs.getString("ip_address"));
                rec.setStudentName(rs.getString("student_name"));
                rec.setModuleName(rs.getString("module_name"));
                rec.setBadgeLevel(rs.getString("badge_level"));
                list.add(rec);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving verification history", e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return list;
    }
}
