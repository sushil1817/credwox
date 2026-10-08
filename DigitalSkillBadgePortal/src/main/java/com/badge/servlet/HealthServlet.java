package com.badge.servlet;

import com.badge.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Health check endpoint for Render and cloud container uptime monitoring.
 * Maps to /health and returns HTTP 200 with service and database status.
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/health"})
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/plain; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        boolean dbConnected = false;
        try {
            dbConnected = DBUtil.testConnection();
        } catch (Exception ignored) {
            // Safe fallback: DB error must never crash the health endpoint or container
        }

        PrintWriter out = response.getWriter();
        out.println("OK");
        out.println("Status: UP");
        out.println("Database: " + (dbConnected ? "CONNECTED" : "DISCONNECTED"));
        if (!dbConnected) {
            out.println("Database_Error: " + DBUtil.getLastConnectionError());
        }
        out.println("Configured_URL: " + DBUtil.getSanitizedUrl());
        out.println("Configured_User: " + DBUtil.getUsername());
        out.flush();
    }
}
