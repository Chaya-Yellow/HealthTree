package com.jks.Spo2MonitorEx.app.Activity.family;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.CheckNet;
import com.jks.Spo2MonitorEx.util.TitlebarUtil;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.adapter.FamilyAdapter;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.dialog.CustomDialog;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.json.FamilyMemBean;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
import com.jks.Spo2MonitorEx.util.photo.PicUtils;
import com.jks.Spo2MonitorEx.util.thread.MyHandlerUtil;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
 * Created by apple on 16/8/30.
 */

/**
 * 家庭成员界面
 */
public class FamilyMainActivity extends MyActivity {

    private ListView familyListView = null;
    private PullToRefreshListView mPullToRefreshListView;
    //创建多个成员的按钮
    private LinearLayout addFamily;
    //创建第一个成员的按钮
    private Button adduser;
    private Dialog confirmDialog;
    private TextView ibRight;

    private List<Family> familys;
    private FamilyAdapter adapter;
    private Handler userHandler;

    //用户列表
    private View viewUser;
    //用户列表为空的显示视图
    private View viewUserNull;

    //是否编辑家庭成员 true: 编辑 false: 选中
    private boolean isShowSelectOrRead = false;
    private boolean isUserNull = true;
    private static final int SERVER_SUCCESS = 0;

    //成员位置
    private int position;//从0开始
    //成员id
    private int familyId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_family_show_user);
        config = (Config) getApplicationContext();
        init();
        initView();
        loadingView();
        initTitleBar();
        initHandler();
        familyListView.setSelection(SharedPreferencesUtil.getFamilyPosition(context) + 1);
    }

    @Override
    protected void init() {
        super.init();
        familys = config.getFamilys();
        if (familys != null && familys.size() != 0) {
            isUserNull = false;
            adapter = new FamilyAdapter(this, familys, false, isShowSelectOrRead);
        } else {
            isUserNull = true;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_user_management);
//        mPullToRefreshListView.
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getResources().getString(R.string.pull_to_refresh_laster) + " " + label);

                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        familyListView = mPullToRefreshListView.getRefreshableView();
        addFamily = (LinearLayout) findViewById(R.id.bottom_add_family);
        addFamily.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startToAddOrUpdateUser("add", 1);
            }
        });

        adduser = (Button) findViewById(R.id.more_ib_add_user);
        adduser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startToAddOrUpdateUser("add", 1);
            }
        });
        //下拉刷新
//        familyListView.setonRefreshListener(new OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                new GetDataTask().execute();
//            }
//        });
    }

    // 绘画listviwe视图
    protected void loadingView() {
        adapter = new FamilyAdapter(this, familys, false, isShowSelectOrRead);

        familyListView.setAdapter(adapter);
        // 长按删除
        familyListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                familyListView.onRefreshComplete();
                mPullToRefreshListView.onRefreshComplete();
                if ((position - 1) < familys.size()) {
//                    familyListView.isRefreshable = false;
                    FamilyMainActivity.this.position = position - 1;
                    showDialog();
                }
                return true;
            }
        });

        // 单击
        familyListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!isShowSelectOrRead) {
                    if (confirmDialog != null) {
                        if (confirmDialog.isShowing()) {
                            return;
                        }
                    }
                    Intent intent1 = getIntent();
                    int activityString = intent1.getIntExtra("Activity", 0);
                    // 从ServiceMainActivity跳转过来
                    if (activityString == 1) {

                    } else {
                        // 从FirstMainActivity跳转过来
                        if ((position - 1) < familys.size()) {
                            SharedPreferencesUtil.setFamilyPosition(context, position - 1);
                            finish();
                        }
                    }
                } else {
                    //编辑更新成员
                    if ((position - 1) < familys.size()) {
                        startToAddOrUpdateUser("position", position - 1);
                    }
                }

            }
        });

        // 判断显示那个视图
        viewUser = findViewById(R.id.first_include_user);
        viewUserNull = findViewById(R.id.first_include_user_null);
        if (isUserNull) {
            viewUser.setVisibility(View.GONE);
            viewUserNull.setVisibility(View.VISIBLE);
        } else {
            viewUser.setVisibility(View.VISIBLE);
            viewUserNull.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        // 设置标题
        TitlebarUtil.showTitleName(this, R.string.first_family_title_name);
        ImageButton ibLeft = TitlebarUtil.showIbLeft(this, R.drawable.titlebar_btn_back_sl);
        ibRight = TitlebarUtil.showTvRight(this, R.string.first_family_title_right_tv_edit);
        ibLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isShowSelectOrRead) {
                    ibRight.setText(getString(R.string.first_family_title_right_tv_carry_out));
                    isShowSelectOrRead = true;
                    adapter = new FamilyAdapter((Activity) context, familys, false, isShowSelectOrRead);
                    familyListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    addFamily.setVisibility(View.GONE);
                } else {
                    ibRight.setText(getString(R.string.first_family_title_right_tv_edit));
                    isShowSelectOrRead = false;
                    adapter = new FamilyAdapter((Activity) context, familys, false, isShowSelectOrRead);
                    familyListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    addFamily.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initHandler() {
        userHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Config.CHANGE_FAMILY:
                        init();
                        loadingView();
                        //获取网络家庭数据完成后停止刷新
                        familyListView.setEnabled(true);
//                        familyListView.onRefreshComplete();
                        mPullToRefreshListView.onRefreshComplete();
                        break;
                    case Config.CHANGE_FAMILY_FAIL:
                        //刷新失败
                        familyListView.setEnabled(true);
//                        familyListView.onRefreshComplete();
                        mPullToRefreshListView.onRefreshComplete();
                        String errMsg = (String) msg.obj;
                        if (!errMsg.equals(getResources().getString(R.string.json_checknet_nettimeout))) {
                            showTip(errMsg);
                        }
                        break;
                    case SERVER_SUCCESS:
                        DialogUtil.dismiss2Msg(R.string.first_family_delete_user_suceed);
                        init();
                        loadingView();
                        break;
                    default:
                        DialogUtil.dismiss2Msg(R.string.first_family_delete_user_fail);
                        showTip(ReErrorCode.getErrodType(context, msg.what));
                        break;
                }
            }
        };
        config.setFamilyHandler(userHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // 弹出删除dialong
    private void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_delete));
        builder.setPositiveButton(getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //
                        confirmDialog.dismiss();
                        if (CheckNet.isWifi(FamilyMainActivity.this)) {
                            DialogUtil.show(FamilyMainActivity.this, R.string.first_family_deleteing_user);
                            familyId = familys.get(position).getFamilyId();
                            delete();

                        } else {
                            Toast.makeText(FamilyMainActivity.this, R.string.checknet_delete_user, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton(getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        confirmDialog.dismiss();
                    }
                });
        confirmDialog = builder.create();
        confirmDialog.show();
        confirmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                familyListView.isRefreshable = true;
            }
        });
    }

    private void delete() {
        try {
            MyThread.startNewThread(new Runnable() {
                @Override
                public void run() {
                    RequestParams params = new RequestParams();
                    FamilyMemBean familyData = new FamilyMemBean();
                    familyData.setKey("DeleteFamMem");
                    familyData.setMemberId(familyId);
                    familyData.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                    familyData.setAccountId(SharedPreferencesUtil.getMemberId(context));
                    String familyDataJson = new JsonUtils<FamilyMemBean>().getJsonString(familyData);
                    try {
                        params.setBodyEntity(new StringEntity(familyDataJson));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
                            if (errorCode == 0) {
                                FamilyIfc ifc = new FamilyIfcImpl(FamilyMainActivity.this);

                                if (SharedPreferencesUtil.getFamilyPosition(context) == position) {
                                    SharedPreferencesUtil.setFamilyPosition(context, 0);//如果是删除当前在测量的用户, 则将第一个用户设置为选中的用户
                                } else if (SharedPreferencesUtil.getFamilyPosition(context) > position) {
                                    SharedPreferencesUtil.setFamilyPosition(context, SharedPreferencesUtil.getFamilyPosition(context) - 1);
                                }

                                //删除数据库中成员
                                ifc.deleteByID(familys.get(position).getId());
                                userHandler.sendEmptyMessage(SERVER_SUCCESS);//更新列表
                                PicUtils.deletePhotoAva(familys.get(position).getAvatar());//将本地头像删除
                                //同步网络家庭数据
                                config.changeFamily2();
                            }
                            userHandler.sendEmptyMessage(errorCode);
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            DialogUtil.dismiss2Msg(R.string.first_family_add_tip_no_submited);
                            showTip(getResources().getString(R.string.json_checknet_nettimeout));
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showTip(String errString) {
        Toast.makeText(this, errString, Toast.LENGTH_LONG).show();
    }

    /**
     * 跳转去AddOrUpdateUserActivity
     * @param extra
     */
    private void startToAddOrUpdateUser(String extra, int value) {
        Intent intent = new Intent(FamilyMainActivity.this, AddOrUpdateUserActivity.class);
        intent.putExtra(extra,  value);
        startActivity(intent);
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
    }

    @SuppressLint("NewApi")
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            familyListView.setEnabled(false);
            if (CheckNet.isWifi(context)) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        MyHandlerUtil.sendMsg(Config.CHANGE_FAMILY, config.getMainHandler2(), null);
                    }
                }, 1000);
            } else {
                Toast.makeText(context, R.string.checknet_false, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
//                        familyListView.setEnabled(true);
//                        familyListView.onRefreshComplete();
                        mPullToRefreshListView.onRefreshComplete();
                    }
                }, 1000);
            }
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    familyListView.setEnabled(true);
//                    familyListView.onRefreshComplete();
//                }
//            }, 3000);
            super.onPostExecute(result);
        }
    }
}
