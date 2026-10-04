package com.badge.servlet.admin;

import com.badge.dao.BadgeDAO;
import com.badge.model.Badge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles administrative viewing, searching, and filtering of all issued badges.
 */
@WebServlet(name = "AdminBadgesServlet", urlPatterns = {"/admin/badges"})
public class AdminBadgesServlet extends HttpServlet {

    private final BadgeDAO badgeDAO = new BadgeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String level = request.getParameter("level");

        List<Badge> badges = badgeDAO.getAllBadges(search, status, level);

        request.setAttribute("badges", badges);
        request.setAttribute("searchParam", search != null ? search : "");
        request.setAttribute("statusParam", status != null ? status : "ALL");
        request.setAttribute("levelParam", level != null ? level : "ALL");

        request.getRequestDispatcher("/admin/badges.jsp").forward(request, response);
    }
}
