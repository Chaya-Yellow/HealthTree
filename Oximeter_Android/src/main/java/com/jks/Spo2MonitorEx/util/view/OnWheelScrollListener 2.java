package com.jks.Spo2MonitorEx.util.view;

/**
 * Created by apple on 16/8/31.
 */
/**
 * Wheel scrolled listener interface.
 */
public interface OnWheelScrollListener {
    /**
     * Callback method to be invoked when scrolling started.
     *
     * @param wheel
     *            the wheel view whose state has changed.
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     *
     * @param wheel
     *            the wheel view whose state has changed.
     * @throws Exception
     */
    void onScrollingFinished(WheelView wheel) throws Exception;
}
