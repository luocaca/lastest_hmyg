package com.hldj.hmyg.saler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.GalleryImageActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.xingguo.huang.mabiwang.util.PictureManageUtil;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zzy.flowers.widget.popwin.EditP2;

public class ValidateDetailActivity extends NeedSwipeBackActivity {

	private String id = "";
	private String tag = ""; // buyer broker
	private String status = "";

	private FinalBitmap fb;
	private TextView tv_status;
	private TextView tv_title;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private TextView tv_06;
	private TextView tv_07;
	private ImageView iv_img;
	private ImageView iv_img_result;
	private TextView tv_plant_type;
	private TextView et_plant_type;
	private TextView tv_unit_type;
	private TextView et_unit_type;
	private TextView tv_count;
	private EditText et_count;
	private TextView tv_diameter;
	private EditText et_diameter;
	private TextView tv_dbh;
	private EditText et_dbh;
	private TextView tv_height;
	private EditText et_height;
	private TextView tv_crown;
	private EditText et_crown;
	private String plantType = "";
	private String unitType = "";
	private String diameterType = "";
	private String dbhType = "";

	private TextView copy;
	private TextView sure;
	ArrayList<Pic> pics = new ArrayList<Pic>();
	private Gson gson;
	private LinearLayout ll_caozuo;
	private String real_imageUrl;
	private JSONArray real_imagesJson;
	private JSONArray imagesJson;
	private JSONArray extParamsList;
	private String imageUrl;
	private KProgressHUD hud;
	ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private String _plantType = "";
	private String _unitType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new Gson();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_validate_detail);
		hud = KProgressHUD.create(ValidateDetailActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		Data.pics1.clear();
		Data.photoList.clear();
		Data.microBmList.clear();
		Data.paramsDatas.clear();
		fb = FinalBitmap.create(this);
		fb.configLoadingImage(R.drawable.no_image_show);
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
		}
		if (getIntent().getStringExtra("tag") != null) {
			tag = getIntent().getStringExtra("tag");
		}
		if (getIntent().getStringExtra("status") != null) {
			status = getIntent().getStringExtra("status");
		}
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_06 = (TextView) findViewById(R.id.tv_06);
		tv_07 = (TextView) findViewById(R.id.tv_07);
		tv_remark = (TextView) findViewById(R.id.tv_remark);
		ll_remark = (LinearLayout) findViewById(R.id.ll_remark);
		iv_img = (ImageView) findViewById(R.id.iv_img);
		iv_img_result = (ImageView) findViewById(R.id.iv_img_result);
		tv_plant_type = (TextView) findViewById(R.id.tv_plant_type);
		et_plant_type = (TextView) findViewById(R.id.et_plant_type);
		tv_unit_type = (TextView) findViewById(R.id.tv_unit_type);
		et_unit_type = (TextView) findViewById(R.id.et_unit_type);
		tv_count = (TextView) findViewById(R.id.tv_count);
		et_count = (EditText) findViewById(R.id.et_count);
		tv_diameter = (TextView) findViewById(R.id.tv_diameter);
		et_diameter = (EditText) findViewById(R.id.et_diameter);
		tv_dbh = (TextView) findViewById(R.id.tv_dbh);
		et_dbh = (EditText) findViewById(R.id.et_dbh);
		tv_height = (TextView) findViewById(R.id.tv_height);
		et_height = (EditText) findViewById(R.id.et_height);
		tv_crown = (TextView) findViewById(R.id.tv_crown);
		et_crown = (EditText) findViewById(R.id.et_crown);
		copy = (TextView) findViewById(R.id.copy);
		sure = (TextView) findViewById(R.id.sure);
		ll_caozuo = (LinearLayout) findViewById(R.id.ll_caozuo);

		et_count.addTextChangedListener(mTextWatcher);
		et_diameter.addTextChangedListener(mTextWatcher);
		et_dbh.addTextChangedListener(mTextWatcher);
		et_height.addTextChangedListener(mTextWatcher);
		et_crown.addTextChangedListener(mTextWatcher);
		init();
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		if (("broker".equals(tag) && "validating".equals(status))
				|| ("broker".equals(tag) && "backed".equals(status))) {
			ll_caozuo.setVisibility(View.VISIBLE);
			et_plant_type.setOnClickListener(multipleClickProcess);
			et_unit_type.setOnClickListener(multipleClickProcess);
			iv_img_result.setImageResource(R.drawable.tianjia);
			sure.setOnClickListener(multipleClickProcess);
			copy.setOnClickListener(multipleClickProcess);

		} else {

			et_count.setEnabled(false);
			et_diameter.setEnabled(false);
			et_dbh.setEnabled(false);
			et_height.setEnabled(false);
			et_crown.setEnabled(false);
			et_plant_type.setFocusable(false);
			et_unit_type.setFocusable(false);
			et_count.setFocusable(false);
			et_diameter.setFocusable(false);
			et_dbh.setFocusable(false);
			et_height.setFocusable(false);
			et_crown.setFocusable(false);
			et_plant_type.setFocusableInTouchMode(false);
			et_unit_type.setFocusableInTouchMode(false);
			et_count.setFocusableInTouchMode(false);
			et_diameter.setFocusableInTouchMode(false);
			et_dbh.setFocusableInTouchMode(false);
			et_height.setFocusableInTouchMode(false);
			et_crown.setFocusableInTouchMode(false);
		}
		iv_img.setOnClickListener(multipleClickProcess);
		iv_img_result.setOnClickListener(multipleClickProcess);

	}

	// 初始化数据
	private void init() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("itemId", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/agent/validateApply/itemDetail", params,
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
								// Toast.makeText(OrderDetailActivity.this,
								// msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								JSONObject validateItem = JsonGetInfo
										.getJSONObject(data, "validateItem");
								JSONObject sellerJson = JsonGetInfo
										.getJSONObject(validateItem,
												"sellerJson");
								JSONObject customer = JsonGetInfo
										.getJSONObject(data, "customer");
								String displayName = JsonGetInfo.getJsonString(
										customer, "adminDisplayName");
								String phone = JsonGetInfo.getJsonString(
										customer, "phone");
								tv_01.setText("客服信息");
								tv_02.setText("客服:" + displayName);
								tv_03.setText("电话:" + phone);
								String statusName = JsonGetInfo.getJsonString(
										validateItem, "statusName");
								String acceptStatusName = JsonGetInfo
										.getJsonString(validateItem,
												"acceptStatusName");
								String name = JsonGetInfo.getJsonString(
										validateItem, "name");
								String unitTypeName = JsonGetInfo
										.getJsonString(validateItem,
												"unitTypeName");
								_unitType = JsonGetInfo.getJsonString(
										validateItem, "unitType");
								double seedlingPrice = JsonGetInfo
										.getJsonDouble(validateItem,
												"seedlingPrice");
								String cityName = JsonGetInfo.getJsonString(
										validateItem, "cityName");

								if ("broker".equals(tag)) {
									tv_status.setText(acceptStatusName);
								} else {
									tv_status.setText(statusName);
								}
								tv_04.setText("商品信息");
								tv_05.setText("品名：" + name);
								tv_06.setText("单价：" + seedlingPrice + "/"
										+ unitTypeName);
								tv_07.setText("苗源地址：" + cityName);

								int dbh = JsonGetInfo.getJsonInt(validateItem,
										"dbh");
								int diameter = JsonGetInfo.getJsonInt(
										validateItem, "diameter");
								int height = JsonGetInfo.getJsonInt(
										validateItem, "height");
								int crown = JsonGetInfo.getJsonInt(
										validateItem, "crown");
								imageUrl = JsonGetInfo.getJsonString(
										validateItem, "imageUrl");
								String plantTypeName = JsonGetInfo
										.getJsonString(validateItem,
												"plantTypeName");
								_plantType = JsonGetInfo.getJsonString(
										validateItem, "plantType");
								diameterType = JsonGetInfo.getJsonString(
										validateItem, "diameterType");
								dbhType = JsonGetInfo.getJsonString(
										validateItem, "dbhType");

								int applyCount = JsonGetInfo.getJsonInt(
										validateItem, "applyCount");
								imagesJson = JsonGetInfo.getJsonArray(
										validateItem, "imagesJson");
								extParamsList = JsonGetInfo.getJsonArray(
										validateItem, "extParamsList");
								//拓展参数
								

								fb.display(iv_img, imageUrl);
								tv_plant_type.setText(plantTypeName);
								tv_unit_type.setText(unitTypeName);
								if (applyCount > 0) {
									tv_count.setText(applyCount + "");
								}
								if (diameter > 0) {
									tv_diameter.setText(diameter + "");
								}
								if (dbh > 0) {
									tv_dbh.setText(dbh + "");
								}
								if (height > 0) {
									tv_height.setText(height + "");
								}
								if (crown > 0) {
									tv_crown.setText(crown + "");
								}

								if (!data.isNull("validateResult")) {
									JSONObject validateResult = JsonGetInfo
											.getJSONObject(data,
													"validateResult");
									int real_dbh = JsonGetInfo.getJsonInt(
											validateResult, "dbh");
									int real_diameter = JsonGetInfo.getJsonInt(
											validateResult, "diameter");
									int real_height = JsonGetInfo.getJsonInt(
											validateResult, "height");
									int real_crown = JsonGetInfo.getJsonInt(
											validateResult, "crown");
									real_imageUrl = JsonGetInfo.getJsonString(
											validateResult, "imageUrl");
									String real_plantTypeName = JsonGetInfo
											.getJsonString(validateResult,
													"plantTypeName");
									String real_unitTypeName = JsonGetInfo
											.getJsonString(validateResult,
													"unitTypeName");
									String largeImageUrl = JsonGetInfo
											.getJsonString(validateResult,
													"imageUrl");
									int realCount = JsonGetInfo.getJsonInt(
											validateResult, "realCount");
									real_imagesJson = JsonGetInfo.getJsonArray(
											validateResult, "imagesJson");
									JSONObject auditLogJson = JsonGetInfo
											.getJSONObject(validateResult,
													"auditLogJson");
									String remarks = JsonGetInfo.getJsonString(
											auditLogJson, "remarks");
									String status = JsonGetInfo.getJsonString(
											auditLogJson, "status");
									if (!"".equals(remarks)
											&& "backed".equals(status)) {
										tv_remark.setText("退回理由：" + remarks);
										ll_remark.setVisibility(View.VISIBLE);
									}else {
										ll_remark.setVisibility(View.GONE);
									}
									if (real_imagesJson.length() > 0) {
										fb.display(iv_img_result, JsonGetInfo
												.getJsonString(real_imagesJson
														.getJSONObject(0),
														"ossUrl"));

										for (int i = 0; i < real_imagesJson
												.length(); i++) {
											new Pic(
													JsonGetInfo.getJsonString(
															real_imagesJson
																	.getJSONObject(i),
															"id"),
													false,
													JsonGetInfo.getJsonString(
															real_imagesJson
																	.getJSONObject(i),
															"ossUrl"), i);
										}
									}
									et_plant_type.setText(real_plantTypeName);
									et_unit_type.setText(real_unitTypeName);
									if (realCount > 0) {
										et_count.setText(realCount + "");
									}
									if (realCount > applyCount) {
										et_count.setTextColor(getResources()
												.getColor(R.color.main_color));
									} else if (realCount == applyCount) {
										et_count.setTextColor(getResources()
												.getColor(R.color.black));
									} else if (realCount < applyCount) {
										et_count.setTextColor(getResources()
												.getColor(R.color.red));
									}
									if (real_diameter > 0) {
										et_diameter.setText(real_diameter + "");
									}
									if (real_diameter > diameter) {
										et_diameter.setTextColor(getResources()
												.getColor(R.color.main_color));
									} else if (real_diameter == diameter) {
										et_diameter.setTextColor(getResources()
												.getColor(R.color.black));
									} else if (real_diameter < diameter) {
										et_diameter.setTextColor(getResources()
												.getColor(R.color.red));
									}
									if (real_dbh > 0) {
										et_dbh.setText(real_dbh + "");
									}
									if (real_dbh > dbh) {
										et_dbh.setTextColor(getResources()
												.getColor(R.color.main_color));
									} else if (real_dbh == dbh) {
										et_dbh.setTextColor(getResources()
												.getColor(R.color.black));
									} else if (real_dbh < dbh) {
										et_dbh.setTextColor(getResources()
												.getColor(R.color.red));
									}
									if (real_height > 0) {
										et_height.setText(real_height + "");
									}
									if (real_height > height) {
										et_height.setTextColor(getResources()
												.getColor(R.color.main_color));
									} else if (real_height == height) {
										et_height.setTextColor(getResources()
												.getColor(R.color.black));
									} else if (real_height < height) {
										et_height.setTextColor(getResources()
												.getColor(R.color.red));
									}
									if (real_crown > 0) {
										et_crown.setText(real_crown + "");
									}
									if (real_crown > crown) {
										et_crown.setTextColor(getResources()
												.getColor(R.color.main_color));
									} else if (real_crown == crown) {
										et_crown.setTextColor(getResources()
												.getColor(R.color.black));
									} else if (real_crown < crown) {
										et_crown.setTextColor(getResources()
												.getColor(R.color.red));
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
						Toast.makeText(ValidateDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

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
	private TextView tv_remark;
	private LinearLayout ll_remark;

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
				case R.id.copy:
					et_plant_type.setText(tv_plant_type.getText().toString());
					plantType = _plantType;
					// if ("地栽苗".equals(et_plant_type.getText().toString())) {
					// plantType = "planted";
					// } else if
					// ("移植苗".equals(et_plant_type.getText().toString())) {
					// plantType = "transplant";
					// } else if
					// ("假植苗".equals(et_plant_type.getText().toString())) {
					// plantType = "heelin";
					// } else if
					// ("容器苗".equals(et_plant_type.getText().toString())) {
					// plantType = "container";
					// }
					et_unit_type.setText(tv_unit_type.getText().toString());
					unitType = _unitType;
					// if ("株".equals(et_unit_type.getText().toString())) {
					// unitType = "plant";
					// } else if ("丛".equals(et_unit_type.getText().toString()))
					// {
					// unitType = "crowd";
					// } else if
					// ("平方米".equals(et_unit_type.getText().toString())) {
					// unitType = "squaremeter";
					// }
					et_count.setText(tv_count.getText().toString());
					et_diameter.setText(tv_diameter.getText().toString());
					et_dbh.setText(tv_dbh.getText().toString());
					et_height.setText(tv_height.getText().toString());
					et_crown.setText(tv_crown.getText().toString());
					break;
				case R.id.sure:
					if(tv_count.getText().toString().length()>0 && et_count.getText().toString().length()==0){
						Toast.makeText(ValidateDetailActivity.this, "请填写数量",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if(tv_diameter.getText().toString().length()>0 && et_diameter.getText().toString().length()==0){
						Toast.makeText(ValidateDetailActivity.this, "请填写地径",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if(tv_dbh.getText().toString().length()>0 && et_dbh.getText().toString().length()==0){
						Toast.makeText(ValidateDetailActivity.this, "请填写胸径",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if(tv_height.getText().toString().length()>0 && et_height.getText().toString().length()==0){
						Toast.makeText(ValidateDetailActivity.this, "请填写高度",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if(tv_crown.getText().toString().length()>0 && et_crown.getText().toString().length()==0){
						Toast.makeText(ValidateDetailActivity.this, "请填写冠幅",
								Toast.LENGTH_SHORT).show();
						return;
					}
					//
					if ("".equals(plantType)) {
						Toast.makeText(ValidateDetailActivity.this, "请选择种植类型",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(unitType)) {
						Toast.makeText(ValidateDetailActivity.this, "请选择苗木单位",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (urlPaths.size() > 0) {
						
						if (!PicValiteIsUtils.needPicValite(urlPaths)) {
							Toast.makeText(ValidateDetailActivity.this, PicValiteIsUtils.notiPicValite(urlPaths)+"请上传完未上传的图片",
									Toast.LENGTH_SHORT).show();
							return;
						}
						
						Data.pics1.clear();
						hud.show();
						for (int i = 0; i < urlPaths.size(); i++) {
							Data.pics1.add(urlPaths.get(i));
						}
						seedlingSave();
					} else {
						Toast.makeText(ValidateDetailActivity.this, "请选择图片",
								Toast.LENGTH_SHORT).show();
					}

					break;

				case R.id.iv_img:
					if (imagesJson != null && imagesJson.length() > 0) {

						ArrayList<Pic> ossUrls = new ArrayList<Pic>();
						String[] urls = new String[imagesJson.length()];
						// TODO Auto-generated method stub
						for (int i = 0; i < imagesJson.length(); i++) {
							try {
								urls[i] = JsonGetInfo.getJsonString(
										imagesJson.getJSONObject(i), "ossUrl");
								ossUrls.add(new Pic(JsonGetInfo.getJsonString(
										imagesJson.getJSONObject(i), "id"),
										false, JsonGetInfo.getJsonString(
												imagesJson.getJSONObject(i),
												"ossUrl"), i));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						GalleryImageActivity.startGalleryImageActivity(
								ValidateDetailActivity.this, 0, ossUrls);
						// Intent intent = new
						// Intent(ValidateDetailActivity.this,
						// ImagePagerActivity.class);
						// intent.putExtra("image_urls", urls);
						// intent.putExtra("image_index", 0); // 从第一张默认
						// intent.putExtra("name", "");
						// startActivity(intent);
						// overridePendingTransition(R.anim.push_left_in,
						// R.anim.push_left_out);
					}

					break;
				case R.id.iv_img_result:
					// Intent intent = new Intent(ValidateDetailActivity.this,
					// ChoosePicsActivity.class);
					// startActivityForResult(intent, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);

					if (("broker".equals(tag) && "validating".equals(status))
							|| ("broker".equals(tag) && "backed".equals(status))) {
						Intent toUpdataImageActivity = new Intent(
								ValidateDetailActivity.this,
								UpdataImageActivity.class);
						Bundle bundleObject = new Bundle();
						final PicSerializableMaplist myMap = new PicSerializableMaplist();
						myMap.setMaplist(urlPaths);
						bundleObject.putSerializable("urlPaths", myMap);
						toUpdataImageActivity.putExtras(bundleObject);
						startActivityForResult(toUpdataImageActivity, 1);
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);
					} else {
						if (real_imagesJson != null
								&& real_imagesJson.length() > 0) {
							String[] urls = new String[real_imagesJson.length()];
							ArrayList<Pic> real_imagesJsons = new ArrayList<Pic>();
							// TODO Auto-generated method stub
							for (int i = 0; i < real_imagesJson.length(); i++) {
								try {
									urls[i] = JsonGetInfo.getJsonString(
											real_imagesJson.getJSONObject(i),
											"ossUrl");
									real_imagesJsons.add(new Pic(JsonGetInfo
											.getJsonString(real_imagesJson
													.getJSONObject(i), "id"),
											false, JsonGetInfo.getJsonString(
													real_imagesJson
															.getJSONObject(i),
													"ossUrl"), i));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							GalleryImageActivity.startGalleryImageActivity(
									ValidateDetailActivity.this, 0,
									real_imagesJsons);
							// Intent intent = new Intent(
							// ValidateDetailActivity.this,
							// ImagePagerActivity.class);
							// intent.putExtra("image_urls", urls);
							// intent.putExtra("image_index", 0); // 从第一张默认
							// intent.putExtra("name", "");
							// startActivity(intent);
							// overridePendingTransition(R.anim.push_left_in,
							// R.anim.push_left_out);
						}
					}

					break;
				case R.id.et_plant_type:
					new ActionSheetDialog(ValidateDetailActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("种植类型")
							.addSheetItem("地栽苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "planted";
											et_plant_type.setText("地栽苗");
										}
									})
							.addSheetItem("移植苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "transplant";
											et_plant_type.setText("移植苗");
										}
									})
							.addSheetItem("假植苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "heelin";
											et_plant_type.setText("假植苗");

										}
									})

							.addSheetItem("容器苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "container";
											et_plant_type.setText("容器苗");
										}
									}).show();

					break;
				case R.id.et_unit_type:
					new ActionSheetDialog(ValidateDetailActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("单位")
							.addSheetItem("株", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "plant";
											et_unit_type.setText("株");
										}
									})
							.addSheetItem("丛", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "crowd";
											et_unit_type.setText("丛");
										}
									})
							.addSheetItem("斤", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "jin";
											et_unit_type.setText("斤");
										}
									})

							.addSheetItem("平方米", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "squaremeter";
											et_unit_type.setText("平方米");
										}
									}).addSheetItem("袋", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "dai";
											et_unit_type.setText("袋");
										}
									}).addSheetItem("盆", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "pen";
											et_unit_type.setText("盆");
										}
									}).show();
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 5) {
			Bundle bundle = data.getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			if (urlPaths.size() > 0) {
				if (urlPaths.get(0).getUrl().startsWith("http")) {
					fb.display(iv_img_result, urlPaths.get(0).getUrl());
				} else {
					iv_img_result.setImageDrawable(new BitmapDrawable(
							PictureManageUtil.getCompressBm(urlPaths.get(0)
									.getUrl())));
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void seedlingSave() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("plantType", plantType);
		params.put("unitType", unitType);
		params.put("diameterType", diameterType);
		params.put("dbhType", dbhType);
		params.put("imagesData", gson.toJson(Data.pics1));
		params.put("paramsData", "");
		params.put("dbh", et_dbh.getText().toString());
		params.put("diameter", et_diameter.getText().toString());
		params.put("height", et_height.getText().toString());
		params.put("crown", et_crown.getText().toString());
		params.put("realCount", et_count.getText().toString());
		params.put("validateItemId", id);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/agent/validateApply/saveValidateResult", params,
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
								Toast.makeText(ValidateDetailActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								Data.paramsDatas.clear();
								setResult(1);
								finish();
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
						Toast.makeText(ValidateDetailActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

}
