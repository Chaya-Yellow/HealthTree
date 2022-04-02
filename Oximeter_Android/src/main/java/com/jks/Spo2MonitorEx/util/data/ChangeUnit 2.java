package com.jks.Spo2MonitorEx.util.data;

import android.content.Context;
import android.util.Log;

import com.jks.Spo2MonitorEx.R;

/**
 * 单位转换
 * Created by apple on 16/8/30.
 */
public class ChangeUnit {
    private final static float KGToPound = 2.20462f; // 公斤转英镑
    private final static float PoundToKG = 0.45392f; // 英镑转公斤
    private final static float CMToInches = 0.39370078740157f; // 厘米转英寸
    private final static float InchesToCM = 2.54f; // 厘米转英寸
    private final static float KCALTOKJ = 4.184f;// 千卡转千焦

    public static float PoundToKG(float pound) {
        if (pound == -1)
            return -1;
        return Math.round(pound * PoundToKG * 10) / 10.0f;
    }

    public static float KGToPound(float weight) {
        if (weight == -1)
            return -1;
        return Math.round(weight * KGToPound * 10) / 10.0f;
    }

    public static float CMToInches(float height) {
        if (height == -1)
            return -1;
        return Math.round(height * CMToInches * 10) / 10.0f;
    }

    public static float KCALTOKJ(float energry) {
        if (energry == -1)
            return -1;
        return Math.round(energry * KCALTOKJ * 10) / 10.0f;
    }

    public static String getHeightUnitInche(Context context) {
        String heightUnit = context.getResources().getString(R.string.more_setting_height_inch);
        return heightUnit;
    }

    // 单位转换MMOL转为Mg
    public static float MmolToMg(float value) {
        float result = value * 18;
        return result;
    }

    // 单位转换Mg转为MMOL
    public static float MglToMMOL(float value) {
        float result = value / 18;
        return result;
    }

    // 根据SPUtil拿出默认单位
    public static String getUnit(Context context) {
        // TODO Auto-generated method stub
        int unitIndex;// 血糖单位下标
        String string = "";
        unitIndex = SharedPreferencesUtil.getUnit(context);
        switch (unitIndex) {
            case 0:
                string = context.getString(R.string.system_setting_unit_mmol);
                break;
            case 1:
                string = context.getString(R.string.system_setting_unit_mg);
                break;
            default:
                break;
        }
        Log.d("test", "getUnit::" + string);
        return string;
    }
}
