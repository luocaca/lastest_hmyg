package com.hldj.hmyg.saler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.VisibleRegion;
import com.hldj.hmyg.R;

/**
 * AMapV1地图中简单介绍OnMapClickListener, OnMapLongClickListener,
 * OnCameraChangeListener三种监听器用法
 */

public class EventsActivity extends Activity implements OnMapClickListener,
		OnMapLongClickListener, OnCameraChangeListener, LocationSource,
		AMapLocationListener, OnMapLoadedListener, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private TextView mTapTextView;
	private TextView mCameraTextView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private Marker mPositionMark;
	private TextView id_tv_edit_all;
	private ImageView btn_back;
	private double latitude = 0.0;
	private double longitude = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		mTapTextView = (TextView) findViewById(R.id.tap_text);
		mCameraTextView = (TextView) findViewById(R.id.camera_text);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		id_tv_edit_all = (TextView) findViewById(R.id.id_tv_edit_all);
		id_tv_edit_all.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	/**
	 * amap添加一些事件监听器
	 */
	private void setUpMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()

		aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
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
	 * 对单击地图事件回调
	 */
	@Override
	public void onMapClick(LatLng point) {
		mTapTextView.setText("tapped, point=" + point);
	}

	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng point) {
		mTapTextView.setText("long pressed, point=" + point);
	}

	/**
	 * 对正在移动地图事件回调
	 */
	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		mCameraTextView.setText("onCameraChange:" + cameraPosition.toString());
	}

	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		mCameraTextView.setText("onCameraChangeFinish:"
				+ cameraPosition.toString());
		VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域、
		LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
		latitude = mPositionMark.getPosition().latitude;
		longitude = mPositionMark.getPosition().longitude;

		// boolean isContain = latLngBounds.contains(Constants.SHANGHAI);//
		// 判断上海经纬度是否包括在当前地图可见区域
		// if (isContain) {
		// ToastUtil.show(EventsActivity.this, "上海市在地图当前可见区域内");
		// } else {
		// ToastUtil.show(EventsActivity.this, "上海市超出地图当前可见区域");
		// }

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
		// TODO Auto-generated method stub
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
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.anchor(0.5f, 0.5f);
		markerOptions.position(new LatLng(0, 0));
		markerOptions
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.dw_taren)));
		mPositionMark = aMap.addMarker(markerOptions);
		mPositionMark.setPositionByPixels(mapView.getWidth() / 2,
				mapView.getHeight() / 2);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.id_tv_edit_all:
			Intent intent = new Intent();
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			setResult(1, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
