package com.mrwujay.cascade.model;

public class DistrictModel {
	private String name;
	private String cityCode;
	private String zipcode;
	
	
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	

	public DistrictModel(String name, String cityCode, String zipcode) {
		super();
		this.name = name;
		this.cityCode = cityCode;
		this.zipcode = zipcode;
	}

	public DistrictModel() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
	}

}
