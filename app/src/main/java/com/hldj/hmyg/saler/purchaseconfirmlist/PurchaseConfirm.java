package com.hldj.hmyg.saler.purchaseconfirmlist;

import java.io.Serializable;

public class PurchaseConfirm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id = "";
	String createBy = "";
	String createDate = "";
	String name = "";
	String contactName = "";
	String contactPhone = "";
	String purchaseDate = "";
	String status = "";
	String sellerId = "";
	long confirmDate = 0;
	String statusName = "";
	double totalJson = 0;
	String spceText = "";
	public PurchaseConfirm(String id, String createBy, String createDate,
			String name, String contactName, String contactPhone,
			String purchaseDate, String status, String sellerId,
			long confirmDate, String statusName, double totalJson, String spceText) {
		super();
		this.id = id;
		this.createBy = createBy;
		this.createDate = createDate;
		this.name = name;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.purchaseDate = purchaseDate;
		this.status = status;
		this.sellerId = sellerId;
		this.confirmDate = confirmDate;
		this.statusName = statusName;
		this.totalJson = totalJson;
		this.spceText = spceText;
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
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public long getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(long confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public double getTotalJson() {
		return totalJson;
	}
	public void setTotalJson(double totalJson) {
		this.totalJson = totalJson;
	}
	public String getSpceText() {
		return spceText;
	}
	public void setSpceText(String spceText) {
		this.spceText = spceText;
	}
}
