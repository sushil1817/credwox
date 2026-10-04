package com.badge.util;

import java.security.SecureRandom;
import java.time.Year;

/**
 * Utility to generate unique, verifiable credential tokens formatted for easy reading.
 * Example format: CWX-GLD-2026-X8P4 or CWX-2026-B7E2-9F1A
 */
public class CodeGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Removed ambiguous chars like 0, O, 1, I
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a unique verification code.
     * @param level Optional credential tier (e.g., Gold, Silver, Bronze)
     * @return Formatted token string, e.g. "CWX-GLD-2026-X8P4"
     */
    public static String generateBadgeCode(String level) {
        int currentYear = Year.now().getValue();
        String prefix = "CWX";
        if (level != null && !level.trim().isEmpty()) {
            String clean = level.trim().toUpperCase();
            if (clean.startsWith("G")) prefix = "CWX-GLD";
            else if (clean.startsWith("S")) prefix = "CWX-SLV";
            else if (clean.startsWith("B")) prefix = "CWX-BRZ";
        }
        
        String chunk1 = randomSegment(4);
        String chunk2 = randomSegment(4);
        return String.format("%s-%d-%s-%s", prefix, currentYear, chunk1, chunk2);
    }

    private static String randomSegment(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }
        return sb.toString();
    }
}
