package com.badge.servlet;

import com.badge.dao.BadgeDAO;
import com.badge.dao.StudentDAO;
import com.badge.model.Badge;
import com.badge.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Serves the Student Dashboard showing badge summary stats and recent awards.
 */
@WebServlet(name = "StudentDashboardServlet", urlPatterns = {"/student/dashboard"})
public class StudentDashboardServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();
    private final BadgeDAO badgeDAO = new BadgeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("studentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Student sessionStudent = (Student) session.getAttribute("studentUser");
        // Reload fresh student details from DB
        Student student = studentDAO.findById(sessionStudent.getId());
        if (student == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        session.setAttribute("studentUser", student);

        List<Badge> badges = badgeDAO.getBadgesByStudentId(student.getId());

        int totalBadges = badges.size();
        int activeBadges = 0;
        int expiredBadges = 0;
        int revokedBadges = 0;
        int goldBadges = 0;
        int silverBadges = 0;
        int bronzeBadges = 0;

        for (Badge b : badges) {
            String effStatus = b.getEffectiveStatus();
            if ("ACTIVE".equalsIgnoreCase(effStatus)) activeBadges++;
            else if ("EXPIRED".equalsIgnoreCase(effStatus)) expiredBadges++;
            else if ("REVOKED".equalsIgnoreCase(effStatus)) revokedBadges++;

            if ("Gold".equalsIgnoreCase(b.getBadgeLevel())) goldBadges++;
            else if ("Silver".equalsIgnoreCase(b.getBadgeLevel())) silverBadges++;
            else if ("Bronze".equalsIgnoreCase(b.getBadgeLevel())) bronzeBadges++;
        }

        request.setAttribute("badges", badges);
        request.setAttribute("totalBadges", totalBadges);
        request.setAttribute("activeBadges", activeBadges);
        request.setAttribute("expiredBadges", expiredBadges);
        request.setAttribute("revokedBadges", revokedBadges);
        request.setAttribute("goldBadges", goldBadges);
        request.setAttribute("silverBadges", silverBadges);
        request.setAttribute("bronzeBadges", bronzeBadges);

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
