package com.jks.Spo2MonitorEx.util.entity;

import java.util.List;

/**
 * Created by apple on 16/9/7.
 */
public class OximeterAndPoint {
    private List<Oximet> oximets;

    private List<Integer> nextBreakPointList;

    public List<Oximet> getOximets() {
        return oximets;
    }

    public void setOximets(List<Oximet> oximets) {
        this.oximets = oximets;
    }

    public List<Integer> getNextBreakPointList() {
        return nextBreakPointList;
    }

    public void setNextBreakPointList(List<Integer> nextBreakPointList) {
        this.nextBreakPointList = nextBreakPointList;
    }
}
