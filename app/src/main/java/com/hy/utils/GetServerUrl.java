package com.hy.utils;

import android.util.Log;

import com.hldj.hmyg.application.MyApplication;
import com.white.utils.AndroidUtil;

import net.tsz.afinal.FinalHttp;

/**
 * 获取服务器IP地址
 */

public class GetServerUrl {

	public static boolean isTest = false;
	static String PGYER = "http://www.pgyer.com/apiv1/app/viewGroup";
	// 正式 可用
	static String API_01 = "http://hmeg.cn:93/";
	static String FIR_01 = "http://api.fir.im/apps/latest/57882cfc748aac17af00001e?api_token=7b3d87a7cb04b3a1624abb900c045c22&type=android&bundle_id=com.hldj.hmyg";
	static String PGYER_UPLOAD_01 = "https://www.pgyer.com/hmeg3";
	static String aId_01 = "87045b497d4eaffc7621c1f2ef75a79f";
	static String _api_key_01 = "eb7b16eabaf1d9af652fc65e921ba205";
	static String uKey_01 = "424e35aa2abf90730b5de65b2d7896e4";

	
	// 正式 不可用
	// static String API = "http://api.hmeg.cn:81/";
	// static String FIR =
	// "http://api.fir.im/apps/latest/57882cfc748aac17af00001e?api_token=7b3d87a7cb04b3a1624abb900c045c22&type=android&bundle_id=com.hldj.hmyg";

	// 测试,xingguo.huang@qq.com
	static String API_03 = "http://test.hmeg.cn:93/";
	static String FIR_03 = "http://api.fir.im/apps/latest/574270cc00fc744aef000000?api_token=d5ec18bebb4cd5acd798ffeeccbed6f4&type=android&bundle_id=com.hldj.hmyg";
	static String PGYER_UPLOAD_03 = "https://www.pgyer.com/hmegandroid";
	static String aId_03 = "ca3d2e3158115aa784e85a145acdcb0f";
	static String _api_key_03 = "686295388c4b09f08725f897937c8dda";
	static String uKey_03 = "2c65837ad0174ea90979e16cf3cb0ca3";

	public static final String purchaseConfirm1 = "http://m.hmeg.cn/purchaseConfirm/confirm/detail?t=";
	public static final String purchaseConfirm3 = "http://192.168.1.252:8090/purchaseConfirm/confirm/detail?t=";

	public static String getPurchaseconfirm() {
		if (isTest) {
			return purchaseConfirm3;
		} else {
			return purchaseConfirm1;
		}
	}

	public static String getPGYER() {
		return PGYER;
	}

	public static String getPGYER_UPLOAD() {
		if (isTest) {
			return PGYER_UPLOAD_03;
		} else {
			return PGYER_UPLOAD_01;
		}
	}

	public static String getFIR() {
		if (isTest) {
			return FIR_03;
		} else {
			return FIR_01;
		}

	}

	public static String getUrl() {
		if (isTest) {
			return API_03;
			// return getTestUrl();
		} else {
			return API_01;
		}
	}

	public static String getaId() {
		if (isTest) {
			return aId_03;
		} else {
			return aId_01;
		}
	}

	public static String get_api_key() {
		if (isTest) {
			return _api_key_03;
		} else {
			return _api_key_01;
		}
	}

	public static String getuKey() {
		if (isTest) {
			return uKey_03;
		} else {
			return uKey_01;
		}
	}

	static String TEST_API = "http://192.168.1.20:83/api/";
	// static String TEST_API = "http://192.168.1.146:8080/api/";
	public final static String WEB = "http://www.hmeg.cn";
	public final static String ICON_PAHT = "http://p3.so.qhimg.com/sdr/449_800_/t0147220f71d4562943.jpg";
	public final static String TEST_URL = "https://m.baidu.com";
	public final static String Customer_Care_Phone = "4006579888";
	public final static String Customer_Care_QQ = "873528519";
	static String keyStr = "hmeg_api_~!@*(hmeg.cn";

	public static String getTestUrl() {
		return TEST_API;
	}

	public static String Ramdom16Str(long current_time) {
		String rad16str = current_time + "";
		rad16str = MD5.Md(rad16str, false);
		return rad16str;
	}

	public static String getKeyStr(long current_time) {
		String Key = "";
		String Salt = current_time + "";
		Salt = MD5.Md(Salt, false);
		Key = Salt + MD5.Md((keyStr + Salt), true);
		return Key;
	}

	public static void addHeaders(FinalHttp finalHttp, boolean needId) {
		// TODO Auto-generated method stub
		finalHttp.addHeader("token",
				GetServerUrl.getKeyStr(System.currentTimeMillis()));
		if (needId) {
			finalHttp.addHeader("authc",
					MyApplication.Userinfo.getString("id", ""));
		}
		finalHttp.addHeader("version", version);
		finalHttp.addHeader("deviceId", deviceId);
		finalHttp.addHeader("type", "Android");

		Log.e("authc", MyApplication.Userinfo.getString("id", ""));
	}

	public static String version = "";
	public static String deviceId = "";
}
