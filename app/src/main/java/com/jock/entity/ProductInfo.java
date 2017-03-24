package com.jock.entity;


public class ProductInfo extends BaseInfo
{
	
	private int position;// 绝对位置，只在ListView构造的购物车中，在删除时有效
	private String imageUrl;
	private String desc;
	private double price; // 单价
	private int count; // 网路获取的数据
	
	private String status; // 状态
	private String createDate; // 时间

	private String name; // 商品name
	private String id; // 商品ID
	private String img; // 商品图片资源ID
	private String plantType; // 类型
	private Double dbh;
	private Double height;
	private Double crown;
	private Double deliveryPrice; // 发货费

	private Double totalPrice; // 总价
	private Double amount; // 合计

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
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Double getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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

	public ProductInfo()
	{
		super();
	}


	public ProductInfo(String imageUrl, String desc, double price, int count,
			String status, String createDate, String name, String id,
			String img, String plantType, Double dbh, Double height,
			Double crown, Double deliveryPrice, Double totalPrice, Double amount,
			int num, String unitTypeName, String tradeType,
			boolean isShowChoose, boolean isChoosed, boolean isSelfSupport,
			boolean freeValidatePrice, boolean cashOnDelivery,
			boolean freeDeliveryPrice, boolean freeValidate) {
		super();
		this.imageUrl = imageUrl;
		this.desc = desc;
		this.price = price;
		this.count = count;
		this.status = status;
		this.createDate = createDate;
		this.name = name;
		this.id = id;
		this.img = img;
		this.plantType = plantType;
		this.dbh = dbh;
		this.height = height;
		this.crown = crown;
		this.deliveryPrice = deliveryPrice;
		this.totalPrice = totalPrice;
		this.amount = amount;
		this.num = num;
		this.unitTypeName = unitTypeName;
		this.tradeType = tradeType;
		this.isShowChoose = isShowChoose;
		this.isChoosed = isChoosed;
		this.isSelfSupport = isSelfSupport;
		this.freeValidatePrice = freeValidatePrice;
		this.cashOnDelivery = cashOnDelivery;
		this.freeDeliveryPrice = freeDeliveryPrice;
		this.freeValidate = freeValidate;
	}


	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

}
