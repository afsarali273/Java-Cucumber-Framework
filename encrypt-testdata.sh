#!/bin/bash
# Encrypt test data files using Base64 encoding
# Usage: ./encrypt-testdata.sh <input-file> [output-file]

if [ $# -lt 1 ]; then
    echo "Usage: ./encrypt-testdata.sh <input-file> [output-file]"
    echo "Example: ./encrypt-testdata.sh testData/credentials.json"
    echo "         ./encrypt-testdata.sh testData/credentials.json testData/credentials.json.encrypted"
    exit 1
fi

INPUT_FILE="$1"
OUTPUT_FILE="${2:-${INPUT_FILE}.encrypted}"

if [ ! -f "$INPUT_FILE" ]; then
    echo "❌ Error: File not found: $INPUT_FILE"
    exit 1
fi

# Encode file to Base64
base64 -i "$INPUT_FILE" -o "$OUTPUT_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Encrypted: $INPUT_FILE → $OUTPUT_FILE"
    echo "📝 You can now safely commit $OUTPUT_FILE to GitHub"
    echo "🔧 Use it in tests: user sets request body from file \"$OUTPUT_FILE\""
else
    echo "❌ Encryption failed"
    exit 1
fi
