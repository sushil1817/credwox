<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="CredWox — Woxsen Digital Credential & Verification Portal" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/navbar.jsp" />

<main class="main-content">
    <div class="container">

        <!-- Hero Section -->
        <section class="hero">
            <div class="hero-pill">
                <span>&#10024; Woxsen University Institutional Authority</span>
                <span>&bull;</span>
                <span>Tamper-Resistant Digital Credentials</span>
            </div>
            <h1 class="hero-title">CredWox</h1>
            <p class="hero-subtitle">
                Woxsen Digital Credential &amp; Verification Portal &mdash; Empowering students to showcase authenticated technical competencies and allowing recruiters and organizations to verify academic credentials instantly.
            </p>

            <!-- Quick Public Verification Box -->
            <form action="${pageContext.request.contextPath}/verify" method="GET" class="verify-search-box">
                <input type="text" name="code" id="verifyCodeInput" class="verify-input" 
                       placeholder="Enter Credential Verification Code (e.g. CWX-GOLD-202601)" required autocomplete="off">
                <button type="submit" class="btn btn-primary btn-lg">Verify Credential</button>
            </form>

            <!-- Sample Test Badges Quick Selector for Demo / Viva Evaluators -->
            <div style="margin-top: 20px; display: flex; align-items: center; justify-content: center; gap: 10px; flex-wrap: wrap;">
                <span style="font-size: 0.85rem; color: var(--text-muted); font-weight: 600;">Try Sample Codes:</span>
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
        </section>

        <!-- Platform Metric Highlights -->
        <section style="margin: 50px 0;">
            <div class="stats-grid">
                <div class="stat-box">
                    <div class="stat-icon" style="background: rgba(99, 102, 241, 0.15); color: var(--primary-light);">
                        &#127891;
                    </div>
                    <div>
                        <div class="stat-val">${not empty stats.totalStudents ? stats.totalStudents : '4+'}</div>
                        <div class="stat-label">Registered Students</div>
                    </div>
                </div>

                <div class="stat-box">
                    <div class="stat-icon" style="background: rgba(245, 158, 11, 0.15); color: var(--gold-primary);">
                        &#127941;
                    </div>
                    <div>
                        <div class="stat-val">${not empty stats.totalBadges ? stats.totalBadges : '5+'}</div>
                        <div class="stat-label">Issued Badges</div>
                    </div>
                </div>

                <div class="stat-box">
                    <div class="stat-icon" style="background: rgba(16, 185, 129, 0.15); color: var(--status-active);">
                        &#9989;
                    </div>
                    <div>
                        <div class="stat-val">${not empty stats.activeBadges ? stats.activeBadges : '3+'}</div>
                        <div class="stat-label">Active Credentials</div>
                    </div>
                </div>

                <div class="stat-box">
                    <div class="stat-icon" style="background: rgba(6, 182, 212, 0.15); color: var(--secondary);">
                        &#128269;
                    </div>
                    <div>
                        <div class="stat-val">${not empty stats.totalVerifications ? stats.totalVerifications : '4+'}</div>
                        <div class="stat-label">Verifications Audited</div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Badge Tiers Section -->
        <section style="margin-bottom: 60px;">
            <div style="text-align: center; margin-bottom: 40px;">
                <h2 style="font-size: 2rem; margin-bottom: 10px;">Three-Tier Skill Hierarchy</h2>
                <p style="color: var(--text-secondary); max-width: 600px; margin: 0 auto;">
                    Students earn stratified recognition reflecting their mastery depth, project complexity, and assessment evaluations.
                </p>
            </div>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 28px;">
                <!-- Gold Tier -->
                <div class="badge-card tier-gold">
                    <div class="badge-emblem">
                        <svg viewBox="0 0 24 24" fill="currentColor">
                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                        </svg>
                    </div>
                    <div class="badge-level-tag">Gold Badge</div>
                    <h3 class="badge-module-title">Advanced Mastery</h3>
                    <p style="font-size: 0.9rem; color: var(--text-secondary); margin-bottom: 16px;">
                        Awarded for capstone project excellence, architectural design, and top-percentile technical assessments.
                    </p>
                    <span class="status-pill status-active"><span class="status-dot"></span>Highest Honor</span>
                </div>

                <!-- Silver Tier -->
                <div class="badge-card tier-silver">
                    <div class="badge-emblem">
                        <svg viewBox="0 0 24 24" fill="currentColor">
                            <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm-2 16l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z"/>
                        </svg>
                    </div>
                    <div class="badge-level-tag">Silver Badge</div>
                    <h3 class="badge-module-title">Proficient Competency</h3>
                    <p style="font-size: 0.9rem; color: var(--text-secondary); margin-bottom: 16px;">
                        Awarded for proficient practical laboratory execution, coursework completion, and demonstrated skill application.
                    </p>
                    <span class="status-pill status-active"><span class="status-dot"></span>Core Proficient</span>
                </div>

                <!-- Bronze Tier -->
                <div class="badge-card tier-bronze">
                    <div class="badge-emblem">
                        <svg viewBox="0 0 24 24" fill="currentColor">
                            <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 14l-5-5 1.41-1.41L12 14.17l7.59-7.59L21 8l-9 9z"/>
                        </svg>
                    </div>
                    <div class="badge-level-tag">Bronze Badge</div>
                    <h3 class="badge-module-title">Foundational Proficiency</h3>
                    <p style="font-size: 0.9rem; color: var(--text-secondary); margin-bottom: 16px;">
                        Awarded for solid fundamental comprehension, module prerequisites, and foundational assessments.
                    </p>
                    <span class="status-pill status-active"><span class="status-dot"></span>Foundation</span>
                </div>
            </div>
        </section>

        <!-- How It Works Section -->
        <section class="card" style="margin-bottom: 40px;">
            <div class="card-header">
                <h2 class="card-title">How Digital Skill Badging Works</h2>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 24px;">
                <div style="padding: 16px;">
                    <div style="font-size: 1.8rem; font-weight: 800; color: var(--primary-light); margin-bottom: 8px;">01</div>
                    <h3 style="font-size: 1.1rem; margin-bottom: 8px;">Curriculum &amp; Assessment</h3>
                    <p style="font-size: 0.88rem; color: var(--text-secondary);">
                        Students enroll and complete specific academic modules such as Full-Stack Web Development, Data Structures, or Cybersecurity.
                    </p>
                </div>

                <div style="padding: 16px;">
                    <div style="font-size: 1.8rem; font-weight: 800; color: var(--primary-light); margin-bottom: 8px;">02</div>
                    <h3 style="font-size: 1.1rem; margin-bottom: 8px;">Faculty Issuance</h3>
                    <p style="font-size: 0.88rem; color: var(--text-secondary);">
                        Department administrators evaluate student performance and issue a tamper-resistant badge with a unique verification code.
                    </p>
                </div>

                <div style="padding: 16px;">
                    <div style="font-size: 1.8rem; font-weight: 800; color: var(--primary-light); margin-bottom: 8px;">03</div>
                    <h3 style="font-size: 1.1rem; margin-bottom: 8px;">Public Verification</h3>
                    <p style="font-size: 0.88rem; color: var(--text-secondary);">
                        Anyone can verify the authenticity, issue date, and validity status (ACTIVE, EXPIRED, REVOKED) without logging in.
                    </p>
                </div>
            </div>
        </section>

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

