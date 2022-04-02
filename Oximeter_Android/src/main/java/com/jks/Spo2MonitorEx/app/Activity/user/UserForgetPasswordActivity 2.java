package com.jks.Spo2MonitorEx.app.Activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.myView.EmailAutoCompleteTextView;
import com.jks.Spo2MonitorEx.util.TitlebarUtil;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.data.DataCheckUtil;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.dialog.MyDialog;
import com.jks.Spo2MonitorEx.util.entity.json.RegisterBean;
import com.jks.Spo2MonitorEx.util.entity.json.ResponseObject;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.jks.Spo2MonitorEx.util.web.CheckNetwork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by apple on 16/8/26.
 */
public class UserForgetPasswordActivity extends MyActivity implements View.OnClickListener {


    //登录返回的邮箱地址
    private String email = null;
    private EmailAutoCompleteTextView forgotPwdEmail;
    private Button sendEmail;

    //titleBar组件
    private ImageButton btnTitleBack;

    private Activity activity;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_forgetpwd);

        init();
        initTitleBar();
        initView();
        initHandler();
    }

    @Override
    protected void init() {
        super.init();
        activity = this;
        Intent fromIntent = getIntent();
        email = fromIntent.getStringExtra("email");
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        TitlebarUtil.showTitleName(activity, R.string.user_login_tv_tx1);
        btnTitleBack = TitlebarUtil.showIbLeft(activity, R.drawable.titlebar_btn_back_sl);
        btnTitleBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        super.initView();

        forgotPwdEmail = (EmailAutoCompleteTextView) findViewById(R.id.et_user_foget_email);
        sendEmail = (Button) findViewById(R.id.btn_user_forget_sure);

        sendEmail.setOnClickListener(this);

        forgotPwdEmail.setFocusable(true);
        forgotPwdEmail.requestFocus();
        onFocusChange(forgotPwdEmail.isFocused(), forgotPwdEmail);

        if (email != null && !email.equals("")) {
            forgotPwdEmail.setText(email);
        } else {
            sendEmail.setEnabled(false);
            sendEmail.setTextColor(getResources().getColor(R.color.textWhiteFF));
        }

        forgotPwdEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                processingData(forgotPwdEmail);
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ReErrorCode.ERRCODE_0:
                        //验证邮件发送成功
                        DialogUtil.dismiss2Msg(R.string.check_forget_email);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!forgotPwdEmail.getText().toString().equals("")) {
                                    //输入框跟注册信息相同, 则提示用户去验证邮箱
                                    gotoMailBoxDialog("", R.string.check_email, R.string.check_email_detail);
                                }
                            }
                        }, 1500);
                        break;
                    case ReErrorCode.ERRCODE_405:
                        DialogUtil.dismiss2Msg(R.string.check_forget_email_fail);
                        showTip(ReErrorCode.getErrodType(context, ReErrorCode.ERRCODE_405));
                        break;
                    default:
                        DialogUtil.dismiss2Msg(R.string.check_forget_email_fail);
                        showTip(ReErrorCode.getErrodType(context, msg.what));
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_title_left:
//                startActivity(new Intent(context, UserLoginActivity.class));
                finish();
                break;
            case R.id.btn_user_forget_sure:
                submitForgotPwd();
                break;
            default:
                break;
        }
    }

    private void submitForgotPwd() {
        String emailAddress = forgotPwdEmail.getText().toString().trim();

        if (CheckNetwork.checkNetwork(activity)) {
            if (processingData(forgotPwdEmail)) {
                if (CheckNetwork.checkNetwork3(context)) {
                    if (DataCheckUtil.checkData(emailAddress)) {//检查是否是邮箱格式
                        DialogUtil.show(this, R.string.processing);
                        MyThread.startNewThread(new Runnable() {
                            @Override
                            public void run() {
                                RequestParams params = new RequestParams();
                                RegisterBean regData = new RegisterBean();
                                regData.setKey("ResetPwdByEmail");
                                regData.setEmail(forgotPwdEmail.getText().toString());
                                String regDataJson = new JsonUtils<RegisterBean>().getJsonString(regData);
                                Log.e("修改密码", regDataJson);
                                try {
                                    params.setBodyEntity(new StringEntity(regDataJson));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        Gson gson = new Gson();
                                        ResponseObject<String> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>() {
                                        }.getType());
                                        sendMsg(object.getError_code());
                                    }

                                    @Override
                                    public void onFailure(HttpException error, String msg) {
                                        DialogUtil.dismiss();
                                        Toast.makeText(UserForgetPasswordActivity.this, getResources().getString(R.string.json_checknet_nettimeout), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }else {
                        showTip(R.string.mail_format_is_incorrect);
                    }

                } else {
                    showTip(R.string.checknet_login);
                }
            }
        }
    }

    /**
     * 显示键盘
     * @param hasFocus
     * @param view
     */
    private void onFocusChange(boolean hasFocus, final View view) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (isFocus) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    private boolean processingData(EditText regist_email) {
        String email = regist_email.getText().toString();
        if (emailOK(email)) {
            sendEmail.setEnabled(true);
            sendEmail.setTextColor(getResources().getColor(R.color.textWhite));
            return true;
        } else {
            sendEmail.setEnabled(false);
            sendEmail.setTextColor(getResources().getColor(R.color.textWhiteFF));
            return false;
        }
    }

    private void gotoMailBoxDialog(String title, int msg, int detail) {
        MyDialog.Builder builder = new MyDialog.Builder(context);
        builder.setMessage(msg);
        builder.setDetail(detail);
        builder.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showTip(int StringId) {
        Toast.makeText(this, StringId, Toast.LENGTH_LONG).show();
    }

    private void showTip(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private boolean emailOK(String email) {
        if (email.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }
}
