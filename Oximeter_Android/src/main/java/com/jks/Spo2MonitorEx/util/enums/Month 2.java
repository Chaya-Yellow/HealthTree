package com.jks.Spo2MonitorEx.util.enums;

/**
 * Created by apple on 16/9/5.
 */

/**
 * 月的枚举
 */
public enum Month {
    one("January"),two("February"),three("March"),four("April"),five("May"),
    six("June"),seven(" July"),eight("August"), night("September"), ten("October"), eleven("November"), twelve("December");

    public String mNum;

    Month(String num){
        this.mNum = num;
    }
}
