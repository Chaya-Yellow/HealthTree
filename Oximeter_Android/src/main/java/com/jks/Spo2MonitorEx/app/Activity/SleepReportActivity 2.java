package com.jks.Spo2MonitorEx.app.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Adapter.ReportIndicatorAdapter;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by badcode on 16/2/29.
 */
public class SleepReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_report);

        // DEFAULT
        ViewPager defaultViewpager = (ViewPager) findViewById(R.id.viewPager);
        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator);
        ReportIndicatorAdapter adapter = new ReportIndicatorAdapter(getSupportFragmentManager());
        defaultViewpager.setAdapter(adapter);
        defaultIndicator.setViewPager(defaultViewpager);

        ImageButton imageButton = (ImageButton) findViewById(R.id.back_report_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        defaultViewpager.setOffscreenPageLimit(2);
    }
}
