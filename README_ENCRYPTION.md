# 🔒 Test Data Encryption - Quick Reference

## Encrypt Files

### macOS/Linux
```bash
./encrypt-testdata.sh testData/credentials.json
```

### Windows
```cmd
encrypt-testdata.bat testData\credentials.json
```

## Use in Tests

```gherkin
When user sets request body from file "testData/credentials.json.encrypted"
```

Framework automatically detects `.encrypted` extension and decodes!

## What Gets Encrypted?

- ✅ JSON files with credentials
- ✅ XML files with sensitive data
- ✅ SOAP envelopes with secrets
- ✅ Query parameter files
- ✅ Any text-based test data

## Why?

- ✅ **GitHub Safe**: Bypasses credential scanners
- ✅ **Auto-Decode**: Framework handles decryption
- ✅ **Variables Work**: `${variableName}` still supported
- ✅ **Zero Code Change**: Just add `.encrypted` extension

## Full Documentation

See `ENCRYPTION_GUIDE.md` for complete guide with examples.
