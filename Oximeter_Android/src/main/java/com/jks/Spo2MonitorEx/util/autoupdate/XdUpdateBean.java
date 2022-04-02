package com.jks.Spo2MonitorEx.util.autoupdate;

import java.io.Serializable;

/**
 * Created by apple on 2016/10/26.
 */
public class XdUpdateBean implements Serializable {
    public int versionCode, size;
    public String versionName, url, note, md5;
}
