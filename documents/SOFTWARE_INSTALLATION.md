# 💻 Software Installation Guide - Windows

## Overview

Complete guide to install all required software for the Sands Automation Framework on Windows machines.

---

## 📋 Installation Checklist

- [ ] Java JDK 17+
- [ ] Maven 3.8+
- [ ] Git
- [ ] IntelliJ IDEA / Eclipse
- [ ] Chrome Browser
- [ ] ChromeDriver
- [ ] Firefox Browser (Optional)
- [ ] GeckoDriver (Optional)
- [ ] Node.js (for Playwright)
- [ ] Playwright Browsers
- [ ] Appium (for Mobile Testing)
- [ ] Android Studio (for Mobile Testing)
- [ ] WinAppDriver (for Desktop Testing)
- [ ] 3270 Terminal Emulator (for Mainframe Testing)
- [ ] Postman (Optional - for API testing)
- [ ] Allure Command Line

**Estimated Time:** 2-3 hours

---

## 1️⃣ Java JDK 17+ Installation

### Download

1. Visit: https://www.oracle.com/java/technologies/downloads/
2. Download: **Java SE Development Kit 17** (Windows x64 Installer)
3. File: `jdk-17_windows-x64_bin.exe`

### Install

1. Run the installer
2. Click **Next** through the wizard
3. Note the installation path (e.g., `C:\Program Files\Java\jdk-17`)
4. Click **Close** when complete

### Configure Environment Variables

1. Open **System Properties**:
   - Press `Win + Pause/Break` OR
   - Right-click **This PC** → **Properties** → **Advanced system settings**

2. Click **Environment Variables**

3. Under **System variables**, click **New**:
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-17`
   - Click **OK**

4. Edit **Path** variable:
   - Select **Path** → Click **Edit**
   - Click **New**
   - Add: `%JAVA_HOME%\bin`
   - Click **OK**

### Verify Installation

```cmd
java -version
```

**Expected Output:**
```
java version "17.0.x"
Java(TM) SE Runtime Environment (build 17.0.x)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x)
```

---

## 2️⃣ Maven 3.8+ Installation

### Download

1. Visit: https://maven.apache.org/download.cgi
2. Download: **Binary zip archive** (apache-maven-3.9.x-bin.zip)

### Install

1. Extract to: `C:\Program Files\Apache\maven`
2. Rename folder to: `apache-maven-3.9.x`

### Configure Environment Variables

1. Open **Environment Variables** (same as Java)

2. Under **System variables**, click **New**:
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven\apache-maven-3.9.x`
   - Click **OK**

3. Edit **Path** variable:
   - Select **Path** → Click **Edit**
   - Click **New**
   - Add: `%MAVEN_HOME%\bin`
   - Click **OK**

### Verify Installation

```cmd
mvn -version
```

**Expected Output:**
```
Apache Maven 3.9.x
Maven home: C:\Program Files\Apache\maven\apache-maven-3.9.x
Java version: 17.0.x
```

---

## 3️⃣ Git Installation

### Download

1. Visit: https://git-scm.com/download/win
2. Download: **64-bit Git for Windows Setup**

### Install

1. Run the installer
2. Accept default options (recommended)
3. Important selections:
   - Editor: **Use Visual Studio Code** (or your preference)
   - PATH: **Git from the command line and also from 3rd-party software**
   - Line endings: **Checkout Windows-style, commit Unix-style**
4. Click **Install**

### Configure Git

```cmd
git config --global user.name "Your Name"
git config --global user.email "your.email@company.com"
```

### Verify Installation

```cmd
git --version
```

**Expected Output:**
```
git version 2.43.x
```

---

## 4️⃣ IntelliJ IDEA Installation

### Download

1. Visit: https://www.jetbrains.com/idea/download/#section=windows
2. Download: **Community Edition** (Free) or **Ultimate Edition** (Paid)

### Install

1. Run the installer
2. Select installation options:
   - [x] Create Desktop Shortcut
   - [x] Update PATH variable
   - [x] Add "Open Folder as Project"
   - [x] .java file association
3. Click **Install**
4. Restart computer if prompted

### Configure IntelliJ

1. Launch IntelliJ IDEA
2. Install plugins:
   - **Cucumber for Java**
   - **Gherkin**
   - **Maven Helper**
3. Configure JDK:
   - File → Project Structure → SDKs
   - Add JDK 17

---

## 5️⃣ Chrome Browser & ChromeDriver

### Chrome Browser

1. Visit: https://www.google.com/chrome/
2. Download and install Chrome
3. Note the version (e.g., 120.0.6099.109)

### ChromeDriver

**Option 1: Automatic (Recommended)**
Framework handles ChromeDriver automatically using WebDriverManager.

**Option 2: Manual**

1. Visit: https://chromedriver.chromium.org/downloads
2. Download version matching your Chrome browser
3. Extract `chromedriver.exe`
4. Place in: `C:\WebDrivers\chromedriver.exe`
5. Add to PATH:
   - Environment Variables → Path → New
   - Add: `C:\WebDrivers`

### Verify

```cmd
chromedriver --version
```

---

## 6️⃣ Firefox & GeckoDriver (Optional)

### Firefox Browser

1. Visit: https://www.mozilla.org/firefox/
2. Download and install Firefox

### GeckoDriver

1. Visit: https://github.com/mozilla/geckodriver/releases
2. Download: `geckodriver-vX.XX.X-win64.zip`
3. Extract `geckodriver.exe`
4. Place in: `C:\WebDrivers\geckodriver.exe`

---

## 7️⃣ Node.js (for Playwright)

### Download

1. Visit: https://nodejs.org/
2. Download: **LTS version** (e.g., 20.x.x)

### Install

1. Run the installer
2. Accept default options
3. Ensure **Add to PATH** is checked

### Verify

```cmd
node --version
npm --version
```

### Install Playwright

```cmd
npm install -g playwright
npx playwright install
```

This installs Chromium, Firefox, and WebKit browsers for Playwright.

---

## 8️⃣ Appium (for Mobile Testing)

### Prerequisites

- Node.js (installed in step 7)

### Install Appium

```cmd
npm install -g appium
npm install -g appium-doctor
```

### Verify

```cmd
appium --version
appium-doctor
```

### Install Appium Drivers

```cmd
appium driver install uiautomator2
appium driver install xcuitest
```

---

## 9️⃣ Android Studio (for Mobile Testing)

### Download

1. Visit: https://developer.android.com/studio
2. Download: **Android Studio**

### Install

1. Run the installer
2. Select components:
   - [x] Android Studio
   - [x] Android SDK
   - [x] Android Virtual Device
3. Complete installation

### Configure Environment Variables

1. Add **ANDROID_HOME**:
   - Variable name: `ANDROID_HOME`
   - Variable value: `C:\Users\<YourUsername>\AppData\Local\Android\Sdk`

2. Add to **Path**:
   - `%ANDROID_HOME%\platform-tools`
   - `%ANDROID_HOME%\tools`
   - `%ANDROID_HOME%\tools\bin`

### Create Virtual Device

1. Open Android Studio
2. Tools → Device Manager
3. Create Device → Select device (e.g., Pixel 6)
4. Select system image (e.g., Android 13)
5. Finish

### Verify

```cmd
adb version
```

---

## 🔟 WinAppDriver (for Desktop Testing)

### Download

1. Visit: https://github.com/microsoft/WinAppDriver/releases
2. Download: **WindowsApplicationDriver.msi**

### Install

1. Run the installer
2. Install to: `C:\Program Files (x86)\Windows Application Driver`

### Enable Developer Mode

1. Settings → Update & Security → For developers
2. Select **Developer mode**

### Start WinAppDriver

```cmd
cd "C:\Program Files (x86)\Windows Application Driver"
WinAppDriver.exe
```

**Expected Output:**
```
Windows Application Driver listening on http://127.0.0.1:4723
```

---

## 1️⃣1️⃣ 3270 Terminal Emulator (for Mainframe)

### Options

Choose one of the following:

#### Option 1: IBM Personal Communications (PCOMM)
- Contact IT for installation
- Enterprise license required

#### Option 2: Rumba Desktop
- Contact IT for installation
- Enterprise license required

#### Option 3: Extra! X-treme
- Contact IT for installation
- Enterprise license required

#### Option 4: BlueZone
- Contact IT for installation
- Enterprise license required

### Verify EHLLAPI DLL

After installation, verify `pcshll32.dll` exists:
- Location: `C:\Windows\System32\pcshll32.dll`
- If not found, contact IT support

### Configure Session

1. Open terminal emulator
2. Create new session:
   - Session ID: **A**
   - Host: `mainframe.company.com`
   - Port: `23`
3. Save configuration

---

## 1️⃣2️⃣ Postman (Optional - for API Testing)

### Download

1. Visit: https://www.postman.com/downloads/
2. Download: **Windows 64-bit**

### Install

1. Run the installer
2. Sign in or create account (optional)
3. Explore sample collections

---

## 1️⃣3️⃣ Allure Command Line

### Install via Scoop (Recommended)

```cmd
# Install Scoop
powershell -Command "Set-ExecutionPolicy RemoteSigned -Scope CurrentUser"
powershell -Command "irm get.scoop.sh | iex"

# Install Allure
scoop install allure
```

### Verify

```cmd
allure --version
```

---

## 🔧 IDE Configuration

### IntelliJ IDEA Plugins

1. File → Settings → Plugins
2. Install:
   - **Cucumber for Java**
   - **Gherkin**
   - **Maven Helper**
   - **Rainbow Brackets**
   - **SonarLint**

### IntelliJ Project Setup

1. File → Open
2. Select framework folder
3. Wait for Maven import
4. Configure JDK:
   - File → Project Structure → Project
   - SDK: Java 17
   - Language level: 17

### Run Configuration

1. Run → Edit Configurations
2. Add New → TestNG
3. Configure:
   - Name: `Run All Tests`
   - Test kind: `Suite`
   - Suite: `testng.xml`
4. Apply → OK

---

## ✅ Verification Script

Create `verify-installation.bat`:

```batch
@echo off
echo ========================================
echo Verifying Software Installation
echo ========================================
echo.

echo Checking Java...
java -version
echo.

echo Checking Maven...
mvn -version
echo.

echo Checking Git...
git --version
echo.

echo Checking Node.js...
node --version
echo.

echo Checking npm...
npm --version
echo.

echo Checking Playwright...
npx playwright --version
echo.

echo Checking Appium...
appium --version
echo.

echo Checking ADB...
adb version
echo.

echo Checking Allure...
allure --version
echo.

echo ========================================
echo Verification Complete!
echo ========================================
pause
```

Run the script:
```cmd
verify-installation.bat
```

---

## 🚀 Clone Framework Repository

### Using Git Bash

```bash
cd C:\Projects
git clone https://github.com/your-org/sands-automation-framework.git
cd sands-automation-framework
```

### Using IntelliJ

1. File → New → Project from Version Control
2. URL: `https://github.com/your-org/sands-automation-framework.git`
3. Directory: `C:\Projects\sands-automation-framework`
4. Clone

---

## 🧪 Run First Test

### Command Line

```cmd
cd C:\Projects\sands-automation-framework
mvn clean test -Dcucumber.filter.tags="@Smoke"
```

### IntelliJ

1. Right-click `testng.xml`
2. Run 'testng.xml'
3. View results in Run window

---

## 📊 View Reports

### ExtentReport

```cmd
start test-output\reports\ExtentReport_<timestamp>.html
```

### Allure Report

```cmd
mvn allure:serve
```

---

## 🐛 Troubleshooting

### Issue: Java not recognized

**Solution:**
- Verify JAVA_HOME is set correctly
- Restart command prompt
- Check PATH includes `%JAVA_HOME%\bin`

### Issue: Maven not recognized

**Solution:**
- Verify MAVEN_HOME is set correctly
- Restart command prompt
- Check PATH includes `%MAVEN_HOME%\bin`

### Issue: ChromeDriver version mismatch

**Solution:**
- Update Chrome browser
- Framework auto-downloads matching ChromeDriver
- Or manually download matching version

### Issue: Appium not starting

**Solution:**
```cmd
npm uninstall -g appium
npm install -g appium@latest
```

### Issue: WinAppDriver not starting

**Solution:**
- Enable Developer Mode in Windows Settings
- Run as Administrator
- Check port 4723 is not in use

### Issue: Mainframe connection failed

**Solution:**
- Verify terminal emulator is running
- Check session ID matches config
- Verify pcshll32.dll exists
- Contact IT for network access

---

## 📞 Support

### Getting Help

- **IT Support:** it-support@company.com
- **Framework Team:** automation-support@company.com
- **Slack:** #automation-framework
- **Documentation:** `/docs` folder

---

## 📝 Installation Checklist Summary

After completing all installations, you should have:

✅ Java JDK 17+ installed and configured  
✅ Maven 3.8+ installed and configured  
✅ Git installed and configured  
✅ IntelliJ IDEA installed with plugins  
✅ Chrome browser and ChromeDriver  
✅ Node.js and Playwright installed  
✅ Appium installed (for mobile testing)  
✅ Android Studio configured (for mobile testing)  
✅ WinAppDriver installed (for desktop testing)  
✅ 3270 Terminal Emulator configured (for mainframe)  
✅ Allure command line installed  
✅ Framework repository cloned  
✅ First test executed successfully  
✅ Reports generated and viewed  

**Congratulations! Your environment is ready for automation testing!** 🎉

---

**Document Version:** 1.0  
**Last Updated:** January 2025  
**Next Review:** March 2025
