@echo off
REM Playwright Test Generator - Simple wrapper script
REM Usage: codegen.bat [url]
REM Example: codegen.bat demo.playwright.dev/todomvc

set URL=%1
if "%URL%"=="" set URL=demo.playwright.dev/todomvc

echo 🎭 Starting Playwright Test Generator...
echo 📝 Recording tests for: %URL%
echo.

mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen %URL%"
