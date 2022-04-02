package com.jks.Spo2MonitorEx.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jks.Spo2MonitorEx.app.Fragment.Report1Fragment;
import com.jks.Spo2MonitorEx.app.Fragment.Report2Fragment;
import com.jks.Spo2MonitorEx.app.Fragment.Report3Fragment;

/**
 * Created by badcode on 16/2/29.
 */
public class ReportIndicatorAdapter extends FragmentPagerAdapter {

    public ReportIndicatorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new Report1Fragment();
        } else if(position == 1) {
            return new Report2Fragment();
        } else {
            return new Report3Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
