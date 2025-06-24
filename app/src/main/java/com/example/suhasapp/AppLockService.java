package com.example.suhasapp;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import java.util.Arrays;

public class AppLockService extends AccessibilityService {

    private static final String[] BANNED_APPS = {
            "com.zhiliaoapp.musically", // TikTok
            "com.lenovo.anyshare.gps", // SHAREit
            "com.uc.browser.en", // UC Browser
            "com.kwai.video", // Snack Video
            "com.tencent.mm" // WeChat
            // Add more package names
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
            if (Arrays.asList(BANNED_APPS).contains(packageName)) {
                // Launch lock screen activity
                Intent lockIntent = new Intent(this, LockScreenActivity.class);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lockIntent);
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Handle interruption
    }
}
