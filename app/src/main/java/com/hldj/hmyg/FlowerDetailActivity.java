package com.hldj.hmyg;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedittext.paramsData;
import com.google.gson.Gson;
import com.hldj.hmyg.adapter.SeedlingParmAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.SeedlingParm;
import com.hldj.hmyg.buyer.AddPurchaseActivity;
import com.hldj.hmyg.saler.SavePriceAndCountAndOutlineActivity;
import com.hldj.hmyg.saler.SaveSeedlingActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;
import com.javis.ab.view.AbOnItemClickListener;
import com.javis.ab.view.AbSlidingPlayView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yangfuhai.asimplecachedemo.lib.ACache;
import com.zf.iosdialog.widget.AlertDialog;
import com.zzy.flowers.widget.popwin.EditP2;
import com.zzy.flowers.widget.popwin.EditP3;

@SuppressLint({ "ResourceAsColor", "Override" })
public class FlowerDetailActivity extends NeedSwipeBackActivity {
	private AbSlidingPlayView viewPager;
	private FinalBitmap fb;
	private ArrayList<String> banners = new ArrayList<String>();
	private ArrayList<Pic> ossImagePaths = new ArrayList<Pic>();
	private ArrayList<View> allListView;
	private String show_type = "";
	private String id = "";
	private TextView tv_name;
	private TextView tv_no_server_area;
	private TextView tv_price;
	private TextView tv_count;
	private TextView tv_saleCount;
	private TextView tv_unitTypeName;
	private TextView tv_statusName;
	private TextView tv_seedlingNum;
	private TextView tv_closeDate;
	private TextView tv_remarks;
	private LinearLayout ll_to_d3;
	private LinearLayout ll_to_d4;
	private LinearLayout ll_manager_backed;
	private LinearLayout ll_manager_unaudit;
	private LinearLayout ll_manager_unsubmit;
	private LinearLayout ll_manager_published;
	private LinearLayout ll_manager_outline;
	private LinearLayout ll_buy_car;
	private MultipleClickProcess multipleClickProcess;
	private TextView tv_01_01;
	private TextView tv_01_02;
	private TextView tv_02_01;
	private TextView tv_03_01;
	private TextView tv_04_01;
	private TextView tv_04_02;
	private TextView tv_04_03;
	private TextView tv_05_02;
	private TextView tv_05_03;
	private TextView tv_add_car;
	private boolean isOwner;
	private boolean cartExist;
	private String url = "";
	public String et_num;
	public String days;
	private View mainView;

	private int saleCount = 0;
	private int stock = 0;
	private KProgressHUD hud;
	private String store_id = "";
	public String displayPhone = "";
	private LinearLayout ll_01;
	private LinearLayout ll_02;
	private TextView tv_01;
	private TextView tv_02;
	private ListView lv_00;
	ArrayList<SeedlingParm> msSeedlingParms = new ArrayList<SeedlingParm>();
	public ArrayList<paramsData> paramsDatas = new ArrayList<paramsData>();
	private TextView tv_status_01;
	private TextView tv_status_02;
	private TextView tv_status_03;
	private TextView tv_status_04;
	private TextView tv_status_05;
	private TextView tv_store_area;
	private TextView tv_store_name;
	private TextView tv_store_phone;
	private TextView tv_contanct_name;
	private LinearLayout ll_store;
	private LinearLayout ll_bohao;
	private Double price = 0.0;
	private Double floorPrice = 0.0;
	private int count = 0;
	private int lastDay = 0;
	private String remarks = "";
	private String name = "";
	private String firstSeedlingTypeId = "";
	private String secondSeedlingTypeId = "";
	private String diameterType = "";
	private String dbhType = "";
	private String plantType = "";
	private String unitType = "";
	private String firstTypeName = "";
	private String secondTypeName = "";
	private String nurseryId = "";
	private String seedlingParams = "";
	private String status;
	private int dbh = 0;
	private int diameter = 0;
	private int crown = 0;
	private int height = 0;
	private int length = 0;
	private int offbarHeight = 0;
	private int turang = 0;
	private int zhixiagao = 0;
	private int validity = 0;
	private ImageView iv_lianxi;
	private LinearLayout ll_data;
	private ImageView sc_ziying;
	private ImageView sc_fuwufugai;
	private ImageView sc_hezuoshangjia;
	private ImageView sc_huodaofukuan;
	private ACache mCache;
	private TextView tv_last_time;
	private String closeDate = "";
	private TextView tv_plant;
	private DisplayImageOptions options;

	private ArrayList<String> urlPaths = new ArrayList<String>();
	MaterialDialog mMaterialDialog;
	private TextView tv_dijia;
	private TextView tv_title;
	private String contactName = "";
	private String contactPhone = "";
	private String address_name = "";
	private Gson gson = new Gson();
	private LinearLayout ll_open;
	private TextView tv_open;
	private ImageView iv_open;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flower_detail);
		mMaterialDialog = new MaterialDialog(this);
		fb = FinalBitmap.create(this);
		mCache = ACache.get(this);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_image_big_show)
				.showImageOnFail(R.drawable.no_image_big_show_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888).cacheOnDisc(true)
				.cacheInMemory(true).build();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		show_type = getIntent().getStringExtra("show_type");
		id = getIntent().getStringExtra("id");
		multipleClickProcess = new MultipleClickProcess();
		mainView = (View) findViewById(R.id.mainView);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		iv_lianxi = (ImageView) findViewById(R.id.iv_lianxi);
		lv_00 = (ListView) findViewById(R.id.lv_00);
		lv_00.setDivider(null);
		tv_no_server_area = (TextView) findViewById(R.id.tv_no_server_area);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_plant = (TextView) findViewById(R.id.tv_plant);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_dijia = (TextView) findViewById(R.id.tv_dijia);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_saleCount = (TextView) findViewById(R.id.tv_saleCount);
		tv_unitTypeName = (TextView) findViewById(R.id.tv_unitTypeName);
		tv_statusName = (TextView) findViewById(R.id.tv_statusName);
		tv_seedlingNum = (TextView) findViewById(R.id.tv_seedlingNum);
		tv_closeDate = (TextView) findViewById(R.id.tv_closeDate);
		tv_remarks = (TextView) findViewById(R.id.tv_remarks);
		tv_status_01 = (TextView) findViewById(R.id.tv_status_01);
		tv_status_02 = (TextView) findViewById(R.id.tv_status_02);
		tv_status_03 = (TextView) findViewById(R.id.tv_status_03);
		tv_status_04 = (TextView) findViewById(R.id.tv_status_04);
		tv_status_05 = (TextView) findViewById(R.id.tv_status_05);
		sc_ziying = (ImageView) findViewById(R.id.sc_ziying);
		sc_fuwufugai = (ImageView) findViewById(R.id.sc_fuwufugai);
		sc_hezuoshangjia = (ImageView) findViewById(R.id.sc_hezuoshangjia);
		sc_huodaofukuan = (ImageView) findViewById(R.id.sc_huodaofukuan);

		tv_store_area = (TextView) findViewById(R.id.tv_store_area);
		tv_store_name = (TextView) findViewById(R.id.tv_store_name);
		tv_store_phone = (TextView) findViewById(R.id.tv_store_phone);
		tv_contanct_name = (TextView) findViewById(R.id.tv_contanct_name);

		ll_open = (LinearLayout) findViewById(R.id.ll_open);
		tv_open = (TextView) findViewById(R.id.tv_open);
		iv_open = (ImageView) findViewById(R.id.iv_open);

		viewPager = (AbSlidingPlayView) findViewById(R.id.viewPager_menu);
		// 设置播放方式为顺序播放
		viewPager.setPlayType(1);
		// 设置播放间隔时间
		viewPager.setSleepTime(3000);
		LayoutParams l_params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager wm = this.getWindowManager();
		l_params.height = wm.getDefaultDisplay().getWidth();
		viewPager.setLayoutParams(l_params);

		tv_last_time = (TextView) findViewById(R.id.tv_last_time);
		ll_data = (LinearLayout) findViewById(R.id.ll_data);
		ll_store = (LinearLayout) findViewById(R.id.ll_store);
		ll_bohao = (LinearLayout) findViewById(R.id.ll_bohao);
		ll_to_d3 = (LinearLayout) findViewById(R.id.ll_to_d3);
		ll_to_d4 = (LinearLayout) findViewById(R.id.ll_to_d4);
		ll_manager_backed = (LinearLayout) findViewById(R.id.ll_manager_backed);
		tv_01_01 = (TextView) findViewById(R.id.tv_01_01);
		tv_01_02 = (TextView) findViewById(R.id.tv_01_02);
		ll_manager_unaudit = (LinearLayout) findViewById(R.id.ll_manager_unaudit);
		tv_02_01 = (TextView) findViewById(R.id.tv_02_01);
		ll_manager_unsubmit = (LinearLayout) findViewById(R.id.ll_manager_unsubmit);
		tv_03_01 = (TextView) findViewById(R.id.tv_03_01);
		ll_manager_published = (LinearLayout) findViewById(R.id.ll_manager_published);
		tv_04_01 = (TextView) findViewById(R.id.tv_04_01);
		tv_04_02 = (TextView) findViewById(R.id.tv_04_02);
		tv_04_03 = (TextView) findViewById(R.id.tv_04_03);
		ll_manager_outline = (LinearLayout) findViewById(R.id.ll_manager_outline);
		tv_05_02 = (TextView) findViewById(R.id.tv_05_02);
		tv_05_03 = (TextView) findViewById(R.id.tv_05_03);
		tv_add_car = (TextView) findViewById(R.id.tv_add_car);
		ll_buy_car = (LinearLayout) findViewById(R.id.ll_buy_car);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);

		if ("manage_list".equals(show_type)) {
			url = "admin/";
		} else if ("seedling_list".equals(show_type)) {
		}
		initData();
		visitsCount();
		ll_to_d3.setOnClickListener(multipleClickProcess);
		ll_to_d4.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_open.setOnClickListener(multipleClickProcess);

	}

	@SuppressLint("ResourceAsColor")
	private void initData() {
		// TODO Auto-generated method stub
		ll_manager_backed.setVisibility(View.GONE);
		ll_manager_outline.setVisibility(View.GONE);
		ll_manager_published.setVisibility(View.GONE);
		ll_manager_unaudit.setVisibility(View.GONE);
		ll_manager_unsubmit.setVisibility(View.GONE);
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		if (!url.contains("admin")) {
			params.put("userId", MyApplication.Userinfo.getString("id", ""));
		}
		finalHttp.post(GetServerUrl.getUrl() + url + "seedling/detail", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						if (!FlowerDetailActivity.this.isFinishing()) {
							hud = KProgressHUD
									.create(FlowerDetailActivity.this)
									.setStyle(
											KProgressHUD.Style.SPIN_INDETERMINATE)
									.setLabel("努力加载中...").setMaxProgress(100)
									.setCancellable(true).show();
						}

					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						super.onLoading(count, current);
					}

					@SuppressLint("ResourceAsColor")
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						mCache.remove("seedlingdetail" + id);
						mCache.put("seedlingdetail" + id, t.toString());
						AcheData(t.toString());
						super.onSuccess(t);
						if (hud != null) {
							hud.dismiss();
						}
					}

					private void AcheData(String t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t);
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
							}
							if ("1".equals(code)) {
								msSeedlingParms.clear();
								banners.clear();
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"), "seedling");
								JSONObject store = JsonGetInfo.getJSONObject(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "store");
								store_id = JsonGetInfo.getJsonString(store,
										"id");
								isOwner = JsonGetInfo.getJsonBoolean(
										jsonObject.getJSONObject("data"),
										"isOwner");
								cartExist = JsonGetInfo.getJsonBoolean(
										jsonObject.getJSONObject("data"),
										"cartExist");
								if (cartExist == true) {
									unAddCart();
								} else {

								}
								JSONArray jsonArray = JsonGetInfo.getJsonArray(
										jsonObject2, "imagesJson");
								banners.clear();
								ossImagePaths.clear();
								if (jsonArray.length() > 0) {
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										banners.add(JsonGetInfo.getJsonString(
												jsonObject3,
												"ossAppLargeImagePath"));
										ossImagePaths.add(new Pic(JsonGetInfo
												.getJsonString(jsonObject3,
														"id"), false,
												JsonGetInfo.getJsonString(
														jsonObject3, "ossUrl"),
												0));

									}
								} else {
									banners.add(JsonGetInfo.getJsonString(
											jsonObject2, "imageUrl"));
									ossImagePaths.add(new Pic(JsonGetInfo
											.getJsonString(jsonObject2, "id"),
											false, JsonGetInfo.getJsonString(
													jsonObject2, "imageUrl"), 0));
								}
								if (banners.size() > 0) {
									initViewPager();
								}
								name = JsonGetInfo.getJsonString(jsonObject2,
										"name");
								tv_title.setText(name);
								firstSeedlingTypeId = JsonGetInfo
										.getJsonString(jsonObject2,
												"firstSeedlingTypeId");
								secondSeedlingTypeId = JsonGetInfo
										.getJsonString(jsonObject2,
												"secondSeedlingTypeId");
								diameterType = JsonGetInfo.getJsonString(
										jsonObject2, "diameterType");
								dbhType = JsonGetInfo.getJsonString(
										jsonObject2, "dbhType");
								plantType = JsonGetInfo.getJsonString(
										jsonObject2, "plantType");
								unitType = JsonGetInfo.getJsonString(
										jsonObject2, "unitType");
								firstTypeName = JsonGetInfo.getJsonString(
										jsonObject2, "firstTypeName");
								secondTypeName = JsonGetInfo.getJsonString(
										jsonObject2, "secondTypeName");
								nurseryId = JsonGetInfo.getJsonString(
										jsonObject2, "nurseryId");
								seedlingParams = JsonGetInfo.getJsonString(
										jsonObject2, "seedlingParams");
								validity = JsonGetInfo.getJsonInt(jsonObject2,
										"validity");

								String standardName = JsonGetInfo
										.getJsonString(jsonObject2,
												"standardName");
								price = JsonGetInfo.getJsonDouble(jsonObject2,
										"price");
								floorPrice = JsonGetInfo.getJsonDouble(
										jsonObject2, "floorPrice");
								count = JsonGetInfo.getJsonInt(jsonObject2,
										"count");
								lastDay = JsonGetInfo.getJsonInt(jsonObject2,
										"lastDay");
								saleCount = JsonGetInfo.getJsonInt(jsonObject2,
										"saleCount");
								stock = JsonGetInfo.getJsonInt(jsonObject2,
										"stock");
								String unitTypeName = JsonGetInfo
										.getJsonString(jsonObject2,
												"unitTypeName");
								String seedlingNum = JsonGetInfo.getJsonString(
										jsonObject2, "seedlingNum");
								status = JsonGetInfo.getJsonString(jsonObject2,
										"status");
								String statusName = JsonGetInfo.getJsonString(
										jsonObject2, "statusName");
								closeDate = JsonGetInfo.getJsonString(
										jsonObject2, "closeDate");
								remarks = JsonGetInfo.getJsonString(
										jsonObject2, "remarks");
								tv_name.setText(standardName);
								tv_price.setText(ValueGetInfo.doubleTrans1(price));
								if (floorPrice > 0
										&& "manage_list".equals(show_type)) {
									tv_dijia.setText("底价："
											+ ValueGetInfo.doubleTrans1(floorPrice));
								}
								tv_count.setText("库存：" + stock);
								tv_saleCount.setText("已售：" + saleCount
										+ unitTypeName);
								tv_unitTypeName.setText("元/" + unitTypeName);
								String specText = JsonGetInfo.getJsonString(
										jsonObject2, "specText");
								tv_seedlingNum.setText("规格：" + specText);
								tv_statusName.setText(statusName);
								tv_closeDate.setText("下架时间：" + closeDate);
								if (remarks.length() == 0) {
									remarks = "-";
								}

								if (JsonGetInfo.getJsonBoolean(jsonObject2,
										"isSelfSupport")) {
									tv_status_01.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo.getJsonBoolean(jsonObject2,
										"freeValidatePrice")) {
									tv_status_02.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo.getJsonBoolean(jsonObject2,
										"cashOnDelivery")) {
									tv_status_03.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo.getJsonBoolean(jsonObject2,
										"freeDeliveryPrice")) {
									tv_status_04.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo.getJsonBoolean(jsonObject2,
										"freeValidate")) {
									tv_status_05.setVisibility(View.VISIBLE);
								}

								//
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"tagList").length() == 0) {
									tv_no_server_area
											.setVisibility(View.VISIBLE);
								} else {
									tv_no_server_area.setVisibility(View.GONE);

								}

								if (plantType.contains("planted")) {
									tv_plant.setBackgroundResource(R.drawable.icon_seller_di);
								} else if (plantType.contains("transplant")) {
									tv_plant.setBackgroundResource(R.drawable.icon_seller_yi);
								} else if (plantType.contains("heelin")) {
									tv_plant.setBackgroundResource(R.drawable.icon_seller_jia);
								} else if (plantType.contains("container")) {
									tv_plant.setBackgroundResource(R.drawable.icon_seller_rong);
								} else {
									tv_plant.setVisibility(View.GONE);
								}

								long lastTime = JsonGetInfo.getJsonLong(
										jsonObject2, "lastTime");
								if (lastTime > 0) {
									tv_last_time.setVisibility(View.VISIBLE);
									MyCount myCount = new MyCount(lastTime,
											1000l);
									myCount.start();
								}

								if (JsonGetInfo
										.getJsonArray(jsonObject2, "tagList")
										.toString().contains(Data.ZIYING)) {
									sc_ziying.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo
										.getJsonArray(jsonObject2, "tagList")
										.toString().contains(Data.FUWU)) {
									sc_fuwufugai.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo
										.getJsonArray(jsonObject2, "tagList")
										.toString()
										.contains(Data.HEZUOSHANGJIA)) {
									sc_hezuoshangjia
											.setVisibility(View.VISIBLE);
								}
								if (JsonGetInfo
										.getJsonArray(jsonObject2, "tagList")
										.toString().contains(Data.ZIJINDANBAO)) {
									sc_huodaofukuan.setVisibility(View.VISIBLE);
								}

								if ("manage_list".equals(show_type)) {
									tv_statusName.setVisibility(View.VISIBLE);
									ll_buy_car.setVisibility(View.GONE);
									if ("backed".equals(status)) {
										ll_manager_backed
												.setVisibility(View.VISIBLE);
										JSONObject auditLogJson = JsonGetInfo
												.getJSONObject(jsonObject2,
														"auditLogJson");
										String remarks = JsonGetInfo
												.getJsonString(auditLogJson,
														"remarks");
										tv_01_01.setText("退回原因：" + remarks);
									} else if ("unaudit".equals(status)) {
										ll_manager_unaudit
												.setVisibility(View.VISIBLE);
									} else if ("unsubmit".equals(status)) {
										ll_manager_unsubmit
												.setVisibility(View.VISIBLE);
									} else if ("published".equals(status)) {
										ll_manager_published
												.setVisibility(View.VISIBLE);
									} else if ("outline".equals(status)) {
										ll_manager_outline
												.setVisibility(View.VISIBLE);
									}
									tv_01_01.setOnClickListener(multipleClickProcess); // 退回原因
									tv_02_01.setOnClickListener(multipleClickProcess); // 撤回
									tv_04_01.setOnClickListener(multipleClickProcess); // 下架
									tv_04_02.setOnClickListener(multipleClickProcess);// 延期
									tv_05_02.setOnClickListener(multipleClickProcess);// 上架

									tv_01_02.setOnClickListener(multipleClickProcess);
									tv_03_01.setOnClickListener(multipleClickProcess);
									tv_04_03.setOnClickListener(multipleClickProcess);
									tv_05_03.setOnClickListener(multipleClickProcess);

								} else if ("seedling_list".equals(show_type)) {
									tv_statusName.setVisibility(View.VISIBLE);
									ll_buy_car.setVisibility(View.VISIBLE);
									ll_manager_backed.setVisibility(View.GONE);
									ll_manager_unaudit.setVisibility(View.GONE);
									ll_manager_unsubmit
											.setVisibility(View.GONE);
									ll_manager_published
											.setVisibility(View.GONE);
									ll_manager_outline.setVisibility(View.GONE);
									tv_add_car
											.setOnClickListener(multipleClickProcess);

									ll_buy_car
											.setOnClickListener(multipleClickProcess);

								}

								if (!"".equals(seedlingNum)) {
									msSeedlingParms.add(new SeedlingParm(
											"商品编号：", seedlingNum));
								}
								if (!"".equals(firstTypeName)) {
									msSeedlingParms.add(new SeedlingParm("分类：",
											firstTypeName));
								}
								String plantTypeName = JsonGetInfo
										.getJsonString(jsonObject2,
												"plantTypeName");
								// tv_remarks.setText("备注：" + remarks);
								tv_remarks.setText("种植类型：" + plantTypeName);
								// if (!"".equals(firstTypeName)) {
								// msSeedlingParms.add(new SeedlingParm(
								// "种植类型：", plantTypeName));
								// }
								String fullName = JsonGetInfo.getJsonString(
										JsonGetInfo.getJSONObject(jsonObject2,
												"ciCity"), "fullName");
								if ("seedling_list".equals(show_type)){
									tv_statusName.setText(fullName);
									tv_statusName.setTextColor(Color.parseColor("#999999"));
								}
								dbh = JsonGetInfo
										.getJsonInt(jsonObject2, "dbh");
								// if (dbh > 0) {
								// msSeedlingParms.add(new SeedlingParm(
								// "胸径",
								// dbh
								// + "cm"
								// + "\u0020"
								// + JsonGetInfo
								// .getJsonString(
								// jsonObject2,
								// "dbhTypeName")));
								// }
								diameter = JsonGetInfo.getJsonInt(jsonObject2,
										"diameter");
								// if (diameter > 0) {
								// msSeedlingParms.add(new SeedlingParm(
								// "地径",
								// diameter
								// + "cm"
								// + "\u0020"
								// + JsonGetInfo
								// .getJsonString(
								// jsonObject2,
								// "diameterTypeName")));
								// }
								height = JsonGetInfo.getJsonInt(jsonObject2,
										"height");
								// if (height > 0) {
								// msSeedlingParms.add(new SeedlingParm("高度",
								// height + "cm"));
								// }
								crown = JsonGetInfo.getJsonInt(jsonObject2,
										"crown");
								// if (crown > 0) {
								// msSeedlingParms.add(new SeedlingParm("冠幅",
								// crown + "cm"));
								// }
								length = JsonGetInfo.getJsonInt(jsonObject2,
										"length");
								// if (length > 0) {
								// msSeedlingParms.add(new SeedlingParm("长度",
								// height + "cm"));
								// }

								offbarHeight = JsonGetInfo.getJsonInt(
										jsonObject2, "offbarHeight");
								// if (offbarHeight > 0) {
								// msSeedlingParms.add(new SeedlingParm("脱杆高",
								// offbarHeight + "cm"));
								// }

								turang = JsonGetInfo.getJsonInt(jsonObject2,
										"turang");
								// if (turang > 0) {
								// msSeedlingParms.add(new SeedlingParm("土球",
								// turang + "cm"));
								// }
								zhixiagao = JsonGetInfo.getJsonInt(jsonObject2,
										"zhixiagao");
								// if (zhixiagao > 0) {
								// msSeedlingParms.add(new SeedlingParm("枝下高",
								// zhixiagao + "cm"));
								// }

								JSONArray extParamsList = JsonGetInfo
										.getJsonArray(jsonObject2,
												"extParamsList");
								JSONArray specList = JsonGetInfo.getJsonArray(
										jsonObject2, "specList");
								// if (specList.length() > 0) {
								// for (int i = 0; i < specList.length(); i++) {
								//
								// if (!"".equals(JsonGetInfo
								// .getJsonString(specList
								// .getJSONObject(i),
								// "value"))) {
								// msSeedlingParms.add(new SeedlingParm(
								// JsonGetInfo.getJsonString(
								// specList.getJSONObject(i),
								// "name"),
								// JsonGetInfo.getJsonString(
								// specList.getJSONObject(i),
								// "value")));
								// }
								// }
								// }
								if (extParamsList.length() > 0) {
									paramsDatas.clear();
									for (int i = 0; i < extParamsList.length(); i++) {
										if (!"".equals(JsonGetInfo
												.getJsonString(extParamsList
														.getJSONObject(i),
														"value"))) {
											msSeedlingParms.add(new SeedlingParm(
													JsonGetInfo.getJsonString(
															extParamsList
																	.getJSONObject(i),
															"name")+" :",
													JsonGetInfo.getJsonString(
															extParamsList
																	.getJSONObject(i),
															"value")));
										}

										paramsData n = new paramsData();
										n.setValue(JsonGetInfo.getJsonString(
												extParamsList.getJSONObject(i),
												"value"));
										n.setCode(JsonGetInfo.getJsonString(
												extParamsList.getJSONObject(i),
												"code"));
										n.setName(JsonGetInfo.getJsonString(
												extParamsList.getJSONObject(i),
												"name"));
										paramsDatas.add(n);
									}

								}
//								if (!"".equals(fullName)) {
//									msSeedlingParms.add(new SeedlingParm("地区：",
//											fullName));
//								}
								msSeedlingParms.add(new SeedlingParm("备注：",
										remarks));

								if (msSeedlingParms.size() > 0) {
									SeedlingParmAdapter myadapter = new SeedlingParmAdapter(
											FlowerDetailActivity.this,
											msSeedlingParms);
									lv_00.setAdapter(myadapter);
								}
								JSONObject ownerJson = JsonGetInfo
										.getJSONObject(jsonObject2, "ownerJson");
								JSONObject coCity = JsonGetInfo.getJSONObject(
										ownerJson, "coCity");
								displayPhone = JsonGetInfo.getJsonString(
										ownerJson, "displayPhone");
								String displayName = JsonGetInfo.getJsonString(
										ownerJson, "displayName");
								String publicName = JsonGetInfo.getJsonString(
										ownerJson, "publicName");
								if ("".equals(JsonGetInfo.getJsonString(coCity,
										"fullName"))) {
									tv_store_area.setText("所在地区：" + fullName);
								} else {
									tv_store_area.setText("所在地区："
											+ JsonGetInfo.getJsonString(coCity,
													"fullName"));
								}

								tv_store_name.setText("公司名称：" + displayName);
								tv_store_phone.setText("电话：" + displayPhone);
								tv_contanct_name.setText("联系人：" + publicName);
								if ("manage_list".equals(show_type)) {
									JSONObject nurseryJson = JsonGetInfo
											.getJSONObject(jsonObject2,
													"nurseryJson");
									address_name = JsonGetInfo.getJsonString(
											nurseryJson, "cityName")
											+ JsonGetInfo.getJsonString(
													nurseryJson,
													"detailAddress");
									contactName = JsonGetInfo.getJsonString(
											nurseryJson, "contactName");
									contactPhone = JsonGetInfo.getJsonString(
											nurseryJson, "contactPhone");
									String companyName = JsonGetInfo
											.getJsonString(nurseryJson,
													"companyName");

									tv_store_area.setText("所在地区："
											+ address_name);
									tv_store_name
											.setText("公司名称：" + companyName);
									tv_store_phone
											.setText("电话：" + contactPhone);
									tv_contanct_name.setText("联系人："
											+ contactName);
									tv_02.setText("苗圃信息");
								}

								// ll_data.setVisibility(View.VISIBLE);
								iv_lianxi.setVisibility(View.VISIBLE);
								iv_lianxi
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View arg0) {
												// TODO Auto-generated
												// method stub
												if (MyApplication.Userinfo
														.getBoolean("isLogin",
																false)) {
													boolean requesCallPhonePermissions = new PermissionUtils(
															FlowerDetailActivity.this)
															.requesCallPhonePermissions(200);
													if (requesCallPhonePermissions) {
														CallPhone(displayPhone);
													}

												} else {
													Intent toLoginActivity = new Intent(
															FlowerDetailActivity.this,
															LoginActivity.class);
													startActivityForResult(
															toLoginActivity, 4);
													overridePendingTransition(
															R.anim.slide_in_left,
															R.anim.slide_out_right);
												}

											}

										});

							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						if (mCache.getAsString("seedlingdetail" + id) != null
								&& !"".equals(mCache
										.getAsString("seedlingdetail" + id))) {
							AcheData(mCache.getAsString("seedlingdetail" + id));
						}
						Toast.makeText(FlowerDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
						if (hud != null) {
							hud.dismiss();
						}
					}

				});

	}

	public void unAddCart() {
		tv_add_car.setBackgroundColor(Color.parseColor("#CCCCCC"));
		tv_add_car.setTextColor(Color.parseColor("#FFFFFF"));
		tv_add_car.setText("已加入购物车");
		tv_add_car.setClickable(false);
	}

	private void visitsCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("seedlingId", id);
		finalHttp.post(GetServerUrl.getUrl() + "seedling/visitsCount", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void initViewPager() {

		if (allListView != null) {
			for (int i = 0; i < allListView.size(); i++) {
				allListView.remove(i);
			}
			allListView.clear();
			allListView = null;
			viewPager.removeAllViews();
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < banners.size(); i++) {
			// 导入ViewPager的布局
			View view = LayoutInflater.from(this).inflate(R.layout.pic_item,
					null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			// fb.display(imageView, banners.get(i));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			ImageLoader.getInstance().displayImage(banners.get(i), imageView);
			allListView.add(view);
		}

		viewPager.addViews(allListView);
		// 开始轮播
		viewPager.startPlay();

		viewPager.setOnItemClickListener(new AbOnItemClickListener() {
			@Override
			public void onClick(int position) {

				GalleryImageActivity.startGalleryImageActivity(
						FlowerDetailActivity.this, position, ossImagePaths);

				// String[] urls = new String[ossImagePaths.size()];
				// // TODO Auto-generated method stub
				// for (int i = 0; i < ossImagePaths.size(); i++) {
				// urls[i] = ossImagePaths.get(i);
				// }
				// Intent intent = new Intent(FlowerDetailActivity.this,
				// ImagePagerActivity.class);
				// intent.putExtra("image_urls", urls);
				// intent.putExtra("image_index", position); // 从第一张默认
				// intent.putExtra("name", "");
				// startActivity(intent);
				// overridePendingTransition(R.anim.push_left_in,
				// R.anim.push_left_out);

			}
		});
	}

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;
		private EditP2 popwin;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				switch (view.getId()) {
				case R.id.btn_back:
					onBackPressed();
					break;
				case R.id.ll_01:
					tv_01.setTextColor(getResources().getColor(
							R.color.main_color));
					tv_02.setTextColor(getResources().getColor(
							R.color.light_gray));
					ll_store.setVisibility(View.GONE);
					lv_00.setVisibility(View.VISIBLE);
					break;
				case R.id.ll_02:
					tv_01.setTextColor(getResources().getColor(
							R.color.light_gray));
					tv_02.setTextColor(getResources().getColor(
							R.color.main_color));
					ll_store.setVisibility(View.VISIBLE);
					lv_00.setVisibility(View.GONE);
					break;
				case R.id.tv_01_01: // 退回原因
					break;
				case R.id.tv_01_02: // 修改信息
					saveSeedling();
					break;
				case R.id.tv_03_01: // 修改信息
					saveSeedling();
					break;
				case R.id.tv_04_03: // 修改信息
					savePriceAndCountAndOutline();
					break;
				case R.id.tv_05_03: // 修改信息
					saveSeedling();
					break;
				case R.id.tv_02_01: // 撤回
					doBack();
					break;
				case R.id.tv_04_01: // 下架

					if (mMaterialDialog != null) {
						mMaterialDialog
								.setMessage("确定是否下架该商品？")
								// mMaterialDialog.setBackgroundResource(R.drawable.background);
								.setPositiveButton(getString(R.string.ok),
										new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												mMaterialDialog.dismiss();
												doOutline();
											}
										})
								.setNegativeButton(getString(R.string.cancle),
										new View.OnClickListener() {
											public void onClick(View v) {
												mMaterialDialog.dismiss();
											}
										}).setCanceledOnTouchOutside(true)
								// You can change the message anytime.
								// mMaterialDialog.setTitle("提示");
								.setOnDismissListener(
										new DialogInterface.OnDismissListener() {
											@Override
											public void onDismiss(
													DialogInterface dialog) {
											}
										}).show();
					} else {
					}

					break;
				case R.id.tv_04_02: // 延期
					getExtensionData();
					break;
				case R.id.tv_05_02: // 上架
					savePriceAndCountAndOutline();
					break;
				case R.id.ll_open:
					if (lv_00.getVisibility() == View.GONE) {
						lv_00.setVisibility(View.VISIBLE);
						tv_open.setText("收起");
						iv_open.setImageResource(R.drawable.xiangqing_shouqi);
					} else if (lv_00.getVisibility() == View.VISIBLE) {
						lv_00.setVisibility(View.GONE);
						tv_open.setText("更多");
						iv_open.setImageResource(R.drawable.xiangqing_gengduo);
					}
					break;

				case R.id.ll_to_d3:
					if (!"".equals(store_id)) {
						Intent toStoreActivity = new Intent(
								FlowerDetailActivity.this, StoreActivity.class);
						toStoreActivity.putExtra("code", store_id);
						startActivity(toStoreActivity);
					}

					break;
				case R.id.ll_to_d4: //
					if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
						Intent toLoginActivity = new Intent(
								FlowerDetailActivity.this, LoginActivity.class);
						startActivityForResult(toLoginActivity, 4);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						return;
					}
					Intent toDActivity4 = new Intent(FlowerDetailActivity.this,
							DActivity5.class);
					toDActivity4.putExtra("type", "1");
					startActivity(toDActivity4);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.tv_add_car:
					if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
						Intent toLoginActivity = new Intent(
								FlowerDetailActivity.this, LoginActivity.class);
						startActivityForResult(toLoginActivity, 4);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
						return;
					}
					if (isOwner == true) {
						Toast.makeText(FlowerDetailActivity.this, "自家商品不可购买",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (stock <= 0) {
						Toast.makeText(FlowerDetailActivity.this, "库存不足",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (!cartExist) {
						popwin = new EditP2(FlowerDetailActivity.this, ""
								+ stock, FlowerDetailActivity.this);
						popwin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
						popwin.showAtLocation(mainView, Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
					}

					break;
				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		public void saveSeedling() {
			if (!"".equals(id)) {
				Intent toSaveSeedlingActivity = new Intent(
						FlowerDetailActivity.this, SaveSeedlingActivity.class);
				Bundle bundleObject = new Bundle();
				final PicSerializableMaplist myMap = new PicSerializableMaplist();
				myMap.setMaplist(ossImagePaths);
				bundleObject.putSerializable("urlPaths", myMap);
				toSaveSeedlingActivity.putExtras(bundleObject);
				toSaveSeedlingActivity.putExtra("id", id);
				toSaveSeedlingActivity.putExtra("name", name);
				toSaveSeedlingActivity.putExtra("price", price + "");
				toSaveSeedlingActivity.putExtra("floorPrice", floorPrice + "");
				toSaveSeedlingActivity.putExtra("count", count + "");
				toSaveSeedlingActivity.putExtra("lastDay", lastDay + "");
				toSaveSeedlingActivity.putExtra("firstSeedlingTypeId",
						firstSeedlingTypeId);
				toSaveSeedlingActivity.putExtra("validity", validity + "");
				toSaveSeedlingActivity.putExtra("addressId", nurseryId);
				toSaveSeedlingActivity.putExtra("address", address_name);
				toSaveSeedlingActivity.putExtra("contactName", contactName);
				toSaveSeedlingActivity.putExtra("contactPhone", contactPhone);
				toSaveSeedlingActivity.putExtra("isDefault", false);
				toSaveSeedlingActivity.putExtra("firstSeedlingTypeName",
						firstTypeName);
				toSaveSeedlingActivity.putExtra("seedlingParams",
						seedlingParams);
				toSaveSeedlingActivity.putExtra("diameter", diameter + "");
				toSaveSeedlingActivity.putExtra("diameterType", diameterType);
				toSaveSeedlingActivity.putExtra("dbh", dbh + "");
				toSaveSeedlingActivity.putExtra("dbhType", dbhType);
				toSaveSeedlingActivity.putExtra("height", height + "");
				toSaveSeedlingActivity.putExtra("crown", crown + "");
				toSaveSeedlingActivity.putExtra("offbarHeight", offbarHeight
						+ "");
				toSaveSeedlingActivity.putExtra("length", length + "");
				toSaveSeedlingActivity.putExtra("plantType", plantType);
				toSaveSeedlingActivity.putExtra("unitType", unitType);
				toSaveSeedlingActivity.putExtra("paramsData",
						gson.toJson(paramsDatas));
				toSaveSeedlingActivity.putExtra("remarks", remarks);
				startActivityForResult(toSaveSeedlingActivity, 1);
			}
		}

		public void savePriceAndCountAndOutline() {
			if (!"".equals(id)) {
				Intent toSavePriceAndCountAndOutlineActivity = new Intent(
						FlowerDetailActivity.this,
						SavePriceAndCountAndOutlineActivity.class);
				toSavePriceAndCountAndOutlineActivity.putExtra("id", id);
				toSavePriceAndCountAndOutlineActivity.putExtra("price", price);
				toSavePriceAndCountAndOutlineActivity.putExtra("floorPrice",
						floorPrice);
				toSavePriceAndCountAndOutlineActivity.putExtra("count", count);
				toSavePriceAndCountAndOutlineActivity.putExtra("lastDay",
						lastDay);
				startActivity(toSavePriceAndCountAndOutlineActivity);
			}
		}

		private void doBack() {
			// TODO Auto-generated method stub
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("id", id);
			finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/doBack",
					params, new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								String msg = JsonGetInfo.getJsonString(
										jsonObject, "msg");
								if (!"".equals(msg)) {
									Toast.makeText(FlowerDetailActivity.this,
											msg, Toast.LENGTH_SHORT).show();
								}
								if ("1".equals(code)) {

									initData();
								} else {

								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							super.onSuccess(t);
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method stub
							Toast.makeText(FlowerDetailActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});

		}

		private void doOutline() {
			// TODO Auto-generated method stub
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("id", id);
			finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/doOutline",
					params, new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								String msg = JsonGetInfo.getJsonString(
										jsonObject, "msg");
								if (!"".equals(msg)) {

								}
								if ("1".equals(code)) {
									Toast.makeText(FlowerDetailActivity.this,
											"下架成功", Toast.LENGTH_SHORT).show();
									initData();
								} else {

								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							super.onSuccess(t);
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method stub
							Toast.makeText(FlowerDetailActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});

		}

		/**
		 * 计时线程（防止在一定时间段内重复点击按钮）
		 */
		private class TimeThread extends Thread {
			public void run() {
				try {
					Thread.sleep(Data.loading_time);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void NoInputMethodManager() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			if (getCurrentFocus() != null) {
				if (getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(FlowerDetailActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		tv_add_car.setTextColor(Color.parseColor("#FFFFFF"));
		tv_add_car.setText("加入购物车");
		tv_add_car.setClickable(true);
	}

	public void addCart() {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null
				&& getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(FlowerDetailActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("seedlingId", id);
		params.put("count", et_num);
		finalHttp.post(GetServerUrl.getUrl() + "admin/cart/add", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {

							}
							if ("1".equals(code)) {
								Toast.makeText(FlowerDetailActivity.this,
										"已加入购物车", Toast.LENGTH_SHORT).show();
								unAddCart();
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(FlowerDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void getExtensionData() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/seedling/getExtensionData", params,
				new AjaxCallBack<Object>() {

					private int maxDays;
					private String maxCloseDate;
					private String closeDate;

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(FlowerDetailActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"),
												"extensionData");
								closeDate = JsonGetInfo.getJsonString(
										jsonObject2, "closeDate");
								maxCloseDate = JsonGetInfo.getJsonString(
										jsonObject2, "maxCloseDate");
								maxDays = JsonGetInfo.getJsonInt(jsonObject2,
										"maxDays");
								if (!"".equals(closeDate)
										&& !"".equals(maxCloseDate)) {
									EditP3 popwin = new EditP3(
											FlowerDetailActivity.this, "下架日期："
													+ closeDate + "可延期至："
													+ maxCloseDate,
											FlowerDetailActivity.this, maxDays);
									popwin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
									popwin.showAtLocation(
											mainView,
											Gravity.BOTTOM
													| Gravity.CENTER_HORIZONTAL,
											0, 0);
								}
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(FlowerDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void saveExtension() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("days", days);
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/saveExtension",
				params, new AjaxCallBack<Object>() {

					private int maxDays;
					private String maxCloseDate;
					private String closeDate;

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(FlowerDetailActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(FlowerDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			@NonNull String[] permissions, @NonNull int[] grantResults) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 200: {
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				// permission was granted, yay! Do the
				// contacts-related task you need to do.
				// 同意给与权限 可以再此处调用拍照
				Log.i("用户同意权限", "user granted the permission!");
				CallPhone(displayPhone);
			} else {

				// permission denied, boo! Disable the
				// f用户不同意 可以给一些友好的提示
				Log.i("用户不同意权限", "user denied the permission!");
			}
			return;
		}

		// other 'case' lines to check for other
		// permissions this app might request
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private void CallPhone(final String displayPhone) {
		// TODO Auto-generated method stub
		if (!"".equals(displayPhone)) {
			new AlertDialog(FlowerDetailActivity.this).builder()
					.setTitle(displayPhone)
					.setPositiveButton("呼叫", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + displayPhone));
							// Intent intent = new Intent(Intent.ACTION_DIAL,
							// Uri
							// .parse("tel:" + displayPhone));
							startActivity(intent);
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();
		}
	}

	class MyCount extends AdvancedCountdownTimer {

		public MyCount(long millisInFuture, long countDownInterval) { // 这两个参数在AdvancedCountdownTimer.java中均有(在“构造函数”中).
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			tv_last_time.setVisibility(View.GONE);
		}

		// 更新剩余时间
		@Override
		public void onTick(long millisUntilFinished, int percent) {
			int day = (int) (((millisUntilFinished / 1000) / 3600) / 24);
			int myhour = (int) (((millisUntilFinished / 1000) / 3600) % 24);
			long myminute = ((millisUntilFinished / 1000) - day * 24 * 3600 - myhour * 3600) / 60;
			long mysecond = millisUntilFinished / 1000 - myhour * 3600 - day
					* 24 * 3600 - myminute * 60;
			if (!"".equals(closeDate)) {
				tv_last_time.setText("距下架还剩下" + day + "天" + myhour + "小时"
						+ myminute + "分钟" + mysecond + "秒");
			}

		}
	}

	public abstract class AdvancedCountdownTimer {
		private static final int MSG_RUN = 1;
		private final long mCountdownInterval;// 定时间隔，以毫秒计
		private long mTotalTime;// 定时时间
		private long mRemainTime;// 剩余时间

		// 构造函数
		public AdvancedCountdownTimer(long millisInFuture,
				long countDownInterval) {
			mTotalTime = millisInFuture;
			mCountdownInterval = countDownInterval;
			mRemainTime = millisInFuture;
		}

		// 取消计时
		public final void cancel() {
			mHandler.removeMessages(MSG_RUN);
		}

		// 重新开始计时
		public final void resume() {
			mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
		}

		// 暂停计时
		public final void pause() {
			mHandler.removeMessages(MSG_RUN);
		}

		// 开始计时
		public synchronized final AdvancedCountdownTimer start() {
			if (mRemainTime <= 0) {// 计时结束后返回
				onFinish();
				return this;
			}
			// 设置计时间隔
			mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
					mCountdownInterval);
			return this;
		}

		public abstract void onTick(long millisUntilFinished, int percent); // 计时中

		public abstract void onFinish();// 计时结束

		// 通过handler更新android UI，显示定时时间
		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				synchronized (AdvancedCountdownTimer.this) {
					if (msg.what == MSG_RUN) {
						mRemainTime = mRemainTime - mCountdownInterval;

						if (mRemainTime <= 0) {
							onFinish();
						} else if (mRemainTime < mCountdownInterval) {
							sendMessageDelayed(obtainMessage(MSG_RUN),
									mRemainTime);
						} else {

							onTick(mRemainTime,
									new Long(100 * (mTotalTime - mRemainTime)
											/ mTotalTime).intValue());

							sendMessageDelayed(obtainMessage(MSG_RUN),
									mCountdownInterval);
						}
					}
				}
			}
		};
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		initData();
		super.onActivityResult(arg0, arg1, arg2);
	}

}
