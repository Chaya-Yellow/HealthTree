package com.jks.Spo2MonitorEx.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.app.Bluetoothle.BLEDevice;
import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;

/**
 * Created by badcode on 15/10/27.
 */
public class DeviceDetailActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_detail);

        ImageView back = (ImageView) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        BLEDevice device = (BLEDevice) getIntent().getSerializableExtra("device_info");

//        TextView name = (TextView) findViewById(R.id.info_device_name);
//        name.setText(device.getName());

        TextView sn = (TextView) findViewById(R.id.info_sn);
        sn.setText(device.getSNumber());

        TextView type = (TextView) findViewById(R.id.info_type);
        type.setText(device.getType());

        TextView main = (TextView) findViewById(R.id.info_main_version);
        main.setText(device.getMainVersion());

        TextView sub = (TextView) findViewById(R.id.info_sub_version);
        sub.setText(device.getSubVersion());
    }
}
