package com.hldj.hmyg.buyer;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.white.utils.DateTimePickDialogUtil;

public class SaveReceipptActivity extends NeedSwipeBackActivity {

	private LinearLayout ll_save;
	private Button save;
	private String orderId = "";
	private String receiptId = "";
	private String receiptAddressId = "";
	private String receiptDate = "";
	private TextView tv_name;
	private TextView tv_address;
	private EditText tv_canshu;
	private EditText et_num;
	private EditText et_remark;
	private LinearLayout ll_02;
	private LinearLayout ll_04;
	private int allowReceiptInfoCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_receipt);
		ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_canshu = (EditText) findViewById(R.id.tv_canshu);
		et_num = (EditText) findViewById(R.id.et_num);
		et_remark = (EditText) findViewById(R.id.et_remark);
		if(getIntent().getStringExtra("count")!=null){
			allowReceiptInfoCount = getIntent().getIntExtra(
					"allowReceiptInfoCount", 0)+Integer.parseInt(getIntent().getStringExtra("count"));
		}else {
			allowReceiptInfoCount = getIntent().getIntExtra(
					"allowReceiptInfoCount", 0);
			et_num.setHint("允许接收的最大数量为"+allowReceiptInfoCount);
		}
	
		if (getIntent().getStringExtra("orderId") != null) {
			orderId = getIntent().getStringExtra("orderId");
		}
		if (getIntent().getStringExtra("receiptId") != null) {
			receiptId = getIntent().getStringExtra("receiptId");
			receiptAddressId = getIntent().getStringExtra("receiptAddressId");
			receiptDate = getIntent().getStringExtra("receiptDate");
			String count = getIntent().getStringExtra("count");
			String remarks = getIntent().getStringExtra("remarks");
			String contactName = getIntent().getStringExtra("contactName");
			String contactPhone = getIntent().getStringExtra("contactPhone");
			String fullAddress = getIntent().getStringExtra("fullAddress");

			tv_name.setText(contactName + contactPhone);
			tv_address.setText(fullAddress);
			tv_canshu.setText(receiptDate);
			et_remark.setText(remarks);
			et_num.setText(count);
//			if (allowReceiptInfoCount > 0) {
//				et_num.setText(allowReceiptInfoCount + "");
//			}
		}
	
		et_num.addTextChangedListener(mTextWatcher);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);

		save = (Button) findViewById(R.id.save);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);

		btn_back.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);
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
				case R.id.ll_02:
					Intent toReceiptAdressListActivity = new Intent(
							SaveReceipptActivity.this,
							ReceiptAdressListActivity.class);
					toReceiptAdressListActivity.putExtra("from",
							"SaveReceipptActivity");
					startActivityForResult(toReceiptAdressListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_04:
					DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
							SaveReceipptActivity.this, "");
					dateTimePicKDialog.dateTimePicKDialog(tv_canshu);
					break;
				case R.id.save:
					if ("".equals(orderId)) {
						return;
					}
					if ("".equals(et_num.getText().toString())) {
						Toast.makeText(SaveReceipptActivity.this, "请填写数量",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (Integer.parseInt(et_num.getText().toString()) > allowReceiptInfoCount) {
						Toast.makeText(SaveReceipptActivity.this,
								"最大可填写数量为" + allowReceiptInfoCount,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(receiptAddressId)) {
						Toast.makeText(SaveReceipptActivity.this, "请选择收货地址",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(tv_canshu.getText().toString())) {
						Toast.makeText(SaveReceipptActivity.this, "请选择收货日期",
								Toast.LENGTH_SHORT).show();
						return;
					}
					saveReceipt();
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

	private void saveReceipt() {
		// TODO Auto-generated method stub

		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", receiptId);
		params.put("receiptDate", tv_canshu.getText().toString());
		params.put("orderId", orderId);
		params.put("count", et_num.getText().toString());
		params.put("remarks", et_remark.getText().toString());
		params.put("receiptAddressId", receiptAddressId);
		finalHttp.post(GetServerUrl.getUrl() + "admin/buyer/order/saveReceipt",
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
								Toast.makeText(SaveReceipptActivity.this, msg,
										Toast.LENGTH_SHORT).show();
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
						Toast.makeText(SaveReceipptActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 2) {
			receiptAddressId = data.getStringExtra("addressId");
			String contactPhone = data.getStringExtra("contactPhone");
			String contactName = data.getStringExtra("contactName");
			String cityName = data.getStringExtra("cityName");
			tv_name.setText(contactName + contactPhone);
			tv_address.setText(cityName);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
