<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Student Registration — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container" style="max-width: 540px;">

        <div class="card" style="padding: 36px 32px;">
            <div style="text-align: center; margin-bottom: 24px;">
                <div class="nav-logo-icon" style="margin: 0 auto 14px; width: 46px; height: 46px;">&#9997;</div>
                <h1 style="font-size: 1.8rem; margin-bottom: 6px;">Student Registration</h1>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">Woxsen University &mdash; Register to manage your digital credentials</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <form id="registerForm" action="${pageContext.request.contextPath}/student/register" method="POST">
                <div class="form-group">
                    <label class="form-label" for="studentId">Student Roll / ID Number *</label>
                    <input type="text" id="studentId" name="studentId" class="form-control" 
                           placeholder="e.g. 25WU0101145" value="${prevStudentId}" required autofocus>
                </div>

                <div class="form-group">
                    <label class="form-label" for="name">Full Name *</label>
                    <input type="text" id="name" name="name" class="form-control" 
                           placeholder="e.g. Sneha Kulkarni" value="${prevName}" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="email">College Email Address *</label>
                    <input type="email" id="email" name="email" class="form-control" 
                           placeholder="e.g. sneha.k@college.edu" value="${prevEmail}" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="course">Degree / Department Course *</label>
                    <select id="course" name="course" class="form-control" required>
                        <option value="">-- Select Your Degree Course --</option>
                        <option value="B.Tech Computer Science &amp; Engineering" ${prevCourse eq 'B.Tech Computer Science & Engineering' ? 'selected' : ''}>B.Tech Computer Science &amp; Engineering</option>
                        <option value="B.Tech Information Technology" ${prevCourse eq 'B.Tech Information Technology' ? 'selected' : ''}>B.Tech Information Technology</option>
                        <option value="B.Tech Artificial Intelligence &amp; Data Science" ${prevCourse eq 'B.Tech Artificial Intelligence & Data Science' ? 'selected' : ''}>B.Tech Artificial Intelligence &amp; Data Science</option>
                        <option value="B.Tech Electronics &amp; Communication" ${prevCourse eq 'B.Tech Electronics & Communication' ? 'selected' : ''}>B.Tech Electronics &amp; Communication</option>
                        <option value="Master of Computer Applications (MCA)" ${prevCourse eq 'Master of Computer Applications (MCA)' ? 'selected' : ''}>Master of Computer Applications (MCA)</option>
                    </select>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px;">
                    <div class="form-group">
                        <label class="form-label" for="password">Password *</label>
                        <input type="password" id="password" name="password" class="form-control" 
                               placeholder="Min. 6 chars" required minlength="6">
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="confirmPassword">Confirm Password *</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                               placeholder="Repeat password" required minlength="6">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">
                    Complete Registration &rarr;
                </button>
            </form>

            <div style="margin-top: 24px; text-align: center; font-size: 0.88rem; color: var(--text-secondary); border-top: 1px solid var(--border-color); padding-top: 18px;">
                Already registered? <a href="${pageContext.request.contextPath}/login.jsp" style="font-weight: 600;">Sign in here</a>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

