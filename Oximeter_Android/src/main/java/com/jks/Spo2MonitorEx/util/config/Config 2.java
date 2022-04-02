package com.jks.Spo2MonitorEx.util.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.jks.Spo2MonitorEx.util.LoadingAva;
import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.AccountIfcImpl;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;

import java.util.List;

/**
 * Created by apple on 16/7/8.
 */
public class Config extends Application {
    private Activity activity;
    private List<Family> familys;
    private List<LoginInfo> loginInfos;
    private Family recordFamily;
    private Family family;
    private boolean isFamilysUpdate;//家庭成员是否有更新
    private int position;
    private Handler mainHandler;
    private Handler mainHandler2;//MainActivity的mHandle
    //FamilyMainActivity中的handle
    private Handler familyHandler;
    private Handler manageHandler;
    private Handler firstMainHandler;
    private Handler recordMainHandler;
    private Handler DeviceBindOrNotBindHandler;
    private Handler DeviceDetailsHandler;
    private Handler HistoryChartHandler;
    private Handler ParamChartHandler;
    private String loginIp;
    private int memberId; //等同AccountId
    private String clientKey;
    //用户的邮箱名
    private String userName;
    public static final int CHANGE_FAMILY = 12;
    public static final int CHANGE_FAMILY_FAIL = 13;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Family> getFamilys() {
        if (familys == null || isFamilysUpdate) {
            FamilyIfc ifc = new FamilyIfcImpl(getApplicationContext());
            familys = ifc.findAll();
            isFamilysUpdate = false;
            Log.e("家庭成员有更新", "true");
        }
        return familys;
    }

    public void setFamilys(List<Family> familys) {
        this.familys = familys;
    }

    public List<LoginInfo> getLoginInfos() {
        AccountIfc ifc = new AccountIfcImpl(getApplicationContext());
        loginInfos = ifc.findAll();
        return loginInfos;
    }

    public void setLoginInfos(List<LoginInfo> loginInfos) {
        this.loginInfos = loginInfos;
    }

    public Family getRecordFamily() {
        return recordFamily;
    }

    public void setRecordFamily(Family recordFamily) {
        this.recordFamily = recordFamily;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public boolean isFamilysUpdate() {
        return isFamilysUpdate;
    }

    public void setIsFamilysUpdate(boolean isFamilysUpdate) {
        this.isFamilysUpdate = isFamilysUpdate;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    public void setMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public Handler getMainHandler2() {
        return mainHandler2;
    }

    public void setMainHandler2(Handler mainHandler2) {
        this.mainHandler2 = mainHandler2;
    }

    public Handler getFamilyHandler() {
        return familyHandler;
    }

    public void setFamilyHandler(Handler familyHandler) {
        this.familyHandler = familyHandler;
    }

    public Handler getManageHandler() {
        return manageHandler;
    }

    public void setManageHandler(Handler manageHandler) {
        this.manageHandler = manageHandler;
    }

    public Handler getFirstMainHandler() {
        return firstMainHandler;
    }

    public void setFirstMainHandler(Handler firstMainHandler) {
        this.firstMainHandler = firstMainHandler;
    }

    public Handler getRecordMainHandler() {
        return recordMainHandler;
    }

    public void setRecordMainHandler(Handler recordMainHandler) {
        this.recordMainHandler = recordMainHandler;
    }

    public Handler getDeviceBindOrNotBindHandler() {
        return DeviceBindOrNotBindHandler;
    }

    public void setDeviceBindOrNotBindHandler(Handler deviceBindOrNotBindHandler) {
        DeviceBindOrNotBindHandler = deviceBindOrNotBindHandler;
    }

    public Handler getDeviceDetailsHandler() {
        return DeviceDetailsHandler;
    }

    public void setDeviceDetailsHandler(Handler deviceDetailsHandler) {
        DeviceDetailsHandler = deviceDetailsHandler;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static int getChangeFamily() {
        return CHANGE_FAMILY;
    }

    public static int getVfFirstPage() {
        return VF_FIRST_PAGE;
    }

    public static void setVfFirstPage(int vfFirstPage) {
        VF_FIRST_PAGE = vfFirstPage;
    }

    public static int getVfSecondPage() {
        return VF_SECOND_PAGE;
    }

    public static void setVfSecondPage(int vfSecondPage) {
        VF_SECOND_PAGE = vfSecondPage;
    }

    public List<Integer> getIntDay() {
        return intDay;
    }

    public void setIntDay(List<Integer> intDay) {
        this.intDay = intDay;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Handler getAskDoctorGVHandler() {
        return askDoctorGVHandler;
    }

    public void setAskDoctorGVHandler(Handler askDoctorGVHandler) {
        this.askDoctorGVHandler = askDoctorGVHandler;
    }

    /**
     * 家庭数据改变, 通知FamilyMainActivity更新列表
     */
    public void changeFamily() {
        isFamilysUpdate = true;
        LoadingAva.isChange = true;//更新图片
        sendFamilyHandler(Config.CHANGE_FAMILY);
        sendRecordMainHandler(Config.CHANGE_FAMILY);
    }

    public void sendFamilyHandler(int what) {
        if (familyHandler != null) {
            familyHandler.sendEmptyMessage(what);
        }
    }

    public void sendRecordMainHandler(int what) {
        if (recordMainHandler != null) {
            recordMainHandler.sendEmptyMessage(what);
        }
    }

    /**
     * 家庭数据改变, 通知MainActivity同步网络家庭数据
     */
    public void changeFamily2() {
        isFamilysUpdate = true;
        LoadingAva.isChange = true;
        sendMainHandler2(Config.CHANGE_FAMILY);
        sendRecordMainHandler(Config.CHANGE_FAMILY);
    }

    public void sendMainHandler2(int what) {
        if (mainHandler2 != null) {
            mainHandler2.sendEmptyMessage(what);
        }
    }

    public Handler getHistoryChartHandler() {
        return HistoryChartHandler;
    }

    public void setHistoryChartHandler(Handler historyChartHandler) {
        HistoryChartHandler = historyChartHandler;
    }

    public Handler getParamChartHandler() {
        return ParamChartHandler;
    }

    public void setParamChartHandler(Handler paramChartHandler) {
        ParamChartHandler = paramChartHandler;
    }

    public static int VF_FIRST_PAGE = 0;
    public static int VF_SECOND_PAGE = 1;
    private List<Integer> intDay;
    private String oldPassword;
    private String newPassword;
    private Handler askDoctorGVHandler;

    // 清除Config数据
    public void cleanData() {
        if (familys != null) {
            familys.clear();
            familys = null;
        }
        loginIp = null;
        memberId = -1;
        clientKey = null;
        position = -1;
        familyHandler = null;
        recordMainHandler = null;
        userName = null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
