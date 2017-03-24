package com.white.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

public class SettingUtils {

	/**
	 * 获取包信息.
	 * 
	 * @param context
	 *            the context
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			String packageName = context.getPackageName();
			info = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static String getPackage(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.packageName;
		} catch (Exception e) {
		}
		return "";
	}

	public static void startMarket(Activity activity, Context context) {
		Uri uri = Uri.parse(String.format("market://details?id=%s",
				getPackageInfo(context).packageName));
		if (isIntentSafe(activity, uri)) {
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			// 没有安装市场
			Toast toast = Toast.makeText(context,
					"没有安装市场", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}
	}

	public static void launchBrowser(Activity from, String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		from.startActivity(intent);
	}

	public static boolean isIntentSafe(Activity activity, Uri uri) {
		Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				mapCall, 0);
		return activities.size() > 0;
	}

	/****************
	 * 
	 * 发起添加群流程。群号：短信轰炸神器(72134462) 的 key 为： g8mRRf3wT42Om3eIjnKcE4xDtlFp-r9C 调用
	 * joinQQGroup(g8mRRf3wT42Om3eIjnKcE4xDtlFp-r9C) 即可发起手Q客户端申请加群
	 * 短信轰炸神器(72134462)
	 * 
	 * @param key
	 *            由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public static boolean joinQQGroup(Activity activity, String key) {
		Intent intent = new Intent();
		intent.setData(Uri
				.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D"
						+ key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
		// //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			// 未安装手Q或安装的版本不支持
			return false;
		}
	}

}
