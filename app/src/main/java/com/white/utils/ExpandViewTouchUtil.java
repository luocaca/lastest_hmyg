package com.white.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

public class ExpandViewTouchUtil {
	/** 
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围 
     *  
     * @param view 
     * @param top 
     * @param bottom 
     * @param left 
     * @param right 
     */  
    public static void expandViewTouchDelegate(final View view, final int top,  
            final int bottom, final int left, final int right) {  
  
        ((View) view.getParent()).post(new Runnable() {  
            @Override  
            public void run() {  
                Rect bounds = new Rect();  
                view.setEnabled(true);  
                view.getHitRect(bounds);  
  
                bounds.top -= top;  
                bounds.bottom += bottom;  
                bounds.left -= left;  
                bounds.right += right;  
  
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);  
  
                if (View.class.isInstance(view.getParent())) {  
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);  
                }  
            }  
        });  
    }  
}
