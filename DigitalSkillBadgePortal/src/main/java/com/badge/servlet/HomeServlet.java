package com.badge.servlet;

import com.badge.dao.AdminDAO;
import com.badge.dao.ModuleDAO;
import com.badge.model.SkillModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Serves the public homepage showcasing portal capabilities and quick verification.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();
    private final ModuleDAO moduleDAO = new ModuleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Pass summary statistics and featured skill modules to landing page
        Map<String, Integer> stats = adminDAO.getDashboardStats();
        List<SkillModule> modules = moduleDAO.getAllModules();

        request.setAttribute("stats", stats);
        request.setAttribute("modules", modules);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
