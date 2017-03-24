package com.hldj.hmyg.saler;

import java.util.ArrayList;
import java.util.HashMap;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.hldj.hmyg.application.MyApplication;
import com.hldj.hmyg.jimiao.SaveMiaoActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.activity.BaseActivity;
import com.mrwujay.cascade.activity.GetCitiyNameByCode;
import com.mrwujay.cascade.activity.GetCodeByName;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class AddAdressActivity extends BaseActivity implements
		OnGeocodeSearchListener, OnWheelChangedListener, LocationSource,
		AMapLocationListener {
	private Dialog dialog;
	private Dialog dialog1;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView tv_type;
	private TextView tv_street;
	private TextView tv_area;
	private TextView tv_map;
	private EditText et_adress;
	private EditText et_name;
	private EditText et_phone;
	private EditText et_num;
	ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
	String type = "";
	String mainType = "";
	private CheckBox remmber;
	private WheelView id_Childs;
	protected String[] mStreetDatas;
	protected String[] mStreetIdDatas;
	private String twCityfullName = "";
	private String twCitycityCode = "";
	private String id = "";
	private String name = "";
	private String cityCode = "";
	private String cityName = "";
	private String detailAddress = "";
	private String contactName = "";
	private String contactPhone = "";
	private String nurseryArea = "";
	private boolean isDefault2;
	private Button edit_btn;
	private EditText et_nursery_name;
	private EditText et_zhuyin;
	private double latitude = 0.0;
	private double longitude = 0.0;

	private GeocodeSearch geocoderSearch;
	private String addressName;
	private AMap aMap;
	private MapView mapView;
	private Marker geoMarker;
	private Marker regeoMarker;
	private ProgressDialog progDialog;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private TagFlowLayout mFlowLayout2;

	private ArrayList<String> types = new ArrayList<String>();
	private ArrayList<String> type_ids = new ArrayList<String>();
	@SuppressWarnings("rawtypes")
	private com.zhy.view.flowlayout.TagAdapter adapter2;
	int a = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_adress);

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写

		init();
		LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);
		getAddress(latLonPoint);// 暂不用
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		MultipleClickProcess multipleClickProcess = new MultipleClickProcess();
		types.add("自营");
		type_ids.add("self");
		types.add("代卖");
		type_ids.add("proxy");
		mFlowLayout2 = (TagFlowLayout) findViewById(R.id.id_flowlayout2);
		mFlowLayout2.setMaxSelectCount(1);

		ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
		LinearLayout ll_01 = (LinearLayout) findViewById(R.id.ll_01);
		LinearLayout ll_02 = (LinearLayout) findViewById(R.id.ll_02);
		LinearLayout ll_03 = (LinearLayout) findViewById(R.id.ll_03);
		LinearLayout ll_04 = (LinearLayout) findViewById(R.id.ll_04);
		LinearLayout ll_05 = (LinearLayout) findViewById(R.id.ll_05);
		LinearLayout ll_06 = (LinearLayout) findViewById(R.id.ll_06);
		LinearLayout ll_07 = (LinearLayout) findViewById(R.id.ll_07);
		LinearLayout ll_08 = (LinearLayout) findViewById(R.id.ll_08);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		edit_btn = (Button) findViewById(R.id.edit_btn);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_street = (TextView) findViewById(R.id.tv_street);
		tv_map = (TextView) findViewById(R.id.tv_map);
		et_nursery_name = (EditText) findViewById(R.id.et_nursery_name);
		et_zhuyin = (EditText) findViewById(R.id.et_zhuyin);
		et_adress = (EditText) findViewById(R.id.et_adress);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_num = (EditText) findViewById(R.id.et_num);
		remmber = (CheckBox) findViewById(R.id.remmber);
		TextView save = (TextView) findViewById(R.id.save);
		if (MyApplication.Userinfo.getBoolean("isDirectAgent", false)) {
			// 只有直属经纪才有联系人和联系电话的填写框，其他用户全部隐藏。 版本2，都显示
			ll_05.setVisibility(View.VISIBLE);
			ll_06.setVisibility(View.VISIBLE);
		} else {
			ll_05.setVisibility(View.VISIBLE);
			ll_06.setVisibility(View.VISIBLE);
		}
		// if (SaveSeedlingActivity.instance != null) {
		// ll_03.setVisibility(View.VISIBLE);
		// } else if (SaveMiaoActivity.instance != null) {
		// ll_03.setVisibility(View.GONE);
		// }

		if (SaveMiaoActivity.instance != null) {
			ll_03.setVisibility(View.GONE);
		}
		if (getIntent().getStringExtra("id") != null) {
			tv_title.setText("编辑苗源地址");
			id = getIntent().getStringExtra("id");
			name = getIntent().getStringExtra("name");
			cityCode = getIntent().getStringExtra("cityCode");
			cityName = getIntent().getStringExtra("cityName");
			mCurrentZipCode = cityCode;
			twCitycityCode = cityCode;
			detailAddress = getIntent().getStringExtra("detailAddress");
			contactName = getIntent().getStringExtra("contactName");
			contactPhone = getIntent().getStringExtra("contactPhone");
			nurseryArea = getIntent().getStringExtra("nurseryArea");
			type = getIntent().getStringExtra("type");
			mainType = getIntent().getStringExtra("mainType");
			isDefault2 = getIntent().getBooleanExtra("isDefault", false);
			remmber.setChecked(isDefault2);
			tv_area.setText(GetCitiyNameByCode.initProvinceDatas(
					AddAdressActivity.this, cityCode));
			tv_street.setText(cityName.replace(GetCitiyNameByCode
					.initProvinceDatas(AddAdressActivity.this, cityCode), ""));
			et_nursery_name.setText(name);
			et_adress.setText(detailAddress);
			et_name.setText(contactName);
			et_phone.setText(contactPhone);
			et_num.setText(nurseryArea);
			et_zhuyin.setText(mainType);
			edit_btn.setVisibility(View.VISIBLE);
			if ("self".equals(type)) {
				tv_type.setText("自营");
			} else if ("proxy".equals(type)) {
				tv_type.setText("代卖");
			}
			remmber.setChecked(isDefault2);
		} else {
			// if (!MyApplication.Userinfo.getBoolean("isAgent", false)) {
			// // Android 新增苗源地址，苗源地联系人和手机默认为当前用户真实姓名和手机号（经纪用户除外）
			// et_name.setText(MyApplication.Userinfo
			// .getString("realName", ""));
			// et_phone.setText(MyApplication.Userinfo.getString("phone", ""));
			// }
		}
		if (types.size() > 0) {

			adapter2 = new com.zhy.view.flowlayout.TagAdapter<String>(types) {

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
							type = type_ids.get(position);
							adapter2.setSelectedList(position);
							return true;
						}
					});
			for (int i = 0; i < type_ids.size(); i++) {
				if (type.equals(type_ids.get(i))) {
					a = i;
					adapter2.setSelectedList(a);
				}
			}
		}
		btn_back.setOnClickListener(multipleClickProcess);
		ll_01.setOnClickListener(multipleClickProcess);
		ll_02.setOnClickListener(multipleClickProcess);
		ll_03.setOnClickListener(multipleClickProcess);
		ll_04.setOnClickListener(multipleClickProcess);
		edit_btn.setOnClickListener(multipleClickProcess);
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
				case R.id.ll_01:
					new ActionSheetDialog(AddAdressActivity.this)
							.builder()
							.setTitle("苗圃类型")
							.setCancelable(false)
							.setCanceledOnTouchOutside(false)
							.addSheetItem("自营", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											tv_type.setText("自营");
											type = "self";
										}
									})
							.addSheetItem("代卖", SheetItemColor.Blue,
									new OnSheetItemClickListener() {
										@Override
										public void onClick(int which) {
											tv_type.setText("代卖");
											type = "proxy";
										}
									}).show();
					break;
				case R.id.ll_02:
					showCitys();
					break;
				case R.id.ll_03:
					if (!"".equals(mCurrentZipCode)) {
						initChildsData(mCurrentZipCode);
					}
					break;
				case R.id.ll_04:
					Intent intent = new Intent(AddAdressActivity.this,
							EventsActivity.class);
					startActivityForResult(intent, 1);
					break;
				case R.id.edit_btn:
					DelAdress(id);
					break;
				case R.id.save:
					// if ("".equals(type)) {
					// Toast.makeText(AddAdressActivity.this, "请填写苗圃类型",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					// if ("".equals(et_nursery_name.getText().toString())) {
					// Toast.makeText(AddAdressActivity.this, "请填写苗圃名称",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					// if ("".equals(et_phone.getText().toString())) {
					// Toast.makeText(AddAdressActivity.this, "请填写手机号",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					// if ("".equals(et_adress.getText().toString())) {
					// Toast.makeText(AddAdressActivity.this, "请填写地址",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					// if ("".equals(et_name.getText().toString())) {
					// Toast.makeText(AddAdressActivity.this, "请填写联系人",
					// Toast.LENGTH_SHORT).show();
					// return;
					// }
					if ("".equals(mCurrentZipCode)) {
						Toast.makeText(AddAdressActivity.this, "请选择地区",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (SaveMiaoActivity.instance == null) {
						if ("".equals(twCitycityCode)) {
							Toast.makeText(AddAdressActivity.this, "请选择街道",
									Toast.LENGTH_SHORT).show();
							return;
						}
					}

					AddAdress();
					break;

				default:
					break;
				}
				setFlag();
				// do some things
				new TimeThread().start();
			}
		}

		private void initChildsData(String parentCode) {
			// TODO Auto-generated method stub
			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp, false);

			AjaxParams params = new AjaxParams();
			params.put("parentCode", parentCode);
			finalHttp.post(GetServerUrl.getUrl() + "city/getChilds", params,
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
									JSONArray cityList = jsonObject
											.getJSONObject("data")
											.getJSONArray("cityList");
									if (cityList.length() > 0) {
										mStreetDatas = new String[cityList
												.length()];
										mStreetIdDatas = new String[cityList
												.length()];
										for (int i = 0; i < cityList.length(); i++) {
											JSONObject jsonObject2 = cityList
													.getJSONObject(i);
											mStreetDatas[i] = JsonGetInfo
													.getJsonString(jsonObject2,
															"name");
											mStreetIdDatas[i] = JsonGetInfo
													.getJsonString(jsonObject2,
															"cityCode");

										}
										if (mStreetIdDatas.length > 0) {
											showAreas();
										}
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
							Toast.makeText(AddAdressActivity.this,
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
					tv_area.setText(mCurrentProviceName + "\u0020"
							+ mCurrentCityName + "\u0020"
							+ mCurrentDistrictName + "\u0020");
					if (!AddAdressActivity.this.isFinishing() && dialog != null) {
						if (dialog.isShowing()) {
							dialog.cancel();
						} else {
							dialog.show();
						}
					}

				}
			});
		}

		if (!AddAdressActivity.this.isFinishing() && dialog.isShowing()) {
			dialog.cancel();
		} else if (!AddAdressActivity.this.isFinishing() && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
		}

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
				AddAdressActivity.this, mStreetDatas));
		// 设置可见条目数量
		id_Childs.setVisibleItems(7);

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
				if ("".equals(twCitycityCode)) {
					twCityfullName = mStreetDatas[0];
					twCitycityCode = mStreetIdDatas[0];
				}
				tv_street.setText(twCityfullName);
				if (!AddAdressActivity.this.isFinishing() && dialog1 != null) {
					if (dialog1.isShowing()) {
						dialog1.cancel();
					} else {
						dialog1.show();
					}
				}

			}
		});

		if (!AddAdressActivity.this.isFinishing() && dialog1.isShowing()) {
			dialog1.cancel();
		} else if (!AddAdressActivity.this.isFinishing() && dialog1 != null
				&& !dialog1.isShowing()) {
			dialog1.show();
		}

	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				AddAdressActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		// mViewProvince.setCurrentItem(GetCodeByName.getItemNum(AddAdressActivity.this,
		// mCurrentZipCode)[0]);
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
			// tv_street.setText(twCityfullName);
		} else if (wheel == mViewCity) {
			updateAreas();
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
			// twCityfullName = "";
			// twCitycityCode = "";
			// tv_street.setText(twCityfullName);
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName
					+ mCurrentDistrictName);
			// twCityfullName = "";
			// twCitycityCode = "";
			// tv_street.setText(twCityfullName);
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
		// mViewDistrict.setCurrentItem(GetCodeByName.getItemNum(AddAdressActivity.this,
		// mCurrentZipCode)[2]);
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
		// mViewCity.setCurrentItem(GetCodeByName.getItemNum(AddAdressActivity.this,
		// mCurrentZipCode)[1]);
		updateAreas();
	}

	private void showSelectedResult() {
		Toast.makeText(
				AddAdressActivity.this,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

	private void DelAdress(String id) {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		finalHttp.post(GetServerUrl.getUrl() + "admin/nursery/delete", params,
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
								Toast.makeText(AddAdressActivity.this, "删除成功",
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
						Toast.makeText(AddAdressActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	private void AddAdress() {
		// TODO Auto-generated method stub
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, true);
		AjaxParams params = new AjaxParams();
		params.put("id", id);
		if (!"".equals(twCitycityCode)) {
			params.put("cityCode", twCitycityCode);
		} else if (!"".equals(mCurrentZipCode)) {
			params.put("cityCode", mCurrentZipCode);
		}
		params.put("name", et_nursery_name.getText().toString());
		params.put("detailAddress", et_adress.getText().toString());
		params.put("contactName", et_name.getText().toString());
		params.put("contactPhone", et_phone.getText().toString());
		params.put("nurseryArea", et_num.getText().toString());
		if (remmber.isChecked()) {
			params.put("isDefault", "1");
		} else {
			params.put("isDefault", "");
		}
		params.put("type", type);
		params.put("mainType", et_zhuyin.getText().toString());
		params.put("latitude", latitude + "");
		params.put("longitude", longitude + "");
		finalHttp.post(GetServerUrl.getUrl() + "admin/nursery/save", params,
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
								Toast.makeText(AddAdressActivity.this, msg,
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
						Toast.makeText(AddAdressActivity.this,
								R.string.error_net, Toast.LENGTH_SHORT).show();
						super.onFailure(t, errorNo, strMsg);
					}

				});

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		// TODO Auto-generated method stub
		if (arg1 == 1) {
			latitude = data.getDoubleExtra("latitude", 0.0);
			longitude = data.getDoubleExtra("longitude", 0.0);
			tv_map.setText("已标注");
		}
		super.onActivityResult(arg0, arg1, data);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

			aMap.setLocationSource(this);// 设置定位监听
			aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
			// aMap.setMyLocationType()
		}
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 1000) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				// aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				// AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				// geoMarker.setPosition(AMapUtil.convertToLatLng(address
				// .getLatLonPoint()));
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
			} else {
				Toast.makeText(AddAdressActivity.this, R.string.no_result, 1)
						.show();
			}

		} else {
			Toast.makeText(AddAdressActivity.this, rCode, 1).show();
		}
	}

	/**
	 * 逆地理编码回调 code就在这里获取
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				// aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				// AMapUtil.convertToLatLng(latLonPoint), 15));
				// regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				// Toast.makeText(AddAdressActivity.this, addressName,
				// 1).show();
			} else {
				Toast.makeText(AddAdressActivity.this, R.string.no_result, 1)
						.show();
			}
		} else {
			Toast.makeText(AddAdressActivity.this, rCode, 1).show();
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				String cityCode2 = amapLocation.getCityCode();
				String adCode = amapLocation.getAdCode();
				if (getIntent().getStringExtra("id") == null) {
					// 新增的情况
					if (!"".equals(GetCitiyNameByCode.initProvinceDatas(
							AddAdressActivity.this, adCode))
							&& "".equals(mCurrentZipCode)) {
						// 已有mCurrentZipCode就不再定位动态获取
						cityCode = adCode;
						mCurrentZipCode = cityCode;
						tv_area.setText(GetCitiyNameByCode.initProvinceDatas(
								AddAdressActivity.this, cityCode));
					}
				}
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
			}
		}
	}
}
