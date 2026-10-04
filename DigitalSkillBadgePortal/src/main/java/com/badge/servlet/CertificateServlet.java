package com.badge.servlet;

import com.badge.dao.BadgeDAO;
import com.badge.model.Badge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Serves the official printable / downloadable Digital Skill Certificate for a badge.
 */
@WebServlet(name = "CertificateServlet", urlPatterns = {"/certificate"})
public class CertificateServlet extends HttpServlet {

    private final BadgeDAO badgeDAO = new BadgeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        String idStr = request.getParameter("id");
        Badge badge = null;

        if (code != null && !code.trim().isEmpty()) {
            badge = badgeDAO.getBadgeByCode(code.trim());
        } else if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                badge = badgeDAO.getBadgeById(id);
            } catch (NumberFormatException ignored) {}
        }

        if (badge == null) {
            response.sendRedirect(request.getContextPath() + "/verify?error=notfound");
            return;
        }

        request.setAttribute("badge", badge);
        request.getRequestDispatcher("/certificate.jsp").forward(request, response);
    }
}
