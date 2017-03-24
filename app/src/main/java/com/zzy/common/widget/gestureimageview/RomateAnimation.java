package com.zzy.common.widget.gestureimageview;


public class RomateAnimation implements Animation {

	private boolean firstFrame = true;

	private float touchX;
	private float touchY;

	private float rotation;
	private float zoom;

	private float startX;
	private float startY;
	private float startRotation;
	private float startScale;

	private float xDiff;
	private float yDiff;
	private float rotationDiff;
	private float scaleDiff;

	private long animationLengthMS = 200;
	private long totalTime = 0;

	private boolean isNeedScale = false;
	private RomateAnimationListener romateAnimationListener;

	@Override
	public boolean update(GestureImageView view, long time) {
		if (firstFrame) {
			firstFrame = false;
			startX = view.getImageX();
			startY = view.getImageY();
			startScale = view.getScale();
			rotationDiff = rotation - startRotation;
			scaleDiff = zoom - startScale;
			if (rotationDiff > 0) {
				xDiff = view.getCenterX() - startX;
				yDiff = view.getCenterY() - startY;
			}
		}
		totalTime += time;
		float ratio = (float) totalTime / (float) animationLengthMS;
		if (ratio < 1) {
			if (ratio > 0) {
				// we still have time left
				float newRotation = (ratio * rotationDiff) + startRotation;
				float newScale = (ratio * scaleDiff) + startScale;
				float newX = (ratio * xDiff) + startX;
				float newY = (ratio * yDiff) + startY;
				if (romateAnimationListener != null) {
					romateAnimationListener.onRomate(newRotation, isNeedScale,
							newScale, newX, newY);
				}
			}
			return true;
		} else {
			float newRotation = rotationDiff + startRotation;
			float newScale = scaleDiff + startScale;
			float newX = xDiff + startX;
			float newY = yDiff + startY;
			if (romateAnimationListener != null) {
				romateAnimationListener.onRomate(newRotation, isNeedScale,
						newScale, newX, newY);
				romateAnimationListener.onComplete(isNeedScale);
				reset();
			}
			return false;
		}
	}

	public void reset() {
		firstFrame = true;
		isNeedScale = false;
		totalTime = 0;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getStartRotation() {
		return startRotation;
	}

	public void setStartRotation(float startRotation) {
		this.startRotation = startRotation;
	}

	public boolean isNeedScale() {
		return isNeedScale;
	}

	public void setNeedScale(boolean isNeedScale) {
		this.isNeedScale = isNeedScale;
	}

	public float getTouchX() {
		return touchX;
	}

	public void setTouchX(float touchX) {
		this.touchX = touchX;
	}

	public float getTouchY() {
		return touchY;
	}

	public void setTouchY(float touchY) {
		this.touchY = touchY;
	}

	public long getAnimationLengthMS() {
		return animationLengthMS;
	}

	public void setAnimationLengthMS(long animationLengthMS) {
		this.animationLengthMS = animationLengthMS;
	}

	public RomateAnimationListener getRomateAnimationListener() {
		return romateAnimationListener;
	}

	public void setRomateAnimationListener(
			RomateAnimationListener romateAnimationListener) {
		this.romateAnimationListener = romateAnimationListener;
	}
}
