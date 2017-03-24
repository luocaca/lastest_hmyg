package com.hldj.hmyg;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class WeituoActivity extends EActivity {
	private WebView webview;
	private TextView tv_loading;
	private ProgressBar bar;
	private String url = "";
	private String title = "";
	private BaseAnimatorSet mBasIn;
	private BaseAnimatorSet mBasOut;

	public void setBasIn(BaseAnimatorSet bas_in) {
		this.mBasIn = bas_in;
	}

	public void setBasOut(BaseAnimatorSet bas_out) {
		this.mBasOut = bas_out;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weituo);

		mBasIn = new BounceTopEnter();
		mBasOut = new SlideBottomExit();
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		TextView noti = (TextView) findViewById(R.id.noti);
		TextView contact = (TextView) findViewById(R.id.contact);

		tv_title.setText(title);
		btn_back.setOnClickListener(multipleClickProcess);
		noti.setOnClickListener(multipleClickProcess);
		contact.setOnClickListener(multipleClickProcess);
		webview = (WebView) findViewById(R.id.webview);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		bar = (ProgressBar) findViewById(R.id.progressBar);
		// webview.setHorizontalScrollBarEnabled(false);// 水平不显示
		// webview.setVerticalScrollBarEnabled(false); // 垂直不显示
		WebSettings s = webview.getSettings();
		s.setBuiltInZoomControls(true);
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);

		// enable navigator.geolocation
		s.setGeolocationEnabled(true);
		s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

		// enable Web Storage: localStorage, sessionStorage
		s.setDomStorageEnabled(true);
		webview.setWebViewClient(new MyWebViewClient());
		webview.setWebChromeClient(new MyWebChromeClient());
		webview.scrollTo(0, 0);
		if (url.startsWith("http")) {
			webview.loadUrl(url);
		}

	}

	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 看不到console.log和alert
	class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Toast.makeText(WeituoActivity.this, message, Toast.LENGTH_SHORT)
					.show();
			return true;
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(final WebView view, String url) {
			bar.setVisibility(View.INVISIBLE);
			tv_loading.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			bar.setVisibility(View.VISIBLE);
			tv_loading.setVisibility(View.VISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return super.shouldOverrideUrlLoading(view, url);
		}

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
					if (webview.canGoBack()) {
						webview.goBack(); // goBack()表示返回WebView的上一页面
					} else {
						onBackPressed();
					}

					break;
				case R.id.noti:
					if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
						Intent toLoginActivity = new Intent(
								WeituoActivity.this, LoginActivity.class);
						startActivityForResult(toLoginActivity, 4);
						overridePendingTransition(
								R.anim.slide_in_left, R.anim.slide_out_right);
						return;
					}
					entrustApply();
					break;
				case R.id.contact:
					Call_Phone();
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

	// 显示是否要更新的对话框
	private void showUpdateDialog(String title, String content) {

		final com.flyco.dialog.widget.MaterialDialog dialog = new com.flyco.dialog.widget.MaterialDialog(
				WeituoActivity.this);
		dialog.title(title)
				.content(content)
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

					}
				});

	}

	public void entrustApply() {
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		finalHttp.post(GetServerUrl.getUrl() + "admin/notice/entrustApply",
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
								// Toast.makeText(WeituoActivity.this, msg,
								// Toast.LENGTH_SHORT).show();
								showUpdateDialog("提示",
										"已通知客服请您耐心等待，客服会第一时间回复您！");
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
						Toast.makeText(WeituoActivity.this, R.string.error_net,
								Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});
	}

}
