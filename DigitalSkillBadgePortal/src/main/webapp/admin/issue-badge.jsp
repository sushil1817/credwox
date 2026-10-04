<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Issue Digital Credential — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/admin-navbar.jsp" />

<main class="main-content">
    <div class="container" style="max-width: 680px;">

        <div style="margin-bottom: 28px; text-align: center;">
            <div class="hero-pill" style="margin-bottom: 12px;">
                <span>&#127891; Woxsen University &bull; Faculty Credential Authority</span>
            </div>
            <h1 style="font-size: 2.2rem; margin-bottom: 6px;">Issue Digital Credential</h1>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">
                Authenticate student competency achievement and generate a verifiable digital credential token.
            </p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <span>${errorMessage}</span>
            </div>
        </c:if>

        <div class="card" style="padding: 36px 32px;">
            <form id="issueBadgeForm" action="${pageContext.request.contextPath}/admin/issue-badge" method="POST">
                
                <!-- Student Selection -->
                <div class="form-group">
                    <label class="form-label" for="studentId">Select Student *</label>
                    <select id="studentId" name="studentId" class="form-control" required>
                        <option value="">-- Choose Student --</option>
                        <c:forEach var="s" items="${students}">
                            <option value="${s.id}" ${param.preSelectStudent eq s.id ? 'selected' : ''}>
                                ${s.name} (${s.studentId}) &bull; ${s.course}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Skill Module Selection -->
                <div class="form-group">
                    <label class="form-label" for="moduleId">Select Skill Module *</label>
                    <select id="moduleId" name="moduleId" class="form-control" required>
                        <option value="">-- Choose Module --</option>
                        <c:forEach var="m" items="${modules}">
                            <option value="${m.id}">
                                [${m.moduleCode}] ${m.moduleName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Badge Level / Tier -->
                <div class="form-group">
                    <label class="form-label" for="badgeLevel">Badge Level / Tier *</label>
                    <select id="badgeLevel" name="badgeLevel" class="form-control" required>
                        <option value="Gold">&#129351; Gold Badge (Advanced Mastery / Top Honors)</option>
                        <option value="Silver">&#129352; Silver Badge (Proficient Competency / High Merit)</option>
                        <option value="Bronze">&#129353; Bronze Badge (Foundational Proficiency)</option>
                    </select>
                </div>

                <!-- Verification Code -->
                <div class="form-group">
                    <label class="form-label" for="badgeCode">
                        Unique Verification Code * 
                        <small style="color: var(--text-muted); font-weight: normal;">(Auto-generated unique token)</small>
                    </label>
                    <input type="text" id="badgeCode" name="badgeCode" class="form-control" 
                           value="${suggestedCode}" required style="font-family: monospace; font-weight: 700; letter-spacing: 1px;">
                </div>

                <!-- Dates Grid -->
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                    <div class="form-group">
                        <label class="form-label" for="issueDate">Issue Date *</label>
                        <input type="date" id="issueDate" name="issueDate" class="form-control" 
                               value="${todayDate}" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="expiryDate">
                            Expiry Date <small style="color: var(--text-muted);">(Optional)</small>
                        </label>
                        <input type="date" id="expiryDate" name="expiryDate" class="form-control" 
                               value="${defaultExpiry}">
                    </div>
                </div>

                <!-- Status -->
                <div class="form-group">
                    <label class="form-label" for="status">Initial Status</label>
                    <select id="status" name="status" class="form-control">
                        <option value="ACTIVE" selected>ACTIVE (Valid &amp; Auditable)</option>
                        <option value="REVOKED">REVOKED (Inactive)</option>
                        <option value="EXPIRED">EXPIRED</option>
                    </select>
                </div>

                <div style="margin-top: 30px; display: flex; gap: 12px;">
                    <button type="submit" class="btn btn-primary btn-lg" style="flex: 1;">
                        &#10003; Issue Badge &amp; Register Token
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/badges" class="btn btn-secondary btn-lg">Cancel</a>
                </div>

            </form>
        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

