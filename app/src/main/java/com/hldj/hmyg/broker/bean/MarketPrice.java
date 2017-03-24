package com.hldj.hmyg.broker.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.hldj.hmyg.bean.Pic;
import com.hldj.hmyg.saler.bean.ParamsList;

public class MarketPrice implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String 	firstSeedlingTypeId="";
	String 	secondSeedlingTypeId="";
	String 	firstTypeName="";
	String 	qualityGrade="";
	String 	qualityGradeName="";
	public String getFirstTypeName() {
		return firstTypeName;
	}
	public void setFirstTypeName(String firstTypeName) {
		this.firstTypeName = firstTypeName;
	}
	public String getQualityGrade() {
		return qualityGrade;
	}
	public void setQualityGrade(String qualityGrade) {
		this.qualityGrade = qualityGrade;
	}
	public String getQualityGradeName() {
		return qualityGradeName;
	}
	public void setQualityGradeName(String qualityGradeName) {
		this.qualityGradeName = qualityGradeName;
	}
	public String getFirstSeedlingTypeId() {
		return firstSeedlingTypeId;
	}
	public void setFirstSeedlingTypeId(String firstSeedlingTypeId) {
		this.firstSeedlingTypeId = firstSeedlingTypeId;
	}
	public String getSecondSeedlingTypeId() {
		return secondSeedlingTypeId;
	}
	public void setSecondSeedlingTypeId(String secondSeedlingTypeId) {
		this.secondSeedlingTypeId = secondSeedlingTypeId;
	}
	String id="";
	String name="";
	String createDate="";
	String specText="";
	String imageUrl="";
	double price=0.0;
	int count=0;
	String plantType="";
	String plantTypeName="";
	String qualityType="";
	String qualityTypeName="";
	boolean isPublish=false;
	int diameter=0;
	int dbh=0;
	int crown=0;
	int offbarHeight=0;
	int height=0;
	int length=0;
	String diameterType="";
	String dbhType="";
	ArrayList<Pic> seedlingTypeJson=new ArrayList<Pic>();
	String priceDate="";
	String cityCode="";
	String cityName="";
	
	ArrayList<ParamsList> paramsLists=new ArrayList<ParamsList>();
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
	public void setStr_plantTypeList_ids_s(ArrayList<String> str_plantTypeList_ids_s) {
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
	public void setStr_qualityTypeList_ids(ArrayList<String> str_qualityTypeList_ids) {
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getSpecText() {
		return specText;
	}
	public void setSpecText(String specText) {
		this.specText = specText;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getPlantType() {
		return plantType;
	}
	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}
	public String getPlantTypeName() {
		return plantTypeName;
	}
	public void setPlantTypeName(String plantTypeName) {
		this.plantTypeName = plantTypeName;
	}
	public String getQualityType() {
		return qualityType;
	}
	public void setQualityType(String qualityType) {
		this.qualityType = qualityType;
	}
	public String getQualityTypeName() {
		return qualityTypeName;
	}
	public void setQualityTypeName(String qualityTypeName) {
		this.qualityTypeName = qualityTypeName;
	}
	public boolean isPublish() {
		return isPublish;
	}
	public void setPublish(boolean isPublish) {
		this.isPublish = isPublish;
	}
	public int getDiameter() {
		return diameter;
	}
	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}
	public int getDbh() {
		return dbh;
	}
	public void setDbh(int dbh) {
		this.dbh = dbh;
	}
	public int getCrown() {
		return crown;
	}
	public void setCrown(int crown) {
		this.crown = crown;
	}
	public int getOffbarHeight() {
		return offbarHeight;
	}
	public void setOffbarHeight(int offbarHeight) {
		this.offbarHeight = offbarHeight;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getDiameterType() {
		return diameterType;
	}
	public void setDiameterType(String diameterType) {
		this.diameterType = diameterType;
	}
	public String getDbhType() {
		return dbhType;
	}
	public void setDbhType(String dbhType) {
		this.dbhType = dbhType;
	}
	public ArrayList<Pic> getSeedlingTypeJson() {
		return seedlingTypeJson;
	}
	public void setSeedlingTypeJson(ArrayList<Pic> seedlingTypeJson) {
		this.seedlingTypeJson = seedlingTypeJson;
	}
	public String getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(String priceDate) {
		this.priceDate = priceDate;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
}
