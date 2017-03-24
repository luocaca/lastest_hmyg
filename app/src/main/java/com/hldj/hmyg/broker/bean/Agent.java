package com.hldj.hmyg.broker.bean;

public class Agent {
	
	String id;
	String createDate;
	String userName;
	String realName;
	String publicName;
	String publicPhone;
	String companyName;
	public Agent(String id, String createDate, String userName,
			String realName, String publicName, String publicPhone,
			String companyName) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.userName = userName;
		this.realName = realName;
		this.publicName = publicName;
		this.publicPhone = publicPhone;
		this.companyName = companyName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPublicName() {
		return publicName;
	}
	public void setPublicName(String publicName) {
		this.publicName = publicName;
	}
	public String getPublicPhone() {
		return publicPhone;
	}
	public void setPublicPhone(String publicPhone) {
		this.publicPhone = publicPhone;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
