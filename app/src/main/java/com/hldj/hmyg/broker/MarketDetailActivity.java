package com.hldj.hmyg.broker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.broker.adapter.PriceListAdapter;
import com.hldj.hmyg.broker.adapter.PriceListAdapter.OnGoodsCheckedChangeListener;
import com.hldj.hmyg.broker.bean.MarketPrice;
import com.hldj.hmyg.broker.bean.Price;
import com.hldj.hmyg.broker.bean.SellectPrice;
import com.hldj.hmyg.saler.bean.ParamsList;
import com.hldj.hmyg.saler.bean.Subscribe;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

@SuppressLint("NewApi")
public class MarketDetailActivity extends BaseSecondActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener,
		OnCheckedChangeListener, OnWheelChangedListener {

	private XListView xListView;
	private String orderBy = "";
	private String priceSort = "";
	private String publishDateSort = "";
	private ArrayList<Price> datas = new ArrayList<Price>();
	private int pageSize = 10;
	private int pageIndex = 0;
	private PriceListAdapter listAdapter;

	boolean getdata; // 避免刷新多出数据
	private String noteType = "1";
	FinalHttp finalHttp = new FinalHttp();
	private String minSpec = "";
	private String maxSpec = "";
	private String minHeight = "";
	private String maxHeight = "";
	private String minCrown = "";
	private String maxCrown = "";
	private String name = "";
	private String cityCode = "";
	private String cityName = "";
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TagView tagView;
	public int i = 0;
	MaterialDialog mMaterialDialog;
	private MarketPrice marketPrice;
	private TextView tv_03;
	private SellectPrice sellectPrice;
	private TextView tv_title;
	private RelativeLayout rl_choose_type;
	private RelativeLayout rl_choose_screen;
	private TextView tv_my;
	private TextView tv_zoushi;
	String chooseType = "my"; // quxian
	String selectType = "quanbu";
	private TextView tv_notifaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market_detail);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_my = (TextView) findViewById(R.id.tv_my);
		tv_notifaction = (TextView) findViewById(R.id.tv_notifaction);
		tv_zoushi = (TextView) findViewById(R.id.tv_zoushi);
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		rl_choose_screen = (RelativeLayout) findViewById(R.id.rl_choose_screen);
		TextView id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			marketPrice = (MarketPrice) extras.get("MarketPrice");
			TextView tv_01 = (TextView) findViewById(R.id.tv_01);
			TextView tv_02 = (TextView) findViewById(R.id.tv_02);
			tv_03 = (TextView) findViewById(R.id.tv_03);
			TextView tv_04 = (TextView) findViewById(R.id.tv_04);
			TextView tv_06 = (TextView) findViewById(R.id.tv_06);
			TextView tv_07 = (TextView) findViewById(R.id.tv_07);
			TextView tv_10 = (TextView) findViewById(R.id.tv_10);
			TextView tv_11 = (TextView) findViewById(R.id.tv_11);
			if (marketPrice.getPlantType().toString().contains("planted")) {
				tv_01.setBackgroundResource(R.drawable.icon_seller_di);
			} else if (marketPrice.getPlantType().toString()
					.contains("transplant")) {
				tv_01.setBackgroundResource(R.drawable.icon_seller_yi);
			} else if (marketPrice.getPlantType().toString().contains("heelin")) {
				tv_01.setBackgroundResource(R.drawable.icon_seller_jia);
			} else if (marketPrice.getPlantType().toString()
					.contains("container")) {
				tv_01.setBackgroundResource(R.drawable.icon_seller_rong);
			} else {
				tv_01.setVisibility(View.GONE);
			}
			tv_title.setText(marketPrice.getName().toString());
			if ("".equals(marketPrice.getFirstTypeName().toString())) {
				tv_02.setText(marketPrice.getName().toString());
			} else {
				tv_02.setText("[" + marketPrice.getFirstTypeName().toString()
						+ "]" + marketPrice.getName().toString());
			}
			tv_03.setText("新增价格");
			tv_03.setTextColor(getResources().getColor(R.color.white));
			tv_03.setBackground(getResources()
					.getDrawable(R.drawable.r_o_green));
			tv_04.setText("规格：" + marketPrice.getSpecText().toString());
			if ("".equals(marketPrice.getQualityGradeName().toString())
					&& "".equals(marketPrice.getQualityTypeName().toString())) {
				tv_06.setText("品质：-");
			} else {
				if ("".equals(marketPrice.getQualityGradeName().toString())) {
					tv_06.setText("品质："
							+ marketPrice.getQualityTypeName().toString());
				} else {
					tv_06.setText("品质："
							+ marketPrice.getQualityTypeName().toString() + "("
							+ marketPrice.getQualityGradeName().toString()
							+ ")");
				}
			}

			tv_10.setText("地区：" + marketPrice.getCityName().toString());
			tv_07.setText(ValueGetInfo.doubleTrans1(marketPrice.getPrice()));
			initData();
		}

		mMaterialDialog = new MaterialDialog(this);

		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		tagView = (TagView) this.findViewById(R.id.tagview);
		tagView.setOnTagDeleteListener(new OnTagDeleteListener() {

			@Override
			public void onTagDeleted(int position, me.kaede.tagview.Tag tag) {
				// TODO Auto-generated method stub
				if (tag.id == 1) {
					cityCode = "";
					onRefresh();
				} else if (tag.id == 2) {
					name = "";
					onRefresh();
				} else if (tag.id == 3) {
					minSpec = "";
					maxSpec = "";
					onRefresh();
				} else if (tag.id == 4) {
					minHeight = "";
					maxHeight = "";
					onRefresh();
				} else if (tag.id == 5) {
					minCrown = "";
					maxCrown = "";
					onRefresh();
				}

			}
		});

		sellectPrice = new SellectPrice();
		Subscribe subscribe = new Subscribe();
		ArrayList<ParamsList> paramsLists = marketPrice.getParamsLists();
		subscribe.setParentName(marketPrice.getFirstTypeName());
		subscribe.setParamsLists(paramsLists);
		subscribe.setStr_plantTypeLists(marketPrice.getStr_plantTypeLists());
		subscribe.setStr_plantTypeList_ids_s(marketPrice
				.getStr_plantTypeList_ids_s());
		subscribe.setStr_qualityGradeLists(marketPrice
				.getStr_qualityGradeLists());
		subscribe.setStr_qualityGradeList_ids(marketPrice
				.getStr_qualityGradeList_ids());
		subscribe
				.setStr_qualityTypeLists(marketPrice.getStr_qualityTypeLists());
		subscribe.setStr_qualityTypeList_ids(marketPrice
				.getStr_qualityTypeList_ids());
		subscribe.setName(marketPrice.getName());
		subscribe.setId(marketPrice.getSecondSeedlingTypeId());
		subscribe.setParentId(marketPrice.getFirstSeedlingTypeId());
		sellectPrice.setSubscribe(subscribe);
		sellectPrice.setCityCode(marketPrice.getCityCode());
		sellectPrice.setCrown(marketPrice.getCrown() + "");
		sellectPrice.setDbh(marketPrice.getDbh() + "");
		sellectPrice.setDbhType(marketPrice.getDbhType());
		sellectPrice.setDiameter(marketPrice.getDiameter() + "");
		sellectPrice.setDiameterType(marketPrice.getDiameterType());
		sellectPrice.setFirstSeedlingTypeId(marketPrice
				.getFirstSeedlingTypeId());
		sellectPrice.setSecondSeedlingTypeId(marketPrice
				.getSecondSeedlingTypeId());
		sellectPrice.setHeight(marketPrice.getHeight() + "");
		sellectPrice.setLength(marketPrice.getLength() + "");
		sellectPrice.setName(marketPrice.getName());
		sellectPrice.setOffbarHeight(marketPrice.getOffbarHeight() + "");
		sellectPrice.setPlantType(marketPrice.getPlantType());
		sellectPrice.setQualityType(marketPrice.getQualityType());

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);

		tv_03.setOnClickListener(multipleClickProcess);
		rl_choose_type.setOnClickListener(multipleClickProcess);
		rl_choose_screen.setOnClickListener(multipleClickProcess);
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
					onBackPressed();
					break;
				case R.id.rl_choose_type:
					tv_my.setTextColor(getResources().getColor(
							R.color.main_color));
					tv_zoushi.setTextColor(getResources()
							.getColor(R.color.gray));
					if ("my".equals(chooseType)) {
						new ActionSheetDialog(MarketDetailActivity.this)
								.builder()
								.setCancelable(false)
								.setCanceledOnTouchOutside(false)
								.setTitle("筛选")
								.addSheetItem("全部价格", SheetItemColor.Blue,
										new OnSheetItemClickListener() {
											@Override
											public void onClick(int which) {
												selectType = "quanbu";
												tv_my.setText("全部价格");
												onRefresh();
											}
										})
								.addSheetItem("我的价格", SheetItemColor.Blue,
										new OnSheetItemClickListener() {
											@Override
											public void onClick(int which) {
												selectType = "wode";
												tv_my.setText("我的价格");
												onRefresh();
											}
										}).show();
					} else {
						xListView.setVisibility(View.VISIBLE);
						tv_notifaction.setVisibility(View.GONE);
					}
					chooseType = "my"; // quxian
					break;
				case R.id.rl_choose_screen:
					chooseType = "quxian";
					tv_zoushi.setTextColor(getResources().getColor(
							R.color.main_color));
					tv_my.setTextColor(getResources().getColor(R.color.gray));
					xListView.setVisibility(View.GONE);
					tv_notifaction.setVisibility(View.VISIBLE);
					tv_notifaction.setText("攻城狮正在努力开发中...");
					break;
				case R.id.tv_03:
					Intent intent = new Intent(MarketDetailActivity.this,
							SaveMarketPriceActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("SellectPrice", sellectPrice);
					bundle.putSerializable("MarketPrice", marketPrice);
					intent.putExtras(bundle);
					startActivityForResult(intent, 3);
					break;

				case R.id.id_tv_edit_all:
					Intent toSaveMarketPriceActivity = new Intent(
							MarketDetailActivity.this,
							SaveMarketPriceActivity.class);
					startActivity(toSaveMarketPriceActivity);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 9) {
			minHeight = data.getStringExtra("minHeight");
			maxHeight = data.getStringExtra("maxHeight");
			minCrown = data.getStringExtra("minCrown");
			maxCrown = data.getStringExtra("maxCrown");
			minSpec = data.getStringExtra("minSpec");
			maxSpec = data.getStringExtra("maxSpec");
			name = data.getStringExtra("name");
			List<Tag> tags = tagView.getTags();
			for (int i = 0; i < tags.size(); i++) {
				if (tags.get(i).id == 2 || tags.get(i).id == 3
						|| tags.get(i).id == 4 || tags.get(i).id == 5) {
					tagView.remove(i);
				}
			}
			if (!"".equals(name)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(name);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 2; // 1 城市 2.品名 3.规格 4.高度 5.冠幅
				tagView.addTag(tag);
			}
			if (!"".equals(minSpec) || !"".equals(maxSpec)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("规格："
						+ minSpec + "-" + maxSpec);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 3; // 1 城市 2.品名 3.规格 4.高度 5.冠幅
				tagView.addTag(tag);
			}
			if (!"".equals(minHeight) || !"".equals(maxHeight)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("高度："
						+ minHeight + "-" + maxHeight);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 4; // 1 城市 2.品名 3.规格 4.高度 5.冠幅
				tagView.addTag(tag);
			}
			if (!"".equals(minCrown) || !"".equals(maxCrown)) {
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag("冠幅："
						+ minCrown + "-" + maxCrown);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 5; // 1 城市 2.品名 3.规格 4.高度 5.冠幅
				tagView.addTag(tag);
			}
			onRefresh();
		} else if (resultCode == 8) {
			onRefresh();
		} else if (resultCode == 1) {
			onRefresh();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new PriceListAdapter(MarketDetailActivity.this,
					datas, sellectPrice, marketPrice);
			xListView.setAdapter(listAdapter);
		} else {
			listAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initData();
		}
		onLoad();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("createBy", MyApplication.Userinfo.getString("id", ""));
		params.put("marketId", marketPrice.getId());
		params.put("selectType", selectType);
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/seedlingMarketPrice/list",
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
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(data, "page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										Price hMap = new Price();
										hMap.setId(JsonGetInfo.getJsonString(
												jsonObject3, "id"));
										hMap.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject3,
														"createDate"));
										hMap.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject3,
														"createBy"));
										hMap.setImageUrl(JsonGetInfo
												.getJsonString(jsonObject3,
														"mediumImageUrl"));
										hMap.setPrice(JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.setStatus(JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.setStatusName(JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										hMap.setMarketId(JsonGetInfo
												.getJsonString(jsonObject3,
														"marketId"));
										hMap.setCreateUserType(JsonGetInfo
												.getJsonString(jsonObject3,
														"createUserType"));
										hMap.setUpdateUserType(JsonGetInfo
												.getJsonString(jsonObject3,
														"updateUserType"));
										hMap.setRemarks(JsonGetInfo
												.getJsonString(jsonObject3,
														"remarks"));
										ArrayList<Pic> seedlingTypeJson = new ArrayList<Pic>();
										JSONArray imagesJson = JsonGetInfo
												.getJsonArray(jsonObject3,
														"imagesJson");
										if (imagesJson.length() > 0) {
											for (int j = 0; j < imagesJson
													.length(); j++) {
												JSONObject images = imagesJson
														.getJSONObject(j);
												Pic pic = new Pic(
														JsonGetInfo
																.getJsonString(
																		images,
																		"id"),
														JsonGetInfo
																.getJsonBoolean(
																		images,
																		"isCover"),
														JsonGetInfo
																.getJsonString(
																		images,
																		"url"),
														JsonGetInfo.getJsonInt(
																images, "sort"));
												seedlingTypeJson.add(pic);
												if (j == 0) {
													hMap.setImageUrl(JsonGetInfo
															.getJsonString(
																	images,
																	"ossMediumImagePath"));
												}
											}
										}
										hMap.setSeedlingTypeJson(seedlingTypeJson);
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new PriceListAdapter(
												MarketDetailActivity.this,
												datas, sellectPrice,
												marketPrice);
										xListView.setAdapter(listAdapter);
										listAdapter
												.setOnGoodsCheckedChangeListener(new OnGoodsCheckedChangeListener() {

													@Override
													public void onGoodsCheckedChange(
															String id,
															boolean isRefresh) {
														// TODO Auto-generated
														// method stub
														if (isRefresh) {
															onRefresh();
														}

													}
												});
										// xListView
										// .setOnItemLongClickListener(new
										// OnItemLongClickListener() {
										//
										// @Override
										// public boolean onItemLongClick(
										// AdapterView<?> parent,
										// View view,
										// int position,
										// long id) {
										// // TODO Auto-generated
										// // method stub
										//
										// if ("1".equals(noteType)) {
										// i = position;
										// if (mMaterialDialog != null) {
										// mMaterialDialog
										// .setMessage(
										// "确定删除这条资源？")
										// //
										// mMaterialDialog.setBackgroundResource(R.drawable.background);
										// .setPositiveButton(
										// getString(R.string.ok),
										// new View.OnClickListener() {
										// @Override
										// public void onClick(
										// View v) {
										// }
										// })
										// .setNegativeButton(
										// getString(R.string.cancle),
										// new View.OnClickListener() {
										// public void onClick(
										// View v) {
										// mMaterialDialog
										// .dismiss();
										// }
										// })
										// .setCanceledOnTouchOutside(
										// true)
										// .setOnDismissListener(
										// new
										// DialogInterface.OnDismissListener() {
										// @Override
										// public void onDismiss(
										// DialogInterface dialog) {
										// }
										// })
										// .show();
										// } else {
										// }
										// } else {
										// Toast.makeText(
										// MarketDetailActivity.this,
										// "共享资源您没有权限删除哦",
										// Toast.LENGTH_SHORT)
										// .show();
										// }
										//
										// return false;
										// }
										// });
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
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.button31:
			noteType = "1";
			// 我的资源
			onRefresh();
			break;
		case R.id.button32:
			noteType = "2";
			// 共享资源
			onRefresh();
			break;
		default:
			// Nothing to do
		}
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
				cityCode = GetCodeByName.initProvinceDatas(
						MarketDetailActivity.this, mCurrentProviceName,
						mCurrentCityName);
				List<Tag> tags = tagView.getTags();
				for (int i = 0; i < tags.size(); i++) {
					if (tags.get(i).id == 1) {
						tagView.remove(i);
					}
				}
				me.kaede.tagview.Tag tag = new me.kaede.tagview.Tag(
						mCurrentProviceName);
				tag.layoutColor = getResources().getColor(R.color.main_color);
				tag.isDeletable = true;
				tag.id = 1; // 1 城市 2.品名 3.规格 4.高度 5.冠幅
				tagView.addTag(tag);
				onRefresh();
				if (!MarketDetailActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!MarketDetailActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!MarketDetailActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				MarketDetailActivity.this, mProvinceDatas));
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
				MarketDetailActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

}
