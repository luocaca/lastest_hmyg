package com.hldj.hmyg;

import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.application.PermissionUtils;
import com.hldj.hmyg.saler.SaveSeedlingActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.white.utils.AndroidUtil;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private ImageView img_sp;
	private FinalBitmap fb;
	private long delayMillis = 3000;
	public Editor e;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fb = FinalBitmap.create(this);
		setContentView(R.layout.activity_splash);
		e = MyApplication.Deviceinfo.edit();
		boolean requestREAD_PHONE_STATE = new PermissionUtils(
				SplashActivity.this).requestREAD_PHONE_STATE(200);
		if (!requestREAD_PHONE_STATE) {
			e.putString("version", "");
			e.putString("deviceId", "");
		} else {
			e.putString("version", AndroidUtil.getVersion(this));
			e.putString("deviceId", AndroidUtil.getDeviceIMEI(this));
		}
		e.commit();
		GetServerUrl.version = AndroidUtil.getVersion(this);
		GetServerUrl.deviceId = AndroidUtil.getDeviceIMEI(this);
		boolean isWear = getPackageManager().hasSystemFeature(
				"android.hardware.type.watch");
		// 设置AppKey
		// StatService.setAppKey("09cd76900b"); //
		// appkey必须在mtj网站上注册生成，该设置建议在AndroidManifest.xml中填写，代码设置容易丢失

		img_sp = (ImageView) findViewById(R.id.img_sp);
		// fb.display(img_sp, GetServerUrl.TEST_IMG);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent toMainActivity = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(toMainActivity);
				overridePendingTransition(R.anim.slide_bottom_in,
						R.anim.slide_bottom_out);
				finish();
			}
		}, delayMillis);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
