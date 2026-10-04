<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Public Credential Verification — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container" style="max-width: 860px;">

        <!-- Verification Search Header -->
        <div style="text-align: center; margin-bottom: 36px;">
            <div class="hero-pill" style="margin-bottom: 16px;">
                <span>&#128274; Woxsen University &bull; Audited Credential Verification</span>
            </div>
            <h1 style="font-size: 2.4rem; margin-bottom: 8px;">Verify Digital Credential</h1>
            <p style="color: var(--text-secondary); max-width: 600px; margin: 0 auto 28px;">
                Enter a unique credential verification code to instantly validate student achievement, issue authenticity, and current validity status without logging in.
            </p>

            <form action="${pageContext.request.contextPath}/verify" method="GET" class="verify-search-box">
                <input type="text" name="code" id="verifyCodeInput" class="verify-input" 
                       placeholder="e.g. CWX-GOLD-202601" 
                       value="${searchedCode}" required autofocus>
                <button type="submit" class="btn btn-primary btn-lg">Verify Now &rarr;</button>
            </form>

            <!-- Quick Demo Code Pills -->
            <div style="margin-top: 18px; display: flex; align-items: center; justify-content: center; gap: 8px; flex-wrap: wrap;">
                <span style="font-size: 0.82rem; color: var(--text-muted); font-weight: 600;">Sample Test Codes:</span>
                <button type="button" class="btn btn-secondary btn-sm" onclick="setSampleVerifyCode('CWX-GOLD-202601')">
                    <span style="color: var(--gold-primary);">&#9679;</span> Active Gold
                </button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="setSampleVerifyCode('CWX-EXPD-202501')">
                    <span style="color: var(--status-expired);">&#9679;</span> Expired Silver
                </button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="setSampleVerifyCode('CWX-REVK-202605')">
                    <span style="color: var(--status-revoked);">&#9679;</span> Revoked Bronze
                </button>
            </div>
        </div>

        <!-- Verification Output Section -->
        <c:if test="${searched eq true}">
            <c:choose>
                <c:when test="${resultType eq 'NOT_FOUND'}">
                    <div class="card" style="border-color: rgba(239, 68, 68, 0.4); background: rgba(239, 68, 68, 0.05); text-align: center; padding: 48px 24px;">
                        <div style="font-size: 3.5rem; margin-bottom: 12px; color: var(--status-revoked);">&#10060;</div>
                        <h2 style="font-size: 1.8rem; margin-bottom: 10px; color: #fff;">Invalid Verification Code</h2>
                        <p style="color: #fca5a5; max-width: 550px; margin: 0 auto 20px; font-size: 1rem;">
                            ${message}
                        </p>
                        <p style="color: var(--text-muted); font-size: 0.84rem;">
                            Every lookup is securely logged for system audit: Attempted Code: <code>${searchedCode}</code>
                        </p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="card" style="border-color: rgba(99, 102, 241, 0.35); padding: 36px;">
                        
                        <!-- Status Banner -->
                        <c:choose>
                            <c:when test="${effectiveStatus eq 'ACTIVE'}">
                                <div class="alert alert-success" style="font-size: 1.05rem; padding: 16px 20px;">
                                    <span style="font-size: 1.4rem;">&#9989;</span>
                                    <div>
                                        <strong>VERIFIED AUTHENTIC BADGE</strong>
                                        <div style="font-size: 0.88rem; font-weight: normal; margin-top: 2px;">
                                            This digital badge was officially issued and is currently in good standing.
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${effectiveStatus eq 'EXPIRED'}">
                                <div class="alert alert-warning" style="font-size: 1.05rem; padding: 16px 20px;">
                                    <span style="font-size: 1.4rem;">&#9888;</span>
                                    <div>
                                        <strong>CREDENTIAL EXPIRED</strong>
                                        <div style="font-size: 0.88rem; font-weight: normal; margin-top: 2px;">
                                            This badge was legitimately issued but expired on <strong>${badge.expiryDate}</strong>.
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-error" style="font-size: 1.05rem; padding: 16px 20px;">
                                    <span style="font-size: 1.4rem;">&#128683;</span>
                                    <div>
                                        <strong>BADGE REVOKED</strong>
                                        <div style="font-size: 0.88rem; font-weight: normal; margin-top: 2px;">
                                            This badge has been revoked by the issuing institution.
                                            <c:if test="${not empty badge.revokedReason}">
                                                <br><em>Reason: ${badge.revokedReason}</em>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Verified Badge Details Grid -->
                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 30px; margin-top: 28px;">
                            
                            <!-- Left: Visual Emblem and Level -->
                            <c:set var="tierClass" value="tier-${badge.badgeLevel.toLowerCase()}" />
                            <div class="badge-card ${tierClass}" style="margin: 0;">
                                <div class="badge-emblem" style="width: 110px; height: 110px;">
                                    <svg viewBox="0 0 24 24" fill="currentColor">
                                        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                                    </svg>
                                </div>
                                <div class="badge-level-tag" style="font-size: 1rem;">${badge.badgeLevel} Tier Badge</div>
                                <h3 class="badge-module-title" style="min-height: auto; font-size: 1.25rem;">${badge.moduleName}</h3>
                                <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 12px;">Module: ${badge.moduleCode}</p>

                                <c:choose>
                                    <c:when test="${effectiveStatus eq 'ACTIVE'}">
                                        <span class="status-pill status-active"><span class="status-dot"></span>ACTIVE</span>
                                    </c:when>
                                    <c:when test="${effectiveStatus eq 'EXPIRED'}">
                                        <span class="status-pill status-expired"><span class="status-dot"></span>EXPIRED</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill status-revoked"><span class="status-dot"></span>REVOKED</span>
                                    </c:otherwise>
                                </c:choose>

                                <div class="badge-code-display" onclick="copyToClipboard('${badge.badgeCode}', this)">
                                    <span>${badge.badgeCode}</span>
                                    <span>&#128203;</span>
                                </div>

                                <div style="margin-top: 14px; text-align: center;">
                                    <img src="https://api.qrserver.com/v1/create-qr-code/?size=100x100&margin=3&data=http://localhost:8080${pageContext.request.contextPath}/verify?code=${badge.badgeCode}" 
                                         alt="Scan to Verify Credential" 
                                         style="width: 80px; height: 80px; border-radius: 8px; border: 1px solid var(--border-color); background: #fff; padding: 3px; display: block; margin: 0 auto 6px;" />
                                    <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 500;">Instant QR Verification</span>
                                </div>
                            </div>

                            <!-- Right: Credential Information Table -->
                            <div>
                                <h3 style="font-size: 1.2rem; margin-bottom: 16px; border-bottom: 1px solid var(--border-color); padding-bottom: 10px;">
                                    Credential Metadata
                                </h3>

                                <table style="width: 100%; border-collapse: collapse; font-size: 0.92rem;">
                                    <tbody>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary); width: 40%;">Recipient Name:</td>
                                            <td style="padding: 10px 0; font-weight: 700; color: #fff;">${badge.studentName}</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Student ID:</td>
                                            <td style="padding: 10px 0; font-family: monospace; color: var(--primary-light);">${badge.studentRoll}</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Course / Degree:</td>
                                            <td style="padding: 10px 0; color: #fff;">${badge.studentCourse}</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Skill Module:</td>
                                            <td style="padding: 10px 0; color: #fff;">${badge.moduleName} (${badge.moduleCode})</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Badge Tier:</td>
                                            <td style="padding: 10px 0; font-weight: 700;">${badge.badgeLevel}</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Issue Date:</td>
                                            <td style="padding: 10px 0; color: #fff;">${badge.issueDate}</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Expiry Date:</td>
                                            <td style="padding: 10px 0; color: #fff;">${not empty badge.expiryDate ? badge.expiryDate : 'No Expiry (Lifetime)'}</td>
                                        </tr>
                                        <tr style="border-bottom: 1px solid var(--border-color);">
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Issuing Authority:</td>
                                            <td style="padding: 10px 0; color: #fff;">${badge.issuedBy}</td>
                                        </tr>
                                        <tr>
                                            <td style="padding: 10px 0; color: var(--text-secondary);">Current Status:</td>
                                            <td style="padding: 10px 0; font-weight: 700;">
                                                <c:choose>
                                                    <c:when test="${effectiveStatus eq 'ACTIVE'}">
                                                        <span style="color: var(--status-active);">&#9679; ACTIVE</span>
                                                    </c:when>
                                                    <c:when test="${effectiveStatus eq 'EXPIRED'}">
                                                        <span style="color: var(--status-expired);">&#9679; EXPIRED</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color: var(--status-revoked);">&#9679; REVOKED</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>

                                <!-- Action Buttons -->
                                <div style="display: flex; gap: 12px; margin-top: 24px; flex-wrap: wrap;">
                                    <a href="${pageContext.request.contextPath}/certificate?code=${badge.badgeCode}" 
                                       target="_blank" class="btn btn-primary" style="flex: 1;">
                                       View Printable Certificate &nearr;
                                    </a>
                                    <button type="button" class="btn btn-secondary" onclick="copyVerificationLink('${badge.badgeCode}', this)">
                                        &#128279; Share URL
                                    </button>
                                </div>
                            </div>

                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

