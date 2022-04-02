package com.jks.Spo2MonitorEx.app.CastomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.jks.Spo2MonitorEx.R;

/**
 * Created by badcode on 16/3/1.
 */
public class ODIView extends View {
    private int viewWidth;
    private int viewHeight;


    public ODIView(Context context) {
        super(context);
    }

    public ODIView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int scaledSize = getResources().getDimensionPixelSize(R.dimen.testSize);
        int scaledNumSize = getResources().getDimensionPixelSize(R.dimen.testNumSize);


        Paint paint = new Paint();
        paint.setColor(Color.rgb(34, 171, 95));
        paint.setAntiAlias(true);
        canvas.drawRect(0, 0, viewWidth / 9, viewHeight / 2, paint);

        paint.setColor(Color.rgb(144, 176, 77));
        canvas.drawRect(viewWidth / 9, 0, viewWidth / 9 * 3, viewHeight / 2, paint);

        paint.setColor(Color.rgb(242, 136, 58));
        canvas.drawRect(viewWidth / 9 * 3, 0, viewWidth / 9 * 6, viewHeight / 2, paint);

        paint.setColor(Color.rgb(226, 102, 102));
        canvas.drawRect(viewWidth / 9 * 6, 0, viewWidth, viewHeight / 2, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setTextSize(scaledSize);

        // 字
        Rect bound = new Rect();
        paint.getTextBounds("正常", 0, 1, bound);

        canvas.drawText("正常", viewWidth / 18, viewHeight / 4 + bound.height() / 2, paint);
        canvas.drawText("轻度", viewWidth / 9 * 2, viewHeight / 4 + bound.height() / 2, paint);
        canvas.drawText("中度", viewWidth / 9 * 4.5f, viewHeight / 4 + bound.height() / 2, paint);
        canvas.drawText("严重", viewWidth / 9 * 7.5f, viewHeight / 4 + bound.height() / 2, paint);

        // 数字
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(scaledNumSize);
        canvas.drawText("0", 2, viewHeight / 2, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("5", viewWidth / 9, viewHeight / 2, paint);
        canvas.drawText("15", viewWidth / 9 * 3, viewHeight / 2, paint);
        canvas.drawText("30", viewWidth / 9 * 6, viewHeight / 2, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("45", viewWidth - 2, viewHeight / 2, paint);

        // 三角形
        float triHeight = viewHeight / 4 - 10;

        paint.setColor(Color.rgb(230, 41, 41));
        Path path = new Path();
        path.moveTo(viewWidth / 45 * 19.7f, viewHeight / 2);// 此点为多边形的起点
        path.lineTo(viewWidth / 45 * 19.7f + triHeight / 2, viewHeight / 2 + triHeight);
        path.lineTo(viewWidth / 45 * 19.7f - triHeight / 2, viewHeight / 2 + triHeight);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);

        // last line word
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setTextSize(scaledSize);

        Rect bounds = new Rect();
        paint.getTextBounds("OID:19.7", 0, 1, bounds);

        canvas.drawText("OID:19.7", viewWidth / 45 * 19.7f,
                viewHeight / 2 + triHeight + bounds.height() + 10, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
    }
}
