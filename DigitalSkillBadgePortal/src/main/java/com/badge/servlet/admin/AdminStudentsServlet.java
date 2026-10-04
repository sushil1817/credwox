package com.badge.servlet.admin;

import com.badge.dao.StudentDAO;
import com.badge.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles administrative viewing of all registered students and their badge performance.
 */
@WebServlet(name = "AdminStudentsServlet", urlPatterns = {"/admin/students"})
public class AdminStudentsServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> students = studentDAO.getAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("/admin/students.jsp").forward(request, response);
    }
}
