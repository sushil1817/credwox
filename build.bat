@echo off
echo ====================================================================
echo  Digital Skill Badge & Verification Portal - Maven Build Script
echo ====================================================================
echo.

cd /d "%~dp0\DigitalSkillBadgePortal"

if exist "..\.tools\apache-maven-3.9.6\bin\mvn.cmd" (
    echo Using local Maven wrapper...
    call "..\.tools\apache-maven-3.9.6\bin\mvn.cmd" clean package
) else (
    echo Using system Maven...
    call mvn clean package
)

echo.
if %ERRORLEVEL% EQU 0 (
    echo ====================================================================
    echo  BUILD SUCCESS!
    echo  WAR file generated at:
    echo  DigitalSkillBadgePortal\target\DigitalSkillBadgePortal.war
    echo ====================================================================
) else (
    echo  BUILD FAILED! Please check the error messages above.
)
pause
