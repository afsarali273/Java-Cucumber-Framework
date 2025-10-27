#!/bin/bash
# Playwright Test Generator - Simple wrapper script
# Usage: ./codegen.sh [url]
# Example: ./codegen.sh demo.playwright.dev/todomvc

URL="${1:-demo.playwright.dev/todomvc}"

echo "🎭 Starting Playwright Test Generator..."
echo "📝 Recording tests for: $URL"
echo ""

mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen $URL"
