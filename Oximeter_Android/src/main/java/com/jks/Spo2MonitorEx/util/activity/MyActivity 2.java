package com.jks.Spo2MonitorEx.util.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.config.Config;

/**
 * Created by apple on 16/7/16.
 */
public class MyActivity extends Activity {

    private ActivityTaskManager manager = ActivityTaskManager.getInstance();
    protected Config config;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        config = (Config) getApplicationContext();
        context = this;
        manager.putActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void init() {

    }

    protected void initView() {

    }

    protected void initTitleBar() {

    }

    protected void loadView() {

    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_lefttoright, R.anim.out_lefttoright);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.removeActivity(this);
    }

    /**
     * 退出该应用
     */
    public void exit(){
        manager.closeAllActivity();
    }

    /**
     * 退出除该acticity外的其他activity
     */
    public void exitOtherActivity() {
        manager.closeAllActivityExceptOne(this);
    }
}
