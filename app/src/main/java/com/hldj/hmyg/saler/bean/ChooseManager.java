package com.hldj.hmyg.saler.bean;


public class ChooseManager {
	String id;
	String name;
	String url;
	int draw;
	boolean redPoint;
	public ChooseManager(String id, String name, String url, int draw,
			boolean redPoint) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.draw = draw;
		this.redPoint = redPoint;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public boolean isRedPoint() {
		return redPoint;
	}
	public void setRedPoint(boolean redPoint) {
		this.redPoint = redPoint;
	}
}
