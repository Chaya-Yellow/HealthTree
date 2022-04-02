package com.jks.Spo2MonitorEx.util.web;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by apple on 16/7/16.
 */
public class HttpUtil {
    static String WebIP = "";
    /**
     * 获取外网Ip
     * @param context
     * @return
     * @throws Exception
     */
    public static String GetNetIp(Context context) throws Exception {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = null;
        // 判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)
                    + "." + (ipAddress >> 24 & 0xFF);
        } else {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress().toString();
                        break;
                    }
                }
            }

        }
        return ip;
    }

    /**
     * 获取网络IP
     * @return
     */
    public static String getWebIp() {
        return "";
//        MyThread.startNewThread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
//                    URL url = new URL(address);
//                    HttpURLConnection connection = (HttpURLConnection) url
//                            .openConnection();
//                    connection.setUseCaches(false);
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
//                    {
//                        InputStream in = connection.getInputStream();// 将流转化为字符串
//                        BufferedReader reader = new BufferedReader(
//                                new InputStreamReader(in));
//
//                        String tmpString = "";
//                        StringBuilder retJSON = new StringBuilder();
//                        while ((tmpString = reader.readLine()) != null)
//                        {
//                            retJSON.append(tmpString + "\n");
//                        }
//
//                        JSONObject jsonObject = new JSONObject(retJSON.toString());
//                        String code = jsonObject.getString("code");
//                        if (code.equals("0"))
//                        {
//                            JSONObject data = jsonObject.getJSONObject("data");
//                            WebIP = data.getString("ip");
//
//                            Log.e("提示", "您的IP地址是：" + WebIP);
//                        }
//                        else
//                        {
//                            WebIP = "";
//                            Log.e("提示", "IP接口异常，无法获取IP地址！");
//                        }
//                    }
//                    else
//                    {
//                        WebIP = "";
//                        Log.e("提示", "网络连接异常，无法获取IP地址！");
//                    }
//                }
//                catch (Exception e)
//                {
//                    WebIP = "";
//                    Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
//                }
//            }
//        });
//
//        return WebIP;
    }

    public static String getRealIp() throws SocketException {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP

        Enumeration<NetworkInterface> netInterfaces =
                NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }
}
