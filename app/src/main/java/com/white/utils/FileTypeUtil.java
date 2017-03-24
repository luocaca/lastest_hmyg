package com.white.utils;

import java.io.File;

import android.provider.MediaStore;

public class FileTypeUtil {

	private static FileTypeUtil instance;

	public static final int OPEN_TYPE_NOMARL_FILE = 0;

	public static final int OPEN_TYPE_AFFICHE_FILE = 1;

	public static FileTypeUtil getInstance() {
		if (instance == null) {
			instance = new FileTypeUtil();
		}
		return instance;
	}

	/**
	 * 设置获取图片的字段 名称 ID 目录 目录名 图片路径
	 */
	public static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // 显示的名称
			MediaStore.Images.Media._ID, // ID
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录ID
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
			MediaStore.Images.Media.DATA, // 目录的路径
			MediaStore.Images.Media.SIZE // 文件的大小
	};

	public static String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
			{ ".3gp", "video/3gpp" },
			{ ".aab", "application/x-authoware-bin" },
			{ ".aam", "application/x-authoware-map" },
			{ ".aas", "application/x-authoware-seg" },
			{ ".ai", "application/postscript" },
			{ ".aif", "audio/x-aiff" },
			{ ".aifc", "audio/x-aiff" },
			{ ".aiff", "audio/x-aiff" },
			{ ".als", "audio/X-Alpha5" },
			{ ".amc", "application/x-mpeg" },
			{ ".ani", "application/octet-stream" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".arj", "application/arj" },
			{ ".asc", "text/plain" },
			{ ".asd", "application/astound" },
			{ ".amr", "audio/amr" },
			{ ".asf", "video/x-ms-asf" },
			{ ".asn", "application/astound" },
			{ ".asp", "application/x-asap" },
			{ ".asx", "video/x-ms-asf" },
			{ ".au", "audio/basic" },
			{ ".avb", "application/octet-stream" },
			{ ".avi", "video/x-msvideo" },
			{ ".awb", "audio/amr-wb" },
			{ ".bcpio", "application/x-bcpio" },
			{ ".bin", "application/octet-stream" },
			{ ".bld", "application/bld" },
			{ ".bld2", "application/bld2" },
			{ ".bmp", "image/bmp" },
			{ ".bpk", "application/octet-stream" },
			{ ".bz2", "application/x-bzip2" },
			{ ".cal", "image/x-cals" },
			{ ".ccn", "application/x-cnc" },
			{ ".cco", "application/x-cocoa" },
			{ ".cab", "application/vnd.cab-com-archive" },
			{ ".cdf", "application/x-netcdf" },
			{ ".cgi", "magnus-internal/cgi" },
			{ ".chat", "application/x-chat" },
			{ ".cdf", "application/x-netcdf" },
			{ ".cgi", "magnus-internal/cgi" },
			{ ".chat", "application/x-chat" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".clp", "application/x-msclip" },
			{ ".cmx", "application/x-cmx" },
			{ ".co", "application/x-cult3d-object" },
			{ ".cod", "image/cis-cod" },
			{ ".cpio", "application/x-cpio" },
			{ ".cpt", "application/mac-compactpro" },
			{ ".crd", "application/x-mscardfile" },
			{ ".csh", "application/x-csh" },
			{ ".csm", "chemical/x-csml" },
			{ ".csml", "chemical/x-csml" },
			{ ".css", "text/css" },
			{ ".cur", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".dcm", "x-lml/x-evm" },
			{ ".dcr", "application/x-director" },
			{ ".dcx", "image/x-dcx" },
			{ ".dhtml", "text/html" },
			{ ".dir", "application/x-director" },
			{ ".dll", "application/octet-stream" },
			{ ".dmg", "application/octet-stream" },
			{ ".dms", "application/octet-stream" },
			{ ".dot", "application/x-dot" },
			{ ".dotx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.template" },
			{ ".dps", "application/ksdps" },
			{ ".doc", "application/msword" },
			{ ".docx", "application/msword" },
			{ ".dvi", "application/x-dvi" },
			{ ".dwf", "drawing/x-dwf" },
			{ ".dwg", "application/x-autocad" },
			{ ".dxf", "application/x-autocad" },
			{ ".dxr", "application/x-director" },
			{ ".ebk", "application/x-expandedbook" },
			{ ".emb", "chemical/x-embl-dl-nucleotide" },
			{ ".embl", "application/x-director" },
			{ ".eps", "application/postscript" },
			{ ".eri", "image/x-eri" },
			{ ".es", "audio/echospeech" },
			{ ".esl", "audio/echospeech" },
			{ ".etc", "application/x-earthtime" },
			{ ".etx", "text/x-setext" },
			{ ".evm", "x-lml/x-evm" },
			{ ".evy", "application/x-envoy" },
			{ ".etx", "text/x-setext" },
			{ ".et", "application/kset" },
			{ ".flv", "video/x-flv" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".ipa", "application/iphone-package-archive" },
			{ ".html", "text/html" },
			{ ".ico", "image/x-icon" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpe", "image/jpeg" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".jpz", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".mdb", "application/x-msaccess" },
			{ ".m4v", "video/x-m4v" },
			{ ".mid", "audio/midi" },
			{ ".midi", "audio/midi" },
			{ ".mov", "video/quicktime" },
			{ ".movie", "video/x-sgi-movie" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".pgm", "image/x-portable-graymap" },
			{ ".png", "image/png" },
			{ ".pnz", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" },
			{ ".qt", "video/quicktime" },
			{ ".ra", "audio/x-pn-realaudio" },
			{ ".ram", "audio/x-pn-realaudio" },
			{ ".rar", "application/x-rar-compressed" },
			{ ".rm", "video/vnd.rn-realvideo" },
			{ ".rc", "text/plain" },
			{ ".rmm", "audio/x-pn-realaudio" },
			{ ".rmvb", "video/vnd.rn-realvideo" },
			{ ".rtf", "application/rtf" },
			{ ".sh", "text/plain" },
			{ ".swf", "application/x-shockwave-flash" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" },
			{ ".txt", "text/plain" },
			{ ".tif", "image/tiff" },
			{ ".tiff", "image/tiff" },
			{ ".wav", "audio/x-wav" },
			{ ".wm", "video/x-ms-wm" },
			{ ".wma", "audio/x-ms-wma" },
			{ ".wmv", "video/x-ms-wmv" },
			{ ".wmf", "application/x-msmetafile" },
			{ ".wps", "application/kswps" },
			{ ".xml", "text/plain" },
			{ ".xlm", "application/vnd.ms-excel" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".xla", "application/vnd.ms-excel" },
			{ ".xlc", "application/vnd.ms-excel" },
			{ ".xll", "application/x-excel" },
			{ ".z", "application/x-compress" }, { ".zip", "application/zip" },
			{ "", "*/*" } };

	public static final String DOC_DIR = "doc";
	public static final String IMAGE_DIR = "image";
	public static final String MUSIC_DIR = "music";
	public static final String VIDEO_DIR = "video";
	public static final String OTHER_DIR = "other";

	public static final String[] PIC_SUFFIX = { ".jpg", ".gif", ".png",
			".jpeg", ".bmp", ".ico", ".tif", ".tiff" };
	public static final String[] STATIC_PIC_SUFFIX = { ".jpg", ".png", ".jpeg",
			".bmp", ".ico", ".tif", ".tiff" };
	public static final String[] MUSIC_SUFFIX = { ".wav", ".mp3", ".wma",
			".ra", ".ram", ".midi", ".mid", ".aif", ".aiff", ".au", ".amr" };
	public static final String[] VIDEO_SUFFIX = { ".mp4", ".mov", ".qt",
			".avi", ".mpeg", ".mpg", ".wmv", ".rmvb", ".movie", ".rm", ".asf",
			".flv", ".swf", ".3gp" };
	public static final String[] DOC_SUFFIX = { ".doc", ".docx", ".txt",
			".xls", ".xlsx", ".ppt", ".pptx", ".pps", ".pdf", ".wps", ".et",
			".dps", ".xml" };

	public static final int TYPE_PIC = 0;
	public static final int TYPE_MUSIC = 1;
	public static final int TYPE_VIDIO = 2;
	public static final int TYPE_DOC = 3;
	public static final int TYPE_OTHER = 4;

	/**
	 * 通过文件名获得离线文件下载的路径
	 * 
	 * @param name
	 * @return
	 */
	public static String getDirByFileName(String name) {
		String dir;
		String lowerStr = name.toLowerCase();
		if (StringUtil.isEndWithStringArray(lowerStr, PIC_SUFFIX)) {
			// 图片类型
			dir = IMAGE_DIR;
		} else if (StringUtil.isEndWithStringArray(lowerStr, MUSIC_SUFFIX)) {
			// 语音类型
			dir = MUSIC_DIR;
		} else if (StringUtil.isEndWithStringArray(lowerStr, VIDEO_SUFFIX)) {
			// 视频类型
			dir = VIDEO_DIR;
		} else if (StringUtil.isEndWithStringArray(lowerStr, DOC_SUFFIX)) {
			// 文档类型
			dir = DOC_DIR;
		} else {
			// 其它类型
			dir = OTHER_DIR;
		}
		return dir;
	}

	/**
	 * 通过文件名获得离线文件下载的路径
	 * 
	 * @param name
	 * @return
	 */
	public static int getFileTypeByFileName(String name) {
		int type;
		if (StringUtil.isEndWithStringArray(name, PIC_SUFFIX)) {
			// 图片类型
			type = TYPE_PIC;
		} else if (StringUtil.isEndWithStringArray(name, MUSIC_SUFFIX)) {
			// 语音类型
			type = TYPE_MUSIC;
		} else if (StringUtil.isEndWithStringArray(name, VIDEO_SUFFIX)) {
			// 视频类型
			type = TYPE_VIDIO;
		} else if (StringUtil.isEndWithStringArray(name, DOC_SUFFIX)) {
			// 文档类型
			type = TYPE_DOC;
		} else {
			// 其它类型
			type = TYPE_OTHER;
		}
		return type;
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 *            文件对象
	 * @result MIME类型
	 */
	public static String getMIMEType(File file) {
		String fName = file.getName();
		return getMIMEType(fName);
	}

	/**
	 * 获得指定文件的MIME类型
	 * 
	 * @param filename
	 *            文件名
	 * @return MIME类型
	 */
	public static String getMIMEType(String filename) {
		String type = "*/*";
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = filename.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = filename.substring(dotIndex, filename.length())
				.toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
}
