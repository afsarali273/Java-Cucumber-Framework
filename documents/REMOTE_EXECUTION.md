# ☁️ Remote Execution - Quick Reference

## Overview

Run tests on **LambdaTest** (web) or **AWS Device Farm** (mobile) without local infrastructure.

---

## Quick Start

### Local (Default)
```properties
remote.provider=
```

### LambdaTest (Web)
```properties
remote.provider=lambdatest
lambdatest.username=YOUR_USERNAME
lambdatest.accessKey=YOUR_ACCESS_KEY
```

### AWS Device Farm (Mobile)
```properties
remote.provider=aws
aws.devicefarm.url=YOUR_DEVICE_FARM_URL
aws.devicefarm.app=YOUR_APP_ARN
```

---

## Benefits

✅ **3000+ browser/OS combinations** (LambdaTest)  
✅ **Real mobile devices** (AWS Device Farm)  
✅ **Zero code changes** - just config  
✅ **Parallel execution** on cloud  
✅ **No infrastructure** maintenance  

---

## Complete Guide

See **REMOTE_EXECUTION_GUIDE.md** in root folder for:
- Detailed setup instructions
- Configuration reference
- Troubleshooting
- Best practices
- Examples

---

## HTML Documentation

See **docs/remote-execution.html** for interactive guide.

---

**Quick Switch:**
```bash
./switch-execution-mode.sh lambdatest
mvn clean test
```
