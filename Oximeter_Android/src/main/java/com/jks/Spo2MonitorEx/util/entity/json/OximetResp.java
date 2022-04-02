package com.jks.Spo2MonitorEx.util.entity.json;

/**
 * Created by apple on 16/9/2.
 */
public class OximetResp {
    private String value;
    private float timestamp;

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
