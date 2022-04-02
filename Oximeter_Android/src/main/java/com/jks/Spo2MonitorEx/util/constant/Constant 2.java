package com.jks.Spo2MonitorEx.util.constant;

import android.content.Context;
import android.util.Xml;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.entity.PublishPlatform;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/7/23.
 */
public class Constant {
    public final static int TIME_FREQUENCY = 0; // 按次数
    public final static int TIME_DAY = 12; // 按日
    public final static int TIME_WEEK = 84; // 按周
    public final static int TIME_MONTH = 365; // 按月

    public final static int MESSAGE_POP = 2016831;
    public static final int DELAY_OPERATION = 115;

    // public final static int BG_LOW = 0;
    // public final static int BG_HEIGHT = 25;
    public final static String AVATAR_PATH = "blt/oximeter/user/";
    //接口url
    public final static String BLT_URL = "http://47.88.25.86:8888/api/data";
//    public final static String BLT_URL = "http://192.168.0.71:8888/api/data";
    //获取用户头像路径
    public final static String BLT_GET_IMGURL = "http://47.88.25.86:8888/home/GetImg/?memberId=";
//    public final static String BLT_GET_IMGURL = "http://192.168.0.71:8888/home/GetImg/?memberId=";

    public final static int LANGUAGE_CN = 0; // 简体中文
    public final static int LANGUAGE_TW_CN = 1; // 繁体中文
    public final static int LANGUAGE_EN = 2; // 英文
    public final static String LANGUAGE_NET_EN = "101"; // 英文

    public final static int AppId = 201;

    //日期选中后传到HistoryActivity Handler
    public static final int BTRECORDHANDLER_UPDATE_BOTTOMLIST = 111;
    //手势标记
    public static final int GESTURELISTENER_UPUI_LEFT = 112;//日期列表左滑
    public static final int GESTURELISTENER_UPUI_RIGHT = 113;//日期;列表右滑
    public static final int GESTURELISTENER_UPUI_UPTEMP = 114;

    public static final int NOTIFICATION_UPDATE_ID = 1;//应用有更新通知的id
    public static final int NOTIFICATION_DOWNING_ID = 2;//更新应用正在下载通知的id

    /**
     * 获取当前手机语言
     *
     * @param context
     * @return
     */
    public static int getlanguage(Context context) {
        int language = Integer.valueOf(context.getResources().getString(R.string.language));
        if (language == 3) {
            language = 2;
        }
        return language;
    }

    /**
     * 获取assets资源文件中发布平台的信息
     * @param context
     * @return
     */
    public static List<PublishPlatform> getPublishPlatform(Context context) {
        try {
            InputStream is = context.getAssets().open("appPlatforms.xml");
            List<PublishPlatform> platforms = null;
            PublishPlatform platform = null;
            XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
            parser.setInput(is, "UTF-8"); // 设置输入流 并指明编码方式

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        platforms = new ArrayList<PublishPlatform>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("platformInfo")) {
                            platform = new PublishPlatform();
                        } else if (parser.getName().equals("platform")) {
                            eventType = parser.next();
                            platform.setPlatform(parser.getText());
                        } else if (parser.getName().equals("urlzh")) {
                            eventType = parser.next();
                            platform.setUrlzh(parser.getText());
                        } else if (parser.getName().equals("urlen")) {
                            eventType = parser.next();
                            platform.setUrlen(parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("platformInfo")) {
                            platforms.add(platform);
                            platform = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            return platforms;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
