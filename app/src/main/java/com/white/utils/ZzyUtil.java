package com.white.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

public class ZzyUtil {

	/** 图片存储最小SDCARD空间 */
	public static final int MIN_SDCARD_AVAILABLE_SIZE = 5 * 1024 * 1024;

	public static String getBaseActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> task_info = manager.getRunningTasks(20);
		if (task_info != null && task_info.size() > 0) {
			ComponentName cname = ((RunningTaskInfo) (task_info.get(0))).baseActivity;
			return cname.getClassName();
		}
		return "";
	}

	/**
	 * 是否处于免打扰时间
	 */
//	public static boolean isInNodisturbTime() {
//		boolean result = false;
//		String nowTime = DateUtil.DateToString2(new Date());
//		String disturbStartTime = DateUtil.convertTimeToStr(SystemSetting
//				.getInstance().doNotDisturb_startTime_hour)
//				+ ":"
//				+ DateUtil
//						.convertTimeToStr(SystemSetting.getInstance().doNotDisturb_startTime_minute);
//		String disturbEndTime = DateUtil.convertTimeToStr(SystemSetting
//				.getInstance().doNotDisturb_endTime_hour)
//				+ ":"
//				+ DateUtil
//						.convertTimeToStr(SystemSetting.getInstance().doNotDisturb_endTime_minute);
//		if (disturbStartTime.compareTo(disturbEndTime) <= 0) {// 设置免打扰开始时间比结束时间早
//			if (nowTime.compareTo(disturbStartTime) >= 0
//					&& nowTime.compareTo(disturbEndTime) <= 0) {// 处于免打扰时间内
//				result = true;
//			}
//		} else {// 设置免打扰开始时间比结束时间晚
//			if (nowTime.compareTo(disturbStartTime) >= 0
//					|| nowTime.compareTo(disturbEndTime) <= 0) {// 处于免打扰时间内
//				result = true;
//			}
//		}
//		return result;
//	}
	
	/**
	 * 获得崩溃信息
	 * 
	 * @param ex
	 *            异常对象
	 * @return 奔溃信息字符串
	 */
	public static String dumpThrowable(Throwable ex) {
		StringWriter out = new StringWriter();
		ex.printStackTrace(new PrintWriter(out));
		return out.toString();
	}


	/**
	 * 判断SDCARD空间是否>5M
	 * 
	 * @param shouldToast
	 *            是否要toast提示
	 * @return
	 */
	public static boolean ToastForSdcardSpaceEnough(Context context,boolean shouldToast) {
		if (FileUtil.getExternalStorageSize() > MIN_SDCARD_AVAILABLE_SIZE) {
			return true;
		} else {
			if (shouldToast) {
			}
			return false;
		}
	}


}
