package com.neopixl.pixlui.components.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.hldj.hmyg.R;
import com.neopixl.pixlui.intern.FontStyleView;
import com.neopixl.pixlui.intern.PixlUIUtils;

/**
 * TextView with custom font by XML or Code This class provided too a font
 * factory
 * 
 * @author odemolliens
 * 
 */
public class TextView extends android.widget.TextView {

	public TextView(Context context) {
		this(context, null);
	}

	public TextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}