# MiniCloud Zero-Touch Startup Orchestrator - STABILIZED EDITION
# This script starts the entire 14-service microservices platform.

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "   MiniCloud Cloud Platform - Starting Up      " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

# 1. Environment Setup
Write-Host "[1/5] Setting up environment..." -ForegroundColor Yellow

# Explicitly use JDK 21 for building/running (Fixes JDK 25 mismatch)
$ENV:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$ENV:PATH = "$ENV:JAVA_HOME\bin;$ENV:PATH"

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

# 4. Tiered Orchestration (Prevents Memory Surge)
Write-Host "[4/5] Orchestrating MiniCloud Tiered Startup..." -ForegroundColor Yellow

# --- Tier 1: Infrastructure ---
Write-Host "-> Tier 1: Infrastructure (Postgres, Eureka, MinIO)..." -ForegroundColor Cyan
docker-compose up -d --build postgres-db eureka-server minio
Start-Sleep -Seconds 15

# --- Tier 2: Identity & Access ---
Write-Host "-> Tier 2: Identity & Security (IAM)..." -ForegroundColor Cyan
docker-compose up -d --build iam-service
Start-Sleep -Seconds 15

# --- Tier 3: Core Application ---
Write-Host "-> Tier 3: Gateway & Consolidated Backend..." -ForegroundColor Cyan
docker-compose up -d --build api-gateway backend-service
Start-Sleep -Seconds 10

# --- Tier 4: Background Services (Non-blocking) ---
Write-Host "-> Tier 4: Background Platform Services (Scaling in background)..." -ForegroundColor Gray
docker-compose up -d --build compute-service storage-service database-service monitoring-service billing-service notification-service loadbalancer-service

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Docker Compose orchestration failed." -ForegroundColor Red
    exit 1
}

# 5. Health Monitoring & UI Launch
Write-Host "[5/5] Waiting for Core Tier to reach HEALTHY state..." -ForegroundColor Yellow
$Services = @("minicloud-eureka", "minicloud-iam", "minicloud-gateway")
$MaxRetries = 60
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

# Wait for API Gateway exposure
Write-Host "`nWaiting for API Gateway to be reachable..." -ForegroundColor Yellow
$Attempt = 1
while ($Attempt -le 10) {
    try {
        $Response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 2
        if ($Response.StatusCode -eq 200) {
            Write-Host "API Gateway is UP." -ForegroundColor Green
            break
        }
    } catch {
        Write-Host "Waiting for Gateway (Attempt $Attempt/10)..." -ForegroundColor Gray
    }
    Start-Sleep -Seconds 5
    $Attempt++
}

# Final Launch
Write-Host "`n===============================================" -ForegroundColor Cyan
Write-Host "   MiniCloud is now LIVE!                      " -ForegroundColor Cyan
Write-Host "   Launching Desktop Management Console...    " -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

# Start the Web Portal in background as fallback
Start-Process "http://localhost:8089/login"

# Launch the JavaFX Admin Console (Primary Screen)
Write-Host "Starting MiniCloud JavaFX App..." -ForegroundColor Yellow
.\mvnw.cmd -pl minicloud-admin-console javafx:run
