package com.hldj.hmyg.buyer;

public class SortList {
	String ID;
	String Name;
	@Override
	public String toString() {
		return "SortList [ID=" + ID + ", Name=" + Name + "]";
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public SortList(String iD, String name) {
		super();
		ID = iD;
		Name = name;
	}
}
