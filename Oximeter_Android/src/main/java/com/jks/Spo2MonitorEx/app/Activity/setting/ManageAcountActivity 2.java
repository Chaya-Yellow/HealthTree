package com.jks.Spo2MonitorEx.app.Activity.setting;

/**
 * Created by apple on 2016/10/5.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.HistoryActivity;
import com.jks.Spo2MonitorEx.app.Activity.user.UserLoginActivity;
import com.jks.Spo2MonitorEx.app.MainActivity;
import com.jks.Spo2MonitorEx.util.LoadingAva;
import com.jks.Spo2MonitorEx.util.TitlebarUtil;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.adapter.AccountAdapter;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.AccountIfcImpl;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;
import com.jks.Spo2MonitorEx.util.entity.json.LoginBean;
import com.jks.Spo2MonitorEx.util.entity.json.ResponseObject;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
import com.jks.Spo2MonitorEx.util.thread.MyHandlerUtil;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 用户账号管理
 */
public class ManageAcountActivity extends MyActivity {
    public final static int SYNCFAMILYICON = 1371;//在账号控制界面中登录

    private SwipeMenuListView mAccountListView;
    private LinearLayout addAccount;

    private List<LoginInfo> loginInfos;
    private AccountAdapter adapter;
    private Handler handler;

    private boolean isAccountNull = true;

    private int position;//从0开始

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        config = (Config) getApplicationContext();
//        init();
        initView();
        initTitleBar();
        initHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        loadingView();
    }

    @Override
    protected void init() {
        super.init();
        loginInfos = config.getLoginInfos();
//        loginInfos = test();
        for (LoginInfo info : loginInfos) {
            Log.e("现在保存的用户:", info.toString());
        }
        if (loginInfos != null || loginInfos.size() != 0) {
            isAccountNull = false;
        }else {
            isAccountNull = true;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mAccountListView = (SwipeMenuListView) findViewById(R.id.lv_account_management);
        addAccount = (LinearLayout) findViewById(R.id.bottom_add_acount);
        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageAcountActivity.this, AddAcountActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
            }
        });
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        TextView tvTitleName = (TextView) findViewById(R.id.tv_title);
        tvTitleName.setText(getResources().getString(R.string.account_management));
        tvTitleName.setTextSize(16);
        ImageButton ibLeft = TitlebarUtil.showIbLeft(this, R.drawable.titlebar_btn_back_sl);
        ibLeft.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        ibLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UserLoginActivity.ERRCODE_SUCEED:
                        //登录成功
                        init();
                        loadingView();
                        DialogUtil.dismiss2Msg(R.string.user_login_suceed);
                        MyHandlerUtil.sendMsg(MainActivity.LOGINWITHACCOUNTMANAGE, config.getMainHandler2(), null);
                        break;
                    case UserLoginActivity.ERRCODE_NO_RETRUN_ERROR:
                        //返回json数据解析错误
                        DialogUtil.dismiss2Msg(R.string.user_login_failure);
                        showTip(ReErrorCode.getErrodType(context, ReErrorCode.ERRCODE_401));
                        break;
                    case UserLoginActivity.ERRCODE_NETCONNECT_TIMEOUT:
                        //网络连接错误
                        DialogUtil.dismiss2Msg(R.string.user_login_failure);
                        showTip(getResources().getString(R.string.json_checknet_nettimeout));
                        break;
                    case SYNCFAMILYICON:
                        init();
                        loadingView();
                        break;
                    default:
                        //登录请求错误(密码, 账号不正确之类的错误)
                        DialogUtil.dismiss2Msg(R.string.user_login_failure);
                        showTip(ReErrorCode.getErrodType(context, msg.what));
                        break;
                }
            }
        };
        config.setManageHandler(handler);
    }

    private void loadingView() {
        super.loadView();
        adapter = new AccountAdapter(this, loginInfos, false, false);
        mAccountListView.setAdapter(adapter);

        //创建侧滑删除按钮
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // 将侧滑按钮添加到cell中
        mAccountListView.setMenuCreator(creator);

        mAccountListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        ManageAcountActivity.this.position = position;
                        deleteAccount(position);
                        break;
                }
                return false;
            }
        });

        mAccountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != SharedPreferencesUtil.getAccountPosition(context)) {

                    login(loginInfos.get(position).getAccount(), loginInfos.get(position).getPassword(), position, false);
                } else {
                    exitOtherActivity();
                    Intent intent = new Intent(ManageAcountActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
                    finish();
                }
            }
        });
    }

    private void deleteAccount(int position) {
        AccountIfc ifc = new AccountIfcImpl(ManageAcountActivity.this);

        if (SharedPreferencesUtil.getAccountPosition(context) == position) {
//            SharedPreferencesUtil.setAccountPosition(context, 0);//如果是删除当前在测量的用户, 则将第一个用户设置为选中的用户
            if (loginInfos.size() == 1) {
                ifc.deleteByID(loginInfos.get(position).getId());
                clearLastUserDataAndBLE();
                Intent intent = new Intent(ManageAcountActivity.this, UserLoginActivity.class);
                intent.putExtra("email_logout", loginInfos.get(0).getAccount());
                loginInfos.clear();
                startActivity(intent);
                overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
            }else if (position == 0) {
                login(loginInfos.get(position + 1).getAccount(), loginInfos.get(position + 1).getPassword(), position, true);
                return;
            } else {
                login(loginInfos.get(0).getAccount(), loginInfos.get(0).getPassword(), position, true);
                return;
            }
        } else if (SharedPreferencesUtil.getAccountPosition(context) > position) {
            ifc.deleteByID(loginInfos.get(position).getId());
            SharedPreferencesUtil.setAccountPosition(context, SharedPreferencesUtil.getAccountPosition(context) - 1);
        }else {
            ifc.deleteByID(loginInfos.get(position).getId());
        }
        init();
        loadingView();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void login(final String userEmail, final String passWord, final int position, final boolean isDelete) {
        DialogUtil.show(this, R.string.logining);
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                final LoginBean loginData = new LoginBean();
                loginData.setKey("Login");
                loginData.setAccount(userEmail);
                loginData.setPwd(passWord);
                final String regDataJson = new JsonUtils<LoginBean>().getJsonString(loginData);
                Log.e("登录", regDataJson);
                try {
                    params.setBodyEntity(new StringEntity(regDataJson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        JSONObject resJson;
                        ResponseObject<String> object = new ResponseObject<String>();
                        try {
                            resJson = new JSONObject(responseInfo.result);
                            //获取登录错误码
                            object.setError_code(resJson.getInt("error_code"));

                            if (object.getError_code() == UserLoginActivity.ERRCODE_SUCEED) {
                                clearLastUserData();
                                //登录成功
                                ResponseObject<LoginBean> loginObject = new Gson().fromJson(responseInfo.result, new TypeToken<ResponseObject<LoginBean>>() {
                                }.getType());
                                SharedPreferencesUtil.setCilenKey(context, loginObject.getData().getClientKey());
                                SharedPreferencesUtil.setMemberId(context, loginObject.getData().getAccountId());
                                SharedPreferencesUtil.setUserName(context, userEmail);
                                config.setUserName(userEmail);//用户的邮箱当做用户名
                                config.setMemberId(loginObject.getData().getAccountId());
                                config.setClientKey(loginObject.getData().getClientKey());

                                AccountIfc ifc = new AccountIfcImpl(context);
                                List<LoginInfo> loginInfos = ifc.findAll();
                                LoginInfo info = null;
                                if (isDelete) {
                                    if (position == 0) {
                                        info = loginInfos.get(position + 1);
                                    }else {
                                        info = loginInfos.get(0);
                                    }
                                }else {
                                    info = loginInfos.get(position);
                                }
                                info.setAccount(userEmail);
                                info.setPassword(passWord);
                                info.setAccountId(loginObject.getData().getAccountId());
                                info.setClientKey(loginObject.getData().getClientKey());
                                ifc.update(info);

                                if (isDelete) {
                                    SharedPreferencesUtil.setAccountPosition(context, 0);//如果是删除当前在测量的用户, 则将第一个用户设置为选中的用户
                                    ifc.deleteByID(loginInfos.get(position).getId());
                                }else {
                                    SharedPreferencesUtil.setAccountPosition(context, position);//设置登录用户位置
                                }

                                sendMsg(loginObject.getError_code());
                            } else {
                                Log.e("登录错误", object.getError_code() + "");
                                sendMsg(object.getError_code());
                            }
                        } catch (JSONException e) {
                            sendMsg(UserLoginActivity.ERRCODE_NO_RETRUN_ERROR);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        sendMsg(UserLoginActivity.ERRCODE_NETCONNECT_TIMEOUT);
                    }
                });
            }
        });
    }

    public void clearLastUserData() {
        SharedPreferencesUtil.clearSp(this);// 先清空偏好
        HistoryActivity.selectDate = null;
        // 清除数据库内容
        clearTable();
        // 清除头像。。避免内存外溢
        LoadingAva.clearAvas();
        // 清空Config的内容避免对下一个用户照成影响
        config.cleanData();
    }

    public void clearLastUserDataAndBLE() {
//        DialogUtil.show(this, R.string.more_exiting);
        SharedPreferencesUtil.clearSp(this);// 先清空偏好
        Message msg = new Message();
        msg.what = MainActivity.CLEARBTE;
        config.getMainHandler2().sendMessage(msg);
        HistoryActivity.selectDate = null;
        // 清除数据库内容
        clearTable();
        // 清除头像。。避免内存外溢
        LoadingAva.clearAvas();
        // 清空Config的内容避免对下一个用户照成影响
        config.cleanData();
    }

    private void clearTable() {
        FamilyIfc familyIfc = new FamilyIfcImpl(this);
        familyIfc.deleteTable();
        SharedPreferencesUtil.setTime(context, "0");
    }

    private void showTip(String errString) {
        Toast.makeText(this, errString, Toast.LENGTH_LONG).show();
    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }

    @Override
    public void finish() {
        super.finish();
        config.setManageHandler(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        config.setManageHandler(null);
    }
}
