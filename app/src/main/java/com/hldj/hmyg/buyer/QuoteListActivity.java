package com.hldj.hmyg.buyer;

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
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class QuoteListActivity extends NeedSwipeBackActivity implements
		IXListViewListener {
	private CheckBox checkBox;
	private TextView popTotalPrice; // 结算的价格
	private TextView popDelete; // 删除
	private TextView popRecycle; // 收藏
	private TextView popCheckOut; // 结算
	private LinearLayout layout; // 结算布局
	private QuoteAdapter adapter; // 自定义适配器
	private List<Quote> list = new ArrayList<Quote>();// 购物车数据集合

	private boolean flag = true; // 全选或全取消
	private int pageSize = 20;
	private int pageIndex = 0;
	boolean getdata; // 避免刷新多出数据
	private XListView xListView;
	private MultipleClickProcess multipleClickProcess;
	private String purchaseItemId = "";
	private String purchaseId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote_list);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		purchaseItemId = getIntent().getStringExtra("purchaseItemId");
		purchaseId = getIntent().getStringExtra("purchaseId");

		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		initViews();
		init();
	}

	// 初始化UI界面
	private void initViews() {
		checkBox = (CheckBox) findViewById(R.id.all_check);
		xListView = (XListView) findViewById(R.id.xlistView);
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
	}

	// 初始化数据
	private void init() {
		// TODO Auto-generated method stub
		getdata = false;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("purchaseItemId", purchaseItemId);
		params.put("pageSize", pageSize + "");
		params.put("pageIndex", pageIndex + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/listQuote", params,
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
								Toast.makeText(QuoteListActivity.this, msg,
										Toast.LENGTH_SHORT).show();
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
										Quote bean = new Quote();
										bean.setId(JsonGetInfo.getJsonString(
												jsonObject4, "id"));
										bean.setRemarks(JsonGetInfo
												.getJsonString(jsonObject4,
														"remarks"));
										bean.setCityCode(JsonGetInfo
												.getJsonString(jsonObject4,
														"cityCode"));
										bean.setCityName(JsonGetInfo
												.getJsonString(jsonObject4,
														"cityName"));
										bean.setPurchaseId(JsonGetInfo
												.getJsonString(jsonObject4,
														"purchaseId"));
										bean.setPurchaseItemId(JsonGetInfo
												.getJsonString(jsonObject4,
														"purchaseItemId"));
										bean.setPrice(JsonGetInfo
												.getJsonDouble(jsonObject4,
														"price"));
										bean.setCount(JsonGetInfo.getJsonInt(
												jsonObject4, "count"));
										bean.setStatus(JsonGetInfo
												.getJsonString(jsonObject4,
														"status"));
										bean.setUsed(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"isUsed"));
										bean.setAlternative(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"isAlternative"));
										bean.setInvoice(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"isInvoice"));
										bean.setShowChoose(true);
										bean.setChoosed(false);
										JSONObject seller = JsonGetInfo
												.getJSONObject(jsonObject4,
														"seller");
										bean.setRealName(JsonGetInfo
												.getJsonString(seller,
														"realName"));
										bean.setPublicName(JsonGetInfo
												.getJsonString(seller,
														"publicName"));
										bean.setCompanyName(JsonGetInfo
												.getJsonString(seller,
														"companyName"));
										bean.setPublicPhone(JsonGetInfo
												.getJsonString(seller,
														"publicPhone"));
										bean.setPhone(JsonGetInfo
												.getJsonString(seller, "phone"));
										bean.setShowName(JsonGetInfo
												.getJsonString(seller,
														"showName"));
										list.add(bean);
										if (adapter != null) {
											adapter.notifyDataSetChanged();
										}
									}
									if (adapter == null) {
										adapter = new QuoteAdapter(
												QuoteListActivity.this, list,
												handler,
												R.layout.list_item_quote);
										xListView.setAdapter(adapter);
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
						Toast.makeText(QuoteListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
		getdata = true;

	}

	// 事件点击监听器
	private final class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_check: // 全选
				selectedAll();
				break;
			case R.id.delete: // 采用

				if (QuoteAdapter.getIsSelected().size() > 0) {
					String shopIndex = deleteOrCheckOutShop();
					purchaseUsedQuote(shopIndex);
				}
				break;
			case R.id.checkOut: // 备选
				String shopIndex = deleteOrCheckOutShop();
				purchaseAlternateQuote(shopIndex);
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

	private void purchaseUsedQuote(String quoteId) {

		// TODO Auto-generated method stub

		if ("".equals(quoteId)) {
			Toast.makeText(QuoteListActivity.this, "error", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("quoteId", quoteId);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/usedQuote",
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
								// 刷新页面
								onRefresh();
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
						Toast.makeText(QuoteListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void purchaseAlternateQuote(String quoteId) {

		// TODO Auto-generated method stub

		if ("".equals(quoteId)) {
			Toast.makeText(QuoteListActivity.this, "error", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("quoteId", quoteId);
		finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/alternateQuote",
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
								// 刷新页面
								onRefresh();
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
						Toast.makeText(QuoteListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	// 全选或全取消
	private void selectedAll() {
		for (int i = 0; i < list.size(); i++) {
			QuoteAdapter.getIsSelected().put(i, flag);
		}
		adapter.notifyDataSetChanged();
	}

	private String deleteOrCheckOutShop() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (QuoteAdapter.getIsSelected().get(i)) {
				sb.append(list.get(i).getId());
				sb.append(",");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	// 弹出对话框询问用户是否删除被选中的商品
	private void showDialogDelete(String str) {
		final String[] delShopIndex = str.split(",");
		new AlertDialog.Builder(QuoteListActivity.this)
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
				float price = (Float) msg.obj;
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
			adapter = new QuoteAdapter(QuoteListActivity.this, list, handler,
					R.layout.list_item_quote);
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
		}, com.hldj.hmyg.application.Data.refresh_time);

	}

}
