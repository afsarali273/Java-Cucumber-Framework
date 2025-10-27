@echo off
REM Encrypt test data files using Base64 encoding (Windows)
REM Usage: encrypt-testdata.bat <input-file> [output-file]

if "%~1"=="" (
    echo Usage: encrypt-testdata.bat ^<input-file^> [output-file]
    echo Example: encrypt-testdata.bat testData\credentials.json
    echo          encrypt-testdata.bat testData\credentials.json testData\credentials.json.encrypted
    exit /b 1
)

set INPUT_FILE=%~1
set OUTPUT_FILE=%~2

if "%OUTPUT_FILE%"=="" (
    set OUTPUT_FILE=%INPUT_FILE%.encrypted
)

if not exist "%INPUT_FILE%" (
    echo ❌ Error: File not found: %INPUT_FILE%
    exit /b 1
)

REM Encode using PowerShell
powershell -Command "[Convert]::ToBase64String([IO.File]::ReadAllBytes('%INPUT_FILE%')) | Out-File -Encoding ASCII '%OUTPUT_FILE%'"

if %ERRORLEVEL% EQU 0 (
    echo ✅ Encrypted: %INPUT_FILE% → %OUTPUT_FILE%
    echo 📝 You can now safely commit %OUTPUT_FILE% to GitHub
    echo 🔧 Use it in tests: user sets request body from file "%OUTPUT_FILE%"
) else (
    echo ❌ Encryption failed
    exit /b 1
)
