package com.jks.Spo2MonitorEx.util.autoupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by apple on 2016/10/26.
 */
public class XdUpdateAgent {
    protected XdUpdateAgent() {
    }

    protected static XdUpdateAgent instance;

    protected boolean forceUpdate;
    protected boolean uncancelable;
    protected boolean allow4G;
    protected XdUpdateBean updateBeanLocallyProvided;
    protected String jsonUrl;
    protected int iconResId;
    protected boolean showNotification;
    protected OnUpdateListener l;
    protected ShowCustomDialog customDialogIfc;
    protected Object customDialog;
    protected Subscription md5Subscription, subscription;
    protected AlertDialog dialog;
    private String channeId;
    public AlertDialog getDialog() {
        return dialog;
    }

    public XdUpdateAgent setJsonUrl(String jsonUrl) {
        instance.jsonUrl = jsonUrl;
        return instance;
    }

    public void onDestroy() {
        if (dialog != null) {
            try {
                Log.e("清除", "dialog");
                dialog.dismiss();
                dialog = null;
            } catch (Throwable ignored) {
            }
        }
        if (customDialogIfc != null) {
            Log.e("清除", "customDialog");
            if (customDialog != null) {
                customDialogIfc.destroyDialog(customDialog);
            }
        }
        if (md5Subscription != null) md5Subscription.unsubscribe();
        if (subscription != null) subscription.unsubscribe();
    }

    public void forceUpdate(Activity activity) {
        forceUpdate = true;
        update(activity);
    }

    public void forceUpdateUncancelable(Activity activity) {
        uncancelable = true;
        forceUpdate(activity);
    }

    public void update(final Activity activity) {
        if (!forceUpdate && !allow4G && !XdUpdateUtils.isWifi(activity)) return;
        if (updateBeanLocallyProvided == null && TextUtils.isEmpty(jsonUrl)) {
            Log.e("更新的jsonUrl出错", "Please set updateBean or jsonUrl.");
            return;
        }
        if (updateBeanLocallyProvided != null) {
            updateMatters(updateBeanLocallyProvided, activity);
        } else {
            subscription = Observable.create(new Observable.OnSubscribe<Response>() {
                @Override
                public void call(Subscriber<? super Response> subscriber) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(jsonUrl).build();
                    Response response;
                    try {
                        response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            subscriber.onNext(response);
                        } else {
                            subscriber.onError(new IOException(response.code() + ": " + response.body().string()));
                        }
                    } catch (Throwable e) {
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new Subscriber<Response>() {

                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (XdConstants.debugMode) e.printStackTrace();
                        }

                        @Override
                        public void onNext(Response response) {
                            String responseBody;
                            try {
                                responseBody = response.body().string();
                            } catch (Throwable e) {
                                if (XdConstants.debugMode) e.printStackTrace();
                                return;
                            }
                            if (XdConstants.debugMode) System.out.println(responseBody);
                            final XdUpdateBean xdUpdateBean = new XdUpdateBean();
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody);
                                xdUpdateBean.versionCode = jsonObject.getInt("versionCode");
                                xdUpdateBean.size = jsonObject.getInt("size");
                                xdUpdateBean.versionName = jsonObject.getString("versionName");
                                xdUpdateBean.url = jsonObject.getString("url");
                                xdUpdateBean.note = jsonObject.getString("note");
                                xdUpdateBean.md5 = jsonObject.getString("md5");
                                Log.e("获取到的更新信息", jsonObject.toString());
                            } catch (JSONException e) {
                                if (XdConstants.debugMode) e.printStackTrace();
                                return;
                            }
                            updateMatters(xdUpdateBean, activity);
                        }
                    });
        }
    }

    protected void updateMatters(final XdUpdateBean updateBean, final Activity activity) {
        final int currentCode = XdUpdateUtils.getVersionCode(activity.getApplicationContext());
        final String currentName = XdUpdateUtils.getVersionName(activity.getApplicationContext());
        final int versionCode = updateBean.versionCode;
        final String versionName = updateBean.versionName;
        if (currentCode < versionCode/* || currentName.compareToIgnoreCase(versionName) < 0*/) {
            if (l != null) l.onUpdate(true, updateBean);
            final SharedPreferences sp = activity.getSharedPreferences("update", Context.MODE_PRIVATE);
            long lastIgnoredDayBegin = sp.getLong("time", 0);
            int lastIgnoredCode = sp.getInt("versionCode", 0);
            String lastIgnoredName = sp.getString("versionName", "");
            long todayBegin = XdUpdateUtils.dayBegin(new Date()).getTime();
            if (!forceUpdate && todayBegin == lastIgnoredDayBegin && versionCode == lastIgnoredCode && versionName.equals(lastIgnoredName)) {
                Log.e("本次更新被忽略", "true");
                return;
            }
            final File file = new File(activity.getExternalCacheDir(), "update.apk");
            Log.e("文件路径", file.getPath());
            if (file.exists()) {
                md5Subscription = XdUpdateUtils.getMd5ByFile(file, new Subscriber<String>() {

                    boolean fileExists = false;

                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        file.delete();
                        if (XdConstants.debugMode) e.printStackTrace();
                        proceedToUI(sp, file, fileExists, activity, versionName, updateBean, versionCode);
                    }

                    @Override
                    public void onNext(String Md5JustDownloaded) {
                        Log.e("下载取得的Md5值", updateBean.md5 + ", 应用md5值: " +  Md5JustDownloaded);
                        String Md5InUpdateBean = updateBean.md5;
                        if (Md5JustDownloaded.equalsIgnoreCase(Md5InUpdateBean)) {
                            fileExists = true;
                        } else {
                            file.delete();
                            if (XdConstants.debugMode) System.err.println("Md5 dismatch. Md5JustDownloaded: " + Md5JustDownloaded + ". Md5InUpdateBean: " + Md5InUpdateBean + ".");
                        }
                        proceedToUI(sp, file, fileExists, activity, versionName, updateBean, versionCode);
                    }
                });
            } else {
                Log.e("更新文件不存在", "true");
                proceedToUI(sp, file, false, activity, versionName, updateBean, versionCode);
            }
        } else {
            if (l != null) l.onUpdate(false, updateBean);
        }
        forceUpdate = false;
        uncancelable = false;
    }

    protected void proceedToUI(SharedPreferences sp, File file, boolean fileExists, Activity activity, String versionName, XdUpdateBean xdUpdateBean, int versionCode) {
        if (showNotification && !forceUpdate) {
            showNotification(sp, file, fileExists, activity, versionName, xdUpdateBean, versionCode);
        } else {
            showAlertDialog(sp, file, fileExists, activity, versionName, xdUpdateBean, versionCode);
        }
    }

    @SuppressWarnings("ResourceType")
    protected void showNotification(final SharedPreferences sp, final File file, final boolean fileExists, final Activity activity, final String versionName, final XdUpdateBean xdUpdateBean, final int versionCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return;
        activity.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                showAlertDialog(sp, file, fileExists, activity, versionName, xdUpdateBean, versionCode);
            }
        }, new IntentFilter("com.xdandroid.xdupdate.UpdateDialog"));
        activity.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                sp.edit()
                        .putLong("time", XdUpdateUtils.dayBegin(new Date()).getTime())
                        .putInt("versionCode", versionCode)
                        .putString("versionName", versionName)
                        .apply();
            }
        }, new IntentFilter("com.xdandroid.xdupdate.IgnoreUpdate"));
        int smallIconResId = iconResId > 0 ? iconResId : XdUpdateUtils.getAppIconResId(activity.getApplicationContext());
        Notification.Builder builder = new Notification.Builder(activity,channeId)
                .setAutoCancel(true)
                .setTicker(XdUpdateUtils.getApplicationName(activity.getApplicationContext()) + " " +  versionName + " " + XdConstants.hintText)
                .setSmallIcon(smallIconResId)
                .setContentTitle(XdUpdateUtils.getApplicationName(activity.getApplicationContext()) + " " + versionName + " " + XdConstants.hintText)
                .setContentText(xdUpdateBean.note)
                .setContentIntent(PendingIntent.getBroadcast(activity.getApplicationContext(), 1, new Intent("com.xdandroid.xdupdate.UpdateDialog"), PendingIntent.FLAG_CANCEL_CURRENT))
                .setDeleteIntent(PendingIntent.getBroadcast(activity.getApplicationContext(), 2, new Intent("com.xdandroid.xdupdate.IgnoreUpdate"), PendingIntent.FLAG_CANCEL_CURRENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setShowWhen(true);
            builder.setVibrate(new long[0]);
        }
        builder.setPriority(Notification.PRIORITY_HIGH);
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification m_Notification = builder.build();
        m_Notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
        manager.notify(1, m_Notification);
    }

    protected void showAlertDialog(final SharedPreferences sp, final File file, boolean fileExists, final Activity activity, final String versionName, final XdUpdateBean xdUpdateBean, final int versionCode) {
        if (customDialogIfc != null) {
            customDialog = customDialogIfc.setCustomDialog(sp, file, fileExists, activity, versionName, xdUpdateBean, versionCode, uncancelable);
        }else {
            AlertDialog.Builder builder = new AlertDialog
                    .Builder(activity)
                    .setCancelable(false)
                    .setTitle(versionName + " " + XdConstants.hintText)
                    .setMessage(xdUpdateBean.note);
            if (!uncancelable) {
                builder.setNegativeButton(XdConstants.laterText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sp.edit()
                                .putLong("time", XdUpdateUtils.dayBegin(new Date()).getTime())
                                .putInt("versionCode", versionCode)
                                .putString("versionName", versionName)
                                .apply();
                    }
                });
            }
            if (fileExists) {
                builder.setPositiveButton(XdConstants.installText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                });
            } else {
                builder.setPositiveButton(XdConstants.downloadText + "(" + XdUpdateUtils.formatToMegaBytes(xdUpdateBean.size) + "M)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, XdUpdateService.class);
                        intent.putExtra("xdUpdateBean", xdUpdateBean);
                        intent.putExtra("appIcon", iconResId);
                        activity.startService(intent);
                    }
                });
            }
            dialog = builder.create();
            if (!activity.isFinishing()) {
//                解决当activity finish后, 再点击通知弹出对话框时出现异常
                dialog.show();
            }
        }
    }

    public static class Builder {

        protected XdUpdateBean mUpdateBean;
        protected String mJsonUrl;
        protected boolean mAllow4G;
        protected int mIconResId;
        protected boolean mShowNotification = true;
        protected OnUpdateListener mListener;
        protected ShowCustomDialog mCustomDialog;

        public Builder setUpdateBean(XdUpdateBean updateBean) {
            mUpdateBean = updateBean;
            return this;
        }

        public Builder setJsonUrl(String jsonUrl) {
            mJsonUrl = jsonUrl;
            return this;
        }

        public Builder setAllow4G(boolean allow4G) {
            mAllow4G = allow4G;
            return this;
        }

        public Builder setIconResId(int iconResId) {
            mIconResId = iconResId;
            return this;
        }

        public Builder setShowNotification(boolean showNotification) {
            mShowNotification = showNotification;
            return this;
        }

        public Builder setOnUpdateListener(OnUpdateListener l) {
            mListener = l;
            return this;
        }

        /**
         * 设置自定义对话框
         * @param customDialog
         * @return
         */
        public Builder setCustomDialog(ShowCustomDialog customDialog) {
            mCustomDialog = customDialog;
            return this;
        }

        public Builder setDebugMode(boolean debugMode) {
            XdConstants.debugMode = debugMode;
            return this;
        }

        public Builder setDownloadText(String downloadText) {
            if (!TextUtils.isEmpty(downloadText)) XdConstants.downloadText = downloadText;
            return this;
        }

        public Builder setInstallText(String installText) {
            if (!TextUtils.isEmpty(installText)) XdConstants.installText = installText;
            return this;
        }

        public Builder setLaterText(String laterText) {
            if (!TextUtils.isEmpty(laterText)) XdConstants.laterText = laterText;
            return this;
        }

        public Builder setHintText(String hintText) {
            if (!TextUtils.isEmpty(hintText)) XdConstants.hintText = hintText;
            return this;
        }

        public Builder setDownloadingText(String downloadingText) {
            if (!TextUtils.isEmpty(downloadingText)) XdConstants.downloadingText = downloadingText;
            return this;
        }

        public XdUpdateAgent build() {
            if (instance == null) instance = new XdUpdateAgent();
            if (mUpdateBean != null) {
                instance.updateBeanLocallyProvided = mUpdateBean;
            } else {
                instance.jsonUrl = mJsonUrl;
            }
            instance.allow4G = mAllow4G;
            instance.iconResId = mIconResId;
            instance.showNotification = mShowNotification;
            instance.l = mListener;
            instance.customDialogIfc = mCustomDialog;
            return instance;
        }
    }

    public interface OnUpdateListener {
        public void onUpdate(boolean needUpdate, XdUpdateBean updateBean);
    }

    public interface ShowCustomDialog<T> {
        T setCustomDialog(final SharedPreferences sp, final File file, boolean fileExists, final Activity activity, final String versionName, final XdUpdateBean xdUpdateBean, final int versionCode, final boolean uncancelable);
        void destroyDialog(T dialog);
    }
}
