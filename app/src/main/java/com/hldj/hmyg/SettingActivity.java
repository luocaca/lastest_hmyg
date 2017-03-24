package com.hldj.hmyg;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import me.mhao.widget.SlipButton;
import me.mhao.widget.SlipButton.OnChangedListener;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.white.update.UpdateInfo;
import com.white.utils.DataCleanManager;
import com.white.utils.SettingUtils;

public class SettingActivity extends NeedSwipeBackActivity implements
		OnChangedListener {
	private Editor e;
	private TextView tv_ache;
	private UpdateInfo updateInfo;

	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;

	// 更新版本要用到的一些信息
	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
		e = MyApplication.Userinfo.edit();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_ache = (TextView) findViewById(R.id.tv_ache);
		tv_version = (TextView) findViewById(R.id.tv_version);
		try {
			tv_ache.setText(com.hldj.hmyg.DataCleanManager
					.getTotalCacheSize(SettingActivity.this));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView tv_out = (TextView) findViewById(R.id.tv_out);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		newMsgAlertStatusOnOff = (SlipButton) findViewById(R.id.newMsgAlertStatusOnOff);
		SlipButton receiptMsg = (SlipButton) findViewById(R.id.receiptMsg);
		newMsgAlertStatusOnOff.setCheck(MyApplication.Userinfo.getBoolean(
				"notification", false));
		receiptMsg.setCheck(MyApplication.Userinfo.getBoolean("receiptMsg",
				true));
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			tv_version.setText(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		btn_back.setOnClickListener(multipleClickProcess);
		tv_out.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		newMsgAlertStatusOnOff.setOnChangedListener(this);
		receiptMsg.setOnChangedListener(this);
	}

	@Override
	public void onChanged(boolean checkState, View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.newMsgAlertStatusOnOff:
			e.putBoolean("notification", checkState);
			e.putBoolean("NeedShowfangxin", true);
			e.putBoolean("NeedShowbangwo", true);
			e.putBoolean("NeedShowdanbao", true);
			e.commit();
			if (checkState) {
				JPushInterface.resumePush(getApplicationContext());
			} else {
				JPushInterface.stopPush(getApplicationContext());
			}
			break;
		case R.id.receiptMsg:
			receiptMsg(checkState);
			break;

		default:
			break;
		}

	}

	public UpdateInfo getUpDateInfo4Pgyer() {

		updateInfo = new UpdateInfo();
		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("aId", GetServerUrl.getaId());
		params.put("_api_key", GetServerUrl.get_api_key());
		params.put("uKey", GetServerUrl.getuKey());
		fh.post(GetServerUrl.getPGYER(), params, new AjaxCallBack<Object>() {

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				JSONObject Jo;
				try {
					Jo = new JSONObject(t.toString());
					JSONArray data = JsonGetInfo.getJsonArray(Jo, "data");
					if (data.length() > 0) {
						JSONObject jsonObject = data.getJSONObject(data
								.length() - 1);
						String changelog = JsonGetInfo.getJsonString(
								jsonObject, "appUpdateDescription");
						String versionShort = JsonGetInfo.getJsonString(
								jsonObject, "appVersion");
						String install_url = GetServerUrl.getPGYER_UPLOAD();
						String new_version = JsonGetInfo.getJsonString(
								jsonObject, "appVersionNo");
						updateInfo.setVersion(versionShort);
						updateInfo.setDescription(changelog);
						updateInfo.setUrl(install_url);
						// 获取当前版本号
						String now_version = "";
						try {
							PackageManager packageManager = getPackageManager();
							PackageInfo packageInfo = packageManager
									.getPackageInfo(getPackageName(), 0);
							now_version = packageInfo.versionName;
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
						if (!versionShort.equals(now_version)) {
							handler1.sendEmptyMessage(0);
						} else {
							handler1.sendEmptyMessage(1);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.onSuccess(t);
			}

		});
		return updateInfo;
	}

	public UpdateInfo getUpDateInfo() {

		updateInfo = new UpdateInfo();
		FinalHttp fh = new FinalHttp();
		fh.get(GetServerUrl.getFIR(), new AjaxCallBack<Object>() {

			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(t.toString());
					String changelog = JsonGetInfo.getJsonString(jsonObject,
							"changelog");
					String versionShort = JsonGetInfo.getJsonString(jsonObject,
							"versionShort");
					String install_url = JsonGetInfo.getJsonString(jsonObject,
							"install_url");
					String new_version = JsonGetInfo.getJsonString(jsonObject,
							"version");
					updateInfo.setVersion(versionShort);
					updateInfo.setDescription(changelog);
					updateInfo.setUrl(install_url);
					// 获取当前版本号
					String now_version = "";
					try {
						PackageManager packageManager = getPackageManager();
						PackageInfo packageInfo = packageManager
								.getPackageInfo(getPackageName(), 0);
						now_version = packageInfo.versionName;
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					if (!versionShort.equals(now_version)) {
						handler1.sendEmptyMessage(0);
					} else {
						handler1.sendEmptyMessage(1);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.onSuccess(t);
			}

		});
		return updateInfo;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示

			if (msg.what == 0) {
				showUpdateDialog();
			} else if (msg.what == 1) {
				Toast toast = Toast.makeText(SettingActivity.this,
						R.string.the_version_is_new, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		};
	};
	private TextView tv_version;
	private SlipButton newMsgAlertStatusOnOff;

	// 显示是否要更新的对话框
	private void showUpdateDialog() {

		final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
				SettingActivity.this);
		dialog.title(
				getResources().getString(R.string.please_update_app_to_version)
						+ updateInfo.getVersion())
				.content(updateInfo.getDescription())
				//
				.btnText(getResources().getString(R.string.cancle),
						getResources().getString(R.string.ok))//
				.showAnim(mBasIn)//
				.dismissAnim(mBasOut)//
				.show();

		dialog.setOnBtnClickL(new OnBtnClickL() {// left btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				}, new OnBtnClickL() {// right btn click listener
					@Override
					public void onBtnClick() {
						dialog.dismiss();
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							// downFile(updateInfo.getUrl());
							SettingUtils.launchBrowser(SettingActivity.this,
									updateInfo.getUrl());
						} else {
							Toast.makeText(SettingActivity.this,
									R.string.sd_card_is_disable,
									Toast.LENGTH_SHORT).show();
						}

					}
				});

	}

	private void receiptMsg(final boolean checkState) {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("receiptMsg", "" + checkState);
		finalHttp.post(GetServerUrl.getUrl() + "admin/user/saveReceiptMsg",
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
								e.putBoolean("receiptMsg", checkState);
								e.commit();
							} else {

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						super.onSuccess(t);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(SettingActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public UpdateInfo getVersion() {

		updateInfo = new UpdateInfo();
		FinalHttp fh = new FinalHttp();
		GetServerUrl.addHeaders(fh,true);
		AjaxParams params = new AjaxParams();
		params.put("appType", "Android");
		fh.post(GetServerUrl.getUrl() + "version/getVersion", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(t.toString());
							JSONObject data = JsonGetInfo.getJSONObject(
									jsonObject, "data");
							JSONObject version = JsonGetInfo.getJSONObject(
									data, "version");
							String changelog = JsonGetInfo.getJsonString(
									version, "remarks");
							String versionNum = JsonGetInfo.getJsonString(
									version, "versionNum");
							String url = JsonGetInfo.getJsonString(version,
									"url");
							String new_version = JsonGetInfo.getJsonString(
									version, "versionNum");
							boolean isForce = JsonGetInfo.getJsonBoolean(data,
									"isForce");
							updateInfo.setVersion(versionNum);
							updateInfo.setDescription(changelog);
							updateInfo.setUrl(url);
							// updateInfo.setUrl(GetServerUrl.getPGYER_UPLOAD());
							updateInfo.setForce(isForce);
							// 获取当前版本号
							String now_version = "";
							try {
								PackageManager packageManager = getPackageManager();
								PackageInfo packageInfo = packageManager
										.getPackageInfo(getPackageName(), 0);
								now_version = packageInfo.versionName;
							} catch (NameNotFoundException e) {
								e.printStackTrace();
							}
							if (!versionNum.equals(now_version)) {
								handler1.sendEmptyMessage(0);
							} else {
								handler1.sendEmptyMessage(1);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.onSuccess(t);
					}

				});
		return updateInfo;
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
				case R.id.ll_02:
					// DataCleanManager.cleanInternalCache(SettingActivity.this);
					com.hldj.hmyg.DataCleanManager
							.clearAllCache(SettingActivity.this);
					tv_ache.setText("0KB");
					break;
				case R.id.ll_03:
					Intent toWebActivity3 = new Intent(SettingActivity.this,
							WebActivity.class);
					toWebActivity3.putExtra("title", "帮助中心");
					toWebActivity3.putExtra("url", Data.helpIndex);
					startActivityForResult(toWebActivity3, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_04:
					Intent toWebActivity4 = new Intent(SettingActivity.this,
							WebActivity.class);
					toWebActivity4.putExtra("title", "关于我们");
					toWebActivity4.putExtra("url", Data.About);
					startActivityForResult(toWebActivity4, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_05:
					// getUpDateInfo();
					// getUpDateInfo4Pgyer();
					getVersion();
					break;
				case R.id.tv_out:
					e.putBoolean("isLogin", false);
					e.clear(); // 清除所有登录数据
					e.commit();
					e.putBoolean("notification",
							newMsgAlertStatusOnOff.isIsOpen());
					e.commit();
					setResult(6);
					finish();
					MainActivity.toA();
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
