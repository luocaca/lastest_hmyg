package com.photo.choosephotos.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.white.utils.ImageSoftReference;

public class CommonBitmapUtil {

	/** 原来的形状 */
	public static final int SHAPE_TYPE_SOURCE = 0;
	/** 圆形 */
	public static final int SHAPE_TYPE_CIRCLE = 1;
	/** 方形 */
	public static final int SHAPE_TYPE_RECT = 2;
	/** 缓存图片map,key为path+lastModify */
	private static HashMap<String, ImageSoftReference> cacheMap = new HashMap<String, ImageSoftReference>();

	/** 软引用队列 */
	private static ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
	/** 发送图片的最大大小 */
	public static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;
	/**
	 * 从缓存获得图片
	 * 
	 * @param paramString
	 * @return
	 */
	public static Bitmap getFromCache(String paramString) {
		Bitmap localBitmap = null;
		ImageSoftReference localabt2 = (ImageSoftReference) cacheMap
				.get(paramString);
		if (localabt2 != null) {
			localBitmap = localabt2.get();
		}
		return localBitmap;
	}

	/**
	 * 增加图像到缓存
	 * 
	 * @param key
	 * @param bm
	 */
	public static void addBitmap(String key, Bitmap bm) {
		ImageSoftReference localabt = new ImageSoftReference(key, bm, queue);
		cleanCache();
		cacheMap.put(key, localabt);
	}

	/**
	 * 移除缓存中的图像
	 * 
	 * @param key
	 * @param bm
	 */
	public static void removeBitmap(String key) {
		cacheMap.remove(key);
	}

	/**
	 * 清楚map对象
	 */
	public static void clearMap() {
		cacheMap.clear();
	}

	/**
	 * 清空无用的引用
	 */
	private static void cleanCache() {
		while (true) {
			ImageSoftReference localabt1 = (ImageSoftReference) queue.poll();
			if (localabt1 == null)
				break;
			cacheMap.remove(localabt1.key);
		}
	}

	protected static String createBitmapCacheKey(String name, int shape,
			int limitW, int limitH, float scale, int rotation) {
		return name + "_" + shape + "_" + limitW + "_" + limitH + "_" + scale
				+ "_" + rotation;
	}

	/**
	 * 新建一张图ARGB_4444
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmap(int width, int height) {
		Bitmap stateBm = null;
		try {
			stateBm = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_4444);
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			cleanCache();
			Thread.yield();
			try {
				stateBm = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_4444);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
			return stateBm;
		}
		return stateBm;
	}

	/**
	 * 新建一张图ARGB_8888
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmapArgb888(int width, int height) {
		Bitmap stateBm = null;
		try {
			stateBm = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			cleanCache();
			Thread.yield();
			try {
				stateBm = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
			return stateBm;
		}
		return stateBm;
	}

	/**
	 * 获得Assert图片 保存至缓存
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static Bitmap decodeAssertFile(Context context, String name)
			throws IOException {
		String key = createBitmapCacheKey(name, SHAPE_TYPE_SOURCE, 0, 0, 0, 0);
		Bitmap bm = getFromCache(key);
		if (bm != null)
			return bm;
		if (name == null || name.equals("")) {
			return null;
		}
		InputStream is = context.getAssets().open(name);
		try {
			bm = BitmapFactory.decodeStream(is);
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				bm = BitmapFactory.decodeStream(is);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
		} finally {
			if (is != null)
				is.close();
		}
		if (bm != null)
			addBitmap(key, bm);
		return bm;
	}

	/**
	 * 获得Assert图片 未保存至缓存
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static Bitmap decodeAssertFile2(Context context, String name)
			throws IOException {
		InputStream is = context.getAssets().open(name);
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeStream(is);
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				bm = BitmapFactory.decodeStream(is);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
			is.close();
			return bm;
		}
		is.close();
		return bm;
	}

	/**
	 * 获得资源图片
	 * 
	 * @param resId
	 * @param n
	 * @param context
	 * @return
	 */
	public static Bitmap decodeResource(Context context, int resId, float scale) {
		String key = createBitmapCacheKey(resId + "", SHAPE_TYPE_SOURCE, 0, 0,
				scale, 0);
		Bitmap bmp = getFromCache(key);
		if (bmp != null) {
			return bmp;
		}
		Bitmap b = null;
		try {
			bmp = BitmapFactory.decodeResource(context.getApplicationContext()
					.getResources(), resId);
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);// 缩小图像
			b = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
					matrix, true);
			if (b != null)
				addBitmap(key, b);
			return b;
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				bmp = BitmapFactory.decodeResource(context
						.getApplicationContext().getResources(), resId);
				Matrix matrix = new Matrix();
				matrix.postScale(0.7f, 0.7f);// 缩小图像
				b = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
				if (b != null)
					addBitmap(key, b);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
			return b;
		}
	}

	/**
	 * 获得资源图片
	 * 
	 * @param resId
	 * @param n
	 * @param context
	 * @return
	 */
	public static Bitmap decodeResource(Context context, int resId,
			int limitHeight) {
		String key = createBitmapCacheKey(resId + "", SHAPE_TYPE_SOURCE,
				limitHeight, limitHeight, 0, 0);
		Bitmap bmp = getFromCache(key);
		if (bmp != null) {
			return bmp;
		}
		Bitmap b = null;
		try {
			bmp = BitmapFactory.decodeResource(context.getApplicationContext()
					.getResources(), resId);
			float scale = (float) limitHeight / bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);// 缩小图像
			b = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
					matrix, true);
			if (b != null)
				addBitmap(key, b);
			return b;
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				bmp = BitmapFactory.decodeResource(context
						.getApplicationContext().getResources(), resId);
				Matrix matrix = new Matrix();
				matrix.postScale(0.7f, 0.7f);// 缩小图像
				b = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
				if (b != null)
					addBitmap(key, b);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
			return b;
		}
	}

	/**
	 * 根据资源ID获取缓存图片
	 * 
	 * @param resId
	 *            资源ID
	 * @param key
	 *            缓存关键字
	 * @param limitH
	 *            高
	 * @param limitW
	 *            宽
	 * @return
	 */
	public static Bitmap getBitmapByResIdAndKey(Context context, int resId,
			int limitW, int limitH) {
		String key = createBitmapCacheKey(resId + "", SHAPE_TYPE_SOURCE,
				limitW, limitH, 0, 0);
		Bitmap bitmap = getFromCache(key);
		if (bitmap != null)
			return bitmap;
		bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
		if (bitmap == null)
			return null;
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * limitW / bitmap.getWidth();
		float sy = (float) 2 * limitH / bitmap.getHeight();
		m.setScale(sx, sy);
		try {
			// 重新构造一个60*60的图片
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, false);
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				// 重新构造一个60*60的图片
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, false);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
			}
			if (bitmap != null)
				addBitmap(key, bitmap);
			return bitmap;
		}
		if (key != null)
			addBitmap(key, bitmap);
		return bitmap;
	}

	/**
	 * 获得文件图片的尺寸变化
	 * 
	 * @param paramString
	 * @param n
	 * @param context
	 * @return
	 */
	public static Bitmap decodeFile(String path, float n) {
		File localFile = new File(path);
		String key = createBitmapCacheKey(
				path + "_" + localFile.lastModified(), SHAPE_TYPE_SOURCE, 0, 0,
				n, 0);
		Bitmap bmp = getFromCache(key);
		if (bmp != null) {
			return bmp;
		}
		Bitmap b = null;
		try {
			bmp = BitmapFactory.decodeFile(path);
			Matrix matrix = new Matrix();
			matrix.postScale(n, n);// 缩小图像
			if (bmp == null) {
				// 如果文件的图片不存在
				return null;
			}
			b = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
					matrix, true);
			if (b != null)
				addBitmap(key, b);
			return b;
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				bmp = BitmapFactory.decodeFile(path);
				Matrix matrix = new Matrix();
				matrix.postScale(n, n);// 缩小图像
				if (bmp == null) {
					return null;
				}
				b = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						bmp.getHeight(), matrix, true);
				if (b != null)
					addBitmap(key, b);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
				clearMap();
			}
			return b;
		}
	}

	/**
	 * 生成bitmap
	 * 
	 * @param paramString
	 * @param paramOptions
	 * @return
	 */
	public static Bitmap decodeFile(String path,
			BitmapFactory.Options paramOptions, int limitW, int limitH) {
		File localFile = new File(path);
		String key = "";
		if (paramOptions == null) {
			key = createBitmapCacheKey(path + "_" + localFile.lastModified(),
					SHAPE_TYPE_SOURCE, 0, 0, 0, 0);
		} else {
			key = createBitmapCacheKey(path + "_" + localFile.lastModified(),
					SHAPE_TYPE_SOURCE, limitW, limitH, 0, 0);
		}
		Bitmap localBitmap = getFromCache(key);
		if (localBitmap != null)
			return localBitmap;
		try {
			localBitmap = BitmapFactory.decodeFile(path, paramOptions);
			if (paramOptions == null || !paramOptions.inJustDecodeBounds)
				addBitmap(key, localBitmap);
			return localBitmap;
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				localBitmap = BitmapFactory.decodeFile(path, paramOptions);
				if (paramOptions == null || !paramOptions.inJustDecodeBounds)
					addBitmap(key, localBitmap);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
				clearMap();
			}
			return localBitmap;
		}
	}

	/**
	 * 根据压缩后像素大小获取压缩后图片
	 * 
	 * @param limitH
	 *            最大像素高
	 * @param limitW
	 *            最大像素宽
	 * @return
	 */
	public static Bitmap converBitmap(File file, int limitW, int limitH) {
		// 设置inJustDecodeBounds为true,这样decode的时候只计算高和宽，而不加载真正的图片，
		// 宽高具体在opts里。
		// 使用decodeFile方法得到图片的宽和高
		// 计算缩放比例，如果比最大高宽小则设为1
		String key = createBitmapCacheKey(
				file.getAbsolutePath() + "_" + file.lastModified(),
				SHAPE_TYPE_SOURCE, limitW, limitH, 0, 0);
		Bitmap localBitmap = getFromCache(key);
		if (localBitmap != null) {
			return localBitmap;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bm = decodeFile(file.getAbsolutePath(), opts, limitH, limitW);
		int sampleHeight = opts.outHeight / limitH;
		int sampleWidth = opts.outWidth / limitW;
		int sampleSize = sampleHeight > sampleWidth ? sampleHeight
				: sampleWidth;
		if (sampleSize < 1) {
			sampleSize = 1;
		}
		// 设置图片属性
		opts.inSampleSize = sampleSize;
		opts.inJustDecodeBounds = false;
		// 未知下面两个属性的含义
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		bm = decodeFile(file.getAbsolutePath(), opts, limitH, limitW);
		return bm;
	}

	/**
	 * 如果是静态图片，则进行压缩处理, 并旋转 传入前保证file不为空
	 * 
	 * @param rotate
	 *            TODO
	 */
	public static Bitmap CompressAndRomateImg(File file, int limitW,
			int limitH, int romate) throws IOException {
		String key = createBitmapCacheKey(
				file.getAbsolutePath() + "_" + file.lastModified(),
				SHAPE_TYPE_SOURCE, limitW, limitH, 0, romate);
		Bitmap localBitmap = getFromCache(key);
		if (localBitmap != null) {
			return localBitmap;
		}
		/** 用于压缩的原图Image */
		Bitmap bitmapSourceImg;
		bitmapSourceImg = converBitmap(file, limitW, limitH);
		bitmapSourceImg = rotate(file.getAbsolutePath(), bitmapSourceImg,
				romate, limitW, limitH);
		if (bitmapSourceImg != null)
			addBitmap(key, bitmapSourceImg);
		// 存储临时文件
		return bitmapSourceImg;
	}

	/**
	 * 将两个bitmap合成为一张bitmap
	 * 
	 * @param background
	 * @param foreground
	 * @return
	 */
	public static Bitmap toCombinBitmap(Bitmap background, Bitmap foreground,
			int left, int top) {
		if (background == null) {
			return null;
		}
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		// int fgWidth = foreground.getWidth();
		// int fgHeight = foreground.getHeight();
		// create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
		Bitmap newbmp = Bitmap
				.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas cv = new Canvas(newbmp);
		// draw bg into
		cv.drawBitmap(background, 0, 0, null);// 在 0，0坐标开始画入bg
		// draw fg into
		cv.drawBitmap(foreground, left, top, null);// 在 0，0坐标开始画入fg ，可以从任意位置画入
		return newbmp;
	}

	/**
	 * 旋转图片
	 * 
	 * @param b
	 */
	public static Bitmap rotate(String paramString, Bitmap b, int degrees,
			int limitH, int limitW) {
		String key = createBitmapCacheKey(paramString, SHAPE_TYPE_SOURCE,
				limitW, limitH, 0, degrees);
		if (degrees == 0) {
			return b;
		}
		Bitmap localBitmap = getFromCache(key);
		if (localBitmap != null)
			return localBitmap;
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth(), (float) b.getHeight());
			try {
				localBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				addBitmap(key, localBitmap);
			} catch (OutOfMemoryError ex) {
				return b;
			}
		}
		return localBitmap;
	}

	/**
	 * 对图片添加圆角效果
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedBitmap(Bitmap bitmap, float roundPx) {
		// 缩放图片
		Matrix m = new Matrix();
		// 重新构造一个40*40的图片
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), m, false);
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xfff5f5f5;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// 圆角半径
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
