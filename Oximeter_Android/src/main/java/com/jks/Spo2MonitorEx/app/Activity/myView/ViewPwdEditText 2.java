package com.jks.Spo2MonitorEx.app.Activity.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.jks.Spo2MonitorEx.R;

/**
 * Created by apple on 16/8/23.
 */
public class ViewPwdEditText extends EditText implements TextWatcher, View.OnFocusChangeListener {
    private Drawable mClearDrawable;

    public ViewPwdEditText(Context context) {
        this(context, null);
    }

    public ViewPwdEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ViewPwdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2]; // 在右边显示删除图片
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.user_cet_btn_view_pwd);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setViewIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setViewIconVisible(s.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    /**
     * 设置图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setViewIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if (hasFocus) {
            setViewIconVisible(getText().length() > 0);
        } else {
            setViewIconVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (getCompoundDrawables()[2] != null) {
            boolean touchable = event.getX() > (getWidth()
                    - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                    && (event.getX() < ((getWidth() - getPaddingRight())));

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (touchable) {
                    this.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD) ;
                }
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (touchable) {
                    this.setInputType(InputType.TYPE_NULL) ;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

    }

}
