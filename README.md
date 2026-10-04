# CredWox — Woxsen Digital Credential & Verification Portal

> A secure web-based digital credential platform for issuing, managing, and publicly verifying student credentials using unique verification tokens and QR codes.

![CredWox](docs/screenshots/credwox-home.png)

## 📌 Project Overview

**CredWox** is a Digital Credential & Verification Portal developed for **Woxsen University** to provide a centralized system for issuing, managing, and verifying student digital credentials.

The platform allows authorized faculty members to issue credentials for specific skill modules and achievement levels. Each credential receives a unique verification token and QR code, allowing anyone to verify its authenticity without requiring an account.

The system supports the complete credential lifecycle:

**Student → Credential Issuance → Unique Token → QR Code → Public Verification → Status Tracking → Audit History**

---

## 🎯 Problem Statement

Traditional certificates and skill credentials can be difficult to verify because they often depend on:

- Physical documents
- Manual verification
- Email-based confirmation
- Easily altered digital files
- Lack of centralized records
- No real-time credential status
- No simple way to check whether a credential has been revoked or expired

CredWox addresses these problems by providing a centralized digital verification system where every credential is registered in a database and can be independently verified using a unique code or QR code.

---

## 💡 Proposed Solution

CredWox provides a centralized credential management system with:

- Digital credential issuance
- Unique verification tokens
- QR-based verification
- Public verification without login
- Active, revoked, and expired statuses
- Student credential records
- Skill/module management
- Faculty administration console
- Verification history
- Database-backed credential validation
- Secure password handling
- Prepared SQL statements
- Responsive web interface

---

# ✨ Key Features

## 1. Digital Credential Issuance

Authorized faculty members can issue digital credentials to students by selecting:

- Student
- Skill module
- Credential tier
- Issue date
- Expiry date
- Initial status

Each credential receives a unique verification code.

---

## 2. Unique Verification Token

Every credential is assigned a unique token such as:

```text
CWX-GOLD-2026XX
```

or

```text
CWX-SILV-202607
```

The token acts as the credential's unique verification identifier.

---

## 3. QR Code Verification

Every issued credential contains a QR code.

The QR code directs the user to the credential verification page, allowing the credential to be verified quickly using a smartphone.

### Verification Flow

```text
Credential
     ↓
Unique Verification Token
     ↓
QR Code
     ↓
Public Verification Page
     ↓
Database Lookup
     ↓
Credential Status
     ↓
Verified Result
```

---

## 4. Public Credential Verification

Verification does not require an account.

A user can enter a credential verification code and instantly retrieve the credential information.

The verification page displays information such as:

- Recipient name
- Student ID
- Course/Degree
- Skill module
- Credential tier
- Issue date
- Expiry date
- Issuing authority
- Current credential status
- Verification token

---

## 5. Credential Status Management

CredWox supports three major credential states:

### 🟢 ACTIVE

The credential is valid and currently recognized.

### 🟡 EXPIRED

The credential was legitimately issued but has passed its expiry date.

### 🔴 REVOKED

The credential was previously issued but has been withdrawn by the authorized authority.

This makes the system more reliable than simply checking whether a certificate exists.

---

## 6. Faculty Console

Authorized faculty members have access to a dedicated administration console.

The console provides access to:

- Dashboard
- Students
- Skill Modules
- Issue Credential
- Credential Registry
- Audit History
- Public Verification

---

## 7. Credential Registry

The credential registry provides a centralized view of issued credentials.

Faculty can:

- Search credentials
- Filter by tier
- Filter by status
- View certificates
- Revoke credentials
- Review issue and expiry information

---

## 8. Skill Module Management

Faculty can create and manage competency modules.

Each module contains:

- Module code
- Module name
- Competency description
- Number of credentials issued

Example modules include:

```text
WT-101
DS-201
AI-401
CS-301
```

---

## 9. Audit History

CredWox maintains verification activity so that credential verification events can be tracked.

The audit system helps provide:

- Verification timestamp
- Credential/token reference
- Verification activity
- Status information

This creates an additional layer of accountability.

---

## 10. Responsive Interface

The application is designed to work across:

- Desktop
- Laptop
- Tablet
- Mobile devices

The public verification experience is especially designed for QR-code-based mobile verification.

---

# 🏗️ System Architecture

```text
                    ┌──────────────────────┐
                    │      User / Phone    │
                    └──────────┬───────────┘
                               │
                         QR / Token
                               │
                               ▼
                    ┌──────────────────────┐
                    │ Public Verification  │
                    │       JSP Page       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      Servlets        │
                    │   Business Logic     │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │        JDBC          │
                    │   Database Access    │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │       MySQL 8        │
                    │ Credential Database  │
                    └──────────────────────┘
```

---

# 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| HTML5 | Page structure |
| CSS3 | UI styling and responsive design |
| JavaScript | Client-side interactions |
| Java 17 | Backend programming |
| JSP | Dynamic web pages |
| Java Servlets | Request handling and business logic |
| JDBC | Database connectivity |
| MySQL 8 | Data storage |
| Apache Tomcat 9 | Application server |
| Maven | Project and dependency management |
| Git | Version control |
| GitHub | Source code hosting |
| QR Code | Credential verification |

---

# 🗄️ Database Design

CredWox uses MySQL as its relational database.

## Main Tables

### `admins`

Stores authorized faculty/admin accounts.

### `students`

Stores student information.

### `modules`

Stores skill and competency modules.

### `badges`

Stores issued digital credentials.

### `verification_history`

Stores credential verification activity.

---

## Database Relationship

```text
ADMIN
  │
  │ issues
  ▼
CREDENTIAL ───────────► MODULE
  │
  │ belongs to
  ▼
STUDENT

CREDENTIAL
     │
     │ verification
     ▼
VERIFICATION_HISTORY
```

---

# 🔐 Security Features

CredWox incorporates several basic web application security practices:

- Password hashing
- Prepared SQL statements
- Input validation
- Session-based authentication
- Authorization for faculty functions
- Public verification without exposing administrative access
- Unique credential tokens
- Database-backed verification
- Separation of public and administrative functionality
- Sensitive database configuration kept outside version control

---

# 👥 User Roles

## Faculty / Administrator

Faculty members can:

- Log in to the faculty console
- View dashboard statistics
- Manage students
- Manage skill modules
- Issue credentials
- View issued credentials
- Revoke credentials
- View audit history
- Access public verification

## Public User

A public user can:

- Enter a credential verification code
- Scan a credential QR code
- View credential information
- Check whether a credential is active, expired, or revoked

No login is required for public verification.

---

# 📸 Application Screenshots

## 1. CredWox Home Page

The landing page provides the primary navigation for the CredWox platform.

Users can access:

- Public credential verification
- Student login
- Registration
- Faculty/Admin console

![CredWox Home Page](docs/screenshots/home-page.png)

---

## 2. Public Credential Verification

The verification page allows anyone to enter a unique credential verification code.

The system then checks the credential against the database and displays its current validity.

![Public Verification Page](docs/screenshots/public-verification.png)

**Key functionality shown:**

- Verification code input
- Verify Now button
- Credential status examples
- Public access without login

---

## 3. Credential Verification Result

After entering a verification token, CredWox displays the credential's complete metadata.

The result includes:

- Recipient name
- Student ID
- Course/Degree
- Skill module
- Credential tier
- Issue date
- Expiry date
- Verification token
- Credential status
- Issuing authority

![Credential Verification Result](docs/screenshots/verification-result.png)

The screenshot demonstrates an **EXPIRED** credential, showing that the system can distinguish between a legitimately issued credential and one that is no longer valid.

---

## 4. Digital Credential Certificate

Each credential has a formal certificate-style view.

The certificate contains:

- Woxsen University branding
- Certificate title
- Student name
- Student ID
- Degree
- Skill module
- Module code
- Credential tier
- QR code
- Verification token
- Issue date
- Valid-through date
- Authorized signatory

![Digital Credential Certificate](docs/screenshots/certificate.png)

The certificate is designed to provide a professional presentation layer while the QR code and verification token provide machine-readable verification.

---

## 5. Faculty Login

The Faculty Console is protected by an administrative login.

![Faculty Login](docs/screenshots/faculty-login.png)

The login system separates administrative credential management from the public verification system.

---

## 6. Student Login

Students can access their own credential dashboard through the student login interface.

![Student Login](docs/screenshots/student-login.png)

Students can use their student ID or registered email along with their password to access their account.

---

## 7. Faculty Dashboard

The Faculty Dashboard provides an overview of the credential system.

The dashboard displays statistics such as:

- Total students
- Total skill modules
- Total credentials issued
- Active credentials
- Revoked credentials
- Verification lookups

![Faculty Dashboard](docs/screenshots/faculty-dashboard.png)

This provides faculty with a quick overview of the credential ecosystem.

---

## 8. Issue Digital Credential

Authorized faculty members can issue a new credential using the credential issuance form.

The form allows faculty to select:

- Student
- Skill module
- Credential level
- Verification token
- Issue date
- Expiry date
- Initial status

![Issue Digital Credential](docs/screenshots/issue-credential.png)

Once issued, the credential is registered in the database and becomes available for verification.

---

## 9. Credential Registry

The Credential Registry provides a complete view of issued credentials.

Faculty can search and filter credentials based on:

- Student
- Roll ID
- Verification code
- Credential tier
- Credential status

![Credential Registry](docs/screenshots/credential-registry.png)

The registry also provides actions such as viewing the certificate and revoking a credential.

---

## 10. Skill Module Management

The Skill Module Management page allows faculty to define the competency areas for which credentials can be issued.

![Skill Module Management](docs/screenshots/skill-modules.png)

Each module contains a code, name, competency description, and number of credentials issued.

---

# 🔄 Credential Lifecycle

```text
        ┌─────────────┐
        │   Student   │
        └──────┬──────┘
               │
               ▼
      ┌─────────────────┐
      │ Faculty Issues  │
      │   Credential    │
      └────────┬────────┘
               │
               ▼
      ┌─────────────────┐
      │ Unique Token +  │
      │    QR Code      │
      └────────┬────────┘
               │
               ▼
      ┌─────────────────┐
      │ Credential      │
      │    ACTIVE       │
      └────────┬────────┘
               │
        ┌──────┴───────┐
        │              │
        ▼              ▼
   ┌─────────┐    ┌─────────┐
   │ EXPIRED │    │ REVOKED │
   └─────────┘    └─────────┘
```

---

# 📱 QR Verification Workflow

The QR verification process is designed for real-world certificate verification.

```text
1. Student receives digital credential
              ↓
2. Credential contains QR code
              ↓
3. Employer / institution scans QR
              ↓
4. Verification page opens
              ↓
5. Credential token is checked
              ↓
6. Database validates credential
              ↓
7. Current status is displayed
```

This removes the need for manual certificate verification.

---

# 📊 Example Credential Information

A verified credential can contain information such as:

```text
Recipient:
Student Name

Student ID:
25WUXXXXXXXX

Course:
B.Tech Computer Science & Engineering

Skill Module:
Cloud Native Systems & Container Orchestration

Module Code:
CS-301

Credential Tier:
Silver

Verification Token:
CWX-SILV-202607

Status:
ACTIVE

Issuing Authority:
Prof. Veeresh Biradar (Dept. Head)

Role:
Program Coordinator & Authorized Signatory
```

---

# 📁 Project Structure

```text
DigitalSkillBadgePortal/
│
├── database/
│   ├── schema.sql
│   └── seed.sql
│
├── docs/
│   └── PROJECT_GUIDE.md
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ...
│       │
│       ├── resources/
│       │   └── db.properties.example
│       │
│       └── webapp/
│           ├── css/
│           ├── js/
│           ├── images/
│           ├── WEB-INF/
│           ├── certificate.jsp
│           ├── verify.jsp
│           └── ...
│
├── pom.xml
├── README.md
└── .gitignore
```

---

# ⚙️ Local Setup

## Prerequisites

Install the following:

- Java 17
- Apache Tomcat 9
- MySQL 8
- Maven
- Git

---

## 1. Clone the Repository

```bash
git clone https://github.com/sushil1817/credwox.git
```

```bash
cd credwox
```

---

## 2. Open the Project

Navigate to:

```text
DigitalSkillBadgePortal
```

---

## 3. Configure MySQL

Create the database using:

```text
database/schema.sql
```

Then populate the initial data using:

```text
database/seed.sql
```

---

## 4. Configure Database Connection

Create your local:

```text
db.properties
```

using:

```text
src/main/resources/db.properties.example
```

Do not commit database passwords or other secrets to GitHub.

---

## 5. Build the Application

Run:

```bash
mvn clean package
```

The generated WAR file will be available inside:

```text
target/
```

---

## 6. Deploy to Tomcat

Copy the generated WAR file to the Tomcat `webapps` directory.

Start Apache Tomcat and open:

```text
http://localhost:8080/DigitalSkillBadgePortal/
```

---

# 🧪 Testing

The application can be tested through the following flows:

### Public Verification

```text
Home
 → Verify Credential
 → Enter Token
 → Verify
 → View Credential Status
```

### Faculty Workflow

```text
Faculty Login
 → Dashboard
 → Students / Modules
 → Issue Credential
 → Credential Registry
 → View Certificate
 → Revoke Credential
```

### QR Workflow

```text
Certificate
 → Scan QR Code
 → Public Verification
 → Verify Credential
```

---

# 🏆 Credential Tiers

CredWox supports multiple credential levels to represent different achievement levels.

### 🥉 Bronze

Foundational competency.

### 🥈 Silver

Intermediate competency and demonstrated practical skills.

### 🥇 Gold

Advanced mastery and high-level achievement.

---

# 📈 Future Scope

The project can be further expanded with:

- Cloud deployment
- Public HTTPS verification
- Mobile-friendly QR scanning
- Email credential delivery
- Downloadable PDF certificates
- Credential sharing links
- Institution-to-institution verification
- Digital credential APIs
- Blockchain-based credential anchoring
- Advanced analytics
- Role-based permissions
- Multi-department support
- Credential expiry notifications
- Automated verification reports

---

# 🔮 Future Deployment Architecture

```text
                    Internet
                       │
                       ▼
                ┌──────────────┐
                │ Public HTTPS │
                │   CredWox    │
                └──────┬───────┘
                       │
                       ▼
                ┌──────────────┐
                │ Apache       │
                │ Tomcat 9     │
                └──────┬───────┘
                       │
                       ▼
                ┌──────────────┐
                │ Java/JSP/    │
                │ Servlets     │
                └──────┬───────┘
                       │
                       ▼
                ┌──────────────┐
                │   MySQL 8    │
                └──────────────┘
```

---

# 🔐 Why CredWox?

CredWox combines a traditional academic credential system with modern digital verification.

Instead of relying only on a certificate document, the system creates a verifiable digital record behind every credential.

This means that:

**A certificate can be presented visually, while its authenticity can be verified digitally.**

---

# 👨‍💻 Project Information

**Project Name:** CredWox

**Full Name:**  
CredWox — Woxsen Digital Credential & Verification Portal

**Institution:**  
Woxsen University

**School:**  
School of Technology

**Project Type:**  
Web Technology PBL Project

**Technology:**  
Java Web Application

**Backend:**  
Java 17, Servlets, JSP, JDBC

**Database:**  
MySQL 8

**Server:**  
Apache Tomcat 9

**Build Tool:**  
Maven

**Version Control:**  
Git & GitHub

---

# 👤 Authorized Signatory

**Prof. Veeresh Biradar (Dept. Head)**

**Program Coordinator & Authorized Signatory**

Woxsen University

---

# 📜 License

This project is developed as an academic Web Technology PBL project for Woxsen University.

---

# ⭐ Project Summary

**CredWox is a centralized digital credential platform that allows authorized faculty to issue secure, QR-enabled credentials and enables students, employers, institutions, and other users to verify those credentials instantly through a public verification system.**

```text
Issue → Register → Generate Token → Generate QR
                         ↓
                    Verify Online
                         ↓
              ACTIVE / EXPIRED / REVOKED
```

---

## 🔗 Repository

**GitHub:**  
https://github.com/sushil1817/credwox.git
