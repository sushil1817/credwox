package com.badge.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles user logout by invalidating the HTTP session and redirecting.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processLogout(request, response);
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        boolean isAdmin = false;
        if (session != null) {
            if (session.getAttribute("adminUser") != null) {
                isAdmin = true;
            }
            session.invalidate();
        }

        if (isAdmin) {
            response.sendRedirect(request.getContextPath() + "/admin/login?logout=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?logout=true");
        }
    }
}
