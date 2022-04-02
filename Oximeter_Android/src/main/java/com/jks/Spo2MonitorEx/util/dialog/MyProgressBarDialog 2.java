package com.jks.Spo2MonitorEx.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;

/**
 * Created by apple on 16/7/16.
 */
public class MyProgressBarDialog extends Dialog {
    private Context context;
    private ProgressBar progressBar;
    private TextView txt;
    private View view;

    public MyProgressBarDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        // 加载布局文件
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dialog, null);
        progressBar = (ProgressBar) view.findViewById(R.id.dialog_progressBar);
        txt = (TextView) view.findViewById(R.id.dialog_text);
        // 给图片添加动态效果
        Animation anim = AnimationUtils.loadAnimation(context,
                R.anim.enteralpha);
        view.setAnimation(anim);
        setCanceledOnTouchOutside(false);
        // dialog添加视图
        setContentView(view);

    }

    public void setMsg(String msg) {
        if(txt !=null){
            txt.setText(msg);
        }
    }

    public void setMsg(int msgId) {
        if(txt != null){
            txt.setText(msgId);
        }
    }

    public void dismiss() {
        // Animation anim=AnimationUtils.loadAnimation(context,
        // R.anim.exitalpha);
        // view.startAnimation(null);
        super.dismiss();
    }
}
