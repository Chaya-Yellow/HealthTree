package com.jks.Spo2MonitorEx.app;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.HistoryActivity;
import com.jks.Spo2MonitorEx.app.Activity.RemindActivity;
import com.jks.Spo2MonitorEx.app.Activity.family.FamilyMainActivity;
import com.jks.Spo2MonitorEx.app.Bluetoothle.BLEDevice;
import com.jks.Spo2MonitorEx.app.Bluetoothle.BluetoothLEService;
import com.jks.Spo2MonitorEx.app.CastomView.SPO2WaveView;
import com.jks.Spo2MonitorEx.app.Class.HistoryData;
import com.jks.Spo2MonitorEx.app.Class.HistoryDataLib;
import com.jks.Spo2MonitorEx.app.Class.SleepBlock;
import com.jks.Spo2MonitorEx.app.Fragment.HistoryChartFragment;
import com.jks.Spo2MonitorEx.util.LoadingAva;
import com.jks.Spo2MonitorEx.util.SnackbarUtil;
import com.jks.Spo2MonitorEx.util.TextUtil;
import com.jks.Spo2MonitorEx.util.VersionManager;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.adapter.DateWeekGridViewAdapter;
import com.jks.Spo2MonitorEx.util.autoupdate.XdConstants;
import com.jks.Spo2MonitorEx.util.autoupdate.XdUpdateAgent;
import com.jks.Spo2MonitorEx.util.autoupdate.XdUpdateBean;
import com.jks.Spo2MonitorEx.util.autoupdate.XdUpdateService;
import com.jks.Spo2MonitorEx.util.autoupdate.XdUpdateUtils;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.AccountIfc;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.OximetIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.AccountIfcImpl;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.dao.impl.OximetIfcImpl;
import com.jks.Spo2MonitorEx.util.data.DataCheckUtil;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.data.StringUtil;
import com.jks.Spo2MonitorEx.util.data.UnitUtils;
import com.jks.Spo2MonitorEx.util.dbhelper.DBHelper;
import com.jks.Spo2MonitorEx.util.dialog.AlertIOSDialog;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximetTamp;
import com.jks.Spo2MonitorEx.util.entity.PublishPlatform;
import com.jks.Spo2MonitorEx.util.entity.json.FamilyMemBean;
import com.jks.Spo2MonitorEx.util.entity.json.FamilyResp;
import com.jks.Spo2MonitorEx.util.entity.json.OximetResp;
import com.jks.Spo2MonitorEx.util.entity.json.ResponseObject;
import com.jks.Spo2MonitorEx.util.entity.json.SyncDataBean;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
import com.jks.Spo2MonitorEx.util.json.SyncUtils;
import com.jks.Spo2MonitorEx.util.other.BLTToast;
import com.jks.Spo2MonitorEx.util.other.GetAgeByBirthday;
import com.jks.Spo2MonitorEx.util.photo.BitmapUtil;
import com.jks.Spo2MonitorEx.util.photo.PicUtils;
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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import static android.content.IntentFilter.SYSTEM_HIGH_PRIORITY;
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
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.OPEN;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_H_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_L_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_L_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_H_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_L_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_L_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_H_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_L_FLAG_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_L_VALUE_DEFAULT;

public class MainActivity extends MyActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Family family;
    private String userEmail;
    private String clientKey;
    private int memberId;
    //private FamilyIfc familyIfc;
    private final static String TAG = MainActivity.class.getSimpleName();

    // UUID
    private final static String UUID_BLUETOOTH_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private final static String UUID_BLUETOOTH_CHARACTERISTIC = "0000ffe1-0000-1000-8000-00805f9b34fb";

    private final static int HANDLER_BLUETOOTH_IS_OFF = 0;
    private final static int HANDLER_SHOW_DEVICE_LIST = 1;
    private final static int HANDLER_NOTIFICATION_CHANGE = 2;
    private final static int HANDLER_CONNECT_BLE_DEVICE = 3;
    private final static int HANDLER_REFRESH_PIVIEW = 4;
    private final static int HANDLER_BLUETOOTH_IS_ON = 5;
    public static final int CHANGEFAMILYSUCCESS = 20;//同步家庭成员的测量数据
    public static final int CHANGEFAMILYFAIL = 21;//同步家庭成员失败
    private static final int CHANGEFAMILY = 17;//更新正在测量的用户
    public static final int CHANGETEMPVALUE = 18;//通知HistoryActivuty更新日期
    public static final int SENDOXIVALUE = 1372;//通知有新的测量数据
    public final static int CLEARBTE = 1114;//退出登陆清除蓝牙
    public final static int LOGINWITHACCOUNTMANAGE = 1379;//在账号控制界面中登录

    private TextView SpO2ValueTextView;
    private TextView HeartRateValueTextView;
    private TextView connectionStateTextView;
    //private TextView respValueTextView;
    private TextView PIValueTextView;
    //private ProgressBar mProgressBar;
    private ImageButton mHistoryButton;
    private ImageButton mSettingsButton;
    // 根据所提供图片修改，将此控件注释
    // private ImageButton mSleepButton;
    private ImageView connectedImage;
    //private ImageView batteryImage;
    //private ImageView nextArrawImage;
    private FrameLayout deviceInfoFrameLayout;
    private FrameLayout usersInfoFrameLayout;
    private TextView familyName, deviceStateText, ageText, heightText, weightText, deviceStateText2;//用户信息的组件
    private ImageView userImage;
    private ImageView aiv_ref;
    private RelativeLayout rl_dev;
    private View mRootView;

    //private PIView mPIView;
    private SPO2WaveView mSPO2WaveView;

    private BluetoothLEService mBluetoothLEService;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattCharacteristic mCharacteristic;

    private Handler mHandler;
    private Timer BLETimer;
    private Timer dialogTimer;
    private Timer PITimer;
    private TimerTask BLETimerTask;
    private TimerTask dialogTimerTask;
    private TimerTask PITimerTask;
    public static Timer syncDataTimer = new Timer();//同步服务器中的oxi数据

    private List<BLEDevice> deviceList;
    private BLEDevice currentDevice;
    private BaseAdapter deviceListAdapter;

    private Vector<Integer> SPO2WaveValues;
    private Vector<Integer> PIValues;

    private List<HistoryData> mHistoryDatas;
    private volatile static List<Oximet> oximetsTamp = new ArrayList<Oximet>();//用于采集oximet数据的数组, 30秒清空一次, 采集最后一个数
    private static Timer collectTimer = new Timer();//采集数据的定时器, 连接到蓝牙的时候开启
    private int historyCounter = 0;

    private List<SleepBlock> sleepDatas;

    private Dialog dialog;
    private List<Family> familys = new ArrayList<Family>();//所有家庭成员
    private int familyPosition = -1;//当前测量用户的位置 0开始
    private BitmapDrawable[] bitmaps;//用户头像

    private boolean isSupportBluetooth = true;    // 手机是否支持蓝牙BLE
    private boolean isBluetoothON = false;  // 手机是否打开蓝牙
    private boolean isScanning = false;   // 是否在扫描设备中
    private boolean isDeviceListDialogShowing = false;  // 是否显示选择列表中
    private boolean isConnecting = false; // 是否在连接中
    private boolean isRegisterBroadcastReceiver = false; // 是否注册了广播
    private boolean isActivityFront = true;  // Activity是否可见
    private boolean isFirstReceive = true;  // 是否第一次收到广播数据
    private boolean isHasPermissions = false;
    public static boolean synchronousFamilyDataFlage = false;//是否在同步家庭数据
    private String receivedData = "";
    private long exitTime = 0;// 退出时间
    private int frequency = 0;// 当同步家庭人员失败后, 继续同, 若四次同步失败, 那就不再同步了
    private FamilyIfc familyIfc;
    private OximetIfc oximetIfc;
    private static Config config;
    private XdUpdateAgent updateAgent;//更新通知

    MediaPlayer mPlayer;

    private AlertDialog mPermissionDialog;
    private boolean mCheckPermissionWhenResume = false;

    // Service生命进程
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLEService = ((BluetoothLEService.LocalBinder) service).getService();
            connectBleDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLEService = null;
        }
    };

    String getBytes(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = s.getBytes("unicode");
            for (int i = 2; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str);
                out.append(str1);
            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 第四步扫描回调中中去获取设备
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int rsi, final byte[] scanRecord) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //String deviceName = "BLT_M70C";
                    String deviceName = "OXIMETER";
                    if (bluetoothDevice.getName() != null && bluetoothDevice.getName().equals(deviceName)) {

                        int type = scanRecord[12];
                        String typeS = "";
                        Log.d(TAG, "type is " + type);

                        String hexString = StringUtil.bytesToHexString(scanRecord);
                        String sn = "";
                        if (hexString != null && hexString.length() > (18 + 32)) {
                            String hexStr = hexString.substring(18, 18 + 32);
                            sn = DataCheckUtil.resolveBleMsg(hexStr);
                        }
                        boolean hasSameAddress = false;
                        for (BLEDevice device : deviceList) {
                            if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                                hasSameAddress = true;
                                break;
                            }
                        }
                        if (!hasSameAddress) {
                            if (type == 48) {
                                typeS = "M70C";
                            } else if (type == 1) {
                                typeS = "WT1";
                            }
                            // todo 序列号长度修改在这里
                            deviceList.add(new BLEDevice(bluetoothDevice.getName(), sn, bluetoothDevice.getAddress(), typeS));
                            Log.d(TAG, "add device");
                            deviceListAdapter.notifyDataSetChanged();
                            Message message = new Message();
                            message.what = HANDLER_NOTIFICATION_CHANGE;
                            mHandler.sendMessage(message);
                        }
                    }
                }
            });
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                try {
                    displayGattServices(mBluetoothLEService.getSupportedGattService());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (BluetoothLEService.ACTION_GATT_CONNECTED.equals(action)) {
                connectionStateTextView.setText(R.string.device_connected);
                //mProgressBar.setVisibility(View.GONE);
                connectedImage.setVisibility(View.VISIBLE);
                //batteryImage.setVisibility(View.VISIBLE);
                //nextArrawImage.setVisibility(View.VISIBLE);
                //if (mPIView.getVisibility() == View.GONE) {
                //mPIView.setVisibility(View.VISIBLE);
                //}
                //mPIView.setVisibility(View.VISIBLE);
                //mPIView.setPIValue(0);
                //deviceInfoFrameLayout.setEnabled(true);
                if (!mSPO2WaveView.getDrawing() && isActivityFront) {
                    mSPO2WaveView.startDraw();
                }
                isFirstReceive = true;

                PIValues = new Vector<>();
                mSPO2WaveView.setPIValues(PIValues);

                /*PITimer = new Timer();
                PITimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (PIValues.size() > 3) {
                            mPIView.setPIValue(PIValues.get(0));
                            Message message = new Message();
                            message.what = HANDLER_REFRESH_PIVIEW;
                            mHandler.sendMessage(message);
                        }
                    }
                };

                PITimer.scheduleAtFixedRate(PITimerTask, 1000,50);*/

                setCollectDataTimer();
            } else if (BluetoothLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnecting = false;
                mSPO2WaveView.stopDraw();
                unbindBleService();
                deviceList = new ArrayList<BLEDevice>();
                connectionStateTextView.setText(getResources().getString(R.string.scaning_device));
                TextUtil.setTextDotJumping(connectionStateTextView);
                connectedImage.setVisibility(View.GONE);
                //batteryImage.setVisibility(View.GONE);
                //nextArrawImage.setVisibility(View.GONE);
                //mPIView.setVisibility(View.GONE);
                //mProgressBar.setVisibility(View.VISIBLE);
                //mPIView.setPIValue(0);
                //deviceInfoFrameLayout.setEnabled(false);
                Message message = new Message();
                message.what = HANDLER_REFRESH_PIVIEW;
                mHandler.sendMessage(message);
                SpO2ValueTextView.setText("--");
                //respValueTextView.setText("--");
                PIValueTextView.setText("-.-");
                HeartRateValueTextView.setText("---");
//                dialogTimer = new Timer();
//                initDialogTimer();
//                dialogTimer.schedule(dialogTimerTask, 5000, 5000);
                //scanDevice(true);
                if (collectTimer != null) {
                    collectTimer.cancel();
                }
                //if (PITimer != null) {
                //PITimer.cancel();
                //}
            } else if (BluetoothLEService.ACTION_FAIL_TO_CONNECTED.equals(action)) {
                isConnecting = false;
                unbindBleService();
                deviceList = new ArrayList<BLEDevice>();
                connectionStateTextView.setText(getResources().getString(R.string.scaning_device));
                TextUtil.setTextDotJumping(connectionStateTextView);
                connectedImage.setVisibility(View.GONE);
                //batteryImage.setVisibility(View.GONE);
                //nextArrawImage.setVisibility(View.GONE);
                //mProgressBar.setVisibility(View.VISIBLE);
                //deviceInfoFrameLayout.setEnabled(false);
//                dialogTimer = new Timer();
//                initDialogTimer();
//                dialogTimer.schedule(dialogTimerTask, 5000, 5000);
                //scanDevice(true);
            } else if (BluetoothLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {
                    String rawData = intent.getStringExtra(BluetoothLEService.EXTRA_DATA);
                    Log.e(TAG, rawData);

                    String head = rawData.substring(0, 2);
                    String data = rawData;
                    //处理AAAA
                    if (rawData.contains("AAAA")) {
                        Log.w("TAG", "AAAAAAAAAAAAAAA shock!");
                        data = rawData.replaceAll("AAAA", "AA");
                    }
                    // if (head.equals("AA") & data.length() > 4) {
                    if (head.equals("FF") & data.length() > 4) {
                        String dataID = data.substring(4, 6);
                        // if (dataID.equals("41")) {
                        if (dataID.equals("01")) {
                            Log.e("TAG", "received data values is : " + receivedData);
                            historyCounter++;
                            if (historyCounter == 10) {
                                historyCounter = 0;
                            }
                            if (!isFirstReceive) {
                                //Log.e("spo2  valuue", "" + receivedData.substring(72, 74));
                                Log.e("spo2 value", "" + receivedData.substring(8, 10));
                                //int SPO2Value = Integer.parseInt(receivedData.substring(72, 74), 16) & 0x7F;//就是为了高位清零
                                int SPO2Value = Integer.parseInt(receivedData.substring(8, 10), 16) & 0x7F;
                                if (SPO2Value == 127) {//如果等于127那就说明超出了范围了
                                    SpO2ValueTextView.setText(" -- ");
                                } else {
                                    SpO2ValueTextView.setText("" + SPO2Value);
                                }
                                if (OPEN.equals(SharedPreferencesUtil.getStringByKey(MainActivity.this, SP_H_FLAG, SP_H_FLAG_DEFAULT))) {
                                    int spv = Integer.parseInt(SharedPreferencesUtil.getStringByKey(MainActivity.this, SP_H_VALUE, SP_H_VALUE_DEFAULT));
                                    if (SPO2Value > spv && SPO2Value != 127) {
                                        mPlayer.start();
                                    }
                                }
                                if (OPEN.equals(SharedPreferencesUtil.getStringByKey(MainActivity.this, SP_L_FLAG, SP_L_FLAG_DEFAULT))) {
                                    int splv = Integer.parseInt(SharedPreferencesUtil.getStringByKey(MainActivity.this, SP_L_VALUE, SP_L_VALUE_DEFAULT));
                                    if (SPO2Value < splv && SPO2Value > 0) {
                                        mPlayer.start();
                                    }
                                }


                                // display heart rate value
                                //int heartRateValue = Integer.parseInt(receivedData.substring(74, 76), 16);
                                // ((13&0x7F)+((24&0x10)<<3))
                                int heartRateValue = (Integer.parseInt(receivedData.substring(10, 12), 16) & 0x7F) +
                                        ((Integer.parseInt(receivedData.substring(14, 16), 16) & 0x10) << 3);
                                //int heartRateValue = Integer.parseInt(receivedData.substring(10, 12), 16);

                                if (heartRateValue == 255) {
                                    HeartRateValueTextView.setText("---");
                                } else {
                                    HeartRateValueTextView.setText(String.valueOf(heartRateValue));
                                }
                                if (OPEN.equals(SharedPreferencesUtil.getStringByKey(MainActivity.this, PR_H_FLAG, PR_H_FLAG_DEFAULT))) {
                                    int prv = Integer.parseInt(SharedPreferencesUtil.getStringByKey(MainActivity.this, PR_H_VALUE, PR_H_VALUE_DEFAULT));
                                    if (heartRateValue > prv && heartRateValue != 255) {
                                        mPlayer.start();
                                    }
                                }

                                if (OPEN.equals(SharedPreferencesUtil.getStringByKey(MainActivity.this, PR_L_FLAG, PR_L_FLAG_DEFAULT))) {
                                    int prlv = Integer.parseInt(SharedPreferencesUtil.getStringByKey(MainActivity.this, PR_L_VALUE, PR_L_VALUE_DEFAULT));
                                    if (heartRateValue < prlv && heartRateValue > 0) {
                                        mPlayer.start();
                                    }
                                }


                                // display PI view // TODO: 15/11/3  set PI value

                                for (int i = 0; i < 30; i++) {
                                    //int PIValue = Integer.parseInt(receivedData.substring(16 + i * 2, 18 + i * 2), 16);
                                    if (i != 1 || i != 7 || i != 13 || i != 19) {
                                        int PIValue = Integer.parseInt(receivedData.substring(76 + i * 2, 78 + i * 2), 16);
                                        PIValues.add(PIValue);
                                    }

                                }

                                Log.d("TAG", "PI Values number is : " + PIValues.size());
                                // TODO: 15/11/3
                                if (PIValues.size() > 150) {
                                }
                                // display PI value
                                //float PI = Integer.parseInt(receivedData.substring(70, 72), 16) / 10.0f;
                                //float PI = Integer.parseInt(receivedData.substring(12, 14), 16) / 10.0f;
                                // System.out.println((((30&0x7F) + (24&0x0F) * 128)));
                                //float PI = Integer.parseInt(receivedData.substring(12,14), 16) / 100.0F;
                                float PI = (((Integer.parseInt(receivedData.substring(12, 14), 16) & 0x7F)
                                        + (Integer.parseInt(receivedData.substring(14, 16), 16) & 0x0F) * 128)) / 100F;

                                if (PI == 25.5) {
                                    PIValueTextView.setText("-.-");
                                } else {
                                    PIValueTextView.setText("" + PI);
                                }
                                if (OPEN.equals(SharedPreferencesUtil.getStringByKey(MainActivity.this, PI_H_FLAG, PI_H_FLAG_DEFAULT))) {
                                    int piv = Integer.parseInt(SharedPreferencesUtil.getStringByKey(MainActivity.this, PI_H_VALUE, PI_H_VALUE_DEFAULT));
                                    if (PI > piv && PI != 25.5) {
                                        mPlayer.start();
                                    }
                                }
                                if (OPEN.equals(SharedPreferencesUtil.getStringByKey(MainActivity.this, PI_L_FLAG, PI_L_FLAG_DEFAULT))) {
                                    int pilv = Integer.parseInt(SharedPreferencesUtil.getStringByKey(MainActivity.this, PI_L_VALUE, PI_L_VALUE_DEFAULT));
                                    if (PI < pilv && PI > 0) {
                                        mPlayer.start();
                                    }
                                }


                                // display resp value
                                int respValue = Integer.parseInt(receivedData.substring(14, 16), 16);
                                /*if (respValue == 255) {
                                    respValueTextView.setText("--");
                                } else {
                                    respValueTextView.setText("" + respValue);
                                }*/

                                /* sleep value */
                                short[] r = new short[4];
                                r[0] = (short) ((short) (Short.parseShort(receivedData.substring(80, 82), 16) << 8)
                                        + Short.parseShort(receivedData.substring(78, 80), 16));

                                r[1] = (short) ((short) (Short.parseShort(receivedData.substring(84, 86), 16) << 8)
                                        + Short.parseShort(receivedData.substring(82, 84), 16));

                                r[2] = (short) ((short) (Short.parseShort(receivedData.substring(88, 90), 16) << 8)
                                        + Short.parseShort(receivedData.substring(86, 88), 16));

                                r[3] = (short) ((short) (Short.parseShort(receivedData.substring(92, 94), 16) << 8)
                                        + Short.parseShort(receivedData.substring(90, 92), 16));
                                long time = System.currentTimeMillis() / 1000;
                                sleepDatas.add(new SleepBlock(r, (short) SPO2Value, (short) heartRateValue, (short) respValue, time));
                                if (historyCounter == 0) {
                                    Date mDate = new Date(System.currentTimeMillis());
                                    SimpleDateFormat sdff = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                                    String mFormatedDate = sdff.format(mDate).substring(5, 19);
                                    Log.d("TAG", mFormatedDate);
                                    mHistoryDatas.add(0, new HistoryData(mFormatedDate, SPO2Value, heartRateValue, respValue));
                                    Oximet oximet = new Oximet();
                                    oximet.setSPO2(SPO2Value);
                                    oximet.setPR(heartRateValue);
                                    oximet.setRESP(respValue);
                                    oximetsTamp.add(oximet);
//                                    Message msg = new Message();
//                                    msg.what = MainActivity.SENDOXIVALUE;
//                                    msg.obj = oximet;
//                                    config.getMainHandler2().sendMessage(msg);//往主线程发送message
                                }
                                /* 保存至文件中 */
                                if (isSdCardExist()) {
                                    try {
                                        Date date = new Date(System.currentTimeMillis());
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                        String dateString = "Oximeter_data_" + format.format(date) + ".txt";
                                        Log.w("txt", dateString);
                                        File file = new File(getSdCardPath(),
                                                dateString);
                                        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                                        bw.write(String.valueOf(SPO2Value));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(heartRateValue));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(respValue));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(r[0]));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(r[1]));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(r[2]));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(r[3]));
                                        bw.write("\r\n");
                                        bw.write(String.valueOf(time));
                                        bw.write("\r\n");
                                        bw.flush();
                                        System.out.println("写入成功");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //String wave = receivedData.substring(94, receivedData.length() - 2);
                                //String wave = receivedData.substring(78, receivedData.length() - 2);
                                //for (int i = 0; i < wave.length(); i = i + 2) {
                                //String value = wave.substring(i, i + 2);
                                //int v = Integer.parseInt(value, 16) & 0x7F;
                                //SPO2WaveValues.add(v);
                                //}
//                                for (int i = 0; i < 26; i++) {
////                                    String value = receivedData.substring(84 + i * 2, 86 + i * 2);
////                                    int v = Integer.parseInt(value, 16) & 0x7F;
////                                    Log.d("receiveValue", "receive number is : " + v);
//                                    if (i % 6 != 0 && i != 1) {
//                                        String value = receivedData.substring(84 + i * 2, 86 + i * 2);
//                                        int v = Integer.parseInt(value, 16) & 0x7F;
//                                        SPO2WaveValues.add(v);
//                                    }
//                                }
                                for (int i = 1; i < 26; i++) {
                                    String value = receivedData.substring(84 + i * 2, 86 + i * 2);
                                    int v = Integer.parseInt(value, 16) & 0x7F;
                                    SPO2WaveValues.add(v);
                                }
                                Log.d("TAG", "Values number is : " + SPO2WaveValues.size());
                                receivedData = data;
                            }
                            receivedData = data;
                            isFirstReceive = false;
                        } else if (dataID.equals("43")) {
                            // deal with battery
                            Log.d(TAG, "battery state is :" + rawData.substring(6, 8));
//                            switch (data.substring(6, 8)) {
//                                case "00":
//                                    batteryImage.setImageResource(R.drawable.ic_battery_alert_white_36dp);
//                                    break;
//                                case "01":
//                                    batteryImage.setImageResource(R.drawable.ic_battery_30_white_36dp);
//                                    break;
//                                case "02":
//                                    batteryImage.setImageResource(R.drawable.ic_battery_80_white_36dp);
//                                    break;
//                                case "03":
//                                    batteryImage.setImageResource(R.drawable.ic_battery_full_white_36dp);
//                                    break;
//                                default:
//                                    break;
//                            }
                            if (currentDevice.getMainVersion().equals("")) {
                                String main = data.substring(8, 9) + "." + data.substring(9, 10);
                                String sub = data.substring(10, 11) + "." + data.substring(11, 12);
                                currentDevice.setMainVersion(main);
                                currentDevice.setSubVersion(sub);
                            }

                        }

                    } else {

                        receivedData = receivedData + data;
                        Log.e(TAG, "to see the final receivedData" + receivedData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("main", "mainactivity creat");
        setContentView(R.layout.activity_main);
        setLastAppVersionCode();
        init();
        initHandler();
        initView();
        startBleService();
        setTimer();
        getBLEPermissions();
        //addOrUpdateFamilytest();
////去掉wifi同步过程
//        if (CheckNet.isWifi(context)) {
//            //在WiFi条件下对数据进行同步
//            synchronousFamily(CHANGEFAMILYSUCCESS);
//        } else {
//            Toast.makeText(context, R.string.checknet_false, Toast.LENGTH_LONG).show();
//        }

        //updateNotify();
    }

    private void startBleService() {
        Intent bleService = new Intent(this, BluetoothLEService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(bleService);
        } else {
            startService(bleService);
        }
    }

    private void stopBleService() {
        unbindBleService();
        Intent bleService = new Intent(this, BluetoothLEService.class);
        stopService(bleService);
    }

    private void updateNotify() {
        List<PublishPlatform> platforms = Constant.getPublishPlatform(context);
        //获取发布的平台
        PublishPlatform platform = platforms != null ? platforms.get(0) : null;
        updateAgent = new XdUpdateAgent.Builder()
                .setDebugMode(false)                          //是否显示调试信息(默认:false)
                .setJsonUrl(platform != null ? platform.getUrl(context) : null) //JSON文件的URL
                .setAllow4G(false)                             //是否允许使用运营商网络检查更新(默认:false)
                .setShowNotification(false)//true: 通过点击通知弹出更新对话框
                //使用通知提示用户有更新，用户点击通知后弹出提示框，而不是检测到更新直接弹框(默认:true，仅对非强制检查更新有效)
                .setOnUpdateListener(new XdUpdateAgent.OnUpdateListener() {
                    //取得更新信息JSON后的回调(可选)，回调在主线程，可执行UI操作，updateBean为JSON对应的数据结构
                    public void onUpdate(boolean needUpdate, XdUpdateBean updateBean) {
//                        if (!needUpdate) Toast.makeText(MainActivity.this, "您的应用为最新版本", Toast.LENGTH_SHORT).show();
                    }
                })
                .setDownloadText(getResources().getString(R.string.download_now))                   //可选，默认为左侧所示的文本
                .setInstallText(getResources().getString(R.string.downloaded))
                .setLaterText(getResources().getString(R.string.later))
                .setHintText(getResources().getString(R.string.version_update))
                .setDownloadingText(getResources().getString(R.string.downloading))
                .setIconResId(R.mipmap.ic_notify)           //设置在通知栏显示的通知图标资源ID(可选)
                .setCustomDialog(new XdUpdateAgent.ShowCustomDialog<AlertIOSDialog>() {
                    @Override
                    public AlertIOSDialog setCustomDialog(final SharedPreferences sp, final File file, final boolean fileExists, final Activity activity, final String versionName, final XdUpdateBean xdUpdateBean, final int versionCode, final boolean uncancelable) {
                        AlertIOSDialog iosDialog = new AlertIOSDialog(activity).builder()
                                .setCancelable(true)
                                .setTitle(versionName + " " + XdConstants.hintText)
                                .setMsgAlignment(Gravity.LEFT)
                                .setMsg(xdUpdateBean.note);
                        if (!uncancelable) {
                            iosDialog.setNegativeButton(XdConstants.laterText, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //这里写法固定, 用于忽略本次更新提醒
                                    sp.edit()
                                            .putLong("time", XdUpdateUtils.dayBegin(new Date()).getTime())
                                            .putInt("versionCode", versionCode)
                                            .putString("versionName", versionName)
                                            .apply();
                                }
                            });
                        }

                        if (fileExists) {
                            iosDialog.setPositiveButton(XdConstants.installText, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //安装已经下载的更新
                                    Uri uri = Uri.fromFile(file);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(intent);
                                }
                            });
                        } else {
                            iosDialog.setPositiveButton(XdConstants.downloadText + "(" + XdUpdateUtils.formatToMegaBytes(xdUpdateBean.size) + "M)", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //启动下载更新的服务
                                    Intent intent = new Intent(activity, XdUpdateService.class);
                                    intent.putExtra("xdUpdateBean", xdUpdateBean);
                                    intent.putExtra("appIcon", R.mipmap.ic_notify);
                                    activity.startService(intent);
                                }
                            });
                        }
                        iosDialog.show();
                        return iosDialog;
                    }

                    @Override
                    public void destroyDialog(AlertIOSDialog dialog) {
                        dialog.dismiss();
                        dialog = null;
                    }
                }).build();
        updateAgent.forceUpdate(this);//直接弹出更新对话框
//        updateAgent.update(this);
    }

    private void creanUpdateNotify() {
        if (updateAgent != null) {
            updateAgent.onDestroy();
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.cancel(Constant.NOTIFICATION_UPDATE_ID);
//        manager.cancel(Constant.NOTIFICATION_DOWNING_ID);
        manager.cancelAll();
    }

    /**
     * 获取请求ACCESS_FINE_LOCATION权限
     */
    private void getBLEPermissions() {
        //检查是否有定位权限
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 1) {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    isHasPermissions = true;
                    initBluetooth();
                } else {
                    isHasPermissions = false;
                    showNeedRequestPermissionDialog();
                }
            }
        }
    }

    private void showNeedRequestPermissionDialog() {
        if (mPermissionDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.bluetooth_permission_tip)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mPermissionDialog = null;
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            launchAppDetailsSettings();
                            mCheckPermissionWhenResume = true;
                        }
                    });
            mPermissionDialog = builder.create();
            mPermissionDialog.show();
        }
    }

    private void launchAppDetailsSettings() {
        String pkgName = this.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkgName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void init() {
        super.init();
        config = (Config) getApplicationContext();
        familyIfc = new FamilyIfcImpl(context);
        oximetIfc = new OximetIfcImpl(context);
        synchronousFamilyDataFlage = false;//一开始先去同步家庭
        //setTimerSync();//将数据同步到服务器上//去掉同步
//        SharedPreferencesUtil.setLocalTime(context, MyDateUtil.getDateFormatToStringSS(null));
        mPlayer = MediaPlayer.create(MainActivity.this, R.raw.didi);
        //mPlayer.start();

    }

    /**
     * 设置上一次versionCode
     */
    private void setLastAppVersionCode() {
        int lastVersionCode = SharedPreferencesUtil.getLastAppVersionCode(context);
        //versionCode为9时，将默认Pr最小值由50改为40(清除之前记录的PR_L_VALUE即可)
        if (lastVersionCode < 9) {
            String prLValue = SharedPreferencesUtil.getStringByKey(context, PR_L_VALUE);
            if ("50".equals(prLValue)) {
                SharedPreferencesUtil.setStringByKey(context, PR_L_VALUE, null);
            }
        }
        SharedPreferencesUtil.setLastAppVersionCode(context, XdUpdateUtils.getVersionCode(context));
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                switch (message.what) {
                    case HANDLER_BLUETOOTH_IS_OFF:
                        connectionStateTextView.setText(getResources().getString(R.string.bluetooth_off));
                        //mProgressBar.setVisibility(View.GONE);
                        isConnecting = false;
                        if (mSPO2WaveView.getDrawing()) {
                            mSPO2WaveView.stopDraw();
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //if (mPIView.getVisibility() == View.VISIBLE) {
                                //mPIView.setVisibility(View.GONE);
                                //mPIView.setPIValue(0);
                                //}
                            }
                        }, 500);
                        break;

                    case HANDLER_SHOW_DEVICE_LIST:
                        showDeviceListDialog();
                        break;

                    case HANDLER_NOTIFICATION_CHANGE:

                        if (isDeviceListDialogShowing) {
                            TextView deviceNumber = (TextView) dialog.findViewById(R.id.device_number);
                            deviceNumber.setText("" + deviceList.size());
                        }
                        break;

                    case HANDLER_CONNECT_BLE_DEVICE:
                        try {
                            currentDevice = (BLEDevice) message.obj;
                            if (currentDevice != null) {
                                isConnecting = true;
                                scanDevice(false);
                                connectionStateTextView.setText(getResources().getString(R.string.connecting_device));
                                TextUtil.setTextDotJumping(connectionStateTextView);
                                if (mBluetoothLEService == null) {
                                    //绑定Service
                                    Intent getServiceIntent = new Intent(MainActivity.this, BluetoothLEService.class);
                                    boolean isBindService = bindService(getServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                                    if (isBindService) {
                                        Log.d(TAG, "Bind Service successfully");
                                    } else {
                                        Log.w(TAG, "Fail to bind service!");
                                        if (connectionStateTextView != null) {
                                            connectionStateTextView.setText(R.string.bluetooth_init_fail_tip);
                                        }
                                    }
                                } else {
                                    connectBleDevice();
                                }
                                if (dialogTimer != null) {
                                    dialogTimer.cancel();
                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (connectionStateTextView != null) {
                                connectionStateTextView.setText(R.string.bluetooth_init_fail_tip);
                            }
                        }
                        break;
                    case HANDLER_REFRESH_PIVIEW:
                        //mPIView.postInvalidate();
                        break;

                    case HANDLER_BLUETOOTH_IS_ON:
                        final ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(aiv_ref, "rotation", 1080f);
                        objectAnimator1.setDuration(2000);
                        objectAnimator1.setRepeatCount(2);
                        if (isHasPermissions) {
                            connectionStateTextView.setText(getResources().getString(R.string.scaning_device));
                            TextUtil.setTextDotJumping(connectionStateTextView);
                            //mProgressBar.setVisibility(View.VISIBLE);

                            objectAnimator1.start();
                        } else {
                            connectionStateTextView.setText(getResources().getString(R.string.authority_device));
                            //mProgressBar.setVisibility(View.GONE);
                            objectAnimator1.clone();
                        }
                        break;
                    case Config.CHANGE_FAMILY:
                        //synchronousFamily(Config.CHANGE_FAMILY);
                        break;
                    case CHANGEFAMILYSUCCESS://若改变家庭的数据成功后,就同步网络上的家庭数据和数据库的数据
                        //synchronousFamilyData();
                        break;
                    case LOGINWITHACCOUNTMANAGE:
                        //synchronousFamily(CHANGEFAMILYSUCCESS);
                        break;
                    case CHANGEFAMILYFAIL:
                        if (frequency < 4) {
                            frequency++;
                            //synchronousFamily(CHANGEFAMILYSUCCESS);
                        }
                        break;
                    case CHANGEFAMILY://更新正在使用的用户
                        // changeFamily();
                        ///MyHandlerUtil.sendMsg(ManageAcountActivity.SYNCFAMILYICON, config.getManageHandler(), null);
                        break;
                    case SENDOXIVALUE://将新数据插入数据库
                        Oximet oximet = (Oximet) message.obj;
                        setOximet(oximet);
                        break;
                    case CLEARBTE://退出登陆
                        creanUpdateNotify();
                        clearBTByGuestMode();
//                    onDestroy();
                        if (collectTimer != null) {
                            collectTimer.cancel();
                        }
                        //if (PITimerTask != null) {
                        //PITimerTask.cancel();
                        //}
                        finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        config.setMainHandler2(mHandler);
    }

    private void connectBleDevice() {
        if (currentDevice != null && mBluetoothLEService != null) {
            if (!mBluetoothLEService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth!");
                if (connectionStateTextView != null) {
                    connectionStateTextView.setText(R.string.bluetooth_init_fail_tip);
                }
            } else {
                Log.d(TAG, "Initialize Bluetooth successfully.");
            }
            //获取成功之
            Log.d(TAG, "Try to connect.");
            registerBleReceiver();
            final boolean result = mBluetoothLEService.connect(currentDevice.getAddress());
            Log.d(TAG, "Connect request result = " + result);
        }
    }

    protected void initView() {
        currentDevice = null;
        SPO2WaveValues = new Vector<Integer>();
        mHistoryDatas = HistoryDataLib.getInstance().mHistoryDatas;
        PIValues = new Vector<>();

        sleepDatas = new ArrayList<>();
        deviceList = new ArrayList<>();
        deviceListAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return deviceList.size();
            }

            @Override
            public Object getItem(int i) {
                return deviceList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.list_item_device, null);
                }

                TextView deviceName = (TextView) view.findViewById(R.id.device_name);
                TextView deviceSN = (TextView) view.findViewById(R.id.device_sn);

                deviceName.setText(deviceList.get(i).getName());
                deviceSN.setText("SN: " + deviceList.get(i).getSNumber());

                return view;
            }
        };

        SpO2ValueTextView = (TextView) findViewById(R.id.spo2_value);
        HeartRateValueTextView = (TextView) findViewById(R.id.pr_value);
        connectionStateTextView = (TextView) findViewById(R.id.connection_state);
        //respValueTextView = (TextView) findViewById(R.id.resp_value);
        PIValueTextView = (TextView) findViewById(R.id.PI_value);
        //mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        connectedImage = (ImageView) findViewById(R.id.connected_image);
        //batteryImage = (ImageView) findViewById(R.id.battery);
        //nextArrawImage = (ImageView) findViewById(R.id.next_arrow);

        mHistoryButton = (ImageButton) findViewById(R.id.history_button);
        mSettingsButton = (ImageButton) findViewById(R.id.setting_button);
        aiv_ref = findViewById(R.id.aiv_ref);
        rl_dev = findViewById(R.id.rl_dev);
        mRootView = findViewById(R.id.root_view);

        rl_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mBluetoothAdapter) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 101);
                    }
                }
            }
        });

        // 根据所提供图片修改，将此控件注释
        // mSleepButton = (ImageButton) findViewById(R.id.sleep_button);

        mHistoryButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                //intent.putExtra("history", mHistoryDatas);
                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Log.d(TAG, "start new activity");
            }
        });

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RemindActivity.class);
                startActivity(i);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                overridePendingTransition(R.anim.in_lefttoright, R.anim.out_lefttoright);
//                Log.d(TAG, "start new activity");
            }
        });

        /* 根据所提供图片修改，将此控件注释
        mSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SleepActivity.class);
                startActivity(intent);
            }
        });

        mSleepButton.setVisibility(View.GONE);//暂时不要睡眠功能

         */

        //final FrameLayout deviceInfoLayout = (FrameLayout) findViewById(R.id.device_info);
        /* 替换前
        deviceInfoLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    deviceInfoLayout.setBackground(getResources().getDrawable(R.drawable.background_opaque));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    deviceInfoLayout.setBackground(getResources().getDrawable(R.drawable.background));

                    Intent i = new Intent(getApplicationContext(), DeviceDetailActivity.class);
                    i.putExtra("device_info", currentDevice);
                    startActivity(i);
                }

                return true;
            }
        });
         */
        //deviceInfoLayout.setEnabled(false);
        //deviceInfoFrameLayout = deviceInfoLayout;
        final FrameLayout usersInfoLayout = (FrameLayout) findViewById(R.id.users_info);
        usersInfoLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    usersInfoLayout.setBackground(getResources().getDrawable(R.drawable.background_opaque));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    usersInfoLayout.setBackground(getResources().getDrawable(R.drawable.background));
                    startActivity(new Intent(MainActivity.this, FamilyMainActivity.class));
                    overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
                }
                return true;
            }
        });
        usersInfoLayout.setEnabled(true);
        usersInfoFrameLayout = usersInfoLayout;
        usersInfoFrameLayout.setVisibility(View.GONE);
        userImage = (ImageView) findViewById(R.id.activity_family_member_icon);
        familyName = (TextView) findViewById(R.id.activity_family_member_text);
        ageText = (TextView) findViewById(R.id.activity_family_member_age_text);
        heightText = (TextView) findViewById(R.id.activity_family_member_height_text);
        weightText = (TextView) findViewById(R.id.activity_family_member_weight_text);
        //mPIView = (PIView) findViewById(R.id.PIView);
        mSPO2WaveView = (SPO2WaveView) findViewById(R.id.SPO2Wave);
        mSPO2WaveView.setValues(SPO2WaveValues);
        mSPO2WaveView.setPIValues(PIValues);
        mSPO2WaveView.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = mSPO2WaveView.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

    }

    private void registerBleReceiver() {
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        isRegisterBroadcastReceiver = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.press_again_exit_app_tip, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                creanUpdateNotify();
                finish();
                stopBleService();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void unbindBleService() {
        unRegisterBleReceiver();
        if (mBluetoothLEService != null) {
            mBluetoothLEService.disconnect();
            mBluetoothLEService.close();
            unbindService(mServiceConnection);
            mBluetoothLEService = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("main", "onDestroy");
        isConnecting = false;
        BLETimer.cancel();
        //if (PITimer != null) {
        //PITimer.cancel();
        //}
        stopBleService();
        scanDevice(false);
        clearBTByGuestMode();
        if (mPermissionDialog != null) {
            mPermissionDialog.dismiss();
        }
    }

    private void unRegisterBleReceiver() {
        if (isRegisterBroadcastReceiver) {
            unregisterReceiver(mGattUpdateReceiver);
            isRegisterBroadcastReceiver = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isActivityFront = false;
        if (mSPO2WaveView.getDrawing()) {
            mSPO2WaveView.stopDraw();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityFront = true;
        SPO2WaveValues = new Vector<Integer>();
        mSPO2WaveView.setValues(SPO2WaveValues);
        PIValues = new Vector<>();
        mSPO2WaveView.setPIValues(PIValues);

        if (mCheckPermissionWhenResume) {
            mCheckPermissionWhenResume = false;
            getBLEPermissions();
        }

        if (!mSPO2WaveView.getDrawing()
                && mBluetoothLEService != null
                && mBluetoothLEService.isBleConnected()) {
            mSPO2WaveView.startDraw();
        }

        changeFamily();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // 获取GATT服务和特性
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            return;
        }

        // 循环查找Gatt服务
        for (BluetoothGattService gattService : gattServices) {
            String ServiceUUID = gattService.getUuid().toString();

            if (ServiceUUID.equals(UUID_BLUETOOTH_SERVICE)) {
                Log.i(TAG, "Service UUID is : " + ServiceUUID);

                // 查找特性
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    String CharacteristicUUID = gattCharacteristic.getUuid().toString();

                    if (CharacteristicUUID.equals(UUID_BLUETOOTH_CHARACTERISTIC)) {
                        Log.i(TAG, "Characteristic UUID is : " + ServiceUUID);
                        mCharacteristic = gattCharacteristic;
                        int a = mCharacteristic.getProperties();
                        int b = mCharacteristic.getPermissions();
                        System.out.println("getProperties is : " + a);
                        System.out.println("getPermissions is : " + b);
                        // 开启同步数据广播
                        mBluetoothLEService.setCharacteristicNotification(mCharacteristic, true);
                    }
                }
            }
        }

    }

    // 广播过滤
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothLEService.ACTION_DATA_AVAILABLE);
        filter.addAction(BluetoothLEService.ACTION_GATT_CONNECTED);
        filter.addAction(BluetoothLEService.ACTION_GATT_DISCONNECTED);
        filter.addAction(BluetoothLEService.ACTION_GATT_SERVICES_DISCOVERED);
        filter.addAction(BluetoothLEService.ACTION_FAIL_TO_CONNECTED);

        return filter;
    }

    private void initBluetooth() {
        // 初始化 bluetoothAdapter
        // 通过蓝牙管理器得到一个蓝牙适配器
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        checkBluetooth();
        checkBLE();
        checkBluetooth();

        if (mBluetoothAdapter != null) {
            isBluetoothON = mBluetoothAdapter.isEnabled();
        }

    }

    // 检查手机是否支持蓝牙
    private void checkBluetooth() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "Not support Bluetooth!", Toast.LENGTH_SHORT).show();
        }
        isSupportBluetooth = false;
    }

    // 检查手机是否支持BLE
    private void checkBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(MainActivity.this, "Not support BLE!", Toast.LENGTH_LONG).show();
        }
        isSupportBluetooth = false;
    }

    private void scanDevice(final boolean enable) {
        if (mBluetoothAdapter != null) {
            if (enable) {
                isScanning = true;
                //第三步..adapter去开始扫描
//            connectionStateTextView.setText("扫描设备中...");
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                isScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    private void showDeviceListDialog() {

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_device_list);

        ListView listView = (ListView) dialog.findViewById(R.id.device_list);
        listView.setAdapter(deviceListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isConnecting = true;
                Message message = new Message();
                message.what = HANDLER_CONNECT_BLE_DEVICE;
                message.obj = deviceList.get(i);
                mHandler.sendMessage(message);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isDeviceListDialogShowing = false;
                deviceList = new ArrayList<BLEDevice>();
                if (!isConnecting) {
                    restartScan();
                }
            }
        });

        TextView deviceNumber = (TextView) dialog.findViewById(R.id.device_number);
        deviceNumber.setText("" + deviceList.size());

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        isDeviceListDialogShowing = true;
    }

    private void restartScan() {
        deviceList = new ArrayList<BLEDevice>();
        dialogTimer.cancel();
        dialogTimer = new Timer();
        initDialogTimer();
        scanDevice(false);
    }

    private void setTimer() {
        BLETimer = new Timer();
        dialogTimer = new Timer();

        BLETimerTask = new TimerTask() {
            @Override
            public void run() {

                //final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                //mBluetoothAdapter = bluetoothManager.getAdapter();

                if (mBluetoothAdapter != null) {
                    isBluetoothON = mBluetoothAdapter.isEnabled();
                }

                if (isBluetoothON) {
                    if (!isScanning && !isConnecting) {
                        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                        mBluetoothAdapter = bluetoothManager.getAdapter();

                        isScanning = true;

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //scanDevice(false);
                                scanDevice(true);
                            }
                        }, 1000);
                        dialogTimer = new Timer();
                        initDialogTimer();
                        dialogTimer.schedule(dialogTimerTask, 5000, 5000);
                        Message message = new Message();
                        message.what = HANDLER_BLUETOOTH_IS_ON;
                        mHandler.sendMessage(message);
                    }
                } else {
                    Message message = new Message();
                    message.what = HANDLER_BLUETOOTH_IS_OFF;
                    mHandler.sendMessage(message);

                    if (isScanning) {
                        scanDevice(false);
                        dialogTimerTask.cancel();
                    }

                    isScanning = false;
                }
            }
        };

        initDialogTimer();

        BLETimer.schedule(BLETimerTask, 0, 100);
    }

    private void initDialogTimer() {
        dialogTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isDeviceListDialogShowing && deviceList.size() >= 2) {
                    Message message = new Message();
                    message.what = HANDLER_SHOW_DEVICE_LIST;
                    mHandler.sendMessage(message);
                } else if (!isDeviceListDialogShowing && deviceList.size() == 1) {
                    isConnecting = true;
                    Message message = new Message();
                    message.what = HANDLER_CONNECT_BLE_DEVICE;
                    message.obj = deviceList.get(0);
                    mHandler.sendMessage(message);
                }
            }
        };
    }

    //达到平衡值插入数据库
    private void setOximet(Oximet oximet) {
        oximet.setPart(1);
        oximet.setRecordDate(MyDateUtil.getDateFormatToStringSS(null));//现在已经改成是用
        oximet.setUpdatedDate(MyDateUtil.getDateFormatToString(null));//其他两个用毫秒
        oximet.setCreatedDate(MyDateUtil.getDateFormatToString(null));
        oximet.setValueId(StringUtil.getTimeNameSql());
        oximet.setIsDeleted(0);

        if (familyPosition != -1) {
            oximet.setFamilyID(familys.get(SharedPreferencesUtil.getFamilyPosition(context)).getFamilyId());
        } else {
            if (familys.size() != 0) {
                oximet.setFamilyID(familys.get(0).getFamilyId());
            } else {
                return;
            }
        }
        Log.e("插入测量的数据", oximet.toString());
        oximetIfc.insert(oximet);
        MyHandlerUtil.sendMsg(CHANGETEMPVALUE, config.getRecordMainHandler(), null);
        MyHandlerUtil.sendMsg(HistoryChartFragment.ADDNEWVALUETOCHART, config.getRecordMainHandler(), oximet);
    }

    /**
     * 更新正在测量的用户
     */
    private void changeFamily() {
        familys = config.getFamilys();
        familyPosition = SharedPreferencesUtil.getFamilyPosition(context);
        if (familys.size() != 0) {//有家庭存在
            bitmaps = LoadingAva.getFamilyAva(context, familys);//获取所有用户的头像
        }

        if (familyPosition != -1) {//有设置用户的位置
            if (familyPosition < familys.size()) {//如果保存用户的位置大于用户的数量, 那肯定有成员被删除
                familyName.setText(familys.get(familyPosition).getName());
                ageText.setText(Integer.toString(GetAgeByBirthday.getAgeByBirthday(familys.get(familyPosition)
                        .getBirthday())) + getString(R.string.first_family_select_age_util));//在成员名下显示年龄
                if (familys.get(familyPosition).getHeight() > 0) {
                    heightText.setVisibility(View.VISIBLE);
                    if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
                        heightText.setText((int) familys.get(familyPosition).getHeight()
                                + getString(R.string.first_family_select_height_util));//在成员名下显示身高
                    } else {//身高为英寸单位
                        heightText.setText(UnitUtils.FloatFormat1(UnitUtils.Cm2In((familys.get(familyPosition)
                                .getHeight()))) + getString(R.string.more_unit_height_in));
                    }
                } else {//不存在身高
                    heightText.setVisibility(View.GONE);
                }
                if (familys.get(familyPosition).getWeight() > 0) {//在成员名下显示体重
                    weightText.setVisibility(View.VISIBLE);
                    if (SharedPreferencesUtil.getWeightUnit(context) == 1) {//体重单位为KG
                        weightText.setText(UnitUtils.FloatFormat1((familys.get(familyPosition).getWeight()))
                                + getString(R.string.first_family_select_weight_util));
                    } else {
                        weightText.setText(UnitUtils.Float2Int(UnitUtils.Kg2Bl(familys.get(familyPosition)
                                .getWeight())) + getString(R.string.more_unit_weight_lb));
                    }
                } else {
                    weightText.setVisibility(View.GONE);
                }
                if (bitmaps[familyPosition] != null) {//如果之前有设置图片则头像为设置的图片
                    Bitmap bitmap = PicUtils.getRoundRectBitmap(bitmaps[familyPosition].getBitmap(), bitmaps[familyPosition].getBitmap().getWidth());
                    userImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                } else {
                    userImage.setBackground(this.getResources().getDrawable(R.drawable.avatar_default));
                }
            } else {
                Log.e("当前位置大于总数", familyPosition + ", " + familys.size());
                familyName.setText(this.getResources().getString(R.string.first_family_add_member));
                ageText.setText("");
                heightText.setText("");
                weightText.setText("");
                userImage.setBackground(this.getResources().getDrawable(R.drawable.avatar_default));
            }
        } else {//没有设置用户位置(第一次使用)
            if (familys.size() != 0) {
                SharedPreferencesUtil.setFamilyPosition(context, 0);
                familyName.setText(familys.get(0).getName());
                ageText.setText(Integer.toString(GetAgeByBirthday.getAgeByBirthday(familys.get(0)
                        .getBirthday())) + getString(R.string.first_family_select_age_util));

                if (familys.get(0).getHeight() > 0) {
                    heightText.setVisibility(View.VISIBLE);
                    if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
                        heightText.setText((int) (familys.get(0).getHeight())
                                + getString(R.string.first_family_select_height_util));
                    } else {
                        heightText.setText(UnitUtils.FloatFormat1(UnitUtils.Cm2In(familys.get(0).getHeight()))
                                + getString(R.string.more_unit_height_in));
                    }
                } else {
                    heightText.setVisibility(View.GONE);
                }
                if (familys.get(0).getWeight() > 0) {
                    weightText.setVisibility(View.VISIBLE);

                    if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
                        weightText.setText(UnitUtils.FloatFormat1((familys.get(0).getWeight()))
                                + getString(R.string.first_family_select_weight_util));
                    } else {
                        weightText.setText(UnitUtils.Float2Int(UnitUtils.Kg2Bl(familys.get(0)
                                .getWeight())) + getString(R.string.more_unit_weight_lb));
                    }
                } else {
                    weightText.setVisibility(View.GONE);

                }

                if (bitmaps[0] != null) {
                    Bitmap bitmap = PicUtils.getRoundRectBitmap(bitmaps[0].getBitmap(), bitmaps[0].getBitmap().getWidth());
                    userImage.setBackground(new BitmapDrawable(getResources(), bitmap));
//                    userImage.setBackgroundDrawable(bitmaps[0]);
                } else {
                    userImage.setBackground(this.getResources().getDrawable(R.drawable.avatar_default));
                }
            } else {
                familyName.setText(this.getResources().getString(R.string.first_family_add_member));
                ageText.setText("");
                heightText.setText("");
                weightText.setText("");
                userImage.setBackground(this.getResources().getDrawable(R.drawable.avatar_default));
            }
        }

        AccountIfc ifc = new AccountIfcImpl(context);
        List<LoginInfo> loginInfos = ifc.findAll();
        for (int i = 0; i < loginInfos.size(); i++) {
            if (SharedPreferencesUtil.getAccountPosition(context) == i) {
                LoginInfo info = loginInfos.get(i);
                if (SharedPreferencesUtil.getFamilyPosition(context) != -1) {
                    if (familys.size() != 0) {
                        info.setAvatar(familys.get(SharedPreferencesUtil.getFamilyPosition(context)).getAvatar());
                    } else {
                        info.setAvatar(null);
                    }
                } else {
                    info.setAvatar(null);
                }
                ifc.update(info);
            }
        }
    }

    /**
     * @param who CHANGEFAMILYSUCCESS: 既同步家庭人员同时也同步其测量数据 Config.CHANGE_FAMILY: 只同步家庭成员
     */
    public void synchronousFamily(final int who) {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                FamilyMemBean familyData = new FamilyMemBean();
                familyData.setKey("GetFamiliesByAccountID");
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
                            Gson gson = new Gson();
                            ResponseObject<FamilyResp> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<FamilyResp>>() {
                            }.getType());
                            List<Family> families = convertNetFamilyToLocalFam(context, object.getData().getFamilies());

                            //将服务器中的头像保存到本地
                            for (Family family : families) {
                                Log.e("服务器的家庭成员", family.toString());
                                if (family.getAvatar() != null && !family.getAvatar().equals("")) {
                                    downloadPhotoAva(config.getMemberId(), family.getAvatar());
                                }
                            }

                            insertFamilys(families, who);

                            //将原来的删除, 插入最新的
//                            familyIfc.deleteTable();
//                            familyIfc.insert(families);
//
//                            config.changeFamily();//通知更新家庭成员列表
//                            MyHandlerUtil.sendMsg(CHANGEFAMILY, clear, null);//通知更新用户头像
//                            if (who == CHANGEFAMILYSUCCESS) {
//                                MyHandlerUtil.sendMsg(CHANGEFAMILYSUCCESS, mHandler, null);
//                            }
                        } else {
                            if (who == CHANGEFAMILYSUCCESS) {
                                MyHandlerUtil.sendMsg(CHANGEFAMILYFAIL, mHandler, null);
                            } else if (who == Config.CHANGE_FAMILY) {
                                //刷新列表失败
                                MyHandlerUtil.sendMsg(Config.CHANGE_FAMILY_FAIL, config.getFamilyHandler(), ReErrorCode.getErrodType(context, errorCode));
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        BLTToast.show(context, getResources().getString(R.string.json_checknet_nettimeout));
                        if (who == CHANGEFAMILYSUCCESS) {
                            MyHandlerUtil.sendMsg(CHANGEFAMILYFAIL, mHandler, null);
                        } else if (who == Config.CHANGE_FAMILY) {
                            //刷新列表失败
                            MyHandlerUtil.sendMsg(Config.CHANGE_FAMILY_FAIL, config.getFamilyHandler(), getResources().getString(R.string.json_checknet_nettimeout));
                        }
                    }
                });
            }
        });
    }

//    private

    /**
     * 将服务器获取到的家庭成员数组转换到本地家庭成员的结构(在设计上有差异, 需要转换)
     *
     * @return
     */
    private List<Family> convertNetFamilyToLocalFam(Context context, List<Family> families) {
        for (int i = 0; i < families.size(); i++) {
            Family family = families.get(i);
            //将用户firstname赋值到name
            family.setName(family.getFirstname());
            family.setFamilyId(family.getMemberId());
            family.setMemberId(SharedPreferencesUtil.getMemberId(context));
        }

        return families;
    }

    private void insertFamilys(final List<Family> families, final int who) {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                //将原来的删除, 插入最新的
                familyIfc.deleteTable();
                familyIfc.insert(families);
                config.changeFamily();//通知更新家庭成员列表
                MyHandlerUtil.sendMsg(CHANGEFAMILY, mHandler, null);//通知更新用户头像
                if (who == CHANGEFAMILYSUCCESS) {
                    MyHandlerUtil.sendMsg(CHANGEFAMILYSUCCESS, mHandler, null);
                }
            }
        });
    }

    /**
     * 获取服务器头像
     *
     * @param MemberId
     * @param name
     */
    public void downloadPhotoAva(int MemberId, final String name) {
        if (name != null || !name.equals("")) {

            String[] url = name.split("/");
            final File file = BitmapUtil.PHOTO_DIR;
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file + "/" + name + ".jpg");
            if (!file2.exists()) {
                MyThread.startNewThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadPhoto(file, Constant.BLT_GET_IMGURL + name, name + ".jpg");
                        config.changeFamily();//图片下载完成, 通知更新家庭成员列表更新头像
                    }
                });
            } else {
            }

        }
    }

    /**
     * 网络获取头像图片
     *
     * @param file
     * @param urlPath
     * @param name
     * @return
     */
    private Bitmap downloadPhoto(File file, String urlPath, String name) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            byte[] dataImage = bos.toByteArray();
            bos.close();
            in.close();
            Bitmap bitmap = null;
            if (dataImage.length > 300) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = 2;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length, options);
            } else {
                bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
            }
            saveBitmap(file, bitmap, name);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将图片保存到本地
     *
     * @param file
     * @param obj
     * @param name
     */
    private void saveBitmap(File file, Bitmap obj, String name) {
        try {
            File imageFile = new File(BitmapUtil.PHOTO_DIR + "/" + name);
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            // 压缩0-100.100为原质量大小
            obj.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public static String getSdCardPath() {
        boolean exist = isSdCardExist();
        String SDpath = "";
        if (exist) {
            SDpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();

            Log.d("path", SDpath);
        } else {
            SDpath = "不适用";
        }
        return SDpath;
    }

    /**
     * 获取默认的文件路径
     *
     * @return
     */
    public static String getDefaultFilePath() {
        String filepath = "";
        File file = new File(Environment.getExternalStorageDirectory(),
                "abc.txt");
        if (file.exists()) {
            filepath = file.getAbsolutePath();
        } else {
            filepath = "不适用";
        }
        return filepath;
    }

    /**
     * 10分钟同步一次数据
     */
    private void setTimerSync() {
        syncDataTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                SyncUtils.count.getAndSet(0);
                if (HistoryActivity.selectDate != null) {
                    DateWeekGridViewAdapter.STATE = MyDateUtil.formateDate2(HistoryActivity.selectDate);
                    HistoryActivity.isClickCalendar = false;
                    MyHandlerUtil.sendMsg(Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST,
                            config.getRecordMainHandler(), HistoryActivity.selectDate);
                } else {
                    String date = MyDateUtil.formateDate2(new Date());
                    DateWeekGridViewAdapter.STATE = MyDateUtil.formateDate2(date);
                    HistoryActivity.isClickCalendar = false;
//                    MyHandlerUtil.sendMsg(Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST, config.getRecordMainHandler(), date);
                }
                if (synchronousFamilyDataFlage) {
                    SyncUtils.syncOximets(mHandler, context, config);
                }
                //SynchronousFamilyData();
            }
        };
        syncDataTimer.schedule(timerTask, 1000, 10 * 60 * 1000);
    }

    /**
     * 同步家庭数据
     */
    private void synchronousFamilyData() {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                final SyncDataBean syncDataBean = new SyncDataBean();
                syncDataBean.setKey("SyncData");
                syncDataBean.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                syncDataBean.setAccountId(SharedPreferencesUtil.getMemberId(context));
                syncDataBean.setTimestamp(Float.parseFloat(SharedPreferencesUtil.getTime(context)));
                syncDataBean.setValue(SyncUtils.oximet2String(context));
//                Log.e("看看往服务器上传的数据是什么??",""+syncDataBean.toString());
                String syncDataJson = new JsonUtils<SyncDataBean>().getJsonString(syncDataBean);
//                Log.e("看看往服务器上传的数据是什么??",""+syncDataJson.toString());
                try {
                    params.setBodyEntity(new StringEntity(syncDataJson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
                synchronousFamilyDataFlage = false;//停止定时器继续向服务器请求(等本次请求完成)
                Log.e("请求的json", syncDataJson);
                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("同步家庭成功后的返回", responseInfo.result);
                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
                        if (errorCode == 0) {
                            synchronousFamilyDataFlage = true;//启动定时器获取网络数据
                            Gson gson = new Gson();
                            ResponseObject<OximetResp> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<OximetResp>>() {
                            }.getType());
                            String synServerData = object.getData().getValue();//获取服务器返回的测量数据
                            if (synServerData != null && !synServerData.equals("")) {
                                float timestamp = object.getData().getTimestamp();
                                insertOximetValue(context, timestamp, synServerData);//将同步下来的数据插入数据库
                            }
                        } else {
                            synchronousFamilyDataFlage = true;//启动定时器获取网络数据
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        synchronousFamilyDataFlage = true;//启动定时器获取网络数据
                    }
                });
            }
        });
    }

    /**
     * 将同步到的数据插入到数据库中
     *
     * @param context
     * @param timestamp     同步的时间戳
     * @param synServerData 解压后的数据字符串
     */
    private static void insertOximetValue(final Context context, final float timestamp, final String synServerData) {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                String dataStr = StringUtil.getDeCompressSting(synServerData);
                Log.e("解压后", dataStr);
//                List<Oximet> oximets = null;
//                try {
//                    JSONArray syncDataJsonArray = new JSONArray(dataStr);
//                    oximets = SyncUtils.getSynRespData(syncDataJsonArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                OximetIfc oximetIfc = new OximetIfcImpl(context);
//                DBHelper.getDb().beginTransaction();
//                for (int i = 0; i < oximets.size(); i++) {
//                    oximetIfc.insert(oximets.get(i));
//                    Log.e("插入", oximets.get(i).toString());
//                }
//                oximets = null;
//                DBHelper.getDb().setTransactionSuccessful();
//                DBHelper.getDb().endTransaction();


                Type listType = new TypeToken<ArrayList<OximetTamp>>() {
                }.getType();
                ArrayList<OximetTamp> syncDataArray = new Gson().fromJson(dataStr, listType);//能够处理过10万条数据(使用JSONArray会出现内存不够)
                Log.e("插入的长度", syncDataArray.size() + "");
                if (syncDataArray.size() > 0) {
                    OximetIfc oximetIfc = new OximetIfcImpl(context);
                    DBHelper.getDb().beginTransaction();
                    for (int i = 0; i < syncDataArray.size(); i++) {
                        OximetTamp oximetTamp = syncDataArray.get(i);
                        oximetTamp.setRecord_date(MyDateUtil.getStringSS(oximetTamp.getRecord_date()));//这里是相当于把他变成了毫秒的
                        oximetTamp.setCreated_date(MyDateUtil.getDateFormatToString(null));//由于获取不到所以设置当前时间
                        oximetTamp.setUpdated_date(MyDateUtil.getDateFormatToString(null));
//                        oximetTamp.setUpdated_date(MyDateUtil.getStringSS(oximetTamp.getUpdated_date()));
                        oximetTamp.setIs_deleted(oximetTamp.getIs_deleted());
                        oximetTamp.setFamilyID(oximetTamp.getMemberId());
                        oximetIfc.insert(oximetTamp);
                    }
                    DBHelper.getDb().setTransactionSuccessful();
                    DBHelper.getDb().endTransaction();
                    //记录本次同步的时间
                    SharedPreferencesUtil.setTime(context, String.valueOf(timestamp));
                }
                Log.e("插入完成timestamp", timestamp + "");
                SharedPreferencesUtil.setLocalTime(context, MyDateUtil.getDateFormatToStringSS(null));
                Log.e("MYActivity的localtime", "看看这个时间" + MyDateUtil.getDateFormatToStringSS(null));
                MyHandlerUtil.sendMsg(CHANGETEMPVALUE, config.getRecordMainHandler(), null);
            }
        });
    }

    /**
     * 每30秒采集一次在30秒内中采集到最后一个的有效数值
     */
    private void setCollectDataTimer() {
        if (collectTimer != null) {
            collectTimer.cancel();
        }
        collectTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                Log.e("30秒有新数据", oximetsTamp.size() + "");
                if (oximetsTamp.size() > 0) {
                    Log.e("30秒有新数据", oximetsTamp.size() + "");
                    for (int i = oximetsTamp.size() - 1; i >= 0; i--) {
                        Oximet oximet = oximetsTamp.get(i);
                        if (isEffectiveValue(oximet)) {
                            Message msg = new Message();
                            msg.what = MainActivity.SENDOXIVALUE;
                            msg.obj = oximet;
                            config.getMainHandler2().sendMessage(msg);//往主线程发送message
                            oximetsTamp.clear();
                            break;
                        }
                    }
                    Log.e("30秒有新数据", oximetsTamp.size() + "");
                }
            }
        };
        collectTimer.schedule(timerTask, 3000, 30000);
    }

    /**
     * 判断测量数据是否是有效值
     *
     * @return
     */
    private boolean isEffectiveValue(Oximet oximet) {
        if (oximet.getSPO2() >= 80 && oximet.getSPO2() <= 100 && oximet.getPR() >= 0 && oximet.getPR() <= 240) {
            return true;
        }
        return false;
    }

    /**
     * 退出当前界面时，去操作 重新登录，或者去执行客人模式时，用到的关闭蓝牙服务的方法
     */
    protected void clearBTByGuestMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BLETimer.cancel();
            //if (PITimer != null) {
            //PITimer.cancel();
            //}
            unbindBleService();
            scanDevice(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            // 打开蓝牙
            Message message = new Message();
            message.what = HANDLER_BLUETOOTH_IS_ON;
            mHandler.sendMessage(message);
        }
    }
}
