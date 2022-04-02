package com.jks.Spo2MonitorEx.util.listener;

import android.os.Handler;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.thread.MyHandlerUtil;

/**
 * Created by apple on 16/9/5.
 */
public class CommonGestureListener extends SimpleOnGestureListener {

    private LinearLayout vf;
    private Handler handler;

    public CommonGestureListener(LinearLayout vf, Handler handler) {
        this.vf = vf;
        this.handler = handler;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int[] location = new int[2];
        vf.getLocationInWindow(location);
        int y = location[1];
        // 高度在ViewFlipper之间时才滑动
        if (y < e1.getRawY() && (y + vf.getMeasuredHeight()) > e1.getRawY() && y < e2.getRawY()
                && (y + vf.getMeasuredHeight()) > e2.getRawY())

        {
            if (e1.getX() - e2.getX() > 30 && Math.abs(velocityX) > 15) {
                // 向左
                MyHandlerUtil.sendMsg(Constant.GESTURELISTENER_UPUI_LEFT, handler, null);

            } else if (e2.getX() - e1.getX() > 30 && Math.abs(velocityX) > 15) {
                // 向右
                MyHandlerUtil.sendMsg(Constant.GESTURELISTENER_UPUI_RIGHT, handler, null);
            }
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

}