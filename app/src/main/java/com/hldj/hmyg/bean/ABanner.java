package com.hldj.hmyg.bean;


public class ABanner {
	
	boolean isNewRecord;
	String delFlag;
	String url;
	String ossThumbnailImagePath;
	String ossMediumImagePath;
	String ossLargeImagePath;
	public boolean isNewRecord() {
		return isNewRecord;
	}
	public void setNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOssThumbnailImagePath() {
		return ossThumbnailImagePath;
	}
	public void setOssThumbnailImagePath(String ossThumbnailImagePath) {
		this.ossThumbnailImagePath = ossThumbnailImagePath;
	}
	public String getOssMediumImagePath() {
		return ossMediumImagePath;
	}
	public void setOssMediumImagePath(String ossMediumImagePath) {
		this.ossMediumImagePath = ossMediumImagePath;
	}
	public String getOssLargeImagePath() {
		return ossLargeImagePath;
	}
	public void setOssLargeImagePath(String ossLargeImagePath) {
		this.ossLargeImagePath = ossLargeImagePath;
	}

}
