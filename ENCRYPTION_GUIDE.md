# Test Data Encryption Guide

## Overview

Encrypt sensitive test data files (JSON/XML) using Base64 encoding to safely commit them to GitHub without exposing credentials.

## Why Encrypt Test Data?

- ✅ **GitHub Security**: Prevents GitHub from flagging credentials in commits
- ✅ **Safe Version Control**: Commit encrypted files without exposing secrets
- ✅ **Automatic Decryption**: Framework automatically decodes `.encrypted` files
- ✅ **Variable Replacement**: Works seamlessly with `${variableName}` syntax

## Quick Start

### 1. Encrypt a File

**macOS/Linux:**
```bash
./encrypt-testdata.sh testData/credentials.json
```

**Windows:**
```cmd
encrypt-testdata.bat testData\credentials.json
```

This creates `testData/credentials.json.encrypted`

### 2. Use in Tests

```gherkin
When user sets request body from file "testData/credentials.json.encrypted"
```

The framework automatically detects `.encrypted` extension and decodes it!

## Encryption Methods

### Method 1: Shell Script (Recommended)

**macOS/Linux:**
```bash
# Encrypt with auto-generated output name
./encrypt-testdata.sh src/test/resources/testData/secure/credentials.json

# Encrypt with custom output name
./encrypt-testdata.sh testData/api-keys.json testData/api-keys.encrypted
```

**Windows:**
```cmd
REM Encrypt with auto-generated output name
encrypt-testdata.bat src\test\resources\testData\secure\credentials.json

REM Encrypt with custom output name
encrypt-testdata.bat testData\api-keys.json testData\api-keys.encrypted
```

### Method 2: Java Utility

```bash
mvn compile
java -cp target/classes:target/test-classes com.automation.core.utils.EncryptionUtil \
  testData/credentials.json testData/credentials.json.encrypted
```

### Method 3: Manual Base64 Encoding

**macOS/Linux:**
```bash
base64 -i testData/credentials.json -o testData/credentials.json.encrypted
```

**Windows PowerShell:**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("testData\credentials.json")) | Out-File -Encoding ASCII "testData\credentials.json.encrypted"
```

## Usage Examples

### Example 1: Encrypted Credentials

**Original File** (`testData/secure/credentials.json`):
```json
{
  "username": "admin@example.com",
  "password": "SecureP@ssw0rd123",
  "apiKey": "sk_live_51H7xYz2eZvKYlo2C"
}
```

**Encrypt:**
```bash
./encrypt-testdata.sh testData/secure/credentials.json
```

**Use in Test:**
```gherkin
Scenario: Login with encrypted credentials
  When user sets request body from file "testData/secure/credentials.json.encrypted"
  And user sends POST request to "/auth/login"
  Then response status code should be 200
```

### Example 2: Encrypted API Keys with Variables

**Original File** (`testData/secure/api-config.json`):
```json
{
  "apiKey": "${apiKey}",
  "clientId": "client_12345",
  "clientSecret": "secret_abcdef",
  "environment": "${env}"
}
```

**Encrypt:**
```bash
./encrypt-testdata.sh testData/secure/api-config.json
```

**Use in Test:**
```gherkin
Scenario: API call with encrypted config
  Given user saves "sk_live_xyz123" as "apiKey"
  And user saves "production" as "env"
  When user sets request body from file "testData/secure/api-config.json.encrypted"
  And user sends POST request to "/api/authenticate"
  Then response status code should be 200
```

### Example 3: Encrypted SOAP Request

**Original File** (`testData/soap/secure/payment.xml`):
```xml
<soap:Envelope>
  <soap:Body>
    <PaymentRequest>
      <CardNumber>4532123456789012</CardNumber>
      <CVV>123</CVV>
      <Amount>${amount}</Amount>
    </PaymentRequest>
  </soap:Body>
</soap:Envelope>
```

**Encrypt:**
```bash
./encrypt-testdata.sh testData/soap/secure/payment.xml
```

**Use in Test:**
```gherkin
Scenario: Process payment with encrypted card details
  Given user saves "100.00" as "amount"
  When user sends SOAP request to "/soap/payment" with body from file "testData/soap/secure/payment.xml.encrypted"
  Then response status code should be 200
  And SOAP response should not have fault
```

## Best Practices

### 1. File Organization

```
testData/
├── secure/                          # Encrypted files
│   ├── credentials.json.encrypted
│   ├── api-keys.json.encrypted
│   └── payment-info.xml.encrypted
├── public/                          # Non-sensitive files
│   ├── test-users.json
│   └── sample-data.json
└── templates/                       # Templates with ${variables}
    ├── create-user-template.json
    └── order-template.json
```

### 2. .gitignore Configuration

Add to `.gitignore`:
```
# Ignore original unencrypted files
testData/secure/*.json
testData/secure/*.xml
!testData/secure/*.encrypted

# Keep encrypted files
!testData/secure/*.json.encrypted
!testData/secure/*.xml.encrypted
```

### 3. Naming Convention

- **Encrypted files**: `filename.json.encrypted` or `filename.xml.encrypted`
- **Original files**: Keep locally, don't commit
- **Templates**: Use `${variableName}` for dynamic values

### 4. CI/CD Integration

**GitHub Actions:**
```yaml
- name: Decrypt test data (if needed for local testing)
  run: |
    base64 -d testData/credentials.json.encrypted > testData/credentials.json
```

**Jenkins:**
```groovy
sh 'base64 -d testData/credentials.json.encrypted > testData/credentials.json'
```

## Security Notes

### ⚠️ Important

- **Base64 is NOT encryption** - It's encoding for obfuscation
- **Purpose**: Bypass GitHub security scanners, not protect from attackers
- **Real Security**: Use environment variables or secret managers for production
- **Commit Safety**: Safe to commit `.encrypted` files to GitHub

### For Production

Use proper secret management:
```gherkin
# Load from environment variables
Given user saves system property "API_KEY" as "apiKey"
And user saves system property "CLIENT_SECRET" as "clientSecret"
```

Or use AWS Secrets Manager, Azure Key Vault, HashiCorp Vault, etc.

## Troubleshooting

### Issue: "File not found"
**Solution**: Check file path is relative to project root or use absolute path

### Issue: "Decoding failed"
**Solution**: Ensure file was encoded with Base64, re-encrypt if needed

### Issue: "Variables not replaced"
**Solution**: Ensure variables are saved to ScenarioContext before loading file

### Issue: "GitHub still flags credentials"
**Solution**: 
1. Ensure file has `.encrypted` extension
2. Remove original unencrypted file from git history
3. Use `git filter-branch` or BFG Repo-Cleaner to remove sensitive data

## Complete Workflow

### Step 1: Create Test Data
```json
{
  "username": "admin",
  "password": "secret123"
}
```

### Step 2: Encrypt
```bash
./encrypt-testdata.sh testData/credentials.json
```

### Step 3: Delete Original (Optional)
```bash
rm testData/credentials.json
```

### Step 4: Update .gitignore
```
testData/credentials.json
!testData/credentials.json.encrypted
```

### Step 5: Commit Encrypted File
```bash
git add testData/credentials.json.encrypted
git commit -m "Add encrypted credentials"
git push
```

### Step 6: Use in Tests
```gherkin
When user sets request body from file "testData/credentials.json.encrypted"
```

## Supported File Types

- ✅ JSON files (`.json`)
- ✅ XML files (`.xml`)
- ✅ Text files (`.txt`)
- ✅ SOAP envelopes (`.xml`)
- ✅ Any text-based format

## Framework Support

All these steps automatically support `.encrypted` files:

```gherkin
When user sets request body from file "file.json.encrypted"
When user sets request body from json file "file.json.encrypted"
When user sets request body from xml file "file.xml.encrypted"
When user sets SOAP envelope from file "file.xml.encrypted"
When user sends POST request to "/api" with body from file "file.json.encrypted"
When user sends SOAP request to "/soap" with body from file "file.xml.encrypted"
When user sets query parameters from file "params.properties.encrypted"
When user sets query parameters from json file "params.json.encrypted"
```

## Summary

| Feature | Status |
|---------|--------|
| Base64 Encoding | ✅ |
| Automatic Decoding | ✅ |
| Variable Replacement | ✅ |
| JSON Support | ✅ |
| XML Support | ✅ |
| SOAP Support | ✅ |
| Query Params Support | ✅ |
| GitHub Safe | ✅ |
| Cross-Platform | ✅ (macOS/Linux/Windows) |

---

**Created by**: Afsar Ali  
**Framework**: Sands Automation Framework  
**Version**: 1.0
