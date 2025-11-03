# GitHub Packages - Publishing & Consuming Guide

Complete guide for publishing the framework to GitHub Packages and using it in downstream projects.

---

## Table of Contents
1. [Publishing to GitHub Packages](#publishing-to-github-packages)
2. [Consuming in Downstream Projects](#consuming-in-downstream-projects)
3. [Troubleshooting](#troubleshooting)

---

## Publishing to GitHub Packages

### Prerequisites
- GitHub repository with the framework code
- Maven project with proper `pom.xml` configuration
- GitHub Personal Access Token (PAT) with package permissions

### Step 1: Create GitHub Personal Access Token (PAT)

1. Go to GitHub → **Settings** (your profile settings, not repository)
2. Navigate to **Developer settings** → **Personal access tokens** → **Tokens (classic)**
3. Click **Generate new token** → **Generate new token (classic)**
4. Configure the token:
   - **Note**: `Maven Package Publishing`
   - **Expiration**: Choose appropriate duration (90 days, 1 year, or no expiration)
   - **Select scopes**:
     - ✅ `write:packages` - Upload packages to GitHub Package Registry
     - ✅ `read:packages` - Download packages from GitHub Package Registry
     - ✅ `repo` - Full control of private repositories (if repo is private)
5. Click **Generate token**
6. **IMPORTANT**: Copy the token immediately (you won't see it again!)

### Step 2: Add PAT to Repository Secrets

1. Go to your repository → **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Add secret:
   - **Name**: `PAT_TOKEN`
   - **Secret**: Paste your PAT token
4. Click **Add secret**

### Step 3: Configure pom.xml

Ensure your `pom.xml` has the correct configuration:

```xml
<groupId>com.automation</groupId>
<artifactId>java-cucumber-framework</artifactId>
<version>1.0.0</version>

<!-- IMPORTANT: artifactId must be lowercase with hyphens only -->

<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/afsarali273/java-cucumber-framework</url>
    </repository>
</distributionManagement>
```

**Key Rules:**
- ✅ `artifactId` must be **lowercase letters, digits, or hyphens only**
- ❌ No uppercase letters in `artifactId` (causes 422 error)
- ✅ URL format: `https://maven.pkg.github.com/OWNER/REPOSITORY`

### Step 4: Publish the Package

**Option A: Using Git Tags (Recommended)**
```bash
# Commit your changes
git add .
git commit -m "Release version 1.0.0"
git push

# Create and push tag
git tag v1.0.0
git push origin v1.0.0
```

The GitHub Actions workflow will automatically publish to GitHub Packages.

**Option B: Manual Workflow Trigger**
1. Go to **Actions** tab in your repository
2. Select **Publish to GitHub Packages** workflow
3. Click **Run workflow**
4. Select branch and click **Run workflow**

**Option C: Local Publishing**
```bash
# Create ~/.m2/settings.xml
cat > ~/.m2/settings.xml << EOF
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_PAT_TOKEN</password>
    </server>
  </servers>
</settings>
EOF

# Deploy to GitHub Packages
mvn clean deploy -DskipTests
```

### Step 5: Verify Package Publication

1. Go to your repository on GitHub
2. Click **Packages** (right sidebar)
3. You should see `java-cucumber-framework` package listed
4. Click on the package to view versions and details

---

## Consuming in Downstream Projects

### Step 1: Create PAT for Consuming Packages

If you don't already have a PAT, create one with:
- ✅ `read:packages` - Download packages from GitHub Package Registry

### Step 2: Configure Maven Settings

Create or update `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_PAT_TOKEN</password>
    </server>
  </servers>
</settings>
```

**For CI/CD (GitHub Actions):**

Add to your workflow:
```yaml
- name: Configure Maven settings
  run: |
    mkdir -p ~/.m2
    cat > ~/.m2/settings.xml << EOF
    <settings>
      <servers>
        <server>
          <id>github</id>
          <username>${{ github.actor }}</username>
          <password>${{ secrets.GITHUB_TOKEN }}</password>
        </server>
      </servers>
    </settings>
    EOF
```

### Step 3: Add Repository to Your Project's pom.xml

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/afsarali273/java-cucumber-framework</url>
    </repository>
</repositories>
```

### Step 4: Add Framework Dependency

```xml
<dependencies>
    <dependency>
        <groupId>com.automation</groupId>
        <artifactId>java-cucumber-framework</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Step 5: Build Your Project

```bash
mvn clean install
```

Maven will automatically download the framework from GitHub Packages.

---

## Example: Complete Downstream Project Setup

### Project Structure
```
my-automation-project/
├── pom.xml
├── src/
│   └── test/
│       ├── java/
│       │   └── com/mycompany/tests/
│       │       └── LoginTest.java
│       └── resources/
│           └── features/
│               └── login.feature
└── .github/
    └── workflows/
        └── test.yml
```

### pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mycompany</groupId>
    <artifactId>my-automation-tests</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>

    <repositories>
        <repository>
            <id>github</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/afsarali273/java-cucumber-framework</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Sands Automation Framework -->
        <dependency>
            <groupId>com.automation</groupId>
            <artifactId>java-cucumber-framework</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### GitHub Actions Workflow (.github/workflows/test.yml)
```yaml
name: Run Tests

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 11
      uses: actions/setup-java@v4
      with:
        java-version: '11'
        distribution: 'temurin'
        cache: maven
    
    - name: Configure Maven for GitHub Packages
      run: |
        mkdir -p ~/.m2
        cat > ~/.m2/settings.xml << EOF
        <settings>
          <servers>
            <server>
              <id>github</id>
              <username>${{ github.actor }}</username>
              <password>${{ secrets.GITHUB_TOKEN }}</password>
            </server>
          </servers>
        </settings>
        EOF
    
    - name: Run Tests
      run: mvn clean test
```

### Sample Test Class
```java
package com.mycompany.tests;

import com.automation.core.driver.DriverManager;
import com.automation.keywords.UIKeywords;
import org.testng.annotations.Test;

public class LoginTest {
    
    @Test
    public void testLogin() {
        UIKeywords.openBrowser();
        UIKeywords.navigateToURL("https://example.com");
        UIKeywords.enterText("#username", "testuser");
        UIKeywords.enterText("#password", "password123");
        UIKeywords.clickElement("#loginBtn");
        UIKeywords.closeBrowser();
    }
}
```

---

## Troubleshooting

### Error: 422 Unprocessable Entity

**Cause**: artifactId contains uppercase letters or version already exists

**Solution**:
```xml
<!-- ❌ Wrong -->
<artifactId>Java-Cucumber-Framework</artifactId>

<!-- ✅ Correct -->
<artifactId>java-cucumber-framework</artifactId>
```

### Error: 401 Unauthorized

**Cause**: Invalid or missing PAT token

**Solution**:
1. Verify PAT token has `read:packages` or `write:packages` scope
2. Check token hasn't expired
3. Ensure `~/.m2/settings.xml` has correct credentials
4. For GitHub Actions, verify `GITHUB_TOKEN` or `PAT_TOKEN` secret exists

### Error: Could not find artifact

**Cause**: Repository not configured or package doesn't exist

**Solution**:
1. Add repository to `pom.xml`:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/afsarali273/java-cucumber-framework</url>
    </repository>
</repositories>
```
2. Verify package exists on GitHub
3. Check version number is correct

### Error: Transfer failed for repository

**Cause**: Network issues or authentication problems

**Solution**:
1. Run with debug: `mvn clean install -X`
2. Check internet connection
3. Verify GitHub Packages is accessible
4. Try clearing Maven cache: `rm -rf ~/.m2/repository/com/automation/java-cucumber-framework`

### Package Not Visible

**Cause**: Package visibility settings

**Solution**:
1. Go to package settings on GitHub
2. Under "Danger Zone" → "Change package visibility"
3. Set to "Public" if you want it accessible to everyone
4. Or add collaborators for private packages

---

## Best Practices

### Version Management
- ✅ Use semantic versioning: `MAJOR.MINOR.PATCH`
- ✅ Increment version for each release
- ✅ Use SNAPSHOT for development: `1.0.0-SNAPSHOT`
- ❌ Don't reuse version numbers (GitHub Packages is immutable)

### Security
- ✅ Use PAT tokens with minimal required scopes
- ✅ Set token expiration dates
- ✅ Store tokens in GitHub Secrets, never in code
- ✅ Rotate tokens regularly
- ❌ Never commit tokens to repository

### CI/CD
- ✅ Automate publishing with GitHub Actions
- ✅ Publish on tagged releases only
- ✅ Run tests before publishing
- ✅ Generate and attach sources/javadoc JARs

### Documentation
- ✅ Document all public APIs
- ✅ Provide usage examples
- ✅ Maintain CHANGELOG.md
- ✅ Update README with latest version

---

## Quick Reference

### Publishing Commands
```bash
# Tag and publish
git tag v1.0.0 && git push origin v1.0.0

# Local publish
mvn clean deploy -DskipTests

# Build only (no publish)
mvn clean install -DskipTests
```

### Consuming Commands
```bash
# Download dependencies
mvn dependency:resolve

# Update dependencies
mvn clean install -U

# View dependency tree
mvn dependency:tree
```

### GitHub Package URLs
- **Repository**: `https://github.com/afsarali273/Java-Cucumber-Framework`
- **Package**: `https://github.com/afsarali273/Java-Cucumber-Framework/packages`
- **Maven URL**: `https://maven.pkg.github.com/afsarali273/java-cucumber-framework`

---

## Support

For issues or questions:
- 📖 Check [README.md](../README.md)
- 🐛 Report issues on [GitHub Issues](https://github.com/afsarali273/Java-Cucumber-Framework/issues)
- 📧 Contact: Framework maintainers

---

**Last Updated**: January 2025  
**Framework Version**: 1.0.0
