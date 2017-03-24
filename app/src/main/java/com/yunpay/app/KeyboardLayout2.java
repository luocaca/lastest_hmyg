package com.yunpay.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class KeyboardLayout2 extends LinearLayout {

    private onSizeChangedListener mChangedListener;
    private static final String TAG ="KeyboardLayoutTAG";
    private boolean mShowKeyboard = false;
    
    public KeyboardLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public KeyboardLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public KeyboardLayout2(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure-----------");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout-------------------");
    }
    
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "--------------------------------------------------------------");
        Log.d(TAG, "w----" + w + "\n" + "h-----" + h + "\n" + "oldW-----" + oldw + "\noldh----" + oldh);
        if (null != mChangedListener && 0 != oldw && 0 != oldh) {
            if (h < oldh) {
                mShowKeyboard = true;
            } else {
                mShowKeyboard = false;
            }
            mChangedListener.onChanged(mShowKeyboard);
            Log.d(TAG, "mShowKeyboard-----      " + mShowKeyboard);
        }
    }
    
    public void setOnSizeChangedListener(onSizeChangedListener listener) {
        mChangedListener = listener;
    }
    
    public interface onSizeChangedListener{
        
        void onChanged(boolean showKeyboard);
    }
    
}
