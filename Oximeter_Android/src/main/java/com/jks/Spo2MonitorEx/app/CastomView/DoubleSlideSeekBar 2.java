package com.jks.Spo2MonitorEx.app.CastomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;

import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PI_H_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PI_L_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PR_H_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.PR_L_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.SP_H_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Key.SP_L_VALUE;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_L_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_VALUE_MAX;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PI_VALUE_MIN;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_L_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_VALUE_MAX;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.PR_VALUE_MIN;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_H_VALUE_DEFAULT;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_VALUE_MAX;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_VALUE_MIN;
import static com.jks.Spo2MonitorEx.app.constants.RemindConstants.Value.SP_L_VALUE_DEFAULT;

public class DoubleSlideSeekBar extends View {

    /**
     * ??????????????????????????????
     */
    private int lineWidth;
    /**
     * ??????????????????????????????
     */
    private int lineLength = 400;
    /**
     * ?????????????????? 100
     */
    private int textHeight;
    /**
     * ?????? ????????????
     */
    private int imageWidth;
    /**
     * ?????? ????????????
     */
    private int imageHeight;
    /**
     * ??????????????????
     */
    private boolean hasRule;
    /**
     * ???????????????????????????
     */
    private boolean isLowerMoving;
    /**
     * ???????????????????????????
     */
    private boolean isUpperMoving;
    /**
     * ????????????
     */
    private int textSize;
    /**
     * ????????????
     */
    private int textColor;
    /**
     * ?????????????????? ???????????????????????????
     */
    private int inColor = Color.GREEN;
    /**
     * ?????????????????? ???????????????????????????
     */
    private int outColor = Color.WHITE;
    /**
     * ???????????????
     */
    private int ruleColor = Color.BLUE;
    /**
     * ?????????????????? ?????????
     */
    private int ruleTextColor = Color.BLUE;
    /**
     * ?????????????????????
     */
    private Bitmap bitmapLow;
    /**
     * ???????????? ?????????
     */
    private Bitmap bitmapBig;
    /**
     * ??????????????????X????????????
     */
    private int slideLowX;
    /**
     * ??????????????????X????????????
     */
    private int slideBigX;
    /**
     * ?????????????????? ??????
     */
    private int bitmapHeight;
    /**
     * ?????????????????? ??????
     */
    private int bitmapWidth;
    /**
     * ?????????padding ?????????????????? ????????????????????????view??????????????????
     */
    private int paddingLeft = 20;
    private int paddingRight = 20;
    private int paddingTop = 16;
    private int paddingBottom = 8;
    /**
     * ?????????????????? ???????????????
     */
    private int lineStart = paddingLeft;
    /**
     * ??????Y?????????
     */
    private int lineY;
    /**
     * ?????????????????????????????????
     */
    private int lineEnd = lineLength + paddingLeft;
    /**
     * ?????????????????????
     */
    private int bigValue = 100;
    /**
     * ?????????????????????
     */
    private int smallValue = 0;
    /**
     * ???????????????????????????
     */
    private int smallRange;
    /**
     * ???????????????????????????
     */
    private int bigRange;
    /**
     * ??????
     */
    private String unit = " ";
    /**
     * ????????????
     */
    private int equal = 20;
    /**
     * ????????????
     */
    private String ruleUnit = " ";
    /**
     * ?????????????????????size
     */
    private int ruleTextSize = 20;
    /**
     * ??????????????????
     */
    private int ruleLineHeight = 20;
    private Paint linePaint;
    private Paint bitmapPaint;
    private Paint textPaint;
    private Paint paintRule;

    public DoubleSlideSeekBar(Context context) {
        this(context, null);
    }

    public DoubleSlideSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleSlideSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DoubleSlideSeekBar, defStyleAttr, 0);
        inColor = typedArray.getColor(R.styleable.DoubleSlideSeekBar_inColor, Color.BLACK);
        lineWidth = typedArray.getDimensionPixelOffset(R.styleable.DoubleSlideSeekBar_lineHeight, dip2px(getContext(), 10));
        outColor = typedArray.getColor(R.styleable.DoubleSlideSeekBar_outColor, Color.YELLOW);
        textColor = typedArray.getColor(R.styleable.DoubleSlideSeekBar_textColor, Color.BLUE);
        textSize = typedArray.getDimensionPixelOffset(
                R.styleable.DoubleSlideSeekBar_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
        );
        int bitmapLowRes = typedArray.getResourceId(R.styleable.DoubleSlideSeekBar_imageLow, 0);
        int bitmapBigRes = typedArray.getResourceId(R.styleable.DoubleSlideSeekBar_imageBig, 0);
        if (bitmapLowRes != 0) {
            bitmapLow = BitmapFactory.decodeResource(getResources(), bitmapLowRes);
        }
        if (bitmapBigRes != 0) {
            bitmapBig = BitmapFactory.decodeResource(getResources(), bitmapBigRes);
        }
        imageHeight = typedArray.getDimensionPixelOffset(R.styleable.DoubleSlideSeekBar_imageheight, dip2px(getContext(), 20));
        imageWidth = typedArray.getDimensionPixelOffset(R.styleable.DoubleSlideSeekBar_imagewidth, dip2px(getContext(), 20));
        hasRule = typedArray.getBoolean(R.styleable.DoubleSlideSeekBar_hasRule, false);
        ruleColor = typedArray.getColor(R.styleable.DoubleSlideSeekBar_ruleColor, Color.BLUE);
        ruleTextColor = typedArray.getColor(R.styleable.DoubleSlideSeekBar_ruleTextColor, Color.BLUE);
        unit = typedArray.getString(R.styleable.DoubleSlideSeekBar_unit);
        if (unit == null) {
            unit = " ";
        }
        equal = typedArray.getInt(R.styleable.DoubleSlideSeekBar_equal, 10);
        ruleUnit = typedArray.getString(R.styleable.DoubleSlideSeekBar_ruleUnit);
        if (ruleUnit == null) {
            ruleUnit = " ";
        }
        ruleTextSize = typedArray.getDimensionPixelOffset(
                R.styleable.DoubleSlideSeekBar_ruleTextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
        );
        ruleLineHeight = typedArray.getDimensionPixelOffset(R.styleable.DoubleSlideSeekBar_ruleLineHeight, dip2px(getContext(), 10));
        String type = typedArray.getString(R.styleable.DoubleSlideSeekBar_currType);
        if ("sp".equals(type)) {
            //884  17.68   3.9288  44.2
            bigValue = SP_VALUE_MAX;
            smallValue = SP_VALUE_MIN;
            bigRange = Integer.parseInt(SharedPreferencesUtil.getStringByKey(
                    context, SP_H_VALUE, SP_H_VALUE_DEFAULT
            ));
            smallRange = Integer.parseInt(SharedPreferencesUtil.getStringByKey(
                    context, SP_L_VALUE, SP_L_VALUE_DEFAULT
            ));
        } else if ("pr".equals(type)) {
            bigValue = PR_VALUE_MAX;
            smallValue = PR_VALUE_MIN;
            bigRange = Integer.parseInt(SharedPreferencesUtil.getStringByKey(
                    context, PR_H_VALUE, PR_H_VALUE_DEFAULT
            ));
            smallRange = Integer.parseInt(SharedPreferencesUtil.getStringByKey(
                    context, PR_L_VALUE, PR_L_VALUE_DEFAULT
            ));
        } else if ("pi".equals(type)) {
            bigValue = PI_VALUE_MAX;
            smallValue = PI_VALUE_MIN;
            bigRange = Integer.parseInt(SharedPreferencesUtil.getStringByKey(
                    context, PI_H_VALUE, PI_H_VALUE_DEFAULT
            ));
            smallRange = Integer.parseInt(SharedPreferencesUtil.getStringByKey(
                    context, PI_L_VALUE, PI_L_VALUE_DEFAULT
            ));
        } else {
            bigValue = typedArray.getInteger(R.styleable.DoubleSlideSeekBar_bigValue, 100);
            smallValue = typedArray.getInteger(R.styleable.DoubleSlideSeekBar_smallValue, 100);
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        /**??????????????????*/
        if (bitmapLow == null) {
            bitmapLow = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hp_a);
        }
        if (bitmapBig == null) {
            bitmapBig = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hp_b);
        }
        /**??????????????????????????? ???????????????????????????????????????????????????????????????*/
        bitmapHeight = bitmapLow.getHeight();
        bitmapWidth = bitmapLow.getWidth();
        // ?????????????????????
        int newWidth = imageWidth;
        int newHeight = imageHeight;
        // ??????????????????
        float scaleWidth = ((float) newWidth) / bitmapWidth;
        float scaleHeight = ((float) newHeight) / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        /**????????????*/
        bitmapLow = Bitmap.createBitmap(bitmapLow, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        bitmapBig = Bitmap.createBitmap(bitmapBig, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        /**?????????????????????????????????*/
        bitmapHeight = bitmapLow.getHeight();
        bitmapWidth = bitmapLow.getWidth();
        /**??????????????????????????????*/

        //slideLowX = lineStart;
        //slideBigX = lineEnd;
        //smallRange = smallValue;
        //bigRange = bigValue;
        if (hasRule) {
            //???????????? paddingTop ????????????text????????????????????????????????????????????????????????????????????? ??????????????????
            paddingTop = paddingTop + Math.max(textSize, ruleLineHeight + ruleTextSize);
        } else {
            //??????????????? paddingTop ?????? text?????????
            paddingTop = paddingTop + textSize;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMyMeasureWidth(widthMeasureSpec);
        int height = getMyMeasureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        slideBigX = computeSlideX(bigRange);
        slideLowX = computeSlideX(smallRange);
    }

    private int getMyMeasureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            // matchparent ?????? ???????????? view???????????? paddingBottom + paddingTop + bitmapHeight + 10 ??????????????????
            size = Math.max(size, paddingBottom + paddingTop + bitmapHeight + 10);
        } else {
            //wrap content
            int height = paddingBottom + paddingTop + bitmapHeight + 10;
            size = Math.min(size, height);
        }
        return size;
    }

    private int getMyMeasureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {

            size = Math.max(size, paddingLeft + paddingRight + bitmapWidth * 2);

        } else {
            //wrap content
            int width = paddingLeft + paddingRight + bitmapWidth * 2;
            size = Math.min(size, width);
        }
        // match parent ?????? ???????????? ?????????????????????????????????????????????
        lineLength = size - paddingLeft - paddingRight - bitmapWidth;
        //?????????????????????????????????
        lineEnd = lineLength + paddingLeft + bitmapWidth / 2;
        //?????????????????????????????????
        lineStart = paddingLeft + bitmapWidth / 2;
        //????????? ????????????
        //slideBigX = lineEnd;
        //slideLowX = lineStart;
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Y??? ??????
        lineY = getHeight() - paddingBottom - bitmapHeight / 2;
        // ???????????????
        textHeight = lineY - bitmapHeight / 2 - 10;
        //???????????????
        if (hasRule) {
            drawRule(canvas);
        }
        if (linePaint == null) {
            linePaint = new Paint();
        }
        //????????????
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(inColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(slideLowX, lineY, slideBigX, lineY, linePaint);
        linePaint.setColor(outColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        //??? ?????????
        canvas.drawLine(lineStart, lineY, slideLowX, lineY, linePaint);
        canvas.drawLine(slideBigX, lineY, lineEnd, lineY, linePaint);
        //?????????
        if (bitmapPaint == null) {
            bitmapPaint = new Paint();
        }
        canvas.drawBitmap(bitmapLow, slideLowX - bitmapWidth / 2, lineY - bitmapHeight / 2, bitmapPaint);
        canvas.drawBitmap(bitmapBig, slideBigX - bitmapWidth / 2, lineY - bitmapHeight / 2, bitmapPaint);
        //??? ??????????????????
        if (textPaint == null) {
            textPaint = new Paint();
        }
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        canvas.drawText(String.valueOf(smallRange + unit), slideLowX - bitmapWidth / 2f, textHeight, textPaint);
        canvas.drawText(String.valueOf(bigRange + unit), slideBigX - bitmapWidth / 2f, textHeight, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //????????????
        super.onTouchEvent(event);
        float nowX = event.getX();
        float nowY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //?????? ??????????????????
                boolean rightY = Math.abs(nowY - lineY) < bitmapHeight / 2f;
                //?????? ??????????????????
                boolean lowSlide = Math.abs(nowX - slideLowX) < bitmapWidth / 2f;
                //?????? ??????????????????
                boolean bigSlide = Math.abs(nowX - slideBigX) < bitmapWidth / 2f;
                if (rightY && lowSlide) {
                    isLowerMoving = true;
                } else if (rightY && bigSlide) {
                    isUpperMoving = true;
                    //????????????????????? ?????????
                } else if (nowX >= lineStart && nowX <= slideLowX - bitmapWidth / 2f && rightY) {
                    slideLowX = (int) nowX;
                    updateRange();
                    postInvalidate();
                } else if (nowX <= lineEnd && nowX >= slideBigX + bitmapWidth / 2 && rightY) {
                    slideBigX = (int) nowX;
                    updateRange();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //???????????????????????????
                if (isLowerMoving) {
                    //?????? X??????????????? ???????????????????????????
                    if (nowX <= slideBigX - bitmapWidth && nowX >= lineStart - bitmapWidth / 2f) {
                        slideLowX = (int) nowX;
                        if (slideLowX < lineStart) {
                            slideLowX = lineStart;
                        }
                        //????????????
                        updateRange();
                        postInvalidate();
                    }

                } else if (isUpperMoving) {
                    //?????? X??????????????? ???????????????????????????
                    if (nowX >= slideLowX + bitmapWidth && nowX <= lineEnd + bitmapWidth / 2f) {
                        slideBigX = (int) nowX;
                        if (slideBigX > lineEnd) {
                            slideBigX = lineEnd;
                        }
                        //????????????
                        updateRange();
                        postInvalidate();

                    }
                }
                break;
            //????????????
            case MotionEvent.ACTION_UP:
                isUpperMoving = false;
                isLowerMoving = false;
                break;
            default:
                break;
        }

        return true;
    }

    private void updateRange() {
        //?????? ??????????????????
        smallRange = computeRange(slideLowX);
        //?????? ??????????????????
        bigRange = computeRange(slideBigX);
        //?????? ??????????????????
        if (onRangeListener != null) {
            onRangeListener.onRange(smallRange, bigRange);
        }
    }

    /**
     * ???????????????
     */
    private int computeRange(int range) {
        return (range - lineStart) * (bigValue - smallValue) / lineLength + smallValue;
    }

    private int computeSlideX(int value) {
        return (value - smallValue) * lineLength / (bigValue - smallValue) + lineStart;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * ?????????
     */
    protected void drawRule(Canvas canvas) {
        if (paintRule == null) {
            paintRule = new Paint();
        }
        paintRule.setStrokeWidth(1);
        paintRule.setTextSize(ruleTextSize);
        paintRule.setTextAlign(Paint.Align.CENTER);
        paintRule.setAntiAlias(true);
        //?????? equal???,?????????
        for (int i = smallValue; i <= bigValue; i += (bigValue - smallValue) / equal) {
            float degX = lineStart + i * lineLength / (bigValue - smallValue);
            int degY = lineY - ruleLineHeight;
            paintRule.setColor(ruleColor);
            canvas.drawLine(degX, lineY, degX, degY, paintRule);
            paintRule.setColor(ruleTextColor);
            canvas.drawText(String.valueOf(i) + ruleUnit, degX, degY, paintRule);
        }
    }

    /**
     * ???????????? ???????????????????????????
     */
    public interface onRangeListener {
        void onRange(int low, int big);
    }

    private onRangeListener onRangeListener;

    public void setOnRangeListener(DoubleSlideSeekBar.onRangeListener onRangeListener) {
        this.onRangeListener = onRangeListener;
    }
}
