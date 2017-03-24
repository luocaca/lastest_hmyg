package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.maxwin.view.XListView;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.adapter.MessageListAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class MessageListActivity extends NeedSwipeBackActivity {
	private ListView xListView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();

	boolean getdata; // 避免刷新多出数据
	private MessageListAdapter listAdapter;

	private MultipleClickProcess multipleClickProcess;
	private String id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("消息列表");
		xListView = (ListView) findViewById(R.id.xlistView);
		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		// initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initData();
		super.onResume();
	}

	private void initData() {
		// TODO Auto-generated method stub
		datas.clear();
		if (listAdapter != null) {
			listAdapter.notify(datas);
		}
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "admin/notice/getStatusCount",
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
								JSONObject jsonObject2 = JsonGetInfo
										.getJSONObject(jsonObject, "data");
								JSONObject noticeMap = JsonGetInfo
										.getJSONObject(jsonObject2, "noticeMap");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"typeList").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2,
													"typeList");
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										HashMap<String, Object> products_hash = new HashMap<String, Object>();
										products_hash.put("text", JsonGetInfo
												.getJsonString(jsonObject4,
														"text"));
										products_hash.put("icon", JsonGetInfo
												.getJsonString(jsonObject4,
														"icon"));
										products_hash.put("value", JsonGetInfo
												.getJsonString(jsonObject4,
														"value"));
										products_hash.put("count", JsonGetInfo
												.getJsonInt(jsonObject4,
														"count"));
										JSONObject noticeMap_value = JsonGetInfo
												.getJSONObject(
														noticeMap,
														JsonGetInfo
																.getJsonString(
																		jsonObject4,
																		"value"));
										products_hash.put("message",
												JsonGetInfo.getJsonString(
														noticeMap_value,
														"message"));
										products_hash.put("createDateStr",
												JsonGetInfo.getJsonString(
														noticeMap_value,
														"createDateStr"));

										datas.add(products_hash);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new MessageListAdapter(
												MessageListActivity.this, datas);
										xListView.setAdapter(listAdapter);
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
						Toast.makeText(MessageListActivity.this,
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
