package com.jks.Spo2MonitorEx.app.Bluetoothle;

import java.io.Serializable;

/**
 * Created by badcode on 15/10/16.
 */
public class BLEDevice implements Serializable{

    private String name;
    private String SNumber;
    private String address;
    private String MainVersion;
    private String SubVersion;
    private String type;

    public BLEDevice(String n, String sn, String add, String typ) {
        name = n;
        SNumber = sn;
        address = add;
        type = typ;
        MainVersion = "";
        SubVersion = "";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSNumber() {
        return SNumber;
    }

    public void setSNumber(String SNumber) {
        this.SNumber = SNumber;
    }

    public String getMainVersion() {
        return MainVersion;
    }

    public void setMainVersion(String mainVersion) {
        MainVersion = mainVersion;
    }

    public String getSubVersion() {
        return SubVersion;
    }

    public void setSubVersion(String subVersion) {
        SubVersion = subVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
