package com.badge.servlet.admin;

import com.badge.dao.BadgeDAO;
import com.badge.dao.ModuleDAO;
import com.badge.dao.StudentDAO;
import com.badge.model.Admin;
import com.badge.model.Badge;
import com.badge.model.SkillModule;
import com.badge.model.Student;
import com.badge.util.CodeGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles the creation and issuance of digital skill badges to students.
 */
@WebServlet(name = "AdminIssueBadgeServlet", urlPatterns = {"/admin/issue-badge"})
public class AdminIssueBadgeServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();
    private final ModuleDAO moduleDAO = new ModuleDAO();
    private final BadgeDAO badgeDAO = new BadgeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> students = studentDAO.getAllStudents();
        List<SkillModule> modules = moduleDAO.getAllModules();

        // Generate suggested badge code
        String suggestedCode = CodeGenerator.generateBadgeCode("Gold");
        LocalDate today = LocalDate.now();
        LocalDate defaultExpiry = today.plusYears(2);

        request.setAttribute("students", students);
        request.setAttribute("modules", modules);
        request.setAttribute("suggestedCode", suggestedCode);
        request.setAttribute("todayDate", today.toString());
        request.setAttribute("defaultExpiry", defaultExpiry.toString());

        request.getRequestDispatcher("/admin/issue-badge.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Admin admin = (Admin) (session != null ? session.getAttribute("adminUser") : null);
        String issuedBy = (admin != null && admin.getFullName() != null) ? admin.getFullName() : "Administrator";

        String studentIdStr = request.getParameter("studentId");
        String moduleIdStr = request.getParameter("moduleId");
        String badgeLevel = request.getParameter("badgeLevel");
        String badgeCode = request.getParameter("badgeCode");
        String issueDateStr = request.getParameter("issueDate");
        String expiryDateStr = request.getParameter("expiryDate");
        String status = request.getParameter("status");

        if (studentIdStr == null || moduleIdStr == null || badgeLevel == null ||
            studentIdStr.isEmpty() || moduleIdStr.isEmpty() || badgeLevel.isEmpty()) {
            request.setAttribute("errorMessage", "Please select student, module, and badge level.");
            doGet(request, response);
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);
            int moduleId = Integer.parseInt(moduleIdStr);

            if (badgeCode == null || badgeCode.trim().isEmpty()) {
                badgeCode = CodeGenerator.generateBadgeCode(badgeLevel);
            } else {
                badgeCode = badgeCode.trim().toUpperCase();
            }

            if (badgeDAO.badgeCodeExists(badgeCode)) {
                request.setAttribute("errorMessage", "A badge with code '" + badgeCode + "' already exists.");
                doGet(request, response);
                return;
            }

            Date issueDate = (issueDateStr != null && !issueDateStr.trim().isEmpty()) 
                ? Date.valueOf(issueDateStr.trim()) 
                : Date.valueOf(LocalDate.now());

            Date expiryDate = null;
            if (expiryDateStr != null && !expiryDateStr.trim().isEmpty()) {
                expiryDate = Date.valueOf(expiryDateStr.trim());
                if (expiryDate.before(issueDate)) {
                    request.setAttribute("errorMessage", "Expiry date cannot be earlier than issue date.");
                    doGet(request, response);
                    return;
                }
            }

            Badge badge = new Badge();
            badge.setBadgeCode(badgeCode);
            badge.setStudentId(studentId);
            badge.setModuleId(moduleId);
            badge.setBadgeLevel(badgeLevel);
            badge.setIssueDate(issueDate);
            badge.setExpiryDate(expiryDate);
            badge.setStatus(status != null && !status.isEmpty() ? status : "ACTIVE");
            badge.setIssuedBy(issuedBy);

            boolean ok = badgeDAO.issueBadge(badge);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/admin/badges?success=issued&code=" + badgeCode);
            } else {
                request.setAttribute("errorMessage", "Failed to issue badge. Database error.");
                doGet(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid form input: " + e.getMessage());
            doGet(request, response);
        }
    }
}
