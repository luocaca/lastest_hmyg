package com.hldj.hmyg.buyer;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.FlowerDetailActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.ValueGetInfo;
import com.zzy.flowers.widget.popwin.EditP2;

public class BuyerOrderDetailActivity extends NeedSwipeBackActivity {

	private String id = "";
	private ImageView item_shop_photo;
	private TextView item_tv_01;
	private TextView item_tv_02;
	private TextView item_tv_03;
	private TextView item_tv_04;
	private TextView item_tv_05;
	private TextView item_tv_06;
	private TextView item_tv_07;
	private TextView item_tv_08;
	private TextView item_tv_09;
	private TextView item_tv_10;
	private TextView item_tv_caozuo01;
	private TextView item_tv_caozuo02;
	private TextView item_tv_caozuo03;
	private TextView item_tv_data;
	private TextView item_tv_status;
	private FinalBitmap fb;
	private LinearLayout layoutRoot;
	private TextView tv_status;
	private TextView tv_title;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_num01;
	private TextView tv_num02;
	private TextView tv_num03;
	private TextView tv_num04;
	private TextView tv_getReceiptList;
	private TextView tv_pay;
	private LinearLayout ll_add_car;
	private BuyOrderBean bean;
	private double price;
	private ArrayList<BuyOrderBean> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer_order_detail);
		fb = FinalBitmap.create(this);
		fb.configLoadingImage(R.drawable.no_image_show);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_num01 = (TextView) findViewById(R.id.tv_num01);
		tv_num02 = (TextView) findViewById(R.id.tv_num02);
		tv_num03 = (TextView) findViewById(R.id.tv_num03);
		tv_num04 = (TextView) findViewById(R.id.tv_num04);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_add_car_detail = (LinearLayout) findViewById(R.id.ll_add_car_detail);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		ll_add_car = (LinearLayout) findViewById(R.id.ll_add_car);
		layoutRoot = (LinearLayout) findViewById(R.id.layoutRoot);

		item_shop_photo = (ImageView) findViewById(R.id.iv_img);
		item_tv_01 = (TextView) findViewById(R.id.tv_detail_01);
		item_tv_02 = (TextView) findViewById(R.id.tv_detail_02);
		item_tv_03 = (TextView) findViewById(R.id.tv_detail_03);
		item_tv_04 = (TextView) findViewById(R.id.tv_detail_04);
		item_tv_05 = (TextView) findViewById(R.id.tv_detail_05);
		item_tv_06 = (TextView) findViewById(R.id.tv_detail_06);
		item_tv_07 = (TextView) findViewById(R.id.tv_detail_07);
		item_tv_08 = (TextView) findViewById(R.id.tv_detail_08);
		item_tv_09 = (TextView) findViewById(R.id.tv_detail_09);
		item_tv_10 = (TextView) findViewById(R.id.tv_detail_10);
		item_tv_caozuo01 = (TextView) findViewById(R.id.tv_detail_caozuo01);
		item_tv_caozuo02 = (TextView) findViewById(R.id.tv_detail_caozuo02);
		item_tv_caozuo03 = (TextView) findViewById(R.id.tv_detail_caozuo03);
		item_tv_data = (TextView) findViewById(R.id.tv_detail_data);
		item_tv_status = (TextView) findViewById(R.id.tv_detail_status);
		tv_getReceiptList = (TextView) findViewById(R.id.tv_getReceiptList);
		tv_pay = (TextView) findViewById(R.id.tv_pay);

		id = getIntent().getStringExtra("id");
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().getSerializable("list") != null) {
			Bundle bundleObject = getIntent().getExtras();
			list = (ArrayList<BuyOrderBean>) bundleObject
					.getSerializable("list");
			price = bundleObject.getDouble("price");
		}

		init();

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ll_01.setOnClickListener(multipleClickProcess);
		ll_add_car_detail.setOnClickListener(multipleClickProcess);
		tv_getReceiptList.setOnClickListener(multipleClickProcess);
		tv_pay.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
		layoutRoot.setOnClickListener(multipleClickProcess);
	}

	// 初始化数据
	private void init() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/order/detail",
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
								// Toast.makeText(BuyerOrderDetailActivity.this,
								// msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject jsonObject4 = jsonObject
										.getJSONObject("data").getJSONObject(
												"order");
								bean = new BuyOrderBean();
								bean.setId(JsonGetInfo.getJsonString(
										jsonObject4, "seedlingId"));
								tv_title.setText(JsonGetInfo.getJsonString(
										jsonObject4, "num"));
								tv_01.setText("发货单号："
										+ JsonGetInfo.getJsonString(
												jsonObject4, "num"));
								bean.setSpecText(JsonGetInfo.getJsonString(
										jsonObject4, "specText"));
								bean.setName(JsonGetInfo.getJsonString(
										jsonObject4, "name"));
								bean.setCreateDate(JsonGetInfo.getJsonString(
										jsonObject4, "createDate"));
								bean.setStatus(JsonGetInfo.getJsonString(
										jsonObject4, "status"));
								bean.setImg(JsonGetInfo.getJsonString(
										jsonObject4, "mediumImageUrl"));
								bean.setPlantType(JsonGetInfo.getJsonString(
										jsonObject4, "plantType"));
								bean.setDbh(JsonGetInfo.getJsonDouble(
										jsonObject4, "dbh"));
								bean.setHeight(JsonGetInfo.getJsonDouble(
										jsonObject4, "height"));
								bean.setCrown(JsonGetInfo.getJsonDouble(
										jsonObject4, "height"));
								bean.setDeliveryPrice(JsonGetInfo
										.getJsonDouble(jsonObject4,
												"deliveryPrice"));
								bean.setPrice(JsonGetInfo.getJsonDouble(
										jsonObject4, "price"));
								bean.setTotalPrice(JsonGetInfo.getJsonDouble(
										jsonObject4, "totalPrice"));
								bean.setAmount(JsonGetInfo.getJsonDouble(
										jsonObject4, "amount"));
								bean.setCount(JsonGetInfo.getJsonInt(
										jsonObject4, "count"));
								bean.setNum(1);
								bean.setUnitTypeName(JsonGetInfo.getJsonString(
										jsonObject4, "unitTypeName"));
								bean.setTagList(JsonGetInfo.getJsonArray(
										jsonObject4, "tagList").toString());
								bean.setTradeType(JsonGetInfo.getJsonString(
										jsonObject4, "tradeType"));
								bean.setShowChoose(true);
								bean.setChoosed(false);
								tv_num01.setText("下单："
										+ JsonGetInfo.getJsonInt(jsonObject4,
												"allowReceiptInfoCountJson"));
								tv_num02.setText("未收："
										+ JsonGetInfo.getJsonInt(jsonObject4,
												"unReceiptCountJson"));
								tv_num03.setText("已收："
										+ JsonGetInfo.getJsonInt(jsonObject4,
												"receiptedCountJson"));
								tv_num04.setText("退货："
										+ JsonGetInfo.getJsonInt(jsonObject4,
												"returnCountJson"));
								fb.display(item_shop_photo, bean.getImg());
								if (bean.getPlantType().contains("planted")) {
									item_tv_01
											.setBackgroundResource(R.drawable.icon_seller_di);
								} else if (bean.getPlantType().contains(
										"transplant")) {
									item_tv_01
											.setBackgroundResource(R.drawable.icon_seller_yi);
								} else if (bean.getPlantType().contains(
										"heelin")) {
									item_tv_01
											.setBackgroundResource(R.drawable.icon_seller_jia);
								} else if (bean.getPlantType().contains(
										"container")) {
									item_tv_01
											.setBackgroundResource(R.drawable.icon_seller_rong);
								}
								item_tv_02.setText(bean.getName());
								item_tv_03.setText("￥" + ValueGetInfo.doubleTrans1(bean.getPrice()));
								item_tv_03.setVisibility(View.GONE);
								item_tv_04.setText("胸径：" + bean.getDbh()
										+ "高度：" + bean.getHeight() + "冠幅："
										+ bean.getCrown());
								item_tv_05.setText("发货费："
										+ bean.getDeliveryPrice() + "元");
								if (bean.getTradeType().contains("proxy")) {
									item_tv_06.setText("委");
								} else {
									item_tv_06.setText("担");
								}
								item_tv_07.setText(ValueGetInfo.doubleTrans1(bean.getPrice()) + "");
								item_tv_08.setText("元／"
										+ bean.getUnitTypeName());
								item_tv_09.setText(bean.getCount()
										+ bean.getUnitTypeName());
								item_tv_10.setText("合计：" + bean.getTotalPrice());
								if (bean.getStatus().contains("unpay")) {
									item_tv_status.setText("待付款");
									tv_status.setText("待付款");
									tv_pay.setVisibility(View.VISIBLE);
								} else if (bean.getStatus().contains("unsend")) {
									item_tv_status.setText("待发货");
									tv_status.setText("待发货");
								} else if (bean.getStatus().contains(
										"unreceipt")) {
									item_tv_status.setText("待收货");
									tv_status.setText("待收货");
								} else if (bean.getStatus()
										.contains("finished")) {
									item_tv_status.setText("已完成");
									tv_status.setText("已完成");
								}
								tv_02.setText("下单日期：" + bean.getCreateDate());
								item_tv_data.setText(bean.getCreateDate());
								layoutRoot.setVisibility(View.VISIBLE);

								JSONObject customerJson = JsonGetInfo
										.getJSONObject(jsonObject4,
												"customerJson");
								String customerJson_id = JsonGetInfo
										.getJsonString(customerJson, "id");
								tv_03.setText("客服信息");
								tv_04.setText("客服："
										+ JsonGetInfo.getJsonString(
												customerJson, "realName"));
								tv_05.setText("电话："
										+ JsonGetInfo.getJsonString(
												customerJson, "phone"));

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
						Toast.makeText(BuyerOrderDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

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
				case R.id.ll_01:
					if (bean != null) {
						Intent toReceiptListActivity = new Intent(
								BuyerOrderDetailActivity.this,
								OrderReceiptListActivity.class);
						toReceiptListActivity.putExtra("from",
								"BuyerOrderDetailActivity");
						toReceiptListActivity.putExtra("id", id);
						toReceiptListActivity.putExtra("status",
								bean.getStatus());
						startActivity(toReceiptListActivity);
					}

					break;
				case R.id.tv_getReceiptList:
					if (bean != null) {
						Intent toReceiptListActivity = new Intent(
								BuyerOrderDetailActivity.this,
								OrderReceiptListActivity.class);
						toReceiptListActivity.putExtra("from",
								"BuyerOrderDetailActivity");
						toReceiptListActivity.putExtra("id", id);
						toReceiptListActivity.putExtra("status",
								bean.getStatus());
						startActivity(toReceiptListActivity);
					}

					break;
				case R.id.ll_add_car_detail:
					Intent toLoadCarListActivity = new Intent(
							BuyerOrderDetailActivity.this,
							LoadCarListActivity.class);
					toLoadCarListActivity.putExtra("id", id);
					toLoadCarListActivity.putExtra("from",
							"BuyerOrderDetailActivity");
					startActivity(toLoadCarListActivity);
					break;
				case R.id.tv_pay:
					if (list.size() > 0 && price > 0) {
						Intent intent = new Intent(
								BuyerOrderDetailActivity.this,
								CheckOrderActivity.class);
						Bundle bundleObject = new Bundle();
						bundleObject.putSerializable("list", list);
						bundleObject.putDouble("price", price);
						// Put Bundle in to Intent and call start Activity
						intent.putExtras(bundleObject);
						startActivityForResult(intent, 1);
					}
					break;
				case R.id.layoutRoot:
					if (bean != null && !"".equals(bean.getId())) {
						Intent toFlowerDetailActivity = new Intent(
								BuyerOrderDetailActivity.this,
								FlowerDetailActivity.class);
						toFlowerDetailActivity.putExtra("id", bean.getId());
						toFlowerDetailActivity.putExtra("show_type",
								"seedling_list");
						startActivity(toFlowerDetailActivity);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
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

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		setResult(1);
		finish();
		super.onActivityResult(arg0, arg1, arg2);
	}

}
