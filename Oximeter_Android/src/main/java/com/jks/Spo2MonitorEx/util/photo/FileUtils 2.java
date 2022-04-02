package com.jks.Spo2MonitorEx.util.photo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by apple on 16/8/31.
 */
public class FileUtils {
    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E", "png");
        mFileTypes.put("474946", "gif");
        mFileTypes.put("49492A", "tif");
        mFileTypes.put("424D36", "bmp");
        //
        mFileTypes.put("414331", "dwg"); // CAD
        mFileTypes.put("384250", "psd");
        mFileTypes.put("7B5C72", "rtf"); // 日记本
        mFileTypes.put("3C3F78", "xml");
        mFileTypes.put("68746D", "html");
        mFileTypes.put("44656C", "eml"); // 邮件
        mFileTypes.put("D0CF11", "doc");
        mFileTypes.put("537461", "mdb");
        mFileTypes.put("252150", "ps");
        mFileTypes.put("255044", "pdf");
        mFileTypes.put("504B03", "zip");
        mFileTypes.put("526172", "rar");
        mFileTypes.put("574156", "wav");
        mFileTypes.put("415649", "avi");
        mFileTypes.put("2E524D", "rm");
        mFileTypes.put("000001", "mpg");
        mFileTypes.put("000001", "mpg");
        mFileTypes.put("6D6F6F", "mov");
        mFileTypes.put("3026B2", "asf");
        mFileTypes.put("4D5468", "mid");
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } catch (Exception e) {
            // Log.e("test","copyFile_e.getMessage():"+e.getMessage());
        } finally {
            try {
                // 关闭流
                if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
            } catch (Exception e2) {
                // Log.e("test","copyFile_e2.getMessage():"+e2.getMessage());
            }
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
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
     * @param args
     */
    public static void main(String[] args)  {
        String[] demandFileTypes = { "jpg", "png","zip" };
        File file = new File("D:\\win8_64_MyDrivers.zip");
        boolean isPicType = isFitFileType(demandFileTypes,file);
        //System.out.println(isPicType);
    }
    /**是否符合要求的文件类型*/
    public static boolean isFitFileType(String[] demandFileTypes, File file) {
        boolean isPicType = false;
        String fileType = getFileType(file);
        for (String targetFileType : demandFileTypes) {
            if (targetFileType.equalsIgnoreCase(fileType)) {
                isPicType = isPicType ||true;
            }else {
                isPicType = isPicType ||false;
            }
        }
        return isPicType;
    }

    private static String getFileType(File file) {
        String fileType = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            byte[] b = new byte[20];
            is.read(b, 0, b.length);
            String key = bytesToHexString(b).substring(0, 6).toUpperCase();
            fileType = mFileTypes.get(key);
        } catch (Exception e) {
            //System.out.println("getFileType_e.getMessage():"+e.getMessage());
        }finally{
            try {
                if (is!=null) {
                    is.close();
                }
            } catch (Exception e2) {
                //System.out.println("getFileType_e2.getMessage():"+e2.getMessage());
            }
        }
        return fileType;
    }
}
