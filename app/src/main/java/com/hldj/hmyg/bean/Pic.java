package com.hldj.hmyg.bean;

import java.io.Serializable;

public class Pic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	boolean isCover;
	String url;
	int sort;

	@Override
	public String toString() {
		return "Pic [id=" + id + ", isCover=" + isCover + ", url=" + url
				+ ", sort=" + sort + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Pic(String id, boolean isCover, String url, int sort) {
		super();
		this.id = id;
		this.isCover = isCover;
		this.url = url;
		this.sort = sort;
	}
}
