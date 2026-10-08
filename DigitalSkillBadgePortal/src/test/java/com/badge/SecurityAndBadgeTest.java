package com.badge;

import com.badge.model.Badge;
import com.badge.util.PasswordUtil;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class SecurityAndBadgeTest {

    @Test
    public void testAdminPasswordHashIntegrity() {
        String adminHash = PasswordUtil.hashPassword("admin123");
        assertEquals("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9", adminHash);
        assertTrue(PasswordUtil.verifyPassword("admin123", adminHash));
        assertFalse(PasswordUtil.verifyPassword("wrongpass", adminHash));
    }

    @Test
    public void testStudentPasswordHashIntegrity() {
        String studentHash = PasswordUtil.hashPassword("student123");
        assertEquals("703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b", studentHash);
        assertTrue(PasswordUtil.verifyPassword("student123", studentHash));
        assertFalse(PasswordUtil.verifyPassword("wrongpass", studentHash));
    }

    @Test
    public void testBadgeEffectiveStatusRevoked() {
        Badge badge = new Badge();
        badge.setStatus("REVOKED");
        badge.setExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
        assertEquals("REVOKED", badge.getEffectiveStatus());
    }

    @Test
    public void testBadgeEffectiveStatusExpired() {
        Badge badge = new Badge();
        badge.setStatus("ACTIVE");
        badge.setExpiryDate(Date.valueOf(LocalDate.now().minusDays(5)));
        assertEquals("EXPIRED", badge.getEffectiveStatus());
    }

    @Test
    public void testBadgeEffectiveStatusActive() {
        Badge badge = new Badge();
        badge.setStatus("ACTIVE");
        badge.setExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
        assertEquals("ACTIVE", badge.getEffectiveStatus());
    }
}
