package com.hldj.hmyg.buyer;

public class Quote {

	String id;
	String remarks;
	String cityCode;
	String cityName;
	String purchaseId;
	String purchaseItemId;
	double price;
	int count;
	String status;
	boolean isInvoice;

	public boolean isInvoice() {
		return isInvoice;
	}

	public void setInvoice(boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	boolean isUsed;
	boolean isAlternative;
	String realName;
	String publicName;
	String companyName;
	String publicPhone;
	String phone;
	String showName;
	private boolean isChoosed; // 商品是否在购物车中被选中
	private boolean isShowChoose; // 是否显示

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

	public boolean isShowChoose() {
		return isShowChoose;
	}

	public void setShowChoose(boolean isShowChoose) {
		this.isShowChoose = isShowChoose;
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

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPurchaseItemId() {
		return purchaseItemId;
	}

	public void setPurchaseItemId(String purchaseItemId) {
		this.purchaseItemId = purchaseItemId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isAlternative() {
		return isAlternative;
	}

	public void setAlternative(boolean isAlternative) {
		this.isAlternative = isAlternative;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPublicName() {
		return publicName;
	}

	public void setPublicName(String publicName) {
		this.publicName = publicName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPublicPhone() {
		return publicPhone;
	}

	public void setPublicPhone(String publicPhone) {
		this.publicPhone = publicPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
}
