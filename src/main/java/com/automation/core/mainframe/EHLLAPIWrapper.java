package com.automation.core.mainframe;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public class EHLLAPIWrapper {

    public interface EHLLAPI extends Library {
        EHLLAPI INSTANCE = Native.load("pcshll32", EHLLAPI.class);
        void hllapi(IntByReference function, byte[] data, IntByReference length, IntByReference returnCode);
    }

    public void connect(String sessionId) {
        callHLLAPI(1, sessionId);
    }

    public void disconnect() {
        callHLLAPI(2, "");
    }

    public void sendKeys(String keys) {
        callHLLAPI(3, keys);
    }

    public String copyFromScreen(int row, int col, int length) {
        String coords = String.format("%04d%04d", row, col);
        callHLLAPI(4, coords);
        return "";
    }

    public void moveCursor(int row, int col) {
        String coords = String.format("%04d%04d", row, col);
        callHLLAPI(18, coords);
    }

    public void waitForUnlock() {
        callHLLAPI(4, "");
    }

    public String getScreenText() {
        return callHLLAPIString(8, "");
    }

    private void callHLLAPI(int functionCode, String data) {
        IntByReference func = new IntByReference(functionCode);
        byte[] buffer = data.getBytes();
        IntByReference length = new IntByReference(buffer.length);
        IntByReference rc = new IntByReference(0);
        EHLLAPI.INSTANCE.hllapi(func, buffer, length, rc);
    }

    private String callHLLAPIString(int functionCode, String data) {
        IntByReference func = new IntByReference(functionCode);
        byte[] buffer = new byte[32768];
        IntByReference length = new IntByReference(buffer.length);
        IntByReference rc = new IntByReference(0);
        EHLLAPI.INSTANCE.hllapi(func, buffer, length, rc);
        return new String(buffer).trim();
    }
}
