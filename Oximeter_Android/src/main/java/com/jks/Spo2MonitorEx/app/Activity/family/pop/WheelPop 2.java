package com.jks.Spo2MonitorEx.app.Activity.family.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.view.MyWheel5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/8/31.
 */
public class WheelPop extends PopupWindow implements View.OnClickListener {
    private static Context context;
    private static WheelPop instance;
    private View view;
    private TextView eliminate, user_register_dialog_btn_sure;
    private TextView popViewTitle;

    private static String title = null;

    private static View parent;
    private static Activity activity;
    private View vPeriodWheel1;
    private static TextView tv;
    private static Config config;
    private static List<String> list;

    /**
     * 获取弹出框单例
     * @param activity
     * @param config
     * @param tv 改变的textview
     * @param title
     * @param list 滚轮数据的数组
     * @param parent 父容器
     * @return
     */
    public static WheelPop newInstance(Activity activity,Config config,TextView tv, String title, List<String> list, View parent) {
        WheelPop.context = activity;
        WheelPop.parent = parent;
        WheelPop.config = config;
        WheelPop.activity = activity;
        WheelPop.tv = tv ;
        WheelPop.list = list;
        WheelPop.title = title;

        instance = new WheelPop(context);
        return instance;
    }

    private WheelPop(Context context) {
        super(context);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pop_selector, null);
        }
        this.setContentView(view);
        init();
        initView();

    }

    private void init() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 弹出窗体可点击
        this.setFocusable(true);
        // 弹出窗体动画效果
        // this.setAnimationStyle(R.style.PopupAnimation);
        // // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
                R.color.transparent));
        // 弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
    }

    private void initView() {
        eliminate = (TextView) view.findViewById(R.id.eliminate);
        user_register_dialog_btn_sure = (TextView) view
                .findViewById(R.id.user_register_dialog_btn_sure);
        popViewTitle = (TextView) view.findViewById(R.id.pop_view_title);

        popViewTitle.setText(WheelPop.title != null ? WheelPop.title : "");

        eliminate.setOnClickListener(this);
        eliminate.setVisibility(View.GONE);
        user_register_dialog_btn_sure.setOnClickListener(this);

        vPeriodWheel1 = view.findViewById(R.id.add_user_Data_list3);
        // 初始化天数滚轮。
        List<String> name = WheelPop.list;
        List<String> list = new ArrayList<String>();
        if(tv.getText().toString().trim().length()<=0){
            tv.setText(name.get(0));
        }
        for (String a : name) {
            list.add(a);
        }
        new MyWheel5(activity, vPeriodWheel1, tv, list, ":");
    }

    public void loadView() {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_register_dialog_btn_sure:
                dismiss();
                break;
            case R.id.eliminate:
                tv.setText("");
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
//        config.getTodayHandler().sendEmptyMessage(Constant.MESSAGE_POP);
        super.dismiss();
    }
}
