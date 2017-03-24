package com.hy.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class GetLotLat {

	private static Context context;
	private final static String ak = "DKkkIgLYIEE1n3ANbW27EzGEpmlTxKLG";

	public static void getBaiduLotLat(Context context, final Editor e) {

		GetLotLat.context = context;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		finalHttp.get(
				"http://api.map.baidu.com/location/ip?ak=<ak>&coor=bd09ll"
						.replace("<ak>", ak), params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						try {
							JSONObject jsonObject = new JSONObject(t.toString());
							JSONObject content = JsonGetInfo.getJSONObject(
									jsonObject, "content");
							JSONObject point = JsonGetInfo.getJSONObject(
									content, "point");
							String x = JsonGetInfo.getJsonString(point, "x");
							String y = JsonGetInfo.getJsonString(point, "y");
//							e.putString("latitude", x);
//							e.putString("longitude", y);
//							e.commit();
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

	public static void getIpAd(Context context) {

		GetLotLat.context = context;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		finalHttp.get("http://pv.sohu.com/cityjson?ie=utf-8", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						String string = t.toString()
								.replace("var returnCitySN =", "")
								.replace(";", "").toString();
						try {
							JSONObject jsonObject = new JSONObject(string);
							String cip = JsonGetInfo.getJsonString(jsonObject,
									"cip");
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

	/**
	 * 添加模拟测试的车的点
	 */
	public static void getIpInfo(Context context) {

		GetLotLat.context = context;
		FinalHttp finalHttp = new FinalHttp();
		GetServerUrl.addHeaders(finalHttp, false);
		AjaxParams params = new AjaxParams();
		finalHttp.get("http://ip.taobao.com/service/getIpInfo.php?ip="
				+ getIp(context), params, new AjaxCallBack<Object>() {

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}

		});

	}

	// 得到本机ip地址 获取GSM网络的IP地址
	public static String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ipaddress = "本机的ip是" + "：" + ip.getHostAddress();
					}
				}

			}
		} catch (SocketException e) {
			Log.e("feige", "获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;

	}

	// 获取WIFI网络的IP地址
	public static String getIp(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();

		// 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109
		String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
		return ip;
	}

}
