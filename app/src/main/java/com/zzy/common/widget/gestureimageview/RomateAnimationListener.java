package com.zzy.common.widget.gestureimageview;

public interface RomateAnimationListener {

	public void onRomate(float rotation, boolean needScale, float scale,
			float x, float y);

	public void onComplete(boolean needScale);
}
