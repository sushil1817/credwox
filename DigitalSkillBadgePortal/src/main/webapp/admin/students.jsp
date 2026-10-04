<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Students Directory — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/admin-navbar.jsp" />

<main class="main-content">
    <div class="container">

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 28px; flex-wrap: wrap; gap: 16px;">
            <div>
                <h1 style="font-size: 2rem; margin-bottom: 4px;">Enrolled Students Directory</h1>
                <p style="color: var(--text-secondary); font-size: 0.95rem;">
                    Woxsen University student profiles and awarded credentials (${students.size()} registered)
                </p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/issue-badge" class="btn btn-primary">&#10133; Issue Credential</a>
            </div>
        </div>

        <div class="card">
            <!-- Search bar -->
            <div style="margin-bottom: 20px;">
                <input type="text" id="tableSearchInput" class="form-control" 
                       placeholder="&#128269; Live search students by name, roll ID, or course..." style="max-width: 450px;">
            </div>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Student ID</th>
                            <th>Student Name</th>
                            <th>Email Address</th>
                            <th>Degree Course</th>
                            <th style="text-align: center;">Total Badges</th>
                            <th style="text-align: center;">Active Badges</th>
                            <th>Enrolled Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${students}">
                            <tr>
                                <td style="font-family: monospace; font-weight: 700; color: var(--primary-light);">${s.studentId}</td>
                                <td><strong>${s.name}</strong></td>
                                <td style="color: var(--text-secondary);">${s.email}</td>
                                <td>${s.course}</td>
                                <td style="text-align: center;">
                                    <span style="font-weight: 700; font-size: 1rem;">${s.totalBadges}</span>
                                </td>
                                <td style="text-align: center;">
                                    <span class="status-pill status-active">${s.activeBadges}</span>
                                </td>
                                <td style="font-size: 0.84rem; color: var(--text-muted);">${s.createdAt}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/issue-badge?preSelectStudent=${s.id}" 
                                       class="btn btn-outline btn-sm">Issue Badge</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

