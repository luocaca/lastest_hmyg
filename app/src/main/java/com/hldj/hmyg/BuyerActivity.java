package com.hldj.hmyg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.car.example.FindFlowerActivity;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.buyer.BuyOrderActivity2;
import com.hldj.hmyg.buyer.BuyerValidateApplyActivity;
import com.hldj.hmyg.buyer.LoadCarListActivity2;
import com.hldj.hmyg.buyer.ManagerPurchaseListActivity;
import com.hldj.hmyg.buyer.PurchaseDemandListActivity;
import com.hldj.hmyg.buyer.ReceiptAdressListActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.hy.utils.Loading;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;
import com.xingguo.huang.mabiwang.util.CacheUtils;
import com.xingguo.huang.mabiwang.util.PictureManageUtil;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zym.selecthead.config.Configs;
import com.zym.selecthead.tools.FileTools;
import com.zym.selecthead.tools.SelectHeadTools;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeViewHelper.BadgeGravity;
import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerActivity extends LoginActivity {
	private BGABadgeLinearLayout ll_yanmiao_nei_01;
	private BGABadgeLinearLayout ll_yanmiao_nei_02;
	private BGABadgeLinearLayout ll_yanmiao_nei_03;
	private BGABadgeLinearLayout ll_yanmiao_nei_04;
	private BGABadgeLinearLayout ll_yanmiao_nei_05;
	private BGABadgeLinearLayout ll_daifahuo_00;
	private BGABadgeLinearLayout ll_daifahuo_01;
	private BGABadgeLinearLayout ll_daifahuo_02;
	private BGABadgeLinearLayout ll_daifahuo_03;
	private BGABadgeLinearLayout ll_loadcar_00;
	private BGABadgeLinearLayout ll_loadcar_01;
	private BGABadgeLinearLayout ll_loadcar_02;
	private ImageView iv_msg;
	private FinalBitmap fb;
	private CircleImageView iv_icon_persion_pic;
	private TextView tv_user_name;
	private BGABadgeLinearLayout ll_yanmiao_nei_00;

	private static final int REQUEST_CODE_ALBUM = 1;
	private static final int REQUEST_CODE_CAMERA = 2;
	String dateTime;
	String targeturl = null;
	private static final int SCALE = 5;// 照片缩小比例
	private Bitmap bitmap;
	private Loading loading;
	private Uri photoUri = null;
	private Bitmap cropBitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buyer);
		fb = FinalBitmap.create(this);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		iv_msg = (ImageView) findViewById(R.id.iv_msg);
		iv_icon_persion_pic = (CircleImageView) findViewById(R.id.iv_icon_persion_pic);
		ll_yanmiao_nei_00 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_00);
		ll_yanmiao_nei_01 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_01);
		ll_yanmiao_nei_02 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_02);
		ll_yanmiao_nei_03 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_03);
		ll_yanmiao_nei_04 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_04);
		ll_yanmiao_nei_05 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_05);
		ll_daifahuo_00 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_00);
		ll_daifahuo_01 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_01);
		ll_daifahuo_02 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_02);
		ll_daifahuo_03 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_03);
		ll_loadcar_00 = (BGABadgeLinearLayout) findViewById(R.id.ll_loadcar_00);
		ll_loadcar_01 = (BGABadgeLinearLayout) findViewById(R.id.ll_loadcar_01);
		ll_loadcar_02 = (BGABadgeLinearLayout) findViewById(R.id.ll_loadcar_02);
		LinearLayout ll_all_yanmiao_order = (LinearLayout) findViewById(R.id.ll_all_yanmiao_order);
		LinearLayout ll_yanmiao_00 = (LinearLayout) findViewById(R.id.ll_yanmiao_00);
		LinearLayout ll_yanmiao_01 = (LinearLayout) findViewById(R.id.ll_yanmiao_01);
		LinearLayout ll_yanmiao_02 = (LinearLayout) findViewById(R.id.ll_yanmiao_02);
		LinearLayout ll_yanmiao_03 = (LinearLayout) findViewById(R.id.ll_yanmiao_03);
		LinearLayout ll_yanmiao_04 = (LinearLayout) findViewById(R.id.ll_yanmiao_04);
		LinearLayout ll_yanmiao_05 = (LinearLayout) findViewById(R.id.ll_yanmiao_05);
		LinearLayout ll_all_order = (LinearLayout) findViewById(R.id.ll_all_order);
		LinearLayout ll_daifukuan = (LinearLayout) findViewById(R.id.ll_daifukuan);
		LinearLayout ll_daifahuo = (LinearLayout) findViewById(R.id.ll_daifahuo);
		LinearLayout ll_daishouhuo = (LinearLayout) findViewById(R.id.ll_daishouhuo);
		LinearLayout ll_yiwancheng = (LinearLayout) findViewById(R.id.ll_yiwancheng);
		LinearLayout ll_loadcar_yizhuangche = (LinearLayout) findViewById(R.id.ll_loadcar_yizhuangche);
		LinearLayout ll_loadcar_daishouhuo = (LinearLayout) findViewById(R.id.ll_loadcar_daishouhuo);
		LinearLayout ll_loadcar_yishouhuo = (LinearLayout) findViewById(R.id.ll_loadcar_yishouhuo);

		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		LinearLayout ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		iv_msg.setOnClickListener(multipleClickProcess);
		ll_all_order.setOnClickListener(multipleClickProcess);
		ll_daifukuan.setOnClickListener(multipleClickProcess);
		ll_daifahuo.setOnClickListener(multipleClickProcess);
		ll_daishouhuo.setOnClickListener(multipleClickProcess);
		ll_yiwancheng.setOnClickListener(multipleClickProcess);
		ll_loadcar_yizhuangche.setOnClickListener(multipleClickProcess);
		ll_loadcar_daishouhuo.setOnClickListener(multipleClickProcess);
		ll_loadcar_yishouhuo.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_07.setOnClickListener(multipleClickProcess);
		ll_all_yanmiao_order.setOnClickListener(multipleClickProcess);
		ll_yanmiao_00.setOnClickListener(multipleClickProcess);
		ll_yanmiao_01.setOnClickListener(multipleClickProcess);
		ll_yanmiao_02.setOnClickListener(multipleClickProcess);
		ll_yanmiao_03.setOnClickListener(multipleClickProcess);
		ll_yanmiao_04.setOnClickListener(multipleClickProcess);
		ll_yanmiao_05.setOnClickListener(multipleClickProcess);
		iv_icon_persion_pic.setOnClickListener(multipleClickProcess);
		iv_msg.setBackgroundResource(R.drawable.icon_msg);
		unReadCount();
		statusCount();
		orderStatusCount();
		loadCarstatusCount();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (iv_msg != null) {
			unReadCount();
			statusCount();
			orderStatusCount();
			loadCarstatusCount();
		}
		if (MyApplication.Userinfo.getBoolean("isLogin", false)
				&& iv_icon_persion_pic != null) {
			getUserInfo(MyApplication.Userinfo.getString("id", ""),
					"SetProfileActivity");
			if (!"".equals(MyApplication.Userinfo.getString("headImage", ""))) {
				ImageLoader.getInstance().displayImage(
						MyApplication.Userinfo.getString("headImage", ""),
						iv_icon_persion_pic);
			}
			tv_user_name.setText(MyApplication.Userinfo.getString(
					"showUserName", ""));
		}
		super.onResume();
	}

	private void unReadCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "admin/notice/unReadCount",
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
							if ("1".equals(code)) {
								int unReadCount = JsonGetInfo.getJsonInt(
										JsonGetInfo.getJSONObject(jsonObject,
												"data"), "unReadCount");
								if (unReadCount > 0) {
									iv_msg.setBackgroundResource(R.drawable.anim_message);
									AnimationDrawable anim = (AnimationDrawable) iv_msg
											.getBackground();
									anim.start();
								} else {
									iv_msg.setBackgroundResource(R.drawable.icon_msg);
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
					}

				});
	}

	@Override
	public void onBackPressed() {

		// 完全由自己控制返回键逻辑，系统不在控制，但是有个前提是不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键

		// 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键（这里处理之后return true.或者return false都会导致onBackPressed不会执行）

		// 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法（如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）

		this.finish();
		overridePendingTransition_back();


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
				case R.id.iv_msg:
					Intent toMessageListDetailActivity = new Intent(
							BuyerActivity.this, MessageListActivity.class);
					startActivity(toMessageListDetailActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_all_order:
					Intent toBuyOrderActivity = new Intent(BuyerActivity.this,
							BuyOrderActivity2.class);
					toBuyOrderActivity.putExtra("status", "");
					toBuyOrderActivity.putExtra("loadItems", false);
					startActivity(toBuyOrderActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

					break;
				case R.id.ll_daifukuan:
					Intent toBuyOrderActivity1 = new Intent(BuyerActivity.this,
							BuyOrderActivity2.class);
					toBuyOrderActivity1.putExtra("status", "unpay");
					toBuyOrderActivity1.putExtra("loadItems", false);
					startActivity(toBuyOrderActivity1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_daifahuo:
					Intent toBuyOrderActivity2 = new Intent(BuyerActivity.this,
							BuyOrderActivity2.class);
					toBuyOrderActivity2.putExtra("status", "unsend");
					toBuyOrderActivity2.putExtra("loadItems", false);
					startActivity(toBuyOrderActivity2);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_daishouhuo:
					Intent toBuyOrderActivity3 = new Intent(BuyerActivity.this,
							BuyOrderActivity2.class);
					toBuyOrderActivity3.putExtra("status", "unreceipt");
					toBuyOrderActivity3.putExtra("loadItems", false);
					startActivity(toBuyOrderActivity3);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_yiwancheng:
					Intent toBuyOrderActivity4 = new Intent(BuyerActivity.this,
							BuyOrderActivity2.class);
					toBuyOrderActivity4.putExtra("status", "finished");
					toBuyOrderActivity4.putExtra("loadItems", false);
					startActivity(toBuyOrderActivity4);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_all_yanmiao_order:
					Intent toBrokerActivity = new Intent(BuyerActivity.this,
							BuyerValidateApplyActivity.class);
					// 新的
					toBrokerActivity.putExtra("acceptStatus", "");
					toBrokerActivity.putExtra("loadItems", false);
					startActivity(toBrokerActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_yanmiao_00:
					Intent toBrokerActivity00 = new Intent(BuyerActivity.this,
							BuyerValidateApplyActivity.class);
					toBrokerActivity00.putExtra("acceptStatus", "unpay");
					toBrokerActivity00.putExtra("loadItems", true);
					startActivity(toBrokerActivity00);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_yanmiao_01:
					Intent toBrokerActivity0 = new Intent(BuyerActivity.this,
							BuyerValidateApplyActivity.class);
					toBrokerActivity0.putExtra("acceptStatus", "unaccept");
					toBrokerActivity0.putExtra("loadItems", true);
					startActivity(toBrokerActivity0);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_yanmiao_02:
					Intent toBrokerActivity1 = new Intent(BuyerActivity.this,
							BuyerValidateApplyActivity.class);
					toBrokerActivity1.putExtra("acceptStatus", "verifing");
					toBrokerActivity1.putExtra("loadItems", true);
					startActivity(toBrokerActivity1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_yanmiao_03:
					Intent toBrokerActivity2 = new Intent(BuyerActivity.this,
							BuyerValidateApplyActivity.class);
					toBrokerActivity2.putExtra("acceptStatus", "verifyed");
					toBrokerActivity2.putExtra("loadItems", true);
					startActivity(toBrokerActivity2);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_yanmiao_04:
					Intent toBrokerActivity3 = new Intent(BuyerActivity.this,
							BuyerValidateApplyActivity.class);
					toBrokerActivity3.putExtra("acceptStatus", "ordered");
					toBrokerActivity3.putExtra("loadItems", true);
					startActivity(toBrokerActivity3);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_01:
//					Intent toSavePurchaseActivity = new Intent(
//							BuyerActivity.this, SavePurchaseActivity.class);
//					startActivity(toSavePurchaseActivity);
//					overridePendingTransition(R.anim.slide_in_left,
//							R.anim.slide_out_right);
					
					Intent toPurchaseDemandListActivity = new Intent(BuyerActivity.this,
					PurchaseDemandListActivity.class);
					toPurchaseDemandListActivity.putExtra("tag", "buyer");
					startActivityForResult(toPurchaseDemandListActivity, 6);
					overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);

					break;
				case R.id.ll_02:
					// Intent toManagerPurchaseActivity = new Intent(
					// BuyerActivity.this, ManagerPurchaseActivity.class);
					// startActivity(toManagerPurchaseActivity);
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);
					Intent toManagerPurchaseActivity = new Intent(
							BuyerActivity.this,
							ManagerPurchaseListActivity.class);
					startActivity(toManagerPurchaseActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

					break;
				case R.id.ll_03:
					Intent toBActivity = new Intent(BuyerActivity.this,
							BActivity.class);
					toBActivity.putExtra("from", "context");
					startActivity(toBActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_04:
					Intent toDActivity4 = new Intent(BuyerActivity.this,
							DActivity5.class);
					toDActivity4.putExtra("type", "1");
					startActivity(toDActivity4);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_05:
					Intent toReceiptAdressListActivity = new Intent(
							BuyerActivity.this, ReceiptAdressListActivity.class);
					toReceiptAdressListActivity.putExtra("from",
							"ReceiptAdressListActivity");
					startActivity(toReceiptAdressListActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_06:
					Intent toFindFlowerActivity = new Intent(
							BuyerActivity.this, FindFlowerActivity.class);
					startActivity(toFindFlowerActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_07:

					break;
				case R.id.ll_loadcar_yizhuangche:
					Intent toLoadCarListActivity21 = new Intent(
							BuyerActivity.this, LoadCarListActivity2.class);
					toLoadCarListActivity21.putExtra("status", "loaded");
					toLoadCarListActivity21.putExtra("loadItems", true);
					startActivity(toLoadCarListActivity21);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_loadcar_daishouhuo:
					Intent toLoadCarListActivity22 = new Intent(
							BuyerActivity.this, LoadCarListActivity2.class);
					toLoadCarListActivity22.putExtra("status", "unreceipt");
					toLoadCarListActivity22.putExtra("loadItems", true);
					startActivity(toLoadCarListActivity22);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);

					break;
				case R.id.ll_loadcar_yishouhuo:
					Intent toLoadCarListActivity23 = new Intent(
							BuyerActivity.this, LoadCarListActivity2.class);
					toLoadCarListActivity23.putExtra("status", "receipted");
					toLoadCarListActivity23.putExtra("loadItems", true);
					startActivity(toLoadCarListActivity23);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
					
				case R.id.iv_icon_persion_pic:
					if (!FileTools.hasSdcard()) {
						Toast.makeText(BuyerActivity.this, "没有找到SD卡，请检查SD卡是否存在",
								Toast.LENGTH_SHORT).show();
						return;
					}
					try {
						photoUri = FileTools.getUriByFileDirAndFileName(
								Configs.SystemPicture.SAVE_DIRECTORY,
								Configs.SystemPicture.SAVE_PIC_NAME);
					} catch (IOException e) {
						Toast.makeText(BuyerActivity.this, "创建文件失败。",
								Toast.LENGTH_SHORT).show();
						return;
					}
					setPics();
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

	private void statusCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/validateApply/statusCount", params,
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"countMap");
								int unpay = JsonGetInfo.getJsonInt(jsonObject2,
										"unpay");
								int unaccept = JsonGetInfo.getJsonInt(
										jsonObject2, "unaccept");
								int verifing = JsonGetInfo.getJsonInt(
										jsonObject2, "verifing");
								int verifyed = JsonGetInfo.getJsonInt(
										jsonObject2, "verifyed");
								int ordered = JsonGetInfo.getJsonInt(
										jsonObject2, "ordered");
								if (unpay != 0) {
									ll_yanmiao_nei_00.showTextBadge(unpay + "");
									ll_yanmiao_nei_00.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_00.hiddenBadge();
								}
								if (unaccept != 0) {
									ll_yanmiao_nei_01.showTextBadge(unaccept
											+ "");
									ll_yanmiao_nei_01.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_01.hiddenBadge();
								}
								if (verifing != 0) {
									ll_yanmiao_nei_02.showTextBadge(verifing
											+ "");
									ll_yanmiao_nei_02.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_02.hiddenBadge();
								}
								if (verifyed != 0) {
									ll_yanmiao_nei_03.showTextBadge(verifyed
											+ "");
									ll_yanmiao_nei_03.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_03.hiddenBadge();
								}
								if (ordered != 0) {
									// ll_yanmiao_nei_04
									// .showTextBadge(ordered + "");
									// ll_yanmiao_nei_04.getBadgeViewHelper()
									// .setBadgeGravity(
									// BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_04.hiddenBadge();
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
						Toast.makeText(BuyerActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private void orderStatusCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/order/statusCount",
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"countMap");
								int unpay = JsonGetInfo.getJsonInt(jsonObject2,
										"unpay");
								int unsend = JsonGetInfo.getJsonInt(
										jsonObject2, "unsend");
								int unreceipt = JsonGetInfo.getJsonInt(
										jsonObject2, "unreceipt");
								int finished = JsonGetInfo.getJsonInt(
										jsonObject2, "finished");
								if (unpay != 0) {
									ll_daifahuo_00.showTextBadge(unpay + "");
									ll_daifahuo_00.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_daifahuo_00.hiddenBadge();
								}
								if (unsend != 0) {
									ll_daifahuo_01.showTextBadge(unsend + "");
									ll_daifahuo_01.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_daifahuo_01.hiddenBadge();
								}
								if (unreceipt != 0) {
									ll_daifahuo_02
											.showTextBadge(unreceipt + "");
									ll_daifahuo_02.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_daifahuo_02.hiddenBadge();
								}
								if (finished != 0) {
									// ll_daifahuo_03.showTextBadge(finished
									// + "");
									// ll_daifahuo_03.getBadgeViewHelper()
									// .setBadgeGravity(
									// BadgeGravity.RightTop);
								} else {
									ll_daifahuo_03.hiddenBadge();
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
						Toast.makeText(BuyerActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private void loadCarstatusCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/loadCar/statusCount", params,
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
								JSONObject jsonObject2 = jsonObject
										.getJSONObject("data").getJSONObject(
												"countMap");
								int loaded = JsonGetInfo.getJsonInt(
										jsonObject2, "loaded");
								int unreceipt = JsonGetInfo.getJsonInt(
										jsonObject2, "unreceipt");
								int receipted = JsonGetInfo.getJsonInt(
										jsonObject2, "receipted");
								if (loaded != 0) {
									ll_loadcar_00.showTextBadge(loaded + "");
									ll_loadcar_00.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_loadcar_00.hiddenBadge();
								}
								if (unreceipt != 0) {
									ll_loadcar_01.showTextBadge(unreceipt + "");
									ll_loadcar_01.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_loadcar_01.hiddenBadge();
								}
								if (receipted != 0) {
									// ll_loadcar_02
									// .showTextBadge(receipted + "");
									// ll_loadcar_02.getBadgeViewHelper()
									// .setBadgeGravity(
									// BadgeGravity.RightTop);
								} else {
									ll_loadcar_02.hiddenBadge();
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
						Toast.makeText(BuyerActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}
	
	
	public void setPics() {
		new ActionSheetDialog(BuyerActivity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("拍照", SheetItemColor.Red,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								boolean requestCamerPermissions = new PermissionUtils(
										BuyerActivity.this)
										.requestCamerPermissions(200);
								if (requestCamerPermissions) {
									SelectHeadTools.startCamearPicCut(
											BuyerActivity.this, photoUri);
								}

							}
						})
				.addSheetItem("从相册选择", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								boolean requestReadSDCardPermissions = new PermissionUtils(
										BuyerActivity.this)
										.requestReadSDCardPermissions(200);
								if (requestReadSDCardPermissions) {
									Crop.pickImage(BuyerActivity.this);
								}

							}
						})
				.addSheetItem("查看大图", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								if (!"".equals(MyApplication.Userinfo
										.getString("headImage", ""))) {
									ArrayList<Pic> ossUrls = new ArrayList<Pic>();
									ossUrls.add(new Pic("", false,
											MyApplication.Userinfo.getString(
													"headImage", ""), 0));
									GalleryImageActivity
											.startGalleryImageActivity(
													BuyerActivity.this, 0, ossUrls);
								} else {
									Toast.makeText(BuyerActivity.this,
											"您还未设置头像，赶紧拍张靓照吧",
											Toast.LENGTH_SHORT).show();
								}

							}
						}).show();
	}

	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	private String imagType_tag = "";
	private LinearLayout ll_jingji;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 6:
			if (resultCode == 6) {
				MainActivity.toA();
			}
			break;
		case Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
			Log.i("onActivityResult", "PHOTO_REQUEST_TAKEPHOTO");
			beginCrop(photoUri);
			break;
		case Configs.SystemPicture.PHOTO_REQUEST_GALLERY:// 相册获取
			if (data != null)
				photoUri = data.getData();
			SelectHeadTools.startPhotoZoom(this, photoUri, 600);
			break;
		case Configs.SystemPicture.PHOTO_REQUEST_CUT: // 接收处理返回的图片结果，这个过程比较重要
			Log.i("onActivityResult", "PHOTO_REQUEST_CUT");
			if (photoUri == null)
				break;
			/*
			 * Bitmap bit = data.getExtras().getParcelable("data");
			 * //不要再用data的方式了，会出现activity result 的时候data == null的空的情况
			 * iv_show.setImageBitmap(bit);
			 */
			try {
				cropBitmap = getBitmapFromUri(photoUri, this); // 通过获取uri的方式，直接解决了报空和图片像素高的oom问题
				if (cropBitmap != null) {
					iv_icon_persion_pic.setImageBitmap(cropBitmap);
					Log.i("onActivityResult", "setImageBitmap");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// 下面可以用来上传pc服务端
			File file = FileTools.getFileByUri(this, photoUri);
			Log.d("File", file.toString());
			break;
		}
		if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
			beginCrop(data.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}
	}

	public Bitmap getBitmapFromUri(Uri uri, Context mContext) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					mContext.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
		Crop.of(source, destination).asSquare().start(this);
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			// iv_icon_persion_pic.setImageDrawable(null);
			// iv_icon_persion_pic.setImageURI(Crop.getOutput(result));
			File file = FileTools.getFileByUri(this, Crop.getOutput(result));
			iv_icon_persion_pic.setImageBitmap(getBitmapFromUri(
					Crop.getOutput(result), BuyerActivity.this));
			ImageLoader.getInstance().displayImage(file.getAbsolutePath(),
					iv_icon_persion_pic);
			updateImage(file.getAbsolutePath(), imagType_tag, "");
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = CacheUtils
				.getCacheDirectory(BuyerActivity.this, true, "pic")
				+ dateTime
				+ "_11";
		File file = new File(files);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public void updateImage(String url, String imagType, String sourceId) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		int rotate = PictureManageUtil.getCameraPhotoOrientation(url);
		// 把压缩的图片进行旋转
		params.put(
				"headImage",
				new ByteArrayInputStream(Bitmap2Bytes(PictureManageUtil
						.rotateBitmap(PictureManageUtil.getCompressBm(url),
								rotate))), System.currentTimeMillis() + ".png");

		finalHttp.post(GetServerUrl.getUrl() + "admin/file/uploadHeadImage",
				params, new AjaxCallBack<Object>() {

					@Override
					public void onStart() {

						if (loading != null && !BuyerActivity.this.isFinishing()) {
							loading.showToastAlong();
						} else if (loading == null
								&& !BuyerActivity.this.isFinishing()) {
							loading = new Loading(BuyerActivity.this, "头像修改中.....");
							loading.showToastAlong();
						}
						super.onStart();
					}

					@Override
					public void onSuccess(Object t) {
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String msg = jsonObject.getString("msg");
							int code = jsonObject.getInt("code");
							if (code == 1) {
								JSONObject data = JsonGetInfo.getJSONObject(
										jsonObject, "data");
								String headImage = JsonGetInfo.getJsonString(
										data, "headImage");
								e.putString("headImage", headImage);
								e.commit();
								if(!"".equals(MyApplication.Userinfo.getString("headImage", ""))){
									ImageLoader.getInstance().displayImage(
											MyApplication.Userinfo.getString("headImage", ""),
											iv_icon_persion_pic);
								}

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (loading != null && !BuyerActivity.this.isFinishing()) {
							loading.cancel();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						if (loading != null && !BuyerActivity.this.isFinishing()) {
							loading.cancel();
						}
						super.onFailure(t, errorNo, strMsg);
					}
				});
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

}
