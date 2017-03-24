package com.hldj.hmyg.broker.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.hldj.hmyg.bean.Pic;

public class Price implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String createBy;
	String createDate;
	double price;
	String status;
	String statusName;
	String marketId;
	String createUserType;
	String updateUserType;
	ArrayList<Pic> seedlingTypeJson;
	String imageUrl;
	String remarks="";
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getCreateUserType() {
		return createUserType;
	}
	public void setCreateUserType(String createUserType) {
		this.createUserType = createUserType;
	}
	public String getUpdateUserType() {
		return updateUserType;
	}
	public void setUpdateUserType(String updateUserType) {
		this.updateUserType = updateUserType;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public ArrayList<Pic> getSeedlingTypeJson() {
		return seedlingTypeJson;
	}
	public void setSeedlingTypeJson(ArrayList<Pic> seedlingTypeJson) {
		this.seedlingTypeJson = seedlingTypeJson;
	}
}
