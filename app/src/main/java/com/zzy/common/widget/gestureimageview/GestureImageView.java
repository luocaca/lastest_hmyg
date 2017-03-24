/*
 * Copyright (c) 2012 Jason Polites
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzy.common.widget.gestureimageview;

import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class GestureImageView extends ImageView {

	public static final String GLOBAL_NS = "http://schemas.android.com/apk/res/android";
	public static final String LOCAL_NS = "http://schemas.polites.com/android";

	private final Semaphore drawLock = new Semaphore(0);
	private Animator animator = null;

	private Drawable drawable = null;

	private float x = 0, y = 0;

	private boolean layout = false;

	/** 当前缩放倍数 */
	private float scaleAdjust = 1.0f;
	/** 起始已缩放倍数 */
	private float startingScale = -1.0f;

	private float startingRomateScale = -1.0f;

	private float startingRotation = 0.0f;

	private float scale = 1.0f;
	/** 缩放最大值 */
	private float maxScale = 5.0f;
	/** 缩放最小值 */
	private float minScale = 0.5f;
	/** 水平倍数 */
	private float fitScaleHorizontal = 1.0f;
	/** 垂直倍数 */
	private float fitScaleVertical = 1.0f;
	/** 旋转后的水平倍数 */
	private float fitRomateScaleHorizontal = 1.0f;
	/** 旋转后的垂直倍数 */
	private float fitRomateScaleVertical = 1.0f;
	/** 旋转角度 */
	private float rotation = 0.0f;

	/** 图片中心坐标 */
	private float centerX;
	private float centerY;

	private Float startX, startY;

	private int hWidth;
	private int hHeight;

	private int resId = -1;
	private boolean recycle = false;
	private boolean strict = false;
	/** 是否初始化缩放值 */
	private boolean resetStartScale = false;
	/** 是否是选择头像界面 */
	private boolean isChooseHeadPic = false;
	/** 是否可以旋转 */
	private boolean isCanRomate = false;
	/** 选择头像的边框 */
	private Rect chooseframeRect = null;
	/** 选取图片框的宽度 */
	private float chooseframeWidth = 0;
	/** 选取图片框的高度 */
	private float chooseFrameHeight = 0;

	/** 用于图片浏览器 */
	private boolean isGalleryView = false;

	private int displayHeight;
	private int displayWidth;

	private int alpha = 255;
	private ColorFilter colorFilter;

	private int deviceOrientation = -1;
	private int imageOrientation;

	private GestureImageViewListener gestureImageViewListener;
	public GestureImageViewTouchListener gestureImageViewTouchListener;

	private OnTouchListener customOnTouchListener;
	private OnClickListener onClickListener;

	/** 图片类型 */
	private int picType = RomateOverCallback.TYPE_CHAT_PIC;
	/** 图片数据库id */
	private long picDBId = 0;

	public GestureImageView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	public GestureImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		isGalleryView = false;

		String scaleType = attrs.getAttributeValue(GLOBAL_NS, "scaleType");

		if (scaleType == null || scaleType.trim().length() == 0) {
			setScaleType(ScaleType.CENTER_INSIDE);
		}

		String strStartX = attrs.getAttributeValue(LOCAL_NS, "start-x");
		String strStartY = attrs.getAttributeValue(LOCAL_NS, "start-y");

		if (strStartX != null && strStartX.trim().length() > 0) {
			startX = Float.parseFloat(strStartX);
		}

		if (strStartY != null && strStartY.trim().length() > 0) {
			startY = Float.parseFloat(strStartY);
		}

		setStartingScale(attrs.getAttributeFloatValue(LOCAL_NS, "start-scale",
				startingScale));
		setMinScale(attrs.getAttributeFloatValue(LOCAL_NS, "min-scale",
				minScale));
		setMaxScale(attrs.getAttributeFloatValue(LOCAL_NS, "max-scale",
				maxScale));
		setStrict(attrs.getAttributeBooleanValue(LOCAL_NS, "strict", strict));
		setRecycle(attrs.getAttributeBooleanValue(LOCAL_NS, "recycle", recycle));

		initImage();
	}

	public GestureImageView(Context context) {
		super(context);
		setScaleType(ScaleType.CENTER_INSIDE);

		setStartingScale((float) -1.0);
		setStartingRomateScale((float) -1.0);
		setMinScale((float) 0.5);
		setMaxScale((float) 10.0);
		setStrict(false);
		setRecycle(false);
		isGalleryView = true;

		initImage();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (drawable != null) {
			int orientation = getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				displayHeight = MeasureSpec.getSize(heightMeasureSpec);

				if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
					float ratio = (float) getImageWidth()
							/ (float) getImageHeight();
					displayWidth = Math.round((float) displayHeight * ratio);
				} else {
					displayWidth = MeasureSpec.getSize(widthMeasureSpec);
				}
			} else {
				displayWidth = MeasureSpec.getSize(widthMeasureSpec);
				if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
					float ratio = (float) getImageHeight()
							/ (float) getImageWidth();
					displayHeight = Math.round((float) displayWidth * ratio);
				} else {
					displayHeight = MeasureSpec.getSize(heightMeasureSpec);
				}
			}
		} else {
			displayHeight = MeasureSpec.getSize(heightMeasureSpec);
			displayWidth = MeasureSpec.getSize(widthMeasureSpec);
		}

		setMeasuredDimension(displayWidth, displayHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed || !layout) {
			setupCanvas(displayWidth, displayHeight, getResources()
					.getConfiguration().orientation);
		}
	}

	protected void setupCanvas(int measuredWidth, int measuredHeight,
			int orientation) {

		if (deviceOrientation != orientation) {
			layout = false;
			deviceOrientation = orientation;
		}
		if (drawable != null && !layout) {
			int imageWidth = getImageWidth();
			int imageHeight = getImageHeight();

			hWidth = Math.round(((float) imageWidth / 2.0f));
			hHeight = Math.round(((float) imageHeight / 2.0f));

			measuredWidth -= (getPaddingLeft() + getPaddingRight());
			measuredHeight -= (getPaddingTop() + getPaddingBottom());

			computeCropScale(imageWidth, imageHeight, measuredWidth,
					measuredHeight);

			if (startingScale <= 0.0f || resetStartScale) {
				resetStartScale = false;
				computeStartingScale(imageWidth, imageHeight, measuredWidth,
						measuredHeight);
			}

			if (MathUtils.isPortraitByDegree(getRotation())) {
				scaleAdjust = startingScale;
			} else {
				scaleAdjust = startingRomateScale;
			}

			if (isChooseHeadPic) {
				this.centerY = (float) ((chooseframeRect.bottom - chooseframeRect.top) / 2.0f + chooseframeRect.top);
			} else {
				this.centerY = (float) measuredHeight / 2.0f;
			}
			this.centerX = (float) measuredWidth / 2.0f;

			if (startX == null) {
				x = centerX;
			} else {
				x = startX;
			}

			if (startY == null) {
				y = centerY;
			} else {
				y = startY;
			}

			gestureImageViewTouchListener = new GestureImageViewTouchListener(
					this, measuredWidth, measuredHeight, startingScale,
					startingRomateScale, startingRotation, chooseframeWidth,
					chooseFrameHeight, chooseframeRect, isChooseHeadPic,
					isCanRomate, picType, picDBId);

			if (!isChooseHeadPic) {
				setMinScale(minScale);
				setMaxScale(maxScale);
			} else {
				gestureImageViewTouchListener.setMinScale(getFrameMinScale(
						imageWidth, imageHeight));
				gestureImageViewTouchListener.setMaxScale(maxScale
						* startingScale);
			}

			gestureImageViewTouchListener.setOnClickListener(onClickListener);

			if (drawable != null)
				drawable.setBounds(-hWidth, -hHeight, hWidth, hHeight);

			if (!isGalleryView) {
				super.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (customOnTouchListener != null) {
							customOnTouchListener.onTouch(v, event);
						}
						return gestureImageViewTouchListener.onTouch(v, event);
					}
				});
			}
			layout = true;
		}
	}

	protected void computeCropScale(int imageWidth, int imageHeight,
			int measuredWidth, int measuredHeight) {
		fitScaleHorizontal = (float) measuredWidth / (float) imageWidth;
		fitScaleVertical = (float) measuredHeight / (float) imageHeight;
		fitRomateScaleHorizontal = (float) measuredWidth / (float) imageHeight;
		fitRomateScaleVertical = (float) measuredHeight / (float) imageWidth;
	}

	protected float getFrameMinScale(int imageWidth, int imageHeight) {
		float frameHScale = (float) chooseframeWidth / (float) imageWidth;
		float frameVScale = (float) chooseFrameHeight / (float) imageHeight;
		return Math.max(frameHScale, frameVScale);
	}

	protected void computeStartingScale(int imageWidth, int imageHeight,
			int measuredWidth, int measuredHeight) {
		switch (getScaleType()) {
		case CENTER:
			// Center the image in the view, but perform no scaling.
			startingScale = 1.0f;
			startingRomateScale = 1.0f;
			break;
		case CENTER_CROP:
			startingScale = Math.max((float) measuredHeight
					/ (float) imageHeight, (float) measuredWidth
					/ (float) imageWidth);
			startingRomateScale = Math.max((float) measuredHeight
					/ (float) imageWidth, (float) measuredWidth
					/ (float) imageHeight);
			break;
		case CENTER_INSIDE:
			startingScale = Math.min(fitScaleHorizontal, fitScaleVertical);
			startingRomateScale = Math.min(fitRomateScaleHorizontal,
					fitRomateScaleVertical);
			if (isChooseHeadPic) {
				if (((float) imageHeight * startingScale) < (float) chooseFrameHeight) {
					startingScale = (float) chooseFrameHeight
							/ (float) imageHeight;
				}
				if (((float) imageWidth * startingScale) < (float) chooseframeWidth) {
					startingScale = (float) chooseframeWidth
							/ (float) imageWidth;
				}
			}
			break;
		case MATRIX:
			startingScale = Math.min(fitScaleHorizontal, fitScaleVertical);
			startingRomateScale = Math.min(fitRomateScaleHorizontal,
					fitRomateScaleVertical);
			if (isChooseHeadPic) {
				if (((float) imageHeight * startingScale) < (float) chooseFrameHeight) {
					startingScale = (float) chooseFrameHeight
							/ (float) imageHeight;
				}
				if (((float) imageWidth * startingScale) < (float) chooseframeWidth) {
					startingScale = (float) chooseframeWidth
							/ (float) imageWidth;
				}
			}
		default:
			break;
		}
	}

	protected boolean isRecycled() {
		if (drawable != null && drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null) {
				return bitmap.isRecycled();
			}
		}
		return false;
	}

	protected void recycle() {
		if (recycle && drawable != null && drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (layout) {
			if (drawable != null && !isRecycled()) {
				canvas.save();
				float adjustedScale = scale * scaleAdjust;
				canvas.translate(x, y);
				if (adjustedScale != 1.0f) {
					canvas.scale(adjustedScale, adjustedScale);
				}
				if (rotation != 0.0f) {
					canvas.rotate(rotation);
				}
				drawable.draw(canvas);
				canvas.restore();
			}
			if (drawLock.availablePermits() <= 0) {
				drawLock.release();
			}
		}
	}

	/**
	 * Waits for a draw
	 * 
	 * @param max
	 *            time to wait for draw (ms)
	 * @throws InterruptedException
	 */
	public boolean waitForDraw(long timeout) throws InterruptedException {
		return drawLock.tryAcquire(timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onAttachedToWindow() {
		if (animator == null) {
			animator = new Animator(this, "GestureImageViewAnimator");
			animator.start();
		}
		if (resId >= 0 && drawable == null) {
			setImageResource(resId);
		}
		super.onAttachedToWindow();
	}

	public void animationStart(Animation animation) {
		if (animator != null) {
			animator.play(animation);
		}
	}

	public void animationStop() {
		if (animator != null) {
			animator.cancel();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (animator != null) {
			animator.finish();
		}
		if (recycle && drawable != null && !isRecycled()) {
			recycle();
			drawable.setCallback(null);
			drawable = null;
		}
		if (drawable != null) {
			drawable.setCallback(null);
			drawable = null;
		}
		gestureImageViewListener = null;
		gestureImageViewTouchListener = null;
		customOnTouchListener = null;
		onClickListener = null;

		super.onDetachedFromWindow();
	}

	protected void initImage() {
		if (this.drawable != null) {
			this.drawable.setAlpha(alpha);
			this.drawable.setFilterBitmap(true);
			if (colorFilter != null) {
				this.drawable.setColorFilter(colorFilter);
			}
		}
		if (!layout) {
			requestLayout();
			redraw();
		}
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		this.drawable = drawable;
		
		setScaleType(ScaleType.CENTER_INSIDE);

		setStartingScale((float) -1.0);
		setStartingRomateScale((float) -1.0);
		setMinScale((float) 0.5);
		setMaxScale((float) 10.0);
		setStrict(false);
		setRecycle(false);
		isGalleryView = true;
		
		layout = false;
		resetStartScale = true;
		isCanRomate = true;
		setStartingRotation(0);
		setRotation(0);
		initImage();
	}

	public void setImageBitmap(Bitmap image) {
		this.drawable = new BitmapDrawable(getResources(), image);
		
		setScaleType(ScaleType.CENTER_INSIDE);

		setStartingScale((float) -1.0);
		setStartingRomateScale((float) -1.0);
		setMinScale((float) 0.5);
		setMaxScale((float) 10.0);
		setStrict(false);
		setRecycle(false);
		isGalleryView = true;
		
		layout = false;
		resetStartScale = true;
		isCanRomate = true;
		setStartingRotation(0);
		setRotation(0);
		initImage();
	}

	/**
	 * 用于图片浏览器
	 * 
	 * @param image
	 */
	public void setGalleryImageBitmap(Bitmap image, boolean isCanRomate,
			float startingRotation, int picType, long picDBId) {
		this.drawable = new BitmapDrawable(getResources(), image);
		setScaleType(ScaleType.CENTER_INSIDE);

		setStartingScale((float) -1.0);
		setStartingRomateScale((float) -1.0);
		setMinScale((float) 0.5);
		setMaxScale((float) 10.0);
		setStrict(false);
		setRecycle(false);
		isGalleryView = true;

		this.isCanRomate = isCanRomate;
		this.picType = picType;
		this.picDBId = picDBId;
		setStartingRotation(startingRotation);
		setRotation(startingRotation);

		layout = false;
		resetStartScale = true;
		initImage();
	}

	/**
	 * 用于选择头像
	 * 
	 * @param image
	 * @param isChooseHeadPic
	 */
	public void setImageBitmap(Bitmap image, boolean isChooseHeadPic) {
		this.drawable = new BitmapDrawable(getResources(), image);
		this.isChooseHeadPic = isChooseHeadPic;
		this.isCanRomate = false;
		layout = false;
		resetStartScale = true;
		initImage();
	}

	public void setImageResource(int id) {
		if (this.drawable != null) {
			this.recycle();
		}
		layout = false;
		resetStartScale = true;
		if (id >= 0) {
			this.resId = id;
			setImageDrawable(getContext().getResources().getDrawable(id));
		}
	}
	
	@Override
	public void setImageURI(Uri mUri) {
		if ("content".equals(mUri.getScheme())) {
			try {
				String[] orientationColumn = { MediaStore.Images.Media.ORIENTATION };

				Cursor cur = getContext().getContentResolver().query(mUri,
						orientationColumn, null, null, null);

				if (cur != null && cur.moveToFirst()) {
					imageOrientation = cur.getInt(cur
							.getColumnIndex(orientationColumn[0]));
				}

				InputStream in = null;
				try {
					in = getContext().getContentResolver()
							.openInputStream(mUri);
					Bitmap bmp = BitmapFactory.decodeStream(in);

					if (imageOrientation != 0) {
						Matrix m = new Matrix();
						m.postRotate(imageOrientation);
						Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0,
								bmp.getWidth(), bmp.getHeight(), m, true);
						bmp.recycle();
						setImageDrawable(new BitmapDrawable(getResources(),
								rotated));
					} else {
						setImageDrawable(new BitmapDrawable(getResources(), bmp));
					}
				} finally {
					if (in != null) {
						in.close();
					}
					if (cur != null) {
						cur.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			setImageDrawable(Drawable.createFromPath(mUri.toString()));
		}

		if (drawable == null) {
			// Don't try again.
			mUri = null;
		}
	}
	
	@Override
	public void setImageMatrix(Matrix matrix) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@Override
	public Matrix getImageMatrix() {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		return super.getImageMatrix();
	}

	public int getScaledWidth() {
		return Math.round(getImageWidth() * getScale());
	}

	public int getScaledHeight() {
		return Math.round(getImageHeight() * getScale());
	}

	/**
	 * 获取旋转后的图片比例宽
	 * 
	 * @return
	 */
	public int getRomateScaledWidth() {
		return Math.round(getRomateImageWidth() * getScale());
	}

	/**
	 * 获取旋转后的图片比例高
	 * 
	 * @return
	 */
	public int getRomateScaledHeight() {
		return Math.round(getRomateImageHeight() * getScale());
	}

	/**
	 * 图片宽
	 * 
	 * @return
	 */
	private int getImageWidth() {
		if (drawable != null) {
			return drawable.getIntrinsicWidth();
		}
		return 0;
	}

	/**
	 * 图片高
	 * 
	 * @return
	 */
	private int getImageHeight() {
		if (drawable != null) {
			return drawable.getIntrinsicHeight();
		}
		return 0;
	}

	/**
	 * 获取旋转过后的宽
	 * 
	 * @return
	 */
	public int getRomateImageWidth() {
		return Math
				.round((float) (getImageHeight() * getScale()
						* Math.abs(Math.sin(Math.toRadians(getRotation()))) + getImageWidth()
						* getScale()
						* Math.abs(Math.cos(Math.toRadians(getRotation())))));
	}

	/**
	 * 获取旋转过后的高
	 * 
	 * @return
	 */
	public int getRomateImageHeight() {
		return Math
				.round((float) (getImageWidth() * getScale()
						* Math.abs(Math.sin(Math.toRadians(getRotation()))) + getImageHeight()
						* getScale()
						* Math.abs(Math.cos(Math.toRadians(getRotation())))));
	}

	public void setChooseFrame(Rect chooseFrameRect) {
		this.chooseframeRect = chooseFrameRect;
	}

	public void setChooseFrameWidth(int chooseFrameWidth, int chooseFrameHeight) {
		this.chooseframeWidth = chooseFrameWidth;
		this.chooseFrameHeight = chooseFrameHeight;
	}

	public void moveBy(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void redraw() {
		postInvalidate();
	}

	public void setMinScale(float min) {
		this.minScale = min;
		if (gestureImageViewTouchListener != null) {
			if (MathUtils.isPortraitByDegree(getRotation())) {
				gestureImageViewTouchListener.setMinScale(min * startingScale);
			} else {
				gestureImageViewTouchListener.setMinScale(min
						* startingRomateScale);
			}
		}
	}

	public void setMaxScale(float max) {
		this.maxScale = max;
		if (gestureImageViewTouchListener != null) {
			if (MathUtils.isPortraitByDegree(getRotation())) {
				gestureImageViewTouchListener.setMaxScale(max * startingScale);
			} else {
				gestureImageViewTouchListener.setMaxScale(max
						* startingRomateScale);
			}
		}
	}

	public float getMinScale() {
		if (MathUtils.isPortraitByDegree(getRotation())) {
			return minScale * startingScale;
		} else {
			return minScale * startingRomateScale;
		}
	}

	public float getMaxScale() {
		if (MathUtils.isPortraitByDegree(getRotation())) {
			return maxScale * startingScale;
		} else {
			return maxScale * startingRomateScale;
		}
	}

	public void setScale(float scale) {
		scaleAdjust = scale;
	}

	public float getScale() {
		return scaleAdjust;
	}

	public float getImageX() {
		return x;
	}

	public float getImageY() {
		return y;
	}

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public boolean isRecycle() {
		return recycle;
	}

	public void setRecycle(boolean recycle) {
		this.recycle = recycle;
	}

	public void reset() {
		x = centerX;
		y = centerY;
		scale = 1.0f;
		if (MathUtils.isPortraitByDegree(getRotation())) {
			scaleAdjust = startingScale;
		} else {
			scaleAdjust = startingRomateScale;
		}
		redraw();
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}

	public void setGestureImageViewListener(
			GestureImageViewListener pinchImageViewListener) {
		this.gestureImageViewListener = pinchImageViewListener;
	}

	public GestureImageViewListener getGestureImageViewListener() {
		return gestureImageViewListener;
	}

	@Override
	public Drawable getDrawable() {
		return drawable;
	}

	@Override
	public void setAlpha(int alpha) {
		this.alpha = alpha;
		if (drawable != null) {
			drawable.setAlpha(alpha);
		}
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		this.colorFilter = cf;
		if (drawable != null) {
			drawable.setColorFilter(cf);
		}
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType == ScaleType.CENTER || scaleType == ScaleType.CENTER_CROP
				|| scaleType == ScaleType.CENTER_INSIDE
				|| scaleType == ScaleType.MATRIX) {

			super.setScaleType(scaleType);
		} else if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@Override
	public void invalidateDrawable(Drawable dr) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.invalidateDrawable(dr);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		return super.onCreateDrawableState(extraSpace);
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.setAdjustViewBounds(adjustViewBounds);
	}

	@Override
	public void setImageLevel(int level) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.setImageLevel(level);
	}

	@Override
	public void setImageState(int[] state, boolean merge) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@Override
	public void setSelected(boolean selected) {
		if (strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.setSelected(selected);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		this.customOnTouchListener = l;
	}

	public float getCenterX() {
		return centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public boolean isLandscape() {
		return getImageWidth() >= getImageHeight();
	}

	public boolean isPortrait() {
		return getImageWidth() <= getImageHeight();
	}

	public void setStartingScale(float startingScale) {
		this.startingScale = startingScale;
	}

	public void setStartingRomateScale(float startingRomateScale) {
		this.startingRomateScale = startingRomateScale;
	}

	public void setStartingRotation(float startingRotation) {
		this.startingRotation = startingRotation;
	}

	public void setStartingPosition(float x, float y) {
		this.startX = x;
		this.startY = y;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;

		if (gestureImageViewTouchListener != null) {
			gestureImageViewTouchListener.setOnClickListener(l);
		}
	}

	/**
	 * Returns true if the image dimensions are aligned with the orientation of
	 * the device.
	 * 
	 * @return
	 */
	public boolean isOrientationAligned() {
		if (deviceOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			return isLandscape();
		} else if (deviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
			return isPortrait();
		}
		return true;
	}

	public int getDeviceOrientation() {
		return deviceOrientation;
	}
}
