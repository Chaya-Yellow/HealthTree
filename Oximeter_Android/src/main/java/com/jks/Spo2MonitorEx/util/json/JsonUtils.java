package com.jks.Spo2MonitorEx.util.json;

import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils<T> {

	/**
	 * 将value转换为json字符串
	 * @param value
	 * @return
	 */
	public String getJsonString(T value) {
		return new GsonBuilder().create().toJson(value);
	}

    /**
     * 获取服务器的错误码
     * @param result 服务器返回的json数据
     * @return
     */
    public static int getServerErrCode(String result) {
        try {
            JSONObject resJson = new JSONObject(result);
            return resJson.getInt("error_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
