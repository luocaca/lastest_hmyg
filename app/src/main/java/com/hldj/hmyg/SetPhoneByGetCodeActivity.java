package com.hldj.hmyg;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.loginjudge.LoginJudge;

public class SetPhoneByGetCodeActivity extends LoginActivity {

	/**
	 */
	private ImageView btn_back;
	private TextView set_passward;
	private EditText et_phone;
	private EditText et_passward;
	private EditText et_code;
	private Editor e;
	private ImageButton btn_clear_password;
	private ImageButton btn_clear_num;
	private String istab_c;
	private Button btn_get_code;
	private String phString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_phone_by_get_code);
		time = new TimeCount(60000, 1000);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		set_passward = (TextView) findViewById(R.id.set_passward);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_passward = (EditText) findViewById(R.id.et_passward);
		et_code = (EditText) findViewById(R.id.et_code);
		btn_clear_num = (ImageButton) findViewById(R.id.btn_clear_num);
		btn_clear_password = (ImageButton) findViewById(R.id.btn_clear_password);
		btn_get_code = (Button) findViewById(R.id.btn_get_code);

		e = MyApplication.Userinfo.edit();

		et_phone.addTextChangedListener(watcher_num);
		et_passward.addTextChangedListener(watcher_password);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_clear_num.setOnClickListener(multipleClickProcess);
		btn_clear_password.setOnClickListener(multipleClickProcess);
		btn_get_code.setOnClickListener(multipleClickProcess);
		btn_back.setOnClickListener(multipleClickProcess);
		set_passward.setOnClickListener(multipleClickProcess);
	}

	TextWatcher watcher_phone = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() > 0) {
				btn_clear_num.setVisibility(View.VISIBLE);
				if (et_phone.getText().toString().length() == 11
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
	TextWatcher watcher_num = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() > 0) {
				btn_clear_num.setVisibility(View.VISIBLE);
				if (et_phone.getText().toString().length() == 11
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
				if (et_phone.getText().toString().length() == 11
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
	private TimeCount time;

	@Override
	public void onBackPressed() {
		if (LoginJudge.isTabc) {
			LoginJudge.isTabc = false;
			startActivity(new Intent(SetPhoneByGetCodeActivity.this,
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
					et_phone.setText("");
					break;
				case R.id.btn_clear_password:
					et_passward.setText("");
					break;
				case R.id.btn_get_code:
					if (!TextUtils.isEmpty(et_phone.getText().toString())) {
						if (et_phone.getText().toString().length() < 11
								&& !et_phone.getText().toString()
										.startsWith("1")) {
							Toast.makeText(SetPhoneByGetCodeActivity.this,
									"手机格式有问题，请检查后重试", Toast.LENGTH_SHORT)
									.show();
						}
						phString = et_phone.getText().toString();
						time.start();
						Checkphone();
					} else {
						Toast.makeText(SetPhoneByGetCodeActivity.this,
								"电话不能为空", Toast.LENGTH_SHORT).show();
					}

					break;
				case R.id.set_passward:
					// 判断是否为空
					if ("".equals(et_code.getText().toString())
							|| "".equals(et_phone.getText().toString())
							|| "".equals(et_passward.getText().toString())) {
						Toast.makeText(SetPhoneByGetCodeActivity.this,
								"以上内容都需要填写！请检查...", Toast.LENGTH_SHORT).show();
						return;
					}
					savePhone();

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

	private void Checkphone() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("phone", phString);
		finalHttp.post(GetServerUrl.getUrl() + "common/getSmsCode", params,
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
								Toast.makeText(SetPhoneByGetCodeActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								Toast.makeText(SetPhoneByGetCodeActivity.this,
										"验证码已经发送", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(SetPhoneByGetCodeActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

	private void savePhone() {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("phone", phString);
		params.put("password", et_passward.getText().toString());
		params.put("smsCode", et_code.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/user/savePhone", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							String code = jsonObject.getString("code");
							String msg = jsonObject.getString("msg");
							if (!"".equals(msg)) {
								Toast.makeText(SetPhoneByGetCodeActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								e.putString("phone", phString);
								onBackPressed();
							} else if ("1006".equals(code)) {
								// 已注册
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(t);
					}
				});

	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_get_code.setClickable(false);
			btn_get_code.setText(millisUntilFinished / 1000 + "S");
		}

		@Override
		public void onFinish() {
			btn_get_code.setText("重新获取");
			btn_get_code.setClickable(true);
		}
	}

}
