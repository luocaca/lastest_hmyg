package com.hldj.hmyg.util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.hldj.hmyg.R;
import com.white.utils.SystemSetting;

/**
 * 对mianactivity 重复的代码剥离
 * Created by Administrator on 2017/4/10.
 */
public class MyUtil {




    public static int getScreenHeight(boolean islandscape, Context context) {
        if (islandscape) {
            return getScreenWidth(false, context);
        }
        if (SystemSetting.getInstance(context).screenHeight != 0)
            return SystemSetting.getInstance(context).screenHeight;
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        SystemSetting.getInstance(context).setGlobalScreenHeight(
                Math.max(display.getWidth(), display.getHeight()));
        return SystemSetting.getInstance(context).screenHeight;
    }

    /**
     * 获得屏幕宽
     *
     * @param islandscape TODO
     */
    public static int getScreenWidth(boolean islandscape, Context context) {
        if (islandscape) {
            return getScreenHeight(false, context);
        }
        if (SystemSetting.getInstance(context).screenWidth != 0)
            return SystemSetting.getInstance(context).screenWidth;
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        SystemSetting.getInstance(context).setGlobalScreenWidth(
                Math.min(display.getWidth(), display.getHeight()));
        return SystemSetting.getInstance(context).screenWidth;
    }

    /**
     * 打开activity 动画
     */
    public static void overridePendingTransition_open(Activity activity) {
        //进
        //100  ---   0
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_left_out);
        //出
        //0  --- -100
    }

    /**
     * 关闭activity 动画
     */


    public static void overridePendingTransition_back(Activity activity) {
        //进
        //-100  ---   0
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        //出
        //0  --- 100
    }

}
