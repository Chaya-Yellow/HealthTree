package com.jks.Spo2MonitorEx.app.Fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jks.Spo2MonitorEx.app.Activity.HistoryActivity;
import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximeterAndPoint;
import com.jks.Spo2MonitorEx.util.thread.MyHandlerUtil;
import com.jks.Spo2MonitorEx.util.view.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 16/9/8.
 */

/**
 * 图表数据回顾界面
 */
public class HistoryChartFragment extends BaseFragment {
    public static final int ADDNEWVALUETOCHART = 137;
    public static final int HADSYNCCHARTDATA = 138;

    LineChart spo2Chart;
    LineChart prChart;

    private static final int TEXTGREEN = Color.parseColor("#48bab6");
    private static final int TEXTLINEBLUE = Color.parseColor("#DD2F2C");

    private List<Oximet> oximets;

    private Handler chartHandle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        initView(v);

        return v;
    }

    private boolean isSpo2DisplayValid(int value) {
        return value >= 80 && value <= 100;
    }

    private boolean isPrDisplayValid(int value) {
        return value >= 0 && value <= 240;
    }

    @Override
    protected void initHandler() {
        super.initHandler();
        chartHandle = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case HistoryChartFragment.ADDNEWVALUETOCHART:
                        if (MyDateUtil.calendarEqToday(HistoryActivity.selectedDate)) {//如果选择的日期不是今天就不要添加到视图上了
                            Oximet oximet = (Oximet) msg.obj;
                            Log.e("添加新的点", oximet.getSPO2() + "");
                            if (isSpo2DisplayValid(oximet.getSPO2())) {
                                spo2Chart.getXAxis().setAxisMaximum(getTimeHMS(oximet.getRecordDate()) + 10);
                                addEntry(spo2Chart, getTimeHMS(oximet.getRecordDate()), oximet.getSPO2());
                            }
                            if (isPrDisplayValid(oximet.getPR())) {
                                prChart.getXAxis().setAxisMaximum(getTimeHMS(oximet.getRecordDate()) + 10);
                                addEntry(prChart, getTimeHMS(oximet.getRecordDate()), oximet.getPR());
                            }
                        }
                        break;
                    case HistoryChartFragment.HADSYNCCHARTDATA:
                        OximeterAndPoint oximeterAndPoint = (OximeterAndPoint) msg.obj;
                        oximets = oximeterAndPoint.getOximets();//获取选择日期的数据
                        Log.e("初始化选择日期后的数据", oximets.size() + "");
                        if (oximets.size() > 0) {
                            ArrayList<Entry> spo2Values = new ArrayList<Entry>();
                            ArrayList<Entry> prValues = new ArrayList<Entry>();
                            for (int i = 0; i < oximets.size(); i++) {
//                                values.add(new Entry(Float.parseFloat(oximets.get(i).getCreatedDate()), oximets.get(i).getSPO2())); // add one entry per hour
//                                spo2Values.add(new Entry(MyDateUtil.getDateTime(oximets.get(i).getRecordDate()), oximets.get(i).getSPO2())); // add one entry per hour
//                                Log.e("数据日期", getTimeHMS(oximets.get(i).getRecordDate()) + "");
                                int spo2Value = oximets.get(i).getSPO2();
                                if (isSpo2DisplayValid(spo2Value)) {
                                    String spo2Date = oximets.get(i).getRecordDate();
                                    spo2Values.add(new Entry(getTimeHMS(spo2Date), spo2Value)); // add one entry per hour
                                }
                            }
                            spo2Chart.getXAxis().setAxisMaximum(getTimeHMS(oximets.get(oximets.size() - 1).getRecordDate()) + 10);
                            setSpO2Data(spo2Values);
                            autoZoomWithCount(oximets.size(), spo2Chart);

                            for (int i = 0; i < oximets.size(); i++) {
//                            values.add(new Entry(Float.parseFloat(oximets.get(i).getCreatedDate()), oximets.get(i).getSPO2())); // add one entry per hour
                                int prValue = oximets.get(i).getPR();
                                if (isPrDisplayValid(prValue)) {
                                    String prDate = oximets.get(i).getRecordDate();
                                    prValues.add(new Entry(getTimeHMS(prDate), prValue)); // add one entry per hour
                                }
                            }
                            prChart.getXAxis().setAxisMaximum(getTimeHMS(oximets.get(oximets.size() - 1).getRecordDate()) + 10);
                            setPrData(prValues);
                            autoZoomWithCount(oximets.size(), prChart);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        config.setHistoryChartHandler(chartHandle);
    }

    private void initView(View v) {
        spo2Chart = (LineChart) v.findViewById(R.id.spo2_chart);
        prChart = (LineChart) v.findViewById(R.id.pr_chart);
        initSpO2Chart(spo2Chart);
        initPRChart(prChart);
    }

    /**
     * 设置SpO2图数据
     * @param values 所有点的集合
     */
    private void setSpO2Data(ArrayList<Entry> values) {

        if (spo2Chart.getData() != null && spo2Chart.getData().getDataSetCount() > 0) {
            //重加载数据
            LineDataSet set = (LineDataSet) spo2Chart.getData().getDataSetByIndex(0);
            set.setValues(values);
            spo2Chart.getData().notifyDataChanged();
            spo2Chart.notifyDataSetChanged();
            spo2Chart.invalidate();
        } else {
            LineData spo2Data = getDataSet(values, "SpO2 trend data", TEXTGREEN);
            spo2Chart.setData(spo2Data);
            spo2Chart.invalidate();
        }
    }

    /**
     * 设置心率图数据
     * @param values 所有点的集合
     */
    private void setPrData(ArrayList<Entry> values) {
        if (prChart.getData() != null && prChart.getData().getDataSetCount() > 0) {
            LineDataSet set = (LineDataSet) prChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            prChart.getData().notifyDataChanged();
            prChart.notifyDataSetChanged();
            prChart.invalidate();
        }else {
            LineData prData = getDataSet(values, "PR trend data", TEXTLINEBLUE);

            prChart.setData(prData);
            prChart.invalidate();
        }
    }

    // 添加进去一个坐标点
    private void addEntry(LineChart mChart, float x, float y) {

        LineData data = mChart.getData();

        Entry entry = new Entry(x, y);

        // 往linedata里面添加点。注意：addentry的第二个参数即代表折线的下标索引。
        // 因为本例只有一个统计折线，那么就是第一个，其下标为0.
        // 如果同一张统计图表中存在若干条统计折线，那么必须分清是针对哪一条（依据下标索引）统计折线添加。
        if (data != null) {
            data.addEntry(entry, 0);

            // 像ListView那样的通知数据更新
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

            // 将坐标移动到最新
            // 此代码将刷新图表的绘图
//        mChart.moveViewToX(data.getXMax() - 5);
            mChart.moveViewToX(mChart.getXChartMax());
            //将视图移到最新添加的点上
            mChart.moveViewTo(mChart.getXChartMax(), y, YAxis.AxisDependency.LEFT);
            mChart.invalidate();
        }else {//如果没数据则重新去查询今天是否有数据
            if (HistoryActivity.selectDate != null) {
                MyHandlerUtil.sendMsg(Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST, config.getRecordMainHandler(), HistoryActivity.selectDate);
            }
        }
    }

    /**
     * 获取数据曲线
     * @param values
     * @param valeLabel
     * @param lineColor 曲线颜色值
     * @return
     */
    private LineData getDataSet(ArrayList<Entry> values, String valeLabel, int lineColor) {
        LineDataSet set = new LineDataSet(values, valeLabel);
//        spo2Set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        spo2Set.setDrawCircles(false);
        set.setDrawValues(false);
//        spo2Set.setHighLightColor(getResources().getColor(R.color.blue));
//        set.setDrawVerticalHighlightIndicator(false);
        set.enableDashedLine(10f, 5f, 0f);
        set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(lineColor);
        set.setCircleColor(lineColor);
        set.setValueTextColor(Color.WHITE);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
            set.setFillDrawable(drawable);
        }
        else {
            set.setFillColor(R.color.textYellow);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);
        LineData data = new LineData(dataSets);
        return data;
    }

    /**
     * 初始化血氧曲线
     * @param mChart
     */
    private void initSpO2Chart(LineChart mChart) {
        initChart(mChart);

        mChart.setNoDataTextDescription("No SpO2 data today");

        Legend l = mChart.getLegend();//曲线标签
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.parseColor("#48bab6"));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(80f);
        leftAxis.setLabelCount(8, true);
    }

    /**
     * 初始化心率曲线
     * @param mChart
     */
    private void initPRChart(LineChart mChart) {
        initChart(mChart);

        mChart.setNoDataTextDescription("No PR data today");

        Legend l = mChart.getLegend();//曲线标签
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(TEXTLINEBLUE);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(240f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(8, true);
    }

    private void initChart(LineChart mChart) {
        mChart.setDrawGridBackground(false);

        // no description
        mChart.setDescription("");
        mChart.setNoDataText("No data");

        // 允许点击手势
        mChart.setTouchEnabled(true);
        // 允许可缩放
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // 允许可以手捏缩放
        mChart.setPinchZoom(true);

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setGranularity(30L);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(5, true);//true 横坐标跟着数据点走
//        mChart.zoom(1000.0f,1.0f,0f,0f);//一个界面显示多少个点，其他点可以通过滑动看到
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
//                return mFormat.format(new Date((long) value));
                return toXaxisPoint((int)value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextColor(getResources().getColor(R.color.white));

        leftAxis.setDrawLimitLinesBehindData(true);//坐标虚线画在数据线后

        mChart.getAxisRight().setEnabled(false);

        mChart.animateXY(2000, 1000);
    }

    /**
     * 将时间结果转换为x坐标时间
     * @param timeSun
     * @return
     */
    private String toXaxisPoint(int timeSun) {
//
        int[] time = MyDateUtil.transform(timeSun, 60);
        String secStr = time[0] + "";
        String minStr = time[1] + "";
        String hourStr = time[2] + "";

        if (time[2] < 10) {
            hourStr = "0" + time[2];
        }
        if (time[1] < 10) {
            minStr = "0" + time[1];
        }
        if (time[0] < 10) {
            secStr = "0" + time[0];
        }

//        Log.e("时分秒", hourt + ", " + mint + ", " + sect);
        return hourStr + ":" + minStr + ":" + secStr;
    }

    private void show(int[] arr,int n){
        for(int i=n;i>=0;i--){
            if(arr[i]>9){
                Log.e("60进制1", String.valueOf((arr[i])));
            }
            else {
                Log.e("60进制", arr[i]+"");
            }
        }
    }

    /**
     * 获取事件的时分秒 例: 2016-09-21 13:27:04 ---> 132704
     * @param time 时间
     * @return
     */
    private int getTimeHMS(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(time);
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY) * 3600;
            int min = calendar.get(Calendar.MINUTE) * 60;
            int sec = calendar.get(Calendar.SECOND);

            int timeSun = hour + min + sec;
            return timeSun;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据数据数量对坐标轴进行缩放
     * @param count
     * @param mChart
     */
    private void autoZoomWithCount(int count, LineChart mChart) {
        if (count < 3) {
            setChartZoom(count, count, mChart);
        }else if (count < 5) {
            setChartZoom(count, 1, mChart);
        }else if (count < 10){
            setChartZoom(count, 2, mChart);
        }else if (count < 20) {
            setChartZoom(count, 3, mChart);
        }else {
            setChartZoom(count, 1, mChart);
        }
    }

    /**
     * 设置坐标轴的缩放
     * @param count
     * @param level
     * @param mChart
     */
    private void setChartZoom(int count, int level, LineChart mChart) {
        float n = (float) Math.ceil(count / level);
        mChart.fitScreen();
        if (n == 0) {
            mChart.zoom(1.0f, 1.0f, 0f, 0f);
        } else {
            mChart.zoom(n, 1.0f, 0f, 0f);
        }
    }
}
