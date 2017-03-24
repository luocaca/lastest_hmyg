package com.white.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.MediaColumns;

public class CommonFileUtil {

	/** 写文件时的缓存大小 */
	protected static int BUFFER_SIZE = 10 * 1024;
	/** 最小的存储空间 */
	protected final static int MIN_SPACE_SIZE = 1024 * 1024 * 1;

	// ************************ 路径和文件创建相关等**********************************

	/**
	 * 判断文件是否存在
	 * 
	 * @return
	 */
	public static boolean isFileExisted(File file) {
		return file.exists();
	}

	/**
	 * 根据路径dir创建文件目录
	 * 
	 * @param dir
	 * @return
	 */
	public static File createDir(String dir) {
		File fileDir = new File(dir);
		fileDir.mkdir();
		return fileDir;
	}

	/**
	 * 根据路径dir创建文件目录
	 * 
	 * @param dir
	 * @return
	 */
	public static String mkDirs(String dir) {
		try {
			File rootFile = FileUtil.getExternalStorageDirectory();
			if (rootFile == null)
				return "";
			File fileDir = new File(rootFile + File.separator + dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			return fileDir.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 根据路径dir创建文件目录
	 * 
	 * @param dir
	 * @return
	 */
	public static String getDirs(String dir) {
		try {
			File rootFile = FileUtil.getExternalStorageDirectory();
			if (rootFile == null)
				return "";
			return rootFile + File.separator + dir;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获得目录下的排序后的文件名列表
	 * 
	 * @param file
	 *            文件夹路径
	 * @return 排完序的文件名数组,不是目录则返回NULL
	 */
	public static String[] getSortedFileNameList(File file,
			Comparator<File> comparator) {
		if (!file.isDirectory()) {
			// 不是目录则返回NULL
			return null;
		}
		File[] filelist = file.listFiles();
		if (filelist == null) {
			return null;
		}
		List<File> files = Arrays.asList(filelist);
		Collections.sort(files, comparator);
		int size = files.size();
		String[] temp = new String[size];
		for (int i = 0; i < size; i++) {
			temp[i] = files.get(i).getName();
		}
		return temp;
	}

	/**
	 * 设置新的文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String setNewFileName(String fileName, int i) {
		int index = 0;
		if (fileName != null) {
			if ((index = fileName.lastIndexOf(".")) != -1)
				fileName = fileName.substring(0, index) + "(" + i + ")"
						+ fileName.substring(index);
			else
				fileName = fileName + "(" + i + ")";
		}
		return fileName;
	}

	/**
	 * 判断文件是否重名，返回重新命名后的文件名 filename(i)
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName, String folder) {
		if (fileName == null)
			return fileName;
		int i = 0;
		File file = new File(folder + File.separator + fileName);
		String newFileName = fileName;
		while (true) {
			if (file.exists()) {
				newFileName = setNewFileName(fileName, ++i);
				file = new File(folder + File.separator + newFileName);
			} else
				break;
		}
		return newFileName;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 是否成功删除
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除整个目录下的所有文件
	 * 
	 * @param dir
	 *            目录路径
	 * @return 是否成功删除
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 使用递归删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		// 删除当前目录
		if (dirFile.delete()) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 删除time以前的文件
	 */
	public static void deleteFileByTime(String dir, long time) {
		try {
			File rootFile = getExternalStorageDirectory();
			if (rootFile == null)
				return;
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
				return;
			}
			File[] listFile = fileDir.listFiles();
			if (listFile.length == 0) {
				return;
			}
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].lastModified() < time) {
					listFile[i].delete();// 删除time之前的文件
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, String targetFilePath,
			String targetFileName) throws IOException {
		File targetFile = new File(targetFilePath + File.separator
				+ targetFileName);
		createDir(targetFilePath);
		FileInputStream fis = new FileInputStream(sourceFile);
		DataInputStream bis = new DataInputStream(fis);
		FileOutputStream fos = new FileOutputStream(targetFile);
		DataOutputStream bos = new DataOutputStream(fos);
		try {
			byte[] buff = new byte[BUFFER_SIZE];
			int len;
			while ((len = bis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			bos.flush();
			fis.close();
			fos.close();
			bos.close();
			bis.close();
		}
	}

	/**
	 * 拷贝整个目录
	 * 
	 * @param souceDir
	 *            源目录
	 * @param targetDir
	 *            目标目录
	 * @throws IOException
	 */
	public static void copyDirectory(String souceDir, String targetDir)
			throws IOException {
		(new File(targetDir)).mkdirs();
		File[] file = (new File(souceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				File sourceFile = file[i];
				copyFile(sourceFile, targetDir, file[i].getName());
			}
			if (file[i].isDirectory()) {
				String dir1 = souceDir + File.separator + file[i].getName();
				String dir2 = targetDir + File.separator + file[i].getName();
				copyDirectory(dir1, dir2);
			}
		}
	}

	// ***************************** 存储空间相关 **********************************

	/**
	 * 判断sd卡是否存在
	 * 
	 * @return sd卡是否存在
	 */
	public static boolean isSDCardMounted() {
		boolean bool = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		return bool;
	}

	/**
	 * 根据路径获取程序内部可用的存储空间
	 * 
	 * @param path
	 * @return
	 */
	public static long getInternalStorageSizeByPath(String path) {
		StatFs stat = new StatFs(path);
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}

	/**
	 * 外部存储空间信息 至少需要1M空间
	 * 
	 * @return 当前可用的存储空间大小
	 */
	public static long getExternalStorageSize() {
		if (isSDCardMounted()) {
			// 判断外部存储空间
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			availableBlocks *= blockSize;
			if (availableBlocks >= MIN_SPACE_SIZE)
				return availableBlocks;
		}
		return 0;
	}

	/**
	 * 外部存储空间信息
	 * 
	 * @return 当前可用的存储空间大小
	 */
	public static long getExternalStorageRealSize() {
		if (isSDCardMounted()) {
			// 判断外部存储空间
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks() * blockSize;
			return availableBlocks;
		}
		return 0;
	}

	/**
	 * 获得当前设备的外部存储目录文件对象
	 * 
	 * @return 当前设备的外部存储目录文件对象
	 */
	public static File getExternalStorageDirectory() {
		if (isSDCardMounted())
			return Environment.getExternalStorageDirectory();
		else
			return null;
	}

	// ****************************** 文件流处理 ******************************

	/**
	 * 获得文件输入流
	 * 
	 * @return 输入流对象
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(File file)
			throws FileNotFoundException {
		if (isFileExisted(file))
			return new FileInputStream(file);
		return null;
	}

	/**
	 * 将字节数组写入到文件中，适用于小量数据的写入
	 * 
	 * @param bytes
	 *            字节数组
	 * @param icompress
	 *            压缩算法接口 为null则不压缩
	 * @param flag
	 *            是否追加形式写入文件
	 * @param dirPath
	 *            文件目录(目录不存在则创建目录）
	 * @param fileName
	 *            文件名
	 * @return 1:成功 0:写入过程中失败 -1:流关闭失败
	 * @throws IOException
	 */
	public static int writeSDCardByBytes(byte[] bytes, ICompress iCompress,
			boolean flag, String dirPath, String fileName) throws IOException {
		OutputStream output = null;
		try {
			createDir(dirPath);
			output = new FileOutputStream(new File(dirPath + File.separator
					+ fileName), flag);
			if (iCompress != null) {
				// 使用压缩算法写入
				output.write(iCompress.compress(bytes));
			} else
				output.write(bytes);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (output != null)
				output.close();
		}
		return 1;
	}

	/**
	 * 由input流读入数据，采用缓存机制，循环向文件中写入，适用于大量数据。
	 * 
	 * @param input
	 *            输入流
	 * @param icompress
	 *            压缩算法接口 为null则不压缩
	 * @param flag
	 *            是否追加形式写入文件的标志
	 * @param dirPath
	 *            文件目录(目录不存在则创建目录）
	 * @param fileName
	 *            文件名
	 * @return 1:成功 0:写入过程中失败 -1:流关闭失败
	 * @throws IOException
	 */
	public static int writeSDCardByInput(InputStream input,
			ICompress icompress, boolean flag, String dirPath, String fileName)
			throws IOException {
		int result = 1;
		OutputStream output = null;
		try {
			createDir(dirPath);
			output = new FileOutputStream(new File(dirPath + File.separator
					+ fileName), flag);
			byte buffer[] = new byte[BUFFER_SIZE];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				if (icompress == null)
					output.write(buffer, 0, temp);
				else
					output.write(icompress.compress(buffer), 0, temp);
			}
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
			result = 0;
			throw e;
		} finally {
			if (output != null)
				output.close();
		}
		return result;
	}

	public static void uploadFile(OutputStream os, String filePath,
			ICompress icompress) throws IOException {
		File file = new File(filePath);
		InputStream is = getInputStream(file);
		if (is == null) {
			throw new IOException();
		}

		byte buffer[] = new byte[BUFFER_SIZE];
		int temp;
		while ((temp = is.read(buffer)) != -1) {
			if (icompress == null)
				os.write(buffer, 0, temp);
			else
				os.write(icompress.compress(buffer), 0, temp);
		}
		os.flush();
		is.close();
	}

	// **************************** 文件byte[]流处理
	// **********************************

	/**
	 * 文件转byte数组，参数为文件路径
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] getFileToBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * byte数组转文件
	 * 
	 * @param bfile
	 * @param filePath
	 * @param fileName
	 */
	public static void changeByteToFile(byte[] bfile, String filePath,
			String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(filePath + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// **************************** 文件大小处理 **********************************

	/**
	 * 获得文件后缀名
	 * 
	 * @param name
	 * @return
	 */
	public static String getFileNameSuffix(String name) {
		int index = name.lastIndexOf(".");
		if (index > 0 && index < name.length() - 1) {
			String suffix = name.substring(index);
			String str = suffix.length() > 10 ? suffix.substring(0, 10)
					: suffix;
			return str.toLowerCase();
		}
		return "";
	}

	/**
	 * 获得文件大小
	 * 
	 * @return 文件大小 -1表示文件不存在
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
			fis.close();
			return s;
		} else {
			return -1;
		}
	}

	/*** 转换文件大小单位(b/kb/mb/gb) ***/
	public static String formatFileSize(long fileS) {
		if (fileS == 0) {
			return "0B";
		}
		// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 获得文件大小的显示
	 * 
	 * @param size
	 * @return
	 */
	public static String getShowFileSizeString(long size) {
		StringBuilder sb = new StringBuilder();
		long a;
		long b;
		if (size > 1 * 1024 * 1024 * 1024) {
			// 大于1G
			a = size / 1024 / 1024 / 1024;
			b = (size % (1024 * 1024 * 1024)) / (1024 * 1024 * 1024 / 1000);
			b = (((b % 10) < 5) ? (b / 10) : (b / 10 + 1));
			if (b == 100) {
				b = 99;
			}
			String bStr = ((b >= 10) ? (b + "") : ("0" + b));
			sb.append(a).append(".").append(bStr).append("GB");
		} else if (size > 1 * 1024 * 1024) {
			// 大于1M
			a = size / 1024 / 1024;
			b = (size % (1024 * 1024)) / (1024 * 1024 / 1000);
			b = (((b % 10) < 5) ? (b / 10) : (b / 10 + 1));
			if (b == 100) {
				b = 99;
			}
			String bStr = ((b >= 10) ? (b + "") : ("0" + b));
			sb.append(a).append(".").append(bStr).append("MB");
		} else if (size > 1024) {
			a = size / 1024;
			sb.append(a).append("KB");
		} else {
			sb.append(size).append("B");
		}
		return sb.toString();
	}

	/** 获取文件数 */
	public static int getFileNumByDir(String dir) {
		try {
			File rootFile = getExternalStorageDirectory();
			if (rootFile == null)
				return 0;
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
				return 0;
			}
			File[] listFile = fileDir.listFiles();
			if (listFile.length == 0) {
				return 0;
			}
			return listFile.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/** 获取文件夹大小 */
	public static long getFileSizeByDir(String dir) {
		try {
			File rootFile = getExternalStorageDirectory();
			if (rootFile == null)
				return 0;
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
				return 0;
			}
			File[] listFile = fileDir.listFiles();
			if (listFile.length == 0) {
				return 0;
			}
			long size = 0;
			for (int i = 0; i < listFile.length; i++) {
				size += listFile[i].length();
			}
			return size;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	// **************************** 调用本地文件管理器处理 *************************

	/**
	 * 选择文件管理器后，通过intent返回的信息解析文件路径
	 * 
	 * @param context
	 * @param data
	 * @return
	 */
	public static String getFilePath(Context context, Intent data) {
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(data.getData(), null, null, null, null);
		// 获取文件路径
		String path = null;
		if (null == cursor) {
			path = data.getDataString().replace("file://", "");
			try {
				path = URLDecoder.decode(path);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				path = cursor.getString(i);
				if (path == null)
					continue;
				File f = new File(path);
				if (f.exists())
					break;
			}
		}
		return path;
	}

	public static boolean getFilePathByContentResolver(Context context,
			Uri uri, String url) {
		if (null == uri) {
			return false;
		}
		Cursor c = context.getContentResolver().query(uri, null, null, null,
				null);
		String filePath = null;
		if (null == c) {
			return false;
		}
		try {
			if ((c.getCount() != 1) || !c.moveToFirst()) {
			} else {
				filePath = c.getString(c
						.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} finally {
			c.close();
		}
		if (filePath != null && filePath.length() > 0) {
			url = filePath;
			return true;
		}
		return false;
	}
}
