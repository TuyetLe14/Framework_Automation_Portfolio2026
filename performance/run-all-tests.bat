@echo off

echo ╔════════════════════════════════════════════╗
echo ║   k6 Performance Testing Suite             ║
echo ╚════════════════════════════════════════════╝
echo.

where k6 >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ k6 is not installed!
    echo Install: choco install k6
    exit /b 1
)

echo ✅ k6 found
echo.

REM Create results directory
if not exist "performance\results" mkdir performance\results

REM Get timestamp
for /f "tokens=2-4 delims=/ " %%a in ('date /t') do (set mydate=%%c%%a%%b)
for /f "tokens=1-2 delims=/:" %%a in ('time /t') do (set mytime=%%a%%b)
set TIMESTAMP=%mydate%_%mytime%

echo 🚀 Starting performance tests...
echo.

REM Load Test
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 1️⃣  Running Load Test (5 users, 30s)
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
k6 run performance\load-test.js ^
  --out json=performance\results\load-test_%TIMESTAMP%.json ^
  --summary-export=performance\results\load-test-summary_%TIMESTAMP%.json
echo.

REM Stress Test
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 2️⃣  Running Stress Test (ramp to 50 users)
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
k6 run performance\stress-test.js ^
  --out json=performance\results\stress-test_%TIMESTAMP%.json ^
  --summary-export=performance\results\stress-test-summary_%TIMESTAMP%.json
echo.

REM Spike Test
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo 3️⃣  Running Spike Test (100 user spike)
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
k6 run performance\spike-test.js ^
  --out json=performance\results\spike-test_%TIMESTAMP%.json ^
  --summary-export=performance\results\spike-test-summary_%TIMESTAMP%.json
echo.

echo ╔════════════════════════════════════════════╗
echo ║   ✅ All Performance Tests Completed       ║
echo ╚════════════════════════════════════════════╝
echo.
echo 📊 Results saved to: performance\results\
echo.
echo Press any key to close...
pause >nul
