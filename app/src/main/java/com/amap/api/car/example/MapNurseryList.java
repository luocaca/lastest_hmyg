package com.amap.api.car.example;

import java.io.Serializable;


public class MapNurseryList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9118963646819823220L;
	String id;
	String createBy;
	String createDate;
	String cityCode;
	String cityName;
	String prCode;
	String ciCode;
	String coCode;
	String twCode;
	String name;
	String companyName;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	String detailAddress;
	String contactName;
	String contactPhone;
	double nurseryArea;
	String type;
	String mainType;
	double longitude;
	double latitude;
	int seedlingCountJson;
	boolean delete;
	boolean locationOdd;
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
	public String getPrCode() {
		return prCode;
	}
	public void setPrCode(String prCode) {
		this.prCode = prCode;
	}
	public String getCiCode() {
		return ciCode;
	}
	public void setCiCode(String ciCode) {
		this.ciCode = ciCode;
	}
	public String getCoCode() {
		return coCode;
	}
	public void setCoCode(String coCode) {
		this.coCode = coCode;
	}
	public String getTwCode() {
		return twCode;
	}
	public void setTwCode(String twCode) {
		this.twCode = twCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public double getNurseryArea() {
		return nurseryArea;
	}
	public void setNurseryArea(double nurseryArea) {
		this.nurseryArea = nurseryArea;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMainType() {
		return mainType;
	}
	public void setMainType(String mainType) {
		this.mainType = mainType;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getSeedlingCountJson() {
		return seedlingCountJson;
	}
	public void setSeedlingCountJson(int seedlingCountJson) {
		this.seedlingCountJson = seedlingCountJson;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public boolean isLocationOdd() {
		return locationOdd;
	}
	public void setLocationOdd(boolean locationOdd) {
		this.locationOdd = locationOdd;
	}
	
	
}
