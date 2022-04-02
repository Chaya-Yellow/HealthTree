package com.jks.Spo2MonitorEx.util;

import android.widget.TextView;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * Created by apple on 2016/10/11.
 */
public class TextUtil {
    /**
     * 设置文本后面有三点在跳
     * @param textView
     */
    public static void setTextDotJumping(TextView textView) {
        JumpingBeans.with(textView)
                .appendJumpingDots()
                .build();
    }
}
