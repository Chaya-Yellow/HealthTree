package com.jks.Spo2MonitorEx.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.LoadingAva;
import com.jks.Spo2MonitorEx.util.data.ChangeUnit;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.data.UnitUtils;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.other.GetAgeByBirthday;
import com.jks.Spo2MonitorEx.util.photo.PicUtils;

import java.util.List;

/**
 * 家庭人员列表适配器
 * Created by apple on 16/8/30.
 */
public class FamilyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Family> list;
    private Context context;
    private String genderMale, genderFemale;
    private String ageStr;
    private boolean isShowAddUser;
    private Activity activity;
    private String heightStr;
    private BitmapDrawable[] bitmaps;
    private boolean isShowSelectOrRead = false;

    public FamilyAdapter(Activity activity, List<Family> list, boolean isShowAddUser,
                         boolean isShowSelectOrRead) {
        this.list = list;
        this.activity = activity;
        this.isShowAddUser = isShowAddUser;
        this.isShowSelectOrRead = isShowSelectOrRead;
        this.context = activity.getApplicationContext();
        this.mInflater = LayoutInflater.from(context);
        ageStr = context.getResources().getString(R.string.first_list_agestr);// 岁
        // 性别
        genderFemale = context.getResources().getString(R.string.more_user_management_add_user_gender_female);
        genderMale = context.getResources().getString(R.string.more_user_management_add_user_gender_male);
        bitmaps = LoadingAva.getFamilyAva(context, list);
    }

    public int getCount() {

        if (!isShowAddUser) {
            if (list == null) {
                return 0;
            } else if ((getDip() - 75) < list.size() * 80) {
                return list.size();
            } else {
                return list.size() + 1;
            }
        } else {
            return list.size();
        }
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
                convertView = mInflater.inflate(R.layout.item_more_user_management, null);
                hodler.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);//头像
                hodler.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
                hodler.username = (TextView) convertView.findViewById(R.id.username);
                hodler.tvgender = (TextView) convertView.findViewById(R.id.gender);
                hodler.tvage = (TextView) convertView.findViewById(R.id.age);
                hodler.height = (TextView) convertView.findViewById(R.id.height);
                hodler.lasthorizonline = (ImageView) convertView.findViewById(R.id.last_item_horizon_line);
                convertView.setTag(hodler);
            } else {
                hodler = (Hodler) convertView.getTag();
            }
            Family family = list.get(position);

            if (position == (list.size() - 1)) {
                hodler.lasthorizonline.setVisibility(View.VISIBLE);
            }else {
                hodler.lasthorizonline.setVisibility(View.GONE);
            }

            // 格式化性别
            String gender = null;
            if (family.getGender() == 0) {
                gender = genderMale;
            } else {
                gender = genderFemale;
            }
            // 格式化年龄
            int age = GetAgeByBirthday.getAgeByBirthday(family.getBirthday());

            if (bitmaps[position] != null) {
                Bitmap bitmap = PicUtils.getRoundRectBitmap(bitmaps[position].getBitmap(), bitmaps[position].getBitmap().getWidth());
                hodler.imageView1.setBackgroundDrawable(new BitmapDrawable(bitmap));
            } else {
                hodler.imageView1.setBackgroundResource(R.drawable.avatar_default);
            }
            if (age >= 0) {

                hodler.tvage.setText(age + ageStr);
            } else {
                hodler.tvage.setVisibility(View.GONE);
            }

            hodler.username.setText(family.getName());
            hodler.tvgender.setText(gender);
            if (isShowSelectOrRead) {
                hodler.imageView2.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.first_vf_right_enter));
            } else {
                if (SharedPreferencesUtil.getFamilyPosition(context) == position) {
                    hodler.imageView2.setBackgroundDrawable(context.getResources().getDrawable(
                            R.drawable.alarm_icon_listitem_choose_normal));
                } else {
                    hodler.imageView2.setBackgroundDrawable(null);
                }
            }

            if (family.getHeight() >= 0) {
                if (SharedPreferencesUtil.getUserSetting(activity, SharedPreferencesUtil.SETTING_KEY[1]) == 0) {
                    if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
                        heightStr = context.getResources().getString(R.string.more_setting_height_cm);
                        hodler.height.setText((int) family.getHeight() + heightStr);
                    } else {
                        heightStr = context.getResources().getString(R.string.more_unit_height_in);
                        Log.v("test", "显示的时候：" + family.getHeight());
                        hodler.height.setText(UnitUtils.FloatFormat1(UnitUtils.Cm2In(family.getHeight()))
                                + heightStr);
                    }
                } else {
                    heightStr = context.getResources().getString(R.string.more_setting_height_inch);
                    hodler.height.setText(ChangeUnit.CMToInches(family.getHeight()) + heightStr);
                }
            } else {
                hodler.height.setVisibility(View.GONE);
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
        TextView tvgender;
        TextView tvage;
        TextView height;
        ImageView lasthorizonline;
    }
}