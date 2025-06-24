package com.example.suhasapp;

import androidx.appcompat.app.AppCompatActivity;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityManager; // FIXED: Correct import
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView appListView;
    private Button scanButton;
    private TextView statusTextView;

    // Comprehensive list of banned Chinese apps
    private static final String[] BANNED_APPS = {
            // Social Media & Video
            "com.zhiliaoapp.musically",      // TikTok
            "com.ss.android.ugc.trill",      // TikTok India
            "com.kwai.video",                // Snack Video (Kwai)
            "video.like",                    // Likee
            "com.sina.weibo",                // Weibo
            "com.tencent.mm",                // WeChat
            "com.immomo.momo",               // Momo
            "com.yy.hiyo",                   // BIGO LIVE
            "live.like",                     // Like

            // File Sharing & Browsers
            "com.lenovo.anyshare.gps",       // SHAREit
            "com.uc.browser.en",             // UC Browser
            "com.UCMobile.intl",             // UC Browser International
            "com.mi.globalbrowser",          // MI Browser
            "com.opera.mini.native",         // Opera Mini (Chinese owned)

            // Utilities & Tools
            "com.intsig.camscanner",         // CamScanner
            "com.commsource.beautyplus",     // Beauty Plus
            "com.wondershare.fotophire",     // Fotophire
            "com.meitu.meiyancamera",        // Meitu
            "com.benqu.wuta",                // Wuta Camera
            "com.mt.mtxx.mtxx",              // MTXX
            "com.ss.android.article.news",   // TopBuzz
            "com.newsdog",                   // NewsDog

            // Games
            "com.tencent.ig",                // PUBG Mobile
            "com.tencent.tmgp.pubgmhd",      // PUBG Mobile HD
            "com.elex.kok",                  // Clash of Kings
            "com.miniclip.plagueinc",        // Plague Inc (partially Chinese owned)
            "com.tencent.mm",                // Mobile Legends (partially)

            // Shopping & Business
            "com.alibaba.aliexpresshd",      // AliExpress
            "com.shein.android",             // SHEIN
            "com.taobao.taobao",             // Taobao
            "com.alibaba.android.rimet",     // DingTalk
            "com.tencent.wework",            // WeWork

            // Others
            "com.duokan.phone.remotecontroller", // Mi Remote Controller
            "com.mi.android.globalFileexplorer", // Mi File Manager
            "com.xiaomi.scanner",            // Mi Scanner
            "com.miui.gallery",              // Mi Gallery
            "com.cleanmaster.mguard",        // Clean Master
            "com.kika.keyboard"              // Kika Keyboard
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();

        // Optional: Check accessibility service status
        checkAccessibilityServiceStatus();
    }

    private void initViews() {
        appListView = findViewById(R.id.app_list_view);
        scanButton = findViewById(R.id.scan_button);
        statusTextView = findViewById(R.id.status_text_view); // Add this to your layout
    }

    private void setupClickListeners() {
        scanButton.setOnClickListener(v -> scanForBannedApps());
    }

    private void scanForBannedApps() {
        scanButton.setEnabled(false);
        scanButton.setText("Scanning...");

        List<String> bannedAppsFound = new ArrayList<>();
        List<String> installedBannedPackages = new ArrayList<>();
        PackageManager pm = getPackageManager();

        // Method 1: Use queries approach (more efficient and privacy-friendly)
        for (String packageName : BANNED_APPS) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                if (packageInfo != null) {
                    ApplicationInfo appInfo = packageInfo.applicationInfo;
                    String appName = appInfo.loadLabel(pm).toString();
                    bannedAppsFound.add("⚠️ " + appName + "\n   (" + packageName + ")");
                    installedBannedPackages.add(packageName);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // App not installed - this is good!
            }
        }

        displayResults(bannedAppsFound, installedBannedPackages);

        scanButton.setEnabled(true);
        scanButton.setText("Scan for Banned Apps");
    }

    private void displayResults(List<String> bannedAppsFound, List<String> packageNames) {
        String statusMessage;

        if (bannedAppsFound.isEmpty()) {
            statusMessage = "✅ Device Clean!\nNo banned Chinese apps detected.";
            bannedAppsFound.add("✅ No banned Chinese apps found");
            bannedAppsFound.add("Your device is secure!");
            Toast.makeText(this, "Device is clean - no banned apps found!", Toast.LENGTH_LONG).show();
        } else {
            statusMessage = "⚠️ Warning!\n" + bannedAppsFound.size() + " banned Chinese app(s) detected.";
            bannedAppsFound.add(0, "📋 SCAN RESULTS:");
            bannedAppsFound.add(1, "Found " + (bannedAppsFound.size() - 2) + " banned app(s)");
            bannedAppsFound.add(2, ""); // Empty line for spacing

            Toast.makeText(this, "Found " + packageNames.size() + " banned apps! Check the list below.",
                    Toast.LENGTH_LONG).show();
        }

        if (statusTextView != null) {
            statusTextView.setText(statusMessage);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, bannedAppsFound);
        appListView.setAdapter(adapter);
    }

    private void checkAccessibilityServiceStatus() {
        if (!isAccessibilityServiceEnabled()) {
            // Only show this if you actually need accessibility service
            // For just detecting apps, you don't need it
            if (statusTextView != null) {
                statusTextView.setText("ℹ️ Accessibility service not enabled\n(Not required for app detection)");
            }
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (accessibilityManager == null) return false;

        String service = getPackageName() + "/" + AppLockService.class.getCanonicalName();
        List<AccessibilityServiceInfo> enabledServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            if (enabledService.getId().equals(service)) {
                return true;
            }
        }
        return false;
    }

    // Optional: Method to open app settings for uninstalling
    private void openAppSettings(String packageName) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + packageName));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot open app settings", Toast.LENGTH_SHORT).show();
        }
    }
}