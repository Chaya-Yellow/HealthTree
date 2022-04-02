package com.jks.Spo2MonitorEx.app.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.print.PrintAttributes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Adapter.IndicatorAdapter;
import com.jks.Spo2MonitorEx.app.Fragment.HistoryChartFragment;
import com.jks.Spo2MonitorEx.app.MainActivity;
import com.jks.Spo2MonitorEx.app.share.ExcelUtils;
import com.jks.Spo2MonitorEx.util.activity.ActivityTaskManager;
import com.jks.Spo2MonitorEx.util.adapter.CalendarGridViewAdapter;
import com.jks.Spo2MonitorEx.util.adapter.DateWeekGridViewAdapter;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.OximetIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.OximetIfcImpl;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximeterAndPoint;
import com.jks.Spo2MonitorEx.util.enums.Month;
import com.jks.Spo2MonitorEx.util.listener.CommonGestureListener;
import com.jks.Spo2MonitorEx.util.photo.PixelConvertUtil;
import com.jks.Spo2MonitorEx.util.thread.MyHandlerUtil;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.jks.Spo2MonitorEx.util.view.MyScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by badcode on 15/10/20.
 */

/**
 * 用户数据记录界面
 */
public class HistoryActivity extends AppCompatActivity implements OnTouchListener, OnClickListener, OnItemClickListener, MyScrollView.ScrollViewUpOrDown {

    private ActivityTaskManager manager = ActivityTaskManager.getInstance();
    private View mRootView;
    private View mainView;
    private ViewFlipper dateVf;
    private View recordDateView1, recordDateView2, recordDateView3;//日期view(一共3个)
    private TextView monText, tueText, wedText, thuText, firText, satText, sunText;//星期Text
    private GridView gv1, gv2, gv3;//日期栅格
    private TextView dateTitle;//日期头
    private LinearLayout calendarLinear;
    private TextView titleView;
    private TextView backToday;
    private ViewPager mViewPager;
    private View mChartReportHead;
    private TextView mTvChartReportTime;
    private TextView mTvChartReportSpo2Max;
    private TextView mTvChartReportSpo2Avg;
    private TextView mTvChartReportSpo2Min;
    private TextView mTvChartReportPrMax;
    private TextView mTvChartReportPrAvg;
    private TextView mTvChartReportPrMin;
    private View mFrameLayout;
    private CircleIndicator mIndicator;

    private Calendar calendar, selectTimeCalendar;
    public static Calendar selectedDate;
    private GestureDetector gestureDetector;//滑动手势
    private DateWeekGridViewAdapter adapterWeek1, adapterWeek2, adapterWeek3;

    private List<Family> familys;
    protected Config config;
    protected Context context;
    private Handler handler;
    private OximetIfc oximetIfc;
    private List<Oximet> oximets = new ArrayList<Oximet>();

    private int width;
    private int week = 0;//星期
    public int mon;
    private float oldX = 0;//日期栏滑动时前一个的位置
    public int FIRST = 1, SECOND = 2, THREE = 3;//日期所在的位置(3个view中的一个)
    public int selectPage = FIRST;//选中的日期页
    public static boolean isClickCalendar = false;//是否选择了日期
    private int selectScrollView = MyScrollView.WEEKGRIDVIEW;//选择的scrollView
    private int familyId = -1;
    private int familyposition;//人员的位置
    private static int slideRight = 0;//往右滑动的次数
    private int slidePos = 0;//往左滑动的次数

    public static String selectDate = null;//选择的日期
    private ArrayList<ArrayList<String>> bill2List;
    private ArrayList<ArrayList<String>> tipsbill2list;
    private String[] title = { "ID","Time","SPO2(%)","PR(bpm)"};
    private String[] title1 = { "序号","记录时间","SPO2(%)","PR(bpm)"};
    private Activity activity;
//     public volatile int test = 0;

    private int mMaxSpo2;
    private int mMinSpo2;
    private float mAvgSpo2;
    private int mSpo2Size;
    private int mMaxPr;
    private int mMinPr;
    private float mAvgPr;
    private int mPrSize;

    private Subscription mExportSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_history);

        init();
        initHandler();
        initView();
        loadView();

        // DEFAULT
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        IndicatorAdapter adapter = new IndicatorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);

        ImageButton backButton = (ImageButton) findViewById(R.id.back_history_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        manager.putActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        familyposition = SharedPreferencesUtil.getFamilyPosition(context);
        if (familyposition != -1) {
            titleView.setText(familys.get(familyposition).getName());
            familyId = familys.get(familyposition).getFamilyId();
        }
//        if (selectDate != null) {
//            if (!oximetIfc.isHaveOximets(selectDate, familyId)) {
//                //如果选择的日期没有数据, 则将红点定位到今天
//                String date = MyDateUtil.formateDate2(calendar.getTime());//获取系统日期
//                DateWeekGridViewAdapter.STATE = MyDateUtil.formateDate2(date);
//                isClickCalendar = false;
//                MyHandlerUtil.sendMsg(
//                        Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST, handler,
//                        date);
//            } else {
//                //将红点定位到选择的日期上
//                DateWeekGridViewAdapter.STATE = MyDateUtil
//                        .formateDate2(selectDate);
//                isClickCalendar = false;
//                MyHandlerUtil.sendMsg(
//                        Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST, handler,
//                        selectDate);
//            }
//        } else {
        if (selectDate == null) {
            //如果没有选择日期, 还是将红点定位到今天
            String date = MyDateUtil.formateDate2(calendar.getTime());
            DateWeekGridViewAdapter.STATE = MyDateUtil.formateDate2(date);
            isClickCalendar = false;
            MyHandlerUtil.sendMsg(
                    Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST,
                    handler, date
            );
        }
    }

    private boolean isSpo2Valid(int value) {
        return value >= 0 && value <= 100;
    }

    private boolean isPrValid(int value) {
        return value >= 0 && value <= 254;
    }

    private void resetValues() {
        mMaxSpo2 = 0;
        mMinSpo2 = 0;
        mAvgSpo2 = 0;
        mSpo2Size = 0;
        mMaxPr = 0;
        mMinPr = 0;
        mAvgPr = 0;
        mPrSize = 0;
    }

    private void calculateSpo2Value(int newValue) {
        mSpo2Size++;
        if (newValue > mMaxSpo2) {
            mMaxSpo2 = newValue;
        }
        if (mMinSpo2 <= 0 || newValue < mMinSpo2) {
            mMinSpo2 = newValue;
        }
        mAvgSpo2 = (mAvgSpo2 * (mSpo2Size - 1) + newValue) / mSpo2Size;
    }

    private void calculatePrValue(int newValue) {
        mPrSize++;
        if (newValue > mMaxPr) {
            mMaxPr = newValue;
        }
        if (mMinPr <= 0 || newValue < mMinPr) {
            mMinPr = newValue;
        }
        mAvgPr = (mAvgPr * (mPrSize - 1) + newValue) / mPrSize;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isClickCalendar = false;
        selectDate = null;
        selectedDate = null;
        manager.removeActivity(this);
        if (mExportSub != null) {
            mExportSub.unsubscribe();
        }
    }

    private void init() {
        config = (Config) getApplicationContext();
        context = this;

        calendar = Calendar.getInstance();
        selectTimeCalendar = Calendar.getInstance();
        selectedDate = Calendar.getInstance();
        familys = config.getFamilys();
        oximetIfc = new OximetIfcImpl(this);
        width = PixelConvertUtil.getScreenWidth(this) * 6;
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST://有数据更新
                        selectDate = (String) msg.obj;//获取选择的日期
                        selectTempByTime();
                        selectTimeCalendar = MyDateUtil.calendarSetTime(selectDate);
                        selectedDate = MyDateUtil.calendarSetTime(selectDate);
                        if (calendarEqToday(selectTimeCalendar)) {
                            //如果是今天就移动到当前时间的位置
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);
                            if (minute > 0) {
                                hour = hour + 1;
                            }
                            float d = ((float) (hour * 60) * (float) (width / 1440))
                                    - width
                                    / 6
                                    + PixelConvertUtil.dip2px(context, 10);
                            // TODO 滑动到指定时间的位置
//                            horizontalScrollView.scrollTo((int) d,
//                                    horizontalScrollView.getHeight());
                        }
                        if (isClickCalendar)
                            dateTitle.setText(getdate(selectTimeCalendar));
                        if (adapterWeek1 != null && adapterWeek2 != null && adapterWeek3 != null) {
                            adapterWeek1.update(familyId);
                            adapterWeek2.update(familyId);
                            adapterWeek3.update(familyId);
                        }
                        break;
                    case Config.CHANGE_FAMILY:
                        familys = config.getFamilys();
                        upTitleBar();
                        break;
                    case Constant.DELAY_OPERATION:
                        setWeekCalendarWeek(0, 1, -1);
                        break;
                    case Constant.GESTURELISTENER_UPUI_LEFT:
                        slidePos++;
                        if (slidePos == 0) {
                            backToday.setVisibility(View.VISIBLE);
                        }else {
                            backToday.setVisibility(View.VISIBLE);
                        }
                        showNext();
                        break;
                    case Constant.GESTURELISTENER_UPUI_RIGHT:
                        slidePos--;
                        if (slidePos == 0) {
                            backToday.setVisibility(View.VISIBLE);
                        }else {
                            backToday.setVisibility(View.VISIBLE);
                        }
                        showPrv();
                        break;
                    case MainActivity.CHANGETEMPVALUE:
                        if (adapterWeek1 != null && adapterWeek2 != null && adapterWeek3 != null) {
                            adapterWeek1.update(familyId);
                            adapterWeek2.update(familyId);
                            adapterWeek3.update(familyId);
                        }
                        break;
                    case Constant.GESTURELISTENER_UPUI_UPTEMP://更新选择日期的数据
                        resetValues();
                        OximeterAndPoint oximeterAndPoint = (OximeterAndPoint) msg.obj;
                        // TODO 更新列表, 图表数据
                        MyHandlerUtil.sendMsg(HistoryChartFragment.HADSYNCCHARTDATA, config.getHistoryChartHandler(), oximeterAndPoint);
                        MyHandlerUtil.sendMsg(HistoryChartFragment.HADSYNCCHARTDATA, config.getParamChartHandler(), oximeterAndPoint);
                        List<Oximet> dataList = oximeterAndPoint.getOximets();
                        if (dataList != null) {
                            for (Oximet oximet : dataList) {
                                if (isSpo2Valid(oximet.getSPO2())) {
                                    calculateSpo2Value(oximet.getSPO2());
                                }
                                if (isPrValid(oximet.getPR())) {
                                    calculatePrValue(oximet.getPR());
                                }
                            }
                        }
                        break;
                    case HistoryChartFragment.ADDNEWVALUETOCHART:
                        Oximet oximet = (Oximet) msg.obj;
                        MyHandlerUtil.sendMsg(HistoryChartFragment.ADDNEWVALUETOCHART, config.getHistoryChartHandler(), oximet);
                        MyHandlerUtil.sendMsg(HistoryChartFragment.ADDNEWVALUETOCHART, config.getParamChartHandler(), oximet);
                        if (MyDateUtil.calendarEqToday(HistoryActivity.selectedDate)) {
                            if (isSpo2Valid(oximet.getSPO2())) {
                                calculateSpo2Value(oximet.getSPO2());
                            }
                            if (isPrValid(oximet.getPR())) {
                                calculatePrValue(oximet.getPR());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        config.setRecordMainHandler(handler);
    }

    private void initView() {
        mRootView = findViewById(R.id.root_view);
        dateVf = (ViewFlipper) findViewById(R.id.first_record_bt_date);
        recordDateView1 = findViewById(R.id.first_record_bt_date_1);
        recordDateView2 = findViewById(R.id.first_record_bt_date_2);
        recordDateView3 = findViewById(R.id.first_record_bt_date_3);
        gv1 = (GridView) recordDateView1.findViewById(R.id.gv);
        gv2 = (GridView) recordDateView2.findViewById(R.id.gv);
        gv3 = (GridView) recordDateView3.findViewById(R.id.gv);
        dateTitle = (TextView) findViewById(R.id.first_record_bt_top_date);
        titleView = (TextView) findViewById(R.id.historyTitle);
        backToday = (TextView) findViewById(R.id.history_tody);
        mChartReportHead = findViewById(R.id.ll_chart_report_head);
        mTvChartReportTime = findViewById(R.id.tv_chart_report_time);
        mTvChartReportSpo2Max = findViewById(R.id.tv_spo2_max);
        mTvChartReportSpo2Avg = findViewById(R.id.tv_spo2_avg);
        mTvChartReportSpo2Min = findViewById(R.id.tv_spo2_min);
        mTvChartReportPrMax = findViewById(R.id.tv_pr_max);
        mTvChartReportPrAvg = findViewById(R.id.tv_pr_avg);
        mTvChartReportPrMin = findViewById(R.id.tv_pr_min);
        mFrameLayout = findViewById(R.id.frameLayout);
        backToday.setOnClickListener(this);
        backToday.setVisibility(View.VISIBLE);
        if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
            //英文条件下从周日开始
            dateTitle.setText(getdate(MyDateUtil.getForWeekSunday(week, calendar)));
        } else {
            //中文条件下从周一开始
            dateTitle.setText(getdate(MyDateUtil.getForWeekMonday(week, calendar)));
        }

        calendarLinear = (LinearLayout) findViewById(R.id.calendar_linear);

        //星期
        monText = (TextView) findViewById(R.id.calendar_mon);
        tueText = (TextView) findViewById(R.id.calendar_tue);
        wedText = (TextView) findViewById(R.id.calendar_wed);
        thuText = (TextView) findViewById(R.id.calendar_thu);
        firText = (TextView) findViewById(R.id.calendar_fri);
        satText = (TextView) findViewById(R.id.calendar_sat);
        sunText = (TextView) findViewById(R.id.calendar_sun);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(Constant.DELAY_OPERATION);
            }
        }, 200);

        gv3.setOnTouchListener(this);
        gv2.setOnTouchListener(this);
        gv1.setOnTouchListener(this);
        gv3.setOnItemClickListener(this);
        gv2.setOnItemClickListener(this);
        gv1.setOnItemClickListener(this);

        gestureDetector = new GestureDetector(new CommonGestureListener(calendarLinear, handler));
    }

    private void loadView() {
        dateVf.getLayoutParams().height = PixelConvertUtil.getScreenWidth(this) / 7;//设置日期栏的高度

        //根据语言设置星期
        if (context.getResources().getString(R.string.language).equals("0")) {
            monText.setText(R.string.alarm_add_d1);
            tueText.setText(R.string.alarm_add_d2);
            wedText.setText(R.string.alarm_add_d3);
            thuText.setText(R.string.alarm_add_d4);
            firText.setText(R.string.alarm_add_d5);
            satText.setText(R.string.alarm_add_d6);
            sunText.setText(R.string.alarm_add_d0);
        } else {
            monText.setText(R.string.alarm_add_d0);
            tueText.setText(R.string.alarm_add_d1);
            wedText.setText(R.string.alarm_add_d2);
            thuText.setText(R.string.alarm_add_d3);
            firText.setText(R.string.alarm_add_d4);
            satText.setText(R.string.alarm_add_d5);
            sunText.setText(R.string.alarm_add_d6);
        }

        calendarLinear.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (oldX - event.getX() > 30) {
                            // 向左
                            showNext();

                        } else if (event.getX() - oldX > 30) {
                            // 向右
                            showPrv();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void upTitleBar() {
        if (familys != null) {
            if (familys.size() != 0) {
                if (SharedPreferencesUtil.getFamilyPosition(context) != -1) {
                    familyId = familys.get(
                            SharedPreferencesUtil.getFamilyPosition(context))
                            .getFamilyId();
                    titleView.setText(familys.get(
                            SharedPreferencesUtil.getFamilyPosition(context))
                            .getName());
                } else {
                    titleView.setText(familys.get(0).getName());
                    familyId = familys.get(0).getFamilyId();
                }
            } else
                titleView.setText("");
        } else {
            titleView.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_tody:
                //////数据导出按钮
                if (mViewPager.getCurrentItem() == 0) {
                    exportPdf();
                } else if (mViewPager.getCurrentItem() == 1) {
                    exportExcel();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
            if (((CalendarGridViewAdapter) parent.getAdapter()).isPositionChanged(position)) {
            }
        } else {
            if (((DateWeekGridViewAdapter) parent.getAdapter()).isPositionChanged(position)) {
                adapterWeek1.notifyDataSetChanged();
                adapterWeek2.notifyDataSetChanged();
                adapterWeek3.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private String getdate(Calendar calendar) {
        if (Constant.getlanguage(this) == Constant.LANGUAGE_CN)
            return calendar.get(Calendar.YEAR) + "年"
                    + (calendar.get(Calendar.MONTH) + 1) + "月";
        else {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            return getEnglishMonth(month) + " " + year;
        }
    }

    public static String getEnglishMonth(int num) {

        String[] str = new String[12];
        int i = 0;
        for (Month mon : Month.values()) {
            str[i] = mon.mNum;
            i++;
        }
        return str[num - 1];
    }

    private void showPrv() {
        if (MyScrollView.MONTHGRIDVIEW == selectScrollView) {
            mon = calendar.get(Calendar.MONTH) - 1;
            CalendarGridViewAdapter.POSITION = -1;
        } else {
            week = week - 1;

        }
        if (selectPage == SECOND) {
            dateVf.setInAnimation(context, R.anim.in_lefttoright);
            dateVf.setOutAnimation(context, R.anim.out_lefttoright);
            dateVf.showPrevious();
            selectPage = FIRST;
            if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
                // calendar1.set(calendar.get(Calendar.YEAR), mon - 1,
                // calendar.get(Calendar.DAY_OF_MONTH));
                // adapterMon3 = new CalendarGridViewAdapter(this, calendar1,
                // 3);
                // gv3.setAdapter(adapterMon3);
            } else {
                if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                    adapterWeek3 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekSunday(week - 1, calendar),
                            3, familyId);

                } else {

                    adapterWeek3 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekMonday(week - 1, calendar),
                            3, familyId);
                }
                gv3.setAdapter(adapterWeek3);
            }
        } else if (selectPage == THREE) {
            dateVf.setInAnimation(context, R.anim.in_lefttoright);
            dateVf.setOutAnimation(context, R.anim.out_lefttoright);
            dateVf.showPrevious();
            selectPage = SECOND;
            if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
                // calendar1.set(calendar.get(Calendar.YEAR), mon - 1,
                // calendar.get(Calendar.DAY_OF_MONTH));
                // adapterMon1 = new CalendarGridViewAdapter(this, calendar1,
                // 1);
                // gv1.setAdapter(adapterMon1);
            } else {
                if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                    adapterWeek1 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekSunday(week - 1, calendar),
                            1, familyId);

                } else {
                    adapterWeek1 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekMonday(week - 1, calendar),
                            1, familyId);

                }
                gv1.setAdapter(adapterWeek1);
            }

        } else if (selectPage == FIRST) {
            dateVf.setInAnimation(context, R.anim.in_lefttoright);
            dateVf.setOutAnimation(context, R.anim.out_lefttoright);
            dateVf.showPrevious();
            selectPage = THREE;
            if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
                // calendar1.set(calendar.get(Calendar.YEAR), mon - 1,
                // calendar.get(Calendar.DAY_OF_MONTH));
                // adapterMon2 = new CalendarGridViewAdapter(this, calendar1,
                // 2);
                // gv2.setAdapter(adapterMon2);
            } else {
                if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                    adapterWeek2 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekSunday(week - 1, calendar),
                            2, familyId);
                } else {
                    adapterWeek2 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekMonday(week - 1, calendar),
                            2, familyId);
                }

                gv2.setAdapter(adapterWeek2);
            }
        }
        if (MyScrollView.MONTHGRIDVIEW == selectScrollView) {
            calendar.set(calendar.get(Calendar.YEAR), mon, calendar.get(Calendar.DAY_OF_MONTH));
            dateTitle.setText(getdate(calendar));
        } else {
            if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                dateTitle.setText(getdate(MyDateUtil.getForWeekSunday(week, calendar)));
            } else {
                dateTitle.setText(getdate(MyDateUtil.getForWeekMonday(week, calendar)));

            }
        }
    }

    private void showNext() {
        if (MyScrollView.MONTHGRIDVIEW == selectScrollView) {
            mon = calendar.get(Calendar.MONTH) + 1;
            CalendarGridViewAdapter.POSITION = -1;
        } else {
            week = week + 1;
        }
        if (selectPage == FIRST) {
            dateVf.setInAnimation(context, R.anim.in_righttoleft);
            dateVf.setOutAnimation(context, R.anim.out_righttoleft);
            dateVf.showNext();
            selectPage = SECOND;
            if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
                // calendar1.set(calendar.get(Calendar.YEAR), mon + 1,
                // calendar.get(Calendar.DAY_OF_MONTH));
                // adapterMon3 = new CalendarGridViewAdapter(this, calendar1,
                // 3);
                // gv3.setAdapter(adapterMon3);
            } else {
                if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                    adapterWeek3 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekSunday(week + 1, calendar), 3,
                            familyId);
                } else {
                    adapterWeek3 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekMonday(week + 1, calendar), 3,
                            familyId);
                }

                gv3.setAdapter(adapterWeek3);
            }
        } else if (selectPage == SECOND) {
            dateVf.setInAnimation(context, R.anim.in_righttoleft);
            dateVf.setOutAnimation(context, R.anim.out_righttoleft);
            dateVf.showNext();
            selectPage = THREE;
            if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
                // calendar1.set(calendar.get(Calendar.YEAR), mon + 1,
                // calendar.get(Calendar.DAY_OF_MONTH));
                // adapterMon1 = new CalendarGridViewAdapter(this, calendar1,
                // 1);
                // gv1.setAdapter(adapterMon1);
            } else {
                if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                    adapterWeek1 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekSunday(week + 1, calendar), 1,
                            familyId);
                } else {
                    adapterWeek1 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekMonday(week + 1, calendar), 1,
                            familyId);
                }

                gv1.setAdapter(adapterWeek1);
            }

        } else if (selectPage == THREE) {
            dateVf.setInAnimation(context, R.anim.in_righttoleft);
            dateVf.setOutAnimation(context, R.anim.out_righttoleft);
            dateVf.showNext();
            selectPage = FIRST;
            if (selectScrollView == MyScrollView.MONTHGRIDVIEW) {
                // calendar1.set(calendar.get(Calendar.YEAR), mon + 1,
                // calendar.get(Calendar.DAY_OF_MONTH));
                // adapterMon2 = new CalendarGridViewAdapter(this, calendar1,
                // 2);
                // gv2.setAdapter(adapterMon2);
            } else {
                if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                    adapterWeek2 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekSunday(week + 1, calendar), 2,
                            familyId);
                } else {
                    adapterWeek2 = new DateWeekGridViewAdapter(this,
                            MyDateUtil.getForWeekMonday(week + 1, calendar), 2,
                            familyId);
                }

                gv2.setAdapter(adapterWeek2);
            }
        }
        if (MyScrollView.MONTHGRIDVIEW == selectScrollView) {
            calendar.set(calendar.get(Calendar.YEAR), mon,
                    calendar.get(Calendar.DAY_OF_MONTH));
            dateTitle.setText(getdate(calendar));
        } else {
            if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
                dateTitle.setText(getdate(MyDateUtil.getForWeekSunday(week,
                        calendar)));
            } else {
                dateTitle.setText(getdate(MyDateUtil.getForWeekMonday(week,
                        calendar)));
            }
        }
    }

    // 判断当前日期的位置
    private Boolean calendarEqToday(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String current = format.format(Calendar.getInstance().getTime());
        String date = format.format(calendar.getTime());
        if (current.equals(date)) {
            return true;
        }
        return false;
    }

    /**
     * 设置日期适配器
     * @param adapter1Week 当前星期 0
     * @param adapter2Week 前一星期 -1
     * @param adapter3Week 后一星期 1
     */
    private void setWeekCalendarWeek(int adapter1Week, int adapter2Week, int adapter3Week) {
        if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
            adapterWeek3 = new DateWeekGridViewAdapter(context,
                    MyDateUtil.getForWeekSunday(adapter3Week, calendar), 3,
                    familyId);
            adapterWeek2 = new DateWeekGridViewAdapter(context,
                    MyDateUtil.getForWeekSunday(adapter2Week, calendar), 2,
                    familyId);
            adapterWeek1 = new DateWeekGridViewAdapter(context,
                    MyDateUtil.getForWeekSunday(adapter1Week, calendar), 1,
                    familyId);
        } else {
            adapterWeek3 = new DateWeekGridViewAdapter(context,
                    MyDateUtil.getForWeekMonday(adapter3Week, calendar), 3,
                    familyId);
            adapterWeek2 = new DateWeekGridViewAdapter(context,
                    MyDateUtil.getForWeekMonday(adapter2Week, calendar), 2,
                    familyId);
            adapterWeek1 = new DateWeekGridViewAdapter(context,
                    MyDateUtil.getForWeekMonday(adapter1Week, calendar), 1,
                    familyId);
        }

        gv3.setAdapter(adapterWeek3);
        gv2.setAdapter(adapterWeek2);
        gv1.setAdapter(adapterWeek1);
    }

    /**
     * 回到今天
     */
    private void backToToday() {
        if (slidePos > 0) {
            int times = Math.abs(slidePos);
            while ((times--) > 0) {
                showPrv();
            }
        }else {
            int times = Math.abs(slidePos);
            while ((times--) > 0) {
                showNext();
            }
        }
        slidePos = 0;
    }

    /**
     * 将选择日期中的血氧数据查询出来
     */
    private void selectTempByTime() {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                synchronized (HistoryActivity.class) {

                    oximets = oximetIfc.findOximetsByTime(selectDate, familyId);
                    Log.e("oximets记录", oximets.size() + ", 日期: " + selectDate);
                    List<Integer> nextBreakPointList = new ArrayList<Integer>();
                    OximeterAndPoint oximeterAndPoint = new OximeterAndPoint();

                    for (int i = 0; i < oximets.size(); i++) {
                        if (i + 1 <= oximets.size() - 1) {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date1 = new Date();
                            Date date2 = new Date();
                            try {
                                date1 = df.parse(oximets.get(i).getRecordDate());
                                date2 = df.parse(oximets.get(i + 1).getRecordDate());
                                long time1 = date1.getTime();
                                long time2 = date2.getTime();
                                // 转换成秒
                                long test = (time1 - time2) / 1000;
                                // 60*5五分钟
                                long timeResult = 300;
                                // 判断时间间隔
                                if (Math.abs(test) > timeResult) {
                                    nextBreakPointList.add(i + 1);
                                }
                            }catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    oximeterAndPoint.setOximets(oximets);
                    oximeterAndPoint.setNextBreakPointList(nextBreakPointList);
                    MyHandlerUtil.sendMsg(Constant.GESTURELISTENER_UPUI_UPTEMP, handler, oximeterAndPoint);

                }
            }
        });
    }

    @Override
    public void scroll(int upOrDown) {

    }




///////////////////数据导出190724
    public static void makeDir(File dir) {
        if (dir != null) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    private void preCut() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTvChartReportTime.setText(dateFormat.format(selectTimeCalendar.getTime()));
        mTvChartReportSpo2Max.setText(String.valueOf(mMaxSpo2));
        mTvChartReportSpo2Avg.setText(String.valueOf(Math.round(mAvgSpo2)));
        mTvChartReportSpo2Min.setText(String.valueOf(mMinSpo2));
        mTvChartReportPrMax.setText(String.valueOf(mMaxPr));
        mTvChartReportPrAvg.setText(String.valueOf(Math.round(mAvgPr)));
        mTvChartReportPrMin.setText(String.valueOf(mMinPr));
        mChartReportHead.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
        calendarLinear.setVisibility(View.GONE);
        mIndicator.setVisibility(View.GONE);
    }

    private void afterCut() {
        mFrameLayout.setVisibility(View.VISIBLE);
        calendarLinear.setVisibility(View.VISIBLE);
        mIndicator.setVisibility(View.VISIBLE);
        mChartReportHead.setVisibility(View.GONE);
    }

    public Bitmap onCut() {
        mRootView.setDrawingCacheEnabled(true);
        mRootView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(
                mRootView.getDrawingCache(),
                0,
                0,
                mRootView.getWidth(),
                mRootView.getHeight()
        );
        mRootView.destroyDrawingCache();
        afterCut();
        return bitmap;
    }

    public void exportPdf() {
        if (mExportSub != null) {
            mExportSub.unsubscribe();
        }
        mExportSub = Observable
                .create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(final Subscriber<? super Bitmap> subscriber) {
                        preCut();
                        mRootView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(onCut());
                                    subscriber.onCompleted();
                                }
                            }
                        });
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Func1<Bitmap, File>() {
                    @Override
                    public File call(Bitmap bitmap) {
                        PdfDocument doc = null;
                        FileOutputStream outputStream = null;
                        try {
                            doc = new PdfDocument();
                            int pageWidth = PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000;
                            float scale = (float) pageWidth / (float) bitmap.getWidth();
                            int pageHeight = (int) (bitmap.getHeight() * scale);

                            Matrix matrix = new Matrix();
                            matrix.postScale(scale, scale);
                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(
                                    pageWidth, pageHeight, 1
                            ).create();
                            PdfDocument.Page page = doc.startPage(newPage);
                            Canvas canvas = page.getCanvas();
                            canvas.drawBitmap(bitmap, matrix, paint);
                            doc.finishPage(page);

                            final String parentName = "Family";
                            File parent;
                            File externalFilesDir = getExternalFilesDir(null);
                            if (externalFilesDir == null) {
                                parent = new File(getFilesDir(), parentName);
                            } else {
                                parent = new File(externalFilesDir, parentName);
                            }
                            File file = new File(parent, System.currentTimeMillis() + ".pdf");
                            makeDir(parent);
                            outputStream = new FileOutputStream(file);
                            doc.writeTo(outputStream);
                            return file;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (doc != null) {
                                doc.close();
                            }
                            try {
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Toast.makeText(
                                HistoryActivity.this,
                                R.string.export_fail,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        startToShare(file, "application/pdf");
                    }
                });
    }

    public void exportExcel() {
        if (mExportSub != null) {
            mExportSub.unsubscribe();
        }
        mExportSub = Observable
                .create(new Observable.OnSubscribe<File>() {
                    @Override
                    public void call(Subscriber<? super File> subscriber) {
                        try {
                            final String parentName = "Family";
                            final String fileName = "DataRecord_" + System.currentTimeMillis() + ".xls";
                            File parent;
                            File externalFilesDir = getExternalFilesDir(null);
                            if (externalFilesDir == null) {
                                parent = new File(getFilesDir(), parentName);
                            } else {
                                parent = new File(externalFilesDir, parentName);
                            }
                            makeDir(parent);
                            File file = new File(parent, fileName);
                            ExcelUtils.initExcel(file, title);
                            ExcelUtils.writeObjListToExcel(getBillData(), file);
                            subscriber.onNext(file);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Toast.makeText(
                                HistoryActivity.this,
                                R.string.export_fail,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        startToShare(file, "application/vnd.ms-excel");
                    }
                });
    }

    private ArrayList<ArrayList<String>> getBillData() {
        //SharedPreferences sp = context.getSharedPreferences(PROJECTNAME, Context.MODE_PRIVATE);
        oximets = oximetIfc.findOximetsByTime(selectDate, familyId);
        bill2List = new ArrayList<ArrayList<String>>();
        int counter = 0;
        for (int i = oximets.size()-1; i >= 0; i--) {
            ArrayList<String> beanList = new ArrayList<String>();
            //if (testlist.get(i).getFamilyId() == F_ID) {
            //beanList.add(testlist.get(i).toString());
            counter++;
            beanList.add(Integer.toString(counter));
            beanList.add(oximets.get(i).getRecordDate());
            beanList.add(Integer.toString(oximets.get(i).getSPO2()));
            beanList.add(Integer.toString(oximets.get(i).getPR()));
            bill2List.add(beanList);
        }
        return bill2List;
    }

    /**
     * 返回uri
     */
    private Uri getUriForFile(File file) {
        //应用包名.fileProvider
        String authority = getPackageName().concat(".app.fileprovider");
        return FileProvider.getUriForFile(this, authority, file);
    }

    private void startToShare(@NonNull File file, @NonNull String type) {
        Uri fileUri = getUriForFile(file);
        if (fileUri != null) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.putExtra(Intent.EXTRA_STREAM, fileUri);
            share.putExtra(Intent.EXTRA_SUBJECT, file.getName());
            share.setType(type);
            String title = getString(R.string.export);
            context.startActivity(Intent.createChooser(share, title));
        }
    }

    final int PERMISSION_REQUEST = 100;

    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();

        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

        } else {//请求权限方法
            String[] permissions1 = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(HistoryActivity.this, permissions1, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }
}

