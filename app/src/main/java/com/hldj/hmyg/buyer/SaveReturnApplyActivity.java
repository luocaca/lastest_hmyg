package com.hldj.hmyg.buyer;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class SaveReturnApplyActivity extends NeedSwipeBackActivity {

	/**
	 */
	private ImageView btn_back;
	private TextView feedback;
	private String sourceId = "";
	private com.hy.utils.ContainsEmojiEditText et_feedback;
	private com.hy.utils.ContainsEmojiEditText et_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_return);
		if (getIntent().getStringExtra("sourceId") != null) {
			sourceId = getIntent().getStringExtra("sourceId");
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		btn_back = (ImageView) findViewById(R.id.btn_back);
		feedback = (TextView) findViewById(R.id.feedback);
		et_feedback = (com.hy.utils.ContainsEmojiEditText) findViewById(R.id.et_feedback);
		et_title = (com.hy.utils.ContainsEmojiEditText) findViewById(R.id.et_title);
		et_feedback.addTextChangedListener(watcher_num);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		feedback.setOnClickListener(multipleClickProcess);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	TextWatcher watcher_num = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (s.length() > 0) {
				if (et_feedback.getText().toString().length() > 1) {
					feedback.setEnabled(true);
					feedback.setTextColor(getResources()
							.getColor(R.color.white));
				}
			} else {
				feedback.setEnabled(false);
				feedback.setTextColor(getResources().getColor(
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
				case R.id.feedback:
					// 判断是否为空
					if ("".equals(et_feedback.getText().toString())) {
						Toast.makeText(SaveReturnApplyActivity.this,
								"以上内容都需要填写！请检查...", Toast.LENGTH_SHORT).show();
						return;
					}

					saveReturnApply(sourceId, et_feedback.getText().toString());

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

	public void saveReturnApply(String orderIds, String content) {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("sourceId", orderIds);
		params.put("content", content);
		finalHttp.post(GetServerUrl.getUrl()
				+ "admin/buyer/loadCar/returnApply", params,
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
								Toast.makeText(SaveReturnApplyActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								setResult(1);
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
						Toast.makeText(SaveReturnApplyActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

}
