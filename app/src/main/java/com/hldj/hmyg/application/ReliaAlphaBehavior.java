//package com.hldj.hmyg.application;
//
//import android.content.Context;
//import android.support.design.widget.CoordinatorLayout;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
//import com.hldj.hmyg.R;
//import com.hy.utils.ToastUtil;
//import com.neopixl.pixlui.components.relativelayout.RelativeLayout;
//
//public class ReliaAlphaBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {
//    private static final String TAG = "ToolbarAlphaBehavior";
//    private int offset = 0;
//    private int startOffset = 0;
//    private int endOffset = 0;
//    private Context context;
//
//    public ReliaAlphaBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//    }
//
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, RelativeLayout child, View directTargetChild, View target, int nestedScrollAxes) {
//        return true;
//    }
//
//
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, RelativeLayout toolbar, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Log.e("ToolbarAlphaBehavior", "onNestedScroll: "+endOffset);
////      ToastUtil.showShortToast(MyApplication.getInstance(),"hellow world"+endOffset);
//        startOffset = 0;
//        endOffset = context.getResources().getDimensionPixelOffset(R.dimen.header_height) - toolbar.getHeight();
//        offset += dyConsumed;
//        if (offset <= startOffset) {  //alpha为0
//            toolbar.getBackground().setAlpha(0);
//        } else if (offset > startOffset && offset < endOffset) { //alpha为0到255
//            float precent = (float) (offset - startOffset) / endOffset;
//            int alpha = Math.round(precent * 255);
//            toolbar.getBackground().setAlpha(alpha);
//        } else if (offset >= endOffset) {  //alpha为255
//            toolbar.getBackground().setAlpha(255);
//        }
//    }
//
//}