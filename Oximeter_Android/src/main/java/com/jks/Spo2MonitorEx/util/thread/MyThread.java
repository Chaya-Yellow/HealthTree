package com.jks.Spo2MonitorEx.util.thread;

/**
 * Created by apple on 16/7/16.
 */
public class MyThread {
    public static Thread startNewThread(Runnable runnable) {
        Thread  thread =  new Thread(runnable);
        thread.start();
        return thread;
    }
}
