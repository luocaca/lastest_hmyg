package com.hldj.hmyg.saler.bean;

import java.io.Serializable;

public class ParamsList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String value ="";
	boolean required=false;
	@Override
	public String toString() {
		return "ParamsList [value=" + value + ", required=" + required + "]";
	}
	public ParamsList(String value, boolean required) {
		super();
		this.value = value;
		this.required = required;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}

}
