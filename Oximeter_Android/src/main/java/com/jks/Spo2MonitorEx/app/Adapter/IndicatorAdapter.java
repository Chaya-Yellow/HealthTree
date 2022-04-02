package com.jks.Spo2MonitorEx.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jks.Spo2MonitorEx.app.Fragment.HistoryChartFragment;
import com.jks.Spo2MonitorEx.app.Fragment.HistoryParamFragment;

/**
 * Created by badcode on 15/10/21.
 */
public class IndicatorAdapter extends FragmentPagerAdapter {

    public IndicatorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /* 替换前
        if (position == 0) {
            return new HistoryParamFragment();
        } else {
            return new HistoryChartFragment();
        }
         */
        if (position == 0) {
            return new HistoryChartFragment();
        } else {
            return new HistoryParamFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
