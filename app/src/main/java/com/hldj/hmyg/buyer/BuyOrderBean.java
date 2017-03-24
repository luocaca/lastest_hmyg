package com.hldj.hmyg.buyer;

import java.io.Serializable;


public class BuyOrderBean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5270701364707564975L;
	private String status; // 状态
	private String createDate; // 时间

	private String name; // 商品name
	private String id; // 商品ID
	private String img; // 商品图片资源ID
	private String plantType; // 类型
	private Double dbh;
	private Double height;
	private Double crown;
	private double deliveryPrice; // 发货费
	private double price; // 单价
	private double totalPrice; // 总价
	private double amount; // 合计
	private int count; // 网路获取的数据
	private int num;
	private String unitTypeName; // 单位
	private String tradeType; // 如委托发货
	private boolean isShowChoose; // 是否显示
	private boolean isChoosed; // 商品是否在购物车中被选中
	private boolean isSelfSupport; //
	private boolean freeValidatePrice; //
	private boolean cashOnDelivery; //
	private boolean freeDeliveryPrice; //
	private boolean freeValidate; //
	private String tagList;
	private String specText;
	
	
	
	
	public String getSpecText() {
		return specText;
	}

	public void setSpecText(String specText) {
		this.specText = specText;
	}

	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList = tagList;
	}

	@Override
	public String toString() {
		return "BuyOrderBean [status=" + status + ", createDate=" + createDate
				+ ", name=" + name + ", id=" + id + ", img=" + img
				+ ", plantType=" + plantType + ", dbh=" + dbh + ", height="
				+ height + ", crown=" + crown + ", deliveryPrice="
				+ deliveryPrice + ", price=" + price + ", totalPrice="
				+ totalPrice + ", amount=" + amount + ", count=" + count
				+ ", num=" + num + ", unitTypeName=" + unitTypeName
				+ ", tradeType=" + tradeType + ", isShowChoose=" + isShowChoose
				+ ", isChoosed=" + isChoosed + ", isSelfSupport="
				+ isSelfSupport + ", freeValidatePrice=" + freeValidatePrice
				+ ", cashOnDelivery=" + cashOnDelivery + ", freeDeliveryPrice="
				+ freeDeliveryPrice + ", freeValidate=" + freeValidate + "]";
	}

	public boolean isSelfSupport() {
		return isSelfSupport;
	}

	public void setSelfSupport(boolean isSelfSupport) {
		this.isSelfSupport = isSelfSupport;
	}

	public boolean isFreeValidatePrice() {
		return freeValidatePrice;
	}

	public void setFreeValidatePrice(boolean freeValidatePrice) {
		this.freeValidatePrice = freeValidatePrice;
	}

	public boolean isCashOnDelivery() {
		return cashOnDelivery;
	}

	public void setCashOnDelivery(boolean cashOnDelivery) {
		this.cashOnDelivery = cashOnDelivery;
	}

	public boolean isFreeDeliveryPrice() {
		return freeDeliveryPrice;
	}

	public void setFreeDeliveryPrice(boolean freeDeliveryPrice) {
		this.freeDeliveryPrice = freeDeliveryPrice;
	}

	public boolean isFreeValidate() {
		return freeValidate;
	}

	public void setFreeValidate(boolean freeValidate) {
		this.freeValidate = freeValidate;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPlantType() {
		return plantType;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public Double getDbh() {
		return dbh;
	}

	public void setDbh(Double dbh) {
		this.dbh = dbh;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getCrown() {
		return crown;
	}

	public void setCrown(Double crown) {
		this.crown = crown;
	}

	public double getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public boolean isShowChoose() {
		return isShowChoose;
	}

	public void setShowChoose(boolean isShowChoose) {
		this.isShowChoose = isShowChoose;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}


}
