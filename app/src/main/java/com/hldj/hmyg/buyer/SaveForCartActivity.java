package com.hldj.hmyg.buyer;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.WebActivity;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.saveForCart;
import com.hldj.hmyg.buy.adapter.SaveForCartSeedlingAdapter;
import com.hldj.hmyg.buy.bean.OrderPreSave;
import com.hldj.hmyg.buy.bean.SaveFortCartSeedling;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class SaveForCartActivity extends NeedSwipeBackActivity {

	boolean getdata; // 避免刷新多出数据

	private MultipleClickProcess multipleClickProcess;
	private String str_SaveForCart = "";
	private String str_itemData = "";
	private String cartIds = "";
	private String t = "";
	private String tag = "";

	private TextView tv_01;

	private TextView tv_02;

	private TextView tv_03;

	private TextView tv_04;

	private TextView tv_05;

	private TextView tv_06;

	private TextView tv_07;

	private TextView tv_08;

	private LinearLayout ll_01;

	private SaveForCartSeedlingAdapter listAdapter;
	private ArrayList<SaveFortCartSeedling> datas = new ArrayList<SaveFortCartSeedling>();
	private ArrayList<BuyOrderBean> list = new ArrayList<BuyOrderBean>();// 购物车数据集合
	private ListView xlistView;
	private Gson gson;

	private double totalPrice= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_for_cart);
		gson = new Gson();
		if (getIntent().getStringExtra("str_SaveForCart") != null) {
			str_SaveForCart = getIntent().getStringExtra("str_SaveForCart");
		}
		if (getIntent().getStringExtra("str_itemData") != null) {
			str_itemData = getIntent().getStringExtra("str_itemData");
		}
		if (getIntent().getStringExtra("cartIds") != null) {
			cartIds = getIntent().getStringExtra("cartIds");
		}
		if (getIntent().getStringExtra("t") != null) {
			t = getIntent().getStringExtra("t");
		}
		if (getIntent().getStringExtra("tag") != null) {
			tag = getIntent().getStringExtra("tag");
		}

		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		xlistView = (ListView) findViewById(R.id.xlistView);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		tv_07 = (TextView) findViewById(R.id.tv_07);
		tv_08 = (TextView) findViewById(R.id.tv_08);
		TextView tv_save = (TextView) findViewById(R.id.tv_save);
		tv_title.setText("下单详情");
		multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		tv_save.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		initData(t);
	}

	private void initData(String t) {
		try {
			JSONObject jsonObject = new JSONObject(t);
			JSONObject data = JsonGetInfo.getJSONObject(jsonObject, "data");
			int count = JsonGetInfo.getJsonInt(data, "count");
			totalPrice = JsonGetInfo.getJsonDouble(data, "totalPrice");
			double deliveryTotalPrice = JsonGetInfo.getJsonDouble(data,
					"deliveryTotalPrice");
			double seedlingTotalPrice = JsonGetInfo.getJsonDouble(data,
					"seedlingTotalPrice");
			JSONObject accountInfo = JsonGetInfo.getJSONObject(data,
					"accountInfo");

			String accountName = JsonGetInfo.getJsonString(accountInfo,
					"accountName");
			String accountBank = JsonGetInfo.getJsonString(accountInfo,
					"accountBank");
			String accountNum = JsonGetInfo.getJsonString(accountInfo,
					"accountNum");
			final double price = JsonGetInfo
					.getJsonDouble(accountInfo, "price");

			tv_05.setText("若线下付款，付款至以下账户：" + "");
			tv_06.setText("账户名称：" + accountName);
			tv_07.setText("开户行：" + accountBank);
			tv_08.setText("帐号：" + accountNum);
			tv_01.setText("" + count);
			tv_02.setText("" + seedlingTotalPrice);
			tv_03.setText("" + deliveryTotalPrice);
			tv_04.setText("" + totalPrice);

			JSONArray orderList = JsonGetInfo.getJsonArray(data, "orderList");
			if (orderList.length() > 0) {
				for (int j = 0; j < orderList.length(); j++) {
					JSONObject jsonObject4 = orderList.getJSONObject(j);
					SaveFortCartSeedling products_hash = new SaveFortCartSeedling();
					products_hash.setPlantType(JsonGetInfo.getJsonString(
							jsonObject4, "plantType"));
					products_hash.setUnitType(JsonGetInfo.getJsonString(
							jsonObject4, "unitType"));
					products_hash.setImageUrl(JsonGetInfo.getJsonString(
							jsonObject4, "imageUrl"));
					products_hash.setUnitTypeName(JsonGetInfo.getJsonString(
							jsonObject4, "unitTypeName"));
					products_hash.setName(JsonGetInfo.getJsonString(
							jsonObject4, "name"));
					products_hash.setSeedlingId(JsonGetInfo.getJsonString(
							jsonObject4, "seedlingId"));
					products_hash.setPrice(JsonGetInfo.getJsonDouble(
							jsonObject4, "price"));
					products_hash.setCount(JsonGetInfo.getJsonInt(jsonObject4,
							"count"));
					if ("".equals(JsonGetInfo.getJsonString(jsonObject4,
							"tradeType"))) {
						if (JsonGetInfo.getJsonArray(jsonObject4, "tagList")
								.toString().contains("ziying")) {
							products_hash.setTradeType("proxy");
						}
					} else {
						products_hash.setTradeType(JsonGetInfo.getJsonString(
								jsonObject4, "tradeType"));
					}
					products_hash.setSelfSupport(JsonGetInfo.getJsonBoolean(
							jsonObject4, "isSelfSupport"));
					products_hash.setPartners(JsonGetInfo.getJsonBoolean(
							jsonObject4, "isPartners"));
					products_hash.setCashOnDelivery(JsonGetInfo.getJsonBoolean(
							jsonObject4, "cashOnDelivery"));
					products_hash.setTradeTypeName(JsonGetInfo.getJsonString(
							jsonObject4, "tradeTypeName"));
					products_hash.setTotalPrice(JsonGetInfo.getJsonDouble(
							jsonObject4, "totalPrice"));
					products_hash.setAmount(JsonGetInfo.getJsonDouble(
							jsonObject4, "amount"));
					products_hash.setTaxPrice(JsonGetInfo.getJsonDouble(
							jsonObject4, "taxPrice"));
					products_hash.setTagList(JsonGetInfo.getJsonArray(
							jsonObject4, "tagList").toString());
					products_hash.setDeliveryPrice(JsonGetInfo.getJsonDouble(
							jsonObject4, "deliveryPrice"));
					datas.add(products_hash);

					if (listAdapter != null) {
						listAdapter.notifyDataSetChanged();
					}
				}
				if (listAdapter == null) {
					listAdapter = new SaveForCartSeedlingAdapter(
							SaveForCartActivity.this, datas);
					xlistView.setAdapter(listAdapter);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				case R.id.tv_save:
					if ("cart".equals(tag)) {
						ArrayList<saveForCart> saveForCarts = new ArrayList<saveForCart>();
						try {
							JSONArray jsonArray = new JSONArray(str_SaveForCart);
							if (jsonArray.length() == datas.size()) {
								for (int i = 0; i < datas.size(); i++) {
									saveForCarts.add(new saveForCart(
											JsonGetInfo.getJsonString(
													jsonArray.getJSONObject(i),
													"seedlingId"), JsonGetInfo
													.getJsonInt(jsonArray
															.getJSONObject(i),
															"count"), datas
													.get(i).getTradeType()));
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						orderSaveForCart(gson.toJson(saveForCarts), cartIds);
					} else if ("order".equals(tag)) {
						ArrayList<OrderPreSave> orderPreSaves = new ArrayList<OrderPreSave>();
						try {
							// JSONArray jsonArray = new
							// JSONArray(str_SaveForCart);
							JSONArray jsonArray2 = new JSONArray(str_itemData);
							if (jsonArray2.length() == datas.size()) {
								for (int i = 0; i < datas.size(); i++) {
//									 这里的seedlingId其实是验苗项ItemID
									 orderPreSaves.add(new OrderPreSave(
									 JsonGetInfo.getJsonString(
									 jsonArray2.getJSONObject(i),
									 "seedlingId"), datas.get(i)
									 .getTradeType(),
									 JsonGetInfo.getJsonInt(
									 jsonArray2.getJSONObject(i),
									 "count")));
								}
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						orderSaveForCart(gson.toJson(orderPreSaves), cartIds);
					}

					break;
				case R.id.ll_01:
					Intent toWebActivity3 = new Intent(
							SaveForCartActivity.this, WebActivity.class);
					toWebActivity3.putExtra("title", "委托发货和资金担保的区别");
					toWebActivity3.putExtra("url", Data.proxyAssureDesc);
					startActivityForResult(toWebActivity3, 1);
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

		private void orderSaveForCart(String ordersData, String cartIds) {
			// TODO Auto-generated method stub
			list.clear();
			String post_url = "";
			if ("cart".equals(tag)) {
				post_url = "admin/buyer/order/saveForCart";
			} else if ("order".equals(tag)) {
				post_url = "admin/buyer/order/save";
			}
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("ordersData", ordersData);
			params.put("cartIds", cartIds);
			finalHttp.post(GetServerUrl.getUrl() + post_url, params,
					new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								if ("1".equals(code)) {
									JSONObject jsonObject2 = JsonGetInfo
											.getJSONObject(jsonObject, "data");
									if (JsonGetInfo.getJsonArray(jsonObject2,
											"orderList").length() > 0) {
										JSONArray jsonArray_cartList = JsonGetInfo
												.getJsonArray(jsonObject2,
														"orderList");
										for (int j = 0; j < jsonArray_cartList
												.length(); j++) {
											JSONObject jsonObject4 = jsonArray_cartList
													.getJSONObject(j);
											BuyOrderBean bean = new BuyOrderBean();
											bean.setId(JsonGetInfo
													.getJsonString(jsonObject4,
															"id"));
											bean.setSpecText(JsonGetInfo.getJsonString(
													jsonObject4, "specText"));
											bean.setName(JsonGetInfo
													.getJsonString(jsonObject4,
															"name"));
											bean.setCreateDate(JsonGetInfo
													.getJsonString(jsonObject4,
															"createDate"));
											bean.setStatus("unpay");
											bean.setImg(JsonGetInfo
													.getJsonString(jsonObject4,
															"mediumImageUrl"));
											bean.setPlantType(JsonGetInfo
													.getJsonString(jsonObject4,
															"plantType"));
											bean.setDbh(JsonGetInfo
													.getJsonDouble(jsonObject4,
															"dbh"));
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
											bean.setCount(JsonGetInfo
													.getJsonInt(jsonObject4,
															"count"));
											bean.setNum(1);
											bean.setUnitTypeName(JsonGetInfo
													.getJsonString(jsonObject4,
															"unitTypeName"));
											bean.setTagList(JsonGetInfo
													.getJsonArray(jsonObject4,
															"tagList")
													.toString());
											bean.setTradeType(JsonGetInfo
													.getJsonString(jsonObject4,
															"tradeType"));

											bean.setSelfSupport(JsonGetInfo
													.getJsonBoolean(
															jsonObject4,
															"isSelfSupport"));
											bean.setFreeValidate(JsonGetInfo
													.getJsonBoolean(
															jsonObject4,
															"freeValidatePrice"));
											bean.setCashOnDelivery(JsonGetInfo
													.getJsonBoolean(
															jsonObject4,
															"cashOnDelivery"));
											bean.setFreeDeliveryPrice(JsonGetInfo
													.getJsonBoolean(
															jsonObject4,
															"freeDeliveryPrice"));
											bean.setFreeValidate(JsonGetInfo
													.getJsonBoolean(
															jsonObject4,
															"freeValidate"));

											bean.setShowChoose(true);
											bean.setChoosed(false);
											list.add(bean);
										}

									}
									if (list.size() > 0 && totalPrice > 0) {
										Intent intent = new Intent(
												SaveForCartActivity.this,
												CheckOrderActivity.class);
										Bundle bundleObject = new Bundle();
										bundleObject.putSerializable("list",
												list);
										bundleObject.putDouble("price",
												totalPrice);
										// Put Bundle in to Intent and call
										// start Activity
										intent.putExtras(bundleObject);
										startActivityForResult(intent, 1);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode ==1){
			setResult(1);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
