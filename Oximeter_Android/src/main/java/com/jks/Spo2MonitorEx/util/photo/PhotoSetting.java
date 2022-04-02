package com.jks.Spo2MonitorEx.util.photo;

import android.content.Context;

/**
 * Created by apple on 16/8/31.
 */
public class PhotoSetting {
    public final static String PROJECTNAME = "Oximeter";
    // /** 图片在SD卡上的存储路径 */
    public static String PICTURE_SAVE_PATH = "blt/oximeter/user/";
    public static final String PICSAVESIZE = "picSaveSize";

    /***
     * 获取图片存放大小偏好, 默认为3
     *
     * @param context
     * @return
     */
    public static int getPicSaveSize(Context context) {
        return context.getSharedPreferences(PROJECTNAME, context.MODE_PRIVATE)
                .getInt(PICSAVESIZE, 3);
    }
}
