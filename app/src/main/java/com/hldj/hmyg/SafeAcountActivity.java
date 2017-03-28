package com.hldj.hmyg;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
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

import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;

/**
 * 账户安全
 */
public class SafeAcountActivity extends NeedSwipeBackActivity {
	private Editor e;
	private TextView tv_phone;
	private String publicPhone = MyApplication.Userinfo.getString(
			"phone", "");
	private TextView tv_has_bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_acount);
		e = MyApplication.Userinfo.edit();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_has_bind = (TextView) findViewById(R.id.tv_has_bind);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		if(!"".equals(publicPhone)){
			tv_has_bind.setText("已绑定手机：");
			tv_phone.setText(publicPhone);
		}
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
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
				case R.id.ll_01:
					Intent toSetProfileActivity = new Intent(
							SafeAcountActivity.this, SetProfileActivity.class);
					startActivity(toSetProfileActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_02:
					Intent toSetPasswardByGetCodeActivity = new Intent(
							SafeAcountActivity.this,
							SetPasswardByGetCodeActivity.class);
					startActivity(toSetPasswardByGetCodeActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_03:
					Intent toSetPhoneByGetCodeActivity = new Intent(
							SafeAcountActivity.this,
							SetPhoneByGetCodeActivity.class);
					startActivity(toSetPhoneByGetCodeActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_04:
					break;
				case R.id.ll_05:
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
