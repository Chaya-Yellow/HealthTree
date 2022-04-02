package com.jks.Spo2MonitorEx.util.entity;

import java.io.Serializable;

/**
 * Created by apple on 16/7/16.
 */

/**
 * 用户登录账号的信息
 */
public class LoginInfo implements Serializable {
    private String avatar;//1
    private int id;
    private String account;
    private int accountId;
    private String password;
    private String clientKey = "";

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", " +
                "avatar: " + getAvatar() + ", " +
                "account: " + getAccount() + ", " +
                "password: " + getPassword() + ", " +
                "clientKey: " + getClientKey() + ", " +
                "accountId: " + getAccountId() + "\n";
    }
}
