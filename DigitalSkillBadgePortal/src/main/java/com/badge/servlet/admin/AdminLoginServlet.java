package com.badge.servlet.admin;

import com.badge.dao.AdminDAO;
import com.badge.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles administrator authentication.
 */
@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/login"})
public class AdminLoginServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminUser") != null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
            return;
        }

        Admin admin = adminDAO.authenticate(username.trim(), password);
        if (admin != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("adminUser", admin);
            session.setAttribute("role", "ADMIN");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            String dbErr = adminDAO.getLastError();
            if (dbErr != null) {
                request.setAttribute("errorMessage", "Database error: " + dbErr);
            } else {
                request.setAttribute("errorMessage", "Invalid administrator credentials.");
            }
            request.setAttribute("prevUsername", username);
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
        }
    }
}
