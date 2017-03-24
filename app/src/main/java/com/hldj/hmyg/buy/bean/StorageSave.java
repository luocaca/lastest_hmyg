package com.hldj.hmyg.buy.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.listedittext.paramsData;
import com.hldj.hmyg.bean.Pic;

public class StorageSave implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id="";
	String firstSeedlingTypeId="";
	String seedlingParams="";


	public String getSeedlingParams() {
		return seedlingParams;
	}

	public void setSeedlingParams(String seedlingParams) {
		this.seedlingParams = seedlingParams;
	}

	String name="";
	String price="";
	String floorPrice="";
	String validity="";
	String nurseryId="";
	String contactPhone="";
	String contactName="";
	boolean isDefault;
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}



	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	String count="";
	String diameterType="";
	String dbhType="";
	String dbh="";
	String height="";
	String crown="";
	String maxHeight="";
	String maxCrown="";
	public String getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
	}

	public String getMaxCrown() {
		return maxCrown;
	}

	public void setMaxCrown(String maxCrown) {
		this.maxCrown = maxCrown;
	}

	String diameter="";
	String offbarHeight="";
	String length="";
	String plantType="";
	String unitType="";
	ArrayList<Pic> urlPaths=new ArrayList<Pic>(); 
	String paramsData="";
	String remarks="";
	String storage_save_id="";
	String minSpec="";
	String maxSpec="";
	
	public String getMinSpec() {
		return minSpec;
	}

	public void setMinSpec(String minSpec) {
		this.minSpec = minSpec;
	}

	public String getMaxSpec() {
		return maxSpec;
	}

	public void setMaxSpec(String maxSpec) {
		this.maxSpec = maxSpec;
	}

	private String address;
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStorage_save_id() {
		return storage_save_id;
	}

	public void setStorage_save_id(String storage_save_id) {
		this.storage_save_id = storage_save_id;
	}

	public ArrayList<Pic> getUrlPaths() {
		return urlPaths;
	}

	public void setUrlPaths(ArrayList<Pic> urlPaths) {
		this.urlPaths = urlPaths;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getFloorPrice() {
		return floorPrice;
	}

	public void setFloorPrice(String floorPrice) {
		this.floorPrice = floorPrice;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getNurseryId() {
		return nurseryId;
	}

	public void setNurseryId(String nurseryId) {
		this.nurseryId = nurseryId;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
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

	public String getDbh() {
		return dbh;
	}

	public void setDbh(String dbh) {
		this.dbh = dbh;
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

	public String getDiameter() {
		return diameter;
	}

	public void setDiameter(String diameter) {
		this.diameter = diameter;
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

	public String getPlantType() {
		return plantType;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}


	public String getParamsData() {
		return paramsData;
	}

	public void setParamsData(String paramsData) {
		this.paramsData = paramsData;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
