package com.badge;

import com.badge.util.AppUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppUtilTest {

    @Test
    public void testVerificationUrlGeneration() {
        // When request is null and no APP_BASE_URL is set, falls back to default localhost:8080
        String url = AppUtil.getVerificationUrl(null, "CWX-GOLD-202601");
        assertNotNull(url);
        assertTrue("Verification URL should contain badge code", url.contains("CWX-GOLD-202601"));
        assertTrue("Verification URL should contain /verify?code=", url.contains("/verify?code="));
    }

    @Test
    public void testVerificationUrlWithNullCode() {
        String url = AppUtil.getVerificationUrl(null, null);
        assertNotNull(url);
        assertTrue(url.endsWith("/verify?code="));
    }
}
