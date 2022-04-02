package com.jks.Spo2MonitorEx.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.HistoryActivity;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.OximetIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.OximetIfcImpl;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.photo.PixelConvertUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by apple on 16/9/5.
 */

/**
 * 日期列表类
 */
public class DateWeekGridViewAdapter extends BaseAdapter {
    private Context context;
    private Calendar calendar, calendar1;
    private int maxDay = 7;
    public static String STATE;//红点的位置
    private LayoutInflater inflater;
    private Config config;
    private int currentPosition = -1;
    private List<Boolean> positions = new ArrayList<Boolean>();//每个星期的测量情况 true: 有测量 false: 无测量
    private OximetIfc oximetIfc;
    private int width = 0;
    private String firstDay;
    private String lastDay;

    public DateWeekGridViewAdapter(Context context, Calendar calendar, int state, int familyId) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        config = (Config) context.getApplicationContext();
        this.calendar = calendar;
        calendar1 = Calendar.getInstance();
        oximetIfc = new OximetIfcImpl(context);
        List<String> days = new ArrayList<String>();
        firstDay = MyDateUtil.formateDate2(calendar.getTime());
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar1.add(Calendar.DAY_OF_MONTH, 6);
        lastDay = MyDateUtil.formateDate2(calendar1.getTime());
        days = oximetIfc.isHaveOximets(firstDay, lastDay, familyId);
        int count = 0;
        for (int i = 0; i < maxDay; i++) {
            //将该星期中的测量情况赋值到positions
            calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            calendar1.add(Calendar.DAY_OF_MONTH, i);
            if (days.size() > count) {
                if (days.get(count).equals(MyDateUtil.formateDate4(calendar1.getTime()))) {
                    positions.add(true);
                    count++;
                } else {
                    positions.add(false);
                }
            } else {
                positions.add(false);
            }
            if (currentPosition == -1) {
                currentPosition = CalendarEqToday(calendar1, i);
            }
        }

        width = PixelConvertUtil.getScreenWidth((Activity) context) / 7;
    }

    // 判断当前日期的位置
    private int CalendarEqToday(Calendar calendar, int i) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String current = format.format(Calendar.getInstance().getTime());
        String date = format.format(calendar.getTime());
        if (current.equals(date)) {
            return i;
        }
        return -1;
    }

    @Override
    public int getCount() {
        return maxDay;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void update(int familyId) {
        List<String> days = new ArrayList<String>();
        firstDay = MyDateUtil.formateDate2(calendar.getTime());
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        calendar1.add(Calendar.DAY_OF_MONTH, 6);
        lastDay = MyDateUtil.formateDate2(calendar1.getTime());
        days = oximetIfc.isHaveOximets(firstDay, lastDay, familyId);
        int count = 0;
        for (int i = 0; i < maxDay; i++) {
            calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            calendar1.add(Calendar.DAY_OF_MONTH, i);
            if (days.size() > count) {
                if (days.get(count).equals(MyDateUtil.formateDate4(calendar1.getTime()))) {
                    positions.set(i, true);
                    count++;
                } else {
                    positions.set(i, false);
                }
            } else {
                positions.set(i, false);
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.record_grid_view_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.gv_tv);
            holder.gv = (RelativeLayout) convertView.findViewById(R.id.gv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.gv.getLayoutParams().width = width;
        holder.gv.getLayoutParams().height = width;
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar1.add(Calendar.DAY_OF_MONTH, position);
        holder.tv.setText(Integer.toString(calendar1.get(Calendar.DAY_OF_MONTH)));
        if (MyDateUtil.formateDate2(MyDateUtil.formateDate2(calendar1.getTime())).equals(STATE)) {
            holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.hei_yuan));
            holder.tv.setTextColor(context.getResources().getColor(R.color.textWhite));
        } else {
            holder.tv.setBackgroundDrawable(null);
            if (currentPosition != -1 && currentPosition == position) {
                holder.tv.setTextColor(context.getResources().getColor(R.color.textWhite));
                holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.record_calendar_day_bg));
            }
            if (positions.get(position)) {
                holder.tv.setTextColor(context.getResources().getColor(R.color.textWhite));
                holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.record_calendar_day_bg_normal));
            }
        }
        return convertView;
    }

    class ViewHolder {
        RelativeLayout gv;
        TextView tv;
    }

    /**
     * 日期被点击后调用
     * @param position
     * @return
     */
    public boolean isPositionChanged(int position) {
        if (positions.get(position)) {
            //如果点击的日期上有数据, 则更新记录
            Message msg = new Message();
            calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            calendar1.add(Calendar.DAY_OF_MONTH, position);
            msg.what = Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST;
            String date = MyDateUtil.formateDate2(calendar1.getTime());
            msg.obj = date;//将日期返回
            STATE = MyDateUtil.formateDate2(date);//保存日期
            HistoryActivity.isClickCalendar = true;
            config.getRecordMainHandler().sendMessage(msg);
            return true;
        } else {
            return false;
        }

    }
}