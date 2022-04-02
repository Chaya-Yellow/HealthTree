package com.jks.Spo2MonitorEx.util.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.jks.Spo2MonitorEx.app.constants.AppConstants.Key.LAST_APP_VERSION_CODE;

/**
 * Created by apple on 16/8/25.
 */
public class SharedPreferencesUtil {
    /** 项目名 */
    public final static String PROJECTNAME = "Oximeter";
    /** 用户偏好 */
    public final static String[] SETTING_KEY = { "weight", "height", "engry" };

    public final static String SETTINGS_VIBRATE_ENABLED = "SETTINGS_VIBRATE_ENABLED";

    /** 获取当前项目的偏好设置 */
    public static SharedPreferences getProjectSP(Context context) {
        return context.getSharedPreferences(PROJECTNAME, context.MODE_PRIVATE);
    }

    /**
     * 获取偏好INT
     * @param mContext
     * @param key
     * @return
     */
    public static int getIntByKey(Context mContext, String key) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt(key, 0);
    }

    /**
     * 设置偏好INT
     * @param mContext
     * @param key
     * @param value
     * @return
     */
    public static boolean setIntByKey(Context mContext, String key, int value) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putInt(key, value).commit();
    }

    /**
     * 获取偏好STRING
     * @param mContext
     * @param key
     * @return
     */
    public static String getStringByKey(Context mContext, String key) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString(key, null);
    }

    /**
     * 获取偏好STRING
     * @param mContext
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringByKey(Context mContext, String key, String defValue) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString(key, defValue);
    }

    /**
     * 设置偏好STRING
     * @param mContext
     * @param key
     * @param value
     * @return
     */
    public static boolean setStringByKey(Context mContext, String key, String value) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString(key, value).commit();
    }

    /**
     * 清空登录的信息
     * @param context
     */
    public static void clearSp(Context context) {
        // 清空用户个人信息密码等
        SharedPreferences sp = getProjectSP(context);
        sp.edit().putInt("memberId", -1).commit();
        sp.edit().putString("userName", null).commit();
        sp.edit().putString("clientKey", null).commit();
        sp.edit().putString("passWord", null).commit();
        // 清空选择用户
        sp.edit().putInt("family_set_id", -1).commit();
        sp.edit().putString("email", "").commit();
        sp.edit().putString("phoneNumber", "").commit();
        // 清空设置
        // SharedPreferences spSet = getProjectSP(context);
        // sp.edit().putBoolean(SETTINGS_VIBRATE_ENABLED, true).commit();
        // sp.edit().putBoolean("isBtn_sound", true).commit();
        // sp.edit().putString("temperature", "").commit();
        // sp.edit().putString("tpt_unit", "℃").commit();

    }

    /**
     * 获取用户偏好
     * @param mContext
     * @param key
     * @return
     */
    public static int getUserSetting(Context mContext, String key) {
        SharedPreferences userSetting = mContext.getSharedPreferences("user_setting", 0);
        return userSetting.getInt(key, 0);
    }

    /**
     * 设置用户偏好
     * @param mContext
     * @param key
     * @param value
     * @return
     */
    public static boolean setUserSetting(Context mContext, String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences("user_setting", 0);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putInt(key, value).commit();
    }

    /**
     * 获取用户登录邮箱
     * @param mContext
     * @return
     */
    public static String getUserName(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString("userName", "0");
       // return sp.getString("userName", "xxx");
    }

    /**
     * 设置用户登录邮箱
     *
     * @param mContext
     * @param userName
     */
    public static boolean setUserName(Context mContext, String userName) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("userName", userName).commit();
    }

    /**
     * 获取用户ID 等同 AccountId
     *
     * @param context
     * @return
     */
    public static int getMemberId(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getInt("memberId", -1);
//        return sp.getInt("memberId", 0);
    }

    /**
     * 设置用户ID 等同 AccountId
     * @param context
     * @param menberId
     * @return
     */
    public static boolean setMemberId(Context context, int menberId) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putInt("memberId", menberId).commit();
    }

    /**
     * 设置用户钥匙
     *
     * @param context
     * @param clientKey
     */
    public static boolean setCilenKey(Context context, String clientKey) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("clientKey", clientKey).commit();
    }

    /**
     * 获取用户钥匙
     *
     * @param context
     * @return
     */
    public static String getCilenKey(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getString("clientKey", null);
        //return sp.getString("clientKey", "xxx");
    }

    /**
     * 获取用户登录密码
     *
     * @param mContext
     * @return
     */
    public static String getPassWord(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString("passWord", "0");

    }

    /**
     * 设置用户登录密码
     *
     * @param mContext
     * @param passWord
     */
    public static boolean setPassWord(Context mContext, String passWord) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("passWord", passWord).commit();
    }

    /**
     * 获取单位偏好
     * @param mContext
     * @return
     */
    public static int getUnit(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt("unit", 0);
    }

    /**
     *
     * @param mContext
     * @param value
     * @return
     */
    public static boolean setUnit(Context mContext, int value) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putInt("unit", value).commit();
    }

    /**
     * 获取高血糖警戒值,默认为
     * @param mContext
     * @return
     */
    public static float getBGHigh(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getFloat("bg_high", 5.0f);
    }

    /**
     * 设置高血糖警戒值
     *
     * @param mContext
     * @param value
     */
    public static boolean setBGHigh(Context mContext, float value) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putFloat("bg_high", value).commit();
    }

    /**
     * 获取低血糖警戒值,默认为
     * @param mContext
     * @return
     */
    public static float getBGLow(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getFloat("bg_low", 5.0f);
    }

    /**
     * 设置低血糖警戒值
     *
     * @param mContext
     * @param value
     */
    public static boolean setBGLow(Context mContext, float value) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putFloat("bg_low", value).commit();
    }

    /**
     * 获取是否开启震动
     *
     * @param mContext
     * @return
     */
    public static boolean isVibrate(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        return sp.getBoolean(SETTINGS_VIBRATE_ENABLED, true);
    }

    /**
     * 设置是否开启震动
     *
     * @param mContext
     * @param bool
     */
    public static void setVibrate(Context mContext, boolean bool) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SETTINGS_VIBRATE_ENABLED, bool).commit();

    }

    /**
     * 设置声音
     *
     * @param mContext
     * @return
     */
    public static void setBtn_sound(Context mContext, boolean bool) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isBtn_sound", bool).commit();
    }

    /**
     * 获取声音
     *
     * @param mContext
     * @return
     */
    public static boolean getBtn_sound(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getBoolean("isBtn_sound", true);
    }

    /**
     * 设置报警温度
     *
     * @param mContext
     * @return
     */
    public static void setTpt(Context mContext, String str) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("temperature", str);
        editor.commit();

    }

    /**
     * 获取报警温度
     *
     * @param mContext
     * @return
     */
    public static String getTpt(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString("temperature", "38.5").trim();

    }

    /**
     * 设置蓝牙断开报警
     *
     * @param mContext
     * @param bool
     */
    public static void setBtAlarm(Context mContext, boolean bool) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isBtAlarm", bool).commit();
    }

    /**
     * 获取蓝牙断开报警
     *
     * @param mContext
     * @return
     */
    public static boolean getBtAlarm(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getBoolean("isBtAlarm", false);
    }

    /**
     * 设置温度单位
     *
     * @param mContext
     * @return
     */
    public static void setUTpt(Context mContext, String str) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("tpt_unit", str).commit();

    }

    /**
     * 获取温度单位
     *
     * @param mContext
     * @return
     */
    public static String getUTpt(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString("tpt_unit", "℃");

    }

    /**
     * 设置family位置 0开始
     *
     * @param mContext
     * @return
     */
    public static void setFamilyPosition(Context mContext, int position) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("family_set_id", position).commit();

    }

    /**
     * 获取family位置 0开始
     *
     * @param mContext
     * @return
     */
    public static int getFamilyPosition(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt("family_set_id", -1);

    }

    /**
     * 设置账号位置 0开始
     *
     * @param mContext
     * @return
     */
    public static void setAccountPosition(Context mContext, int position) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("account_set_id", position).commit();

    }

    /**
     * 获取账号位置 0开始
     *
     * @param mContext
     * @return
     */
    public static int getAccountPosition(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt("account_set_id", -1);

    }

    /**
     * 设置有效邮件地址
     *
     * @param mContext
     * @return
     */
    public static void setValidEmailAddress(Context mContext, String email) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email).commit();

    }

    /**
     * 获取有效邮件地址
     *
     * @param mContext
     * @return
     */
    public static String getValidEmailAddress(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString("email", "");

    }

    /**
     * 设置有效手机号码
     *
     * @param mContext
     * @return
     */
    public static void setValidPhoneNumber(Context mContext, String number) {
        SharedPreferences sp = mContext.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("phoneNumber", number).commit();

    }

    /**
     * 获取有效手机号码
     *
     * @param mContext
     * @return
     */
    public static String getValidPhoneNumber(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getString("phoneNumber", "");
    }

    public static boolean isLoginIn(Context mContext) {
        String userName = getUserName(mContext);
        String clientKey = getCilenKey(mContext);
        int memberId = getMemberId(mContext);

        if (memberId != -1)
            Log.d("test", "memberId::" + memberId);
        if (userName != null && clientKey != null && memberId != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 本地 时间戳的存储
     */
    public static boolean setLocalTime(Context context, String time) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("local_time", time).commit();
    }

    /**
     * 得到本地时间戳
     */
    public static String getLocalTime(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getString("local_time", "1980-01-01 00:00:00");
    }

    /**
     * 本地 时间戳的存储
     */
    public static boolean setEventLocalTime(Context context, String time) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("local_event_time", time).commit();
    }

    /**
     * 得到本地时间戳
     */
    public static String getEventLocalTime(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getString("local_event_time", "1980-01-01 00:00:00");
    }

    /**
     * 设置服务器的时间戳
     */
    public static boolean setTime(Context context, String time) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("time", time).commit();
    }

    /**
     * 得到服务器时间戳
     */
    public static String getTime(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getString("time", "0");
    }

    /**
     * 设置服务器的时间戳
     */
    public static boolean setEvenTime(Context context, String time) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        return editor.putString("EvenTime", time).commit();
    }

    /**
     * 得到服务器时间戳
     */
    public static String getEvenTime(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getString("EvenTime", "0");
    }

    /**
     * 设置水温测试开关
     *
     * @param mContext
     * @return
     */
    public static void setTemperature_test(Context mContext, boolean bool) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("Temperature_test", bool).commit();
    }

    /**
     * 获取水温测试开关
     *
     * @param mContext
     * @return
     */
    public static boolean getTemperature_test(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getBoolean("Temperature_test", false);
    }

    /**
     * 设置宝莱特产品开关 0完全没连接过。1，宝莱特 2，鱼跃 3,Quest
     *
     * @param mContext
     * @return
     */
    public static void setBiolight(Context mContext, int bool) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Biolight", bool).commit();
    }

    /**
     * 获取宝莱特产品开关 0完全没连接过。1，宝莱特 2，鱼跃 3,Quest
     *
     * @param mContext
     * @return
     */
    public static int getBiolight(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt("Biolight", 0);
    }

    /**
     * 设置身高单位1，cm（厘米） 2，in（英寸）
     *
     * @param mContext
     * @return
     */
    public static void setHeightUnit(Context mContext, int height_unit) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("height_unit", height_unit).commit();
    }

    /**
     * 获取身高单位1，cm（厘米） 2，in（英寸）
     *
     * @param mContext
     * @return
     */
    public static int getHeightUnit(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt("height_unit", 1);
    }

    /**
     * 设置体重单位1，kg（公斤） 2，lb（磅）
     *
     * @param mContext
     * @return
     */
    public static void setWeightUnit(Context mContext, int weight_unit) {
        SharedPreferences sp = getProjectSP(mContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("weight_unit", weight_unit).commit();
    }

    /**
     * 获取体重单位1，kg（公斤） 2，lb（磅）
     *
     * @param mContext
     * @return
     */
    public static int getWeightUnit(Context mContext) {
        SharedPreferences sp = getProjectSP(mContext);
        return sp.getInt("weight_unit", 1);
    }

    /**
     * 设置上次的versionCode
     * @param context
     * @param versionCode
     */
    public static void setLastAppVersionCode(Context context, int versionCode) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(LAST_APP_VERSION_CODE, versionCode).commit();
    }

    /**
     * 上次的versionCode
     * @param context
     * @return
     */
    public static int getLastAppVersionCode(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getInt(LAST_APP_VERSION_CODE, 0);
    }

    /**
     * 是否是首次打开APP
     * @param context
     * @param is_first_open
     */
    public static void setFirstOpen(Context context, boolean is_first_open) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("is_first_open", is_first_open).commit();
    }

    /**
     * 是否是首次打开APP
     * @param context
     * @return
     */
    public static boolean getFirstOpen(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getBoolean("is_first_open", true);
    }

    public static void setAlarmSound(Context context, int i) {
        SharedPreferences sp = getProjectSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("alarm_sound", i).commit();
    }

    public static int getAlarmSound(Context context) {
        SharedPreferences sp = getProjectSP(context);
        return sp.getInt("alarm_sound", 1);
    }
}
