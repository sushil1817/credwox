<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Credential Registry — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/admin-navbar.jsp" />

<main class="main-content">
    <div class="container">

        <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
            <div>
                <h1 style="font-size: 2rem; margin-bottom: 4px;">Issued Credentials Registry</h1>
                <p style="color: var(--text-secondary); font-size: 0.95rem;">
                    Full audit registry of all conferred credentials across Woxsen cohorts (${badges.size()} records)
                </p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/admin/issue-badge" class="btn btn-primary">&#10133; Issue Credential</a>
            </div>
        </div>

        <!-- Success & Status Alerts -->
        <c:if test="${param.success eq 'issued'}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>Badge successfully issued with verification code: <strong>${param.code}</strong></span>
            </div>
        </c:if>
        <c:if test="${param.success eq 'revoked'}">
            <div class="alert alert-warning alert-auto-dismiss">
                <span>Badge status has been updated to REVOKED.</span>
            </div>
        </c:if>
        <c:if test="${param.success eq 'reactivated'}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>Badge has been reactivated to ACTIVE standing.</span>
            </div>
        </c:if>
        <c:if test="${param.success eq 'deleted'}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>Badge record deleted from database.</span>
            </div>
        </c:if>

        <div class="card">
            <!-- Filter Bar -->
            <form action="${pageContext.request.contextPath}/admin/badges" method="GET" style="display: flex; gap: 12px; margin-bottom: 24px; flex-wrap: wrap;">
                <input type="text" name="search" class="form-control" placeholder="Search by student, roll ID, or code..." 
                       value="${searchParam}" style="flex: 1; min-width: 240px;">

                <select name="level" class="form-control" style="width: auto;" onchange="this.form.submit()">
                    <option value="ALL" ${levelParam eq 'ALL' ? 'selected' : ''}>All Tiers</option>
                    <option value="Gold" ${levelParam eq 'Gold' ? 'selected' : ''}>Gold Only</option>
                    <option value="Silver" ${levelParam eq 'Silver' ? 'selected' : ''}>Silver Only</option>
                    <option value="Bronze" ${levelParam eq 'Bronze' ? 'selected' : ''}>Bronze Only</option>
                </select>

                <select name="status" class="form-control" style="width: auto;" onchange="this.form.submit()">
                    <option value="ALL" ${statusParam eq 'ALL' ? 'selected' : ''}>All Statuses</option>
                    <option value="ACTIVE" ${statusParam eq 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                    <option value="EXPIRED" ${statusParam eq 'EXPIRED' ? 'selected' : ''}>EXPIRED</option>
                    <option value="REVOKED" ${statusParam eq 'REVOKED' ? 'selected' : ''}>REVOKED</option>
                </select>

                <button type="submit" class="btn btn-secondary">Filter</button>
                <c:if test="${not empty searchParam or statusParam ne 'ALL' or levelParam ne 'ALL'}">
                    <a href="${pageContext.request.contextPath}/admin/badges" class="btn btn-outline" style="display: flex; align-items: center;">Reset</a>
                </c:if>
            </form>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Badge Code</th>
                            <th>Student Recipient</th>
                            <th>Skill Module</th>
                            <th>Level</th>
                            <th>Issue &bull; Expiry</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${badges}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/verify?code=${b.badgeCode}" target="_blank" 
                                       style="font-family: monospace; font-weight: 700;">
                                        ${b.badgeCode}
                                    </a>
                                </td>
                                <td>
                                    <div><strong>${b.studentName}</strong></div>
                                    <small style="color: var(--text-muted);">${b.studentRoll} &bull; ${b.studentCourse}</small>
                                </td>
                                <td>
                                    <div><strong>${b.moduleName}</strong></div>
                                    <small style="color: var(--text-muted);">${b.moduleCode}</small>
                                </td>
                                <td>
                                    <span class="badge-level-tag" style="margin: 0; font-size: 0.8rem;">${b.badgeLevel}</span>
                                </td>
                                <td style="font-size: 0.84rem;">
                                    <div>${b.issueDate}</div>
                                    <small style="color: var(--text-muted);">
                                        Exp: ${not empty b.expiryDate ? b.expiryDate : 'None'}
                                    </small>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${b.effectiveStatus eq 'ACTIVE'}">
                                            <span class="status-pill status-active"><span class="status-dot"></span>ACTIVE</span>
                                        </c:when>
                                        <c:when test="${b.effectiveStatus eq 'EXPIRED'}">
                                            <span class="status-pill status-expired"><span class="status-dot"></span>EXPIRED</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-pill status-revoked"><span class="status-dot"></span>REVOKED</span>
                                            <c:if test="${not empty b.revokedReason}">
                                                <div style="font-size: 0.72rem; color: #fca5a5; max-width: 150px; margin-top: 2px;">
                                                    ${b.revokedReason}
                                                </div>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="${pageContext.request.contextPath}/certificate?code=${b.badgeCode}" target="_blank" class="btn btn-outline btn-sm" title="View Certificate">
                                        Cert &nearr;
                                    </a>

                                    <c:choose>
                                        <c:when test="${b.status eq 'REVOKED'}">
                                            <form action="${pageContext.request.contextPath}/admin/revoke-badge" method="POST" style="display: inline;">
                                                <input type="hidden" name="action" value="reactivate">
                                                <input type="hidden" name="badgeId" value="${b.id}">
                                                <button type="submit" class="btn btn-success btn-sm" onclick="return confirm('Reactivate badge ${b.badgeCode}?');">
                                                    Reactivate
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" class="btn btn-secondary btn-sm" style="color: #f87171;" 
                                                    onclick="promptRevokeBadge('${b.id}', '${b.badgeCode}')">
                                                Revoke
                                            </button>
                                        </c:otherwise>
                                    </c:choose>

                                    <form action="${pageContext.request.contextPath}/admin/revoke-badge" method="POST" style="display: inline;" onsubmit="return confirm('Permanently delete badge ${b.badgeCode}?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="badgeId" value="${b.id}">
                                        <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--text-muted);">
                                            &times;
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<!-- Hidden Revocation Form for JS prompt -->
<form id="revokeForm" action="${pageContext.request.contextPath}/admin/revoke-badge" method="POST" style="display:none;">
    <input type="hidden" name="action" value="revoke">
    <input type="hidden" name="badgeId" id="revokeBadgeId">
    <input type="hidden" name="reason" id="revokeReason">
</form>

<script>
function promptRevokeBadge(badgeId, badgeCode) {
    const reason = prompt("Enter official reason for revoking badge [" + badgeCode + "]:", "Assessment violation or administrative review");
    if (reason !== null && reason.trim() !== "") {
        document.getElementById('revokeBadgeId').value = badgeId;
        document.getElementById('revokeReason').value = reason.trim();
        document.getElementById('revokeForm').submit();
    }
}
</script>

<jsp:include page="/includes/footer.jsp" />

