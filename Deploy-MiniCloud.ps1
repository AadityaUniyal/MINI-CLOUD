<#
.SYNOPSIS
Deploys the MiniCloud project to the Public Internet using Pinggy.

.DESCRIPTION
This script establishes a secure reverse SSH tunnel connecting your local Spring Boot instance 
to a remote server, granting you a public URL (e.g. https://xxxx.pinggy.link) that anyone can use
to access your JavaFX backend and virtual hosted S3 Websites!

.EXAMPLE
.\Deploy-MiniCloud.ps1
#>

$port = 8080

Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "       MiniCloud Public Cloud Deployer       " -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Target Local Port: $port (Spring Boot Core)" -ForegroundColor Yellow
Write-Host "Ensuring ssh.exe is available..."
if (-not (Get-Command "ssh" -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] SSH client not found on this Windows system. Cannot deploy tunnel." -ForegroundColor Red
    exit 1
}

Write-Host "Starting SSH tunnel via pinggy.io... Press Ctrl+C to stop." -ForegroundColor Green
Write-Host "When prompted, type 'yes' to accept the SSH key fingerprint." -ForegroundColor Yellow
Write-Host "============================"
Write-Host "Look for the HTTPS URL below"
Write-Host "============================"

# Using Pinggy to expose the localhost port 8080
ssh -p 443 -R0:localhost:$port a.pinggy.io
