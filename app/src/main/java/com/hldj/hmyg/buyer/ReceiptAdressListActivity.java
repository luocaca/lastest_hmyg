package com.hldj.hmyg.buyer;

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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.ManagerListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.ProductListAdapterForManager;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.saler.AddAdressActivity;
import com.hldj.hmyg.saler.AdressListActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class ReceiptAdressListActivity extends NeedSwipeBackActivity implements
		IXListViewListener {
	ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	private XListView xListView;
	int pageIndex = 0;
	int pageSize = 20;
	boolean getdata; // 避免刷新多出数据
	MaterialDialog mMaterialDialog;
	private String from = "";
	private MyAdapter ftadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recepit_address_list);
		from = getIntent().getStringExtra("from");
		mMaterialDialog = new MaterialDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		ImageView iv_add_adress = (ImageView) findViewById(R.id.iv_add_adress);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		initData();
		btn_back.setOnClickListener(multipleClickProcess);
		iv_add_adress.setOnClickListener(multipleClickProcess);
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
							ReceiptAdressListActivity.this,
							AddReceiptAdressActivity.class);
					startActivityForResult(toAddAdressActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
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

		finalHttp.post(GetServerUrl.getUrl() + "admin/receiptAddress/list",
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
								// Toast.makeText(ReceiptAdressListActivity.this,
								// msg, Toast.LENGTH_SHORT).show();
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
									hMap.put("isDefault", JsonGetInfo
											.getJsonBoolean(jsonObject2,
													"isDefault"));
									datas.add(hMap);
								}
								if (datas.size() > 0) {
									if (ftadapter == null) {
										ftadapter = new MyAdapter(
												ReceiptAdressListActivity.this,
												datas);
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
						Toast.makeText(ReceiptAdressListActivity.this,
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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_adress, null);
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

			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if ("SaveReceipptActivity".equals(from)) {
						Intent intent = new Intent();
						intent.putExtra("addressId",
								data.get(position).get("id").toString());
						intent.putExtra("contactName",
								data.get(position).get("contactName")
										.toString());
						intent.putExtra("contactPhone",
								data.get(position).get("contactPhone")
										.toString());
						intent.putExtra("cityName",
								data.get(position).get("cityName").toString());
						setResult(2, intent);
						finish();
					}

				}
			});

			iv_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent toAddAdressActivity = new Intent(
							ReceiptAdressListActivity.this,
							AddReceiptAdressActivity.class);
					toAddAdressActivity.putExtra("contactName",
							data.get(position).get("contactName").toString());
					toAddAdressActivity.putExtra("id",
							data.get(position).get("id").toString());
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
					toAddAdressActivity.putExtra("isDefault", (Boolean) data
							.get(position).get("isDefault"));
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
			ftadapter = new MyAdapter(ReceiptAdressListActivity.this, datas);
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
