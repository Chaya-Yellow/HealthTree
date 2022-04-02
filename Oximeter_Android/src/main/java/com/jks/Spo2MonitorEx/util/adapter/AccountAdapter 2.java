package com.jks.Spo2MonitorEx.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.constant.Constant;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.entity.LoginInfo;
import com.jks.Spo2MonitorEx.util.photo.BitmapUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

/**
 * 家庭人员列表适配器
 * Created by apple on 16/8/30.
 */
public class AccountAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<LoginInfo> list;
    private Context context;
    private boolean isShowAddUser;
    private Activity activity;
    private boolean isShowSelectOrRead = false;

    public AccountAdapter(Activity activity, List<LoginInfo> list, boolean isShowAddUser,
                          boolean isShowSelectOrRead) {
        this.list = list;
        this.activity = activity;
        this.isShowAddUser = isShowAddUser;
        this.isShowSelectOrRead = isShowSelectOrRead;
        this.context = activity.getApplicationContext();
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < list.size()) {
            Hodler hodler = null;
            if (convertView == null || (Hodler) (convertView.getTag()) == null) {
                hodler = new Hodler();
                convertView = mInflater.inflate(R.layout.item_more_account_management, null);
                hodler.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);//头像
                hodler.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
                hodler.username = (TextView) convertView.findViewById(R.id.username);
                hodler.loginedAccount = (TextView) convertView.findViewById(R.id.login_item_text);
                hodler.firsthorizonline1 = (ImageView) convertView.findViewById(R.id.first_item_horizon_line1);
                hodler.firsthorizonline2 = (ImageView) convertView.findViewById(R.id.first_item_horizon_line2);
                hodler.lasthorizonline = (ImageView) convertView.findViewById(R.id.last_item_horizon_line);
                convertView.setTag(hodler);
            } else {
                hodler = (Hodler) convertView.getTag();
            }
            LoginInfo loginInfo = list.get(position);

            if (position == 0) {
                hodler.firsthorizonline1.setVisibility(View.VISIBLE);
                hodler.firsthorizonline2.setVisibility(View.GONE);
            }else {
                hodler.firsthorizonline1.setVisibility(View.GONE);
                hodler.firsthorizonline2.setVisibility(View.VISIBLE);
            }

            if (position == (list.size() - 1)) {
                hodler.lasthorizonline.setVisibility(View.VISIBLE);
            }else {
                hodler.lasthorizonline.setVisibility(View.GONE);
            }

            File mAvatarPhotoFile = new File(BitmapUtil.PHOTO_DIR, loginInfo.getAvatar() + ".jpg");
            Picasso picasso = Picasso.with(parent.getContext());
            RequestCreator creator;
            if (mAvatarPhotoFile.exists()) {
                //如果本地图片存在, 则加载本地图片
                creator = picasso.load(mAvatarPhotoFile);
            }else {
                //如果本地图片不存在, 则加载网络图片
                creator = picasso.load(Constant.BLT_GET_IMGURL + loginInfo.getAvatar());
            }
            creator.resize(60, 60).centerCrop()
                    .placeholder(R.drawable.avatar_default)
                    .error(R.drawable.avatar_default)
                    .transform(new CircleTransform())
                    .into(hodler.imageView1);


            hodler.username.setText(loginInfo.getAccount());
            if (isShowSelectOrRead) {
                hodler.imageView2.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.first_vf_right_enter));
            } else {
                if (SharedPreferencesUtil.getAccountPosition(context) == position) {
                    hodler.imageView2.setBackgroundDrawable(context.getResources().getDrawable(
                            R.drawable.alarm_icon_listitem_choose_normal));
                    hodler.loginedAccount.setVisibility(View.VISIBLE);
                } else {
                    hodler.imageView2.setBackgroundDrawable(null);
                    hodler.loginedAccount.setVisibility(View.GONE);
                }
            }

        } else {
            convertView = mInflater.inflate(R.layout.pull_to_refresh_foot, null);
            convertView.setMinimumHeight(getPx(getDip() - 75 - 50 - list.size() * 80));
        }
        return convertView;
    }

    private int getDip() {

        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (screenHeight / scale + 0.5f);
    }

    private int getPx(int px) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5);
    }

    private int getHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    class Hodler {
        ImageView imageView1;
        ImageView imageView2;
        TextView username;
        TextView loginedAccount;
        ImageView firsthorizonline1;
        ImageView firsthorizonline2;
        ImageView lasthorizonline;
    }

    class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig() != null
                    ? source.getConfig() : Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}