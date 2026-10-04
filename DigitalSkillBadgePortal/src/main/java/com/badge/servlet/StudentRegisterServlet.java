package com.badge.servlet;

import com.badge.dao.StudentDAO;
import com.badge.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles student registration and input validation.
 */
@WebServlet(name = "StudentRegisterServlet", urlPatterns = {"/student/register"})
public class StudentRegisterServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String studentId = request.getParameter("studentId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String course = request.getParameter("course");

        // Basic validations
        if (studentId == null || studentId.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            course == null || course.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required.");
            repopulate(request, studentId, name, email, course);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            repopulate(request, studentId, name, email, course);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            repopulate(request, studentId, name, email, course);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (studentDAO.studentIdExists(studentId)) {
            request.setAttribute("errorMessage", "A student with this Student ID (" + studentId + ") already exists.");
            repopulate(request, studentId, name, email, course);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (studentDAO.emailExists(email)) {
            request.setAttribute("errorMessage", "A student with this Email (" + email + ") already exists.");
            repopulate(request, studentId, name, email, course);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        Student newStudent = new Student();
        newStudent.setStudentId(studentId.trim().toUpperCase());
        newStudent.setName(name.trim());
        newStudent.setEmail(email.trim().toLowerCase());
        newStudent.setPassword(password);
        newStudent.setCourse(course.trim());

        boolean success = studentDAO.register(newStudent);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
        } else {
            request.setAttribute("errorMessage", "An error occurred while creating your account. Please try again.");
            repopulate(request, studentId, name, email, course);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void repopulate(HttpServletRequest request, String studentId, String name, String email, String course) {
        request.setAttribute("prevStudentId", studentId);
        request.setAttribute("prevName", name);
        request.setAttribute("prevEmail", email);
        request.setAttribute("prevCourse", course);
    }
}
