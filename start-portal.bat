@echo off
echo ====================================================================
echo  Starting CredWox — Woxsen Digital Credential & Verification Portal
echo ====================================================================
echo.

set PROJECT_ROOT=%~dp0

echo 1. Starting MySQL Server (Port 3306)...
start "MySQL-Server" /MIN "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld.exe" --datadir="%PROJECT_ROOT%.tools\mysql-data" --port=3306 --console
timeout /t 3 /nobreak >nul

echo 2. Starting Apache Tomcat 9 (Port 8080)...
set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.2"
set "JRE_HOME=C:\Program Files\Java\jdk-25.0.2"
start "Apache-Tomcat-9" "%PROJECT_ROOT%.tools\apache-tomcat-9.0.86\bin\catalina.bat" run
timeout /t 5 /nobreak >nul

echo.
echo ====================================================================
echo  PORTAL IS NOW LIVE!
echo.
echo  Homepage:      http://localhost:8080/DigitalSkillBadgePortal/
echo  Public Verify: http://localhost:8080/DigitalSkillBadgePortal/verify
echo  Student Login: http://localhost:8080/DigitalSkillBadgePortal/login.jsp
echo  Admin Console: http://localhost:8080/DigitalSkillBadgePortal/admin/login
echo ====================================================================
echo.
echo Launching default web browser...
start http://localhost:8080/DigitalSkillBadgePortal/
pause
