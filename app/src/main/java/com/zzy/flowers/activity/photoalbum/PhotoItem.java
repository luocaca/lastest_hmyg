package com.zzy.flowers.activity.photoalbum;

import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.hldj.hmyg.R;
import com.hldj.hmyg.saler.GlobalConstant;
import com.white.utils.GifImgHelperUtil;
import com.white.utils.ImageTools;

public class PhotoItem implements Serializable, Comparable<PhotoItem> {

	private static final long serialVersionUID = 7220611792457079398L;

	/** 自增ID */
	public long id;
	/** 附件大小 */
	public long picSize;
	/** 附件路径 */
	public String picPath = "";
	/** 附件名 */
	public String name = "";
	/** 图片ID */
	private long photoId = 0;
	private boolean isSelect = false;
	public boolean isHadThumbnailBm = false;
	/** 是否正在缓存预览图 */
	public boolean isLoadingThumbnailBm = false;

	/**
	 * 用于相册选择图片
	 * 
	 * @param photoId
	 *            本地图片ID
	 * @param path
	 *            图片路径
	 * @param thumbnailBm
	 *            缩略图
	 */
	public PhotoItem(long photoId, String path) {
		super();
		this.photoId = photoId;
		this.picPath = path;
		File file = new File(picPath);
		this.picSize = file.length();
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public Bitmap getThumbnailBm() {
		String key = picPath + ",rotate_"
				+ ImageTools.CIRCLE_IMAGE_BG_HALFWIDTH + "_"
				+ ImageTools.CIRCLE_IMAGE_BG_HALFWIDTH;
		// 先查找缓存，缓存中存在，则利用缓存，否则，使用临时图片
		Bitmap thumbnailBm = ImageTools.getFromCache(key);
		if (thumbnailBm == null) {
			key = "photo_thumbnail_" + photoId;
			return ImageTools.getFromCache(key);
		} else {
			return thumbnailBm;
		}
	}

	public boolean getIsHadThumbnail() {
		return isHadThumbnailBm;
	}

	public void setIsHadThumbnail(boolean isHadThumbnailBm) {
		this.isHadThumbnailBm = isHadThumbnailBm;
	}

	/**
	 * 获取原图
	 */
	public Bitmap getSourceBitmap(Context context) {
		Bitmap bm = null;
		// 获取原图
		if (picPath != null && picPath.length() > 0) {
			File file = new File(picPath);
			if (file.exists() && file.length() == picSize) {
				bm = getCompressBitmap(file);
				if (bm == null) { // 原图存在但是加载失败
					Bitmap loadFailPic = ImageTools
							.getFromCache(GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE);
					if (loadFailPic == null) {
						loadFailPic = BitmapFactory.decodeResource(
								context.getResources(),
								R.drawable.un_down_load_pic_icon);
						ImageTools.addBitmap(
								GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE,
								loadFailPic);
					}
					bm = loadFailPic;
					Toast.makeText(context, R.string.image_load_failed,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		if (bm == null) { // 无原图，则先显示小图
			Bitmap unDownPic = ImageTools
					.getFromCache(GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE);
			if (unDownPic == null) {
				unDownPic = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.un_down_load_pic_icon);
				ImageTools.addBitmap(GlobalConstant.IMAGE_PREVIEW_LOAD_FAILURE,
						unDownPic);
			}
			bm = unDownPic;
			return bm;
		}
		return bm;
	}

	public boolean isGifPic() {
		boolean isGif = false;
		if (picPath != null && picPath.length() > 0) {
			File file = new File(picPath);
			// 存在原图
			if (file.exists() && file.length() == picSize) {
				try {
					isGif = GifImgHelperUtil.isGif(picPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return isGif;
	}

	public Bitmap getCompressBitmap(File file) {
		Bitmap result = null;
		try {
			result = ImageTools.CompressAndSaveImg(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getPath() {
		return picPath;
	}

	public void setPath(String path) {
		this.picPath = path;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	@Override
	public int compareTo(PhotoItem another) {
		if (getPhotoId() > another.getPhotoId()) {
			return -1;
		} else if (getPhotoId() < another.getPhotoId()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "PhotoItem [id=" + id + ", picSize=" + picSize + ", picPath="
				+ picPath + ", name=" + name + ", photoId=" + photoId
				+ ", isSelect=" + isSelect + ", isHadThumbnailBm="
				+ isHadThumbnailBm + ", isLoadingThumbnailBm="
				+ isLoadingThumbnailBm + "]";
	}
}
