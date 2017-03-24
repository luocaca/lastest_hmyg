package com.white.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.zzy.flowers.activity.photoalbum.PhotoItem;

public class GlobalData {
	/** 是否正在运行 */
	public static boolean isActivity = true;
	/** 发布图片手势切换图片浏览器的数据源 */
	public static List<PhotoItem> galleryImageData = new ArrayList<PhotoItem>();

	/**
	 * 获得屏幕高
	 * 
	 * @param islandscape
	 *            TODO
	 */
	public static int getScreenHeight(boolean islandscape, Context context) {
		if (islandscape) {
			return getScreenWidth(false, context);
		}
		if (SystemSetting.getInstance(context).screenHeight != 0)
			return SystemSetting.getInstance(context).screenHeight;
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		SystemSetting.getInstance(context).setGlobalScreenHeight(
				Math.max(display.getWidth(), display.getHeight()));
		return SystemSetting.getInstance(context).screenHeight;
	}

	/**
	 * 获得屏幕宽
	 * 
	 * @param islandscape
	 *            TODO
	 */
	public static int getScreenWidth(boolean islandscape, Context context) {
		if (islandscape) {
			return getScreenHeight(false, context);
		}
		if (SystemSetting.getInstance(context).screenWidth != 0)
			return SystemSetting.getInstance(context).screenWidth;
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		SystemSetting.getInstance(context).setGlobalScreenWidth(
				Math.min(display.getWidth(), display.getHeight()));
		return SystemSetting.getInstance(context).screenWidth;
	}

	/**
	 * 获得可见屏幕高，包括状态栏
	 * 
	 * @param islandscape
	 *            TODO
	 */
	public static int getScreenVisiableHeight(Context context) {
		if (SystemSetting.getInstance(context).screenVisiableHeight != 0)
			return SystemSetting.getInstance(context).screenVisiableHeight;
		if (SystemSetting.getInstance(context).screenVisiableHeight != 0)
			return SystemSetting.getInstance(context).screenVisiableHeight;
		return 0;
	}

}
