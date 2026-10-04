<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Student Login — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container" style="max-width: 480px;">

        <div class="card" style="padding: 36px 32px;">
            <div style="text-align: center; margin-bottom: 28px;">
                <div class="nav-logo-icon" style="margin: 0 auto 16px; width: 48px; height: 48px;">&#127891;</div>
                <h1 style="font-size: 1.8rem; margin-bottom: 6px;">Student Login</h1>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">Sign in to view your earned skill badges &amp; credentials</p>
            </div>

            <!-- Alerts -->
            <c:if test="${param.registered eq 'true'}">
                <div class="alert alert-success alert-auto-dismiss">
                    <span>Account created successfully! You can now log in.</span>
                </div>
            </c:if>

            <c:if test="${param.logout eq 'true'}">
                <div class="alert alert-success alert-auto-dismiss">
                    <span>You have been logged out securely.</span>
                </div>
            </c:if>

            <c:if test="${param.error eq 'unauthorized'}">
                <div class="alert alert-warning">
                    <span>Please log in to access this page.</span>
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/student/login" method="POST">
                <div class="form-group">
                    <label class="form-label" for="identifier">Student ID or Email</label>
                    <input type="text" id="identifier" name="identifier" class="form-control" 
                           placeholder="e.g. 25WU0101142 or student@woxsen.edu.in" 
                           value="${prevIdentifier}" required autofocus>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" 
                           placeholder="Enter your password" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">
                    Sign In to Student Dashboard &rarr;
                </button>
            </form>

            <div style="margin-top: 24px; text-align: center; font-size: 0.88rem; color: var(--text-secondary); border-top: 1px solid var(--border-color); padding-top: 18px;">
                Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" style="font-weight: 600;">Register here</a>
            </div>

            <!-- Demo Credentials Helper for Examiners/Viva -->
            <div style="margin-top: 20px; background: rgba(0, 0, 0, 0.3); border: 1px dashed var(--border-color); border-radius: var(--radius-sm); padding: 12px 14px; font-size: 0.82rem; color: var(--text-muted);">
                <div style="font-weight: 700; color: var(--text-secondary); margin-bottom: 4px;">Sample Student Login:</div>
                <div>ID: <strong style="color: var(--primary-light);">25WU0101142</strong> | Password: <strong style="color: var(--primary-light);">student123</strong></div>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

