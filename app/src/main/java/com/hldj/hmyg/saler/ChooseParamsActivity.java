package com.hldj.hmyg.saler;

import info.hoang8f.android.segmented.SegmentedGroup;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.NeedSwipeBackActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedittext.MyAdapter;
import com.example.listedittext.MyAdapter.OnListRemovedListener;
import com.example.listedittext.paramsData;
import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

public class ChooseParamsActivity extends NeedSwipeBackActivity implements
		OnCheckedChangeListener, OnItemSelectedListener, OnListRemovedListener {

	private TextView tv_TypeName;
	private TextView tv_unitType;
	private String seedlingParams = "";
	private EditText et_count;
	private EditText et_dbh;
	private EditText et_height;
	private EditText et_crown;
	private EditText et_diameter;
	private EditText et_offbarHeight;
	private EditText et_length;

	private String count = "";

	private String diameter = "";

	private String diameterType = "";

	private String dbh = "";

	private String dbhType = "";

	private String height = "";

	private String crown = "";

	private String offbarHeight = "";

	private String length = "";

	private String plantType = "";

	private String unitType = "";
	private Gson gson;
	ListView lv;
	MyAdapter mAdapter;
	private String seedlingTypeId = "";
	ArrayList<String> str_plantTypeLists = new ArrayList<String>();
	ArrayList<String> str_plantTypeList_ids_s = new ArrayList<String>();

	private com.zhy.view.flowlayout.TagAdapter<String> adapter2;
	int a = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_param);
		lv = (ListView) findViewById(R.id.lv);
		gson = new Gson();
		Intent data = getIntent();
		seedlingTypeId = getIntent().getStringExtra("seedlingTypeId");
		if (!"".equals(seedlingTypeId)) {

			if (Data.paramsDatas.size() == 0) {
				initData(seedlingTypeId);
			} else {
				mAdapter = new MyAdapter(Data.paramsDatas,
						ChooseParamsActivity.this);
				lv.setAdapter(mAdapter);
				mAdapter.setOnListRemovedListener(ChooseParamsActivity.this);
			}
		}
		str_plantTypeLists = data.getStringArrayListExtra("str_plantTypeLists");
		str_plantTypeList_ids_s = data
				.getStringArrayListExtra("str_plantTypeList_ids_s");

		count = data.getStringExtra("count");
		diameter = data.getStringExtra("diameter");
		diameterType = data.getStringExtra("diameterType");
		dbh = data.getStringExtra("dbh");
		dbhType = data.getStringExtra("dbhType");
		height = data.getStringExtra("height");
		crown = data.getStringExtra("crown");
		offbarHeight = data.getStringExtra("offbarHeight");
		length = data.getStringExtra("length");
		plantType = data.getStringExtra("plantType");
		unitType = data.getStringExtra("unitType");
		if ("".equals(dbhType)) {
			dbhType = "size100";
		}
		if ("".equals(diameterType)) {
			diameterType = "size0";
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		seedlingParams = getIntent().getStringExtra("seedlingParams");
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		mFlowLayout2 = (TagFlowLayout) findViewById(R.id.id_flowlayout2);
		mFlowLayout2.setMaxSelectCount(1);
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		LinearLayout ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		LinearLayout ll_08 = (LinearLayout) findViewById(R.id.ll_08);
		LinearLayout ll_09 = (LinearLayout) findViewById(R.id.ll_09);
		LinearLayout ll_10 = (LinearLayout) findViewById(R.id.ll_10);
		et_count = (EditText) findViewById(R.id.et_count);
		et_dbh = (EditText) findViewById(R.id.et_dbh);
		et_height = (EditText) findViewById(R.id.et_height);
		et_crown = (EditText) findViewById(R.id.et_crown);
		et_diameter = (EditText) findViewById(R.id.et_diameter);
		et_offbarHeight = (EditText) findViewById(R.id.et_offbarHeight);
		et_length = (EditText) findViewById(R.id.et_length);
		LinearLayout ll_save = (LinearLayout) findViewById(R.id.ll_save);
		tv_guige = (TextView) findViewById(R.id.tv_guige);
		tv_TypeName = (TextView) findViewById(R.id.tv_TypeName);
		tv_unitType = (TextView) findViewById(R.id.tv_unitType);

		et_count.addTextChangedListener(mTextWatcher);
		et_dbh.addTextChangedListener(mTextWatcher);
		et_height.addTextChangedListener(mTextWatcher);
		et_crown.addTextChangedListener(mTextWatcher);
		et_diameter.addTextChangedListener(mTextWatcher);
		et_offbarHeight.addTextChangedListener(mTextWatcher);
		et_length.addTextChangedListener(mTextWatcher);

		SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented3);
		SegmentedGroup segmented2 = (SegmentedGroup) findViewById(R.id.segmented2);
		RadioButton button31 = (RadioButton) findViewById(R.id.button31);
		RadioButton button32 = (RadioButton) findViewById(R.id.button32);
		RadioButton button33 = (RadioButton) findViewById(R.id.button33);
		RadioButton button21 = (RadioButton) findViewById(R.id.button21);
		RadioButton button2x = (RadioButton) findViewById(R.id.button2x);
		RadioButton button22 = (RadioButton) findViewById(R.id.button22);

		et_count.setText(count);
		et_dbh.setText(dbh);
		et_height.setText(height);
		et_crown.setText(crown);
		et_diameter.setText(diameter);
		et_offbarHeight.setText(offbarHeight);
		et_length.setText(length);
		if ("plant".equals(unitType)) {
			tv_unitType.setText("株");
		} else if ("crowd".equals(unitType)) {
			tv_unitType.setText("丛");
		} else if ("jin".equals(unitType)) {
			tv_unitType.setText("斤");
		} else if ("squaremeter".equals(unitType)) {
			tv_unitType.setText("平方米");
		} else if ("dai".equals(unitType)) {
			tv_unitType.setText("袋");
		} else if ("pen".equals(unitType)) {
			tv_unitType.setText("盆");
		} else {
			tv_unitType.setText("");
		}
		if ("planted".equals(plantType)) {
			tv_TypeName.setText("地栽苗");
		} else if ("transplant".equals(plantType)) {
			tv_TypeName.setText("移植苗");
		} else if ("heelin".equals(plantType)) {
			tv_TypeName.setText("假植苗");
		} else if ("container".equals(plantType)) {
			tv_TypeName.setText("容器苗");
		}
		if ("size30".equals(dbhType)) {
			button31.setChecked(true);
			tv_guige.setText("地径");
		} else if ("size100".equals(dbhType)) {
			button32.setChecked(true);
			tv_guige.setText("米径");
		} else if ("size130".equals(dbhType)) {
			button33.setChecked(true);
			tv_guige.setText("胸径");
		} else {
			button32.setChecked(true);
			tv_guige.setText("米径");
		}

		if ("size0".equals(diameterType)) {
			button21.setChecked(true);
		} else if ("size10".equals(diameterType)) {
			button2x.setChecked(true);
		} else if ("size30".equals(diameterType)) {
			button22.setChecked(true);
		}

		if (seedlingParams.contains("dbh")) {
			ll_05.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("height")) {
			ll_07.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("crown")) {
			ll_08.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("diameter")) {
			ll_06.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("offbarHeight")) {
			ll_09.setVisibility(View.VISIBLE);
		}
		if (seedlingParams.contains("length")) {
			ll_10.setVisibility(View.VISIBLE);
		}

		if (str_plantTypeLists.size() > 0) {

			adapter2 = new com.zhy.view.flowlayout.TagAdapter<String>(
					str_plantTypeLists) {

				@Override
				public View getView(FlowLayout parent, int position, String s) {
					TextView tv = (TextView) getLayoutInflater().inflate(
							R.layout.tv, mFlowLayout2, false);
					tv.setText(s);
					return tv;
				}
			};
			mFlowLayout2.setAdapter(adapter2);
			mFlowLayout2
					.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
						@Override
						public boolean onTagClick(View view, int position,
								FlowLayout parent) {
							plantType = str_plantTypeList_ids_s.get(position);
							adapter2.setSelectedList(position);
							return true;
						}
					});
			for (int i = 0; i < str_plantTypeList_ids_s.size(); i++) {
				if (plantType.equals(str_plantTypeList_ids_s.get(i))) {
					a = i;
					adapter2.setSelectedList(a);
				}
			}
		}
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_07.setOnClickListener(multipleClickProcess);
		ll_08.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		segmented2.setOnCheckedChangeListener(this);
		segmented3.setOnCheckedChangeListener(this);

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
	private TagFlowLayout mFlowLayout2;
	private TextView tv_guige;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.button31:
			dbhType = "size30";
			tv_guige.setText("地径");
			break;
		case R.id.button32:
			dbhType = "size100";
			tv_guige.setText("米径");
			break;
		case R.id.button33:
			dbhType = "size130";
			tv_guige.setText("胸径");
			break;
		case R.id.button21:
			diameterType = "size0";
			break;
		case R.id.button2x:
			diameterType = "size10";
			break;
		case R.id.button22:
			diameterType = "size30";
			break;
		default:
			// Nothing to do
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
					onBackPressed();
					break;
				case R.id.ll_01:

					new ActionSheetDialog(ChooseParamsActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("种植类型")
							.addSheetItem("地栽苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "planted";
											tv_TypeName.setText("地栽苗");
										}
									})
							.addSheetItem("移植苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "transplant";
											tv_TypeName.setText("移植苗");
										}
									})
							.addSheetItem("假植苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "heelin";
											tv_TypeName.setText("假植苗");

										}
									})

							.addSheetItem("容器苗", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											plantType = "container";
											tv_TypeName.setText("容器苗");
										}
									}).show();

					break;
				case R.id.ll_04:

					new ActionSheetDialog(ChooseParamsActivity.this)
							.builder()
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.setTitle("单位")
							.addSheetItem("株", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "plant";
											tv_unitType.setText("株");
										}
									})
							.addSheetItem("丛", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "crowd";
											tv_unitType.setText("丛");
										}
									})
							.addSheetItem("斤", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "jin";
											tv_unitType.setText("斤");
										}
									})

							.addSheetItem("平方米", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "squaremeter";
											tv_unitType.setText("平方米");
										}
									})
							.addSheetItem("袋", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "dai";
											tv_unitType.setText("袋");
										}
									})
							.addSheetItem("盆", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											unitType = "pen";
											tv_unitType.setText("盆");
										}
									}).show();

					break;
				case R.id.ll_save:
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					if (imm.isActive() && getCurrentFocus() != null
							&& getCurrentFocus().getWindowToken() != null) {
						imm.hideSoftInputFromWindow(ChooseParamsActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
					Intent intent = new Intent();
					intent.putExtra("count", et_count.getText().toString());
					intent.putExtra("diameter", et_diameter.getText()
							.toString());
					intent.putExtra("diameterType", diameterType);
					intent.putExtra("dbh", et_dbh.getText().toString());
					intent.putExtra("dbhType", dbhType);
					intent.putExtra("height", et_height.getText().toString());
					intent.putExtra("crown", et_crown.getText().toString());
					intent.putExtra("offbarHeight", et_offbarHeight.getText()
							.toString());
					intent.putExtra("length", et_length.getText().toString());
					intent.putExtra("plantType", plantType);
					intent.putExtra("unitType", unitType);
					intent.putExtra("paramsData", gson.toJson(Data.paramsDatas));
					setResult(3, intent);
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
								if (typeList.length() > 0) {
									Data.paramsDatas.clear();
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
									mAdapter = new MyAdapter(Data.paramsDatas,
											ChooseParamsActivity.this);
									lv.setAdapter(mAdapter);
									mAdapter.setOnListRemovedListener(ChooseParamsActivity.this);
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
						Toast.makeText(ChooseParamsActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	public void onRemoved() {
		// TODO Auto-generated method stub
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
