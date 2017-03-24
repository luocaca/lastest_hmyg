package com.hldj.hmyg.store;

import java.io.Serializable;

public class TypeEx implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int groupPosition;
	int childPosition;
	String name;
	String firstSeedlingTypeId;

	@Override
	public String toString() {
		return "TypeEx [groupPosition=" + groupPosition + ", childPosition="
				+ childPosition + ", name=" + name + ", firstSeedlingTypeId="
				+ firstSeedlingTypeId + "]";
	}

	public TypeEx(int groupPosition, int childPosition, String name,
			String firstSeedlingTypeId) {
		super();
		this.groupPosition = groupPosition;
		this.childPosition = childPosition;
		this.name = name;
		this.firstSeedlingTypeId = firstSeedlingTypeId;
	}

	public int getGroupPosition() {
		return groupPosition;
	}

	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}

	public int getChildPosition() {
		return childPosition;
	}

	public void setChildPosition(int childPosition) {
		this.childPosition = childPosition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstSeedlingTypeId() {
		return firstSeedlingTypeId;
	}

	public void setFirstSeedlingTypeId(String firstSeedlingTypeId) {
		this.firstSeedlingTypeId = firstSeedlingTypeId;
	}
}
