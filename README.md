# CredWox — Woxsen Digital Credential & Verification Portal

A secure web-based digital credential platform for issuing, managing, and publicly verifying student credentials using unique verification tokens and QR codes.

---

## Project Overview

**CredWox** is a Digital Credential & Verification Portal developed for **Woxsen University** to provide a centralized system for issuing, managing, and verifying student digital credentials.

The platform allows authorized faculty members to issue credentials for specific skill modules and achievement levels. Each credential receives a unique verification token and QR code, allowing anyone to verify its authenticity without requiring an account.

The system supports the complete credential lifecycle:

```text
Student → Credential Issuance → Unique Token → QR Code → Public Verification → Status Tracking → Audit History
```

---

## Problem Statement

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

## Proposed Solution

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

# Key Features

## 1. Digital Credential Issuance

Authorized faculty members can issue digital credentials to students by selecting:

- Student
- Skill module
- Credential tier
- Issue date
- Expiry date
- Initial status

Each credential receives a unique verification code.

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

## 4. Public Credential Verification

Verification does not require an account.

A user can enter a credential verification code and instantly retrieve the credential information.

The verification page displays:

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

## 5. Credential Status Management

CredWox supports three major credential states:

### ACTIVE

The credential is valid and currently recognized.

### EXPIRED

The credential was legitimately issued but has passed its expiry date.

### REVOKED

The credential was previously issued but has been withdrawn by the authorized authority.

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

## 7. Credential Registry

The credential registry provides a centralized view of issued credentials.

Faculty can:

- Search credentials
- Filter by tier
- Filter by status
- View certificates
- Revoke credentials
- Review issue and expiry information

## 8. Skill Module Management

Faculty can create and manage competency modules.

Each module contains:

- Module code
- Module name
- Competency description
- Number of credentials issued

## 9. Audit History

CredWox maintains verification activity so that credential verification events can be tracked.

The audit system helps provide:

- Verification timestamp
- Credential/token reference
- Verification activity
- Status information

## 10. Responsive Interface

The application is designed to work across:

- Desktop
- Laptop
- Tablet
- Mobile devices

The public verification experience is especially designed for QR-code-based mobile verification.

---

# System Architecture

```text
                    User / Phone
                         |
                     QR / Token
                         |
                         v
                Public Verification
                       JSP
                         |
                         v
                     Servlets
                         |
                         v
                       JDBC
                         |
                         v
                      MySQL 8
```

---

# Technology Stack

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

# Database Design

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

### Database Relationship

```text
ADMIN
  |
  | issues
  v
CREDENTIAL ---------> MODULE
  |
  | belongs to
  v
STUDENT

CREDENTIAL
     |
     | verification
     v
VERIFICATION_HISTORY
```

---

# Security Features

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

# User Roles

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

# Application Screenshots

> The image paths below assume you create a `docs/screenshots` folder inside your GitHub repository.

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

The page demonstrates public credential verification without requiring the user to log in.

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

The screenshot demonstrates an expired credential, showing that the system can distinguish between a legitimately issued credential and one that is no longer valid.

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

The certificate provides a professional presentation layer while the QR code and verification token provide digital verification.

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

# How to Add the Screenshots to GitHub

Since your repository currently has:

```text
credwox
└── DigitalSkillBadgePortal
```

I recommend putting the screenshots here:

```text
credwox
└── DigitalSkillBadgePortal
    ├── docs
    │   ├── PROJECT_GUIDE.md
    │   └── screenshots
    │       ├── home-page.png
    │       ├── public-verification.png
    │       ├── verification-result.png
    │       ├── certificate.png
    │       ├── faculty-login.png
    │       ├── student-login.png
    │       ├── faculty-dashboard.png
    │       ├── issue-credential.png
    │       ├── credential-registry.png
    │       └── skill-modules.png
    ├── src
    ├── database
    ├── pom.xml
    └── README.md
```

### In GitHub Desktop

1. Open your `webtech pbl` folder.
2. Open:

```text
DigitalSkillBadgePortal
```

3. Create:

```text
docs
```

4. Inside `docs`, create:

```text
screenshots
```

5. Put your screenshots inside that folder.
6. Rename them exactly according to the names used above.
7. Open GitHub Desktop.
8. You will see the screenshots under **Changes**.
9. Enter a commit message:

```text
Add project screenshots and documentation
```

10. Click **Commit to master**.
11. Click **Push origin**.

After pushing, GitHub will automatically display the images inside the README.

---

# Important: Where the README Should Be

Because your GitHub repository currently contains:

```text
credwox/
└── DigitalSkillBadgePortal/
```

and your actual Maven project is inside `DigitalSkillBadgePortal`, the easiest option is to keep the main README here:

```text
DigitalSkillBadgePortal/README.md
```

However, if you want the README to appear **directly on the main GitHub repository homepage**, the better structure is:

```text
credwox/
├── README.md
└── DigitalSkillBadgePortal/
    ├── src/
    ├── database/
    ├── docs/
    │   └── screenshots/
    └── pom.xml
```

In that case, the image paths in the root README should be:

```markdown
![CredWox Home Page](DigitalSkillBadgePortal/docs/screenshots/home-page.png)
```

instead of:

```markdown
![CredWox Home Page](docs/screenshots/home-page.png)
```

For your current repository, **I recommend the second structure** because anyone opening `https://github.com/sushil1817/credwox` will immediately see the project description and screenshots.

---

# Credential Lifecycle

```text
Student
   |
   v
Faculty Issues Credential
   |
   v
Unique Verification Token
   |
   v
QR Code Generated
   |
   v
Credential Registered in Database
   |
   v
Public Verification
   |
   +----------+----------+
   |          |          |
   v          v          v
 ACTIVE    EXPIRED    REVOKED
```

---

# QR Verification Workflow

```text
1. Student receives digital credential
              |
              v
2. Credential contains QR code
              |
              v
3. Employer / institution scans QR
              |
              v
4. Verification page opens
              |
              v
5. Credential token is checked
              |
              v
6. Database validates credential
              |
              v
7. Current status is displayed
```

---

# Project Structure

```text
DigitalSkillBadgePortal/
|
├── database/
│   ├── schema.sql
│   └── seed.sql
|
├── docs/
│   ├── PROJECT_GUIDE.md
│   └── screenshots/
|
├── src/
│   └── main/
│       ├── java/
│       ├── resources/
│       └── webapp/
|
├── pom.xml
├── README.md
└── .gitignore
```

---

# Local Setup

## Prerequisites

Install:

- Java 17
- Apache Tomcat 9
- MySQL 8
- Maven
- Git

## Clone the Repository

```bash
git clone https://github.com/sushil1817/credwox.git
```

```bash
cd credwox
```

## Configure MySQL

Create the database using:

```text
database/schema.sql
```

Then populate the initial data using:

```text
database/seed.sql
```

## Configure Database Connection

Create your local:

```text
db.properties
```

using:

```text
src/main/resources/db.properties.example
```

Do not commit database passwords or other secrets to GitHub.

## Build the Application

```bash
mvn clean package
```

The generated WAR file will be available inside:

```text
target/
```

## Deploy to Tomcat

Copy the generated WAR file to the Tomcat `webapps` directory.

Start Apache Tomcat and open:

```text
http://localhost:8080/DigitalSkillBadgePortal/
```

---

# Testing

## Public Verification

```text
Home
 → Verify Credential
 → Enter Token
 → Verify
 → View Credential Status
```

## Faculty Workflow

```text
Faculty Login
 → Dashboard
 → Students / Modules
 → Issue Credential
 → Credential Registry
 → View Certificate
 → Revoke Credential
```

## QR Workflow

```text
Certificate
 → Scan QR Code
 → Public Verification
 → Verify Credential
```

---

# Credential Tiers

## Bronze

Foundational competency.

## Silver

Intermediate competency and demonstrated practical skills.

## Gold

Advanced mastery and high-level achievement.

---

# Future Scope

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

# Project Information

**Project Name:** CredWox

**Full Name:** CredWox — Woxsen Digital Credential & Verification Portal

**Institution:** Woxsen University

**School:** School of Technology

**Project Type:** Web Technology PBL Project

**Backend:** Java 17, Servlets, JSP, JDBC

**Database:** MySQL 8

**Server:** Apache Tomcat 9

**Build Tool:** Maven

**Version Control:** Git & GitHub

---

# Authorized Signatory

**Prof. Veeresh Biradar (Dept. Head)**

Program Coordinator & Authorized Signatory

Woxsen University

---

# Project Summary

CredWox is a centralized digital credential platform that allows authorized faculty to issue secure, QR-enabled credentials and enables students, employers, institutions, and other users to verify those credentials through a public verification system.

```text
Issue
  ↓
Register
  ↓
Generate Token
  ↓
Generate QR
  ↓
Verify Online
  ↓
ACTIVE / EXPIRED / REVOKED
```

---

# Repository

https://github.com/sushil1817/credwox.git
