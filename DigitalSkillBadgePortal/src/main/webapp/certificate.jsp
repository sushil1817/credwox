<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Official Digital Credential — ${badge.studentName} | CredWox" />
<jsp:include page="/includes/header.jsp" />

<div class="no-print" style="background: rgba(11, 15, 25, 0.95); border-bottom: 1px solid var(--border-color); padding: 14px 0;">
    <div class="container" style="display: flex; justify-content: space-between; align-items: center; flex-wrap: gap; gap: 12px;">
        <a href="${pageContext.request.contextPath}/verify?code=${badge.badgeCode}" class="btn btn-secondary btn-sm">&larr; Back to Verification</a>
        <div style="display: flex; gap: 10px;">
            <button onclick="printCertificate()" class="btn btn-primary btn-sm">&#128424; Print / Save as PDF</button>
            <button onclick="copyToClipboard('${badge.badgeCode}', this)" class="btn btn-secondary btn-sm">&#128203; Copy Code</button>
        </div>
    </div>
</div>

<main class="main-content" style="padding: 40px 0;">
    <div class="container">

        <!-- Printable Certificate Card -->
        <div class="certificate-frame">
            
            <div class="cert-header">
                <div class="cert-institution">WOXSEN UNIVERSITY</div>
                <div style="font-size: 0.9rem; font-weight: 600; color: #475569; margin-top: 4px;">School of Technology &bull; Office of Academic Credentials</div>
                <h1 class="cert-title">CERTIFICATE OF COMPETENCY MASTERY</h1>
                <div class="cert-subtitle">This official digital credential is authenticated by CredWox and conferred upon</div>
            </div>

            <div class="cert-recipient-name">${badge.studentName}</div>
            
            <div class="cert-course-info">
                Student ID: <strong>${badge.studentRoll}</strong> &bull; ${badge.studentCourse}
            </div>

            <div class="cert-desc">
                In formal recognition of successfully passing comprehensive assessment benchmarks, demonstrating practical laboratory implementation, and mastering competency objectives in:
            </div>

            <div style="background: #f8fafc; border: 1px solid #cbd5e1; border-radius: 8px; padding: 18px 24px; max-width: 650px; margin: 0 auto 28px;">
                <div style="font-size: 1.35rem; font-weight: 800; color: #0f172a; margin-bottom: 6px;">
                    ${badge.moduleName}
                </div>
                <div style="font-size: 0.9rem; color: #475569;">
                    Module Code: <strong>${badge.moduleCode}</strong> &bull; Level: <strong style="color: #b45309;">${badge.badgeLevel} Tier</strong>
                </div>
            </div>

            <!-- Emblem / Ribbon & Dynamic QR Code -->
            <div style="display: flex; align-items: center; justify-content: center; gap: 36px; margin: 25px 0; flex-wrap: wrap;">
                <!-- Official Gold Ribbon Emblem -->
                <div style="width: 84px; height: 84px; border-radius: 50%; background: linear-gradient(135deg, #f59e0b 0%, #b45309 100%); display: flex; align-items: center; justify-content: center; color: #fff; font-size: 2.2rem; box-shadow: 0 4px 14px rgba(180, 83, 9, 0.4);">
                    &#127942;
                </div>

                <!-- Dynamic Scannable QR Code -->
                <div style="text-align: center;">
                    <img src="https://api.qrserver.com/v1/create-qr-code/?size=110x110&margin=4&data=http://localhost:8080${pageContext.request.contextPath}/verify?code=${badge.badgeCode}" 
                         alt="Scan to Verify Credential" 
                         style="width: 84px; height: 84px; border: 2px solid #cbd5e1; border-radius: 8px; padding: 2px; background: #fff; display: block; margin: 0 auto 4px;" />
                    <span style="font-size: 0.7rem; color: #64748b; font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;">Scan to Verify</span>
                </div>

                <!-- Token & Status -->
                <div style="text-align: left;">
                    <div style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 1px; color: #64748b;">Verification Token</div>
                    <div style="font-size: 1.25rem; font-family: monospace; font-weight: 800; color: #1e3a8a;">
                        ${badge.badgeCode}
                    </div>
                    <div style="font-size: 0.8rem; color: #10b981; font-weight: 700; margin-top: 3px;">
                        Status: &bull; ${badge.effectiveStatus}
                    </div>
                </div>
            </div>

            <!-- Footer Signatures and Dates -->
            <div class="cert-footer">
                <div class="cert-sig-block">
                    <div style="font-size: 0.82rem; color: #64748b; margin-bottom: 6px;">Issued On: <strong>${badge.issueDate}</strong></div>
                    <div style="font-size: 0.82rem; color: #64748b;">Valid Thru: <strong>${not empty badge.expiryDate ? badge.expiryDate : 'Lifetime'}</strong></div>
                </div>

                <div class="cert-sig-block">
                    <div style="font-family: 'Brush Script MT', cursive; font-size: 1.6rem; color: #1e3a8a; line-height: 1;">
                        ${badge.issuedBy}
                    </div>
                    <div class="cert-sig-line"></div>
                    <div class="cert-sig-name">${badge.issuedBy}</div>
                    <div class="cert-sig-title">Program Coordinator &amp; Authorized Signatory</div>
                </div>
            </div>

        </div>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

