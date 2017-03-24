package com.hldj.hmyg.buyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.amap.api.car.example.MapNurseryList;
import com.hldj.hmyg.BActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.StoreActivity;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.HomeStore;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.white.utils.AndroidUtil;
import com.zzy.flowers.widget.popwin.EditP2;

public class PurchaseSearchListActivity extends NeedSwipeBackActivity {
	private XListView xListView;
	private ArrayList<MapNurseryList> datas = new ArrayList<MapNurseryList>();
	ArrayList<HomeStore> url0s = new ArrayList<HomeStore>();
	boolean getdata; // 避免刷新多出数据
	private SearchAdapter listAdapter;
	private GridViewAdapter gridViewAdapter;

	private MultipleClickProcess multipleClickProcess;
	private String id = "";
	private EditText et_search;
	private Button edit_btn;
	private ArrayAdapter<String> mSPAdapter;
	private LinearLayout ll_sp;
	private String from = "";

	List<Map<String, String>> moreList;
	private PopupWindow pwMyPopWindow;// popupwindow
	private ListView lvPopupList;// popupwindow中的ListView
	private int NUM_OF_VISIBLE_LIST_ROWS = 4;// 指定popupwindow中Item的数量
	private TextView tv_choose;
	private int choose = 0;
	private Spinner mSpinner;

	String url = "seedling/search";

	// /store/search 店铺 搜索
	// /purchase/search 采购报价
	// /seedling/search 苗木资源

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_search_list);
		if (getIntent().getStringExtra("from") != null) {
			from = getIntent().getStringExtra("from");
		}
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		edit_btn = (Button) findViewById(R.id.edit_btn);
		ll_sp = (LinearLayout) findViewById(R.id.ll_sp);
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setHint("品种名称/别名/品种编号");
		xListView = (XListView) findViewById(R.id.xlistView);
		if (from.equals("StorePurchaseListActivity")) {
			ll_sp.setVisibility(View.GONE);
		} else {
			ll_sp.setVisibility(View.VISIBLE);
		}
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		// 建立Adapter并且绑定数据源
		List<String> ls = new ArrayList<String>();
		ls.add("苗木资源");
		ls.add("快速报价");
		ls.add("店铺搜索");
		mSPAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,
				ls.toArray(new String[ls.size()]));
		// 绑定 Adapter到控件
		mSpinner.setAdapter(mSPAdapter);

		tv_choose = (TextView) findViewById(R.id.tv_choose);
		moreList = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		map = new HashMap<String, String>();
		map.put("share_key", "苗木资源");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "快速报价");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "店铺搜索");
		moreList.add(map);
		iniPopupWindow();
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		edit_btn.setOnClickListener(multipleClickProcess);
		tv_choose.setOnClickListener(multipleClickProcess);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_search.addTextChangedListener(watcher);
		et_search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchIt2();
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
				searchIt();
			} else {
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

	private void iniPopupWindow() {

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		lvPopupList.setAdapter(new SimpleAdapter(
				PurchaseSearchListActivity.this, moreList,
				R.layout.list_item_popupwindow, new String[] { "share_key" },
				new int[] { R.id.tv_list_item }));
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				choose = position;
				tv_choose.setText(moreList.get(position).get("share_key"));
				et_search.setText("");
				datas.clear();
				if (listAdapter != null) {
					listAdapter.notifyDataSetChanged();
				}
				url0s.clear();
				if (gridViewAdapter != null) {
					gridViewAdapter.notifyDataSetChanged();
				}
				//清除数据
				if(choose==0){
					et_search.setHint("品种名称/别名/品种编号");
				}else if (choose==1) {
					et_search.setHint("品种名称/别名");
				}else if(choose==2) {
					et_search.setHint("店铺名称");
				}
				
				if (pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();// 关闭
				}

			}
		});

		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
		pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)
				* NUM_OF_VISIBLE_LIST_ROWS);
		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	private void searchIt2() {
		// TODO Auto-generated method stub
		if(choose==0){
			et_search.setHint("品种名称/别名/品种编号");
		}else if (choose==1) {
			et_search.setHint("品种名称/别名");
		}else if(choose==2) {
			et_search.setHint("店铺名称");
		}
		if (choose != 2) {
			if (from.equals("StorePurchaseListActivity")) {
				Intent intent = new Intent();
				intent.putExtra("searchKey", et_search.getText().toString());
				setResult(8, intent);
				finish();
			} else if (from.equals("BActivity") && choose == 0) {
				// 商城搜索
				Intent intent = new Intent();
				intent.putExtra("searchKey", et_search.getText().toString());
				setResult(8, intent);
				finish();
			} else if ((from.equals("AActivity") && choose == 1)
					|| (from.equals("BActivity") && choose == 1)) {
				// 采购搜索 首页，商城
//				Intent intent = new Intent(PurchaseSearchListActivity.this,
//						StorePurchaseListActivity.class);
//				intent.putExtra("searchKey", et_search.getText().toString());
//				startActivity(intent);
//				finish();
				
			} else if (from.equals("AActivity") && choose == 0) {
				// 首页到商城
				Intent toBActivity = new Intent(
						PurchaseSearchListActivity.this, BActivity.class);
				toBActivity.putExtra("from", "context");
				toBActivity.putExtra("searchKey", et_search.getText()
						.toString());
				startActivity(toBActivity);
				finish();
			}
		} else if (choose == 2) {
			initData(et_search.getText().toString());
		}

	}

	private void searchIt() {
		// TODO Auto-generated method stub
		if (choose == 0) {
			et_search.setHint("品种名称/别名/品种编号");
			url ="seedling/search";
		} else if (choose == 1) {
			et_search.setHint("品种名称/别名");
			url ="purchase/search";
		} else if (choose == 2) {
			et_search.setHint("店铺名称");
			url ="store/search";
		}
		initData(et_search.getText().toString());
		

	}

	private void initData(String name) {
		// TODO Auto-generated method stub
		datas.clear();
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}
		url0s.clear();
		if (gridViewAdapter != null) {
			gridViewAdapter.notifyDataSetChanged();
		}
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("searchKey", name);
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
								
								if (url.contains("store")) {
									JSONArray storeList = JsonGetInfo.getJsonArray(
											data, "storeList");
									// 商铺
									for (int i = 0; i < storeList.length(); i++) {
										JSONObject jsonObject2 = storeList
												.getJSONObject(i);
										HomeStore a_first_product = new HomeStore(
												JsonGetInfo.getJsonString(
														jsonObject2, "id"),
												JsonGetInfo.getJsonString(
														jsonObject2, "code"),
												JsonGetInfo.getJsonString(
														jsonObject2,
														"maintType"),
												JsonGetInfo.getJsonString(
														jsonObject2, "id"),
												JsonGetInfo.getJsonString(
														jsonObject2, "name"));
										url0s.add(a_first_product);
									}
								} else if (url.contains("purchase")) {
									// 采购报价
									JSONArray purchaseItemList = JsonGetInfo.getJsonArray(
											data, "purchaseItemList");
									for (int i = 0; i < purchaseItemList.length(); i++) {
										JSONObject jsonObject2 = purchaseItemList
												.getJSONObject(i);
										HomeStore a_first_product = new HomeStore(
												JsonGetInfo.getJsonString(
														jsonObject2, "secondSeedlingTypeId"),
												JsonGetInfo.getJsonString(
														jsonObject2, "code"),
												JsonGetInfo.getJsonString(
														jsonObject2,
														"maintType"),
												JsonGetInfo.getJsonString(
														jsonObject2, "secondSeedlingTypeId"),
												JsonGetInfo.getJsonString(
														jsonObject2, "name"));
										url0s.add(a_first_product);
									}
								} else if (url.contains("seedling")) {

									// 苗木
									JSONArray seedlingList = JsonGetInfo.getJsonArray(
											data, "seedlingList");
									for (int i = 0; i < seedlingList.length(); i++) {
										JSONObject jsonObject2 = seedlingList
												.getJSONObject(i);
										HomeStore a_first_product = new HomeStore(
												JsonGetInfo.getJsonString(
														jsonObject2, "id"),
												JsonGetInfo.getJsonString(
														jsonObject2, "code"),
												JsonGetInfo.getJsonString(
														jsonObject2,
														"maintType"),
												JsonGetInfo.getJsonString(
														jsonObject2, "id"),
												JsonGetInfo.getJsonString(
														jsonObject2, "name"));
										url0s.add(a_first_product);
									}
								

								}

								if (url0s.size() > 0) {
									if (gridViewAdapter == null) {
										gridViewAdapter = new GridViewAdapter(
												PurchaseSearchListActivity.this,
												url0s);
										xListView.setAdapter(gridViewAdapter);
									} else {
										gridViewAdapter.notify(url0s);
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
					searchIt();
					break;
				case R.id.tv_choose:
					if (pwMyPopWindow.isShowing()) {

						pwMyPopWindow.dismiss();// 关闭
					} else {

						pwMyPopWindow.showAsDropDown(tv_choose);// 显示
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

	public class SearchAdapter extends BaseAdapter {
		private static final String TAG = "MapSearchAdapter";

		private ArrayList<MapNurseryList> data = null;

		private Context context = null;
		private FinalBitmap fb;

		public SearchAdapter(Context context, ArrayList<MapNurseryList> data) {
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

	class GridViewAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<HomeStore> arrayList;
		private int dip20px;
		private int width;

		public GridViewAdapter(Context context, ArrayList<HomeStore> arrayList) {
			this.context = context;
			this.arrayList = arrayList;
			dip20px = AndroidUtil.dip2px(context, 2);
			WindowManager wm = ((Activity) context).getWindowManager();
			width = wm.getDefaultDisplay().getWidth();
		}

		public void notify(ArrayList<HomeStore> datas) {
			// TODO Auto-generated method stub
			this.arrayList = datas;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View currentView, ViewGroup arg2) {
			View inflate = LayoutInflater.from(context).inflate(
					R.layout.list_item_store, null);
			TextView iv = (TextView) inflate.findViewById(R.id.iv);
			iv.setText(arrayList.get(position).getName());
			inflate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if (choose != 2) {
						if (from.equals("StorePurchaseListActivity")) {
							Intent intent = new Intent();
							intent.putExtra("searchKey", arrayList.get(position).getName());
							setResult(8, intent);
							finish();
						} else if (from.equals("BActivity") && choose == 0) {
							// 商城搜索
							Intent intent = new Intent();
							intent.putExtra("searchKey", arrayList.get(position).getName());
							setResult(8, intent);
							finish();
						} else if ((from.equals("AActivity") && choose == 1)
								|| (from.equals("BActivity") && choose == 1)) {
							// 采购搜索 首页，商城
							Intent intent = new Intent(PurchaseSearchListActivity.this,
									StorePurchaseListActivity.class);
							intent.putExtra("secondSeedlingTypeId", arrayList.get(position)
									.getId());
							intent.putExtra("title",arrayList.get(position).getName());
							startActivity(intent);
							finish();
						} else if (from.equals("AActivity") && choose == 0) {
							// 首页到商城
							Intent toBActivity = new Intent(
									PurchaseSearchListActivity.this, BActivity.class);
							toBActivity.putExtra("from", "context");
							toBActivity.putExtra("searchKey", arrayList.get(position).getName());
							startActivity(toBActivity);
							finish();
						}
					} else if (choose == 2) {
						if (!"".equals(arrayList.get(position).getId())) {
							Intent toStoreActivity = new Intent(context,
									StoreActivity.class);
							toStoreActivity.putExtra("code", arrayList
									.get(position).getId());
							context.startActivity(toStoreActivity);
							finish();
						}
					
					}
					

				}
			});
			return inflate;
		}

	}

}
