package com.jks.Spo2MonitorEx.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jks.Spo2MonitorEx.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

/**
 * Created by badcode on 16/2/29.
 */
public class Report1Fragment extends Fragment {
    private PieChart pieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_1, container, false);

        // pie chart
        pieChart = (PieChart) v.findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);

        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
//        pieChart.setBackgroundColor(Color.WHITE);

        pieChart.setExtraOffsets(0,0,30,0);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
        mLegend.setXEntrySpace(0f);
        mLegend.setYEntrySpace(0f);
        mLegend.setYOffset(30f);
        mLegend.setXOffset(10f);
        mLegend.setTextColor(Color.WHITE);
        mLegend.setTextSize(13);

        pieChart.setDescription("");

        setChartData(1, 100);

        return v;
    }

    private void setChartData(int count, float range) {

//        float mult = range;
//
//        ArrayList<Entry> yVals1 = new ArrayList<>();
//
//        // IMPORTANT: In a PieChart, no values (Entry) should have the same
//        // xIndex (even if from different DataSets), since no values can be
//        // drawn above each other.
//        for (int i = 0; i < count + 1; i++) {
//            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
//        }
//
//        ArrayList<String> xVals = new ArrayList<>();
//
//        xVals.add("清醒");
//        xVals.add("睡着");
//
//        PieDataSet dataSet = new PieDataSet(yVals1, "");
//        dataSet.setSliceSpace(0f);
//        dataSet.setSelectionShift(5f);
//
//        // add a lot of colors
//        ArrayList<Integer> colors = new ArrayList<>();
//
//        colors.add(Color.argb(255, 56, 207, 204));
//        colors.add(Color.argb(255, 137, 87, 161));
//
//        dataSet.setColors(colors);
//        //dataSet.setSelectionShift(0f);
//
//        PieData data = new PieData(xVals, dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.TRANSPARENT);
////        data.setValueTypeface(tf);
//        pieChart.setData(data);
//
//        // undo all highlights
////        pieChart.highlightValues(null);
//
//        pieChart.invalidate();
    }
}
