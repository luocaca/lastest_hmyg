package com.hldj.hmyg.bean;

public class PlatformForShare {

	String name;
	String ename;
	String id;
	int pic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPic() {
		return pic;
	}
	public void setPic(int pic) {
		this.pic = pic;
	}
	public PlatformForShare(String name, String ename, String id, int pic) {
		super();
		this.name = name;
		this.ename = ename;
		this.id = id;
		this.pic = pic;
	}
}
