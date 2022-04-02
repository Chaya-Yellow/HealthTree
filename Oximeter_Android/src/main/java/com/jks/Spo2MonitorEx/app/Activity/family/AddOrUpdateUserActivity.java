package com.jks.Spo2MonitorEx.app.Activity.family;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.app.Activity.family.pop.WheelPop;
import com.jks.Spo2MonitorEx.util.LoadingAva;
import com.jks.Spo2MonitorEx.util.TitlebarUtil;
import com.jks.Spo2MonitorEx.util.activity.MyActivity;
import com.jks.Spo2MonitorEx.util.config.Config;
import com.jks.Spo2MonitorEx.util.dao.FamilyIfc;
import com.jks.Spo2MonitorEx.util.dao.impl.FamilyIfcImpl;
import com.jks.Spo2MonitorEx.util.data.DataCheckUtil;
import com.jks.Spo2MonitorEx.util.data.SharedPreferencesUtil;
import com.jks.Spo2MonitorEx.util.data.StringUtil;
import com.jks.Spo2MonitorEx.util.data.UnitUtils;
import com.jks.Spo2MonitorEx.util.dialog.ActionSheetDialog;
import com.jks.Spo2MonitorEx.util.dialog.CustomDialog;
import com.jks.Spo2MonitorEx.util.dialog.DialogUtil;
import com.jks.Spo2MonitorEx.util.entity.Family;
import com.jks.Spo2MonitorEx.util.entity.json.FamilyMemBean;
import com.jks.Spo2MonitorEx.util.json.JsonUtils;
import com.jks.Spo2MonitorEx.util.json.ReErrorCode;
import com.jks.Spo2MonitorEx.util.other.BLTToast;
import com.jks.Spo2MonitorEx.util.photo.BitmapUtil;
import com.jks.Spo2MonitorEx.util.photo.FileUtils;
import com.jks.Spo2MonitorEx.util.photo.PhotoSetting;
import com.jks.Spo2MonitorEx.util.photo.PicUtils;
import com.jks.Spo2MonitorEx.util.photo.PixelConvertUtil;
import com.jks.Spo2MonitorEx.util.photo.ReNameBitmapUtil;
import com.jks.Spo2MonitorEx.util.thread.MyThread;
import com.jks.Spo2MonitorEx.util.web.CheckNetwork;
import com.jks.Spo2MonitorEx.util.web.StreamUtil;
import com.lidroid.xutils.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by apple on 16/8/31.
 */
public class AddOrUpdateUserActivity extends MyActivity implements OnClickListener {

    private EditText etUserName;
    private View[] v = new View[5];
    //单位显示的view
    private TextView tvWeightUnit, tvHeightUnit;
    //用户头像
    private ImageView ivHeadImage;
    //用户头像栏
    private LinearLayout tvImageChange;
    //各个信息文本的数组0:用户名 1:性别 2:生日 3:身高 4:体重
    private TextView[] textView = new TextView[5];
    private Button sureBtn;
//    private MyWheel4 myWheel;
    private WheelPop myWheelPop;

    //拍照的对话框
    private Dialog pic_style_Dialog;

    private Config config;
    //输入框控制
    private InputMethodManager imm;
    //性别数组(用于滚轮)
    private List<String> gender = new ArrayList<String>();
    //身高数组(用于滚轮)
    private List<String> height = new ArrayList<String>();
    //体重数组(用于滚轮)
    private List<String> weight = new ArrayList<String>();

    private static boolean isAddUser = true;
    private static boolean isChangeAvatar = false;//是否修改了头像

    private static final int RES_SUCCESS = 0;
    //被选中的view的位置
    private int prePosition = -1;
    private String oldAvatar = null;//原来的头像

    //性别的字符串
    private String genderMale, genderFemale;
    //照片的文件名
    private String fileName = "";
    //头像数据
    private Bitmap bitmap = null;
    private Family family;
    private int[] textViewId = {
            R.id.et_more_user_management_add_user_username,
            R.id.tv_more_user_management_add_user_gender,
            R.id.tv_more_user_management_add_user_birthday,
            R.id.tv_more_user_management_add_user_height,
            R.id.tv_more_user_management_add_user_weight
    };
    private int[] vId = {
            R.id.ll_more_user_management_add_user_username,
            R.id.ll_more_user_management_add_user_gender,
            R.id.ll_more_user_management_add_user_birthday,
            R.id.ll_more_user_management_add_user_height,
            R.id.ll_more_user_management_add_user_weight
    };
    private LinearLayout.LayoutParams params;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_family_new_user);
        config = (Config) getApplicationContext();
        init();
        initView();
        loadView();
        initTitleBar();
    }

    @Override
    protected void init() {
        super.init();
        genderFemale = getResources().getString(R.string.more_user_management_add_user_gender_female);
        genderMale = getResources().getString(R.string.more_user_management_add_user_gender_male);
        imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        pic_style_Dialog = getPicDialog();
        gender.add(getResources().getString(R.string.more_user_management_add_user_gender_male));
        gender.add(getResources().getString(R.string.more_user_management_add_user_gender_female));

        //初始化体重数组
        if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
            for (int i = 0; i <= 1800; i++) {
                float weightValue = (float) ((float) i / 10);
                weight.add(Float.toString(weightValue));
            }
        } else {
            for (int i = 0; i <= 397; i++) {
                weight.add(i + "");
            }
        }
        //初始化身高数组
        if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
            for (int i = 30; i <= 230; i++) {
                height.add(String.valueOf(i));
            }
        } else {
            for (int i = 120; i <= 900; i++) {
                height.add(UnitUtils.FloatFormat1(i / 10.0F) + "");
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        tvWeightUnit = (TextView) findViewById(R.id.tv_more_user_management_add_user_weight_unit);
        tvHeightUnit = (TextView) findViewById(R.id.tv_more_user_management_add_user_height_unit);
        ivHeadImage = (ImageView) findViewById(R.id.more_iv_add_user_image);
        tvImageChange = (LinearLayout) findViewById(R.id.ll_more_user_management_add_user_portrait);
        etUserName = (EditText) findViewById(R.id.et_more_user_management_add_user_username);
        //params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        sureBtn = (Button) findViewById(R.id.btn_user_login_sure);

        tvImageChange.setOnClickListener(this);
        sureBtn.setOnClickListener(this);

        for (int i = 0; i < v.length; i++) {
            if (i != 0) {
                textView[i] = (TextView) findViewById(textViewId[i]);
            }
            v[i] = findViewById(vId[i]);
            v[i].setOnClickListener(this);
        }
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        if (isAddUser) {
            TitlebarUtil.showTitleName(this, R.string.add_user_new_family);
        }else {
            TitlebarUtil.showTitleName(this, R.string.update_user_new_family);
        }

        ImageButton ibLeft = TitlebarUtil.showIbLeft(this);
        ibLeft.setImageResource(R.drawable.titlebar_btn_back_sl);

        ibLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //应用返回
                finish();
            }
        });
    }

    @Override
    protected void loadView() {
        super.loadView();
        add();
    }

    /**
     * 拍照的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = BitmapUtil.getPicUriCallBack(requestCode, resultCode, data);
        switch (requestCode) {
            case BitmapUtil.PIC_FROM_CAMERA:
                switch (resultCode) {
                    case 0:
                        break;
                    case -1:
                        if (!DataCheckUtil.isNull(fileName)) {
                            File file = new File(BitmapUtil.PHOTO_DIR, fileName);
                            PicUtils.showPicToCutPhoto(Uri.fromFile(file), AddOrUpdateUserActivity.this);
                        }
                        break;
                }
                break;
            case BitmapUtil.PIC_FROM_PHOTOALBUM:
                if (uri != null) {
                    String selectedImagePath = BitmapUtil.getPath(uri, AddOrUpdateUserActivity.this);
                    File sourceFile = new File(selectedImagePath);
                    String[] demandFileTypes = { "jpg", "png", "gif", "tif", "bmp", "jpeg" };
                    boolean isFitFileType = FileUtils.isFitFileType(demandFileTypes, sourceFile);
                    if (isFitFileType) {
                        PicUtils.showPicToCutPhoto(Uri.fromFile(sourceFile), AddOrUpdateUserActivity.this);
                    } else {
                        DataCheckUtil.showToast(R.string.activity_first_family_no_show_drawable,
                                AddOrUpdateUserActivity.this);
                    }
                }
                break;
            case BitmapUtil.PIC_FROM_CUTPHOTO:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        int zoomSacle = PhotoSetting.getPicSaveSize(AddOrUpdateUserActivity.this);
                        File sourceFile = new File(BitmapUtil.PHOTO_DIR, fileName);
                        try {
                            // 如果图片大于512KB则进行放大10倍压缩
                            if (sourceFile.length() / 1024 > 512) {
                                zoomSacle = zoomSacle * 10;
                            }
                            PicUtils.showCutPhoto(data, zoomSacle, sourceFile.getPath());
                            //将压缩图片进行显示
                            if (isShowFilePic(sourceFile)) {

                            }

                        } catch (FileNotFoundException e) {
                            Toast.makeText(AddOrUpdateUserActivity.this, getString(R.string.shear_figure_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (prePosition != -1) {
            //取消被选中的状态
            v[prePosition].setSelected(false);
        }

        switch (view.getId()) {
            case R.id.ll_more_user_management_add_user_portrait:
                // 显示图片选择对话框
//                pic_style_Dialog.show();
                showTakePhotoActionSheet();
                break;
            case R.id.ll_more_user_management_add_user_username:
                // 用户名获取焦点
                etUserName.requestFocus();
                break;
            case R.id.ll_more_user_management_add_user_gender:
                // 选中性别
                selector(1);
                closeKeybord();
                myWheelPop = WheelPop.newInstance(AddOrUpdateUserActivity.this, config,
                        textView[1], getResources().getString(R.string.first_family_add_user_gender), gender, AddOrUpdateUserActivity.this.getWindow().getDecorView());
                myWheelPop.loadView();
                break;
            case R.id.ll_more_user_management_add_user_birthday:
                selector(2);
                Calendar c = Calendar.getInstance();
                //c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)//取得当前时间
                String[] times = textView[2].getText().toString().split("-");
                Dialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        textView[2].setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, Integer.parseInt(times[0]), //年
                    Integer.parseInt(times[1]) - 1, //月
                    Integer.parseInt(times[2]) // 日
                );
                dialog.show();
                break;

            case R.id.ll_more_user_management_add_user_height:
                selector(3);
                closeKeybord();
                myWheelPop = WheelPop.newInstance(AddOrUpdateUserActivity.this, config,
                        textView[3], getResources().getString(R.string.first_family_add_user_height), height, AddOrUpdateUserActivity.this.getWindow().getDecorView());
                myWheelPop.loadView();
                break;
            case R.id.ll_more_user_management_add_user_weight:
                selector(4);
                closeKeybord();
                myWheelPop = WheelPop.newInstance(AddOrUpdateUserActivity.this, config,
                        textView[4], getResources().getString(R.string.first_family_add_user_weight), weight, AddOrUpdateUserActivity.this.getWindow().getDecorView());
                myWheelPop.loadView();
                break;
            case R.id.btn_user_login_sure:
                closeKeybord();
                addOrUpdateFamily(isAddUser);
                break;
            default:
                break;
        }

    }

    /**
     * 对家庭人员进行添加或者更新
     */
    private void add() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -1);
        int add = intent.getIntExtra("add", 0);
        if (position == -1) {
            if (add == 1) {
                isAddUser = true;
                //添加新成员
                addNewUser();
            }
        }else {
            isAddUser = false;
            //更新人员
            updateUser(position);
        }

    }

    private void addNewUser() {
        family = new Family();
        sureBtn.setText(R.string.first_family_add_new_family);
        ivHeadImage.setBackgroundResource(R.drawable.avatar_default);
        etUserName.setText("");
        textView[1].setText(genderMale);
        textView[2].setText("1980-01-01");

        tvHeightUnit.setVisibility(View.VISIBLE);
        if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
            tvHeightUnit.setText(R.string.more_setting_height_cm);
        } else {
            tvHeightUnit.setText(R.string.more_unit_height_in);
        }
        if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
            textView[3].setText(168 + "");
        } else {
            textView[3].setText(UnitUtils.FloatFormat1(UnitUtils.Cm2In(168)) + "");
        }
//        textView[3].setText("168");

        tvWeightUnit.setVisibility(View.VISIBLE);
        if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
            tvWeightUnit.setText(R.string.more_unit_weight_kg);
        } else {
            tvWeightUnit.setText(R.string.more_unit_weight_lb);
        }
//        textView[4].setText("50.0");
        if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
            textView[4].setText(UnitUtils.FloatFormat1(50.0f) + "");
        } else {
            textView[4].setText(UnitUtils.Float2Int(UnitUtils.Kg2Bl(50.0f)) + "");
        }
    }

    /**
     * 添加或者更新家庭成员
     * @param isAddUser
     */
    private void addOrUpdateFamily(boolean isAddUser) {
        oldAvatar = family.getAvatar();//临时保存该成员的头像
        if (isNullByTv()) {
            family.setBirthday(textView[2].getText().toString());
            family.setName(etUserName.getText().toString());
            if (textView[1]
                    .getText()
                    .toString()
                    .equals(getResources().getString(
                            R.string.more_user_management_add_user_gender_male))) {
                // 男的性别为0
                family.setGender(0);
            } else {
                // 女的性别为1
                family.setGender(1);
            }
            try {
                if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
                    family.setHeight(Integer.valueOf(textView[3].getText().toString()));
                } else {
                    family.setHeight(UnitUtils.FloatFormat3(UnitUtils.In2Cm(Float.valueOf(textView[3].getText().toString()))));
                }
            } catch (Exception e) {

            }
            try {
                if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
                    family.setWeight(UnitUtils.FloatFormat1(Float.valueOf(textView[4].getText().toString())));
                } else {
                    family.setWeight(UnitUtils.FloatFormat3(UnitUtils.Bl2kg(Float.valueOf(textView[4]
                            .getText().toString()))));
                }
            } catch (Exception e) {

            }

            if (bitmap != null) {
                try {
                    FileInputStream in = new FileInputStream(BitmapUtil.PHOTO_DIR + "/" + fileName);
                    byte buffer[] = StreamUtil.read(in);
                    byte[] encod = Base64.encode(buffer, Base64.DEFAULT);
                    family.setAvatar(new String(encod));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("图片不存在", BitmapUtil.PHOTO_DIR + "/" + fileName);
                    family.setAvatar(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("base64", "图片转换出错");
                    family.setAvatar(null);
                }
            } else {
                family.setAvatar(null);
            }

            if (CheckNetwork.checkNetwork(AddOrUpdateUserActivity.this)) {//检查网络, 并提示打开
                if (CheckNetwork.checkNetwork3(context)) {
                    if (isAddUser) {
                        addFamily();
                    }else {
                        updateFamily();
                    }
                } else {
                    showNetwordToas();
                }
            }else {
                showNetwordToas();
            }
        }
    }

    /**
     * 更新用户信息界面初始化
     * @param position
     */
    private void updateUser(int position) {
        sureBtn.setText(R.string.first_family_update_old_family);

        family = config.getFamilys().get(position);
        fileName = family.getAvatar();
        BitmapDrawable[] bitmaps = LoadingAva.getFamilyAva(context, config.getFamilys());
        if (bitmaps[position] != null) {
            Bitmap bitmap = bitmaps[position].getBitmap();
            bitmap = PicUtils.getRoundRectBitmap(bitmap, bitmap.getWidth());
            ivHeadImage.setBackgroundDrawable(new BitmapDrawable(bitmap));
        } else {
            ivHeadImage.setBackgroundResource(R.drawable.avatar_default);
        }
        etUserName.setText(family.getName());
        String gender = null;
        if (family.getGender() == 0) {
            gender = genderMale;
        } else {
            gender = genderFemale;
        }
        textView[1].setText(gender);
        textView[2].setText(family.getBirthday());
        if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
            tvHeightUnit.setText(R.string.more_setting_height_cm);
        } else {
            tvHeightUnit.setText(R.string.more_unit_height_in);
        }
        if (family.getHeight() >= 0) {

            tvHeightUnit.setVisibility(View.VISIBLE);
            if (SharedPreferencesUtil.getHeightUnit(context) == 1) {
                if (family.getHeight() > 230) {
                    family.setHeight(230.0F);
                }
                if (family.getHeight() < 30.0F) {
                    family.setHeight(30.0F);
                }
                textView[3].setText((int) family.getHeight() + "");
            } else {
                if (UnitUtils.FloatFormat1(UnitUtils.Cm2In(family.getHeight())) > 90.0F) {
                    textView[3].setText("90.0");
                } else if (UnitUtils.FloatFormat1(UnitUtils.Cm2In(family.getHeight())) < 12.0F) {
                    textView[3].setText("12.0");
                } else {
                    textView[3].setText(UnitUtils.FloatFormat1(UnitUtils.Cm2In(family.getHeight())) + "");
                }
            }
        } else {
            textView[3].setText(getResources().getString(R.string.activity_first_family_tips));
        }
        if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
            tvWeightUnit.setText(R.string.more_unit_weight_kg);
        } else {
            tvWeightUnit.setText(R.string.more_unit_weight_lb);
        }
        if (family.getWeight() >= 0) {
            tvWeightUnit.setVisibility(View.VISIBLE);
            if (SharedPreferencesUtil.getWeightUnit(context) == 1) {
                if (UnitUtils.FloatFormat1(family.getWeight()) > 180.0F) {
                    family.setWeight(180.0F);
                }
                textView[4].setText(UnitUtils.FloatFormat1(family.getWeight()) + "");
            } else {
                if (UnitUtils.Float2Int(UnitUtils.Kg2Bl(family.getWeight())) > 397) {
                    family.setWeight(180.0F);
                }

                textView[4].setText(UnitUtils.Float2Int(UnitUtils.Kg2Bl(family.getWeight())) + "");
            }
        } else {
            textView[4].setText(getResources().getString(R.string.activity_first_family_tips));
        }
    }

//    /**
//     * 上传家庭人员到服务器
//     */
//    private void addFamily() {
//        DialogUtil.show(this, R.string.first_family_add_tip_submitting);
//        MyThread.startNewThread(new Runnable() {
//            @Override
//            public void run() {
//                RequestParams params = new RequestParams();
//                FamilyMemBean familyData = new FamilyMemBean();
//                familyData.setKey("AddFamMem");
//                familyData.setMemInfo(family);
//                familyData.setClientKey(SharedPreferencesUtil.getCilenKey(context));
//                familyData.setAccountId(SharedPreferencesUtil.getMemberId(context));
//                String familyDataJson = new JsonUtils<FamilyMemBean>().getJsonString(familyData);
//                Log.e("添加成员", familyDataJson);
//                try {
//                    params.setBodyEntity(new StringEntity(familyDataJson, HTTP.UTF_8));//解决中文传送到服务器时出现乱码
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                new HttpUtils().send(HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
//                        if (errorCode == 0) {
//                            Gson gson = new Gson();
//                            ResponseObject<FamilyMemBean> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<FamilyMemBean>>() {
//                            }.getType());
//                            renameAvatarDir(fileName, object.getData().getAvatarDir());
//
//                            family.setAvatar(object.getData().getAvatarDir());
//                            family.setFamilyId(object.getData().getMemberId());
//                            family.setMemberId(config.getMemberId());
//
//                            //将成员存进数据库中
//                            FamilyIfc ifc = new FamilyIfcImpl(AddOrUpdateUserActivity.this);
//                            ifc.insert(family);
//
//                            config.changeFamily();
//
//                        }
//                        sendMsg(errorCode);
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        DialogUtil.dismiss2Msg(R.string.first_family_add_tip_no_submited);
//                        showTip(getResources().getString(R.string.json_checknet_nettimeout));
//                    }
//                });
//            }
//        });
//    }
    /**
     * 上传家庭人员到服务器
     */
    public void addFamily() {
        //DialogUtil.show(this, R.string.first_family_add_tip_submitting);
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                FamilyMemBean familyData = new FamilyMemBean();
                familyData.setKey("AddFamMem");
                familyData.setMemInfo(family);

                familyData.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                familyData.setAccountId(SharedPreferencesUtil.getMemberId(context));

                String familyDataJson = new JsonUtils<FamilyMemBean>().getJsonString(familyData);
                Log.e("添加成员", familyDataJson);
                //renameAvatarDir(fileName, object.getData().getAvatarDir());
                //familyData.getAvatarDir()
                            family.setAvatar(familyData.getAvatarDir());
                            family.setFamilyId(familyData.getMemberId()+1);
                            family.setMemberId(config.getMemberId());
                            //将成员存进数据库中
                            FamilyIfc ifc = new FamilyIfcImpl(AddOrUpdateUserActivity.this);
                            ifc.insert(family);
                            config.changeFamily();

            }
        });
    }
//    /**
//     * 更新服务器中的家庭成员
//     */
//    private void updateFamily() {
//        DialogUtil.show(this, R.string.first_family_add_tip_submitting);
//        MyThread.startNewThread(new Runnable() {
//            @Override
//            public void run() {
//                RequestParams params = new RequestParams();
//                FamilyMemBean familyData = new FamilyMemBean();
//                familyData.setKey("UpdateMemInfo");
//                familyData.setMemInfo(family);
//                familyData.setMemberId(family.getFamilyId());
//                familyData.setClientKey(SharedPreferencesUtil.getCilenKey(context));
//                familyData.setAccountId(SharedPreferencesUtil.getMemberId(context));
//                String familyDataJson = new JsonUtils<FamilyMemBean>().getJsonString(familyData);
//                Log.e("修改成员", familyDataJson);
//                try {
//                    params.setBodyEntity(new StringEntity(familyDataJson, HTTP.UTF_8));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                new HttpUtils().send(HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
//                        if (errorCode == 0) {
//                            Gson gson = new Gson();
//                            ResponseObject<FamilyMemBean> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<FamilyMemBean>>() {
//                            }.getType());
//                            renameAvatarDir(fileName, object.getData().getAvatarDir());
//
//                            String avatar = object.getData().getAvatarDir();
//                            if (avatar != null && !avatar.equals("")) {
//                                //上传新的头像
//                                family.setAvatar(object.getData().getAvatarDir());
//                                PicUtils.deletePhotoAva(oldAvatar);//删除就的头像
//                                Log.e("旧图片", oldAvatar);
//                            }else {
//                                family.setAvatar(oldAvatar);
//                            }
//
//                            FamilyIfc ifc = new FamilyIfcImpl(AddOrUpdateUserActivity.this);
//                            ifc.updateByID(family);
//                            config.changeFamily();
//
//                        }
//                        sendMsg(errorCode);
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//                        DialogUtil.dismiss2Msg(R.string.first_family_add_tip_no_submited);
//                        showTip(getResources().getString(R.string.json_checknet_nettimeout));
//                    }
//                });
//            }
//        });
//    }
    /**
     * 更新服务器中的家庭成员
     */
    private void updateFamily() {
        //DialogUtil.show(this, R.string.first_family_add_tip_submitting);
        MyThread.startNewThread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                FamilyMemBean familyData = new FamilyMemBean();
                familyData.setKey("UpdateMemInfo");
                familyData.setMemInfo(family);
                familyData.setMemberId(family.getFamilyId());
                familyData.setClientKey(SharedPreferencesUtil.getCilenKey(context));
                familyData.setAccountId(SharedPreferencesUtil.getMemberId(context));
                String familyDataJson = new JsonUtils<FamilyMemBean>().getJsonString(familyData);
                Log.e("修改成员", familyDataJson);
//                try {
//                    params.setBodyEntity(new StringEntity(familyDataJson, HTTP.UTF_8));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                new HttpUtils().send(HttpMethod.POST, Constant.BLT_URL, params, new RequestCallBack<String>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        int errorCode = JsonUtils.getServerErrCode(responseInfo.result);
//                        if (errorCode == 0) {
//                            Gson gson = new Gson();
//                            ResponseObject<FamilyMemBean> object = gson.fromJson(responseInfo.result, new TypeToken<ResponseObject<FamilyMemBean>>() {
//                            }.getType());
                            //renameAvatarDir(fileName, familyData.getAvatarDir());

                            String avatar = familyData.getAvatarDir();
                            if (avatar != null && !avatar.equals("")) {
                                //上传新的头像
                                family.setAvatar(familyData.getAvatarDir());
                                PicUtils.deletePhotoAva(oldAvatar);//删除就的头像
                                Log.e("旧图片", oldAvatar);
                            }else {
                                family.setAvatar(oldAvatar);
                            }

                            FamilyIfc ifc = new FamilyIfcImpl(AddOrUpdateUserActivity.this);
                            ifc.updateByID(family);
                            config.changeFamily();



                }
            });

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //处理服务器处理返回信息
            switch (msg.what) {
                case RES_SUCCESS:
                    DialogUtil.setMsg(R.string.first_family_add_tip_submited);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.dismiss();
                            finish();
                        }
                    }, 1500);
                    break;
                default:
                    DialogUtil.dismiss2Msg(R.string.first_family_add_tip_no_submited);
                    showTip(ReErrorCode.getErrodType(context, msg.what));
                    break;
            }

        }
    };

    /**
     * 判断文本框是否为空, 如果为空则震动
     * @return
     */
    private boolean isNullByTv() {
        String userName = etUserName.getText().toString();
        if (userName == null || userName.length() == 0) {
            showTipId(R.string.first_family_add_tip_1);
            v[0].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return false;
        }else if (StringUtil.containsEmoji(userName)) {//名字不能包含表情
            showTipId(R.string.special_symbols);
            v[0].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return false;
        } else if (tvToString(textView[1]) == null || tvToString(textView[1]).length() == 0) {
            showTipId(R.string.first_family_add_tip_2);
            v[1].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return false;
        } else if (tvToString(textView[2]) == null || tvToString(textView[2]).length() == 0) {
            showTipId(R.string.first_family_add_tip_3);
            v[2].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return false;
        } else if (tvToString(textView[3]) == null || tvToString(textView[3]).length() == 0) {
            showTipId(R.string.first_family_add_tip_4);
            v[3].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return false;
        } else if (tvToString(textView[4]) == null || tvToString(textView[4]).length() == 0) {
            showTipId(R.string.first_family_add_tip_5);
            v[4].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return false;
        }

        if (userName != null || userName.length() != 0) {
            try {
                if (StringUtil.isContainChinese(etUserName.getText().toString())) {
                    //中文名字不能超过6个汉字
                    if (etUserName.getText().toString().getBytes("GBK").length > 12) {
                        showTipId(R.string.first_family_add_name_long_tip);
                        v[0].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                        return false;
                    }
                }else {
                    //英文名字不超过20个字母
                    if (etUserName.getText().toString().getBytes("GBK").length > 20) {
                        showTipId(R.string.first_family_add_name_long_tip_2);
                        v[0].startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                        return false;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 使position位置的view被选中
     * @param position
     */
    private void selector(int position) {
        v[position].setSelected(true);
        prePosition = position;
    }

    private Dialog getPicDialog() {
        CustomDialog.Builder pic_style_Builder = new CustomDialog.Builder(AddOrUpdateUserActivity.this);
        pic_style_Builder
                .setTitle(context.getResources().getString(R.string.first_family_add_user_getphotos));
        View v = LayoutInflater.from(AddOrUpdateUserActivity.this).inflate(R.layout.user_info_pic_add_style, null);
        Button takePhoto = (Button) v.findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fileName = getFileName() + ".jpg";
                BitmapUtil.showPicFromCamera(AddOrUpdateUserActivity.this, fileName);
                pic_style_Dialog.dismiss();
            }
        });

        Button album = (Button) v.findViewById(R.id.album);
        album.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fileName = getFileName() + ".jpg";
                BitmapUtil.showPicFromPhotoAlbum(AddOrUpdateUserActivity.this);
                pic_style_Dialog.dismiss();
            }
        });

        pic_style_Builder.setView(v);
        pic_style_Builder.isShowLine(false);
        return pic_style_Builder.create();
    }

    private void showTakePhotoActionSheet() {
        new ActionSheetDialog(AddOrUpdateUserActivity.this)
                .builder()
                .setTitle(context.getResources().getString(R.string.first_family_add_user_getphotos))
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(context.getResources().getString(R.string.family_sex_photo_add_user_photograph), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                fileName = getFileName() + ".jpg";
                                BitmapUtil.showPicFromCamera(AddOrUpdateUserActivity.this, fileName);
                            }
                        })
                .addSheetItem(context.getResources().getString(R.string.family_sex_photo_add_user_albums), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                fileName = getFileName() + ".jpg";
                                BitmapUtil.showPicFromPhotoAlbum(AddOrUpdateUserActivity.this);
                            }
                        }).show();
    }

    /**
     * 获取照片名: family_{time}
     * @return
     */
    private String getFileName() {
        return "family" + "_" + StringUtil.getTimeName();
    }

    private void sendMsg(int whatId) {
        Message msg = new Message();
        msg.what = whatId;
        if (handler != null) {
            handler.sendMessage(msg);
        }
    }

    private void showTipId(int StrId) {
        Toast.makeText(this, StrId, Toast.LENGTH_LONG).show();
    }

    private void closeKeybord() {
        imm.hideSoftInputFromWindow(AddOrUpdateUserActivity.this.v[3].getWindowToken(), 0);
    }

    private void showNetwordToas() {
        if (isAddUser) {
            BLTToast.show(context, R.string.checknet_add_user);
        }else {
            showTipId(R.string.checknet_uptate);
        }
    }

    private void showTip(String errString) {
        Toast.makeText(this, errString, Toast.LENGTH_LONG).show();
    }

    /**
     * 修改用户头像的名字
     * @param oldName
     * @param newName
     */
    private void renameAvatarDir(String oldName, String newName) {
        try {
            ReNameBitmapUtil.reNameFile(BitmapUtil.PHOTO_DIR, oldName,  newName + ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("文件操作", "文件名修改失败");
        }
    }

    /**
     * 获取textView的文本
     * @param tv
     * @return
     */
    private String tvToString(TextView tv) {
        return tv.getText().toString();
    }

    public boolean isShowFilePic(File file) {
        boolean exists = file.exists();
        if (exists) {
            int zoomSacle = PicUtils.getZoomSacleByHeightAndWidth(file.getPath(),
                    PixelConvertUtil.dip2px(AddOrUpdateUserActivity.this, 73),
                    PixelConvertUtil.dip2px(AddOrUpdateUserActivity.this, 73));
            try {
                bitmap = PicUtils.getBitmapByZoomSacle(file.getPath(), zoomSacle);
                //获取圆形图片
                bitmap = PicUtils.getRoundRectBitmap(bitmap, bitmap.getWidth());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ivHeadImage.setBackgroundDrawable(new BitmapDrawable(bitmap));

        }
        return exists;
    }

    @Override
    public void finish() {
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        super.finish();
        overridePendingTransition(R.anim.in_lefttoright, R.anim.out_lefttoright);
    }
}
