# Testing Remote Execution Integration

## Pre-Test Checklist

✅ RemoteDriverManager.java created  
✅ DriverManager.java updated  
✅ config.properties updated with remote settings  
✅ Documentation created  
✅ Example configs created  
✅ Helper scripts created  

---

## Test Plan

### Test 1: Local Execution (Baseline)

**Purpose:** Verify existing tests still work

**Steps:**
1. Ensure `remote.provider=` (empty) in config.properties
2. Run: `mvn clean test -Dcucumber.filter.tags="@UI"`
3. Verify tests run locally

**Expected Result:** ✅ Tests run on local browser

---

### Test 2: LambdaTest Web Execution

**Purpose:** Verify LambdaTest integration

**Prerequisites:**
- LambdaTest account
- Valid username and access key

**Steps:**
1. Update config.properties:
   ```properties
   remote.provider=lambdatest
   lambdatest.username=YOUR_USERNAME
   lambdatest.accessKey=YOUR_ACCESS_KEY
   framework.type=selenium
   browser=chrome
   ```

2. Run: `mvn clean test -Dcucumber.filter.tags="@UI"`

3. Check LambdaTest dashboard: https://automation.lambdatest.com/

**Expected Result:** 
- ✅ Tests run on LambdaTest cloud
- ✅ Session visible in LambdaTest dashboard
- ✅ Video recorded
- ✅ Logs captured

**Verification Points:**
- Check console logs for "LambdaTest RemoteWebDriver initialized"
- Verify test name appears in LambdaTest dashboard
- Confirm browser/OS matches config

---

### Test 3: AWS Device Farm Mobile Execution

**Purpose:** Verify AWS Device Farm integration

**Prerequisites:**
- AWS account with Device Farm access
- App uploaded to Device Farm
- Test run created

**Steps:**
1. Upload app to Device Farm
2. Create test run and get Appium URL
3. Update config.properties:
   ```properties
   remote.provider=aws
   framework.type=mobile
   mobile.platformName=Android
   aws.devicefarm.url=YOUR_DEVICE_FARM_URL
   aws.devicefarm.app=YOUR_APP_ARN
   ```

4. Run: `mvn clean test -Dcucumber.filter.tags="@Mobile"`

5. Check AWS Device Farm console

**Expected Result:**
- ✅ Tests run on AWS Device Farm
- ✅ Session visible in Device Farm console
- ✅ Video recorded
- ✅ Device logs captured

**Verification Points:**
- Check console logs for "AWS Device Farm driver initialized"
- Verify test appears in Device Farm console
- Confirm device matches config

---

### Test 4: Parallel Execution on LambdaTest

**Purpose:** Verify thread-safety with parallel execution

**Steps:**
1. Update config.properties:
   ```properties
   remote.provider=lambdatest
   parallel.execution=true
   thread.count=3
   ```

2. Run: `mvn clean test -Dcucumber.filter.tags="@Regression"`

3. Check LambdaTest dashboard for multiple concurrent sessions

**Expected Result:**
- ✅ 3 parallel sessions on LambdaTest
- ✅ Each thread has isolated session
- ✅ No cross-thread interference
- ✅ All tests complete successfully

**Verification Points:**
- Check LambdaTest dashboard shows 3 concurrent tests
- Verify each test has unique session ID
- Confirm no driver conflicts in logs

---

### Test 5: Switch Between Modes

**Purpose:** Verify easy switching between local and remote

**Steps:**
1. Run: `./switch-execution-mode.sh local`
2. Run tests: `mvn test -Dcucumber.filter.tags="@UI"`
3. Verify runs locally

4. Run: `./switch-execution-mode.sh lambdatest`
5. Run tests: `mvn test -Dcucumber.filter.tags="@UI"`
6. Verify runs on LambdaTest

**Expected Result:**
- ✅ Script updates config correctly
- ✅ Tests switch between local and remote
- ✅ No manual config editing needed

---

### Test 6: Runtime Override

**Purpose:** Verify runtime property override

**Steps:**
1. Set config.properties: `remote.provider=`
2. Run: `mvn test -Dremote.provider=lambdatest -Dcucumber.filter.tags="@UI"`

**Expected Result:**
- ✅ Tests run on LambdaTest despite config saying local
- ✅ Runtime property takes precedence

---

### Test 7: Error Handling

**Purpose:** Verify proper error messages

**Test 7a: Missing Credentials**
```properties
remote.provider=lambdatest
lambdatest.username=
lambdatest.accessKey=
```
**Expected:** ❌ Clear error: "LambdaTest credentials not configured"

**Test 7b: Invalid Provider**
```properties
remote.provider=invalid_provider
```
**Expected:** ❌ Clear error: "Unsupported remote provider: invalid_provider"

**Test 7c: Missing Device Farm URL**
```properties
remote.provider=aws
aws.devicefarm.url=
```
**Expected:** ❌ Clear error: "AWS Device Farm URL not configured"

---

## Validation Checklist

### Code Quality
- [ ] RemoteDriverManager follows framework patterns
- [ ] Thread-safe implementation (ThreadLocal)
- [ ] Proper exception handling
- [ ] Logging at key points
- [ ] No hardcoded values

### Functionality
- [ ] Local execution works (baseline)
- [ ] LambdaTest execution works
- [ ] AWS Device Farm execution works
- [ ] Parallel execution works
- [ ] Mode switching works
- [ ] Error handling works

### Documentation
- [ ] REMOTE_EXECUTION_GUIDE.md complete
- [ ] REMOTE_EXECUTION_SUMMARY.md clear
- [ ] README.md updated
- [ ] Example configs provided
- [ ] Helper scripts work

### Backward Compatibility
- [ ] Existing tests run without changes
- [ ] No breaking changes
- [ ] Default behavior unchanged (local)
- [ ] All existing features work

---

## Performance Benchmarks

### Local vs Remote Execution Time

**Test Suite:** 10 UI tests

| Mode | Time | Notes |
|------|------|-------|
| Local | ~2 min | Baseline |
| LambdaTest (1 thread) | ~3 min | Network overhead |
| LambdaTest (5 threads) | ~1 min | Parallel speedup |

---

## Troubleshooting Tests

### Issue: Tests still run locally when remote.provider=lambdatest

**Debug Steps:**
1. Check config.properties has correct value
2. Verify no spaces: `remote.provider=lambdatest` (not `remote.provider= lambdatest`)
3. Check logs for "LambdaTest RemoteWebDriver initialized"
4. Verify credentials are set

### Issue: Connection timeout to LambdaTest

**Debug Steps:**
1. Verify internet connection
2. Check LambdaTest status page
3. Verify credentials are correct
4. Check firewall/proxy settings

### Issue: Parallel tests fail on LambdaTest

**Debug Steps:**
1. Check LambdaTest plan supports parallel execution
2. Verify thread.count doesn't exceed plan limit
3. Check for rate limiting in logs

---

## Success Criteria

✅ All 7 tests pass  
✅ No breaking changes to existing tests  
✅ Documentation is clear and complete  
✅ Error messages are helpful  
✅ Performance is acceptable  
✅ Code follows framework patterns  

---

## Sign-Off

**Tested By:** _________________  
**Date:** _________________  
**Result:** ☐ PASS  ☐ FAIL  
**Notes:** _________________

---

## Next Steps After Testing

1. ✅ Commit changes to repository
2. ✅ Update team documentation
3. ✅ Train team on remote execution
4. ✅ Set up CI/CD integration
5. ✅ Monitor cloud usage and costs

---

## Quick Test Commands

```bash
# Test local
./switch-execution-mode.sh local
mvn clean test -Dcucumber.filter.tags="@UI"

# Test LambdaTest
./switch-execution-mode.sh lambdatest
mvn clean test -Dcucumber.filter.tags="@UI"

# Test AWS Device Farm
./switch-execution-mode.sh aws
mvn clean test -Dcucumber.filter.tags="@Mobile"

# Test parallel on LambdaTest
mvn test -Dremote.provider=lambdatest -Dparallel.execution=true -Dthread.count=5
```

---

**Framework Version:** 2.0 (with Remote Execution)  
**Test Plan Version:** 1.0  
**Last Updated:** 2025-01-21
