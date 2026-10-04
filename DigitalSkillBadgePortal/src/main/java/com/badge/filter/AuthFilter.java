package com.badge.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication and Security Filter:
 * 1. Restricts access to /student/* routes to logged-in students.
 * 2. Restricts access to /admin/* routes to logged-in administrators (except /admin/login).
 * 3. Enforces HTTP caching prevention on authenticated pages.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/student/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        // Prevent browser caching on secured pages
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);

        // 1. Admin route protection
        if (path.startsWith("/admin")) {
            // Allow admin login page and its authentication endpoint
            if (path.equals("/admin/login") || path.equals("/admin/login.jsp")) {
                chain.doFilter(request, response);
                return;
            }

            boolean isAdminLoggedIn = (session != null && session.getAttribute("adminUser") != null);
            if (!isAdminLoggedIn) {
                res.sendRedirect(contextPath + "/admin/login?error=unauthorized");
                return;
            }
        }

        // 2. Student route protection
        if (path.startsWith("/student")) {
            // Allow student login and register
            if (path.equals("/student/login") || path.equals("/student/register") ||
                path.equals("/student/login.jsp") || path.equals("/student/register.jsp")) {
                chain.doFilter(request, response);
                return;
            }

            boolean isStudentLoggedIn = (session != null && session.getAttribute("studentUser") != null);
            if (!isStudentLoggedIn) {
                res.sendRedirect(contextPath + "/login.jsp?error=unauthorized");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
