package com.zzy.flowers.data.entity.image;

import android.graphics.Bitmap;

public class UploadImageData {
	public static final int IMAGE_POS_COMPANY = 0;
	public static final int IMAGE_POS_FIRST = 1;
	public static final int IMAGE_POS_SECOND = 2;
	/** 2:企业图片 1:第一张 2:第二张 */
	private int pos;

	private Bitmap showBitmap;
	
	private String picPath = "";
	
	private ImageFileData resultData;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	public ImageFileData getResultData() {
		return resultData;
	}

	public void setResultData(ImageFileData resultData) {
		this.resultData = resultData;
	}

	@Override
	public String toString() {
		return "UploadImageData [pos=" + pos + ", picPath=" + picPath
				+ ", resultData=" + resultData + "]";
	}
}
