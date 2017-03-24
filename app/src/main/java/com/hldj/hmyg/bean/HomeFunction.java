package com.hldj.hmyg.bean;


public class HomeFunction {
	int postion;
	String id;
	String name;
	String img;
	int drawable;
	public HomeFunction(int postion, String id, String name, String img,
			int drawable) {
		super();
		this.postion = postion;
		this.id = id;
		this.name = name;
		this.img = img;
		this.drawable = drawable;
	}
	public int getPostion() {
		return postion;
	}
	public void setPostion(int postion) {
		this.postion = postion;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getDrawable() {
		return drawable;
	}
	public void setDrawable(int drawable) {
		this.drawable = drawable;
	}

}
