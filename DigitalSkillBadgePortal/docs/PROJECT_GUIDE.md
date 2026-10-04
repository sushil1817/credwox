# CredWox — Woxsen Digital Credential & Verification Portal
## Comprehensive Woxsen University Project & Viva Preparation Guide

---

## 1. Project Overview & Motivation

### The Problem
Traditional college paper certificates, PDF attachments, and physical letters of recommendation are easily forged, hard to verify by potential recruiters, and often get lost. When a student applies for internships or jobs, HR departments often have no convenient, instantaneous method to verify if a student's claimed skill certificate is genuine, expired, or revoked due to disciplinary or academic reasons.

### The Solution
**CredWox** provides Woxsen University with a centralized, cryptographically auditable platform where:
1. **Academic departments** create curriculum modules and issue digital credentials to students with unique verification tokens.
2. **Students** view, manage, and share their earned digital credentials and printable certificates.
3. **Public third parties (recruiters, companies, evaluators)** can verify any credential in seconds without needing an account.
4. **The institution** maintains a complete audit trail of every verification lookup.

---

## 2. Technical Stack Explained (Why these technologies?)

| Technology | Role | Why Used for Web Technology PBL |
| :--- | :--- | :--- |
| **Java 17** | Core Language | Long Term Support (LTS) version of Java providing strong type safety, modern syntax, and enterprise stability. |
| **Java Servlets (4.0)** | Controller Layer | Server-side Java classes that handle HTTP requests (`doGet`, `doPost`), process business logic, manage sessions, and forward responses. |
| **JSP (JavaServer Pages)** | View Layer | Server-side templating engine that merges HTML with dynamic data using JSTL (`<c:forEach>`, `<c:if>`) and Expression Language (`${}`). |
| **JDBC** | Data Access Layer | Java Database Connectivity API using `PreparedStatement` to communicate directly with MySQL without the overhead of heavy ORM frameworks. |
| **MySQL 8** | Relational Database | Reliable relational database enforcing ACID properties, foreign key integrity, and indexing. |
| **Apache Tomcat 9** | Servlet Container / Server | Standard Java EE web server that executes Java Servlets and compiles JSP files into servlets at runtime. |
| **HTML5 & Vanilla CSS3** | User Interface | Custom modern UI with glassmorphism, responsive CSS Grid/Flexbox, and print stylesheets without bulky third-party CSS frameworks. |
| **Vanilla JavaScript** | Client Behavior | Provides client-side form validation, copy-to-clipboard functionality, print invocation, and live filtering. |

---

## 3. Software Architecture: Model-View-Controller (MVC) Pattern

This project strictly adheres to the standard enterprise **MVC Architecture**:

```
                 HTTP Request
       [Browser] ------------> [AuthFilter]
                                   |
                                   v
                            [Controller / Servlet]
                                   |
                     +-------------+-------------+
                     |                           |
                     v                           v
              [Business / DAO]               [Session]
                     |                           |
                     v                           |
                (Database)                       |
                     |                           |
                     v (Populates Model POJO)    |
              [Badge / Student]                  |
                     |                           |
                     +-------------+-------------+
                                   |
                         Passes data via request.setAttribute()
                                   |
                                   v
                            [View / JSP Page]
                                   |
                             HTML Response
                                   v
                               [Browser]
```

### 1. Model (`com.badge.model`)
Plain Old Java Objects (POJOs) representing domain entities:
- `Student.java`: Models student roll ID, name, email, course, and earned badge metrics.
- `Badge.java`: Models badge code, level, issue/expiry dates, status, joined student and module details, and status helper methods.
- `SkillModule.java`: Models curriculum module codes and descriptions.
- `Admin.java`: Models faculty administrator credentials.
- `VerificationRecord.java`: Models audit log entries of public lookup attempts.

### 2. View (`src/main/webapp/*.jsp`)
JSP files rendering HTML responses:
- `index.jsp`: Landing showcase and quick verification lookup.
- `verify.jsp`: Public verification results page displaying active, expired, or revoked statuses.
- `certificate.jsp`: Official printable certificate layout styled with CSS `@media print`.
- `dashboard.jsp` & `badges.jsp`: Student credential repositories.
- `admin/*.jsp`: Department management console pages.

### 3. Controller (`com.badge.servlet.*`)
Java Servlets intercepting HTTP GET and POST requests:
- Process user parameters (`request.getParameter()`).
- Call DAO methods to read or write data.
- Attach computed models to the request (`request.setAttribute("badge", badge)`).
- Forward to the appropriate JSP (`request.getRequestDispatcher("/verify.jsp").forward(request, response)`).

### 4. Data Access Layer (`com.badge.dao.*`)
Encapsulates SQL operations using JDBC `PreparedStatement`:
- `StudentDAO`: Student registration, credential check, profile updates, and cohort metrics.
- `BadgeDAO`: Issuing badges, querying student badges, filtering, revoking, and code lookups.
- `ModuleDAO`: Skill module CRUD.
- `AdminDAO`: Faculty authentication and 6-metric platform aggregations.
- `VerificationDAO`: Real-time audit recording of lookup attempts.

---

## 4. Database Schema & Relational Design

The database is named `badge_portal` and comprises 5 relational tables:

```
+--------------------+        +--------------------+
|      students      |        |      modules       |
+--------------------+        +--------------------+
| id (PK)            |<---+   | id (PK)            |<---+
| student_id (UNIQUE)|    |   | module_code(UNIQUE)|    |
| name               |    |   | module_name        |    |
| email (UNIQUE)     |    |   | description        |    |
| password (HASHED)  |    |   +--------------------+    |
| course             |    |                             |
+--------------------+    |   +--------------------+    |
                          |   |       admins       |    |
                          |   +--------------------+    |
                          |   | id (PK)            |    |
                          |   | username (UNIQUE)  |    |
                          |   | password (HASHED)  |    |
                          |   +--------------------+    |
                          |                             |
                          +-------------+               |
                                        |               |
                              +--------------------+    |
                              |       badges       |    |
                              +--------------------+    |
                              | id (PK)            |    |
                              | badge_code (UNIQUE)|    |
                              | student_id (FK)----+----+
                              | module_id (FK)-----+----+
                              | badge_level (ENUM) |
                              | issue_date         |
                              | expiry_date        |
                              | status (ENUM)      |
                              | issued_by          |
                              | revoked_reason     |
                              +--------------------+

                              +----------------------+
                              | verification_history |
                              +----------------------+
                              | id (PK)              |
                              | badge_code (INDEX)   |
                              | verification_time    |
                              | verification_result  |
                              | ip_address           |
                              +----------------------+
```

### Status Lifecycle Management
A badge has 3 distinct operational statuses:
1. **`ACTIVE`**: The badge is valid, verified, and within its expiration window (or has lifetime validity).
2. **`EXPIRED`**: The badge was legitimately awarded, but the current date is strictly after `expiry_date`. Calculated dynamically via `badge.getEffectiveStatus()`.
3. **`REVOKED`**: The badge was officially invalidated by faculty due to academic misconduct or policy violations. Displays the stated reason.

---

## 5. Security & Best Practices

### 1. SQL Injection Defense
Every single database query uses **Parameterized PreparedStatements**. User inputs are treated strictly as literal values, completely neutralizing SQL injection attacks:
```java
// SAFE: Input parameters cannot modify SQL AST
String sql = "SELECT * FROM badges WHERE UPPER(badge_code) = UPPER(?)";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1, badgeCode.trim());
```

### 2. Password Hashing (SHA-256)
Passwords are never stored in plain text. When a student registers or an admin logs in, `PasswordUtil.hashPassword()` computes a deterministic cryptographic SHA-256 hash. Even if the database were compromised, raw passwords cannot be reversed.

### 3. Session Authentication & Authorization Filter (`AuthFilter`)
A centralized servlet filter intercepts requests:
- Unauthenticated requests to `/student/*` are redirected to `/login.jsp`.
- Unauthenticated requests to `/admin/*` (except `/admin/login`) are redirected to `/admin/login`.
- Prevents horizontal privilege escalation (students cannot view admin pages).

### 4. HTTP Cache Prevention Headers
On authenticated routes, `AuthFilter` injects:
```java
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
```
This prevents sensitive student or admin pages from being viewed via the browser's "Back" button after logout.

---

## 6. End-to-End Execution Trace: Public Verification

To explain how the application works during your viva, trace this exact flow:

1. **User action**: A recruiter visits `verify.jsp` and enters `DSB-GOLD-202601`.
2. **Controller invocation**: The browser sends a GET request to `/verify?code=DSB-GOLD-202601`, matching `@WebServlet(name = "VerifyServlet", urlPatterns = {"/verify"})`.
3. **Audit and Lookup**:
   - `VerifyServlet.java` extracts client IP (`request.getRemoteAddr()`).
   - It calls `badgeDAO.getBadgeByCode("DSB-GOLD-202601")`.
   - `BadgeDAO` executes a SQL `JOIN` across `badges`, `students`, and `modules` tables.
4. **Status Evaluation**:
   - The badge entity is found.
   - `badge.getEffectiveStatus()` evaluates if `expiryDate < today` or if `status == 'REVOKED'`.
   - Result: `ACTIVE`.
5. **Audit Logging**:
   - `verificationDAO.recordVerification("DSB-GOLD-202601", "ACTIVE", clientIp)` inserts an audit record into `verification_history`.
6. **View Rendering**:
   - `request.setAttribute("badge", badge)` sets the model into the request scope.
   - The servlet forwards to `/verify.jsp`.
   - JSTL renders the verified student name, skill module, Gold badge emblem, and the link to `certificate.jsp`.

---

## 7. Viva Questions & Model Answers

### Q1: What is a Java Servlet and what is its lifecycle?
**Answer**: A Servlet is a Java class that extends the capabilities of a web server to handle HTTP requests and generate dynamic responses. Its lifecycle consists of three primary phases managed by the servlet container (Tomcat):
1. `init()`: Called once when the servlet is first instantiated to load configurations.
2. `service()`: Called on every incoming HTTP request, dispatching to `doGet()` or `doPost()`.
3. `destroy()`: Called once when the application server shuts down or undeploys the servlet to release resources.

---

### Q2: What is the difference between JSP and Servlets?
**Answer**:
- **Servlets** are Java files that can generate HTML. They are best suited as **Controllers** because they excel at routing, input validation, and business logic.
- **JSPs** are HTML files with embedded Java tags. They are best suited as **Views** because designing visual layouts is easier in HTML/CSS.
- In modern Java web applications (like ours), Servlets process requests, retrieve data, and forward the data to JSPs for rendering using JSTL and Expression Language (`${}`).

---

### Q3: How do you prevent SQL Injection in this project?
**Answer**: By strictly using `PreparedStatement` instead of regular `Statement` for all database interactions. `PreparedStatement` pre-compiles the SQL query structure in MySQL and sends user inputs as separate data parameters. This guarantees that special characters like `' OR '1'='1` cannot alter the query logic.

---

### Q4: How does session management work in this application?
**Answer**: We use Java's `HttpSession`. When a student or administrator logs in successfully:
- A new session is created with `request.getSession(true)`.
- The user object is saved in the session (`session.setAttribute("studentUser", student)`).
- Tomcat sets a cookie named `JSESSIONID` in the user's browser.
- Subsequent requests send this cookie, allowing our `AuthFilter` to check if a valid session exists.
- On logout, `session.invalidate()` destroys the session and clears all session-bound attributes.

---

### Q5: What is the role of `web.xml` vs `@WebServlet` annotations?
**Answer**:
- `@WebServlet` is a Servlet 3.0+ annotation placed directly on Java servlet classes to define URL mappings without XML configuration.
- `web.xml` (the deployment descriptor) provides application-wide configurations, such as session timeout durations (30 minutes), welcome file lists (`index.jsp`), character encoding filters, and global error page mappings (e.g. 404).

---

### Q6: Why did you use JSTL instead of scriptlets `<% ... %>` in JSP?
**Answer**: JSP Scriptlets (`<% Java code %>`) mix Java logic into HTML, violating the separation of concerns and making code difficult to read and maintain. JSTL (Java Standard Tag Library) provides clean, readable declarative tags like `<c:if>` and `<c:forEach>` that work seamlessly with Expression Language (`${}`), keeping the presentation layer clean.

---

### Q7: How does the public verification feature work without requiring login?
**Answer**: The `/verify` route is intentionally excluded from the authentication filter (`AuthFilter`). Anyone with a badge verification code can send a request to `VerifyServlet`. The servlet looks up the badge in the database using JDBC, checks whether it is active, expired, or revoked, logs the lookup in `verification_history`, and displays the public credential details on `verify.jsp`.

---

### Q8: What happens when an expired badge is verified?
**Answer**: The `Badge` model contains an `isExpired()` method that compares the badge's `expiry_date` with the current system date (`LocalDate.now()`). If the expiration date has passed, the effective status is calculated as `EXPIRED`. The public verification page displays an amber warning banner showing the date it expired, and the audit log records the verification result as `EXPIRED`.

---

### Q9: How is badge revocation handled?
**Answer**: Only administrators can revoke badges. When an admin selects "Revoke" on the admin console, they provide a reason. The servlet updates the `status` column to `'REVOKED'` and sets `revoked_reason`. When anyone later verifies the code, the portal warns that the badge was officially revoked and displays the administrative reason.

---

### Q10: How does JDBC connect to MySQL in this project?
**Answer**:
1. We register the MySQL JDBC driver: `Class.forName("com.mysql.cj.jdbc.Driver")`.
2. The `DBUtil` class reads the JDBC URL (`jdbc:mysql://localhost:3306/badge_portal`), username, and password from `db.properties`.
3. It obtains a connection via `DriverManager.getConnection()`.
4. After executing statements and reading result sets, `DBUtil.closeQuietly()` safely closes resources to avoid database connection leaks.
