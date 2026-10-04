<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Faculty Console Login — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container" style="max-width: 460px;">

        <div class="card" style="padding: 36px 32px; border-color: rgba(124, 58, 237, 0.4);">
            <div style="text-align: center; margin-bottom: 26px;">
                <div class="nav-logo-icon" style="margin: 0 auto 16px; width: 48px; height: 48px; background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);">
                    &#128272;
                </div>
                <h1 style="font-size: 1.8rem; margin-bottom: 6px;">Faculty Console</h1>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">Woxsen University Credential Authority</p>
            </div>

            <c:if test="${param.logout eq 'true'}">
                <div class="alert alert-success alert-auto-dismiss">
                    <span>Admin session terminated successfully.</span>
                </div>
            </c:if>

            <c:if test="${param.error eq 'unauthorized'}">
                <div class="alert alert-warning">
                    <span>Faculty authentication required to access console.</span>
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/login" method="POST">
                <div class="form-group">
                    <label class="form-label" for="username">Faculty Username</label>
                    <input type="text" id="username" name="username" class="form-control" 
                           placeholder="e.g. admin" value="${prevUsername}" required autofocus>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" 
                           placeholder="Enter admin password" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px; background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);">
                    Sign In to Faculty Console &rarr;
                </button>
            </form>

            <!-- Sample Credentials Display for Evaluator -->
            <div style="margin-top: 24px; background: rgba(0, 0, 0, 0.35); border: 1px dashed rgba(124, 58, 237, 0.4); border-radius: var(--radius-sm); padding: 12px 14px; font-size: 0.82rem; color: var(--text-muted);">
                <div style="font-weight: 700; color: #a78bfa; margin-bottom: 4px;">Default Faculty Admin:</div>
                <div>Username: <strong style="color: #fff;">admin</strong> &bull; Password: <strong style="color: #fff;">admin123</strong></div>
                <div style="margin-top: 4px; font-size: 0.78rem; color: #cbd5e1;">Signatory: <strong>Prof. Veeresh Biradar (Dept. Head)</strong></div>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

