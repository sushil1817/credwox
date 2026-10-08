package com.badge.filter;

import com.badge.util.AppUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter that automatically populates the canonical appBaseUrl in the request attributes
 * for every incoming HTTP request so all JSPs and views have unified, reliable access.
 */
@WebFilter(filterName = "AppConfigFilter", urlPatterns = {"/*"})
public class AppConfigFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            req.setAttribute("appBaseUrl", AppUtil.getBaseUrl(req));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
