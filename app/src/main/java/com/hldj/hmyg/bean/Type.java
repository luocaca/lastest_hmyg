package com.hldj.hmyg.bean;

import java.io.Serializable;

public class Type implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String name;
	String urlString;
	int img;
	public Type(String id, String name, String urlString, int img) {
		super();
		this.id = id;
		this.name = name;
		this.urlString = urlString;
		this.img = img;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlString() {
		return urlString;
	}
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
}
