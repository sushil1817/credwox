<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Student Dashboard — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container">

        <!-- Welcome Banner -->
        <div class="card" style="margin-bottom: 32px; background: linear-gradient(135deg, rgba(30, 41, 59, 0.8) 0%, rgba(15, 23, 42, 0.9) 100%);">
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px;">
                <div>
                    <span class="user-badge" style="margin-bottom: 12px; display: inline-block;">Woxsen Student Credential Wallet</span>
                    <h1 style="font-size: 2.2rem; margin-bottom: 8px;">Welcome back, ${sessionScope.studentUser.name}!</h1>
                    <p style="color: var(--text-secondary); font-size: 0.95rem;">
                        <strong>Student ID:</strong> ${sessionScope.studentUser.studentId} &bull; 
                        <strong>Course:</strong> ${sessionScope.studentUser.course}
                    </p>
                </div>
                <div style="display: flex; gap: 12px; flex-wrap: wrap;">
                    <a href="${pageContext.request.contextPath}/student/badges" class="btn btn-primary">My Credentials &rarr;</a>
                    <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-secondary">My Profile</a>
                </div>
            </div>
        </div>

        <!-- Metric Stat Cards -->
        <div class="stats-grid">
            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(99, 102, 241, 0.15); color: var(--primary-light);">&#127942;</div>
                <div>
                    <div class="stat-val">${totalBadges}</div>
                    <div class="stat-label">Total Badges Earned</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(16, 185, 129, 0.15); color: var(--status-active);">&#9989;</div>
                <div>
                    <div class="stat-val">${activeBadges}</div>
                    <div class="stat-label">Active Credentials</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(245, 158, 11, 0.15); color: var(--gold-primary);">&#11088;</div>
                <div>
                    <div class="stat-val">${goldBadges}</div>
                    <div class="stat-label">Gold Tier Badges</div>
                </div>
            </div>

            <div class="stat-box">
                <div class="stat-icon" style="background: rgba(203, 213, 225, 0.15); color: var(--silver-primary);">&#129358;</div>
                <div>
                    <div class="stat-val">${silverBadges}</div>
                    <div class="stat-label">Silver Tier Badges</div>
                </div>
            </div>
        </div>

        <!-- Earned Badges Section -->
        <div class="card">
            <div class="card-header">
                <div>
                    <h2 class="card-title">My Digital Skill Credentials</h2>
                    <p style="color: var(--text-secondary); font-size: 0.88rem; margin-top: 4px;">
                        Click on any verification code to copy it or view the official printable certificate.
                    </p>
                </div>
                <a href="${pageContext.request.contextPath}/student/badges" class="btn btn-outline btn-sm">View Detailed Catalog &rarr;</a>
            </div>

            <c:choose>
                <c:when test="${empty badges}">
                    <div style="text-align: center; padding: 48px 20px; color: var(--text-muted);">
                        <div style="font-size: 3rem; margin-bottom: 12px;">&#127894;</div>
                        <h3 style="font-size: 1.25rem; color: #fff; margin-bottom: 6px;">No Badges Issued Yet</h3>
                        <p style="max-width: 450px; margin: 0 auto 20px; font-size: 0.9rem;">
                            Your course instructors and administrators will issue digital skill badges once you complete module assessments.
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(290px, 1fr)); gap: 24px;">
                        <c:forEach var="badge" items="${badges}">
                            <c:set var="tierClass" value="tier-${badge.badgeLevel.toLowerCase()}" />
                            <div class="badge-card ${tierClass}">
                                <div class="badge-emblem">
                                    <svg viewBox="0 0 24 24" fill="currentColor">
                                        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                    </svg>
                                </div>
                                <div class="badge-level-tag">${badge.badgeLevel} Tier Badge</div>
                                <h3 class="badge-module-title">${badge.moduleName}</h3>
                                <p style="font-size: 0.84rem; color: var(--text-muted); margin-bottom: 8px;">Code: ${badge.moduleCode}</p>

                                <!-- Status Badge -->
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

                                <!-- Verification Code -->
                                <div class="badge-code-display" title="Click to copy code" onclick="copyToClipboard('${badge.badgeCode}', this)">
                                    <span>${badge.badgeCode}</span>
                                    <span style="font-size: 0.75rem;">&#128203;</span>
                                </div>

                                <div style="font-size: 0.8rem; color: var(--text-secondary); margin-bottom: 16px;">
                                    Issued: <strong>${badge.issueDate}</strong>
                                    <c:if test="${not empty badge.expiryDate}">
                                        &bull; Valid Thru: <strong>${badge.expiryDate}</strong>
                                    </c:if>
                                </div>

                                <div style="display: flex; gap: 8px; width: 100%;">
                                    <a href="${pageContext.request.contextPath}/certificate?code=${badge.badgeCode}" 
                                       target="_blank" class="btn btn-primary btn-sm" style="flex: 1;">
                                       View Certificate &nearr;
                                    </a>
                                    <button type="button" class="btn btn-secondary btn-sm" 
                                            onclick="copyVerificationLink('${badge.badgeCode}', this)" title="Copy public verification link">
                                        &#128279; Share
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

