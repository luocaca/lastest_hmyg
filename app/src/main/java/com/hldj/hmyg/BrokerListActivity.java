package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barryzhang.temptyview.TViewUtil;
import com.hldj.hmyg.adapter.BrokerListAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.BuyOrderActivity2;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class BrokerListActivity extends NeedSwipeBackActivity implements
		IXListViewListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private BrokerListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	boolean loadItems;
	private String acceptStatus = "";
	private MultipleClickProcess multipleClickProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broker_list);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		multipleClickProcess = new MultipleClickProcess();
		acceptStatus = getIntent().getStringExtra("acceptStatus");
		loadItems = getIntent().getBooleanExtra("loadItems", false);
		if ("".equals(acceptStatus)) {
			tv_title.setText("全部验苗订单");
		} else if ("accepted".equals(acceptStatus)) {
			tv_title.setText("新验苗");
		} else if ("validating".equals(acceptStatus)) {
			tv_title.setText("验苗中");
		} else if ("auditing".equals(acceptStatus)) {
			tv_title.setText("审核中");
		} else if ("backed".equals(acceptStatus)) {
			tv_title.setText("被退回");
		} else if ("finished".equals(acceptStatus)) {
			tv_title.setText("已验苗");
		}

		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		btn_back.setOnClickListener(multipleClickProcess);
		initData();
		TViewUtil.EmptyViewBuilder.getInstance(BrokerListActivity.this)
				.setEmptyText(getResources().getString(R.string.nodata))
				.setEmptyTextSize(12).setEmptyTextColor(Color.GRAY)
				.setShowButton(false)
				.setActionText(getResources().getString(R.string.reload))
				.setAction(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						onRefresh();
					}
				}).setShowIcon(true).setShowText(true).bindView(xListView);
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("acceptStatus", acceptStatus);
		params.put("loadItems", loadItems + "");
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/agent/validateApply/list",
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									if (jsonArray.length() > 0) {
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject3 = jsonArray
													.getJSONObject(i);
											HashMap<String, Object> hMap = new HashMap<String, Object>();
											hMap.put("acceptStatus",
													acceptStatus);
											hMap.put("id", JsonGetInfo
													.getJsonString(jsonObject3,
															"id"));
											hMap.put("cityName", JsonGetInfo
													.getJsonString(jsonObject3,
															"cityName"));
											hMap.put("cityCode", JsonGetInfo
													.getJsonString(jsonObject3,
															"cityCode"));
											hMap.put("createDate", JsonGetInfo
													.getJsonString(jsonObject3,
															"createDate"));
											hMap.put("num", JsonGetInfo
													.getJsonString(jsonObject3,
															"num"));
											hMap.put("validatePrice",
													JsonGetInfo.getJsonDouble(
															jsonObject3,
															"validatePrice"));
											hMap.put("itemCountJson",
													JsonGetInfo.getJsonInt(
															jsonObject3,
															"itemCountJson"));
											hMap.put(
													"verifyedCountJson",
													JsonGetInfo
															.getJsonInt(
																	jsonObject3,
																	"verifyedCountJson"));
											JSONArray jsonArray_cartList = JsonGetInfo
													.getJsonArray(jsonObject3,
															"itemListJson");
											ArrayList<HashMap<String, Object>> cartList = new ArrayList<HashMap<String, Object>>();
											if (jsonArray_cartList.length() > 0) {
												for (int j = 0; j < jsonArray_cartList
														.length(); j++) {
													JSONObject jsonObject4 = jsonArray_cartList
															.getJSONObject(j);
													HashMap<String, Object> products_hash = new HashMap<String, Object>();
													products_hash
															.put("id",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"id"));
													products_hash
													.put("specText",
															JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"specText"));
													products_hash
															.put("acceptStatus",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"acceptStatus"));
													products_hash
															.put("name",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"name"));
													products_hash
															.put("imageUrl",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"largeImageUrl"));
													products_hash
															.put("cityName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"cityName"));
													products_hash
															.put("price",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"seedlingPrice"));
													products_hash
															.put("stockJson",
																	JsonGetInfo
																			.getJsonInt(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"seedlingJson"),
																					"stock"));
													products_hash
															.put("applyCount",
																	JsonGetInfo
																			.getJsonInt(
																					jsonObject4,
																					"applyCount"));
													products_hash
															.put("unitTypeName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"unitTypeName"));
													products_hash
													.put("plantType",
															JsonGetInfo
																	.getJsonString(
																			jsonObject4,
																			"plantType"));
													products_hash
															.put("diameter",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"diameter"));
													products_hash
															.put("height",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"height"));
													products_hash
															.put("dbh",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"dbh"));
													products_hash
															.put("crown",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"crown"));
													products_hash
															.put("realName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"sellerJson"),
																					"realName"));
													products_hash
															.put("companyName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"sellerJson"),
																					"companyName"));
													products_hash
															.put("publicName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"sellerJson"),
																					"publicName"));
													products_hash
															.put("status",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"status"));
													products_hash
															.put("statusName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"statusName"));
													products_hash
															.put("tagList",
																	JsonGetInfo
																			.getJsonArray(
																					jsonObject4,
																					"tagList")
																			.toString());//
													cartList.add(products_hash);

												}
											}
											hMap.put("cartList", cartList);

											datas.add(hMap);
											if (listAdapter != null) {
												listAdapter
														.notifyDataSetChanged();
											}
										}

										if (listAdapter == null) {
											listAdapter = new BrokerListAdapter(
													BrokerListActivity.this,
													datas);
											xListView.setAdapter(listAdapter);
										}

										pageIndex++;
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
						Toast.makeText(BrokerListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new BrokerListAdapter(BrokerListActivity.this, datas);
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
					Thread.sleep(Data.loading_time);
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
		if(resultCode==1){
			onRefresh();
		}	
		super.onActivityResult(requestCode, resultCode, data);
	}

}
