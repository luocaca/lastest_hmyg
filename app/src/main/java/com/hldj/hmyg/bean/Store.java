package com.hldj.hmyg.bean;

import java.io.Serializable;
import java.util.List;

public class Store implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String cityName;
	String cityCode;
	Double totalPrice;
	List<Product> products;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
