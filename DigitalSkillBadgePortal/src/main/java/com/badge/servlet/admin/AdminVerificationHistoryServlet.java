package com.badge.servlet.admin;

import com.badge.dao.VerificationDAO;
import com.badge.model.VerificationRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Handles administrative viewing of public badge verification audit records.
 */
@WebServlet(name = "AdminVerificationHistoryServlet", urlPatterns = {"/admin/verifications"})
public class AdminVerificationHistoryServlet extends HttpServlet {

    private final VerificationDAO verificationDAO = new VerificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String limitStr = request.getParameter("limit");
        int limit = 100;
        if (limitStr != null) {
            try {
                limit = Integer.parseInt(limitStr.trim());
            } catch (NumberFormatException ignored) {}
        }

        List<VerificationRecord> verifications = verificationDAO.getRecentVerifications(limit);
        request.setAttribute("verifications", verifications);
        request.setAttribute("limit", limit);

        request.getRequestDispatcher("/admin/verifications.jsp").forward(request, response);
    }
}
