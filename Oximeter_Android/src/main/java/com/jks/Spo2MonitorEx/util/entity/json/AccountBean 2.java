package com.jks.Spo2MonitorEx.util.entity.json;

/**
 * Created by apple on 2016/9/26.
 */
public class AccountBean extends BaseBean {
    private int accountId;
    private String clientKey;

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
}
