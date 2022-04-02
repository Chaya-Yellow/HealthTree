package com.jks.Spo2MonitorEx.util.entity.json;

/**
 * Created by apple on 2016/9/26.
 */
public class ChangePwdBean extends AccountBean {
    private String oldPwd;
    private String newPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
