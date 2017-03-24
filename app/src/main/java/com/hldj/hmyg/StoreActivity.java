package com.hldj.hmyg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.hwang.library.widgit.SmartRefreshLayout;
import me.hwang.library.widgit.SmartRefreshLayout.onRefreshListener;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.google.zxing.WriterException;
import com.hldj.hmyg.adapter.ProductGridAdapter;
import com.hldj.hmyg.adapter.ProductListAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PlatformForShare;
import com.hldj.hmyg.store.StoreTypeActivity;
import com.hldj.hmyg.store.TypeEx;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.white.utils.AndroidUtil;
import com.white.utils.FileUtil;
import com.zf.iosdialog.widget.AlertDialog;
import com.zxing.encoding.EncodingHandler;
import com.zzy.flowers.widget.popwin.EditP5;

@SuppressLint("Override")
public class StoreActivity extends NeedSwipeBackActivity implements
		PlatformActionListener, onRefreshListener {

	private FinalBitmap fb;
	private ImageView iv_icon_persion_pic;
	private TextView tv_user_name;
	private TextView tv_companyName;
	private MultipleClickProcess multipleClickProcess;
	private LinearLayout ll_01;
	private LinearLayout ll_02;
	private LinearLayout ll_03;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private ImageView iv_01;
	private ImageView iv_02;
	private ImageView iv_03;
	private WebView webview;
	private TextView tv_loading;
	private ProgressBar bar;
	private String code = "";
	private LinearLayout ll_web;
	private LinearLayout ll_store_home;
	private LinearLayout ll_all_flower;
	private TextView tv_name;
	private TextView tv_user_address;
	private TextView tv_user_phone;
	private ImageView iv_banner;
	private ProductGridAdapter gridAdapter;
	private ProductGridAdapter SpecialgridAdapter;
	private ProductGridAdapter AllgridAdapter;
	private ProductListAdapter AlllistAdapter;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> reco_datas = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> Special_datas = new ArrayList<HashMap<String, Object>>();
	private GridView gd_00;
	private GridView gd_01;
	private RelativeLayout ll_store_home2;
	private RelativeLayout rl_choose_screen;
	private RelativeLayout rl_choose_price;
	private ImageView iv_seller_arrow2;
	private ImageView iv_view_type;
	private GridView gd_all;
	private ListView lv_all;

	String view_type = "list";
	private String priceSort = "";
	private String displayPhone = "";
	private String ownerId = "";
	private ImageView iv_lianxi;

	private Dialog dialog;
	private String platform = "1,2,3,4,5,6,7,8";
	private ArrayList<PlatformForShare> shares = new ArrayList<PlatformForShare>();
	private String img = "";
	private String title = "欢迎使用花木易购代购型苗木交易平台！";
	private String text = "苗木交易原来可以如此简单,配上花木易购APP,指尖轻点,交易无忧。";
	private String url = Data.share;
	private String img_path = "";
	private LinearLayout mainView;
	public String plantTypes = "";
	public String firstSeedlingTypeId = "";
	public String secondSeedlingTypeId = "";
	private JSONArray typeList;
	public TextView tv_plant_type;
	public TextView tv_type;
	private PopupWindow pwMyPopWindow;
	private ImageView iv_fenxiang;
	private ImageView iv_erweima;
	private KProgressHUD hud;
	private int SHARE_TYPE = 2; // 1表示二维码分享 2表示网页分享
	private ListView lvPopupList;
	private int NUM_OF_VISIBLE_LIST_ROWS = 5;// 指定popupwindow中Item的数量
	private ArrayList<Pic> ossImagePaths = new ArrayList<Pic>();
	private int pageSize = 10;
	private int pageIndex = 0;
	private SmartRefreshLayout mLayout;
	private TypeEx typeEx;
	private LinearLayout ll_plant_type;
	private LinearLayout ll_type;
	private TextView tv_store_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		typeEx = new TypeEx(-1, -1, "", secondSeedlingTypeId);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		fb = FinalBitmap.create(this);
		fb.configLoadingImage(R.drawable.no_image_show);
		hud = KProgressHUD.create(StoreActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("努力加载中...").setMaxProgress(100).setCancellable(true);
		if (platform.contains("2")) {
			PlatformForShare platformForShare = new PlatformForShare("分享微信",
					"Wechat", "2", R.drawable.fenxiang_weixin);
			shares.add(platformForShare);
		}
		if (platform.contains("1")) {
			PlatformForShare platformForShare = new PlatformForShare("分享朋友圈",
					"WechatMoments", "1", R.drawable.fenxiang_pengyouquan);
			shares.add(platformForShare);
		}
		// if (platform.contains("3")) {
		// PlatformForShare platformForShare = new PlatformForShare("新浪微博",
		// "SinaWeibo", "3", R.drawable.sns_icon_1);
		// shares.add(platformForShare);
		// }
		if (platform.contains("4")) {
			PlatformForShare platformForShare = new PlatformForShare("分享qq",
					"QZone", "4", R.drawable.fenxiang_qq);
			shares.add(platformForShare);
		}
		code = getIntent().getStringExtra("code");
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		iv_fenxiang = (ImageView) findViewById(R.id.iv_fenxiang);
		iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
		iv_lianxi = (ImageView) findViewById(R.id.iv_lianxi);
		iv_banner = (ImageView) findViewById(R.id.iv_banner);
		LayoutParams l_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		WindowManager wm = this.getWindowManager();
		l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 1 / 2);
		iv_banner.setLayoutParams(l_params);
		tv_store_name = (TextView) findViewById(R.id.tv_store_name);
		tv_plant_type = (TextView) findViewById(R.id.tv_plant_type);
		tv_type = (TextView) findViewById(R.id.tv_type);
		ll_plant_type = (LinearLayout) findViewById(R.id.ll_plant_type);
		ll_type = (LinearLayout) findViewById(R.id.ll_type);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_user_address = (TextView) findViewById(R.id.tv_user_address);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_companyName = (TextView) findViewById(R.id.tv_companyName);
		tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
		iv_icon_persion_pic = (ImageView) findViewById(R.id.iv_icon_persion_pic);
		mainView = (LinearLayout) findViewById(R.id.mainView);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		gd_00 = (GridView) findViewById(R.id.gd_00);
		gd_01 = (GridView) findViewById(R.id.gd_01);
		gd_all = (GridView) findViewById(R.id.gd_all);
		lv_all = (ListView) findViewById(R.id.lv_all);
		mLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh_layout);
		mLayout.setOnRefreshListener(this);
		ll_web = (LinearLayout) findViewById(R.id.ll_web);
		ll_store_home = (LinearLayout) findViewById(R.id.ll_store_home);
		ll_all_flower = (LinearLayout) findViewById(R.id.ll_all_flower);

		ll_store_home2 = (RelativeLayout) findViewById(R.id.rl_choose_type);
		rl_choose_screen = (RelativeLayout) findViewById(R.id.rl_choose_screen);
		rl_choose_price = (RelativeLayout) findViewById(R.id.rl_choose_price);
		iv_seller_arrow2 = (ImageView) findViewById(R.id.iv_seller_arrow2);
		iv_view_type = (ImageView) findViewById(R.id.iv_view_type);

		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		iv_01 = (ImageView) findViewById(R.id.iv_01);
		iv_02 = (ImageView) findViewById(R.id.iv_02);
		iv_03 = (ImageView) findViewById(R.id.iv_03);
		webview = (WebView) findViewById(R.id.webview);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		bar = (ProgressBar) findViewById(R.id.progressBar);
		// webview.setHorizontalScrollBarEnabled(false);// 水平不显示
		// webview.setVerticalScrollBarEnabled(false); // 垂直不显示
		WebSettings s = webview.getSettings();
		s.setBuiltInZoomControls(true);
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);

		// enable navigator.geolocation
		s.setGeolocationEnabled(true);
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

		// enable Web Storage: localStorage, sessionStorage
		s.setDomStorageEnabled(true);
		webview.setWebViewClient(new MyWebViewClient());
		webview.setWebChromeClient(new MyWebChromeClient());

		storeIndex(code);

		init00();
		init02();

		iniPopupWindow();
		iniPopupWindow2();

		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);

		ll_store_home2.setOnClickListener(multipleClickProcess);
		rl_choose_screen.setOnClickListener(multipleClickProcess);
		rl_choose_price.setOnClickListener(multipleClickProcess);
		iv_view_type.setOnClickListener(multipleClickProcess);

	}

	private void iniPopupWindow2() {

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout, AndroidUtil.dip2px(
				StoreActivity.this, 130), LayoutParams.WRAP_CONTENT);
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件
		SharePoPAdapter sharePoPAdapter = new SharePoPAdapter(
				StoreActivity.this, shares);
		lvPopupList.setAdapter(sharePoPAdapter);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// 关闭
				}
				if ("1".equals(shares.get(position).getId())) {
					ShareToWechatMoments();
				} else if ("2".equals(shares.get(position).getId())) {
					ShareToWechat();
				} else if ("4".equals(shares.get(position).getId())) {
					ShareToQzone();
				}

			}
		});

		// // 控制popupwindow的宽度和高度自适应
		// lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
		// View.MeasureSpec.UNSPECIFIED);
		// pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
		// pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight())
		// * NUM_OF_VISIBLE_LIST_ROWS + 20);
		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
	}

	class SharePoPAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<PlatformForShare> shares;
		private ImageView icon;

		public SharePoPAdapter(Context context,
				ArrayList<PlatformForShare> shares) {
			this.context = context;
			this.shares = shares;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shares.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View inflate = getLayoutInflater().inflate(
					R.layout.list_item_pop_share, null);
			TextView tv_list_item = (TextView) inflate
					.findViewById(R.id.tv_list_item);
			icon = (ImageView) inflate.findViewById(R.id.icon);
			tv_list_item.setText(shares.get(position).getName());
			icon.setImageResource(shares.get(position).getPic());
			return inflate;
		}
	}

	private void iniPopupWindow() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.store_share_popupwindow, null);
		LinearLayout ll_share_erweima = (LinearLayout) layout
				.findViewById(R.id.ll_share_erweima);
		LinearLayout ll_share_lianjie = (LinearLayout) layout
				.findViewById(R.id.ll_share_lianjie);
		pwMyPopWindow = new PopupWindow(layout);
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件
		// 控制popupwindow的宽度和高度自适应
		pwMyPopWindow.setWidth(LayoutParams.WRAP_CONTENT);
		pwMyPopWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.share_dianjiu_bg));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		ll_share_erweima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SHARE_TYPE = 1;
				showDialog();
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// 关闭
				}

			}
		});
		ll_share_lianjie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SHARE_TYPE = 2;
				showDialog();
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// 关闭
				}

			}
		});

	}

	private void storeIndex(String code2) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", code2);
		Log.e("authc", MyApplication.Userinfo.getString("id", ""));
		Log.e("id", code2);
		finalHttp.post(GetServerUrl.getUrl() + "store/index", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						hud.show();
						super.onStart();
					}

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							JSONObject data = JsonGetInfo.getJSONObject(
									jsonObject, "data");
							String codes = JsonGetInfo.getJsonString(
									jsonObject, "code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							JSONObject store = JsonGetInfo.getJSONObject(data,
									"store");
							JSONObject owner = JsonGetInfo.getJSONObject(data,
									"owner");
							JSONObject ciCity = JsonGetInfo.getJSONObject(
									owner, "ciCity");
							JSONArray recommendList = JsonGetInfo.getJsonArray(
									data, "recommendList");
							typeList = JsonGetInfo.getJsonArray(data,
									"typeList");
							if (!"".equals(msg)) {
							}
							if ("1".equals(codes)) {
								String name = JsonGetInfo.getJsonString(store,
										"name");
								tv_store_name.setText(name);
								ownerId = JsonGetInfo.getJsonString(store,
										"ownerId");
								String id = JsonGetInfo.getJsonString(store,
										"id");
								if (!"".equals(id)) {
									if (GetServerUrl.isTest) {
										webview.loadUrl(Data.Store_Page + id
												+ ".html");
									} else {
										webview.loadUrl(Data.Store_Page3 + id
												+ ".html");
									}

								}

								String appBannerUrl = JsonGetInfo
										.getJsonString(store, "appBannerUrl");
								String logoUrl = JsonGetInfo.getJsonString(
										store, "logoUrl");
								String shareTitle = JsonGetInfo.getJsonString(
										store, "shareTitle");
								String shareContent = JsonGetInfo
										.getJsonString(store, "shareContent");
								String shareUrl = JsonGetInfo.getJsonString(
										store, "shareUrl");
								title = shareTitle;
								text = shareContent;
								url = shareUrl;

								String address = JsonGetInfo.getJsonString(
										owner, "address");
								String cityName = JsonGetInfo.getJsonString(
										owner, "cityName");
								String companyName = JsonGetInfo.getJsonString(
										owner, "companyName");
								String displayName = JsonGetInfo.getJsonString(
										owner, "publicName");
								String realName = JsonGetInfo.getJsonString(
										owner, "realName");
								displayPhone = JsonGetInfo.getJsonString(owner,
										"displayPhone");
								if (!"".equals(appBannerUrl)) {
									fb.display(iv_banner, appBannerUrl);
								}
								if (!"".equals(logoUrl)) {
									// fb.display(iv_icon_persion_pic, logoUrl);
									fb.display(iv_icon_persion_pic, JsonGetInfo
											.getJsonString(owner, "headImage"));
								}

								tv_name.setText(name);
								if ("".equals(cityName) && "".equals(address)) {
									tv_user_address.setText(JsonGetInfo
											.getJsonString(ciCity, "fullName"));
								} else {
									tv_user_address.setText(cityName + address);
								}

								if ("".equals(displayName)) {
									tv_user_name.setText(realName);
								} else {
									tv_user_name.setText(displayName);
								}
								tv_user_phone.setText(displayPhone);
								tv_companyName.setText(companyName);
								if (!"".equals(displayPhone)) {
									iv_lianxi
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View arg0) {
													// TODO Auto-generated
													// method stub
													boolean requesCallPhonePermissions = new PermissionUtils(
															StoreActivity.this)
															.requesCallPhonePermissions(200);
													if (requesCallPhonePermissions) {
														CallPhone(displayPhone);
													}

												}
											});
								}

								if (recommendList.length() > 0) {
									for (int i = 0; i < recommendList.length(); i++) {
										JSONObject jsonObject3 = recommendList
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("show_type", "seedling_list");
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										hMap.put("specText", JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"name"));
										hMap.put("plantType", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.put("isSelfSupport", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSelfSupportJson")); // 自营
										hMap.put("freeValidatePrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeValidatePrice")); // 返验苗费
										hMap.put("cashOnDelivery", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"cashOnDelivery")); // 担
																			// 资金担保
										hMap.put("freeDeliveryPrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeDeliveryPrice"));// 免发货费
										hMap.put(
												"tagList",
												JsonGetInfo.getJsonArray(
														jsonObject3, "tagList")
														.toString());//
										hMap.put(
												"paramsList",
												JsonGetInfo.getJsonArray(
														jsonObject3,
														"paramsList")
														.toString());//
										hMap.put("freeValidate", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"freeValidate")); // 免验苗
										hMap.put("isRecommend", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isRecommend")); // 推荐
										hMap.put("isSpecial", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSpecial")); // 清场

										hMap.put("imageUrl", JsonGetInfo
												.getJsonString(jsonObject3,
														"mediumImageUrl"));

										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
										hMap.put("diameter", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"diameter"));
										hMap.put("dbh", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"dbh"));
										hMap.put("height", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"height"));
										hMap.put("crown", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"crown"));
										hMap.put("offbarHeight", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"offbarHeight"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"fullName"));
										hMap.put("ciCity_name", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"name"));
										hMap.put("realName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"realName"));
										hMap.put("companyName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"companyName"));
										hMap.put("publicName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"publicName"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										reco_datas.add(hMap);
										if (gridAdapter != null) {
											gridAdapter.notifyDataSetChanged();
										}
									}

									if (gridAdapter == null) {
										gridAdapter = new ProductGridAdapter(
												StoreActivity.this, reco_datas);
										gd_00.setAdapter(gridAdapter);
									}
								}
								try {
									Bitmap qrCodeBitmap = EncodingHandler
											.createQRCode(url, 350);
									try {
										img_path = FileUtil.saveMyBitmap(
												System.currentTimeMillis() + "",
												qrCodeBitmap);
										if (!"".equals(img_path)) {
											ossImagePaths.add(new Pic("",
													false, img_path, 0));
										}

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} catch (WriterException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								iv_fenxiang
										.setOnClickListener(multipleClickProcess);
								iv_erweima
										.setOnClickListener(multipleClickProcess);
								// seedlingListisSpecial();
								seedlingList();

							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
						if (hud != null) {
							hud.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(StoreActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
						if (hud != null) {
							hud.dismiss();
						}
					}

				});

	}

	public void seedlingList() {
		Log.e("secondSeedlingTypeId", secondSeedlingTypeId);
		// getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("ownerId", ownerId);
		params.put("orderBy", priceSort);
		if (plantTypes.length() > 0) {
			params.put("plantTypes",
					plantTypes.substring(0, plantTypes.length() - 1));
		} else {
			params.put("plantTypes", plantTypes);
		}
		// params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("secondSeedlingTypeId", secondSeedlingTypeId);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "seedling/list", params,
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("show_type", "seedling_list");
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										hMap.put("specText", JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"name"));
										hMap.put("plantType", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.put("isSelfSupport", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSelfSupportJson")); // 自营
										hMap.put("freeValidatePrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeValidatePrice")); // 返验苗费
										hMap.put("cashOnDelivery", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"cashOnDelivery")); // 担
										// 资金担保
										hMap.put("freeDeliveryPrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeDeliveryPrice"));// 免发货费
										hMap.put(
												"tagList",
												JsonGetInfo.getJsonArray(
														jsonObject3, "tagList")
														.toString());//
										hMap.put(
												"paramsList",
												JsonGetInfo.getJsonArray(
														jsonObject3,
														"paramsList")
														.toString());//
										hMap.put("freeValidate", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"freeValidate")); // 免验苗
										hMap.put("isRecommend", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isRecommend")); // 推荐
										hMap.put("isSpecial", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSpecial")); // 清场

										hMap.put("imageUrl", JsonGetInfo
												.getJsonString(jsonObject3,
														"largeImageUrl"));

										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
										hMap.put("diameter", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"diameter"));
										hMap.put("dbh", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"dbh"));
										hMap.put("height", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"height"));
										hMap.put("crown", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"crown"));
										hMap.put("offbarHeight", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"offbarHeight"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"fullName"));
										hMap.put("ciCity_name", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"name"));
										hMap.put("realName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"realName"));
										hMap.put("companyName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"companyName"));
										hMap.put("publicName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"publicName"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										datas.add(hMap);
										// if (AllgridAdapter != null) {
										// AllgridAdapter
										// .notifyDataSetChanged();
										// }
										// if (AlllistAdapter != null) {
										// AlllistAdapter
										// .notifyDataSetChanged();
										// }
									}

									if (SpecialgridAdapter == null) {
										AllgridAdapter = new ProductGridAdapter(
												StoreActivity.this, datas);
										gd_all.setAdapter(AllgridAdapter);
									} else {
										AllgridAdapter.notifyDataSetChanged();
									}
									if (AlllistAdapter == null) {
										AlllistAdapter = new ProductListAdapter(
												StoreActivity.this, datas);
										lv_all.setAdapter(AlllistAdapter);
									} else {
										AlllistAdapter.notifyDataSetChanged();
									}
									pageIndex++;

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
						Toast.makeText(StoreActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		// getdata = true;

	}

	private void seedlingListisSpecial() {
		// getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("ownerId", ownerId);
		params.put("isSpecial", true + "");
		finalHttp.post(GetServerUrl.getUrl() + "seedling/list", params,
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("show_type", "seedling_list");
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"name"));
										hMap.put("specText", JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.put("plantType", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.put("isSelfSupport", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSelfSupportJson")); // 自营
										hMap.put("freeValidatePrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeValidatePrice")); // 返验苗费
										hMap.put("cashOnDelivery", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"cashOnDelivery")); // 担
																			// 资金担保
										hMap.put("freeDeliveryPrice",
												JsonGetInfo.getJsonBoolean(
														jsonObject3,
														"freeDeliveryPrice"));// 免发货费
										hMap.put(
												"tagList",
												JsonGetInfo.getJsonArray(
														jsonObject3, "tagList")
														.toString());//
										hMap.put(
												"paramsList",
												JsonGetInfo.getJsonArray(
														jsonObject3,
														"paramsList")
														.toString());//
										hMap.put("freeValidate", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"freeValidate")); // 免验苗
										hMap.put("isRecommend", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isRecommend")); // 推荐
										hMap.put("isSpecial", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isSpecial")); // 清场

										hMap.put("imageUrl", JsonGetInfo
												.getJsonString(jsonObject3,
														"largeImageUrl"));

										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
										hMap.put("diameter", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"diameter"));
										hMap.put("dbh", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"dbh"));
										hMap.put("height", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"height"));
										hMap.put("crown", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"crown"));
										hMap.put("offbarHeight", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"offbarHeight"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"fullName"));
										hMap.put("ciCity_name", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"name"));
										hMap.put("realName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"realName"));
										hMap.put("companyName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"companyName"));
										hMap.put("publicName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"publicName"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										Special_datas.add(hMap);
										if (SpecialgridAdapter != null) {
											SpecialgridAdapter
													.notifyDataSetChanged();
										}
									}

									if (SpecialgridAdapter == null) {
										SpecialgridAdapter = new ProductGridAdapter(
												StoreActivity.this,
												Special_datas);
										gd_01.setAdapter(SpecialgridAdapter);
									}

									// pageIndex++;

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
						Toast.makeText(StoreActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		// getdata = true;

	}

	// 看不到console.log和alert
	class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Toast.makeText(StoreActivity.this, message, Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(final WebView view, String url) {
			bar.setVisibility(View.INVISIBLE);
			tv_loading.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			bar.setVisibility(View.INVISIBLE);
			tv_loading.setVisibility(View.INVISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public class MultipleClickProcess implements OnClickListener {

		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.iv_fenxiang:
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// 关闭
				} else {
					pwMyPopWindow.showAsDropDown(iv_fenxiang);// 显示
				}
				// showDialog();
				break;
			case R.id.iv_erweima:
				if (ossImagePaths.size() > 0) {
					GalleryImageActivity.startGalleryImageActivity(
							StoreActivity.this, 0, ossImagePaths);
				} else {
				}
				break;
			case R.id.ll_01:
				init00();
				init01();
				break;
			case R.id.ll_02:
				init00();
				init02();
				break;
			case R.id.ll_03:
				init00();
				init03();
				break;

			case R.id.iv_view_type:
				if ("grid".equals(view_type)) {
					// gridview
					view_type = "list";
					iv_view_type.setImageResource(R.drawable.icon_list_view);
					gd_all.setVisibility(View.GONE);
					lv_all.setVisibility(View.VISIBLE);
				} else if ("list".equals(view_type)) {
					// listview
					view_type = "grid";
					iv_view_type.setImageResource(R.drawable.icon_grid_view);
					lv_all.setVisibility(View.GONE);
					gd_all.setVisibility(View.VISIBLE);
				}
				// Refresh();

				break;
			case R.id.rl_choose_type:
				// 苗木分类

				if (typeList != null && typeList.length() != 0) {
					// EditP6 popwin1 = new EditP6(StoreActivity.this, typeList,
					// "", StoreActivity.this);
					// popwin1.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
					// popwin1.showAtLocation(mainView, Gravity.BOTTOM
					// | Gravity.CENTER_HORIZONTAL, 0, 0);
					Intent intent = new Intent(StoreActivity.this,
							StoreTypeActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("TypeEx", typeEx);
					mBundle.putString("typeList", typeList.toString());
					intent.putExtras(mBundle);
					startActivityForResult(intent, 13);
					Log.e("typeList", typeList.toString());
				} else {
					Toast.makeText(StoreActivity.this, "该店铺暂无分类",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.rl_choose_screen:
				EditP5 popwin = new EditP5(StoreActivity.this, plantTypes,
						StoreActivity.this);
				popwin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				popwin.showAtLocation(mainView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
				break;

			case R.id.rl_choose_price:
				if ("".equals(priceSort)) {
					priceSort = "price_asc";
					iv_seller_arrow2
							.setImageResource(R.drawable.icon_seller_arrow2);
				} else if ("price_asc".equals(priceSort)) {
					priceSort = "price_desc";
					iv_seller_arrow2
							.setImageResource(R.drawable.icon_seller_arrow3);
				} else if ("price_desc".equals(priceSort)) {
					priceSort = "";
					iv_seller_arrow2
							.setImageResource(R.drawable.icon_seller_arrow1);
				}
				Refresh();

				break;
			default:
				break;
			}
		}

	}

	public void init00() {
		tv_01.setTextColor(getResources().getColor(R.color.light_gray));
		tv_02.setTextColor(getResources().getColor(R.color.light_gray));
		tv_03.setTextColor(getResources().getColor(R.color.light_gray));
		iv_01.setImageDrawable(getResources().getDrawable(R.drawable.dpsy_h));
		iv_02.setImageDrawable(getResources().getDrawable(R.drawable.qbmm_h));
		iv_03.setImageDrawable(getResources().getDrawable(R.drawable.dpxq_h));
		ll_store_home.setVisibility(View.GONE);
		ll_all_flower.setVisibility(View.GONE);
		ll_web.setVisibility(View.GONE);
	}

	public void init01() {
		tv_01.setTextColor(getResources().getColor(R.color.main_color));
		iv_01.setImageDrawable(getResources().getDrawable(R.drawable.dpsy));
		ll_store_home.setVisibility(View.VISIBLE);
	}

	public void init02() {
		tv_02.setTextColor(getResources().getColor(R.color.main_color));
		iv_02.setImageDrawable(getResources().getDrawable(R.drawable.qbmm_lv));
		ll_all_flower.setVisibility(View.VISIBLE);
	}

	public void init03() {
		tv_03.setTextColor(getResources().getColor(R.color.main_color));
		iv_03.setImageDrawable(getResources().getDrawable(R.drawable.dpxq_lv));
		ll_web.setVisibility(View.VISIBLE);

	}

	public void Refresh() {
		datas.clear();
		if (AllgridAdapter != null) {
			AllgridAdapter.notifyDataSetChanged();
		}
		if (AlllistAdapter != null) {
			AlllistAdapter.notifyDataSetChanged();
		}
		pageSize = 10;
		pageIndex = 0;
		seedlingList();
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
			new AlertDialog(StoreActivity.this).builder()
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

	class SharePlatformAdapter extends BaseAdapter {

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return shares.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View inflate = getLayoutInflater().inflate(
					R.layout.list_item_share_platform, null);
			ImageView iv_icon = (ImageView) inflate.findViewById(R.id.iv_icon);
			TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
			iv_icon.setImageResource(shares.get(position).getPic());
			tv_name.setText(shares.get(position).getName());
			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!StoreActivity.this.isFinishing() && dialog != null
							&& dialog.isShowing()) {
						dialog.cancel();
					}

					try {
						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(url,
								350);
						try {
							img_path = FileUtil.saveMyBitmap(
									System.currentTimeMillis() + "",
									qrCodeBitmap);
							if (!"".equals(img_path)) {
								if ("WechatMoments".equals(shares.get(position)
										.getEname())) {
									ShareToWechatMoments();
								} else if ("Wechat".equals(shares.get(position)
										.getEname())) {
									ShareToWechat();
								} else if ("SinaWeibo".equals(shares.get(
										position).getEname())) {
									ShareToSinaWeibo();
								} else if ("QZone".equals(shares.get(position)
										.getEname())) {
									ShareToQzone();
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			return inflate;
		}
	}

	private void ShareToQzone() {
		if (SHARE_TYPE == 1) {
			ShareParams sp5 = new ShareParams();
			sp5.setShareType(Platform.SHARE_IMAGE);
			sp5.setImagePath(img_path);
			Platform qzone = ShareSDK.getPlatform(QQ.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp5);
		} else if (SHARE_TYPE == 2) {
			ShareParams sp5 = new ShareParams();
			sp5.setShareType(Platform.SHARE_WEBPAGE);
			sp5.setTitle(title);
			sp5.setTitleUrl(url); // 标题的超链接
			sp5.setText(text);
			sp5.setImageUrl(img);
			sp5.setSite(getString(R.string.app_name));
			sp5.setSiteUrl(url);
			sp5.setImagePath(img_path);
			Platform qzone = ShareSDK.getPlatform(QQ.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp5);
		}

	}

	private void ShareToSinaWeibo() {
		if (SHARE_TYPE == 1) {
			ShareParams sp3 = new ShareParams();
			sp3.setShareType(Platform.SHARE_IMAGE);
			sp3.setImagePath(img_path);
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp3);
		} else if (SHARE_TYPE == 2) {
			ShareParams sp3 = new ShareParams();
			sp3.setShareType(Platform.SHARE_WEBPAGE);
			sp3.setText(text);
			sp3.setImagePath(img_path);
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp3);
		}

	}

	private void ShareToWechat() {
		if (SHARE_TYPE == 1) {
			ShareParams sp1 = new ShareParams();
			sp1.setShareType(Platform.SHARE_IMAGE);
			sp1.setImagePath(img_path);
			Platform Wechat = ShareSDK.getPlatform("Wechat");
			Wechat.setPlatformActionListener(this);
			Wechat.share(sp1);
		} else if (SHARE_TYPE == 2) {
			ShareParams sp1 = new ShareParams();
			sp1.setShareType(Platform.SHARE_WEBPAGE);
			sp1.setTitle(title);
			sp1.setText(text);
			sp1.setImageUrl(img);
			sp1.setUrl(url);
			sp1.setSiteUrl(url);
			sp1.setImagePath(img_path);
			Platform Wechat = ShareSDK.getPlatform("Wechat");
			Wechat.setPlatformActionListener(this);
			Wechat.share(sp1);
		}

	}

	private void ShareToWechatMoments() {
		if (SHARE_TYPE == 1) {
			ShareParams sp2 = new ShareParams();
			sp2.setShareType(Platform.SHARE_IMAGE);
			sp2.setImagePath(img_path);
			Platform Wechat_men = ShareSDK.getPlatform("WechatMoments");
			Wechat_men.setPlatformActionListener(this);
			Wechat_men.share(sp2);
		} else if (SHARE_TYPE == 2) {
			ShareParams sp2 = new ShareParams();
			sp2.setShareType(Platform.SHARE_WEBPAGE);
			sp2.setTitle(title);
			sp2.setText(text);
			sp2.setImageUrl(img);
			sp2.setUrl(url);
			sp2.setTitleUrl(url);
			sp2.setSiteUrl(url);
			sp2.setImagePath(img_path);
			Platform Wechat_men = ShareSDK.getPlatform("WechatMoments");
			Wechat_men.setPlatformActionListener(this);
			Wechat_men.share(sp2);
		}

	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Toast.makeText(StoreActivity.this, "分享出错", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(StoreActivity.this, "分享已取消", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		String expName = arg0.getName();
		if ("SinaWeibo".equals(expName)) {
			platform = "3";
		} else if ("QZone".equals(expName)) {
			platform = "4";
		} else if ("Wechat".equals(expName)) {
			platform = "2";
		} else if ("WechatMoments".equals(expName)) {
			platform = "1";
		}

	}

	private void showDialog() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_share, null);
		GridView gridView = (GridView) dia_choose_share
				.findViewById(R.id.gridView);
		Button btn_cancle = (Button) dia_choose_share
				.findViewById(R.id.btn_cancle);
		gridView.setAdapter(new SharePlatformAdapter());
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
				if (!StoreActivity.this.isFinishing() && dialog != null) {
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
				if (!StoreActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!StoreActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!StoreActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mLayout.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		seedlingList();
		mLayout.stopRefresh();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 13 && arg1 == 1) {
			Bundle bundle = arg2.getExtras();
			if (bundle.get("TypeEx") != null) {
				typeEx = (TypeEx) bundle.get("TypeEx");
				secondSeedlingTypeId = typeEx.getFirstSeedlingTypeId();
				Refresh();
				tv_type.setText(typeEx.getName());
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

}
