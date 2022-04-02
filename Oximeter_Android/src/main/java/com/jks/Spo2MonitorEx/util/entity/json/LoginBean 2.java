package com.jks.Spo2MonitorEx.util.entity.json;

import com.jks.Spo2MonitorEx.util.constant.Constant;

/**
 * Created by apple on 16/8/25.
 */
public class LoginBean extends BaseBean {
    private String account;
    private String pwd;
    private String loginIp = "";
    private String appId = Constant.AppId + "";
    private String language = "101";
    //服务器返回
    private int accountId = 0;
    private String clientKey = "";

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
