package com.hldj.hmyg.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hldj.hmyg.R;
import com.hldj.hmyg.application.MyApplication;
import com.hy.utils.GetLotLat;
import com.mrwujay.cascade.activity.GetCitiyNameByCode;

/**
 * Created by Administrator on 2017/4/10.
 */

public class MapFragment extends Fragment implements GeocodeSearch.OnGeocodeSearchListener, LocationSource,
        AMapLocationListener {

    private AMap aMap;
    private OnLocationChangedListener mListener;
    private MapView mapView;
    private String addressName;
    private GeocodeSearch geocoderSearch;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private Marker geoMarker;
    private Marker regeoMarker;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    protected Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public SharedPreferences.Editor e;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initView(container, container, savedInstanceState);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private void initView(View view, ViewGroup container, Bundle savedInstanceState) {
        e = MyApplication.Userinfo.edit();//初始化 sp
        GetLotLat.getBaiduLotLat(activity, e);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();

        LatLonPoint latLonPoint = new LatLonPoint(40.003662, 116.465271);
        getAddress(latLonPoint);// 暂不用

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
        geocoderSearch = new GeocodeSearch(activity);
        geocoderSearch.setOnGeocodeSearchListener(this);


    }


    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        // showDialog2();
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        // showDialog2();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }


    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
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
                Toast.makeText(activity, R.string.no_result, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(activity, rCode, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 逆地理编码回调 code就在这里获取
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

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
                Toast.makeText(activity, R.string.no_result, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, rCode, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(activity);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
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
                String cityName = amapLocation.getCity();
                String adCode = amapLocation.getAdCode();
                e.putString("ScityCode", cityCode2);
                e.putString("ScityName", GetCitiyNameByCode.initProvinceDatas(
                        activity, adCode));
                e.putString("latitude", amapLocation.getLatitude() + "");
                e.putString("longitude", amapLocation.getLongitude() + "");
                e.commit();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


}
