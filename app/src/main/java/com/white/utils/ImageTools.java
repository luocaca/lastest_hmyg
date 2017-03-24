package com.white.utils;

//Download by http://www.codefans.net
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

/**
 * Tools for handler picture
 * 
 * @author Ryan.Tang
 * 
 */
public final class ImageTools {

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
	/** 最大高度 自定义表情显示 */
	public static final int MAX_IMAGE_HEIGHT_DIP = 120;
	/** 最大宽度 */
	public static final int MAX_IMAGE_WIDTH_DIP = 120;
	/** 最大高度 会话窗口的图片小图显示 */
	public static final int MAX_IMAGE_HEIGHT_DIP_200 = 200;
	/** 最大宽度 */
	public static final int MAX_IMAGE_WIDTH_DIP_200 = 200;
	/** 头像的缩放 */
	private static final int HEAD_IMAGE_HALFWIDTH = 58;
	/** 圆形头像的缩放 */
	private static final int CIRCLE_IMAGE_HALFWIDTH = 57;
	/** 圆形头像背景的缩放 */
	public static final int CIRCLE_IMAGE_BG_HALFWIDTH = 180;
	// public static final int CIRCLE_IMAGE_BG_HALFWIDTH = 60; //缩略图的大小高度
	/** 图片压缩至960像素以内 */
	public static final int COMPRESS_IMAGE_HEIGHT_PX = 960;
	public static final int COMPRESS_IMAGE_WIDTH_PX = 960;

	/**
	 * 如果是静态图片，则进行压缩处理, 并旋转 传入前保证file不为空
	 * 
	 * @param rotate
	 *            TODO
	 */
	public static Bitmap CompressAndSaveImg(File file) throws IOException {
		int degree = 0;
		ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
		int orc = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				-1);
		// 然后旋转
		if (orc == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		/** 用于压缩的原图Image */
		Bitmap bitmapSourceImg;
		if (degree == 0) {
			bitmapSourceImg = converBitmap(file, COMPRESS_IMAGE_HEIGHT_PX,
					COMPRESS_IMAGE_WIDTH_PX);
		} else {
			bitmapSourceImg = converBitmap(file, COMPRESS_IMAGE_HEIGHT_PX / 2,
					COMPRESS_IMAGE_WIDTH_PX / 2);
			bitmapSourceImg = rotate(file.getAbsolutePath(), bitmapSourceImg,
					degree, COMPRESS_IMAGE_HEIGHT_PX / 2,
					COMPRESS_IMAGE_WIDTH_PX / 2);
		}
		// 存储临时文件
		return bitmapSourceImg;
	}

	/**
	 * 根据压缩后往缓存中加入图片,用于相册
	 * 
	 * @param height
	 *            最大像素高
	 * @param width
	 *            最大像素宽
	 * @return
	 */
	public static Bitmap converThumbnailBitmap(File file, String key) {
		// 设置inJustDecodeBounds为true,这样decode的时候只计算高和宽，而不加载真正的图片，
		// 宽高具体在opts里。
		// 使用decodeFile方法得到图片的宽和高
		// 计算缩放比例，如果比最大高宽小则设为1
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bm = ImageTools.decodeFileNoFromCache(key,
				file.getAbsolutePath(), opts, CIRCLE_IMAGE_BG_HALFWIDTH,
				CIRCLE_IMAGE_BG_HALFWIDTH);
		int sampleHeight = opts.outHeight / CIRCLE_IMAGE_BG_HALFWIDTH;
		int sampleWidth = opts.outWidth / CIRCLE_IMAGE_BG_HALFWIDTH;
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
		bm = ImageTools.decodeFileNoFromCache(key, file.getAbsolutePath(),
				opts, CIRCLE_IMAGE_BG_HALFWIDTH, CIRCLE_IMAGE_BG_HALFWIDTH);
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(
					file.getAbsolutePath());
			int orc = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			// 然后旋转
			if (orc == ExifInterface.ORIENTATION_ROTATE_90) {
				degree = 90;
			} else if (orc == ExifInterface.ORIENTATION_ROTATE_180) {
				degree = 180;
			} else if (orc == ExifInterface.ORIENTATION_ROTATE_270) {
				degree = 270;
			}
			/** 用于旋转图片 */
			if (degree != 0) {
				bm = ImageTools.rotate(file.getAbsolutePath(), bm, degree,
						CIRCLE_IMAGE_BG_HALFWIDTH, CIRCLE_IMAGE_BG_HALFWIDTH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	/**
	 * 生成bitmap,不从缓存找 用于相册
	 * 
	 * @param paramString
	 * @param paramOptions
	 * @return
	 */
	public static Bitmap decodeFileNoFromCache(String key, String paramString,
			BitmapFactory.Options paramOptions, int limitW, int limitH) {
		String pathStr = key + ","
				+ (paramOptions == null ? "_1" : "_" + limitW + "_" + limitH);
		Bitmap localBitmap = getFromCache(pathStr);
		if (localBitmap != null)
			return localBitmap;
		try {
			localBitmap = BitmapFactory.decodeFile(paramString, paramOptions);
			if (paramOptions == null || !paramOptions.inJustDecodeBounds)
				addBitmap(key, localBitmap);
			return localBitmap;
		} catch (OutOfMemoryError localOutOfMemoryError1) {
			System.gc();
			Thread.yield();
			try {
				localBitmap = BitmapFactory.decodeFile(paramString,
						paramOptions);
				if (paramOptions == null || !paramOptions.inJustDecodeBounds)
					addBitmap(key, localBitmap);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
				clearMap();
			}
			return localBitmap;
		}
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

	protected static String createBitmapCacheKey(String name, int shape,
			int limitW, int limitH, float scale, int rotation) {
		return name + "_" + shape + "_" + limitW + "_" + limitH + "_" + scale
				+ "_" + rotation;
	}

	/**
	 * Transfer drawable to bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap to drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * Input stream to bitmap
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Bitmap inputStreamToBitmap(InputStream inputStream)
			throws Exception {
		return BitmapFactory.decodeStream(inputStream);
	}

	/**
	 * Byte transfer to bitmap
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	/**
	 * Byte transfer to drawable
	 * 
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = null;
		if (byteArray != null) {
			ins = new ByteArrayInputStream(byteArray);
		}
		return Drawable.createFromStream(ins, null);
	}

	/**
	 * Bitmap transfer to bytes
	 * 
	 * @param byteArray
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		byte[] bytes = null;
		if (bm != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		}
		return bytes;
	}

	/**
	 * Drawable transfer to bytes
	 * 
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();
		byte[] bytes = bitmapToBytes(bitmap);
		;
		return bytes;
	}

	/**
	 * Base64 to byte[] //
	 */
	// public static byte[] base64ToBytes(String base64) throws IOException {
	// byte[] bytes = Base64.decode(base64);
	// return bytes;
	// }
	//
	// /**
	// * Byte[] to base64
	// */
	// public static String bytesTobase64(byte[] bytes) {
	// String base64 = Base64.encode(bytes);
	// return base64;
	// }

	/**
	 * Create reflection images
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
				h / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * Get rounded corner images
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            5 10
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Resize the bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * Resize the drawable
	 * 
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		float sx = ((float) w / width);
		float sy = ((float) h / height);
		matrix.postScale(sx, sy);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newbmp);
	}

	/**
	 * Get images from SD card by path and the name of image
	 * 
	 * @param photoName
	 * @return
	 */
	public static Bitmap getPhotoFromSDCard(String path, String photoName) {
		Bitmap photoBitmap = BitmapFactory.decodeFile(path + "/" + photoName
				+ ".png");
		if (photoBitmap == null) {
			return null;
		} else {
			return photoBitmap;
		}
	}

	/**
	 * Check the SD card
	 * 
	 * @return
	 */
	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * Get image from SD card by path and the name of image
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean findPhotoFromSDCard(String path, String photoName) {
		boolean flag = false;

		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (dir.exists()) {
				File folders = new File(path);
				File photoFile[] = folders.listFiles();
				for (int i = 0; i < photoFile.length; i++) {
					String fileName = photoFile[i].getName().split("\\.")[0];
					if (fileName.equals(photoName)) {
						flag = true;
					}
				}
			} else {
				flag = false;
			}
			// File file = new File(path + "/" + photoName + ".jpg" );
			// if (file.exists()) {
			// flag = true;
			// }else {
			// flag = false;
			// }

		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * Save image to the SD card
	 * 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap, String path,
			String photoName) {
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path, photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
						// fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete the image from SD card
	 * 
	 * @param context
	 * @param path
	 *            file:///sdcard/temp.jpg
	 */
	public static void deleteAllPhoto(String path) {
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	public static void deletePhotoAtPathAndName(String path, String fileName) {
		if (checkSDCardAvailable()) {
			File folder = new File(path);
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					files[i].delete();
				}
			}
		}
	}

}
