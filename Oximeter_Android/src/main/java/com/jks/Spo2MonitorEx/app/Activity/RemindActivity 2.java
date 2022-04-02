package com.jks.Spo2MonitorEx.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.CastomView.DoubleSlideSeekBar;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.suke.widget.SwitchButton;

import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PI_H_FLAG;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PI_H_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PI_L_FLAG;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PI_L_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PR_H_FLAG;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PR_H_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PR_L_FLAG;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PR_L_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.SP_H_FLAG;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.SP_H_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.SP_L_FLAG;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.SP_L_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.CLOSE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_H_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_L_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_L_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_H_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_L_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_L_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_H_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.OPEN;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_L_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_L_VALUE_DEFAULT;

public class RemindActivity extends MyActivity {

    ImageButton ib_back_remind;
    TextView tv_sp_h_v;
    SwitchButton sb_remind_sp_h;
    TextView tv_sp_l_v;
    SwitchButton sb_remind_sp_l;
    DoubleSlideSeekBar srb_remind_sp;
    TextView tv_pr_h_v;
    SwitchButton sb_pr_h;
    TextView tv_pr_l_v;
    SwitchButton sb_pr_l;
    DoubleSlideSeekBar srb_remind_pr;
    TextView tv_pi_h_v;
    SwitchButton sb_pi_h;
    TextView tv_pi_l_v;
    SwitchButton sb_pi_l;
    DoubleSlideSeekBar srb_remind_pi;
    DoubleSlideSeekBar.onRangeListener sp_listener;
    DoubleSlideSeekBar.onRangeListener pr_listener;
    DoubleSlideSeekBar.onRangeListener pi_listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        initView();
    }

    protected void initView() {
        ib_back_remind = findViewById(R.id.ib_back_remind);
        ib_back_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_sp_h_v = findViewById(R.id.tv_sp_h_v);
        sb_remind_sp_h = findViewById(R.id.sb_remind_sp_h);
        tv_sp_l_v = findViewById(R.id.tv_sp_l_v);
        sb_remind_sp_l = findViewById(R.id.sb_remind_sp_l);
        srb_remind_sp = findViewById(R.id.srb_remind_sp);
        tv_pr_h_v = findViewById(R.id.tv_pr_h_v);
        sb_pr_h = findViewById(R.id.sb_pr_h);
        tv_pr_l_v = findViewById(R.id.tv_pr_l_v);
        sb_pr_l = findViewById(R.id.sb_pr_l);
        srb_remind_pr = findViewById(R.id.srb_remind_pr);
        tv_pi_h_v = findViewById(R.id.tv_pi_h_v);
        sb_pi_h = findViewById(R.id.sb_pi_h);
        tv_pi_l_v = findViewById(R.id.tv_pi_l_v);
        sb_pi_l = findViewById(R.id.sb_pi_l);
        srb_remind_pi = findViewById(R.id.srb_remind_pi);

        tv_sp_h_v.setText(SharedPreferencesUtil.getStringByKey(RemindActivity.this, SP_H_VALUE, SP_H_VALUE_DEFAULT));
        
        if (OPEN.equals(SharedPreferencesUtil.getStringByKey(RemindActivity.this, SP_H_FLAG, SP_H_FLAG_DEFAULT))) {
            sb_remind_sp_h.setChecked(true);
        } else {
            sb_remind_sp_h.setChecked(false);
        }
        sb_remind_sp_h.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, SP_H_FLAG, OPEN);
                } else {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, SP_H_FLAG, CLOSE);
                }
            }
        });
        
        tv_sp_l_v.setText(SharedPreferencesUtil.getStringByKey(RemindActivity.this, SP_L_VALUE, SP_L_VALUE_DEFAULT));

        if (OPEN.equals(SharedPreferencesUtil.getStringByKey(RemindActivity.this, SP_L_FLAG, SP_L_FLAG_DEFAULT))) {
            sb_remind_sp_l.setChecked(true);
        } else {
            sb_remind_sp_l.setChecked(false);
        }
        sb_remind_sp_l.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, SP_L_FLAG, OPEN);
                } else {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, SP_L_FLAG, CLOSE);
                }
            }
        });
        
        tv_pr_h_v.setText(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PR_H_VALUE, PR_H_VALUE_DEFAULT));

        if (OPEN.equals(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PR_H_FLAG, PR_H_FLAG_DEFAULT))) {
            sb_pr_h.setChecked(true);
        } else {
            sb_pr_h.setChecked(false);
        }
        sb_pr_h.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PR_H_FLAG, OPEN);
                } else {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PR_H_FLAG, CLOSE);
                }
            }
        });

        tv_pr_l_v.setText(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PR_L_VALUE, PR_L_VALUE_DEFAULT));

        if (OPEN.equals(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PR_L_FLAG, PR_L_FLAG_DEFAULT))) {
            sb_pr_l.setChecked(true);
        } else {
            sb_pr_l.setChecked(false);
        }
        sb_pr_l.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PR_L_FLAG, OPEN);
                } else {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PR_L_FLAG, CLOSE);
                }
            }
        });

        tv_pi_h_v.setText(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PI_H_VALUE, PI_H_VALUE_DEFAULT));

        if (OPEN.equals(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PI_H_FLAG, PI_H_FLAG_DEFAULT))) {
            sb_pi_h.setChecked(true);
        } else {
            sb_pi_h.setChecked(false);
        }
        sb_pi_h.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PI_H_FLAG, OPEN);
                } else {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PI_H_FLAG, CLOSE);
                }
            }
        });

        tv_pi_l_v.setText(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PI_L_VALUE, PI_L_VALUE_DEFAULT));

        if (OPEN.equals(SharedPreferencesUtil.getStringByKey(RemindActivity.this, PI_L_FLAG, PI_L_FLAG_DEFAULT))) {
            sb_pi_l.setChecked(true);
        } else {
            sb_pi_l.setChecked(false);
        }
        sb_pi_l.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PI_L_FLAG, OPEN);
                } else {
                    SharedPreferencesUtil.setStringByKey(RemindActivity.this, PI_L_FLAG, CLOSE);
                }
            }
        });

        sp_listener = new DoubleSlideSeekBar.onRangeListener() {
            @Override
            public void onRange(int low, int big) {
                String hValue = String.valueOf(big);
                String lValue = String.valueOf(low);
                tv_sp_h_v.setText(hValue);
                tv_sp_l_v.setText(lValue);
                SharedPreferencesUtil.setStringByKey(RemindActivity.this, SP_H_VALUE, hValue);
                SharedPreferencesUtil.setStringByKey(RemindActivity.this, SP_L_VALUE, lValue);
            }
        };
        pr_listener = new DoubleSlideSeekBar.onRangeListener() {
            @Override
            public void onRange(int low, int big) {
                String hValue = String.valueOf(big);
                String lValue = String.valueOf(low);
                tv_pr_h_v.setText(hValue);
                tv_pr_l_v.setText(lValue);
                SharedPreferencesUtil.setStringByKey(RemindActivity.this, PR_H_VALUE, hValue);
                SharedPreferencesUtil.setStringByKey(RemindActivity.this, PR_L_VALUE, lValue);
            }
        };
        pi_listener = new DoubleSlideSeekBar.onRangeListener() {
            @Override
            public void onRange(int low, int big) {
                String hValue = String.valueOf(big);
                String lValue = String.valueOf(low);
                tv_pi_h_v.setText(hValue);
                tv_pi_l_v.setText(lValue);
                SharedPreferencesUtil.setStringByKey(RemindActivity.this, PI_H_VALUE, hValue);
                SharedPreferencesUtil.setStringByKey(RemindActivity.this, PI_L_VALUE, lValue);
            }
        };
        srb_remind_sp.setOnRangeListener(sp_listener);
        srb_remind_pr.setOnRangeListener(pr_listener);
        srb_remind_pi.setOnRangeListener(pi_listener);
    }
}
