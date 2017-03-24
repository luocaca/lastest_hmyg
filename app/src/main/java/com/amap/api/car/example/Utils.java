/**  
 * Project Name:Android_Car_Example  
 * File Name:Utils.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月7日下午3:43:05  
 *  
 */

package com.amap.api.car.example;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.hldj.hmyg.R;
import com.hy.utils.GetServerUrl;
import com.hy.utils.JsonGetInfo;

/**
 * ClassName:Utils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2015年4月7日 下午3:43:05 <br/>
 * 
 * @author yiyi.qi
 * @version
 * @since JDK 1.6
 * @see
 */

public class Utils {

	public static ArrayList<HashMap<Marker, MapNurseryList>> sArrayList = new ArrayList<HashMap<Marker, MapNurseryList>>();
	public static ArrayList<Marker> markers = new ArrayList<Marker>();
	public static BitmapDescriptor bitmapDescriptor;

	/**
	 * 添加模拟测试的车的点
	 */
	public static void addEmulateData(final AMap amap, LatLng center,
			final Activity activity) {
		// 根据缩放比判断显示大区域还是显示小区域
		if (amap.getCameraPosition().zoom >= 1) {

			View view = LayoutInflater.from(activity).inflate(
					R.layout.custom_amap_maker, null);
			bitmapDescriptor = BitmapDescriptorFactory
					.fromResource(R.drawable.dw_taren);

			FinalHttp finalHttp = new FinalHttp();
			GetServerUrl.addHeaders(finalHttp,false);
			AjaxParams params = new AjaxParams();
			finalHttp.post(GetServerUrl.getUrl() + "map/seedling/nurseryList",
			// finalHttp.post("http://192.168.1.183:83/api/map/seedling/nurseryList",
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
									Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
								}
								if ("1".equals(code)) {

									JSONObject jsonObject2 = jsonObject
											.getJSONObject("data");
									if (JsonGetInfo.getJsonArray(jsonObject2,
											"nurseryList").length() > 0) {
										JSONArray jsonArray = JsonGetInfo
												.getJsonArray(jsonObject2,
														"nurseryList");
										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject jsonObject3 = jsonArray
													.getJSONObject(i);
											MapNurseryList mList = new MapNurseryList();
											mList.setId(JsonGetInfo
													.getJsonString(jsonObject3,
															"id"));
											mList.setCreateBy(JsonGetInfo
													.getJsonString(jsonObject3,
															"createBy"));
											mList.setCreateDate(JsonGetInfo
													.getJsonString(jsonObject3,
															"createDate"));
											mList.setCiCode(JsonGetInfo
													.getJsonString(jsonObject3,
															"cityCode"));
											mList.setCityName(JsonGetInfo
													.getJsonString(jsonObject3,
															"cityName"));
											mList.setPrCode(JsonGetInfo
													.getJsonString(jsonObject3,
															"prCode"));
											mList.setCiCode(JsonGetInfo
													.getJsonString(jsonObject3,
															"ciCode"));
											mList.setCoCode(JsonGetInfo
													.getJsonString(jsonObject3,
															"coCode"));
											mList.setTwCode(JsonGetInfo
													.getJsonString(jsonObject3,
															"twCode"));
											mList.setName(JsonGetInfo
													.getJsonString(jsonObject3,
															"name"));
											mList.setDetailAddress(JsonGetInfo
													.getJsonString(jsonObject3,
															"detailAddress"));
											mList.setContactName(JsonGetInfo
													.getJsonString(jsonObject3,
															"contactName"));
											mList.setContactPhone(JsonGetInfo
													.getJsonString(jsonObject3,
															"contactPhone"));
											mList.setNurseryArea(JsonGetInfo
													.getJsonDouble(jsonObject3,
															"nurseryArea"));
											mList.setType(JsonGetInfo
													.getJsonString(jsonObject3,
															"type"));
											mList.setMainType(JsonGetInfo
													.getJsonString(jsonObject3,
															"mainType"));
											mList.setLongitude(JsonGetInfo
													.getJsonDouble(jsonObject3,
															"longitude"));
											mList.setLatitude(JsonGetInfo
													.getJsonDouble(jsonObject3,
															"latitude"));
											mList.setSeedlingCountJson(JsonGetInfo
													.getJsonInt(jsonObject3,
															"seedlingCountJson"));
											mList.setDelete(JsonGetInfo
													.getJsonBoolean(
															jsonObject3,
															"delete"));
											mList.setLocationOdd(JsonGetInfo
													.getJsonBoolean(
															jsonObject3,
															"locationOdd"));
											mList.setCompanyName(JsonGetInfo
													.getJsonString(jsonObject3,
															"companyName"));
											MarkerOptions markerOptions = new MarkerOptions();
											markerOptions.title("");
											markerOptions.anchor(0.5f, 0.5f);
											markerOptions
													.icon(bitmapDescriptor);
											markerOptions.position(new LatLng(
													mList.latitude,
													mList.longitude));
											Marker marker = amap
													.addMarker(markerOptions);
											HashMap<Marker, MapNurseryList> sHashMap = new HashMap<Marker, MapNurseryList>();
											sHashMap.put(marker, mList);
											sArrayList.add(sHashMap);
											markers.add(marker);
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
							super.onFailure(t, errorNo, strMsg);
						}

					});

		}

//		MarkerOptions markerOptions = new MarkerOptions();
//		markerOptions.setFlat(true);
//		markerOptions.anchor(0.5f, 0.5f);
//		markerOptions.position(center);
//		markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//				.decodeResource(activity.getResources(), R.drawable.dw_ziji)));
//
//		Marker marker = amap.addMarker(markerOptions);
	}

	private Bitmap getViewBitmap(View addViewContent) {

		addViewContent.setDrawingCacheEnabled(true);

		addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(),
				addViewContent.getMeasuredHeight());

		addViewContent.buildDrawingCache();
		Bitmap cacheBitmap = addViewContent.getDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

		return bitmap;
	}
}
