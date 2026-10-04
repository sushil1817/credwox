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
 * Handles student profile view and updates (name, course, and password).
 */
@WebServlet(name = "StudentProfileServlet", urlPatterns = {"/student/profile"})
public class StudentProfileServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("studentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Student sessionStudent = (Student) session.getAttribute("studentUser");
        Student student = studentDAO.findById(sessionStudent.getId());
        session.setAttribute("studentUser", student);

        request.setAttribute("student", student);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("studentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Student student = (Student) session.getAttribute("studentUser");
        String action = request.getParameter("action");

        if ("updateInfo".equalsIgnoreCase(action)) {
            String name = request.getParameter("name");
            String course = request.getParameter("course");

            if (name == null || name.trim().isEmpty() || course == null || course.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Name and Course cannot be empty.");
            } else {
                boolean success = studentDAO.updateProfile(student.getId(), name, course);
                if (success) {
                    student.setName(name.trim());
                    student.setCourse(course.trim());
                    session.setAttribute("studentUser", student);
                    request.setAttribute("successMessage", "Profile information updated successfully.");
                } else {
                    request.setAttribute("errorMessage", "Failed to update profile information.");
                }
            }
        } else if ("changePassword".equalsIgnoreCase(action)) {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            if (currentPassword == null || newPassword == null || confirmNewPassword == null ||
                currentPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
                request.setAttribute("passwordError", "All password fields are required.");
            } else if (!newPassword.equals(confirmNewPassword)) {
                request.setAttribute("passwordError", "New passwords do not match.");
            } else if (newPassword.length() < 6) {
                request.setAttribute("passwordError", "New password must be at least 6 characters.");
            } else {
                // Verify current password
                Student authenticated = studentDAO.authenticate(student.getEmail(), currentPassword);
                if (authenticated == null) {
                    request.setAttribute("passwordError", "Current password is incorrect.");
                } else {
                    boolean success = studentDAO.updatePassword(student.getId(), newPassword);
                    if (success) {
                        request.setAttribute("passwordSuccess", "Password updated successfully.");
                    } else {
                        request.setAttribute("passwordError", "Failed to update password.");
                    }
                }
            }
        }

        Student refreshed = studentDAO.findById(student.getId());
        request.setAttribute("student", refreshed);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
}
