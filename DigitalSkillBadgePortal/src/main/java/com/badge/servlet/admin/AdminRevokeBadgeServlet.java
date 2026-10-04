package com.badge.servlet.admin;

import com.badge.dao.BadgeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles badge revocation, reactivation, and deletion actions initiated by administrators.
 */
@WebServlet(name = "AdminRevokeBadgeServlet", urlPatterns = {"/admin/revoke-badge"})
public class AdminRevokeBadgeServlet extends HttpServlet {

    private final BadgeDAO badgeDAO = new BadgeDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String badgeIdStr = request.getParameter("badgeId");

        if (badgeIdStr == null || badgeIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/badges?error=missing_id");
            return;
        }

        try {
            int badgeId = Integer.parseInt(badgeIdStr.trim());

            if ("revoke".equalsIgnoreCase(action)) {
                String reason = request.getParameter("reason");
                boolean ok = badgeDAO.revokeBadge(badgeId, reason);
                if (ok) {
                    response.sendRedirect(request.getContextPath() + "/admin/badges?success=revoked");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/badges?error=revoke_failed");
                }
            } else if ("reactivate".equalsIgnoreCase(action)) {
                boolean ok = badgeDAO.reactivateBadge(badgeId);
                if (ok) {
                    response.sendRedirect(request.getContextPath() + "/admin/badges?success=reactivated");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/badges?error=reactivate_failed");
                }
            } else if ("delete".equalsIgnoreCase(action)) {
                boolean ok = badgeDAO.deleteBadge(badgeId);
                if (ok) {
                    response.sendRedirect(request.getContextPath() + "/admin/badges?success=deleted");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/badges?error=delete_failed");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/badges");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/badges?error=invalid_id");
        }
    }
}
