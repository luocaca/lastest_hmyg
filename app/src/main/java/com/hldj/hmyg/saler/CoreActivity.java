package com.hldj.hmyg.saler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hldj.hmyg.R;
import com.white.utils.AndroidUtil;
import com.white.utils.GlobalData;
import com.white.utils.ZzyProgressDiologUtil;

public abstract class CoreActivity extends FragmentActivity {

	/** 系统输入管理对象 */
	private InputMethodManager inputMethod;

	protected Dialog pd;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// if (GlobalData.currentAccount == null) {
		// // 如果数据异常，直接返回程序入口
		// int loginState = SystemSetting.getInstance().getLoginState();
		// if (loginState != GlobalConstant.LOGIN_STATE_NO) {
		// Intent intent = new Intent(this, BossmailQActivity.class);
		// startActivity(intent);
		// return;
		// } else {
		// doOnCreate(savedInstanceState);
		// }
		// } else {
		doOnCreate(savedInstanceState);
		// }
	}

	/**
	 * 显示等待对话框
	 * 
	 * @param isCanCancel
	 *            TODO
	 */
	public void showDialog(boolean isCanCancel) {
		if (!isCanCancel) {
			pd = ZzyProgressDiologUtil.createCustomeDialogDisCancle(this,
					R.string.please_wait);
		} else {
			pd = ZzyProgressDiologUtil.createCustomeDialog(this,
					R.string.please_wait);
		}
		pd.show();
	}

	/**
	 * 隐藏对话框
	 */
	public void hideDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	protected abstract void doOnCreate(Bundle savedInstanceState);

	protected void superOnResume() {
		super.onResume();
		// GlobalData.isJumping = false;
	}

	protected void superOnStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// logger.info("onStart");
		super.onStart();
		// if (SystemSetting.getInstance().isProgramStop
		// && SystemSetting.getInstance().isGestureCodeOpen
		// && !SystemSetting.getInstance().gestureCode.equals("")
		// && !GlobalData.isScreenLockActive
		// && !GlobalData.isSafeLoginActive
		// && GlobalData.currentAccount != null
		// && !GlobalData.isFromOtherIntent) {// 若果是程序从后台切换到前台则进入锁屏界面且保护状态打开
		// ZzyUtil.printMessage("--------core start---------");
		// Log4JUtil.warn("go to ScreenLockActivity");
		// Intent toScrLock = new Intent(this, ScreenLockActivity.class);
		// startActivity(toScrLock);
		// }
		// SystemSetting.getInstance().saveIsProgramStop(this, false);
		// GlobalData.isFromOtherIntent = false;

	}

	@Override
	protected void onResume() {
		super.onResume();
		// GlobalData.isJumping = false;
		// 取消通知栏图标
		new Handler().post(new Runnable() {// 设置字体为系统默认大小，不受系统设置影响

					@Override
					public void run() {
						Resources resource = getResources();
						Configuration c = resource.getConfiguration();
						c.fontScale = (float) 1.0;
						resource.updateConfiguration(c,
								resource.getDisplayMetrics());
					}
				});
		if(!GlobalData.isActivity){
			GlobalData.isActivity = true;
//			ServerInfoManager manager = new ServerInfoManager(false, false);
//			manager.sendZzyHttprequest();
		}
	}

	/**
	 * 隐藏键盘
	 */
	public void hideKeyboard() {
		if (this.getCurrentFocus() != null) {
			inputMethod.hideSoftInputFromWindow(
					// 隐藏软键盘
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 显示键盘
	 */
	public void showKeyboard(EditText inputEt) {
		inputMethod.showSoftInput(inputEt, 0);
	}
	
	/**
	 * 切换键盘显示/隐藏
	 */				
	public void toggleKeyboard() {
		inputMethod.toggleSoftInput(0, 0);
	}


	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		// GlobalData.isJumpInActivity = true;
		// GlobalData.isJumping = true;
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		// GlobalData.isJumpInActivity = true;
		// GlobalData.isJumping = true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		hideKeyboard();
		super.onPause();
	}

	protected void superOnStop() {
		super.onStop();
	}

	@Override
	protected void onStop() {
		// logger.info("coreActivity...onStop");
		super.onStop();
		// if (GlobalData.isJumpInActivity) {
		// GlobalData.isJumpInActivity = false;
		// if (!GlobalData.isJumping) {
		// return;
		// }
		// }
		// if (!ZzyUtil.isClientRunTop(this)) {
		// NotificationHelper.notificationNetwork(this);
		// if (GlobalData.currentAccount != null) {
		// SystemSetting.getInstance().saveIsProgramStop(this, true);
		// }
		// // 清除bitmap对象
		// BitmapUtil.clearMap();
		// GlobalData.isBackPressed = false;
		// }
		if(!AndroidUtil.isClientRunTop(CoreActivity.this)){
			GlobalData.isActivity = false;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		try {
			super.onRestoreInstanceState(savedInstanceState);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
							| AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
							| AudioManager.FLAG_SHOW_UI);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// GlobalData.isFromOtherIntent = true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
