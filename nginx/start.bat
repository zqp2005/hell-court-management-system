@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo.
echo   [INFO] Di Fu Management System - Nginx
echo   Frontend: http://localhost:80
echo   API Doc:  http://localhost:80/doc.html
echo.
echo   Close this window to stop nginx.
echo.
.\nginx.exe -g "daemon off;"
