package com.mrwujay.cascade.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import com.hy.utils.JsonGetInfo;
import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.JsonParserHandler;

public class GetCitiyNameByCode {

	public static String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	public static String mCurrentZipCode = "";
	public static JSONObject jObject;

	/**
	 * 解析省市区的XML数据
	 */

	public static String initProvinceDatas(Context context, String code) {
		AssetManager asset = context.getAssets();
		try {
			InputStream input = asset.open("document.json");
			byte[] buffer = new byte[input.available()];
			input.read(buffer);
			String json = new String(buffer, "utf-8");
			input.close();
			jObject = new JSONObject(json);
			JSONArray array = jObject.getJSONObject("data").getJSONArray(
					"bannerList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				JSONArray jsonArray = jsonObject.getJSONArray("childs");
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(j);
					JSONArray jsonArray2 = JsonGetInfo.getJsonArray(
							jsonObject2, "childs");
					if (jsonArray2.length() > 0) {
						for (int k = 0; k < jsonArray2.length(); k++) {
							JSONObject jsonObject3 = jsonArray2
									.getJSONObject(k);
							if (code.substring(0, 6).equals(
									JsonGetInfo.getJsonString(jsonObject3,
											"cityCode"))) {
								mCurrentDistrictName = JsonGetInfo
										.getJsonString(jsonObject3, "fullName");
							}
						}
					} else {
					}
				}

			}

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
		return mCurrentDistrictName;
	}

}
