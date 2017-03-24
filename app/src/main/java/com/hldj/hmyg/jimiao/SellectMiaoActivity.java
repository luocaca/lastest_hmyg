package com.hldj.hmyg.jimiao;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.yangfuhai.asimplecachedemo.lib.ACache;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class SellectMiaoActivity extends Activity {
	private EditText et_min_guige;
	private EditText et_max_guige;
	private EditText et_pinming;
	private EditText et_minCrown;
	private EditText et_maxCrown;
	private EditText et_minHeight;
	private EditText et_maxHeight;
	private String minSpec = "";
	private String maxSpec = "";
	private String minHeight = "";
	private String maxHeight = "";
	private String minCrown = "";
	private String maxCrown = "";
	private String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellect_miao);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView iv_reset = (TextView) findViewById(R.id.iv_reset);
		et_pinming = (EditText) findViewById(R.id.et_pinming);
		et_min_guige = (EditText) findViewById(R.id.et_min_guige);
		et_max_guige = (EditText) findViewById(R.id.et_max_guige);
		et_minCrown = (EditText) findViewById(R.id.et_minCrown);
		et_maxCrown = (EditText) findViewById(R.id.et_maxCrown);
		et_minHeight = (EditText) findViewById(R.id.et_minHeight);
		et_maxHeight = (EditText) findViewById(R.id.et_maxHeight);
		initData();
		TextView sure = (TextView) findViewById(R.id.sure);
		btn_back.setOnClickListener(multipleClickProcess);
		iv_reset.setOnClickListener(multipleClickProcess);
		sure.setOnClickListener(multipleClickProcess);
	}

	public void initData() {
		minSpec = getIntent().getStringExtra("minSpec");
		maxSpec = getIntent().getStringExtra("maxSpec");
		minHeight = getIntent().getStringExtra("minHeight");
		maxHeight = getIntent().getStringExtra("maxHeight");
		minCrown = getIntent().getStringExtra("minCrown");
		maxCrown = getIntent().getStringExtra("maxCrown");
		name = getIntent().getStringExtra("name");
		et_max_guige.setText(maxSpec);
		et_min_guige.setText(minSpec);
		et_pinming.setText(name);
		et_minHeight.setText(minHeight);
		et_maxHeight.setText(maxHeight);
		et_minCrown.setText(minCrown);
		et_maxCrown.setText(maxCrown);

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
				case R.id.iv_reset:
					minSpec = "";
					maxSpec = "";
					minHeight = "";
					maxHeight = "";
					minCrown = "";
					maxCrown = "";
					name = "";
					et_max_guige.setText(maxSpec);
					et_min_guige.setText(minSpec);
					et_pinming.setText(name);
					et_minHeight.setText(minHeight);
					et_maxHeight.setText(maxHeight);
					et_minCrown.setText(minCrown);
					et_maxCrown.setText(maxCrown);
					break;
				case R.id.sure:
					Intent intent = new Intent();
					intent.putExtra("minSpec", et_min_guige.getText()
							.toString());
					intent.putExtra("maxSpec", et_max_guige.getText()
							.toString());
					intent.putExtra("minHeight", et_minHeight.getText()
							.toString());
					intent.putExtra("maxHeight", et_maxHeight.getText()
							.toString());
					intent.putExtra("minCrown", et_minCrown.getText()
							.toString());
					intent.putExtra("maxCrown", et_maxCrown.getText()
							.toString());
					intent.putExtra("name", et_pinming.getText().toString());
					setResult(9, intent);
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
					Thread.sleep(200);
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
