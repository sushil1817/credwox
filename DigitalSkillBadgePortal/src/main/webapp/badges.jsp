<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Digital Credentials — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container">

        <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 28px; flex-wrap: wrap; gap: 16px;">
            <div>
                <h1 style="font-size: 2.2rem; margin-bottom: 6px;">Earned Digital Credentials</h1>
                <p style="color: var(--text-secondary); font-size: 0.95rem;">
                    Full repository of your validated credentials and public verification tokens (${totalCount} total)
                </p>
            </div>
            
            <!-- Filters -->
            <form action="${pageContext.request.contextPath}/student/badges" method="GET" style="display: flex; gap: 12px; flex-wrap: wrap;">
                <select name="level" class="form-control" style="width: auto; padding: 8px 14px;" onchange="this.form.submit()">
                    <option value="ALL" ${selectedLevel eq 'ALL' ? 'selected' : ''}>All Badge Tiers</option>
                    <option value="Gold" ${selectedLevel eq 'Gold' ? 'selected' : ''}>Gold Badges</option>
                    <option value="Silver" ${selectedLevel eq 'Silver' ? 'selected' : ''}>Silver Badges</option>
                    <option value="Bronze" ${selectedLevel eq 'Bronze' ? 'selected' : ''}>Bronze Badges</option>
                </select>

                <select name="status" class="form-control" style="width: auto; padding: 8px 14px;" onchange="this.form.submit()">
                    <option value="ALL" ${selectedStatus eq 'ALL' ? 'selected' : ''}>All Statuses</option>
                    <option value="ACTIVE" ${selectedStatus eq 'ACTIVE' ? 'selected' : ''}>ACTIVE Only</option>
                    <option value="EXPIRED" ${selectedStatus eq 'EXPIRED' ? 'selected' : ''}>EXPIRED Only</option>
                    <option value="REVOKED" ${selectedStatus eq 'REVOKED' ? 'selected' : ''}>REVOKED Only</option>
                </select>
                
                <c:if test="${selectedLevel ne 'ALL' or selectedStatus ne 'ALL'}">
                    <a href="${pageContext.request.contextPath}/student/badges" class="btn btn-secondary btn-sm" style="display: flex; align-items: center;">Reset</a>
                </c:if>
            </form>
        </div>

        <c:choose>
            <c:when test="${empty badges}">
                <div class="card" style="text-align: center; padding: 60px 20px;">
                    <div style="font-size: 3rem; margin-bottom: 12px;">&#128269;</div>
                    <h3 style="color: #fff; margin-bottom: 8px;">No Badges Matched Your Filter</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 20px;">Try switching filter dropdowns to view all earned badges.</p>
                    <a href="${pageContext.request.contextPath}/student/badges" class="btn btn-outline">Clear All Filters</a>
                </div>
            </c:when>
            <c:otherwise>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 24px;">
                    <c:forEach var="badge" items="${badges}">
                        <c:set var="tierClass" value="tier-${badge.badgeLevel.toLowerCase()}" />
                        <div class="badge-card ${tierClass}">
                            <div class="badge-emblem">
                                <svg viewBox="0 0 24 24" fill="currentColor">
                                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                </svg>
                            </div>

                            <div class="badge-level-tag">${badge.badgeLevel} Tier Badge</div>
                            <h3 class="badge-module-title" style="min-height: auto; margin-bottom: 6px;">${badge.moduleName}</h3>
                            <div style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 14px;">Module Code: <strong>${badge.moduleCode}</strong></div>

                            <!-- Description -->
                            <p style="font-size: 0.86rem; color: var(--text-secondary); margin-bottom: 16px; line-height: 1.5;">
                                ${badge.moduleDescription}
                            </p>

                            <!-- Status Pill -->
                            <div style="margin-bottom: 12px;">
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
                            </div>

                            <!-- Click to copy code -->
                            <div class="badge-code-display" title="Click to copy" onclick="copyToClipboard('${badge.badgeCode}', this)">
                                <span>Code: ${badge.badgeCode}</span>
                                <span>&#128203;</span>
                            </div>

                            <div style="width: 100%; border-top: 1px solid var(--border-color); padding-top: 14px; margin-top: 8px; font-size: 0.82rem; color: var(--text-muted); text-align: left;">
                                <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                                    <span>Issued:</span> <strong style="color: var(--text-primary);">${badge.issueDate}</strong>
                                </div>
                                <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
                                    <span>Expires:</span> <strong style="color: var(--text-primary);">${not empty badge.expiryDate ? badge.expiryDate : 'No Expiry'}</strong>
                                </div>
                                <div style="display: flex; justify-content: space-between;">
                                    <span>Issued By:</span> <strong style="color: var(--text-primary);">${badge.issuedBy}</strong>
                                </div>
                                <c:if test="${badge.effectiveStatus eq 'REVOKED' and not empty badge.revokedReason}">
                                    <div style="margin-top: 8px; padding: 6px 10px; background: var(--status-revoked-bg); border-radius: var(--radius-sm); color: #fca5a5; font-size: 0.78rem;">
                                        <strong>Revocation Reason:</strong> ${badge.revokedReason}
                                    </div>
                                </c:if>
                            </div>

                            <div style="display: flex; gap: 8px; width: 100%; margin-top: 18px;">
                                <a href="${pageContext.request.contextPath}/certificate?code=${badge.badgeCode}" target="_blank" class="btn btn-primary btn-sm" style="flex: 1;">
                                    Official Certificate &nearr;
                                </a>
                                <a href="${pageContext.request.contextPath}/verify?code=${badge.badgeCode}" target="_blank" class="btn btn-secondary btn-sm" title="Public Verification Link">
                                    Verify
                                </a>
                                <button type="button" class="btn btn-secondary btn-sm" onclick="copyVerificationLink('${badge.badgeCode}', this)" title="Copy Shareable Link">
                                    &#128279;
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

