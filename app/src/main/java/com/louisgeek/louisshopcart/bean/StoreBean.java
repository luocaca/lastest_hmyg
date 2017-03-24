package com.louisgeek.louisshopcart.bean;

import java.io.Serializable;


/**
 * Created by louisgeek on 2016/4/27.
 */
public class StoreBean implements Serializable{
	/** 店铺ID */
	private String id;
	/** 店铺名称 */
	private String name;

	private boolean isChecked;

	private boolean isEditing;
	private int text_color;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getText_color() {
		return text_color;
	}

	public void setText_color(int text_color) {
		this.text_color = text_color;
	}

	public StoreBean(String id, String name, boolean isChecked,
			boolean isEditing,int text_color, String type) {
		this.id = id;
		this.name = name;
		this.isChecked = isChecked;
		this.isEditing = isEditing;
		this.text_color = text_color;
		this.type = type;
	}

	public StoreBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setIsEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
