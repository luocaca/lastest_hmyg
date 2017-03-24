package com.hldj.hmyg.store;

public class GroupItem {
	private String title;
	private int imageId;
	private String id;
	private String name;
	private String aliasName;
	private String parentId;
	private int level;
	private String firstPinyin;
	private String fullPinyin;
	private String mainSpec;
	
	public GroupItem(String title, int imageId, String id, String name,
			String aliasName, String parentId, int level, String firstPinyin,
			String fullPinyin, String mainSpec) {
		super();
		this.title = title;
		this.imageId = imageId;
		this.id = id;
		this.name = name;
		this.aliasName = aliasName;
		this.parentId = parentId;
		this.level = level;
		this.firstPinyin = firstPinyin;
		this.fullPinyin = fullPinyin;
		this.mainSpec = mainSpec;
	}

	public GroupItem(String title, int imageId)
	{
		this.title = title;
		this.imageId = imageId;
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
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	

}
