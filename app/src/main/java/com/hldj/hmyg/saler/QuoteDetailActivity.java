package com.hldj.hmyg.saler;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.LoginActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mrwujay.cascade.activity.BaseActivity;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

@SuppressLint("NewApi")
public class QuoteDetailActivity extends BaseActivity implements
		OnWheelChangedListener, OnClickListener {

	private ImageView btn_back;
	private String id = "";
	private TextView tv_01;
	private TextView tv_ac;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	private TextView tv_07;
	private TextView tv_08;
	private TextView tv_09;
	private TextView tv_10;
	private TextView tv_11;
	private TextView tv_caozuo01;
	private TextView tv_caozuo02;
	private TextView tv_caozuo03;
	private TextView tv_user_01;
	private TextView tv_user_ac;
	private TextView tv_user_03;
	private TextView tv_user_06;
	private TextView tv_user_08;
	private TextView tv_user_10;
	private TextView tv_user_11;
	private TextView tv_user_09;
	private TextView tv_user_07;
	private TextView tv_user_05;
	private TextView tv_user_04;
	private TextView tv_user_02;
	private TextView tv_seller_quote_json_02;
	private TextView tv_seller_quote_json_03;
	private TextView tv_seller_quote_json_04;
	private TextView tv_seller_quote_json_05;
	private TextView tv_seller_quote_json_06;
	private TextView tv_seller_quote_json_07;
	private TextView tv_seller_quote_json_08;
	private TextView tv_seller_quote_json_09;
	private TextView tv_seller_quote_json_caozuo01;
	private TextView tv_seller_quote_json_caozuo02;
	private TextView tv_seller_quote_json_caozuo03;
	private LinearLayout ll_user;
	private LinearLayout ll_seller_quote;
	private LinearLayout ll_login;
	private LinearLayout ll_save;
	private LinearLayout ll_save_quote;
	private LinearLayout ll_05;
	private LinearLayout ll_07;
	private TextView tv_address;
	private TextView tv_needInvoice;
	private EditText et_price;
	private EditText et_num;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private boolean needInvoice;
	private String purchaseItemId = "";
	private String purchaseId = "";
	private EditText et_shuoming;
	private String status = "";

	private double price = 0;
	private int counts = 0;
	private String remarks = "";
	private String cityName = "";
	private String plantType = "";
	private String sellerQuoteJson_id = "";
	ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private KProgressHUD hud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote_detail);
		hud = KProgressHUD.create(QuoteDetailActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		LinearLayout ll_caozuo = (LinearLayout) findViewById(R.id.ll_caozuo);
		ll_user = (LinearLayout) findViewById(R.id.ll_user);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		ll_seller_quote = (LinearLayout) findViewById(R.id.ll_seller_quote);
		ll_save_quote = (LinearLayout) findViewById(R.id.ll_save_quote);
		Button save = (Button) findViewById(R.id.save);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		tv_pics = (TextView) findViewById(R.id.tv_pics);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_TypeName = (TextView) findViewById(R.id.tv_TypeName);
		tv_needInvoice = (TextView) findViewById(R.id.tv_needInvoice);
		et_price = (EditText) findViewById(R.id.et_price);
		et_num = (EditText) findViewById(R.id.et_num);
		et_num.addTextChangedListener(mTextWatcher);
		et_shuoming = (EditText) findViewById(R.id.et_shuoming);
		ll_caozuo.setVisibility(View.GONE);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_ac = (TextView) findViewById(R.id.tv_ac);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		tv_07 = (TextView) findViewById(R.id.tv_07);
		tv_08 = (TextView) findViewById(R.id.tv_08);
		tv_09 = (TextView) findViewById(R.id.tv_09);
		tv_10 = (TextView) findViewById(R.id.tv_10);
		tv_11 = (TextView) findViewById(R.id.tv_11);

		tv_user_01 = (TextView) findViewById(R.id.tv_user_01);
		tv_user_ac = (TextView) findViewById(R.id.tv_user_ac);
		tv_user_02 = (TextView) findViewById(R.id.tv_user_02);
		tv_user_03 = (TextView) findViewById(R.id.tv_user_03);
		tv_user_04 = (TextView) findViewById(R.id.tv_user_04);
		tv_user_05 = (TextView) findViewById(R.id.tv_user_05);
		tv_user_06 = (TextView) findViewById(R.id.tv_user_06);
		tv_user_07 = (TextView) findViewById(R.id.tv_user_07);
		tv_user_08 = (TextView) findViewById(R.id.tv_user_08);
		tv_user_09 = (TextView) findViewById(R.id.tv_user_09);
		tv_user_10 = (TextView) findViewById(R.id.tv_user_10);
		tv_user_11 = (TextView) findViewById(R.id.tv_user_11);
		tv_caozuo01 = (TextView) findViewById(R.id.tv_caozuo01);
		tv_caozuo02 = (TextView) findViewById(R.id.tv_caozuo02);
		tv_caozuo03 = (TextView) findViewById(R.id.tv_caozuo03);

		tv_seller_quote_json_02 = (TextView) findViewById(R.id.tv_seller_quote_json_02);
		tv_seller_quote_json_03 = (TextView) findViewById(R.id.tv_seller_quote_json_03);
		tv_seller_quote_json_04 = (TextView) findViewById(R.id.tv_seller_quote_json_04);
		tv_seller_quote_json_05 = (TextView) findViewById(R.id.tv_seller_quote_json_05);
		tv_seller_quote_json_06 = (TextView) findViewById(R.id.tv_seller_quote_json_06);
		tv_seller_quote_json_07 = (TextView) findViewById(R.id.tv_seller_quote_json_07);
		tv_seller_quote_json_08 = (TextView) findViewById(R.id.tv_seller_quote_json_08);
		tv_seller_quote_json_09 = (TextView) findViewById(R.id.tv_seller_quote_json_09);
		tv_seller_quote_json_caozuo01 = (TextView) findViewById(R.id.tv_seller_quote_json_caozuo01);
		tv_seller_quote_json_caozuo02 = (TextView) findViewById(R.id.tv_seller_quote_json_caozuo02);
		tv_seller_quote_json_caozuo03 = (TextView) findViewById(R.id.tv_seller_quote_json_caozuo03);

		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			initData1();
			initData();
		}
		if (MyApplication.Userinfo.getBoolean("isLogin", false) == true) {
			ll_login.setVisibility(View.GONE);
		}

		btn_back.setOnClickListener(this);
		ll_login.setOnClickListener(this);
		ll_save.setOnClickListener(this);
		ll_01.setOnClickListener(this);
		ll_05.setOnClickListener(this);
		ll_06.setOnClickListener(this);
		ll_07.setOnClickListener(this);
		tv_seller_quote_json_caozuo02.setOnClickListener(this);
		tv_seller_quote_json_caozuo03.setOnClickListener(this);
		save.setOnClickListener(this);

	}

	// 在一开始声明TextWatcher，在afterTextChange内操作
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String text = s.toString();
			int len = s.toString().length();
			if (len == 1 && text.equals("0")) {
				s.clear();
			}
		}

	};
	private LinearLayout ll_06;
	private TextView tv_pics;
	private LinearLayout ll_01;
	private TextView tv_TypeName;

	public void initData1() {
		tv_01.setText("品种名称：" + getIntent().getStringExtra("name"));
		tv_ac.setText(getIntent().getStringExtra("count")
				+ getIntent().getStringExtra("unitTypeName"));
		tv_02.setText("采购单：" + getIntent().getStringExtra("num"));
		tv_03.setText("用苗地：" + getIntent().getStringExtra("fullName"));
		tv_04.setText("期望收货日期：" + getIntent().getStringExtra("receiptDate"));
		tv_05.setText("截至日期：" + getIntent().getStringExtra("closeDate"));
		tv_06.setText("分类：" + getIntent().getStringExtra("firstTypeName"));
		tv_07.setText("种植类型：" + getIntent().getStringExtra("plantTypeName"));
		tv_10.setText("其他要求：" + getIntent().getStringExtra("remarks"));
		tv_08.setText("参数：地径：" + getIntent().getStringExtra("diameter") + "高度："
				+ getIntent().getStringExtra("height") + "冠幅："
				+ getIntent().getStringExtra("crown"));
		tv_caozuo01.setText("已有："
				+ getIntent().getStringExtra("quoteCountJson") + "条报价");
	}

	private void initData() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("userId", MyApplication.Userinfo.getString("id", ""));
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/detail", params,
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
								// Toast.makeText(ManagerListActivity.this, msg,
								// Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject item = JsonGetInfo.getJSONObject(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "item");
								JSONObject buyer = JsonGetInfo.getJSONObject(
										item, "buyer");
								JSONObject sellerQuoteJson = JsonGetInfo
										.getJSONObject(item, "sellerQuoteJson");
								int count = JsonGetInfo.getJsonInt(item,
										"count");
								int quoteCountJson = JsonGetInfo.getJsonInt(
										item, "quoteCountJson");
								purchaseId = JsonGetInfo.getJsonString(item,
										"purchaseId");
								purchaseItemId = JsonGetInfo.getJsonString(
										item, "id");

								String publicName = JsonGetInfo.getJsonString(
										buyer, "publicName");
								String realName = JsonGetInfo.getJsonString(
										buyer, "realName");
								String companyName = JsonGetInfo.getJsonString(
										buyer, "companyName");
								String publicPhone = JsonGetInfo.getJsonString(
										buyer, "publicPhone");
								String address = JsonGetInfo.getJsonString(
										buyer, "address");
								tv_user_01.setText("采购商家信息");
								if (!"".equals(companyName)) {
									tv_user_02.setText("采购商家：" + companyName);
								} else if ("".equals(companyName)
										&& !"".equals(publicName)) {
									tv_user_02.setText("采购商家：" + publicName);
								} else if ("".equals(companyName)
										&& "".equals(publicName)) {
									tv_user_02.setText("采购商家：" + realName);
								}
								tv_user_04.setText("所在地区：" + address);
								tv_user_06.setText("采购数量：");
								tv_user_07.setText("已有报价：" + quoteCountJson
										+ "条");
								if (MyApplication.Userinfo.getBoolean(
										"isLogin", false) == false) {
									tv_user_02.setText("采购商家登陆后可查看");
									tv_user_08.setText("联系电话："
											+ getDisplayStr(publicPhone));
								} else {
									ll_user.setVisibility(View.VISIBLE);
								}
								boolean isQuoted = JsonGetInfo.getJsonBoolean(
										item, "isQuoted");

								sellerQuoteJson_id = JsonGetInfo.getJsonString(
										sellerQuoteJson, "id");
								remarks = JsonGetInfo.getJsonString(
										sellerQuoteJson, "remarks");
								mCurrentZipCode = JsonGetInfo.getJsonString(
										sellerQuoteJson, "cityCode");
								cityName = JsonGetInfo.getJsonString(
										sellerQuoteJson, "cityName");
								needInvoice = JsonGetInfo.getJsonBoolean(
										sellerQuoteJson, "isInvoice");
								boolean isAudit = JsonGetInfo.getJsonBoolean(
										sellerQuoteJson, "isAudit");

								status = JsonGetInfo.getJsonString(
										sellerQuoteJson, "status");
								price = JsonGetInfo.getJsonDouble(
										sellerQuoteJson, "price");
								counts = JsonGetInfo.getJsonInt(
										sellerQuoteJson, "count");
								tv_seller_quote_json_02.setText("苗木报价");
								tv_seller_quote_json_03.setText("以下是您的报价记录");
								tv_seller_quote_json_04.setText("价格：" + price);
								tv_seller_quote_json_05.setText("可供数量："
										+ counts);
								tv_seller_quote_json_06.setText("苗源地址："
										+ cityName);
								tv_seller_quote_json_08.setText("报价说明："
										+ remarks);

								if ("unsubmit".equals(status)) {
									tv_seller_quote_json_caozuo02.setText("编辑");
									tv_seller_quote_json_caozuo03.setText("删除");
									tv_seller_quote_json_caozuo02
											.setVisibility(View.VISIBLE);
									tv_seller_quote_json_caozuo03
											.setVisibility(View.VISIBLE);
								} else if ("unaudit".equals(status)) {
									tv_seller_quote_json_caozuo02
											.setVisibility(View.INVISIBLE);
									tv_seller_quote_json_caozuo03.setText("撤回");
									tv_seller_quote_json_caozuo03
											.setVisibility(View.VISIBLE);
								} else if ("quoted".equals(status)) {

								} else if ("backed".equals(status)) {
									tv_seller_quote_json_caozuo02.setText("编辑");
									tv_seller_quote_json_caozuo03.setText("删除");
									tv_seller_quote_json_caozuo02
											.setVisibility(View.VISIBLE);
									tv_seller_quote_json_caozuo03
											.setVisibility(View.VISIBLE);

								} else if ("invalid".equals(status)) {
								}
								if (MyApplication.Userinfo.getBoolean(
										"isLogin", false) == false) {

								} else {
									ll_login.setVisibility(View.GONE);
									ll_user.setVisibility(View.VISIBLE);
									ll_seller_quote.setVisibility(View.VISIBLE);
									if (isQuoted == true) {
										ll_seller_quote
												.setVisibility(View.VISIBLE);
										ll_save_quote.setVisibility(View.GONE);
										ll_save.setVisibility(View.GONE);
									} else {
										ll_seller_quote
												.setVisibility(View.GONE);
										ll_save_quote
												.setVisibility(View.VISIBLE);
										ll_save.setVisibility(View.VISIBLE);
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
						Toast.makeText(QuoteDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private String getDisplayStr(String realStr) {
		String result = new String(realStr);
		char[] cs = result.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (i >= 3 && i <= 10) {// 把3和10区间的字符隐藏
				cs[i] = '*';
			}
		}
		return new String(cs);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.ll_login:
			Intent toLoginActivity = new Intent(QuoteDetailActivity.this,
					LoginActivity.class);
			startActivityForResult(toLoginActivity, 4);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.ll_01:

			new ActionSheetDialog(QuoteDetailActivity.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.setTitle("种植类型")
					.addSheetItem("地栽苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "planted";
									tv_TypeName.setText("地栽苗");
								}
							})
					.addSheetItem("移植苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "transplant";
									tv_TypeName.setText("移植苗");
								}
							})
					.addSheetItem("假植苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "heelin";
									tv_TypeName.setText("假植苗");

								}
							})

					.addSheetItem("容器苗", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									plantType = "container";
									tv_TypeName.setText("容器苗");
								}
							}).show();

			break;
		case R.id.ll_05:
			showCitys();
			break;
		case R.id.ll_06:
			Intent toUpdataImageActivity = new Intent(QuoteDetailActivity.this,
					UpdataImageActivity.class);
			Bundle bundleObject = new Bundle();
			final PicSerializableMaplist myMap = new PicSerializableMaplist();
			myMap.setMaplist(urlPaths);
			bundleObject.putSerializable("urlPaths", myMap);
			toUpdataImageActivity.putExtras(bundleObject);
			startActivityForResult(toUpdataImageActivity, 1);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;

		case R.id.ll_07:
			new ActionSheetDialog(QuoteDetailActivity.this)
					.builder()
					.setTitle("是否需要发票")
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("是", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									tv_needInvoice.setText("是");
									needInvoice = true;

								}
							})
					.addSheetItem("否", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									tv_needInvoice.setText("否");
									needInvoice = false;
								}
							}).show();
			break;
		case R.id.save:
			// 隐藏关闭输入法
			if ("".equals(GetCodeByName.initProvinceDatasDocument(
					QuoteDetailActivity.this, mCurrentProviceName,
					mCurrentCityName))) {
				Toast.makeText(QuoteDetailActivity.this, "请选择苗源地址所在城市",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if ("".equals(plantType)) {
				Toast.makeText(QuoteDetailActivity.this, "请选择种植类型",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if ("".equals(et_num.getText().toString())) {
				Toast.makeText(QuoteDetailActivity.this, "请输入数量",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if ("".equals(et_price.getText().toString())) {
				Toast.makeText(QuoteDetailActivity.this, "请输入价格",
						Toast.LENGTH_SHORT).show();

				return;
			}
			if (Double.parseDouble(et_price.getText().toString()) <= 0) {
				Toast.makeText(QuoteDetailActivity.this, "请输入超过0的价格",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (urlPaths.size() > 0) {
				Data.pics1.clear();
				hud.show();
				for (int i = 0; i < urlPaths.size(); i++) {
					Data.pics1.add(urlPaths.get(i));
				}
				quoteSave();
			} else {
				Toast.makeText(QuoteDetailActivity.this, "请选择图片",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.tv_seller_quote_json_caozuo02:
			if ("unsubmit".equals(status)) {
				quoteEdit();
			} else if ("unaudit".equals(status)) {
			} else if ("quoted".equals(status)) {
			} else if ("backed".equals(status)) {
				quoteEdit();
			} else if ("invalid".equals(status)) {
			}
			break;
		case R.id.tv_seller_quote_json_caozuo03:
			if ("unsubmit".equals(status)) {
				// 删除
				quoteDdel();
			} else if ("unaudit".equals(status)) {
				// 撤回
				quoteRevoke();
			} else if ("quoted".equals(status)) {
				//
			} else if ("backed".equals(status)) {
				// 删除
				quoteDdel();
			} else if ("invalid".equals(status)) {
			}
			break;

		default:
			break;
		}
	}

	// 编辑
	public void quoteEdit() {
		ll_save_quote.setVisibility(View.VISIBLE);
		ll_save.setVisibility(View.VISIBLE);
		et_num.setText(counts + "");
		et_price.setText(price + "");
		tv_address.setText(cityName);
		if (needInvoice) {
			tv_needInvoice.setText("提供发票");
		} else {
			tv_needInvoice.setText("不提供发票");
		}
		et_shuoming.setText(remarks);

	}

	public void quoteRevoke() {
		// TODO Auto-generated method stub
		if ("".equals(sellerQuoteJson_id)) {
			Toast.makeText(QuoteDetailActivity.this, "error",
					Toast.LENGTH_SHORT).show();
			return;
		}
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", sellerQuoteJson_id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/revoke", params,
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
								initData1();
								initData();

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
						Toast.makeText(QuoteDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void quoteDdel() {
		// TODO Auto-generated method stub

		if ("".equals(sellerQuoteJson_id)) {
			Toast.makeText(QuoteDetailActivity.this, "error",
					Toast.LENGTH_SHORT).show();
			return;
		}
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);

		AjaxParams params = new AjaxParams();
		params.put("id", sellerQuoteJson_id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/del", params,
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
								initData1();
								initData();
								ll_seller_quote.setVisibility(View.GONE);

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
						Toast.makeText(QuoteDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void quoteSave() {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null
				&& getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(QuoteDetailActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);

		AjaxParams params = new AjaxParams();
		if (!"".equals(sellerQuoteJson_id)) {
			params.put("id", sellerQuoteJson_id);
		}
		params.put("cityCode", mCurrentZipCode.substring(0, 4));
		params.put("isInvoice", needInvoice + "");
		params.put("purchaseId", purchaseId);
		params.put("purchaseItemId", purchaseItemId);
		params.put("plantType", plantType);
		params.put("price", et_price.getText().toString());
		params.put("count", et_num.getText().toString());
		params.put("remarks", et_shuoming.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/quote/save", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						if (hud != null
								&& !QuoteDetailActivity.this.isFinishing()) {
							hud.dismiss();
						}
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(QuoteDetailActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {

								initData1();
								initData();
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
						if (hud != null
								&& !QuoteDetailActivity.this.isFinishing()) {
							hud.dismiss();
						}
						Toast.makeText(QuoteDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 5) {
			Bundle bundle = data.getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			tv_pics.setText(PicValiteIsUtils.notiPicValite(urlPaths));

		} else if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			initData1();
			initData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showCitys() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city_for_purchase, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		setUpData();

		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(dia_choose_share, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_address.setText(mCurrentProviceName + "\u0020"
						+ mCurrentCityName + "\u0020");
				if (!QuoteDetailActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!QuoteDetailActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!QuoteDetailActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				QuoteDetailActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private void showSelectedResult() {
		Toast.makeText(
				QuoteDetailActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

}
