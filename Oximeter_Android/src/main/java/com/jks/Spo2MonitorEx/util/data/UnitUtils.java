package com.jks.Spo2MonitorEx.util.data;

import java.math.BigDecimal;

/**
 * 单位转换
 * Created by apple on 16/8/30.
 */
public class UnitUtils {
    /** 厘米转英寸系数 */
    public static final float CM2IN = 0.3937008F;
    /** 英寸转厘米系数 */
    public static final float IN2CM = 2.54F;
    /** 公斤转磅系数 */
    public static final float KG2BL = 2.2046226F;
    /** 磅转公斤 */
    public static final float BL2KG = 0.4535924F;

    /**
     * 华氏度转摄氏度
     * @param f
     * @return
     */
    public static float F2C(float f) {
        return FloatFormat((f - 32.0F) / 1.8F);
    }

    /**
     * 摄氏度转华氏度。
     *
     * @param
     * @return
     */
    public static float C2F(float c) {
        return FloatFormat(c * 1.8F + 32.0F);
    }

    /**
     * 保持两个数字的格式，如果小于10，在前面加一个0；
     *
     * @param num
     * @return
     */
    public static String TwoReserved(int num) {
        if (num <= 9) {
            return "0" + num;
        } else {
            return num + "";
        }

    }

    /**
     * 公斤转磅。
     *
     * @param num
     * @return
     */
    public static float Kg2Bl(float num) {
        return num * KG2BL;
    }

    /**
     * 磅转公斤。
     *
     * @param num
     * @return
     */
    public static float Bl2kg(float num) {
        return num * BL2KG;
    }

    // /**
    // * 保留一位小数、
    // *
    // * @param num
    // * @return
    // */
    // public static String Float2Str(float num) {
    // // float price=1.2;
    //
    // DecimalFormat decimalFormat = new DecimalFormat(".0");//
    // 构造方法的字符格式这里如果小数不足2位,会以0补足.
    // return decimalFormat.format(num);// format 返回的是字符串
    // }

    /**
     * 保留2位小数。
     *
     * @param num
     * @return
     */
    public static float FloatFormat(float num) {
        BigDecimal b = new BigDecimal(num);
        // b.setScale(2, BigDecimal.ROUND_HALF_UP) 表明四舍五入，保留两位小数
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 保留3位小数。
     *
     * @param num
     * @return
     */
    public static float FloatFormat3(float num) {
        BigDecimal b = new BigDecimal(num);
        // b.setScale(2, BigDecimal.ROUND_HALF_UP) 表明四舍五入，保留两位小数
        return b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 保留1位小数。
     *
     * @param num
     * @return
     */
    public static float FloatFormat1(float num) {
        BigDecimal b = new BigDecimal(num);
        // b.setScale(2, BigDecimal.ROUND_HALF_UP) 表明四舍五入，保留两位小数
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 四舍五入取整。
     *
     * @param num
     * @return
     */
    public static int Float2Int(float num) {
        return (int) (num + 0.5F);
    }

    /**
     * 厘米转英寸
     *
     * @param num
     * @return
     */
    public static float Cm2In(float num) {
        return num * CM2IN;
    }

    /**
     * 英寸转厘米。
     *
     * @param num
     * @return
     */
    public static float In2Cm(float num) {
        // Log.v("test", "num * IN2CM::"+num * IN2CM);
        return num * IN2CM;
    }
}
