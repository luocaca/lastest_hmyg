package com.white.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.provider.MediaStore;

import com.hldj.hmyg.saler.GlobalConstant;

/**
 * 系统设置模型类 增加属性时修改loadSystemProperty方法
 */
public class SystemSetting {

	private static SystemSetting property;
	private SharedPreferences sp;
	/** 软件版本数字信息 */
	public String ver;
	/** 标准名库最后更新时间 */
	public long standarLibUpdatetime = 0;
	/** 定位的省 */
	public String currentProvince;
	/** 定位的市 */
	public String currentCity;
	/** 手机屏幕高 */
	public int screenHeight = 0;
	/** 手机屏幕宽 */
	public int screenWidth = 0;
	/** 手机可见屏幕高，包括状态栏 */
	public int screenVisiableHeight = 0;
	/** 上一次选择相册的路径ID */
	public String choosePhotoDirId;
	/** 与服务器的时间差 */
	public long serverTimeOffset = 0;
	/** 省市版本 */
	public int provincesVersion = 0;
	/** 省市 */
	public String provinceDataStr = "";
	/** push地址 */
	public String pushServer = "";
	/** push端口 */
	public int pushPost = 0;
	/** banner数据jsonarray的字符串 */
	public String bannerListStr = "";
	/** 是否需要升级 */
	public boolean isUpgrade = false;

	public static SystemSetting getInstance(Context context) {
		if (property == null) {
			property = new SystemSetting();
			property.loadSystemProperty(context);
		}
		return property;
	}

	private SystemSetting() {
	}

	/**
	 * 获取配置文件写对象
	 * 
	 * @return
	 */
	private Editor getSPEditor() {
		Editor editor = sp.edit();
		return editor;
	}

	/**
	 * 读取配置文件中的系统属性
	 */
	private void loadSystemProperty(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(GlobalConstant.SP_SETTING,
					Context.MODE_PRIVATE);
			// 获得版本信息
			// ver = sp.getString(GlobalConstant.SP_VERSION, "");
			ver = AndroidUtil.getVersion(context);
			standarLibUpdatetime = sp.getLong(
					GlobalConstant.SP_STANDAR_LIB_UPDATE_TIME, 0);
			isUpgrade = sp.getBoolean(GlobalConstant.SP_IS_UPGRADE, false);

			currentProvince = sp.getString(GlobalConstant.SP_CURRENT_PROVINCE,
					"");
			currentCity = sp.getString(GlobalConstant.SP_CURRENT_CITY, "");
			screenHeight = sp.getInt(GlobalConstant.SP_GLOBAL_SCREEN_HEIGHT, 0);
			screenWidth = sp.getInt(GlobalConstant.SP_GLOBAL_SCREEN_WIDTH, 0);
			screenVisiableHeight = sp.getInt(
					GlobalConstant.SP_GLOBAL_SCREEN_VISIABLE_HEIGHT, 0);
			choosePhotoDirId = sp.getString(
					GlobalConstant.SP_CHOOSE_PHOTO_DIR_ID, "");
			serverTimeOffset = sp.getLong(GlobalConstant.SP_SERVER_TIME_OFFSET,
					0);
			provincesVersion = sp.getInt(GlobalConstant.SP_PROVINCE_VERSION, 0);
			provinceDataStr = sp.getString(GlobalConstant.SP_PROVINCE_DATA_STR,
					"");
			pushServer = sp.getString(GlobalConstant.SP_PUSH_SERVER_STR, "");
			pushPost = sp.getInt(GlobalConstant.SP_PUSH_POST_STR, 0);
			bannerListStr = sp.getString(
					GlobalConstant.SP_BANNER_JSON_LIST_STR, "");
		}
	}

	/**
	 * 设置版本
	 * 
	 * @param isRingRemind
	 */
	public void setVersion(String ver) {
		Editor editor = getSPEditor();
		this.ver = ver;
		editor.putString(GlobalConstant.SP_VERSION, ver);
		editor.commit();
	}

	/**
	 * 设置标准名库最后更新时间
	 * 
	 */
	public void setStandarLibUpdatetime(long standarLibUpdatetime) {
		this.standarLibUpdatetime = standarLibUpdatetime;
		Editor editor = getSPEditor();
		editor.putLong(GlobalConstant.SP_STANDAR_LIB_UPDATE_TIME,
				standarLibUpdatetime);
		editor.commit();
	}

	/**
	 * 设置省
	 * 
	 * @param isAcceptTempChat
	 */
	public void setCurrentProvince(String currentProvince) {
		this.currentProvince = currentProvince;
		Editor editor = getSPEditor();
		editor.putString(GlobalConstant.SP_CURRENT_PROVINCE, currentProvince);
		editor.commit();
	}

	/**
	 * 设置市
	 * 
	 * @param isAcceptTempChat
	 */
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
		Editor editor = getSPEditor();
		editor.putString(GlobalConstant.SP_CURRENT_CITY, currentCity);
		editor.commit();
	}

	/**
	 * 设置手机屏幕高
	 * 
	 * @param isAutoReceivePic
	 */
	public void setGlobalScreenHeight(int globalscreenheight) {
		this.screenHeight = globalscreenheight;
		Editor editor = getSPEditor();
		editor.putInt(GlobalConstant.SP_GLOBAL_SCREEN_HEIGHT,
				globalscreenheight);
		editor.commit();
	}

	/**
	 * 设置手机屏幕宽
	 * 
	 * @param isAutoReceivePic
	 */
	public void setGlobalScreenWidth(int globalscreenwidth) {
		this.screenWidth = globalscreenwidth;
		Editor editor = getSPEditor();
		editor.putInt(GlobalConstant.SP_GLOBAL_SCREEN_WIDTH, globalscreenwidth);
		editor.commit();
	}

	/**
	 * 设置手机可见屏幕高，包括状态栏
	 * 
	 * @param isAutoReceivePic
	 */
	public void setGlobalScreenVisiableHeight(int globalscreenvisiableheight) {
		this.screenVisiableHeight = globalscreenvisiableheight;
		Editor editor = getSPEditor();
		editor.putInt(GlobalConstant.SP_GLOBAL_SCREEN_VISIABLE_HEIGHT,
				globalscreenvisiableheight);
		editor.commit();
	}

	/**
	 * 设置上一次选取图片路径ID
	 * 
	 */
	public void setChoosePhotoDirId(String choosePhotoDirId) {
		this.choosePhotoDirId = choosePhotoDirId;
		Editor editor = getSPEditor();
		editor.putString(GlobalConstant.SP_CHOOSE_PHOTO_DIR_ID,
				choosePhotoDirId);
		editor.commit();
	}

	/**
	 * 设置与服务器的时间差
	 * 
	 */
	public void setServerTimeOffset(long serverTimeOffset) {
		this.serverTimeOffset = serverTimeOffset;
		Editor editor = getSPEditor();
		editor.putLong(GlobalConstant.SP_SERVER_TIME_OFFSET, serverTimeOffset);
		editor.commit();
	}

	/**
	 * 设置省市数据版本
	 * 
	 */
	public void setProvincesVersion(int provincesVersion) {
		this.provincesVersion = provincesVersion;
		Editor editor = getSPEditor();
		editor.putInt(GlobalConstant.SP_PROVINCE_VERSION, provincesVersion);
		editor.commit();
	}

	/**
	 * 设置省市数据
	 * 
	 */
	public void setProvinceDataStr(String provinceDataStr) {
		this.provinceDataStr = provinceDataStr;
		Editor editor = getSPEditor();
		editor.putString(GlobalConstant.SP_PROVINCE_DATA_STR, provinceDataStr);
		editor.commit();
	}

	/**
	 * 设置push地址
	 * 
	 */
	public void setPushServerStr(String pushServer) {
		this.pushServer = pushServer;
		Editor editor = getSPEditor();
		editor.putString(GlobalConstant.SP_PUSH_SERVER_STR, pushServer);
		editor.commit();
	}

	/**
	 * 设置push端口
	 * 
	 */
	public void setPushPostStr(int pushPost) {
		this.pushPost = pushPost;
		Editor editor = getSPEditor();
		editor.putInt(GlobalConstant.SP_PUSH_POST_STR, pushPost);
		editor.commit();
	}

	/**
	 * 设置banner数据jsonarray的字符串
	 * 
	 */

	/**
	 * 判断之前保存的路径里面是否还有图片
	 */
	public static boolean isAlbumHasPhoto(ContentResolver contentResolver,
			Context context) {
		boolean result = false;
		Cursor cursor = MediaStore.Images.Media.query(contentResolver,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				FileTypeUtil.STORE_IMAGES, MediaStore.Images.Media.BUCKET_ID
						+ " = "
						+ SystemSetting.getInstance(context).choosePhotoDirId,
				null);
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		return result;
	}
}
