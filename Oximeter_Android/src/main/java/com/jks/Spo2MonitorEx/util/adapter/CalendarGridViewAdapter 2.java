package com.jks.Spo2MonitorEx.util.adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by apple on 16/9/5.
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private Context context;
    private Calendar calendar;
    private LayoutInflater inflater;
    private int maxDay;
    private int year, month, day;
    private int firstDay;
    private int currentPosition = -1;
    private Config config;
    public static int POSITION = -1;
    public static int STATE = 1;
    private List<Boolean> positions;
    public static boolean isShowSelect = true;
    private int state;
    private int indexStart = -1;

    public CalendarGridViewAdapter(Context context, Calendar calendar, int state) {
        this.context = context;
        this.state = state;
        inflater = LayoutInflater.from(context);
        config = (Config) context.getApplicationContext();
        this.calendar = calendar;
        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, 1);
        firstDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(year, month, day);
        currentPosition = CalendarEqToday(calendar);
        positions = new ArrayList<Boolean>();
        for (int i = 0; i < 42; i++) {
            positions.add(false);
            indexStart = i - firstDay + 2;
            if (indexStart > 0 && indexStart <= maxDay) {
                positions.set(i, true);
                if (state == 1 && isShowSelect) {
                    POSITION = i;
                }
                if (POSITION != -1 && state == 1 && isShowSelect) {
                    isShowSelect = false;
                    isPositionChanged(POSITION);
                }
            }
        }

    }

    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.record_grid_view_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.gv_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        indexStart = position - firstDay + 2;
        if (indexStart > 0 && indexStart <= maxDay) {
            holder.tv.setText(indexStart + "");
            if (position == POSITION && state == STATE) {
                holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.record_calendar_day_bg_press));
                holder.tv.setTextColor(context.getResources().getColor(R.color.textWhite));
            } else {
                holder.tv.setBackgroundDrawable(null);
                holder.tv.setTextColor(context.getResources().getColor(R.color.textBlack));
                if (currentPosition != -1 && currentPosition == position)
                    holder.tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.record_calendar_day_bg));
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }

    // 判断当前日期的位置
    private int CalendarEqToday(Calendar calendar) {
        if (MyDateUtil.equalCalendarToMonth(calendar, Calendar.getInstance())) {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            return (day + firstDay - 2);
        }
        return -1;
    }

    public boolean isPositionChanged(int position) {
        System.out.println("state=" + state);
        if (positions.get(position)) {
            STATE = state;
            POSITION = position;
            Message msg = new Message();
            msg.what = Constant.BTRECORDHANDLER_UPDATE_BOTTOMLIST;
            calendar.set(year, month, position - firstDay + 2);
            String date = MyDateUtil.formateDate2(calendar.getTime());
            msg.obj = date;//将日期传回
            config.getRecordMainHandler().sendMessage(msg);
            return true;
        } else {
            return false;
        }
    }

}