# CredWox — Woxsen Digital Credential & Verification Portal

A secure web-based digital credential management and verification platform developed for Woxsen University. CredWox provides a centralized system for issuing, managing, viewing, and verifying student digital credentials.

The platform allows students to access their credentials, authorized faculty and administrators to manage credential records, and external users to independently verify credentials using a unique verification code or QR code.

Site link - https://credwox.onrender.com/

---

## Submitted By

**Sushil Pal** — `25WU0101141`  
**Tanishq Hanumanta** — `25WU0101142`  
**Suhaan Kapoor** — `25WU0104027`

### Section

**CSE PANTHERS**

### Submitted To

**Prof. Veeresh Biradar**

---

## Features

### Student Portal

- Secure student login
- Session-based authentication
- Student dashboard
- View personal digital credentials
- View credential details
- View digital credential certificate
- Access credential verification information
- QR code based credential verification
- Secure logout

### Faculty Portal

- Faculty authentication
- Faculty dashboard
- View student records
- Manage digital credential records
- Issue and manage credentials
- View credential information
- Monitor credential status

### Admin Portal

- Administrator authentication
- Administrative dashboard
- Manage students
- Manage credentials
- Manage credential status
- View system information
- Monitor credential records

### Public Verification

- Public verification without login
- Verification using unique credential codes
- QR code based verification
- Database-backed verification
- Credential status validation
- Verification history
- Invalid credential detection

### Digital Credentials

- Unique credential identification
- Unique verification code
- Digital credential certificate
- QR code generation
- Credential status management
- Student credential association
- Module/academic information
- Credential verification history

### Credential Status

CredWox supports three major credential states:

- `ACTIVE` — Credential is currently valid.
- `REVOKED` — Credential has been invalidated by an authorized user.
- `EXPIRED` — Credential has passed its validity period.

---

## Technology Stack

| Component | Technology |
|---|---|
| Programming Language | Java 17 |
| Backend | Java Servlets |
| View Layer | JSP |
| Database Connectivity | JDBC |
| Database | MySQL 8 |
| Application Server | Apache Tomcat 9 |
| Frontend | HTML5, CSS3, JavaScript |
| Build Tool | Apache Maven |
| Credential Verification | QR Code |
| Version Control | Git |
| Repository | GitHub |

The project is implemented using core Java Web Technologies without Spring Boot, Hibernate, React, Node.js, Firebase, or MongoDB.

---

## System Architecture

```text
                    ┌──────────────────────┐
                    │   Student / Faculty  │
                    │   Admin / Verifier   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   JSP / HTML / CSS   │
                    │     JavaScript       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │   Java Servlets      │
                    │ Request Handling     │
                    │ Application Logic    │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │        JDBC          │
                    │ Database Connectivity│
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      MySQL 8         │
                    │    Credential Data   │
                    └──────────────────────┘
```

---

## Application Architecture

CredWox follows a server-side Java web architecture:

```text
Client
  ↓
JSP
  ↓
Servlet
  ↓
Business Logic
  ↓
JDBC
  ↓
MySQL
```

### Presentation Layer

The presentation layer consists of JSP, HTML, CSS, and JavaScript.

It provides:

- Login interfaces
- Dashboards
- Credential pages
- Certificate pages
- Verification pages
- Forms
- Navigation
- Responsive layouts

### Application Layer

Java Servlets handle:

- HTTP requests
- Authentication
- Session management
- Credential operations
- Verification requests
- Form processing
- Database interaction

### Data Layer

JDBC is used to communicate with MySQL.

The data layer handles:

- Student records
- Admin records
- Module information
- Credential records
- Verification history

---

## User Roles

### Student

Students can:

- Log in to the portal
- Access their dashboard
- View their digital credentials
- View credential details
- Open their digital certificate
- Access credential verification information

### Faculty

Faculty members can:

- Log in to the faculty portal
- Access the faculty console
- View student information
- Manage credential records
- Manage credential-related information

### Administrator

Administrators can:

- Access the admin console
- Manage student records
- Manage credential records
- Manage credential status
- Monitor the credential system

### Public Verifier

Public users can:

- Open the verification page
- Enter a credential verification code
- Scan a credential QR code
- View the verification result
- Verify the current credential status

Public verification does not require an account.

---

## Credential Lifecycle

```text
Student Achievement
        ↓
Credential Created
        ↓
Credential Stored in Database
        ↓
Unique Verification Code
        ↓
QR Code Generated
        ↓
Digital Credential Certificate
        ↓
Credential Shared
        ↓
Credential Verified
        ↓
ACTIVE / REVOKED / EXPIRED
```

The credential remains connected to its database record throughout its lifecycle.

This allows the verification result to reflect the current state of the credential instead of relying only on a static certificate.

---

## Credential Verification

CredWox provides database-backed credential verification.

The verification process is:

```text
Verification Code / QR Code
             ↓
       Verify Endpoint
             ↓
        Java Servlet
             ↓
       JDBC Database Query
             ↓
        MySQL Database
             ↓
     Credential Retrieved
             ↓
       Status Checked
             ↓
      Verification Result
```

### Valid Credential

If the credential exists and has an `ACTIVE` status, the system displays the credential as valid.

### Revoked Credential

If the credential exists but has a `REVOKED` status, the system displays the credential as revoked.

### Expired Credential

If the credential has expired, the system displays the expired status.

### Invalid Credential

If the verification code does not match a credential in the database, the system reports that the credential could not be verified.

---

## QR Code Verification

Each digital credential can contain a QR code connected to its verification URL.

The intended verification flow is:

```text
Digital Credential Certificate
             ↓
          QR Code
             ↓
       Mobile Camera
             ↓
    Public Verification URL
             ↓
       CredWox Server
             ↓
       MySQL Database
             ↓
    Verification Result
```

This allows an external organization, recruiter, university, or other verifier to verify a credential directly from a mobile device.

For local development, the QR code may point to a localhost URL.

For production deployment, the QR code must point to the publicly accessible CredWox domain.

---

## Database Design

CredWox uses MySQL 8 as the relational database management system.

The main tables are:

```text
admins
students
modules
badges
verification_history
```

### Database Relationship

```text
                    ┌──────────────┐
                    │    admins    │
                    └──────┬───────┘
                           │
                           │ manages
                           ▼
┌──────────────┐     ┌──────────────┐
│   students   │────►│    badges    │
└──────┬───────┘     └──────┬───────┘
       │                     │
       │                     ├──────────────► modules
       │                     │
       │                     ▼
       │             verification_history
       │
       └── Student Credential Association
```

---

## Database Tables

### `admins`

Stores administrator account information used for administrative authentication and access.

### `students`

Stores student information used by the credential management system.

### `modules`

Stores academic/module-related information associated with credentials.

### `badges`

Stores digital credential information, including:

- Credential details
- Student association
- Verification code
- Credential status
- Validity information

### `verification_history`

Stores credential verification activity.

This provides a record of verification requests and can be used for auditing and monitoring.

---

## Security

Security is implemented at multiple levels of the application.

### Password Hashing

User passwords are protected using password hashing rather than storing plain-text passwords.

### Prepared Statements

JDBC prepared statements are used for database operations to reduce SQL injection risks.

### Session Management

Authenticated users are managed using server-side sessions.

### Role-Based Access

Students, faculty, and administrators have separate access levels.

### Server-Side Validation

Important input values are validated on the server before being processed.

### Unique Verification Codes

Each credential has a unique verification identifier.

### Credential Status Validation

The backend checks the current status of a credential before returning the verification result.

### Verification History

Verification activity can be stored for auditing and monitoring.

### Configuration Protection

Sensitive local configuration files are excluded from Git.

The repository provides:

```text
db.properties.example
```

instead of committing real database credentials.

---

## User Interface

CredWox uses a professional academic and technology-oriented interface designed around clarity and usability.

The interface includes:

- Responsive layouts
- Student dashboard
- Faculty console
- Admin console
- Digital credential certificate
- Public verification page
- Login interfaces
- Credential status indicators
- QR-based verification

The visual design uses a professional university-oriented style with dark navy/charcoal elements, neutral backgrounds, restrained accent colors, and clear information hierarchy.

---

## Application Pages

### Home Page

The home page introduces CredWox and provides access to the platform's major functionality.

### Student Login

Provides secure authentication for students.

### Student Dashboard

Allows students to access their credentials and credential-related information.

### Faculty Login

Provides authentication for authorized faculty members.

### Faculty Console

Provides faculty members with access to student and credential management functionality.

### Admin Console

Provides administrators with system-level credential and student management functionality.

### Digital Credential Certificate

Displays a formal digital representation of a student's credential.

The certificate includes credential information and a QR-based verification mechanism.

### Public Verification Page

Allows external users to verify credentials without logging in.

The page checks the submitted verification code against the database and displays the current credential status.

---

## Verification History

Credential verification activity is stored through the `verification_history` table.

The verification history can be used to:

- Track verification requests
- Audit credential usage
- Monitor verification activity
- Detect unusual verification patterns
- Maintain a record of credential verification
=======
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
>>>>>>> 13044f8 (Configure production deployment for Render and Railway MySQL with dynamic QR code verification and /health endpoint)

---

## Project Structure
<<<<<<< HEAD

```text
credwox/
│
├── DigitalSkillBadgePortal/
│   │
│   ├── database/
│   │   ├── schema.sql
│   │   └── seed.sql
│   │
│   ├── docs/
│   │   └── PROJECT_GUIDE.md
│   │
│   ├── src/
│   │   └── main/
│   │       │
│   │       ├── java/
│   │       │   └── ...
│   │       │
│   │       ├── resources/
│   │       │   ├── db.properties.example
│   │       │   └── ...
│   │       │
│   │       └── webapp/
│   │           ├── WEB-INF/
│   │           ├── css/
│   │           ├── js/
│   │           ├── images/
│   │           ├── index.jsp
│   │           ├── verify.jsp
│   │           ├── certificate.jsp
│   │           └── ...
│   │
│   ├── pom.xml
│   └── README.md
│
├── .gitignore
└── README.md
=======
```
├── DigitalSkillBadgePortal/     # Maven Root & Render Deployment Directory
│   ├── database/
│   │   ├── schema.sql           # MySQL database schema (DDL)
│   │   └── seed.sql             # Demo data with required students & credentials
│   ├── docs/
│   │   └── PROJECT_GUIDE.md     # Architecture and viva reference guide
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/badge/  # Servlets, DAOs, Filters, Models, Utilities
│   │   │   ├── resources/       # Configuration templates (db.properties.example)
│   │   │   └── webapp/          # JSP views, CSS stylesheets, and JavaScript
│   │   └── test/
│   │       └── java/com/badge/  # Automated unit and deployment test suite
│   ├── Dockerfile               # Multi-stage production container build (Java 17 + Tomcat 9)
│   ├── pom.xml                  # Maven Project Object Model configuration
│   └── README.md                # Deployment and documentation guide
├── build.bat                    # Local build automation script
├── run-setup-db.bat             # Local database setup script
├── start-portal.bat             # Local one-click server startup
├── stop-portal.bat              # Local server shutdown script
└── README.md                    # Root project documentation
>>>>>>> 13044f8 (Configure production deployment for Render and Railway MySQL with dynamic QR code verification and /health endpoint)
```

---

<<<<<<< HEAD
## Prerequisites

The following software is required:

- Java Development Kit 17
- Apache Maven
- MySQL 8
- Apache Tomcat 9
- Git
- Modern web browser

Check Java installation:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

Check Git:

```bash
git --version
```

---

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/sushil1817/credwox.git
```

### 2. Enter the Project

```bash
cd credwox/DigitalSkillBadgePortal
```

### 3. Create the Database

Open MySQL and execute:

```text
database/schema.sql
```

### 4. Insert Demonstration Data

Execute:

```text
database/seed.sql
```

### 5. Configure Database Connection

Create a local database configuration using:

```text
src/main/resources/db.properties.example
```

Create:

```text
db.properties
```

and configure the MySQL connection.

Example:

```properties
db.url=jdbc:mysql://localhost:3306/credwox
db.username=root
db.password=your_password
```

Do not commit this file if it contains real credentials.

---

## Build

From the `DigitalSkillBadgePortal` directory:

```bash
mvn clean package
```

After a successful build, Maven generates:

```text
target/DigitalSkillBadgePortal.war
```

---

## Running with Apache Tomcat

Copy the generated WAR file into the Tomcat `webapps` directory.

Start Apache Tomcat.

The application can be accessed at:

```text
http://localhost:8080/DigitalSkillBadgePortal/
```

---

## Local Development Flow

```text
MySQL
  ↓
Database Schema
  ↓
Seed Data
  ↓
Java Application
  ↓
Maven Build
  ↓
WAR File
  ↓
Apache Tomcat
  ↓
Browser
```

---

## Configuration

CredWox supports database configuration through local properties and environment-based configuration.

Important configuration values include:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Local development can use:

```text
db.properties
```

The repository provides:

```text
db.properties.example
```

as a safe configuration reference.

Production credentials should be provided through environment variables or secure server configuration rather than committed to the repository.

---

## Production Deployment

To make QR verification accessible from mobile phones and external organizations, CredWox must be deployed to a publicly accessible server.

The production architecture is:

```text
                Internet
                   │
                   ▼
            Public CredWox URL
                   │
                   ▼
             Apache Tomcat
                   │
                   ▼
             Java Application
                   │
                   ▼
              MySQL Database
```

The QR code should point to the public verification URL.

The production verification flow becomes:

```text
Mobile Phone
     ↓
Scan QR Code
     ↓
Public CredWox URL
     ↓
Verification Endpoint
     ↓
MySQL Database
     ↓
Credential Status
     ↓
Verification Result
```

A localhost QR URL should only be used during local development because other devices cannot normally access the developer's localhost server.

---

## Testing

### Student Test

1. Open the application.
2. Open student login.
3. Authenticate with a valid student account.
4. Open the student dashboard.
5. View available credentials.
6. Open the credential certificate.
7. Verify the displayed credential information.

### Faculty Test

1. Open faculty login.
2. Authenticate as faculty.
3. Open the faculty console.
4. View student records.
5. View and manage credential information.

### Admin Test

1. Open administrator login.
2. Authenticate as administrator.
3. Open the admin console.
4. Review student and credential information.
5. Test credential management functionality.

### Verification Test

1. Open the public verification page.
2. Enter a valid credential code.
3. Submit the verification request.
4. Confirm the correct credential is returned.
5. Confirm the credential status.

### Invalid Code Test

1. Open the verification page.
2. Enter an invalid verification code.
3. Submit the request.
4. Confirm that the system does not return a valid credential.

### QR Test

1. Open a digital credential certificate.
2. Scan the QR code with a mobile phone.
3. Open the verification URL.
4. Confirm that the verification page loads.
5. Confirm that the correct credential is displayed.

---

## Demonstration Data

The project contains the following student records for demonstration:

| Student Name | Student ID |
|---|---|
| Sushil Pal | `25WU0101141` |
| Tanishq Hanumanta | `25WU0101142` |
| Suhaan Kapoor | `25WU0104027` |

---

## Certificate Information

Digital credential certificates are issued under the CredWox platform and display the authorized signatory information:

**Prof. Veeresh Biradar (Dept. Head)**

**Program Coordinator & Authorized Signatory**

---

## Advantages

- Centralized credential management
- Database-backed verification
- Public credential verification
- QR-based verification
- Student credential access
- Faculty credential management
- Administrative management
- Credential status tracking
- Verification history
- Secure authentication
- Prepared SQL statements
- Role-based access
- Responsive web interface
- Scalable Java web architecture

---

## Limitations

- Public QR verification requires deployment to a public server.
- Localhost QR codes are only suitable for local development.
- The current system is web-based.
- University-wide student information system integration is not currently implemented.
- Dedicated mobile applications are not currently included.
- Automated email notifications are not part of the current core implementation.

---

## Future Scope

### Cloud Deployment

Deploy CredWox on cloud infrastructure with a public HTTPS domain.

### Email Notifications

Automatically notify students when credentials are issued, updated, revoked, or nearing expiration.

### PDF Export

Allow students to download credentials as PDF documents.

### Credential Sharing

Allow students to generate shareable credential URLs.

### Analytics

Add dashboards showing:

- Total credentials
- Active credentials
- Revoked credentials
- Expired credentials
- Verification activity
- Student credential statistics

### Expiry Notifications

Notify students and administrators before credentials expire.

### University Integration

Connect CredWox with existing university academic and student management systems.

### Verification API

Provide an API that allows external organizations to verify credentials programmatically.

### Mobile Application

Develop dedicated mobile applications for students and verifiers.

### Multi-Institution Support

Extend the platform to support multiple educational institutions.

### Advanced Verification

Future versions can explore decentralized identity and blockchain-based credential verification.

---

## Project Information

**Project Name:** CredWox — Woxsen Digital Credential & Verification Portal

**Institution:** Woxsen University

**School:** School of Technology

**Section:** CSE PANTHERS

**Project Type:** Web Technology PBL Project

**Application Type:** Java Web Application

**Backend:** Java Servlets

**Frontend:** JSP, HTML, CSS, JavaScript

**Database:** MySQL 8

**Server:** Apache Tomcat 9

**Build Tool:** Maven

---

## Submitted By

**Sushil Pal** — `25WU0101141`  
**Tanishq Hanumanta** — `25WU0101142`  
**Suhaan Kapoor** — `25WU0104027`

**Section:** CSE PANTHERS

**Submitted To:** Prof. Veeresh Biradar

---

## GitHub Repository

https://github.com/sushil1817/credwox

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
   - Execute [`DigitalSkillBadgePortal/database/schema.sql`](DigitalSkillBadgePortal/database/schema.sql) to create tables (`admins`, `students`, `modules`, `badges`, `verification_history`).
   - Execute [`DigitalSkillBadgePortal/database/seed.sql`](DigitalSkillBadgePortal/database/seed.sql) to populate demo data (including students Sushil Pal, Tanishq Hanumanta, and Suhaan Kapoor).
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
4. The service will use the multi-stage [`DigitalSkillBadgePortal/Dockerfile`](DigitalSkillBadgePortal/Dockerfile) which:
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
Run `start-portal.bat` from the repository root, or manually deploy `DigitalSkillBadgePortal/target/DigitalSkillBadgePortal.war` to Apache Tomcat 9 `webapps/ROOT.war`.
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
