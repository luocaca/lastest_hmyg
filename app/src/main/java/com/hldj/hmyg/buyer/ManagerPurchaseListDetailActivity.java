package com.hldj.hmyg.buyer;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.ManagerPurchaseListDetailAdapter;
import com.hldj.hmyg.adapter.SerializableHashMaplist;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class ManagerPurchaseListDetailActivity extends NeedSwipeBackActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener {
	private XListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private ManagerPurchaseListDetailAdapter listAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private String id = "";
	private String projectName = "";
	private View mainView;
	private Button edit_btn;
	private String purchaseId;
	private HashMap<String, Object> hashMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_purchase_list_detail);
		projectName = getIntent().getStringExtra("projectName");
		id = getIntent().getStringExtra("id");
		Bundle bundle = getIntent().getExtras();
		SerializableHashMaplist serializableMap = (SerializableHashMaplist) bundle
				.get("map");
		hashMap = serializableMap.getHashMap();
		View inflate = getLayoutInflater().inflate(
				R.layout.list_item_manager_purchase, null);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		ImageView iv_like = (ImageView) inflate.findViewById(R.id.iv_like);
		TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
		TextView tv_ac = (TextView) inflate.findViewById(R.id.tv_ac);
		TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
		TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
		TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
		TextView tv_06 = (TextView) inflate.findViewById(R.id.tv_06);
		TextView tv_07 = (TextView) inflate.findViewById(R.id.tv_07);
		TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
		TextView tv_09 = (TextView) inflate.findViewById(R.id.tv_09);
		TextView tv_caozuo01 = (TextView) inflate
				.findViewById(R.id.tv_caozuo01);
		TextView tv_caozuo02 = (TextView) inflate
				.findViewById(R.id.tv_caozuo02);
		TextView tv_caozuo03 = (TextView) inflate
				.findViewById(R.id.tv_caozuo03);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		edit_btn = (Button) findViewById(R.id.edit_btn);
		mainView = (View) findViewById(R.id.mainView);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		xListView.setXListViewListener(this);
		xListView.addHeaderView(inflate);
		tv_caozuo01.setVisibility(View.GONE);
		tv_caozuo02.setVisibility(View.GONE);
		tv_caozuo03.setVisibility(View.GONE);
		tv_ac.setText(hashMap.get("statusName").toString());
		tv_01.setText("项目名称："
				+ hashMap.get("projectName").toString());
		tv_02.setText("采购单号：" + hashMap.get("num").toString());
		tv_04.setText("采购地：" + hashMap.get("cityName").toString());
		tv_03.setText("发布日期："
				+ hashMap.get("publishDate").toString());
		tv_07.setText("截止日期" + hashMap.get("closeDate").toString());
		tv_05.setText("收货日期："
				+ hashMap.get("receiptDate").toString());
		if (hashMap.get("needInvoice").toString().contains("true")) {
			tv_06.setText("发票要求:需要发票");
		} else {
			tv_06.setText("发票要求:不需要发票");
		}
		tv_08.setText("采购品种："
				+ hashMap.get("itemCountJson").toString());
		tv_09.setText("报价总数："
				+ hashMap.get("quoteCountJson").toString());

		if ("unsubmit".equals(hashMap.get("status").toString())
				&& "unaudit"
						.equals(hashMap.get("status").toString())) {
			tv_caozuo01.setText("剩余天数："
					+ hashMap.get("lastDays").toString());
		}

		if ("unsubmit".equals(hashMap.get("status").toString())
				|| "backed".equals(hashMap.get("status").toString())) {
			tv_caozuo02.setText("编辑");
			tv_caozuo03.setText("删除");
			if ("unsubmit".equals(hashMap.get("status").toString())) {
				tv_ac.setTextColor(Color.parseColor("#ff6601"));
			} else if ("backed".equals(hashMap.get("status")
					.toString())) {
				tv_ac.setTextColor(Color.parseColor("#ff0000"));
			}

		} else if ("unaudit"
				.equals(hashMap.get("status").toString())) {
			tv_caozuo02.setVisibility(View.GONE);
			tv_caozuo03.setText("撤回");
			tv_ac.setTextColor(Color.parseColor("#179bed"));
		} else if ("published".equals(hashMap.get("status")
				.toString())) {
			tv_caozuo02.setText("关闭");
			tv_caozuo03.setText("延期");
			tv_ac.setTextColor(Color.parseColor("#00843c"));
		} else if ("expired"
				.equals(hashMap.get("status").toString())) {
			tv_caozuo02.setVisibility(View.GONE);
			tv_caozuo03.setText("延期");
			tv_ac.setTextColor(Color.parseColor("#4caf50"));
		} else if ("closed".equals(hashMap.get("status").toString())) {
			tv_caozuo02.setVisibility(View.GONE);
			tv_caozuo03.setVisibility(View.GONE);
			tv_ac.setTextColor(Color.parseColor("#999999"));
			edit_btn.setVisibility(View.GONE);

		}
		
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		edit_btn.setOnClickListener(multipleClickProcess);

	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/detail", params,
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
												"purchase");
								 purchaseId = JsonGetInfo
									.getJsonString(jsonObject2,
											"id");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"itemListJson").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2, "itemListJson");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										HashMap<String, Object> hMap = new HashMap<String, Object>();
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
										hMap.put("firstSeedlingTypeId", JsonGetInfo
												.getJsonString(jsonObject3,
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
										hMap.put("dbh", JsonGetInfo
												.getJsonInt(jsonObject3,
														"dbh"));
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
										hMap.put("status",hashMap.get("status").toString());
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new ManagerPurchaseListDetailAdapter(
												ManagerPurchaseListDetailActivity.this,
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
						Toast.makeText(ManagerPurchaseListDetailActivity.this,
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
				case R.id.edit_btn:
					if(purchaseId!=null){
						Intent toAddPurchaseActivity = new Intent(
								ManagerPurchaseListDetailActivity.this, AddPurchaseActivity.class);
						toAddPurchaseActivity.putExtra("id",
								"");
						toAddPurchaseActivity.putExtra("purchaseId",
								purchaseId);
						startActivityForResult(toAddPurchaseActivity, 1);
						overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_right);
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (listAdapter == null) {
			listAdapter = new ManagerPurchaseListDetailAdapter(
					ManagerPurchaseListDetailActivity.this, datas, mainView);
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
				xListView.setPullLoadEnable(false);
				xListView.setPullRefreshEnable(false);

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
