package com.hldj.hmyg;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.loginjudge.LoginJudge;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class SetProfileActivity extends LoginActivity implements
		OnWheelChangedListener {

	private Dialog dialog;
	private Dialog dialog1;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	/**
	 */
	private ImageView btn_back;
	private String sex = MyApplication.Userinfo.getString("sex", "1");
	private String phone = MyApplication.Userinfo.getString("phone", "");
	private String userName = MyApplication.Userinfo.getString("userName", "");
	private String realName = MyApplication.Userinfo.getString("realName", "");
	private String email = MyApplication.Userinfo.getString("email", "");
	private String address = MyApplication.Userinfo.getString("address", "");
	private String companyName = MyApplication.Userinfo.getString(
			"companyName", "");
	private String publicName = MyApplication.Userinfo.getString("publicName",
			"");
	private String publicPhone = MyApplication.Userinfo.getString(
			"publicPhone", "");
	private String coCityfullName = MyApplication.Userinfo.getString(
			"coCityfullName", "");
	private String coCitycityCode = MyApplication.Userinfo.getString(
			"coCitycityCode", "");
	private String twCityfullName = MyApplication.Userinfo.getString(
			"twCityfullName", "");
	private String twCitycityCode = MyApplication.Userinfo.getString(
			"twCitycityCode", "");
	private TextView tv_phone;
	private TextView tv_cocity;
	private TextView tv_twcity;
	private EditText et_name;
	private EditText et_real_name;
	private EditText et_email;
	private EditText et_address;
	private EditText et_company;
	private EditText et_open_name;
	private EditText et_open_phone;
	private WheelView id_Childs;
	protected String[] mStreetDatas;
	protected String[] mStreetIdDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_profile);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_area1 = (LinearLayout) findViewById(R.id.ll_area1);
		LinearLayout ll_area2 = (LinearLayout) findViewById(R.id.ll_area2);
		LinearLayout ll_save = (LinearLayout) findViewById(R.id.ll_save);
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		// if sex = 1 boy sex =0 girl
		if ("1".equals(sex)) {
			group.check(R.id.radio1);
		} else if ("0".equals(sex)) {
			group.check(R.id.radio2);
		} else {
			group.check(R.id.radio1);
		}

		// 绑定一个匿名监听器
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(radioButtonId);
				if ("男".equals(rb.getText().toString())) {
					sex = "1";
				} else if ("女".equals(rb.getText().toString())) {
					sex = "0";
				}
			}

		});

		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_cocity = (TextView) findViewById(R.id.tv_cocity);
		tv_twcity = (TextView) findViewById(R.id.tv_twcity);
		et_name = (EditText) findViewById(R.id.et_name);
		et_real_name = (EditText) findViewById(R.id.et_real_name);
		et_email = (EditText) findViewById(R.id.et_email);
		et_address = (EditText) findViewById(R.id.et_address);
		et_company = (EditText) findViewById(R.id.et_company);
		et_open_name = (EditText) findViewById(R.id.et_open_name);
		et_open_phone = (EditText) findViewById(R.id.et_open_phone);

		tv_phone.setText(phone);
		tv_cocity.setText(coCityfullName);
		tv_twcity.setText(twCityfullName);
		tv_phone.setText(phone);
		tv_phone.setText(phone);
		et_name.setText(userName);
		et_real_name.setText(realName);
		et_email.setText(email);
		et_address.setText(address);
		et_company.setText(companyName);
		et_open_name.setText(publicName);
		et_open_phone.setText(publicPhone);

		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
			// 直属经济不让改公开资料，公司名称也不能改
			et_company.setFocusable(false);
			et_open_name.setFocusable(false);
			et_open_phone.setFocusable(false);
			et_company.setClickable(false);
			et_open_name.setClickable(false);
			et_open_phone.setClickable(false);
		}
		mCurrentZipCode = coCitycityCode;
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_area1.setOnClickListener(multipleClickProcess);
		ll_area2.setOnClickListener(multipleClickProcess);
		ll_save.setOnClickListener(multipleClickProcess);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		phone = MyApplication.Userinfo.getString("phone", "");
		tv_phone.setText(phone);
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (LoginJudge.isTabc) {
			LoginJudge.isTabc = false;
			startActivity(new Intent(SetProfileActivity.this,
					MainActivity.class));
			finish();
		}
		finish();
		overridePendingTransition_back();
//		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
					Intent toSetPhoneByGetCodeActivity = new Intent(
							SetProfileActivity.this,
							SetPhoneByGetCodeActivity.class);
					startActivity(toSetPhoneByGetCodeActivity);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					break;
				case R.id.ll_save:
					// if ("".equals(mCurrentZipCode)) {
					// Toast.makeText(SetProfileActivity.this, "请选择城市",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }

					isOnlyUserName();
					break;
				case R.id.ll_area1:
					showCitys();
					break;
				case R.id.ll_area2:
					initChildsData(mCurrentZipCode);
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

	public void isOnlyUserName() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);
		AjaxParams params = new AjaxParams();
		params.put("id", MyApplication.Userinfo.getString("id", ""));
		params.put("userName", et_name.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/user/isOnlyUserName",
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
								// Toast.makeText(SetProfileActivity.this, msg,
								// Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								userSave();
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
						Toast.makeText(SetProfileActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	public void userSave() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,true);

		AjaxParams params = new AjaxParams();
		params.put("id", MyApplication.Userinfo.getString("id", ""));
		params.put("realName", et_real_name.getText().toString());
		params.put("userName", et_name.getText().toString());
		params.put("email", et_email.getText().toString());
		if (!"".equals(twCitycityCode)) {
			params.put("cityCode", twCitycityCode);
		} else if (!"".equals(coCitycityCode)) {
			params.put("cityCode", coCitycityCode);
		}
		params.put("sex", sex);
		params.put("address", et_address.getText().toString());
		params.put("companyName", et_company.getText().toString());
		params.put("publicName", et_open_name.getText().toString());
		params.put("publicPhone", et_open_phone.getText().toString());
		finalHttp.post(GetServerUrl.getUrl() + "admin/user/save", params,
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
								Toast.makeText(SetProfileActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}
							if ("1".equals(code)) {
								getUserInfo(MyApplication.Userinfo.getString(
										"id", ""), "SetProfileActivity");
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
						Toast.makeText(SetProfileActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void showCitys() {

		if (dialog == null) {
			View dia_choose_share = getLayoutInflater().inflate(
					R.layout.dia_choose_city, null);
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
					coCityfullName = mCurrentProviceName + "\u0020"
							+ mCurrentCityName + "\u0020"
							+ mCurrentDistrictName + "\u0020";
					coCitycityCode = mCurrentZipCode;
					tv_cocity.setText(coCityfullName);
					if (!SetProfileActivity.this.isFinishing()
							&& dialog != null) {
						if (dialog.isShowing()) {
							dialog.cancel();
						} else {
							dialog.show();
						}
					}

				}
			});
		}

		if (!SetProfileActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!SetProfileActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

	}

	private void initChildsData(String parentCode) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp,false);
		AjaxParams params = new AjaxParams();
		params.put("parentCode", parentCode);
		finalHttp.post(GetServerUrl.getUrl() + "city/getChilds", params,
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
							}
							if ("1".equals(code)) {
								JSONArray cityList = jsonObject.getJSONObject(
										"data").getJSONArray("cityList");
								if (cityList.length() > 0) {
									mStreetDatas = new String[cityList.length()];
									mStreetIdDatas = new String[cityList
											.length()];
									for (int i = 0; i < cityList.length(); i++) {
										JSONObject jsonObject2 = cityList
												.getJSONObject(i);
										// hMap.put("name", JsonGetInfo
										// .getJsonString(jsonObject2,
										// "name"));
										// hMap.put("fullName", JsonGetInfo
										// .getJsonString(jsonObject2,
										// "fullName"));
										// hMap.put("cityCode", JsonGetInfo
										// .getJsonString(jsonObject2,
										// "cityCode"));
										// datas.add(hMap);
										mStreetDatas[i] = JsonGetInfo
												.getJsonString(jsonObject2,
														"name");
										mStreetIdDatas[i] = JsonGetInfo
												.getJsonString(jsonObject2,
														"cityCode");

									}
								}

								if (mStreetIdDatas.length > 0) {
									showAreas();
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
						Toast.makeText(SetProfileActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void showAreas() {
		View dia_choose_share = getLayoutInflater().inflate(
				R.layout.dia_choose_area, null);
		TextView tv_sure = (TextView) dia_choose_share
				.findViewById(R.id.tv_sure);
		id_Childs = (WheelView) dia_choose_share.findViewById(R.id.id_Childs);
		// 添加change事件
		id_Childs.addChangingListener(this);

		id_Childs.setViewAdapter(new ArrayWheelAdapter<String>(
				SetProfileActivity.this, mStreetDatas));
		id_Childs.setCurrentItem(0);
		// 设置可见条目数量
		id_Childs.setVisibleItems(7);
		int pCurrent = id_Childs.getCurrentItem();
		twCityfullName = mStreetDatas[pCurrent];
		twCitycityCode = mStreetIdDatas[pCurrent];

		dialog1 = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog1.setContentView(dia_choose_share, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		Window window = dialog1.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog1.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog1.setCanceledOnTouchOutside(true);
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_twcity.setText(twCityfullName);
				if (!SetProfileActivity.this.isFinishing() && dialog1 != null) {
					if (dialog1.isShowing()) {
						dialog1.cancel();
					} else {
						dialog1.show();
					}
				}

			}
		});

		if (!SetProfileActivity.this.isFinishing() && dialog1.isShowing()) {
			dialog1.cancel();
		} else if (!SetProfileActivity.this.isFinishing() && dialog1 != null
				&& !dialog1.isShowing()) {
			dialog1.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				SetProfileActivity.this, mProvinceDatas));
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
			// twCityfullName = "";
			// twCitycityCode = "";
			// tv_twcity.setText(twCityfullName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
			// twCityfullName = "";
			// twCitycityCode = "";
			// tv_twcity.setText(twCityfullName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
			// twCityfullName = "";
			// twCitycityCode = "";
			// tv_twcity.setText(twCityfullName);
		} else if (wheel == id_Childs) {
			twCityfullName = mStreetDatas[newValue];
			twCitycityCode = mStreetIdDatas[newValue];
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
				SetProfileActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

}
