<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="navbar">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/" class="nav-brand">
            <div class="nav-logo-icon">CWX</div>
            <span>CredWox</span>
        </a>

        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/verify" class="nav-link">Verify Credential</a></li>
            
            <c:choose>
                <c:when test="${not empty sessionScope.studentUser}">
                    <li><a href="${pageContext.request.contextPath}/student/dashboard" class="nav-link">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/student/badges" class="nav-link">My Credentials</a></li>
                    <li><a href="${pageContext.request.contextPath}/student/profile" class="nav-link">Profile</a></li>
                </c:when>
                <c:when test="${not empty sessionScope.adminUser}">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Faculty Console</a></li>
                </c:when>
            </c:choose>
        </ul>

        <div class="nav-user">
            <c:choose>
                <c:when test="${not empty sessionScope.studentUser}">
                    <span class="user-badge">${sessionScope.studentUser.name} (${sessionScope.studentUser.studentId})</span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout</a>
                </c:when>
                <c:when test="${not empty sessionScope.adminUser}">
                    <span class="user-badge">Admin: ${sessionScope.adminUser.username}</span>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline btn-sm">Console</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline btn-sm">Student Login</a>
                    <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary btn-sm">Register</a>
                    <a href="${pageContext.request.contextPath}/admin/login" class="btn btn-secondary btn-sm" title="Faculty/Admin Portal">Admin</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

