package com.white.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.TextPaint;
import android.util.FloatMath;

public class AndroidUtil {

	/**
	 * 判断当前客户端是否在前台运行
	 * 
	 * @param context
	 * @return false 后台运行 true 前台运行
	 */
	public static boolean isClientRunTop(Context context) {
		if (isInKeyguardRestrictedInputMode(context))// 锁屏
			return false;
		boolean result = false;
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> task_info = manager.getRunningTasks(20);
		if (task_info != null && task_info.size() > 0) {
			ComponentName cname = ((RunningTaskInfo) (task_info.get(0))).topActivity;
			if (cname != null
					&& context.getPackageName().equals(
							cname.getPackageName().trim())) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 判断当前是否处于锁屏
	 * 
	 * @param context
	 * @return false 否 true 是
	 */
	public static boolean isInKeyguardRestrictedInputMode(Context context) {
		KeyguardManager mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {// 锁屏
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断与包名相同的应用程序是否有安装
	 * 
	 * @param packageName
	 * @param context
	 * @return
	 */
	public static boolean isPackageExist(String packageName, Context context) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取设备imei号
	 */
	public static String getDeviceIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		/*
		 * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
		 * available.
		 */
		String result = tm.getDeviceId();
		return result == null ? ("" + System.currentTimeMillis()) : result;
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 回到桌面
	 * 
	 * @param context
	 *            当前环境上下文对象
	 */
	public static void backToHome(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}

	/**
	 * 获得版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pinfo = null;
		try {
			pinfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (pinfo != null)
			return pinfo.versionName;
		else
			return "";
	}
	
	
	public static String getVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pinfo = null;
		try {
			pinfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (pinfo != null)
			return pinfo.versionCode+"";
		else
			return "";
	}

	/**
	 * 获得屏幕超时设置
	 * 
	 * @param context
	 * @return
	 */
	public static int getSCREEN_OFF_TIMEOUT(Context context) {
		int time = 0;
		try {
			time = Settings.System.getInt(context.getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 重新设置屏幕超时
	 * 
	 * @param context
	 * @param time
	 *            毫秒为单位
	 */
	public static void setSCREEN_OFF_TIMEOUT(Context context, int time) {
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.SCREEN_OFF_TIMEOUT, time);
	}

	/**
	 * 获得提醒对话框(AlertDialog)的Builder对象
	 * 
	 * @param context
	 *            当前环境上下文对象
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @return 提醒对话框的Builder对象
	 */
	public static Builder createADBuilder(Context context, int title,
			int message) {
		return new Builder(context).setTitle(title).setMessage(message);
	}

	/**
	 * 获得提醒对话框(AlertDialog)的Builder对象
	 * 
	 * @param context
	 *            当前环境上下文对象
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @return 提醒对话框的Builder对象
	 */
	public static Builder createADBuilder(Context context, String title,
			int message) {
		return new Builder(context).setTitle(title).setMessage(message);
	}

	/**
	 * 获得提醒对话框(AlertDialog)的Builder对象
	 * 
	 * @param context
	 *            当前环境上下文对象
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @return 提醒对话框的Builder对象
	 */
	public static Builder createADBuilder(Context context, String title,
			String message) {
		return new Builder(context).setTitle(title).setMessage(message);
	}

	/**
	 * 将px值转化为dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** 获取对应字体的数字宽 */
	public static float getSingleFontWidth(float fontSize) {
		TextPaint itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		itemsPaint.setTextSize(14);
		return FloatMath.ceil(Layout.getDesiredWidth("0", itemsPaint));
	}

	/** 获取对应字体数字行高 */
	public static float getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (float) Math.ceil(fm.descent - fm.top);
	}

	/**
	 * 获取手机操作系统版本
	 * 
	 * @return
	 * @author SHANHY
	 * @date 2015年12月4日
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	/**
	 * 复制文字到剪贴板
	 */
	@SuppressWarnings("deprecation")
	public static void copyText2clipboard(Context context, int sdkVersion, String text) {
		if (getSDKVersionNumber() >= 11) {
			android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setText(text);
		} else {
			// 得到剪贴板管理器
			android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
		}
	}

}
