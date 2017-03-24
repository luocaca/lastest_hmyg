package com.hldj.hmyg.bean;

import java.io.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 6287420905418813028L;
	private String show_type;
	private String id;
	private String name;
	private String imageUrl;
	private String cityName;
	private double price;
	private int count;
	private String unitTypeName;
	private double diameter;
	private double height;
	private double crown;
	private String fullName;
	private String ciCity_name;
	private String realName;
	private String publicName;
	private String status;
	private String statusName;
	private String closeDate;
	@Override
	public String toString() {
		return "Product [show_type=" + show_type + ", id=" + id + ", name="
				+ name + ", imageUrl=" + imageUrl + ", cityName=" + cityName
				+ ", price=" + price + ", count=" + count + ", unitTypeName="
				+ unitTypeName + ", diameter=" + diameter + ", height="
				+ height + ", crown=" + crown + ", fullName=" + fullName
				+ ", ciCity_name=" + ciCity_name + ", realName=" + realName
				+ ", publicName=" + publicName + ", status=" + status
				+ ", statusName=" + statusName + ", closeDate=" + closeDate
				+ "]";
	}
	public Product(String show_type, String id, String name, String imageUrl,
			String cityName, double price, int count, String unitTypeName,
			double diameter, double height, double crown, String fullName,
			String ciCity_name, String realName, String publicName,
			String status, String statusName, String closeDate) {
		super();
		this.show_type = show_type;
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.cityName = cityName;
		this.price = price;
		this.count = count;
		this.unitTypeName = unitTypeName;
		this.diameter = diameter;
		this.height = height;
		this.crown = crown;
		this.fullName = fullName;
		this.ciCity_name = ciCity_name;
		this.realName = realName;
		this.publicName = publicName;
		this.status = status;
		this.statusName = statusName;
		this.closeDate = closeDate;
	}
	public String getShow_type() {
		return show_type;
	}
	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	public String getUnitTypeName() {
		return unitTypeName;
	}
	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}
	public double getDiameter() {
		return diameter;
	}
	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getCrown() {
		return crown;
	}
	public void setCrown(double crown) {
		this.crown = crown;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getCiCity_name() {
		return ciCity_name;
	}
	public void setCiCity_name(String ciCity_name) {
		this.ciCity_name = ciCity_name;
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
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
}
