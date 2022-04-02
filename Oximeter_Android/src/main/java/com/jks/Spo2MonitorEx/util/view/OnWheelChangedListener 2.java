package com.jks.Spo2MonitorEx.util.view;

/**
 * Created by apple on 16/8/31.
 */
/**
 * Wheel changed listener interface.
 * <p>
 * The currentItemChanged() method is called whenever current wheel positions is
 * changed:
 * <li>New Wheel position is set
 * <li>Wheel view is scrolled
 */
public interface OnWheelChangedListener {
    /**
     * Callback method to be invoked when current item changed
     *
     * @param wheel
     *            the wheel view whose state has changed
     * @param oldValue
     *            the old value of current item
     * @param newValue
     *            the new value of current item
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}