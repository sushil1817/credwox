# CredWox — Digital Skill Badge & Verification Portal

CredWox is a decentralized-style verification platform developed for Woxsen University's Web Technology PBL project. It allows academic departments to issue verifiable digital credentials to students, enables students to showcase and share certificates, and provides employers/third parties with instant, tamper-proof credential verification.

---

## Technical Stack
- **Language:** Java 17 (LTS)
- **Controller Layer:** Java Servlets 4.0
- **View Layer:** JSP (JavaServer Pages) & JSTL 1.2
- **Persistence Layer:** JDBC (MySQL Connector/J 8.3.0)
- **Database:** MySQL 8.x
- **Container / Server:** Apache Tomcat 9.0
- **Frontend:** HTML5, Modern Vanilla CSS3 (Glassmorphism & Responsive Design), Vanilla JavaScript

---

## Project Structure
```
DigitalSkillBadgePortal/
├── database/
│   ├── schema.sql           # MySQL database schema (DDL)
│   └── seed.sql             # Initial curriculum, credentials, and user data
├── docs/
│   └── PROJECT_GUIDE.md     # Comprehensive architecture and viva guide
├── src/
│   └── main/
│       ├── java/com/badge/  # Controllers (Servlets), DAOs, Models, and Utilities
│       ├── resources/       # Configuration templates (db.properties.example)
│       └── webapp/          # JSP views, CSS stylesheets, and JavaScript
└── pom.xml                  # Maven Project Object Model configuration
```

---

## Configuration & Environment Variables
CredWox supports standard environment variables for seamless local, Docker, and cloud deployments:

| Variable | Description | Default Fallback |
| :--- | :--- | :--- |
| `DB_URL` / `DATABASE_URL` / `MYSQL_URL` | JDBC Connection String | `jdbc:mysql://localhost:3306/badge_portal` |
| `DB_USERNAME` / `DB_USER` / `MYSQLUSER` | MySQL Username | `root` |
| `DB_PASSWORD` / `MYSQLPASSWORD` | MySQL Password | Empty / reads `db.properties` |
| `DB_DRIVER` | JDBC Driver Class | `com.mysql.cj.jdbc.Driver` |

---

## Build Instructions
Ensure Java 17 and Maven 3.8+ are installed:
```bash
mvn clean package
```
The packaged WAR file will be generated at `target/DigitalSkillBadgePortal.war`.
Deploy this WAR file to Apache Tomcat 9's `webapps/` folder.
