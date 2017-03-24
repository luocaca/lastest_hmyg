package com.hldj.hmyg.buyer;

import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dyr.custom.GetValidatePriceCustomDialog;
import com.dyr.custom.PayCustomDialog;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseActivity;
import com.unionpay.UPPayAssistEx;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

public class CheckOrderActivity extends BaseActivity implements OnClickListener {

	/**
	 */

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	
	private final String mMode = "00";
	private ImageView btn_back;
	private CheckBox checkBox;
	private ListView listView;
	private TextView popTotalPrice; // 结算的价格
	private TextView popDelete; // 删除
	private TextView popRecycle; // 收藏
	private TextView popCheckOut; // 结算
	private LinearLayout layout; // 结算布局
	private CheckOutAdapter adapter;
	private boolean flag = true; // 全选或全取消
	private double price;
	private ArrayList<BuyOrderBean> list;
	private String id = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_order);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		initViews();
		Bundle bundleObject = getIntent().getExtras();
		list = (ArrayList<BuyOrderBean>) bundleObject.getSerializable("list");
		price = bundleObject.getDouble("price");
		if (adapter == null) {
			adapter = new CheckOutAdapter(CheckOrderActivity.this, list,
					R.layout.list_item_cart_product);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		popTotalPrice.setText("¥" + price);
	}

	// 初始化UI界面
	private void initViews() {
		checkBox = (CheckBox) findViewById(R.id.all_check);
		listView = (ListView) findViewById(R.id.main_listView);
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

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	// 获取集合数据

	// 事件点击监听器
	private final class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_check: // 全选
				break;
			case R.id.delete: // 线下付款

				getValidatePrice(list.size(), "", "");
				break;
			case R.id.checkOut: // 结算 线上付款
				new ActionSheetDialog(CheckOrderActivity.this)
						.builder()
						.setTitle("在线支付只支持个人账户付款，对公账户请到网页版操作，是否继续？")
						.setCancelable(false)
						.setCanceledOnTouchOutside(false)
						.addSheetItem("是", SheetItemColor.Blue,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										StringBuffer ids = new StringBuffer();
										for (int i = 0; i < list.size(); i++) {
											ids.append(list.get(i).getId()
													+ ",");
										}
										if (ids.length() > 0) {
											ids.deleteCharAt(ids.length() - 1);
											appConsumeToPay(ids.toString(),
													"ORDER", "");
										}

									}
								}).show();
				break;
			}
		}
	}

	public void appConsumeToPay(String ids, String stype, String bizType) {

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("ids", ids);
		params.put("stype", stype);
		params.put("bizType", bizType);
		finalHttp.post(GetServerUrl.getUrl() + "admin/appConsume/toPay",
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
							JSONObject data = JsonGetInfo.getJSONObject(
									jsonObject, "data");
							if (!"".equals(msg)) {
								// Toast.makeText(CheckOrderActivity.this, msg,
								// Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								id = JsonGetInfo.getJsonString(data, "id");
								String tn = JsonGetInfo.getJsonString(data,
										"tn");
								UPPayAssistEx.startPay(CheckOrderActivity.this,
										null, null, tn, mMode);
							} else if ("6007".equals(code)) {

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
						Toast.makeText(CheckOrderActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void getValidatePrice(final int num, final String str_itemData,
			final String cartIds) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "common/getAccount", params,
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
							if ("1".equals(code) && !jsonObject.isNull("data")) {
								// 验苗

								JSONObject jsonObject3 = JsonGetInfo
										.getJSONObject(jsonObject, "data");
								JSONObject accountInfo = JsonGetInfo
										.getJSONObject(jsonObject3,
												"accountInfo");
								String accountName = JsonGetInfo.getJsonString(
										accountInfo, "accountName");
								String accountBank = JsonGetInfo.getJsonString(
										accountInfo, "accountBank");
								String accountNum = JsonGetInfo.getJsonString(
										accountInfo, "accountNum");
								double totalPrice = JsonGetInfo.getJsonDouble(
										accountInfo, "price");
								int count = num;
								// 未付款
								PayCustomDialog.Builder builder = new PayCustomDialog.Builder(
										CheckOrderActivity.this);
								builder.setTitle("线下付款");
								builder.setPrice(totalPrice + "");
								builder.setCount(count + "");
								builder.setAccountName(accountName);
								builder.setAccountBank(accountBank);
								builder.setAccountNum(accountNum);
								builder.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												finish();
												// 设置你的操作事项
											}
										});

								builder.setNegativeButton(
										"取消",
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});

								builder.create().show();

							} else if ("1".equals(code)
									&& jsonObject.isNull("data")) {

								GetValidatePriceCustomDialog.Builder builder = new GetValidatePriceCustomDialog.Builder(
										CheckOrderActivity.this);
								builder.setTitle("申请验苗");
								builder.setPrice("");
								builder.setCount("");
								builder.setAccountName("");
								builder.setAccountBank("");
								builder.setAccountNum("");
								builder.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
												// 设置你的操作事项
											}
										});

								builder.setNegativeButton(
										"取消",
										new android.content.DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});

								builder.create().show();

							} else if ("6007".equals(code)) {

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
						Toast.makeText(CheckOrderActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			// 支付成功后，extra中如果存在result_data，取出校验
			// result_data结构见c）result_data参数说明
			if (data.hasExtra("result_data")) {
				String result = data.getExtras().getString("result_data");
				try {
					JSONObject resultJson = new JSONObject(result);
					String sign = resultJson.getString("sign");
					String dataOrg = resultJson.getString("data");
					// 验签证书同后台验签证书
					// 此处的verify，商户需送去商户后台做验签
					boolean ret = verify(dataOrg, sign, mMode);
					if (ret) {
						// 验证通过后，显示支付结果
						appConsumeVerifyAppData(id);
						msg = "支付成功！";
					} else {
						// 验证不通过后的处理
						// 建议通过商户后台查询支付结果
						msg = "支付失败！";
					}
				} catch (JSONException e) {
				}
			} else {
				// 未收到签名信息
				// 建议通过商户后台查询支付结果
				appConsumeVerifyAppData(id);
				msg = "支付成功！";
			}
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("支付结果通知");
		// builder.setMessage(msg);
		// builder.setInverseBackgroundForced(true);
		// // builder.setCustomTitle();
		// builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();
	}

	private void appConsumeVerifyAppData(String payInfoId) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("payInfoId", payInfoId);
		finalHttp.post(
				GetServerUrl.getUrl() + "admin/appConsume/verifyAppData",
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
								Toast.makeText(CheckOrderActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								setResult(1);
								finish();

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
						Toast.makeText(CheckOrderActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private boolean verify(String msg, String sign64, String mode) {
		// 此处的verify，商户需送去商户后台做验签

		return true;

	}

}
