package com.jks.Spo2MonitorEx.util;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;

/**
 * Created by apple on 16/8/23.
 */
public class TitlebarUtil {
    /**
     * 显示标题
     *
     * @param activity 当前的activity
     * @param titleName 标题
     */
    public static void showTitleName(Activity activity, String titleName) {
        TextView tvTitleName = (TextView) activity.findViewById(R.id.tv_title);
        tvTitleName.setText(titleName);
    }

    /**
     * 显示标题
     *
     * @param activity
     *            当前的activity
     * @param titleNameId
     *            标题id
     */
    public static void showTitleName(Activity activity, int titleNameId) {
        TextView tvTitleName = (TextView) activity.findViewById(R.id.tv_title);
        tvTitleName.setText(titleNameId);
    }

    /**
     * 显示标题
     *
     * @param activity
     *            当前的activity
     * @param titleNameId
     *            标题id
     */
    public static TextView showTitleNameGetView(Activity activity, int titleNameId) {
        TextView tvTitleName = (TextView) activity.findViewById(R.id.tv_title);
        tvTitleName.setText(titleNameId);
        return tvTitleName;
    }

    /**
     * 显示标题
     *
     * @param activity
     *            当前的activity
     * @param titleName
     *            标题id
     */
    public static TextView showTitleNameGetView(Activity activity, String titleName) {
        TextView tvTitleName = (TextView) activity.findViewById(R.id.tv_title);
        tvTitleName.setText(titleName);
        return tvTitleName;
    }

    /**
     * 显示progressbar
     *
     * @param activity
     *            当前的activity
     * @return
     */
    public static ProgressBar showProgressBar(Activity activity) {
        ProgressBar progressbar = (ProgressBar) activity.findViewById(R.id.title_progessbar);
        // progressbar.setVisibility(View.VISIBLE);
        return progressbar;
    }

    /**
     * 显示箭头
     *
     * @param activity
     *            当前的activity
     */
    public static ImageView showTitleArrow(Activity activity) {
        ImageView imageView = (ImageView) activity.findViewById(R.id.tv_title_image);
        imageView.setVisibility(View.VISIBLE);
        return imageView;
    }

    public static RelativeLayout getRelativeLayout(Activity activity) {
        RelativeLayout relativelayout = (RelativeLayout) activity.findViewById(R.id.tv_title_relative_layout);
        return relativelayout;
    }

    /**
     * 显示左边Button按钮
     *
     * @param activity
     *            当前的activity
     * @return
     */
    public static Button showBtnLeft(Activity activity) {
        Button btnLeft = (Button) activity.findViewById(R.id.btn_title_left);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setText(R.string.title_back);
        return btnLeft;
    }

    /**
     * 显示右边的Button按钮
     *
     * @param activity
     *            当前的activity
     * @return
     */
    public static Button showBtnRight(Activity activity) {
        Button btnRight = (Button) activity.findViewById(R.id.btn_title_right);
        btnRight.setVisibility(View.VISIBLE);
        return btnRight;
    }

    /**
     * 显示左边的ImageButton按钮
     *
     * @param activity
     *            当前的activity
     * @param drawableId
     *            iamgebutton的图标按钮
     * @return
     */
    public static ImageButton showIbLeft(Activity activity, int drawableId) {
        ImageButton btnLeft = (ImageButton) activity.findViewById(R.id.ib_title_left);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setImageResource(drawableId);
        return btnLeft;
    }

    /**
     * 显示左边的ImageButton按钮
     *
     * @param activity
     *            当前的activity
     * @return
     */
    public static ImageButton showIbLeft(Activity activity) {
        ImageButton btnLeft = (ImageButton) activity.findViewById(R.id.ib_title_left);
        btnLeft.setVisibility(View.VISIBLE);
        return btnLeft;
    }

    /**
     * 显示右边的ImageButton按钮
     *
     * @param activity
     *            当前的activity
     * @param drawableId
     *            iamgebutton的图标按钮
     * @return
     */
    public static ImageButton showIbRight(Activity activity, int drawableId) {
        ImageButton btnRight = (ImageButton) activity.findViewById(R.id.ib_title_right);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(drawableId);
        return btnRight;
    }

    public static ImageButton showIbRight(Activity activity) {
        ImageButton btnRight = (ImageButton) activity.findViewById(R.id.ib_title_right);
        btnRight.setVisibility(View.VISIBLE);
        return btnRight;
    }

    /**
     * 显示标题
     *
     * @param activity
     *            当前的activity
     * @param tvRightName
     *            显示注册的TextView
     */
    public static TextView showTvRight(Activity activity, String tvRightName) {
        TextView tvTitleRight = (TextView) activity.findViewById(R.id.tv_title_right);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText(tvRightName);
        return tvTitleRight;
    }

    /**
     * 显示标题
     *
     * @param activity
     *            当前的activity
     * @param tvRightNameId
     *            显示注册的TextView
     */
    public static TextView showTvRight(Activity activity, int tvRightNameId) {
        TextView tvTitleRight = (TextView) activity.findViewById(R.id.tv_title_right);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText(tvRightNameId);
        return tvTitleRight;

    }
}
