package com.jks.Spo2MonitorEx.util.web;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by apple on 16/9/1.
 */
public class StreamUtil {

    /**
     * 返回字节数组
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] read(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (in != null) {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            return out.toByteArray();
        }
        return null;
    }
}
