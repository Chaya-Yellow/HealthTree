package com.jks.Spo2MonitorEx.util.entity;

import android.content.Context;

import com.jks.Spo2MonitorEx.R;

import java.io.Serializable;

/**
 * Created by apple on 2016/10/28.
 */
public class PublishPlatform implements Serializable {
    private String platform;
    private String url;
    private String urlen;
    private String urlzh;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl(Context context) {
        url = context.getString(R.string.language).equals("0") ? getUrlzh() : getUrlen();
        return url;
    }

//    public void setUrl(String url) {
//        this.url = url;
//    }

    public String getUrlen() {
        return urlen;
    }

    public void setUrlen(String urlen) {
        this.urlen = urlen;
    }

    public String getUrlzh() {
        return urlzh;
    }

    public void setUrlzh(String urlzh) {
        this.urlzh = urlzh;
    }
}
