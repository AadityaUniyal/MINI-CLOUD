# MiniCloud Zero-Touch Startup Orchestrator - STABILIZED EDITION
# This script starts the entire 14-service microservices platform.

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "   MiniCloud Cloud Platform - Starting Up      " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

# 1. Environment Setup
Write-Host "[1/5] Setting up environment..." -ForegroundColor Yellow
if (Test-Path ".\setup_env.ps1") {
    . .\setup_env.ps1
} else {
    Write-Host "WARNING: setup_env.ps1 not found. Proceeding with system defaults." -ForegroundColor Gray
}

# Check for .env file
if (-not (Test-Path ".env")) {
    Write-Host "ERROR: .env file is missing. Please create it from .env.example or run generate_env.ps1." -ForegroundColor Red
    exit 1
}

# 2. Check for Docker
Write-Host "[2/5] Checking Docker engine status..." -ForegroundColor Yellow
docker info > $null 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Docker is NOT running. Please start Docker Desktop and ensure the engine is reachable." -ForegroundColor Red
    exit 1
}
Write-Host "Docker is ready." -ForegroundColor Green

# 3. Build (Optional/Pre-check)
Write-Host "[3/5] Verifying system components..." -ForegroundColor Yellow
# We rely on docker-compose --build, but we check if mvnw is executable.
if (-not (Test-Path ".\mvnw.cmd")) {
    Write-Host "ERROR: Maven wrapper (mvnw.cmd) not found in root." -ForegroundColor Red
    exit 1
}

# 4. Orchestrate Services
Write-Host "[4/5] Orchestrating 14-module platform (Docker Compose)..." -ForegroundColor Yellow
Write-Host "This will build and start: Eureka, IAM, Gateway, Backend, Compute, and more."
docker-compose up -d --build

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Docker Compose orchestration failed." -ForegroundColor Red
    exit 1
}

# 5. Health Monitoring Loop
Write-Host "[5/5] Waiting for services to reach HEALTHY state..." -ForegroundColor Yellow
$Services = @("minicloud-eureka", "minicloud-iam", "minicloud-gateway", "minicloud-portal")
$MaxRetries = 40
$WaitInterval = 10

for ($i = 1; $i -le $MaxRetries; $i++) {
    $AllHealthy = $true
    Write-Host "`n--- Health Check Attempt $i/$MaxRetries ---" -ForegroundColor Gray
    
    foreach ($Service in $Services) {
        $Status = docker inspect --format='{{.State.Health.Status}}' $Service 2>$null
        if ($Status -eq "healthy") {
            Write-Host "[PASS] $Service is HEALTHY" -ForegroundColor Green
        } else {
            Write-Host "[WAIT] $Service is $Status" -ForegroundColor Yellow
            $AllHealthy = $false
        }
    }

    if ($AllHealthy) {
        Write-Host "`nSUCCESS: All critical core services are HEALTHY." -ForegroundColor Green
        break
    }

    if ($i -eq $MaxRetries) {
        Write-Host "`nWARNING: Some services are taking longer than expected to stabilize." -ForegroundColor Red
        Write-Host "Running 'docker ps' to show current state:"
        docker ps
    }
    
    Start-Sleep -Seconds $WaitInterval
}

# Final Launch
Write-Host "`n===============================================" -ForegroundColor Cyan
Write-Host "   MiniCloud is now LIVE at http://localhost:8089/login " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

Start-Process "http://localhost:8089/login"
