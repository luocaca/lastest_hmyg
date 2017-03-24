package com.hldj.hmyg.buyer;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseActivity;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin;
import com.zzy.common.widget.wheelview.popwin.CustomDaysPickPopwin.DayChangeListener;

public class SavePurchaseActivity extends BaseActivity implements
		OnWheelChangedListener {
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
	private TextView tv_needInvoice;
	private TextView tv_address;
	private String firstSeedlingTypeId = "";
	private String addressId = "";
	private String firstSeedlingTypeName = "";
	private String seedlingParams = "";

	private EditText et_name;

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
	private String paramsData = "";

	private TextView tv_canshu;

	private TextView tv_qitacanshu;

	private LinearLayout ll_save;
	private Button save;
	private Dialog dialog;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	boolean isSelDate;
	boolean isSelTime;
	private Calendar now;
	private boolean needInvoice;
	private String receiptDate = "";
	private String validity = "";

	private Gson gson;

	private String id = "";

	private String projectName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_purchase);
		Data.purchases.clear();
		gson = new Gson();
		now = Calendar.getInstance();

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		mainView = (View) findViewById(R.id.ll_mainView);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		LinearLayout ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		LinearLayout ll_08 = (LinearLayout) findViewById(R.id.ll_08);
		save = (Button) findViewById(R.id.save);
		ll_save = (LinearLayout) findViewById(R.id.ll_save);
		tv_day = (TextView) findViewById(R.id.tv_day);
		tv_needInvoice = (TextView) findViewById(R.id.tv_needInvoice);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_canshu = (TextView) findViewById(R.id.tv_canshu);
		tv_qitacanshu = (TextView) findViewById(R.id.tv_qitacanshu);
		et_name = (EditText) findViewById(R.id.et_name);

		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			projectName = getIntent().getStringExtra("projectName");
			mCurrentZipCode = getIntent().getStringExtra("cityCode");
			String cityName = getIntent().getStringExtra("cityName");
			receiptDate = getIntent().getStringExtra("receiptDate");
			validity = getIntent().getStringExtra("validity");
			needInvoice = getIntent().getBooleanExtra("needInvoice", false);
			et_name.setText(projectName);
			tv_address.setText(cityName);
			tv_canshu.setText(receiptDate);
			tv_day.setText(validity);
			if (needInvoice) {
				tv_needInvoice.setText("提供发票");
			} else {
				tv_needInvoice.setText("不提供发票");
			}
			ll_08.setVisibility(View.GONE);

		}
	
		btn_back.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		ll_05.setOnClickListener(multipleClickProcess);
		ll_06.setOnClickListener(multipleClickProcess);
		ll_07.setOnClickListener(multipleClickProcess);
		ll_08.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
		save.setOnClickListener(multipleClickProcess);
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
				case R.id.ll_04:
					OnDateSetListener callBack = new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							tv_canshu.setText("" + year + "-"
									+ String.format("%02d", monthOfYear + 1)
									+ "-" + String.format("%02d", dayOfMonth));
							receiptDate = "" + year + "-"
									+ String.format("%02d", monthOfYear + 1)
									+ "-" + String.format("%02d", dayOfMonth);
							isSelDate = true;
						}
					};
					DatePickerDialog datePickerDialog = new DatePickerDialog(
							SavePurchaseActivity.this, callBack,
							now.get(Calendar.YEAR), now.get(Calendar.MONTH),
							now.get(Calendar.DAY_OF_MONTH));
					datePickerDialog.show();
					break;
				case R.id.ll_05:
					showCitys();
					break;
				case R.id.ll_06:
					CustomDaysPickPopwin daysPopwin = new CustomDaysPickPopwin(
							SavePurchaseActivity.this, new DayChangeListener() {

								@Override
								public void onDayChange(int dayType, int pos) {
									if (dayType == 0) {
										// model.setValidity(Short.valueOf(days[pos]));
										tv_day.setText(Short.valueOf(days[pos])
												+ "天");
										validity = Short.valueOf(days[pos])
												+ "";
									} else {
										// model.setValidity(Short
										// .valueOf(customDays[pos]));
										tv_day.setText(""
												+ Short.valueOf(customDays[pos])
												+ "天");
										validity = Short
												.valueOf(customDays[pos]) + "";
									}
									// model.setValidityTextView(daysTv);
								}
							}, days, customDays, 0);
					daysPopwin.showAtLocation(mainView, Gravity.BOTTOM
							| Gravity.CENTER, 0, 0);
					break;
				case R.id.ll_07:
					new ActionSheetDialog(SavePurchaseActivity.this)
							.builder()
							.setTitle("是否提供发票")
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.addSheetItem("是", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											tv_needInvoice.setText("是");
											needInvoice = true;

										}
									})
							.addSheetItem("否", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											tv_needInvoice.setText("否");
											needInvoice = false;
										}
									}).show();
					break;
				case R.id.ll_08:
					Intent toPurchaseListActivity = new Intent(
							SavePurchaseActivity.this,
							PurchaseListActivity.class);
					toPurchaseListActivity.putExtra("", id);
					startActivityForResult(toPurchaseListActivity, 1);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.save:
					if ("".equals(mCurrentZipCode)) {
						Toast.makeText(SavePurchaseActivity.this, "请选择城市",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(receiptDate)) {
						Toast.makeText(SavePurchaseActivity.this, "请选择期望收货日期",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if ("".equals(validity)) {
						Toast.makeText(SavePurchaseActivity.this, "请选择有效期",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (getIntent().getStringExtra("id") != null) {
						purchaseUpdate();
					} else {
						purchaseSave();
					}

					break;

				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		public void purchaseUpdate() {
			// TODO Auto-generated method stub

			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);

			AjaxParams params = new AjaxParams();
			params.put("id", id);
			params.put("projectName", et_name.getText().toString());
			params.put("cityCode", mCurrentZipCode.substring(0, 4));
			params.put("receiptDate", receiptDate);
			params.put("validity", validity);
			params.put("needInvoice", needInvoice + "");
			finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/update",
					params, new AjaxCallBack<Object>() {

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
									Toast.makeText(SavePurchaseActivity.this,
											"保存成功", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(SavePurchaseActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});

		}

		public void purchaseSave() {
			// TODO Auto-generated method stub

			if (Data.purchases.size() == 0) {
				Toast.makeText(SavePurchaseActivity.this, "请选择添加品种",
						Toast.LENGTH_SHORT).show();
				return;
			}

			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,true);

			AjaxParams params = new AjaxParams();
			params.put("projectName", et_name.getText().toString());
			params.put("cityCode", mCurrentZipCode.substring(0, 4));
			params.put("receiptDate", receiptDate);
			params.put("validity", validity);
			params.put("needInvoice", needInvoice + "");
			params.put("itemData", gson.toJson(Data.purchases));
			finalHttp.post(GetServerUrl.getUrl() + "admin/purchase/save",
					params, new AjaxCallBack<Object>() {

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
									Toast.makeText(SavePurchaseActivity.this,
											"保存成功", Toast.LENGTH_SHORT).show();
								}
								if ("1".equals(code)) {
									Data.purchases.clear();
									finish();
									
									Intent toManagerPurchaseActivity = new Intent(
											SavePurchaseActivity.this, ManagerPurchaseActivity.class);
									startActivity(toManagerPurchaseActivity);
									overridePendingTransition(R.anim.slide_in_left,
											R.anim.slide_out_right);
									
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
							Toast.makeText(SavePurchaseActivity.this,
									R.string.error_net, Toast.LENGTH_SHORT)
									.show();
							super.onFailure(t, errorNo, strMsg);
						}

					});

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

	private void showCitys() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_city_for_purchase, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		mViewProvince = (WheelView) dia_choose_share
				.findViewById(R.id.id_province);
		mViewCity = (WheelView) dia_choose_share.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) dia_choose_share
				.findViewById(R.id.id_district);
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
				tv_address.setText(mCurrentProviceName + "\u0020"
						+ mCurrentCityName + "\u0020");
				if (!SavePurchaseActivity.this.isFinishing() && dialog != null) {
					if (dialog.isShowing()) {
						dialog.cancel();
					} else {
						dialog.show();
					}
				}

			}
		});

		if (!SavePurchaseActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!SavePurchaseActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				SavePurchaseActivity.this, mProvinceDatas));
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
				SavePurchaseActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			if (Data.purchases.size() > 0) {
				tv_qitacanshu.setText(Data.purchases.size() + "");
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
