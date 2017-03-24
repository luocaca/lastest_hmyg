package com.hldj.hmyg.buy.bean;

import java.util.ArrayList;

import com.hldj.hmyg.bean.Pic;

public class PurchaseDemandChildItem {
	private String id;
	private String remarks;
	private String createBy;
	private String createDate;
	private String cityCode;
	private String cityName;
	ArrayList<Pic> imagesJson=new ArrayList<Pic>();

	public ArrayList<Pic> getImagesJson() {
		return imagesJson;
	}

	public void setImagesJson(ArrayList<Pic> imagesJson) {
		this.imagesJson = imagesJson;
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

	private String prCode;
	private String ciCode;
	private String coCode;
	private String twCode;
	private String demandId;
	private String demandItemId;
	private double price;
	private double floorPrice;
	public double getFloorPrice() {
		return floorPrice;
	}

	public void setFloorPrice(double floorPrice) {
		this.floorPrice = floorPrice;
	}

	private String plantType;
	private String unitType;
	private String unitTypeName;
	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}

	private boolean clerkConfirm;
	private boolean isNew;
	private boolean isUse;
	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}

	private String plantTypeName;
	private String specText;
	public String getSpecText() {
		return specText;
	}

	public void setSpecText(String specText) {
		this.specText = specText;
	}

	@Override
	public String toString() {
		return "PurchaseDemandChildItem [id=" + id + ", remarks=" + remarks
				+ ", createBy=" + createBy + ", createDate=" + createDate
				+ ", prCode=" + prCode + ", ciCode=" + ciCode + ", coCode="
				+ coCode + ", twCode=" + twCode + ", demandId=" + demandId
				+ ", demandItemId=" + demandItemId + ", price=" + price
				+ ", plantType=" + plantType + ", clerkConfirm=" + clerkConfirm
				+ ", isNew=" + isNew + ", plantTypeName=" + plantTypeName + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public String getDemandItemId() {
		return demandItemId;
	}

	public void setDemandItemId(String demandItemId) {
		this.demandItemId = demandItemId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPlantType() {
		return plantType;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public boolean isClerkConfirm() {
		return clerkConfirm;
	}

	public void setClerkConfirm(boolean clerkConfirm) {
		this.clerkConfirm = clerkConfirm;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getPlantTypeName() {
		return plantTypeName;
	}

	public void setPlantTypeName(String plantTypeName) {
		this.plantTypeName = plantTypeName;
	}
}
