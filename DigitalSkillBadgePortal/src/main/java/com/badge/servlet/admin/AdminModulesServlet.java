package com.badge.servlet.admin;

import com.badge.dao.ModuleDAO;
import com.badge.model.SkillModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles Skill Module curriculum CRUD administration.
 */
@WebServlet(name = "AdminModulesServlet", urlPatterns = {"/admin/modules"})
public class AdminModulesServlet extends HttpServlet {

    private final ModuleDAO moduleDAO = new ModuleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<SkillModule> modules = moduleDAO.getAllModules();
        request.setAttribute("modules", modules);
        request.getRequestDispatcher("/admin/modules.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            String moduleCode = request.getParameter("moduleCode");
            String moduleName = request.getParameter("moduleName");
            String description = request.getParameter("description");

            if (moduleCode == null || moduleCode.trim().isEmpty() || moduleName == null || moduleName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Module Code and Module Name are required.");
            } else if (moduleDAO.moduleCodeExists(moduleCode, 0)) {
                request.setAttribute("errorMessage", "Module Code '" + moduleCode + "' already exists.");
            } else {
                SkillModule m = new SkillModule();
                m.setModuleCode(moduleCode);
                m.setModuleName(moduleName);
                m.setDescription(description);
                boolean ok = moduleDAO.addModule(m);
                if (ok) {
                    response.sendRedirect(request.getContextPath() + "/admin/modules?success=added");
                    return;
                } else {
                    request.setAttribute("errorMessage", "Failed to add module.");
                }
            }
        } else if ("update".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String moduleCode = request.getParameter("moduleCode");
                String moduleName = request.getParameter("moduleName");
                String description = request.getParameter("description");

                if (moduleDAO.moduleCodeExists(moduleCode, id)) {
                    request.setAttribute("errorMessage", "Another module already uses code '" + moduleCode + "'.");
                } else {
                    SkillModule m = new SkillModule();
                    m.setId(id);
                    m.setModuleCode(moduleCode);
                    m.setModuleName(moduleName);
                    m.setDescription(description);
                    boolean ok = moduleDAO.updateModule(m);
                    if (ok) {
                        response.sendRedirect(request.getContextPath() + "/admin/modules?success=updated");
                        return;
                    } else {
                        request.setAttribute("errorMessage", "Failed to update module.");
                    }
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Invalid module update request.");
            }
        } else if ("delete".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = moduleDAO.deleteModule(id);
                if (ok) {
                    response.sendRedirect(request.getContextPath() + "/admin/modules?success=deleted");
                    return;
                } else {
                    request.setAttribute("errorMessage", "Cannot delete module because badges have been issued under it.");
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Invalid delete request.");
            }
        }

        List<SkillModule> modules = moduleDAO.getAllModules();
        request.setAttribute("modules", modules);
        request.getRequestDispatcher("/admin/modules.jsp").forward(request, response);
    }
}
