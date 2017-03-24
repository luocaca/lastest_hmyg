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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import cn.jpush.a.a.n;

import com.hldj.hmyg.R;
import com.hldj.hmyg.buy.adapter.PurchaseDemandConsumerListAdapter;
import com.hldj.hmyg.buy.bean.PurchaseDemandConsumer;
import com.huewu.pla.lib.me.maxwin.view.PLAXListView;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseSecondActivity;

@SuppressLint("NewApi")
public class PurchaseDemandConsumerListActivity extends BaseSecondActivity
		implements IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener {

	private PLAXListView xListView;
	private ArrayList<PurchaseDemandConsumer> datas = new ArrayList<PurchaseDemandConsumer>();
	private int pageSize = 10;
	private int pageIndex = 0;
	private PurchaseDemandConsumerListAdapter listAdapter;
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
	private String tag = "";
	private String url = "admin/clerk/purchaseDemand/consumerList";
	private EditText et_search;
	private String str_search_hint = "买家名称";
	private String SearchKey = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_purchase_demand_consumer_list);
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		et_search = (EditText) findViewById(R.id.et_search);
		final String hintText = "<img src=\"" + R.drawable.search_icon
				+ "\" /> " + str_search_hint;
		et_search.setHint(Html.fromHtml(hintText, imageGetter, null));
		// et_search.setGravity(Gravity.CENTER);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);

		xListView = (PLAXListView) findViewById(R.id.xlistView);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		tv_btn.setOnClickListener(multipleClickProcess);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					SearchKey = et_search.getText().toString();
					onRefresh();
				}
				return false;
			}
		});

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
				case R.id.tv_btn:
					SearchKey = et_search.getText().toString();
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
			listAdapter = new PurchaseDemandConsumerListAdapter(
					PurchaseDemandConsumerListActivity.this, datas, tag);
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
		params.put("searchKey", SearchKey);

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
										PurchaseDemandConsumer hMap = new PurchaseDemandConsumer();
										hMap.setId(JsonGetInfo.getJsonString(
												jsonObject3, "id"));
										hMap.setCiCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"ciCode"));
										hMap.setCoCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"coCode"));
										hMap.setName(JsonGetInfo.getJsonString(
												jsonObject3, "name"));
										hMap.setPrCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"prCode"));
										hMap.setTwCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"twCode"));
										datas.add(hMap);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}

									if (listAdapter == null) {
										listAdapter = new PurchaseDemandConsumerListAdapter(
												PurchaseDemandConsumerListActivity.this,
												datas, tag);
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

}
