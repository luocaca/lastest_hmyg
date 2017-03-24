package com.mrwujay.cascade.model;

import java.util.List;

public class ProvinceModel {
	private String name;
	private String cityCode;
	private List<CityModel> cityList;
	
	public ProvinceModel(String name, String cityCode, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityCode = cityCode;
		this.cityList = cityList;
	}
	
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}



	public ProvinceModel() {
		super();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
