package com.jarvis.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class MaxLinearLayout extends LinearLayout{

	public MaxLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MaxLinearLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	private static final int MAX_HEIGHT = 1600;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View child = getChildAt(0);
		child.measure(widthMeasureSpec, heightMeasureSpec);
		int width = child.getMeasuredWidth();
		int height = Math.min(child.getMeasuredHeight(), MAX_HEIGHT);
		setMeasuredDimension(width, height);
	}

}
