package com.badge.servlet;

import com.badge.dao.BadgeDAO;
import com.badge.dao.VerificationDAO;
import com.badge.model.Badge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Public Badge Verification Servlet:
 * Allows anyone (employers, recruiters, educational institutions) to publicly
 * verify any digital skill badge using its unique verification code without needing an account.
 * Every verification attempt is audited in verification_history table.
 */
@WebServlet(name = "VerifyServlet", urlPatterns = {"/verify"})
public class VerifyServlet extends HttpServlet {

    private final BadgeDAO badgeDAO = new BadgeDAO();
    private final VerificationDAO verificationDAO = new VerificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        request.setAttribute("appBaseUrl", com.badge.util.AppUtil.getBaseUrl(request));
        if (code != null && !code.trim().isEmpty()) {
            performVerification(request, response, code.trim());
        } else {
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("badgeCode");
        performVerification(request, response, code != null ? code.trim() : "");
    }

    private void performVerification(HttpServletRequest request, HttpServletResponse response, String code)
            throws ServletException, IOException {

        if (code.isEmpty()) {
            request.setAttribute("searched", true);
            request.setAttribute("resultType", "EMPTY_CODE");
            request.setAttribute("message", "Please enter a valid verification code.");
            request.getRequestDispatcher("/verify.jsp").forward(request, response);
            return;
        }

        String clientIp = extractClientIp(request);
        Badge badge = badgeDAO.getBadgeByCode(code);

        request.setAttribute("searched", true);
        request.setAttribute("searchedCode", code);
        request.setAttribute("appBaseUrl", com.badge.util.AppUtil.getBaseUrl(request));

        if (badge == null) {
            // Not found
            verificationDAO.recordVerification(code, "NOT_FOUND", clientIp);
            request.setAttribute("resultType", "NOT_FOUND");
            request.setAttribute("message", "No digital badge found with verification code \"" + code + "\". Please verify the code and try again.");
        } else {
            String effectiveStatus = badge.getEffectiveStatus();
            // Audit the successful lookup with the badge's status
            verificationDAO.recordVerification(badge.getBadgeCode(), effectiveStatus, clientIp);

            request.setAttribute("badge", badge);
            request.setAttribute("effectiveStatus", effectiveStatus);

            if ("ACTIVE".equalsIgnoreCase(effectiveStatus)) {
                request.setAttribute("resultType", "ACTIVE");
                request.setAttribute("message", "This digital skill badge is VALID and in good standing.");
            } else if ("EXPIRED".equalsIgnoreCase(effectiveStatus)) {
                request.setAttribute("resultType", "EXPIRED");
                request.setAttribute("message", "This badge was legitimately issued but has EXPIRED on " + badge.getExpiryDate() + ".");
            } else if ("REVOKED".equalsIgnoreCase(effectiveStatus)) {
                request.setAttribute("resultType", "REVOKED");
                request.setAttribute("message", "This badge has been officially REVOKED by the issuing authority.");
            }
        }

        request.getRequestDispatcher("/verify.jsp").forward(request, response);
    }

    /**
     * Extracts client IP addressing proxies and load balancers.
     */
    private String extractClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isEmpty() && !"unknown".equalsIgnoreCase(xf)) {
            return xf.split(",")[0].trim();
        }
        String ip = request.getRemoteAddr();
        return (ip != null) ? ip : "127.0.0.1";
    }
}
