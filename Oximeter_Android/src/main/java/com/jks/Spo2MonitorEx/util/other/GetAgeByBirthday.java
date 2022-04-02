package com.jks.Spo2MonitorEx.util.other;

import com.jks.Spo2MonitorEx.util.data.MyDateUtil;

/**
 * Created by apple on 16/8/30.
 */
public class GetAgeByBirthday {
    public static int getAgeByBirthday(String birthday){
        int age = 0;
        try {
            String nowDate = MyDateUtil.getDateFormatToString(null);
            // Log.d("test", "nowDate::" + nowDate);
             nowDate=MyDateUtil.getStringSS(nowDate);
            String[] dateStr = nowDate.split(" ");
            String[] dateNow = dateStr[0].split("-");
            String[] dateBir = birthday.split("-");
            // Log.d("test", "dateNow[0]::" + dateNow[0]);
            // Log.d("test", "dateBir[0]::" + dateBir[0]);
            age = Integer.valueOf(dateNow[0]) - Integer.valueOf(dateBir[0]);
            if (Integer.valueOf(dateNow[1]) < Integer.valueOf(dateBir[1])) {
                age--;
            } else if (Integer.valueOf(dateNow[1]) == Integer.valueOf(dateBir[1])
                    && Integer.valueOf(dateNow[2]) < Integer.valueOf(dateBir[2])) {
                age--;
            }
            // 由于系统时间不正确导致年龄为负数 。系统崩溃

        } catch (Exception e) {
            // TODO: handle exception
            age = 1;
        }
        if (age < 0) {
            age = 1;
        }
        return age;
    }
}
