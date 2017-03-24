package com.hldj.hmyg.broker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.adapter.PublishFlowerInfoPhotoAdapter;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.PicValiteIsUtils;
import com.hldj.hmyg.saler.FlowerInfoPhotoChoosePopwin2;
import com.hldj.hmyg.saler.FlowerInfoPhotoChoosePopwin5;
import com.hldj.hmyg.saler.SaveSeedlingActivity;
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
import com.zzy.common.widget.MeasureGridView;
import com.zzy.flowers.activity.photoalbum.EditGalleryImageActivity;
import com.zzy.flowers.activity.photoalbum.PhotoActivity;
import com.zzy.flowers.activity.photoalbum.PhotoAlbumActivity;

public class AddCarActivity extends NeedSwipeBackActivity {

	private LinearLayout ll_to_d4;
	private LinearLayout ll_fahuo;
	private TextView save;
	private String orderId = "";
	private ArrayList<ReceiptListJson> list = new ArrayList<ReceiptListJson>();//
	private TextView popCheckOut; // 结算
	private LinearLayout layout; // 结算布局
	private ReceiptAdapter radapter; // 自定义适配器
	private float price = 0;
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

	private RefreshHandler Rehandler;
	private FlowerInfoPhotoChoosePopwin5 popwin;
	public static AddCarActivity instance;
	private KProgressHUD hud_numHud;
	FinalHttp finalHttp = new FinalHttp();
	public int a = 0;
	private ArrayList<Pic> urlPaths = new ArrayList<Pic>();
	private View mainView;

	private Gson gson;
	private KProgressHUD hud;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_car);
		gson = new Gson();
		hud_numHud = KProgressHUD.create(AddCarActivity.this)
				.setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true);
		instance = this;
		hud = KProgressHUD.create(AddCarActivity.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("上传中，请等待...").setMaxProgress(100)
				.setCancellable(true).setDimAmount(0.5f);
		Data.pics1.clear();
		Data.photoList.clear();
		Data.microBmList.clear();
		Data.paramsDatas.clear();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		orderId = getIntent().getStringExtra("id");
		lv = (ListView) findViewById(R.id.lv);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_car_num = (EditText) findViewById(R.id.et_car_num);
		et_car_remark = (EditText) findViewById(R.id.et_car_remark);
		// 订单ID
		save = (TextView) findViewById(R.id.tv_zancun);
		ll_to_d4 = (LinearLayout) findViewById(R.id.ll_to_d4);
		ll_fahuo = (LinearLayout) findViewById(R.id.ll_fahuo);
		initData();
		photoGv = (MeasureGridView) findViewById(R.id.publish_flower_info_gv);
		adapter = new PublishFlowerInfoPhotoAdapter(AddCarActivity.this,
				urlPaths);
		photoGv.setAdapter(adapter);
		PhotoGvOnItemClickListener itemClickListener = new PhotoGvOnItemClickListener();
		photoGv.setOnItemClickListener(itemClickListener);
		Rehandler = new RefreshHandler(this.getMainLooper());
		mainView = (View) findViewById(R.id.ll_mainView);
		btn_back.setOnClickListener(multipleClickProcess);
		ll_to_d4.setOnClickListener(multipleClickProcess);
		ll_fahuo.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);

	}

	private void initData() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", orderId);
		finalHttp.post(GetServerUrl.getUrl() + "admin/agent/delivery/detail",
				params, new AjaxCallBack<Object>() {

					private String deliveryId;
					private String buyerId;

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
								JSONObject delivery = JsonGetInfo
										.getJSONObject(JsonGetInfo
												.getJSONObject(jsonObject,
														"data"), "delivery");
								deliveryId = JsonGetInfo.getJsonString(
										delivery, "id");
								JSONObject buyerJson = JsonGetInfo
										.getJSONObject(delivery, "buyerJson");
								buyerId = JsonGetInfo.getJsonString(buyerJson,
										"id");
								JSONArray jsonArray_cartList = JsonGetInfo
										.getJsonArray(delivery,
												"receiptListJson");
								if (jsonArray_cartList.length() > 0) {
									for (int j = 0; j < jsonArray_cartList
											.length(); j++) {
										JSONObject jsonObject4 = jsonArray_cartList
												.getJSONObject(j);
										ReceiptListJson bean = new ReceiptListJson();
										bean.setId(JsonGetInfo.getJsonString(
												jsonObject4, "id"));
										bean.setRemarks(JsonGetInfo
												.getJsonString(jsonObject4,
														"remarks"));
										bean.setCreateBy(JsonGetInfo
												.getJsonString(jsonObject4,
														"specText"));
										bean.setCreateDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"createDate"));
										bean.setReceiptDate(JsonGetInfo
												.getJsonString(jsonObject4,
														"receiptDate"));
										bean.setReceiptAddressId(JsonGetInfo
												.getJsonString(jsonObject4,
														"receiptAddressId"));
										bean.setOrderId(JsonGetInfo
												.getJsonString(jsonObject4,
														"orderId"));
										bean.setBuyerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"buyerId"));
										bean.setDeliveryId(JsonGetInfo
												.getJsonString(jsonObject4,
														"deliveryId"));
										bean.setOrderName(JsonGetInfo
												.getJsonString(jsonObject4,
														"orderName"));
										bean.setOrderNum(JsonGetInfo
												.getJsonString(jsonObject4,
														"orderNum"));
										bean.setSellerId(JsonGetInfo
												.getJsonString(jsonObject4,
														"sellerId"));
										bean.setCount(JsonGetInfo.getJsonInt(
												jsonObject4, "count"));
										bean.setLoadedCountJson(JsonGetInfo
												.getJsonInt(jsonObject4,
														"loadedCountJson"));
										bean.setDelivery(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"isDelivery"));
										bean.setPayConfirm(JsonGetInfo
												.getJsonBoolean(jsonObject4,
														"payConfirm"));
										bean.setContactName(JsonGetInfo.getJsonString(
												JsonGetInfo.getJSONObject(
														jsonObject4,
														"receiptAddressJson"),
												"contactName"));
										bean.setContactPhone(JsonGetInfo.getJsonString(
												JsonGetInfo.getJSONObject(
														jsonObject4,
														"receiptAddressJson"),
												"contactPhone"));
										bean.setFullAddress(JsonGetInfo.getJsonString(
												JsonGetInfo.getJSONObject(
														jsonObject4,
														"receiptAddressJson"),
												"fullAddress"));
										bean.setShowCheckBox(true);
										bean.setChoosed(false);
										list.add(bean);
										if (radapter != null) {
											radapter.notifyDataSetChanged();
										}
									}
									if (radapter == null) {
										radapter = new ReceiptAdapter(
												AddCarActivity.this, list,
												handler,
												R.layout.list_item_add_receipt);
										lv.setAdapter(radapter);
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
						Toast.makeText(AddCarActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) { // 更改选中商品的总价格
				price = (Float) msg.obj;
				if (price > 0) {
				} else {
				}
			} else if (msg.what == 11) {
			}
		}
	};

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
	private EditText et_name;
	private EditText et_phone;
	private EditText et_car_num;
	private EditText et_car_remark;
	private ListView lv;

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
				case R.id.ll_to_d4:
					Intent toAddReceiptActivity = new Intent(
							AddCarActivity.this, AddReceiptActivity.class);
					toAddReceiptActivity.putExtra("id", orderId);
					startActivityForResult(toAddReceiptActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_fahuo:

					FinalHttp finalHttp = new FinalHttp();
					GetServerUrl.addHeaders(finalHttp,true);
					AjaxParams params = new AjaxParams();
					params.put("id", orderId);
					finalHttp.post(GetServerUrl.getUrl()
							+ "admin/agent/loadCar/saveAndSend", params,
							new AjaxCallBack<Object>() {

								private String deliveryId;
								private String buyerId;

								@Override
								public void onSuccess(Object t) {
									// TODO Auto-generated method stub

									try {
										JSONObject jsonObject = new JSONObject(
												t.toString());
										String code = JsonGetInfo
												.getJsonString(jsonObject,
														"code");
										String msg = JsonGetInfo.getJsonString(
												jsonObject, "msg");
										if (!"".equals(msg)) {
										}
										if ("1".equals(code)) {
											JSONObject delivery = JsonGetInfo
													.getJSONObject(JsonGetInfo
															.getJSONObject(
																	jsonObject,
																	"data"),
															"delivery");
											deliveryId = JsonGetInfo
													.getJsonString(delivery,
															"id");
											JSONObject buyerJson = JsonGetInfo
													.getJSONObject(delivery,
															"buyerJson");
											buyerId = JsonGetInfo
													.getJsonString(buyerJson,
															"id");
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
									Toast.makeText(AddCarActivity.this,
											R.string.error_net,
											Toast.LENGTH_SHORT).show();
									super.onFailure(t, errorNo, strMsg);
								}

							});

					break;

				case R.id.tv_zancun:
					// 改成下一步
					// if ("".equals(et_name.getText().toString())) {
					// Toast.makeText(AddCarActivity.this, "请先输入司机姓名",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					if ("".equals(et_phone.getText().toString())) {
						Toast.makeText(AddCarActivity.this, "请先输入司机电话",
								Toast.LENGTH_SHORT).show();
						return;
					}
					// if ("".equals(et_car_num.getText().toString())) {
					// Toast.makeText(AddCarActivity.this, "请先输入车牌号",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					if (!et_phone.getText().toString().startsWith("1")
							|| et_phone.getText().toString().length() != 11) {
						Toast.makeText(AddCarActivity.this, "请输入正确的电话号码",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (urlPaths.size() == 0) {
						// Toast.makeText(SaveMarketPriceActivity.this,
						// "请选择图片上传",
						// Toast.LENGTH_SHORT).show();
						save.setClickable(false);
						seedlingSave();
						return;
					}

					save.setClickable(false);
					hud_numHud.show();
					a = 0;
					for (int i = 0; i < urlPaths.size(); i++) {

						if (!StringUtil.isHttpUrlPicPath(urlPaths.get(i)
								.getUrl())) {
							FinalHttp finalHttp1 = new FinalHttp();
							GetServerUrl.addHeaders(finalHttp1,true);
							finalHttp1.addHeader("Content-Type",
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
							finalHttp1.post(GetServerUrl.getUrl()
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
											super.onFailure(t, errorNo, strMsg);
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

					// 下一步2
					// Intent toChooseReceptListActivity = new Intent(
					// AddCarActivity.this, ChooseReceptListActivity.class);
					// toChooseReceptListActivity.putExtra("id", orderId);
					// toChooseReceptListActivity.putExtra("driverName", et_name
					// .getText().toString());
					// toChooseReceptListActivity.putExtra("driverPhone",
					// et_phone
					// .getText().toString());
					// toChooseReceptListActivity.putExtra("carNum", et_car_num
					// .getText().toString());
					// toChooseReceptListActivity.putExtra("remarks",
					// et_car_remark.getText().toString());
					// startActivityForResult(toChooseReceptListActivity, 1);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);
					break;
				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		public void hudProgress() {
			if (hud_numHud != null && !AddCarActivity.this.isFinishing()) {
				hud_numHud.setProgress(a * 100 / urlPaths.size());
				hud_numHud.setProgressText("上传中(" + a + "/" + urlPaths.size()
						+ "张)");
			}
			if (a == urlPaths.size()) {
				if (urlPaths.size() > 0) {
					if (hud_numHud != null
							&& !AddCarActivity.this.isFinishing()) {
						hud_numHud.dismiss();
					}

					if (urlPaths.size() > 0) {
						if (!PicValiteIsUtils.needPicValite(urlPaths)) {
							Toast.makeText(AddCarActivity.this, "请上传完未上传的图片",
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
						Toast.makeText(AddCarActivity.this, "请选择图片上传",
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

			if (list.size() == 0) {
				Toast.makeText(AddCarActivity.this, "您还没有添加装车品种哦",
						Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayList<itemsData> itemsDatas = new ArrayList<itemsData>();
			for (int i = 0; i < list.size(); i++) {
				itemsData itemsData = new itemsData();
				itemsData.setId("");
				itemsData.setDeliveryId(list.get(i).getDeliveryId());
				// itemsData.setOrderId(orderId);
				itemsData.setOrderId(list.get(i).getOrderId());
				itemsData.setReceiptId(list.get(i).getId());
				itemsData.setLoadCount(list.get(i).getNeed_loadedCount() + "");
				itemsDatas.add(itemsData);
			}
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("id", "");
			params.put("deliveryId", list.get(0).getDeliveryId());
			params.put("buyerId", list.get(0).getBuyerId());
			params.put("itemsData", gson.toJson(itemsDatas));
			params.put("driverName", et_name.getText().toString());
			params.put("driverPhone", et_phone.getText().toString());
			params.put("carNum", et_car_num.getText().toString());
			params.put("remarks", et_car_remark.getText().toString());
			params.put("imagesData", gson.toJson(Data.pics1));
			finalHttp.post(GetServerUrl.getUrl()
					+ "admin/agent/loadCar/saveAndSend", params,
					new AjaxCallBack<Object>() {

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
								}
								if ("1".equals(code)) {
									Toast.makeText(AddCarActivity.this, "装车成功",
											Toast.LENGTH_SHORT).show();
									setResult(2);
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
							Toast.makeText(AddCarActivity.this,
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
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == 2) {
			setResult(2);
			finish();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void finish() {
		super.finish();
		instance = null;
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
						AddCarActivity.this).requestCamerPermissions(200);
				if (!requestCamerPermissions) {
					Toast.makeText(AddCarActivity.this, "您未同意拍照权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				boolean requestReadSDCardPermissions = new PermissionUtils(
						AddCarActivity.this).requestReadSDCardPermissions(200);
				if (!requestReadSDCardPermissions) {
					Toast.makeText(AddCarActivity.this, "您未同意应用读取SD卡权限",
							Toast.LENGTH_SHORT).show();
					return;
				}
				popwin = new FlowerInfoPhotoChoosePopwin5(AddCarActivity.this,
						AddCarActivity.this);
				popwin.showAtLocation(mainView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			} else {
				EditGalleryImageActivity.startEditGalleryImageActivity(
						AddCarActivity.this,
						EditGalleryImageActivity.TO_EDIT_PUBLISH_IMAGE,
						position, adapter.getDataList());
			}

		}
	}

	/**
	 * 拍照
	 */
	public void toTakePic() {
		Log.e("toTakePic", "photostatus1");
		String photostatus = Environment.getExternalStorageState();
		Log.e("toTakePic", photostatus);
		if (photostatus.equals(Environment.MEDIA_MOUNTED)) {
			if (!ZzyUtil.ToastForSdcardSpaceEnough(AddCarActivity.this, true)) {
				// SD卡空间不足
				Toast.makeText(AddCarActivity.this, R.string.sdcard_is_full,
						Toast.LENGTH_SHORT).show();
				return;
			}
			doTakePhoto();
			Log.e("toTakePic", "photostatus2");
		} else {
			Toast.makeText(AddCarActivity.this, R.string.sdcard_is_unmount,
					Toast.LENGTH_SHORT).show();
			Log.e("toTakePic", "photostatus3");
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
			Toast.makeText(AddCarActivity.this, R.string.cannot_select_pic,
					Toast.LENGTH_SHORT).show();

		}
		Log.e("toTakePic", "doTakePhoto");
	}

	/**
	 * 选择相片
	 */
	public void toChoosePic() {
		String picstatus = Environment.getExternalStorageState();
		if (picstatus.equals(Environment.MEDIA_MOUNTED)) {
			if (SystemSetting.getInstance(AddCarActivity.this).choosePhotoDirId
					.length() > 0
					&& SystemSetting.isAlbumHasPhoto(
							AddCarActivity.this.getContentResolver(),
							AddCarActivity.this)) {
				// UpdataImageActivity
				// .startPhotoActivity(
				// UpdataImageActivity.this,
				// PhotoActivity.START_TYPE_JUMP_IN_NOT_FROM_ALBUM,
				// SystemSetting
				// .getInstance(UpdataImageActivity.this).choosePhotoDirId,
				// PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
				// adapter.getUrlPathsCount(),
				// PhotoActivity.INTENT_NOT_NEED_FOR_RESULT);
				PhotoAlbumActivity.startPhotoAlbumActivity(AddCarActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			} else {
				PhotoAlbumActivity.startPhotoAlbumActivity(AddCarActivity.this,
						PhotoActivity.PHOTO_TYPE_PUBLISH_SEED_ATTACH,
						adapter.getUrlPathsCount());
			}
		} else {
			Toast.makeText(AddCarActivity.this, R.string.sdcard_is_unmount,
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
			if (ZzyUtil.ToastForSdcardSpaceEnough(AddCarActivity.this, false)) {
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
				Toast.makeText(AddCarActivity.this, R.string.image_load_failed,
						Toast.LENGTH_SHORT).show();
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
