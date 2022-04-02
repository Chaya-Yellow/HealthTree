package com.jks.Spo2MonitorEx.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.photo.BitmapUtil;
import com.jks.Spo2MonitorEx.util.photo.PicUtils;
import com.jks.Spo2MonitorEx.util.photo.PixelConvertUtil;

import java.io.File;
import java.util.List;

/**
 * Created by apple on 16/8/30.
 */
public class LoadingAva {
    public static boolean isChange = false;//是否更新图片 true: 重新加载本地文件缓存的图 false: 不进行加载本地文件中的图片
    public static BitmapDrawable[] familyAvas = null;

    /**
     * 获取家庭成员的头像
     * @param context
     * @param familys
     * @return
     */
    public static BitmapDrawable[] getFamilyAva(Context context, List<Family> familys) {
        if (familyAvas != null && !isChange)
            return familyAvas;
        familyAvas = new BitmapDrawable[familys.size()];
        for (int i = 0; i < familys.size(); i++) {
            Bitmap bitmap = loadingAva(context, familys.get(i).getAvatar());
            if (bitmap != null) {
                familyAvas[i] = new BitmapDrawable(bitmap);
            }
        }
        isChange = false;
        return familyAvas;
    }

    public static Bitmap loadingAva(Context context, String url) {
        Bitmap bitmap = null;
        if(url==null){
            return bitmap;
        }
        if(url.length()<=0){
            return bitmap;
        }
//        String[] urls = url.split("/");
//        url=urls[urls.length - 1];
        File file = new File(BitmapUtil.PHOTO_DIR, url + ".jpg");
        boolean exists = file.exists();
        if (exists) {
            int zoomSacle = PicUtils.getZoomSacleByHeightAndWidth(file.getPath(), PixelConvertUtil.dip2px(context, 73),
                    PixelConvertUtil.dip2px(context, 73));
            try {
                bitmap = PicUtils.getBitmapByZoomSacle(file.getPath(), zoomSacle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void clearAvas(){
        if(familyAvas != null){
            for(int i=0;i<familyAvas.length;i++){
                if(familyAvas[i] != null){
                    Bitmap bitmap = familyAvas[i].getBitmap();
                    if(bitmap != null){
                        bitmap.recycle();
                    }
                }
            }
            familyAvas = null;
        }
    }
}
