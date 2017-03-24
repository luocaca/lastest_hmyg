package com.hy.utils;

import android.content.Context;
import android.widget.Toast;

import com.white.utils.AndroidUtil;

public class CommonToastUtil {

	static Toast t = null;

	public static void showShortToast(Context context, String text) {
		if (AndroidUtil.isClientRunTop(context)) {// 前台运行
			if (t == null) {
				t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			} else {
				t.setText(text);
			}
			t.show();
		}
	}

	public static void showShortToast_All(Context context, String text) {
		if (t == null) {
			t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			t.setText(text);
		}
		t.show();
	}

	public static void showShortToast(Context context, int textRid) {
		if (AndroidUtil.isClientRunTop(context)) {// 前台运行
			if (t == null) {
				t = Toast.makeText(context, textRid, Toast.LENGTH_SHORT);
			} else {
				t.setText(textRid);
			}
			t.show();
		}
	}

	public static void showShortToast_All(Context context, int textRid) {
		if (t == null) {
			t = Toast.makeText(context, textRid, Toast.LENGTH_SHORT);
		} else {
			t.setText(textRid);
		}
		t.show();
	}

	public static void cancelToast() {
		if (t != null) {
			t.cancel();
		}
	}
}
