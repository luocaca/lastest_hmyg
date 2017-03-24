package com.hy.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonGetInfo {

	public static String getJsonString(JSONObject jsonObject, String key) {
		String value = "";
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	public static boolean getJsonBoolean(JSONObject jsonObject, String key) {
		boolean value = false;
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getBoolean(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	public static int getJsonInt(JSONObject jsonObject, String key) {
		int value = 0;
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getInt(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	public static long getJsonLong(JSONObject jsonObject, String key) {
		long value = 1000l;
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getLong(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	public static double getJsonDouble(JSONObject jsonObject, String key) {
		double value = 0;
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getDouble(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	public static JSONArray getJsonArray(JSONObject jsonObject, String key) {
		JSONArray value = new JSONArray();
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getJSONArray(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	public static JSONObject getJSONObject(JSONObject jsonObject, String key) {
		JSONObject value = new JSONObject();
		if (!jsonObject.isNull(key)) {
			try {
				value = jsonObject.getJSONObject(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}
}
