package com.jks.Spo2MonitorEx.app.Activity.user;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.myView.ClearEditText;
import com.jks.Spo2MonitorEx.app.Activity.myView.EmailAutoCompleteTextView;
import com.jks.Spo2MonitorEx.app.MainActivity;
import com.jks.Spo2MonitorEx.util.KeyboardUtil;
import com.jks.Spo2MonitorEx.util.MD5;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.AccountIfcImpl;
import com.jks.Spo2MonitorEx.util.data.DataCheckUtil;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.dialog.MyDialog;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;
import com.jks.Spo2MonitorEx.util.entity.json.LoginBean;
import com.jks.Spo2MonitorEx.util.entity.json.ResponseObject;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
import com.jks.Spo2MonitorEx.util.other.BLTToast;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by apple on 16/7/16.
 */
public class UserLoginActivity extends MyActivity implements View.OnClickListener {
    private Activity activity = this;
    private Context context = this;
    private MD5 md5;
    //注册返回的邮箱地址和密码
    private String email = null;
    private String passWord = null;

    private String email_logout = null;

    private EmailAutoCompleteTextView login_username;
    private ClearEditText login_password;// 输入用户名和密码
    private Button login_login;// 登录按钮
    private Button registerBtn;// 注册按钮
    private TextView login_forget_tv;
    private LinearLayout loginBg;
    private RelativeLayout logoLayout;

    private Intent intent;
    private IntentFilter intentFilter;
    private Handler handler;

    public static final int ERRCODE_NO_RETRUN_ERROR = 2000;//网络处理异常
    public static final int ERRCODE_SUCEED = 0;//请求无错误
    public static final int ERRCODE_NETCONNECT_TIMEOUT = 3000;//请求无错误

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_login);

        init();
        initView();
        initHandler();

        md5 = new MD5(activity);
    }

    @Override
    protected void init() {
        super.init();
        Intent fromIntent = getIntent();
        email = fromIntent.getStringExtra("email");
        email_logout = fromIntent.getStringExtra("email_logout");

        passWord = fromIntent.getStringExtra("password");

        intent = new Intent();
        intentFilter = new IntentFilter();
        reciver = new Reciver();
        intentFilter.addAction("退出应用程序");
        this.registerReceiver(reciver, intentFilter);
    }

    @Override
    protected void initView() {
        super.initView();

        login_username = (EmailAutoCompleteTextView) findViewById(R.id.et_user_login_username);
        login_password = (ClearEditText) findViewById(R.id.et_user_login_password);

        login_login = (Button) findViewById(R.id.btn_user_login_sure);
        registerBtn = (Button) findViewById(R.id.btn_user_Register_sure);
        login_forget_tv = (TextView) findViewById(R.id.user_login_find_password_tv);
        loginBg = (LinearLayout) findViewById(R.id.login_bg);
        loginBg.setOnClickListener(this);
        logoLayout = (RelativeLayout) findViewById(R.id.loadpage_logo);
        logoLayout.setOnClickListener(this);
        login_forget_tv.setOnClickListener(this);

        onFocusChange(login_username.isFocused(), login_username);
        if (passWord != null && email != null) {
            login_username.setText(email);
            login_password.setText(passWord);
            login_login.setEnabled(true);
            login_login.setTextColor(getResources().getColor(R.color.textWhite));
        } else {
            if (email_logout != null) {
                login_username.setText(email_logout);
            }
            login_login.setEnabled(false);
            login_login.setTextColor(getResources().getColor(R.color.textWhiteFF));
        }

        login_username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processingData(login_username, login_password);

            }
        });

        login_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                processingData(login_username, login_password);

            }
        });

        registerBtn.setOnClickListener(this);
        login_login.setOnClickListener(this);
        KeyboardUtil.controlKeyboardLayout(loginBg, login_login);
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ERRCODE_SUCEED:
                        //登录成功
                        DialogUtil.dismiss2Msg(R.string.user_login_suceed);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(context, MainActivity.class);
                                intent.putExtra("from", 2);
                                startActivity(intent);
                                Intent outintent = new Intent("退出应用程序");
                                sendBroadcast(outintent);
                            }
                        }, 1500);
                        break;
                    case ERRCODE_NO_RETRUN_ERROR:
                        //返回json数据解析错误
                        DialogUtil.dismiss2Msg(R.string.user_login_failure);
                        showTip(ReErrorCode.getErrodType(context, ReErrorCode.ERRCODE_401));
                        break;
                    case ERRCODE_NETCONNECT_TIMEOUT:
                        //网络连接错误
                        DialogUtil.dismiss2Msg(R.string.user_login_failure);
                        showTip(getResources().getString(R.string.json_checknet_nettimeout));
                        break;
                    default:
                        //登录请求错误(密码, 账号不正确之类的错误)
                        DialogUtil.dismiss2Msg(R.string.user_login_failure);
                        showTip(ReErrorCode.getErrodType(context, msg.what));
                        if (msg.what == 405) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (email != null && passWord != null) {//是注册返回的
                                        if (email.equals(login_username.getText().toString()) && passWord.equals(login_password.getText().toString())) {
                                            //输入框跟注册信息相同, 则提示用户去验证邮箱
                                            gotoEmailBoxDialog(email + " " + getResources().getString(R.string.check_email_verified), R.string.check_email, R.string.check_email_detail);
                                        }
                                    }
                                }
                            }, 1500);
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_login_sure:
                if (CheckNetwork.checkNetwork(activity)) {//检查网络, 并提示打开
                    if (processingData(login_username, login_password)) {
                        if (CheckNetwork.checkNetwork3(context)) {
                            if (DataCheckUtil.StringFilter(login_password.getText().toString())) {
                                BLTToast.show(context, R.string.user_login_password_error_tips);
                            } else {
                                login(login_username.getText().toString(), login_password.getText().toString(), "");
                            }
                        } else {
                            BLTToast.show(context, R.string.checknet_login);
                        }
                    }
                }
                break;
            case R.id.btn_user_Register_sure:
                intent.setClass(this, UserRegisterEmailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
                break;
            case R.id.user_login_find_password_tv:
                Intent intent = new Intent();
                intent.setClass(this, UserForgetPasswordActivity.class);
                intent.putExtra("email", login_username.getText().toString().trim());
                startActivity(intent);
                break;
            default:
                closeKeyBoard();
                break;
        }
    }

    /**
     * 退出应用
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent("退出应用程序");
        sendBroadcast(intent);
        exit();
        MyThread.startNewThread(new Runnable() {

            @Override
            public void run() {
                System.exit(0);
            }
        });
        super.onBackPressed();

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

    /** 处理数据，能否提交 */
    private Boolean processingData(EditText login_username, EditText login_password) {
        final String username = login_username.getText().toString();
        final String password = login_password.getText().toString();
        if (username.length() > 0) {
            if (password.length() > 0) {
                login_login.setEnabled(true);
                login_login.setText(getString(R.string.user_login_btn_login));
                login_login.setTextColor(getResources().getColor(R.color.textWhite));
                return true;
            } else {
                login_login.setEnabled(false);
                login_login.setTextColor(getResources().getColor(R.color.textWhiteFF));
                return false;
            }

        } else {
            login_login.setEnabled(false);
            login_login.setTextColor(getResources().getColor(R.color.textWhiteFF));
            return false;
        }
    }

    /**
     * 用户登录
     * @param userEmail
     * @param passWord
     */
    private void login(final String userEmail, final String passWord, final String getNetIp) {
        DialogUtil.show(this, R.string.logining);
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                final LoginBean loginData = new LoginBean();
                loginData.setKey("Login");
                loginData.setAccount(userEmail);
                loginData.setPwd(md5.getMD5(passWord));
                final String regDataJson = new JsonUtils<LoginBean>().getJsonString(loginData);
                Log.e("登录", regDataJson);
//                try {
//                    params.setBodyEntity(new StringEntity(regDataJson));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject resJson;
                        ResponseObject<String> object = new ResponseObject<String>();
                        try {
                            resJson = new JSONObject(responseInfo.result);
                            //获取登录错误码
                            object.setError_code(resJson.getInt("error_code"));

                            if (object.getError_code() == ERRCODE_SUCEED) {
                                //登录成功
                                ResponseObject<LoginBean> loginObject = new Gson().fromJson(responseInfo.result, new TypeToken<ResponseObject<LoginBean>>() {
                                }.getType());
                                SharedPreferencesUtil.setCilenKey(context, loginObject.getData().getClientKey());
                                SharedPreferencesUtil.setMemberId(context, loginObject.getData().getAccountId());
                                SharedPreferencesUtil.setUserName(context, userEmail);
                                config.setUserName(userEmail);//用户的邮箱当做用户名
                                config.setMemberId(loginObject.getData().getAccountId());
                                config.setClientKey(loginObject.getData().getClientKey());

                                addAccountManage(userEmail, passWord, loginObject.getData().getAccountId(), loginObject.getData().getClientKey());

                                sendMsg(loginObject.getError_code());
                            } else {
                                Log.e("登录错误", object.getError_code() + "");
                                sendMsg(object.getError_code());
                            }
                        } catch (JSONException e) {
                            sendMsg(ERRCODE_NO_RETRUN_ERROR);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        sendMsg(ERRCODE_NETCONNECT_TIMEOUT);
                    }
                });
            }
        });
    }

    /**
     * 跳转到邮箱的弹出框
     * @param title
     * @param msg
     * @param detail
     */
    private void gotoEmailBoxDialog(String title, int msg, int detail) {
        MyDialog.Builder builder = new MyDialog.Builder(context);
        builder.setTitle(title);
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

    private void closeKeyBoard() {
        onFocusChange(false, login_username);
        onFocusChange(false, login_password);
    }

    /**
     * 添加该登录信息到账号管理中
     * @param userEmail
     * @param passWord
     * @param accountId
     * @param clientKey
     */
    private void addAccountManage(String userEmail, String passWord, int accountId, String clientKey) {
        AccountIfc ifc = new AccountIfcImpl(context);
        List<LoginInfo> loginInfos = ifc.findAll();
        LoginInfo info = new LoginInfo();
        for (LoginInfo info1 : loginInfos) {
            Log.e("存在的账号信息", info1.toString());
        }
        boolean isHadAccount = false;
//        for (int i = 0; i < loginInfos.size(); i++) {
//            if (loginInfos.get(i).getAccount().equals(userEmail)) {
//                SharedPreferencesUtil.setAccountPosition(context, i);
//                info.setId(i + 1);
//                ifc.update(info);//更新已经存在的用户
//                isHadAccount = true;
//                break;
//            }
//        }

        for (int i = 0; i < loginInfos.size(); i++) {
            if (loginInfos.get(i).getAccount().equals(userEmail)) {
                SharedPreferencesUtil.setAccountPosition(context, i);
                info = loginInfos.get(i);
                info.setAccount(userEmail);
                info.setPassword(md5.getMD5(passWord));
                info.setAccountId(accountId);
                info.setClientKey(clientKey);
                ifc.update(info);
                isHadAccount = true;
                break;
            }
        }

        if (!isHadAccount) {
            info.setAccount(userEmail);
            info.setPassword(md5.getMD5(passWord));
            info.setAccountId(accountId);
            info.setClientKey(clientKey);
            ifc.insert(info);//添加新登录的用户
            SharedPreferencesUtil.setAccountPosition(context, loginInfos.size());
        }
    }

    private void showTip(String errString) {
        Toast.makeText(this, errString, Toast.LENGTH_LONG).show();
    }

    private void sendMsg(int whatId, Object objs) {
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
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }

    private Reciver reciver;
    class Reciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("退出应用程序")) {
                unregisterReceiver(reciver);
                exit();
                finish();
            }
        }
    }

}
