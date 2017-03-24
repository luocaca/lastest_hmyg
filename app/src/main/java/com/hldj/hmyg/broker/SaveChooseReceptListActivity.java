package com.hldj.hmyg.broker;

import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.broker.SaveChooseReceptAdapter.OnListRemovedListener;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class SaveChooseReceptListActivity extends NeedSwipeBackActivity
		implements OnClickListener, OnItemSelectedListener,
		OnListRemovedListener {
	private Gson gson;
	ListView lv;
	Button btn_addRow, btn_get;
	SaveChooseReceptAdapter mAdapter;
	private LinearLayout ll_save;
	private ArrayList<ReceiptListJson> list = new ArrayList<ReceiptListJson>();//
	private String orderId = "";
	private String driverName = "";
	private String driverPhone = "";
	private String carNum = "";
	private String remarks = "";
	private Button back;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_choose_recept);
		gson = new Gson();
		orderId = getIntent().getStringExtra("id");
		driverName = getIntent().getStringExtra("driverName");
		driverPhone = getIntent().getStringExtra("driverPhone");
		carNum = getIntent().getStringExtra("carNum");
		remarks = getIntent().getStringExtra("remarks");
		Bundle bundle = getIntent().getExtras();
		list = ((ChooseReceptSerializableMaplist) bundle.get("list"))
				.getMaplist();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		tv_01 = (TextView) findViewById(R.id.tv_01);
		tv_02 = (TextView) findViewById(R.id.tv_02);
		tv_03 = (TextView) findViewById(R.id.tv_03);
		tv_04 = (TextView) findViewById(R.id.tv_04);
		tv_05 = (TextView) findViewById(R.id.tv_05);
		tv_01.setText("车辆信息");
		tv_02.setText("司机姓名：" + driverName);
		tv_03.setText("司机电话：" + driverPhone);
		tv_04.setText("车牌号：" + carNum);
		tv_05.setText("备注：" + remarks);
		btn_addRow = (Button) findViewById(R.id.btn_addRow);
		btn_addRow.setOnClickListener(this);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		back = (Button) findViewById(R.id.back);
		btn_get = (Button) findViewById(R.id.btn_get);
		btn_get.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		back.setOnClickListener(this);

		lv = (ListView) findViewById(R.id.lv);
		mAdapter = new SaveChooseReceptAdapter(list,
				SaveChooseReceptListActivity.this);
		lv.setAdapter(mAdapter);
		mAdapter.setOnListRemovedListener(SaveChooseReceptListActivity.this);
		ll_save.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_get) {
			if(list.size()==0){
				Toast.makeText(SaveChooseReceptListActivity.this, "您还没有添加装车品种哦", Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayList<itemsData> itemsDatas = new ArrayList<itemsData>();
			for (int i = 0; i < list.size(); i++) {
				itemsData itemsData = new itemsData();
				itemsData.setId("");
				itemsData.setDeliveryId(list.get(i).getDeliveryId());
				// itemsData.setOrderId(orderId);
				itemsData.setOrderId(list.get(i).getOrderId());
				itemsData.setReceiptId(list.get(i).getId());
				itemsData.setLoadCount(list.get(i).getNeed_loadedCount() + "");
				itemsDatas.add(itemsData);
			}
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);
			AjaxParams params = new AjaxParams();
			params.put("id", "");
			params.put("deliveryId", list.get(0).getDeliveryId());
			params.put("buyerId", list.get(0).getBuyerId());
			params.put("itemsData", gson.toJson(itemsDatas));
			params.put("driverName", driverName);
			params.put("driverPhone", driverPhone);
			params.put("carNum", carNum);
			params.put("remarks", remarks);
			finalHttp.post(GetServerUrl.getUrl()
					+ "admin/agent/loadCar/saveAndSend", params,
					new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub

							try {
								JSONObject jsonObject = new JSONObject(t
										.toString());
								String code = JsonGetInfo.getJsonString(
										jsonObject, "code");
								String msg = JsonGetInfo.getJsonString(
										jsonObject, "msg");
								if (!"".equals(msg)) {
								}
								if ("1".equals(code)) {
									Toast.makeText(
											SaveChooseReceptListActivity.this,
											"装车成功", Toast.LENGTH_SHORT).show();
									setResult(2);
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
							Toast.makeText(SaveChooseReceptListActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});

		} else if (v.getId() == R.id.btn_back || v.getId() == R.id.back) {
			finish();
		}
	}

	@Override
	public void onRemoved() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
}