package com.badge.servlet;

import com.badge.dao.StudentDAO;
import com.badge.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Handles student login requests with validation and session creation.
 */
@WebServlet(name = "StudentLoginServlet", urlPatterns = {"/student/login"})
public class StudentLoginServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("studentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        if (identifier == null || identifier.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please provide both Student ID / Email and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        Student student = studentDAO.authenticate(identifier, password);
        if (student != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("studentUser", student);
            session.setAttribute("role", "STUDENT");
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } else {
            String dbErr = studentDAO.getLastError();
            if (dbErr != null) {
                request.setAttribute("errorMessage", "Database error: " + dbErr);
            } else {
                request.setAttribute("errorMessage", "Invalid Student ID / Email or password. Please try again.");
            }
            request.setAttribute("prevIdentifier", identifier);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
