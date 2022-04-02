package com.jks.Spo2MonitorEx.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by apple on 16/8/20.
 */
public class VersionManager {

    /**
     * 获取系统版本号
     * @return
     */
    public static int getSystemVersion() {
        int version = Integer.parseInt(android.os.Build.VERSION.RELEASE.charAt(0) + "");
        return version;
    }

    public static String getAppVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        String versionname = "";
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionname = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionname;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }
}
