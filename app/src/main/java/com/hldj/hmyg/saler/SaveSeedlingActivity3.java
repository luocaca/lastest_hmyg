package com.hldj.hmyg.saler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.next.tagview.TagCloudView;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedittext.ParamsListActivity;
import com.google.gson.Gson;
import com.hldj.hmyg.ManagerListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin.DayChangeListener;

public class SaveSeedlingActivity3 extends NeedSwipeBackActivity implements
		OnTagClickListener {
	private String[] days = { "30", "90", "180" };

	private String[] customDays = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
			"42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63",
			"64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74",
			"75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85",
			"86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96",
			"97", "98", "99", "100", "101", "102", "103", "104", "105", "106",
			"107", "108", "109", "110", "111", "112", "113", "114", "115",
			"116", "117", "118", "119", "120", "121", "122", "123", "124",
			"125", "126", "127", "128", "129", "130", "131", "132", "133",
			"134", "135", "136", "137", "138", "139", "140", "141", "142",
			"143", "144", "145", "146", "147", "148", "149", "150", "151",
			"152", "153", "154", "155", "156", "157", "158", "159", "160",
			"161", "162", "163", "164", "165", "166", "167", "168", "169",
			"170", "171", "172", "173", "174", "175", "176", "177", "178",
			"179", "180" };

	private View mainView;
	private TextView tv_day;
	private TextView tv_pic;
	private TextView tv_address;
	private String firstSeedlingTypeId = "";
	private String validity = "";
	private String addressId = "";
	private String firstSeedlingTypeName = "";
	private String seedlingParams = "";

	private TextView tv_firstSeedlingTypeName;

	private EditText et_name;

	private EditText et_price;
	private EditText et_FloorPrice;

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
	private String paramsData = "";

	private TextView tv_canshu;

	private TextView tv_qitacanshu;

	private LinearLayout ll_FloorPrice;

	private LinearLayout ll_save;
	private Button save;
	private Gson gson;
	private KProgressHUD hud;
	private String id = "";
	private TextView tv_pics;
	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_seedling);
		List<String> tags = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			tags.add("就是个标签" + i);
		}
		hud = KProgressHUD.create(SaveSeedlingActivity3.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		Data.pics1.clear();
		Data.photoList.clear();
		Data.microBmList.clear();
		Data.paramsDatas.clear();
		gson = new Gson();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		mainView = (View) findViewById(R.id.ll_mainView);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		LinearLayout ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		LinearLayout ll_08 = (LinearLayout) findViewById(R.id.ll_08);
		save = (Button) findViewById(R.id.save);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		ll_FloorPrice = (LinearLayout) findViewById(R.id.ll_FloorPrice);
		tv_day = (TextView) findViewById(R.id.tv_day);
		tv_pics = (TextView) findViewById(R.id.tv_pics);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_canshu = (TextView) findViewById(R.id.tv_canshu);
		tv_qitacanshu = (TextView) findViewById(R.id.tv_qitacanshu);
		tv_firstSeedlingTypeName = (TextView) findViewById(R.id.tv_firstSeedlingTypeName);
		et_name = (EditText) findViewById(R.id.et_name);
		et_price = (EditText) findViewById(R.id.et_price);
		et_FloorPrice = (EditText) findViewById(R.id.et_FloorPrice);
		et_remark = (EditText) findViewById(R.id.et_remark);
		TagCloudView tagCloudView8 = (TagCloudView) findViewById(R.id.tag_cloud_view_8);
		tagCloudView8.setTags(tags);
		tagCloudView8.setOnTagClickListener(this);
		tagCloudView8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getApplicationContext(), "TagView onClick",
						Toast.LENGTH_SHORT).show();
			}
		});
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			// urlPaths = getIntent().getStringArrayListExtra("urlPaths");
			Bundle bundle = getIntent().getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			firstSeedlingTypeId = getIntent().getStringExtra(
					"firstSeedlingTypeId");
			validity = getIntent().getStringExtra("validity");
			addressId = getIntent().getStringExtra("addressId");
			firstSeedlingTypeName = getIntent().getStringExtra(
					"firstSeedlingTypeName");
			seedlingParams = getIntent().getStringExtra("seedlingParams");
			count = getIntent().getStringExtra("count");
			diameter = getIntent().getStringExtra("diameter");
			diameterType = getIntent().getStringExtra("diameterType");
			dbh = getIntent().getStringExtra("dbh");
			dbhType = getIntent().getStringExtra("dbhType");
			height = getIntent().getStringExtra("height");
			crown = getIntent().getStringExtra("crown");
			offbarHeight = getIntent().getStringExtra("offbarHeight");
			length = getIntent().getStringExtra("length");
			plantType = getIntent().getStringExtra("plantType");
			unitType = getIntent().getStringExtra("unitType");
			paramsData = getIntent().getStringExtra("paramsData");
			tv_day.setText(getIntent().getStringExtra("lastDay"));
			et_name.setText(getIntent().getStringExtra("name"));
			et_price.setText(getIntent().getStringExtra("price"));
			et_FloorPrice.setText(getIntent().getStringExtra("floorPrice"));
			et_remark.setText(getIntent().getStringExtra("remarks"));
			tv_pics = (TextView) findViewById(R.id.tv_pics);
			tv_qitacanshu = (TextView) findViewById(R.id.tv_qitacanshu);
			tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
			tv_canshu.setText("已填写");
			tv_address.setText("已选择");

		}
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_07.setOnClickListener(multipleClickProcess);
		ll_08.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);
		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
			ll_FloorPrice.setVisibility(View.VISIBLE);
		}
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
				case R.id.ll_01:
					Intent toChooseFirstTypeActivity = new Intent(
							SaveSeedlingActivity3.this,
							ChooseFirstTypeActivity.class);
					startActivityForResult(toChooseFirstTypeActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_04:
					if ("".equals(firstSeedlingTypeId)) {
						Toast.makeText(SaveSeedlingActivity3.this, "请先选择分类",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Intent toChooseParamsActivity = new Intent(
							SaveSeedlingActivity3.this,
							ChooseParamsActivity.class);
					toChooseParamsActivity.putExtra("count", count);
					toChooseParamsActivity.putExtra("diameter", diameter);
					toChooseParamsActivity.putExtra("diameterType",
							diameterType);
					toChooseParamsActivity.putExtra("dbh", dbh);
					toChooseParamsActivity.putExtra("dbhType", dbhType);
					toChooseParamsActivity.putExtra("height", height);
					toChooseParamsActivity.putExtra("crown", crown);
					toChooseParamsActivity.putExtra("offbarHeight",
							offbarHeight);
					toChooseParamsActivity.putExtra("length", length);
					toChooseParamsActivity.putExtra("plantType", plantType);
					toChooseParamsActivity.putExtra("unitType", unitType);
					toChooseParamsActivity.putExtra("seedlingParams",
							seedlingParams);
					toChooseParamsActivity.putExtra("seedlingTypeId",
							firstSeedlingTypeId);
					startActivityForResult(toChooseParamsActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_05:
					Intent toAdressListActivity = new Intent(
							SaveSeedlingActivity3.this, AdressListActivity.class);
					toAdressListActivity.putExtra("from",
							"SaveSeedlingActivity");
					startActivityForResult(toAdressListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_06:
					CustomDaysPickPopwin daysPopwin = new CustomDaysPickPopwin(
							SaveSeedlingActivity3.this, new DayChangeListener() {

								@Override
								public void onDayChange(int dayType, int pos) {
									if (dayType == 0) {
										// model.setValidity(Short.valueOf(days[pos]));
										tv_day.setText(Short.valueOf(days[pos])
												+ "天");
										validity = days[pos];
									} else {
										// model.setValidity(Short
										// .valueOf(customDays[pos]));
										tv_day.setText(""
												+ Short.valueOf(customDays[pos])
												+ "天");
										validity = customDays[pos];
									}
									// model.setValidityTextView(daysTv);
								}
							}, days, customDays, 0);
					daysPopwin.showAtLocation(mainView, Gravity.BOTTOM
							| Gravity.CENTER, 0, 0);
					break;
				case R.id.ll_07:
					Intent toUpdataImageActivity = new Intent(
							SaveSeedlingActivity3.this,
							UpdataImageActivity.class);
					Bundle bundleObject = new Bundle();
					final PicSerializableMaplist myMap = new PicSerializableMaplist();
					myMap.setMaplist(urlPaths);
					bundleObject.putSerializable("urlPaths", myMap);
					toUpdataImageActivity.putExtras(bundleObject);
					startActivityForResult(toUpdataImageActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

					// Intent intent = new Intent(SaveSeedlingActivity.this,
					// ChoosePicsActivity.class);
					// startActivityForResult(intent, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					// Intent intent = new Intent(SaveSeedlingActivity.this,
					// ChooseMorePicsActivity.class);
					// startActivityForResult(intent, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					break;
				case R.id.ll_08:
					Intent toParamsListActivity = new Intent(
							SaveSeedlingActivity3.this, ParamsListActivity.class);
					toParamsListActivity.putExtra("seedlingTypeId",
							firstSeedlingTypeId);
					startActivityForResult(toParamsListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.save:
					if ("".equals(firstSeedlingTypeId)) {
						Toast.makeText(SaveSeedlingActivity3.this, "请先选择分类",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(et_name.getText().toString())) {
						Toast.makeText(SaveSeedlingActivity3.this, "请先输入苗木名称",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(et_price.getText().toString())) {
						Toast.makeText(SaveSeedlingActivity3.this, "请先输入苗木价格",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (Double.parseDouble(et_price.getText().toString()) <= 0) {
						Toast.makeText(SaveSeedlingActivity3.this, "请输入超过0的价格",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (MyApplication.Userinfo.getBoolean("isDirectAgent",
							false)) {
						if ("".equals(et_FloorPrice.getText().toString())) {
							Toast.makeText(SaveSeedlingActivity3.this, "请先输入底价",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.parseDouble(et_FloorPrice.getText()
								.toString()) <= 0) {
							Toast.makeText(SaveSeedlingActivity3.this,
									"请输入超过0的底价", Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.parseDouble(et_FloorPrice.getText()
								.toString()) > Double.parseDouble(et_price
								.getText().toString())) {
							Toast.makeText(SaveSeedlingActivity3.this,
									"输入底价不能超过苗木价格", Toast.LENGTH_SHORT).show();
							return;
						}
					}

					if ("".equals(validity)) {
						Toast.makeText(SaveSeedlingActivity3.this, "请先选择发布有效期",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(addressId)) {
						Toast.makeText(SaveSeedlingActivity3.this, "请先选择苗源地址",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(plantType)) {
						Toast.makeText(SaveSeedlingActivity3.this, "请选择种植类型",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(unitType)) {
						Toast.makeText(SaveSeedlingActivity3.this, "请选择单位",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(count)) {
						Toast.makeText(SaveSeedlingActivity3.this,
								"请先输入苗木参数下数量", Toast.LENGTH_SHORT).show();
						return;
					}

					if (urlPaths.size() > 0) {

						if (!PicValiteIsUtils.needPicValite(urlPaths)) {
							Toast.makeText(SaveSeedlingActivity3.this,
									"请上传完未上传的图片", Toast.LENGTH_SHORT).show();
							return;
						}
						Data.pics1.clear();
						hud.show();
						for (int i = 0; i < urlPaths.size(); i++) {
							Data.pics1.add(urlPaths.get(i));
						}
						seedlingSave();
					} else {
						Toast.makeText(SaveSeedlingActivity3.this, "请选择图片上传",
								Toast.LENGTH_SHORT).show();

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

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	private void seedlingSave() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		if (!"".equals(id)) {
			params.put("id", id);
		}
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("name", et_name.getText().toString());
		params.put("price", et_price.getText().toString());
		params.put("floorPrice", et_FloorPrice.getText().toString());
		params.put("validity", validity);
		params.put("nurseryId", addressId);
		params.put("count", count);
		params.put("diameterType", diameterType);
		params.put("dbhType", dbhType);
		params.put("dbh", dbh);
		params.put("height", height);
		params.put("crown", crown);
		params.put("diameter", diameter);
		params.put("offbarHeight", offbarHeight);
		params.put("length", length);
		params.put("plantType", plantType);
		params.put("unitType", unitType);
		params.put("imagesData", gson.toJson(Data.pics1));
		params.put("paramsData", paramsData);
		params.put("remarks", et_remark.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/save", params,
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
								Toast.makeText(SaveSeedlingActivity3.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								Data.pics1.clear();
								Data.photoList.clear();
								Data.microBmList.clear();
								Data.paramsDatas.clear();
								setResult(1);
								finish();
								Intent toManagerListActivity = new Intent(
										SaveSeedlingActivity3.this,
										ManagerListActivity.class);
								startActivity(toManagerListActivity);
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);
							} else {

							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
						if (hud != null) {
							hud.dismiss();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(SaveSeedlingActivity3.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
						if (hud != null) {
							hud.dismiss();
						}
					}

				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			firstSeedlingTypeId = data.getStringExtra("firstSeedlingTypeId");
			firstSeedlingTypeName = data
					.getStringExtra("firstSeedlingTypeName");
			seedlingParams = data.getStringExtra("seedlingParams");
			tv_firstSeedlingTypeName.setText(firstSeedlingTypeName);
			Data.paramsDatas.clear();
			tv_canshu.setText("");
			tv_qitacanshu.setText("");
		} else if (resultCode == 2) {
			addressId = data.getStringExtra("addressId");
			tv_address.setText("已选择");
		} else if (resultCode == 3) {
			count = data.getStringExtra("count");
			diameter = data.getStringExtra("diameter");
			diameterType = data.getStringExtra("diameterType");
			dbh = data.getStringExtra("dbh");
			dbhType = data.getStringExtra("dbhType");
			height = data.getStringExtra("height");
			crown = data.getStringExtra("crown");
			offbarHeight = data.getStringExtra("offbarHeight");
			length = data.getStringExtra("length");
			plantType = data.getStringExtra("plantType");
			unitType = data.getStringExtra("unitType");
			paramsData = data.getStringExtra("paramsData");
			tv_canshu.setText("已填写");
		} else if (resultCode == 4) {
			paramsData = data.getStringExtra("paramsData");
			tv_qitacanshu.setText("已填写");
		} else if (resultCode == 5) {
			Bundle bundle = data.getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			tv_pics.setText(PicValiteIsUtils.notiPicValite(urlPaths));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTagClick(int position) {
		// TODO Auto-generated method stub
		if (position == -1) {
			Toast.makeText(getApplicationContext(), "点击末尾文字",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(),
					"点击 position : " + position, Toast.LENGTH_SHORT).show();
		}
	}

}
