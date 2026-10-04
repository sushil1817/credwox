package com.badge.dao;

import com.badge.model.SkillModule;
import com.badge.util.DBUtil;

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
 * Data Access Object for managing Skill Modules / Course Curriculum.
 */
public class ModuleDAO {

    private static final Logger LOGGER = Logger.getLogger(ModuleDAO.class.getName());

    /**
     * Retrieves all skill modules with their badges count.
     */
    public List<SkillModule> getAllModules() {
        List<SkillModule> modules = new ArrayList<>();
        String sql = "SELECT m.id, m.module_code, m.module_name, m.description, m.created_at, " +
                     "COUNT(b.id) AS badges_issued " +
                     "FROM modules m " +
                     "LEFT JOIN badges b ON m.id = b.module_id " +
                     "GROUP BY m.id, m.module_code, m.module_name, m.description, m.created_at " +
                     "ORDER BY m.module_code ASC";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                SkillModule m = new SkillModule();
                m.setId(rs.getInt("id"));
                m.setModuleCode(rs.getString("module_code"));
                m.setModuleName(rs.getString("module_name"));
                m.setDescription(rs.getString("description"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                m.setBadgesIssuedCount(rs.getInt("badges_issued"));
                modules.add(m);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching skill modules", e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }

        return modules;
    }

    /**
     * Finds a module by ID.
     */
    public SkillModule getModuleById(int id) {
        String sql = "SELECT id, module_code, module_name, description, created_at FROM modules WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                SkillModule m = new SkillModule();
                m.setId(rs.getInt("id"));
                m.setModuleCode(rs.getString("module_code"));
                m.setModuleName(rs.getString("module_name"));
                m.setDescription(rs.getString("description"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                return m;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching module by ID: " + id, e);
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
        return null;
    }

    /**
     * Inserts a new skill module.
     */
    public boolean addModule(SkillModule module) {
        String sql = "INSERT INTO modules (module_code, module_name, description) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, module.getModuleCode().trim().toUpperCase());
            ps.setString(2, module.getModuleName().trim());
            ps.setString(3, module.getDescription() != null ? module.getDescription().trim() : "");

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        module.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding skill module: " + module.getModuleCode(), e);
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
        return false;
    }

    /**
     * Updates an existing skill module.
     */
    public boolean updateModule(SkillModule module) {
        String sql = "UPDATE modules SET module_code = ?, module_name = ?, description = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, module.getModuleCode().trim().toUpperCase());
            ps.setString(2, module.getModuleName().trim());
            ps.setString(3, module.getDescription() != null ? module.getDescription().trim() : "");
            ps.setInt(4, module.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating module ID: " + module.getId(), e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Deletes a skill module if no badges are tied to it.
     */
    public boolean deleteModule(int id) {
        String sql = "DELETE FROM modules WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting module ID: " + id + " (may have referenced badges)", e);
            return false;
        } finally {
            DBUtil.closeQuietly(ps, conn);
        }
    }

    /**
     * Checks if a module code already exists.
     */
    public boolean moduleCodeExists(String moduleCode, int excludeId) {
        String sql = "SELECT id FROM modules WHERE module_code = ? AND id != ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, moduleCode.trim().toUpperCase());
            ps.setInt(2, excludeId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking module code: " + moduleCode, e);
            return false;
        } finally {
            DBUtil.closeQuietly(rs, ps, conn);
        }
    }
}
