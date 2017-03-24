package com.zzy.flowers.data.entity.image;

public class ImageFileData {
	/** 原图URL */
	private String url = "";
	/** 缩略图URL */
	private String thumbnailUrl = "";

	/**
	 * @param url
	 * @param thumbnailUrl
	 */
	public ImageFileData(String url, String thumbnailUrl) {
		super();
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getUrl() {
		return url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	@Override
	public String toString() {
		return "ImageFileData [url=" + url + ", thumbnailUrl=" + thumbnailUrl
				+ "]";
	}
}
