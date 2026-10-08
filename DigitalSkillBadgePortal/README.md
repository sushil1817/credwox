# CredWox — Digital Skill Badge & Verification Portal

CredWox is an enterprise-grade digital credential and verification platform developed for Woxsen University's Web Technology PBL project. It empowers academic authorities to issue verifiable digital credentials to students, allows students to showcase official certificates, and provides employers and recruiters with instant, tamper-proof QR verification.

---

## Technical Stack
- **Language:** Java 17 (LTS)
- **Controller Layer:** Java Servlets 4.0
- **View Layer:** JSP (JavaServer Pages) & JSTL 1.2
- **Persistence Layer:** JDBC (MySQL Connector/J 8.3.0)
- **Database:** MySQL 8.x (Railway Cloud MySQL / Local MySQL)
- **Application Server:** Apache Tomcat 9.0 (Dockerized Temurin-17)
- **Frontend:** HTML5, Modern Vanilla CSS3 (Glassmorphism & Responsive Design), Vanilla JavaScript
- **Cloud Infrastructure:** Render (Docker Web Service) & Railway (Managed MySQL 8)

---

## Project Structure
```
DigitalSkillBadgePortal/
├── database/
│   ├── schema.sql           # MySQL database schema (DDL)
│   └── seed.sql             # Demo data with required students & credentials
├── docs/
│   └── PROJECT_GUIDE.md     # Architecture and viva reference guide
├── src/
│   ├── main/
│   │   ├── java/com/badge/  # Servlets, DAOs, Filters, Models, Utilities
│   │   ├── resources/       # Configuration templates (db.properties.example)
│   │   └── webapp/          # JSP views, CSS stylesheets, and JavaScript
│   └── test/
│       └── java/com/badge/  # Automated unit and deployment test suite
├── Dockerfile               # Multi-stage production container build (Java 17 + Tomcat 9)
├── pom.xml                  # Maven Project Object Model configuration
└── README.md                # Deployment and documentation guide
```

---

## Cloud Deployment Guide

### 1. Railway MySQL Database Setup
1. Log in to [Railway](https://railway.app) and create a **MySQL** database service.
2. In the MySQL service settings, enable **Public Networking** to allow external connections.
3. Railway provides connection variables under the **Variables** or **Connect** tab:
   - `MYSQL_PUBLIC_URL` (format: `mysql://root:<password>@<host>:<port>/railway`)
   - Or separate connection variables: `MYSQLHOST`, `MYSQLPORT`, `MYSQLUSER`, `MYSQLPASSWORD`, `MYSQLDATABASE`
4. **Initialize Database Tables:**
   - Go to the **Query** / **Data** tab in the Railway dashboard (or connect via MySQL Workbench / CLI using `MYSQL_PUBLIC_URL`).
   - Execute [`database/schema.sql`](database/schema.sql) to create tables (`admins`, `students`, `modules`, `badges`, `verification_history`).
   - Execute [`database/seed.sql`](database/seed.sql) to populate demo data (including students Sushil Pal, Tanishq Hanumanta, and Suhaan Kapoor).
   *(Note: `seed.sql` is a manual demo script and is never run automatically on application startup to protect production data).*

---

### 2. Render Web Service Deployment
1. Log in to [Render](https://render.com) and create a **New Web Service**.
2. Connect your GitHub repository: `https://github.com/sushil1817/credwox.git`.
3. Configure the service settings:
   - **Service Name:** `credwox`
   - **Region:** Singapore (or closest to your Railway database)
   - **Branch:** `main`
   - **Root Directory:** `DigitalSkillBadgePortal`
   - **Runtime:** `Docker`
   - **Instance Type:** `Free`
4. The service will use the multi-stage [`Dockerfile`](Dockerfile) which:
   - Compiles with Maven 3.9 on JDK 17
   - Deploys the packaged WAR as `ROOT.war` on Tomcat 9
   - Dynamically adapts Tomcat's listening port to Render's `$PORT` environment variable

---

### 3. Environment Variables Configuration (Render)
Under your Render Web Service **Environment** settings, configure the following variables:

| Variable | Required | Description | Example / Note |
| :--- | :--- | :--- | :--- |
| `DB_URL` | Yes | JDBC URL pointing to Railway MySQL | `jdbc:mysql://<host>:<port>/railway` or `mysql://...` |
| `DB_USERNAME` | Yes* | Railway MySQL user | e.g. `root` (*optional if embedded in DB_URL) |
| `DB_PASSWORD` | Yes* | Railway MySQL password | Provided by Railway (*optional if embedded in DB_URL) |
| `APP_BASE_URL` | Yes | Public HTTPS URL of the deployed Render app | `https://credwox.onrender.com` |

> **Note on URL Normalization:** `DBUtil` automatically parses and normalizes Railway's `mysql://` and `jdbc:mysql://` formats, adds required MySQL 8 parameters (`allowPublicKeyRetrieval=true`, `serverTimezone=UTC`, `characterEncoding=UTF-8`), and masks all credentials in server logs.

---

### 4. Public QR Code Verification Flow
In production, QR codes must resolve to the public Render domain rather than `localhost`:
1. When `APP_BASE_URL` is set (e.g. `https://credwox.onrender.com`), all QR codes and verification links use `${APP_BASE_URL}/verify?code=<badgeCode>`.
2. If `APP_BASE_URL` is omitted, `AppUtil` dynamically resolves the domain from reverse proxy headers (`X-Forwarded-Proto` and `X-Forwarded-Host`).
3. **Verification Flow:**
   ```
   Mobile Phone Scanner
           │
           ▼
   Scans Dynamic QR Code on Certificate / Badge
           │
           ▼
   https://credwox.onrender.com/verify?code=CWX-GOLD-202601
           │
           ▼
   VerifyServlet (Root Context /)
           │
           ▼
   JDBC Connection to Railway MySQL
           │
           ▼
   Audits Lookup in `verification_history` table
           │
           ▼
   Displays Tamper-Proof Verification Result (ACTIVE / REVOKED / EXPIRED)
   ```

---

### 5. Health Check Monitoring Endpoint
- **URL:** `/health` (e.g. `https://credwox.onrender.com/health`)
- **HTTP Status:** `200 OK`
- **Output:**
  ```text
  OK
  Status: UP
  Database: CONNECTED
  ```
- **Resilience:** Database connection failures are handled gracefully without crashing the container or failing the `/health` HTTP 200 response.

---

## Local Development & Testing

### 1. Requirements
- Java 17 (Adoptium / Eclipse Temurin recommended)
- Maven 3.8+
- MySQL 8.0 running locally on port 3306

### 2. Local Build & Test
```bash
cd DigitalSkillBadgePortal
mvn clean test
mvn clean package
```
All unit tests in `src/test/java/` run automatically during `mvn test`.

### 3. Running Locally
Run `start-portal.bat` from the repository root, or manually deploy `target/DigitalSkillBadgePortal.war` to Apache Tomcat 9 `webapps/ROOT.war`.
- Portal Homepage: `http://localhost:8080/`
- Verification Portal: `http://localhost:8080/verify`
- Health Endpoint: `http://localhost:8080/health`
- Student Login: `http://localhost:8080/login.jsp`
- Admin Console: `http://localhost:8080/admin/login`

---

## Demo Credentials

### Administrator Portal
- **URL:** `/admin/login`
- **Username:** `admin`
- **Password:** `admin123`

### Student Credentials
- **Default Student Password:** `student123`
1. **Sushil Pal:** Roll `25WU0101141` | Email `sushil.pal@woxsen.edu.in`
2. **Tanishq Hanumanta:** Roll `25WU0101142` | Email `tanishq.hanumanta@woxsen.edu.in`
3. **Suhaan Kapoor:** Roll `25WU0104027` | Email `suhaan.kapoor@woxsen.edu.in`

### Test Verification Badges
- **Valid / Active:** `CWX-GOLD-202601` (Sushil Pal — Full-Stack Web Development)
- **Active Credential:** `CWX-SILV-202602` (Tanishq Hanumanta — Data Structures)
- **Expired Credential:** `CWX-EXPD-202501` (Suhaan Kapoor — Cloud Native Systems)
- **Revoked Credential:** `CWX-REVK-202605` (Sushil Pal — Revocation Demonstration)
