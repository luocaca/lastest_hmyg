package com.zzy.common.widget.galleryView;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import cn.jpush.a.a.z;

import com.hldj.hmyg.R;
import com.hldj.hmyg.application.Data;
import com.white.utils.GlobalData;
import com.zzy.common.widget.gestureimageview.GestureImageView;

public class ImageGalleryView extends Gallery {

	private static final int GESTURE_TYPE_NONE = 0;
	private static final int GESTURE_TYPE_ON_SCROLL = 1;
	private static final int GESTURE_TYPE_MORE_POINTS = 2;
	private GestureDetector gestureScanner;
	private GestureImageView imageView = null;
	private boolean isInHandler = false;
	private int gestureType = GESTURE_TYPE_NONE;

	private SingleTapListener singleTapListener;

	// private View layoutView;

	private boolean isLanscape = false;

	public void setIsLandscape(boolean isLandscape) {
		this.isLanscape = isLandscape;
	}

	public ImageGalleryView(Context context) {
		super(context);

	}

	public ImageGalleryView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		imageView = null;
		super.onDetachedFromWindow();
	}

	public ImageGalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureScanner = new GestureDetector(new MySimpleGesture());
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getLastVisiblePosition() == getFirstVisiblePosition()) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						break;
					default:
						break;
					}
					if (event.getPointerCount() == 1
							|| gestureType != GESTURE_TYPE_ON_SCROLL) {
						if (event != null && v != null) {
							try {
								isInHandler = doGestureViewTouchListener(event,
										v);
							} catch (Exception e) {
								// TODO: handle exception
							}

						}

					}
				}
				return gestureScanner.onTouchEvent(event);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			gestureType = GESTURE_TYPE_NONE;
			break;
		}
		if (event.getPointerCount() > 1
				&& gestureType != GESTURE_TYPE_ON_SCROLL) {
			// || getLastVisiblePosition() == getFirstVisiblePosition()) {
			doGestureViewTouchListener(event, null);
			gestureType = GESTURE_TYPE_MORE_POINTS;
		} else if (event.getAction() == MotionEvent.ACTION_CANCEL
				|| event.getAction() == MotionEvent.ACTION_UP) {
			doGestureViewTouchListener(event, null);
		}
		return super.onTouchEvent(event);
	}

	private boolean doGestureViewTouchListener(MotionEvent event, View touchView) {
		View selectView = ImageGalleryView.this.getSelectedView();
		// if (view instanceof GestureImageView) {
		if (selectView instanceof RelativeLayout) {
			imageView = (GestureImageView) selectView
					.findViewById(R.id.image_gallery_item_image);
			if (imageView.getVisibility() == View.VISIBLE) {
				if (imageView.gestureImageViewTouchListener != null
						&& touchView != null) {
					return imageView.gestureImageViewTouchListener
							.onTouch((touchView == null ? selectView
									: touchView), event);
				}

			}
		}
		return false;
	}

	private boolean doScrollView(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		gestureType = GESTURE_TYPE_ON_SCROLL;
		super.onScroll(e1, e2, distanceX, distanceY);
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		boolean result = false;
		if (gestureType == GESTURE_TYPE_MORE_POINTS)
			return result;
		View view = ImageGalleryView.this.getSelectedView();
		if (view instanceof RelativeLayout) {
			imageView = (GestureImageView) view
					.findViewById(R.id.image_gallery_item_image);
			float v[] = new float[9];
			Matrix m = imageView.getImageMatrix();
			m.getValues(v);
			float left, right;
			float width;
			width = imageView.getRomateImageWidth();
			if (imageView.getVisibility() == View.VISIBLE) {
				if ((int) width <= Data.screenWidth) {
					result = doScrollView(e1, e2, distanceX, distanceY);
				} else {
					left = v[Matrix.MTRANS_X];
					right = left + width;
					Rect r = new Rect();
					imageView.getGlobalVisibleRect(r);

					if (distanceX > 0) {
						if (r.left > 0
								|| right < Data.screenWidth
								|| imageView.gestureImageViewTouchListener
										.getIsToNext()) {
							result = doScrollView(e1, e2, distanceX, distanceY);
						}
					} else if (distanceX < 0) {
						if (left > 0
								|| r.right < Data.screenWidth
								|| imageView.gestureImageViewTouchListener
										.getIsTotBefore()) {
							result = doScrollView(e1, e2, distanceX, distanceY);
						}
					}
				}
			} else {
				result = doScrollView(e1, e2, distanceX, distanceY);
			}
		} else {
			result = doScrollView(e1, e2, distanceX, distanceY);
		}
		return result;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		boolean result = false;
		if (isInHandler || gestureType == GESTURE_TYPE_MORE_POINTS)
			return result;
		View view = ImageGalleryView.this.getSelectedView();
		// if (view instanceof GestureImageView) {
		if (view instanceof RelativeLayout) {
			imageView = (GestureImageView) view
					.findViewById(R.id.image_gallery_item_image);
			float v[] = new float[9];
			Matrix m = imageView.getImageMatrix();
			m.getValues(v);
			float left, right;
			float width;
			width = imageView.getRomateImageWidth();
			if (imageView.getVisibility() == View.VISIBLE) {
				if ((int) width <= Data.screenWidth) {
					if (velocityX > 200
							|| imageView.gestureImageViewTouchListener
									.getIsTotBefore()) {
						result = doScrollView(e1, e2, -300, 0);
					} else if (velocityX < -200
							|| imageView.gestureImageViewTouchListener
									.getIsToNext()) {
						result = doScrollView(e1, e2, 300, 0);
					}
				} else {
					left = v[Matrix.MTRANS_X];
					right = left + width;
					Rect r = new Rect();
					imageView.getGlobalVisibleRect(r);

					if (velocityX > 0) {
						if (r.left > 0
								|| right < Data.screenWidth
								|| imageView.gestureImageViewTouchListener
										.getIsToNext()) {
							if (velocityX > 200
									|| imageView.gestureImageViewTouchListener
											.getIsTotBefore()) {
								result = doScrollView(e1, e2, -300, 0);
							}
						}
					} else if (velocityX < 0) {
						if (left > 0
								|| r.right < Data.screenWidth
								|| imageView.gestureImageViewTouchListener
										.getIsTotBefore()) {
							if (velocityX < -200
									|| imageView.gestureImageViewTouchListener
											.getIsToNext()) {
								result = doScrollView(e1, e2, 300, 0);
							}
						}
					}
				}
			} else {
				if (velocityX > 200) {
					result = doScrollView(e1, e2, -300, 0);
				} else if (velocityX < -200) {
					result = doScrollView(e1, e2, 300, 0);
				}
			}
		}
		return result;
	}

	public void setSingleTapListener(SingleTapListener singleTapListener) {
		this.singleTapListener = singleTapListener;
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		public boolean onDoubleTap(MotionEvent e) {
			if (getLastVisiblePosition() == getFirstVisiblePosition()) {
				View view = ImageGalleryView.this.getSelectedView();
				// if (view instanceof GestureImageView) {
				if (view instanceof RelativeLayout) {
					imageView = (GestureImageView) view
							.findViewById(R.id.image_gallery_item_image);
					if (imageView.getVisibility() == View.VISIBLE) {
						imageView.gestureImageViewTouchListener.startZoom(e);
					}
				}
			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (singleTapListener != null) {
				singleTapListener.doSngleTap();
				return true;
			}
			return false;
		}
	}
}
