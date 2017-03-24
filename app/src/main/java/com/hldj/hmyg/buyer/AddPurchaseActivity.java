package com.hldj.hmyg.buyer;

import info.hoang8f.android.segmented.SegmentedGroup;
import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Purchase;
import com.hldj.hmyg.saler.ChooseFirstTypeActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

public class AddPurchaseActivity extends NeedSwipeBackActivity implements
		OnCheckedChangeListener {

	private TextView tv_TypeName;
	private TextView tv_unitType;
	private String seedlingParams = "";
	private String firstSeedlingTypeName = "";
	private String firstSeedlingTypeId = "";
	private EditText et_name;
	private EditText et_count;
	private EditText et_dbh;
	private EditText et_height;
	private EditText et_crown;
	private EditText et_diameter;
	private EditText et_offbarHeight;
	private EditText et_length;
	private EditText et_remark;

	private String count = "";

	private String diameter = "";

	private String diameterType = "";

	private String dbh = "";

	private String dbhType = "";

	private String height = "";

	private String crown = "";

	private String offbarHeight = "";

	private String length = "";

	private String plantType = "";

	private String unitType = "";
	private LinearLayout ll_00;
	private LinearLayout ll_01;
	private LinearLayout ll_03;
	private LinearLayout ll_04;
	private LinearLayout ll_05;
	private LinearLayout ll_07;
	private LinearLayout ll_08;
	private LinearLayout ll_09;
	private LinearLayout ll_10;
	private LinearLayout ll_06;
	private TextView tv_firstSeedlingTypeName;
	private String purchaseId = "";
	private String id = "";
	private String remarks;
	private String name = "";
	private Button edit_btn;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_purcha);
		Intent data = getIntent();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		edit_btn = (Button) findViewById(R.id.edit_btn);
		ll_00 = (LinearLayout) findViewById(R.id.ll_00);
		ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		ll_08 = (LinearLayout) findViewById(R.id.ll_08);
		ll_09 = (LinearLayout) findViewById(R.id.ll_09);
		ll_10 = (LinearLayout) findViewById(R.id.ll_10);
		et_name = (EditText) findViewById(R.id.et_name);
		et_count = (EditText) findViewById(R.id.et_count);
		et_dbh = (EditText) findViewById(R.id.et_dbh);
		et_height = (EditText) findViewById(R.id.et_height);
		et_crown = (EditText) findViewById(R.id.et_crown);
		et_diameter = (EditText) findViewById(R.id.et_diameter);
		et_offbarHeight = (EditText) findViewById(R.id.et_offbarHeight);
		et_length = (EditText) findViewById(R.id.et_length);
		et_remark = (EditText) findViewById(R.id.et_remark);
		LinearLayout ll_save = (LinearLayout) findViewById(R.id.ll_save);
		tv_firstSeedlingTypeName = (TextView) findViewById(R.id.tv_firstSeedlingTypeName);
		tv_TypeName = (TextView) findViewById(R.id.tv_TypeName);
		tv_unitType = (TextView) findViewById(R.id.tv_unitType);

		SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
		SegmentedGroup segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
		button31 = (RadioButton) findViewById(R.id.button31);
		button32 = (RadioButton) findViewById(R.id.button32);
		button33 = (RadioButton) findViewById(R.id.button33);
		button21 = (RadioButton) findViewById(R.id.button21);
		button2x = (RadioButton) findViewById(R.id.button2x);
		button22 = (RadioButton) findViewById(R.id.button22);
		if (getIntent().getStringExtra("id") != null
				&& "".equals(getIntent().getStringExtra("id"))) {
			id = getIntent().getStringExtra("id");
			purchaseId = getIntent().getStringExtra("purchaseId");
		}
		if (getIntent().getStringExtra("id") != null
				&& !"".equals(getIntent().getStringExtra("id"))) {
			id = getIntent().getStringExtra("id");
			name = getIntent().getStringExtra("name");
			seedlingParams = getIntent().getStringExtra("seedlingParams");
			purchaseId = getIntent().getStringExtra("purchaseId");
			firstSeedlingTypeId = getIntent().getStringExtra(
					"firstSeedlingTypeId");
			firstSeedlingTypeName = getIntent().getStringExtra("firstTypeName");
			count = getIntent().getStringExtra("count");
			unitType = getIntent().getStringExtra("unitType");
			plantType = getIntent().getStringExtra("plantType");
			diameter = getIntent().getStringExtra("diameter");
			diameterType = getIntent().getStringExtra("diameterType");
			dbh = getIntent().getStringExtra("dbh");
			dbhType = getIntent().getStringExtra("dbhType");
			height = getIntent().getStringExtra("height");
			crown = getIntent().getStringExtra("crown");
			offbarHeight = getIntent().getStringExtra("offbarHeight");
			length = getIntent().getStringExtra("length");
			remarks = getIntent().getStringExtra("remarks");
			edit_btn.setOnClickListener(multipleClickProcess);
		}

		if (getIntent().getSerializableExtra("Purchase") != null) {
			position = getIntent().getIntExtra("position", 0);
			Purchase mPurchase = (Purchase) getIntent().getSerializableExtra(
					"Purchase");
			name = mPurchase.getName();
			seedlingParams = getIntent().getStringExtra("seedlingParams");
			firstSeedlingTypeId = mPurchase.getFirstSeedlingTypeId();
			firstSeedlingTypeName = mPurchase.getFirstSeedlingType();
			count = mPurchase.getCount();
			unitType = mPurchase.getUnitType();
			plantType = mPurchase.getPlantType();
			diameter = mPurchase.getDiameter();
			diameterType = mPurchase.getDbhType();
			dbh = mPurchase.getDbh();
			dbhType = mPurchase.getDbhType();
			height = mPurchase.getHeight();
			crown = mPurchase.getCrown();
			offbarHeight = mPurchase.getOffbarHeight();
			length = mPurchase.getLength();
			remarks = mPurchase.getRemarks();
		}

		tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
		et_name.setText(name);
		et_count.setText(count);
		et_dbh.setText(dbh);
		et_height.setText(height);
		et_crown.setText(crown);
		et_diameter.setText(diameter);
		et_offbarHeight.setText(offbarHeight);
		et_length.setText(length);
		if (remarks != null) {
			et_remark.setText(remarks);
		}
		if ("plant".equals(unitType)) {
			tv_unitType.setText("株");
		} else if ("crowd".equals(unitType)) {
			tv_unitType.setText("丛");
		} else if ("squaremeter".equals(unitType)) {
			tv_unitType.setText("平方米");
		}else if ("dai".equals(unitType)) {
			tv_unitType.setText("袋");
		} else if ("pen".equals(unitType)) {
			tv_unitType.setText("盆");
		} else {
			tv_unitType.setText("");
		}
		if ("planted".equals(plantType)) {
			tv_TypeName.setText("地栽苗");
		} else if ("transplant".equals(plantType)) {
			tv_TypeName.setText("移植苗");
		} else if ("heelin".equals(plantType)) {
			tv_TypeName.setText("假植苗");
		} else if ("container".equals(plantType)) {
			tv_TypeName.setText("容器苗");
		}
		if ("size30".equals(dbhType)) {
			button31.setChecked(true);
		} else if ("size100".equals(dbhType)) {
			button32.setChecked(true);
		} else if ("size130".equals(dbhType)) {
			button33.setChecked(true);
		}

		if ("size0".equals(diameterType)) {
			button21.setChecked(true);
		} else if ("size10".equals(diameterType)) {
			button2x.setChecked(true);
		} else if ("size30".equals(diameterType)) {
			button22.setChecked(true);
		}

		initView(ll_05, ll_06, ll_07, ll_08, ll_09, ll_10);
		btn_back.setOnClickListener(multipleClickProcess);
		ll_00.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_07.setOnClickListener(multipleClickProcess);
		ll_08.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		segmented2.setOnCheckedChangeListener(this);
		segmented3.setOnCheckedChangeListener(this);

		et_count.addTextChangedListener(mTextWatcher);
		et_name.addTextChangedListener(mTextWatcher);
		et_count.addTextChangedListener(mTextWatcher);
		et_dbh.addTextChangedListener(mTextWatcher);
		et_height.addTextChangedListener(mTextWatcher);
		et_crown.addTextChangedListener(mTextWatcher);
		et_diameter.addTextChangedListener(mTextWatcher);
		et_offbarHeight.addTextChangedListener(mTextWatcher);
		et_length.addTextChangedListener(mTextWatcher);

	}

	public void initView(LinearLayout ll_05, LinearLayout ll_06,
			LinearLayout ll_07, LinearLayout ll_08, LinearLayout ll_09,
			LinearLayout ll_10) {

		ll_05.setVisibility(View.GONE);
		ll_07.setVisibility(View.GONE);
		ll_08.setVisibility(View.GONE);
		ll_06.setVisibility(View.GONE);
		ll_09.setVisibility(View.GONE);
		ll_10.setVisibility(View.GONE);
		if (seedlingParams.contains("dbh")) {
			ll_05.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("height")) {
			ll_07.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("crown")) {
			ll_08.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("diameter")) {
			ll_06.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("offbarHeight")) {
			ll_09.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("length")) {
			ll_10.setVisibility(View.VISIBLE);
		}

		dbhType = "size100";
		button31.setChecked(true);
		diameterType = "size0";
		button21.setChecked(true);

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
	private RadioButton button31;
	private RadioButton button32;
	private RadioButton button33;
	private RadioButton button21;
	private RadioButton button2x;
	private RadioButton button22;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.button31:
			dbhType = "size30";
			break;
		case R.id.button32:
			dbhType = "size100";
			break;
		case R.id.button33:
			dbhType = "size130";
			break;
		case R.id.button21:
			diameterType = "size0";
			break;
		case R.id.button2x:
			diameterType = "size10";
			break;
		case R.id.button22:
			diameterType = "size30";
			break;
		default:
			// Nothing to do
		}
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
					finish();
					break;
				case R.id.ll_00:
					Intent toChooseFirstTypeActivity = new Intent(
							AddPurchaseActivity.this,
							ChooseFirstTypeActivity.class);
					startActivityForResult(toChooseFirstTypeActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_01:
					new ActionSheetDialog(AddPurchaseActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("种植类型")
							.addSheetItem("无要求", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "";
											tv_TypeName.setText("无要求");
										}
									})
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
				case R.id.ll_04:

					new ActionSheetDialog(AddPurchaseActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("单位")
							.addSheetItem("株", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "plant";
											tv_unitType.setText("株");
										}
									})
							.addSheetItem("丛", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "crowd";
											tv_unitType.setText("丛");
										}
									})
							.addSheetItem("斤", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "jin";
											tv_unitType.setText("斤");
										}
									})

							.addSheetItem("平方米", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "squaremeter";
											tv_unitType.setText("平方米");
										}
									}).addSheetItem("袋", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "dai";
											tv_unitType.setText("袋");
										}
									}).addSheetItem("盆", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "pen";
											tv_unitType.setText("盆");
										}
									}).show();

					break;
				case R.id.ll_save:
					// Intent intent = new Intent();
					// intent.putExtra("count", et_count.getText().toString());
					// intent.putExtra("diameter",
					// et_diameter.getText().toString());
					// intent.putExtra("diameterType", diameterType);
					// intent.putExtra("dbh", et_dbh.getText().toString());
					// intent.putExtra("dbhType", dbhType);
					// intent.putExtra("height",
					// et_height.getText().toString());
					// intent.putExtra("crown", et_crown.getText().toString());
					// intent.putExtra("offbarHeight",
					// et_offbarHeight.getText().toString());
					// intent.putExtra("length",
					// et_length.getText().toString());
					// intent.putExtra("plantType", plantType);
					// intent.putExtra("unitType", unitType);
					// setResult(3, intent);
					// finish();
					if ("".equals(firstSeedlingTypeId)) {
						Toast.makeText(AddPurchaseActivity.this, "请选择苗木分类", 1)
								.show();
						return;
					}
					if ("".equals(et_count.getText().toString())) {
						Toast.makeText(AddPurchaseActivity.this, "请输入数量", 1)
								.show();
						return;
					}
					if ("".equals(unitType)) {
						Toast.makeText(AddPurchaseActivity.this, "请选择单位", 1)
								.show();
						return;
					}
					if ("".equals(plantType)) {
						Toast.makeText(AddPurchaseActivity.this, "请选择种植类型", 1)
								.show();
						return;
					}
					// if ("".equals(et_diameter.getText().toString())
					// && "".equals(et_dbh.getText().toString())
					// && "".equals(et_height.getText().toString())
					// && "".equals(et_crown.getText().toString())
					// && "".equals(et_offbarHeight.getText().toString())
					// && "".equals(et_length.getText().toString())) {
					// Toast.makeText(AddPurchaseActivity.this, "请输入苗木参数", 1)
					// .show();
					// return;
					// }

					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					if (imm.isActive() && getCurrentFocus() != null
							&& getCurrentFocus().getWindowToken() != null) {
						imm.hideSoftInputFromWindow(AddPurchaseActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
					// 隐藏关闭输入法

					if (getIntent().getStringExtra("id") != null
							&& !"".equals(getIntent().getStringExtra("id"))) {
						// 修改
						purchaseAddItem();
					} else if (getIntent().getStringExtra("id") != null
							&& "".equals(getIntent().getStringExtra("id"))) {
						// 新增
						purchaseAddItem();
					} else if (getIntent().getSerializableExtra("Purchase") != null) {
						// 修改
						Data.purchases.remove(position);
						Purchase purchase = new Purchase(et_name.getText()
								.toString(), firstSeedlingTypeName,
								firstSeedlingTypeId, et_count.getText()
										.toString(), unitType, plantType,
								et_diameter.getText().toString(), diameterType,
								et_dbh.getText().toString(), dbhType, et_height
										.getText().toString(), et_crown
										.getText().toString(), et_offbarHeight
										.getText().toString(), et_length
										.getText().toString(), et_remark
										.getText().toString());
						Data.purchases.add(purchase);
						setResult(1);
						finish();
					} else {
						// 普通发布采购新增，本地
						Purchase purchase = new Purchase(et_name.getText()
								.toString(), firstSeedlingTypeName,
								firstSeedlingTypeId, et_count.getText()
										.toString(), unitType, plantType,
								et_diameter.getText().toString(), diameterType,
								et_dbh.getText().toString(), dbhType, et_height
										.getText().toString(), et_crown
										.getText().toString(), et_offbarHeight
										.getText().toString(), et_length
										.getText().toString(), et_remark
										.getText().toString());
						Data.purchases.add(purchase);
						setResult(1);
						finish();
					}

					break;
				case R.id.edit_btn:
					if (getIntent().getStringExtra("id") != null
							&& !"".equals(getIntent().getStringExtra("id"))) {
						// 修改
						purchaseDeleteItem(getIntent().getStringExtra("id"));
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

		private void purchaseDeleteItem(String id) {
			// TODO Auto-generated method stub
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("id", id);
			finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/deleteItem",
					params, new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								String msg = JsonGetInfo.getJsonString(
										jsonObject, "msg");
								if (!"".equals(msg)) {
									Toast.makeText(AddPurchaseActivity.this,
											msg, Toast.LENGTH_SHORT).show();
								}
								if ("1".equals(code)) {
									setResult(1);
									onBackPressed();
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
							Toast.makeText(AddPurchaseActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});

		}

		public void purchaseAddItem() {
			// TODO Auto-generated method stub

			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("id", id);
			params.put("purchaseId", purchaseId);
			params.put("name", et_name.getText().toString());
			params.put("firstSeedlingTypeId", firstSeedlingTypeId);
			params.put("count", et_count.getText().toString());
			params.put("unitType", unitType);
			params.put("plantType", plantType);
			params.put("diameter", et_diameter.getText().toString());
			params.put("diameterType", diameterType);
			params.put("dbhType", dbhType);
			params.put("dbh", et_dbh.getText().toString());
			params.put("height", et_height.getText().toString());
			params.put("crown", et_crown.getText().toString());
			params.put("offbarHeight", et_offbarHeight.getText().toString());
			params.put("length", et_length.getText().toString());
			params.put("remarks", et_remark.getText().toString());
			finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/saveItem",
					params, new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								String msg = JsonGetInfo.getJsonString(
										jsonObject, "msg");
								if (!"".equals(msg)) {
									Toast.makeText(AddPurchaseActivity.this,
											msg, Toast.LENGTH_SHORT).show();
								}
								if ("1".equals(code)) {
									setResult(1);
									finish();
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
							Toast.makeText(AddPurchaseActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
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
		if (resultCode == 1) {
			firstSeedlingTypeId = data.getStringExtra("firstSeedlingTypeId");
			firstSeedlingTypeName = data
					.getStringExtra("firstSeedlingTypeName");
			seedlingParams = data.getStringExtra("seedlingParams");
			initView(ll_05, ll_06, ll_07, ll_08, ll_09, ll_10);
			tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
