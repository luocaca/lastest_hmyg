package com.hldj.hmyg.store;

public class ChildItem {
	private String title;//子项显示的文字
	private int markerImgId;//每个子项的图标
	private String id;
	private String name;
	private String aliasName;
	private String parentId;
	private int level;
	private String firstPinyin;
	private String fullPinyin;
	private String mainSpec;
	
	
	public ChildItem(String title, int markerImgId, String id, String name,
			String aliasName, String parentId, int level, String firstPinyin,
			String fullPinyin, String mainSpec) {
		super();
		this.title = title;
		this.markerImgId = markerImgId;
		this.id = id;
		this.name = name;
		this.aliasName = aliasName;
		this.parentId = parentId;
		this.level = level;
		this.firstPinyin = firstPinyin;
		this.fullPinyin = fullPinyin;
		this.mainSpec = mainSpec;
	}
	@Override
	public String toString() {
		return "ChildItem [title=" + title + ", markerImgId=" + markerImgId
				+ ", id=" + id + ", name=" + name + ", aliasName=" + aliasName
				+ ", parentId=" + parentId + ", level=" + level
				+ ", firstPinyin=" + firstPinyin + ", fullPinyin=" + fullPinyin
				+ ", mainSpec=" + mainSpec + "]";
	}
	public ChildItem(String title, int markerImgId)
	{
		this.title = title;
		this.markerImgId = markerImgId;
		
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

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFirstPinyin() {
		return firstPinyin;
	}

	public void setFirstPinyin(String firstPinyin) {
		this.firstPinyin = firstPinyin;
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	public String getMainSpec() {
		return mainSpec;
	}

	public void setMainSpec(String mainSpec) {
		this.mainSpec = mainSpec;
	}


	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMarkerImgId() {
		return markerImgId;
	}
	public void setMarkerImgId(int markerImgId) {
		this.markerImgId = markerImgId;
	}
	

}
