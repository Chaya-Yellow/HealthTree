package com.jks.Spo2MonitorEx.util.other;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by apple on 16/8/23.
 */
public class BLTToast {
    // 用于显示提示的
    public static void show(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    // 用于显示提示的
    public static void show(Context context, int msgId) {
        Toast toast = Toast.makeText(context, msgId, Toast.LENGTH_SHORT);
        toast.show();
    }

    // 用于显示提示的
    public static void showCenter(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // 用于显示提示的
    public static void showView(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ProgressBar pb = new ProgressBar(context);
        toastView.addView(pb, 0);
        toast.show();
    }
}
