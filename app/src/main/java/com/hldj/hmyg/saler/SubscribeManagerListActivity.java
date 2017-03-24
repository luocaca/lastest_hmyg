package com.hldj.hmyg.saler;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.buyer.StorePurchaseListActivity;
import com.hldj.hmyg.saler.bean.Subscribe;
import com.hldj.hmyg.saler.purchase.PurchasePyMapActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class SubscribeManagerListActivity extends NeedSwipeBackActivity {
	ArrayList<Subscribe> datas = new ArrayList<Subscribe>();
	boolean getdata; // 避免刷新多出数据
	MaterialDialog mMaterialDialog;
	private String addressId = "";
	private String type = "";
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private MyAdapter ftadapter;
	private GridView gridView;
	private TextView tv_manager;
	private TextView tv_add;
	private TextView tv_finish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe_manager_list);
		mMaterialDialog = new MaterialDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		ftadapter = new MyAdapter(SubscribeManagerListActivity.this, datas,
				false);
		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(ftadapter);
		tv_manager = (TextView) findViewById(R.id.tv_manager);
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_finish = (TextView) findViewById(R.id.tv_finish);
		initData();
		btn_back.setOnClickListener(multipleClickProcess);
		tv_manager.setOnClickListener(multipleClickProcess);
		tv_add.setOnClickListener(multipleClickProcess);
		tv_finish.setOnClickListener(multipleClickProcess);
	}

	public void NoInputMethodManager() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			if (getCurrentFocus() != null) {
				if (getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(
							SubscribeManagerListActivity.this.getCurrentFocus()
									.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(12);
		finish();
		super.onBackPressed();
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
							SubscribeManagerListActivity.this,
							AddAdressActivity.class);
					startActivityForResult(toAddAdressActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;

				case R.id.tv_manager:
					tv_finish.setVisibility(View.VISIBLE);
					tv_manager.setVisibility(View.GONE);
					tv_add.setVisibility(View.GONE);
					if (ftadapter != null) {
						ftadapter.notify(datas, true);
					} else {

					}
					break;
				case R.id.tv_add:
					Intent toSubscribeSearchListActivity = new Intent(
							SubscribeManagerListActivity.this,
							SubscribeSearchListActivity.class);
					startActivityForResult(toSubscribeSearchListActivity, 7);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.tv_finish:
					tv_manager.setVisibility(View.VISIBLE);
					tv_add.setVisibility(View.VISIBLE);
					tv_finish.setVisibility(View.GONE);
					if (ftadapter != null) {
						ftadapter.notify(datas, false);
					} else {

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
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/subscribe/manager/list", params,
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
									JSONObject seedlingType = JsonGetInfo
											.getJSONObject(jsonObject2,
													"seedlingType");
									Subscribe hMap = new Subscribe();
									hMap.setId(JsonGetInfo.getJsonString(
											jsonObject2, "id"));
									hMap.setSeedlingTypeId(JsonGetInfo
											.getJsonString(jsonObject2,
													"seedlingTypeId"));
									hMap.setCountPurchaseBysubscribeJson(JsonGetInfo
											.getJsonInt(jsonObject2,
													"countPurchaseItemJson"));
									hMap.setCreateBy(JsonGetInfo.getJsonString(
											jsonObject2, "createBy"));
									hMap.setCreateDate(JsonGetInfo
											.getJsonString(jsonObject2,
													"createDate"));
									hMap.setName(JsonGetInfo.getJsonString(
											seedlingType, "name"));
									hMap.setAliasName(JsonGetInfo
											.getJsonString(seedlingType,
													"aliasName"));
									hMap.setParentId(JsonGetInfo.getJsonString(
											seedlingType, "parentId"));
									hMap.setParentName(JsonGetInfo
											.getJsonString(seedlingType,
													"parentName"));
									hMap.setFullPinyin(JsonGetInfo
											.getJsonString(seedlingType,
													"firstPinyin"));
									hMap.setFirstPinyin(JsonGetInfo
											.getJsonString(seedlingType,
													"firstPinyin"));
									hMap.setSeedlingParams(JsonGetInfo
											.getJsonString(seedlingType,
													"seedlingParams"));
									hMap.setIsTop(JsonGetInfo.getJsonString(
											seedlingType, "isTop"));
									hMap.setParentSeedlingParams(JsonGetInfo
											.getJsonString(seedlingType,
													"parentSeedlingParams"));
									hMap.setSubscribeId(JsonGetInfo
											.getJsonString(seedlingType,
													"subscribeId"));
									hMap.setLevel(JsonGetInfo.getJsonInt(
											seedlingType, "level"));
									hMap.setSort(JsonGetInfo.getJsonInt(
											seedlingType, "sort"));
									hMap.setEdit(false);
									datas.add(hMap);
								}
								if (datas.size() > 0) {
									if (ftadapter == null) {
										ftadapter = new MyAdapter(
												SubscribeManagerListActivity.this,
												datas, false);
										gridView.setAdapter(ftadapter);
									} else {
										ftadapter.notify(datas, false);
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
						Toast.makeText(SubscribeManagerListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;
	}

	class MyAdapter extends BaseAdapter {
		private static final String TAG = "MyAdapter";

		private ArrayList<Subscribe> data = null;
		private Context context = null;
		private boolean isEdit;
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

		public MyAdapter(Context context, ArrayList<Subscribe> data,
				boolean isEdit) {
			this.data = data;
			this.context = context;
			this.isEdit = isEdit;
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
					R.layout.list_item_manager_subscribe, null);
			LinearLayout ll_item = (LinearLayout) inflate
					.findViewById(R.id.ll_item);
			TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
			TextView tv_type = (TextView) inflate.findViewById(R.id.tv_type);
			ImageView iv_img2 = (ImageView) inflate.findViewById(R.id.iv_img2);
			ImageView iv_img = (ImageView) inflate.findViewById(R.id.iv_img);
			tv_name.setText(data.get(position).getName() + "("
					+ data.get(position).getCountPurchaseBysubscribeJson()
					+ ")");
			tv_type.setText("分类：" + data.get(position).getParentName());
			if (isEdit) {
				iv_img2.setImageResource(R.drawable.seller_x);
				tv_name.setTextColor(getResources().getColor(R.color.gray));
				tv_type.setTextColor(getResources().getColor(R.color.gray));
				ll_item.setBackgroundResource(R.drawable.gray_btn_selector);
			} else {
				iv_img2.setVisibility(View.GONE);
				tv_name.setTextColor(getResources()
						.getColor(R.color.main_color));
				tv_type.setTextColor(getResources().getColor(R.color.gray));
				ll_item.setBackgroundResource(R.drawable.green_btn_selector);
			}
			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isEdit) {
						doDel(data.get(position).getId(), position);
					} else {
//						Intent intent = new Intent();
//						intent.putExtra("seedlingTypeId", data.get(position)
//								.getSeedlingTypeId());
//						intent.putExtra("name", data.get(position).getName());
//						setResult(11, intent);
//						finish();
						
						Intent intent = new Intent(SubscribeManagerListActivity.this,
								StorePurchaseListActivity.class);
						intent.putExtra("secondSeedlingTypeId", data.get(position)
								.getSeedlingTypeId());
						intent.putExtra("title", "["
								+ data.get(position).getParentName() + "]"
								+ data.get(position).getName());
						startActivity(intent);
					}

				}
			});
			iv_img2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					doDel(data.get(position).getId(), position);
				}
			});
			return inflate;
		}

		public void notify(ArrayList<Subscribe> data, boolean isEdit) {
			this.data = data;
			this.isEdit = isEdit;
			notifyDataSetChanged();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 14) {
			datas.clear();
			ftadapter.notify(datas, false);
			initData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doDel(String id, final int pos) {

		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/subscribe/doDel",
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
								datas.remove(pos);
								ftadapter.notify(datas, true);
							} else {
								Toast.makeText(
										SubscribeManagerListActivity.this,
										"删除失败", Toast.LENGTH_SHORT).show();
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
