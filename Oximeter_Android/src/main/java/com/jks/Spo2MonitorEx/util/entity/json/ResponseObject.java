package com.jks.Spo2MonitorEx.util.entity.json;

/**
 * Created by apple on 16/8/24.
 */
public class ResponseObject<T> {
    private int error_code;
    private T data;

    public ResponseObject() {
    }

    public ResponseObject(int state, T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
