package com.hldj.hmyg;

import java.util.HashMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.buyer.SavePurchaseActivity;
import com.hldj.hmyg.saler.SaveSeedlingActivity;

public class CActivity extends Activity implements OnClickListener {
	private WebView webview;
	private TextView tv_loading;
	private ProgressBar bar;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c);
		TextView tv_cactivity_public_sale = (TextView) findViewById(R.id.tv_cactivity_public_sale);
		TextView tv_cactivity_public_buy = (TextView) findViewById(R.id.tv_cactivity_public_buy);
		tv_cactivity_public_sale.setOnClickListener(this);
		tv_cactivity_public_buy.setOnClickListener(this);
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
		// GetHtml();
	}

	private void GetHtml() {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		HashMap<String, String> pHashMap = new HashMap<String, String>();

		finalHttp.post(Data.GetHtml, params, new AjaxCallBack<Object>() {

			private String url;

			@Override
			public void onSuccess(Object t) {
				try {
					JSONObject jsonObject = new JSONObject(t.toString());
					String code = jsonObject.getString("code");
					String msg = jsonObject.getString("msg");
					url = jsonObject.getString("url");
					if (!"".equals(msg)) {
					}
					if ("ok".equals(code)) {
						if (url.startsWith("http")) {
							webview.loadUrl(url);
						}
					} else if ("error".equals(code)) {
						if (Data.proxyAssureDesc.startsWith("http")) {
							webview.loadUrl(Data.proxyAssureDesc);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO
				// Auto-generated
				// method stub
				Toast.makeText(CActivity.this, R.string.error_net,
						Toast.LENGTH_SHORT).show();
				super.onFailure(t, errorNo, strMsg);
			}

		});
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
			Toast.makeText(CActivity.this, message, Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (MyApplication.Userinfo.getBoolean("isLogin", false) == false) {
			Intent toLoginActivity = new Intent(CActivity.this,
					LoginActivity.class);
			startActivityForResult(toLoginActivity, 4);
			getParent().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			return;
		}
		switch (v.getId()) {

		case R.id.tv_cactivity_public_sale:
			// if ("".equals(MyApplication.Userinfo.getString("storeId", ""))) {
			// Intent toStoreSettingActivity = new Intent(CActivity.this,
			// StoreSettingActivity.class);
			// startActivity(toStoreSettingActivity);
			// getParent().overridePendingTransition(R.anim.slide_in_left,
			// R.anim.slide_out_right);
			// return;
			// }
			Intent toPublicCaigouActivity = new Intent(CActivity.this,
					SaveSeedlingActivity.class);
			startActivity(toPublicCaigouActivity);
			getParent().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;
		case R.id.tv_cactivity_public_buy:
			// if ("".equals(MyApplication.Userinfo.getString("storeId", ""))) {
			// Intent toStoreSettingActivity = new Intent(CActivity.this,
			// StoreSettingActivity.class);
			// startActivity(toStoreSettingActivity);
			// getParent().overridePendingTransition(R.anim.slide_in_left,
			// R.anim.slide_out_right);
			// return;
			// }
			
			Intent toSavePurchaseActivity = new Intent(CActivity.this,
					SavePurchaseActivity.class);
			startActivity(toSavePurchaseActivity);
			getParent().overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			break;

		default:
			break;
		}
	}

}
