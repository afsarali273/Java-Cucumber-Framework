# Remote Execution Guide

This guide explains how to run tests on cloud platforms: **LambdaTest** (web) and **AWS Device Farm** (mobile).

## Overview

The framework supports three execution modes:
- **Local**: Tests run on your machine (default)
- **LambdaTest**: Web tests run on LambdaTest cloud
- **AWS Device Farm**: Mobile tests run on AWS Device Farm

## Configuration

### Enable Remote Execution

Set `remote.provider` in `config.properties`:

```properties
# For local execution (default)
remote.provider=

# For LambdaTest (web)
remote.provider=lambdatest

# For AWS Device Farm (mobile)
remote.provider=aws
# OR
remote.provider=devicefarm
```

---

## LambdaTest Setup (Web Automation)

### 1. Get Credentials

1. Sign up at [LambdaTest](https://www.lambdatest.com/)
2. Go to **Profile** → **Account Settings** → **Password & Security**
3. Copy your **Username** and **Access Key**

### 2. Configure Properties

```properties
# Enable LambdaTest
remote.provider=lambdatest

# Credentials
lambdatest.username=your_username
lambdatest.accessKey=your_access_key

# Browser & Platform
browser=chrome
lambdatest.browserVersion=latest
lambdatest.platform=Windows 10

# Test Metadata
lambdatest.build=Sands Framework Build
lambdatest.project=Sands Automation
lambdatest.testName=My Test

# Features
lambdatest.video=true
lambdatest.visual=false
lambdatest.network=false
lambdatest.console=false
```

### 3. Supported Platforms

**Operating Systems:**
- Windows 10, Windows 11, Windows 8.1, Windows 8, Windows 7
- macOS Ventura, macOS Monterey, macOS Big Sur, macOS Catalina
- Ubuntu, Debian, Fedora

**Browsers:**
- Chrome (latest, latest-1, latest-2, specific versions)
- Firefox (latest, latest-1, latest-2, specific versions)
- Edge, Safari

### 4. Run Tests

```bash
# Run all tests on LambdaTest
mvn clean test

# Run specific tags
mvn test -Dcucumber.filter.tags="@UI"

# Override browser
mvn test -Dbrowser=firefox
```

### 5. View Results

- Dashboard: [LambdaTest Automation Dashboard](https://automation.lambdatest.com/)
- View videos, screenshots, logs, network logs
- Download test artifacts

---

## AWS Device Farm Setup (Mobile Automation)

### 1. Prerequisites

- AWS Account with Device Farm access
- Mobile app (.apk for Android, .ipa for iOS)
- AWS CLI configured (optional)

### 2. Upload App to Device Farm

**Option A: AWS Console**
1. Go to [AWS Device Farm Console](https://console.aws.amazon.com/devicefarm/)
2. Create a new project
3. Upload your app (.apk or .ipa)
4. Note the app ARN

**Option B: AWS CLI**
```bash
# Upload app
aws devicefarm create-upload \
  --project-arn arn:aws:devicefarm:us-west-2:123456789012:project:xxxxx \
  --name app.apk \
  --type ANDROID_APP

# Get upload URL and upload file
curl -T app.apk "PRESIGNED_URL"
```

### 3. Create Test Run

1. In Device Farm Console, create a new test run
2. Select **Appium Java TestNG** as framework
3. Upload your test package (JAR)
4. Select devices
5. Start the run
6. Copy the **Appium endpoint URL** from test run details

### 4. Configure Properties

```properties
# Enable AWS Device Farm
remote.provider=aws

# Framework type
framework.type=mobile

# Device Farm URL (from test run)
aws.devicefarm.url=https://appium-us-west-2.devicefarm.aws.amazon.com/wd/hub

# Platform
mobile.platformName=Android

# Device & App
aws.devicefarm.deviceName=Samsung Galaxy S21
aws.devicefarm.app=arn:aws:devicefarm:us-west-2:123456789012:upload:xxxxx

# Android specific
mobile.appPackage=com.example.android
mobile.appActivity=com.example.android.MainActivity
mobile.automationName=UiAutomator2

# iOS specific (if using iOS)
# mobile.platformName=iOS
# mobile.bundleId=com.example.ios
```

### 5. Run Tests

```bash
# Run mobile tests on AWS Device Farm
mvn clean test -Dcucumber.filter.tags="@Mobile"
```

### 6. View Results

- Dashboard: [AWS Device Farm Console](https://console.aws.amazon.com/devicefarm/)
- View videos, screenshots, logs, performance metrics
- Download test artifacts

---

## Parallel Execution

Both LambdaTest and AWS Device Farm support parallel execution:

```properties
# Enable parallel execution
parallel.execution=true
thread.count=5

# Each thread gets its own remote session
```

**How it works:**
- Each thread creates a separate remote driver session
- ThreadLocal ensures thread-safety
- Sessions run independently on cloud
- Results are aggregated in reports

---

## Environment-Specific Configuration

Create environment files for different setups:

**qa.properties** (Local)
```properties
remote.provider=
```

**qa-cloud.properties** (LambdaTest)
```properties
remote.provider=lambdatest
lambdatest.username=${LAMBDATEST_USERNAME}
lambdatest.accessKey=${LAMBDATEST_ACCESS_KEY}
```

**qa-mobile-cloud.properties** (AWS Device Farm)
```properties
remote.provider=aws
aws.devicefarm.url=${AWS_DEVICEFARM_URL}
```

Run with:
```bash
mvn test -Denvironment=qa-cloud
```

---

## CI/CD Integration

### Jenkins

```groovy
pipeline {
    environment {
        LAMBDATEST_USERNAME = credentials('lambdatest-username')
        LAMBDATEST_ACCESS_KEY = credentials('lambdatest-access-key')
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Dremote.provider=lambdatest'
            }
        }
    }
}
```

### GitHub Actions

```yaml
- name: Run Tests on LambdaTest
  env:
    LAMBDATEST_USERNAME: ${{ secrets.LAMBDATEST_USERNAME }}
    LAMBDATEST_ACCESS_KEY: ${{ secrets.LAMBDATEST_ACCESS_KEY }}
  run: mvn clean test -Dremote.provider=lambdatest
```

---

## Troubleshooting

### LambdaTest Issues

**Problem:** Authentication failed
```
Solution: Verify username and access key in config.properties
```

**Problem:** Browser not launching
```
Solution: Check browser and platform compatibility on LambdaTest
```

**Problem:** Test timeout
```
Solution: Increase timeouts in config.properties:
  page.load.timeout=60
  explicit.wait=30
```

### AWS Device Farm Issues

**Problem:** App not found
```
Solution: Verify app ARN in aws.devicefarm.app
```

**Problem:** Connection timeout
```
Solution: Ensure aws.devicefarm.url is correct from test run
```

**Problem:** Device not available
```
Solution: Check device availability in Device Farm console
```

---

## Best Practices

1. **Use Environment Variables for Credentials**
   ```bash
   export LAMBDATEST_USERNAME=your_username
   export LAMBDATEST_ACCESS_KEY=your_access_key
   ```

2. **Tag Tests Appropriately**
   ```gherkin
   @UI @Smoke
   Feature: Login
   ```

3. **Set Meaningful Test Names**
   ```properties
   lambdatest.testName=Login Test - Chrome - Windows 10
   ```

4. **Enable Video Only for Failures**
   ```properties
   lambdatest.video=true
   screenshot.on.failure=true
   ```

5. **Use Build Numbers**
   ```properties
   lambdatest.build=Build-${BUILD_NUMBER}
   ```

---

## Cost Optimization

### LambdaTest
- Use parallel execution wisely (more threads = higher cost)
- Disable video/network logs for passing tests
- Use shorter timeouts
- Run smoke tests locally first

### AWS Device Farm
- Select only required devices
- Use device pools efficiently
- Clean up old test runs
- Monitor usage in AWS Cost Explorer

---

## Comparison: Local vs Remote

| Feature | Local | LambdaTest | AWS Device Farm |
|---------|-------|------------|-----------------|
| Setup | Easy | Medium | Complex |
| Cost | Free | Paid | Paid |
| Browsers | Limited | 3000+ | N/A |
| Devices | Emulators | N/A | Real Devices |
| Parallel | Limited | High | High |
| Debugging | Easy | Medium | Hard |
| CI/CD | Easy | Easy | Medium |

---

## Support

- **LambdaTest**: [Support Portal](https://www.lambdatest.com/support)
- **AWS Device Farm**: [AWS Support](https://aws.amazon.com/support/)
- **Framework Issues**: Check inline documentation in RemoteDriverManager.java

---

## Examples

### Example 1: Run on LambdaTest Chrome

```properties
remote.provider=lambdatest
browser=chrome
lambdatest.platform=Windows 10
lambdatest.browserVersion=latest
```

```bash
mvn test -Dcucumber.filter.tags="@UI"
```

### Example 2: Run on AWS Device Farm Android

```properties
remote.provider=aws
framework.type=mobile
mobile.platformName=Android
aws.devicefarm.url=https://appium-us-west-2.devicefarm.aws.amazon.com/wd/hub
```

```bash
mvn test -Dcucumber.filter.tags="@Mobile"
```

### Example 3: Parallel Execution on LambdaTest

```properties
remote.provider=lambdatest
parallel.execution=true
thread.count=5
```

```bash
mvn test -Dcucumber.filter.tags="@Regression"
```

---

## License

Licensed under the Apache License, Version 2.0.
