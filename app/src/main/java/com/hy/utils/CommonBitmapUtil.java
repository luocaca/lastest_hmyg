package com.hy.utils;

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

	public static final int SHAPE_TYPE_SOURCE = 0;
	public static final int SHAPE_TYPE_CIRCLE = 1;
	public static final int SHAPE_TYPE_RECT = 2;
	private static HashMap<String, ImageSoftReference> cacheMap = new HashMap<String, ImageSoftReference>();
	/** 图片压缩至960像素以内 */
	public static final int COMPRESS_IMAGE_HEIGHT_PX = 960;
	public static final int COMPRESS_IMAGE_WIDTH_PX = 960;
	private static ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
	/** 发送图片的最大大小 */
	public static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;
	/**
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
	 * 
	 * @param key
	 * @param bm
	 */
	public static void removeBitmap(String key) {
		cacheMap.remove(key);
	}

	/**
	 */
	public static void clearMap() {
		cacheMap.clear();
	}

	/**
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
	 * �½�һ��ͼARGB_4444
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
	 * �����ԴͼƬ
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
			matrix.postScale(scale, scale);// ��Сͼ��
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
				matrix.postScale(0.7f, 0.7f);// ��Сͼ��
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
	 * �����ԴͼƬ
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
			matrix.postScale(scale, scale);// ��Сͼ��
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
				matrix.postScale(0.7f, 0.7f);// ��Сͼ��
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
	 * ������ԴID��ȡ����ͼƬ
	 * 
	 * @param resId
	 *            ��ԴID
	 * @param key
	 *            ����ؼ���
	 * @param limitH
	 *            ��
	 * @param limitW
	 *            ��
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
		// ����ͼƬ
		Matrix m = new Matrix();
		float sx = (float) 2 * limitW / bitmap.getWidth();
		float sy = (float) 2 * limitH / bitmap.getHeight();
		m.setScale(sx, sy);
		try {
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, false);
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
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
	 * ����ļ�ͼƬ�ĳߴ�仯
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
			matrix.postScale(n, n);// ��Сͼ��
			if (bmp == null) {
				// ����ļ���ͼƬ������
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
				matrix.postScale(n, n);// 
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
	 * ����bitmap
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
	 * ����ѹ�������ش�С��ȡѹ����ͼƬ
	 * 
	 * @param limitH
	 *            ������ظ�
	 * @param limitW
	 *            ������ؿ�
	 * @return
	 */
	public static Bitmap converBitmap(File file, int limitW, int limitH) {
		// ����inJustDecodeBoundsΪtrue,����decode��ʱ��ֻ����ߺͿ���������������ͼƬ��
		// ��߾�����opts�
		// ʹ��decodeFile�����õ�ͼƬ�Ŀ�͸�
		// �������ű�������������߿�С����Ϊ1
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
		// ����ͼƬ����
		opts.inSampleSize = sampleSize;
		opts.inJustDecodeBounds = false;
		// δ֪�����������Եĺ���
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		bm = decodeFile(file.getAbsolutePath(), opts, limitH, limitW);
		return bm;
	}

	/**
	 * ����Ǿ�̬ͼƬ�������ѹ������, ����ת ����ǰ��֤file��Ϊ��
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
		/** ����ѹ����ԭͼImage */
		Bitmap bitmapSourceImg;
		bitmapSourceImg = converBitmap(file, limitW, limitH);
		bitmapSourceImg = rotate(file.getAbsolutePath(), bitmapSourceImg,
				romate, limitW, limitH);
		if (bitmapSourceImg != null)
			addBitmap(key, bitmapSourceImg);
		// �洢��ʱ�ļ�
		return bitmapSourceImg;
	}

	/**
	 * ������bitmap�ϳ�Ϊһ��bitmap
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
		// create the new blank bitmap ����һ���µĺ�SRC���ȿ��һ����λͼ
		Bitmap newbmp = Bitmap
				.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas cv = new Canvas(newbmp);
		// draw bg into
		cv.drawBitmap(background, 0, 0, null);// �� 0��0���꿪ʼ����bg
		// draw fg into
		cv.drawBitmap(foreground, left, top, null);// �� 0��0���꿪ʼ����fg �����Դ�����λ�û���
		return newbmp;
	}

	/**
	 * ��תͼƬ
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
	 * ��ͼƬ���Բ��Ч��
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedBitmap(Bitmap bitmap, float roundPx) {
		// ����ͼƬ
		Matrix m = new Matrix();
		// ���¹���һ��40*40��ͼƬ
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), m, false);
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xfff5f5f5;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// Բ�ǰ뾶
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
