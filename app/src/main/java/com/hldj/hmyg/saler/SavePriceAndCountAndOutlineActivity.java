package com.hldj.hmyg.saler;

import java.io.ByteArrayOutputStream;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin.DayChangeListener;

public class SavePriceAndCountAndOutlineActivity extends NeedSwipeBackActivity {
	private String[] days = { "30", "90", "180" };

	private String[] customDays = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41",
			"42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63",
			"64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74",
			"75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85",
			"86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96",
			"97", "98", "99", "100", "101", "102", "103", "104", "105", "106",
			"107", "108", "109", "110", "111", "112", "113", "114", "115",
			"116", "117", "118", "119", "120", "121", "122", "123", "124",
			"125", "126", "127", "128", "129", "130", "131", "132", "133",
			"134", "135", "136", "137", "138", "139", "140", "141", "142",
			"143", "144", "145", "146", "147", "148", "149", "150", "151",
			"152", "153", "154", "155", "156", "157", "158", "159", "160",
			"161", "162", "163", "164", "165", "166", "167", "168", "169",
			"170", "171", "172", "173", "174", "175", "176", "177", "178",
			"179", "180" };
	private View mainView;
	private TextView tv_day;

	private EditText et_price;
	private EditText et_FloorPrice;

	private LinearLayout ll_FloorPrice;

	private LinearLayout ll_save;
	private Button save;
	private String validity = "";

	private EditText et_count;

	private String id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_price_and_count);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		double price = getIntent().getDoubleExtra("price", 0);
		double floorPrice = getIntent().getDoubleExtra("floorPrice", 0);
		int count = getIntent().getIntExtra("count", 0);
		int lastDay = getIntent().getIntExtra("lastDay", 0);
		id = getIntent().getStringExtra("id");
		mainView = (View) findViewById(R.id.ll_mainView);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		save = (Button) findViewById(R.id.save);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		ll_FloorPrice = (LinearLayout) findViewById(R.id.ll_FloorPrice);
		tv_day = (TextView) findViewById(R.id.tv_day);
		et_count = (EditText) findViewById(R.id.et_count);
		et_price = (EditText) findViewById(R.id.et_price);
		et_FloorPrice = (EditText) findViewById(R.id.et_FloorPrice);
		et_count.setText(count+"");
		et_price.setText(price+"");
		et_FloorPrice.setText(floorPrice+"");
		et_count.addTextChangedListener(mTextWatcher); 
		btn_back.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);
		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
			ll_FloorPrice.setVisibility(View.VISIBLE);
		}
	}
	
	

	// 在一开始声明TextWatcher，在afterTextChange内操作
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String text = s.toString();
			int len = s.toString().length();
			if (len == 1 && text.equals("0")) {
				s.clear();
			}
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
				case R.id.ll_06:
					CustomDaysPickPopwin daysPopwin = new CustomDaysPickPopwin(
							SavePriceAndCountAndOutlineActivity.this,
							new DayChangeListener() {

								@Override
								public void onDayChange(int dayType, int pos) {
									if (dayType == 0) {
										// model.setValidity(Short.valueOf(days[pos]));
										tv_day.setText(Short.valueOf(days[pos])
												+ "天");
										validity = days[pos];
									} else {
										// model.setValidity(Short
										// .valueOf(customDays[pos]));
										tv_day.setText(""
												+ Short.valueOf(customDays[pos])
												+ "天");
										validity = customDays[pos];
									}
									// model.setValidityTextView(daysTv);
								}
							}, days, customDays, 0);
					daysPopwin.showAtLocation(mainView, Gravity.BOTTOM
							| Gravity.CENTER, 0, 0);
					break;
				case R.id.save:
					if ("".equals(et_count.getText().toString())) {
						Toast.makeText(
								SavePriceAndCountAndOutlineActivity.this,
								"请先输入库存", Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(et_price.getText().toString())) {
						Toast.makeText(
								SavePriceAndCountAndOutlineActivity.this,
								"请先输入苗木价格", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if (Double.parseDouble(et_price.getText().toString())<=0) {
						Toast.makeText(SavePriceAndCountAndOutlineActivity.this, "请输入超过0的价格",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (MyApplication.Userinfo.getBoolean("isDirectAgent",
							false)) {
						if ("".equals(et_FloorPrice.getText().toString())) {
							Toast.makeText(
									SavePriceAndCountAndOutlineActivity.this,
									"请先输入底价", Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.parseDouble(et_FloorPrice.getText().toString())<=0) {
							Toast.makeText(SavePriceAndCountAndOutlineActivity.this, "请输入超过0的底价",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (Double.parseDouble(et_FloorPrice.getText()
								.toString()) >= Double.parseDouble(et_price
								.getText().toString())) {
							Toast.makeText(
									SavePriceAndCountAndOutlineActivity.this,
									"输入底价请小于苗木价格", Toast.LENGTH_SHORT).show();
							return;
						}
					}

					// 下一步操作
					seedlingSave();

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

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	private void seedlingSave() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		params.put("count", et_count.getText().toString());
		params.put("price", et_price.getText().toString());
		params.put("floorPrice", et_FloorPrice.getText().toString());
		params.put("validity", validity);

		finalHttp.post(GetServerUrl.getUrl() + "admin/seedling/savePriceAndCountAndOutline", params,
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
								Toast.makeText(
										SavePriceAndCountAndOutlineActivity.this,
										msg, Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
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
						Toast.makeText(
								SavePriceAndCountAndOutlineActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

}
