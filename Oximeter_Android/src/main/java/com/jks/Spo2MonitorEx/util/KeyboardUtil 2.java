package com.jks.Spo2MonitorEx.util;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by apple on 2016/10/14.
 */
public class KeyboardUtil {
    private static int frontRect_top = 0;

    /**
     * 键盘弹出后, 界面滑动到指定scrollToView的底部
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView
     */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                if (rootInvisibleHeight > 100) {
                    //软键盘弹出来的时候
                    int[] location = new int[2];
                    // 获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    // 计算root滚动高度，使scrollToView在可见区域的底部
                    int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom + 10;
                    if (scrollHeight != 0) {
                        startAnimation(0, scrollHeight, root);
                        frontRect_top = scrollHeight;
                        root.scrollTo(0, scrollHeight);
                    }
                } else {
                    // 软键盘没有弹出来的时候
                    startAnimation(frontRect_top, 0, root);
                    frontRect_top = 0;
//                    root.scrollTo(0, 0);
                }
            }
        });
    }

    private static void startAnimation(int from, final int to, final View view) {
        ValueAnimator toDestAnim = ValueAnimator.ofInt(from, to);
        toDestAnim.setDuration(400);
        toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        toDestAnim.start();
        toDestAnim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.scrollTo(0, (int) animation.getAnimatedValue());
            }
        });
        toDestAnim.start();
    }
}
