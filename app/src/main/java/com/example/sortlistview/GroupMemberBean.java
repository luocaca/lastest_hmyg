package com.example.sortlistview;

public class GroupMemberBean {

	private String name;   //显示的数据
	private String id;   //  
	private String Pm;   //  
	private String logo;   //  
	private String sortLetters;  //显示数据拼音的首字母
	public GroupMemberBean(String name, String id, String pm, String logo,
			String sortLetters) {
		super();
		this.name = name;
		this.id = id;
		this.Pm = pm;
		this.logo = logo;
		this.sortLetters = sortLetters;
	}
	public String getPm() {
		return Pm;
	}
	public void setPm(String pm) {
		Pm = pm;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
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
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
