package me.imid.swipebacklayout.lib.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hldj.hmyg.R;

@SuppressLint("NewApi")
public class SwipeBackActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
