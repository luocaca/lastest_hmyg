package com.hldj.hmyg.bean;

public class DaQuYu {
	String id;
	String name;
	@Override
	public String toString() {
		return "DaQuYu [id=" + id + ", name=" + name + "]";
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
	public DaQuYu(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	

}
