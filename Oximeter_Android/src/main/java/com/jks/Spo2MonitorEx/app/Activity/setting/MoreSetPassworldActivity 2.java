package com.jks.Spo2MonitorEx.app.Activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.CheckNet;
import com.jks.Spo2MonitorEx.util.MD5;
import com.jks.Spo2MonitorEx.util.TitlebarUtil;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.AccountIfcImpl;
import com.jks.Spo2MonitorEx.util.data.DataCheckUtil;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;
import com.jks.Spo2MonitorEx.util.entity.json.ChangePwdBean;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
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
import java.util.List;

/**
 * Created by apple on 2016/9/26.
 */

/**
 * 重置密码
 */
public class MoreSetPassworldActivity extends MyActivity implements View.OnClickListener {

    private Activity activity = this;
    private EditText login_password, new_password, confirm_password;
    private Button enter;

    private final int CHANGE_SUCCESS = 0;

    private Handler handler;
    private MD5 md5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_set_password);
        init();
        initTitleBar();
        loadView();
        initHandler();
    }

    @Override
    protected void init() {
        super.init();
        md5 = new MD5(activity);
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        TextView tvTitleName = (TextView) activity.findViewById(R.id.tv_title);
        tvTitleName.setText(R.string.more_alter_passworld);
        tvTitleName.setTextSize(16);
//        Button ibLeft = TitlebarUtil.showBtnLeft(this);
        ImageButton ibLeft = TitlebarUtil.showIbLeft(this, R.drawable.titlebar_btn_back_sl);
//        ibLeft.setText(R.string.btn_cancel);
//        ibLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ibLeft.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        ibLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    protected void loadView() {
        super.loadView();
        login_password = (EditText) findViewById(R.id.login_pwd);
        new_password = (EditText) findViewById(R.id.new_pwd);
        confirm_password = (EditText) findViewById(R.id.confirm_pwd);
        enter = (Button) findViewById(R.id.enter_btn);
        enter.setOnClickListener(this);

        login_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                processingData(login_password, new_password, confirm_password);

            }
        });

        new_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                processingData(login_password, new_password, confirm_password);

            }
        });

        confirm_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                processingData(login_password, new_password, confirm_password);

            }
        });

        processingData(login_password, new_password, confirm_password);
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == CHANGE_SUCCESS) {
                    DialogUtil.dismiss2Msg(R.string.more_alter_pwd_success);
                    postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1500);
                } else {
                    DialogUtil.dismiss2Msg(R.string.more_alter_pwd_Failure);
                    showTip(ReErrorCode.getErrodType(context, msg.what));
                }
            };
        };
    }

    private void alter(final String oldPassword, final String newPassword) {
        DialogUtil.show(this, R.string.altering);
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                ChangePwdBean changePwdBean = new ChangePwdBean();
                changePwdBean.setKey("ChangePwd");
//                familyData.setMemberId(familyId);
                changePwdBean.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                changePwdBean.setAccountId(SharedPreferencesUtil.getMemberId(context));
                changePwdBean.setOldPwd(md5.getMD5(oldPassword));
                changePwdBean.setNewPwd(md5.getMD5(newPassword));
                String changePwdJson = new JsonUtils<ChangePwdBean>().getJsonString(changePwdBean);
                Log.e("修改密码JSON", changePwdJson);
                try {
                    params.setBodyEntity(new StringEntity(changePwdJson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
                        if (errorCode == 0) {
                            AccountIfc ifc = new AccountIfcImpl(context);
                            List<LoginInfo> loginInfos = ifc.findAll();
                            for (int i = 0; i < loginInfos.size(); i++) {
                                if (SharedPreferencesUtil.getAccountPosition(context) == i) {
                                    LoginInfo info = loginInfos.get(i);
                                    info.setPassword(md5.getMD5(newPassword));
                                    ifc.update(info);
                                }
                            }
                            sendMsg(CHANGE_SUCCESS, null);
                        }else {
                            sendMsg(errorCode, null);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        DialogUtil.dismiss2Msg(R.string.more_alter_pwd_Failure);
                        showTip(getResources().getString(R.string.json_checknet_nettimeout));
                    }
                });
            }
        });
    }

    /**
     * 判断输入不为空
     * @param login_pwd
     * @param new_pwd
     * @param confirm_pwd
     * @return
     */
    private Boolean processingData(EditText login_pwd, EditText new_pwd, EditText confirm_pwd) {
        final String loginPwd = login_pwd.getText().toString();
        final String newPassword = new_pwd.getText().toString();
        final String confPassword = confirm_pwd.getText().toString();
        if (loginPwd.length() > 0) {
            if (newPassword.length() > 0 && confPassword.length() > 0) {
                enter.setEnabled(true);
                enter.setTextColor(getResources().getColor(R.color.textWhite));
                return true;
            } else {
                enter.setEnabled(false);
                enter.setTextColor(getResources().getColor(R.color.textWhiteFF));
                return false;
            }
        } else {
            enter.setEnabled(false);
            enter.setTextColor(getResources().getColor(R.color.textWhiteFF));
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (CheckNetwork.checkNetwork(activity)) {
            if (processingData(login_password, new_password, confirm_password)) {
                if (CheckNet.isWifi(context)) {
                    if (new_password.getText().toString().equals(confirm_password.getText().toString())) {
                        if (DataCheckUtil.StringFilter(new_password.getText().toString())) {
                            Toast.makeText(context, R.string.user_error_password_input_char_error, Toast.LENGTH_SHORT).show();
                        } else {
                            if (DataCheckUtil.checkMainData(new_password.getText().toString(), 6, 30)) {
                                if (!new_password.getText().toString().equals(login_password.getText().toString())) {
                                    alter(login_password.getText().toString(), new_password.getText().toString());
                                }else {
                                    Toast.makeText(context, R.string.more_alter_pwd_same, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(context, R.string.user_error_password_combination_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        showTip(R.string.more_alter_pwd);
                    }
                } else {
                    showTip(R.string.checknet_login);
                }
            }
        }
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
