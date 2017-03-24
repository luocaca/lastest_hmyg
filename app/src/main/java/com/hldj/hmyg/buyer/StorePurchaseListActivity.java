package com.hldj.hmyg.buyer;

import info.hoang8f.android.segmented.SegmentedGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.mhao.widget.SlipButton;
import me.mhao.widget.SlipButton.OnChangedListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.PinyinComparator;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.hldj.hmyg.LoginActivity;
import com.hldj.hmyg.MainActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.StorePurchaseListAdapter;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.DaQuYu;
import com.hldj.hmyg.bean.XiaoQuYu;
import com.hldj.hmyg.saler.SubscribeManagerListActivity;
import com.hldj.hmyg.saler.SubscribeSearchListActivity;
import com.hldj.hmyg.saler.purchase.WebViewDialog;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;

public class StorePurchaseListActivity extends BaseSecondActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener,
		OnChangedListener, OnWheelChangedListener, OnCheckedChangeListener,
		OnClickListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private StorePurchaseListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private View mainView;
	private RelativeLayout rl_choose_type;
	private DaquyuAdapter daquyuAdapter;
	private XiaoquyuAdapter xiaoquyuAdapter;

	private String searchKey = "";
	private String seedlingTypeId = "";
	private String name = "";
	private String isSubscribe = "";
	private String minPrice = "";
	private String maxPrice = "";
	private String minDiameter = "";
	private String maxDiameter = "";
	private String minDbh = "";
	private String maxDbh = "";
	private String minHeight = "";
	private String maxHeight = "";
	private String minCrown = "";
	private String maxCrown = "";
	private String minOffbarHeight = "";
	private String maxOffbarHeight = "";
	private String minLength = "";
	private String maxLength = "";
	private String firstSeedlingTypeId = "";
	private String firstSeedlingTypeName = "";
	private String secondSeedlingTypeId = "";
	private String secondSeedlingTypeName = "";
	private String plantTypes = "";
	private String orderBy = "";
	private String priceSort = "";
	private String publishDateSort = "";
	private String cityCode = "";
	private String cityName = "全国";
	private ArrayList<DaQuYu> daQuYus = new ArrayList<DaQuYu>();
	private List<XiaoQuYu> xiaoQuYus = new ArrayList<XiaoQuYu>();
	private PopupWindow popupWindow;
	private String[] keySort = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private SideBar sideBar;
	private ListView listview_xiaoquyu;
	private RelativeLayout rl_choose_time;
	private ArrayList<SortList> sortLists = new ArrayList<SortList>();
	private SlipButton newMsgAlertStatusOnOff;
	private PopupWindow popupWindow2;
	private SortListAdapter sortListAdapter;
	private TextView tv_sorts;
	private LinearLayout ll_choice;
	private TagView tagView;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView edit_btn;
	private TextView tv_notifaction;
	private RadioButton button31;
	private RadioButton button32;
	private int subscribeUserCount = 0;

	private String purchaseFormId = "";
	private String title = "";
	private TextView tv_title;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	private String quoteDesc = "";
	boolean isFirstLoading = true;

	/**
	 * 快速报价
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_purchase_list);

		View inflate = getLayoutInflater()
				.inflate(R.layout.head_purchase, null);
		tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		tv_06 = (TextView) inflate.findViewById(R.id.tv_06);

		edit_btn = (TextView) findViewById(R.id.edit_btn);
		tv_title = (TextView) findViewById(R.id.tv_title);
		SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
		button31 = (RadioButton) findViewById(R.id.button31);
		button32 = (RadioButton) findViewById(R.id.button32);
		button31.setChecked(true);
		segmented3.setOnCheckedChangeListener(this);
		tagView = (TagView) this.findViewById(R.id.tagview);
		if (getIntent().getStringExtra("purchaseFormId") != null) {
			purchaseFormId = getIntent().getStringExtra("purchaseFormId");
		}
		if (getIntent().getStringExtra("secondSeedlingTypeId") != null) {
			secondSeedlingTypeId = getIntent().getStringExtra(
					"secondSeedlingTypeId");
		}
		if (getIntent().getStringExtra("title") != null) {
			title = getIntent().getStringExtra("title");
			tv_title.setText(title);
		}
		if (getIntent().getStringExtra("searchKey") != null) {
			searchKey = getIntent().getStringExtra("searchKey");
			tagView.removeAllTags();
			if (!"".equals(searchKey)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(searchKey);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 1; // 1 搜索 2分类
				tagView.addTag(tag);
			}

		}
		tagView.setOnTagDeleteListener(new OnTagDeleteListener() {

			@Override
			public void onTagDeleted(int position, me.kaede.tagview.Tag tag) {
				// TODO Auto-generated method stub
				if (tag.id == 1) {
					searchKey = "";
					onRefresh();
				} else if (tag.id == 2) {
					firstSeedlingTypeId = "";
					firstSeedlingTypeName = "";
					onRefresh();
				} else if (tag.id == 3) {
					secondSeedlingTypeId = "";
					secondSeedlingTypeName = "";
					onRefresh();
				} else if (tag.id == 4) {
					seedlingTypeId = "";
					name = "";
					onRefresh();
				}

			}
		});
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		ImageView iv_sousuo = (ImageView) findViewById(R.id.iv_sousuo);

		tv_notifaction = (TextView) findViewById(R.id.tv_notifaction);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		RelativeLayout rl_choose_screen = (RelativeLayout) findViewById(R.id.rl_choose_screen);
		rl_choose_time = (RelativeLayout) findViewById(R.id.rl_choose_time);
		ll_choice = (LinearLayout) findViewById(R.id.ll_choice);
		tv_sorts = (TextView) findViewById(R.id.tv_sorts);

		mainView = (View) findViewById(R.id.mainView);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		if (!"".equals(purchaseFormId)) {
			xListView.addHeaderView(inflate);
		}
		initSortList();
		initDataGetFirstType();
		// initCount();
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		iv_sousuo.setOnClickListener(multipleClickProcess);
		rl_choose_type.setOnClickListener(multipleClickProcess);
		rl_choose_screen.setOnClickListener(multipleClickProcess);
		rl_choose_time.setOnClickListener(multipleClickProcess);
		edit_btn.setOnClickListener(multipleClickProcess);
		tv_notifaction.setOnClickListener(multipleClickProcess);

	}

	private void initSortList() {
		// TODO Auto-generated method stub
		SortList sortList1 = new SortList("", "默认");
		SortList sortList2 = new SortList("publishDate", "发布时间");
		SortList sortList3 = new SortList("closeDate", "截止时间");
		sortLists.add(sortList1);
		sortLists.add(sortList2);
		sortLists.add(sortList3);
	}

	private void initCount() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("subscribeUserId",
				MyApplication.Userinfo.getString("id", ""));
		params.put("isSubscribe", "1");
		params.put("purchaseId", purchaseFormId);
		params.put("searchKey", searchKey);
		params.put("minDiameter", minDiameter);
		params.put("maxDiameter", maxDiameter);
		params.put("minDbh", minDbh);
		params.put("maxDbh", maxDbh);
		params.put("minHeight", minHeight);
		params.put("maxHeight", maxHeight);
		params.put("minCrown", minCrown);
		params.put("maxCrown", maxCrown);
		params.put("minOffbarHeight", minOffbarHeight);
		params.put("maxOffbarHeight", maxOffbarHeight);
		params.put("minLength", minLength);
		params.put("maxLength", maxLength);
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("secondSeedlingTypeId", secondSeedlingTypeId);
		params.put("cityCode", cityCode);
		params.put("seedlingTypeId", seedlingTypeId);
		if (plantTypes.length() > 0) {
			params.put("plantTypes",
					plantTypes.substring(0, plantTypes.length() - 1));
		} else {
			params.put("plantTypes", plantTypes);
		}
		params.put("orderBy", orderBy);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "purchase/list", params,
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
								// Toast.makeText(ManagerListActivity.this, msg,
								// Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(data, "page");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									isSubscribe = "1";
									edit_btn.setVisibility(View.VISIBLE);
									button32.setChecked(true);
								} else {
									isSubscribe = "0";
									edit_btn.setVisibility(View.INVISIBLE);
									button31.setChecked(true);
								}
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
						Toast.makeText(StorePurchaseListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("subscribeUserId",
				MyApplication.Userinfo.getString("id", ""));
		if ("1".equals(isSubscribe)) {
			params.put("isSubscribe", isSubscribe);
		}
		if (MyApplication.Userinfo.getBoolean("isLogin", false)) {
			params.put("userId", MyApplication.Userinfo.getString("id", ""));
		}
		params.put("purchaseId", purchaseFormId);
		params.put("searchKey", searchKey);
		params.put("minDiameter", minDiameter);
		params.put("maxDiameter", maxDiameter);
		params.put("minDbh", minDbh);
		params.put("maxDbh", maxDbh);
		params.put("minHeight", minHeight);
		params.put("maxHeight", maxHeight);
		params.put("minCrown", minCrown);
		params.put("maxCrown", maxCrown);
		params.put("minOffbarHeight", minOffbarHeight);
		params.put("maxOffbarHeight", maxOffbarHeight);
		params.put("minLength", minLength);
		params.put("maxLength", maxLength);
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("secondSeedlingTypeId", secondSeedlingTypeId);
		params.put("cityCode", cityCode);
		params.put("seedlingTypeId", seedlingTypeId);
		if (plantTypes.length() > 0) {
			params.put("plantTypes",
					plantTypes.substring(0, plantTypes.length() - 1));
		} else {
			params.put("plantTypes", plantTypes);
		}
		params.put("orderBy", orderBy);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		Log.e("purchase/list", params.toString());
		finalHttp.post(GetServerUrl.getUrl() + "purchase/list", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						Log.e("purchase/list", t.toString());
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								// Toast.makeText(ManagerListActivity.this, msg,
								// Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(data, "page");
								JSONObject headPurchase = JsonGetInfo
										.getJSONObject(data, "headPurchase");
								String id = JsonGetInfo.getJsonString(
										headPurchase, "id");
								String blurProjectName = JsonGetInfo
										.getJsonString(headPurchase,
												"blurProjectName");
								quoteDesc = JsonGetInfo.getJsonString(
										headPurchase, "quoteDesc");
								String cityName = JsonGetInfo.getJsonString(
										headPurchase, "cityName");
								String dispatchName = JsonGetInfo.getJsonString(
										headPurchase, "dispatchName");
								String dispatchPhone = JsonGetInfo.getJsonString(
										headPurchase, "dispatchPhone");
								String displayPhone = JsonGetInfo
										.getJsonString(JsonGetInfo
												.getJSONObject(headPurchase,
														"buyer"),
												"displayPhone");
								String displayName = JsonGetInfo.getJsonString(
										JsonGetInfo.getJSONObject(headPurchase,
												"buyer"), "displayName");
								String closeDate = JsonGetInfo.getJsonString(
										headPurchase, "closeDate");
								tv_01.setText(blurProjectName);
								tv_02.setText(displayName);
								tv_03.setText("报价说明");
								tv_04.setText("用苗地：" + cityName);
								 if (!"".equals(dispatchPhone) && "".equals(dispatchName)) {
									tv_05.setText("联系电话：" + dispatchPhone);
								}else if ("".equals(dispatchPhone) && !"".equals(dispatchName)) {
									tv_05.setText("联系人：" + dispatchName);
								}else if (!"".equals(dispatchPhone) && !"".equals(dispatchName)) {
									tv_05.setText("联系电话：" + dispatchName+"/"+dispatchPhone);
								}else {
									tv_05.setText("联系电话：" + displayPhone);
								}
							
								tv_06.setText("截止时间：" + closeDate);
								tv_03.setOnClickListener(StorePurchaseListActivity.this);
								if (!"".equals(quoteDesc) && isFirstLoading) {
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											showWebViewDialog(quoteDesc);
											isFirstLoading = false;

										}
									}, 1500);

								}
								subscribeUserCount = JsonGetInfo.getJsonInt(
										data, "subscribeUserCount");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										JSONObject purchaseJson = JsonGetInfo
												.getJSONObject(jsonObject3,
														"purchaseJson");
										JSONObject buyer = JsonGetInfo
												.getJSONObject(purchaseJson,
														"buyer");
										JSONObject ciCity = JsonGetInfo
												.getJSONObject(purchaseJson,
														"ciCity");
										hMap.put("displayName", JsonGetInfo
												.getJsonString(buyer,
														"displayName"));
										hMap.put("displayPhone", JsonGetInfo
												.getJsonString(buyer,
														"displayPhone"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(ciCity,
														"fullName"));
										hMap.put("num", JsonGetInfo
												.getJsonString(purchaseJson,
														"num"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(purchaseJson,
														"cityName"));
										hMap.put("needInvoice", JsonGetInfo
												.getJsonBoolean(purchaseJson,
														"needInvoice"));
										hMap.put("receiptDate", JsonGetInfo
												.getJsonString(purchaseJson,
														"receiptDate"));
										hMap.put("closeDate", JsonGetInfo
												.getJsonString(purchaseJson,
														"closeDate"));
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										hMap.put("remarks", JsonGetInfo
												.getJsonString(jsonObject3,
														"remarks"));
										hMap.put("createDate", JsonGetInfo
												.getJsonString(jsonObject3,
														"createDate"));
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"name"));
										hMap.put("seedlingParams", JsonGetInfo
												.getJsonString(jsonObject3,
														"seedlingParams"));
										hMap.put("firstSeedlingTypeId",
												JsonGetInfo.getJsonString(
														jsonObject3,
														"firstSeedlingTypeId"));
										hMap.put("firstTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"firstTypeName"));
										hMap.put("plantType", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.put("specText", JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.put("unitType", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitType"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
										hMap.put("plantTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantTypeName"));
										hMap.put("diameterType", JsonGetInfo
												.getJsonString(jsonObject3,
														"diameterType"));
										hMap.put("dbhType", JsonGetInfo
												.getJsonString(jsonObject3,
														"dbhType"));
										hMap.put("purchaseId", JsonGetInfo
												.getJsonString(jsonObject3,
														"purchaseId"));
										hMap.put("prCode", JsonGetInfo
												.getJsonString(jsonObject3,
														"prCode"));
										hMap.put("ciCode", JsonGetInfo
												.getJsonString(jsonObject3,
														"ciCode"));
										hMap.put("coCode", JsonGetInfo
												.getJsonString(jsonObject3,
														"coCode"));
										hMap.put("twCode", JsonGetInfo
												.getJsonString(jsonObject3,
														"twCode"));
										hMap.put("diameter", JsonGetInfo
												.getJsonInt(jsonObject3,
														"diameter"));
										hMap.put("dbh", JsonGetInfo.getJsonInt(
												jsonObject3, "dbh"));
										hMap.put("height", JsonGetInfo
												.getJsonInt(jsonObject3,
														"height"));
										hMap.put("crown", JsonGetInfo
												.getJsonInt(jsonObject3,
														"crown"));
										hMap.put("offbarHeight", JsonGetInfo
												.getJsonInt(jsonObject3,
														"offbarHeight"));
										hMap.put("length", JsonGetInfo
												.getJsonInt(jsonObject3,
														"length"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("quoteCountJson", JsonGetInfo
												.getJsonInt(jsonObject3,
														"quoteCountJson"));
										hMap.put("isQuoted", JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isQuoted"));
										if(!"".equals(purchaseFormId)){
											hMap.put("hasShowDialog", true);
										}else {
											hMap.put("hasShowDialog", false);
										}
									
										
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new StorePurchaseListAdapter(
												StorePurchaseListActivity.this,
												datas, mainView);
										xListView.setAdapter(listAdapter);
									}

									pageIndex++;

								}

								if (datas.size() > 0) {
									tv_notifaction.setVisibility(View.GONE);
									xListView.setVisibility(View.VISIBLE);
								} else if (datas.size() == 0) {
									tv_notifaction.setVisibility(View.VISIBLE);
									xListView.setVisibility(View.GONE);
									if ("1".equals(isSubscribe)) {
										if (subscribeUserCount == 0) {
											tv_notifaction
													.setText("点击屏幕或右上角添加品种订阅");
										} else {
											tv_notifaction
													.setText("暂无订阅品种的询价单");
										}

									} else {
										// 全部
										tv_notifaction.setText("暂无询价单");
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
						Toast.makeText(StorePurchaseListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;

	}

	private void initDataGetFirstType() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "seedlingType/getFirstType",
				params, new AjaxCallBack<Object>() {

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

								daQuYus.add(new DaQuYu("", "不限"));

								JSONArray typeList = jsonObject.getJSONObject(
										"data").getJSONArray("typeList");
								for (int i = 0; i < typeList.length(); i++) {
									JSONObject jsonObject2 = typeList
											.getJSONObject(i);
									HashMap<String, Object> hMap = new HashMap<String, Object>();
									hMap.put("id", JsonGetInfo.getJsonString(
											jsonObject2, "id"));
									hMap.put("isNewRecord", JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isNewRecord"));
									hMap.put("createBy", JsonGetInfo
											.getJsonString(jsonObject2,
													"createBy"));
									hMap.put("createDate", JsonGetInfo
											.getJsonString(jsonObject2,
													"createDate"));
									hMap.put("updateBy", JsonGetInfo
											.getJsonString(jsonObject2,
													"updateBy"));
									hMap.put("updateDate", JsonGetInfo
											.getJsonString(jsonObject2,
													"updateDate"));
									hMap.put("delFlag", JsonGetInfo
											.getJsonString(jsonObject2,
													"delFlag"));
									hMap.put("name", JsonGetInfo.getJsonString(
											jsonObject2, "name"));
									hMap.put("aliasName", JsonGetInfo
											.getJsonString(jsonObject2,
													"aliasName"));
									hMap.put("parentId", JsonGetInfo
											.getJsonString(jsonObject2,
													"parentId"));
									hMap.put("level", JsonGetInfo.getJsonInt(
											jsonObject2, "level"));
									hMap.put("firstPinyin", JsonGetInfo
											.getJsonString(jsonObject2,
													"firstPinyin"));
									hMap.put("fullPinyin", JsonGetInfo
											.getJsonString(jsonObject2,
													"fullPinyin"));
									hMap.put("seedlingParams", JsonGetInfo
											.getJsonString(jsonObject2, "id"));
									hMap.put("sort", JsonGetInfo.getJsonInt(
											jsonObject2, "sort"));
									hMap.put("isTop",
											JsonGetInfo.getJsonString(
													jsonObject2, "isTop"));
									hMap.put("url", JsonGetInfo.getJsonString(
											jsonObject2, "url"));
									hMap.put("ossThumbnailImagePath",
											JsonGetInfo.getJsonString(
													jsonObject2,
													"ossThumbnailImagePath"));
									DaQuYu daQuYu = new DaQuYu(JsonGetInfo
											.getJsonString(jsonObject2, "id"),
											JsonGetInfo.getJsonString(
													jsonObject2, "name"));
									daQuYus.add(daQuYu);
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
						Toast.makeText(StorePurchaseListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();

						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void ChoiceArea() {
		// TODO Auto-generated method stub
		View quyu = getLayoutInflater().inflate(R.layout.popo_quyu, null);
		ListView listview_daquyu = (ListView) quyu
				.findViewById(R.id.listview_daquyu);
		listview_xiaoquyu = (ListView) quyu
				.findViewById(R.id.listview_xiaoquyu);

		sideBar = (SideBar) quyu.findViewById(R.id.sidrbar);
		TextView dialog = (TextView) quyu.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setVisibility(View.GONE);
		listview_xiaoquyu.setVisibility(View.GONE);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = xiaoquyuAdapter.getPositionForSection(s
						.charAt(0));
				if (position != -1) {
					listview_xiaoquyu.setSelection(position);
				}

			}
		});
		if (daquyuAdapter != null) {
			listview_daquyu.setAdapter(daquyuAdapter);
		} else {
			daquyuAdapter = new DaquyuAdapter();
			listview_daquyu.setAdapter(daquyuAdapter);
		}
		if (xiaoquyuAdapter != null) {
			listview_xiaoquyu.setAdapter(xiaoquyuAdapter);
		} else {
			xiaoquyuAdapter = new XiaoquyuAdapter(
					StorePurchaseListActivity.this, xiaoQuYus);
			listview_xiaoquyu.setAdapter(xiaoquyuAdapter);
		}

		listview_daquyu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (popupWindow != null) {
					popupWindow.dismiss();
				}

				if ("不限".equals(daQuYus.get(position).getName())) {
					// TODO Auto-generated method stub
					firstSeedlingTypeId = "";
					secondSeedlingTypeId = "";
					firstSeedlingTypeName = "";
					secondSeedlingTypeName = "";
					daquyuAdapter.setSeclection(position);
					xiaoquyuAdapter.notifyDataSetChanged();
					tagView.removeAllTags();
					onRefresh();

				} else {
					firstSeedlingTypeId = daQuYus.get(position).getId();
					firstSeedlingTypeName = daQuYus.get(position).getName();
					secondSeedlingTypeId = "";
					secondSeedlingTypeName = "";
					daquyuAdapter.setSeclection(position);
					daquyuAdapter.notifyDataSetChanged();

					tagView.removeAllTags();
					me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
							firstSeedlingTypeName);
					tag.layoutColor = getResources().getColor(
							R.color.main_color);
					tag.isDeletable = true;
					tag.id = 2; // 1 搜索 2分类
					tagView.addTag(tag);
					onRefresh();

					FinalHttp finalHttp = new FinalHttp();
					GetServerUrl.addHeaders(finalHttp, false);
					AjaxParams params = new AjaxParams();
					params.put("firstTypeId", firstSeedlingTypeId);
					finalHttp.post(GetServerUrl.getUrl()
							+ "seedlingType/getSecondTypeMap", params,
							new AjaxCallBack<Object>() {

								@Override
								public void onSuccess(Object t) {
									// TODO Auto-generated method stub
									xiaoQuYus.clear();
									try {
										JSONObject jsonObject = new JSONObject(
												t.toString());
										JSONObject jsonObject2 = JsonGetInfo
												.getJSONObject(JsonGetInfo
														.getJSONObject(
																jsonObject,
																"data"),
														"childData");
										for (int i = 0; i < keySort.length; i++) {
											JSONArray jsonArray = JsonGetInfo
													.getJsonArray(jsonObject2,
															keySort[i]);

											if (jsonArray.length() > 0) {
												for (int j = 0; j < jsonArray
														.length(); j++) {
													String id = jsonArray
															.getJSONObject(j)
															.getString("id");
													String name = jsonArray
															.getJSONObject(j)
															.getString("name");
													XiaoQuYu xiaoQuYu = new XiaoQuYu(
															id,
															name,
															keySort[i]
																	.toLowerCase());
													xiaoQuYus.add(xiaoQuYu);
													xiaoquyuAdapter
															.notifyDataSetChanged();
												}
												// 根据a-z进行排序源数据
												Collections.sort(xiaoQuYus,
														pinyinComparator);
											}
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									super.onSuccess(t);
								}
							});
				}

			}

		});
		listview_xiaoquyu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				secondSeedlingTypeName = xiaoQuYus.get(position).getName();
				secondSeedlingTypeId = xiaoQuYus.get(position).getId();
				xiaoquyuAdapter.setSeclection(position);
				xiaoquyuAdapter.notifyDataSetChanged();
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				onRefresh();

			}

		});

		popupWindow = new PopupWindow(quyu, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// popupWindow = new PopupWindow(view, getResources()
		// .getDimensionPixelSize(R.dimen.popmenu_width),
		// LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.showAsDropDown(rl_choose_type, 1,
		// 保证尺寸是根据屏幕像素密度来的
				getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	class DaquyuAdapter extends BaseAdapter {

		private int clickTemp = 0;

		// 标识选择的Item
		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return daQuYus.size();
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
			View daquyu_list_item = getLayoutInflater().inflate(
					R.layout.list_item, null);
			RelativeLayout da_rl_popo_list_item = (RelativeLayout) daquyu_list_item
					.findViewById(R.id.rl_popo_list_item);
			TextView daquyu_tv_item = (TextView) daquyu_list_item
					.findViewById(R.id.tv_item);
			TextView tvLetter = (TextView) daquyu_list_item
					.findViewById(R.id.catalog);
			tvLetter.setVisibility(View.GONE);
			daquyu_tv_item.setText(daQuYus.get(position).getName());
			if (clickTemp == position) {
				da_rl_popo_list_item.setBackgroundColor(Color.argb(155, 192,
						192, 192)); // #COCOCO
			} else {
				da_rl_popo_list_item.setBackgroundColor(Color.argb(155, 255,
						255, 255)); // #FFFFFF
			}
			return daquyu_list_item;
		}

	}

	class XiaoquyuAdapter extends BaseAdapter implements SectionIndexer {

		private int clickTemp = 0;
		private List<XiaoQuYu> list = null;
		private Context mContext;

		// 标识选择的Item
		public void setSeclection(int position) {
			clickTemp = position;
		}

		public XiaoquyuAdapter(Context mContext, List<XiaoQuYu> list) {
			this.mContext = mContext;
			this.list = list;
		}

		/**
		 * 当ListView数据发生变化时,调用此方法来更新ListView
		 * 
		 * @param list
		 */
		public void updateListView(List<XiaoQuYu> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			View xiaoquyu_list_item = getLayoutInflater().inflate(
					R.layout.list_item, null);
			RelativeLayout xiao_rl_popo_list_item = (RelativeLayout) xiaoquyu_list_item
					.findViewById(R.id.rl_popo_list_item);
			TextView xiao_quyu_tv_item = (TextView) xiaoquyu_list_item
					.findViewById(R.id.tv_item);
			TextView tvLetter = (TextView) xiaoquyu_list_item
					.findViewById(R.id.catalog);
			tvLetter.setVisibility(View.GONE);
			xiao_quyu_tv_item.setText(list.get(position).getName());
			if (clickTemp == position) {
				xiao_rl_popo_list_item.setBackgroundColor(Color.argb(155, 192,
						192, 192)); // #COCOCO
			} else {
				xiao_rl_popo_list_item.setBackgroundColor(Color.argb(155, 255,
						255, 255)); // #FFFFFF
			}
			return xiaoquyu_list_item;
		}

		/**
		 * 根据ListView的当前位置获取分类的首字母的Char ascii值
		 */
		public int getSectionForPosition(int position) {
			return list.get(position).getSortLetters().charAt(0);
		}

		/**
		 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
		 */
		public int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = list.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}

			return -1;
		}

		/**
		 * 提取英文的首字母，非英文字母用#代替。
		 * 
		 * @param str
		 * @return
		 */
		private String getAlpha(String str) {
			String sortStr = str.trim().substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortStr.matches("[A-Z]")) {
				return sortStr;
			} else {
				return "#";
			}
		}

		@Override
		public Object[] getSections() {
			return null;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 9) {
			cityCode = data.getStringExtra("cityCode");
			cityName = data.getStringExtra("cityName");
			minPrice = data.getStringExtra("minPrice");
			maxPrice = data.getStringExtra("maxPrice");
			minDiameter = data.getStringExtra("minDiameter");
			maxDiameter = data.getStringExtra("maxDiameter");
			minDbh = data.getStringExtra("minDbh");
			maxDbh = data.getStringExtra("maxDbh");
			minHeight = data.getStringExtra("minHeight");
			maxHeight = data.getStringExtra("maxHeight");
			minLength = data.getStringExtra("minLength");
			maxLength = data.getStringExtra("maxLength");
			minCrown = data.getStringExtra("minCrown");
			maxCrown = data.getStringExtra("maxCrown");
			minOffbarHeight = data.getStringExtra("minOffbarHeight");
			maxOffbarHeight = data.getStringExtra("maxOffbarHeight");
			plantTypes = data.getStringExtra("plantTypes");
			onRefresh();
		} else if (resultCode == 8) {
			seedlingTypeId = "";
			name = "";
			searchKey = data.getStringExtra("searchKey");
			List<Tag> tags = tagView.getTags();
			for (int i = 0; i < tags.size(); i++) {
				if (tags.get(i).id == 1 || tags.get(i).id == 4) {
					tagView.remove(i);
				}
			}
			if (!"".equals(searchKey)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(searchKey);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 1; // 1 搜索 2分类 3.订阅
				tagView.addTag(tag);
			}
			onRefresh();
		} else if (resultCode == 11 && data != null) {
			seedlingTypeId = data.getStringExtra("seedlingTypeId");
			name = data.getStringExtra("name");
			searchKey = "";
			List<Tag> tags = tagView.getTags();
			for (int i = 0; i < tags.size(); i++) {
				if (tags.get(i).id == 1 || tags.get(i).id == 4) {
					tagView.remove(i);
				}
			}
			me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(name);
			tag.layoutColor = getResources().getColor(R.color.main_color);
			tag.isDeletable = true;
			tag.id = 4; // 1 搜索 2分类 4.订阅
			tagView.addTag(tag);
			onRefresh();
		} else if (requestCode == 4) {
			onRefresh();
		} else if (requestCode == 7) {
			// 去订阅
			onRefresh();
		} else if (resultCode == 12) {
			// 去订阅
			onRefresh();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				switch (view.getId()) {
				case R.id.btn_back:
					finish();
					break;
				case R.id.iv_sousuo:
					Intent intent = new Intent(StorePurchaseListActivity.this,
							PurchaseSearchListActivity.class);
					intent.putExtra("from", "StorePurchaseListActivity");
					startActivityForResult(intent, 1);
					break;
				case R.id.rl_choose_type:
					if (popupWindow == null) {
						ChoiceArea();
					} else {
						if (!popupWindow.isShowing()) {
							ChoiceArea();
						}
					}
					break;
				case R.id.rl_choose_screen:
					showCitys();
					// Intent toSellectActivity = new Intent(
					// StorePurchaseListActivity.this,
					// SellectActivity.class);
					// toSellectActivity.putExtra("from",
					// "StorePurchaseListActivity");
					// toSellectActivity.putExtra("minPrice", minPrice);
					// toSellectActivity.putExtra("maxPrice", maxPrice);
					// toSellectActivity.putExtra("minDiameter", minDiameter);
					// toSellectActivity.putExtra("maxDiameter", maxDiameter);
					// toSellectActivity.putExtra("minDbh", minDbh);
					// toSellectActivity.putExtra("maxDbh", maxDbh);
					// toSellectActivity.putExtra("minHeight", minHeight);
					// toSellectActivity.putExtra("maxHeight", maxHeight);
					// toSellectActivity.putExtra("minLength", minLength);
					// toSellectActivity.putExtra("maxLength", maxLength);
					// toSellectActivity.putExtra("minCrown", minCrown);
					// toSellectActivity.putExtra("maxCrown", maxCrown);
					// toSellectActivity.putExtra("cityCode", cityCode);
					// toSellectActivity.putExtra("cityName", cityName);
					// toSellectActivity.putExtra("minOffbarHeight",
					// minOffbarHeight);
					// toSellectActivity.putExtra("maxOffbarHeight",
					// maxOffbarHeight);
					// toSellectActivity.putExtra("plantTypes", plantTypes);
					// startActivityForResult(toSellectActivity, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);
					break;

				case R.id.rl_choose_time:
					// if (sortLists.size() > 0) {
					// ChoiceSortList();
					// }
					Intent toPurchaseSearchListActivity = new Intent(
							StorePurchaseListActivity.this,
							PurchaseSearchListActivity.class);
					toPurchaseSearchListActivity.putExtra("from",
							"StorePurchaseListActivity");
					startActivityForResult(toPurchaseSearchListActivity, 1);
					break;
				case R.id.tv_notifaction:
					if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
						Intent toLoginActivity = new Intent(
								StorePurchaseListActivity.this,
								LoginActivity.class);
						startActivityForResult(toLoginActivity, 4);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
					} else {
						Intent toSubscribeSearchListActivity = new Intent(
								StorePurchaseListActivity.this,
								SubscribeSearchListActivity.class);
						startActivityForResult(toSubscribeSearchListActivity, 7);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
					}
					break;

				case R.id.edit_btn:
					if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
						Intent toLoginActivity = new Intent(
								StorePurchaseListActivity.this,
								LoginActivity.class);
						startActivityForResult(toLoginActivity, 4);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
					} else {
						Intent toSubscribeManagerListActivity = new Intent(
								StorePurchaseListActivity.this,
								SubscribeManagerListActivity.class);
						startActivityForResult(toSubscribeManagerListActivity,
								1);
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

		/**
		 * 计时线程（防止在一定时间段内重复点击按钮）
		 */
		private class TimeThread extends Thread {
			public void run() {
				try {
					Thread.sleep(200);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void ChoiceSortList() {
		// TODO Auto-generated method stub
		View popo_shop_type_list = getLayoutInflater().inflate(
				R.layout.popo_shop_type_list, null);
		ListView listview = (ListView) popo_shop_type_list
				.findViewById(R.id.listview);
		newMsgAlertStatusOnOff = (SlipButton) popo_shop_type_list
				.findViewById(R.id.newMsgAlertStatusOnOff);
		// newMsgAlertStatusOnOff.setCheck(DemoApplication.Userinfo.getBoolean(
		// "newMsgAlertStatusOnOff", false));
		newMsgAlertStatusOnOff.setOnChangedListener(this);
		if (sortListAdapter != null) {
			listview.setAdapter(sortListAdapter);
		} else {
			sortListAdapter = new SortListAdapter();
			listview.setAdapter(sortListAdapter);
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sortListAdapter.setSeclection(position);
				sortListAdapter.notifyDataSetChanged();
				tv_sorts.setText(sortLists.get(position).getName());
				String sort_id = sortLists.get(position).getID();
				if (popupWindow2 != null) {
					popupWindow2.dismiss();
				}
				if ("默认".equals(sortLists.get(position).getName())) {

				} else if ("发布时间".equals(sortLists.get(position).getName())) {

				} else if ("截至时间".equals(sortLists.get(position).getName())) {
				}
				orderBy = sortLists.get(position).getID();
				onRefresh();
			}

		});

		popupWindow2 = new PopupWindow(popo_shop_type_list,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// popupWindow = new PopupWindow(view, getResources()
		// .getDimensionPixelSize(R.dimen.popmenu_width),
		// LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow2.setBackgroundDrawable(new BitmapDrawable());

		popupWindow2.showAsDropDown(ll_choice, 1,
		// 保证尺寸是根据屏幕像素密度来的
				getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));

		// 使其聚集
		popupWindow2.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow2.setOutsideTouchable(true);
		// 刷新状态
		popupWindow2.update();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new StorePurchaseListAdapter(
					StorePurchaseListActivity.this, datas, mainView);
			xListView.setAdapter(listAdapter);
		} else {
			listAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initData();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		xListView.setPullRefreshEnable(false);
		initData();
		onLoad();
	}

	private void onLoad() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				xListView.stopRefresh();
				xListView.stopLoadMore();
				xListView.setRefreshTime(new Date().toLocaleString());
				xListView.setPullLoadEnable(true);
				xListView.setPullRefreshEnable(true);

			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

	class SortListAdapter extends BaseAdapter {

		private int clickTemp = 0;

		// 标识选择的Item
		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sortLists.size();
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
			View sort_list_item = getLayoutInflater().inflate(
					R.layout.list_item_sort, null);
			RelativeLayout rl_sort_list_item = (RelativeLayout) sort_list_item
					.findViewById(R.id.rl_popo_list_item);
			TextView area_tv_item = (TextView) sort_list_item
					.findViewById(R.id.tv_item);
			ImageView is_check = (ImageView) sort_list_item
					.findViewById(R.id.is_check);
			area_tv_item.setText(sortLists.get(position).getName());
			if (clickTemp == position) {
				rl_sort_list_item.setBackgroundColor(Color.argb(155, 192, 192,
						192)); // #COCOCO
				is_check.setVisibility(View.VISIBLE);
			} else {
				rl_sort_list_item.setBackgroundColor(Color.argb(155, 255, 255,
						255)); // #FFFFFF
				is_check.setVisibility(View.INVISIBLE);
			}
			return sort_list_item;
		}

	}

	@Override
	public void onChanged(boolean checkState, View v) {
		// TODO Auto-generated method stub

	}

	private void showCitys() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
		mViewCity.setVisibility(View.GONE);
		mViewDistrict.setVisibility(View.GONE);
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
				cityName = mCurrentProviceName + "\u0020" + mCurrentCityName
						+ "\u0020" + mCurrentDistrictName + "\u0020";
				cityCode = mCurrentZipCode;
				cityCode = GetCodeByName.initProvinceDatas(
						StorePurchaseListActivity.this, mCurrentProviceName,
						mCurrentCityName);
				onRefresh();
				// tv_area.setText(cityName);
				if (!StorePurchaseListActivity.this.isFinishing()
						&& dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!StorePurchaseListActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!StorePurchaseListActivity.this.isFinishing()
				&& dialog != null && !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				StorePurchaseListActivity.this, mProvinceDatas));
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
				StorePurchaseListActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.button31:
			isSubscribe = "0";
			edit_btn.setVisibility(View.INVISIBLE);
			seedlingTypeId = "";
			name = "";
			List<Tag> tags = tagView.getTags();
			for (int i = 0; i < tags.size(); i++) {
				if (tags.get(i).id == 4) {
					tagView.remove(i);
				}
			}
			// 全部
			onRefresh();
			break;
		case R.id.button32:
			isSubscribe = "1";
			edit_btn.setVisibility(View.VISIBLE);
			// 我的订阅
			onRefresh();
			break;
		default:
			// Nothing to do
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_03:
			showWebViewDialog(quoteDesc);
			break;
		default:
			break;
		}
	}

	private void showWebViewDialog(String quoteDesc) {
		// TODO Auto-generated method stub
		if (quoteDesc != null) {
			WebViewDialog.Builder builder = new WebViewDialog.Builder(
					StorePurchaseListActivity.this);
			builder.setTitle("报价说明");
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

}
