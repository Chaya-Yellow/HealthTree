package com.jks.Spo2MonitorEx.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by apple on 16/9/5.
 */
public class MyScrollView extends ScrollView {

    GestureDetector gestureDetector;
    private float initY = 0;
    private ScrollViewUpOrDown scrollViewUpOrDown;
    public static final int MONTHGRIDVIEW = 2;
    public static final int WEEKGRIDVIEW = 1;
    private GestureDetector mGestureDetector;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
        setHorizontalFadingEdgeEnabled(false);
    }

    public void setGestureDetector(GestureDetector gestureDetector, ScrollViewUpOrDown scrollViewUpOrDown) {
        this.gestureDetector = gestureDetector;
        this.scrollViewUpOrDown = scrollViewUpOrDown;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getY() - initY > 50) {
                    scrollViewUpOrDown.scroll(MyScrollView.MONTHGRIDVIEW);
                } else if (event.getY() - initY < 50) {
                    scrollViewUpOrDown.scroll(MyScrollView.WEEKGRIDVIEW);
                }
                break;
            default:
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    public interface ScrollViewUpOrDown {
        public void scroll(int upOrDown);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        super.dispatchTouchEvent(ev);
        return true;
    }

}