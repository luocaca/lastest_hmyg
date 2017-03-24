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

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

@SuppressLint("NewApi") public class GestureImageViewTouchListener implements OnTouchListener {

	private GestureImageView image = null;
	private OnClickListener onClickListener;

	private final PointF current = new PointF();
	private final PointF last = new PointF();
	private final PointF next = new PointF();
	private final PointF midpoint = new PointF();

	private final VectorF scaleVector = new VectorF();
	private final VectorF pinchVector = new VectorF();

	private boolean touched = false;
	private boolean inZoom = false;

	private float initialDistance;
	private float initialRotation = 0.0f;
	private float lastScale = 1.0f;
	private float currentScale = 1.0f;
	private float currentRotation = 0.0f;
	private float lastRotation = 0.0f;

	private float boundaryLeft = 0;
	private float boundaryTop = 0;
	private float boundaryRight = 0;
	private float boundaryBottom = 0;

	private float maxScale = 5.0f;
	private float minScale = 0.25f;

	private float centerX = 0;
	private float centerY = 0;

	private float startingScale = 0;
	private float startingRomateScale = 0;
	private boolean isChoosePic = false;
	/** 选取图片框的宽度 */
	private float chooseframeWidth = 0;
	/** 选取图片框的高度 */
	private float chooseframeHeight = 0;

	private boolean canDragX = false;
	private boolean canDragY = false;

	private boolean multiTouch = false;

	private int displayWidth;
	private int displayHeight;

	private FlingListener flingListener;
	private FlingAnimation flingAnimation;
	private RomateAnimation romateAnimation;
	private ZoomAnimation zoomAnimation;
	private MoveAnimation moveAnimation;
	private GestureDetector tapDetector;
	private GestureDetector flingDetector;
	private GestureImageViewListener imageListener;

	/** 是否能进行切换下一张 */
	private boolean isToNext = false;
	/** 是否能进行切换上一张 */
	private boolean isToBefore = false;
	/** 是否旋转了 */
	private boolean isRotation = false;
	/** 是否正在旋转 */
	private boolean isInRomating = false;
	/** 单次手势操作是否可旋转 */
	private boolean isTempCanRomate = true;
	/** 是否可旋转 */
	private boolean isCanRomate = false;
	/** 图片类型 */
	private int picType = RomateOverCallback.TYPE_CHAT_PIC;
	/** 图片数据库id */
	private long picDBId = 0;

	public GestureImageViewTouchListener(final GestureImageView image,
			int displayWidth, int displayHeight, float startingScale,
			float startingRomateScale, float startingRotation,
			float chooseframeWidth, float chooseframeHeight,
			Rect chooseframeRect, boolean isChoosePic, boolean isCanRomate,
			int picType, long picDBId) {
		super();
		this.image = image;

		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;

		this.startingScale = startingScale;
		this.startingRomateScale = startingRomateScale;

		this.isChoosePic = isChoosePic;
		this.chooseframeWidth = chooseframeWidth;
		this.chooseframeHeight = chooseframeHeight;

		this.isCanRomate = isCanRomate;
		this.picType = picType;
		this.picDBId = picDBId;

		if (!isChoosePic) {
			this.centerY = (float) displayHeight / 2.0f;
		} else {
			this.centerY = (float) ((chooseframeRect.bottom - chooseframeRect.top) / 2.0f + chooseframeRect.top);
		}
		this.centerX = (float) displayWidth / 2.0f;

		if (MathUtils.isPortraitByDegree(startingRotation)) {
			currentScale = startingScale;
			lastScale = startingScale;
		} else {
			currentScale = startingRomateScale;
			lastScale = startingRomateScale;
		}

		currentRotation = startingRotation;
		lastRotation = startingRotation;

		if (!isChoosePic) {
			boundaryRight = displayWidth;
			boundaryBottom = displayHeight;
			boundaryLeft = 0;
			boundaryTop = 0;
		} else {
			boundaryRight = chooseframeRect.right;
			boundaryBottom = chooseframeRect.bottom;
			boundaryLeft = chooseframeRect.left;
			boundaryTop = chooseframeRect.top;
		}

		next.x = image.getImageX();
		next.y = image.getImageY();

		flingListener = new FlingListener();
		flingAnimation = new FlingAnimation();
		romateAnimation = new RomateAnimation();
		zoomAnimation = new ZoomAnimation();
		moveAnimation = new MoveAnimation();

		flingAnimation.setListener(new FlingAnimationListener() {
			@Override
			public void onMove(float x, float y) {
				handleDrag(current.x + x, current.y + y);
			}

			@Override
			public void onComplete() {
			}
		});

		zoomAnimation.setZoom(2.0f);
		zoomAnimation.setZoomAnimationListener(new ZoomAnimationListener() {
			@Override
			public void onZoom(float scale, float x, float y) {
				if (scale <= maxScale && scale >= minScale) {
					handleScale(scale, x, y);
				}
			}

			@Override
			public void onComplete() {
				inZoom = false;
				handleUp();
			}
		});

		romateAnimation.setRotation(0.0f);
		romateAnimation
				.setRomateAnimationListener(new RomateAnimationListener() {

					@Override
					public void onRomate(float rotation, boolean needScale,
							float scale, float x, float y) {
						handleRomate(rotation);
						if (needScale) {
							handleScale(scale, x, y);
						}
					}

					@Override
					public void onComplete(boolean needScale) {
						isRotation = false;
						isInRomating = false;
						handleUp();
						lastRotation = currentRotation;
						setMaxScale(GestureImageViewTouchListener.this.image
								.getMaxScale());
						setMinScale(GestureImageViewTouchListener.this.image
								.getMinScale());
						if (needScale) {
							RomateOverCallback
									.getInstance()
									.notifyRomateOver(
											GestureImageViewTouchListener.this.picType,
											GestureImageViewTouchListener.this.picDBId,
											(int) MathUtils
													.calculateCurrentRotation(lastRotation));
						}
					}
				});

		moveAnimation.setMoveAnimationListener(new MoveAnimationListener() {

			@Override
			public void onMove(float x, float y) {
				image.setPosition(x, y);
				image.redraw();
			}
		});

		tapDetector = new GestureDetector(image.getContext(),
				new SimpleOnGestureListener() {
					@Override
					public boolean onDoubleTap(MotionEvent e) {
						startZoom(e);
						return true;
					}

					@Override
					public boolean onSingleTapConfirmed(MotionEvent e) {
						if (!inZoom) {
							if (onClickListener != null) {
								onClickListener.onClick(image);
								return true;
							}
						}
						return false;
					}
				});

		flingDetector = new GestureDetector(image.getContext(), flingListener);
		imageListener = image.getGestureImageViewListener();
		calculateBoundaries();
	}

	private void startFling() {
		flingAnimation.setVelocityX(flingListener.getVelocityX());
		flingAnimation.setVelocityY(flingListener.getVelocityY());
		image.animationStart(flingAnimation);
	}

	private void startRomate(float rotation) {
		if (currentRotation < 45) {
			rotation = 0.0f;
		} else if (currentRotation < 135 && currentRotation >= 45) {
			rotation = 90.0f;
		} else if (currentRotation >= 135 && currentRotation < 225) {
			rotation = 180.0f;
		} else if (currentRotation > 225 && currentRotation <= 315) {
			rotation = 270.0f;
		} else {
			rotation = 360.0f;
		}
		if ((((int) lastRotation / 90) != ((int) rotation / 90))
				&& (MathUtils.calculateCurrentRotation(rotation) != MathUtils
						.calculateCurrentRotation(lastRotation))) {
			isRotation = true;
		} else {
			isRotation = false;
		}
		romateAnimation.reset();
		romateAnimation.setNeedScale(isRotation);
		romateAnimation.setStartRotation(currentRotation);
		romateAnimation.setRotation(rotation);
		if (MathUtils.isPortraitByDegree(rotation)) {
			romateAnimation.setZoom(startingScale);
		} else {
			romateAnimation.setZoom(startingRomateScale);
		}
		image.animationStart(romateAnimation);
	}

	public void startZoom(MotionEvent e) {
		inZoom = true;
		zoomAnimation.reset();
		float zoomTo = 1.0f;
		if (currentScale == startingScale) {
			if (MathUtils.isPortraitByDegree(currentRotation)) {
				zoomTo = startingRomateScale;
				zoomAnimation.setTouchX(image.getCenterX());
				zoomAnimation.setTouchY(image.getCenterY());
			} else {
				zoomTo = currentScale * 2.0f;
				zoomAnimation.setTouchX(e.getX());
				zoomAnimation.setTouchY(e.getY());
			}
		} else if (currentScale == startingRomateScale) {
			if (!MathUtils.isPortraitByDegree(currentRotation)) {
				zoomTo = startingScale;
				zoomAnimation.setTouchX(image.getCenterX());
				zoomAnimation.setTouchY(image.getCenterY());
			} else {
				zoomTo = currentScale * 2.0f;
				zoomAnimation.setTouchX(e.getX());
				zoomAnimation.setTouchY(e.getY());
			}
		} else {
			if (MathUtils.isPortraitByDegree(currentRotation)) {
				zoomTo = startingScale;
			} else {
				zoomTo = startingRomateScale;
			}
			zoomAnimation.setTouchX(image.getCenterX());
			zoomAnimation.setTouchY(image.getCenterY());
		}
		zoomAnimation.setZoom(zoomTo);
		image.animationStart(zoomAnimation);
	}

	private void stopAnimations() {
		image.animationStop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean result = true;
		if (!inZoom) {
			if (!tapDetector.onTouchEvent(event)) {
				if (event.getPointerCount() == 1
						&& flingDetector.onTouchEvent(event)) {
					startFling();
				}
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_CANCEL) {
					handleUp();
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					stopAnimations();
					last.x = event.getX();
					last.y = event.getY();
					if (imageListener != null) {
						imageListener.onTouch(last.x, last.y);
					}
					touched = true;
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (event.getPointerCount() > 1) {
						multiTouch = true;
						if (initialDistance > 0) {
							pinchVector.set(event);
							pinchVector.calculateLength();
							float distance = pinchVector.length;
							float rotation = MathUtils.getRotation(event)
									- initialRotation;
							if (isCanRomate && isTempCanRomate
									&& (isInRomating || Math.abs(rotation) > 5)) {
								handleRomate(rotation + lastRotation);
							}
							if (initialDistance != distance) {
								float newScale = (distance / initialDistance)
										* lastScale;
								if (newScale <= maxScale) {
									scaleVector.length *= newScale;
									scaleVector.calculateEndPoint();
									scaleVector.length /= newScale;
									float newX = scaleVector.end.x;
									float newY = scaleVector.end.y;
									handleScale(newScale, newX, newY);
								}
							}
						} else {
							isTempCanRomate = isTempCanRomate();
							initialDistance = MathUtils.distance(event);
							initialRotation = MathUtils.getRotation(event);
							MathUtils.midpoint(event, midpoint);
							scaleVector.setStart(midpoint);
							scaleVector.setEnd(next);
							scaleVector.calculateLength();
							scaleVector.calculateAngle();
							scaleVector.length /= lastScale;
						}
					} else {
						if (!touched) {
							touched = true;
							last.x = event.getX();
							last.y = event.getY();
							next.x = image.getImageX();
							next.y = image.getImageY();
						} else if (!multiTouch) {
							if (handleDrag(event.getX(), event.getY())) {
								image.redraw();
							} else {
								result = false;
							}
						}
					}
				}
			}
		}
		return result;
	}

	private boolean isTempCanRomate() {
		boolean result = true;
		if (MathUtils.isPortraitByDegree(currentRotation)) {
			if (image.getScale() > startingScale) {
				result = false;
			}
		} else {
			if (image.getScale() > startingRomateScale) {
				result = false;
			}
		}
		return result;
	}

	protected void handleUp() {
		multiTouch = false;
		isTempCanRomate = true;
		initialDistance = 0;
		initialRotation = 0;
		lastScale = currentScale;
		if (!canDragX) {
			next.x = centerX;
		}
		if (!canDragY) {
			next.y = centerY;
		}
		boundCoordinates();
		image.setScale(currentScale);
		image.setRotation(currentRotation);
		image.setPosition(next.x, next.y);
		if (imageListener != null) {
			imageListener.onScale(currentScale);
			imageListener.onRotation(currentRotation);
			imageListener.onPosition(next.x, next.y);
		}
		image.redraw();
		if (isCanRomate
				&& (!isCurrentRotationRight() || lastRotation != currentRotation)) {
			startRomate(image.getRotation());
		}
	}

	public void handleScale(float scale, float x, float y) {
		currentScale = scale;
		if (currentScale > maxScale) {
			currentScale = maxScale;
		} else if (currentScale < minScale) {
			currentScale = minScale;
		} else {
			next.x = x;
			next.y = y;
		}
		calculateBoundaries();
		image.setScale(currentScale);
		image.setPosition(next.x, next.y);
		if (imageListener != null) {
			imageListener.onScale(currentScale);
			imageListener.onPosition(next.x, next.y);
		}
		image.redraw();
	}

	public void handleRomate(float rotation) {
		rotation = MathUtils.calculateCurrentRotation(rotation);
		currentRotation = rotation;
		isInRomating = true;
		calculateBoundaries();
		image.setRotation(rotation);
		image.setPosition(next.x, next.y);
		if (imageListener != null) {
			imageListener.onRotation(rotation);
			imageListener.onPosition(next.x, next.y);
		}
		image.redraw();
	}

	protected boolean handleDrag(float x, float y) {
		current.x = x;
		current.y = y;
		float diffX = (current.x - last.x);
		float diffY = (current.y - last.y);
		if (diffX != 0 || diffY != 0) {
			if (canDragX)
				next.x += diffX;
			if (canDragY)
				next.y += diffY;
			boundCoordinates();
			last.x = current.x;
			last.y = current.y;
			if (canDragX || canDragY) {
				image.setPosition(next.x, next.y);
				if (imageListener != null) {
					imageListener.onPosition(next.x, next.y);
				}
				return true;
			}
		}
		return false;
	}

	public void reset() {
		currentScale = startingScale;
		next.x = centerX;
		next.y = centerY;
		calculateBoundaries();
		image.setScale(currentScale);
		image.setPosition(next.x, next.y);
		image.redraw();
	}

	public float getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(float maxScale) {
		this.maxScale = maxScale;
	}

	public float getMinScale() {
		return minScale;
	}

	public void setMinScale(float minScale) {
		this.minScale = minScale;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	protected void boundCoordinates() {
		isToBefore = false;
		isToNext = false;
		if (next.x <= boundaryLeft) {
			next.x = boundaryLeft;
			isToBefore = false;
			isToNext = true;
		} else if (next.x >= boundaryRight) {
			next.x = boundaryRight;
			isToBefore = true;
			isToNext = false;
		}
		if (next.y < boundaryTop) {
			next.y = boundaryTop;
		} else if (next.y > boundaryBottom) {
			next.y = boundaryBottom;
		}
	}

	/**
	 * 是否能切换下一张图片
	 */
	public boolean getIsToNext() {
		return isToNext;
	}

	/**
	 * 是否能切换上一张图片
	 */
	public boolean getIsTotBefore() {
		return isToBefore;
	}

	protected void calculateBoundaries() {
		int effectiveWidth = image.getRomateImageWidth();
		int effectiveHeight = image.getRomateImageHeight();
		if (!isChoosePic) {
			canDragX = effectiveWidth > displayWidth;
			canDragY = effectiveHeight > displayHeight;
		} else {
			canDragX = effectiveWidth > chooseframeWidth;
			canDragY = effectiveHeight > chooseframeHeight;
		}

		if (!isChoosePic) {
			if (canDragX) {
				float diff = (float) (effectiveWidth - displayWidth) / 2.0f;
				boundaryLeft = centerX - diff;
				boundaryRight = centerX + diff;
			}

			if (canDragY) {
				float diff = (float) (effectiveHeight - displayHeight) / 2.0f;
				boundaryTop = centerY - diff;
				boundaryBottom = centerY + diff;
			}
		} else {
			if (canDragX) {
				float diff = (float) (effectiveWidth - chooseframeWidth) / 2.0f;
				boundaryLeft = centerX - diff;
				boundaryRight = centerX + diff;
			}

			if (canDragY) {
				float diff = (float) (effectiveHeight - chooseframeHeight) / 2.0f;
				boundaryTop = centerY - diff;
				boundaryBottom = centerY + diff;
			}
		}
	}

	private boolean isCurrentRotationRight() {
		return currentRotation % 90.0f == 0;
	}
}
