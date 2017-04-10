package com.amap.api.car.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.hldj.hmyg.MapSearchListActivity;
import com.hldj.hmyg.R;
import com.hldj.hmyg.StoreActivity;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zf.iosdialog.widget.AlertDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 地图找苗 界面
 */
@SuppressLint("ResourceAsColor")
public class FindFlowerActivity extends Activity implements
        OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener,
        OnMapLoadedListener, OnClickListener, InfoWindowAdapter,
        LocationSource, AMapLocationListener {

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MapView mapView;
    private AMap aMap;
    public static BitmapDescriptor bitmapDescriptor;
    private Button editButton;

    private ImageView btn_back;
    private ImageView location_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flower);
        editButton = (Button) findViewById(R.id.edit_btn);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        location_image = (ImageView) findViewById(R.id.location_image);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
        bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.dw_ziji);
        editButton.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        location_image.setOnClickListener(this);

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

    }

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
        // zoom - 描述了一个缩放级别。高德地图的缩放级别是在3-19 之间
        // 缩放按钮，AMap.moveCamera(CameraUpdateFactory.
        // zoomIn())可以实现地图级别增大1级。缩小一级看下zoomOut()方法。
        // 比例尺，AMap. getScalePerPixel()可以获取当前地图级别下1像素点对应的长度
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        Utils.addEmulateData(aMap, new LatLng(0, 0), FindFlowerActivity.this);// 往地图上添加marker

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        activate(mListener);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
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
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }



    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);
        for (int i = 0; i < Utils.sArrayList.size(); i++) {
            Marker marker2 = Utils.markers.get(i);
            if (marker.equals(marker2)) {
                HashMap<Marker, MapNurseryList> hashMap = Utils.sArrayList
                        .get(i);
                MapNurseryList mapNurseryList = hashMap.get(marker);
                render(marker, infoWindow, mapNurseryList);

            }
        }
        return infoWindow;

    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view,
                       final MapNurseryList mapNurseryList) {

        TextView tv_mp = ((TextView) view.findViewById(R.id.tv_mp));
        TextView tv_companyName = ((TextView) view
                .findViewById(R.id.tv_companyName));
        TextView tv_contactName = ((TextView) view
                .findViewById(R.id.tv_contactName));
        TextView tv_dianhua = ((TextView) view.findViewById(R.id.tv_dianhua));
        TextView tv_zhuyingpz = ((TextView) view
                .findViewById(R.id.tv_zhuyingpz));
        TextView tv_count = ((TextView) view.findViewById(R.id.tv_count));
        TextView iv_toHere = ((TextView) view.findViewById(R.id.iv_toHere));
        LinearLayout ll_toHere = ((LinearLayout) view
                .findViewById(R.id.ll_toHere));
        tv_mp.setText("苗圃名称：" + mapNurseryList.getName());
        tv_companyName.setText("苗圃名称：" + mapNurseryList.getCompanyName());
        tv_contactName.setText("联系人：" + mapNurseryList.getContactName());
        tv_dianhua.setText("联系电话：" + mapNurseryList.getContactPhone());
        tv_zhuyingpz.setText("主营品种：" + mapNurseryList.getMainType());
        tv_count.setText("在售苗木：" + mapNurseryList.getSeedlingCountJson());
        tv_dianhua.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                toCall(mapNurseryList.getContactPhone());
            }
        });
        ll_toHere.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                toHere(mapNurseryList.getLongitude(),
                        mapNurseryList.getLatitude());
            }

        });
        tv_count.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getStore(mapNurseryList.getId());
            }

            private void getStore(String id) {
                // TODO Auto-generated method stub
                FinalHttp finalHttp = new FinalHttp();
                GetServerUrl.addHeaders(finalHttp, false);
                AjaxParams params = new AjaxParams();
                params.put("nurseryId", id);
                finalHttp.post(GetServerUrl.getUrl() + "map/seedling/getStore",
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
                                        // Toast.makeText(FindFlowerActivity.this,
                                        // msg, Toast.LENGTH_SHORT).show();
                                    }
                                    if ("1".equals(code)) {
                                        JSONObject jsonObject2 = JsonGetInfo
                                                .getJSONObject(JsonGetInfo
                                                                .getJSONObject(
                                                                        jsonObject,
                                                                        "data"),
                                                        "store");
                                        String code_ID = JsonGetInfo
                                                .getJsonString(jsonObject2,
                                                        "id");
                                        String name = JsonGetInfo
                                                .getJsonString(jsonObject2,
                                                        "name");
                                        Intent toStoreActivity = new Intent(
                                                FindFlowerActivity.this,
                                                StoreActivity.class);
                                        toStoreActivity.putExtra("code",
                                                code_ID);
                                        startActivity(toStoreActivity);

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
                                Toast.makeText(FindFlowerActivity.this,
                                        R.string.error_net, Toast.LENGTH_SHORT)
                                        .show();
                                super.onFailure(t, errorNo, strMsg);
                            }

                        });
            }

        });
        // ((ImageView) view.findViewById(R.id.badge))
        // .setImageResource(R.drawable.dw_ziji);
        // String title = marker.getTitle();
        // TextView titleUi = ((TextView) view.findViewById(R.id.title));
        // if (title != null) {
        // SpannableString titleText = new SpannableString(title);
        // titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
        // titleText.length(), 0);
        // titleUi.setTextSize(15);
        // titleUi.setText(titleText);
        //
        // } else {
        // titleUi.setText("");
        // }
        // String snippet = marker.getSnippet();
        // TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        // if (snippet != null) {
        // SpannableString snippetText = new SpannableString(snippet);
        // snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
        // snippetText.length(), 0);
        // snippetUi.setTextSize(20);
        // snippetUi.setText(snippetText);
        // } else {
        // snippetUi.setText("");
        // }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.edit_btn:
                Intent toMapSearchListActivity = new Intent(this,
                        MapSearchListActivity.class);
                startActivityForResult(toMapSearchListActivity, 1);
                break;
            case R.id.location_image:
                deactivate();
                if (aMap.getCameraPosition().zoom <= 13) {
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                }
                break;
        }
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // TODO Auto-generated method stub
        // Toast.makeText(FindFlowerActivity.this,
        // "你点击了infoWindow窗口" + marker.getTitle(), 1).show();
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void toHere(final double longitude, final double latitude) {
        // TODO Auto-generated method stub
        if (longitude <= 0 || longitude <= 0) {
            Toast.makeText(FindFlowerActivity.this, "该区域未设置经纬度，暂不支持导航",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new ActionSheetDialog(FindFlowerActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                // .addSheetItem("打开百度地图", SheetItemColor.Red,
                // new OnSheetItemClickListener() {
                //
                // @SuppressWarnings("deprecation")
                // @Override
                // public void onClick(int which) {
                // // TODO Auto-generated method stub
                // if (isAvilible(FindFlowerActivity.this,
                // "com.baidu.BaiduMap") == true) {
                // // 移动APP调起Android百度地图方式举例
                // Intent intent;
                // try {
                // String to = "intent://map/marker?location="
                // + latitude
                // + ","
                // + longitude
                // + "&title="
                // + "要去的地方&content="
                // + getIntent().getStringExtra(
                // "title")
                // +
                // "&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                // intent = Intent.getIntent(to);
                // startActivity(intent); // 启动调用
                // } catch (URISyntaxException e) {
                // // TODO Auto-generated catch
                // // block
                // e.printStackTrace();
                // }
                //
                // } else {
                // Toast.makeText(FindFlowerActivity.this,
                // "未安装百度地图", Toast.LENGTH_SHORT)
                // .show();
                // }
                //
                // }
                // })
                .addSheetItem("打开高德地图", SheetItemColor.Red,
                        new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {
                                // TODO Auto-generated method stub
                                if (isAvilible(FindFlowerActivity.this,
                                        "com.autonavi.minimap") == true) {
                                    String to = "androidamap://viewMap?sourceApplication=妈比网&poiname="
                                            + getIntent().getStringExtra(
                                            "title")
                                            + "&lat="
                                            + latitude
                                            + "&lon="
                                            + longitude
                                            + "&dev=0";
                                    Intent intent = new Intent(
                                            "android.intent.action.VIEW",
                                            android.net.Uri.parse(to));
                                    intent.setPackage("com.autonavi.minimap");
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(FindFlowerActivity.this,
                                            "未安装高德地图", Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }
                        })
                .addSheetItem("打开本机地图", SheetItemColor.Red,
                        new OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {
                                Intent localIntent = new Intent(
                                        "android.intent.action.VIEW",
                                        Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
                                                + longitude + "," + latitude));
                                FindFlowerActivity.this
                                        .startActivity(localIntent);

                            }
                        }).show();
        // 本地地图

    }

    private boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    public void toCall(final String tel) {
        if ("".equals(tel)) {
            Toast.makeText(FindFlowerActivity.this, "该苗圃未设置联系电话",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog(FindFlowerActivity.this).builder().setTitle(tel)
                .setPositiveButton("呼叫", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + tel));
//						Intent intent = new Intent(Intent.ACTION_DIAL, Uri
//								.parse("tel:" + tel));
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
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
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == 1) {
            MapNurseryList mapNurseryList = (MapNurseryList) data
                    .getSerializableExtra("mapNurseryList");

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("");
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(bitmapDescriptor);
            markerOptions.position(new LatLng(mapNurseryList.latitude,
                    mapNurseryList.longitude));
            Marker marker = aMap.addMarker(markerOptions);
            HashMap<Marker, MapNurseryList> sHashMap = new HashMap<Marker, MapNurseryList>();
            sHashMap.put(marker, mapNurseryList);
            Utils.sArrayList.add(sHashMap);
            Utils.markers.add(marker);

            // 定位到该点
            CameraUpdate cameraUpate = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapNurseryList.latitude,
                            mapNurseryList.longitude),
                    aMap.getCameraPosition().zoom);
            aMap.animateCamera(cameraUpate);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
