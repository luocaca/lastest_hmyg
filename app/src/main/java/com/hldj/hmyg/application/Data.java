package com.hldj.hmyg.application;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.listedittext.paramsData;
import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.bean.Purchase;
import com.hy.utils.GetServerUrl;
import com.photo.choosephotos.photo.Item;

/**
 * Created by Administrator on 14-1-3.
 */

public class Data {

	public static final int STATUS_GRAY = 0xFF999999;
	public static final int STATUS_ORANGE = 0xFFFF6601;
	public static final int STATUS_BLUE = 0xFF179BED;
	public static final int STATUS_GREEN = 0xFF4CAF50;
	public static final int STATUS_STROGE_GREEN = 0xFF00843C;
	public static final int STATUS_RED = 0xFFFF0000;

	public static int screenWidth = 0;
	public static ArrayList<HashMap<String, Object>> TYPES = new ArrayList<HashMap<String, Object>>();
	public static long loading_time = 200l;
	public static long refresh_time = 10l;
	public static String ZIYING = "ziying";
	public static String FUWU = "fuwu";
	public static String ZIJINDANBAO = "danbao";
	public static String HEZUOSHANGJIA = "hezuo";
	// 图标
	public static ArrayList<String> collect_ids = new ArrayList<String>();
	// 图标
	public static ArrayList<paramsData> paramsDatas = new ArrayList<paramsData>();
	// 要上传的图片
	public static ArrayList<String> pics = new ArrayList<String>();

	public static ArrayList<Purchase> purchases = new ArrayList<Purchase>();
	// 要上传的图片
	// 用来显示预览图
	public static ArrayList<Bitmap> microBmList = new ArrayList<Bitmap>();
	// 所选图的信息(主要是路径)
	public static ArrayList<Item> photoList = new ArrayList<Item>();

	public static ArrayList<Pic> pics1 = new ArrayList<Pic>();

	public static int reqWidth = 960;
	public static int reqHeight = 960;
	// 热门搜索
	public static final String GetHtml = "http://api1.cn2che.com/Banner/Banner.asmx/GetHtml";
	public static final String About01 = "http://chechengtong.cn2che.com";
	public static final String About02 = "http://jiameng.cn2che.com";
	public static final String About03 = "http://www.cn2che.com/pinggushi.html";
	public static final String About04 = "http://chechengtong.cn2che.com";
	public static final String Content_Type = "application/x-www-form-urlencoded;";
	public static final String proxyAssureDesc = GetServerUrl.getUrl()
			+ "h5/page/labelicondesc.html";
	public static final String Store_Page = GetServerUrl.getUrl()
			+ "h5/page/store/";
	public static final String Store_Page3 = GetServerUrl.getUrl()
			+ "h5/page/store/";
	public static final String About = GetServerUrl.getUrl()
			+ "h5/page/aboutus.html";
	public static final String share = GetServerUrl.getUrl()
			+ "h5/page/share.html";
	public static final String helpIndex = GetServerUrl.getUrl()
			+ "h5/page/help.html";
	public static final String weituogou = GetServerUrl.getUrl()
			+ "h5/page/weituogou.html";
	

}
