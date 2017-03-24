package com.hldj.hmyg.saler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.barryzhang.temptyview.TViewUtil;
import com.dyr.custom.CustomDialog;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.BuyOrderActivity2;
import com.hldj.hmyg.buyer.BuyOrderBean;
import com.hldj.hmyg.buyer.ShopAdapter;
import com.hldj.hmyg.buyer.ShopAdapter.onNeedRefreshListener;
import com.hldj.hmyg.saler.SalerOrderAdapter.NeedRefreshListener;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class SalerOrderActivity2 extends NeedSwipeBackActivity implements
		IXListViewListener {
	private String status = "";
	private CheckBox checkBox;
	private TextView popTotalPrice; // 结算的价格
	private TextView popDelete; // 删除
	private TextView popRecycle; // 收藏
	private TextView popCheckOut; // 结算
	private LinearLayout layout; // 结算布局
	private SalerOrderAdapter adapter; // 自定义适配器
	private List<BuyOrderBean> list = new ArrayList<BuyOrderBean>();// 购物车数据集合

	private boolean flag = true; // 全选或全取消
	private int pageSize = 20;
	private int pageIndex = 0;
	boolean getdata; // 避免刷新多出数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saler_order2);
		status = getIntent().getStringExtra("status");
		initViews();
		initData();

		TViewUtil.EmptyViewBuilder.getInstance(SalerOrderActivity2.this)
				.setEmptyText(getResources().getString(R.string.nodata))
				.setEmptyTextSize(12).setEmptyTextColor(Color.GRAY)
				.setShowButton(false)
				.setActionText(getResources().getString(R.string.reload))
				.setAction(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// Toast.makeText(getApplicationContext(),
						// "Yes, clicked~",Toast.LENGTH_SHORT).show();
						onRefresh();
					}
				}).setShowIcon(true).setShowText(true).bindView(xListView);
	}

	// 初始化UI界面
	private void initViews() {
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		if ("".equals(status)) {
			tv_title.setText("全部订单");
		} else if ("unpay".equals(status)) {
			tv_title.setText("待付款");
		} else if ("unsend".equals(status)) {
			tv_title.setText("待发货");
		} else if ("unreceipt".equals(status)) {
			tv_title.setText("待收货");
		} else if ("finished".equals(status)) {
			tv_title.setText("已完成");
		}
		checkBox = (CheckBox) findViewById(R.id.all_check);
		xListView = (XListView) findViewById(R.id.main_listView);
		xListView.setDivider(null);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
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
		btn_back.setOnClickListener(cl);
	}

	// 初始化数据
	private void initData() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("status", status);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/seller/order/list",
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
								// Toast.makeText(SalerOrderActivity2.this, msg,
								// Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"page");
								int total = JsonGetInfo.getJsonInt(jsonObject2,
										"total");
								if (JsonGetInfo.getJsonArray(jsonObject2,
										"data").length() > 0) {
									JSONArray jsonArray_cartList = JsonGetInfo
											.getJsonArray(jsonObject2, "data");

									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										BuyOrderBean bean = new BuyOrderBean();
										bean.setId(JsonGetInfo.getJsonString(
												jsonObject4, "id"));
										bean.setSpecText(JsonGetInfo.getJsonString(
												jsonObject4, "specText"));
										bean.setName(JsonGetInfo.getJsonString(
												jsonObject4, "name"));
										bean.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"createDate"));
										bean.setStatus(status);
										bean.setImg(JsonGetInfo.getJsonString(
												jsonObject4, "mediumImageUrl"));
										bean.setPlantType(JsonGetInfo
												.getJsonString(jsonObject4,
														"plantType"));
										bean.setDbh(JsonGetInfo.getJsonDouble(
												jsonObject4, "dbh"));
										bean.setHeight(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"height"));
										bean.setCrown(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"height"));
										bean.setDeliveryPrice(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"deliveryPrice"));
										bean.setPrice(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"price"));
										bean.setTotalPrice(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"totalPrice"));
										bean.setAmount(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"amount"));
										bean.setCount(JsonGetInfo.getJsonInt(
												jsonObject4, "count"));
										bean.setNum(1);
										bean.setTagList(JsonGetInfo
												.getJsonArray(jsonObject4,
														"tagList").toString());
										bean.setUnitTypeName(JsonGetInfo
												.getJsonString(jsonObject4,
														"unitTypeName"));
										bean.setTradeType(JsonGetInfo
												.getJsonString(jsonObject4,
														"tradeType"));
										bean.setSelfSupport(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"isSelfSupport"));
										bean.setFreeValidate(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"freeValidatePrice"));
										bean.setCashOnDelivery(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"cashOnDelivery"));
										bean.setFreeDeliveryPrice(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"freeDeliveryPrice"));
										bean.setFreeValidate(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"freeValidate"));

										bean.setShowChoose(true);
										bean.setChoosed(false);

										list.add(bean);
										if (adapter != null) {
											adapter.notify(list);
											flag = false;
											selectedAll();
										}
									}
									if (adapter == null) {
										adapter = new SalerOrderAdapter(
												SalerOrderActivity2.this, list,
												handler,
												R.layout.list_item_saler_order);
										xListView.setAdapter(adapter);
										adapter.setoNeedRefreshListener(new NeedRefreshListener() {

											@Override
											public void OnNeedRefreshListener(
													boolean refresh) {
												// TODO Auto-generated method
												// stub
												if (true) {
													onRefresh();
												}

											}
										});

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
						Toast.makeText(SalerOrderActivity2.this,
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
				String shopIndex = deleteOrCheckOutShop();
				showDialogDelete(shopIndex);
				break;
			case R.id.checkOut: // 结算
				goCheckOut();
				break;
			case R.id.btn_back:
				onBackPressed();
				break;
			}
		}
	}

	// 结算
	private void goCheckOut() {
		// String shopIndex = deleteOrCheckOutShop();
		// Intent checkOutIntent = new
		// Intent(BuyOrderActivity2.this,CheckOutActivity.class);
		// checkOutIntent.putExtra("shopIndex", shopIndex);
		// startActivity(checkOutIntent);

	}

	// 全选或全取消
	private void selectedAll() {
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (ShopAdapter.getIsSelected() != null) {
					ShopAdapter.getIsSelected().put(i, flag);
				}
			}
			adapter.notifyDataSetChanged();
		}
	}

	// 删除或结算商品
	private String deleteOrCheckOutShop() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (SalerOrderAdapter.getIsSelected().get(i)) {
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
		new AlertDialog.Builder(SalerOrderActivity2.this)
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
				double price = (Double) msg.obj;
				if (price > 0) {
					popTotalPrice.setText("￥" + price);
					layout.setVisibility(View.VISIBLE);
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
	private XListView xListView;

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xListView.setPullLoadEnable(false);
		pageIndex = 0;
		list.clear();
		if (adapter == null) {
			adapter = new SalerOrderAdapter(SalerOrderActivity2.this, list,
					handler, R.layout.list_item_saler_order);
			xListView.setAdapter(adapter);
		} else {
			adapter.notify(list);
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
