package com.zzy.flowers.activity.photoalbum;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.white.utils.ImageTools;
import com.zzy.flowers.iupdate.image.IThumbnailUpdate;

/**
 * 相册缓存图片通知
 * 
 * @author Administrator
 * 
 */
public class ThumbnailAddManage {

	private static ThumbnailAddManage instance;

	private IThumbnailUpdate iThumbnailCallback;

	private ThumbnailAddManage() {
		super();
	}

	public static ThumbnailAddManage getThumbnailInstance() {
		if (instance == null) {
			instance = new ThumbnailAddManage();
		}
		return instance;
	}

	public void addNewThumbnailToCache(final Context context,
			final String path, final long photoId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File file = new File(path);
				String key = "photo_thumbnail_" + photoId;
				Bitmap cacheBm = ImageTools.converThumbnailBitmap(file, key);
				if (cacheBm != null
						&& ThumbnailAddManage.getThumbnailInstance().iThumbnailCallback != null) {
					ThumbnailAddManage.getThumbnailInstance().iThumbnailCallback
							.updateThumbnailView(photoId);
				}
			}
		}).start();
	}

	public IThumbnailUpdate getiThumbnailCallback() {
		return iThumbnailCallback;
	}

	public void setiThumbnailCallback(IThumbnailUpdate iThumbnailCallback) {
		this.iThumbnailCallback = iThumbnailCallback;
	}
}
