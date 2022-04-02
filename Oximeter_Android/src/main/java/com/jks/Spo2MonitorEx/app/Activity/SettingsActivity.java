package com.jks.Spo2MonitorEx.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.setting.ManageAcountActivity;
import com.jks.Spo2MonitorEx.app.Activity.setting.MoreSetPassworldActivity;
import com.jks.Spo2MonitorEx.app.Activity.user.UserLoginActivity;
import com.jks.Spo2MonitorEx.app.MainActivity;
import com.jks.Spo2MonitorEx.util.CheckNet;
import com.jks.Spo2MonitorEx.util.LoadingAva;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.AccountIfcImpl;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.entity.json.AccountBean;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.jks.Spo2MonitorEx.util.web.CheckNetwork;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by badcode on 15/10/23.
 */
public class SettingsActivity extends MyActivity implements OnClickListener {

    private final int LOGINOUT_SUCCESS = 0;

    private TextView unit_h, unit_w;
    private LinearLayout loginOut;
    private LinearLayout management_account;

    private Handler handler;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        initHandler();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void initView() {
        super.initView();
        unit_h = (TextView) findViewById(R.id.unit_h);
        unit_w = (TextView) findViewById(R.id.unit_w);
        findViewById(R.id.unit_h_linearlayout).setOnClickListener(this);
        findViewById(R.id.unit_w_linearlayout).setOnClickListener(this);

        loginOut = (LinearLayout) findViewById(R.id.login_out);
        loginOut.setOnClickListener(this);

        findViewById(R.id.change_password).setOnClickListener(this);
        management_account = (LinearLayout) findViewById(R.id.management_account);
        management_account.setOnClickListener(this);
//        management_account.setVisibility(View.GONE);


        setWeightUnit();
        setHeightUnit();

        ImageButton backButton = (ImageButton) findViewById(R.id.back_settings_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final FrameLayout aboutLayout = (FrameLayout) findViewById(R.id.about_layout);
        aboutLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    aboutLayout.setBackgroundColor(getResources().getColor(R.color.backgroundDark));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    aboutLayout.setBackgroundColor(getResources().getColor(R.color.backgroundLight));
                    Intent i = new Intent(SettingsActivity.this, AboutActivity.class);
                    startActivity(i);
                }

                return true;
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOGINOUT_SUCCESS) {
                    exit_settings();
                } else {
//                    DialogUtil.dismiss2Msg(R.string.more_exit_fail);
//                    showTip(ReErrorCode.getErrodType(context, msg.what));
                    exit_settings();
                }
            };
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unit_h_linearlayout:
                if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
                    SharedPreferencesUtil.setHeightUnit(context, 2);
                } else {
                    SharedPreferencesUtil.setHeightUnit(context, 1);
                }
                setHeightUnit();
                break;
            case R.id.unit_w_linearlayout:
                if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
                    SharedPreferencesUtil.setWeightUnit(context, 2);
                } else {
                    SharedPreferencesUtil.setWeightUnit(context, 1);
                }
                setWeightUnit();
                break;
            case R.id.login_out:
                if (CheckNetwork.checkNetwork(activity)) {
                    if (CheckNet.isWifi(context)) {
                        logout();
                    } else {
                        showTip(R.string.checknet_login);
                    }
                }
                break;
            case R.id.change_password:
                startActivity(new Intent(SettingsActivity.this, MoreSetPassworldActivity.class));
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft); // 在跳转的语句后加上这条语句。
                break;
            case R.id.management_account:
                startActivity(new Intent(SettingsActivity.this, ManageAcountActivity.class));
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft); // 在跳转的语句后加上这条语句。
                break;
            default:
                break;
        }
    }

    private void setHeightUnit() {
        String[] b = new String[3];
        StringBuffer sb = new StringBuffer();
        // 身高单位设置。
        if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
            b[0] = "<font font color=#ffffff>" + this.getResources().getString(R.string.more_unit_height_cm)
                    + " </font>";
            b[1] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_h)
                    + " </font>";
            b[2] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_height_in)
                    + " </font>";
        } else {
            b[0] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_height_cm)
                    + " </font>";
            b[1] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_h)
                    + " </font>";
            b[2] = "<font font color=#ffffff>" + this.getResources().getString(R.string.more_unit_height_in)
                    + " </font>";
        }
        for (int i = 0; i < 3; i++) {
            sb.append(b[i]);
        }
        unit_h.setText(Html.fromHtml(sb.toString()));
    }

    private void setWeightUnit() {
        String[] b = new String[3];
        StringBuffer sb = new StringBuffer();
        // 体重单位设置。
        if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
            b[0] = "<font font color=#ffffff>" + this.getResources().getString(R.string.more_unit_weight_kg)
                    + " </font>";
            b[1] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_h)
                    + " </font>";
            b[2] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_weight_lb)
                    + " </font>";
        } else {
            b[0] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_weight_kg)
                    + " </font>";
            b[1] = "<font font color=#a3a3a3>" + this.getResources().getString(R.string.more_unit_h)
                    + " </font>";
            b[2] = "<font font color=#ffffff>" + this.getResources().getString(R.string.more_unit_weight_lb)
                    + " </font>";
        }
        for (int i = 0; i < 3; i++) {
            sb.append(b[i]);
        }
        unit_w.setText(Html.fromHtml(sb.toString()));
    }

    private void logout() {
        DialogUtil.show(this, R.string.more_exiting);
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                AccountBean accountBean = new AccountBean();
                accountBean.setKey("Logout");
//                familyData.setMemberId(familyId);
                accountBean.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                accountBean.setAccountId(SharedPreferencesUtil.getMemberId(context));
                String logoutJson = new JsonUtils<AccountBean>().getJsonString(accountBean);
                Log.e("退出登陆JSON", logoutJson);
                try {
                    params.setBodyEntity(new StringEntity(logoutJson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
                        DialogUtil.dismiss2Msg(R.string.more_exit_suceed);
                        if (errorCode == 0) {
                            sendMsg(LOGINOUT_SUCCESS, null);
                        } else {
                            sendMsg(errorCode, null);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
//                        DialogUtil.dismiss2Msg(R.string.more_alter_pwd_Failure);
//                        showTip(getResources().getString(R.string.json_checknet_nettimeout));
                        DialogUtil.dismiss2Msg(R.string.more_exit_suceed);
                        exit_settings();
                    }
                });
            }
        });
    }

    // 退出登陆
    public void exit_settings() {
//        DialogUtil.show(this, R.string.more_exiting);
        SharedPreferencesUtil.clearSp(this);// 先清空偏好，不然没法跳转到欢迎页面
        // DialogUtil.setMsg(R.string.more_exit_suceed);
        // 标志退出
//        MainActivity.isExit = true;
//        MainActivity.tabHostCurrentTab = 0;
        Message msg = new Message();
        msg.what = MainActivity.CLEARBTE;
        config.getMainHandler2().sendMessage(msg);
        HistoryActivity.selectDate = null;
        // 清除数据库内容
        clearTable();
        // 清除头像。。避免内存外溢
        LoadingAva.clearAvas();
        final List<String> preEmail = new ArrayList<String>();
        if (config.getUserName() != null && !config.getUserName().equals("")) {
            preEmail.add(config.getUserName());
        }else {
            preEmail.add("");
        }
        // 清空Config的内容避免对下一个用户照成影响
        final Config config = (Config) SettingsActivity.this.getApplicationContext();
        config.cleanData();
        // 关闭dialog
        DialogUtil.dismiss2Msg(R.string.more_exit_suceed);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SettingsActivity.this, UserLoginActivity.class);
                intent.putExtra("email_logout", preEmail.get(0));
                preEmail.clear();
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private void clearTable() {
        FamilyIfc familyIfc = new FamilyIfcImpl(this);
        familyIfc.deleteTable();
        SharedPreferencesUtil.setTime(context, "0");

        AccountIfc ifc = new AccountIfcImpl(context);
        ifc.deleteByID(ifc.findByAccount(config.getUserName()).getId());

        //关闭同步的定时器
        MainActivity.syncDataTimer.cancel();
    }

    private void showTip(String errString) {
        Toast.makeText(this, errString, Toast.LENGTH_LONG).show();
    }

    private void showTip(int StringId) {
        Toast.makeText(this, StringId, Toast.LENGTH_LONG).show();
    }

    private void sendMsg(int whatId, Object objs) {
        Message msg = new Message();
        msg.what = whatId;
        msg.obj = objs;
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }
}
