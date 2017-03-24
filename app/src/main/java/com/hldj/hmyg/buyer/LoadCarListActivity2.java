package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.Date;

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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyr.custom.MyDialog;
import com.dyr.custom.MyDialog.Dialogcallback;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.LoadCarListAdapter.OnCheckBoxCheckedLsitener;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zzy.flowers.widget.popwin.EditP2;

public class LoadCarListActivity2 extends NeedSwipeBackActivity implements
		IXListViewListener {
	private XListView xListView;
	private ArrayList<loadCarList> datas = new ArrayList<loadCarList>();

	boolean getdata; // 避免刷新多出数据
	private LoadCarListAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private MultipleClickProcess multipleClickProcess;
	private String status = "";
	private int mPositon;
	private String sourceId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadcar_list);
		status = getIntent().getStringExtra("status");
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		TextView tv_getProduct = (TextView) findViewById(R.id.tv_getProduct);
		TextView tv_change = (TextView) findViewById(R.id.tv_change);
		ll_add_car = (LinearLayout) findViewById(R.id.ll_add_car);
		if ("loaded".equals(status)) {
			tv_title.setText("已装车");
			tv_change.setVisibility(View.VISIBLE);
		} else if ("unreceipt".equals(status)) {
			tv_title.setText("待收货");
			tv_getProduct.setVisibility(View.VISIBLE);
			tv_change.setVisibility(View.VISIBLE);
		} else if ("receipted".equals(status)) {
			tv_title.setText("已收货");
			ll_add_car.setVisibility(View.GONE);
		}

		multipleClickProcess = new MultipleClickProcess();
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		btn_back.setOnClickListener(multipleClickProcess);
		tv_getProduct.setOnClickListener(multipleClickProcess);
		tv_change.setOnClickListener(multipleClickProcess);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		// params.put("orderId", id);
		params.put("status", status);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/loadCar/list",
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
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2, "data");
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										loadCarList lCarList = new loadCarList();
										lCarList.setId(JsonGetInfo
												.getJsonString(jsonObject4,
														"id"));
										lCarList.setRemarks(JsonGetInfo
												.getJsonString(jsonObject4,
														"remarks"));
										lCarList.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject4,
														"createBy"));
										lCarList.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"createDate"));
										lCarList.setCarNum(JsonGetInfo
												.getJsonString(jsonObject4,
														"carNum"));
										lCarList.setDeliveryId(JsonGetInfo
												.getJsonString(jsonObject4,
														"deliveryId"));
										lCarList.setDriverName(JsonGetInfo
												.getJsonString(jsonObject4,
														"driverName"));
										lCarList.setDriverPhone(JsonGetInfo
												.getJsonString(jsonObject4,
														"driverPhone"));
										lCarList.setBuyerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"buyerId"));
										lCarList.setStatus(JsonGetInfo
												.getJsonString(jsonObject4,
														"status"));
										lCarList.setStatusName(JsonGetInfo
												.getJsonString(jsonObject4,
														"statusName"));
										if ("loaded".equals(status)) {
											lCarList.setShowCheck(true);
										} else if ("unreceipt".equals(status)) {
											lCarList.setShowCheck(true);
										} else if ("receipted".equals(status)) {
											lCarList.setShowCheck(false);
										}
									
										lCarList.setShowItemListJson(false);
										JSONArray jsonArray = JsonGetInfo
												.getJsonArray(jsonObject4,
														"itemListJson");
										ArrayList<itemListJson> itemListJsons = new ArrayList<itemListJson>();
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject3 = jsonArray
													.getJSONObject(i);
											JSONObject orderJson = JsonGetInfo
													.getJSONObject(jsonObject3,
															"orderJson");
											JSONObject receiptJson = JsonGetInfo
													.getJSONObject(jsonObject3,
															"receiptJson");
											String num = JsonGetInfo
													.getJsonString(orderJson,
															"num");
											String orderName = JsonGetInfo
													.getJsonString(receiptJson,
															"orderName");
											String receiptDate = JsonGetInfo
													.getJsonString(receiptJson,
															"receiptDate");
											int loadCount = JsonGetInfo
													.getJsonInt(jsonObject3,
															"loadCount");
											itemListJson sItemListJson = new itemListJson();
											sItemListJson.setCount(loadCount);
											sItemListJson.setName(orderName);
											sItemListJson.setNum(num);
											sItemListJson
													.setReceiptDate(receiptDate);
											itemListJsons.add(sItemListJson);
										}
										lCarList.setItemListJsons(itemListJsons);
										datas.add(lCarList);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new LoadCarListAdapter(
												LoadCarListActivity2.this,
												datas);
										xListView.setAdapter(listAdapter);
										listAdapter
												.setOnCheckBoxCheckedLsitener(new OnCheckBoxCheckedLsitener() {

													@Override
													public void getChecked(
															int position) {
														mPositon = position;
														// submit.setOnClickListener(new
														// OnClickListener() {
														// @Override
														// public void
														// onClick(View v) {
														// // TODO
														// Auto-generated method
														// stub
														// int
														// mpositon=xListView.getCheckedItemPosition();
														// Toast.makeText(LoadCarListActivity.this,datas.get(mPositon).toString(),0).show();
														// }
														// });

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
						if (datas.size() == 0 && pageIndex == 0) {
							ll_add_car.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(LoadCarListActivity2.this,
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
			listAdapter = new LoadCarListAdapter(LoadCarListActivity2.this,
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
				case R.id.tv_getProduct:

					new ActionSheetDialog(LoadCarListActivity2.this)
							.builder()
							.setTitle("确认收货吗？")
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.addSheetItem("确认", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											sourceId = datas.get(mPositon)
													.getId();
											if ("".equals(sourceId)) {
												return;
											}
											receipt(sourceId);
										}
									}).show();

					break;
				case R.id.tv_change:
					sourceId = datas.get(mPositon).getId();
					if ("".equals(sourceId)) {
						return;
					}

					// 退换货申请内容
					Intent toSaveReturnApplyActivity = new Intent(
							LoadCarListActivity2.this,
							SaveReturnApplyActivity.class);
					toSaveReturnApplyActivity.putExtra("sourceId", sourceId);
					startActivityForResult(toSaveReturnApplyActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

					// MyDialog myDialog = new
					// MyDialog(LoadCarListActivity2.this);
					// myDialog.setContent("请填写退换货申请内容");
					// myDialog.setDialogCallback(dialogcallback);
					// myDialog.show();
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

	/**
	 * 设置mydialog需要处理的事情
	 */
	Dialogcallback dialogcallback = new Dialogcallback() {
		@Override
		public void dialogdo(String string) {
			saveReturnApply(sourceId, string);

		}
	};
	private LinearLayout ll_add_car;

	public void saveReturnApply(String orderIds, String content) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("sourceId", orderIds);
		params.put("content", content);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/loadCar/returnApply", params,
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
								onRefresh();

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
						Toast.makeText(LoadCarListActivity2.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	public void receipt(String id) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/loadCar/receipt",
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
								onRefresh();

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
						Toast.makeText(LoadCarListActivity2.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
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
