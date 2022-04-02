package com.jks.Spo2MonitorEx.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.json.FamilyMemBean;
//import com.crashlytics.android.Crashlytics;
//
//import io.fabric.sdk.android.Fabric;

/**
 * Created by apple on 16/8/30.
 */
public class LoadPage extends MyActivity{
    //private ImageView loadImage;

    private String userEmail;
    private String clientKey;
    private int memberId;
    private FamilyIfc familyIfc;
    private Class classActivity = null;
    private Intent intent;
    private Family family;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actvity_loadpage);

        init();
        //initView();
        //starAnimationIcon();
        //jumpActivity();
    }

    @Override
    protected void init() {
        super.init();



        intent = new Intent();

        //第一次打开应用, 设置单位的默认值
        if (SharedPreferencesUtil.getFirstOpen(context)) {
            addOrUpdateFamilytest();
            if (getResources().getString(R.string.language).equals("0")) {
                SharedPreferencesUtil.setHeightUnit(context, 1);
                SharedPreferencesUtil.setWeightUnit(context, 1);
                SharedPreferencesUtil.setUTpt(context, "℃");
                SharedPreferencesUtil.setUserName(context,"历史数据");
                SharedPreferencesUtil.setFamilyPosition(context,0);
                SharedPreferencesUtil.setMemberId(context,0);
                       SharedPreferencesUtil.setUserName(context,"xxx@qq.com");
        SharedPreferencesUtil.setCilenKey(context,"A888888");
            } else {
                SharedPreferencesUtil.setWeightUnit(context, 2);
                SharedPreferencesUtil.setHeightUnit(context, 2);
                SharedPreferencesUtil.setUTpt(context, "°F");
                SharedPreferencesUtil.setUserName(context,"Record");
                SharedPreferencesUtil.setFamilyPosition(context,0);
                SharedPreferencesUtil.setMemberId(context,0);
                        SharedPreferencesUtil.setUserName(context,"xxx@qq.com");
        SharedPreferencesUtil.setCilenKey(context,"A888888");
            }
            SharedPreferencesUtil.setFirstOpen(context, false);
            Intent intent = new Intent(LoadPage.this, GuidePage.class);
            //intent.putExtra("from", 1);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LoadPage.this, MainActivity.class);
            intent.putExtra("from", 1);
            startActivity(intent);
            finish();
        }

        //获取登录过用户的信息
        //SharedPreferences sp = SharedPreferencesUtil.getProjectSP(context);
//        memberId = sp.getInt("memberId", -1);//获取成员的ID
//        userEmail = sp.getString("userName", null);//获取用户邮箱
//        clientKey = sp.getString("clientKey", null);//获取密码
//        SharedPreferencesUtil.setMemberId(context,0);
//        SharedPreferencesUtil.setUserName(context,"xxx@qq.com");
//        SharedPreferencesUtil.setCilenKey(context,"A888888");
        SharedPreferences sp = SharedPreferencesUtil.getProjectSP(context);
        memberId = sp.getInt("memberId", -1);//获取成员的ID
        userEmail = sp.getString("userName", null);//获取用户邮箱
        clientKey = sp.getString("clientKey", null);//获取密码
        Log.e("登录的用户", "memberId: " + memberId + ", userEmail: " + userEmail + ", clientKey: " + clientKey);
    }

    private void addOrUpdateFamilytest() {
        family = new Family();
        family.setBirthday("1987-01-01");
        family.setName("历史数据");
        family.setGender(0);
        family.setHeight(180);
        family.setWeight(180);
        family.setAvatar(null);
        FamilyMemBean familyData = new FamilyMemBean();
        familyData.setKey("AddFamMem");
        familyData.setMemInfo(family);
        familyData.setClientKey(SharedPreferencesUtil.getCilenKey(context));
        familyData.setAccountId(SharedPreferencesUtil.getMemberId(context));
        family.setAvatar(familyData.getAvatarDir());
        family.setFamilyId(familyData.getMemberId()+1);
        family.setMemberId(config.getMemberId());
        //将成员存进数据库中
        FamilyIfc ifc = new FamilyIfcImpl(context);
        ifc.insert(family);
        config.changeFamily();

    }





    @Override
    protected void initView() {
        super.initView();
        //loadImage = findViewById (R.id.loadpage_image1);
    }

    private void jumpActivity() {
//        if (userEmail != null && clientKey != null && memberId != -1) {
//            config.setMemberId(memberId);
//            config.setClientKey(clientKey);
//            config.setUserName(userEmail);
            classActivity = MainActivity.class;
            intent.putExtra("from", 1);
//        } else {
//            classActivity = UserLoginActivity.class;
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.setClass(LoadPage.this, classActivity);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

/*    private void starAnimationIcon() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.1f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.1f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.1f, 1f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(loadImage, alpha, scaleX, scaleY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(2000);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                jumpActivity();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }*/

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft);
    }
}
