package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.adapter.BrokerSendProductListAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.AddCarActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class BrokerOrderDetailActivity extends NeedSwipeBackActivity {

	private String id = "";
	private ListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private BrokerSendProductListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private TextView tv_07;
	private TextView tv_06;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broker_order_detail);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		TextView tv_status = (TextView) findViewById(R.id.tv_status);
		TextView tv_01 = (TextView) findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		tv_07 = (TextView) findViewById(R.id.tv_07);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_add_car_detail = (LinearLayout) findViewById(R.id.ll_add_car_detail);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_add_car = (LinearLayout) findViewById(R.id.ll_add_car);
		tv_title.setText(getIntent().getStringExtra("num"));
		id = getIntent().getStringExtra("id");
		if ("unsend".equals(getIntent().getStringExtra("status"))) {
			tv_status.setText("待发货");
			ll_add_car.setVisibility(View.VISIBLE);
		} else if ("unreceipt".equals(getIntent().getStringExtra("status"))) {
			tv_status.setText("待收货");
		} else if ("finished".equals(getIntent().getStringExtra("status"))) {
			tv_status.setText("已完成");
		}
		tv_01.setText("发货品种："
				+ getIntent().getStringExtra("recriptOrderNameText"));
		tv_02.setText("收货日期：" + getIntent().getStringExtra("receiptDate"));
		tv_03.setText("买家信息");

		if (!"".equals(getIntent().getStringExtra("displayName"))) {
			tv_04.setText("买家：" + getIntent().getStringExtra("displayName"));
		}
		if (!"".equals(getIntent().getStringExtra("displayPhone"))) {
			tv_05.setText("电话：" + getIntent().getStringExtra("displayPhone"));
		}
		xListView = (ListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		initData();
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ll_01.setOnClickListener(multipleClickProcess);
		ll_add_car_detail.setOnClickListener(multipleClickProcess);
		ll_add_car.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		// params.put("pageSize", pageSize + "");
		// params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/agent/delivery/detail",
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
								// Toast.makeText(BrokerSendProductsActivity.this,
								// msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"delivery");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"receiptListJson").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2,
													"receiptListJson");
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										HashMap<String, Object> products_hash = new HashMap<String, Object>();
										products_hash.put("id", JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										products_hash.put("orderNum",
												JsonGetInfo
														.getJsonString(
																jsonObject4,
																"orderNum"));
										products_hash.put("orderName",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"orderName"));
										products_hash.put("specText",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"specText"));
										products_hash.put("receiptDate",
												JsonGetInfo.getJsonString(
														jsonObject4,
														"receiptDate"));
										products_hash.put("count", JsonGetInfo
												.getJsonInt(jsonObject4,
														"count"));
										products_hash.put("loadedCountJson",
												JsonGetInfo.getJsonInt(
														jsonObject4,
														"loadedCountJson"));

										products_hash.put(
												"contactName",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"receiptAddressJson"),
														"contactName"));
										products_hash
												.put("contactPhone",
														JsonGetInfo.getJsonString(
																JsonGetInfo
																		.getJSONObject(
																				jsonObject4,
																				"receiptAddressJson"),
																"contactPhone"));
										products_hash.put(
												"fullAddress",
												JsonGetInfo.getJsonString(
														JsonGetInfo
																.getJSONObject(
																		jsonObject4,
																		"receiptAddressJson"),
														"fullAddress"));
										datas.add(products_hash);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new BrokerSendProductListAdapter(
												BrokerOrderDetailActivity.this,
												datas);
										xListView.setAdapter(listAdapter);
									}
									if (datas.size() > 0) {
										tv_06.setVisibility(View.VISIBLE);
										tv_07.setVisibility(View.GONE);
									} else {
										tv_07.setVisibility(View.VISIBLE);
										tv_06.setVisibility(View.GONE);
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
						Toast.makeText(BrokerOrderDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
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
					Intent toBrokerSendProductsActivity = new Intent(
							BrokerOrderDetailActivity.this,
							BrokerSendProductsActivity.class);
					toBrokerSendProductsActivity.putExtra("id", id);
					startActivity(toBrokerSendProductsActivity);
					break;
				case R.id.ll_add_car_detail:
					Intent toBrokerSendCarDetailActivity = new Intent(
							BrokerOrderDetailActivity.this,
							BrokerSendCarDetailActivity.class);
					toBrokerSendCarDetailActivity.putExtra("id", id);
					startActivity(toBrokerSendCarDetailActivity);
					break;

				case R.id.ll_add_car:
					Intent toAddCarActivity = new Intent(
							BrokerOrderDetailActivity.this,
							AddCarActivity.class);
					toAddCarActivity.putExtra("id", id);
					startActivityForResult(toAddCarActivity, 1);
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
					Thread.sleep(Data.loading_time);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
