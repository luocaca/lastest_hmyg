package com.hldj.hmyg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
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

import com.hldj.hmyg.adapter.ThematicDetailAdapter;
import com.huewu.pla.lib.me.maxwin.view.PLAXListView;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class ThematicActivity extends NeedSwipeBackActivity implements
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener {
	private PLAXListView glistView;
	private ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	private ImageView btn_back;
	boolean getdata; // 避免刷新多出数据
	private ThematicDetailAdapter gridAdapter;

	private int pageSize = 20;
	private int pageIndex = 0;
	private String id = "";
	private String title = "";
	private String type = "";
	private String ossLargeImagePath = "";
	private FinalBitmap fb;
	private TextView tv_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thematic_detail);
		fb = FinalBitmap.create(this);
		fb.configLoadingImage(R.drawable.no_image_show);
		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");
		type = getIntent().getStringExtra("type");
		ossLargeImagePath = getIntent().getStringExtra("ossLargeImagePath");
		View inflate = getLayoutInflater().inflate(
				R.layout.thematic_detail_head, null);
		tv_type = (TextView) findViewById(R.id.tv_type);
		ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
		fb.display(iv_img, ossLargeImagePath);
		tv_type.setText(title);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		glistView = (PLAXListView) findViewById(R.id.glistView);
		glistView.addHeaderView(inflate);
		glistView.setPullLoadEnable(true);
		glistView.setPullRefreshEnable(true);
		glistView.setXListViewListener(this);
		initData();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("thematicId", id);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "thematic/detail", params,
				new AjaxCallBack<Object>() {

					private JSONObject jsonObject3;

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
//								Toast.makeText(ThematicActivity.this, msg,
//										Toast.LENGTH_SHORT).show();
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
									for (int i = 0; i < jsonArray.length(); i++) {

										if ("promotion".equals(type)) {
											jsonObject3 = JsonGetInfo
													.getJSONObject(jsonArray
															.getJSONObject(i),
															"seedlingJson");
										} else {
											jsonObject3 = jsonArray
													.getJSONObject(i);
										}
										HashMap<String, Object> hMap = new HashMap<String, Object>();
										hMap.put("show_type", "seedling_list");
										hMap.put("id", JsonGetInfo
												.getJsonString(jsonObject3,
														"id"));
										hMap.put("name", JsonGetInfo
												.getJsonString(jsonObject3,
														"name"));
										hMap.put("plantType", JsonGetInfo
												.getJsonString(jsonObject3,
														"plantType"));
										hMap.put("isSelfSupport", JsonGetInfo.getJsonBoolean(jsonObject3, "isSelfSupportJson"));  //自营
										hMap.put("freeValidatePrice", JsonGetInfo.getJsonBoolean(jsonObject3, "freeValidatePrice")); //返验苗费
										hMap.put("cashOnDelivery", JsonGetInfo.getJsonBoolean(jsonObject3, "cashOnDelivery")); //担  资金担保
										hMap.put("freeDeliveryPrice", JsonGetInfo.getJsonBoolean(jsonObject3, "freeDeliveryPrice"));//免发货费
										hMap.put("freeValidate", JsonGetInfo.getJsonBoolean(jsonObject3, "freeValidate")); //免验苗
										hMap.put("tagList",
												JsonGetInfo.getJsonArray(
														jsonObject3,
														"tagList").toString());// 
										hMap.put("imageUrl", JsonGetInfo
												.getJsonString(jsonObject3,
														"largeImageUrl"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("price", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"price"));
										hMap.put("count", JsonGetInfo
												.getJsonInt(jsonObject3,
														"count"));
										hMap.put("unitTypeName", JsonGetInfo
												.getJsonString(jsonObject3,
														"unitTypeName"));
										hMap.put("diameter", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"diameter"));
										hMap.put("dbh", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"dbh"));
										hMap.put("height", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"height"));
										hMap.put("crown", JsonGetInfo
												.getJsonDouble(jsonObject3,
														"crown"));
										hMap.put("cityName", JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										hMap.put("fullName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"fullName"));
										hMap.put("ciCity_name", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ciCity"),
														"name"));
										hMap.put("realName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"realName"));
										hMap.put("companyName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"companyName"));
										hMap.put("publicName", JsonGetInfo
												.getJsonString(JsonGetInfo
														.getJSONObject(
																jsonObject3,
																"ownerJson"),
														"publicName"));
										hMap.put("status", JsonGetInfo
												.getJsonString(jsonObject3,
														"status"));
										hMap.put("statusName", JsonGetInfo
												.getJsonString(jsonObject3,
														"statusName"));
										datas.add(hMap);
										if (gridAdapter != null) {
											gridAdapter.notifyDataSetChanged();
										}
									}

									if (gridAdapter == null) {
										gridAdapter = new ThematicDetailAdapter(
												ThematicActivity.this, datas);
										glistView.setAdapter(gridAdapter);
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
						Toast.makeText(ThematicActivity.this,
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
		glistView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (gridAdapter == null) {
			gridAdapter = new ThematicDetailAdapter(ThematicActivity.this,
					datas);
			glistView.setAdapter(gridAdapter);
		} else {
			gridAdapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			initData();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		glistView.setPullRefreshEnable(false);
		initData();
		onLoad();
	}

	private void onLoad() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				glistView.stopRefresh();
				glistView.stopLoadMore();
				glistView.setRefreshTime(new Date().toLocaleString());
				glistView.setPullLoadEnable(true);
				glistView.setPullRefreshEnable(true);

			}
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

}
