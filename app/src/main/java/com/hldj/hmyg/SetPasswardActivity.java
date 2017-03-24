package com.hldj.hmyg;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.loginjudge.LoginJudge;

public class SetPasswardActivity extends NeedSwipeBackActivity {

	/**
	 */
	private ImageView btn_back;
	private TextView set_passward;
	private EditText et_old_passward;
	private EditText et_passward;
	private Editor e;
	private ImageButton btn_clear_password;
	private ImageButton btn_clear_num;
	private String istab_c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_passward);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		set_passward = (TextView) findViewById(R.id.set_passward);
		et_old_passward = (EditText) findViewById(R.id.et_old_passward);
		et_passward = (EditText) findViewById(R.id.et_passward);
		btn_clear_num = (ImageButton) findViewById(R.id.btn_clear_num);
		btn_clear_password = (ImageButton) findViewById(R.id.btn_clear_password);

		e = MyApplication.Userinfo.edit();

		et_old_passward.addTextChangedListener(watcher_num);
		et_passward.addTextChangedListener(watcher_password);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_clear_num.setOnClickListener(multipleClickProcess);
		btn_clear_password.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
		set_passward.setOnClickListener(multipleClickProcess);
	}

	TextWatcher watcher_num = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() > 0) {
				btn_clear_num.setVisibility(View.VISIBLE);
				if (et_old_passward.getText().toString().length() > 5
						&& et_passward.getText().toString().length() > 5) {
					set_passward.setEnabled(true);
					set_passward.setTextColor(getResources().getColor(
							R.color.white));
				}
			} else {
				btn_clear_num.setVisibility(View.GONE);
				set_passward.setEnabled(false);
				set_passward.setTextColor(getResources().getColor(
						R.color.main_color));

			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};
	TextWatcher watcher_password = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() > 0) {
				btn_clear_password.setVisibility(View.VISIBLE);
				if (et_old_passward.getText().toString().length() > 5
						&& et_passward.getText().toString().length() > 5) {
					set_passward.setEnabled(true);
					set_passward.setTextColor(getResources().getColor(
							R.color.white));
				}
			} else {
				btn_clear_password.setVisibility(View.GONE);
				set_passward.setEnabled(false);
				set_passward.setTextColor(getResources().getColor(
						R.color.main_color));
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onBackPressed() {
		if (LoginJudge.isTabc) {
			LoginJudge.isTabc = false;
			startActivity(new Intent(SetPasswardActivity.this,
					MainActivity.class));
			finish();
		}
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
				case R.id.btn_clear_num:
					et_old_passward.setText("");
					break;
				case R.id.btn_clear_password:
					et_passward.setText("");
					break;
				case R.id.set_passward:
					SetPassward();
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

	private void SetPassward() {

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("password", et_passward.getText().toString());
		params.put("newPassword", et_old_passward.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/user/savePwd", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = jsonObject.getString("code");
							String msg = jsonObject.getString("msg");
							if (!"".equals(msg)) {
								Toast.makeText(SetPasswardActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								onBackPressed();
							} else if ("1003".equals(code)) {
								// 密码错误
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}
				});

	}

}
