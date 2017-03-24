package com.hldj.hmyg.broker;

import java.util.ArrayList;
import java.util.Date;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class AddReceiptActivity extends NeedSwipeBackActivity implements IXListViewListener,
		com.huewu.pla.lib.me.maxwin.view.PLAXListView.IXListViewListener {
	private CheckBox checkBox;
	private TextView popTotalPrice; // 结算的价格
	private TextView popDelete; // 删除
	private TextView popRecycle; // 收藏

	private TextView popCheckOut; // 结算
	private LinearLayout layout; // 结算布局
	private ReceiptAdapter adapter; // 自定义适配器
	private ArrayList<ReceiptListJson> list = new ArrayList<ReceiptListJson>();//
	private ArrayList<ReceiptListJson> list_ischeck = new ArrayList<ReceiptListJson>();//
	private XListView xListView;
	private boolean flag = true; // 全选或全取消
	private int pageSize = 20;
	private int pageIndex = 0;
	boolean getdata; // 避免刷新多出数据
	private MultipleClickProcess multipleClickProcess;
	private float price = 0;
	private String orderId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_receipt);
		orderId = getIntent().getStringExtra("id");
		ids = getIntent().getStringArrayListExtra("ids");
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		tv_save.setOnClickListener(multipleClickProcess);
		initViews();
		init();
	}

	// 初始化UI界面
	private void initViews() {
		checkBox = (CheckBox) findViewById(R.id.all_check);
		xListView = (XListView) findViewById(R.id.xlistView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		xListView.setXListViewListener(this);
		popTotalPrice = (TextView) findViewById(R.id.shopTotalPrice);
		popDelete = (TextView) findViewById(R.id.delete);
		popRecycle = (TextView) findViewById(R.id.collection);
		popCheckOut = (TextView) findViewById(R.id.checkOut);
		layout = (LinearLayout) findViewById(R.id.price_relative);

		ClickListener cl = new ClickListener();
		checkBox.setOnClickListener(cl);
		popDelete.setOnClickListener(cl);
		popCheckOut.setOnClickListener(cl);
		popRecycle.setOnClickListener(cl);
	}

	// 初始化数据
	private void init() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", orderId);
		finalHttp.post(GetServerUrl.getUrl() + "admin/agent/delivery/detail",
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
								JSONObject delivery = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"), "delivery");
								JSONArray jsonArray_cartList = JsonGetInfo
										.getJsonArray(delivery,
												"receiptListJson");
								if (jsonArray_cartList.length() > 0) {
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										ReceiptListJson bean = new ReceiptListJson();
										bean.setId(JsonGetInfo.getJsonString(
												jsonObject4, "id"));
										bean.setRemarks(JsonGetInfo
												.getJsonString(jsonObject4,
														"remarks"));
										bean.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject4,
														"createBy"));
										bean.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"createDate"));
										bean.setReceiptDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"receiptDate"));
										bean.setReceiptAddressId(JsonGetInfo
												.getJsonString(jsonObject4,
														"receiptAddressId"));
										bean.setOrderId(JsonGetInfo
												.getJsonString(jsonObject4,
														"orderId"));
										bean.setBuyerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"buyerId"));
										bean.setDeliveryId(JsonGetInfo
												.getJsonString(jsonObject4,
														"deliveryId"));
										bean.setOrderName(JsonGetInfo
												.getJsonString(jsonObject4,
														"orderName"));
										bean.setOrderNum(JsonGetInfo
												.getJsonString(jsonObject4,
														"orderNum"));
										bean.setSellerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"sellerId"));
										bean.setCount(JsonGetInfo.getJsonInt(
												jsonObject4, "count"));
										bean.setLoadedCountJson(JsonGetInfo
												.getJsonInt(jsonObject4,
														"loadedCountJson"));
										bean.setDelivery(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"isDelivery"));
										bean.setPayConfirm(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"payConfirm"));
										bean.setContactName(JsonGetInfo.getJsonString(
												JsonGetInfo.getJSONObject(
														jsonObject4,
														"receiptAddressJson"),
												"contactName"));
										bean.setContactPhone(JsonGetInfo.getJsonString(
												JsonGetInfo.getJSONObject(
														jsonObject4,
														"receiptAddressJson"),
												"contactPhone"));
										bean.setFullAddress(JsonGetInfo.getJsonString(
												JsonGetInfo.getJSONObject(
														jsonObject4,
														"receiptAddressJson"),
												"fullAddress"));
										if(ids.toString().contains(JsonGetInfo.getJsonString(
												jsonObject4, "id"))){
											bean.setShowCheckBox(false);
										}else {
											bean.setShowCheckBox(true);
										}

										bean.setChoosed(false);
										list.add(bean);
										if (adapter != null) {
											adapter.notifyDataSetChanged();
										}
									}
									if (adapter == null) {
										adapter = new ReceiptAdapter(
												AddReceiptActivity.this, list,
												handler,
												R.layout.list_item_add_receipt);
										xListView.setAdapter(adapter);
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
						Toast.makeText(AddReceiptActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;

	}

	// 获取集合数据

	// 事件点击监听器
	private final class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_check: // 全选
				selectedAll();
				break;
			case R.id.delete: // 删除
				if (ReceiptAdapter.getIsSelected().size() > 0) {
					String shopIndex = deleteOrCheckOutShop();
					showDialogDelete(shopIndex);
				}
				break;
			case R.id.checkOut: // 结算
				goCheckOut();
				break;

			}
		}
	}

	// 结算
	private void goCheckOut() {
		list_ischeck.clear();
		for (int i = 0; i < list.size(); i++) {
			if (ReceiptAdapter.getIsSelected().get(i)) {
				list_ischeck.add(list.get(i));
			}
		}
		

	}

	// 全选或全取消
	private void selectedAll() {
		for (int i = 0; i < list.size(); i++) {
			ReceiptAdapter.getIsSelected().put(i, flag);
		}
		adapter.notifyDataSetChanged();
	}

	// 删除或结算商品
	private String deleteOrCheckOutShop() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (ReceiptAdapter.getIsSelected().get(i)) {
				sb.append(i);
				sb.append(",");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	// 弹出对话框询问用户是否删除被选中的商品
	private void showDialogDelete(String str) {
		final String[] delShopIndex = str.split(",");
		new AlertDialog.Builder(AddReceiptActivity.this)
				.setMessage("您确定删除这" + delShopIndex.length + "商品吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						for (String s : delShopIndex) {
							int index = Integer.valueOf(s);
							list.remove(index);
							// 连接服务器之后，获取数据库中商品对应的ID，删除商品
							// list.get(index).getShopId();
						}
						flag = false;
						selectedAll(); // 删除商品后，取消所有的选择
						flag = true; // 刷新页面后，设置Flag为true，恢复全选功能
					}
				}).setNegativeButton("取消", null).create().show();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) { // 更改选中商品的总价格
				price = (Float) msg.obj;
				if (price > 0) {
					popTotalPrice.setText("￥" + price);
					layout.setVisibility(View.GONE);
				} else {
					layout.setVisibility(View.GONE);
				}
			} else if (msg.what == 11) {
				// 所有列表中的商品全部被选中，让全选按钮也被选中
				// flag记录是否全被选中
				flag = !(Boolean) msg.obj;
				checkBox.setChecked((Boolean) msg.obj);
			}
		}
	};
	private TextView tv_save;
	private ArrayList<String> ids;

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
				case R.id.tv_save:
					list_ischeck.clear();
					for (int i = 0; i < list.size(); i++) {
						if (ReceiptAdapter.getIsSelected().get(i)) {
							list_ischeck.add(list.get(i));
						}
					}
					//返回给上一页
					Intent intent = new Intent();
					Bundle bundleObject = new Bundle();
					final ChooseReceptSerializableMaplist myMap = new ChooseReceptSerializableMaplist();
					myMap.setMaplist(list_ischeck);
					bundleObject.putSerializable("list", myMap);
					intent.putExtras(bundleObject);
					setResult(1,intent);
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		list.clear();
		if (adapter == null) {
			adapter = new ReceiptAdapter(AddReceiptActivity.this, list,
					handler, R.layout.list_item_cart_product);
			xListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		if (getdata == true) {
			init();
		}
		onLoad();
	}

	@Override
	public void onLoadMore() {
		xListView.setPullRefreshEnable(false);
		init();
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
				xListView.setPullLoadEnable(false);
				xListView.setPullRefreshEnable(false);

			}
		}, 3000);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			flag = false;
			selectedAll();
			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
