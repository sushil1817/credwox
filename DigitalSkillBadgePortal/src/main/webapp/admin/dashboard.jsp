<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Faculty Dashboard — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/admin-navbar.jsp" />

<main class="main-content">
    <div class="container">

        <!-- Welcome Banner -->
        <div class="card" style="margin-bottom: 30px; background: linear-gradient(135deg, rgba(30, 27, 75, 0.8) 0%, rgba(15, 23, 42, 0.9) 100%); border-color: rgba(99, 102, 241, 0.3);">
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 16px;">
                <div>
                    <span class="user-badge" style="background: rgba(124, 58, 237, 0.2); border-color: rgba(124, 58, 237, 0.5); color: #c4b5fd; margin-bottom: 10px; display: inline-block;">
                        Woxsen University &bull; School of Technology
                    </span>
                    <h1 style="font-size: 2.2rem; margin-bottom: 6px;">CredWox Faculty Console</h1>
                    <p style="color: var(--text-secondary); font-size: 0.95rem;">
                        Logged in as: <strong>${sessionScope.adminUser.fullName}</strong> (${sessionScope.adminUser.username}) &bull; Authorized Signatory
                    </p>
                </div>
                <div style="display: flex; gap: 10px; flex-wrap: wrap;">
                    <a href="${pageContext.request.contextPath}/admin/issue-badge" class="btn btn-primary">&#10133; Issue Credential</a>
                    <a href="${pageContext.request.contextPath}/admin/modules" class="btn btn-secondary">Curriculum Modules</a>
                </div>
            </div>
        </div>

        <!-- 6-Metric Aggregation Grid -->
        <div class="stats-grid">
            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(99, 102, 241, 0.15); color: var(--primary-light);">&#127891;</div>
                <div>
                    <div class="stat-val">${stats.totalStudents}</div>
                    <div class="stat-label">Total Students</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(6, 182, 212, 0.15); color: var(--secondary);">&#128218;</div>
                <div>
                    <div class="stat-val">${stats.totalModules}</div>
                    <div class="stat-label">Skill Modules</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(245, 158, 11, 0.15); color: var(--gold-primary);">&#127941;</div>
                <div>
                    <div class="stat-val">${stats.totalBadges}</div>
                    <div class="stat-label">Total Badges Issued</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(16, 185, 129, 0.15); color: var(--status-active);">&#9989;</div>
                <div>
                    <div class="stat-val">${stats.activeBadges}</div>
                    <div class="stat-label">Active Credentials</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(239, 68, 68, 0.15); color: var(--status-revoked);">&#128683;</div>
                <div>
                    <div class="stat-val">${stats.revokedBadges}</div>
                    <div class="stat-label">Revoked Badges</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(148, 163, 184, 0.15); color: var(--text-secondary);">&#128269;</div>
                <div>
                    <div class="stat-val">${stats.totalVerifications}</div>
                    <div class="stat-label">Verification Lookups</div>
                </div>
            </div>
        </div>

        <!-- 2 Column Section: Recently Issued Badges & Verification History -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(480px, 1fr)); gap: 24px; margin-top: 10px;">
            
            <!-- Recently Issued Badges -->
            <div class="card">
                <div class="card-header">
                    <div>
                        <h2 class="card-title">Recently Issued Badges</h2>
                    </div>
                    <a href="${pageContext.request.contextPath}/admin/badges" class="btn btn-outline btn-sm">All Badges &rarr;</a>
                </div>

                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Student</th>
                                <th>Module</th>
                                <th>Level</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="badge" items="${recentBadges}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/verify?code=${badge.badgeCode}" target="_blank" style="font-family: monospace; font-weight: 700;">
                                            ${badge.badgeCode}
                                        </a>
                                    </td>
                                    <td>
                                        <div><strong>${badge.studentName}</strong></div>
                                        <small style="color: var(--text-muted);">${badge.studentRoll}</small>
                                    </td>
                                    <td>${badge.moduleName}</td>
                                    <td>
                                        <span class="badge-level-tag" style="margin: 0; font-size: 0.78rem;">${badge.badgeLevel}</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${badge.effectiveStatus eq 'ACTIVE'}">
                                                <span class="status-pill status-active"><span class="status-dot"></span>ACTIVE</span>
                                            </c:when>
                                            <c:when test="${badge.effectiveStatus eq 'EXPIRED'}">
                                                <span class="status-pill status-expired"><span class="status-dot"></span>EXPIRED</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-pill status-revoked"><span class="status-dot"></span>REVOKED</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Recent Public Verifications -->
            <div class="card">
                <div class="card-header">
                    <div>
                        <h2 class="card-title">Recent Public Verifications</h2>
                    </div>
                    <a href="${pageContext.request.contextPath}/admin/verifications" class="btn btn-outline btn-sm">Audit History &rarr;</a>
                </div>

                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Time</th>
                                <th>Badge Code</th>
                                <th>Result</th>
                                <th>IP Address</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="audit" items="${recentVerifications}">
                                <tr>
                                    <td style="font-size: 0.8rem; color: var(--text-muted);">${audit.verificationTime}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/verify?code=${audit.badgeCode}" target="_blank" style="font-family: monospace;">
                                            ${audit.badgeCode}
                                        </a>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${audit.verificationResult eq 'ACTIVE'}">
                                                <span class="status-pill status-active">ACTIVE</span>
                                            </c:when>
                                            <c:when test="${audit.verificationResult eq 'EXPIRED'}">
                                                <span class="status-pill status-expired">EXPIRED</span>
                                            </c:when>
                                            <c:when test="${audit.verificationResult eq 'REVOKED'}">
                                                <span class="status-pill status-revoked">REVOKED</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-pill" style="background: rgba(148, 163, 184, 0.15); color: #cbd5e1;">NOT FOUND</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="font-family: monospace; font-size: 0.82rem; color: var(--text-muted);">${audit.ipAddress}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

