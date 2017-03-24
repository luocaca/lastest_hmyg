package me.imid.swipebacklayout.lib.app;

import android.os.Build;
import android.os.Bundle;

public class NeedSwipeBackActivity extends SwipeBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		if (Build.VERSION.SDK_INT >= 23) {
//			setSwipeBackEnable(false);
//		}
	}
}
