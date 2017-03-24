package com.hldj.hmyg.saler.purchaseconfirmlist;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.hldj.hmyg.MiCControModel;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;

@SuppressLint("JavascriptInterface")
public class ConfirmWebActivity extends NeedSwipeBackActivity {
	private WebView webview;
	private TextView tv_loading;
	private ProgressBar bar;
	private String url = "";
	private String title = "";
	private String id = "";
	private ImageView guanbi;
	private TextView id_tv_edit_all;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_seedling_market);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		if (getIntent().getStringExtra("title") != null) {
			title = getIntent().getStringExtra("title");
		}
		if (getIntent().getStringExtra("url") != null) {
			url = getIntent().getStringExtra("url");
		}
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
		}

		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		guanbi = (ImageView) findViewById(R.id.guanbi);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		id_tv_edit_all.setVisibility(View.GONE);
		tv_title.setText(title);
		btn_back.setOnClickListener(multipleClickProcess);
		guanbi.setOnClickListener(multipleClickProcess);
		webview = (WebView) findViewById(R.id.webview);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		bar = (ProgressBar) findViewById(R.id.progressBar);
		// webview.setHorizontalScrollBarEnabled(false);// 水平不显示
		// webview.setVerticalScrollBarEnabled(false); // 垂直不显示
		WebSettings s = webview.getSettings();
		s.setBuiltInZoomControls(false);
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		s.setUseWideViewPort(false);
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
		MiCControModel miCControModel = new MiCControModel(this);

		webview.addJavascriptInterface(miCControModel, "ProxyBridge");

		miCControModel
				.setOnJsControlListener(new MiCControModel.IJsControllListener() {
					@Override
					public void callback() {

						onBackPressed();
						// runOnUiThread(new Runnable() {
						// @Override
						// public void run() {
						//
						//
						// }
						// });
					}
				});
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
			Toast.makeText(ConfirmWebActivity.this, message,
					Toast.LENGTH_SHORT).show();
			return true;
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(final WebView view, String url) {
			bar.setVisibility(View.INVISIBLE);
			tv_loading.setVisibility(View.INVISIBLE);
			if (webview.canGoBack()) {
				//
				guanbi.setVisibility(View.VISIBLE);
			} else {
				guanbi.setVisibility(View.GONE);
			}
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
						Log.e("webview.canGoBack()", "webview.canGoBack()");
						webview.goBack(); // goBack()表示返回WebView的上一页面
					} else {
						onBackPressed();
					}

					break;
				case R.id.guanbi:
					finish();
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
	
	
	
	  // 注入js函数监听  
    private void addImageClickListner() {  
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去  
        webview.loadUrl("javascript:(function(){" +  
        "ProxyBridge.callback();" +   
        "})()");  
    }  

}
