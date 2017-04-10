package com.hldj.hmyg;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.BrokerYanmiaoOrderActivity;
import com.hldj.hmyg.buyer.PurchaseDemandConsumerListActivity;
import com.hldj.hmyg.buyer.PurchaseDemandListActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeViewHelper.BadgeGravity;
import de.hdodenhof.circleimageview.CircleImageView;

public class BrokerActivity extends LoginActivity {

	private BGABadgeLinearLayout ll_yanmiao_nei_01;
	private BGABadgeLinearLayout ll_yanmiao_nei_02;
	private BGABadgeLinearLayout ll_yanmiao_nei_03;
	private BGABadgeLinearLayout ll_yanmiao_nei_04;
	private BGABadgeLinearLayout ll_yanmiao_nei_05;
	private BGABadgeLinearLayout ll_daifahuo_01;
	private BGABadgeLinearLayout ll_daifahuo_02;
	private BGABadgeLinearLayout ll_daifahuo_03;
	private ImageView iv_msg;
	private FinalBitmap fb;
	private CircleImageView iv_icon_persion_pic;
	private TextView tv_user_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broker);
		fb = FinalBitmap.create(this);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		iv_msg = (ImageView) findViewById(R.id.iv_a_msg);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		iv_icon_persion_pic = (CircleImageView) findViewById(R.id.iv_icon_persion_pic);
		LinearLayout ll_all_yanmiao_order = (LinearLayout) findViewById(R.id.ll_all_yanmiao_order);
		ll_yanmiao_nei_01 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_01);
		ll_yanmiao_nei_02 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_02);
		ll_yanmiao_nei_03 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_03);
		ll_yanmiao_nei_04 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_04);
		ll_yanmiao_nei_05 = (BGABadgeLinearLayout) findViewById(R.id.ll_yanmiao_nei_05);
		ll_daifahuo_01 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_01);
		ll_daifahuo_02 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_02);
		ll_daifahuo_03 = (BGABadgeLinearLayout) findViewById(R.id.ll_daifahuo_03);
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

		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);

		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		iv_msg.setOnClickListener(multipleClickProcess);
		ll_all_order.setOnClickListener(multipleClickProcess);
		ll_daifukuan.setOnClickListener(multipleClickProcess);
		ll_daifahuo.setOnClickListener(multipleClickProcess);
		ll_daishouhuo.setOnClickListener(multipleClickProcess);
		ll_yiwancheng.setOnClickListener(multipleClickProcess);
		// ll_all_yanmiao_order.setOnClickListener(multipleClickProcess);
		ll_yanmiao_01.setOnClickListener(multipleClickProcess);
		ll_yanmiao_02.setOnClickListener(multipleClickProcess);
		ll_yanmiao_03.setOnClickListener(multipleClickProcess);
		ll_yanmiao_04.setOnClickListener(multipleClickProcess);
		ll_yanmiao_05.setOnClickListener(multipleClickProcess);
		if (MyApplication.Userinfo.getBoolean("isClerk", false)) {
			ll_01.setOnClickListener(multipleClickProcess);
			ll_02.setOnClickListener(multipleClickProcess);
		} else {
			ll_01.setVisibility(View.INVISIBLE);
			ll_02.setVisibility(View.INVISIBLE);
		}
		iv_msg.setBackgroundResource(R.drawable.icon_msg);
		unReadCount();
		statusCount();
		deliverystatusCount();

	}

	private void unReadCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
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

	private void deliverystatusCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/agent/delivery/statusCount", params,
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
								int unreceipt = JsonGetInfo.getJsonInt(
										jsonObject2, "unreceipt");
								int finished = JsonGetInfo.getJsonInt(
										jsonObject2, "finished");
								int unsend = JsonGetInfo.getJsonInt(
										jsonObject2, "unsend");
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
						Toast.makeText(BrokerActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private void statusCount() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/agent/validateApply/statusCount", params,
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
								int accepted = JsonGetInfo.getJsonInt(
										jsonObject2, "accepted");
								int backed = JsonGetInfo.getJsonInt(
										jsonObject2, "backed");
								int finished = JsonGetInfo.getJsonInt(
										jsonObject2, "finished");
								int auditing = JsonGetInfo.getJsonInt(
										jsonObject2, "auditing");
								int validating = JsonGetInfo.getJsonInt(
										jsonObject2, "validating");
								if (accepted != 0) {
									ll_yanmiao_nei_01.showTextBadge(accepted
											+ "");
									ll_yanmiao_nei_01.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_01.hiddenBadge();
								}
								if (validating != 0) {
									ll_yanmiao_nei_02.showTextBadge(validating
											+ "");
									ll_yanmiao_nei_02.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_02.hiddenBadge();
								}
								if (auditing != 0) {
									ll_yanmiao_nei_03.showTextBadge(auditing
											+ "");
									ll_yanmiao_nei_03.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_03.hiddenBadge();
								}
								if (backed != 0) {
									ll_yanmiao_nei_04
											.showTextBadge(backed + "");
									ll_yanmiao_nei_04.getBadgeViewHelper()
											.setBadgeGravity(
													BadgeGravity.RightTop);
								} else {
									ll_yanmiao_nei_04.hiddenBadge();
								}
								if (finished != 0) {
									// ll_yanmiao_nei_05.showTextBadge(finished
									// + "");
									// ll_yanmiao_nei_05.getBadgeViewHelper()
									// .setBadgeGravity(
									// BadgeGravity.RightTop);
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
						Toast.makeText(BrokerActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (iv_msg != null) {
			unReadCount();
			statusCount();
			deliverystatusCount();
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

	public class MultipleClickProcess implements OnClickListener {

		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.iv_a_msg:
				Intent toMessageListDetailActivity = new Intent(
						BrokerActivity.this, MessageListActivity.class);
				startActivity(toMessageListDetailActivity);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_all_order:

				break;
			case R.id.ll_daifukuan:

				break;
			case R.id.ll_daifahuo:
				Intent toBrokerOrderListActivity2 = new Intent(
						BrokerActivity.this, BrokerOrderListActivity.class);
				toBrokerOrderListActivity2.putExtra("status", "unsend");
				startActivity(toBrokerOrderListActivity2);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);

				break;
			case R.id.ll_daishouhuo:
				Intent toBrokerOrderListActivity3 = new Intent(
						BrokerActivity.this, BrokerOrderListActivity.class);
				toBrokerOrderListActivity3.putExtra("status", "unreceipt");
				startActivity(toBrokerOrderListActivity3);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_yiwancheng:
				Intent toBrokerOrderListActivity4 = new Intent(
						BrokerActivity.this, BrokerOrderListActivity.class);
				toBrokerOrderListActivity4.putExtra("status", "finished");
				startActivity(toBrokerOrderListActivity4);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);

				break;
			case R.id.ll_all_yanmiao_order:
				Intent toBrokerActivity = new Intent(BrokerActivity.this,
						BrokerListActivity.class);
				toBrokerActivity.putExtra("acceptStatus", "");
				toBrokerActivity.putExtra("loadItems", true);
				startActivity(toBrokerActivity);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_yanmiao_01:
				Intent toBrokerActivity0 = new Intent(BrokerActivity.this,
						BrokerListActivity.class);
				toBrokerActivity0.putExtra("acceptStatus", "accepted");
				toBrokerActivity0.putExtra("loadItems", true);
				startActivity(toBrokerActivity0);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_yanmiao_02:
				// Intent toBrokerActivity1 = new Intent(BrokerActivity.this,
				// BrokerListActivity.class);
				// toBrokerActivity1.putExtra("acceptStatus", "validating");
				// toBrokerActivity1.putExtra("loadItems", true);
				// startActivity(toBrokerActivity1);
				// overridePendingTransition(R.anim.slide_in_left,
				// R.anim.slide_out_right);

				Intent toBrokerActivity1 = new Intent(BrokerActivity.this,
						BrokerYanmiaoOrderActivity.class);
				toBrokerActivity1.putExtra("acceptStatus", "validating");
				toBrokerActivity1.putExtra("loadItems", true);
				startActivity(toBrokerActivity1);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_yanmiao_03:
				Intent toBrokerActivity2 = new Intent(BrokerActivity.this,
						BrokerListActivity.class);
				toBrokerActivity2.putExtra("acceptStatus", "auditing");
				toBrokerActivity2.putExtra("loadItems", true);
				startActivity(toBrokerActivity2);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_yanmiao_04:
				Intent toBrokerActivity3 = new Intent(BrokerActivity.this,
						BrokerListActivity.class);
				toBrokerActivity3.putExtra("acceptStatus", "backed");
				toBrokerActivity3.putExtra("loadItems", true);
				startActivity(toBrokerActivity3);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_yanmiao_05:
				Intent toBrokerActivity4 = new Intent(BrokerActivity.this,
						BrokerListActivity.class);
				toBrokerActivity4.putExtra("acceptStatus", "finished");
				toBrokerActivity4.putExtra("loadItems", true);
				startActivity(toBrokerActivity4);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_01:

				Intent toPurchaseDemandListActivity = new Intent(
						BrokerActivity.this, PurchaseDemandListActivity.class);
				toPurchaseDemandListActivity.putExtra("tag", "clerk");
				startActivityForResult(toPurchaseDemandListActivity, 6);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			case R.id.ll_02:

				Intent toPurchaseDemandConsumerListActivity = new Intent(
						BrokerActivity.this,
						PurchaseDemandConsumerListActivity.class);
				startActivityForResult(toPurchaseDemandConsumerListActivity, 6);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			default:
				break;
			}
		}
	}

}
