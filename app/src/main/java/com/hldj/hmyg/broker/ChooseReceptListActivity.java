package com.hldj.hmyg.broker;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.hldj.hmyg.bean.PicSerializableMaplist;

public class ChooseReceptListActivity extends NeedSwipeBackActivity implements
		OnClickListener, OnItemSelectedListener,
		com.hldj.hmyg.broker.ChooseReceptAdapter.OnListRemovedListener {
	private Gson gson;
	ListView lv;
	Button btn_addRow, btn_get;
	ChooseReceptAdapter mAdapter;
	private LinearLayout ll_save;
	private ArrayList<ReceiptListJson> list = new ArrayList<ReceiptListJson>();//
	private TextView id_tv_edit_all;
	private String orderId = "";
	private String driverName = "";
	private String driverPhone = "";
	private String carNum = "";
	private String remarks = "";
	private Button back;
	private LinearLayout ll_noti_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_recept);
		gson = new Gson();
		orderId = getIntent().getStringExtra("id");
		driverName = getIntent().getStringExtra("driverName");
		driverPhone = getIntent().getStringExtra("driverPhone");
		carNum = getIntent().getStringExtra("carNum");
		remarks = getIntent().getStringExtra("remarks");
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		ll_noti_add = (LinearLayout) findViewById(R.id.ll_noti_add);
		btn_addRow = (Button) findViewById(R.id.btn_addRow);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		btn_addRow.setOnClickListener(this);
		id_tv_edit_all.setOnClickListener(this);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		back = (Button) findViewById(R.id.back);
		btn_get = (Button) findViewById(R.id.btn_get);
		btn_get.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		back.setOnClickListener(this);

		lv = (ListView) findViewById(R.id.lv);
		mAdapter = new ChooseReceptAdapter(list, ChooseReceptListActivity.this);
		lv.setAdapter(mAdapter);
		if(list.size()>0){
			ll_noti_add.setVisibility(View.GONE);
		}else {
			ll_noti_add.setVisibility(View.VISIBLE);
		}
		mAdapter.setOnListRemovedListener(ChooseReceptListActivity.this);
		ll_save.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_get) {
			if (list.size() == 0) {
				Toast.makeText(ChooseReceptListActivity.this, "未添加装车品种", Toast.LENGTH_SHORT).show();
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getNeed_loadedCount()>(list.get(i).getCount()-list.get(i).getLoadedCountJson())){
					Toast.makeText(ChooseReceptListActivity.this, "装车数量超过可装数量", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			Intent toChooseReceptListActivity = new Intent(
					ChooseReceptListActivity.this,
					SaveChooseReceptListActivity.class);
			toChooseReceptListActivity.putExtra("id", orderId);
			toChooseReceptListActivity.putExtra("driverName", driverName);
			toChooseReceptListActivity.putExtra("driverPhone", driverPhone);
			toChooseReceptListActivity.putExtra("carNum", carNum);
			toChooseReceptListActivity.putExtra("remarks", carNum);
			Bundle bundleObject = new Bundle();
			final ChooseReceptSerializableMaplist myMap = new ChooseReceptSerializableMaplist();
			myMap.setMaplist(list);
			bundleObject.putSerializable("list", myMap);
			toChooseReceptListActivity.putExtras(bundleObject);
			startActivityForResult(toChooseReceptListActivity, 1);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		} else if (v.getId() == R.id.btn_back || v.getId() == R.id.back) {
			finish();
		} else if (v.getId() == R.id.id_tv_edit_all) {

			ArrayList<String> ids = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				ids.add(list.get(i).getId());
			}
			Intent toAddReceiptActivity = new Intent(
					ChooseReceptListActivity.this, AddReceiptActivity.class);
			toAddReceiptActivity.putExtra("id", orderId);
			toAddReceiptActivity.putStringArrayListExtra("ids", ids);
			startActivityForResult(toAddReceiptActivity, 1);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		// TODO Auto-generated method stub
		if (arg1 == 1) {
			Bundle bundle = data.getExtras();
			list = ((ChooseReceptSerializableMaplist) bundle.get("list"))
					.getMaplist();
			mAdapter.notify(list);
			if(list.size()>0){
				ll_noti_add.setVisibility(View.GONE);
			}else {
				ll_noti_add.setVisibility(View.VISIBLE);
			}
		} else if (arg1 == 2) {
			setResult(2);
			finish();
		}

		super.onActivityResult(arg0, arg1, data);
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