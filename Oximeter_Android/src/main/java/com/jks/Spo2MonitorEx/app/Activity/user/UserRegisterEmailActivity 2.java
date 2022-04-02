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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.myView.ClearEditText;
import com.jks.Spo2MonitorEx.app.Activity.myView.EmailAutoCompleteTextView;
import com.jks.Spo2MonitorEx.app.Activity.setting.AddAcountActivity;
import com.jks.Spo2MonitorEx.util.KeyboardUtil;
import com.jks.Spo2MonitorEx.util.MD5;
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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by apple on 16/8/23.
 */
public class UserRegisterEmailActivity extends MyActivity implements OnClickListener {

    private ClearEditText registerCetNickname;
    private ClearEditText registerCetPwd;
    private ClearEditText registerConfirmPwd;
    private Button registerBtn;
    private EmailAutoCompleteTextView registerCetEmail;
    private ImageButton btnTitleBack;
    private ProgressBar progressBar;
    private RelativeLayout registLogoBg;
    private LinearLayout registerMain;

    private int appId = Constant.AppId;
    private Activity activity;
    private Handler handler;
    private MD5 md5;

    private String fromActivity;

    private final int ERRCODE_SUCEED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_email);
        init();
        initTitleBar();
        initView();
        initHandler();
    }

    @Override
    protected void init() {
        super.init();
        activity = this;
        md5 = new MD5(activity);
        fromActivity = getIntent().getStringExtra("from_activity");
    }


    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        TitlebarUtil.showTitleName(activity, R.string.user_login_btn_register);
        btnTitleBack = TitlebarUtil.showIbLeft(activity, R.drawable.titlebar_btn_back_sl);
        progressBar = TitlebarUtil.showProgressBar(activity);

        btnTitleBack.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
        registerCetEmail = (EmailAutoCompleteTextView) findViewById(R.id.et_user_register_username);
        registerCetPwd = (ClearEditText) findViewById(R.id.et_user_register_password1);
        registerConfirmPwd = (ClearEditText) findViewById(R.id.et_user_register_password2);
        registerBtn = (Button) findViewById(R.id.btn_user_register_sure);

        registLogoBg = (RelativeLayout) findViewById(R.id.registerpage_logo);
        registLogoBg.setOnClickListener(this);

        registerBtn.setEnabled(false);
        registerBtn.setTextColor(getResources().getColor(R.color.textWhiteFF));
        registerBtn.setOnClickListener(this);

        registerCetEmail.setFocusable(true);
        registerCetEmail.requestFocus();
        onFocusChange(registerCetEmail.isFocused(), registerCetEmail);

        registerMain = (LinearLayout) findViewById(R.id.register_main);

        KeyboardUtil.controlKeyboardLayout(registerMain, registerBtn);

        registerCetEmail.addTextChangedListener(new TextWatcher() {

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
                processingData(registerCetEmail, registerCetPwd, registerConfirmPwd);
            }
        });

        registerCetPwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                processingData(registerCetEmail, registerCetPwd, registerConfirmPwd);
            }
        });

        registerConfirmPwd.addTextChangedListener(new TextWatcher() {

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
                processingData(registerCetEmail, registerCetPwd, registerConfirmPwd);
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ERRCODE_SUCEED) {
                    DialogUtil.dismiss();
                    showToastDialog(activity, getString(R.string.activity_user_register_success), ReErrorCode.ERRCODE_0);
                }else {
                    //出现错误
                    DialogUtil.dismiss();
                    showToastDialog(activity, ReErrorCode.getErrodType(context, msg.what), ReErrorCode.ERRCODE_2000);
                }
            }
        };
    }

    @Override
    protected void loadView() {
        super.loadView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_title_left:
//                startActivity(new Intent(context, UserLoginActivity.class));
                if (fromActivity != null && fromActivity.equals(AddAcountActivity.ADDACCOUNTACTIVITY)) {
                    startActivity(new Intent(context, AddAcountActivity.class));
                }
                finish();
                break;
            case R.id.btn_user_register_sure:
                SubmitRegistration();
                break;
            case R.id.registerpage_logo:
                closeKeyboard();
                break;
            default:
                break;
        }
    }

    /**
     * 获取焦点时弹出键盘
     * @param hasFocus
     * @param view
     */
    private void onFocusChange(boolean hasFocus, final View view) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isFocus) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }, 500);
    }

    private boolean processingData(EditText regist_email, EditText regist_password, EditText confirm_password) {
        String password = regist_password.getText().toString();
        String email = regist_email.getText().toString();
        String confirmEmail = confirm_password.getText().toString();
        if (emailOK(email)) {
            if (passwordOK(password)) {
                if (passwordOK(confirmEmail)) {
                    registerBtn.setEnabled(true);
                    registerBtn.setTextColor(getResources().getColor(R.color.textWhite));
                    return true;
                } else {
                    registerBtn.setEnabled(false);
                    registerBtn.setTextColor(getResources().getColor(R.color.textWhiteFF));
                    return false;
                }
            } else {
                registerBtn.setEnabled(false);
                registerBtn.setTextColor(getResources().getColor(R.color.textWhiteFF));
                return false;
            }
        } else {
            registerBtn.setEnabled(false);
            registerBtn.setTextColor(getResources().getColor(R.color.textWhiteFF));
            return false;
        }
    }

    /**
     * 给handler发送信息
     * @param whatId
     * @param objs
     */
    protected void sendMsg(int whatId, Object objs) {
        Message msg = new Message();
        msg.what = whatId;
        msg.obj = objs;
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    private boolean passwordOK(String password) {
        if (password.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean emailOK(String email) {
        if (email.length() > 0) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * 显示对话框
     * @param mActivity
     * @param msgId
     * @param key
     */
    private void showToastDialog(Activity mActivity, String msgId, final int key) {
        MyDialog.Builder builder = new MyDialog.Builder(context);
        builder.setMessage(msgId);
        builder.isShowLine(false);
        builder.setPositiveButton(getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        Intent intent;
                        switch (key) {
                            case ReErrorCode.ERRCODE_0:
                                progressBar.setVisibility(View.GONE);
                                if (fromActivity != null && fromActivity.equals(AddAcountActivity.ADDACCOUNTACTIVITY)) {
                                    intent = new Intent(context, AddAcountActivity.class);
                                }else {
                                    intent = new Intent(context, UserLoginActivity.class);
                                }
                                String email = registerCetEmail.getText().toString().trim();
                                intent.putExtra("email", email);
                                intent.putExtra("password", registerCetPwd.getText().toString().trim());
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                progressBar.setVisibility(View.GONE);
                                break;
                        }

                    }
                });
        builder.create().show();
    }

    private void SubmitRegistration() {
        String password1 = registerCetPwd.getText().toString().trim();
        String password2 = registerConfirmPwd.getText().toString().trim();

        if (CheckNetwork.checkNetwork(activity)) {
            if (processingData(registerCetPwd, registerConfirmPwd, registerConfirmPwd)) {
                if (CheckNetwork.checkNetwork3(context)) {
                    if (password1.equals(password2) == false) {
                        showTip(R.string.user_password_tip3);
                    } else if (password1.equals(password2) == true) {
                        if (DataCheckUtil.StringFilter(password2)) {
                            Toast.makeText(context, R.string.user_error_password_input_char_error, Toast.LENGTH_SHORT).show();
                        } else {
                            if (DataCheckUtil.checkMainData(password1, 6, 30)) {
                                DialogUtil.show(this, R.string.processing);
                                MyThread.startNewThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RequestParams params = new RequestParams();
                                        RegisterBean regData = new RegisterBean();
                                        regData.setKey("Register");
                                        regData.setEmail(registerCetEmail.getText().toString());
                                        regData.setPwd(md5.getMD5(registerConfirmPwd.getText().toString()));
                                        String regDataJson = new JsonUtils<RegisterBean>().getJsonString(regData);
                                        Log.e("注册", regDataJson);
                                        try {
                                            params.setBodyEntity(new StringEntity(regDataJson));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        new HttpUtils().send(HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                                            @Override
                                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                                DialogUtil.dismiss();
                                                Gson gson = new Gson();
                                                ResponseObject<String> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<String>>(){}.getType());
                                                sendMsg(object.getError_code());
                                            }

                                            @Override
                                            public void onFailure(HttpException error, String msg) {
                                                DialogUtil.dismiss();
                                                Toast.makeText(UserRegisterEmailActivity.this, getResources().getString(R.string.json_checknet_nettimeout), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            } else {
                                Toast.makeText(context, R.string.user_error_password_combination_error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } else {
                    showTip(R.string.checknet_login);
                }
            }
        }
    }

    private void showTip(int StringId) {
        Toast.makeText(this, StringId, Toast.LENGTH_LONG).show();
    }

    private void closeKeyboard() {
        onFocusChange(false, registerCetEmail);
        onFocusChange(false, registerCetPwd);
        onFocusChange(false, registerConfirmPwd);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromActivity != null && fromActivity.equals(AddAcountActivity.ADDACCOUNTACTIVITY)) {
            startActivity(new Intent(context, AddAcountActivity.class));
        }
    }
}
