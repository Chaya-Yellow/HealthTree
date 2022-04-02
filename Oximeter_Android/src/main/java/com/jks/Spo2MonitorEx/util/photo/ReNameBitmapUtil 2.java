package com.jks.Spo2MonitorEx.util.photo;

import java.io.File;
import java.io.IOException;

/**
 * Created by apple on 16/9/1.
 */
public class ReNameBitmapUtil {
    /**
     * 修改文件名失败
     * @param dir 路径
     * @param oldName 要修改的文件名字
     * @param newName 要改成的文件名字
     * @throws IOException
     */
    public static void reNameFile(File dir, String oldName, String newName) throws IOException {
        File oldFile = new File(dir + File.separator + oldName);
        if (!oldFile.exists()) {
            oldFile.createNewFile();
        }
        File newFile = new File(dir + File.separator + newName);
        if (oldFile.renameTo(newFile)) {
            //修改文件名成功
        } else {
        }
    }
}
