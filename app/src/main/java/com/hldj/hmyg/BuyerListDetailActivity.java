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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.adapter.BuyerProductListAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class BuyerListDetailActivity extends NeedSwipeBackActivity implements
		IXListViewListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private BuyerProductListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private MultipleClickProcess multipleClickProcess;
	private String id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broker_list);
		id = getIntent().getStringExtra("id");
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		multipleClickProcess = new MultipleClickProcess();
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		btn_back.setOnClickListener(multipleClickProcess);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/validateApply/listItems", params,
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
								Toast.makeText(BuyerListDetailActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"itemList");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2, "data");

									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										HashMap<String, Object> products_hash = new HashMap<String, Object>();
										products_hash.put("id", JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										products_hash.put("acceptStatus", JsonGetInfo
												.getJsonString(jsonObject4,
														"acceptStatus"));
										products_hash.put("name", JsonGetInfo
												.getJsonString(jsonObject4,
														"name"));
										products_hash.put("imageUrl",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"largeImageUrl"));
										products_hash.put("cityName",
												JsonGetInfo
														.getJsonString(
																jsonObject4,
																"cityName"));
										products_hash.put("price", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"seedlingPrice"));
										products_hash.put("stockJson",
												JsonGetInfo.getJsonInt(
														jsonObject4,
														"stockJson"));
										products_hash.put("applyCount",
												JsonGetInfo.getJsonInt(
														jsonObject4,
														"applyCount"));
										products_hash.put("unitTypeName",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"unitTypeName"));
										products_hash.put("diameter",
												JsonGetInfo
														.getJsonDouble(
																jsonObject4,
																"diameter"));
										products_hash.put("dbh", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"dbh"));
										products_hash.put("height", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"height"));
										products_hash.put("crown", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"crown"));
										products_hash.put(
												"realName",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ownerJson"),
														"realName"));
										products_hash.put(
												"companyName",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ownerJson"),
														"companyName"));
										products_hash.put(
												"publicName",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"ownerJson"),
														"publicName"));
										products_hash.put("status", JsonGetInfo
												.getJsonString(jsonObject4,
														"status"));
										products_hash.put("statusName",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"statusName"));
										datas.add(products_hash);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new BuyerProductListAdapter(
												BuyerListDetailActivity.this,
												datas);
										xListView.setAdapter(listAdapter);
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
						Toast.makeText(BuyerListDetailActivity.this,
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
			listAdapter = new BuyerProductListAdapter(
					BuyerListDetailActivity.this, datas);

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

}
