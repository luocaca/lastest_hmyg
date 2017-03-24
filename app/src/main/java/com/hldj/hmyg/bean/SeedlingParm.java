package com.hldj.hmyg.bean;

public class SeedlingParm {

	String name;
	String Value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public SeedlingParm(String name, String value) {
		super();
		this.name = name;
		Value = value;
	}

}
