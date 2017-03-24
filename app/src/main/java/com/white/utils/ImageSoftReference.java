package com.white.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

/**
 * 图片软引用类
 * 
 * @author Administrator
 * 
 */
public class ImageSoftReference extends SoftReference<Bitmap> {

	public final String key;

	public ImageSoftReference(String key, Bitmap r,
			ReferenceQueue<? super Bitmap> q) {
		super(r, q);
		this.key = key;
	}
}