package com.hldj.hmyg.broker;

import java.io.Serializable;

public class ReceiptListJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5270701364707564975L;

	private String id;
	private String remarks;
	private String createBy;
	private String createDate; // 时间
	private String receiptDate;
	private String receiptAddressId;
	private String orderId;
	private int count; // 网路获取的数据
	private String buyerId;
	private boolean isDelivery;
	private String deliveryId;
	private String orderName;
	private String orderNum;
	private String sellerId;
	private boolean payConfirm;
	private int loadedCountJson;
	private int need_loadedCount;
	private boolean isShowCheckBox;
	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}

	public int getNeed_loadedCount() {
		return need_loadedCount;
	}

	public void setNeed_loadedCount(int need_loadedCount) {
		this.need_loadedCount = need_loadedCount;
	}

	private boolean isChoosed;
	private String contactName;
	private String contactPhone;
	private String fullAddress;
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

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
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

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getReceiptAddressId() {
		return receiptAddressId;
	}

	public void setReceiptAddressId(String receiptAddressId) {
		this.receiptAddressId = receiptAddressId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public boolean isDelivery() {
		return isDelivery;
	}

	public void setDelivery(boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public boolean isPayConfirm() {
		return payConfirm;
	}

	public void setPayConfirm(boolean payConfirm) {
		this.payConfirm = payConfirm;
	}

	public int getLoadedCountJson() {
		return loadedCountJson;
	}

	public void setLoadedCountJson(int loadedCountJson) {
		this.loadedCountJson = loadedCountJson;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

}
