package com.jks.Spo2MonitorEx.util.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.VersionManager;
import com.jks.Spo2MonitorEx.util.constant.Constant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by apple on 16/8/30.
 */
public class BitmapUtil {
    /* 拍照的照片存储位置 */
    public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/"
            + Constant.AVATAR_PATH);
    public static final int PIC_FROM_CAMERA = 0;
    public static final int PIC_FROM_PHOTOALBUM = 1;
    public static final int PIC_FROM_CUTPHOTO = 2;
    private static final String TAG = "BitmapUtil";

    /**
     * 转换圆角方形图片
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = null;
        if (bitmap == null) {
            return output;
        }

        if (bitmap.getWidth() >= bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(),
                    android.graphics.Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(),
                    android.graphics.Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /** Bitmap转化为InputStream */
    public static InputStream Bitmap2ISJPEG(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    public static InputStream compressInSampleSize(InputStream in, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return Bitmap2ISJPEG(BitmapFactory.decodeStream(in, null, options));
    }

    /**
     * 图片灰化处理
     *
     * @param mBitmap
     * @return
     */
    public static Bitmap getGrayBitmap(Bitmap mBitmap) {
        Bitmap mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
                android.graphics.Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mGrayBitmap);
        Paint mPaint = new Paint();
        // 创建颜色变换矩阵
        ColorMatrix mColorMatrix = new ColorMatrix();
        // 设置灰度影响范围
        mColorMatrix.setSaturation(0);
        // 创建颜色过滤矩阵
        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
        // 设置画笔的颜色过滤矩阵
        mPaint.setColorFilter(mColorFilter);
        // 使用处理后的画笔绘制图像
        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
        return mGrayBitmap;

    }

    /**
     * 将Drawable转化为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 图片变色处理
     *
     * @param mBitmap
     * @return
     */
    public static Bitmap getDiscolorBitmap(Bitmap mBitmap) {
        Bitmap mGrayBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(),
                android.graphics.Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mGrayBitmap);
        Paint mPaint = new Paint();
        // 创建颜色变换矩阵
        ColorMatrix mColorMatrix = new ColorMatrix();
        // 设置灰度影响范围
        mColorMatrix.setSaturation(0);
        // 创建颜色过滤矩阵
        ColorMatrixColorFilter mColorFilter = new ColorMatrixColorFilter(mColorMatrix);
        // 设置画笔的颜色过滤矩阵
        mPaint.setColorFilter(mColorFilter);
        // 使用处理后的画笔绘制图像
        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
        return mGrayBitmap;

    }

    /**
     * 打开相机
     *
     * @param fileName
     */
    public static void showPicFromCamera(Activity activity, String fileName) {

        // System.out.println("showPicFromCamera");
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
            System.out.println("PHOTO_DIR:" + PHOTO_DIR);
            if (!PHOTO_DIR.exists()) {
                boolean iscreat = PHOTO_DIR.mkdirs();// 创建照片的存储目录
                // Log.v("test","_iscreat:"+iscreat);
            }

            File mCurrentPhotoFile = new File(PHOTO_DIR, fileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            activity.startActivityForResult(intent, PIC_FROM_CAMERA);
        } else {
            System.out.println("没有sd卡");
            Toast.makeText(activity, R.string.more_user_management_add_user_sd, Toast.LENGTH_LONG).show();
        }
    }

    /** 显示相册 */
    public static void showPicFromPhotoAlbum(Activity activity) {

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
            if (!PHOTO_DIR.exists()) {
                boolean iscreat = PHOTO_DIR.mkdirs();// 创建照片的存储目录
                Log.v("test", "_iscreat:" + iscreat);
            }

            Intent intent = null;
            if (VersionManager.getAndroidSDKVersion() < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            } else {
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            intent.setType("image/*");
            activity.startActivityForResult(intent, PIC_FROM_PHOTOALBUM);
        } else {
            Toast.makeText(activity, R.string.more_user_management_add_user_sd, Toast.LENGTH_LONG).show();
        }
    }

    /** 把选择好的图片或者拍照后放到相应的，并返回一个bitmap对象 */
    public static InputStream getPicBitmapCallBack(Activity activity, int requestCode, int resultCode,
                                                   Intent data) {
        InputStream picStream = null;
        if (resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
            ContentResolver cr = activity.getContentResolver();
            try {
                picStream = cr.openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return picStream;
    }

    /** 把选择好或者拍照后的图片返回该文件的Uri */
    public static Uri getPicUriCallBack(int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        if (resultCode == Activity.RESULT_OK && null != data) {
            uri = data.getData();
        } else {
            uri = null;
        }
        return uri;
    }

    /** 通过Uri来获取InputStream */
    public static InputStream getPicInputStream(Uri uri, Activity activity, int resultCode, Intent data) {
        InputStream picStream = null;
        if (resultCode == Activity.RESULT_OK && null != data) {
            uri = data.getData();
            ContentResolver cr = activity.getContentResolver();
            try {
                picStream = cr.openInputStream(uri);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return picStream;
    }

    // /**通过流来获取图片*/
    // public static Bitmap getBitmapFromPicStream(InputStream picStream,Bitmap
    // bitmap) {
    //
    //
    // if (picStream != null) {
    // BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
    // bitmapOptions.inJustDecodeBounds = true;
    // bitmap = BitmapFactory.decodeStream(picStream, null, bitmapOptions);;
    // Log.i("test",
    // "~~~~~~~~~~~~~org bitmap:"+bitmap.getWidth()+" "+bitmap.getHeight());
    // //
    // bitmapOptions.inSampleSize = computeSampleSize(bitmapOptions, -1,
    // 160*160);
    // Log.i("test", "~~~~~~~~~~~~~inSampleSize:"+bitmapOptions.inSampleSize);
    // bitmapOptions.inJustDecodeBounds = false;
    //
    // bitmap = BitmapFactory.decodeStream(picStream, null, bitmapOptions);
    // Log.i("test",
    // "~~~~~~~~~~~~~bitmap:"+bitmap.getWidth()+" "+bitmap.getHeight());
    //
    // return bitmap;
    // } else {
    // return bitmap;
    // }
    // }

    /**
     * 通过流来获取图片
     *
     * @param fileSize
     */
    public static Bitmap getBitmapFromPicStream(InputStream picStream, Bitmap bitmap, double fileSize) {
        if (picStream != null) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            if (fileSize >= 1024) {
                bitmapOptions.inSampleSize = 10;
            } else if (fileSize < 1024 && fileSize >= 512) {
                bitmapOptions.inSampleSize = 5;
            } else if (fileSize < 512 && fileSize >= 256) {
                bitmapOptions.inSampleSize = 3;
            } else if (fileSize < 256 && fileSize >= 128) {
                bitmapOptions.inSampleSize = 2;
            } else {
                bitmapOptions.inSampleSize = 1;
            }
            // Log.i("test",
            // "~~~~~~~~~~~~~inSampleSize:"+bitmapOptions.inSampleSize);

            bitmap = BitmapFactory.decodeStream(picStream, null, bitmapOptions);
            if (bitmap != null) {
                if (bitmap.getWidth() >= 160 && bitmap.getWidth() < bitmap.getHeight()) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getWidth());// 分割图片
                }
                if (bitmap.getHeight() >= 160 && bitmap.getWidth() > bitmap.getHeight()) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getHeight(), bitmap.getHeight());// 分割图片
                }
                // Log.i("test",
                // "~~~~~~~~~~~~~ bitmap:"+bitmap.getWidth()+" "+bitmap.getHeight());

                // bitmap = toRoundCorner(bitmap, 30);
                return bitmap;
            } else {
                return null;
            }
        } else {
            return bitmap;
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                                int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /** Bitmap转化为InputStream */
    public static InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    /** 针对webServer来上传的文件数据 */
    public static byte[] getPicUploadData(Bitmap bitmap) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        Bitmap.CompressFormat compressformat = Bitmap.CompressFormat.JPEG;
        bitmap.compress(compressformat, 100, bytearrayoutputstream);
        byte[] picData = bytearrayoutputstream.toByteArray();
        return picData;
    }

    /**
     * Bitmap转为文件保存
     *
     * @param bmp
     * @param filename
     * @return
     */
    // public static String saveBitmap2file(Bitmap bmp, String filename) {
    // String filePath = SettingUtil.PICTURE_SAVE_PATH+filename+".jpg";
    // CompressFormat format = Bitmap.CompressFormat.JPEG;
    // int quality = 100;
    // OutputStream stream = null;
    // try {
    //
    // stream = new FileOutputStream(filePath);
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // }
    // bmp.compress(format, quality, stream);
    // return filePath ;
    // }
    /** 通过所选中的文件URI，来得到该文件的绝对路径的文件 */
    public static File getUriFile(Uri uri, Activity activity) {
        String[] proj = { MediaStore.Images.Media.DATA };

        // Cursor actualimagecursor = activity.managedQuery(uri, proj, null,
        // null, null);
        //
        // int actual_image_column_index =
        // actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //
        // actualimagecursor.moveToFirst();
        //
        // String img_path =
        // actualimagecursor.getString(actual_image_column_index);

        String fileName = null;

        if (uri != null) {
            if (uri.getScheme().toString().compareTo("content") == 0) // content://开头的uri
            {
                Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileName = cursor.getString(column_index); // 取出文件路径
                    if (!fileName.startsWith("/mnt")) {
                        // 检查是否有”/mnt“前缀

                        fileName = "/mnt" + fileName;
                    }
                    cursor.close();
                }
            } else if (uri.getScheme().compareTo("file") == 0) // file:///开头的uri
            {
                fileName = uri.toString();
                fileName = uri.toString().replace("file://", "");
                // 替换file://
                if (!fileName.startsWith("/mnt")) {
                    // 加上"/mnt"头
                    fileName += "/mnt";
                }
            }
        }

        File file = new File(fileName);
        return file;
    }

    /**
     * 获取访问SD卡中图片路径
     *
     * @return
     */
    public static String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        String path = null;
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } else {
            path = getPath(uri);
        }

        return path;
    }

    /**
     * 获取访问SD卡中图片路径
     *
     * @return
     */
    public static String getPath(Uri uri) {
        return uri.getPath();
    }

    /** 根据文件名BitmapDrawable */
    public static BitmapDrawable getBitmapDrawableByFileName(String fileName) {
        File file = new File(PHOTO_DIR, fileName);
        InputStream picStream = null;
        try {
            picStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        double fileSize = (double) (file.length() * 100 / 1024) / 100;
        Bitmap bitmap = null;
        bitmap = BitmapUtil.getBitmapFromPicStream(picStream, bitmap, fileSize);
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);

        } else {
            return null;
        }
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800 / 3;// 这里设置高度为800f
        float ww = 480 / 3;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800 / 3;// 这里设置高度为800f
        float ww = 480 / 3;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

}
