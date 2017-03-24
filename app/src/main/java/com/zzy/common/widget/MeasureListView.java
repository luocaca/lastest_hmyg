package com.zzy.common.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 全部显示的LISTVIEW
 */
@SuppressLint("NewApi")
public class MeasureListView extends ListView {

	@SuppressLint("NewApi")
	public MeasureListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOverScrollMode(ListView.OVER_SCROLL_NEVER);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public MeasureListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(ListView.OVER_SCROLL_NEVER);
	}

	public MeasureListView(Context context) {
		super(context);
		setOverScrollMode(ListView.OVER_SCROLL_NEVER);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
