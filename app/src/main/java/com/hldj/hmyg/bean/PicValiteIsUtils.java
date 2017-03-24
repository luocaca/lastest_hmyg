package com.hldj.hmyg.bean;

import java.util.ArrayList;

import com.white.utils.StringUtil;

public class PicValiteIsUtils {

	public static String notiString = "";
	public static int web = 0;
	public static int ex = 0;
	public static boolean needpicValite;

	public static String notiPicValite(ArrayList<Pic> urlPaths) {

		if (needPicValite(urlPaths)) {
			notiString = "图片全部上传完成";
		} else {
			web = 0;
			ex = 0;
			for (int i = 0; i < urlPaths.size(); i++) {
				if (StringUtil.isHttpUrlPicPath(urlPaths.get(i).getUrl())) {
					web++;
				} else {
					ex++;
				}
			}
			notiString = web + "张成功，" + ex + "张已失败";
		}

		return notiString;

	}

	public static boolean needPicValite(ArrayList<Pic> urlPaths) {
		web = 0;
		ex = 0;
		for (int i = 0; i < urlPaths.size(); i++) {
			if (StringUtil.isHttpUrlPicPath(urlPaths.get(i).getUrl())) {
				web++;
			} else {
				ex++;
			}
		}
		if (web == urlPaths.size() && ex == 0) {
			needpicValite = true;
		} else {
			needpicValite = false;
		}
		return needpicValite;

	}

}