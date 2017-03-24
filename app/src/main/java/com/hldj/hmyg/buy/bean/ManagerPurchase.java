package com.hldj.hmyg.buy.bean;

import java.io.Serializable;

public class ManagerPurchase implements Serializable{

	
	String name;
	String id;
	int drawable;
	String imgUrl;
	String status;
	public ManagerPurchase(String name, String id, int drawable, String imgUrl,
			String status) {
		super();
		this.name = name;
		this.id = id;
		this.drawable = drawable;
		this.imgUrl = imgUrl;
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDrawable() {
		return drawable;
	}
	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
