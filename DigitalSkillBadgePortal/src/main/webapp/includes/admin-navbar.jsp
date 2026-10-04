<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="navbar" style="border-bottom: 2px solid rgba(99, 102, 241, 0.4);">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-brand">
            <div class="nav-logo-icon" style="background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);">CWX</div>
            <span>CredWox &bull; Faculty Console</span>
        </a>

        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/students" class="nav-link">Students</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/modules" class="nav-link">Modules</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/issue-badge" class="nav-link">Issue Credential</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/badges" class="nav-link">Credentials</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/verifications" class="nav-link">Audit History</a></li>
        </ul>

        <div class="nav-user">
            <a href="${pageContext.request.contextPath}/verify" class="btn btn-outline btn-sm" target="_blank">Public Verify &nearr;</a>
            <span class="user-badge" style="background: rgba(124, 58, 237, 0.2); border-color: rgba(124, 58, 237, 0.4);">${sessionScope.adminUser.username}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout</a>
        </div>
    </div>
</header>

