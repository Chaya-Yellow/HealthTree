package com.jks.Spo2MonitorEx.util.web;

/**
 * Created by apple on 16/7/16.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;

import com.jks.Spo2MonitorEx.R;

public class CheckNetwork {
    /** 没有网络的时候弹出提示 */
    public static boolean checkNetwork(final Context myContext) {
        // ProgressDialogUtil.show(myContext);
        boolean flag = false;
        ConnectivityManager CM = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (CM.getActiveNetworkInfo() != null)
            flag = CM.getActiveNetworkInfo().isAvailable();
        if (!flag) {
            Builder b = new AlertDialog.Builder(myContext)
                    // .setTitle("没有可用的网络")
                    .setMessage(R.string.checknet_nonet_to_link);
            b.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        myContext.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    } else {
                        myContext.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            }).setNeutralButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            }).create();
            b.show();
        }

        return flag;
    }

    /** 单纯检查网络 */
    public static boolean checkNetwork3(final Context myContext) {
        // ProgressDialogUtil.show(myContext);
        boolean flag = false;
        ConnectivityManager CM = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (CM.getActiveNetworkInfo() != null)
            flag = CM.getActiveNetworkInfo().isAvailable();
        return flag;
    }

    public static boolean checkNetwork2(final Activity myContext) {
        // ProgressDialogUtil.show(myContext);
        boolean flag = false;
        ConnectivityManager CM = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (CM.getActiveNetworkInfo() != null)
            flag = CM.getActiveNetworkInfo().isAvailable();
        if (!flag) {
            Builder b = new AlertDialog.Builder(myContext).setMessage(R.string.checknet_nonet_to_link);
            b.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent mIntent = new Intent("/");
                    ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    mIntent.setComponent(comp);
                    mIntent.setAction("android.intent.action.VIEW");
                    myContext.startActivity(mIntent);

                }
            }).setNeutralButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                    myContext.finish();
                }
            }).create();
            b.show();
        }

        return flag;
    }


}
