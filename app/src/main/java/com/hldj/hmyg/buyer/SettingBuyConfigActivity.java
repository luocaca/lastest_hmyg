package com.hldj.hmyg.buyer;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.mhao.widget.SlipButton;
import me.mhao.widget.SlipButton.OnChangedListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.LoginActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.WebActivity;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.white.utils.DataCleanManager;

public class SettingBuyConfigActivity extends NeedSwipeBackActivity implements
		OnChangedListener {
	private Editor e;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_buy_config);
		e = MyApplication.Userinfo.edit();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		SlipButton cashOnDelivery = (SlipButton) findViewById(R.id.cashOnDelivery);
		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
			// 是不是直属经济
			cashOnDelivery.setCheck(false);
			cashOnDelivery.setClickable(false);
		} else {
			if (MyApplication.Userinfo.getBoolean("isPartners", false)) {
				cashOnDelivery.setCheck(true);
				cashOnDelivery.setClickable(false);
			} else {
				cashOnDelivery.setCheck(MyApplication.Userinfo.getBoolean(
						"cashOnDelivery", true));
				cashOnDelivery.setOnChangedListener(this);
			}
		}
		btn_back.setOnClickListener(multipleClickProcess);

	}

	@Override
	public void onChanged(boolean checkState, View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cashOnDelivery:
			cashOnDelivery(checkState);
			break;

		default:
			break;
		}

	}

	private void cashOnDelivery(final boolean checkState) {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("cashOnDelivery", "" + checkState);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/user/buyer/saveBuyerConfig", params,
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
								e.putBoolean("cashOnDelivery", checkState);
								e.commit();
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
						Toast.makeText(SettingBuyConfigActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

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
					onBackPressed();
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

}
