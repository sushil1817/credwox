package com.badge.servlet;

import com.badge.dao.BadgeDAO;
import com.badge.model.Badge;
import com.badge.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serves the Student Badges page where students view their earned badges,
 * filter by level or status, inspect verification codes, and trigger certificate views.
 */
@WebServlet(name = "StudentBadgesServlet", urlPatterns = {"/student/badges"})
public class StudentBadgesServlet extends HttpServlet {

    private final BadgeDAO badgeDAO = new BadgeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("studentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Student student = (Student) session.getAttribute("studentUser");
        List<Badge> allBadges = badgeDAO.getBadgesByStudentId(student.getId());

        String levelFilter = request.getParameter("level");
        String statusFilter = request.getParameter("status");

        List<Badge> filteredBadges = new ArrayList<>();
        for (Badge b : allBadges) {
            boolean matchesLevel = (levelFilter == null || levelFilter.isEmpty() || "ALL".equalsIgnoreCase(levelFilter) ||
                                    b.getBadgeLevel().equalsIgnoreCase(levelFilter));
            
            String effStatus = b.getEffectiveStatus();
            boolean matchesStatus = (statusFilter == null || statusFilter.isEmpty() || "ALL".equalsIgnoreCase(statusFilter) ||
                                     effStatus.equalsIgnoreCase(statusFilter));

            if (matchesLevel && matchesStatus) {
                filteredBadges.add(b);
            }
        }

        request.setAttribute("badges", filteredBadges);
        request.setAttribute("totalCount", allBadges.size());
        request.setAttribute("selectedLevel", levelFilter != null ? levelFilter : "ALL");
        request.setAttribute("selectedStatus", statusFilter != null ? statusFilter : "ALL");

        request.getRequestDispatcher("/badges.jsp").forward(request, response);
    }
}
