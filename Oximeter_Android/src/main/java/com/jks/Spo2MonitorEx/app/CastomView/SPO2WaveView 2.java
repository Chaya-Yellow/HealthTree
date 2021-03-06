package com.jks.Spo2MonitorEx.app.CastomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by badcode on 15/10/10.
 * Surface View
 */
public class SPO2WaveView extends SurfaceView implements SurfaceHolder.Callback {

    private final static int TOTAL_POINTS = 180;

    private Canvas mCanvas;
    private Paint mPaint;

    private Handler mHandler;
    private SurfaceHolder mSurfaceHolder;

    private volatile Vector<Integer> values;
    private volatile Vector<Integer> PIValues;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private int Counter = 0,tmp=0;

    private int viewWidth;
    private int viewHeight;

    private boolean isFirstDraw = true;
    private boolean isDrawing = false;

    private float perX;
    private SimpleDateFormat simpleDateFormat;

    public SPO2WaveView(Context context) {
        super(context);
    }

    public SPO2WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#74ADA5"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
                            draw();
                            logic();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();

        perX = (float) viewWidth / (float) TOTAL_POINTS;

        Log.d("life", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mTimer != null) {
            mTimer.cancel();
        }

        Log.d("life", "surface Destroyed");
        if (isDrawing) {
            stopDraw();
        }
    }

    private void draw() {
        //Log.d("ljh", "??????" + (++tmp));
        mCanvas = mSurfaceHolder.lockCanvas(new Rect((int) (Counter * perX), 0, (int) ((Counter + 2) * perX + 20), 500));
        if (mCanvas != null) {
            //mCanvas.drawColor(Color.rgb(255, 255, 255));
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //>=5
            if (PIValues.size() > 0) {
                Log.d("ljh", "PIV[0]" + PIValues.get(0));
            }
            if (values.size() >= 5) {
                if (isFirstDraw) {
                    isFirstDraw = false;
                    mCanvas.drawLine(Counter * perX, viewHeight / 8f * 7f - values.get(0) * viewHeight / 128.0f / 2.0f, (Counter + 1) * perX, viewHeight / 8f * 7f - values.get(1) * viewHeight / 128.0f / 4 * 3, mPaint);
                    values.remove(0);
                    PIValues.remove(0);
                    mCanvas.drawLine((Counter + 1) * perX, viewHeight / 8f * 7f - values.get(0) * viewHeight / 128.0f / 4 * 3, (Counter + 2) * perX, viewHeight / 8f * 7f - values.get(1) * viewHeight / 128.0f / 4 * 3, mPaint);
                } else {
                    mCanvas.drawLine((Counter - 1) * perX, viewHeight / 8f * 7f - values.get(0) * viewHeight / 128.0f / 4 * 3, Counter * perX, viewHeight / 8f * 7f - values.get(1) * viewHeight / 128.0f / 4 * 3, mPaint);
                    values.remove(0);
                    mCanvas.drawLine(Counter * perX, viewHeight / 8f * 7f - values.get(0) * viewHeight / 128.0f / 4 * 3, (Counter + 1) * perX, viewHeight / 8f * 7f - values.get(1) * viewHeight / 128.0f / 4 * 3, mPaint);
                    values.remove(0);
                    mCanvas.drawLine((Counter + 1) * perX, viewHeight / 8f * 7f - values.get(0) * viewHeight / 128.0f / 4 * 3, (Counter + 2) * perX, viewHeight / 8f * 7f - values.get(1) * viewHeight / 128.0f / 4 * 3, mPaint);

                    // >3
                    if (PIValues.size() > 3) {
                        PIValues.remove(0);
                        //16
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PIValues.remove(0);
                            }
                        }, 16);
                        //33
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PIValues.remove(0);
                            }
                        }, 33);
                    }
                }

            } else {
                mCanvas.drawLine((Counter - 1) * perX, viewHeight / 2f, (Counter + 2) * perX, viewHeight / 2f, mPaint);
            }

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void printValue() {
        if (values.size() > 1) {
            Log.d("printValue", "values[0]" + values.get(0) + ",1=" + values.get(1));
        }
    }

    private void clearCanvas() {

        mCanvas = mSurfaceHolder.lockCanvas();
        if (mCanvas != null) {
            Log.d("TAG", "clear");
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void logic() {
        Counter = Counter + 2;
        if (Counter == TOTAL_POINTS) {
            Counter = 0;
        }
    }

    public void setValues(Vector<Integer> vs) {
        values = vs;
    }

    public void setPIValues(Vector<Integer> piValues) {
        PIValues = piValues;
    }

    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    public boolean getDrawing() {
        return isDrawing;
    }

    public void startDraw() {
        Log.d("start", "start draw");
        isDrawing = true;
        if (mTimer != null) {
            mTimer.cancel();
        }
        mHandler.removeMessages(1);
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
                //Log.d("ljh", "task????????????" + simpleDateFormat.format(new Date()));
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 2000, 60);
    }

    public void stopDraw() {
        isDrawing = false;
        Counter = 0;

        if (mTimer != null) {
            mTimer.cancel();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    clearCanvas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }
}
