package com.jks.Spo2MonitorEx.util.json;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.dao.OximetIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.OximetIfcImpl;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.data.StringUtil;
import com.jks.Spo2MonitorEx.util.dbhelper.DBHelper;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximetTamp;
import com.jks.Spo2MonitorEx.util.entity.json.OximetResp;
import com.jks.Spo2MonitorEx.util.entity.json.ResponseObject;
import com.jks.Spo2MonitorEx.util.entity.json.SyncDataBean;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by apple on 16/9/18.
 */
public class SyncUtils {
    public final static int SENDSYNCSUCCESS = 12121;//同步成功
    public final static int SENDSYNCFAIL = 12122;//同步失败
    public static AtomicInteger count = new AtomicInteger(0);

    public static void syncOximets(final Handler handler, final Context context, final Config config) {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                SyncDataBean syncDataBean = new SyncDataBean();
                syncDataBean.setKey("SyncData");
                syncDataBean.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                syncDataBean.setAccountId(SharedPreferencesUtil.getMemberId(context));
                syncDataBean.setTimestamp(Float.parseFloat(SharedPreferencesUtil.getTime(context)));
                syncDataBean.setValue(oximet2String(context));
                String syncDataJson = new JsonUtils<SyncDataBean>().getJsonString(syncDataBean);
                try {
                    params.setBodyEntity(new StringEntity(syncDataJson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
                new HttpUtils().send(HttpRequest.HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.e("同步测量数据成功后的返回", responseInfo.result);
                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
                        if (errorCode == 0) {
                            Gson gson = new Gson();
                            ResponseObject<OximetResp> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<OximetResp>>() {
                            }.getType());
                            String synServerData = object.getData().getValue();//获取服务器返回的测量数据
                            if (synServerData != null && !synServerData.equals("")) {
//                                String dataStr = StringUtil.getDeCompressSting(synServerData);
//
//                                List<Oximet> oximets = null;
//                                try {
//                                    JSONArray syncDataJson = new JSONArray(dataStr);
//                                    oximets = getSynRespData(syncDataJson);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                insertOximetValue(context, handler, oximets);
//                                SharedPreferencesUtil.setTime(context, String.valueOf(timestamp));
                                float timestamp = object.getData().getTimestamp();
                                insertOximetValue(context, timestamp, synServerData, handler);
                            }
                        }else {
                            if (count.get() < 5) {
                                count.getAndIncrement();
                                syncOximets(handler, context, config);
                            }else {
                                sendMessage(handler, SENDSYNCFAIL);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (count.get() < 5) {
                            count.getAndIncrement();
                            syncOximets(handler, context, config);
                        }else {
                            sendMessage(handler, SENDSYNCFAIL);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取数据库中血氧数据的压缩数据
     * @param context
     * @return
     */
    public static String oximet2String(Context context) {
        OximetIfcImpl oximetIfc = new OximetIfcImpl(context);
        //Log.e("SharedPreferencesUtil.getLocalTime(context)","SharedPreferencesUtil.getLocalTime(context)"+SharedPreferencesUtil.getLocalTime(context));
        List<Oximet> oximets = oximetIfc.findAllByTime(SharedPreferencesUtil.getLocalTime(context));
             for (Oximet oximet:oximets)
             {
              oximet.setRecord_date(MyDateUtil.formateDate3(oximet.getRecordDate()));
             }
        for (Oximet oximet:oximets)
        {
            Log.e("oximet.........发送的",""+oximet.toString());
        }
        String jsonData = new GsonBuilder().create().toJson(oximets);
        String noBlankStr = StringUtil.getCompressString(jsonData);
        return noBlankStr;
    }

    /**
     * 获取同步后的数据(解析json数组, 获取数组中的每一个数据)
     * @param data
     * @return
     */
//    public static List<Oximet> getSynRespData(JSONArray data) {
//        List<Oximet> oximets = new ArrayList<Oximet>();
//        Log.e("在解析", data.length() + "");
//        for (int i = 0; i < data.length(); i++) {
//            try {
////                Oximet oximet = new Oximet();
//                JSONObject object = data.getJSONObject(i);
//                Oximet oximet = getOximetObj(object);
//                oximets.add(oximet);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.e("解析完", oximets.size() + "");
//        return oximets;
//    }

//    public static Oximet getOximetObj(JSONObject object) {
//        try {
//            Oximet oximet = new Oximet();
//            oximet.setSPO2(object.getInt("SPO2"));
//            oximet.setIs_deleted(object.getBoolean("is_deleted") ? 1 : 0);
//            oximet.setPI(object.getInt("PI"));
//            oximet.setPR(object.getInt("PR"));
//            oximet.setRecord_date(MyDateUtil.getStringSS(object.getString("record_date")));
//            oximet.setCreated_date(MyDateUtil.getStringSS(object.getString("created_date")));
//            oximet.setUpdated_date(MyDateUtil.getStringSS(object.getString("updated_date")));
//            oximet.setValueId(String.valueOf(object.getLong("valueId")));
//            oximet.setMemberId(object.getInt("memberId"));
//            oximet.setRESP(object.getInt("RESP"));
//            return oximet;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 将同步到的数据插入到数据库中
     * @param context
     * @param handler
     * @param oximets 同步到的数据
     */
//    public static void insertOximetValue(final Context context, final Handler handler, final List<Oximet> oximets) {
//        MyThread.startNewThread(new Runnable() {
//            @Override
//            public void run() {
//                OximetIfc oximetIfc = new OximetIfcImpl(context);
//                DBHelper.getDb().beginTransaction();
//                for (int i = 0; i < oximets.size(); i++) {
//                    oximetIfc.insert(oximets.get(i));
//                }
//                DBHelper.getDb().setTransactionSuccessful();
//                DBHelper.getDb().endTransaction();
//                //记录本次同步的时间
//                SharedPreferencesUtil.setLocalTime(context, MyDateUtil.getDateFormatToStringSS(null));
//                sendMessage(handler, SENDSYNCSUCCESS);
//            }
//        });
//    }

    /**
     * 将同步到的数据插入到数据库中
     * @param context
     * @param timestamp 同步的时间戳
     * @param synServerData 解压后的数据字符串
     * @param handler
     */
    private static void insertOximetValue(final Context context, final float timestamp, final String synServerData, final Handler handler) {
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                String dataStr = StringUtil.getDeCompressSting(synServerData);
                Log.e("解压后", dataStr);

                Type listType = new TypeToken<ArrayList<OximetTamp>>(){}.getType();
                ArrayList<OximetTamp> syncDataArray = new Gson().fromJson(dataStr, listType);
                Log.e("插入的长度", syncDataArray.size() + "");
                if (syncDataArray.size() > 0) {
                    OximetIfc oximetIfc = new OximetIfcImpl(context);
                    DBHelper.getDb().beginTransaction();
                    for (int i = 0; i < syncDataArray.size(); i++) {
                        OximetTamp oximetTamp = syncDataArray.get(i);
                        oximetTamp.setRecord_date(MyDateUtil.getStringSS(oximetTamp.getRecord_date()));//接收下来额是
                        oximetTamp.setCreated_date(MyDateUtil.getDateFormatToString(null));//这个是在
                        oximetTamp.setUpdated_date(MyDateUtil.getDateFormatToString(null));
//                        oximetTamp.setUpdated_date(MyDateUtil.getStringSS(oximetTamp.getUpdated_date()));
                        oximetTamp.setIs_deleted(oximetTamp.getIs_deleted());
                        oximetTamp.setFamilyID(oximetTamp.getMemberId());
                        oximetIfc.insert(oximetTamp);
                    }
                    DBHelper.getDb().setTransactionSuccessful();
                    DBHelper.getDb().endTransaction();
                    //记录本次同步的时间
                    SharedPreferencesUtil.setTime(context, String.valueOf(timestamp));
                }
                Log.e("插入完成", "2222");

                SharedPreferencesUtil.setLocalTime(context, MyDateUtil.getDateFormatToStringSS(null));
                Log.e("MYActivity的localtime" ,"看看这个时间"+MyDateUtil.getDateFormatToStringSS(null));
                sendMessage(handler, SENDSYNCSUCCESS);
            }
        });
    }

    private static void sendMessage(Handler handler, int what) {
        Message message = new Message();
        message.what = what;
        handler.sendMessage(message);

    }
}
