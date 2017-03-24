package com.white.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class FileUtil extends CommonFileUtil {
	/** 图片文件目录 */
	public static final String IMAGE_DIR = "Flowers/image";

	/**
	 * 得到本机SD卡的存储目录--需要判断空间
	 * 
	 * @return
	 */
	public static String getPath_Space(Context context) {
		if (getExternalPath() != null && FileUtil.getExternalStorageSize() > 0) {
			File dir = new File(getExternalPath() + "/hmyg");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			return getExternalPath() + "/hmyg";
		} else {
			// 设置手机程序内部存储空间目录
			return getInternalPath_Space(context);
		}
	}

	/**
	 * 获得图片路径
	 * 
	 * @return
	 */
	public static String getFlowerPicPath(String filename) {
		String filepath = mkDirs(IMAGE_DIR) + "/" + filename;
		return filepath;
	}

	/**
	 * 得到本机SD卡的目录
	 * 
	 * @return
	 */
	public static String getExternalPath() {
		return (FileUtil.getExternalStorageDirectory() == null) ? null
				: FileUtil.getExternalStorageDirectory().getPath();
	}

	/**
	 * 得到本机程序内部的存储目录--需要判断空间
	 * 
	 * @return
	 */
	public static String getInternalPath_Space(Context context) {
		if (FileUtil.getInternalStorageSizeByPath(context.getFilesDir()
				.getParent()) >= MIN_SPACE_SIZE) {
			return context.getFilesDir().getPath();
		} else
			return null;

	}

	/**
	 * 得到本机程序内部的存储目录--不需要判断空间
	 * 
	 * @return
	 */
	public static String getInternalPath_NoSpace(Context context) {
		return context.getFilesDir().getPath();
	}

	public static String saveMyBitmap(String bitName, Bitmap mBitmap)
			throws IOException {
		String img_path = "";
		img_path = FileUtil.getFlowerPicPath("") + "/" + "flower_image_"
				+ System.currentTimeMillis() + ".png";
		File f = new File(img_path);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img_path;
	}

}
