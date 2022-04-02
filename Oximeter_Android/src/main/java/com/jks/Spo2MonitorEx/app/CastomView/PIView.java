package com.jks.Spo2MonitorEx.app.CastomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by badcode on 15/10/19.
 */
public class PIView extends View {

    private int PIvalue;

    private int viewWidth;
    private int viewHeight;

    private Paint paint;


    public PIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        PIvalue = -1;
        paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));

    }

    public PIView(Context context) {
        super(context);
        PIvalue = -1;
        paint = new Paint();
        paint.setColor(Color.rgb(255, 255, 255));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = viewHeight / 44.0f;

        //canvas.drawColor(Color.rgb(5, 115, 66));

        if (PIvalue != -1) {
            paint.setColor(Color.rgb(15, 200, 100));
            RectF rectF = new RectF(0, 0, viewWidth, viewHeight);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            paint.setColor(Color.rgb(100, 215, 155));
            RectF rectFore = new RectF(0, viewHeight - viewHeight * PIvalue / 15.0f, viewWidth, viewHeight);
            canvas.drawRoundRect(rectFore, 5, 5, paint);

            paint.setColor(Color.rgb(255, 255, 255));
            for (int i = 1 ; i < 15; i++) {
                canvas.drawRect(0, height * (i * 3 - 1), viewWidth, height * (i * 3), paint);
            }
        }
    }

    public void setPIValue(int value){
        PIvalue = value;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
    }
}
