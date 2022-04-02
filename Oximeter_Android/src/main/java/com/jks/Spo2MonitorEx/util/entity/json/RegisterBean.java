package com.jks.Spo2MonitorEx.util.entity.json;

import com.jks.Spo2MonitorEx.util.constant.Constant;

/**
 * Created by apple on 16/8/24.
 */
public class RegisterBean extends BaseBean {
    private String type = "303";
    private String appId = Constant.AppId + "";
    private String email;
    private String pwd;
    private String language = Constant.LANGUAGE_NET_EN;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
