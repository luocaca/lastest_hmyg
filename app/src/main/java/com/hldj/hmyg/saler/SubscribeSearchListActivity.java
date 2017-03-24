package com.hldj.hmyg.saler;

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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.saler.bean.Subscribe;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class SubscribeSearchListActivity extends Activity {
	private XListView xListView;
	private ArrayList<Subscribe> datas = new ArrayList<Subscribe>();

	boolean getdata; // 避免刷新多出数据
	private MapSearchAdapter listAdapter;

	private MultipleClickProcess multipleClickProcess;
	private String id = "";
	private EditText et_search;
	private Button edit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_search_list);
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
		et_search.addTextChangedListener(watcher);
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

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() > 0) {
				initData(et_search.getText().toString());
			} else {
				initData(et_search.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void NoInputMethodManager() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null
				&& getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		}
	}

	private void initData(String name) {
		// TODO Auto-generated method stub

		datas.clear();
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("name", name);
		finalHttp.post(GetServerUrl.getUrl() + "seedlingType/search", params,
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
								JSONArray typeList = JsonGetInfo.getJsonArray(
										data, "typeList");
								for (int i = 0; i < typeList.length(); i++) {
									JSONObject jsonObject2 = typeList
											.getJSONObject(i);
									Subscribe hMap = new Subscribe();
									hMap.setId(JsonGetInfo.getJsonString(
											jsonObject2, "id"));
									hMap.setCreateBy(JsonGetInfo.getJsonString(
											jsonObject2, "createBy"));
									hMap.setCreateDate(JsonGetInfo
											.getJsonString(jsonObject2,
													"createDate"));
									hMap.setName(JsonGetInfo.getJsonString(
											jsonObject2, "name"));
									hMap.setAliasName(JsonGetInfo
											.getJsonString(jsonObject2,
													"aliasName"));
									hMap.setParentId(JsonGetInfo.getJsonString(
											jsonObject2, "parentId"));
									hMap.setParentName(JsonGetInfo
											.getJsonString(jsonObject2,
													"parentName"));
									hMap.setFullPinyin(JsonGetInfo
											.getJsonString(jsonObject2,
													"firstPinyin"));
									hMap.setFirstPinyin(JsonGetInfo
											.getJsonString(jsonObject2,
													"firstPinyin"));
									hMap.setSeedlingParams(JsonGetInfo
											.getJsonString(jsonObject2,
													"seedlingParams"));
									hMap.setIsTop(JsonGetInfo.getJsonString(
											jsonObject2, "isTop"));
									hMap.setParentSeedlingParams(JsonGetInfo
											.getJsonString(jsonObject2,
													"parentSeedlingParams"));
									hMap.setSubscribeId(JsonGetInfo
											.getJsonString(jsonObject2,
													"subscribeId"));
									hMap.setLevel(JsonGetInfo.getJsonInt(
											jsonObject2, "level"));
									hMap.setSort(JsonGetInfo.getJsonInt(
											jsonObject2, "sort"));
									hMap.setCountPurchaseBysubscribeJson(JsonGetInfo
											.getJsonInt(jsonObject2,
													"countPurchaseBysubscribeJson"));
									hMap.setEdit(false);
									datas.add(hMap);
									if (listAdapter != null) {
										listAdapter.notifyDataSetChanged();
									}
								}
								if (datas.size() > 0) {
									if (listAdapter == null) {
										listAdapter = new MapSearchAdapter(
												SubscribeSearchListActivity.this,
												datas);
										xListView.setAdapter(listAdapter);
									} else {
										listAdapter.notify(datas);
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

		private ArrayList<Subscribe> data = null;

		private Context context = null;
		private FinalBitmap fb;

		public MapSearchAdapter(Context context, ArrayList<Subscribe> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.no_image_show);
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
					R.layout.list_item_subscribe_search, null);
			TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
			TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
			TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
			tv_01.setText("[" + data.get(position).getParentName() + "]");
			tv_02.setText(data.get(position).getName());
			tv_03.setText(data.get(position).getAliasName());
			inflate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					subscribeSave(data.get(position).getId(), position);
				}

			});

			return inflate;
		}

		public void notify(ArrayList<Subscribe> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		NoInputMethodManager();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		NoInputMethodManager();
		finish();
		super.onBackPressed();
	}

	private void subscribeSave(String seedlingTypeId, int postion) {

		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("authc", MyApplication.Userinfo.getString("id", ""));
		params.put("seedlingTypeId", seedlingTypeId);
		finalHttp.post(GetServerUrl.getUrl() + "admin/subscribe/save", params,
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
								setResult(14);
								onBackPressed();
							} else {
								Toast.makeText(
										SubscribeSearchListActivity.this, msg,
										Toast.LENGTH_SHORT).show();
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

	}

}
