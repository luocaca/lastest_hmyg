package com.hldj.hmyg.broker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.bean.Agent;
import com.hldj.hmyg.buy.bean.GoodsBean;
import com.hldj.hmyg.buy.bean.StoreBean;
import com.hldj.hmyg.buyer.SerializableMaplist;
import com.hldj.hmyg.saler.bean.ParamsList;
import com.hldj.hmyg.saler.bean.Subscribe;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class SearchAgent4TransferValidateApplyAActivity extends Activity {
	private XListView xListView;
	private ListView lv;
	private ArrayList<Agent> datas = new ArrayList<Agent>();

	boolean getdata; // 避免刷新多出数据
	private MapSearchAdapter listAdapter;

	private MultipleClickProcess multipleClickProcess;
	private String id = "";
	private EditText et_search;
	private Button edit_btn;
	List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
	// 定义子列表项List数据集合
	List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();
	ArrayList<com.hldj.hmyg.buy.bean.GoodsBean> goodsBeans = new ArrayList<GoodsBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_agent_for_transfer_validate);
		gson = new Gson();
		Bundle bundle = getIntent().getExtras();
		SerializableMaplist serializableMap = (SerializableMaplist) bundle
				.get("map");
		parentMapList = serializableMap.getMap();
		childMapList_list = serializableMap.getChildMapList();
		for (int i = 0; i < childMapList_list.size(); i++) {
			List<Map<String, Object>> list = childMapList_list.get(i);
			for (int j = 0; j < list.size(); j++) {
				final GoodsBean goodsBean = (GoodsBean) list.get(j).get(
						"childName");
				goodsBeans.add(goodsBean);
			}
		}
		lv = (ListView) findViewById(R.id.lv);
		if (goodsBeans.size() > 0) {
			TransferValidateAdapter adapter = new TransferValidateAdapter(this,
					parentMapList, childMapList_list, goodsBeans);
			lv.setAdapter(adapter);
		}
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
					if (!et_search.getText().toString().startsWith("1")
							|| et_search.getText().toString().length() != 11) {
						Toast.makeText(
								SearchAgent4TransferValidateApplyAActivity.this,
								"请输入正确的电话号码", Toast.LENGTH_SHORT).show();
					} else {
						initData(et_search.getText().toString());
					}

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
			// if (s.length() > 0) {
			// initData(et_search.getText().toString());
			// } else {
			// initData(et_search.getText().toString());
			// }
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
	private Gson gson;

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
		params.put("phone", name);
		finalHttp.post(GetServerUrl.getUrl() + "admin/user/selectedAgent",
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
							if (!"".equals(msg) && !"1".equals(code)) {
								Toast.makeText(
										SearchAgent4TransferValidateApplyAActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject user = JsonGetInfo.getJSONObject(
										data, "user");
								String id = JsonGetInfo.getJsonString(user,
										"id");
								String createDate = JsonGetInfo.getJsonString(
										user, "createDate");
								String userName = JsonGetInfo.getJsonString(
										user, "userName");
								String realName = JsonGetInfo.getJsonString(
										user, "realName");
								String publicName = JsonGetInfo.getJsonString(
										user, "publicName");
								String publicPhone = JsonGetInfo.getJsonString(
										user, "publicPhone");
								String companyName = JsonGetInfo.getJsonString(
										user, "companyName");
								Agent agent = new Agent(id, createDate,
										userName, realName, publicName,
										publicPhone, companyName);
								datas.add(agent);
								if (datas.size() > 0) {
									if (listAdapter == null) {
										listAdapter = new MapSearchAdapter(
												SearchAgent4TransferValidateApplyAActivity.this,
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

		private ArrayList<Agent> data = null;

		private Context context = null;
		private FinalBitmap fb;

		public MapSearchAdapter(Context context, ArrayList<Agent> data) {
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
					R.layout.list_item_agent_search, null);
			TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
			TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);

			if (!"".equals(data.get(position).getRealName())) {
				tv_01.setText(data.get(position).getRealName());
			} else {
				tv_01.setText(data.get(position).getUserName());
			}
			tv_02.setText("确定");
			tv_02.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					StringBuffer sbf = new StringBuffer();
					for (int i = parentMapList.size() - 1; i >= 0; i--) {
						List<Map<String, Object>> childMapList = childMapList_list
								.get(i);
						for (int j = childMapList.size() - 1; j >= 0; j--) {
							String id = childMapList.get(j).get("id")
									.toString();
							sbf.append(id + ",");
						}
					}
					sbf.deleteCharAt(sbf.length() - 1);
					transferValidateApply(data.get(position).getId(),
							sbf.toString());
					// 873528519
				}

			});

			return inflate;
		}

		public void notify(ArrayList<Agent> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

	private void transferValidateApply(String userId, String ids) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", ids);
		params.put("userId", userId);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/agent/validateApply/transferValidateApply", params,
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
								onBackPressed();
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

	class TransferValidateAdapter extends BaseAdapter {

		List<Map<String, Object>> parentMapList;
		List<List<Map<String, Object>>> childMapList_list;
		ArrayList<GoodsBean> beans;
		Context context;

		@SuppressLint("UseSparseArrays")
		public TransferValidateAdapter(Context context,
				List<Map<String, Object>> parentMapList,
				List<List<Map<String, Object>>> childMapList_list,
				ArrayList<GoodsBean> beans) {
			this.parentMapList = parentMapList;
			this.childMapList_list = childMapList_list;
			this.beans = beans;
			this.context = context;
		}

		@Override
		public int getCount() {
			return beans.size();
		}

		@Override
		public Object getItem(int position) {
			return beans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_transfer_validate_apply_seedling, null);
			TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
			TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);

			if (!"".equals(beans.get(position).getName())) {
				tv_01.setText(beans.get(position).getName());
			}
			tv_02.setText(beans.get(position).getDesc());

			return inflate;
		}

	}

}
