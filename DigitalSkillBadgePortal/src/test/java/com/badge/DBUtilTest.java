package com.badge;

import com.badge.util.DBUtil;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class DBUtilTest {

    @After
    public void cleanupProperties() {
        System.clearProperty("DB_URL");
        System.clearProperty("DB_USERNAME");
        System.clearProperty("DB_PASSWORD");
        DBUtil.reloadConfiguration();
    }

    @Test
    public void testSanitizeUrlMasksPassword() {
        String raw = "mysql://root:secretPassword123@roundhouse.proxy.rlwy.net:3306/railway";
        String sanitized = DBUtil.sanitizeUrl(raw);
        assertFalse("Sanitized URL must not contain plain password", sanitized.contains("secretPassword123"));
        assertTrue("Sanitized URL should contain masked token", sanitized.contains("****"));
    }

    @Test
    public void testSanitizeUrlHandlesNull() {
        assertEquals("null", DBUtil.sanitizeUrl(null));
    }

    @Test
    public void testRailwayMysqlUrlNormalization() {
        System.setProperty("DB_URL", "mysql://myuser:mypassword@roundhouse.proxy.rlwy.net:3306/railway");
        DBUtil.reloadConfiguration();

        String configuredUrl = DBUtil.getSanitizedUrl();
        assertTrue("Normalized URL must start with jdbc:mysql://", configuredUrl.startsWith("jdbc:mysql://"));
        assertTrue("Must contain host", configuredUrl.contains("roundhouse.proxy.rlwy.net:3306/railway"));
        assertTrue("Must include allowPublicKeyRetrieval", configuredUrl.contains("allowPublicKeyRetrieval=true"));
        assertTrue("Must include serverTimezone", configuredUrl.contains("serverTimezone=UTC"));
        assertEquals("User should be extracted from URI", "myuser", DBUtil.getUsername());
    }

    @Test
    public void testStandardJdbcUrlNormalization() {
        System.setProperty("DB_URL", "jdbc:mysql://autorack.proxy.rlwy.net:12345/badge_portal");
        System.setProperty("DB_USERNAME", "adminuser");
        DBUtil.reloadConfiguration();

        String configuredUrl = DBUtil.getSanitizedUrl();
        assertTrue(configuredUrl.startsWith("jdbc:mysql://autorack.proxy.rlwy.net:12345/badge_portal"));
        assertTrue(configuredUrl.contains("allowPublicKeyRetrieval=true"));
        assertEquals("adminuser", DBUtil.getUsername());
    }
}
