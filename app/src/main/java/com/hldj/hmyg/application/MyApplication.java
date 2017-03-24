package com.hldj.hmyg.application;

import im.fir.sdk.FIR;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Vibrator;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.testin.agent.TestinAgent;

public class MyApplication extends Application {

	public static SharedPreferences Userinfo;
	public static SharedPreferences Deviceinfo;
	private static MyApplication myApplication = null;

	public TextView mLocationResult, logMsg;
	public TextView trigger, exit;
	public Vibrator mVibrator;

	@SuppressLint("SdCardPath")
	public void onCreate() {
		super.onCreate();
		FIR.init(this);
		TestinAgent.init(this, "S9Ip9zGgJzj779e9S849s9z94X9DTUGJ",
				"your channel ID");
		CrashReport
				.initCrashReport(getApplicationContext(), "900021393", false);
		ShareSDK.initSDK(this);
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());
		// 本地奔溃保存
		Userinfo = getSharedPreferences("Userinfo", Context.MODE_PRIVATE);
		Deviceinfo = getSharedPreferences("Deviceinfo", Context.MODE_PRIVATE);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		initImageLoader(getApplicationContext());
		createSDCardDir(getApplicationContext());
		// 由于Application类本身已经单例，所以直接按以下处理即可。
		myApplication = this;
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);

	}

	public static MyApplication getInstance() {
		return myApplication;
	}

	@SuppressLint("SdCardPath")
	public static void createSDCardDir(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = "/mnt/sdcard/hmyg";
			File file = new File(path);
			if (!file.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				file.mkdirs();
			}
		} else {
			// Toast.makeText(context, "未检查到sd卡，部分功能无法使用",
			// Toast.LENGTH_SHORT).show();
		}

	}

	public static int dp2px(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dp(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
