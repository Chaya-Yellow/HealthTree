package com.jks.Spo2MonitorEx.util.data;

import android.content.Context;
import android.util.Log;

import com.jks.Spo2MonitorEx.util.constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 16/7/23.
 */
public class MyDateUtil {
    public static final int EVERYTIME = 0;
    public static final int EVERYDAY = 1;
    public static final int EVERYWEEK = 2;
    public static final int EVERYMONTH = 3;

    /** 如果不传格式。。就用默认的格式:yyyy-MM-dd HH-mm-ss   改成是假毫秒的 */
    public static String getDateFormatToString(String format) {
        Date date = new Date();
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss.SSS";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }
    //用来获取时间戳
    public static String getDateFormatToStringSS(String format) {
        Date date = new Date();
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }
    /** :yyyy-MM-dd */
    public static String getStringDD(String date) {
        String[] dateStr = date.split(" ");
        // System.out.println("dateStr[0]:" + dateStr[0]);
        return dateStr[0];
    }

    /** :yyyy-MM-dd:HH:mm:ss */
    public static String getStringSS(String date) {
        if (date.contains("."))
        {
            String[] dateStr = date.split("\\.");
            // System.out.println("dateStr[0]:" + dateStr[0]);
            // System.out.println("dateStr[0] =" + dateStr[0]);
            return dateStr[0];
        }
        else
        {
            return date;
        }
//        String[] dateStr = date.split("\\.");
        // System.out.println("dateStr[0]:" + dateStr[0]);
        // System.out.println("dateStr[0] =" + dateStr[0]);
//        return dateStr[0];
    }

    // public static String getY_m_d(String y_m_d) {
    // SimpleDateFormat dateFormat = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Date date = null;
    // try {
    // date = dateFormat.parse(y_m_d);
    // } catch (Exception e) {
    // Log.e("test","getY_m_d_e.getMessage():"+e.getMessage());
    // }
    // dateFormat = new SimpleDateFormat("yy-MM-dd");
    // y_m_d = dateFormat.format(date);
    // return y_m_d;
    // }
    /***
     *
     * @param date
     *            yy-mm-dd hh:mm:ss
     * @return hh:mm am/pm
     */
    public static String getH_MinByDate(String date) {
        // System.out.println("date="+date);
        String dateStr = date.split(" ")[1];
        // System.out.println("dateStr1 = "+dateStr);
        String[] dateStrs = dateStr.split(":");
        // System.out.println("dateStrs[0]="+dateStrs[0]);
        int hh = Integer.valueOf(dateStrs[0]);
        int mm = Integer.valueOf(dateStrs[1]);
        // if(hh>=12){
        // hh = hh - 12;
        // return getSpell_H_M(hh,mm)+"";
        // }else{
        // return getSpell_H_M(hh,mm)+"";
        // // return getSpell_H_M(hh,mm)+" AM";
        // }
        // dateStr = dateStrs[0]+":"+dateStrs[1];
        // return dateStr;
        return getSpell_H_M(hh, mm) + "";
    }

    public static String getH_Min(String h_min) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(h_min);
        } catch (Exception e) {
            // Log.e("test","getH_Min_e.getMessage():"+e.getMessage());
        }
        h_min = dateFormat.format(date);
        return h_min;
    }

    /** 是否早于今天 */
    public static boolean isBeforeToDay(String y_m_d) {
        int compareTo = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(y_m_d);

            Date nowDate = dateFormat.parse(dateFormat.format(new Date()));
            compareTo = date.compareTo(nowDate);

        } catch (Exception e) {
            // Log.e("test","isBeforeToDay_e.getMessage():"+e.getMessage());
        }
        if (compareTo < 0) {
            return true;
        } else {
            return false;
        }
    }

    /** 返回拼写好格式为2008-11-05 21:33 */
    public static String getSpellTime(int curYears, int curMonths, int curDays, int curHours, int curMinutes) {
        StringBuilder y_m_d = getSpell_Y_M_D(curYears, curMonths, curDays);
        StringBuilder h_M = getSpell_H_M(curHours, curMinutes);
        return y_m_d.toString() + h_M.toString();
    }

    public static StringBuilder getSpell_H_M(int curHours, int curMinutes) {
        StringBuilder h_M = new StringBuilder();
        if (curHours > 9) {
            h_M.append(curHours);
        } else {
            h_M.append("0" + curHours);
        }
        h_M.append(":");
        if (curMinutes > 9) {
            h_M.append(curMinutes);
        } else {
            h_M.append("0" + curMinutes);
        }

        return h_M;
    }

    public static StringBuilder getSpell_Y_M_D(int curYears, int curMonths, int curDays) {
        StringBuilder y_m_d = new StringBuilder();
        y_m_d.append(curYears);
        y_m_d.append("-");
        if (curMonths + 1 > 9) {
            y_m_d.append(curMonths + 1);
        } else {
            y_m_d.append("0" + (curMonths + 1));
        }
        y_m_d.append("-");
        if (curDays > 9) {
            y_m_d.append(curDays);
        } else {
            y_m_d.append("0" + curDays);
        }
        y_m_d.append(" ");
        return y_m_d;
    }

    public static StringBuilder getSpell_M_D(int curMonths, int curDays) {
        StringBuilder y_m_d = new StringBuilder();

        if (curMonths + 1 > 9) {
            y_m_d.append(curMonths + 1);
        } else {
            y_m_d.append("0" + (curMonths + 1));
        }
        y_m_d.append("-");
        if (curDays > 9) {
            y_m_d.append(curDays);
        } else {
            y_m_d.append("0" + curDays);
        }
        y_m_d.append(" ");
        return y_m_d;
    }

    // 给定一个日期，返回指定格式的日期
    public static String formateDate3(String date_Str) {
        // yyyy-MM-dd HH:mm:ss E
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateformat.parse(date_Str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateformat.format(date);
    }

    // 给定一个日期，返回指定格式的日期
    public static String formateDate4(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String formateDate = dateformat.format(date);
        return formateDate;
    }

    // 给定一个日期，返回指定格式的日期
    public static String formateDate2(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formateDate = dateformat.format(date);
        return formateDate;
    }

    // 给定一个日期，返回指定格式的日期
    public static String formateDate2(String date) {
        // yyyy-MM-dd
        String[] dateCopy = date.split(" ");
        return dateCopy[0];
    }

    public static int[] formateDate(String date) {
        int[] dates = new int[3];
        String[] dateStr = date.split("-");
        for (int i = 0; i < dateStr.length; i++)
            dates[i] = Integer.valueOf(dateStr[i]);
        return dates;
    }

    /**
     * 传入日期，返回今天或明天的
     *
     * @param date
     * @return
     */
    // public static String getDate(Context context, String date) {
    // // System.out.println("date=" + date);
    // int days = getDateDays(getDateFormatToString(null), date);
    // // System.out.println("days===========" + days);
    // String[] dateStr = date.split(" ");
    // String[] i = dateStr[1].split(":");
    // dateStr[1] = i[0] + ":" + i[1];
    // if (days == 0) {
    // date = context.getString(R.string.first_day) + " " + dateStr[1];
    // } else if (days == 1) {
    // date = context.getString(R.string.first_yesterday) + " "
    // + dateStr[1];
    // } else {
    // String[] j = dateStr[0].split("-");
    // date = j[1] + context.getString(R.string.first_time_moon) + j[2]
    // + context.getString(R.string.first_time_day) + " "
    // + dateStr[1];
    // }
    // return date;
    // }
    //

    public static String getDate(Context context, String date) {
        // System.out.println("date=" + date);
        int days = getDateDays(getDateFormatToString(null), date);
        // System.out.println("days===========" + days);
        String[] dateStr = date.split(" ");
        String[] i = dateStr[1].split(":");
        int h, m;
        h = Integer.valueOf(i[0]);
        m = Integer.valueOf(i[1]);
        dateStr[1] = i[0] + ":" + i[1];
        if (Constant.getlanguage(context) == Constant.LANGUAGE_EN) {
            if (days == 0) {
                if (h > 12) {
                    h = h - 12;
                    date = "Today," + h + ":" + m + " PM";
                } else {
                    date = "Today, " + h + ":" + m + " AM";
                }
            } else if (days == 1) {
                if (h > 12) {
                    h = h - 12;
                    date = "Yesterday," + h + ":" + m + " PM";
                } else {
                    date = "Yesterday, " + h + ":" + m + " AM";
                }
            } else if (days <= 28) {
                date = days + " days ago";
            } else {
                date = i[2] + "-" + i[1] + "-" + i[0];
            }
        } else {
            if (days == 0) {
                if (h > 12) {
                    h = h - 12;
                    date = "今天，下午 " + h + ":" + m;
                } else {
                    date = "今天，上午 " + h + ":" + m;
                }
            } else if (days == 1) {
                if (h > 12) {
                    h = h - 12;
                    date = "昨天，下午 " + h + ":" + m;
                } else {
                    date = "昨天，上午 " + h + ":" + m;
                }
            } else if (days <= 28) {
                date = days + "天前";
            } else {
                date = dateStr[0];
            }
        }
        return date;
    }

    public static int getDateDays(String date1, String date2) {
        date1 = date1.split(" ")[0];
        date2 = date2.split(" ")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int days = 0;
        try {
            Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
            Date dateBegin = sdf.parse(date2);
            // System.out.println("getDateDays:date" + date);
            // ;
            // System.out.println("dateBegin" + dateBegin);
            ;
            long betweenTime = date.getTime() - dateBegin.getTime();
            days = (int) (betweenTime / 1000 / 60 / 60 / 24);
        } catch (Exception e) {
            // System.out.println(e.toString());
        }
        // System.out.println("day==" + days);
        return days;
    }

    public static String getDateDays(String date1) {
        date1 = date1.split(" ")[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = null;
        try {
            Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期
            long betweenTime = date.getTime();
            str = betweenTime + "";
        } catch (Exception e) {
        }
        return str;
    }

    // 前一天 23:59:59
    public static String getPreDay(Calendar calendar) {

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return dateFormat.format(calendar.getTime());
    }

    // 其实是前天 23:59:59
    public static String getNextDay(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return dateFormat.format(calendar.getTime());
    }

    // 其实是前天 23:59:59
    public static String getCurrentDay(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return dateFormat.format(calendar.getTime());
    }

    public static int getMondayPlus(Calendar cd) {
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    public static int getSundayPlus(Calendar cd) {
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        return 1 - dayOfWeek;
    }

    public static Calendar getMonday(Calendar currentDate) {
        int sundayPlus = getMondayPlus(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, sundayPlus);
        return calendar;
    }

    public static Calendar getSunday(Calendar currentDate) {
        int sundayPlus = getSundayPlus(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, sundayPlus);
        return calendar;
    }

    public static Calendar getForWeekMonday(int week, Calendar NextDate) {
        Calendar calendar = getMonday(NextDate);
        calendar.add(Calendar.DATE, (week * 7));
        return calendar;
    }

    public static Calendar getForWeekSunday(int week, Calendar NextDate) {
        Calendar calendar = getSunday(NextDate);
        calendar.add(Calendar.DATE, (week * 7));
        return calendar;
    }

    /**
     * 获取对应日期的日期对象
     * @param date
     * @return
     */
    public static Calendar calendarSetTime(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(date);
            calendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static Boolean equalCalendarToMonth(Calendar calendar1, Calendar calendar2) {
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
            return true;
        }else {
            return false;
        }
    }

    public static float getDateTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(dateStr);
            float time = date.getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 十进制转换任意进制[秒, 分, 时]
     * 主要用来: 将十进制转换为时间60进制
     * @param num
     * @param n
     * @return
     */
    public static int[] transform(int num,int n){
        //参数num为输入的十进制数，参数n为要转换的进制
        int[] data = new int[100];
        int array[]=new int[100];
        int location=0;
        while(num!=0){//当输入的数不为0时循环执行求余和赋值
            int remainder=num%n;
            num=num/n;
            array[location]=remainder;//将结果加入到数组中去
            location++;
        }
//        show(array,location-1);
        return array;
    }
    private void show(int[] arr, int n){
        for(int i=n;i>=0;i--){
            if(arr[i]>9){
                Log.e("60进制1", String.valueOf((arr[i])));
            }
            else {
                Log.e("60进制", arr[i]+"");
            }
        }
    }

    // 判断当前日期的位置
    public static Boolean calendarEqToday(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String current = format.format(Calendar.getInstance().getTime());
            String date = format.format(calendar.getTime());
            if (current.equals(date)) {
                return true;
            }
        }
        return false;
    }
}
