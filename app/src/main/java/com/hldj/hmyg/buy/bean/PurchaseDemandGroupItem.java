package com.hldj.hmyg.buy.bean;

public class PurchaseDemandGroupItem {
	private String id;
	private String createBy;
	private String createDate;
	private String demandId;
	private String name;
	private int minDiameter;
	private int maxDiameter;
	private int minMijing;
	private int maxMijing;
	private int minDbh;
	private int maxDbh;
	private int minHeight;
	private int maxHeight;
	private int minCrown;
	private int maxCrown;
	private int minLength;
	private int maxLength;
	private int minOffbarHeight;
	private int maxOffbarHeight;
	private int count;
	private String unit;
	private String quoteUnit;
	public String getQuoteUnit() {
		return quoteUnit;
	}
	public void setQuoteUnit(String quoteUnit) {
		this.quoteUnit = quoteUnit;
	}
	private int quoteCount;
	private double quotePrice;
	private double quoteTotalPrice;
	public double getQuotePrice() {
		return quotePrice;
	}
	public void setQuotePrice(double quotePrice) {
		this.quotePrice = quotePrice;
	}
	public double getQuoteTotalPrice() {
		return quoteTotalPrice;
	}
	public void setQuoteTotalPrice(double quoteTotalPrice) {
		this.quoteTotalPrice = quoteTotalPrice;
	}
	private String diameter;
	private String mijing;
	private String dbh;
	private String height;
	private String crown;
	private String length;
	private String offbarHeight;
	private String specText;
	private String remarks;
	private boolean usedQuote;
	public boolean isUsedQuote() {
		return usedQuote;
	}
	public void setUsedQuote(boolean usedQuote) {
		this.usedQuote = usedQuote;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "PurchaseDemandGroupItem [id=" + id + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", demandId=" + demandId
				+ ", name=" + name + ", minDiameter=" + minDiameter
				+ ", maxDiameter=" + maxDiameter + ", minMijing=" + minMijing
				+ ", maxMijing=" + maxMijing + ", minDbh=" + minDbh
				+ ", maxDbh=" + maxDbh + ", minHeight=" + minHeight
				+ ", maxHeight=" + maxHeight + ", minCrown=" + minCrown
				+ ", maxCrown=" + maxCrown + ", minLength=" + minLength
				+ ", maxLength=" + maxLength + ", minOffbarHeight="
				+ minOffbarHeight + ", maxOffbarHeight=" + maxOffbarHeight
				+ ", count=" + count + ", unit=" + unit + ", quoteCount="
				+ quoteCount + ", diameter=" + diameter + ", mijing=" + mijing
				+ ", dbh=" + dbh + ", height=" + height + ", crown=" + crown
				+ ", length=" + length + ", offbarHeight=" + offbarHeight
				+ ", specText=" + specText + "]";
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
	public String getDemandId() {
		return demandId;
	}
	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMinDiameter() {
		return minDiameter;
	}
	public void setMinDiameter(int minDiameter) {
		this.minDiameter = minDiameter;
	}
	public int getMaxDiameter() {
		return maxDiameter;
	}
	public void setMaxDiameter(int maxDiameter) {
		this.maxDiameter = maxDiameter;
	}
	public int getMinMijing() {
		return minMijing;
	}
	public void setMinMijing(int minMijing) {
		this.minMijing = minMijing;
	}
	public int getMaxMijing() {
		return maxMijing;
	}
	public void setMaxMijing(int maxMijing) {
		this.maxMijing = maxMijing;
	}
	public int getMinDbh() {
		return minDbh;
	}
	public void setMinDbh(int minDbh) {
		this.minDbh = minDbh;
	}
	public int getMaxDbh() {
		return maxDbh;
	}
	public void setMaxDbh(int maxDbh) {
		this.maxDbh = maxDbh;
	}
	public int getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}
	public int getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}
	public int getMinCrown() {
		return minCrown;
	}
	public void setMinCrown(int minCrown) {
		this.minCrown = minCrown;
	}
	public int getMaxCrown() {
		return maxCrown;
	}
	public void setMaxCrown(int maxCrown) {
		this.maxCrown = maxCrown;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public int getMinOffbarHeight() {
		return minOffbarHeight;
	}
	public void setMinOffbarHeight(int minOffbarHeight) {
		this.minOffbarHeight = minOffbarHeight;
	}
	public int getMaxOffbarHeight() {
		return maxOffbarHeight;
	}
	public void setMaxOffbarHeight(int maxOffbarHeight) {
		this.maxOffbarHeight = maxOffbarHeight;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getQuoteCount() {
		return quoteCount;
	}
	public void setQuoteCount(int quoteCount) {
		this.quoteCount = quoteCount;
	}
	public String getDiameter() {
		return diameter;
	}
	public void setDiameter(String diameter) {
		this.diameter = diameter;
	}
	public String getMijing() {
		return mijing;
	}
	public void setMijing(String mijing) {
		this.mijing = mijing;
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
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getOffbarHeight() {
		return offbarHeight;
	}
	public void setOffbarHeight(String offbarHeight) {
		this.offbarHeight = offbarHeight;
	}
	public String getSpecText() {
		return specText;
	}
	public void setSpecText(String specText) {
		this.specText = specText;
	}
}
