<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Curriculum Modules — CredWox | Woxsen University" />
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/admin-navbar.jsp" />

<main class="main-content">
    <div class="container">

        <div style="margin-bottom: 28px;">
            <h1 style="font-size: 2rem; margin-bottom: 4px;">Curriculum &amp; Skill Modules</h1>
            <p style="color: var(--text-secondary); font-size: 0.95rem;">
                Define courses and competencies for which Woxsen digital credentials can be awarded.
            </p>
        </div>

        <!-- Success / Error Feedback -->
        <c:if test="${param.success eq 'added'}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>New skill module added successfully.</span>
            </div>
        </c:if>
        <c:if test="${param.success eq 'updated'}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>Skill module updated successfully.</span>
            </div>
        </c:if>
        <c:if test="${param.success eq 'deleted'}">
            <div class="alert alert-success alert-auto-dismiss">
                <span>Skill module removed successfully.</span>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                <span>${errorMessage}</span>
            </div>
        </c:if>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 28px; align-items: start;">
            
            <!-- Add New Module Form -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">&#10133; Add Skill Module</h2>
                </div>

                <form action="${pageContext.request.contextPath}/admin/modules" method="POST">
                    <input type="hidden" name="action" value="add">

                    <div class="form-group">
                        <label class="form-label" for="moduleCode">Module Code *</label>
                        <input type="text" id="moduleCode" name="moduleCode" class="form-control" 
                               placeholder="e.g. WT-101, DS-201, AI-401" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="moduleName">Module / Course Name *</label>
                        <input type="text" id="moduleName" name="moduleName" class="form-control" 
                               placeholder="e.g. Full-Stack Web Development" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="description">Competency Description</label>
                        <textarea id="description" name="description" class="form-control" rows="4" 
                                  placeholder="Describe the skills and learning objectives verified by this module..."></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%;">Create Module &rarr;</button>
                </form>
            </div>

            <!-- Existing Modules List -->
            <div class="card" style="grid-column: span 2;">
                <div class="card-header">
                    <h2 class="card-title">Curriculum Catalog (${modules.size()})</h2>
                </div>

                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Module Name</th>
                                <th>Description</th>
                                <th style="text-align: center;">Badges Issued</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="m" items="${modules}">
                                <tr>
                                    <td style="font-family: monospace; font-weight: 700; color: var(--secondary);">${m.moduleCode}</td>
                                    <td><strong>${m.moduleName}</strong></td>
                                    <td style="font-size: 0.86rem; color: var(--text-secondary); max-width: 280px;">${m.description}</td>
                                    <td style="text-align: center;">
                                        <span class="user-badge" style="font-size: 0.8rem;">${m.badgesIssuedCount}</span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/modules" method="POST" onsubmit="return confirm('Are you sure you want to delete module ${m.moduleCode}?');" style="display: inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${m.id}">
                                            <button type="submit" class="btn btn-secondary btn-sm" style="color: #f87171;" 
                                                    ${m.badgesIssuedCount > 0 ? 'disabled title="Cannot delete module with active badges"' : ''}>
                                                Delete
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

    </div>
</main>

<jsp:include page="/includes/footer.jsp" />

