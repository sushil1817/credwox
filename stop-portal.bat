@echo off
echo ====================================================================
echo  Stopping CredWox Portal Services
echo ====================================================================
echo.

set PROJECT_ROOT=%~dp0

echo Stopping Apache Tomcat 9...
call "%PROJECT_ROOT%.tools\apache-tomcat-9.0.86\bin\catalina.bat" stop

echo Stopping MySQL Server...
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqladmin.exe" -u root -proot shutdown

echo.
echo All services stopped successfully.
pause
