package com.hldj.hmyg.buyer;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.hldj.hmyg.GalleryImageActivity;
import com.hldj.hmyg.LoginActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hldj.hmyg.bean.SeedlingParm;
import com.hldj.hmyg.saler.UpdataImageActivity;
import com.hldj.hmyg.saler.purchase.PurchaseSeedlingParmAdapter;
import com.hldj.hmyg.saler.purchase.StoreDeteilDialog;
import com.hldj.hmyg.saler.purchase.WebViewDialog;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mrwujay.cascade.activity.BaseThirdActivity;
import com.mrwujay.cascade.activity.GetCitiyNameByCode;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

@SuppressLint("NewApi")
public class PurchaseDetailActivity extends BaseThirdActivity implements
		OnGeocodeSearchListener, OnWheelChangedListener, LocationSource,
		AMapLocationListener, OnClickListener {

	private ImageView btn_back;
	private String id = "";
	private String tag = "";
	private String url = "";
	private TextView tv_01;
	private TextView tv_ac;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	private TextView tv_07;
	private TextView tv_08;
	private TextView tv_09;
	private TextView tv_10;
	private TextView tv_11;
	private TextView tv_caozuo01;
	private TextView tv_caozuo02;
	private TextView tv_caozuo03;
	private TextView tv_user_01;
	private TextView tv_user_ac;
	private TextView tv_user_03;
	private TextView tv_user_06;
	private TextView tv_user_08;
	private TextView tv_user_10;
	private TextView tv_user_11;
	private TextView tv_user_09;
	private TextView tv_user_07;
	private TextView tv_user_05;
	private TextView tv_user_04;
	private TextView tv_user_02;
	private TextView tv_seller_quote_json_02;
	private TextView tv_seller_quote_json_03;
	private TextView tv_seller_quote_json_04;
	private TextView tv_seller_quote_json_05;
	private TextView tv_seller_quote_json_06;
	private TextView tv_seller_quote_json_07;
	private TextView tv_seller_quote_json_08;
	private TextView tv_seller_quote_json_09;

	private TextView tv_seller_quote_json_caozuo01;
	private TextView tv_seller_quote_json_caozuo02;
	private TextView tv_seller_quote_json_caozuo03;
	private LinearLayout ll_user;
	private LinearLayout ll_seller_quote;
	private LinearLayout ll_login;
	private LinearLayout ll_save;
	private LinearLayout ll_save_quote;
	private LinearLayout ll_05;
	private LinearLayout ll_07;
	private TextView tv_address;
	private TextView tv_needInvoice;
	private EditText et_price;
	private EditText et_num;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private boolean needInvoice;
	private String purchaseItemId = "";
	private String purchaseId = "";
	private EditText et_shuoming;
	private String status = "";

	private double price = 0;
	private int counts = 0;
	private String remarks = "";
	private String cityName = "";
	private String sellerQuoteJson_id = "";
	private String plantType = "";
	ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private KProgressHUD hud;

	private double latitude = 0.0;
	private double longitude = 0.0;
	private boolean canQuote = false;

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
	private String cityCode = "";
	private ArrayList<String> planttype_names = new ArrayList<String>();
	private ArrayList<String> planttype_ids = new ArrayList<String>();
	private boolean isQuoted;
	private Gson gson;
	private ArrayList<Pic> ossImagePaths = new ArrayList<Pic>();
	ArrayList<SeedlingParm> msSeedlingParms = new ArrayList<SeedlingParm>();
	private String quoteDesc;
	private String num = "";
	private String purchaseFormId = "";
	private ArrayList<String> buyerDatas = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puchase_detail);
		gson = new Gson();
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);
		getAddress(latLonPoint);// 暂不用
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		hud = KProgressHUD.create(PurchaseDetailActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		LinearLayout ll_caozuo = (LinearLayout) findViewById(R.id.ll_caozuo);
		ll_user = (LinearLayout) findViewById(R.id.ll_user);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		ll_seller_quote = (LinearLayout) findViewById(R.id.ll_seller_quote);
		ll_save_quote = (LinearLayout) findViewById(R.id.ll_save_quote);
		ll_canQuote = (LinearLayout) findViewById(R.id.ll_canQuote);
		Button save = (Button) findViewById(R.id.save);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		tv_pics = (TextView) findViewById(R.id.tv_pics);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_TypeName = (TextView) findViewById(R.id.tv_TypeName);
		tv_needInvoice = (TextView) findViewById(R.id.tv_needInvoice);
		et_price = (EditText) findViewById(R.id.et_price);
		et_num = (EditText) findViewById(R.id.et_num);
		et_num.addTextChangedListener(mTextWatcher);
		et_shuoming = (EditText) findViewById(R.id.et_shuoming);
		ll_caozuo.setVisibility(View.GONE);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_ac = (TextView) findViewById(R.id.tv_ac);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		tv_07 = (TextView) findViewById(R.id.tv_07);
		tv_08 = (TextView) findViewById(R.id.tv_08);
		tv_09 = (TextView) findViewById(R.id.tv_09);
		tv_10 = (TextView) findViewById(R.id.tv_10);
		tv_11 = (TextView) findViewById(R.id.tv_11);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		id_tv_edit_all.setVisibility(View.GONE);
		tv_user_01 = (TextView) findViewById(R.id.tv_user_01);
		tv_user_ac = (TextView) findViewById(R.id.tv_user_ac);
		tv_user_02 = (TextView) findViewById(R.id.tv_user_02);
		tv_user_03 = (TextView) findViewById(R.id.tv_user_03);
		tv_user_04 = (TextView) findViewById(R.id.tv_user_04);
		tv_user_05 = (TextView) findViewById(R.id.tv_user_05);
		tv_user_06 = (TextView) findViewById(R.id.tv_user_06);
		tv_user_07 = (TextView) findViewById(R.id.tv_user_07);
		tv_user_08 = (TextView) findViewById(R.id.tv_user_08);
		tv_user_09 = (TextView) findViewById(R.id.tv_user_09);
		tv_user_10 = (TextView) findViewById(R.id.tv_user_10);
		tv_user_11 = (TextView) findViewById(R.id.tv_user_11);

		mFlowLayout3 = (TagFlowLayout) findViewById(R.id.id_flowlayout3);
		tv_new_v_00 = (TextView) findViewById(R.id.tv_new_v_00);
		tv_new_v_01 = (TextView) findViewById(R.id.tv_new_v_01);
		tv_new_v_02 = (TextView) findViewById(R.id.tv_new_v_02);
		tv_new_v_03 = (TextView) findViewById(R.id.tv_new_v_03);
		tv_new_v_04 = (TextView) findViewById(R.id.tv_new_v_04);
		tv_new_v_05 = (TextView) findViewById(R.id.tv_new_v_05);
		tv_new_v_06 = (TextView) findViewById(R.id.tv_new_v_06);
		tv_new_v_07 = (TextView) findViewById(R.id.tv_new_v_07);

		tv_caozuo01 = (TextView) findViewById(R.id.tv_caozuo01);
		tv_caozuo02 = (TextView) findViewById(R.id.tv_caozuo02);
		tv_caozuo03 = (TextView) findViewById(R.id.tv_caozuo03);

		tv_seller_quote_json_02 = (TextView) findViewById(R.id.tv_seller_quote_json_02);
		tv_seller_quote_json_03 = (TextView) findViewById(R.id.tv_seller_quote_json_03);
		tv_seller_quote_json_04 = (TextView) findViewById(R.id.tv_seller_quote_json_04);
		tv_seller_quote_json_05 = (TextView) findViewById(R.id.tv_seller_quote_json_05);
		tv_seller_quote_json_06 = (TextView) findViewById(R.id.tv_seller_quote_json_06);
		tv_seller_quote_json_07 = (TextView) findViewById(R.id.tv_seller_quote_json_07);
		tv_seller_quote_json_08 = (TextView) findViewById(R.id.tv_seller_quote_json_08);
		tv_seller_quote_json_09 = (TextView) findViewById(R.id.tv_seller_quote_json_09);
		tv_seller_quote_json_10 = (TextView) findViewById(R.id.tv_seller_quote_json_10);
		iv_baojia_chakantupian = (ImageView) findViewById(R.id.iv_baojia_chakantupian);
		tv_seller_quote_json_caozuo01 = (TextView) findViewById(R.id.tv_seller_quote_json_caozuo01);
		tv_seller_quote_json_caozuo02 = (TextView) findViewById(R.id.tv_seller_quote_json_caozuo02);
		tv_seller_quote_json_caozuo03 = (TextView) findViewById(R.id.tv_seller_quote_json_caozuo03);

		tv_closeDate = (TextView) findViewById(R.id.tv_closeDate);
		info_01 = (TextView) findViewById(R.id.info_01);
		info_02 = (TextView) findViewById(R.id.info_02);
		gv_seedlingparms = (GridView) findViewById(R.id.gv_seedlingparms);
		initSearch();
		hasShowDialog = getIntent().getBooleanExtra("hasShowDialog", false);
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			if (getIntent().getStringExtra("tag") != null) {
				tag = getIntent().getStringExtra("tag");
			}
			initData();
		}
		if (MyApplication.Userinfo.getBoolean("isLogin", false) == true) {
			ll_login.setVisibility(View.GONE);
		}

		btn_back.setOnClickListener(this);
		ll_login.setOnClickListener(this);
		ll_save.setOnClickListener(this);
		ll_01.setOnClickListener(this);
		ll_05.setOnClickListener(this);
		ll_06.setOnClickListener(this);
		ll_07.setOnClickListener(this);
		tv_seller_quote_json_caozuo02.setOnClickListener(this);
		tv_seller_quote_json_caozuo03.setOnClickListener(this);
		iv_baojia_chakantupian.setOnClickListener(this);
		save.setOnClickListener(this);

	}

	private void initSearch() {

		// planttype_names.add("不限");
		planttype_names.add("地栽苗");
		planttype_names.add("移植苗");
		planttype_names.add("假植苗");
		planttype_names.add("容器苗");
		// planttype_ids.add("");
		planttype_ids.add("planted");
		planttype_ids.add("transplant");
		planttype_ids.add("heelin");
		planttype_ids.add("container");
		if (planttype_names.size() > 0) {

			adapter3 = new com.zhy.view.flowlayout.TagAdapter<String>(
					planttype_names) {

				@Override
				public View getView(FlowLayout parent, int position, String s) {
					TextView tv = (TextView) getLayoutInflater().inflate(
							R.layout.tv, mFlowLayout3, false);
					tv.setText(s);
					return tv;
				}
			};
			mFlowLayout3.setAdapter(adapter3);
			mFlowLayout3
					.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
						@Override
						public boolean onTagClick(View view, int position,
								FlowLayout parent) {
							plantType = planttype_ids.get(position);
							return true;
						}
					});
		}
	}

	// 在一开始声明TextWatcher，在afterTextChange内操作
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String text = s.toString();
			int len = s.toString().length();
			if (len == 1 && text.equals("0")) {
				s.clear();
			}
		}

	};
	private LinearLayout ll_01;
	private TextView tv_TypeName;
	private LinearLayout ll_06;
	private TextView tv_pics;
	private TextView tv_new_v_00;
	private TextView tv_new_v_01;
	private TextView tv_new_v_02;
	private TextView tv_new_v_03;
	private TextView tv_new_v_04;
	private TextView tv_new_v_05;
	private TextView tv_new_v_06;
	private TextView tv_new_v_07;
	private TagAdapter adapter3;
	private TagFlowLayout mFlowLayout3;
	private LinearLayout ll_canQuote;
	private TextView tv_seller_quote_json_10;
	private ImageView iv_baojia_chakantupian;
	private TextView tv_closeDate;
	private TextView info_01;
	private TextView info_02;
	private GridView gv_seedlingparms;
	private TextView id_tv_edit_all;
	private boolean hasShowDialog;

	private void initData() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("userId", MyApplication.Userinfo.getString("id", ""));
		if ("Quote".equals(tag)) {
			url = "admin/quote/detail";
		} else {
			url = "purchase/itemDetail";
		}
		Log.e("id", id);
		Log.e("userId", MyApplication.Userinfo.getString("id", ""));
		finalHttp.post(GetServerUrl.getUrl() + url, params,
				new AjaxCallBack<Object>() {

					private PurchaseSeedlingParmAdapter myadapter;

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
								JSONObject item = JsonGetInfo.getJSONObject(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "item");
								canQuote = JsonGetInfo.getJsonBoolean(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "canQuote");
								JSONObject buyer = JsonGetInfo.getJSONObject(
										item, "buyer");
								JSONObject purchaseJson = JsonGetInfo
										.getJSONObject(item, "purchaseJson");
								JSONObject ciCity = JsonGetInfo.getJSONObject(
										purchaseJson, "ciCity");
								String displayName = JsonGetInfo.getJsonString(
										buyer, "displayName");
								String displayPhone = JsonGetInfo
										.getJsonString(buyer, "displayPhone");
								String fullName = JsonGetInfo.getJsonString(
										ciCity, "fullName");
								String cityName = JsonGetInfo.getJsonString(
										purchaseJson, "cityName");
								num = JsonGetInfo.getJsonString(purchaseJson,
										"num");
								purchaseFormId = JsonGetInfo.getJsonString(
										purchaseJson, "id");
								if ("".equals(num) && "".equals(purchaseFormId)) {
									id_tv_edit_all.setVisibility(View.GONE);
								} else {
									id_tv_edit_all.setVisibility(View.GONE);
									id_tv_edit_all
											.setOnClickListener(PurchaseDetailActivity.this);
								}
								boolean needInvoice = JsonGetInfo
										.getJsonBoolean(purchaseJson,
												"needInvoice");
								String receiptDate = JsonGetInfo.getJsonString(
										purchaseJson, "receiptDate");
								String closeDate = JsonGetInfo.getJsonString(
										purchaseJson, "closeDate");
								quoteDesc = JsonGetInfo.getJsonString(
										purchaseJson, "quoteDesc");
								info_02.setOnClickListener(PurchaseDetailActivity.this);
								if (!"".equals(quoteDesc) && !hasShowDialog) {
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											showWebViewDialog();
										}
									}, 1500);

								}

								purchaseId = JsonGetInfo.getJsonString(
										purchaseJson, "id");
								String remarks = JsonGetInfo.getJsonString(
										item, "remarks");

								@SuppressWarnings("unused")
								String createDate = JsonGetInfo.getJsonString(
										item, "createDate");
								String name = JsonGetInfo.getJsonString(item,
										"name");
								@SuppressWarnings("unused")
								String seedlingParams = JsonGetInfo
										.getJsonString(item, "seedlingParams");
								@SuppressWarnings("unused")
								String firstSeedlingTypeId = JsonGetInfo
										.getJsonString(item,
												"firstSeedlingTypeId");
								String firstTypeName = JsonGetInfo
										.getJsonString(item, "firstTypeName");
								String unitTypeName = JsonGetInfo
										.getJsonString(item, "unitTypeName");
								String plantTypeName = JsonGetInfo
										.getJsonString(item, "plantTypeName");
								String diameterType = JsonGetInfo
										.getJsonString(item, "diameterType");
								String dbhType = JsonGetInfo.getJsonString(
										item, "dbhType");
								String plantType = JsonGetInfo.getJsonString(
										item, "plantType");
								String unitType = JsonGetInfo.getJsonString(
										item, "unitType");
								String prCode = JsonGetInfo.getJsonString(item,
										"prCode");
								String ciCode = JsonGetInfo.getJsonString(item,
										"ciCode");
								String coCode = JsonGetInfo.getJsonString(item,
										"coCode");
								String twCode = JsonGetInfo.getJsonString(item,
										"twCode");

								String specText = JsonGetInfo.getJsonString(
										item, "specText");

								int diameter = JsonGetInfo.getJsonInt(item,
										"diameter");
								int dbh = JsonGetInfo.getJsonInt(item, "dbh");
								int height = JsonGetInfo.getJsonInt(item,
										"height");
								int crown = JsonGetInfo.getJsonInt(item,
										"crown");
								int offbarHeight = JsonGetInfo.getJsonInt(item,
										"offbarHeight");
								int length = JsonGetInfo.getJsonInt(item,
										"length");
								int count = JsonGetInfo.getJsonInt(item,
										"count");
								int quoteCountJson = JsonGetInfo.getJsonInt(
										item, "quoteCountJson");

								tv_01.setText(name);
								tv_ac.setText(count + unitTypeName);
								tv_02.setText("采购单：" + num);
								tv_03.setText("用苗地：" + fullName);
								tv_04.setText("期望收货日期：" + receiptDate);
								tv_closeDate.setText("截止日期：" + closeDate);
								tv_05.setText("截至日期：" + closeDate);
								tv_06.setText("分类：" + firstTypeName);
								tv_07.setText("种植类型：" + plantTypeName);
								tv_10.setText("其他要求：" + remarks);
								tv_08.setText(specText);
								// tv_08.setText("参数："
								// + ValueGetInfo.getValueString(dbh + "",
								// height + "", crown + "",
								// diameter + "", offbarHeight
								// + ""));
								tv_caozuo01.setText("已有：" + quoteCountJson
										+ "条报价");

								tv_new_v_01.setText("[" + firstTypeName + "]"
										+ name);
								// tv_new_v_02.setText(ValueGetInfo
								// .getValueString(dbh + "", height + "",
								// crown + "", diameter + "",
								// offbarHeight + ""));
								tv_new_v_02.setText(specText);
								tv_new_v_03.setText(count + unitTypeName);
								tv_new_v_04.setText(cityName);
								tv_new_v_05.setText(plantTypeName);
								tv_new_v_06.setText(receiptDate);
								Log.e(specText, specText + remarks);
								if (!"".equals(specText) && !"".equals(remarks)) {
									tv_new_v_07.setText(specText + ";"
											+ remarks);
								} else if (!"".equals(remarks)) {
									tv_new_v_07.setText(remarks);
								} else if (!"".equals(specText)) {
									tv_new_v_07.setText(specText);
								} else {
									tv_new_v_07.setText("-");
								}

								JSONArray specList = JsonGetInfo.getJsonArray(
										item, "specList");
								if (specList.length() > 0) {
									msSeedlingParms.clear();
									if (myadapter != null) {
										myadapter.notify(msSeedlingParms);
									}

									msSeedlingParms.add(new SeedlingParm(
											"种植类型", plantTypeName));
									for (int i = 0; i < specList.length(); i++) {
										if (!"".equals(JsonGetInfo
												.getJsonString(specList
														.getJSONObject(i),
														"value"))) {
											msSeedlingParms.add(new SeedlingParm(
													JsonGetInfo.getJsonString(
															specList.getJSONObject(i),
															"name"),
													JsonGetInfo.getJsonString(
															specList.getJSONObject(i),
															"value")));
										}

									}
									if (msSeedlingParms.size() > 0) {
										myadapter = new PurchaseSeedlingParmAdapter(
												PurchaseDetailActivity.this,
												msSeedlingParms);
										gv_seedlingparms.setAdapter(myadapter);
									}
								}

								JSONObject sellerQuoteJson = JsonGetInfo
										.getJSONObject(item, "sellerQuoteJson");
								JSONArray jsonArray = JsonGetInfo.getJsonArray(
										sellerQuoteJson, "imagesJson");
								ossImagePaths.clear();
								if (jsonArray.length() > 0) {
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										ossImagePaths.add(new Pic(JsonGetInfo
												.getJsonString(jsonObject3,
														"id"), false,
												JsonGetInfo.getJsonString(
														jsonObject3, "ossUrl"),
												0));

									}
								}
								purchaseItemId = JsonGetInfo.getJsonString(
										item, "id");
								String publicName = JsonGetInfo.getJsonString(
										buyer, "publicName");
								String realName = JsonGetInfo.getJsonString(
										buyer, "realName");
								String companyName = JsonGetInfo.getJsonString(
										buyer, "displayName");
								String publicPhone = JsonGetInfo.getJsonString(
										buyer, "displayPhone");
								String address = JsonGetInfo.getJsonString(
										buyer, "address");

								buyerDatas.clear();
								buyerDatas.add("采购商家：" + companyName);
								buyerDatas.add("所在地区：" + address);
								buyerDatas.add("采购数量：" + count);
								buyerDatas.add("已有报价：" + quoteCountJson + "条");
								info_01.setOnClickListener(PurchaseDetailActivity.this);

								if (MyApplication.Userinfo.getBoolean(
										"isLogin", false) == false) {
									buyerDatas.add("联系电话："
											+ getDisplayStr(publicPhone));
								} else {
									buyerDatas.add("联系电话：" + publicPhone);
								}

								tv_user_01.setText("采购商家信息");
								if (!"".equals(companyName)) {
									tv_user_02.setText("采购商家：" + companyName);
								} else if ("".equals(companyName)
										&& !"".equals(publicName)) {
									tv_user_02.setText("采购商家：" + publicName);
								} else if ("".equals(companyName)
										&& "".equals(publicName)) {
									tv_user_02.setText("采购商家：" + realName);
								}
								tv_user_04.setText("所在地区：" + address);
								tv_user_06.setText("采购数量：" + count);
								tv_user_07.setText("已有报价：" + quoteCountJson
										+ "条");
								if (MyApplication.Userinfo.getBoolean(
										"isLogin", false) == false) {
									tv_user_02.setText("采购商家登陆后可查看");
									tv_user_08.setText("联系电话："
											+ getDisplayStr(publicPhone));
								} else {
									ll_user.setVisibility(View.VISIBLE);
								}
								isQuoted = JsonGetInfo.getJsonBoolean(item,
										"isQuoted");

								sellerQuoteJson_id = JsonGetInfo.getJsonString(
										sellerQuoteJson, "id");
								remarks = JsonGetInfo.getJsonString(
										sellerQuoteJson, "remarks");
								mCurrentZipCode = JsonGetInfo.getJsonString(
										sellerQuoteJson, "cityCode");
								cityName = JsonGetInfo.getJsonString(
										sellerQuoteJson, "cityName");
								String sellerQuoteJson_plantTypeName = JsonGetInfo
										.getJsonString(sellerQuoteJson,
												"plantTypeName");
								needInvoice = JsonGetInfo.getJsonBoolean(
										sellerQuoteJson, "isInvoice");
								boolean isAudit = JsonGetInfo.getJsonBoolean(
										sellerQuoteJson, "isAudit");
								Log.e("sellerQuoteJson",
										sellerQuoteJson.toString());
								status = JsonGetInfo.getJsonString(
										sellerQuoteJson, "status");
								price = JsonGetInfo.getJsonDouble(
										sellerQuoteJson, "price");
								counts = JsonGetInfo.getJsonInt(
										sellerQuoteJson, "count");
								tv_seller_quote_json_02.setText("苗木报价");
								tv_seller_quote_json_03.setText("以下是您的报价记录");
								tv_seller_quote_json_04.setText("价格："
										+ ValueGetInfo.doubleTrans1(price));
								// tv_seller_quote_json_05.setText("可供数量："
								// + counts);
								tv_seller_quote_json_05.setText("种植类型："
										+ sellerQuoteJson_plantTypeName);
								tv_seller_quote_json_06.setText("苗源地址："
										+ cityName);
								tv_seller_quote_json_08.setText("报价说明："
										+ remarks);

								if ("unsubmit".equals(status)) {
									tv_seller_quote_json_caozuo02.setText("编辑");
									tv_seller_quote_json_caozuo03.setText("删除");
									tv_seller_quote_json_caozuo02
											.setVisibility(View.VISIBLE);
									tv_seller_quote_json_caozuo03
											.setVisibility(View.VISIBLE);
								} else if ("unaudit".equals(status)) {
									tv_seller_quote_json_caozuo02
											.setVisibility(View.INVISIBLE);
									tv_seller_quote_json_caozuo03.setText("撤回");
									tv_seller_quote_json_caozuo03
											.setVisibility(View.VISIBLE);
								} else if ("quoted".equals(status)) {

								} else if ("backed".equals(status)) {
									tv_seller_quote_json_caozuo02.setText("编辑");
									tv_seller_quote_json_caozuo03.setText("删除");
									tv_seller_quote_json_caozuo02
											.setVisibility(View.VISIBLE);
									tv_seller_quote_json_caozuo03
											.setVisibility(View.VISIBLE);

								} else if ("invalid".equals(status)) {
								}
								if (MyApplication.Userinfo.getBoolean(
										"isLogin", false) == false) {

								} else {
									ll_login.setVisibility(View.GONE);
									// ll_user.setVisibility(View.VISIBLE);
									ll_user.setVisibility(View.GONE);
									ll_seller_quote.setVisibility(View.GONE);
									if (isQuoted == true) {
										ll_seller_quote
												.setVisibility(View.VISIBLE);
										tv_seller_quote_json_caozuo01
												.setText("当前报价状态:已报价");
										if (ossImagePaths.size() > 0) {
											iv_baojia_chakantupian
													.setVisibility(View.VISIBLE);
											tv_seller_quote_json_10
													.setText("苗木图片"
															+ ossImagePaths
																	.size()
															+ "张");
										} else {
											iv_baojia_chakantupian
													.setVisibility(View.INVISIBLE);
											tv_seller_quote_json_10
													.setText("苗木图片:无");
										}

										tv_seller_quote_json_caozuo03
												.setText("删除");
										ll_save_quote.setVisibility(View.GONE);
										ll_save.setVisibility(View.GONE);
									} else {
										ll_seller_quote
												.setVisibility(View.GONE);
										if (canQuote) {
											ll_save_quote
													.setVisibility(View.VISIBLE);
											ll_canQuote
													.setVisibility(View.GONE);
										} else {
											ll_save_quote
													.setVisibility(View.GONE);
											// 提示
											ll_canQuote
													.setVisibility(View.VISIBLE);
										}

										ll_save.setVisibility(View.VISIBLE);
									}

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
						Toast.makeText(PurchaseDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private String getDisplayStr(String realStr) {
		String result = new String(realStr);
		char[] cs = result.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (i >= 3 && i <= 10) {// 把3和10区间的字符隐藏
				cs[i] = '*';
			}
		}
		return new String(cs);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.info_02:
			showWebViewDialog();
			break;
		case R.id.info_01:
			if (!MyApplication.Userinfo.getBoolean("isLogin", false)) {
				Intent toLoginActivity = new Intent(
						PurchaseDetailActivity.this, LoginActivity.class);
				startActivityForResult(toLoginActivity, 4);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				return;
			}
			if (buyerDatas.size() > 0) {
				StoreDeteilDialog.Builder builder = new StoreDeteilDialog.Builder(
						PurchaseDetailActivity.this);
				builder.setTitle("商家信息");
				builder.setPrice("");
				builder.setCount("");
				builder.setAccountName(quoteDesc);
				builder.setAccountBank("");
				builder.setAccountNum("");
				builder.setData(buyerDatas);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});

				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				builder.create().show();
			}

			break;
		case R.id.ll_login:
			Intent toLoginActivity = new Intent(PurchaseDetailActivity.this,
					LoginActivity.class);
			startActivityForResult(toLoginActivity, 4);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.ll_01:

			new ActionSheetDialog(PurchaseDetailActivity.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.setTitle("种植类型")
					.addSheetItem("地栽苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "planted";
									tv_TypeName.setText("地栽苗");
								}
							})
					.addSheetItem("移植苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "transplant";
									tv_TypeName.setText("移植苗");
								}
							})
					.addSheetItem("假植苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "heelin";
									tv_TypeName.setText("假植苗");

								}
							})
					.addSheetItem("容器苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "container";
									tv_TypeName.setText("容器苗");
								}
							}).show();

			break;
		case R.id.ll_05:
			showCitys();
			break;
		case R.id.ll_06:
			Intent toUpdataImageActivity = new Intent(
					PurchaseDetailActivity.this, UpdataImageActivity.class);
			Bundle bundleObject = new Bundle();
			final PicSerializableMaplist myMap = new PicSerializableMaplist();
			myMap.setMaplist(urlPaths);
			bundleObject.putSerializable("urlPaths", myMap);
			toUpdataImageActivity.putExtras(bundleObject);
			startActivityForResult(toUpdataImageActivity, 1);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.ll_07:
			new ActionSheetDialog(PurchaseDetailActivity.this)
					.builder()
					.setTitle("是否需要发票")
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("是", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									tv_needInvoice.setText("是");
									needInvoice = true;

								}
							})
					.addSheetItem("否", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									tv_needInvoice.setText("否");
									needInvoice = false;
								}
							}).show();
			break;
		case R.id.save:
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			if (imm.isActive() && getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(PurchaseDetailActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}

			// if ("".equals(plantType)) {
			// Toast.makeText(PurchaseDetailActivity.this, "请选择种植类型",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			// if ("".equals(et_num.getText().toString())) {
			// Toast.makeText(PurchaseDetailActivity.this, "请输入数量",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			if ("".equals(et_price.getText().toString())) {
				Toast.makeText(PurchaseDetailActivity.this, "请输入价格",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 隐藏关闭输入法
			// if ("".equals(GetCodeByName.initProvinceDatasDocument(
			// PurchaseDetailActivity.this, mCurrentProviceName,
			// mCurrentCityName))) {
			// Toast.makeText(PurchaseDetailActivity.this, "请选择苗源地址所在城市",
			// Toast.LENGTH_SHORT).show();
			// return;
			// }
			if (Double.parseDouble(et_price.getText().toString()) <= 0) {
				Toast.makeText(PurchaseDetailActivity.this, "请输入超过0的价格",
						Toast.LENGTH_SHORT).show();
				return;
			}

			Data.pics1.clear();
			hud.show();
			for (int i = 0; i < urlPaths.size(); i++) {
				Data.pics1.add(urlPaths.get(i));
			}
			quoteSave();

			break;
		case R.id.tv_seller_quote_json_caozuo02:
			if ("unsubmit".equals(status)) {
				quoteEdit();
			} else if ("unaudit".equals(status)) {
			} else if ("quoted".equals(status)) {
			} else if ("backed".equals(status)) {
				quoteEdit();
			} else if ("invalid".equals(status)) {
			}
			break;
		case R.id.tv_seller_quote_json_caozuo03:
			if (isQuoted) {
				// 删除
				quoteDdel();
			}

			if ("unsubmit".equals(status)) {
				// 删除
				quoteDdel();
			} else if ("unaudit".equals(status)) {
				// 撤回
				quoteRevoke();
			} else if ("quoted".equals(status)) {
				//
			} else if ("backed".equals(status)) {
				// 删除
				quoteDdel();
			} else if ("invalid".equals(status)) {
			}
			break;
		case R.id.iv_baojia_chakantupian:
			if (ossImagePaths.size() > 0) {
				GalleryImageActivity.startGalleryImageActivity(
						PurchaseDetailActivity.this, 0, ossImagePaths);

			}
			break;
		case R.id.id_tv_edit_all:
			if (!"".equals(num) && !"".equals(purchaseFormId)) {
				Intent intent = new Intent(PurchaseDetailActivity.this,
						StorePurchaseListActivity.class);
				intent.putExtra("purchaseFormId", purchaseFormId);
				intent.putExtra("title", num);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);

			}
			break;
		default:
			break;
		}
	}

	private void showWebViewDialog() {
		// TODO Auto-generated method stub
		if (quoteDesc != null) {
			WebViewDialog.Builder builder = new WebViewDialog.Builder(
					PurchaseDetailActivity.this);
			builder.setTitle("报价要求");
			builder.setPrice("");
			builder.setCount("");
			builder.setAccountName(quoteDesc);
			builder.setAccountBank("");
			builder.setAccountNum("");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
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

	}

	// 编辑
	public void quoteEdit() {
		ll_save_quote.setVisibility(View.VISIBLE);
		ll_save.setVisibility(View.VISIBLE);
		et_num.setText(counts + "");
		et_price.setText(ValueGetInfo.doubleTrans1(price));
		tv_address.setText(cityName);
		if (needInvoice) {
			tv_needInvoice.setText("提供发票");
		} else {
			tv_needInvoice.setText("不提供发票");
		}
		et_shuoming.setText(remarks);

	}

	public void quoteRevoke() {
		// TODO Auto-generated method stub
		if ("".equals(sellerQuoteJson_id)) {
			Toast.makeText(PurchaseDetailActivity.this, "error",
					Toast.LENGTH_SHORT).show();
			return;
		}
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("id", sellerQuoteJson_id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/revoke", params,
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
						Toast.makeText(PurchaseDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void quoteDdel() {
		// TODO Auto-generated method stub

		if ("".equals(sellerQuoteJson_id)) {
			Toast.makeText(PurchaseDetailActivity.this, "error",
					Toast.LENGTH_SHORT).show();
			return;
		}
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);

		AjaxParams params = new AjaxParams();
		params.put("id", sellerQuoteJson_id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/del", params,
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
						Toast.makeText(PurchaseDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void quoteSave() {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);

		AjaxParams params = new AjaxParams();
		if (!"".equals(sellerQuoteJson_id)) {
			params.put("id", sellerQuoteJson_id);
		}
		params.put("cityCode", GetCodeByName.initProvinceDatasDocument(
				PurchaseDetailActivity.this, mCurrentProviceName,
				mCurrentCityName));
		params.put("isInvoice", needInvoice + "");
		params.put("purchaseId", purchaseId);
		params.put("purchaseItemId", purchaseItemId);
		params.put("plantType", plantType);
		params.put("price", et_price.getText().toString());
		params.put("count", et_num.getText().toString());
		params.put("remarks", et_shuoming.getText().toString());
		params.put("imagesData", gson.toJson(Data.pics1));

		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/save", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						if (hud != null
								&& !PurchaseDetailActivity.this.isFinishing()) {
							hud.dismiss();
						}
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(PurchaseDetailActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								initData();
//								finish();
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
						if (hud != null
								&& !PurchaseDetailActivity.this.isFinishing()) {
							hud.dismiss();
						}
						Toast.makeText(PurchaseDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 5) {
			Bundle bundle = data.getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			tv_pics.setText(PicValiteIsUtils.notiPicValite(urlPaths));

		} else if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			initData();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showCitys() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city_for_purchase, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		setUpData();

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
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_address.setText(mCurrentProviceName + "\u0020"
						+ mCurrentCityName + "\u0020" + mCurrentDistrictName
						+ "\u0020");
				if (!PurchaseDetailActivity.this.isFinishing()
						&& dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!PurchaseDetailActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!PurchaseDetailActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				PurchaseDetailActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private void showSelectedResult() {
		Toast.makeText(
				PurchaseDetailActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
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
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
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
	public void showDialog() {
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
		// showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		// showDialog();
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
				Toast.makeText(PurchaseDetailActivity.this, R.string.no_result,
						1).show();
			}

		} else {
			Toast.makeText(PurchaseDetailActivity.this, rCode, 1).show();
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
				Toast.makeText(PurchaseDetailActivity.this, R.string.no_result,
						1).show();
			}
		} else {
			Toast.makeText(PurchaseDetailActivity.this, rCode, 1).show();
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
				String adCode = amapLocation.getAdCode();
				if (getIntent().getStringExtra("id") == null) {
					// 新增的情况
					if (!"".equals(GetCitiyNameByCode.initProvinceDatas(
							PurchaseDetailActivity.this, adCode))
							&& "".equals(mCurrentZipCode)) {
						// 已有mCurrentZipCode就不再定位动态获取
						cityCode = adCode;
						mCurrentZipCode = cityCode;
						tv_address.setText(GetCitiyNameByCode
								.initProvinceDatas(PurchaseDetailActivity.this,
										cityCode));
					}
				}
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
			}
		}
	}

}
