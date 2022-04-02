package com.jks.Spo2MonitorEx.util.entity.json;

import com.jks.Spo2MonitorEx.util.entity.Family;

import java.util.List;

/**
 * Created by apple on 16/9/2.
 */
public class FamilyResp {
    private List<Family> families;

    public List<Family> getFamilies() {
        return families;
    }

    public void setFamilies(List<Family> families) {
        this.families = families;
    }
}
