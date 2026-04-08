# setup_env.ps1 - MiniCloud Environment Setup
# This script sets the JAVA_HOME and PATH for the current terminal session.

$JavaPath = "C:\Program Files\Java\jdk-21"

if (Test-Path $JavaPath) {
    $env:JAVA_HOME = $JavaPath
    $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
    Write-Host "SUCCESS: JAVA_HOME set to $env:JAVA_HOME" -ForegroundColor Green
    Write-Host "SUCCESS: Path updated." -ForegroundColor Green
    java -version
} else {
    Write-Host "ERROR: JDK 21 not found at $JavaPath" -ForegroundColor Red
    Write-Host "Attempting to auto-locate JDK..." -ForegroundColor Yellow
    
    $AutoPath = Get-ChildItem -Path "C:\Program Files\Java" -Filter "jdk-21*" | Select-Object -First 1 -ExpandProperty FullName
    if ($AutoPath) {
        $env:JAVA_HOME = $AutoPath
        $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
        Write-Host "SUCCESS: Auto-located JDK at $env:JAVA_HOME" -ForegroundColor Green
        java -version
    } else {
        Write-Host "CRITICAL ERROR: Could not find JDK 21. Please install JDK 21 to continue." -ForegroundColor Red
        exit 1
    }
}

Write-Host "`nEnvironment is ready for Maven build." -ForegroundColor Cyan
