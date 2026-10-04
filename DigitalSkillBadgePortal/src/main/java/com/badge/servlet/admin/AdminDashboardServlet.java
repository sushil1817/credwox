package com.badge.servlet.admin;

import com.badge.dao.AdminDAO;
import com.badge.dao.BadgeDAO;
import com.badge.dao.VerificationDAO;
import com.badge.model.Badge;
import com.badge.model.VerificationRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Serves the primary Administrator Dashboard with real-time counters,
 * recently issued badges, and recent verification audits.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();
    private final BadgeDAO badgeDAO = new BadgeDAO();
    private final VerificationDAO verificationDAO = new VerificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Integer> stats = adminDAO.getDashboardStats();
        List<Badge> recentBadges = badgeDAO.getAllBadges(null, null, null);
        if (recentBadges.size() > 5) {
            recentBadges = recentBadges.subList(0, 5);
        }

        List<VerificationRecord> recentVerifications = verificationDAO.getRecentVerifications(5);

        request.setAttribute("stats", stats);
        request.setAttribute("recentBadges", recentBadges);
        request.setAttribute("recentVerifications", recentVerifications);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
