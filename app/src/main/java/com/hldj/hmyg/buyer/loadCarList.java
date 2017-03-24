package com.hldj.hmyg.buyer;

import java.util.ArrayList;

public class loadCarList {

	String id;
	String remarks;
	String createBy;
	String createDate;
	String carNum;
	String driverName;
	String driverPhone;
	String deliveryId;
	String buyerId;
	String status;
	String statusName;
	boolean isShowItemListJson;
	boolean isShowCheck;
	public boolean isShowCheck() {
		return isShowCheck;
	}


	public void setShowCheck(boolean isShowCheck) {
		this.isShowCheck = isShowCheck;
	}
	boolean isCheck;
	public boolean isCheck() {
		return isCheck;
	}


	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	ArrayList<itemListJson> itemListJsons;
	
	@Override
	public String toString() {
		return "loadCarList [id=" + id + ", remarks=" + remarks + ", createBy="
				+ createBy + ", createDate=" + createDate + ", carNum="
				+ carNum + ", driverName=" + driverName + ", driverPhone="
				+ driverPhone + ", deliveryId=" + deliveryId + ", buyerId="
				+ buyerId + ", status=" + status + ", statusName=" + statusName
				+ ", isShowItemListJson=" + isShowItemListJson + ", isCheck="
				+ isCheck + ", itemListJsons=" + itemListJsons + "]";
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
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public String getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public boolean isShowItemListJson() {
		return isShowItemListJson;
	}
	public void setShowItemListJson(boolean isShowItemListJson) {
		this.isShowItemListJson = isShowItemListJson;
	}
	public ArrayList<itemListJson> getItemListJsons() {
		return itemListJsons;
	}
	public void setItemListJsons(ArrayList<itemListJson> itemListJsons) {
		this.itemListJsons = itemListJsons;
	}

}
