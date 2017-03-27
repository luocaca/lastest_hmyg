package me.imid.swipebacklayout.lib.app;

import android.os.Build;
import android.os.Bundle;

import com.hldj.hmyg.R;

public class NeedSwipeBackActivity extends SwipeBackBActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		if (Build.VERSION.SDK_INT >= 23) {
			setSwipeBackEnable(true);
//		}
	}


    /**
     * 打开activity 动画
     */
    public void overridePendingTransition_open() {
        //进
        //100  ---   0
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_left_out);
        //出
        //0  --- -100
    }

    /**
     * 关闭activity 动画
     */


    public  void overridePendingTransition_back() {
        //进
        //-100  ---   0
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        //出
        //0  --- 100
    }

}
