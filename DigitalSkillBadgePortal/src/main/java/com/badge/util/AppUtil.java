package com.badge.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility for application-level configurations such as base URL resolution for
 * public verification links and dynamic QR code generation.
 */
public class AppUtil {

    private static String configuredBaseUrl = null;

    static {
        String envUrl = System.getenv("APP_BASE_URL");
        if (envUrl == null || envUrl.trim().isEmpty()) {
            envUrl = System.getProperty("APP_BASE_URL");
        }
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            String trimmed = envUrl.trim();
            while (trimmed.endsWith("/")) {
                trimmed = trimmed.substring(0, trimmed.length() - 1);
            }
            configuredBaseUrl = trimmed;
        }
    }

    /**
     * Resolves the canonical base URL of the application.
     * Priority:
     * 1. APP_BASE_URL environment variable or system property (e.g., https://credwox.onrender.com)
     * 2. Reverse proxy headers (X-Forwarded-Proto and X-Forwarded-Host)
     * 3. Request scheme, server name, and port
     * 4. Fallback default
     */
    public static String getBaseUrl(HttpServletRequest request) {
        if (configuredBaseUrl != null && !configuredBaseUrl.isEmpty()) {
            return configuredBaseUrl;
        }

        if (request != null) {
            String forwardedProto = request.getHeader("X-Forwarded-Proto");
            String forwardedHost = request.getHeader("X-Forwarded-Host");

            if (forwardedProto != null && !forwardedProto.trim().isEmpty() 
                    && forwardedHost != null && !forwardedHost.trim().isEmpty()) {
                String proto = forwardedProto.split(",")[0].trim();
                String host = forwardedHost.split(",")[0].trim();
                String contextPath = request.getContextPath();
                if (contextPath == null) contextPath = "";
                return proto + "://" + host + contextPath;
            }

            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String contextPath = request.getContextPath();
            if (contextPath == null) contextPath = "";

            StringBuilder sb = new StringBuilder();
            sb.append(scheme).append("://").append(serverName);
            if (("http".equalsIgnoreCase(scheme) && serverPort != 80)
                    || ("https".equalsIgnoreCase(scheme) && serverPort != 443)) {
                sb.append(":").append(serverPort);
            }
            sb.append(contextPath);
            return sb.toString();
        }

        return "http://localhost:8080";
    }

    /**
     * Computes the absolute public verification URL for a given badge code.
     */
    public static String getVerificationUrl(HttpServletRequest request, String badgeCode) {
        String base = getBaseUrl(request);
        String code = (badgeCode != null) ? badgeCode.trim() : "";
        return base + "/verify?code=" + code;
    }
}
