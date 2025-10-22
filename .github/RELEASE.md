# GitHub Actions Setup Guide

## Workflows Created

### 1. **build.yml** - Continuous Integration
- Triggers on: push to main/master/develop, pull requests
- Actions:
  - Builds framework with Maven
  - Uploads JAR artifacts (retained for 30 days)
  - Deploys to GitHub Packages on release

### 2. **release.yml** - Release Management
- Triggers on: Git tags (v*)
- Actions:
  - Creates GitHub Release
  - Uploads 3 JARs as release assets:
    - Main JAR
    - Sources JAR
    - Javadoc JAR

## Setup Instructions

### Step 1: Update pom.xml
Replace `YOUR_GITHUB_USERNAME` in pom.xml with your actual GitHub username:
```xml
<url>https://github.com/YOUR_USERNAME/SandsAutoFramework</url>
<distributionManagement>
    <repository>
        <url>https://maven.pkg.github.com/YOUR_USERNAME/SandsAutoFramework</url>
    </repository>
</distributionManagement>
```

### Step 2: Push to GitHub
```bash
cd /Users/afsarali/AquaProjects/SandsAutoFramework
git init
git add .
git commit -m "Initial commit with GitHub Actions"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/SandsAutoFramework.git
git push -u origin main
```

### Step 3: Create a Release
```bash
# Tag the release
git tag -a v1.0 -m "Release version 1.0"
git push origin v1.0
```

This will:
- Trigger the release workflow
- Build all JARs
- Create a GitHub Release
- Upload JARs as downloadable assets

## Using the Framework from GitHub Packages

### Option 1: Download from Releases (Easiest)
1. Go to: `https://github.com/YOUR_USERNAME/SandsAutoFramework/releases`
2. Download `SandsAutoFramework-1.0.jar`
3. Install locally:
```bash
mvn install:install-file \
  -Dfile=SandsAutoFramework-1.0.jar \
  -DgroupId=com.automation \
  -DartifactId=SandsAutoFramework \
  -Dversion=1.0 \
  -Dpackaging=jar
```

### Option 2: Use GitHub Packages (Advanced)
Add to your project's `pom.xml`:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/YOUR_USERNAME/SandsAutoFramework</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.automation</groupId>
    <artifactId>SandsAutoFramework</artifactId>
    <version>1.0</version>
</dependency>
```

Add to `~/.m2/settings.xml`:
```xml
<servers>
    <server>
        <id>github</id>
        <username>YOUR_GITHUB_USERNAME</username>
        <password>YOUR_GITHUB_TOKEN</password>
    </server>
</servers>
```

Generate GitHub token: Settings → Developer settings → Personal access tokens → Generate new token (with `read:packages` scope)

## Workflow Status Badges

Add to README.md:
```markdown
![Build](https://github.com/YOUR_USERNAME/SandsAutoFramework/workflows/Build%20and%20Release%20Framework/badge.svg)
![Release](https://github.com/YOUR_USERNAME/SandsAutoFramework/workflows/Create%20Release%20with%20Artifacts/badge.svg)
```

## Artifacts Location

After each build:
- **Build artifacts**: Actions tab → Select workflow run → Artifacts section
- **Release artifacts**: Releases page → Download assets

## Version Management

To release a new version:
```bash
# Update version in pom.xml
mvn versions:set -DnewVersion=1.1

# Commit and tag
git add pom.xml
git commit -m "Bump version to 1.1"
git tag -a v1.1 -m "Release version 1.1"
git push origin main
git push origin v1.1
```

## Troubleshooting

**Build fails**: Check Java version (must be 11+)
**Deploy fails**: Verify GitHub token has `write:packages` permission
**Artifacts not found**: Check workflow logs in Actions tab
