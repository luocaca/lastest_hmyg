package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.Date;

import me.drakeet.materialdialog.MaterialDialog;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hldj.hmyg.R;
import com.hldj.hmyg.buy.adapter.PurchaseDemandListAdapter;
import com.hldj.hmyg.buy.bean.PurchaseDemand;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseSecondActivity;

@SuppressLint("NewApi")
public class PurchaseDemandListActivity extends BaseSecondActivity implements
		IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener,
		OnClickListener {

	private XListView xListView;
	private ArrayList<PurchaseDemand> datas = new ArrayList<PurchaseDemand>();
	private int pageSize = 10;
	private int pageIndex = 0;
	private PurchaseDemandListAdapter listAdapter;
	boolean getdata; // 避免刷新多出数据
	FinalHttp finalHttp = new FinalHttp();
	public int i = 0;
	MaterialDialog mMaterialDialog;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	private String status = "";
	private String tag = "";
	private String url = "";
	private EditText et_search;
	private String str_search_hint = "";
	private String SearchKey = "";
	private String searchKeyName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_purchase_demand_list);
		if (getIntent().getStringExtra("tag") != null) {
			tag = getIntent().getStringExtra("tag");
			if ("clerk".equals(tag)) {
				url = "admin/clerk/purchaseDemand/list";
				str_search_hint = "项目名称/项目地址/买家名称/买家电话";
				searchKeyName = "clerkSearchKey";
			} else if ("buyer".equals(tag)) {
				url = "admin/buyer/purchaseDemand/list";
				str_search_hint = "项目名称/项目地址";
				searchKeyName = "buyerSearchKey";
			}
		}

		tv_btn = (TextView) findViewById(R.id.tv_btn);
		et_search = (EditText) findViewById(R.id.et_search);
		final String hintText = "<img src=\"" + R.drawable.search_icon
				+ "\" /> " + str_search_hint;
		et_search.setHint(Html.fromHtml(hintText, imageGetter, null));
		// et_search.setGravity(Gravity.CENTER);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		LinearLayout rl_all = (LinearLayout) findViewById(R.id.rl_all);
		RelativeLayout rl_01 = (RelativeLayout) findViewById(R.id.rl_01);
		RelativeLayout rl_02 = (RelativeLayout) findViewById(R.id.rl_02);
		RelativeLayout rl_03 = (RelativeLayout) findViewById(R.id.rl_03);
		RelativeLayout rl_04 = (RelativeLayout) findViewById(R.id.rl_04);
		RelativeLayout rl_05 = (RelativeLayout) findViewById(R.id.rl_05);
		RelativeLayout rl_06 = (RelativeLayout) findViewById(R.id.rl_06);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		mMaterialDialog = new MaterialDialog(this);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);

		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);

		if (getIntent().getStringExtra("name") != null
				&& getIntent().getStringExtra("consumerId") != null) {
			name = getIntent().getStringExtra("name");
			consumerId = getIntent().getStringExtra("consumerId");
			tv_title.setText(name);
			rl_all.setVisibility(View.GONE);
			confirm();
		} else {
			all();
		}
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		rl_01.setOnClickListener(multipleClickProcess);
		rl_02.setOnClickListener(multipleClickProcess);
		rl_03.setOnClickListener(multipleClickProcess);
		rl_04.setOnClickListener(multipleClickProcess);
		rl_05.setOnClickListener(multipleClickProcess);
		rl_06.setOnClickListener(multipleClickProcess);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					SearchKey = et_search.getText().toString();
					all();
					onRefresh();
				}
				return false;
			}
		});
		tv_btn.setOnClickListener(this);

	}

	final Html.ImageGetter imageGetter = new Html.ImageGetter() {

		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			int rId = Integer.parseInt(source);
			drawable = getResources().getDrawable(rId);
			drawable.setBounds(0, 0, 30, 30);
			return drawable;
		}
	};
	private TextView tv_btn;
	private String name = "";
	private String consumerId = "";
	private TextView tv_title;

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
					break;
				case R.id.rl_01:
					all();
					onRefresh();
					break;
				case R.id.rl_02:
					unSubmit();
					onRefresh();
					break;
				case R.id.rl_03:
					quote();
					onRefresh();
					break;
				case R.id.rl_04:
					confirm();
					onRefresh();
					break;
				case R.id.rl_05:
					finished();
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
			listAdapter = new PurchaseDemandListAdapter(
					PurchaseDemandListActivity.this, datas, tag, consumerId);
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
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		params.put("status", status);
		params.put(searchKeyName, SearchKey);
		params.put("consumerId", consumerId);
		finalHttp.post(GetServerUrl.getUrl() + url, params,
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
										PurchaseDemand hMap = new PurchaseDemand();
										hMap.setId(JsonGetInfo.getJsonString(
												jsonObject3, "id"));
										hMap.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject3,
														"createBy"));
										hMap.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject3,
														"createDate"));
										hMap.setProjectName(JsonGetInfo
												.getJsonString(jsonObject3,
														"projectName"));
										hMap.setAddress(JsonGetInfo
												.getJsonString(jsonObject3,
														"address"));
										hMap.setBuyerId(JsonGetInfo
												.getJsonString(jsonObject3,
														"buyerId"));
										hMap.setBuyerPhone(JsonGetInfo
												.getJsonString(jsonObject3,
														"buyerPhone"));
										hMap.setBuyerName(JsonGetInfo
												.getJsonString(jsonObject3,
														"buyerName"));
										hMap.setClerkId(JsonGetInfo
												.getJsonString(jsonObject3,
														"clerkId"));
										hMap.setStatus(JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.setStatusName(JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										hMap.setItemCount(JsonGetInfo
												.getJsonInt(jsonObject3,
														"itemCountJson"));
										hMap.setUsedCountJson(JsonGetInfo
												.getJsonInt(jsonObject3,
														"usedCountJson"));
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new PurchaseDemandListAdapter(
												PurchaseDemandListActivity.this,
												datas, tag, consumerId);
										xListView.setAdapter(listAdapter);
										xListView
												.setOnItemClickListener(new OnItemClickListener() {

													@Override
													public void onItemClick(
															AdapterView<?> arg0,
															View arg1,
															int position,
															long arg3) {
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

	private void all() {
		// TODO Auto-generated method stub
		status = "";
		tv_01.setTextColor(getResources().getColor(R.color.main_color));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void unSubmit() {
		// TODO Auto-generated method stub
		status = "unSubmit";
		tv_02.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void quote() {
		// TODO Auto-generated method stub
		status = "quote";
		tv_03.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void confirm() {
		// TODO Auto-generated method stub
		status = "confirm";
		tv_04.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_05.setTextColor(getResources().getColor(R.color.little_gray));
	}

	private void finished() {
		// TODO Auto-generated method stub
		status = "finished";
		tv_05.setTextColor(getResources().getColor(R.color.main_color));
		tv_01.setTextColor(getResources().getColor(R.color.little_gray));
		tv_02.setTextColor(getResources().getColor(R.color.little_gray));
		tv_03.setTextColor(getResources().getColor(R.color.little_gray));
		tv_04.setTextColor(getResources().getColor(R.color.little_gray));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && resultCode == 1) {
			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_btn:
			SearchKey = et_search.getText().toString();
			all();
			onRefresh();
			break;

		default:
			break;
		}
	}

}
