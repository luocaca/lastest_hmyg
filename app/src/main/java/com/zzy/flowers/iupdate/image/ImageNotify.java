package com.zzy.flowers.iupdate.image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.zzy.flowers.data.entity.image.UploadImageData;

public class ImageNotify {

	public interface IImageNotify {

		public void notifyUploadImageSuccess(List<UploadImageData> dataList,
				int type);

		public void notifyDownloadImageSuccess(int type, int pos,
				String filename, boolean isSmall);

		public void notifyNormalUploadImageSuccess(ArrayList<String> dataList,
				int type);
	}

	/** 软引用队列 */
	private List<WeakReference<IImageNotify>> iImageNotifys = new ArrayList<WeakReference<IImageNotify>>();

	private static ImageNotify imageNotify;

	public static ImageNotify getInstance() {
		if (imageNotify == null)
			imageNotify = new ImageNotify();
		return imageNotify;
	}

	/**
	 * 添加接口监听
	 * 
	 * @param iJoinGroup
	 */
	public void addIImageUpLoadNotify(IImageNotify imageNotify) {
		iImageNotifys.add(new WeakReference<IImageNotify>(imageNotify));
	}

	public void removeIImageUpLoadNotify(IImageNotify imageNotify) {
		iImageNotifys.remove(new WeakReference<IImageNotify>(imageNotify));
	}

	public void notifyUploadImageSuccess(List<UploadImageData> dataList,
			int type) {
		for (WeakReference<IImageNotify> i : iImageNotifys) {
			IImageNotify iq = i.get();
			if (iq != null)
				iq.notifyUploadImageSuccess(dataList, type);
		}
	}

	public void notifyDownloadImageSuccess(int type, int pos, String filename,
			boolean isSmall) {
		for (WeakReference<IImageNotify> i : iImageNotifys) {
			IImageNotify iq = i.get();
			if (iq != null)
				iq.notifyDownloadImageSuccess(type, pos, filename, isSmall);
		}
	}

	public void notifyNormalUploadImageSuccess(ArrayList<String> dataList,
			int type) {
		for (WeakReference<IImageNotify> i : iImageNotifys) {
			IImageNotify iq = i.get();
			if (iq != null)
				iq.notifyNormalUploadImageSuccess(dataList, type);
		}
	}
}
