package com.example.suhasapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.accessibilityservice.AccessibilityServiceInfo;
//import android.view.accessibility.AccessibilityManager; // FIXED: Correct import
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ListView appListView;
//    private Button scanButton;
//    private TextView statusTextView;
//
//    // Comprehensive list of banned Chinese apps
//    private static final String[] BANNED_APPS = {
//            // Social Media & Video
//            "com.zhiliaoapp.musically",      // TikTok
//            "com.ss.android.ugc.trill",      // TikTok India
//            "com.kwai.video",                // Snack Video (Kwai)
//            "video.like",                    // Likee
//            "com.sina.weibo",                // Weibo
//            "com.tencent.mm",                // WeChat
//            "com.immomo.momo",               // Momo
//            "com.yy.hiyo",                   // BIGO LIVE
//            "live.like",                     // Like
//
//            // File Sharing & Browsers
//            "com.lenovo.anyshare.gps",       // SHAREit
//            "com.uc.browser.en",             // UC Browser
//            "com.UCMobile.intl",             // UC Browser International
//            "com.mi.globalbrowser",          // MI Browser
//            "com.opera.mini.native",         // Opera Mini (Chinese owned)
//
//            // Utilities & Tools
//            "com.intsig.camscanner",         // CamScanner
//            "com.commsource.beautyplus",     // Beauty Plus
//            "com.wondershare.fotophire",     // Fotophire
//            "com.meitu.meiyancamera",        // Meitu
//            "com.benqu.wuta",                // Wuta Camera
//            "com.mt.mtxx.mtxx",              // MTXX
//            "com.ss.android.article.news",   // TopBuzz
//            "com.newsdog",                   // NewsDog
//
//            // Games
//            "com.tencent.ig",                // PUBG Mobile
//            "com.tencent.tmgp.pubgmhd",      // PUBG Mobile HD
//            "com.elex.kok",                  // Clash of Kings
//            "com.miniclip.plagueinc",        // Plague Inc (partially Chinese owned)
//            "com.tencent.mm",                // Mobile Legends (partially)
//
//            // Shopping & Business
//            "com.alibaba.aliexpresshd",      // AliExpress
//            "com.shein.android",             // SHEIN
//            "com.taobao.taobao",             // Taobao
//            "com.alibaba.android.rimet",     // DingTalk
//            "com.tencent.wework",            // WeWork
//
//            // Others
//            "com.duokan.phone.remotecontroller", // Mi Remote Controller
//            "com.mi.android.globalFileexplorer", // Mi File Manager
//            "com.xiaomi.scanner",            // Mi Scanner
//            "com.miui.gallery",              // Mi Gallery
//            "com.cleanmaster.mguard",        // Clean Master
//            "com.kika.keyboard"              // Kika Keyboard
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        initViews();
//        setupClickListeners();
//
//        // Optional: Check accessibility service status
//        checkAccessibilityServiceStatus();
//    }
//
//    private void initViews() {
//        appListView = findViewById(R.id.app_list_view);
//        scanButton = findViewById(R.id.scan_button);
//        statusTextView = findViewById(R.id.status_text_view); // Add this to your layout
//    }
//
//    private void setupClickListeners() {
//        scanButton.setOnClickListener(v -> scanForBannedApps());
//    }
//
//    private void scanForBannedApps() {
//        scanButton.setEnabled(false);
//        scanButton.setText("Scanning...");
//
//        List<String> bannedAppsFound = new ArrayList<>();
//        List<String> installedBannedPackages = new ArrayList<>();
//        PackageManager pm = getPackageManager();
//
//        // Method 1: Use queries approach (more efficient and privacy-friendly)
//        for (String packageName : BANNED_APPS) {
//            try {
//                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
//                if (packageInfo != null) {
//                    ApplicationInfo appInfo = packageInfo.applicationInfo;
//                    String appName = appInfo.loadLabel(pm).toString();
//                    bannedAppsFound.add("⚠️ " + appName + "\n   (" + packageName + ")");
//                    installedBannedPackages.add(packageName);
//                }
//            } catch (PackageManager.NameNotFoundException e) {
//                // App not installed - this is good!
//            }
//        }
//
//        displayResults(bannedAppsFound, installedBannedPackages);
//
//        scanButton.setEnabled(true);
//        scanButton.setText("Scan for Banned Apps");
//    }
//
//    private void displayResults(List<String> bannedAppsFound, List<String> packageNames) {
//        String statusMessage;
//
//        if (bannedAppsFound.isEmpty()) {
//            statusMessage = "✅ Device Clean!\nNo banned Chinese apps detected.";
//            bannedAppsFound.add("✅ No banned Chinese apps found");
//            bannedAppsFound.add("Your device is secure!");
//            Toast.makeText(this, "Device is clean - no banned apps found!", Toast.LENGTH_LONG).show();
//        } else {
//            statusMessage = "⚠️ Warning!\n" + bannedAppsFound.size() + " banned Chinese app(s) detected.";
//            bannedAppsFound.add(0, "📋 SCAN RESULTS:");
//            bannedAppsFound.add(1, "Found " + (bannedAppsFound.size() - 2) + " banned app(s)");
//            bannedAppsFound.add(2, ""); // Empty line for spacing
//
//            Toast.makeText(this, "Found " + packageNames.size() + " banned apps! Check the list below.",
//                    Toast.LENGTH_LONG).show();
//        }
//
//        if (statusTextView != null) {
//            statusTextView.setText(statusMessage);
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, bannedAppsFound);
//        appListView.setAdapter(adapter);
//    }
//
//    private void checkAccessibilityServiceStatus() {
//        if (!isAccessibilityServiceEnabled()) {
//            // Only show this if you actually need accessibility service
//            // For just detecting apps, you don't need it
//            if (statusTextView != null) {
//                statusTextView.setText("ℹ️ Accessibility service not enabled\n(Not required for app detection)");
//            }
//        }
//    }
//
//    private boolean isAccessibilityServiceEnabled() {
//        AccessibilityManager accessibilityManager =
//                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
//
//        if (accessibilityManager == null) return false;
//
//        String service = getPackageName() + "/" + AppLockService.class.getCanonicalName();
//        List<AccessibilityServiceInfo> enabledServices =
//                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
//
//        for (AccessibilityServiceInfo enabledService : enabledServices) {
//            if (enabledService.getId().equals(service)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Optional: Method to open app settings for uninstalling
//    private void openAppSettings(String packageName) {
//        try {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(android.net.Uri.parse("package:" + packageName));
//            startActivity(intent);
//        } catch (Exception e) {
//            Toast.makeText(this, "Cannot open app settings", Toast.LENGTH_SHORT).show();
//        }
//    }
//}

import androidx.appcompat.app.AppCompatActivity;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
    private Button scanButton, testButton, detailsButton;
    private TextView statusTextView;
    private boolean testMode = false;
    private boolean showDetails = false;
    private List<String> lastFoundApps = new ArrayList<>();
    private List<String> lastFoundPackages = new ArrayList<>();

    // Real banned Chinese apps
    private static final String[] BANNED_APPS = {
            "com.zhiliaoapp.musically",      // TikTok
            "com.ss.android.ugc.trill",      // TikTok India
            "com.kwai.video",                // Snack Video
            "video.like",                    // Likee
            "com.lenovo.anyshare.gps",       // SHAREit
            "com.uc.browser.en",             // UC Browser
            "com.UCMobile.intl",             // UC Browser International
            "com.intsig.camscanner",         // CamScanner
            "com.commsource.beautyplus",     // Beauty Plus
            "com.tencent.ig",                // PUBG Mobile
            "com.elex.kok",                  // Clash of Kings
            "com.tencent.mm",                // WeChat
            "com.sina.weibo",                // Weibo
            "com.alibaba.aliexpresshd",      // AliExpress
            "com.shein.android"              // SHEIN
    };

    // Test apps (common apps for demonstration)
    private static final String[] TEST_APPS = {
            "com.whatsapp",                  // WhatsApp (for demo)
            "com.instagram.android",         // Instagram (for demo)
            "com.google.android.youtube",    // YouTube (usually installed)
            "com.android.chrome",            // Chrome (usually installed)
            "com.spotify.music",             // Spotify (if installed)
            "com.fake.nonexistent.app1",     // Non-existent app
            "com.fake.nonexistent.app2"      // Non-existent app
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
        showTestingInstructions();
    }

    private void initViews() {
        appListView = findViewById(R.id.app_list_view);
        scanButton = findViewById(R.id.scan_button);
        testButton = findViewById(R.id.test_button); // Add this to your layout
        detailsButton = findViewById(R.id.details_button); // Add this to your layout
        statusTextView = findViewById(R.id.status_text_view);
    }

    private void setupClickListeners() {
        scanButton.setOnClickListener(v -> {
            if (testMode) {
                runTestScan();
            } else {
                scanForBannedApps();
            }
        });

        testButton.setOnClickListener(v -> toggleTestMode());

        detailsButton.setOnClickListener(v -> toggleDetailsView());

        // Long press on list items to show file paths
        appListView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position < lastFoundPackages.size()) {
                showAppDetails(lastFoundPackages.get(position));
            }
            return true;
        });
    }

    private void toggleTestMode() {
        testMode = !testMode;
        if (testMode) {
            testButton.setText("Switch to Real Mode");
            scanButton.setText("Run Test Scan");
            statusTextView.setText("🧪 TEST MODE ACTIVE\nUsing demo apps for testing");
            Toast.makeText(this, "Test mode enabled - using safe demo apps", Toast.LENGTH_LONG).show();
        } else {
            testButton.setText("Switch to Test Mode");
            scanButton.setText("Scan for Banned Apps");
            statusTextView.setText("📱 REAL MODE ACTIVE\nScanning for actual banned Chinese apps");
            Toast.makeText(this, "Real mode enabled - scanning actual banned apps", Toast.LENGTH_LONG).show();
        }
    }

    private void runTestScan() {
        Log.d("TestMode", "Running test scan with demo apps");
        scanButton.setEnabled(false);
        scanButton.setText("Testing...");

        List<String> foundApps = new ArrayList<>();
        List<String> installedPackages = new ArrayList<>();
        PackageManager pm = getPackageManager();

        // Test with demo apps
        for (String packageName : TEST_APPS) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                if (packageInfo != null) {
                    ApplicationInfo appInfo = packageInfo.applicationInfo;
                    String appName = appInfo.loadLabel(pm).toString();
                    String apkPath = appInfo.sourceDir;
                    String dataPath = appInfo.dataDir;

                    // Format app info with paths
                    StringBuilder appDetails = new StringBuilder();
                    appDetails.append("🧪 ").append(appName).append("\n");
                    appDetails.append("   Package: ").append(packageName).append("\n");
                    appDetails.append("   APK Path: ").append(apkPath).append("\n");
                    appDetails.append("   Data Path: ").append(dataPath);

                    foundApps.add(appDetails.toString());
                    installedPackages.add(packageName);
                    Log.d("TestMode", "Found test app: " + appName + " at " + apkPath);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("TestMode", "Test app not found: " + packageName);
            }
        }

        displayTestResults(foundApps, installedPackages);

        // Store for details view
        lastFoundApps = new ArrayList<>(foundApps);
        lastFoundPackages = new ArrayList<>(installedPackages);

        scanButton.setEnabled(true);
        scanButton.setText("Run Test Scan");
    }

    private void scanForBannedApps() {
        Log.d("RealMode", "Running real scan for banned apps");
        scanButton.setEnabled(false);
        scanButton.setText("Scanning...");

        List<String> bannedAppsFound = new ArrayList<>();
        List<String> installedBannedPackages = new ArrayList<>();
        PackageManager pm = getPackageManager();

        for (String packageName : BANNED_APPS) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                if (packageInfo != null) {
                    ApplicationInfo appInfo = packageInfo.applicationInfo;
                    String appName = appInfo.loadLabel(pm).toString();
                    String apkPath = appInfo.sourceDir;
                    String dataPath = appInfo.dataDir;
                    String installTime = getInstallTime(packageInfo);
                    String appSize = getAppSize(apkPath);

                    // Format detailed app info
                    StringBuilder appDetails = new StringBuilder();
                    appDetails.append("⚠️ ").append(appName).append("\n");
                    appDetails.append("   Package: ").append(packageName).append("\n");
                    appDetails.append("   APK Path: ").append(apkPath).append("\n");
                    appDetails.append("   Data Path: ").append(dataPath).append("\n");
                    appDetails.append("   Installed: ").append(installTime).append("\n");
                    appDetails.append("   Size: ").append(appSize);

                    bannedAppsFound.add(appDetails.toString());
                    installedBannedPackages.add(packageName);
                    Log.d("RealMode", "Found banned app: " + appName + " at " + apkPath);
                }
            } catch (PackageManager.NameNotFoundException e) {
                // App not installed - this is good!
            }
        }

        displayRealResults(bannedAppsFound, installedBannedPackages);

        // Store for details view
        lastFoundApps = new ArrayList<>(bannedAppsFound);
        lastFoundPackages = new ArrayList<>(installedBannedPackages);

        scanButton.setEnabled(true);
        scanButton.setText("Scan for Banned Apps");
    }

    private void displayTestResults(List<String> foundApps, List<String> packageNames) {
        String statusMessage;

        if (foundApps.isEmpty()) {
            statusMessage = "🧪 TEST COMPLETE\nNo demo apps found on device\n(This simulates a clean device)";
            foundApps.add("🧪 TEST RESULT: Clean Device Simulation");
            foundApps.add("No demo apps detected");
            foundApps.add("");
            foundApps.add("💡 In real mode, this would mean:");
            foundApps.add("✅ No banned Chinese apps found");
            foundApps.add("✅ Device is secure");
        } else {
            statusMessage = "🧪 TEST COMPLETE\n" + foundApps.size() + " demo app(s) detected\n(Simulating banned app detection)";
            foundApps.add(0, "🧪 TEST RESULTS:");
            foundApps.add(1, "Found " + packageNames.size() + " demo app(s)");
            foundApps.add(2, "");
            foundApps.add("💡 In real mode, these would be:");
            foundApps.add("⚠️ Banned Chinese apps requiring attention");
        }

        statusTextView.setText(statusMessage);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, foundApps);
        appListView.setAdapter(adapter);

        Toast.makeText(this, "Test completed! Found " + packageNames.size() + " demo apps",
                Toast.LENGTH_LONG).show();
    }

    private void displayRealResults(List<String> bannedAppsFound, List<String> packageNames) {
        String statusMessage;

        if (bannedAppsFound.isEmpty()) {
            statusMessage = "✅ DEVICE SECURE!\nNo banned Chinese apps detected";
            bannedAppsFound.add("✅ Device Clean!");
            bannedAppsFound.add("No banned Chinese apps found");
            bannedAppsFound.add("");
            bannedAppsFound.add("🛡️ Your device is secure");
            bannedAppsFound.add("📱 No security concerns detected");
            Toast.makeText(this, "Device is clean - no banned apps found!", Toast.LENGTH_LONG).show();
        } else {
            statusMessage = "⚠️ SECURITY ALERT!\n" + bannedAppsFound.size() + " banned Chinese app(s) detected";
            bannedAppsFound.add(0, "⚠️ SECURITY SCAN RESULTS:");
            bannedAppsFound.add(1, "Found " + packageNames.size() + " banned app(s)");
            bannedAppsFound.add(2, "");
            bannedAppsFound.add("🚨 These apps may pose security risks:");

            Toast.makeText(this, "ALERT: Found " + packageNames.size() + " banned apps!",
                    Toast.LENGTH_LONG).show();
        }

        statusTextView.setText(statusMessage);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, bannedAppsFound);
        appListView.setAdapter(adapter);
    }

    private void showTestingInstructions() {
        List<String> instructions = new ArrayList<>();
        instructions.add("📋 BANNED APP DETECTOR");
        instructions.add("");
        instructions.add("🔍 How to test this app:");
        instructions.add("");
        instructions.add("1️⃣ REAL MODE:");
        instructions.add("   • Scans for actual banned Chinese apps");
        instructions.add("   • Shows security status of your device");
        instructions.add("");
        instructions.add("2️⃣ TEST MODE:");
        instructions.add("   • Uses safe demo apps for testing");
        instructions.add("   • Simulates banned app detection");
        instructions.add("   • Safe for school demonstration");
        instructions.add("");
        instructions.add("👆 Tap 'Switch to Test Mode' to demo");
        instructions.add("👆 Then tap 'Scan' to see results");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, instructions);
        appListView.setAdapter(adapter);

        statusTextView.setText("📱 Ready to scan\nChoose Real Mode or Test Mode");
    }

    // Utility method for performance testing
    private void logScanPerformance(long startTime, int appsScanned, int appsFound) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Log.d("Performance", String.format(
                "Scan completed: %dms, %d apps checked, %d found",
                duration, appsScanned, appsFound
        ));

        // Show performance info in test mode
        if (testMode) {
            Toast.makeText(this,
                    String.format("Performance: %dms (%d apps checked)", duration, appsScanned),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Optional: Accessibility service check (if needed)
    private boolean isAccessibilityServiceEnabled() {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (accessibilityManager == null) return false;

        try {
            String service = getPackageName() + "/" + AppLockService.class.getCanonicalName();
            List<AccessibilityServiceInfo> enabledServices =
                    accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

            for (AccessibilityServiceInfo enabledService : enabledServices) {
                if (enabledService.getId().equals(service)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("AccessibilityCheck", "Error checking accessibility service", e);
        }
        return false;
    }

    // Helper method to get install time
    private String getInstallTime(PackageInfo packageInfo) {
        try {
            long installTime = packageInfo.firstInstallTime;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm");
            return sdf.format(new java.util.Date(installTime));
        } catch (Exception e) {
            return "Unknown";
        }
    }

    // Helper method to get app size
    private String getAppSize(String apkPath) {
        try {
            java.io.File apkFile = new java.io.File(apkPath);
            if (apkFile.exists()) {
                long sizeBytes = apkFile.length();
                return formatFileSize(sizeBytes);
            }
        } catch (Exception e) {
            Log.e("AppSize", "Error getting app size", e);
        }
        return "Unknown";
    }

    // Helper method to format file size
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        else return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }

    // Method to get additional app security info
    private String getSecurityInfo(ApplicationInfo appInfo) {
        StringBuilder securityInfo = new StringBuilder();

        // Check if app has system permissions
        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            securityInfo.append("   🔒 System App\n");
        } else {
            securityInfo.append("   📱 User App\n");
        }

        // Check if app can be debugged
        if ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            securityInfo.append("   🐛 Debuggable\n");
        }

        // Check if app is external
        if ((appInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
            securityInfo.append("   💾 External Storage\n");
        }

        // Target SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            securityInfo.append("   🎯 Target SDK: ").append(appInfo.targetSdkVersion);
        }

        return securityInfo.toString();
    }

    // Toggle between simple and detailed view
    private void toggleDetailsView() {
        showDetails = !showDetails;
        if (showDetails) {
            detailsButton.setText("Hide Details");
            Toast.makeText(this, "Detailed view enabled - showing file paths", Toast.LENGTH_SHORT).show();
        } else {
            detailsButton.setText("Show Details");
            Toast.makeText(this, "Simple view enabled", Toast.LENGTH_SHORT).show();
        }

        // Refresh current display
        if (!lastFoundApps.isEmpty()) {
            if (testMode) {
                displayTestResults(lastFoundApps, lastFoundPackages);
            } else {
                displayRealResults(lastFoundApps, lastFoundPackages);
            }
        }
    }

    // Show detailed app information in a popup
    private void showAppDetails(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            ApplicationInfo appInfo = packageInfo.applicationInfo;

            StringBuilder details = new StringBuilder();
            details.append("📱 DETAILED APP INFO\n\n");
            details.append("App Name: ").append(appInfo.loadLabel(pm)).append("\n");
            details.append("Package: ").append(packageName).append("\n\n");

            details.append("📁 FILE LOCATIONS:\n");
            details.append("APK Path: ").append(appInfo.sourceDir).append("\n");
            details.append("Data Dir: ").append(appInfo.dataDir).append("\n");
            if (appInfo.nativeLibraryDir != null) {
                details.append("Native Libs: ").append(appInfo.nativeLibraryDir).append("\n");
            }
            details.append("\n");

            details.append("📊 APP INFO:\n");
            details.append("Version: ").append(packageInfo.versionName != null ? packageInfo.versionName : "Unknown").append("\n");
            details.append("Install Time: ").append(getInstallTime(packageInfo)).append("\n");
            details.append("Size: ").append(getAppSize(appInfo.sourceDir)).append("\n");
            details.append("UID: ").append(appInfo.uid).append("\n");
            details.append("\n");

            details.append("🔒 SECURITY INFO:\n");
            details.append(getSecurityInfo(appInfo));

            // Show in a simple alert dialog
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("App Details")
                    .setMessage(details.toString())
                    .setPositiveButton("OK", null)
                    .setNeutralButton("Open Settings", (dialog, which) -> openAppSettings(packageName))
                    .show();

        } catch (Exception e) {
            Toast.makeText(this, "Error getting app details", Toast.LENGTH_SHORT).show();
            Log.e("AppDetails", "Error showing app details", e);
        }
    }

    // Open app settings for uninstalling
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