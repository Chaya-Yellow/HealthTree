package com.jks.Spo2MonitorEx.util.dbhelper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by apple on 16/7/17.
 */
public class SoftParam {
    /** 获取软件版本号 */
    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageInfo packInfo = getPackageInfo(context);
        int versionCode = packInfo.versionCode;
        return versionCode;
    }

    /** 获取软件版本号 */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageInfo packInfo = getPackageInfo(context);
        String versionName = packInfo.versionName;
        return versionName;
    }
    /**返回一个版本号与版本名的字符串作为升级的标识*/
    public static String getStrByVersionCode_VersionName(Context context){
        return getVersionCode(context)+"_"+getVersionName(context);
    }


    /** 获取软件详细信息 */
    private static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (Exception e) {
            //Log.e("test", "getVersionName_e.getMessage():" + e.getMessage());
        }
        return packInfo;
    }
    /**获取Application中定义的metaData的值*/
    public static String getApplicationInfo_ToMetaData(Context context,String keyWord){
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = applicationInfo.metaData;
            String value =  String.valueOf(metaData.get(keyWord));
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            //Log.e("test","getApplicationInfo_ToMetaData_e.getMessage():"+e.getMessage());
            return "";
        }
    }
}
