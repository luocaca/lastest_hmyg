package com.zzy.flowers.activity.photoalbum;

import java.io.Serializable;

import android.graphics.Bitmap;

import com.white.utils.ImageTools;

public class PhotoAlbumItem implements Serializable, Comparable<PhotoAlbumItem> {

	private static final long serialVersionUID = -8238615423483535423L;
	/** 相册路径ID */
	private String dir_id = "";
	/** 相册封面路径 */
	private String album_path = "";
	/** 相册名称 */
	private String name = "";
	/** 相片数量 */
	private int count = 0;
	/** 封面缩略图 */
	private Bitmap thumbnailBm;
	/** 图片ID */
	private long photoId;
	public boolean isHadThumbnailBm = false;
	public boolean isLoadingThumbnailBm = false;

	public PhotoAlbumItem() {
	}

	public PhotoAlbumItem(String dir_id, String name, int count, long photoId,
			String album_path) {
		super();
		this.dir_id = dir_id;
		this.name = name;
		this.count = count;
		this.photoId = photoId;
		this.album_path = album_path;
	}

	/** 获取相册名称 */
	public String getName() {
		return name;
	}

	/** 设置相册名称 */
	public void setName(String name) {
		this.name = name;
	}

	/** 封路径面 */
	public String getAlbumPath() {
		return album_path;
	}

	/** 封路径面 */
	public void setAlbumPath(String album_path) {
		this.album_path = album_path;
	}

	/** 获取路径 */
	public String getDirId() {
		return dir_id;
	}

	/** 设置路径 */
	public void setDirId(String path) {
		this.dir_id = path;
	}

	/** 获取数量 */
	public int getCount() {
		return count;
	}

	/** 设置数量 */
	public void setCount(int count) {
		this.count = count;
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public Bitmap getThumbnailBm() {
		String key = album_path + ",rotate_"
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

	@Override
	public int compareTo(PhotoAlbumItem another) {
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
		return "PhotoAlbumItem [dir_id=" + dir_id + ", name=" + name
				+ ", count=" + count + ", thumbnailBm=" + thumbnailBm
				+ ", photoId=" + photoId + ", isHadThumbnailBm="
				+ isHadThumbnailBm + "]";
	}
}
