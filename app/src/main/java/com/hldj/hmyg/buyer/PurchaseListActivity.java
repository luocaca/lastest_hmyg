package com.hldj.hmyg.buyer;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn2che.androids.swipe.ListViewSwipeGesture;
import com.cn2che.androids.swipe.OnlyListViewSwipeGesture;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.bean.Purchase;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;

public class PurchaseListActivity extends NeedSwipeBackActivity {
	private ListView listView;
	int pageIndex = 0;
	int pageSize = 20;
	MaterialDialog mMaterialDialog;
	private MyAdapter ftadapter;
	private OnlyListViewSwipeGesture.TouchCallbacks swipeListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_list);
		mMaterialDialog = new MaterialDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		ImageView iv_add_adress = (ImageView) findViewById(R.id.iv_add_adress);
		TextView tv_add = (TextView) findViewById(R.id.tv_add);
		listView = (ListView) findViewById(R.id.listView);
		swipeListener = new OnlyListViewSwipeGesture.TouchCallbacks() {

			@Override
			public void FullSwipeListView(int position) {
				// TODO Auto-generated method stub
				Toast.makeText(PurchaseListActivity.this, "Action_2",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void HalfSwipeListView(int position) {
				// TODO Auto-generated method stub
				Data.purchases.remove(position);
				if (ftadapter != null) {
					ftadapter.notify(Data.purchases);
				}
			}

			@Override
			public void LoadDataForScroll(int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDismiss(ListView listView, int[] reverseSortedPositions) {
				// TODO Auto-generated method stub
				// Toast.makeText(ManagerListActivity.this,"Delete",
				// Toast.LENGTH_SHORT).show();
				// for(int i:reverseSortedPositions){
				// data.remove(i);
				// new MyAdapter().notifyDataSetChanged();
				// }

			}

			@Override
			public void OnClickListView(int position) {
				// TODO Auto-generated method stub

			}

		};
		initData();
		btn_back.setOnClickListener(multipleClickProcess);
		iv_add_adress.setOnClickListener(multipleClickProcess);
		tv_add.setOnClickListener(multipleClickProcess);
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
					Intent toAddPurchaseActivity = new Intent(
							PurchaseListActivity.this,
							AddPurchaseActivity.class);
					startActivityForResult(toAddPurchaseActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.tv_add:
					setResult(1);
					finish();
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
		if (Data.purchases.size() > 0) {
			if (ftadapter != null) {
				ftadapter.notify(Data.purchases);
			} else {
				ftadapter = new MyAdapter(PurchaseListActivity.this,
						Data.purchases);
				final OnlyListViewSwipeGesture touchListener = new OnlyListViewSwipeGesture(
						listView, swipeListener,
						PurchaseListActivity.this);
				touchListener.SwipeType = ListViewSwipeGesture.Double; // 设置两个选项列表项的背景
				listView
						.setOnTouchListener(touchListener);
				listView.setAdapter(ftadapter);
				
			}

		}
	}

	class MyAdapter extends BaseAdapter {
		private static final String TAG = "MyAdapter";

		private ArrayList<Purchase> data = null;
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

		public MyAdapter(Context context, ArrayList<Purchase> data) {
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
					R.layout.list_item_purchase_delete, null);
			TextView tv_01 = (TextView) inflate.findViewById(R.id.tv_01);
			TextView tv_02 = (TextView) inflate.findViewById(R.id.tv_02);
			TextView tv_03 = (TextView) inflate.findViewById(R.id.tv_03);
			TextView tv_04 = (TextView) inflate.findViewById(R.id.tv_04);
			TextView tv_05 = (TextView) inflate.findViewById(R.id.tv_05);
			TextView tv_08 = (TextView) inflate.findViewById(R.id.tv_08);
			tv_01.setText("品种名称：" + data.get(position).getName());
			tv_02.setText("数量：" + data.get(position).getCount());
			tv_03.setText("分类：" + data.get(position).getFirstSeedlingType());
			if ("planted".equals(data.get(position).getPlantType())) {
				tv_04.setText("种植类型：地栽苗");
			} else if ("transplant".equals(data.get(position).getPlantType())) {
				tv_04.setText("种植类型：移植苗");
			} else if ("heelin".equals(data.get(position).getPlantType())) {
				tv_04.setText("种植类型：假植苗");
			} else if ("container".equals(data.get(position).getPlantType())) {
				tv_04.setText("种植类型：容器苗");
			}
			tv_05.setText(ValueGetInfo.getValueString(data.get(position)
					.getDbh(), data.get(position).getHeight(),
					data.get(position).getCrown(), data.get(position)
							.getDiameter(), data.get(position)
							.getOffbarHeight()));
			tv_08.setText("备注：" + data.get(position).getRemarks());
//			inflate.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					// TODO Auto-generated method stub
//					FinalHttp finalHttp = new FinalHttp();
//					finalHttp.addHeader("token",
//							GetServerUrl.getKeyStr(System.currentTimeMillis()));
//					AjaxParams params = new AjaxParams();
//					finalHttp.post(GetServerUrl.getUrl()
//							+ "seedlingType/getFirstType", params,
//							new AjaxCallBack<Object>() {
//
//								@Override
//								public void onSuccess(Object t) {
//									// TODO Auto-generated method stub
//									try {
//										JSONObject jsonObject = new JSONObject(
//												t.toString());
//										String code = JsonGetInfo
//												.getJsonString(jsonObject,
//														"code");
//										String msg = JsonGetInfo.getJsonString(
//												jsonObject, "msg");
//										if (!"".equals(msg)) {
////											Toast.makeText(
////													PurchaseListActivity.this,
////													msg, Toast.LENGTH_SHORT)
////													.show();
//										}
//										if ("1".equals(code)) {
//											JSONArray typeList = jsonObject
//													.getJSONObject("data")
//													.getJSONArray("typeList");
//											for (int i = 0; i < typeList
//													.length(); i++) {
//												JSONObject jsonObject2 = typeList
//														.getJSONObject(i);
//												if (data.get(position)
//														.getFirstSeedlingTypeId()
//														.equals(JsonGetInfo
//																.getJsonString(
//																		jsonObject2,
//																		"id"))) {
//													Intent toAddPurchaseActivity = new Intent(
//															PurchaseListActivity.this,
//															AddPurchaseActivity.class);
//													toAddPurchaseActivity
//															.putExtra(
//																	"Purchase",
//																	data.get(position));
//													toAddPurchaseActivity
//															.putExtra(
//																	"position",
//																	position);
//													toAddPurchaseActivity
//															.putExtra(
//																	"seedlingParams",
//																	JsonGetInfo
//																			.getJsonString(
//																					jsonObject2,
//																					"seedlingParams"));
//													startActivityForResult(
//															toAddPurchaseActivity,
//															2);
//													overridePendingTransition(
//															R.anim.slide_in_left,
//															R.anim.slide_out_right);
//												}
//
//											}
//
//										} else {
//
//										}
//
//									} catch (JSONException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//									super.onSuccess(t);
//								}
//
//								@Override
//								public void onFailure(Throwable t, int errorNo,
//										String strMsg) {
//									// TODO Auto-generated method stub
//									Toast.makeText(PurchaseListActivity.this,
//											R.string.error_net,
//											Toast.LENGTH_SHORT).show();
//									super.onFailure(t, errorNo, strMsg);
//								}
//
//							});
//
//				}
//			});

			return inflate;
		}

		public void notify(ArrayList<Purchase> data) {
			this.data = data;
			notifyDataSetChanged();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		if (requestCode == 1 && resultCode == 1) {
//		
//		}
		initData();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
