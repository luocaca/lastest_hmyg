package com.hldj.hmyg.saler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.drakeet.materialdialog.MaterialDialog;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
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
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.jimiao.SaveMiaoActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class AdressListActivity extends NeedSwipeBackActivity implements
		IXListViewListener {
	ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	private XListView xListView;
	int pageIndex = 0;
	int pageSize = 100;
	boolean getdata; // 避免刷新多出数据
	MaterialDialog mMaterialDialog;
	private String from = "";
	private String addressId = "";
	private String type = "";
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private MyAdapter ftadapter;
	private EditText et_search;
	private String searchKey = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);
		from = getIntent().getStringExtra("from");
		if(getIntent().getStringExtra("addressId")!=null){
			addressId = getIntent().getStringExtra("addressId");
		}
		mMaterialDialog = new MaterialDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		ImageView iv_add_adress = (ImageView) findViewById(R.id.iv_add_adress);
		et_search = (EditText) findViewById(R.id.et_search);
		RelativeLayout rl_choose_01 = (RelativeLayout) findViewById(R.id.rl_choose_01);
		RelativeLayout rl_choose_02 = (RelativeLayout) findViewById(R.id.rl_choose_02);
		RelativeLayout rl_choose_03 = (RelativeLayout) findViewById(R.id.rl_choose_03);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		initData();
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// initData(et_search.getText().toString());
					NoInputMethodManager();
					searchKey = et_search.getText().toString();
					onRefresh();
				}
				return false;
			}
		});
		btn_back.setOnClickListener(multipleClickProcess);
		iv_add_adress.setOnClickListener(multipleClickProcess);
		rl_choose_01.setOnClickListener(multipleClickProcess);
		rl_choose_02.setOnClickListener(multipleClickProcess);
		rl_choose_03.setOnClickListener(multipleClickProcess);
	}

	public void NoInputMethodManager() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			if (getCurrentFocus() != null) {
				if (getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(AdressListActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
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
				case R.id.iv_add_adress:
					Intent toAddAdressActivity = new Intent(
							AdressListActivity.this, AddAdressActivity.class);
					startActivityForResult(toAddAdressActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.rl_choose_01:
					type = "";
					onRefresh();

					break;
				case R.id.rl_choose_02:
					type = "self";
					onRefresh();

					break;
				case R.id.rl_choose_03:
					type = "proxy";
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
					Thread.sleep(Data.loading_time);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("pageIndex", pageIndex + "");
		params.put("pageSize", pageSize + "");
		params.put("type", type);
		params.put("searchKey", searchKey);
		finalHttp.post(GetServerUrl.getUrl() + "admin/nursery/list", params,
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
								JSONArray typeList = jsonObject
										.getJSONObject("data")
										.getJSONObject("page")
										.getJSONArray("data");
								for (int i = 0; i < typeList.length(); i++) {
									JSONObject jsonObject2 = typeList
											.getJSONObject(i);
									HashMap<String, Object> hMap = new HashMap<String, Object>();
									hMap.put("id", JsonGetInfo.getJsonString(
											jsonObject2, "id"));
									hMap.put("name", JsonGetInfo.getJsonString(
											jsonObject2, "name"));
									hMap.put("isNewRecord", JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isNewRecord"));
									hMap.put("createBy", JsonGetInfo
											.getJsonString(jsonObject2,
													"createBy"));
									hMap.put("createDate", JsonGetInfo
											.getJsonString(jsonObject2,
													"createDate"));
									hMap.put("updateBy", JsonGetInfo
											.getJsonString(jsonObject2,
													"updateBy"));
									hMap.put("updateDate", JsonGetInfo
											.getJsonString(jsonObject2,
													"updateDate"));
									hMap.put("delFlag", JsonGetInfo
											.getJsonString(jsonObject2,
													"delFlag"));
									hMap.put("cityCode", JsonGetInfo
											.getJsonString(jsonObject2,
													"cityCode"));
									hMap.put("cityName", JsonGetInfo
											.getJsonString(jsonObject2,
													"cityName"));
									hMap.put("prCode", JsonGetInfo
											.getJsonString(jsonObject2,
													"prCode"));
									hMap.put("ciCode", JsonGetInfo.getJsonInt(
											jsonObject2, "ciCode"));
									hMap.put("coCode", JsonGetInfo
											.getJsonString(jsonObject2,
													"coCode"));
									hMap.put("twCode", JsonGetInfo
											.getJsonString(jsonObject2,
													"twCode"));
									hMap.put("detailAddress", JsonGetInfo
											.getJsonString(jsonObject2,
													"detailAddress"));
									hMap.put("contactName", JsonGetInfo
											.getJsonString(jsonObject2,
													"contactName"));
									hMap.put("contactPhone", JsonGetInfo
											.getJsonString(jsonObject2,
													"contactPhone"));
									hMap.put("nurseryArea", JsonGetInfo
											.getJsonInt(jsonObject2,
													"nurseryArea"));
									hMap.put("isDefault", JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isDefault"));
									hMap.put("type", JsonGetInfo.getJsonString(
											jsonObject2, "type"));
									hMap.put("mainType", JsonGetInfo
											.getJsonString(jsonObject2,
													"mainType"));
									hMap.put("longitude", JsonGetInfo
											.getJsonDouble(jsonObject2,
													"longitude"));
									hMap.put("latitude", JsonGetInfo
											.getJsonDouble(jsonObject2,
													"latitude"));
									datas.add(hMap);
								}

								if (datas.size() > 0) {
									if (ftadapter == null) {
										ftadapter = new MyAdapter(
												AdressListActivity.this, datas);
										xListView.setAdapter(ftadapter);
									} else {
										ftadapter.notifyDataSetChanged();
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
						Toast.makeText(AdressListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	class MyAdapter extends BaseAdapter {
		private static final String TAG = "MyAdapter";

		private ArrayList<HashMap<String, Object>> data = null;
		private Context context = null;
		private FinalBitmap fb;
		private LayoutParams l_params;

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		public MyAdapter(Context context,
				ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			this.context = context;
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.no_image_show);
			l_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			WindowManager wm = ((Activity) context).getWindowManager();
			l_params.height = (int) (wm.getDefaultDisplay().getWidth() * 9 / 20);
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

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_adress, null);
			LinearLayout ll_adress_item = (LinearLayout) inflate
					.findViewById(R.id.ll_adress_item);
			ImageView iv_edit = (ImageView) inflate.findViewById(R.id.iv_edit);
			TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
			TextView tv_address_name = (TextView) inflate
					.findViewById(R.id.tv_address_name);
			TextView tv_is_defoloat = (TextView) inflate
					.findViewById(R.id.tv_is_defoloat);
			tv_name.setText(data.get(position).get("contactName").toString()
					+ "\u0020"
					+ data.get(position).get("contactPhone").toString());
			tv_address_name.setText(data.get(position).get("cityName")
					.toString());
			if ((Boolean) data.get(position).get("isDefault")) {
				tv_is_defoloat.setVisibility(View.VISIBLE);
			} else {
				tv_is_defoloat.setVisibility(View.GONE);
			}
			if (addressId.equals(data.get(position).get("id").toString())) {
				ll_adress_item.setBackgroundResource(R.color.littleblue);
			} else {
				ll_adress_item.setBackgroundResource(R.color.white);
			}
			if ("SaveSeedlingActivity".equals(from)) {
				inflate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (SaveSeedlingActivity.instance != null) {
							if ("".equals(data.get(position).get("twCode").toString())) {
								Toast.makeText(AdressListActivity.this, "请编辑完整这条地址的所在街道",
										Toast.LENGTH_SHORT).show();
								return;
							}
						}
						Intent intent = new Intent();
						intent.putExtra("addressId",
								data.get(position).get("id").toString());
						intent.putExtra("contactPhone",
								data.get(position).get("contactPhone")
										.toString());
						intent.putExtra("contactName",
								data.get(position).get("contactName")
										.toString());
						intent.putExtra("cityName",
								data.get(position).get("cityName").toString());
						intent.putExtra("isDefault",
								(Boolean) data.get(position).get("isDefault"));

						setResult(2, intent);
						finish();

					}
				});
			}
			iv_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent toAddAdressActivity = new Intent(
							AdressListActivity.this, AddAdressActivity.class);
					toAddAdressActivity.putExtra("contactName",
							data.get(position).get("contactName").toString());
					toAddAdressActivity.putExtra("id",
							data.get(position).get("id").toString());
					toAddAdressActivity.putExtra("name", data.get(position)
							.get("name").toString());
					if (!"".equals(data.get(position).get("twCode").toString())) {
						toAddAdressActivity.putExtra("cityCode",
								data.get(position).get("twCode").toString());
					} else if (!"".equals(data.get(position).get("coCode")
							.toString())) {
						toAddAdressActivity.putExtra("cityCode",
								data.get(position).get("coCode").toString());
					} else if (!"".equals(data.get(position).get("ciCode")
							.toString())) {
						toAddAdressActivity.putExtra("cityCode",
								data.get(position).get("ciCode").toString());
					}
					toAddAdressActivity.putExtra("cityName", data.get(position)
							.get("cityName").toString());
					toAddAdressActivity.putExtra("detailAddress",
							data.get(position).get("detailAddress").toString());
					toAddAdressActivity.putExtra("contactName",
							data.get(position).get("contactName").toString());
					toAddAdressActivity.putExtra("contactPhone",
							data.get(position).get("contactPhone").toString());
					toAddAdressActivity.putExtra("nurseryArea",
							data.get(position).get("nurseryArea").toString());
					toAddAdressActivity.putExtra("isDefault", (Boolean) data
							.get(position).get("isDefault"));
					toAddAdressActivity.putExtra("type", data.get(position)
							.get("type").toString());
					toAddAdressActivity.putExtra("mainType", data.get(position)
							.get("mainType").toString());
					startActivityForResult(toAddAdressActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

				}
			});

			return inflate;
		}

		public void notify(ArrayList<HashMap<String, Object>> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		onRefresh();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		datas.clear();
		if (ftadapter == null) {
			ftadapter = new MyAdapter(AdressListActivity.this, datas);
			xListView.setAdapter(ftadapter);
		} else {
			ftadapter.notifyDataSetChanged();
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

}
