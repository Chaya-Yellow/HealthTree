package com.jks.Spo2MonitorEx.util.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;

import java.util.List;

/**
 * Created by apple on 16/8/31.
 */
public class MyWheel5 {
    private Activity activity;
    private TextView tvBP;
    private List<String> SBP;
    private WheelView wheelSBP;
    private View v;
    private String str;

    public MyWheel5(Activity activity, TextView tvBP, List<String> SBP, String str) {
        this.activity = activity;
        this.SBP = SBP;
        this.tvBP = tvBP;
        this.str = str;
        // this.index = index;
        init();
        initView();
        setArrayAdapter();
        if(tvBP!=null){
            System.out.println("tvBP.getText().toString()::" + tvBP.getText().toString());
            setPointBP(tvBP.getText().toString());
        }
        onclick();
    }

    public MyWheel5(Activity activity, View v, TextView tvBP, List<String> SBP, String str) {
        this.activity = activity;
        this.SBP = SBP;
        this.tvBP = tvBP;
        this.str = str;
        this.v = v;
        // this.index = index;
        init();
        initView();
        System.out.println("SBP:" + SBP.toString());
        setArrayAdapter();
        if(tvBP!=null){
            System.out.println("tvBP.getText().toString()::" + tvBP.getText().toString());
            setPointBP(tvBP.getText().toString());}
        onclick();
    }

    private void onclick() {
        wheelSBP.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int index = wheel.getCurrentItem();
                tvBP.setText(SBP.get(index));
            }
        });

    }

    private void initView() {
        if (v != null) {
            wheelSBP = (WheelView) v.findViewById(R.id.add_user_Status);
        } else {
            wheelSBP = (WheelView) v.findViewById(R.id.add_user_Status);
        }
    }
    /**
     * 根据传过来的天数，进行比较，来显示当前选中的位置
     * @param str
     */
    public void setPointBP(String str) {
        for (int i = 0; i < SBP.size(); i++) {
            if (SBP.get(i).equals(str)) {
                wheelSBP.setCurrentItem(i);
                break;
            }
        }

    }

    private void setArrayAdapter() {
        wheelSBP.setAdapter(new ArrayWheelAdapter<String>(SBP));
    }
    private void init() {
    }
}
