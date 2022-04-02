package com.jks.Spo2MonitorEx.app.Bluetoothle;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.MainActivity;

import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;

/**
 * Created by badcode on 15/10/15.
 * ble service
 */
public class BluetoothLEService extends Service {
    // �õ�BluetoothLEService�ļ�д���ƣ���������������getClass()��������
    private final static String TAG = BluetoothLEService.class.getSimpleName();
    private final static String CHANNEL_ID = TAG;
    public final static String EXTRA_DATA = "com.jks.Spo2MonitorEx.EXTRA_DATA";
    public final static UUID UUID_BLUETOOTHLE_CHARACTERISTIC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    // gatt action
    public final static String ACTION_GATT_CONNECTED = "com.example.oximeter.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.oximeter.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.oximeter.ACTION_GATT_SERVICES_DISCOVERED ";
    public final static String ACTION_DATA_AVAILABLE = "com.example.oximeter.ACTION_DATA_AVAILABLE";
    public final static String ACTION_FAIL_TO_CONNECTED = "com.example.oximeter.ACTION_FAIL_TO_CONNECTED";
    // status
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    // bluetooth
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    // previous device address
    private String mBluetoothDeviceAddress;
    private int mConnectionState = STATE_DISCONNECTED;
    // GATT�¼�Callback
    public final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // ���ӳɹ����Է���GATT����
                boolean success = mBluetoothGatt.discoverServices();
                Log.i(TAG, "5.0 attempting to start service discovery: " + success);
            } else {
                mConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(ACTION_FAIL_TO_CONNECTED);
                Log.i(TAG, "Failed to set MTU.");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            Log.d(TAG, "============status: " + status);
            if (status == 133) { // ����ʧ��
                intentAction = ACTION_FAIL_TO_CONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Failed to connect.");
            } else {
                // �������״̬����Ϊconnected
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    intentAction = ACTION_GATT_CONNECTED;
                    mConnectionState = STATE_CONNECTED;
                    broadcastUpdate(intentAction);
                    Log.i(TAG, "Connected to GATT server.");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        gatt.requestMtu(80);
                    } else {
                        // ���ӳɹ����Է���GATT����
                        boolean success = mBluetoothGatt.discoverServices();
                        Log.i(TAG, "below 5.0 attempting to start service discovery: " + success);
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mConnectionState = STATE_DISCONNECTED;
                    Log.i(TAG, "Disconnected from GATT server.");
                    broadcastUpdate(intentAction);
                }
            }
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "----------write success---------status: " + status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.i(TAG, "onDescriptorWrite = " + status + ", descriptor = " + descriptor.getUuid().toString());
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.i(TAG, "rssi = " + rssi);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }
    };

    public class LocalBinder extends Binder {

        // get service
        public BluetoothLEService getService() {
            return BluetoothLEService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForegroundService();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     *通过通知启动服务
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void setForegroundService()
    {
        //设定的通知渠道名称
        String channelName = getString(R.string.app_name);
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        );
        builder
                .setSmallIcon(R.mipmap.ic) //设置通知图标
                .setContentIntent(pendingIntent)
                .setContentTitle(channelName)//设置通知标题
                .setContentText(getString(R.string.ble_service_running))//设置通知内容
                .setAutoCancel(false) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(101, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // ʹ��һ���豸֮��ȷ��������close()
        disconnect();
        close();

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
        close();
    }

    private final IBinder mIBinder = new LocalBinder();

    public boolean isBleConnected() {
        return mConnectionState == STATE_CONNECTED;
    }

    public boolean initialize() {
        if (mBluetoothManager == null) {
            //��һ��ȥ��ȡBluetoothManager
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
    //�ڶ���ȥ��ȡmBluetoothAdapter
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to initialize BluetoothAdapter.");
            return false;
        }
        return true;
    }

    // ����BLE�豸��GATT����
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || TextUtils.isEmpty(address)) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
        }
        // ������������֮ǰ���ӵ��豸
        // �ڱ��ε�ַ��֮ǰ�豸��ַ��ͬ�����
        if (address.equals(mBluetoothDeviceAddress)) {
            Log.d(TAG, "Trying to use an existing BluetoothGatt for connection.");
            if (mBluetoothGatt != null && mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found. Unable to connect.");
            return false;
        }
        // ֱ�������豸�������趨�Զ�����Ϊfalse
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback, TRANSPORT_LE);
        } else {
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        }
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    // ȡ������
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    // �ù�һ��BLE�豸�󣬱����������������ͷ���Դ
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        mBluetoothDeviceAddress = null;
    }


    // ���¹㲥���ҷ��㲥
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        final byte[] data = characteristic.getValue(); // ��ȡԭʼ����

         Log.e("data is what",""+data.toString());
        int length = data.length;// ���ݳ���
        if (length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(length);
            // ������ת��ʮ������String
            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X", byteChar));
            }
            Log.e("yuanshishuju ",""+stringBuilder.toString());
            intent.putExtra(EXTRA_DATA, stringBuilder.toString());
        } else {
            intent.putExtra(EXTRA_DATA, "null");
        }
        sendBroadcast(intent);
    }

    // д������
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    // ��ȡ����
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    // ���������㲥֪ͨ
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);//���óɿ���֪ͨ��
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_BLUETOOTHLE_CHARACTERISTIC);
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }
    // ��÷����б�
    public List<BluetoothGattService> getSupportedGattService() {
        if (mBluetoothGatt == null) {
            return null;
        }
        return mBluetoothGatt.getServices();
    }
    // ��ȡRSSI
    public boolean getRssiVal() {
        if (mBluetoothGatt == null) {
            return false;
        }
        return mBluetoothGatt.readRemoteRssi();
    }
}
