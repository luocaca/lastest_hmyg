package com.hldj.hmyg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.neopixl.pixlui.components.textview.TextView;

/**
 * Created by Administrator on 2017/4/1.
 */

public class DrawableCenterText extends TextView {


    public DrawableCenterText(Context context) {
        super(context);
        initView();
    }

    public DrawableCenterText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DrawableCenterText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (null != drawables) {
            Drawable drawableLeft = drawables[0];
            //文字宽度
            float textWidth = getPaint().measureText(getText().toString());
            if (null != drawableLeft) {
                setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                //内容区域
                float contentWidth = textWidth + getCompoundDrawablePadding() + drawableLeft.getIntrinsicWidth();
                //向X轴的正方向平移
                canvas.translate((getWidth() - contentWidth) / 2, 0);
            }
            Drawable drawableRight = drawables[2];
            if (null != drawableRight ){
                setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                //内容区域
                float contentWidth = textWidth + getCompoundDrawablePadding() + drawableRight.getIntrinsicWidth();
                //向X轴的正方向平移
                canvas.translate(-(getWidth() - contentWidth) / 2, 0);
            }

        }






        super.onDraw(canvas);
    }
}