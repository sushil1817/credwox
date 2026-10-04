<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Profile — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container" style="max-width: 800px;">

        <div style="margin-bottom: 28px;">
            <h1 style="font-size: 2.2rem; margin-bottom: 6px;">Student Profile</h1>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">Manage your academic registration information and security settings</p>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>${successMessage}</span>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <span>${errorMessage}</span>
            </div>
        </c:if>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 24px;">

            <!-- Profile Info Card -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Academic Details</h2>
                </div>

                <form action="${pageContext.request.contextPath}/student/profile" method="POST">
                    <input type="hidden" name="action" value="updateInfo">

                    <div class="form-group">
                        <label class="form-label">Student Roll / ID</label>
                        <input type="text" class="form-control" value="${student.studentId}" disabled 
                               style="background: rgba(0, 0, 0, 0.4); color: var(--text-muted); cursor: not-allowed;">
                        <small style="color: var(--text-muted); font-size: 0.78rem;">Student ID cannot be changed once enrolled.</small>
                    </div>

                    <div class="form-group">
                        <label class="form-label">College Email</label>
                        <input type="email" class="form-control" value="${student.email}" disabled 
                               style="background: rgba(0, 0, 0, 0.4); color: var(--text-muted); cursor: not-allowed;">
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="name">Full Name *</label>
                        <input type="text" id="name" name="name" class="form-control" value="${student.name}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="course">Degree / Department Course *</label>
                        <input type="text" id="course" name="course" class="form-control" value="${student.course}" required>
                    </div>

                    <div style="font-size: 0.82rem; color: var(--text-muted); margin-bottom: 20px;">
                        Member Since: <strong>${student.createdAt}</strong>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%;">Save Changes</button>
                </form>
            </div>

            <!-- Change Password Card -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Security &amp; Password</h2>
                </div>

                <c:if test="${not empty passwordSuccess}">
                    <div class="alert alert-success alert-auto-dismiss">
                        <span>${passwordSuccess}</span>
                    </div>
                </c:if>
                <c:if test="${not empty passwordError}">
                    <div class="alert alert-error">
                        <span>${passwordError}</span>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/student/profile" method="POST">
                    <input type="hidden" name="action" value="changePassword">

                    <div class="form-group">
                        <label class="form-label" for="currentPassword">Current Password *</label>
                        <input type="password" id="currentPassword" name="currentPassword" class="form-control" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="newPassword">New Password (min. 6 chars) *</label>
                        <input type="password" id="newPassword" name="newPassword" class="form-control" required minlength="6">
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="confirmNewPassword">Confirm New Password *</label>
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" class="form-control" required minlength="6">
                    </div>

                    <button type="submit" class="btn btn-outline" style="width: 100%; margin-top: 10px;">Update Password</button>
                </form>
            </div>

        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

