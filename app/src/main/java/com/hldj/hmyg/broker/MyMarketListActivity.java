package com.hldj.hmyg.broker;

import info.hoang8f.android.segmented.SegmentedGroup;

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
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.broker.adapter.MyMarketListAdapter;
import com.hldj.hmyg.broker.bean.MarketPrice;
import com.hldj.hmyg.broker.bean.SellectPrice;
import com.hldj.hmyg.jimiao.SellectMiaoActivity;
import com.hldj.hmyg.saler.bean.ParamsList;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;

@SuppressLint("NewApi")
public class MyMarketListActivity extends BaseSecondActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener,
		OnCheckedChangeListener, OnWheelChangedListener {

	private RelativeLayout rl_choose_type;
	private ImageView iv_seller_arrow2;
	private ImageView iv_seller_arrow3;
	private XListView xListView;
	private String orderBy = "";
	private String priceSort = "";
	private String publishDateSort = "";
	private ArrayList<MarketPrice> datas = new ArrayList<MarketPrice>();
	private int pageSize = 10;
	private int pageIndex = 0;
	private MyMarketListAdapter listAdapter;
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
	private SellectPrice sellectPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_market_list);
		sellectPrice = new SellectPrice();
		mMaterialDialog = new MaterialDialog(this);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
		RadioButton button31 = (RadioButton) findViewById(R.id.button31);
		RadioButton button32 = (RadioButton) findViewById(R.id.button32);
		button31.setChecked(true);
		segmented3.setOnCheckedChangeListener(this);
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		RelativeLayout rl_choose_price = (RelativeLayout) findViewById(R.id.rl_choose_price);
		RelativeLayout rl_choose_time = (RelativeLayout) findViewById(R.id.rl_choose_time);
		RelativeLayout rl_choose_screen = (RelativeLayout) findViewById(R.id.rl_choose_screen);
		RelativeLayout RelativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);

		TextView id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		iv_seller_arrow2 = (ImageView) findViewById(R.id.iv_seller_arrow2);
		iv_seller_arrow3 = (ImageView) findViewById(R.id.iv_seller_arrow3);
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
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		rl_choose_type.setOnClickListener(multipleClickProcess);
		rl_choose_price.setOnClickListener(multipleClickProcess);
		rl_choose_time.setOnClickListener(multipleClickProcess);
		rl_choose_screen.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		RelativeLayout2.setOnClickListener(multipleClickProcess);

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
				case R.id.id_tv_edit_all:
					Intent toSaveMarketPriceActivity = new Intent(
							MyMarketListActivity.this,
							SaveMarketPriceActivity.class);
					startActivity(toSaveMarketPriceActivity);
					break;
				case R.id.rl_choose_type:
					showCitys();
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
					onRefresh();
					break;
				case R.id.rl_choose_time:
					if ("".equals(publishDateSort)) {
						publishDateSort = "createDate_asc";
						iv_seller_arrow3
								.setImageResource(R.drawable.icon_seller_arrow2);
					} else if ("createDate_asc".equals(publishDateSort)) {
						publishDateSort = "createDate_desc";
						iv_seller_arrow3
								.setImageResource(R.drawable.icon_seller_arrow3);
					} else if ("createDate_desc".equals(publishDateSort)) {
						publishDateSort = "";
						iv_seller_arrow3
								.setImageResource(R.drawable.icon_seller_arrow1);
					}
					onRefresh();
					break;
				case R.id.RelativeLayout2:
					Intent toSellectActivity = new Intent(
							MyMarketListActivity.this,
							SellectMarketPriceActivity.class);
					toSellectActivity.putExtra("sellectPrice", sellectPrice);
					startActivityForResult(toSellectActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
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
			onRefresh();
		} else if (resultCode == 8) {
			onRefresh();
		} else if (resultCode == 16) {
			if (data.getExtras() != null) {
				Bundle extras = data.getExtras();
				sellectPrice = (SellectPrice) extras.get("sellectPrice");
			}
			onRefresh();
		} else if (resultCode == 18) {
			Intent toSellectActivity = new Intent(MyMarketListActivity.this,
					SellectMarketPriceActivity.class);
			sellectPrice = new SellectPrice();
			toSellectActivity.putExtra("sellectPrice", sellectPrice);
			startActivityForResult(toSellectActivity, 1);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
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
			listAdapter = new MyMarketListAdapter(MyMarketListActivity.this,
					datas);
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
		params.put("cityCode", sellectPrice.getCityCode());
		params.put("secondSeedlingTypeId",
				sellectPrice.getSecondSeedlingTypeId());
		params.put("firstSeedlingTypeId", sellectPrice.getFirstSeedlingTypeId());
		params.put("name", sellectPrice.getName());
		params.put("diameter", sellectPrice.getDiameter());
		params.put("diameterType", sellectPrice.getDiameterType());
		params.put("dbh", sellectPrice.getDbh());
		params.put("dbhType", sellectPrice.getDbhType());
		params.put("crown", sellectPrice.getCrown());
		params.put("offbarHeight", sellectPrice.getOffbarHeight());
		params.put("height", sellectPrice.getHeight());
		params.put("length", sellectPrice.getLength());
		params.put("plantType", sellectPrice.getPlantType());
		params.put("qualityType", sellectPrice.getQualityType());
		params.put("qualityGrade", sellectPrice.getQualityGrade());
		
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedlingMarket/list",
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
										MarketPrice hMap = new MarketPrice();
										hMap.setFirstSeedlingTypeId(JsonGetInfo.getJsonString(
												jsonObject3, "firstSeedlingTypeId"));
										hMap.setSecondSeedlingTypeId(JsonGetInfo.getJsonString(
												jsonObject3, "secondSeedlingTypeId"));
										hMap.setId(JsonGetInfo.getJsonString(
												jsonObject3, "id"));
										hMap.setFirstTypeName(JsonGetInfo.getJsonString(
												jsonObject3, "firstTypeName"));
										hMap.setQualityGrade(JsonGetInfo.getJsonString(
												jsonObject3, "qualityGrade"));
										hMap.setQualityGradeName(JsonGetInfo.getJsonString(
												jsonObject3, "qualityGradeName"));
										hMap.setName(JsonGetInfo.getJsonString(
												jsonObject3, "name"));
										hMap.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject3,
														"createDate"));
										hMap.setSpecText(JsonGetInfo
												.getJsonString(jsonObject3,
														"specText"));
										hMap.setImageUrl(JsonGetInfo
												.getJsonString(jsonObject3,
														"mediumImageUrl"));
										hMap.setPlantType(JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.setPlantTypeName(JsonGetInfo
												.getJsonString(jsonObject3,
														"plantTypeName"));
										hMap.setQualityType(JsonGetInfo
												.getJsonString(jsonObject3,
														"qualityType"));
										hMap.setQualityTypeName(JsonGetInfo
												.getJsonString(jsonObject3,
														"qualityTypeName"));
										// hMap.setPrice(JsonGetInfo
										// .getJsonDouble(JsonGetInfo.getJSONObject(jsonObject3,
										// "marketPriceJson"),
										// "price"));
										hMap.setPrice(JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.setCount(JsonGetInfo.getJsonInt(
												jsonObject3, "count"));
										hMap.setPublish(JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"isPublish"));
										hMap.setCityCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"cityCode"));
										if("".equals(JsonGetInfo
												.getJsonString(JsonGetInfo.getJSONObject(jsonObject3, "ciCity"),
														"fullName"))){
											hMap.setCityName(JsonGetInfo
													.getJsonString(jsonObject3,
															"cityName"));
										}else {
											hMap.setCityName(JsonGetInfo
													.getJsonString(JsonGetInfo.getJSONObject(jsonObject3, "ciCity"),
															"fullName"));
										}
										hMap.setDiameterType(JsonGetInfo
												.getJsonString(jsonObject3,
														"diameter"));
										hMap.setDbhType(JsonGetInfo
												.getJsonString(jsonObject3,
														"dbhType"));
										hMap.setDiameter(JsonGetInfo
												.getJsonInt(jsonObject3,
														"diameter"));
										hMap.setDbh(JsonGetInfo.getJsonInt(
												jsonObject3, "dbh"));
										hMap.setCrown(JsonGetInfo.getJsonInt(
												jsonObject3, "crown"));
										hMap.setOffbarHeight(JsonGetInfo
												.getJsonInt(jsonObject3,
														"offbarHeight"));
										hMap.setHeight(JsonGetInfo.getJsonInt(
												jsonObject3, "height"));
										hMap.setLength(JsonGetInfo.getJsonInt(
												jsonObject3, "length"));
										ArrayList<String> str_plantTypeLists = new ArrayList<String>();
										ArrayList<String> str_plantTypeList_ids_s = new ArrayList<String>();
										ArrayList<String> str_qualityTypeLists = new ArrayList<String>();
										ArrayList<String> str_qualityTypeList_ids = new ArrayList<String>();
										ArrayList<String> str_qualityGradeLists = new ArrayList<String>();
										ArrayList<String> str_qualityGradeList_ids = new ArrayList<String>();
										ArrayList<ParamsList> paramsLists = new ArrayList<ParamsList>();
										JSONArray paramsList = JsonGetInfo
												.getJsonArray(JsonGetInfo.getJSONObject(jsonObject3, "firstSeedlingTypeJson"),
														"paramsList");
										JSONArray qualityTypeList = JsonGetInfo
												.getJsonArray(JsonGetInfo.getJSONObject(jsonObject3, "firstSeedlingTypeJson"),
														"qualityTypeList");
										JSONArray plantTypeList = JsonGetInfo
												.getJsonArray(JsonGetInfo.getJSONObject(jsonObject3, "firstSeedlingTypeJson"),
														"plantTypeList");
										JSONArray qualityGradeList = JsonGetInfo
												.getJsonArray(JsonGetInfo.getJSONObject(jsonObject3, "firstSeedlingTypeJson"),
														"qualityGradeList");
										for (int j = 0; j < paramsList.length(); j++) {
											JSONObject jsonObject4 = paramsList
													.getJSONObject(j);
											paramsLists.add(new ParamsList(
													JsonGetInfo.getJsonString(
															jsonObject4, "value"),
													JsonGetInfo
															.getJsonBoolean(
																	jsonObject4,
																	"required")));
										}
										for (int o = 0; o < qualityTypeList
												.length(); o++) {
											JSONObject qualityType = qualityTypeList
													.getJSONObject(o);
											str_qualityTypeLists.add(JsonGetInfo
													.getJsonString(qualityType,
															"text"));
											str_qualityTypeList_ids.add(JsonGetInfo
													.getJsonString(qualityType,
															"value"));
										}
										for (int p = 0; p < plantTypeList.length(); p++) {
											JSONObject plantType1 = plantTypeList
													.getJSONObject(p);
											str_plantTypeLists.add(JsonGetInfo
													.getJsonString(plantType1,
															"text"));
											str_plantTypeList_ids_s.add(JsonGetInfo
													.getJsonString(plantType1,
															"value"));
										}
										for (int q = 0; q < qualityGradeList
												.length(); q++) {
											JSONObject qualityGrade = qualityGradeList
													.getJSONObject(q);
											str_qualityGradeLists.add(JsonGetInfo
													.getJsonString(qualityGrade,
															"text"));
											str_qualityGradeList_ids
													.add(JsonGetInfo.getJsonString(
															qualityGrade, "value"));
										}
										hMap.setStr_plantTypeLists(str_plantTypeLists);
										hMap.setStr_plantTypeList_ids_s(str_plantTypeList_ids_s);
										hMap.setStr_qualityGradeLists(str_qualityGradeLists);
										hMap.setStr_qualityGradeList_ids(str_qualityGradeList_ids);
										hMap.setStr_qualityTypeLists(str_qualityTypeLists);
										hMap.setStr_qualityTypeList_ids(str_qualityTypeList_ids);
										hMap.setParamsLists(paramsLists);
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
										// String priceDate;
										hMap.setPriceDate(new Date(JsonGetInfo
												.getJsonLong(jsonObject3,
														"priceDate"))
												.toString());

										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new MyMarketListAdapter(
												MyMarketListActivity.this,
												datas);
										xListView.setAdapter(listAdapter);

										xListView
												.setOnItemLongClickListener(new OnItemLongClickListener() {

													@Override
													public boolean onItemLongClick(
															AdapterView<?> parent,
															View view,
															int position,
															long id) {
														// TODO Auto-generated
														// method stub

														if ("1".equals(noteType)) {
															i = position;
															if (mMaterialDialog != null) {
																mMaterialDialog
																		.setMessage(
																				"确定删除这条资源？")
																		// mMaterialDialog.setBackgroundResource(R.drawable.background);
																		.setPositiveButton(
																				getString(R.string.ok),
																				new View.OnClickListener() {
																					@Override
																					public void onClick(
																							View v) {
																					}
																				})
																		.setNegativeButton(
																				getString(R.string.cancle),
																				new View.OnClickListener() {
																					public void onClick(
																							View v) {
																						mMaterialDialog
																								.dismiss();
																					}
																				})
																		.setCanceledOnTouchOutside(
																				true)
																		.setOnDismissListener(
																				new DialogInterface.OnDismissListener() {
																					@Override
																					public void onDismiss(
																							DialogInterface dialog) {
																					}
																				})
																		.show();
															} else {
															}
														} else {
															Toast.makeText(
																	MyMarketListActivity.this,
																	"共享资源您没有权限删除哦",
																	Toast.LENGTH_SHORT)
																	.show();
														}

														return false;
													}
												});
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

	private void doDel(String id, final int pos) {

		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedlingNote/doDel",
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
								datas.remove(pos);
								listAdapter.notify(datas);
							} else {
								Toast.makeText(MyMarketListActivity.this,
										"删除失败", Toast.LENGTH_SHORT).show();
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
						MyMarketListActivity.this, mCurrentProviceName,
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
				if (!MyMarketListActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!MyMarketListActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!MyMarketListActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				MyMarketListActivity.this, mProvinceDatas));
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
				MyMarketListActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

}
