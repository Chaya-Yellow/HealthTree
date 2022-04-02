package com.jks.Spo2MonitorEx.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.VersionManager;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;

/**
 * Created by badcode on 15/11/25.
 */
public class AboutActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
        TextView appVersionName = (TextView) findViewById(R.id.app_version_name);
        appVersionName.setText("V" + VersionManager.getAppVersion(this));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
