package com.hldj.hmyg.saler.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Subscribe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 订阅
	String id = "";
	String seedlingTypeId = "";
	private String sortLetters= "";  //显示数据拼音的首字母

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public String toString() {
		return "Subscribe [id=" + id + ", seedlingTypeId=" + seedlingTypeId
				+ ", createBy=" + createBy + ", createDate=" + createDate
				+ ", name=" + name + ", aliasName=" + aliasName + ", parentId="
				+ parentId + ", level=" + level + ", firstPinyin="
				+ firstPinyin + ", fullPinyin=" + fullPinyin
				+ ", seedlingParams=" + seedlingParams + ", sort=" + sort
				+ ", isTop=" + isTop + ", parentName=" + parentName
				+ ", parentSeedlingParams=" + parentSeedlingParams
				+ ", subscribeId=" + subscribeId
				+ ", countPurchaseBysubscribeJson="
				+ countPurchaseBysubscribeJson + ", isEdit=" + isEdit
				+ ", paramsLists=" + paramsLists + ", str_plantTypeLists="
				+ str_plantTypeLists + ", str_plantTypeList_ids_s="
				+ str_plantTypeList_ids_s + ", str_qualityTypeLists="
				+ str_qualityTypeLists + ", str_qualityTypeList_ids="
				+ str_qualityTypeList_ids + ", str_qualityGradeLists="
				+ str_qualityGradeLists + ", str_qualityGradeList_ids="
				+ str_qualityGradeList_ids + "]";
	}

	public String getSeedlingTypeId() {
		return seedlingTypeId;
	}

	public void setSeedlingTypeId(String seedlingTypeId) {
		this.seedlingTypeId = seedlingTypeId;
	}

	String createBy = "";
	String createDate = "";
	String name = "";
	String aliasName = "";
	String parentId = "";
	int level;
	String firstPinyin = "";
	String fullPinyin = "";
	String seedlingParams = "";
	int sort = 0;
	String isTop = "";
	String parentName = "";
	String parentSeedlingParams = "";
	String subscribeId = "";
	int countPurchaseBysubscribeJson = 0;
	boolean isEdit = false;
	ArrayList<ParamsList> paramsLists = new ArrayList<ParamsList>();
	ArrayList<String> str_plantTypeLists = new ArrayList<String>();
	ArrayList<String> str_plantTypeList_ids_s = new ArrayList<String>();
	ArrayList<String> str_qualityTypeLists = new ArrayList<String>();
	ArrayList<String> str_qualityTypeList_ids = new ArrayList<String>();
	ArrayList<String> str_qualityGradeLists = new ArrayList<String>();
	ArrayList<String> str_qualityGradeList_ids = new ArrayList<String>();

	public ArrayList<String> getStr_plantTypeLists() {
		return str_plantTypeLists;
	}

	public void setStr_plantTypeLists(ArrayList<String> str_plantTypeLists) {
		this.str_plantTypeLists = str_plantTypeLists;
	}

	public ArrayList<String> getStr_plantTypeList_ids_s() {
		return str_plantTypeList_ids_s;
	}

	public void setStr_plantTypeList_ids_s(
			ArrayList<String> str_plantTypeList_ids_s) {
		this.str_plantTypeList_ids_s = str_plantTypeList_ids_s;
	}

	public ArrayList<String> getStr_qualityTypeLists() {
		return str_qualityTypeLists;
	}

	public void setStr_qualityTypeLists(ArrayList<String> str_qualityTypeLists) {
		this.str_qualityTypeLists = str_qualityTypeLists;
	}

	public ArrayList<String> getStr_qualityTypeList_ids() {
		return str_qualityTypeList_ids;
	}

	public void setStr_qualityTypeList_ids(
			ArrayList<String> str_qualityTypeList_ids) {
		this.str_qualityTypeList_ids = str_qualityTypeList_ids;
	}

	public ArrayList<String> getStr_qualityGradeLists() {
		return str_qualityGradeLists;
	}

	public void setStr_qualityGradeLists(ArrayList<String> str_qualityGradeLists) {
		this.str_qualityGradeLists = str_qualityGradeLists;
	}

	public ArrayList<String> getStr_qualityGradeList_ids() {
		return str_qualityGradeList_ids;
	}

	public void setStr_qualityGradeList_ids(
			ArrayList<String> str_qualityGradeList_ids) {
		this.str_qualityGradeList_ids = str_qualityGradeList_ids;
	}

	public ArrayList<ParamsList> getParamsLists() {
		return paramsLists;
	}

	public void setParamsLists(ArrayList<ParamsList> paramsLists) {
		this.paramsLists = paramsLists;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

	public String getSeedlingParams() {
		return seedlingParams;
	}

	public void setSeedlingParams(String seedlingParams) {
		this.seedlingParams = seedlingParams;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentSeedlingParams() {
		return parentSeedlingParams;
	}

	public void setParentSeedlingParams(String parentSeedlingParams) {
		this.parentSeedlingParams = parentSeedlingParams;
	}

	public String getSubscribeId() {
		return subscribeId;
	}

	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}

	public int getCountPurchaseBysubscribeJson() {
		return countPurchaseBysubscribeJson;
	}

	public void setCountPurchaseBysubscribeJson(int countPurchaseBysubscribeJson) {
		this.countPurchaseBysubscribeJson = countPurchaseBysubscribeJson;
	}

}
