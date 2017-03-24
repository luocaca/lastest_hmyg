package com.zzy.common.widget.galleryView;

import java.io.Serializable;

public class CertifyPhotoItem implements Serializable {
	
	private static final long serialVersionUID = -6354349347238580092L;
	/** ��ͼ·�� */
	public String bigPicPath = "";
	/** ����·�� */
	public String smallPicPath = "";
	/** ������ */
	public String name = "";
	public boolean isHadThumbnailBm = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHadThumbnailBm() {
		return isHadThumbnailBm;
	}

	public void setHadThumbnailBm(boolean isHadThumbnailBm) {
		this.isHadThumbnailBm = isHadThumbnailBm;
	}

	public String getBigPicPath() {
		return bigPicPath;
	}

	public void setBigPicPath(String bigPicPath) {
		this.bigPicPath = bigPicPath;
	}

	public String getSmallPicPath() {
		return smallPicPath;
	}

	public void setSmallPicPath(String smallPicPath) {
		this.smallPicPath = smallPicPath;
	}

}
