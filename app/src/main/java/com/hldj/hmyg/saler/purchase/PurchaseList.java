package com.hldj.hmyg.saler.purchase;

import java.io.Serializable;
import java.util.ArrayList;

public class PurchaseList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id = "";
	String createBy = "";
	String createDate = "";
	String cityCode = "";
	String cityName = "";
	String prCode = "";
	String ciCode = "";
	String coCode = "";
	String twCode = "";
	String num = "";
	String projectName = "";
	String receiptDate = "";
	String validity = "";
	String publishDate = "";
	String closeDate = "";
	boolean needInvoice = false;
	String buyerId = "";
	String buyer = "";
	String purchaseFormId = "";
	
	String customerId = "";
	String status = "";
	String source = "";
	String statusName = "";
	int quoteCountJson = 0;
	int lastDays = 0;
	ArrayList<String> itemNameList = new ArrayList<String>();
	
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getPurchaseFormId() {
		return purchaseFormId;
	}
	public void setPurchaseFormId(String purchaseFormId) {
		this.purchaseFormId = purchaseFormId;
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
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public boolean isNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(boolean needInvoice) {
		this.needInvoice = needInvoice;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public int getQuoteCountJson() {
		return quoteCountJson;
	}
	public void setQuoteCountJson(int quoteCountJson) {
		this.quoteCountJson = quoteCountJson;
	}
	public int getLastDays() {
		return lastDays;
	}
	public void setLastDays(int lastDays) {
		this.lastDays = lastDays;
	}
	public ArrayList<String> getItemNameList() {
		return itemNameList;
	}
	public void setItemNameList(ArrayList<String> itemNameList) {
		this.itemNameList = itemNameList;
	}
}
