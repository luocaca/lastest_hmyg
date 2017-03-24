package com.hldj.hmyg.bean;

public class XiaoQuYu {
String id;
String name;
private String sortLetters;  //显示数据拼音的首字母
public XiaoQuYu(String id, String name, String sortLetters) {
	super();
	this.id = id;
	this.name = name;
	this.sortLetters = sortLetters;
}
public String getSortLetters() {
	return sortLetters;
}
public void setSortLetters(String sortLetters) {
	this.sortLetters = sortLetters;
}
@Override
public String toString() {
	return "XiaoQuYu [id=" + id + ", name=" + name + "]";
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
}
