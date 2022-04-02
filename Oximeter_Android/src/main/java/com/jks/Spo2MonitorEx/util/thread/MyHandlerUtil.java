package com.jks.Spo2MonitorEx.util.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by apple on 16/8/30.
 */
public class MyHandlerUtil {
    /**
     * 公用的发送通知
     *
     * @param flag
     * @param handler
     * @param object
     */
    public static void sendMsg(int flag, Handler handler, Object object) {
        Message msg = new Message();
        msg.what = flag;
        msg.obj = object;
        if (handler != null) {
            handler.sendMessage(msg);
        }else {
            Log.e("handler", "为空");
        }
    }
}
