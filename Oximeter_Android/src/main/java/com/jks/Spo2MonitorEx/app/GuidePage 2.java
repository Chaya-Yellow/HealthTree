package com.jks.Spo2MonitorEx.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.json.FamilyMemBean;

import java.util.ArrayList;


/**
 * 多图引导页
 */
public class GuidePage extends MyActivity {

    /**
     * 存放图片资源ID
     */
    ArrayList<Integer> imgs;
    /**
     * 引导页轮播组件
     */
    ConvenientBanner cb_guide;
    /**
     * 进入到App按钮
     */
    //Button btn_guide;
    private Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        init();



        // 添加图片
        imgs = new ArrayList<>();
        imgs.add(R.drawable.welcome1);
        imgs.add(R.drawable.huanying);

        // 初始化页面控件
        cb_guide = findViewById(R.id.cb_guide);
        //btn_guide = findViewById(R.id.btn_guide);

        cb_guide.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new LocalImageHolderView(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_guide_page;
            }
        }, imgs)
                .setPageIndicator(new int[] {R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPointViewVisible(false)
                .setCanLoop(false)
                .setOnPageChangeListener(new OnPageChangeListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    }

                    @Override
                    public void onPageSelected(int index) {
                        if (index == 1) {
                            //btn_guide.setVisibility(View.VISIBLE);
                            //cb_guide.setPointViewVisible(false);
                        } else {
                            //btn_guide.setVisibility(View.GONE);
                            //cb_guide.setPointViewVisible(true);
                        }
                    }
                });
        cb_guide.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 1) {
                    Intent intent = new Intent(GuidePage.this, MainActivity.class);
                    intent.putExtra("from", 1);
                    startActivity(intent);
                    finish();
                }
            }
        });

//        btn_guide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GuidePage.this, MainActivity.class);
//                intent.putExtra("from", 1);
//                startActivity(intent);
//                finish();
//            }
//        });

    }

    @Override
    protected void init() {
        super.init();
        //原有轻存储逻辑，第一次打开应用, 设置单位的默认值
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
        } else {
            Intent intent = new Intent(GuidePage.this, MainActivity.class);
            intent.putExtra("from", 1);
            startActivity(intent);
            finish();

        }
    }

    /**
     * 轮播对应holder
     */
    class LocalImageHolderView extends Holder<Integer> {
        private ImageView iv_guide_page;

        public LocalImageHolderView(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            iv_guide_page = itemView.findViewById(R.id.iv_guide_page);
            iv_guide_page.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override
        public void updateUI(Integer data) {
            iv_guide_page.setImageResource(data);
        }
    }

    /**
     * 原有入库方法
     */
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
}
