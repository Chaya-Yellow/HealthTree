package com.jks.Spo2MonitorEx.util.dialog;

import android.content.Context;
import android.os.Handler;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by apple on 16/7/16.
 */
public class DialogUtil {
    private static KProgressHUD hud;
    private static Context hudContext;

    public static void show(Context context, int msgId) {
        hudContext = context;
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getResources().getString(msgId))
                .setDimAmount(0.5f)
                .setCancellable(true);
        hud.show();
    }

    public static void dismiss() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    public static void setMsg(int msgId) {
        if (hud != null) {
            hud.setLabel(hudContext.getResources().getString(msgId));
        }
    }

    public static void dismiss2Msg(int msgId) {
        hud.setLabel(hudContext.getResources().getString(msgId));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               hud.dismiss();
            }
        }, 1000);
    }

    public static void dismiss2Msg(String msg) {
        hud.setLabel(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }
}
