package com.badge.util;

import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBUtil manages JDBC database connections for the CredWox Badge Portal application.
 * Reads configuration from db.properties if available, and seamlessly adapts to cloud
 * environment variables (Render, Railway, Heroku, Docker).
 *
 * Supported environment variables:
 * - URL: DB_URL, DATABASE_URL, MYSQL_PUBLIC_URL, MYSQL_URL
 * - User: DB_USERNAME, DB_USER, MYSQLUSER, MYSQL_USER, MYSQL_USERNAME
 * - Password: DB_PASSWORD, MYSQLPASSWORD, MYSQL_PASSWORD
 * - Driver: DB_DRIVER
 */
public class DBUtil {

    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());
    
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/badge_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "";

    static {
        initConfiguration();
    }

    private static void initConfiguration() {
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
            LOGGER.log(Level.FINE, "db.properties not found on classpath: " + e.getMessage());
        }

        // 2. Override with Environment variables or System properties (cloud deployment friendly)
        String envDriver = getEnvOrProperty("DB_DRIVER");
        if (envDriver != null && !envDriver.trim().isEmpty()) {
            driver = envDriver.trim();
        }

        String envUrl = getEnvOrProperty("DB_URL", "DATABASE_URL", "MYSQL_PUBLIC_URL", "MYSQL_URL");
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            url = envUrl.trim();
        } else {
            String host = getEnvOrProperty("MYSQLHOST", "DB_HOST");
            String port = getEnvOrProperty("MYSQLPORT", "DB_PORT");
            String db = getEnvOrProperty("MYSQLDATABASE", "DB_NAME");
            if (host != null && !host.trim().isEmpty()) {
                if (port == null || port.trim().isEmpty()) port = "3306";
                if (db == null || db.trim().isEmpty()) db = "railway";
                url = "jdbc:mysql://" + host.trim() + ":" + port.trim() + "/" + db.trim();
            }
        }

        String envUser = getEnvOrProperty("DB_USERNAME", "DB_USER", "MYSQLUSER", "MYSQL_USER", "MYSQL_USERNAME");
        if (envUser != null && !envUser.trim().isEmpty()) {
            username = envUser.trim();
        }

        String envPass = getEnvOrProperty("DB_PASSWORD", "MYSQLPASSWORD", "MYSQL_PASSWORD");
        if (envPass != null) {
            password = envPass;
        }

        // 3. Normalize URL format (handling Railway mysql:// syntax and parameters)
        normalizeUrl();

        // 4. Register JDBC Driver
        try {
            Class.forName(driver);
            LOGGER.info("JDBC Driver successfully registered: " + driver);
            LOGGER.info("Configured Database URL: " + sanitizeUrl(url) + ", User: " + username);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to register JDBC Driver: " + driver, e);
        }
    }

    /**
     * Normalizes the database URL. Handles raw mysql:// URLs (as provided by Railway
     * or other cloud providers) and extracts credentials if embedded in the URI.
     */
    private static void normalizeUrl() {
        if (url == null || url.trim().isEmpty()) {
            return;
        }
        url = url.trim();

        if (url.startsWith("mysql://")) {
            try {
                URI uri = new URI(url);
                String userInfo = uri.getUserInfo();
                if (userInfo != null && !userInfo.isEmpty()) {
                    String[] parts = userInfo.split(":", 2);
                    if (parts.length > 0 && !parts[0].isEmpty() && (username == null || username.isEmpty() || "root".equals(username))) {
                        username = parts[0];
                    }
                    if (parts.length > 1 && (password == null || password.isEmpty())) {
                        password = parts[1];
                    }
                }

                String host = uri.getHost();
                int port = uri.getPort() != -1 ? uri.getPort() : 3306;
                String path = uri.getPath();
                if (path == null || path.isEmpty() || "/".equals(path)) {
                    path = "/badge_portal";
                }
                String query = uri.getQuery();

                StringBuilder jdbc = new StringBuilder("jdbc:mysql://");
                jdbc.append(host).append(":").append(port).append(path);

                if (query != null && !query.isEmpty()) {
                    jdbc.append("?").append(query);
                    if (!query.contains("allowPublicKeyRetrieval")) {
                        jdbc.append("&allowPublicKeyRetrieval=true");
                    }
                    if (!query.contains("useSSL")) {
                        jdbc.append("&useSSL=false");
                    }
                    if (!query.contains("serverTimezone")) {
                        jdbc.append("&serverTimezone=UTC");
                    }
                    if (!query.contains("characterEncoding")) {
                        jdbc.append("&characterEncoding=UTF-8");
                    }
                } else {
                    jdbc.append("?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8");
                }
                url = jdbc.toString();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error parsing mysql:// URI: " + e.getMessage());
                url = "jdbc:" + url;
            }
        } else if (url.startsWith("jdbc:mysql://")) {
            // Check if user credentials are embedded inside jdbc:mysql://user:pass@host:port/db
            String afterPrefix = url.substring("jdbc:mysql://".length());
            if (afterPrefix.contains("@")) {
                int atIdx = afterPrefix.indexOf('@');
                String userInfo = afterPrefix.substring(0, atIdx);
                String rest = afterPrefix.substring(atIdx + 1);
                String[] parts = userInfo.split(":", 2);
                if (parts.length > 0 && !parts[0].isEmpty()) {
                    username = parts[0];
                }
                if (parts.length > 1) {
                    password = parts[1];
                }
                url = "jdbc:mysql://" + rest;
            }

            // Ensure essential MySQL 8 parameters are present
            if (!url.contains("?")) {
                url = url + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
            } else {
                if (!url.contains("allowPublicKeyRetrieval")) {
                    url = url + "&allowPublicKeyRetrieval=true";
                }
                if (!url.contains("serverTimezone")) {
                    url = url + "&serverTimezone=UTC";
                }
                if (!url.contains("characterEncoding")) {
                    url = url + "&characterEncoding=UTF-8";
                }
            }
        }
    }

    private static String getEnvOrProperty(String... keys) {
        for (String key : keys) {
            String val = System.getenv(key);
            if (val != null && !val.trim().isEmpty()) {
                return val;
            }
            val = System.getProperty(key);
            if (val != null && !val.trim().isEmpty()) {
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
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to database at: " + sanitizeUrl(url) 
                    + " [user=" + username + "]. Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Masks any password in the connection string to protect credentials in logs.
     */
    public static String sanitizeUrl(String rawUrl) {
        if (rawUrl == null) return "null";
        return rawUrl.replaceAll("(?i):[^/@:]+@", ":****@");
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

    private static volatile String lastConnectionError = "No connection attempted yet";

    /**
     * Helper to test whether the database connection is alive and working.
     * @return true if connection succeeds, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean ok = (conn != null && !conn.isClosed());
            if (ok) {
                lastConnectionError = "None (Connection active)";
            }
            return ok;
        } catch (SQLException e) {
            lastConnectionError = e.getMessage();
            LOGGER.log(Level.WARNING, "Connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves the last recorded database connection error.
     */
    public static String getLastConnectionError() {
        return lastConnectionError;
    }

    /**
     * Re-initializes database configuration from environment variables or properties.
     */
    public static void reloadConfiguration() {
        initConfiguration();
    }

    /**
     * Returns sanitized configured JDBC URL (without passwords).
     */
    public static String getSanitizedUrl() {
        return sanitizeUrl(url);
    }

    /**
     * Returns configured username.
     */
    public static String getUsername() {
        return username;
    }
}
