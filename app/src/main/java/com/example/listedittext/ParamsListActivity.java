package com.example.listedittext;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listedittext.MyAdapter.OnListRemovedListener;
import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

public class ParamsListActivity extends NeedSwipeBackActivity implements OnClickListener,
		OnItemSelectedListener, OnListRemovedListener {
	private Gson gson;
	ListView lv;
	Button btn_addRow, btn_get;
	MyAdapter mAdapter;
	private String seedlingTypeId ="";
	private LinearLayout ll_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_params);
		gson = new Gson();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		seedlingTypeId  = getIntent().getStringExtra("seedlingTypeId");
		btn_addRow = (Button) findViewById(R.id.btn_addRow);
		btn_addRow.setOnClickListener(this);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		btn_get = (Button) findViewById(R.id.btn_get);
		btn_get.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		lv = (ListView) findViewById(R.id.lv);
		if(!"".equals(seedlingTypeId)){
			if(Data.paramsDatas.size()==0){
				initData(seedlingTypeId);
			}else {
				mAdapter = new MyAdapter(Data.paramsDatas, ParamsListActivity.this);
				lv.setAdapter(mAdapter);
				mAdapter.setOnListRemovedListener(ParamsListActivity.this);
				ll_save.setVisibility(View.VISIBLE);
			}
		}
	
	

		
	}

	private void initData(String seedlingTypeId) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("seedlingTypeId", seedlingTypeId);
		finalHttp.post(GetServerUrl.getUrl() + "seedlingType/getParams",
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
							
								JSONArray typeList = jsonObject.getJSONObject(
										"data").getJSONArray("paramList");
								if(typeList.length()>0){
									for (int i = 0; i < typeList.length(); i++) {
										JSONObject jsonObject2 = typeList
												.getJSONObject(i);
										paramsData n = new paramsData();
										n.setValue("");
										n.setCode(JsonGetInfo.getJsonString(
												jsonObject2, "code"));
										n.setName(JsonGetInfo.getJsonString(
												jsonObject2, "name"));
										Data.paramsDatas.add(n);
									}
								}
								
								if (Data.paramsDatas.size() > 0) {
									mAdapter = new MyAdapter(Data.paramsDatas, ParamsListActivity.this);
									lv.setAdapter(mAdapter);
									mAdapter.setOnListRemovedListener(ParamsListActivity.this);
									ll_save.setVisibility(View.VISIBLE);
								}
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
						Toast.makeText(ParamsListActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_addRow) {
			paramsData n = new paramsData();
			n.setValue("");
			n.setName("手机");
			mAdapter.addItem(n);
			mAdapter.notifyDataSetChanged();
		} else if (v.getId() == R.id.btn_get) {
			Intent intent = new Intent();
			intent.putExtra("paramsData", gson.toJson(Data.paramsDatas));
			setResult(4, intent);
			finish();
		}else if (v.getId() == R.id.btn_back) {
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