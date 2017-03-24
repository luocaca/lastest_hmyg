package com.hldj.hmyg.bean;

import java.io.Serializable;

public class Purchase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7599227856844072223L;
	String name;
	String firstSeedlingType;
	public String getFirstSeedlingType() {
		return firstSeedlingType;
	}
	public void setFirstSeedlingType(String firstSeedlingType) {
		this.firstSeedlingType = firstSeedlingType;
	}
	String firstSeedlingTypeId;
	String count;
	String unitType;
	String plantType;
	String diameter;
	String diameterType;
	String dbh;
	String dbhType;
	String height;
	String crown;
	String offbarHeight;
	String length;
	String remarks;
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
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getPlantType() {
		return plantType;
	}
	public void setPlantType(String plantType) {
		this.plantType = plantType;
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
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
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
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Purchase(String name,String firstSeedlingType ,String firstSeedlingTypeId, String count,
			String unitType, String plantType, String diameter,
			String diameterType, String dbh, String dbhType, String height,
			String crown, String offbarHeight, String length, String remarks) {
		super();
		this.name = name;
		this.firstSeedlingType = firstSeedlingType;
		this.firstSeedlingTypeId = firstSeedlingTypeId;
		this.count = count;
		this.unitType = unitType;
		this.plantType = plantType;
		this.diameter = diameter;
		this.diameterType = diameterType;
		this.dbh = dbh;
		this.dbhType = dbhType;
		this.height = height;
		this.crown = crown;
		this.offbarHeight = offbarHeight;
		this.length = length;
		this.remarks = remarks;
	}
}
