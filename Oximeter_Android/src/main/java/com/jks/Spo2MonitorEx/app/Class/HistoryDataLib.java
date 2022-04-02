package com.jks.Spo2MonitorEx.app.Class;

import java.util.ArrayList;

/**
 * Created by badcode on 16/2/24.
 */
public class HistoryDataLib {
    private static HistoryDataLib instance = null;
    public ArrayList<HistoryData> mHistoryDatas;

    private HistoryDataLib() {
        mHistoryDatas = new ArrayList<>();
    }

    public static HistoryDataLib getInstance() {
        if (instance == null) {
            instance = new HistoryDataLib();
        }
        return instance;
    }
}
