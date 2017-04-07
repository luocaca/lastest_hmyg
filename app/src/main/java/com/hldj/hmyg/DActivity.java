package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.hldj.hmyg.adapter.CartListAdapter;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class DActivity extends Activity implements IXListViewListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata = true; // 避免刷新多出数据
	private CartListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_d);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		// initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/cart/list", params,
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
								Toast.makeText(DActivity.this, msg,
										Toast.LENGTH_SHORT).show();
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
											hMap.put("isCheck", false);
											hMap.put("cityName", JsonGetInfo
													.getJsonString(jsonObject3,
															"cityName"));
											hMap.put("cityCode", JsonGetInfo
													.getJsonString(jsonObject3,
															"cityCode"));
											hMap.put("totalPrice", JsonGetInfo
													.getJsonDouble(jsonObject3,
															"totalPrice"));
											JSONArray jsonArray_cartList = JsonGetInfo
													.getJsonArray(jsonObject3,
															"cartList");
											ArrayList<HashMap<String, Object>> cartList = new ArrayList<HashMap<String, Object>>();

											if (jsonArray_cartList.length() > 0) {
												for (int j = 0; j < jsonArray_cartList
														.length(); j++) {
													JSONObject jsonObject4 = JsonGetInfo.getJSONObject(
															jsonArray_cartList
																	.getJSONObject(j),
															"seedling");
													HashMap<String, Object> products_hash = new HashMap<String, Object>();
													products_hash.put(
															"isCheck", false);
													products_hash.put(
															"show_type",
															"seedling_list");
													products_hash
															.put("id",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"id"));
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
																					"price"));
													products_hash
															.put("count",
																	JsonGetInfo
																			.getJsonInt(
																					jsonObject4,
																					"count"));
													products_hash
															.put("unitTypeName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"unitTypeName"));
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
															.put("crown",
																	JsonGetInfo
																			.getJsonDouble(
																					jsonObject4,
																					"crown"));
													products_hash
															.put("cityName",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"cityName"));
													products_hash
															.put("fullName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ciCity"),
																					"fullName"));
													products_hash
															.put("ciCity_name",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ciCity"),
																					"name"));
													products_hash
															.put("realName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ownerJson"),
																					"realName"));
													products_hash
															.put("companyName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ownerJson"),
																					"companyName"));
													products_hash
															.put("publicName",
																	JsonGetInfo
																			.getJsonString(
																					JsonGetInfo
																							.getJSONObject(
																									jsonObject4,
																									"ownerJson"),
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
															.put("closeDate",
																	JsonGetInfo
																			.getJsonString(
																					jsonObject4,
																					"closeDate"));
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
											listAdapter = new CartListAdapter(
													DActivity.this, datas);
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
						Toast.makeText(DActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
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
			listAdapter = new CartListAdapter(DActivity.this, datas);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		onRefresh();
		super.onResume();
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

}
