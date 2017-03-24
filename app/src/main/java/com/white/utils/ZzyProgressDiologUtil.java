package com.white.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.hldj.hmyg.R;

public class ZzyProgressDiologUtil {

	
	/**
	 * 生成弹出窗,可取消
	 * 
	 * @param activity
	 * @return
	 */
	public static Dialog createCustomeDialog(Activity activity, String text) {
		Dialog pd;
		pd = new Dialog(activity, R.style.myDialogTheme);
		View layout = activity.getLayoutInflater().inflate(
				R.layout.menu_refresh_popwin, null);
		TextView popWinMes = (TextView) layout
				.findViewById(R.id.menuRefreshMesTv);
		popWinMes.setText(text);
		pd.setContentView(layout);
		pd.setCancelable(true);
		return pd;
	}

	/**
	 * 生成弹出窗,可取消
	 * 
	 * @param activity
	 * @return
	 */
	public static Dialog createCustomeDialog(Activity activity, int Rid) {
		Dialog pd;
		pd = new Dialog(activity, R.style.myDialogTheme);
		View layout = activity.getLayoutInflater().inflate(
				R.layout.menu_refresh_popwin, null);
		TextView popWinMes = (TextView) layout
				.findViewById(R.id.menuRefreshMesTv);
		popWinMes.setText(Rid);
		pd.setContentView(layout);
		pd.setCancelable(true);
		return pd;
	}

	/**
	 * 生成弹出窗,不可取消
	 * 
	 * @param activity
	 * @return
	 */
	public static Dialog createCustomeDialogDisCancle(Activity activity,
			String text) {
		Dialog pd;
		pd = new Dialog(activity, R.style.myDialogTheme);
		View layout = activity.getLayoutInflater().inflate(
				R.layout.menu_refresh_popwin, null);
		TextView popWinMes = (TextView) layout
				.findViewById(R.id.menuRefreshMesTv);
		popWinMes.setText(text);
		pd.setContentView(layout);
		pd.setCancelable(false);
		return pd;
	}

	/**
	 * 生成弹出窗,不可取消
	 * 
	 * @param activity
	 * @return
	 */
	public static Dialog createCustomeDialogDisCancle(Activity activity, int Rid) {
		Dialog pd;
		pd = new Dialog(activity, R.style.myDialogTheme);
		View layout = activity.getLayoutInflater().inflate(
				R.layout.menu_refresh_popwin, null);
		TextView popWinMes = (TextView) layout
				.findViewById(R.id.menuRefreshMesTv);
		popWinMes.setText(Rid);
		pd.setContentView(layout);
		pd.setCancelable(false);
		return pd;
	}
}
