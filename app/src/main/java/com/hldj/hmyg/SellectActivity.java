package com.hldj.hmyg;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import me.drakeet.materialdialog.MaterialDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.mrwujay.cascade.activity.BaseSecondActivity;
import com.mrwujay.cascade.activity.GetCodeByName;

public class SellectActivity extends BaseSecondActivity implements
		OnWheelChangedListener {
	MaterialDialog mMaterialDialog;
	private TextView tv_type01;
	private TextView tv_type02;
	private TextView tv_type03;
	private TextView tv_type04;
	private static String type01 = ""; // planted,
	private String type02 = ""; // transplant,
	private String type03 = ""; // heelin,
	private String type04 = ""; // container,
	private EditText et_minPrice;
	private EditText et_maxPrice;
	private EditText et_minDiameter;
	private EditText et_maxDiameter;
	private EditText et_minDbh;
	private EditText et_maxDbh;
	private EditText et_minHeight;
	private EditText et_maxHeight;
	private EditText et_minLength;
	private EditText et_maxLength;
	private EditText et_minCrown;
	private EditText et_maxCrown;
	private EditText et_minOffbarHeight;
	private EditText et_maxOffbarHeight;
	private LinearLayout ll_area;
	private LinearLayout ll_price;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView tv_area;
	private String cityCode = "";
	private String cityName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellect);
		mMaterialDialog = new MaterialDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		TextView iv_reset = (TextView) findViewById(R.id.iv_reset);
		ll_area = (LinearLayout) findViewById(R.id.ll_area);
		ll_price = (LinearLayout) findViewById(R.id.ll_price);
		et_minPrice = (EditText) findViewById(R.id.et_minPrice);
		et_maxPrice = (EditText) findViewById(R.id.et_maxPrice);
		et_minDiameter = (EditText) findViewById(R.id.et_minDiameter);
		et_maxDiameter = (EditText) findViewById(R.id.et_maxDiameter);
		et_minDbh = (EditText) findViewById(R.id.et_minDbh);
		et_maxDbh = (EditText) findViewById(R.id.et_maxDbh);
		et_minHeight = (EditText) findViewById(R.id.et_minHeight);
		et_maxHeight = (EditText) findViewById(R.id.et_maxHeight);
		et_minLength = (EditText) findViewById(R.id.et_minLength);
		et_maxLength = (EditText) findViewById(R.id.et_maxLength);
		et_minCrown = (EditText) findViewById(R.id.et_minCrown);
		et_maxCrown = (EditText) findViewById(R.id.et_maxCrown);
		et_minOffbarHeight = (EditText) findViewById(R.id.et_minOffbarHeight);
		et_maxOffbarHeight = (EditText) findViewById(R.id.et_maxOffbarHeight);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_type01 = (TextView) findViewById(R.id.tv_type01);
		tv_type02 = (TextView) findViewById(R.id.tv_type02);
		tv_type03 = (TextView) findViewById(R.id.tv_type03);
		tv_type04 = (TextView) findViewById(R.id.tv_type04);
		TextView sure = (TextView) findViewById(R.id.sure);

		initData();

		btn_back.setOnClickListener(multipleClickProcess);
		iv_reset.setOnClickListener(multipleClickProcess);
		ll_area.setOnClickListener(multipleClickProcess);
		tv_type01.setOnClickListener(multipleClickProcess);
		tv_type02.setOnClickListener(multipleClickProcess);
		tv_type03.setOnClickListener(multipleClickProcess);
		tv_type04.setOnClickListener(multipleClickProcess);
		sure.setOnClickListener(multipleClickProcess);
	}

	public void initData() {
		String from = getIntent().getStringExtra("from");
		if ("StorePurchaseListActivity".equals(from)) {
			ll_price.setVisibility(View.GONE);
		}
		cityCode = getIntent().getStringExtra("cityCode");
		cityName = getIntent().getStringExtra("cityName");
		String minPrice = getIntent().getStringExtra("minPrice");
		String maxPrice = getIntent().getStringExtra("maxPrice");
		String minDiameter = getIntent().getStringExtra("minDiameter");
		String maxDiameter = getIntent().getStringExtra("maxDiameter");
		String minDbh = getIntent().getStringExtra("minDbh");
		String maxDbh = getIntent().getStringExtra("maxDbh");
		String minHeight = getIntent().getStringExtra("minHeight");
		String maxHeight = getIntent().getStringExtra("maxHeight");
		String minLength = getIntent().getStringExtra("minLength");
		String maxLength = getIntent().getStringExtra("maxLength");
		String minCrown = getIntent().getStringExtra("minCrown");
		String maxCrown = getIntent().getStringExtra("maxCrown");
		String minOffbarHeight = getIntent().getStringExtra("minOffbarHeight");
		String maxOffbarHeight = getIntent().getStringExtra("maxOffbarHeight");
		String plantTypes = getIntent().getStringExtra("plantTypes");
		mCurrentZipCode = cityCode;
		tv_area.setText(cityName);
		et_minPrice.setText(minPrice);
		et_maxPrice.setText(maxPrice);
		et_minDiameter.setText(minDiameter);
		et_maxDiameter.setText(maxDiameter);
		et_minDbh.setText(minDbh);
		et_maxDbh.setText(maxDbh);
		et_minHeight.setText(minHeight);
		et_maxHeight.setText(maxHeight);
		et_minLength.setText(minLength);
		et_maxLength.setText(maxLength);
		et_minCrown.setText(minCrown);
		et_maxCrown.setText(maxCrown);
		et_minOffbarHeight.setText(minOffbarHeight);
		et_maxOffbarHeight.setText(maxOffbarHeight);
		if (plantTypes.contains("planted")) {
			type01 = "planted,";
			tv_type01.setTextColor(getResources().getColor(R.color.main_color));
			tv_type01.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (plantTypes.contains("transplant")) {
			type02 = "transplant,";
			tv_type02.setTextColor(getResources().getColor(R.color.main_color));
			tv_type02.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (plantTypes.contains("heelin")) {
			type03 = "heelin,";
			tv_type03.setTextColor(getResources().getColor(R.color.main_color));
			tv_type03.setBackgroundResource(R.drawable.search_edit_selector);
		}
		if (plantTypes.contains("container")) {
			type04 = "planted,";
			tv_type04.setTextColor(getResources().getColor(R.color.main_color));
			tv_type04.setBackgroundResource(R.drawable.search_edit_selector);
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
				case R.id.ll_area:
					showCitys();
					break;
				case R.id.iv_reset:
					cityCode = "";
					cityName = "全国";
					tv_area.setText(cityName);
					type01 = "";
					type02 = "";
					type03 = "";
					type04 = "";
					tv_type01.setTextColor(getResources()
							.getColor(R.color.gray));
					tv_type01
							.setBackgroundResource(R.drawable.sellect_edit_selector);
					tv_type02.setTextColor(getResources()
							.getColor(R.color.gray));
					tv_type02
							.setBackgroundResource(R.drawable.sellect_edit_selector);
					tv_type03.setTextColor(getResources()
							.getColor(R.color.gray));
					tv_type03
							.setBackgroundResource(R.drawable.sellect_edit_selector);
					tv_type04.setTextColor(getResources()
							.getColor(R.color.gray));
					tv_type04
							.setBackgroundResource(R.drawable.sellect_edit_selector);
					et_minPrice.setText("");
					et_maxPrice.setText("");
					et_minDiameter.setText("");
					et_maxDiameter.setText("");
					et_minDbh.setText("");
					et_maxDbh.setText("");
					et_minHeight.setText("");
					et_maxHeight.setText("");
					et_minLength.setText("");
					et_maxLength.setText("");
					et_minCrown.setText("");
					et_maxCrown.setText("");
					et_minOffbarHeight.setText("");
					et_maxOffbarHeight.setText("");
					break;
				case R.id.tv_type01:
					if ("".equals(type01)) {
						type01 = "planted,";
						tv_type01.setTextColor(getResources().getColor(
								R.color.main_color));
						tv_type01
								.setBackgroundResource(R.drawable.search_edit_selector);
					} else {
						type01 = "";
						tv_type01.setTextColor(getResources().getColor(
								R.color.gray));
						tv_type01
								.setBackgroundResource(R.drawable.sellect_edit_selector);

					}
					break;
				
				case R.id.tv_type02:
					if ("".equals(type02)) {
						type02 = "transplant,";
						tv_type02.setTextColor(getResources().getColor(
								R.color.main_color));
						tv_type02
								.setBackgroundResource(R.drawable.search_edit_selector);
					} else {
						type02 = "";
						tv_type02.setTextColor(getResources().getColor(
								R.color.gray));
						tv_type02
								.setBackgroundResource(R.drawable.sellect_edit_selector);

					}
					break;
				case R.id.tv_type03:
					if ("".equals(type03)) {
						type03 = "heelin,";
						tv_type03.setTextColor(getResources().getColor(
								R.color.main_color));
						tv_type03
								.setBackgroundResource(R.drawable.search_edit_selector);
					} else {
						type03 = "";
						tv_type03.setTextColor(getResources().getColor(
								R.color.gray));
						tv_type03
								.setBackgroundResource(R.drawable.sellect_edit_selector);

					}
					break;
				case R.id.tv_type04:
					if ("".equals(type04)) {
						type04 = "container,";
						tv_type04.setTextColor(getResources().getColor(
								R.color.main_color));
						tv_type04
								.setBackgroundResource(R.drawable.search_edit_selector);
					} else {
						type04 = "";
						tv_type04.setTextColor(getResources().getColor(
								R.color.gray));
						tv_type04
								.setBackgroundResource(R.drawable.sellect_edit_selector);
					}
					break;
				case R.id.sure:
					Intent intent = new Intent();
					intent.putExtra("cityCode", GetCodeByName
							.initProvinceDatas(SellectActivity.this,
									mCurrentProviceName, mCurrentCityName));
					intent.putExtra("cityName", cityName);
					intent.putExtra("minPrice", et_minPrice.getText()
							.toString());
					intent.putExtra("maxPrice", et_maxPrice.getText()
							.toString());
					intent.putExtra("minDiameter", et_minDiameter.getText()
							.toString());
					intent.putExtra("maxDiameter", et_maxDiameter.getText()
							.toString());
					intent.putExtra("minDbh", et_minDbh.getText().toString());
					intent.putExtra("maxDbh", et_maxDbh.getText().toString());
					intent.putExtra("minHeight", et_minHeight.getText()
							.toString());
					intent.putExtra("maxHeight", et_maxHeight.getText()
							.toString());
					intent.putExtra("minLength", et_minLength.getText()
							.toString());
					intent.putExtra("maxLength", et_maxLength.getText()
							.toString());
					intent.putExtra("minCrown", et_minCrown.getText()
							.toString());
					intent.putExtra("maxCrown", et_maxCrown.getText()
							.toString());
					intent.putExtra("minOffbarHeight", et_minOffbarHeight
							.getText().toString());
					intent.putExtra("maxOffbarHeight", et_maxOffbarHeight
							.getText().toString());
					intent.putExtra("plantTypes", type01 + type02 + type03
							+ type04);
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

	private void showCitys() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
		mViewDistrict.setVisibility(View.GONE);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		setUpData();

		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(dia_choose_share, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cityName = mCurrentProviceName + "\u0020" + mCurrentCityName
						+ "\u0020" + mCurrentDistrictName + "\u0020";
				cityCode = mCurrentZipCode;
				tv_area.setText(cityName);

				if (!SellectActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!SellectActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!SellectActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				SellectActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private void showSelectedResult() {
		Toast.makeText(
				SellectActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}
}
