package com.hldj.hmyg.saler;

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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class ManagerQuoteListActivity extends NeedSwipeBackActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private ManagerQuoteListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private String status = "";
	private View mainView;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	// 报价管理列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_quote_list);
		RelativeLayout rl_01 = (RelativeLayout) findViewById(R.id.rl_01);
		RelativeLayout rl_02 = (RelativeLayout) findViewById(R.id.rl_02);
		RelativeLayout rl_03 = (RelativeLayout) findViewById(R.id.rl_03);
		RelativeLayout rl_04 = (RelativeLayout) findViewById(R.id.rl_04);
		RelativeLayout rl_05 = (RelativeLayout) findViewById(R.id.rl_05);
		RelativeLayout rl_06 = (RelativeLayout) findViewById(R.id.rl_06);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		mainView = (View) findViewById(R.id.mainView);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		if (getIntent().getStringExtra("status") != null) {
			status = getIntent().getStringExtra("status");
		}
		if ("".equals(status)) {
			all();
		} else if ("unaudit".equals(status)) {
			quoted();
		} else if ("published".equals(status)) {
			invalid();
		} 
		initData();
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		rl_01.setOnClickListener(multipleClickProcess);
		rl_02.setOnClickListener(multipleClickProcess);
		rl_03.setOnClickListener(multipleClickProcess);
		rl_04.setOnClickListener(multipleClickProcess);
		rl_05.setOnClickListener(multipleClickProcess);
		rl_06.setOnClickListener(multipleClickProcess);

	}
	
	
	private void invalid() {
		// TODO Auto-generated method stub
		status = "invalid";
		tv_03.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void quoted() {
		// TODO Auto-generated method stub
		status = "quoted";
		tv_02.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void all() {
		// TODO Auto-generated method stub
		status = "";
		tv_01.setTextColor(getResources().getColor(R.color.main_color));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
		tv_06.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("status", status);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/list", params,
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject4 = jsonArray
												.getJSONObject(i);
										JSONObject jsonObject3 = JsonGetInfo
												.getJSONObject(jsonArray
														.getJSONObject(i),
														"purchaseItemJson");
										JSONObject purchaseJson = JsonGetInfo
												.getJSONObject(jsonArray
														.getJSONObject(i),
														"purchaseJson");
										JSONObject buyer = JsonGetInfo
												.getJSONObject(purchaseJson,
														"buyer");
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										hMap.put("remarks", JsonGetInfo
												.getJsonString(jsonObject4,
														"remarks"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject4,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject4,
														"statusName"));
										hMap.put("createDate", JsonGetInfo
												.getJsonString(jsonObject4,
														"createDate"));
										hMap.put("closeDate", JsonGetInfo
												.getJsonString(jsonObject4,
														"closeDate"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject4,
														"count"));
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject4,
														"price"));
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
										hMap.put(
												"count_purchaseItemJson",
												JsonGetInfo
														.getJsonInt(
																jsonObject3,
																"count_purchaseItemJson"));
										JSONObject ciCity = JsonGetInfo
												.getJSONObject(purchaseJson,
														"ciCity");
										hMap.put("fullName", JsonGetInfo
												.getJsonString(ciCity,
														"fullName"));
										hMap.put("num", JsonGetInfo
												.getJsonString(purchaseJson,
														"num"));
										hMap.put("receiptDate", JsonGetInfo
												.getJsonString(purchaseJson,
														"receiptDate"));
										hMap.put("closeDate", JsonGetInfo
												.getJsonString(purchaseJson,
														"closeDate"));

										String publicName = JsonGetInfo
												.getJsonString(buyer,
														"publicName");
										String realName = JsonGetInfo
												.getJsonString(buyer,
														"realName");
										String companyName = JsonGetInfo
												.getJsonString(buyer,
														"companyName");
										String publicPhone = JsonGetInfo
												.getJsonString(buyer,
														"publicPhone");
										String address = JsonGetInfo
												.getJsonString(purchaseJson,
														"cityName");
										hMap.put("publicName", publicName);
										hMap.put("realName", realName);
										hMap.put("companyName", companyName);
										hMap.put("publicPhone", publicPhone);
										hMap.put("address", address);
										hMap.put("quoteCountJson", JsonGetInfo
												.getJsonInt(jsonObject3,
														"quoteCountJson"));
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new ManagerQuoteListAdapter(
												ManagerQuoteListActivity.this,
												datas, mainView);
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
						Toast.makeText(ManagerQuoteListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
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
				case R.id.rl_01:
					all();
					onRefresh();
					break;
				case R.id.rl_02:
					quoted();
					onRefresh();
					break;
				case R.id.rl_03:
					invalid();
					onRefresh();
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
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new ManagerQuoteListAdapter(
					ManagerQuoteListActivity.this, datas, mainView);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
