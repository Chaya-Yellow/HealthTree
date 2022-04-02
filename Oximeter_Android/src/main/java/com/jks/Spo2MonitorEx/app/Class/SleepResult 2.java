package com.jks.Spo2MonitorEx.app.Class;

/**
 * Created by badcode on 16/3/3.
 */
public class SleepResult {
    private int totalTime, sleepTime, awakeTime;
    private float sleepPercent, awakePercent;
    private short latency;
    private short wakeTimes;
    private char score;
    private char[] SW;
    private short ODI;
    private short[] OnOff;
    private int[][] ODIdata;
    private short spo2;
    private short pr;
    private int[] Onpr;

    public SleepResult() {

    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getAwakeTime() {
        return awakeTime;
    }

    public void setAwakeTime(int awakeTime) {
        this.awakeTime = awakeTime;
    }

    public float getSleepPercent() {
        return sleepPercent;
    }

    public void setSleepPercent(float sleepPercent) {
        this.sleepPercent = sleepPercent;
    }

    public float getAwakePercent() {
        return awakePercent;
    }

    public void setAwakePercent(float awakePercent) {
        this.awakePercent = awakePercent;
    }

    public short getLatency() {
        return latency;
    }

    public void setLatency(short latency) {
        this.latency = latency;
    }

    public short getWakeTimes() {
        return wakeTimes;
    }

    public void setWakeTimes(short wakeTimes) {
        this.wakeTimes = wakeTimes;
    }

    public char getScore() {
        return score;
    }

    public void setScore(char score) {
        this.score = score;
    }

    public char[] getSW() {
        return SW;
    }

    public void setSW(char[] SW) {
        this.SW = SW;
    }

    public short getODI() {
        return ODI;
    }

    public void setODI(short ODI) {
        this.ODI = ODI;
    }

    public short[] getOnOff() {
        return OnOff;
    }

    public void setOnOff(short[] onOff) {
        OnOff = onOff;
    }

    public int[][] getODIdata() {
        return ODIdata;
    }

    public void setODIdata(int[][] ODIdata) {
        this.ODIdata = ODIdata;
    }

    public short getSpo2() {
        return spo2;
    }

    public void setSpo2(short spo2) {
        this.spo2 = spo2;
    }

    public short getPr() {
        return pr;
    }

    public void setPr(short pr) {
        this.pr = pr;
    }

    public int[] getOnpr() {
        return Onpr;
    }

    public void setOnpr(int[] onpr) {
        Onpr = onpr;
    }
}
