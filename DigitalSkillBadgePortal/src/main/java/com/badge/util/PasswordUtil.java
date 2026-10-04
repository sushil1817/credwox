package com.badge.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing and verifying passwords securely using SHA-256.
 */
public class PasswordUtil {

    /**
     * Hashes a plain-text password using SHA-256 algorithm.
     * @param plainPassword the user's raw password
     * @return hex-encoded SHA-256 hash string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available in environment", e);
        }
    }

    /**
     * Verifies if a given plain password matches the stored SHA-256 hash.
     * @param plainPassword raw password entered by user
     * @param hashedPassword stored SHA-256 hash from database
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        String computedHash = hashPassword(plainPassword);
        return computedHash.equalsIgnoreCase(hashedPassword);
    }
}
