package com.hldj.hmyg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.jar.*;

import me.drakeet.materialdialog.MaterialDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.*;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.utils.StatusBarUtils;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.application.StateBarUtil;
import com.hldj.hmyg.broker.adapter.ChooseManagerAdapter;
import com.hldj.hmyg.buy.bean.CollectCar;
import com.hldj.hmyg.saler.StorageSaveActivity;
import com.hldj.hmyg.saler.bean.ChooseManager;
import com.hldj.hmyg.update.UpdateDialog;
import com.hy.utils.GetLotLat;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.GetCitiyNameByCode;
import com.readystatesoftware.systembartint.StatusBarCompat;
import com.readystatesoftware.systembartint.StatusBarUtil;
import com.white.update.UpdateInfo;
import com.white.utils.SettingUtils;
import com.white.utils.SystemSetting;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements
        OnCheckedChangeListener, OnGeocodeSearchListener, LocationSource,
        AMapLocationListener {

    private static TabHost tabHost;
    private static RadioGroup radioderGroup;
    MaterialDialog mMaterialDialog;
    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;
    // 更新版本要用到的一些信息
    private ProgressDialog progressDialog;
    private UpdateInfo updateInfo;
    private static String DB_NAME = "flower.db";
    private static final String DB_TABLE = "savetable";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;
    private ArrayList<CollectCar> userList = new ArrayList<CollectCar>();

    ArrayList<ChooseManager> chooseManagers = new ArrayList<ChooseManager>();
    private ChooseManager chooseManager3;
    private String check = "1";

    public void setBasIn(BaseAnimatorSet bas_in) {
        this.mBasIn = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.mBasOut = bas_out;
    }

    private double latitude = 0.0;
    private double longitude = 0.0;

    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private Marker geoMarker;
    private Marker regeoMarker;
    private ProgressDialog progDialog;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    public Editor e;







        @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = MyApplication.Userinfo.edit();//初始化 sp
        GetLotLat.getBaiduLotLat(MainActivity.this, e);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);
        getAddress(latLonPoint);// 暂不用

        Data.screenWidth = getScreenWidth(false, MainActivity.this);

        if (Build.VERSION.SDK_INT >= 23 && MyApplication.getInstance().getApplicationInfo().targetSdkVersion >=23) {
            new PermissionUtils(this).needPermission(200);
        }
//         StatusBarCompat.compat(this);// 状态栏
//         StateBarUtil.setStatusBarIconDark(this,true);
        mMaterialDialog = new MaterialDialog(this);
        mBasIn = new BounceTopEnter();//进出场动画
        mBasOut = new SlideBottomExit();//进出场动画
        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this,//创建数据库
                DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
        // 执行SQL语句
        Cursor cursor = db.query(DB_TABLE, null, null, null, null, null,
                "_id DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String img = cursor.getString(1);
            String title = cursor.getString(2);
            String time = cursor.getString(3);
            String money = cursor.getString(4);
            String storage_save_id = cursor.getString(5);
            CollectCar car = new CollectCar(img, title, time, money, id,
                    storage_save_id, false, false, "");
            userList.add(car);
            cursor.moveToNext();
        }
        cursor.close();
        if (userList.size() > 0) {
            // 草稿箱上有东西
            if (mMaterialDialog != null) {
                mMaterialDialog
                        .setTitle("提示")
                        .setMessage("草稿箱内有未上传资源。是否进入草稿箱？")
                        .setPositiveButton(getString(R.string.ok),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                        Intent toStorageSaveActivity = new Intent(
                                                MainActivity.this,
                                                StorageSaveActivity.class);
                                        startActivity(toStorageSaveActivity);
                                    }

                                })
                        .setNegativeButton(getString(R.string.cancle),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                }).setCanceledOnTouchOutside(true).show();
            }
        }

        initData();

        tabHost = this.getTabHost();
        JPushInterface.setAlias(MainActivity.this, MyApplication.Userinfo
                .getString("id", "").replace("-", "").toLowerCase().trim()
                .toString(), new TagAliasCallback() {
            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                // TODO Auto-generated method stub

            }
        });
        if (MyApplication.Userinfo.getBoolean("notification", false)) {
            JPushInterface.resumePush(getApplicationContext());
        } else {
            JPushInterface.stopPush(getApplicationContext());
        }

        tabHost.addTab(tabHost.newTabSpec("1").setIndicator("1")
                .setContent(new Intent(this, AActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("2").setIndicator("2")
                .setContent(new Intent(this, BActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator("3")
                .setContent(new Intent(this, CActivity2.class)));
        tabHost.addTab(tabHost.newTabSpec("4").setIndicator("4")
                .setContent(new Intent(this, DActivity5.class)));
        tabHost.addTab(tabHost.newTabSpec("5").setIndicator("5")
                .setContent(new Intent(this, EActivity.class)));
        radioderGroup = (RadioGroup) findViewById(R.id.rg_tab);
        RadioButton tab_c = (RadioButton) findViewById(R.id.tab_c);
        radioderGroup.setOnCheckedChangeListener(this);
        radioderGroup.check(R.id.tab_a);// 默认第一个按钮
        check = "1";
        // getUpDateInfo();
        // getUpDateInfo4Pgyer();
        getVersion();
        if (getIntent().getScheme() != null
                && getIntent().getDataString() != null) {
            String scheme = this.getIntent().getScheme();// 获得Scheme名称
            String sche_data = getIntent().getDataString();// 获得Uri全部路径
            Log.e("onCreate", scheme + sche_data);
            // hldjhmeg+hldjhmeg://store?code=longlongago
            // hldjhmeg+hldjhmeg://mallDetailed?id=331561d94cee4017a5067c94f83203af
            if ("hldjhmeg".equals(scheme)) {
                if (sche_data.contains("hldjhmeg://store?id=")) {
                    Intent toStoreActivity = new Intent(MainActivity.this,
                            StoreActivity.class);
                    toStoreActivity.putExtra("id",
                            sche_data.replace("hldjhmeg://store?id=", ""));
                    startActivity(toStoreActivity);
                } else if (sche_data.contains("hldjhmeg://mallDetailed?id=")) {
                    Intent toFlowerDetailActivity = new Intent(
                            MainActivity.this, FlowerDetailActivity.class);
                    toFlowerDetailActivity.putExtra("id", sche_data.replace(
                            "hldjhmeg://mallDetailed?id=", ""));
                    startActivity(toFlowerDetailActivity);
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                } else if (sche_data.contains("hldjhmeg://mall")) {
                    radioderGroup.check(R.id.tab_b);
                }
            }
        }

    }

    private void initData() {
        // TODO Auto-generated method stub
        userList.clear();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this,
                DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
        // 执行SQL语句
        Cursor cursor = db.query(DB_TABLE, null, null, null, null, null,
                "_id DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String img = cursor.getString(1);
            String title = cursor.getString(2);
            String time = cursor.getString(3);
            String money = cursor.getString(4);
            String storage_save_id = cursor.getString(5);
            CollectCar car = new CollectCar(img, title, time, money, id,
                    storage_save_id, false, false, "");
            userList.add(car);
            cursor.moveToNext();
        }
        cursor.close();
        ChooseManager chooseManager1 = new ChooseManager("1", "发布苗木", "",
                R.drawable.fabuye_1, false);
        ChooseManager chooseManager2 = new ChooseManager("2", "苗木管理", "",
                R.drawable.fabuye_2, false);
        if (userList.size() > 0) {
            chooseManager3 = new ChooseManager("3", "草稿箱", "",
                    R.drawable.caogaoxiang2, true);
        } else {
            chooseManager3 = new ChooseManager("3", "草稿箱", "",
                    R.drawable.fabuye_3, false);
        }
        ChooseManager chooseManager4 = new ChooseManager("4", "发布采购", "",
                R.drawable.fabuye_4, false);
        ChooseManager chooseManager5 = new ChooseManager("5", "采购管理", "",
                R.drawable.fabuye_5, false);
        ChooseManager chooseManager6 = new ChooseManager("6", "快速记苗", "",
                R.drawable.fabuyemian_kuaisujimiao, false);
        ChooseManager chooseManager7 = new ChooseManager("7", "记苗本", "",
                R.drawable.fabuyemian_jimiaoben, false);
        ChooseManager chooseManager8 = new ChooseManager("8", "发布行情", "",
                R.drawable.fabuye_fabuhangqing, false);
        ChooseManager chooseManager9 = new ChooseManager("9", "实时行情", "",
                R.drawable.fabuye_shishihangqing, false);
        chooseManagers.add(chooseManager1);
        chooseManagers.add(chooseManager2);
        chooseManagers.add(chooseManager3);
        // chooseManagers.add(chooseManager4);
        // chooseManagers.add(chooseManager5);
        chooseManagers.add(chooseManager6);
        chooseManagers.add(chooseManager7);

        if (MyApplication.Userinfo.getBoolean("isLogin", false)) {
            if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
                chooseManagers.add(chooseManager8);
                chooseManagers.add(chooseManager9);
            } else {
            }
        }

    }

    public static void toA() {
        radioderGroup.check(R.id.tab_a);
    }

    int old_item_id = R.id.tab_a ;//默认是 主页
    public void resetGroupState(RadioGroup group,int checkedId)
    {
        for (int i = 0; i <group.getChildCount() ; i++) {
            if (old_item_id ==checkedId ){
                ( (RadioButton)group.getChildAt(i)).setChecked(true);
            }else{
//                ( (RadioButton)group.getChildAt(i)).setChecked(false);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.tab_a:
                tabHost.setCurrentTabByTag("1");
                check = "1";
                break;
            case R.id.tab_b:
                tabHost.setCurrentTabByTag("2");
                check = "2";
                break;
            case R.id.tab_c:
                tabHost.setCurrentTabByTag("3");
                check = "3";
                // showDialog();
                break;
            case R.id.tab_d:
                if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
                    Intent toLoginActivity = new Intent(MainActivity.this,
                            LoginActivity.class);
                    startActivityForResult(toLoginActivity, 4);
//                    overridePendingTransition(R.anim.slide_in_left,
//                            R.anim.slide_out_right);
                    overridePendingTransition_open();
                    Log.e("onCheckedChanged", "onCheckedChanged: check = "+check);
                    ((RadioButton)group.getChildAt((Integer.parseInt(check))-1)).setChecked(true);
                } else {
                    tabHost.setCurrentTabByTag("4");
                    check = "4";
                }

                break;
            case R.id.tab_e:


                if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
                    Intent toLoginActivity = new Intent(MainActivity.this,
                            LoginActivity.class);
                    startActivityForResult(toLoginActivity, 4);
//                    overridePendingTransition(R.anim.slide_in_left,
//                            R.anim.slide_out_right);
                    overridePendingTransition_open();
                    Log.e("onCheckedChanged", "onCheckedChanged: check = "+check);
                    ((RadioButton)group.getChildAt((Integer.parseInt(check))-1)).setChecked(true);
                } else {
                    tabHost.setCurrentTabByTag("5");
                    check = "5";

                }

                break;
        }


//        resetGroupState(group,checkedId);

    }

    /**
     * 打开activity 动画
     */
    public void overridePendingTransition_open() {
        //进
        //100  ---   0
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_left_out);
        //出
        //0  --- -100
    }

    /**
     * 关闭activity 动画
     */


    public  void overridePendingTransition_back() {
        //进
        //-100  ---   0
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        //出
        //0  --- 100
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishAc();
        }
        return super.dispatchKeyEvent(event);
    }

    // public boolean dispatchKeyEvent(KeyEvent event) {
    // if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
    // && event.getAction() == KeyEvent.ACTION_DOWN) {
    // if (mMaterialDialog != null) {
    // mMaterialDialog
    // .setTitle(getString(R.string.out))
    // .setMessage(getString(R.string.out_msg))
    // // mMaterialDialog.setBackgroundResource(R.drawable.background);
    // .setPositiveButton(getString(R.string.ok),
    // new View.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // mMaterialDialog.dismiss();
    //
    // finishAc();
    //
    // }
    //
    // })
    // .setNegativeButton(getString(R.string.cancle),
    // new View.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // mMaterialDialog.dismiss();
    // }
    // }).setCanceledOnTouchOutside(true)
    // // You can change the message anytime.
    // // mMaterialDialog.setTitle("提示");
    // .setOnDismissListener(
    // new DialogInterface.OnDismissListener() {
    // @Override
    // public void onDismiss(DialogInterface dialog) {
    // }
    // }).show();
    // // You can change the message anytime.
    // // mMaterialDialog
    // //
    // .setMessage("嗨！这是一个 MaterialDialog. 它非常方便使用，你只需将它实例化，这个美观的对话框便会自动地显示出来。它简洁小巧，完全遵照 Google 2014 年发布的 Material Design 风格，希望你能喜欢它！^ ^");
    // } else {
    // Toast.makeText(getApplicationContext(),
    // "You should init firstly!", Toast.LENGTH_SHORT).show();
    // }
    //
    // return false;
    // }
    // return super.dispatchKeyEvent(event);
    // }

    // public boolean dispatchKeyEvent(KeyEvent event) {
    // if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
    // && event.getAction() == KeyEvent.ACTION_DOWN) {
    //
    // final com.flyco.dialog.widget.MaterialDialog dialog = new
    // com.flyco.dialog.widget.MaterialDialog(
    // MainActivity.this);
    // dialog.isTitleShow(false)
    // //
    // .btnNum(3).title(getString(R.string.out)).content(getResources().getString(R.string.out_msg))//
    // .btnText(getString(R.string.out), getString(R.string.out_msg), "退出账号")//
    // .showAnim(mBasIn)//
    // .dismissAnim(mBasOut)//
    // .show();
    //
    // dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
    // @Override
    // public void onBtnClick() {
    // dialog.dismiss();
    // }
    // }, new OnBtnClickL() {// right btn click listener
    // @Override
    // public void onBtnClick() {
    // dialog.dismiss();
    // }
    // }, new OnBtnClickL() {// middle btn click listener
    // @Override
    // public void onBtnClick() {
    // dialog.dismiss();
    // }
    // });
    //
    //
    // return false;
    // }
    // return super.dispatchKeyEvent(event);
    // }

    public void finishAc() {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {// android2.1
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.restartPackage(getPackageName());
        }
    }

    public UpdateInfo getUpDateInfo() {

        updateInfo = new UpdateInfo();
        FinalHttp fh = new FinalHttp();
        fh.get(GetServerUrl.getFIR(), new AjaxCallBack<Object>() {

            @Override
            public void onSuccess(Object t) {
                // TODO Auto-generated method stub
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(t.toString());
                    String changelog = JsonGetInfo.getJsonString(jsonObject,
                            "changelog");
                    String versionShort = JsonGetInfo.getJsonString(jsonObject,
                            "versionShort");
                    String install_url = JsonGetInfo.getJsonString(jsonObject,
                            "install_url");
                    String new_version = JsonGetInfo.getJsonString(jsonObject,
                            "version");
                    updateInfo.setVersion(versionShort);
                    updateInfo.setDescription(changelog);
                    updateInfo.setUrl(install_url);
                    // 获取当前版本号
                    String now_version = "";
                    try {
                        PackageManager packageManager = getPackageManager();
                        PackageInfo packageInfo = packageManager
                                .getPackageInfo(getPackageName(), 0);
                        now_version = packageInfo.versionName;
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (!versionShort.equals(now_version)) {
                        handler1.sendEmptyMessage(0);
                    } else {
                        handler1.sendEmptyMessage(1);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                super.onSuccess(t);
            }

        });
        return updateInfo;
    }

    public UpdateInfo getVersion() {

        updateInfo = new UpdateInfo();
        FinalHttp fh = new FinalHttp();
        GetServerUrl.addHeaders(fh, true);
        AjaxParams params = new AjaxParams();
        params.put("appType", "Android");
        fh.post(GetServerUrl.getUrl() + "version/getVersion", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(t.toString());
                            JSONObject data = JsonGetInfo.getJSONObject(
                                    jsonObject, "data");
                            JSONObject version = JsonGetInfo.getJSONObject(
                                    data, "version");
                            String changelog = JsonGetInfo.getJsonString(
                                    version, "remarks");
                            String versionNum = JsonGetInfo.getJsonString(
                                    version, "versionNum");
                            String url = JsonGetInfo.getJsonString(version,
                                    "url");
                            String new_version = JsonGetInfo.getJsonString(
                                    version, "versionNum");
                            boolean isForce = JsonGetInfo.getJsonBoolean(data,
                                    "isForce");
                            updateInfo.setVersion(versionNum);
                            updateInfo.setDescription(changelog);
                            updateInfo.setUrl(url);
                            // updateInfo.setUrl(GetServerUrl.getPGYER_UPLOAD());
                            updateInfo.setForce(isForce);
                            // 获取当前版本号
                            String now_version = "";
                            try {
                                PackageManager packageManager = getPackageManager();
                                PackageInfo packageInfo = packageManager
                                        .getPackageInfo(getPackageName(), 0);
                                now_version = packageInfo.versionName;
                            } catch (NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (!versionNum.equals(now_version)) {
                                handler1.sendEmptyMessage(0);
                            } else {
                                handler1.sendEmptyMessage(1);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        super.onSuccess(t);
                    }

                });
        return updateInfo;
    }

    public UpdateInfo getUpDateInfo4Pgyer() {

        updateInfo = new UpdateInfo();
        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("aId", GetServerUrl.getaId());
        params.put("_api_key", GetServerUrl.get_api_key());
        params.put("uKey", GetServerUrl.getuKey());
        fh.post(GetServerUrl.getPGYER(), params, new AjaxCallBack<Object>() {

            @Override
            public void onSuccess(Object t) {
                // TODO Auto-generated method stub
                JSONObject Jo;
                try {
                    Jo = new JSONObject(t.toString());
                    JSONArray data = JsonGetInfo.getJsonArray(Jo, "data");
                    if (data.length() > 0) {
                        JSONObject jsonObject = data.getJSONObject(data
                                .length() - 1);
                        String changelog = JsonGetInfo.getJsonString(
                                jsonObject, "appUpdateDescription");
                        String versionShort = JsonGetInfo.getJsonString(
                                jsonObject, "appVersion");
                        String install_url = GetServerUrl.getPGYER_UPLOAD();
                        String new_version = JsonGetInfo.getJsonString(
                                jsonObject, "appVersionNo");
                        updateInfo.setVersion(versionShort);
                        updateInfo.setDescription(changelog);
                        updateInfo.setUrl(install_url);
                        // 获取当前版本号
                        String now_version = "";
                        try {
                            PackageManager packageManager = getPackageManager();
                            PackageInfo packageInfo = packageManager
                                    .getPackageInfo(getPackageName(), 0);
                            now_version = packageInfo.versionName;
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (!versionShort.equals(now_version)) {
                            handler1.sendEmptyMessage(0);
                        } else {
                            handler1.sendEmptyMessage(1);
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                super.onSuccess(t);
            }

        });
        return updateInfo;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示

            if (msg.what == 0) {
                // showUpdateDialog();
                showUpdateDialog2();
            } else if (msg.what == 1) {
                // Toast toast = Toast.makeText(MainActivity.this,
                // R.string.the_version_is_new, Toast.LENGTH_SHORT);
                // toast.setGravity(Gravity.CENTER, 0, 0);
                // toast.show();
            }
        }

        ;
    };
    private Dialog dialog;
    private ChooseManagerAdapter myadapter;

    private void showUpdateDialog2() {

        UpdateDialog.Builder builder = new UpdateDialog.Builder(this);
        builder.setTitle(getResources().getString(
                R.string.please_update_app_to_version)
                + updateInfo.getVersion());
        builder.setPrice("");
        builder.setCount("");
        builder.setAccountName(updateInfo.getDescription());
        builder.setAccountBank("");
        builder.setAccountNum("");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // downFile(updateInfo.getUrl());
                    SettingUtils.launchBrowser(MainActivity.this,
                            updateInfo.getUrl());
                } else {
                    Toast.makeText(MainActivity.this,
                            R.string.sd_card_is_disable, Toast.LENGTH_SHORT)
                            .show();
                }
                // 设置你的操作事项
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();

    }

    // 显示是否要更新的对话框
    private void showUpdateDialog() {

        final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
                MainActivity.this);
        dialog.title(
                getResources().getString(R.string.please_update_app_to_version)
                        + updateInfo.getVersion())
                .content(updateInfo.getDescription())
                //
                .btnText(getResources().getString(R.string.cancle),
                        getResources().getString(R.string.ok))//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
            @Override
            public void onBtnClick() {
                if (updateInfo.isForce()) {
                    finishAc();
                } else {
                    dialog.dismiss();
                }

            }
        }, new OnBtnClickL() {// right btn click listener
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // downFile(updateInfo.getUrl());
                    SettingUtils.launchBrowser(MainActivity.this,
                            updateInfo.getUrl());
                } else {
                    Toast.makeText(MainActivity.this,
                            R.string.sd_card_is_disable,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void downFile(final String url) {
        progressDialog = new ProgressDialog(MainActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(getResources().getString(R.string.is_download));
        progressDialog.setMessage(getResources().getString(
                R.string.please_waiting));
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    public static int getScreenHeight(boolean islandscape, Context context) {
        if (islandscape) {
            return getScreenWidth(false, context);
        }
        if (SystemSetting.getInstance(context).screenHeight != 0)
            return SystemSetting.getInstance(context).screenHeight;
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        SystemSetting.getInstance(context).setGlobalScreenHeight(
                Math.max(display.getWidth(), display.getHeight()));
        return SystemSetting.getInstance(context).screenHeight;
    }

    /**
     * 获得屏幕宽
     *
     * @param islandscape TODO
     */
    public static int getScreenWidth(boolean islandscape, Context context) {
        if (islandscape) {
            return getScreenHeight(false, context);
        }
        if (SystemSetting.getInstance(context).screenWidth != 0)
            return SystemSetting.getInstance(context).screenWidth;
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        SystemSetting.getInstance(context).setGlobalScreenWidth(
                Math.min(display.getWidth(), display.getHeight()));
        return SystemSetting.getInstance(context).screenWidth;
    }

    /**
     * 静态Helper类，用于建立、更新和打开数据库
     */
    private static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE = "create table savetable(_id integer primary key autoincrement,img text,title text,time text,money text,storage_save_id text)";

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
                              int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(_db);
        }
    }

    private void showDialog() {
        View dia_choose_share = getLayoutInflater().inflate(
                R.layout.dia_choose_publish, null);
        GridView gridView = (GridView) dia_choose_share
                .findViewById(R.id.gridView);
        Button btn_cancle = (Button) dia_choose_share
                .findViewById(R.id.btn_cancle);
        if (chooseManagers.size() > 0) {
            if (myadapter == null) {
                myadapter = new ChooseManagerAdapter(MainActivity.this,
                        chooseManagers);
                gridView.setAdapter(myadapter);
            } else {
                myadapter.notify(chooseManagers);
            }

        }
        dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(dia_choose_share, new LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dia_choose_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!MainActivity.this.isFinishing() && dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    } else {
                        dialog.show();
                    }
                }

            }
        });
        btn_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!MainActivity.this.isFinishing() && dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    } else {
                        dialog.show();
                    }
                }

            }
        });

        if (!MainActivity.this.isFinishing() && dialog.isShowing()) {
            dialog.cancel();
        } else if (!MainActivity.this.isFinishing() && dialog != null
                && !dialog.isShowing()) {
            dialog.show();
        }

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            // aMap.setMyLocationType()
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        JPushInterface.onResume(MyApplication.getInstance());
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        JPushInterface.onPause(MyApplication.getInstance());

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog2() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        // showDialog2();
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        // showDialog2();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                // AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                // geoMarker.setPosition(AMapUtil.convertToLatLng(address
                // .getLatLonPoint()));
                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();
            } else {
                Toast.makeText(MainActivity.this, R.string.no_result, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this, rCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 逆地理编码回调 code就在这里获取
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                // AMapUtil.convertToLatLng(latLonPoint), 15));
                // regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                // Toast.makeText(AddAdressActivity.this, addressName,
                // 1).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.no_result, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, rCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                String cityCode2 = amapLocation.getCityCode();
                String cityName = amapLocation.getCity();
                String adCode = amapLocation.getAdCode();
                e.putString("ScityCode", cityCode2);
                e.putString("ScityName", GetCitiyNameByCode.initProvinceDatas(
                        MainActivity.this, adCode));
                e.putString("latitude", amapLocation.getLatitude() + "");
                e.putString("longitude", amapLocation.getLongitude() + "");
                e.commit();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

}
