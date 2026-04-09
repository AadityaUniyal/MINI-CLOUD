@echo off
TITLE MiniCloud Unified Launcher
echo ===============================================
echo   MiniCloud Platform - One-Click Starter      
echo ===============================================
echo.
echo Launching 14-service microservices platform...
echo Please ensure Docker Desktop is running.
echo.
powershell.exe -ExecutionPolicy Bypass -File ".\start_minicloud.ps1"
pause
