package com.jks.Spo2MonitorEx.util;

/**
 * Created by apple on 16/7/16.
 */
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jks.Spo2MonitorEx.R;

public class CheckNet {
    public static boolean isWifi(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null) {
            return true;
        } else {
            return false;
        }
    }

    /** 没有网络的时候弹出提示 */
    public static boolean checkNetwork(final Context myContext) {
        // ProgressDialogUtil.show(myContext);
        boolean flag = false;
        boolean flag2 = false;
        ConnectivityManager CM = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (CM.getActiveNetworkInfo() != null && CM.getActiveNetworkInfo().isConnected()) {
            flag = CM.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED;
        }
        if (CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null
                && CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            flag2 = CM.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        }
        if (!flag && !flag2) {
            Builder b = new AlertDialog.Builder(myContext)
                    // .setTitle("没有可用的网络")
                    .setMessage(R.string.checknet_nonet_to_link);
            b.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        myContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
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
        return flag || flag2;
    }
}
