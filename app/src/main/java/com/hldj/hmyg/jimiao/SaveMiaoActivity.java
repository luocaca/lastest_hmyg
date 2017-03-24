package com.hldj.hmyg.jimiao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.next.tagview.TagCloudView.OnTagClickListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedittext.paramsData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.PublishFlowerInfoPhotoAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicSerializableMaplist;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hldj.hmyg.buy.bean.StorageSave;
import com.hldj.hmyg.saler.AdressListActivity;
import com.hldj.hmyg.saler.FlowerInfoPhotoChoosePopwin3;
import com.hldj.hmyg.saler.UpdataImageActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.white.utils.FileUtil;
import com.white.utils.GifImgHelperUtil;
import com.white.utils.ImageTools;
import com.white.utils.StringUtil;
import com.white.utils.SystemSetting;
import com.white.utils.ZzyUtil;
import com.yangfuhai.asimplecachedemo.lib.ACache;
import com.yunpay.app.KeyBordStateListener;
import com.yunpay.app.KeyboardLayout3;
import com.zzy.common.widget.MeasureGridView;
import com.zzy.flowers.activity.photoalbum.EditGalleryImageActivity;
import com.zzy.flowers.activity.photoalbum.PhotoActivity;
import com.zzy.flowers.activity.photoalbum.PhotoAlbumActivity;

public class SaveMiaoActivity extends NeedSwipeBackActivity implements
		OnTagClickListener, KeyBordStateListener {

	private View mainView;
	private TextView tv_pic;
	private TextView tv_address;
	private String firstSeedlingTypeId = "";
	private String addressId = "";
	private String fullAddress = "";
	private String detailAddress = "";
	private String contactName = "";
	private String contactPhone = "";
	private String companyName = "";
	private boolean isDefault;
	private String firstSeedlingTypeName = "";
	private String seedlingParams = "";

	private EditText et_name;

	private EditText et_price;
	private EditText et_FloorPrice;

	private String count = "";

	private String diameter = "";

	private String diameterType = "";

	private String dbh = "";

	private String dbhType = "";

	private String height = "";

	private String crown = "";
	
	private String maxHeight = "";

	private String maxCrown = "";

	private String offbarHeight = "";

	private String length = "";

	private String plantType = "";

	private String paramsData = "";

	private String minSpec = "";
	private String maxSpec = "";

	private LinearLayout ll_save;
	private Button save;
	private Gson gson;
	private KProgressHUD hud;
	private String id = "";
	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();

	/**
	 */
	public static final String INTENT_START_TYPE_KEY = "intent_start_type";
	public static final String INTENT_DIR_ID_KEY = "intent_dir_id";
	public static final String INTENT_PHOTO_TYPE_KEY = "intent_photo_type";
	public static final String INTENT_HAD_CHOOSE_PHOTO_KEY = "intent_had_choose_photo";
	public static final int INTENT_NOT_NEED_FOR_RESULT = -1;
	/** 图片压缩至960像素以内 */
	public static final int COMPRESS_IMAGE_HEIGHT_PX = 960;
	public static final int COMPRESS_IMAGE_WIDTH_PX = 960;
	/** 显示图片压缩至160像素以内 */
	public static final int GRID_COMPRESS_IMAGE_HEIGHT_PX = 160;
	public static final int GRID_COMPRESS_IMAGE_WIDTH_PX = 160;

	public static final int TO_TAKE_PIC = 1;
	public static final int TO_CHOOSE_PIC = 2;
	/** 图片太大 */
	public static final int PIC_IS_TOO_BIG = 3;
	/** 加载图片失败 */
	public static final int LOAD_PIC_FAILURE = 4;
	/** 添加图片 */
	public static final int ADD_NEW_PIC = 5;
	/** 照片列表 */
	private MeasureGridView photoGv;
	/** 照片适配器 */
	private PublishFlowerInfoPhotoAdapter adapter;
	/** 新增图片的宽 */
	public int newWidth;
	/** 新增图片的高 */
	public int newHeight;

	private String flowerInfoPhotoPath = "";

	private RefreshHandler handler;
	private FlowerInfoPhotoChoosePopwin3 popwin;
	public static SaveMiaoActivity instance;
	private KProgressHUD hud_numHud;
	FinalHttp finalHttp = new FinalHttp();
	public int a = 0;
	private int BACK_TYPE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_miao);
		mCache = ACache.get(this);
		hud_numHud = KProgressHUD.create(SaveMiaoActivity.this)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true);
		instance = this;
		SystemSetting.getInstance(SaveMiaoActivity.this).choosePhotoDirId = "";

		hud = KProgressHUD.create(SaveMiaoActivity.this)
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
		photoGv = (MeasureGridView) findViewById(R.id.publish_flower_info_gv);
		adapter = new PublishFlowerInfoPhotoAdapter(SaveMiaoActivity.this,
				urlPaths);
		photoGv.setAdapter(adapter);
		PhotoGvOnItemClickListener itemClickListener = new PhotoGvOnItemClickListener();
		photoGv.setOnItemClickListener(itemClickListener);
		handler = new RefreshHandler(this.getMainLooper());
		mainView = (View) findViewById(R.id.ll_mainView);
		com.yunpay.app.KeyboardLayout3 resizeLayout = (com.yunpay.app.KeyboardLayout3) findViewById(R.id.ll_mainView);
		// 获得要控制隐藏和显示的区域
		resizeLayout.setKeyBordStateListener(this);// 设置回调方法

		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		list_item_adress = (LinearLayout) findViewById(R.id.list_item_adress);
		iv_edit = (ImageView) findViewById(R.id.iv_edit);
		tv_contanct_name = (TextView) findViewById(R.id.tv_name);
		tv_address_name = (TextView) findViewById(R.id.tv_address_name);
		tv_is_defoloat = (TextView) findViewById(R.id.tv_is_defoloat);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		iv_ready_save = (TextView) findViewById(R.id.iv_ready_save);
		save = (Button) findViewById(R.id.save);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		tv_address = (TextView) findViewById(R.id.tv_address);
		et_name = (EditText) findViewById(R.id.et_name);
		et_price = (EditText) findViewById(R.id.et_price);
		et_FloorPrice = (EditText) findViewById(R.id.et_FloorPrice);
		et_count = (EditText) findViewById(R.id.et_count);
		et_height = (EditText) findViewById(R.id.et_height);
		et_crown = (EditText) findViewById(R.id.et_crown);
		et_maxHeight = (EditText) findViewById(R.id.et_maxHeight);
		et_maxCrown = (EditText) findViewById(R.id.et_maxCrown);
		et_minSpec = (EditText) findViewById(R.id.et_minSpec);
		et_maxSpec = (EditText) findViewById(R.id.et_maxSpec);
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			Bundle bundle = getIntent().getExtras();
			urlPaths = ((PicSerializableMaplist) bundle.get("urlPaths"))
					.getMaplist();
			adapter.notify(urlPaths);

			GetServerUrl.addHeaders(finalHttp, true);
			AjaxParams params1 = new AjaxParams();
			params1.put("id", id);
			finalHttp.post(GetServerUrl.getUrl() + "admin/seedlingNote/detail",
					params1, new AjaxCallBack<Object>() {

						@Override
						public void onStart() {
							super.onStart();
						}

						@Override
						public void onSuccess(Object t) {
							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								int code = jsonObject.getInt("code");
								if (code == 1) {
									JSONObject seedling = JsonGetInfo.getJSONObject(
											JsonGetInfo.getJSONObject(
													jsonObject, "data"),
											"seedling");
									JSONArray imagesJson = JsonGetInfo
											.getJsonArray(seedling,
													"imagesJson");
									for (int i = 0; i < imagesJson.length(); i++) {
										JSONObject image = imagesJson
												.getJSONObject(i);
										urlPaths.add(new Pic(JsonGetInfo
												.getJsonString(image, "id"),
												false, JsonGetInfo
														.getJsonString(image,
																"url"), i));
									}
									adapter.notify(urlPaths);

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}

							super.onSuccess(t);
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method
							// stub
							super.onFailure(t, errorNo, strMsg);

						}
					});

			addressId = getIntent().getStringExtra("addressId");
			count = getIntent().getStringExtra("count");
			height = getIntent().getStringExtra("height");
			crown = getIntent().getStringExtra("crown");
			maxHeight = getIntent().getStringExtra("maxHeight");
			maxCrown = getIntent().getStringExtra("maxCrown");
			contactName = getIntent().getStringExtra("contactName");
			contactPhone = getIntent().getStringExtra("contactPhone");
			fullAddress = getIntent().getStringExtra("address");
			isDefault = getIntent().getBooleanExtra("isDefault", false);
			minSpec = getIntent().getStringExtra("minSpec");
			maxSpec = getIntent().getStringExtra("maxSpec");
			et_name.setText(getIntent().getStringExtra("name"));
			if (!"0".equals(getIntent().getStringExtra("price"))
					&& !"0.0".equals(getIntent().getStringExtra("price"))) {
				et_price.setText(getIntent().getStringExtra("price"));
			}
			if (!"0".equals(height) && !"0.0".equals(height)) {
				et_height.setText(height);
			}
			if (!"0".equals(maxHeight) && !"0.0".equals(maxHeight)) {
				et_maxHeight.setText(maxHeight);
			}
			if (!"0".equals(crown) && !"0.0".equals(crown)) {
				et_crown.setText(crown);
			}
			if (!"0".equals(maxCrown) && !"0.0".equals(maxCrown)) {
				et_maxCrown.setText(maxCrown);
			}
			if (!"0".equals(minSpec) && !"0.0".equals(minSpec)) {
				et_minSpec.setText(minSpec);
			}
			if (!"0".equals(maxSpec) && !"0.0".equals(maxSpec)) {
				et_maxSpec.setText(maxSpec);
			}
			if (!"0".equals(count) && !"0.0".equals(count)) {
				et_count.setText(count);
			}

			if (!"".equals(addressId)) {
				list_item_adress.setVisibility(View.VISIBLE);
				ll_05.setVisibility(View.GONE);
				tv_contanct_name.setText(contactName + "\u0020" + contactPhone);
				tv_address_name.setText(fullAddress);
				if (isDefault) {
					tv_is_defoloat.setVisibility(View.VISIBLE);
				} else {
					tv_is_defoloat.setVisibility(View.GONE);
				}
			}

		} else {
			// 从缓存中取
			if (mCache.getAsString("savemiao") != null
					&& !"".equals(mCache.getAsString("savemiao"))) {
				StorageSave fromJson = gson.fromJson(
						mCache.getAsString("savemiao"),
						com.hldj.hmyg.buy.bean.StorageSave.class);

				id = fromJson.getId();
				urlPaths = fromJson.getUrlPaths();
				adapter.notify(urlPaths);
				firstSeedlingTypeId = fromJson.getFirstSeedlingTypeId();
				addressId = fromJson.getNurseryId();
				contactName = fromJson.getContactName();
				contactPhone = fromJson.getContactPhone();
				fullAddress = fromJson.getAddress();
				isDefault = fromJson.isDefault();
				seedlingParams = fromJson.getSeedlingParams();
				count = fromJson.getCount();
				diameter = fromJson.getDiameter();
				diameterType = fromJson.getDiameterType();
				dbh = fromJson.getDbh();
				dbhType = fromJson.getDbhType();
				height = fromJson.getHeight();
				crown = fromJson.getCrown();
				maxHeight= fromJson.getMaxHeight();
				maxCrown= fromJson.getMaxCrown();
				offbarHeight = fromJson.getOffbarHeight();
				length = fromJson.getLength();
				minSpec = fromJson.getMinSpec();
				maxSpec = fromJson.getMaxSpec();
				plantType = fromJson.getPlantType();
				paramsData = fromJson.getParamsData();
				ArrayList<paramsData> ps = gson.fromJson(paramsData,
						new TypeToken<ArrayList<paramsData>>() {
						}.getType());
				if (ps != null && ps.size() > 0) {
					Data.paramsDatas = ps;
				}
				et_name.setText(fromJson.getName());
				et_price.setText(fromJson.getPrice());
				et_FloorPrice.setText(fromJson.getFloorPrice());
				et_count.setText(count);
				et_crown.setText(crown);
				et_height.setText(height);
				et_maxHeight.setText(maxHeight);
				et_maxCrown.setText(maxCrown);
				et_minSpec.setText(minSpec);
				et_maxSpec.setText(maxSpec);
				if (!"".equals(addressId)) {
					list_item_adress.setVisibility(View.VISIBLE);
					ll_05.setVisibility(View.GONE);
					tv_contanct_name.setText(contactName + "\u0020"
							+ contactPhone);
					tv_address_name.setText(fullAddress);
					if (isDefault) {
						tv_is_defoloat.setVisibility(View.VISIBLE);
					} else {
						tv_is_defoloat.setVisibility(View.GONE);
					}

				}

			}

		}

		btn_back.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		list_item_adress.setOnClickListener(multipleClickProcess);
		id_tv_edit_all.setOnClickListener(multipleClickProcess);
		iv_ready_save.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);
	}

	@Override
	public void stateChange(int state) {
		// TODO Auto-generated method stub
		switch (state) {
		case KeyboardLayout3.KEYBORAD_HIDE:
			ll_save.setVisibility(View.VISIBLE);
			break;
		case KeyboardLayout3.KEYBORAD_SHOW:
			ll_save.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mCache.remove("savemiao");
		StorageSave mStorageSave = new StorageSave();
		mStorageSave.setId(id);
		mStorageSave.setFirstSeedlingTypeId(firstSeedlingTypeId);
		mStorageSave.setSeedlingParams(seedlingParams);
		mStorageSave.setName(et_name.getText().toString());
		mStorageSave.setPrice(et_price.getText().toString());
		mStorageSave.setFloorPrice(et_FloorPrice.getText().toString());
		mStorageSave.setNurseryId(addressId);
		mStorageSave.setContactName(contactName);
		mStorageSave.setContactPhone(contactPhone);
		mStorageSave.setDefault(isDefault);
		mStorageSave.setCount(et_count.getText().toString());
		mStorageSave.setMinSpec(et_minSpec.getText().toString());
		mStorageSave.setMaxSpec(et_maxSpec.getText().toString());
		mStorageSave.setDiameterType(diameterType);
		mStorageSave.setDbhType(dbhType);
		mStorageSave.setDbh(dbh);
		mStorageSave.setHeight(et_height.getText().toString());
		mStorageSave.setCrown(et_crown.getText().toString());
		mStorageSave.setMaxHeight(et_maxHeight.getText().toString());
		mStorageSave.setMaxCrown(et_maxCrown.getText().toString());
		mStorageSave.setDiameter(diameter);
		mStorageSave.setOffbarHeight(offbarHeight);
		mStorageSave.setLength(length);
		mStorageSave.setPlantType(plantType);
		mStorageSave.setUrlPaths(urlPaths);
		mStorageSave.setParamsData(paramsData);
		// 额外数据
		mStorageSave.setAddress(fullAddress);
		// 保存缓存
		mCache.put("savemiao", gson.toJson(mStorageSave));
		finish();
		super.onBackPressed();
	}

	@Override
	public void finish() {
		super.finish();
		instance = null;
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

	private LinearLayout list_item_adress;

	private ImageView iv_edit;

	private TextView tv_contanct_name;

	private TextView tv_address_name;

	private TextView tv_is_defoloat;

	private LinearLayout ll_05;

	private EditText et_count;

	private ACache mCache;

	private TextView id_tv_edit_all;
	private EditText et_crown;
	private EditText et_height;
	private EditText et_minSpec;
	private EditText et_maxSpec;
	private TextView iv_ready_save;
	private EditText et_maxHeight;
	private EditText et_maxCrown;

	public class MultipleClickProcess implements OnClickListener {
		private boolean flag = true;

		private synchronized void setFlag() {
			flag = false;
		}

		public void onClick(View view) {
			if (flag) {
				if (view.getId() == R.id.btn_back) {
					onBackPressed();
				} else if (view.getId() == R.id.list_item_adress) {
					Intent toAdressListActivity1 = new Intent(
							SaveMiaoActivity.this, AdressListActivity.class);
					toAdressListActivity1.putExtra("addressId", addressId);
					toAdressListActivity1.putExtra("from",
							"SaveSeedlingActivity");
					startActivityForResult(toAdressListActivity1, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.ll_05) {
					Intent toAdressListActivity = new Intent(
							SaveMiaoActivity.this, AdressListActivity.class);
					toAdressListActivity.putExtra("addressId", addressId);
					toAdressListActivity.putExtra("from",
							"SaveSeedlingActivity");
					startActivityForResult(toAdressListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				} else if (view.getId() == R.id.save
						|| view.getId() == R.id.iv_ready_save) {
					if (view.getId() == R.id.save) {
						BACK_TYPE = 0;
					} else if (view.getId() == R.id.iv_ready_save) {
						BACK_TYPE = 1;
					}
					if ("".equals(et_name.getText().toString())) {
						Toast.makeText(SaveMiaoActivity.this, "请先输入苗木名称",
								Toast.LENGTH_SHORT).show();
						return;
					}
//					if ("".equals(et_price.getText().toString())) {
//						Toast.makeText(SaveMiaoActivity.this, "请先输入苗木价格",
//								Toast.LENGTH_SHORT).show();
//						return;
//					}

//					if (Double.parseDouble(et_price.getText().toString()) <= 0) {
//						Toast.makeText(SaveMiaoActivity.this, "请输入超过0的价格",
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
					if ("".equals(addressId)) {
						Toast.makeText(SaveMiaoActivity.this, "请先选择苗源地址",
								Toast.LENGTH_SHORT).show();
						return;
					}
//					if ("".equals(et_count.getText().toString())) {
//						Toast.makeText(SaveMiaoActivity.this, "请先输入数量",
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
					if (!"".equals(et_minSpec.getText().toString())
							&& !"".equals(et_maxSpec.getText().toString())
							&& Double.parseDouble(et_minSpec.getText()
									.toString()) >= Double
									.parseDouble(et_maxSpec.getText()
											.toString())) {
						Toast.makeText(SaveMiaoActivity.this, "最大规格必须大于最小规格",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (urlPaths.size() == 0) {
						seedlingSave();
					} else {
						save.setClickable(false);
						hud_numHud.show();
						a = 0;
						for (int i = 0; i < urlPaths.size(); i++) {

							if (!StringUtil.isHttpUrlPicPath(urlPaths.get(i)
									.getUrl())) {
								GetServerUrl.addHeaders(finalHttp, true);
								finalHttp.addHeader("Content-Type",
										"application/octet-stream");
								AjaxParams params1 = new AjaxParams();
								params1.put("sourceId", "");
								File file1 = new File(urlPaths.get(i).getUrl());
								try {
									params1.put("file", file1);
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								params1.put("imagType", "seedling");
								finalHttp.post(GetServerUrl.getUrl()
										+ "admin/file/image", params1,
										new AjaxCallBack<Object>() {

											@Override
											public void onStart() {
												super.onStart();
											}

											@Override
											public void onSuccess(Object t) {
												try {
													JSONObject jsonObject = new JSONObject(
															t.toString());
													int code = jsonObject
															.getInt("code");
													if (code == 1) {
														JSONObject image = JsonGetInfo.getJSONObject(
																JsonGetInfo
																		.getJSONObject(
																				jsonObject,
																				"data"),
																"image");
														urlPaths.set(
																a,
																new Pic(
																		JsonGetInfo
																				.getJsonString(
																						image,
																						"id"),
																		false,
																		JsonGetInfo
																				.getJsonString(
																						image,
																						"url"),
																		a));
														a++;
														hudProgress();
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

												super.onSuccess(t);
											}

											@Override
											public void onFailure(Throwable t,
													int errorNo, String strMsg) {
												// TODO Auto-generated method
												// stub
												super.onFailure(t, errorNo,
														strMsg);
												save.setClickable(true);
												a++;
												hudProgress();

											}
										});
							} else {
								a++;
								hudProgress();
							}

						}
					}

				} else if (view.getId() == R.id.id_tv_edit_all) {
					mCache.remove("savemiao");
					finish();
					startActivity(new Intent(SaveMiaoActivity.this,
							SaveMiaoActivity.class));
					//
					// 清空
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

	public void hudProgress() {
		if (hud_numHud != null && !SaveMiaoActivity.this.isFinishing()) {
			hud_numHud.setProgress(a * 100 / urlPaths.size());
			hud_numHud.setProgressText("上传中(" + a + "/" + urlPaths.size()
					+ "张)");
		}
		if (a == urlPaths.size()) {
			if (urlPaths.size() > 0) {
				if (hud_numHud != null && !SaveMiaoActivity.this.isFinishing()) {
					hud_numHud.dismiss();
				}

				if (urlPaths.size() > 0) {
					if (!PicValiteIsUtils.needPicValite(urlPaths)) {
						Toast.makeText(SaveMiaoActivity.this, "请上传完未上传的图片",
								Toast.LENGTH_SHORT).show();
						return;
					}
					// if(PicValiteIsUtils.needPicValite(urlPaths)){}
					Data.pics1.clear();
					hud.show();
					for (int i = 0; i < urlPaths.size(); i++) {
						Data.pics1.add(urlPaths.get(i));
					}
					seedlingSave();
				} else {
					Toast.makeText(SaveMiaoActivity.this, "请选择图片上传",
							Toast.LENGTH_SHORT).show();

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
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		if (!"".equals(id)) {
			params.put("id", id);
		}
		params.put("firstSeedlingTypeId", firstSeedlingTypeId);
		params.put("name", et_name.getText().toString());
		params.put("price", et_price.getText().toString());
		params.put("nurseryId", addressId);
		params.put("count", et_count.getText().toString());
		params.put("height", et_height.getText().toString());
		params.put("crown", et_crown.getText().toString());
		params.put("maxHeight", et_maxHeight.getText().toString());
		params.put("maxCrown", et_maxCrown.getText().toString());
		params.put("minSpec", et_minSpec.getText().toString());
		params.put("maxSpec", et_maxSpec.getText().toString());
		params.put("imagesData", gson.toJson(Data.pics1));
		finalHttp.post(GetServerUrl.getUrl() + "admin/seedlingNote/save",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						save.setClickable(true);
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = JsonGetInfo.getJsonString(jsonObject,
									"code");
							String msg = JsonGetInfo.getJsonString(jsonObject,
									"msg");
							if (!"".equals(msg)) {
								Toast.makeText(SaveMiaoActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {

								if (BACK_TYPE == 0) {
									if (!"".equals(id)) {
										mCache.remove("savemiao");
										StorageSave mStorageSave = new StorageSave();
										mStorageSave.setNurseryId(addressId);
										mStorageSave
												.setContactName(contactName);
										mStorageSave
												.setContactPhone(contactPhone);
										mStorageSave.setDefault(isDefault);
										// 额外数据
										mStorageSave.setAddress(fullAddress);
										// 保存缓存
										mCache.put("savemiao",
												gson.toJson(mStorageSave));
										setResult(8);
										finish();
										Intent toSaveMiaoActivity = new Intent(
												SaveMiaoActivity.this,
												SaveMiaoActivity.class);
										startActivity(toSaveMiaoActivity);
										overridePendingTransition(
												R.anim.slide_in_left,
												R.anim.slide_out_right);
									} else {
										Data.pics1.clear();
										Data.photoList.clear();
										Data.microBmList.clear();
										Data.paramsDatas.clear();
										mCache.remove("savemiao");
										setResult(1);
										finish();
										Intent toManagerListActivity = new Intent(
												SaveMiaoActivity.this,
												MiaoNoteListActivity.class);
										startActivity(toManagerListActivity);
										overridePendingTransition(
												R.anim.slide_in_left,
												R.anim.slide_out_right);
									}
								} else if (BACK_TYPE == 1) {
									finish();
									Intent toMiaoNoteListActivity = new Intent(
											SaveMiaoActivity.this,
											MiaoNoteListActivity.class);
									startActivity(toMiaoNoteListActivity);
									overridePendingTransition(
											R.anim.slide_in_left,
											R.anim.slide_out_right);
								}

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
						Toast.makeText(SaveMiaoActivity.this,
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
		if (requestCode == TO_TAKE_PIC && resultCode == RESULT_OK) {
			try {
				addImageItem(flowerInfoPhotoPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 其次把文件插入到系统图库
			try {
				MediaStore.Images.Media.insertImage(getContentResolver(),
						flowerInfoPhotoPath, flowerInfoPhotoPath, null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 最后通知图库更新
			// sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
			// Uri.parse("file://" + flowerInfoPhotoPath)));
		} else if (resultCode == 2) {
			addressId = data.getStringExtra("addressId");
			contactPhone = data.getStringExtra("contactPhone");
			contactName = data.getStringExtra("contactName");
			fullAddress = data.getStringExtra("cityName");
			boolean isDefault = data.getBooleanExtra("isDefault", false);
			tv_contanct_name.setText(contactName + "\u0020" + contactPhone);
			tv_address_name.setText(fullAddress);
			if (isDefault) {
				tv_is_defoloat.setVisibility(View.VISIBLE);
			} else {
				tv_is_defoloat.setVisibility(View.GONE);
			}
			ll_05.setVisibility(View.GONE);
			list_item_adress.setVisibility(View.VISIBLE);
			if (!"".equals(addressId)) {
				tv_address.setText("已选择");
			}
		} else if (resultCode == 3) {
			count = data.getStringExtra("count");
			diameter = data.getStringExtra("diameter");
			diameterType = data.getStringExtra("diameterType");
			dbh = data.getStringExtra("dbh");
			dbhType = data.getStringExtra("dbhType");
			height = data.getStringExtra("height");
			crown = data.getStringExtra("crown");
			maxHeight = data.getStringExtra("maxHeight");
			maxCrown = data.getStringExtra("maxCrown");
			offbarHeight = data.getStringExtra("offbarHeight");
			length = data.getStringExtra("length");
			plantType = data.getStringExtra("plantType");
			paramsData = data.getStringExtra("paramsData");
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

	public static void startPhotoActivity(Activity context, int startType,
			String dirId, int photoType, int hadChoosePicCount, int requestCode) {
		Intent intent = new Intent(context, UpdataImageActivity.class);
		intent.putExtra(INTENT_START_TYPE_KEY, startType);
		intent.putExtra(INTENT_DIR_ID_KEY, dirId);
		intent.putExtra(INTENT_PHOTO_TYPE_KEY, photoType);
		intent.putExtra(INTENT_HAD_CHOOSE_PHOTO_KEY, hadChoosePicCount);
		if (requestCode != INTENT_NOT_NEED_FOR_RESULT) {
			context.startActivityForResult(intent, requestCode);
		} else {
			context.startActivity(intent);
		}
	}

	public static void startPhotoAlbumActivity(Context context, int photoType,
			int hadChoosePicCount) {
		Intent intent = new Intent(context, PhotoAlbumActivity.class);
		intent.putExtra(UpdataImageActivity.INTENT_PHOTO_TYPE_KEY, photoType);
		intent.putExtra(UpdataImageActivity.INTENT_HAD_CHOOSE_PHOTO_KEY,
				hadChoosePicCount);
		context.startActivity(intent);
	}

	private class PhotoGvOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == adapter.getUrlPathsCount()) {
				boolean requestCamerPermissions = new PermissionUtils(
						SaveMiaoActivity.this).requestCamerPermissions(200);
				if (!requestCamerPermissions) {
					Toast.makeText(SaveMiaoActivity.this, "您未同意拍照权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				boolean requestReadSDCardPermissions = new PermissionUtils(
						SaveMiaoActivity.this)
						.requestReadSDCardPermissions(200);
				if (!requestReadSDCardPermissions) {
					Toast.makeText(SaveMiaoActivity.this, "您未同意应用读取SD卡权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				popwin = new FlowerInfoPhotoChoosePopwin3(
						SaveMiaoActivity.this, SaveMiaoActivity.this);
				popwin.showAtLocation(mainView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			} else {
				EditGalleryImageActivity.startEditGalleryImageActivity(
						SaveMiaoActivity.this,
						EditGalleryImageActivity.TO_EDIT_PUBLISH_IMAGE,
						position, adapter.getDataList());
			}

		}
	}

	/**
	 * 拍照
	 */
	public void toTakePic() {
		String photostatus = Environment.getExternalStorageState();
		if (photostatus.equals(Environment.MEDIA_MOUNTED)) {
			if (!ZzyUtil.ToastForSdcardSpaceEnough(SaveMiaoActivity.this, true)) {
				// SD卡空间不足
				Toast.makeText(SaveMiaoActivity.this, R.string.sdcard_is_full,
						Toast.LENGTH_SHORT).show();
				return;
			}
			doTakePhoto();
		} else {
			Toast.makeText(SaveMiaoActivity.this, R.string.sdcard_is_unmount,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 进入拍照
	 */
	private void doTakePhoto() {
		long str = System.currentTimeMillis();
		String filename = "flower_info_" + str + ".png";
		File photoFile = new File(FileUtil.getFlowerPicPath(""), filename);
		flowerInfoPhotoPath = FileUtil.getFlowerPicPath(filename);
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			startActivityForResult(intent, TO_TAKE_PIC);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(SaveMiaoActivity.this, R.string.cannot_select_pic,
					Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * 选择相片
	 */
	public void toChoosePic() {
		String picstatus = Environment.getExternalStorageState();
		if (picstatus.equals(Environment.MEDIA_MOUNTED)) {
			if (SystemSetting.getInstance(SaveMiaoActivity.this).choosePhotoDirId
					.length() > 0
					&& SystemSetting.isAlbumHasPhoto(
							SaveMiaoActivity.this.getContentResolver(),
							SaveMiaoActivity.this)) {
				// UpdataImageActivity
				// .startPhotoActivity(
				// UpdataImageActivity.this,
				// PhotoActivity.START_TYPE_JUMP_IN_NOT_FROM_ALBUM,
				// SystemSetting
				// .getInstance(UpdataImageActivity.this).choosePhotoDirId,
				// PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
				// adapter.getUrlPathsCount(),
				// PhotoActivity.INTENT_NOT_NEED_FOR_RESULT);
				PhotoAlbumActivity.startPhotoAlbumActivity(
						SaveMiaoActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			} else {
				PhotoAlbumActivity.startPhotoAlbumActivity(
						SaveMiaoActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			}
		} else {
			Toast.makeText(SaveMiaoActivity.this, R.string.sdcard_is_unmount,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 添加图片
	 * 
	 * @param sourchImagePath
	 */
	private void addImageItem(String sourchImagePath) throws IOException {
		// TODO 还需要做动态图片的预览处理和大小限制
		String image_path = "";
		long imageSize = 0;
		File file = new File(sourchImagePath);
		// 获取原图的宽高
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bm = ImageTools.decodeFile(sourchImagePath, opts,
				COMPRESS_IMAGE_WIDTH_PX, COMPRESS_IMAGE_HEIGHT_PX);
		if (bm != null) {
			newWidth = bm.getWidth();
			newHeight = bm.getHeight();
		}
		ExifInterface exifInterface = new ExifInterface(sourchImagePath);
		int orc = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				-1);
		// 然后旋转
		int degree = 0;
		if (orc == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		long sourceImgSize = file.length();
		imageSize = sourceImgSize;
		boolean isGif = GifImgHelperUtil.isGif(sourchImagePath);

		/** 如果不是GIF图片 */
		if (!isGif) {
			// SD卡空间足够才压缩
			if (ZzyUtil.ToastForSdcardSpaceEnough(SaveMiaoActivity.this, false)) {
				image_path = CompressAndSaveImg(file, degree, sourchImagePath);
				file = new File(image_path);
				imageSize = file.length();
				if (imageSize > sourceImgSize) {
					image_path = sourchImagePath;
					file = new File(sourchImagePath);
					imageSize = sourceImgSize;
				}
			}
		} else {
			image_path = sourchImagePath;
		}
		// 图片不可超过5M，如果压缩成功，则用压缩后图片
		if (imageSize > ImageTools.MAX_IMAGE_SIZE) {
			handler.sendEmptyMessage(PIC_IS_TOO_BIG);
			return;
		}
		Bitmap showbm = null;
		if (degree == 0) {
			showbm = ImageTools
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		} else {
			showbm = ImageTools
					.converBitmap(file, GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
			showbm = ImageTools
					.rotate(file.getAbsolutePath(), showbm, degree,
							GRID_COMPRESS_IMAGE_HEIGHT_PX,
							GRID_COMPRESS_IMAGE_WIDTH_PX);
		}
		if (showbm != null) {
			flowerInfoPhotoPath = image_path;
			Pic pic = new Pic("", false, flowerInfoPhotoPath, 0);
			adapter.addItem(pic);
			handler.sendEmptyMessage(ADD_NEW_PIC);
		} else {
			handler.sendEmptyMessage(LOAD_PIC_FAILURE);
			return;
		}
	}

	private class RefreshHandler extends Handler {

		public RefreshHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_PIC_FAILURE:
				Toast.makeText(SaveMiaoActivity.this,
						R.string.image_load_failed, Toast.LENGTH_SHORT).show();
				break;
			case ADD_NEW_PIC:
				// adapter.notifyDataSetChanged();
				adapter.notify(urlPaths);

				break;
			default:
				break;
			}
		}
	}

	/**
	 * 添加图片
	 */
	public void addPicUrls(ArrayList<Pic> items) {
		// GlobalData.reqSeedlingData.addPictureUrlItems(items);
		adapter.addItems(items);
	}

	/**
	 * 删除图片
	 */
	public void removePicUrls(int pos) {
		// GlobalData.reqSeedlingData.removePic(pos);
		// ZzyUtil.printMessage(adapter.getDataList().get(pos));
		adapter.removeItem(pos);
	}

	/**
	 * 如果是静态图片，则进行压缩处理 压缩并存储临时文件至Image目录
	 * 
	 * @param rotate
	 */
	private String CompressAndSaveImg(File file, int degree,
			String sourceImgPath) throws IOException {
		/** 用于压缩的原图Image */
		Bitmap bitmapSourceImg;
		if (degree == 0) {
			bitmapSourceImg = ImageTools.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX, COMPRESS_IMAGE_WIDTH_PX);
		} else {
			bitmapSourceImg = ImageTools.converBitmap(file,
					COMPRESS_IMAGE_HEIGHT_PX / 2, COMPRESS_IMAGE_WIDTH_PX / 2);
			bitmapSourceImg = ImageTools.rotate(file.getAbsolutePath(),
					bitmapSourceImg, degree, COMPRESS_IMAGE_HEIGHT_PX / 2,
					COMPRESS_IMAGE_WIDTH_PX / 2);
		}
		String img_path = "";
		img_path = FileUtil.getFlowerPicPath("") + "/" + "flower_image_"
				+ System.currentTimeMillis() + ".png";
		File filetemp = new File(img_path);
		// 存储临时文件
		if (bitmapSourceImg != null) {
			FileOutputStream out = new FileOutputStream(filetemp);
			bitmapSourceImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} else {
			return sourceImgPath;
		}
		newWidth = bitmapSourceImg.getWidth();
		newHeight = bitmapSourceImg.getHeight();
		bitmapSourceImg = null;
		return img_path;
	}

}
