package com.hldj.hmyg;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.amap.api.car.example.MapNurseryList;
import com.hldj.hmyg.application.Data;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class MapSearchListActivity extends Activity {
	private XListView xListView;
	private ArrayList<MapNurseryList> datas = new ArrayList<MapNurseryList>();

	boolean getdata; // 避免刷新多出数据
	private MapSearchAdapter listAdapter;

	private MultipleClickProcess multipleClickProcess;
	private String id = "";
	private EditText et_search;
	private Button edit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_search_list);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		edit_btn = (Button) findViewById(R.id.edit_btn);
		et_search = (EditText) findViewById(R.id.et_search);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		edit_btn.setOnClickListener(multipleClickProcess);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

		et_search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					initData(et_search.getText().toString());

				}
				return false;
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initData(String searchKey) {
		// TODO Auto-generated method stub

		datas.clear();
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}

		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		params.put("searchKey", searchKey);
		finalHttp.post(GetServerUrl.getUrl() + "map/seedling/searchNursery",
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
										.getJSONObject("data");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"nurseryList").length() > 0) {
									JSONArray jsonArray = JsonGetInfo
											.getJsonArray(jsonObject2,
													"nurseryList");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3 = jsonArray
												.getJSONObject(i);
										MapNurseryList mList = new MapNurseryList();
										mList.setId(JsonGetInfo.getJsonString(
												jsonObject3, "id"));
										mList.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject3,
														"createBy"));
										mList.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject3,
														"createDate"));
										mList.setCiCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"cityCode"));
										mList.setCityName(JsonGetInfo
												.getJsonString(jsonObject3,
														"cityName"));
										mList.setPrCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"prCode"));
										mList.setCiCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"ciCode"));
										mList.setCoCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"coCode"));
										mList.setTwCode(JsonGetInfo
												.getJsonString(jsonObject3,
														"twCode"));
										mList.setName(JsonGetInfo
												.getJsonString(jsonObject3,
														"name"));
										mList.setDetailAddress(JsonGetInfo
												.getJsonString(jsonObject3,
														"detailAddress"));
										mList.setContactName(JsonGetInfo
												.getJsonString(jsonObject3,
														"contactName"));
										mList.setContactPhone(JsonGetInfo
												.getJsonString(jsonObject3,
														"contactPhone"));
										mList.setNurseryArea(JsonGetInfo
												.getJsonDouble(jsonObject3,
														"nurseryArea"));
										mList.setType(JsonGetInfo
												.getJsonString(jsonObject3,
														"type"));
										mList.setMainType(JsonGetInfo
												.getJsonString(jsonObject3,
														"mainType"));
										mList.setLongitude(JsonGetInfo
												.getJsonDouble(jsonObject3,
														"longitude"));
										mList.setLatitude(JsonGetInfo
												.getJsonDouble(jsonObject3,
														"latitude"));
										mList.setSeedlingCountJson(JsonGetInfo
												.getJsonInt(jsonObject3,
														"seedlingCountJson"));
										mList.setDelete(JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"delete"));
										mList.setLocationOdd(JsonGetInfo
												.getJsonBoolean(jsonObject3,
														"locationOdd"));
										mList.setCompanyName(JsonGetInfo
												.getJsonString(jsonObject3,
														"companyName"));
										datas.add(mList);
										if (listAdapter != null) {
											listAdapter.notifyDataSetChanged();
										}
									}
									if (listAdapter == null) {
										listAdapter = new MapSearchAdapter(
												MapSearchListActivity.this,
												datas);
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
				case R.id.edit_btn:
					initData(et_search.getText().toString());
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

	public class MapSearchAdapter extends BaseAdapter {
		private static final String TAG = "MapSearchAdapter";

		private ArrayList<MapNurseryList> data = null;

		private Context context = null;
		private FinalBitmap fb;

		public MapSearchAdapter(Context context, ArrayList<MapNurseryList> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
		}

		@Override
		public int getCount() {
			return this.data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return this.data.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_map_search, null);
			TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
			TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
			TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
			tv_01.setText("苗圃名称：" + data.get(position).getName());
			tv_02.setText("公司名称：" + data.get(position).getCompanyName());
			tv_03.setText("主营品种：" + data.get(position).getMainType());
			inflate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MapNurseryList mapNurseryList = data.get(position);
					Intent intent = new Intent();
					intent.putExtra("mapNurseryList", mapNurseryList);
					setResult(1, intent);
					finish();

				}

			});

			return inflate;
		}

		public void notify(ArrayList<MapNurseryList> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

}
