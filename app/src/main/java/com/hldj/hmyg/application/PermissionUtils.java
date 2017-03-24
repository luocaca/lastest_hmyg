package com.hldj.hmyg.application;

import android.Manifest;
import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzq on 2016/6/30.
 */
public class PermissionUtils {
	static Activity context;
	static ArrayList<String> allNeedPermissions = new ArrayList<String>();

	public PermissionUtils(Activity context) {
		this.context = context;
	}

	public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 200;

	public static void needPermission(int requestCode) {
		if (Build.VERSION.SDK_INT < 23) {
			return;
		}
		requestAllPermissions(requestCode);
	}

	public static void requestAllPermissions(int requestCode) {

		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			allNeedPermissions.add(Manifest.permission.CALL_PHONE);
		}
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			allNeedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			allNeedPermissions.add(Manifest.permission.CAMERA);
		}

		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			allNeedPermissions.add(Manifest.permission.READ_CONTACTS);
		}
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			allNeedPermissions.add(Manifest.permission.GET_ACCOUNTS);
		}
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			allNeedPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
		}

		if (allNeedPermissions.size() > 0) {
			String[] needReqPre = new String[allNeedPermissions.size()];
			for (int i = 0; i < allNeedPermissions.size(); i++) {
				needReqPre[i] = allNeedPermissions.get(i);
			}

			ActivityCompat.requestPermissions(context, needReqPre,
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			// ActivityCompat.requestPermissions(context, new String[] {
			// Manifest.permission.CALL_PHONE,
			// Manifest.permission.READ_EXTERNAL_STORAGE,
			// Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,
			// Manifest.permission.GET_ACCOUNTS,
			// Manifest.permission.ACCESS_FINE_LOCATION },
			// MY_PERMISSIONS_REQUEST_CALL_PHONE);
		}

	}

	public static boolean requesCallPhonePermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.CALL_PHONE },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean requestMountUnmountFilesystemsPermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}

	public static boolean requestReadSDCardPermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}

	public static boolean requestCamerPermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.CAMERA },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}
	
	
	public static boolean requestREAD_PHONE_STATE(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.READ_PHONE_STATE },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}

	public static boolean requestReadConstantPermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.READ_CONTACTS },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}

	public static boolean requestGET_ACCOUNTSPermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.GET_ACCOUNTS },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}

	public static boolean requestLocationPermissions(int requestCode) {
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {// 没有权限
			ActivityCompat.requestPermissions(context,
					new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
					MY_PERMISSIONS_REQUEST_CALL_PHONE);
			return false;
		} else {
			return true;
		}
	}
}