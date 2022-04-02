package com.jks.Spo2MonitorEx.util.entity.json;

/**
 * Created by apple on 16/9/18.
 */
public class SyncDataBean extends BaseBean {
    private int accountId;
    private String clientKey;
    private String value;//血压数据数组经过压缩和base64编码后的字符串
    private float timestamp;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }
}
