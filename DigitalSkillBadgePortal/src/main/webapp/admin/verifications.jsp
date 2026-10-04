<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Verification Audit Trail — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/admin-navbar.jsp" />

<main class="main-content">
    <div class="container">

        <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
            <div>
                <h1 style="font-size: 2rem; margin-bottom: 4px;">Public Credential Verification Audit Trail</h1>
                <p style="color: var(--text-secondary); font-size: 0.95rem;">
                    Chronological audit log of all CredWox lookups and verification attempts (${verifications.size()} entries shown)
                </p>
            </div>
            
            <form action="${pageContext.request.contextPath}/admin/verifications" method="GET" style="display: flex; align-items: center; gap: 8px;">
                <label style="font-size: 0.85rem; color: var(--text-secondary);">Show:</label>
                <select name="limit" class="form-control" style="width: auto; padding: 6px 12px;" onchange="this.form.submit()">
                    <option value="25" ${limit eq 25 ? 'selected' : ''}>Latest 25</option>
                    <option value="50" ${limit eq 50 ? 'selected' : ''}>Latest 50</option>
                    <option value="100" ${limit eq 100 ? 'selected' : ''}>Latest 100</option>
                    <option value="250" ${limit eq 250 ? 'selected' : ''}>Latest 250</option>
                </select>
            </form>
        </div>

        <div class="card">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Verification Timestamp</th>
                            <th>Target Badge Code</th>
                            <th>Recipient Student</th>
                            <th>Module &bull; Tier</th>
                            <th>Audit Result</th>
                            <th>Verifier IP</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${verifications}">
                            <tr>
                                <td style="color: var(--text-muted); font-size: 0.82rem;">${v.id}</td>
                                <td style="font-size: 0.86rem; color: var(--text-secondary);">${v.verificationTime}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/verify?code=${v.badgeCode}" target="_blank" 
                                       style="font-family: monospace; font-weight: 700;">
                                        ${v.badgeCode}
                                    </a>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty v.studentName}">
                                            <strong>${v.studentName}</strong>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: var(--text-muted); font-style: italic;">Unknown Code</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${not empty v.moduleName}">
                                        <div>${v.moduleName}</div>
                                        <span class="badge-level-tag" style="margin: 0; font-size: 0.75rem;">${v.badgeLevel}</span>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.verificationResult eq 'ACTIVE'}">
                                            <span class="status-pill status-active"><span class="status-dot"></span>ACTIVE</span>
                                        </c:when>
                                        <c:when test="${v.verificationResult eq 'EXPIRED'}">
                                            <span class="status-pill status-expired"><span class="status-dot"></span>EXPIRED</span>
                                        </c:when>
                                        <c:when test="${v.verificationResult eq 'REVOKED'}">
                                            <span class="status-pill status-revoked"><span class="status-dot"></span>REVOKED</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-pill" style="background: rgba(148, 163, 184, 0.12); color: #94a3b8; border: 1px solid rgba(148, 163, 184, 0.3);">
                                                NOT FOUND
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="font-family: monospace; font-size: 0.82rem; color: var(--text-muted);">${v.ipAddress}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

