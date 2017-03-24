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

public class GetCodeByName {

	public static String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	public static String mCurrentZipCode = "";
	public static JSONObject jObject;

	/**
	 * 解析省市区的XML数据
	 */

	public static String initProvinceDatas(Context context, String first,
			String sec) {
		AssetManager asset = context.getAssets();
		try {
			InputStream input = asset.open("choose.json");
			byte[] buffer = new byte[input.available()];
			input.read(buffer);
			String json = new String(buffer, "utf-8");
			input.close();
			jObject = new JSONObject(json);
			JSONArray array = jObject.getJSONObject("data").getJSONArray(
					"bannerList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				if (first.equals(JsonGetInfo.getJsonString(jsonObject, "name"))) {
					mCurrentZipCode = JsonGetInfo.getJsonString(jsonObject,
							"cityCode");
					JSONArray jsonArray = jsonObject.getJSONArray("childs");
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(j);
						if (sec.equals(JsonGetInfo.getJsonString(jsonObject2,
								"name"))) {
							mCurrentZipCode = JsonGetInfo.getJsonString(
									jsonObject2, "cityCode");
						}
					}
				}

			}

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
		return mCurrentZipCode;
	}

	public static String initProvinceDatasDocument(Context context,
			String first, String sec) {
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
				if (first.equals(JsonGetInfo.getJsonString(jsonObject, "name"))) {
					mCurrentZipCode = JsonGetInfo.getJsonString(jsonObject,
							"cityCode");
					JSONArray jsonArray = jsonObject.getJSONArray("childs");
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(j);
						if (sec.equals(JsonGetInfo.getJsonString(jsonObject2,
								"name"))) {
							mCurrentZipCode = JsonGetInfo.getJsonString(
									jsonObject2, "cityCode");
						}
					}
				}

			}

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
		return mCurrentZipCode;
	}

	/**
	 * 根据省市区获取item行数
	 * 
	 */
	public static int first_item = 0;
	public static int sec_item = 0;
	public static int third_item = 0;
	static int item_num[] = new int[] { first_item, sec_item, third_item };

	public static int[] getItemNum(Context context, String code) {
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
				if (code.startsWith(JsonGetInfo.getJsonString(jsonObject,
						"cityCode"))) {
					first_item = i;
					JSONArray jsonArray = jsonObject.getJSONArray("childs");
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(j);
						if (code.startsWith(JsonGetInfo.getJsonString(
								jsonObject2, "cityCode"))) {
							sec_item = j;
							JSONArray jsonArray2 = jsonObject.getJSONArray("childs");
							for (int k = 0; k < jsonArray2.length(); k++) {
								JSONObject jsonObjectk= jsonArray2.getJSONObject(k);
								if (code.startsWith(JsonGetInfo.getJsonString(
										jsonObjectk, "cityCode"))) {
									third_item = k;
								}
							}
						}
					}
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
		return new int[] { first_item, sec_item, third_item };
	}

}
