package com.jks.Spo2MonitorEx.util.entity.json;

/**
 * Created by apple on 16/9/1.
 */

import com.jks.Spo2MonitorEx.util.entity.Family;

/**
 * 添加人员以及更新人员所用到的bean
 */
public class FamilyMemBean extends BaseBean {
    //上传服务器的参数
    private int accountId;
    private String clientKey;
    private Family memInfo;

    //返回值的参数
    private String avatarDir;
    private int memberId;

    public String getAvatarDir() {
        return avatarDir;
    }

    public void setAvatarDir(String avatarDir) {
        this.avatarDir = avatarDir;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public Family getMemInfo() {
        return memInfo;
    }

    public void setMemInfo(Family memInfo) {
        this.memInfo = memInfo;
    }
}
