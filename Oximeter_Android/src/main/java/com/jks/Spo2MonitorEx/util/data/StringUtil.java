package com.jks.Spo2MonitorEx.util.data;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 16/8/31.
 */

/**
 * 处理字符串的工具类
 */
public class StringUtil {
    public static String checkString(String string) {
        boolean isNum = false, isInfo = true;
        String[] temp = string.split("/(.*)\r\n|\n|\r/g");
        StringBuffer resoult = new StringBuffer();
        // Log.v("test", "temp.length:" + temp.length);
        if (temp.length > 1) {
            resoult.append("<div class='section_content'>");
            for (String str : temp) {
                // Log.e("test", "str:" + str);

                String reg1 = "^\\d.*?$"; // ^表示字符串开始，\\d表示数字
                if (Pattern.matches(reg1, str)) {
                    resoult.append("<div class='section_title'><p>" + str + "<p></div>");
                    // Log.e("test", "str:" + str);
                } else if (str.startsWith("●")) {
                    resoult.append("<div class='section_step'><p>" + str + "<p></div>");
                    // Log.e("test", "str:" + str);
                } else {
                    resoult.append("<div class='section_info'><p>" + str + "<p></div>");
                }
            }
            resoult.append("</div>");
        } else {

            resoult.append("<div class='section_info'>");
            resoult.append("<p>");
            resoult.append(string);
            resoult.append("</p>");
            resoult.append("</div>");
            // Log.v("test",resoult.toString());
            // Log.v("test", "str:" + resoult.toString());
        }

        return resoult.toString();

    }

    /**
     * 获取当前时间来命名，以免有重复的文件名,再加上5位的随机数
     *
     * @return
     */
    public static String getTimeName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmssSSS");
        String timeName = dateFormat.format(date) + "_" + new Random().nextInt(99999);
        return timeName;
    }

    public static String getTimeNameSql() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#000");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddhhmmssSSS");
        String timeName = dateFormat.format(date) + df.format(new Random().nextInt(999));
        return timeName;
    }

    /**
     * 将 8/29拿出两位数字
     */
    public static int[] getIntByString(String str) {
        String[] bgStrs = str.split("\\.");
        int[] bgs = new int[bgStrs.length];
        for (int i = 0; i < bgStrs.length; i++) {
            bgs[i] = Integer.valueOf(bgStrs[i]);
        }
        return bgs;
    }

    /**
     * 判断字符串中是否含有中文字符
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            try {
                if (isChineseChar(str.charAt(i))) {
                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean isChineseChar(char c)
            throws UnsupportedEncodingException {
        // 如果字节数大于1，是汉字
        return String.valueOf(c).getBytes("GBK").length > 1;
    }

    /**
     * 去除字符串中的空格
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取value字符串压缩后base64编码的字符串
     * @param value 需要压缩的字符串
     * @return base64编码的字符串
     */
    public static String getCompressString(String value) {
        byte[] result = ZLibUtils.compress(value.getBytes());
        byte[] encod = Base64.encode(result, Base64.DEFAULT);
        String compressStr = new String(encod);
        String noBlankStr = StringUtil.replaceBlank(compressStr);
        return noBlankStr;
    }

    /**
     * 获取base64编码字符串解压后的字符串
     * @param value base64编码字符串
     * @return 原字符串
     */
    public static String getDeCompressSting(String value) {
        byte[] unencod = Base64.decode(value.getBytes(), Base64.DEFAULT);
        byte[] dedata = ZLibUtils.decompress(unencod);
        String deStr = new String(dedata);
        return deStr;
    }

    /** * 检测是否有emoji表情 * @param source * @return */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /** * 判断是否是Emoji * @param codePoint 比较的单个字符 * @return */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 将byte数组转16进制字符串
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 将字符串转换为long形
     * @param str
     * @param base
     * @return
     */
    public static long strtoul(String str,int base){
        return strtoul((str+"\0").toCharArray(),base);
    }

    /**
     * 将chart数组转换为long性
     * @param cp
     * @param base
     * @return
     */
    private static long strtoul(char[] cp,int base){
        long result=0,value;
        int i=0;
        if(base==0){
            base=10;
            if(cp[i]=='0'){
                base=8;
                i++;
                if(Character.toLowerCase(cp[i])=='x'&&isxdigit(cp[1])){
                    i++;
                    base=16;
                }
            }
        }else if(base==16){
            if(cp[0]=='0'&&Character.toLowerCase(cp[1])=='x')
                i+=2;
        }
        //获取16进制数
        while(isxdigit(cp[i])&&(value = isdigit(cp[i]) ? cp[i]-'0' : Character.toLowerCase(cp[i])-'a'+10) < base){
            result=result*base+value;
            i++;
        }
        return result;
    }

    /**
     * 判断chart字符为16进制字符
     * @param c
     * @return
     */
    private static boolean isxdigit(char c){
        return ('0' <= c && c <= '9')||('a' <= c && c <= 'f')||('A' <= c && c <= 'F');
    }
    private static boolean isdigit(char c){
        return '0' <= c && c <= '9';
    }
}
