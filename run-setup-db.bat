@echo off
echo ====================================================================
echo  Digital Skill Badge & Verification Portal - MySQL Database Setup
echo ====================================================================
echo.

set /p MYSQL_USER="Enter MySQL Username [default: root]: "
if "%MYSQL_USER%"=="" set MYSQL_USER=root

echo.
echo Running schema.sql...
mysql -u %MYSQL_USER% -p < "%~dp0\DigitalSkillBadgePortal\database\schema.sql"

echo.
echo Running seed.sql...
mysql -u %MYSQL_USER% -p < "%~dp0\DigitalSkillBadgePortal\database\seed.sql"

echo.
echo ====================================================================
echo  Database 'badge_portal' created and seeded successfully!
echo ====================================================================
pause
