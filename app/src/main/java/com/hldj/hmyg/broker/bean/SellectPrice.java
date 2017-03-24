package com.hldj.hmyg.broker.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.hldj.hmyg.saler.bean.Subscribe;

public class SellectPrice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String cityCode="";
	String cityName="";
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	String secondSeedlingTypeId="";
	String firstSeedlingTypeId="";
	String name="";
	String diameter="";
	String diameterType="";
	String dbh="";
	String dbhType="";
	String crown="";
	String offbarHeight="";
	String height="";
	String length="";
	String plantType="";
	String qualityType="";
	String qualityGrade="";
	String plantTypeName="";
	String qualityTypeName="";
	String qualityGradeName="";
	public String getPlantTypeName() {
		return plantTypeName;
	}
	public void setPlantTypeName(String plantTypeName) {
		this.plantTypeName = plantTypeName;
	}
	public String getQualityTypeName() {
		return qualityTypeName;
	}
	public void setQualityTypeName(String qualityTypeName) {
		this.qualityTypeName = qualityTypeName;
	}
	public String getQualityGradeName() {
		return qualityGradeName;
	}
	public void setQualityGradeName(String qualityGradeName) {
		this.qualityGradeName = qualityGradeName;
	}
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
	public String getQualityGrade() {
		return qualityGrade;
	}
	public void setQualityGrade(String qualityGrade) {
		this.qualityGrade = qualityGrade;
	}
	Subscribe subscribe = new Subscribe();
	public Subscribe getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(Subscribe subscribe) {
		this.subscribe = subscribe;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getSecondSeedlingTypeId() {
		return secondSeedlingTypeId;
	}
	public void setSecondSeedlingTypeId(String secondSeedlingTypeId) {
		this.secondSeedlingTypeId = secondSeedlingTypeId;
	}
	public String getFirstSeedlingTypeId() {
		return firstSeedlingTypeId;
	}
	public void setFirstSeedlingTypeId(String firstSeedlingTypeId) {
		this.firstSeedlingTypeId = firstSeedlingTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDiameter() {
		return diameter;
	}
	public void setDiameter(String diameter) {
		this.diameter = diameter;
	}
	public String getDiameterType() {
		return diameterType;
	}
	public void setDiameterType(String diameterType) {
		this.diameterType = diameterType;
	}
	public String getDbh() {
		return dbh;
	}
	public void setDbh(String dbh) {
		this.dbh = dbh;
	}
	public String getDbhType() {
		return dbhType;
	}
	public void setDbhType(String dbhType) {
		this.dbhType = dbhType;
	}
	public String getCrown() {
		return crown;
	}
	public void setCrown(String crown) {
		this.crown = crown;
	}
	public String getOffbarHeight() {
		return offbarHeight;
	}
	public void setOffbarHeight(String offbarHeight) {
		this.offbarHeight = offbarHeight;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPlantType() {
		return plantType;
	}
	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}
	public String getQualityType() {
		return qualityType;
	}
	public void setQualityType(String qualityType) {
		this.qualityType = qualityType;
	}
	
}
