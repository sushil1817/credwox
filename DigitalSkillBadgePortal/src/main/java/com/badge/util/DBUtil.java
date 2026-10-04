package com.badge.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBUtil manages JDBC database connections for the Badge Portal application.
 * Reads configuration from db.properties if available, otherwise falls back to defaults.
 */
public class DBUtil {

    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());
    
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/badge_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "";

    static {
        // 1. Load configuration from classpath properties file if present
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                if (props.getProperty("db.driver") != null) driver = props.getProperty("db.driver").trim();
                if (props.getProperty("db.url") != null) url = props.getProperty("db.url").trim();
                if (props.getProperty("db.username") != null) username = props.getProperty("db.username").trim();
                if (props.getProperty("db.password") != null) password = props.getProperty("db.password").trim();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "db.properties not found or could not be loaded: " + e.getMessage());
        }

        // 2. Override with Environment variables or System properties (cloud deployment friendly)
        String envDriver = getEnvOrProperty("DB_DRIVER");
        if (envDriver != null && !envDriver.trim().isEmpty()) {
            driver = envDriver.trim();
        }

        String envUrl = getEnvOrProperty("DB_URL", "DATABASE_URL", "MYSQL_URL");
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            url = envUrl.trim();
        }

        String envUser = getEnvOrProperty("DB_USERNAME", "DB_USER", "MYSQLUSER");
        if (envUser != null && !envUser.trim().isEmpty()) {
            username = envUser.trim();
        }

        String envPass = getEnvOrProperty("DB_PASSWORD", "MYSQLPASSWORD", "MYSQL_PASSWORD");
        if (envPass != null) {
            password = envPass;
        }

        try {
            Class.forName(driver);
            LOGGER.info("JDBC Driver successfully registered: " + driver);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to register JDBC Driver: " + driver, e);
        }
    }

    private static String getEnvOrProperty(String... keys) {
        for (String key : keys) {
            String val = System.getenv(key);
            if (val != null) {
                return val;
            }
            val = System.getProperty(key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * Obtains a new database connection from the driver manager.
     * @return Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Safely closes one or more database resources (Connection, Statement, ResultSet).
     * @param closeables resources to close
     */
    public static void closeQuietly(AutoCloseable... closeables) {
        if (closeables == null) return;
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error closing resource", e);
                }
            }
        }
    }

    /**
     * Helper to test whether the database connection is alive and working.
     * @return true if connection succeeds, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
