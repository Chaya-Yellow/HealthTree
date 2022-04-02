package com.jks.Spo2MonitorEx.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jks.Spo2MonitorEx.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by badcode on 16/2/29.
 * Sleep Activity
 */
public class SleepActivity extends AppCompatActivity {
    private TextView mTimerTV;
    private ImageView mTimerBg;

    private TextView mTimerButtonBg;
    private TextView mTimerButton;

    private boolean isBegan = false;
    private int timeCount = 0;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sleep);

        ImageButton backButton = (ImageButton) findViewById(R.id.back_sleep_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton reportButton = (ImageButton) findViewById(R.id.sleep_report_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SleepActivity.this, SleepReportActivity.class);
                startActivity(intent);
            }
        });

        mTimerTV = (TextView) findViewById(R.id.time);
        mTimerBg = (ImageView) findViewById(R.id.clock);
        mTimerButtonBg = (TextView) findViewById(R.id.inner_circle);
        mTimerButton = (TextView) findViewById(R.id.button);

        mHandler = new Handler(new Handler.Callback(){

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        int hour = timeCount / 3600;
                        int minute = timeCount / 60;
                        if (minute >= 60) {
                            minute = minute % 60;
                        }
                        int second = timeCount % 60;

                        String time = "";

                        if (hour < 10) {
                            time = "0" + hour;
                        } else {
                            time = "" + hour;
                        }

                        if (minute < 10) {
                            time = time + ":0" + minute;
                        } else {
                            time = time + ":" + minute;
                        }

                        if (second < 10) {
                            time = time + ":0" + second;
                        } else {
                            time = time + ":" + second;
                        }

                        mTimerTV.setText(time);
                        timeCount++;
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        mTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBegan) {
                    new MaterialDialog.Builder(SleepActivity.this)
                            .title("确定结束？")
                            .content("Really?")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    isBegan = false;
                                    mTimerButtonBg.setBackground(getResources().getDrawable(R.drawable.circle_inside));
                                    mTimerButton.setText("开始");

                                    mTimerBg.setVisibility(View.VISIBLE);
                                    mTimerTV.setVisibility(View.INVISIBLE);

                                    mTimer.cancel();
                                    timeCount = 0;
                                    mTimerTV.setText("00:00:00");
                                }
                            })
                            .show();
                } else {
                    isBegan = true;
                    mTimerButtonBg.setBackground(getResources().getDrawable(R.drawable.circle_b));
                    mTimerButton.setText("结束");

                    mTimerBg.setVisibility(View.INVISIBLE);
                    mTimerTV.setVisibility(View.VISIBLE);

                    mTimer = new Timer();
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }
                    };
                    mTimer.scheduleAtFixedRate(mTimerTask, 0, 1000);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBegan) {
            mTimer.cancel();
        }
    }
}
